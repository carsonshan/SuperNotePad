package com.fairhand.supernotepad.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.fragment.GuideFragmentO;
import com.fairhand.supernotepad.fragment.GuideFragmentT;
import com.fairhand.supernotepad.fragment.GuideFragmentTh;
import com.fairhand.supernotepad.view.ViewPagerContainer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新手引导页
 *
 * @author Phanton - 2018/11/24 - Saturday
 */
public class GuideActivity extends AppCompatActivity {
    
    @BindView(R.id.view_pager_guide)
    ViewPager viewPagerGuide;
    
    private ArrayList<Fragment> fragments;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        
        setData();
    }
    
    private void setData() {
        fragments = new ArrayList<>();
        fragments.add(new GuideFragmentO());
        fragments.add(new GuideFragmentT());
        fragments.add(new GuideFragmentTh());
        
        viewPagerGuide.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }
            
            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        
        ViewPagerContainer viewPagerContainer = new ViewPagerContainer(this);
        viewPagerContainer.setBottomMargin(50);
        viewPagerContainer.setBottomWidth(400);
        viewPagerContainer.setPointRadius(15);
        viewPagerContainer.setViewPager(viewPagerGuide);
        viewPagerContainer.create();
    }
    
}
