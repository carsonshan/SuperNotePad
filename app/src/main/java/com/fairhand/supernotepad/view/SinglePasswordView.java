package com.fairhand.supernotepad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 单个密码位
 *
 * @author Phanton
 * @date 2018/11/10 - 星期六 - 20:34
 */
public class SinglePasswordView extends View {
    
    private Paint mPaint;
    private int paintColor = Color.BLACK;
    
    public SinglePasswordView(Context context) {
        this(context, null);
    }
    
    public SinglePasswordView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public SinglePasswordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        mPaint = new Paint();
        mPaint.setColor(paintColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取宽-测量规则的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        // 获取高-测量规则的模式和大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // 设置wrap_content的默认宽 / 高值
        // 默认宽/高的设定无固定依据,根据需要灵活设置
        int mWidth = 36;
        int mHeight = 36;
        // 当模式是AT_MOST（即wrap_content）时设置默认值
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
            // 宽 / 高任意一个模式为AT_MOST（即wrap_content）时，都设置默认值
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, mHeight);
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 16, mPaint);
    }
    
    public void setPaintColor(int color) {
        paintColor = color;
        mPaint.setColor(paintColor);
        invalidate();
    }
    
    public void setStrokeStyle() {
        mPaint.setStyle(Paint.Style.STROKE);
        invalidate();
    }
    
    public void setFillStyle() {
        mPaint.setStyle(Paint.Style.FILL);
        invalidate();
    }
    
}
