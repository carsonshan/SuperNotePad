package com.fairhand.supernotepad.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author Phanton
 * @date 11/24/2018 - Saturday - 3:48 PM
 */
public class ViewPagerContainer extends FrameLayout implements ViewPager.OnPageChangeListener {
    
    private ViewPager viewPager;
    private StickerPoint stickerPoint;
    
    //单次最大移动量
    private int maxOffset;
    //默认底部高度
    private int defaultBottomWidth = 400;
    //默认距离底部的距离
    private int defaultMarginBottom = 50;
    
    public ViewPagerContainer(Context context) {
        super(context);
        init(context);
    }
    
    public ViewPagerContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    public ViewPagerContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    
    private void init(Context context) {
        stickerPoint = new StickerPoint(context);
    }
    
    public void setPointRadius(int radius) {
        stickerPoint.setCircleRadius(radius);
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
        stickerPoint.setOffSet(offSet);
    }
    
    public void setSelectColor(int selectColor) {
        stickerPoint.setSelectColor(selectColor);
    }
    
    public void setNormalColor(int normalColor) {
        stickerPoint.setNormalColor(normalColor);
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
            stickerPoint.setCount(count);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(defaultBottomWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        layoutParams.bottomMargin = defaultMarginBottom;
        addView(stickerPoint, layoutParams);
    }
    
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        
        if (positionOffsetPixels > 0) {
            stickerPoint.setTranslateX(positionOffsetPixels + maxOffset * position);
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
