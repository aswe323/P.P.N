package com.example.rems;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.joestelmach.natty.*;


import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import com.joestelmach.natty.*;
import org.antlr.v4.runtime.tree.*;
import org.slf4j.impl.*;


import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import module.ActivityTask;
import module.ActivityTasksUsed;
import module.MasloCategorys;
import module.Repetition;
import module.SubActivity;
import module.WordPriority;

public class MainActivity extends AppCompatActivity {


    DataBaseHelper db;
    private WordPriority wordPriority;
    /***********************!!!!!!!!!!!!!!!!**********************/
    private SubActivity subActivity;
    private SubActivity subUpdateTester;
    private ActivityTask activityTask1;
    private ActivityTask activityTask2;
    private ActivityTask activityTask3;
    private ActivityTask activityTask4;
    private ActivityTask activityTask5;
    private ArrayList<SubActivity> ArrayListOfSubActivities=null;
    private ArrayList<ActivityTask> ActivityTask_ArrayList=null;
    /***********************!!!!!!!!!!!!!!!!**********************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = DataBaseHelper.getInstance(getApplicationContext());
        wordPriority = new WordPriority();
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
                String test="9:91-15:21";
                String[] splittest=test.split("-");
                Toast.makeText(getApplication(), "event launched", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public static boolean checkIfWordExist(String str, String word){
        String regex=".*?\\b(?i)("+word+")\\b.*";
        return  str.matches(regex);
    }
}
