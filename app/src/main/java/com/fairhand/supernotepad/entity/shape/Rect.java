package com.fairhand.supernotepad.entity.shape;

import android.graphics.PointF;

/**
 * 矩形
 *
 * @author Phanton
 * @date 11/27/2018 - Tuesday - 8:36 PM
 */
public class Rect {
    
    /**
     * 手指的初始位置
     */
    private PointF mOrigin;
    
    /**
     * 手指的当前位置
     */
    private PointF mCurrent;
    
    public Rect(PointF origin) {
        mOrigin = origin;
        mCurrent = origin;
    }
    
    public PointF getmCurrent() {
        return mCurrent;
    }
    
    public void setmCurrent(PointF mCurrent) {
        this.mCurrent = mCurrent;
    }
    
    public PointF getmOrigin() {
        return mOrigin;
    }
    
}
