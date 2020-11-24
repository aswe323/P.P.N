package com.example.rems;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import module.WordPriority;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link edit_words_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class edit_words_fragment extends Fragment implements View.OnClickListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private DataBaseHelper db;
    private static SeekBar thisSeekBar;
    private TextView thisSeekBarTextView;
    private static Button addWordButton;
    private Button cancelWordButton;
    private static EditText wordText;
    private static boolean isEditFlag=false; //if i opened a reminder from my "next reminders" list in the home button flag will be true and it's means we need to call Update query and not inset
    private static String oldWord;
    private static int oldScore;

    public edit_words_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment edit_words_fragment.
     */
    public static edit_words_fragment newInstance() {
        edit_words_fragment fragment = new edit_words_fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public void onClick(View view)//TODO: make a utility method for switching fragments on the main_activity_fragment(see note).
    //note: will probably need to make a static variable to track the current fragment displayed to be replaced
    {
        switch (view.getId()) {
            case R.id.buttonAddWord:
                if(!isEditFlag){
                    if (!wordText.getText().toString().equals("") && !wordText.getText().toString().equals(" ") && wordText.getText().toString().matches("[a-zA-Z0-9]+")) {
                        if (WordPriority.addWord(wordText.getText().toString(), thisSeekBar.getProgress())) {
                            if (db.insertPriorityWord(wordText.getText().toString(), thisSeekBar.getProgress()))
                                Toast.makeText(getActivity(), "the word " + wordText.getText().toString() + " with priority " + thisSeekBar.getProgress() + " added successfully", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getActivity(), "Error accrued, make sure to write a word and it not existing already", Toast.LENGTH_SHORT).show();
                    } else
                        //TODO: add a toast to indicate that the entered word is illegal
                        Toast.makeText(getActivity(), "enter a word please", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(WordPriority.editWord(oldWord,wordText.getText().toString(),oldScore,thisSeekBar.getProgress())) {
                        Toast.makeText(getActivity(), "updated the word " + oldWord + " to " + wordText.getText().toString() + " with priority " + thisSeekBar.getProgress(), Toast.LENGTH_SHORT).show();
                    }
                }
            //break;

            case  R.id.buttonCancelWord:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                key_words_fragment maf = new key_words_fragment();
                ft.replace(R.id.fragment_key_words_fragment, maf).commit();
            break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_words_fragment, container, false);// the hell is this even?
        isEditFlag=false;
        db = DataBaseHelper.getInstance(null);
        //accessing seekBar and the Text of Seekbar by ID and setting progress to 0
        thisSeekBar = view.findViewById(R.id.seekBar);
        thisSeekBarTextView = view.findViewById(R.id.textViewPriorityChoosen);
        thisSeekBar.setProgress(0);

        //attaching Listender to Seekbar
        thisSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                thisSeekBarTextView.setText("priority: " + String.valueOf(progress) + "/10");//every time something changed, update the text of seekbar
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        addWordButton = view.findViewById(R.id.buttonAddWord);
        wordText=view.findViewById(R.id.editTextForWord);
        addWordButton.setOnClickListener(this);

        cancelWordButton=view.findViewById(R.id.buttonCancelWord);
        cancelWordButton.setOnClickListener(this);

        return view;
    }

    public static void editingword(String word,int priority){//this function is called before the fragment is presented,it's inserting the data of the needed WordPriority to the elements TODO:add to the book
        isEditFlag=true; //turn edit flag to true so we update instead of insert to Database
        oldWord=word;
        oldScore=priority;

        addWordButton.setText("save");
        thisSeekBar.setProgress(priority);
        wordText.setText(word);
    }

}