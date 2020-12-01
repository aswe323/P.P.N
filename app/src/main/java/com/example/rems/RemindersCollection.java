package com.example.rems;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import module.ActivityTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RemindersCollection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RemindersCollection extends Fragment implements View.OnClickListener {//show all the activities and search in them

    private static DataBaseHelper db = DataBaseHelper.getInstance(null);
    private Context context;

    private static boolean externalDataSet = false;
    private static ArrayList<ActivityTask> mdataSet = new ArrayList<>();

    private FloatingActionButton buttonSearchReminders;


    public RemindersCollection() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RemindersCollection newInstance() {
        RemindersCollection fragment = new RemindersCollection();
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

        //get hold of the RecyclerView element

        RecyclerView recyclerView = view.findViewById((R.id.recyclerView));


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //init the data (reminders)

        mdataSet = externalDataSet ? mdataSet : db.queryForAllActivityTasks();
        externalDataSet = false;


        //create the adapter with the data

        RecyclerViewCustomAdapter adapter = new RecyclerViewCustomAdapter(mdataSet);

        //attach the RecyclerView element to the adapter(setAdapter)

        recyclerView.setAdapter(adapter);

        buttonSearchReminders = view.findViewById(R.id.buttonSearchReminders);
        buttonSearchReminders.setOnClickListener(v -> {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            SearchFragment sf = new SearchFragment();
            ft.replace(R.id.fragment_reminders_colletion, sf).commit();
        });

        return view;
    }

    public static void setDataSet(ArrayList<ActivityTask> dataSet) {
        externalDataSet = true;
        mdataSet = dataSet;
    }

    @Override
    public void onClick(View v) {

    }

}