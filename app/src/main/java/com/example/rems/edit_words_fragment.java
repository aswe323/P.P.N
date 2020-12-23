package com.example.rems;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

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
    private Calendar calendar = Calendar.getInstance();
    private TextView thisSeekBarTextView;
    private static Button addWordButton;
    private Button cancelWordButton;
    private static EditText wordText;
    private static boolean isEditFlag = false; //if i opened a reminder from my "next reminders" list in the home button flag will be true and it's means we need to call Update query and not inset
    private static boolean isBucketFlag = false; //used to distinguish if it's a priority word or bucket word
    private static String oldWord;
    private static int oldScore;
    private static TextView SetTimeTo;
    private static TextView SetTimeFrom;
    private static Switch switchTimeBucketWords;
    private static String totalRange="";

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View view)//TODO: make a utility method for switching fragments on the main_activity_fragment(see note).
    //note: will probably need to make a static variable to track the current fragment displayed to be replaced
    {
        switch (view.getId()) {
            case R.id.buttonAddWord:
                //if the word is not edited:
                if(!isEditFlag){
                    //if the word is no a bucket word:
                    if(!isBucketFlag){
                        if(!wordText.getText().toString().equals("") && !wordText.getText().toString().equals(" ") && wordText.getText().toString().matches("[a-zA-Z0-9]+")) {
                            if (WordPriority.addWord(wordText.getText().toString(), thisSeekBar.getProgress())) {
                                if (db.insertPriorityWord(wordText.getText().toString(), thisSeekBar.getProgress()))
                                    Toast.makeText(getActivity(), "the word " + wordText.getText().toString() + " with priority " + thisSeekBar.getProgress() + " added successfully", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(getActivity(), "Error accrued, make sure to write a word and it not existing already", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(getActivity(), "make sure you entered a word or the word is legal word (no chars like 3,*,$)", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                    else{//if the word is bucket word:
                        if(SetTimeTo.getText().toString().matches("[0-9:]+")&&SetTimeFrom.getText().toString().matches("[0-9:]+")){//if the time was set then enter
                            if(!wordText.getText().toString().equals("") && !wordText.getText().toString().equals(" ") && wordText.getText().toString().matches("[a-zA-Z0-9]+")){
                                totalRange+=""+SetTimeFrom.getText().toString()+"-"+SetTimeTo.getText().toString();
                                if(WordPriority.addBucketWord(wordText.getText().toString(),totalRange))
                                    Toast.makeText(getActivity(), "bucket word was added", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getActivity(), "make sure that the bucket word doesn't exist already", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getActivity(), "make sure you entered a word or the word is legal word (no chars like 3,*,$)", Toast.LENGTH_LONG).show();
                                break;
                            }
                        }else{
                            Toast.makeText(getActivity(), "set the time range (starts-ends)", Toast.LENGTH_LONG).show();
                            break;
                        }
                    }

                } else{//if the word is edited:
                    //if the word is no a bucket word:
                    if(!isBucketFlag) {
                        if (WordPriority.editWord(oldWord, wordText.getText().toString(), oldScore, thisSeekBar.getProgress())) {
                            Toast.makeText(getActivity(), "updated the word " + oldWord + " to " + wordText.getText().toString() + " with priority " + thisSeekBar.getProgress(), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if (WordPriority.updateBucketWordWord(oldWord, wordText.getText().toString(), totalRange)) {
                            Toast.makeText(getActivity(), "updated the word " + oldWord + " to " + wordText.getText().toString() + " with priority " + thisSeekBar.getProgress(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            case  R.id.buttonCancelWord:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                key_words_fragment maf = new key_words_fragment();
                ft.replace(R.id.fragment_key_words_fragment, maf).commit();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_words_fragment, container, false);// the hell is this even?
        isEditFlag = false;
        db = DataBaseHelper.getInstance(null);
        //accessing seekBar and the Text of Seekbar by ID and setting progress to 0

        switchTimeBucketWords = view.findViewById(R.id.switchTimeBucketWords);
        switchTimeBucketWords.setOnClickListener(v -> {
            if (switchTimeBucketWords.isChecked()) {
                isBucketFlag = true;
                thisSeekBar.setVisibility(View.GONE);
                thisSeekBarTextView.setVisibility(View.GONE);
                thisSeekBar.setEnabled(false);
                thisSeekBar.setEnabled(false);

                SetTimeFrom.setVisibility(View.VISIBLE);
                SetTimeTo.setVisibility(View.VISIBLE);
                SetTimeTo.setEnabled(true);
                SetTimeFrom.setEnabled(true);
            } else {
                isBucketFlag = false;
                thisSeekBar.setVisibility(View.VISIBLE);
                thisSeekBarTextView.setVisibility(View.VISIBLE);
                thisSeekBar.setEnabled(true);
                thisSeekBar.setEnabled(true);

                SetTimeFrom.setVisibility(View.GONE);
                SetTimeTo.setVisibility(View.GONE);
                SetTimeTo.setEnabled(false);
                SetTimeFrom.setEnabled(false);

            }
        });
        SetTimeFrom = view.findViewById(R.id.SetTimeFrom);
        SetTimeTo = view.findViewById(R.id.SetTimeTo);
        SetTimeTo.setOnClickListener(v -> {
            final int hour = calendar.get(calendar.HOUR_OF_DAY);
            final int minute = calendar.get(calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String timeChecker = "";
                    if (hourOfDay < 10)
                        timeChecker += "0" + hourOfDay + ":";
                        //settimetext.setText("0"+hourOfDay+":"+minute);
                    else
                        timeChecker += hourOfDay + ":";
                    //settimetext.setText(hourOfDay+":"+minute);
                    if (minute < 10)
                        timeChecker += "0" + minute;
                    else
                        timeChecker += minute;

                    SetTimeTo.setText(timeChecker);
                }
            }, hour, minute, android.text.format.DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();
        });

        SetTimeFrom.setOnClickListener(v -> {
            final int hour = calendar.get(calendar.HOUR_OF_DAY);
            final int minute = calendar.get(calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String timeChecker = "";
                    if (hourOfDay < 10)
                        timeChecker += "0" + hourOfDay + ":";
                        //settimetext.setText("0"+hourOfDay+":"+minute);
                    else
                        timeChecker += hourOfDay + ":";
                    //settimetext.setText(hourOfDay+":"+minute);
                    if (minute < 10)
                        timeChecker += "0" + minute;
                    else
                        timeChecker += minute;

                    SetTimeFrom.setText(timeChecker);
                }
            }, hour, minute, android.text.format.DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();
        });
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

    public static void editingword(String word,int priority){//this function is called before the fragment is presented,it's inserting the data of the needed WordPriority to the elements
        isEditFlag=true; //turn edit flag to true so we update instead of insert to Database
        oldWord=word;
        oldScore=priority;

        addWordButton.setText("save");
        thisSeekBar.setProgress(priority);
        wordText.setText(word);
    }
    public static void editingBucketWord(String word,String Range){//this function is called before the fragment is presented,it's inserting the data of the needed WordPriority to the elements
        String[] FromTo;
        isEditFlag=true; //turn edit flag to true so we update instead of insert to Database
        oldWord=word;
        totalRange=Range;
        FromTo=Range.split("-");
        SetTimeFrom.setText(FromTo[0]);
        SetTimeTo.setText(FromTo[1]);

        addWordButton.setText("save");
        wordText.setText(word);
    }

}