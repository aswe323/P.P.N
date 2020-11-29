package com.example.rems;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import module.ActivityTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemindersColletion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemindersColletion extends Fragment implements View.OnClickListener {//show all the activities and search in them

    private static DataBaseHelper db = DataBaseHelper.getInstance(null);
    private Context context;


    public RemindersColletion() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RemindersColletion newInstance() {
        RemindersColletion fragment = new RemindersColletion();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminders_colletion, container, false);

        FloatingActionButton buttonReturnToMainFragment = view.findViewById(R.id.buttonReturnToMainFragment);
        buttonReturnToMainFragment.setOnClickListener(this);
        //get hold of the RecyclerView element

        RecyclerView recyclerView = view.findViewById((R.id.recyclerView));


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //init the data (reminders)

        ArrayList<ActivityTask> dataSet = db.queryForAllActivityTasks();


        //create the adapter with the data

        RecyclerViewCustomAdapter adapter = new RecyclerViewCustomAdapter(dataSet);

        //attach the RecyclerView element to the adapter(setAdapter)

        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }

    }
}