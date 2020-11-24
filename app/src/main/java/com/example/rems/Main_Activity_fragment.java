package com.example.rems;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import module.ActivityTask;
import module.ActivityTasksUsed;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Main_Activity_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main_Activity_fragment extends Fragment implements View.OnClickListener {

    private ScrollView scrollView;
    private ArrayList<ActivityTask> topActivities;
    private LinearLayout hoster; //can't add more then one layout to ScrollView so the hoster will hold all the data lines to print (like a collection for layouts).
    //those ArrayLists will hold the buttons for edit/delete and content of the reminder ordered,
    //so every reminder will have matching button indexes for the program to easily know what reminder to work on.
    private ArrayList<ImageButton> deleteReminderButton;
    private ArrayList<ImageButton> editReminderButton;
    private ArrayList<TextView> reminderText;

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

                //disable all buttons of edit/delete
                for(ImageButton imageButton:editReminderButton)
                    imageButton.setEnabled(false);
                for(ImageButton imageButton:deleteReminderButton)
                    imageButton.setEnabled(false);

                edit_reminder_fragment erf = new edit_reminder_fragment();//creating the fragment to put insted
                ft.replace(R.id.main_Activity_fragment, erf).commit();//making the transaction

                Toast.makeText(getActivity(), "event launched", Toast.LENGTH_SHORT).show();//notifying the event was called

                break;
            //endregion

            case R.id.buttonShowAllReminders:
                //region
                RemindersColletion rc = new RemindersColletion();//creating the fragment to put insted
                ft.replace(R.id.main_Activity_fragment, rc).commit();//making the transaction

                Toast.makeText(getActivity(), "event launched", Toast.LENGTH_SHORT).show();//notifying the event was called
                break;
            default:
                Toast.makeText(getActivity(), "event launched", Toast.LENGTH_SHORT).show();//notifying the event was called
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {//TODO: what exactly is the onCreateView ment for?

        View view = inflater.inflate(R.layout.fragment_main__activity_fragment, container, false);
        Button buttonAddNewReminder = view.findViewById(R.id.buttonAddNewReminder); //button to move to the adding reminder layout
        buttonAddNewReminder.setOnClickListener(this);

        Button buttonShowAllReminders = view.findViewById(R.id.buttonShowAllReminders);
        buttonShowAllReminders.setOnClickListener(this);

        scrollView = view.findViewById(R.id.scrollViewMain);
        hoster = new LinearLayout(getActivity());
        hoster.setOrientation(LinearLayout.VERTICAL);
        hoster.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        editReminderButton = new ArrayList<>();
        deleteReminderButton = new ArrayList<>();
        reminderText = new ArrayList<>();
        topActivities = ActivityTasksUsed.getCloseActivities(); //fetch the next X reminders TODO: change the X to a number in the future

        for (ActivityTask taskDisplay : topActivities)
            addWordToScrollViewFuture(taskDisplay);

        scrollView.addView(hoster);


        for(ImageButton imageButton:editReminderButton){ //create the functionality to each edit button
            final ImageButton Editbtn=imageButton;
            Editbtn.setId(editReminderButton.indexOf(imageButton));

            Editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    caller(topActivities.get(Editbtn.getId()));
                }
            });
        }

        for(ImageButton imageButton:deleteReminderButton){ //create the functionality to each delete button
            final ImageButton Editbtn=imageButton;
            Editbtn.setId(deleteReminderButton.indexOf(imageButton));

            Editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String reminderText=topActivities.get(Editbtn.getId()).getContent();
                    if(ActivityTasksUsed.removeActivityTask(topActivities.get(Editbtn.getId()))) {
                        Toast.makeText(getActivity(), "deleted " + reminderText, Toast.LENGTH_SHORT).show();
                        //reload the fragment to update the reminder list
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Main_Activity_fragment erf = new Main_Activity_fragment();
                        ft.replace(R.id.main_Activity_fragment, erf).commit();
                    }
                }
            });
        }

        return view;


        //return inflater.inflate(R.layout.fragment_main__activity_fragment, container, false);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void caller(ActivityTask activityTask){ //calls the editingReminder method from edit_reminder_fragment to open the edit fragment with the info of our reminder we want to edit TODO:add to the book
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        edit_reminder_fragment erf = new edit_reminder_fragment();//creating the fragment to put instead
        ft.replace(R.id.main_Activity_fragment, erf).commit();//making the transaction
        getFragmentManager().executePendingTransactions();//used to stop the onCreateView and allow the editingReminder() method to set the information
        edit_reminder_fragment.editingReminder(activityTask);
    }

    /**
     *  this method is privet and called only by <b><i>onCreateView</i></b> method.<br>
     *  this method is dynamically creating in the UI the text and the delete\edit buttons of the activityTask.
     *  @param activityTask contain the relevant info for the text
     *  @return void
     * */

    /*     genera UI element hierarchy
    *
    *         outerLayout
    *    * * * * * * * * * * * * * * * * * * *
    *    *      innerLayout                  *
    *    *   * * * * * * * * * * * * * * *   *
    *    *   * TXT         edit | delete *   *
    *    *   * * * * * * * * * * * * * * *   *
    *    *                                   *
    *    * * * * * * * * * * * * * * * * * * *
    *
     */
    private void addWordToScrollViewFuture(ActivityTask activityTask){ //this method dynamically creates the elements of the reminders on our home page,called in onCreateView
        //hierarchy holder of our elements please look up for the schema
        LinearLayout outerLayout = new LinearLayout(getActivity());
        LinearLayout innerLayout =new LinearLayout(getActivity());
        ImageButton btnEdit = new ImageButton(getActivity());
        ImageButton btnDelete = new ImageButton(getActivity());
        TextView reminderText = new TextView(getActivity());
        LinearLayout.LayoutParams btnSize = new LinearLayout.LayoutParams(180, 120); //TODO:need to use some math and magic to make sure it fit any screen size and resolution
        btnSize.setMargins(0,5,15,5);

        outerLayout.setOrientation(LinearLayout.VERTICAL);
        outerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParamsInnerLayout= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsInnerLayout.setMargins(0,7,0,11);
        innerLayout.setLayoutParams(layoutParamsInnerLayout);

        btnEdit.setImageResource(R.drawable.ic_action_edit);
        btnEdit.setBackgroundResource(R.drawable.main_edit_button_raunding);
        btnEdit.setLayoutParams(btnSize);
        editReminderButton.add(btnEdit);

        btnDelete.setImageResource(R.drawable.ic_action_delete);
        btnDelete.setBackgroundResource(R.drawable.main_edit_button_raunding);
        btnDelete.setLayoutParams(btnSize);
        deleteReminderButton.add(btnDelete);

        reminderText.setText(""+activityTask.getContent());
        reminderText.setTextColor(Color.BLACK);
        reminderText.setTextSize(30);
        LinearLayout.LayoutParams paramstxt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1);
        paramstxt.setMargins(20,5,0,0);
        reminderText.setLayoutParams(paramstxt);


        if(innerLayout!=null && outerLayout!=null && scrollView != null){
            outerLayout.addView(innerLayout);
            innerLayout.addView(reminderText);
            innerLayout.addView(btnEdit);
            innerLayout.addView(btnDelete);
            hoster.addView(outerLayout);
        }

    }

}