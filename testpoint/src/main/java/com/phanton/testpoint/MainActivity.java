package com.phanton.testpoint;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    
    ViewPager viewPager;
    
    private MyViewPager myViewPager;
    
    private ArrayList<Fragment> fragments;
    private MyPagerAdapter pagerAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        viewPager = findViewById(R.id.viewpager);
        
        fragments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            MyFragment fragment = new MyFragment();
            fragments.add(fragment);
        }
        
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        
        myViewPager = new MyViewPager(this);
        myViewPager.setBottomMargin(50);
        myViewPager.setBottomWidth(400);
        myViewPager.setPointRadius(15);
        myViewPager.setViewPager(viewPager);
        
        myViewPager.create();
    }
    
    
}
