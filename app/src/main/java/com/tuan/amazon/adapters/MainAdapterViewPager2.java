package com.tuan.amazon.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tuan.amazon.fragments.GoiYKetBanFragment;
import com.tuan.amazon.fragments.HomeFragment;
import com.tuan.amazon.fragments.OtherFragment;

public class MainAdapterViewPager2 extends FragmentStateAdapter {

    public MainAdapterViewPager2(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new GoiYKetBanFragment();
            case 2:
                return new OtherFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
