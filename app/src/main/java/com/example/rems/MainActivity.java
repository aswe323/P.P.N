package com.example.rems;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

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

public class MainActivity extends AppCompatActivity {

    /*private TabLayout tableLayout= findViewById(R.id.Tablayouting);
    private TabItem tabItem1=findViewById(R.id.Tabitem1);
    private TabItem tabItem2=findViewById(R.id.Tabitem2);
    private TabItem tabItem3=findViewById(R.id.Tabitem3);*/
    /***********************!!!!!!!!!!!!!!!!**********************/
    private SubActivity subActivity;
    private SubActivity subUpdateTester;
    private ActivityTask activityTask;
    private ArrayList<SubActivity> ArrayListOfSubActivities=null;
    private ArrayList<ActivityTask> ActivityTask_ArrayList=null;
    /***********************!!!!!!!!!!!!!!!!**********************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TabLayout tableLayout = findViewById(R.id.Tablayouting);
        TabItem tabItem1 = findViewById(R.id.Tabitem1);
        TabItem tabItem2 = findViewById(R.id.Tabitem2);
        TabItem tabItem3 = findViewById(R.id.Tabitem3);
        final ViewPager viewPager = findViewById(R.id.ViewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tableLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        /***********************!!!!!!!!!!!!!!!!**********************/
        //ActivityTasksUsed activityTasksUsed=new ActivityTasksUsed();
        //SubActivity subActivity=new SubActivity(DataBaseHelper.getMaxIdOfActivityTask())
        /***********************!!!!!!!!!!!!!!!!**********************/

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                //check 1 insert
                if(tableLayout.getSelectedTabPosition()==1){
                    DataBaseHelper db=DataBaseHelper.getInstance(getApplicationContext());
                    activityTask=new ActivityTask(db.getMaxIdOfActivityTask()+1,20,MasloCategorys.Esteem,Repetition.no_repeating,"testing 1234",LocalDateTime.of(2020,6,14,12,25),null);
                    if(ActivityTasksUsed.addActivityTask(activityTask))
                        Toast.makeText(MainActivity.this, "inserted this shit", Toast.LENGTH_SHORT).show();
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


                /*if(tableLayout.getSelectedTabPosition()==1) {
                    db.insertPriorityWord("test1", 100);
                    db.insertPriorityWord("test2", 200);
                    db.insertPriorityWord("test3", 50);
                }*/

                //Toast.makeText(MainActivity.this, ""+tableLayout.getSelectedTabPosition(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
