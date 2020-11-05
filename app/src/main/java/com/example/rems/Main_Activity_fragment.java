package com.example.rems;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Main_Activity_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main_Activity_fragment extends Fragment implements View.OnClickListener {

    public Main_Activity_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Main_Activity_fragment.
     */
    public static Main_Activity_fragment newInstance() {
        Main_Activity_fragment fragment = new Main_Activity_fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public void buttonAddNewReminderClickHandeler(View v) {

    }

    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {

        }
    }

    public void onClick(View view)//TODO: make a utility method for switching fragments on the main_activity_fragment(see note).
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();// built in to android studio
        switch (view.getId()) {//recognizing what button was pushed

            case R.id.buttonAddNewReminder:

                edit_reminder_fragment erf = new edit_reminder_fragment();//creating the fragment to put insted
                ft.replace(R.id.main_Activity_fragment, erf).commit();//making the transaction

                Toast.makeText(getActivity(), "event launched", Toast.LENGTH_SHORT).show();//notifying the event was called

                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {//TODO: what exactly is the onCreateView ment for?

        View view = inflater.inflate(R.layout.fragment_main__activity_fragment, container, false);
        Button buttonAddNewReminder = view.findViewById(R.id.buttonAddNewReminder);
        buttonAddNewReminder.setOnClickListener(this);
        return view;


        //return inflater.inflate(R.layout.fragment_main__activity_fragment, container, false);
    }

}