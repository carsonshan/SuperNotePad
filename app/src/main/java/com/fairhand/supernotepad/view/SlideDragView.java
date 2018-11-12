package com.fairhand.supernotepad.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.fairhand.supernotepad.util.Logger;

/**
 * 侧滑View
 *
 * @author FairHand
 * @date 2018/11/1
 */
public class SlideDragView extends FrameLayout {
    
    private ViewDragHelper mDragHelper;
    
    /**
     * 侧滑View，MainView
     */
    private View mLeftView, mMainView;
    
    /**
     * 屏幕宽度，高度(也是屏幕的高宽)
     */
    private int mWidth, mHeight;
    
    public SlideDragView(@NonNull Context context) {
        super(context);
        // 通过ViewDragHelper的静态工厂方法进行初始化
        mDragHelper = ViewDragHelper.create(this, callback);
    }
    
    public SlideDragView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 通过ViewDragHelper的静态工厂方法进行初始化
        mDragHelper = ViewDragHelper.create(this, callback);
    }
    
    public SlideDragView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 通过ViewDragHelper的静态工厂方法进行初始化
        mDragHelper = ViewDragHelper.create(this, callback);
    }
    
    /**
     * 加载完布局回调
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 获取到侧滑View
        mLeftView = getChildAt(0);
        // 获取到MainView
        mMainView = getChildAt(1);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 获取到子控件的高宽
        mWidth = mMainView.getMeasuredWidth();
        mHeight = mMainView.getMeasuredHeight();
    }
    
    /**
     * 安顿好子View的位置
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // mLeftView放在负一屏中
        mLeftView.layout(-mWidth, 0, 0, mHeight);
        // mMainView放在主屏中
        mMainView.layout(0, 0, mWidth, mHeight);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 由viewDragHelper 来判断是否应该拦截此事件
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        // 将触摸事件传递给ViewDragHelper来解析处理，此操作必不可少
        mDragHelper.processTouchEvent(event);
        // 消费掉此事件，自己来处理
        return true;
    }
    
    @Override
    public boolean performClick() {
        return super.performClick();
    }
    
    /**
     * 返回主视图
     */
    public void backMainView() {
        // mLeftView放在负一屏中
        mLeftView.layout(-mWidth, 0, 0, mHeight);
        mMainView.layout(0, 0, mWidth, mHeight);
    }
    
    /**
     * ViewDragHelper回调
     */
    private ViewDragHelper.Callback callback =
            new ViewDragHelper.Callback() {
                
                /**
                 * 何时开始检测触摸事件
                 */
                @Override
                public boolean tryCaptureView(@NonNull View view, int i) {
                    // 返回true响应任何子View的触摸事件
                    return true;
                }
                
                /**
                 * 滑动位置改变时监听
                 */
                @Override
                public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                    super.onViewPositionChanged(changedView, left, top, dx, dy);
                    if (changedView == mMainView) {
                        // 滑动mMainView的同时滑动mLeftView
                        mLeftView.offsetLeftAndRight(dx);
                    } else if (changedView == mLeftView) {
                        // 滑动mLeftView的同时滑动mMainView
                        mMainView.offsetLeftAndRight(dx);
                    }
                }
                
                /**
                 * 拖动结束后调用
                 */
                @Override
                public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                    super.onViewReleased(releasedChild, xvel, yvel);
                    // 阈值，判断拖动出的距离有没有子View的一半
                    final int validSize = 2;
                    
                    if (releasedChild == mMainView) {
                        Logger.d("此时View为MainView");
                        if (mMainView.getLeft() < mWidth / validSize) {
                            // 将mMainView弹回去
                            mDragHelper.smoothSlideViewTo(mMainView, 0, 0);
                        } else {
                            // 将mLeftView弹出来
                            mDragHelper.smoothSlideViewTo(mMainView, mWidth, 0);
                        }
                    } else if (releasedChild == mLeftView) {
                        Logger.d("此时View为侧滑View");
                        if (-mLeftView.getLeft() < mWidth / validSize) {
                            // 将mLeftView弹回去
                            mDragHelper.smoothSlideViewTo(mLeftView, 0, 0);
                        } else {
                            // 将mMainView弹出来
                            mDragHelper.smoothSlideViewTo(mLeftView, -mWidth, 0);
                        }
                    }
                    ViewCompat.postInvalidateOnAnimation(SlideDragView.this);
                }
                
                /**
                 * 水平滑动
                 */
                @Override
                public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                    // 处理滑动边界
                    // mLeftView(-mWidth~0)
                    if (child == mLeftView) {
                        if (left < -mWidth) {
                            left = -mWidth;
                        } else if (left > 0) {
                            left = 0;
                        }
                    }
                    // mMainView(0~mWidth)
                    if (child == mMainView) {
                        if (left < 0) {
                            left = 0;
                        } else if (left > mWidth) {
                            left = mWidth;
                        }
                    }
                    return left;
                }
                
                /**
                 * 垂直滑动
                 */
                @Override
                public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                    // 禁止垂直滑动
                    return 0;
                }
            };
    
    @Override
    public void computeScroll() {
        // continueSettling()方法判断是否结束
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
    
}
