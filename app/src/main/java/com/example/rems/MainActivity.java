package com.example.rems;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


import java.util.ArrayList;

import module.ActivityTask;
import module.MasloCategorys;
import module.Repetition;
import module.SubActivity;
import module.WordPriority;

public class MainActivity extends AppCompatActivity {

    DataBaseHelper db;


    private boolean isFirstRun = true;
    private SharedPreferences prefs;
    private WordPriority wordPriority;
    /***********************!!!!!!!!!!!!!!!!**********************/
    private SubActivity subActivity;
    private SubActivity subUpdateTester;
    private ActivityTask activityTask1;
    private ActivityTask activityTask2;
    private ActivityTask activityTask3;
    private ActivityTask activityTask4;
    private ActivityTask activityTask5;
    private ArrayList<SubActivity> ArrayListOfSubActivities = null;
    private ArrayList<ActivityTask> ActivityTask_ArrayList = null;

    /***********************!!!!!!!!!!!!!!!!**********************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = DataBaseHelper.getInstance(getApplicationContext());
        wordPriority = new WordPriority();
        wordPriority.setTimeWords();
        final TabLayout tableLayout = findViewById(R.id.Tablayouting);
        final ViewPager2 viewPager = findViewById(R.id.ViewPager);
        viewPager.setAdapter(new customAdapter(getSupportFragmentManager(), getLifecycle()));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tableLayout, viewPager, true,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Home");
                            break;
                        case 1:
                            tab.setText("Word Management");
                            break;
                        case 2:
                            tab.setText("Group And Points");
                            break;
                        case 3:
                            tab.setText("ALL Reminders");
                            break;
                        case 4:
                            tab.setText("Shouldn't Display");
                            break;
                        default:
                            return;
                    }
                }
        );
        tabLayoutMediator.attach();
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //tests,can be deleted.
                ActivityTask atest=new ActivityTask(1,MasloCategorys.Esteem,Repetition.no_repeating,"this and that",null);
                Toast.makeText(getApplication(), "event launched", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        OpeningDialog opening = new OpeningDialog();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isFirstRun = prefs.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            opening.show(getFragmentManager(), "opening");
            isFirstRun = false;
            prefs.edit().putBoolean("isFirstRun", false).commit();
        }
    }

    public static boolean checkIfWordExist(String str, String word) {
        String regex = ".*?\\b(?i)(" + word + ")\\b.*";
        return str.matches(regex);
    }


    //region test
    public static class OpeningDialog extends DialogFragment {
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Recommended Bucket Words\n" +
                    "Bucket words are words that represent a time period, the system will search for them and take into account when they show up in a reminder and make use of the word dedicated time range\n" +
                    "They can overlap, so you have all the freedom in the world to make as many and as specific a time word as you wish! We do, however, recommend the following for a start:" +
                    "\nWakeup - A period of time dedicated to starting your day right! \nsleeping - Time at which you are getting ready to or is a sleep\nProductivity - When do you work to accomplish goals? a time period dedicated to accomplishments! \nHome - when you are or already is home. \nawake - when are you expected to be awake?")
                    .setPositiveButton("Ok thanks!", (DialogInterface.OnClickListener) (dialog, id) -> {
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    //endregion


}
