package com.example.rems;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

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


    }





}
