package com.android.achievix.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.achievix.Adapter.ViewPagerAdapter;
import com.android.achievix.Fragment.AppLaunchesFragment;
import com.android.achievix.Fragment.InternetUsageFragment;
import com.android.achievix.Fragment.UsageOverviewFragment;
import com.android.achievix.R;
import com.google.android.material.tabs.TabLayout;

public class UsageOverviewActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage_overview);

        tabLayout = findViewById(R.id.stats_tab_layout);
        viewPager = findViewById(R.id.stats_fragment_container);

        UsageOverviewFragment usageOverviewFragment = new UsageOverviewFragment();
        AppLaunchesFragment appLaunchesFragment = new AppLaunchesFragment();
        InternetUsageFragment internetUsageFragment = new InternetUsageFragment();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(usageOverviewFragment, "Usage Overview");
        viewPagerAdapter.addFragment(appLaunchesFragment, "App Launches");
        viewPagerAdapter.addFragment(internetUsageFragment, "Internet Usage");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
