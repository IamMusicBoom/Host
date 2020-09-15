package com.optima.plugin.host.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.optima.plugin.host.R;
import com.optima.plugin.host.adapter.HomePagerAdapter;
import com.optima.plugin.host.fragment.CustomFragment;
import com.optima.plugin.host.fragment.FunctionFragment;
import com.optima.plugin.host.fragment.MessageFragment;
import com.optima.plugin.host.fragment.PendingFragment;
import com.optima.plugin.repluginlib.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * create by wma
 * on 2020/9/11 0011
 */
public class HomeActivity extends BaseActivity {
    ViewPager mViewPager;
    TabLayout mTabLayout;
    List<Fragment> mFragments = new ArrayList<>();
    HomePagerAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tab_layout);
        mFragments.add(new FunctionFragment());
        mFragments.add(new PendingFragment());
        mFragments.add(new MessageFragment());
        mFragments.add(new CustomFragment());
        mAdapter = new HomePagerAdapter(getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
