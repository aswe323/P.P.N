package com.example.rems;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

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
  /*          Switch automaticAssignment = view.findViewById(R.id.switchForAi);
            switch(view.getId()) {
                case(R.id.ButtonSaveReminder):
                    Spinner masloCategory = view.findViewById(R.id.spinnerForCategory);
                    Spinner repetition = view.findViewById(R.id.spinnerForRepeat);
                    EditText textBox = view.findViewById((R.id.editTextForReminder));
                    if (automaticAssignment.isChecked()) {
                        ActivityTasksUsed.addActivityTask(new ActivityTask(0,
                                MasloCategorys.valueOf(masloCategory.getSelectedItem().toString()),
                                Repetition.valueOf(repetition.getSelectedItem().toString()),
                                textBox.getText().toString(),
                                null));
                    }else{

                        ActivityTasksUsed.addActivityTask(new ActivityTask(
                                0,
                                0,
                                MasloCategorys.valueOf(masloCategory.getSelectedItem().toString()),
                                Repetition.valueOf(repetition.getSelectedItem().toString()),
                                textBox.getText().toString(),

                                ));


                                0,
                                MasloCategorys.valueOf(masloCategory.getSelectedItem().toString()),
                                Repetition.valueOf(repetition.getSelectedItem().toString()),
                                textBox.getText().toString(),
                                null));
                    }




            }*/

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_reminder_fragment, container, false);
    }
}