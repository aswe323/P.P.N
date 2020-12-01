package com.example.rems;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import module.ActivityTask;
import module.ActivityTasksUsed;
import module.MasloCategorys;
import module.Repetition;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {


    //region Search Elements
    private EditText editTextNumberSearch;
    private EditText editTextContentSearch;
    private TextView setTimeTextViewSearch;
    private TextView setDateTextViewSearch;
    private Spinner spinnerForRepeatSearch;
    private Spinner spinnerForCategorySearch;
    private Switch switchOnlyPastReminders;
    private FloatingActionButton buttonSearchAction;
    //endregion

    private FloatingActionButton buttonCancelSearchAction;

    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String date = "";
    private LocalDateTime TextToDate;

    private RecyclerView recyclerView;


    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment searchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        //region finding relevent UI elements
        editTextNumberSearch = view.findViewById(R.id.editTextNumberSearch);
        editTextContentSearch = view.findViewById(R.id.editTextContentSearch);

        setTimeTextViewSearch = view.findViewById(R.id.setTimeTextViewSearch);
        setDateTextViewSearch = view.findViewById(R.id.setDateTextViewSearch);

        spinnerForRepeatSearch = view.findViewById(R.id.spinnerForRepeatSearch);
        spinnerForCategorySearch = view.findViewById(R.id.spinnerForCategorySearch);

        switchOnlyPastReminders = view.findViewById(R.id.switchOnlyPastReminders);

        buttonSearchAction = view.findViewById(R.id.buttonSearchAction);
        buttonCancelSearchAction = view.findViewById(R.id.buttonCancelSearchAction);

        recyclerView = view.findViewById((R.id.recyclerView));

        //endregion
        //region eventListeners
        dateSetListener = (view1, year, month, dayOfMonth) -> {
            month += 1;
            if (month < 10)
                date = year + "-0" + month;
            else
                date = year + "-" + month;
            if (dayOfMonth < 10)
                date += "-0" + dayOfMonth;
            else
                date += "-" + dayOfMonth;
            setDateTextViewSearch.setText(date);
        };
        setTimeTextViewSearch.setOnClickListener(v -> {
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

                    setTimeTextViewSearch.setText(timeChecker);
                }
            }, hour, minute, android.text.format.DateFormat.is24HourFormat(getContext()));
            timePickerDialog.show();
        });
        setDateTextViewSearch.setOnClickListener(v -> {
            final int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
            dialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
            dialog.show();
        });
        buttonSearchAction.setOnClickListener(this);
        buttonCancelSearchAction.setOnClickListener(this);
        //endregion

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSearchAction:
                //region if we choose search we create a dataSet to send to ReminderCollection

                //checking if the date was set.
                int priority = Integer.parseInt(editTextNumberSearch.getText().toString());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                if (!setDateTextViewSearch.getText().toString().equals("select to choose date") && !setTimeTextViewSearch.getText().toString().equals("select to choose time")) {
                    String datemaker = "" + setDateTextViewSearch.getText() + " " + setTimeTextViewSearch.getText();
                    TextToDate = LocalDateTime.parse(datemaker, formatter);
                } else {
                    TextToDate = null;
                }
                ArrayList<ActivityTask> dataSet = ActivityTasksUsed.findExactActivityTask(
                        priority,
                        TextToDate,
                        Repetition.valueOf(spinnerForRepeatSearch.getSelectedItem().toString()),
                        MasloCategorys.valueOf(spinnerForCategorySearch.getSelectedItem().toString()),
                        editTextContentSearch.getText().toString()
                );

                if (switchOnlyPastReminders.isChecked()) {
                    dataSet.removeIf(activityTask -> activityTask.getTimeOfActivity().isAfter(LocalDateTime.now()));
                }

                RemindersCollection.setDataSet(dataSet);
                //endregion
            case R.id.buttonCancelSearchAction:
                //region switch/create and move to ReminderCollection fragment.
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                RemindersCollection rc = new RemindersCollection();
                ft.replace(R.id.fragment_search, rc);
                ft.commit();
                break;

            //endregion
        }


    }
}