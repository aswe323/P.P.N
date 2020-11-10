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
    private SeekBar thisSeekBar;
    private TextView thisSeekBarTextView;
    private Button addWordButton;
    private Button cancelWordButton;
    private EditText wordText;


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
                if(!wordText.getText().toString().equals("")){
                    if(WordPriority.addWord(wordText.getText().toString(),thisSeekBar.getProgress())) {
                        if(db.insertPriorityWord(wordText.getText().toString(),thisSeekBar.getProgress()))
                            Toast.makeText(getActivity(), "the word " + wordText.getText().toString() +" with priority " + thisSeekBar.getProgress() + " added successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getActivity(), "Error accrued, make sure to write a word and it not existing already", Toast.LENGTH_SHORT).show();
                }
                else

                    Toast.makeText(getActivity(), "enter a word please", Toast.LENGTH_SHORT).show();
            break;

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
}