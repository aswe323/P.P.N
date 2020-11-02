package com.example.rems;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
        final DataBaseHelper db = DataBaseHelper.getInstance(this);
        //SubActivity subActivity=new SubActivity(DataBaseHelper.getMaxIdOfActivityTask())
        /***********************!!!!!!!!!!!!!!!!**********************/

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                //check 1 insert
                if(tableLayout.getSelectedTabPosition()==1){
                    LocalDateTime date =LocalDateTime.of(2021,1,24,10,41,37);
                    //subUpdateTester=ArrayListOfSubActivities.get(1);
                    ActivityTask_ArrayList=db.queryForExactActivityTask(0,999,null,null,Repetition.no_repeating,null);
                    Toast.makeText(MainActivity.this, "Got ActivityTask List", Toast.LENGTH_SHORT).show();
                }
                //check 2,query to get the map
                if(tableLayout.getSelectedTabPosition()==2){
                    //db.deleteSubActivity(subUpdateTester);
                    for(ActivityTask task : ActivityTask_ArrayList)
                        Toast.makeText(MainActivity.this, "ID "+task.getActivityTaskID()+"\ncontent "+task.getContent()+"\nCategory "+task.getCategory()+"\nPriority "+task.getPriority()+"\nRepetition"+task.getRepetition()+"\ntime"+task.getTimeOfActivity(), Toast.LENGTH_SHORT).show();
                }
                //check 3,update the priority
                if(tableLayout.getSelectedTabPosition()==3){
                    Toast.makeText(MainActivity.this, "ID "+ActivityTask_ArrayList.get(0).getActivityTaskID()+"\ncontent "+ActivityTask_ArrayList.get(0).getContent()+"\nCategory "+ActivityTask_ArrayList.get(0).getCategory()+"\nPriority "+ActivityTask_ArrayList.get(0).getPriority()+"\nRepetition"+ActivityTask_ArrayList.get(0).getRepetition()+"\ntime"+ActivityTask_ArrayList.get(0).getTimeOfActivity(), Toast.LENGTH_SHORT).show();
                }
                //check 4,update the word
                if(tableLayout.getSelectedTabPosition()==0){
                    db.deleteActivityTask(2);
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
