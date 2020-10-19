package com.example.rems;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    //https://www.youtube.com/watch?v=HHd-Fa3DCng&ab_channel=MasterCoding
    private int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Main_Activity_fragment();
            case 1:
                return new key_words_fragment();
            case 2:
                return new groups_and_points_fragment();
            case 3:
                return new Fragment_Past_Reminders();
            case 4:
                return new edit_reminder_fragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
