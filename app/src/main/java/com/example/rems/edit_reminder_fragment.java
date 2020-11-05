package com.example.rems;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import module.ActivityTask;
import module.ActivityTasksUsed;
import module.MasloCategorys;
import module.Repetition;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link edit_reminder_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class edit_reminder_fragment extends Fragment implements View.OnClickListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Context context;
    TextView settimetext;
    TextView setdatetext;
    Button buttonIdentifier;
    Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateSetListener;

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
                Switch automaticAssignment = view.findViewById(R.id.switchForAi);
                Boolean stupid = automaticAssignment.isChecked();
                Spinner masloCategory = view.findViewById(R.id.spinnerForCategory);
                Spinner repetition = view.findViewById(R.id.spinnerForRepeat);
                EditText discription = view.findViewById(R.id.editTextForReminder);

                if (stupid) {
                    //if the automantic assignment option is checked.
                    ActivityTasksUsed.addActivityTask(new ActivityTask(0,
                            MasloCategorys.valueOf(masloCategory.getSelectedItem().toString()),
                            Repetition.valueOf(repetition.getSelectedItem().toString()),
                            discription.getText().toString(),
                            null));
                    Toast.makeText(getActivity(), "auto assigned", Toast.LENGTH_SHORT).show();//notifying the event was called
                } else {
                    //if the user choose to select time and date by himself.
                    EditText selectedTime = view.findViewById(R.id.SetTimeTextView);
                    EditText selectedDate = view.findViewById(R.id.SetDateTextView);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    ActivityTasksUsed.addActivityTask(new ActivityTask(
                            0,
                            0,
                            MasloCategorys.valueOf(masloCategory.getSelectedItem().toString()),//MasloCategory
                            Repetition.valueOf(repetition.getSelectedItem().toString()),//Repetition
                            discription.getText().toString(),
                            (LocalDateTime) formatter.parse("" + selectedDate.getText().toString() + " " + selectedTime.getText().toString()),
                            null
                            //yyyy-MM-dd HH:mm:ss
                    ));
                    Toast.makeText(getActivity(), "manualy assigned", Toast.LENGTH_SHORT).show();//notifying the event was called
                }
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
        settimetext = view.findViewById(R.id.SetTimeTextView);
        settimetext.setOnClickListener(this);
        buttonIdentifier = view.findViewById(R.id.ButtonSaveReminder);
        buttonIdentifier.setOnClickListener(this);

        setdatetext = view.findViewById(R.id.SetDateTextView);
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                setdatetext.setText(date);
            }
        };
        setdatetext.setOnClickListener(this);
        return view;
    }
}