package com.example.rems;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import module.ActivityTask;
import module.ActivityTasksUsed;
import module.MasloCategorys;
import module.Repetition;
import module.SubActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link edit_reminder_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class edit_reminder_fragment extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Switch automaticAssignment;
    private Spinner masloCategory;
    private Spinner repetition;
    private EditText discription;
    private DateTimeFormatter formatter;
    private LocalDateTime TextToDate;
    private String date="";
    private TextView settimetext;
    private TextView setdatetext;
    private Button buttonIdentifier;
    private Button buttonAddSubActivity;
    private Button cancelButton;
    private EditText inputForSubActivityDialog;
    private String subactivitytext="";
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private AlertDialog.Builder subActivityDialogBox;
    private ArrayList<SubActivity> subActivitiesArrayList;

    public edit_reminder_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment edit_reminder_fragment.
     */
    public static edit_reminder_fragment newInstance() {
        edit_reminder_fragment fragment = new edit_reminder_fragment();
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
    {
        switch (view.getId()) {//recognizing what button was pushed

            case R.id.SetTimeTextView:
                //region
                final int hour=calendar.get(calendar.HOUR_OF_DAY);
                final int minute=calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        settimetext.setText(hourOfDay+":"+minute);
                    }
                },hour,minute,android.text.format.DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();

                Toast.makeText(getActivity(), "time set", Toast.LENGTH_SHORT).show();//notifying the event was called
                break;
            //endregion
            case R.id.SetDateTextView:
                //region
                final int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
                dialog.show();


                Toast.makeText(getActivity(), "time set", Toast.LENGTH_SHORT).show();//notifying the event was called
                break;
            //endregion
            case R.id.ButtonSaveReminder:
                //region
                if (automaticAssignment.isChecked()) {
                    //if the automantic assignment option is checked.
                    ActivityTasksUsed.addActivityTask(new ActivityTask(0,
                            MasloCategorys.valueOf(masloCategory.getSelectedItem().toString()),
                            Repetition.valueOf(repetition.getSelectedItem().toString()),
                            discription.getText().toString(),
                            new ArrayList<SubActivity>()));
                    Toast.makeText(getActivity(), "auto assigned", Toast.LENGTH_SHORT).show();//notifying the event was called
                } else {
                    //if the user choose to select time and date by himself.

                    if (settimetext.getText().toString().equals("select to choose time") || setdatetext.getText().toString().equals("select to choose date")) {
                        Toast.makeText(getActivity(), "select date and time", Toast.LENGTH_SHORT).show();//notifying the event was called
                        return;
                    }

                    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    TextToDate = LocalDateTime.parse("2020-05-06 20:43:00", formatter);
                    ActivityTasksUsed.addActivityTask(new ActivityTask(
                            0,
                            0,
                            MasloCategorys.valueOf(masloCategory.getSelectedItem().toString()),//MasloCategory
                            Repetition.valueOf(repetition.getSelectedItem().toString()),//Repetition
                            discription.getText().toString(),
                            TextToDate,
                            subActivitiesArrayList
                            //yyyy-MM-dd HH:mm:ss
                    ));
                    Toast.makeText(getActivity(), "manually assigned", Toast.LENGTH_SHORT).show();//notifying the event was called
                }

                break;
            //endregion
            case R.id.ButtonAddSubActivity:
                //region
                subActivityDialogBox = new AlertDialog.Builder(getContext());
                subActivityDialogBox.setTitle("sub reminder:");
                subActivityDialogBox.setMessage("enter the sub reminder here: ");
                inputForSubActivityDialog = new EditText(getContext());
                subActivityDialogBox.setView(inputForSubActivityDialog);

                subActivityDialogBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        subactivitytext = inputForSubActivityDialog.getText().toString();
                        subActivitiesArrayList.add(new SubActivity(0,0,subactivitytext));
                        Toast.makeText(getActivity(), "sub reminder was added: "+subactivitytext, Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
                subActivityDialogBox.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                inputForSubActivityDialog.setText("");
                                return;
                            }
                        });
                subActivityDialogBox.show();
                break;
            //endregion
            case R.id.switchForAi://neccesery for prototype.
                //region
                automaticAssignment.setChecked(false);
                Toast.makeText(getActivity(), "feature not ready yet", Toast.LENGTH_SHORT).show();
                break;
            //endregion
            case R.id.ButtonCancelReminder:
                //region
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Main_Activity_fragment maf = new Main_Activity_fragment();
                ft.replace(R.id.fragment_edit_reminder, maf).commit();

                break;
            //endregion
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_edit_reminder_fragment, container, false);
        View view = inflater.inflate(R.layout.fragment_edit_reminder_fragment, container, false);

        automaticAssignment = view.findViewById(R.id.switchForAi);
        masloCategory = view.findViewById(R.id.spinnerForCategory);
        repetition = view.findViewById(R.id.spinnerForRepeat);
        discription = view.findViewById(R.id.editTextForReminder);
        subActivitiesArrayList = new ArrayList<>();
        automaticAssignment.setOnClickListener(this);

        cancelButton = view.findViewById(R.id.ButtonCancelReminder);
        cancelButton.setOnClickListener(this);

        settimetext = view.findViewById(R.id.SetTimeTextView);
        settimetext.setOnClickListener(this);

        buttonIdentifier = view.findViewById(R.id.ButtonSaveReminder);
        buttonIdentifier.setOnClickListener(this);

        buttonAddSubActivity = view.findViewById(R.id.ButtonAddSubActivity);
        buttonAddSubActivity.setOnClickListener(this);

        setdatetext = view.findViewById(R.id.SetDateTextView);
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                date = year + "-" + month + "-" + dayOfMonth;
                setdatetext.setText(date);
            }
        };
        setdatetext.setOnClickListener(this);


        return view;
    }
}