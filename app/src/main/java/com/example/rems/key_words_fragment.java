package com.example.rems;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import module.ActivityTask;
import module.ActivityTasksUsed;
import module.WordPriority;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link key_words_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class key_words_fragment extends Fragment implements View.OnClickListener {

    private ScrollView scrollView;
    private Map<String, Integer> allWords;
    private Map<String, String> allBucketWords;
    private LinearLayout hoster; //can't add more then one layout to ScrollView so the hoster will hold all the data lines to print (like a collection for layouts).
    private LinearLayout PriorityWordsSectionHolder;
    private LinearLayout BucketWordsSectionHolder;
    private TextView prioritySectionText;
    private TextView bucketSectionText;
    //those ArrayLists will hold the buttons for edit/delete and word/priority of all the words ordered,
    //so every word will have matching button indexes for the program to easily know what word to work on.
    private ArrayList<ImageButton> deleteReminderButton;
    private ArrayList<ImageButton> editReminderButton;
    private ArrayList<TextView> wordText;
    private ArrayList<TextView> wordPriority;
    //bucket words variables
    private ArrayList<ImageButton> deleteBucketButton;
    private ArrayList<ImageButton> editBucketButton;
    private ArrayList<TextView> BucketWordText;
    private ArrayList<TextView> BucketRange;

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
                for(ImageButton edtbtn:editReminderButton)
                    edtbtn.setEnabled(false);
                for(ImageButton edtbtn:deleteReminderButton)
                    edtbtn.setEnabled(false);
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

        scrollView= view.findViewById(R.id.scrollViewKeyWords);
        hoster= new LinearLayout(getActivity());
        hoster.setOrientation(LinearLayout.VERTICAL);
        hoster.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //region call to show all priority words
        //set the "priority words" Title to divide between priority words and bucket words
        prioritySectionText = new TextView(getActivity());
        prioritySectionText.setText("Priority words");
        prioritySectionText.setTextSize(30);
        prioritySectionText.setGravity(Gravity.CENTER);
        prioritySectionText.setTextColor(Color.BLACK);
        prioritySectionText.setTypeface(null,Typeface.BOLD);
        PriorityWordsSectionHolder=new LinearLayout(getActivity());
        PriorityWordsSectionHolder.setOrientation(LinearLayout.VERTICAL);
        PriorityWordsSectionHolder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        PriorityWordsSectionHolder.addView(prioritySectionText);
        hoster.addView(PriorityWordsSectionHolder);
        //add the priority words to the UI
        editReminderButton = new ArrayList<>();
        deleteReminderButton = new ArrayList<>();
        wordText = new ArrayList<>();
        wordPriority = new ArrayList<>();
        allWords= WordPriority.getPriorityWords(); //fetch all words
        for(Map.Entry<String,Integer> entry:allWords.entrySet())
           addWordToScrollViewFuture(entry.getKey(),entry.getValue());
        //endregion

        //region call to show all bucket words
        //set the "bucket words" Title to divide between priority words and bucket words
        bucketSectionText = new TextView(getActivity());
        bucketSectionText.setText("Bucket words");
        bucketSectionText.setTextSize(30);
        bucketSectionText.setGravity(Gravity.CENTER);
        bucketSectionText.setTextColor(Color.BLACK);
        bucketSectionText.setTypeface(null,Typeface.BOLD);
        BucketWordsSectionHolder=new LinearLayout(getActivity());
        BucketWordsSectionHolder.setOrientation(LinearLayout.VERTICAL);
        BucketWordsSectionHolder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        BucketWordsSectionHolder.addView(bucketSectionText);
        hoster.addView(BucketWordsSectionHolder);
        //add the bucket words to the UI
        editBucketButton = new ArrayList<>();
        deleteBucketButton = new ArrayList<>();
        BucketWordText = new ArrayList<>();
        BucketRange = new ArrayList<>();
        allBucketWords= WordPriority.getBucketWords(); //fetch all words
        for(Map.Entry<String,String> entry:allBucketWords.entrySet())
            addBucketWordToScrollViewFuture(entry.getKey(),entry.getValue());
        //endregion

        scrollView.addView(hoster); //add the words to the scrollView

        //region add functionality to buttons of priority words
        for(ImageButton imageButton:editReminderButton){ //create the functionality to each edit button
            final ImageButton Editbtn=imageButton;
            Editbtn.setId(editReminderButton.indexOf(imageButton));

            Editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    caller((String) allWords.keySet().toArray()[Editbtn.getId()],(int) allWords.get(allWords.keySet().toArray()[Editbtn.getId()]));
                }
            });
        }

        for(ImageButton imageButton:deleteReminderButton){ //create the functionality to each delete button
            final ImageButton Editbtn=imageButton;
            Editbtn.setId(deleteReminderButton.indexOf(imageButton));

            Editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String reminderText=(String) allWords.keySet().toArray()[Editbtn.getId()];
                    if(WordPriority.removeWord(reminderText)) {
                        Toast.makeText(getActivity(), "deleted " + reminderText, Toast.LENGTH_SHORT).show();
                        //reload the fragment to update the word list
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        key_words_fragment maf = new key_words_fragment();
                        ft.replace(R.id.fragment_key_words_fragment, maf).commit();
                    }
                }
            });
        }
        //endregion
        //region add functionality to buttons of bucket words
        for(ImageButton imageButton:editBucketButton){ //create the functionality to each edit button
            final ImageButton Editbtn=imageButton;
            Editbtn.setId(editBucketButton.indexOf(imageButton));

            Editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bucketWordCaller((String) allBucketWords.keySet().toArray()[Editbtn.getId()],(String) allBucketWords.get(allBucketWords.keySet().toArray()[Editbtn.getId()]));
                }
            });
        }

        for(ImageButton imageButton:deleteBucketButton){ //create the functionality to each delete button
            final ImageButton Editbtn=imageButton;
            Editbtn.setId(deleteBucketButton.indexOf(imageButton));

            Editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String reminderText=(String) allBucketWords.keySet().toArray()[Editbtn.getId()];
                    if(WordPriority.deleteBucketWord(reminderText)) {
                        Toast.makeText(getActivity(), "deleted " + reminderText, Toast.LENGTH_SHORT).show();
                        //reload the fragment to update the word list
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        key_words_fragment maf = new key_words_fragment();
                        ft.replace(R.id.fragment_key_words_fragment, maf).commit();
                    }
                }
            });
        }
        //endregion

        return view;
    }

    private void caller(String word,int priority){ //calls the editingReminder method from edit_reminder_fragment to open the edit fragment with the info of our reminder we want to edit
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        edit_words_fragment erf = new edit_words_fragment();//creating the fragment to put instead
        ft.replace(R.id.fragment_key_words_fragment, erf).commit();//making the transaction
        getFragmentManager().executePendingTransactions();//used to stop the onCreateView and allow the editingReminder() method to set the information
        edit_words_fragment.editingword(word,priority);
    }
    private void bucketWordCaller(String word,String range){ //calls the editingReminder method from edit_reminder_fragment to open the edit fragment with the info of our reminder we want to edit
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        edit_words_fragment erf = new edit_words_fragment();//creating the fragment to put instead
        ft.replace(R.id.fragment_key_words_fragment, erf).commit();//making the transaction
        getFragmentManager().executePendingTransactions();//used to stop the onCreateView and allow the editingReminder() method to set the information
        edit_words_fragment.editingBucketWord(word,range);
    }

    /**
     *  this method is privet and called only by <b><i>onCreateView</i></b> method.<br>
     *  this method is dynamically creating in the UI the text and the delete\edit buttons of the priority word.
     *  @param - contain the relevant info for the text
     *  @return void
     * */

    /*     genera UI element hierarchy
     *
     *         outerLayout
     *    * * * * * * * * * * * * * * * * * * *
     *    *      innerLayout                  *
     *    *   * * * * * * * * * * * * * * *   *
     *    *   * TXT  | num  edit | delete *   *
     *    *   * * * * * * * * * * * * * * *   *
     *    *                                   *
     *    * * * * * * * * * * * * * * * * * * *
     *
     */
    private void addWordToScrollViewFuture(String word, int priority){ //this method dynamically creates the elements of the priority word on our fragment,called in onCreateView
        //hierarchy holder of our elements please look up for the schema
        LinearLayout outerLayout = new LinearLayout(getActivity());
        LinearLayout innerLayout =new LinearLayout(getActivity());
        LinearLayout wordholder =new LinearLayout(getActivity());
        LinearLayout priorityholder =new LinearLayout(getActivity());
        ImageButton btnEdit = new ImageButton(getActivity());
        ImageButton btnDelete = new ImageButton(getActivity());
        TextView wordText = new TextView(getActivity());
        TextView wordPriority = new TextView(getActivity());

        wordholder.setOrientation(LinearLayout.VERTICAL);
        wordholder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        priorityholder.setOrientation(LinearLayout.VERTICAL);
        priorityholder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        LinearLayout.LayoutParams btnSize = new LinearLayout.LayoutParams(180, 120); //TODO:need to use some math and magic to make sure it fit any screen size and resolution
        btnSize.setMargins(0,5,15,5);

        outerLayout.setOrientation(LinearLayout.VERTICAL);
        outerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParamsInnerLayout= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsInnerLayout.setMargins(0,20,0,11);
        innerLayout.setLayoutParams(layoutParamsInnerLayout);

        btnEdit.setImageResource(R.drawable.ic_action_edit);
        btnEdit.setBackgroundResource(R.drawable.main_edit_button_raunding);
        btnEdit.setLayoutParams(btnSize);
        editReminderButton.add(btnEdit);

        btnDelete.setImageResource(R.drawable.ic_action_delete);
        btnDelete.setBackgroundResource(R.drawable.main_edit_button_raunding);
        btnDelete.setLayoutParams(btnSize);
        deleteReminderButton.add(btnDelete);

        wordText.setText(""+word);
        wordText.setTextColor(Color.BLACK);
        wordText.setTextSize(30);
        LinearLayout.LayoutParams paramstxt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1);
        paramstxt.setMargins(20,5,0,0);
        wordText.setLayoutParams(paramstxt);
        wordholder.addView(wordText);

        wordPriority.setText(""+priority);
        wordPriority.setTextColor(Color.BLACK);
        wordPriority.setTextSize(30);
        wordPriority.setLayoutParams(paramstxt);
        priorityholder.addView(wordPriority);


        if(innerLayout!=null && outerLayout!=null && scrollView != null){
            outerLayout.addView(innerLayout);
            innerLayout.addView(wordholder);
            innerLayout.addView(priorityholder);
            innerLayout.addView(btnEdit);
            innerLayout.addView(btnDelete);
            hoster.addView(outerLayout);
        }
    }


    /*     genera UI element hierarchy
     *
     *         outerLayout
     *    * * * * * * * * * * * * * * * * * * *
     *    *      innerLayout                  *
     *    *   * * * * * * * * * * * * * * *   *
     *    *   * TXT  | TXT  edit | delete *   *
     *    *   * * * * * * * * * * * * * * *   *
     *    *                                   *
     *    * * * * * * * * * * * * * * * * * * *
     *
     */
    private void addBucketWordToScrollViewFuture(String word, String range) { //this method dynamically creates the elements of the bucket word on our keyword fragment,called in onCreateView
        //hierarchy holder of our elements please look up for the schema
        LinearLayout outerLayout = new LinearLayout(getActivity());
        LinearLayout innerLayout = new LinearLayout(getActivity());
        LinearLayout wordholder = new LinearLayout(getActivity());
        LinearLayout Rabgeholder = new LinearLayout(getActivity());
        ImageButton btnEdit = new ImageButton(getActivity());
        ImageButton btnDelete = new ImageButton(getActivity());
        TextView BucketWordText = new TextView(getActivity());
        TextView BucketRange = new TextView(getActivity());

        wordholder.setOrientation(LinearLayout.VERTICAL);
        wordholder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        Rabgeholder.setOrientation(LinearLayout.VERTICAL);
        Rabgeholder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f));
        LinearLayout.LayoutParams btnSize = new LinearLayout.LayoutParams(180, 120); //TODO:need to use some math and magic to make sure it fit any screen size and resolution
        btnSize.setMargins(0, 5, 15, 5);

        outerLayout.setOrientation(LinearLayout.VERTICAL);
        outerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParamsInnerLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsInnerLayout.setMargins(0, 20, 0, 11);
        innerLayout.setLayoutParams(layoutParamsInnerLayout);

        btnEdit.setImageResource(R.drawable.ic_action_edit);
        btnEdit.setBackgroundResource(R.drawable.main_edit_button_raunding);
        btnEdit.setLayoutParams(btnSize);
        editBucketButton.add(btnEdit);

        btnDelete.setImageResource(R.drawable.ic_action_delete);
        btnDelete.setBackgroundResource(R.drawable.main_edit_button_raunding);
        btnDelete.setLayoutParams(btnSize);
        deleteBucketButton.add(btnDelete);

        BucketWordText.setText("" + word);
        BucketWordText.setTextColor(Color.BLACK);
        BucketWordText.setTextSize(30);
        LinearLayout.LayoutParams paramstxt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        paramstxt.setMargins(20, 5, 0, 0);
        BucketWordText.setLayoutParams(paramstxt);
        wordholder.addView(BucketWordText);

        BucketRange.setText("" + range);
        BucketRange.setTextColor(Color.BLACK);
        BucketRange.setTextSize(30);
        BucketRange.setLayoutParams(paramstxt);
        Rabgeholder.addView(BucketRange);


        if (innerLayout != null && outerLayout != null && scrollView != null) {
            outerLayout.addView(innerLayout);
            innerLayout.addView(wordholder);
            innerLayout.addView(Rabgeholder);
            innerLayout.addView(btnEdit);
            innerLayout.addView(btnDelete);
            hoster.addView(outerLayout);
        }
    }

}