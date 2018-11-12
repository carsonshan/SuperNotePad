package com.fairhand.supernotepad.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 画笔属性展示点
 *
 * @author FairHand
 * @date 2018/11/7
 */
public class PointView extends View {
    
    /**
     * 点画笔
     */
    private Paint mPaint;
    
    /**
     * 画笔的粗细，透明度，颜色属性
     */
    public static int mStroke = 5;
    public static int mAlpha = 255;
    public static int mColor = Color.BLACK;
    
    public PointView(Context context) {
        super(context);
    }
    
    public PointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    private void init() {
        mPaint = new Paint();
        mPaint.setStrokeWidth(mStroke);
        mPaint.setAlpha(mAlpha);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
        // 设置边角连接处为圆
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        // 设置线条末端为圆
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPoint(getWidth() / 2, getHeight() / 2, mPaint);
    }
    
    /**
     * 设置画笔粗细
     */
    public void setPaintStroke(int stroke) {
        mStroke = stroke;
        mPaint.setStrokeWidth(stroke);
        mOnPaintStyleChange.strokeChanged(stroke);
        invalidate();
    }
    
    /**
     * 设置画笔透明度
     */
    public void setPaintAlpha(int alpha) {
        mAlpha = alpha;
        mPaint.setAlpha(alpha);
        mOnPaintStyleChange.alphaChanged(alpha);
        invalidate();
    }
    
    /**
     * 设置画笔颜色
     */
    public void setPaintColor(int color) {
        mColor = color;
        mPaint.setColor(color);
        mOnPaintStyleChange.colorChanged(color);
        invalidate();
    }
    
    private static OnPaintStyleChanged mOnPaintStyleChange;
    
    public static void setPaintStyleChanged(OnPaintStyleChanged onPaintStyleChange) {
        mOnPaintStyleChange = onPaintStyleChange;
    }
    
    /**
     * 回调画笔Paint改变属性
     */
    public interface OnPaintStyleChanged {
        /**
         * 颜色变化
         *
         * @param color 改变的颜色
         */
        void colorChanged(int color);
        
        /**
         * 透明度变化
         *
         * @param alpha 改变的透明度
         */
        void alphaChanged(int alpha);
        
        /**
         * 粗细变化
         *
         * @param stroke 改变的粗细
         */
        void strokeChanged(int stroke);
    }
    
}
