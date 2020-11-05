package com.example.rems;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link edit_words_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class edit_words_fragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private SeekBar thisSeekBar;
    private TextView thisSeekBarTextView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_edit_words_fragment, container, false);// the hell is this even?

        //accessing seekBar and the Text of Seekbar by ID and setting progress to 0
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
        return view;
    }
}