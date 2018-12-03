package com.fairhand.supernotepad.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.fairhand.supernotepad.R;

/**
 * 大波浪
 *
 * @author Phanton
 * @date 11/27/2018 - Tuesday - 9:40 PM
 */
public class BigWaveHaHaHaView extends View {
    
    private DrawFilter mDrawFilter;
    
    /**
     * 主波浪的画笔
     */
    private Paint mPrimeWavePaint;
    
    private int width;
    /**
     * 基线(波浪的高度)
     */
    private int baseLine;
    /**
     * 波长
     */
    private int waveWidth;
    /**
     * X轴偏移量
     */
    private float offsetX;
    /**
     * 波浪起伏的最大高度
     */
    private int rippleHeight = 36;
    
    public BigWaveHaHaHaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        
        mPrimeWavePaint = new Paint();
        mPrimeWavePaint.setColor(ContextCompat.getColor(context, R.color.colorItem));
        mPrimeWavePaint.setAntiAlias(true);
        mPrimeWavePaint.setStyle(Paint.Style.FILL);
    }
    
    private void startAnimation() {
        // 设置一个波长的偏移
        ValueAnimator mValueAnimator = ValueAnimator.ofFloat(0, waveWidth);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(animation -> {
            // 不断的设置X轴偏移量
            offsetX = (float) animation.getAnimatedValue();
            invalidate();
        });
        mValueAnimator.setDuration(3600);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.start();
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
        int mWidth = getResources().getDisplayMetrics().widthPixels;
        int mHeight = (int) (getResources().getDisplayMetrics().heightPixels / 2.4);
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        waveWidth = w;
        baseLine = h - rippleHeight;
        startAnimation();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除抗锯齿
        canvas.setDrawFilter(mDrawFilter);
        canvas.drawPath(getWavePath(), mPrimeWavePaint);
    }
    
    /**
     * 获取波浪的路径
     */
    private Path getWavePath() {
        Path mPath = new Path();
        int startPosition = -3;
        int endPosition = 2;
        // 半个波长的长度
        int halfWaveWidth = waveWidth / 2;
        // 将点移动到起始坐标(即贝塞尔曲线起始点)
        mPath.moveTo(-halfWaveWidth * 3, baseLine);
        // for循环绘制半个波长(总共画两个半波浪，从屏幕左侧画起)
        for (int i = startPosition; i < endPosition; i++) {
            int startX = i * halfWaveWidth;
            // 绘制贝塞尔曲线(半个波浪)
            mPath.quadTo(
                    startX + halfWaveWidth / 2 + offsetX,
                    getWaveHeight(i),
                    startX + halfWaveWidth + offsetX,
                    baseLine);
        }
        // 连接成一个封闭区域
        mPath.lineTo(width, 0);
        mPath.lineTo(0, 0);
        mPath.close();
        return mPath;
    }
    
    /**
     * 获取每半个波浪的高度
     */
    private int getWaveHeight(int num) {
        int factor = 2;
        if (num % factor == 0) {
            return baseLine + rippleHeight;
        }
        return baseLine - rippleHeight;
    }
    
}
