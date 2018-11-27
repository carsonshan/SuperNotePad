package com.fairhand.supernotepad.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * 悬浮按钮滑动隐藏与显示
 *
 * @author Phanton
 * @date 11/26/2018 - Monday - 10:40 AM
 */
public class FloatingActionButtonBehavior extends FloatingActionButton.Behavior {
    
    public FloatingActionButtonBehavior(Context context, AttributeSet attrs) {
        super();
    }
    
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull FloatingActionButton child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        // 只处理垂直方向的滑动
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
                       || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }
    
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull FloatingActionButton child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        // 使用FloatingActionButton自带的动画
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            // 下滑隐藏
            child.hide();
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            // 上滑显示
            child.show();
        }
    }
    
}
