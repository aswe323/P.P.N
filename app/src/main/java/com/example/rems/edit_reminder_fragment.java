package com.example.rems;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.Inflater;

import module.ActivityTask;
import module.ActivityTasksUsed;
import module.MasloCategorys;
import module.Repetition;
import module.SubActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link edit_reminder_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class edit_reminder_fragment extends Fragment implements View.OnClickListener {

    //region the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static int returnToID = R.id.main_Activity_fragment;


    private static ScrollView SubActivitiesScrollView;
    private static LinearLayout hoster;
    private static ArrayList<ImageButton> deleteReminderButton;
    private static ArrayList<SubActivity> subactivities;
    private static boolean isReopen = false;
    private static boolean mIsActive;
    private ArrayList<TextView> reminderText;
    private static View Mview; //TODO:find solution for this,used in creation of subactivities

    private static Context context; //TODO:find solution for this,used in creation of subactivities
    private Switch automaticAssignment;
    private static Spinner masloCategory;
    private static Spinner repetition;
    private static EditText discription;
    private static DateTimeFormatter formatter;
    private LocalDateTime TextToDate;
    private String date = "";
    private static TextView settimetext;
    private static TextView setdatetext;
    private Button buttonIdentifier;
    private Button buttonAddSubActivity;
    private Button cancelButton;
    private EditText inputForSubActivityDialog;
    private String subactivitytext="";
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private AlertDialog.Builder subActivityDialogBox;
    private static ArrayList<SubActivity> subActivitiesArrayList;
    private static boolean isEditFlag=false; //if i opened a reminder from my "next reminders" list in the home button flag will be true and it's means we need to call Update query and not inset
    private static ActivityTask EditedActivityTask;
    private static FloatingActionButton buttonTaskComplete;
    //endregion

    public edit_reminder_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment edit_reminder_fragment.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static edit_reminder_fragment newInstance() {
        edit_reminder_fragment fragment = new edit_reminder_fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static boolean isActive() {
        return mIsActive;
    }

    public static void setmIsActive(boolean mIsActive) {
        edit_reminder_fragment.mIsActive = mIsActive;
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
        switch (view.getId()) {//recognizing what button was pushed

            case R.id.SetTimeTextView:
                //region
                final int hour=calendar.get(calendar.HOUR_OF_DAY);
                final int minute=calendar.get(calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String timeChecker="";
                        if(hourOfDay<10)
                            timeChecker+="0"+hourOfDay+":";
                            //settimetext.setText("0"+hourOfDay+":"+minute);
                        else
                            timeChecker+=hourOfDay+":";
                            //settimetext.setText(hourOfDay+":"+minute);
                        if(minute<10)
                            timeChecker+="0"+minute;
                        else
                            timeChecker+=minute;

                        settimetext.setText(timeChecker);
                    }
                },hour,minute,android.text.format.DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();

                Toast.makeText(getActivity(), "time set", Toast.LENGTH_SHORT).show();//notifying the event was called
                break;
            //endregion
            case R.id.SetDateTextView:
                //region
                final int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable((new ColorDrawable(Color.TRANSPARENT)));
                dialog.show();


                Toast.makeText(getActivity(), "time set", Toast.LENGTH_SHORT).show();//notifying the event was called
                break;
            //endregion
            case R.id.buttonTaskComplete:
                //region
                ActivityTasksUsed.markComplete(EditedActivityTask);
                ActivityTasksUsed.getUserPersonalScore();
                //endregion
            case R.id.ButtonSaveReminder:
                //region
                if (!isEditFlag) { //if i opened a reminder from my "next reminders" list in the home button flag will be true and it's means we need to call Update query and not inset
                    if (automaticAssignment.isChecked()) {
                        //if the automantic assignment option is checked.
                        ActivityTasksUsed.addActivityTask(new ActivityTask(0,
                                MasloCategorys.valueOf(masloCategory.getSelectedItem().toString()),
                                Repetition.valueOf(repetition.getSelectedItem().toString()),
                                discription.getText().toString(),
                                new ArrayList<SubActivity>()));
                        Toast.makeText(getActivity(), "auto assigned", Toast.LENGTH_SHORT).show();//notifying the event was called
                    } else {
                        //if the user choose to select time and date by himself.

                        if (settimetext.getText().toString().equals("select to choose time") || setdatetext.getText().toString().equals("select to choose date")) {
                            Toast.makeText(getActivity(), "select date and time", Toast.LENGTH_SHORT).show();//notifying the event was called
                            return;
                        }

                        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        String datemaker=""+setdatetext.getText()+" "+settimetext.getText();
                        TextToDate = LocalDateTime.parse(datemaker, formatter);
                        ActivityTasksUsed.addActivityTask(new ActivityTask(
                                0,
                                0,
                                MasloCategorys.valueOf(masloCategory.getSelectedItem().toString()),//MasloCategory
                                Repetition.valueOf(repetition.getSelectedItem().toString()),//Repetition
                                discription.getText().toString(),
                                TextToDate,
                                subActivitiesArrayList
                                //yyyy-MM-dd HH:mm:ss
                        ));
                        Toast.makeText(getActivity(), "manually assigned", Toast.LENGTH_SHORT).show();//notifying the event was called
                    }
                }
                else{
                    String reminderContent=EditedActivityTask.getContent();
                    EditedActivityTask.setCategory(MasloCategorys.valueOf(masloCategory.getSelectedItem().toString()));
                    EditedActivityTask.setRepetition(Repetition.valueOf(repetition.getSelectedItem().toString()));
                    EditedActivityTask.setContent(discription.getText().toString());
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    String datemaker = "" + setdatetext.getText() + " " + settimetext.getText();
                    TextToDate = LocalDateTime.parse(datemaker, formatter);
                    EditedActivityTask.setTimeOfActivity(TextToDate);
                    EditedActivityTask.setSubActivities(subActivitiesArrayList);
                    if (ActivityTasksUsed.editActivityTask(EditedActivityTask))
                        Toast.makeText(getActivity(), "updated: " + reminderContent, Toast.LENGTH_SHORT).show();
                }

            case R.id.ButtonCancelReminder:
                //region
                returnBack();
                break;

            //endregion
            case R.id.ButtonAddSubActivity:
                //region
                subActivityDialogBox = new AlertDialog.Builder(getContext());
                subActivityDialogBox.setTitle("sub reminder:");
                subActivityDialogBox.setMessage("enter the sub reminder here: ");
                inputForSubActivityDialog = new EditText(getContext());
                subActivityDialogBox.setView(inputForSubActivityDialog);

                subActivityDialogBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        subactivitytext = inputForSubActivityDialog.getText().toString();
                        if(!isEditFlag)
                            subActivitiesArrayList.add(new SubActivity(0,0,subactivitytext));
                        else
                            subActivitiesArrayList.add(new SubActivity(0,EditedActivityTask.getActivityTaskID(),subactivitytext));
                        Toast.makeText(getActivity(), "sub reminder was added: "+subactivitytext, Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
                subActivityDialogBox.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                inputForSubActivityDialog.setText("");
                                return;
                            }
                        });
                subActivityDialogBox.show();
                break;
            //endregion
            case R.id.switchForAi://neccesery for prototype.
                //region
                automaticAssignment.setChecked(false);
                Toast.makeText(getActivity(), "feature not ready yet", Toast.LENGTH_SHORT).show();
                break;

            //wrg
            //endregion
            //endregion
        }

    }

    public void returnBack() {//this method was seperated from onClick to allow overriding the destination of the return button.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment destinationFragment = new Main_Activity_fragment();

        switch (returnToID) {
            case R.id.fragment_reminders_colletion:
                destinationFragment = new RemindersCollection();
                break;
            case R.id.main_Activity_fragment:
                destinationFragment = new Main_Activity_fragment();
                break;
        }
        ft.replace(this.getId(), destinationFragment).commit();
        edit_reminder_fragment.setmIsActive(false);

    }

    /**
     * used to set the return destination of the fragment after it is being closed. will be reset to main_activity_fragment after closed.
     *
     * @param id
     */
    public static void setReturnToID(@IdRes int id) {
        returnToID = id;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_edit_reminder_fragment, container, false);
        View view = inflater.inflate(R.layout.fragment_edit_reminder_fragment, container, false);
        this.Mview=view;
        context=getActivity();
        isEditFlag = false;
        edit_reminder_fragment.setmIsActive(true);

        SubActivitiesScrollView=(ScrollView) view.findViewById(R.id.subActivitiesWindow);
        hoster = new LinearLayout(getActivity());
        hoster.setOrientation(LinearLayout.VERTICAL);
        hoster.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        deleteReminderButton = new ArrayList<>();
        subactivities=new ArrayList<>();

        automaticAssignment = view.findViewById(R.id.switchForAi);
        masloCategory = view.findViewById(R.id.spinnerForCategory);
        repetition = view.findViewById(R.id.spinnerForRepeat);
        discription = view.findViewById(R.id.editTextForReminder);
        subActivitiesArrayList = new ArrayList<>();
        automaticAssignment.setOnClickListener(this);

        cancelButton = view.findViewById(R.id.ButtonCancelReminder);
        cancelButton.setOnClickListener(this);

        settimetext = view.findViewById(R.id.SetTimeTextView);
        settimetext.setOnClickListener(this);

        buttonIdentifier = view.findViewById(R.id.ButtonSaveReminder);
        buttonIdentifier.setOnClickListener(this);

        buttonAddSubActivity = view.findViewById(R.id.ButtonAddSubActivity);
        buttonAddSubActivity.setOnClickListener(this);

        setdatetext = view.findViewById(R.id.SetDateTextView);
        dateSetListener = (view1, year, month, dayOfMonth) -> {
            month += 1;
            if (month < 10)
                date = year + "-0" + month;
            else
                date = year + "-" + month;
            if (dayOfMonth < 10)
                date += "-0" + dayOfMonth;
            else
                date += "-" + dayOfMonth;
            setdatetext.setText(date);
        };
        setdatetext.setOnClickListener(this);

        buttonTaskComplete = view.findViewById(R.id.buttonTaskComplete);
        buttonTaskComplete.setEnabled(false);
        buttonTaskComplete.hide();

        buttonTaskComplete.setOnClickListener(this);



        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void editingReminder(ActivityTask activityTask){ //this function is called before the fragment is presented,it's inserting the data of the needed ActivityTask to the elements TODO:add to the book
        isEditFlag=true; //turn edit flag to true so we update instead of insert to Database
        EditedActivityTask=activityTask;//save the ActivityTask to sed for editing in ActivityTasksUsed.editActivityTask();
        //subActivitiesArrayList=activityTask.getSubActivities(); used to get all existing SubActivities //TODO:create some sort of check if in DB has already same SubActivity (as if its has same content and notify user

        //setting all the data to the elementsString DateInFormat=formatter.format(activityTask.getTimeOfActivity());
        discription.setText(activityTask.getContent());
        masloCategory.setSelection(activityTask.getCategory().ordinal());
        repetition.setSelection(activityTask.getRepetition().ordinal());
        formatter = DateTimeFormatter.ofPattern("HH:mm");
        String DateInFormat = formatter.format(activityTask.getTimeOfActivity());
        settimetext.setText(DateInFormat);
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateInFormat = formatter.format(activityTask.getTimeOfActivity());
        setdatetext.setText(DateInFormat);

        //View view=edit_words_fragment.newInstance().getView();
        //SubActivitiesScrollView=(ScrollView) Mview.findViewById(R.id.subActivitiesWindow);
        subactivities = activityTask.getSubActivities();
        for (SubActivity addSubActivity : subactivities)
            addSubActivityToScrollView(addSubActivity);
        SubActivitiesScrollView.addView(hoster);


        for (ImageButton imageButton : deleteReminderButton) { //create the functionality to each delete button
            final ImageButton Editbtn = imageButton;
            Editbtn.setId(deleteReminderButton.indexOf(imageButton));

            Editbtn.setOnClickListener(view12 -> {
                SubActivity subActivityToDelete = subactivities.get(Editbtn.getId());
                ArrayList<ActivityTask> activityTaskOfSubActivity = ActivityTasksUsed.findExactActivityTask(subActivityToDelete.getActivityTaskID(), 0, null, null, null, null);

                if (ActivityTasksUsed.removeSubActivity(subActivityToDelete)) {
                    Toast.makeText(context, "deleted: ", Toast.LENGTH_SHORT).show();
                    //reload the fragment to update the reminder list
                    isReopen = true;


                    FragmentTransaction ft = ((FragmentActivity) Mview.getContext()).getSupportFragmentManager().beginTransaction();

                    edit_reminder_fragment erf = new edit_reminder_fragment();
                    ft.replace(R.id.main_Activity_fragment, erf).commit();
                    ((FragmentActivity) view12.getContext()).getSupportFragmentManager().executePendingTransactions();//used to stop the onCreateView and allow the editingReminder() method to set the information
                    editingReminder(activityTaskOfSubActivity.get(0));

                    SubActivitiesScrollView.requestLayout();


                }
            });
        }

        buttonTaskComplete.setEnabled(true);
        if (activityTask.getPriority() > 0) {
            buttonTaskComplete.setImageResource(android.R.drawable.checkbox_off_background);
        } else {
            buttonTaskComplete.setImageResource(android.R.drawable.checkbox_on_background);

        }
        buttonTaskComplete.show();
    }

    /*     genera UI element hierarchy
     *
     *         outerLayout
     *    * * * * * * * * * * * * * * * * * * *
     *    *      innerLayout                  *
     *    *   * * * * * * * * * * * * * * *   *
     *    *   * TXT              delete   *   *
     *    *   * * * * * * * * * * * * * * *   *
     *    *                                   *
     *    * * * * * * * * * * * * * * * * * * *
     *
     */ //TODO:add to the book
    private static void addSubActivityToScrollView(SubActivity subActivity){ //this method dynamically creates the elements of the SubActivity on our reminder creator/editor,called in onCreateView
        //hierarchy holder of our elements please look up for the schema
        LinearLayout outerLayout = new LinearLayout(context);
        LinearLayout innerLayout =new LinearLayout(context);
        ImageButton btnDelete = new ImageButton(context);
        TextView reminderText = new TextView(context);
        LinearLayout.LayoutParams btnSize = new LinearLayout.LayoutParams(90, 60); //TODO:need to use some math and magic to make sure it fit any screen size and resolution
        btnSize.setMargins(0,5,15,5);

        outerLayout.setOrientation(LinearLayout.VERTICAL);
        outerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        innerLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParamsInnerLayout= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParamsInnerLayout.setMargins(0,7,0,11);
        innerLayout.setLayoutParams(layoutParamsInnerLayout);

        btnDelete.setImageResource(R.drawable.ic_baseline_close_24);
        btnDelete.setBackgroundResource(R.drawable.main_edit_button_raunding);
        btnDelete.setLayoutParams(btnSize);
        deleteReminderButton.add(btnDelete);

        reminderText.setText(""+subActivity.getContent());
        reminderText.setTextColor(Color.BLACK);
        reminderText.setTextSize(30);
        LinearLayout.LayoutParams paramstxt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1);
        paramstxt.setMargins(20,5,0,0);
        reminderText.setLayoutParams(paramstxt);


        if(innerLayout!=null && outerLayout!=null && SubActivitiesScrollView != null){
            outerLayout.addView(innerLayout);
            innerLayout.addView(reminderText);
            innerLayout.addView(btnDelete);
            hoster.addView(outerLayout);
        }

    }
}