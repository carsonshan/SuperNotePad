package com.phanton.testpoint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Phanton
 * @date 11/24/2018 - Saturday - 1:42 PM
 */
public class BesselView extends View {
    
    private Paint mLeftPaint;
    private Paint mRightPaint;
    
    Path leftPath;
    Path rightPath;
    
    //点集合
    ArrayList<Point> mLeftPoints;
    ArrayList<Point> mRightPoints;
    
    //波浪的宽度和高度
    float waveWidth;
    float waveHeight;
    
    //view的宽度和高度
    float viewWidth;
    float viewHeight;
    
    //基准线-高度的一半
    float levelHeight;
    
    //移动的总距离
    float leftTotalLen;
    float rightTotalLen;
    //移动一次的距离
    float leftMoveLen = 1.2f;
    float rightMoveLen = 0.8f;
    //移动间隔时间-越小越快
    private long speed = 7;
    
    private boolean isMeasured = false;
    
    private Task mTask;
    private Timer mTimer;
    
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            
            leftTotalLen += leftMoveLen;
            
            if (leftTotalLen > waveWidth) {
                leftTotalLen = 0;
            }
            
            rightTotalLen += rightMoveLen;
            if (rightTotalLen > waveWidth) {
                rightTotalLen = 0;
            }
            
            invalidate();
        }
    };
    
    
    public BesselView(Context context) {
        super(context);
        init();
    }
    
    
    public BesselView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public BesselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        mLeftPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLeftPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLeftPaint.setColor(Color.parseColor("#ffffffff"));
        
        mRightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRightPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mRightPaint.setColor(Color.parseColor("#88ffffff"));
        
        leftPath = new Path();
        rightPath = new Path();
        
        mLeftPoints = new ArrayList<Point>();
        mRightPoints = new ArrayList<Point>();
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!isMeasured) {
            isMeasured = true;
            viewHeight = getMeasuredHeight();
            viewWidth = getMeasuredWidth();
            
            levelHeight = viewHeight / 2;
            
            //波的宽度是view的四倍  就是只能看到1/4的波
            waveWidth = viewWidth*1.0f;
            //波的高度是view高度的 1/10;
            waveHeight = levelHeight / 3f;
            
            System.out.println("lh " + levelHeight + "wh " + waveHeight + "ww " + waveWidth);
            
            float x = 0;
            float y = 0;
            for (int i = 1; i <= 9; i++) {
                
                //y的计算
                if (i % 2 == 1) {
                    y = levelHeight;
                } else {
                    if (i % 4 == 0) {
                        y = levelHeight + waveHeight * 2;
                    } else {
                        y = levelHeight - waveHeight * 2;
                    }
                }
                
                //x的计算
                x = waveWidth / 4 * (i - 1);
                
                mLeftPoints.add(new Point(x, y));
            }
            
            //另一个
            for (Point point : mLeftPoints) {
                Point rightPoint = new Point(point.x-waveWidth, point.y);
                mRightPoints.add(rightPoint);
            }
            
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        
        leftPath.reset();
        leftPath.moveTo(mLeftPoints.get(0).x - leftTotalLen, levelHeight + waveHeight * 3);
        leftPath.lineTo(mLeftPoints.get(0).x - leftTotalLen, levelHeight);
        for (int i = 0; i < 4; i++) {
            leftPath.quadTo(mLeftPoints.get(1 + 2 * i).x - leftTotalLen, mLeftPoints.get(1 + 2 * i).y,
                    mLeftPoints.get(2 + 2 * i).x - leftTotalLen, mLeftPoints.get(2 + 2 * i).y);
            
        }
        leftPath.lineTo(mLeftPoints.get(8).x - leftTotalLen, levelHeight + waveHeight * 3);
        leftPath.close();
        canvas.drawPath(leftPath, mLeftPaint);
        
        
        rightPath.reset();
        rightPath.moveTo(mRightPoints.get(0).x + rightTotalLen, levelHeight + waveHeight * 3);
        rightPath.lineTo(mRightPoints.get(0).x + rightTotalLen, levelHeight);
        for (int i = 0; i < 4; i++) {
            rightPath.quadTo(mRightPoints.get(1 + 2 * i).x + rightTotalLen, mRightPoints.get(1 + 2 * i).y,
                    mRightPoints.get(2 + 2 * i).x + rightTotalLen, mRightPoints.get(2 + 2 * i).y);
            
        }
        rightPath.lineTo(mRightPoints.get(8).x + rightTotalLen, levelHeight + waveHeight * 3);
        rightPath.close();
        canvas.drawPath(rightPath, mRightPaint);
        
    }
    
    
    /**
     * 开始动画
     * 务必stopFollow()
     */
    public void startMove() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        
        mTimer = new Timer();
        mTask = new Task(handler);
        mTimer.schedule(mTask, 0, speed);
    }
    
    public void stopMove() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
    }
    
    
    
    class Task extends TimerTask {
        
        Handler mHandler;
        
        public Task(Handler handler) {
            mHandler = handler;
        }
        
        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }
    
    class Point {
        private float x;
        private float y;
        
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
        
    }
    
}
