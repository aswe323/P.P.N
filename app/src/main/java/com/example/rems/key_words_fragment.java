package com.example.rems;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link key_words_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class key_words_fragment extends Fragment implements View.OnClickListener {


    public key_words_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment key_words_fragment.
     */
    public static key_words_fragment newInstance(String param1, String param2) {
        key_words_fragment fragment = new key_words_fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public void onClick(View view)//TODO: make a utility method for switching fragments on the main_activity_fragment(see note).
    //note: will probably need to make a static variable to track the current fragment displayed to be replaced
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.buttonAddNewWord:
                edit_words_fragment ewf = new edit_words_fragment();
                ft.replace(R.id.fragment_key_words_fragment, ewf).commit();
                Toast.makeText(getActivity(), "event launched", Toast.LENGTH_SHORT).show();
                break;
        }
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

        View view = inflater.inflate(R.layout.fragment_key_words_fragment, container, false);
        Button buttonEditKeyWords = view.findViewById(R.id.buttonAddNewWord);
        buttonEditKeyWords.setOnClickListener(this);

        return view;
    }
}