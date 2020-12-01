package com.example.rems;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class customAdapter extends FragmentStateAdapter { //control the viewPager2


    public customAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Main_Activity_fragment();
            case 1:
                return new key_words_fragment();
            case 2:
                return new groups_and_points_fragment();
            case 3:
                return new RemindersCollection();
            case 4:
                return new edit_reminder_fragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
