package com.example.rems;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import module.ActivityTask;
import module.SubActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link activity_display#newInstance} factory method to
 * create an instance of this fragment.
 */
public class activity_display extends Fragment {

    private static TextView dateAndTime, repetitionDisplay, masloCategoryDisplay, contentDisplay;
    private static RecyclerView subAcRecyclerView;


    public activity_display() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static activity_display newInstance(ArrayList<SubActivity> subActivities) {
        activity_display fragment = new activity_display();
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

    public static void setDisplayedActivityTask(ActivityTask activityTask) {
        dateAndTime.setText(activityTask.getTimeOfActivity().toString());
        repetitionDisplay.setText(activityTask.getRepetition().toString());
        masloCategoryDisplay.setText(activityTask.getCategory().toString());
        contentDisplay.setText(activityTask.getContent());


        subAcRecyclerView.setAdapter(new RecyclerViewSubActivityCustomAdapter(activityTask.getSubActivities()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_details_display, container, false);

        //region assigning views to members
        dateAndTime = view.findViewById(R.id.textDateAndTime);
        repetitionDisplay = view.findViewById(R.id.textRepeat);
        masloCategoryDisplay = view.findViewById(R.id.textMasloCategory);
        contentDisplay = view.findViewById(R.id.contentDisplay);

        subAcRecyclerView = view.findViewById(R.id.recyclerViewSubActivityDisplayer);
        //endregion

        subAcRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }
}