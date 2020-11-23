package com.example.rems;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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

    /*private TabLayout tableLayout= findViewById(R.id.Tablayouting);
    private TabItem tabItem1=findViewById(R.id.Tabitem1);
    private TabItem tabItem2=findViewById(R.id.Tabitem2);
    private TabItem tabItem3=findViewById(R.id.Tabitem3);*/
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
        TabItem tabItem1 = findViewById(R.id.Tabitem1);
        TabItem tabItem2 = findViewById(R.id.Tabitem2);
        TabItem tabItem3 = findViewById(R.id.Tabitem3);


        final ViewPager2 viewPager = findViewById(R.id.ViewPager);

        viewPager.setAdapter(new customAdapter(getSupportFragmentManager(), getLifecycle()));

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tableLayout, viewPager, true,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
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
                                tab.setText("past Reminders");
                                break;
                            case 4:
                                tab.setText("ALL Reminders");
                                break;
                            default:
                                return;
                        }
                    }
                }
        );
        tabLayoutMediator.attach();

        /***********************!!!!!!!!!!!!!!!!**********************/
        //SubActivity subActivity=new SubActivity(DataBaseHelper.getMaxIdOfActivityTask())
        /***********************!!!!!!!!!!!!!!!!**********************/
        //region legacy viewpager integration


        //region ViewPager and PagerAdapter instantiation
        /*final ViewPager viewPager = findViewById(R.id.ViewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tableLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);*/
        //endregion
        //region event management (onTabSelected)
        /*
        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        */
        //endregion
        //region general testing
        /*
                //check 1 insert
                if(tableLayout.getSelectedTabPosition()==1){
                    DataBaseHelper db=DataBaseHelper.getInstance(getApplicationContext());
                    //everybody will have id 1 when using getMaxIdOfActivityTask() because we didn't added the activitytask yet into the database
                    activityTask1=new ActivityTask(1,20,MasloCategorys.Esteem,Repetition.no_repeating,"testing 1234",LocalDateTime.of(2020,6,14,12,25),null);
                    activityTask2=new ActivityTask(2,69,MasloCategorys.none,Repetition.every_24_hours,"1234 test",LocalDateTime.of(2020,10,10,22,8),null);
                    activityTask3=new ActivityTask(3,420,MasloCategorys.Physiological_needs,Repetition.every_month,"help to paint the wall",LocalDateTime.of(2020,4,20,0,10),null);
                    activityTask4=new ActivityTask(4,37,MasloCategorys.none,Repetition.no_repeating,"testing 1234",LocalDateTime.of(2021,3,22,9,45),null);
                    activityTask5=new ActivityTask(5,20,MasloCategorys.Love_And_Belonging,Repetition.no_repeating,"i need to eat the icecreame",LocalDateTime.of(2020,12,25,16,0),null);
                    ActivityTask_ArrayList=new ArrayList<>();
                    ActivityTask_ArrayList.add(activityTask1);
                    ActivityTask_ArrayList.add(activityTask2);
                    ActivityTask_ArrayList.add(activityTask3);
                    ActivityTask_ArrayList.add(activityTask4);
                    ActivityTask_ArrayList.add(activityTask5);
                    for (ActivityTask activitytaskiterator:ActivityTask_ArrayList)
                        if(ActivityTasksUsed.addActivityTask(activitytaskiterator))
                            Toast.makeText(MainActivity.this, "inserted Activity with ID: "+activitytaskiterator.getActivityTaskID(), Toast.LENGTH_SHORT).show();
                    if(ActivityTasksUsed.addActivityTask(activityTask1))
                        Toast.makeText(MainActivity.this, "inserted Activity with ID: "+activityTask1.getActivityTaskID(), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "the Activity with ID: "+activityTask1.getActivityTaskID()+" is already existing in the fricking db", Toast.LENGTH_SHORT).show();
                }
                //check 2,query to get the map
                if(tableLayout.getSelectedTabPosition()==2){

                }
                //check 3,update the priority
                if(tableLayout.getSelectedTabPosition()==3){

                }
                //check 4,update the word
                if(tableLayout.getSelectedTabPosition()==0){

                }
                //check 5,delete the word


                if(tableLayout.getSelectedTabPosition()==1) {
                    db.insertPriorityWord("test1", 100);
                    db.insertPriorityWord("test2", 200);
                    db.insertPriorityWord("test3", 50);
                }

                //Toast.makeText(MainActivity.this, ""+tableLayout.getSelectedTabPosition(), Toast.LENGTH_SHORT).show();*/
        //endregion


        //endregion
    }



}
