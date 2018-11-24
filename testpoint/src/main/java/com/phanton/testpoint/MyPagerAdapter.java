package com.phanton.testpoint;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 *
 * @author Android
 * @date 17/4/17
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> integers;


    public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> integers) {
        super(fm);
        this.integers = integers;
    }


    @Override
    public Fragment getItem(int position) {
        return integers.get(position);
    }

    @Override
    public int getCount() {
        return integers.size();
    }
    
}
