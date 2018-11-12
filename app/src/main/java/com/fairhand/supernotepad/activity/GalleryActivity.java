package com.fairhand.supernotepad.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.adapter.PictureAdapter;
import com.fairhand.supernotepad.fragment.GalleryFragment;

import java.util.ArrayList;

/**
 * 浏览已选图片
 *
 * @author FairHand
 * @date 2018/11/2
 */
public class GalleryActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        
        ViewPager mViewPager = findViewById(R.id.view_pager_gallery);
        
        Intent intent = getIntent();
        // 获取传递过来的所有已选图片数据
        ArrayList<String> data = intent.getStringArrayListExtra(PictureAdapter.KEY_ALL_PICK_IMAGE);
        // 获取传递过来的点击位置
        final int position = intent.getIntExtra(PictureAdapter.KEY_PICK_POSITION, 0);
        
        // 初始化Fragment
        final ArrayList<Fragment> fragments = new ArrayList<>();
        for (String path : data) {
            fragments.add(GalleryFragment.newInstance(path));
        }
        
        // 设置ViewPager适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
            
            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        
        // 设置当前位置为点击的位置
        mViewPager.setCurrentItem(position);
        
    }
    
}
