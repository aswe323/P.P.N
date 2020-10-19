package com.example.rems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    /*private TabLayout tableLayout= findViewById(R.id.Tablayouting);
    private TabItem tabItem1=findViewById(R.id.Tabitem1);
    private TabItem tabItem2=findViewById(R.id.Tabitem2);
    private TabItem tabItem3=findViewById(R.id.Tabitem3);*/

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

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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
