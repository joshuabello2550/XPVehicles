package com.example.xpvehicles.interfaces;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public interface IndicatorDots {

    default void setViewPagerIndicatorDots(TabLayout tabLayout, ViewPager2 viewPager) {
        //indicator dots at the bottom of a viewPager
        TabLayoutMediator tabLayoutMediator =
                new TabLayoutMediator(tabLayout, viewPager, true,
                        new TabLayoutMediator.TabConfigurationStrategy() {
                            @Override public void onConfigureTab(
                                    @NonNull TabLayout.Tab tab, int position) { }
                        }
                );
        tabLayoutMediator.attach();
    }
}
