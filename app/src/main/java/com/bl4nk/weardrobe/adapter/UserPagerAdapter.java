package com.bl4nk.weardrobe.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bl4nk.weardrobe.fragment.Closet;
import com.bl4nk.weardrobe.fragment.Home;
import com.bl4nk.weardrobe.fragment.MainCloset;
import com.bl4nk.weardrobe.fragment.Recomendation;

public class UserPagerAdapter extends FragmentStateAdapter {

    private static final int TAB_ITEM_SIZE = 3;

    public UserPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Home();
            case 1:
                return new Recomendation();
            case 2:
                return new MainCloset();
            default:
                return new Home();
        }
    }

    @Override
    public int getItemCount() {
        return TAB_ITEM_SIZE;
    }
}
