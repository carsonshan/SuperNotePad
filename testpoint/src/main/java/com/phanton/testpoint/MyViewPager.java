package com.phanton.testpoint;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author Android
 * @date 17/4/17
 */

public class MyViewPager extends FrameLayout implements ViewPager.OnPageChangeListener {
    
    private ViewPager viewPager;
    private ViewPagerPoint viewPagerPoint;
    
    //单次最大移动量
    private int maxOffset;
    //默认底部高度
    private int defaultBottomWidth = 400;
    //默认距离底部的距离
    private int defaultMarginBottom = 50;
    
    public MyViewPager(Context context) {
        super(context);
        init(context);
    }
    
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    public MyViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    
    private void init(Context context) {
        viewPagerPoint = new ViewPagerPoint(context);
    }
    
    public void setPointRadius(int radius) {
        viewPagerPoint.setCircleRadius(radius);
    }
    
    public void setBottomWidth(int bottomWidth) {
        defaultBottomWidth = bottomWidth;
    }
    
    public void setBottomMargin(int marginBottom) {
        this.defaultMarginBottom = marginBottom;
    }
    
    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }
    
    public void setOffSet(int offSet) {
        viewPagerPoint.setOffSet(offSet);
    }
    
    public void setSelectColor(int selectColor) {
        viewPagerPoint.setSelectColor(selectColor);
    }
    
    public void setNormalColor(int normalColor) {
        viewPagerPoint.setNormalColor(normalColor);
    }
    
    public void create() {
        initViewPager();
    }
    
    //初始化ViewPager
    private void initViewPager() {
        if (viewPager == null) {
            return;
        }
        ViewGroup viewParent = ((ViewGroup) viewPager.getParent());
        if (viewParent != null) {
            viewParent.removeView(viewPager);
            viewParent.addView(this);
        }
        addView(viewPager);
        viewPager.addOnPageChangeListener(this);
        PagerAdapter pagerAdapter = viewPager.getAdapter();
        if (pagerAdapter != null) {
            int count = pagerAdapter.getCount();
            viewPagerPoint.setCount(count);
        }
        LayoutParams layoutParams = new LayoutParams(defaultBottomWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        layoutParams.bottomMargin = defaultMarginBottom;
        addView(viewPagerPoint, layoutParams);
    }
    
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        
        if (positionOffsetPixels > 0) {
            viewPagerPoint.setTranslateX(positionOffsetPixels + maxOffset * position);
        }
        if (positionOffsetPixels > maxOffset) {
            maxOffset = positionOffsetPixels;
        }
    }
    
    @Override
    public void onPageSelected(int position) {
    
    }
    
    @Override
    public void onPageScrollStateChanged(int state) {
        //state ==1的时表示正在滑动，state==2的时表示滑动完毕了，state==0的时表示什么都没做
    }
}
