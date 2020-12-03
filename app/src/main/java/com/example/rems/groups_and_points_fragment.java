package com.example.rems;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import module.ActivityTasksUsed;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link groups_and_points_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class groups_and_points_fragment extends Fragment {

    TextView textViewUserPoints;


    public groups_and_points_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment groups_and_points_fragment.
     */
    public static groups_and_points_fragment newInstance() {
        groups_and_points_fragment fragment = new groups_and_points_fragment();
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_groups_and_points_fragment, container, false);

        textViewUserPoints = view.findViewById(R.id.textViewUserPoints);
        textViewUserPoints.setText(String.valueOf(ActivityTasksUsed.getUserPersonalScore()));
        return view;
    }
}