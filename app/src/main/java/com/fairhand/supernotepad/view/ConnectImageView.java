package com.fairhand.supernotepad.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.fairhand.supernotepad.util.Logger;

import java.util.ArrayList;

/**
 * 图片连接
 *
 * @author Phanton
 * @date 2018/11/10 - 星期六 - 11:53
 */
public class ConnectImageView extends AppCompatImageView {
    
    private Bitmap resultBitmap;
    
    public ConnectImageView(Context context) {
        super(context);
    }
    
    public ConnectImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public ConnectImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    private int mWidth;
    private int mHeight;
    
    /**
     * 纵向拼接图片
     */
    public void mergeBitmap(ArrayList<Bitmap> bitmaps, float width) {
        mWidth = (int) width;
        // 遍历获取总高度
        for (Bitmap item : bitmaps) {
            mHeight += item.getHeight();
        }
        // 创建空背景(就是拼接完成后的图)
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        
        // 遍历画Bitmap
        int top = 0;
        for (Bitmap item : bitmaps) {
            // 宽度不等，缩放以适应
            int w = item.getWidth();
            if (w != mWidth) {
                item = getNewBitmap(item, mWidth, w);
            }
            canvas.drawBitmap(item, 0, top, null);
            top += item.getHeight();
            Logger.d("top值：" + top);
        }
        // 全部画完，重绘界面
        resultBitmap = bitmap;
        invalidate();
    }
    
    /**
     * 获取新的符合标准的Bitmap
     */
    private Bitmap getNewBitmap(Bitmap bitmap, float newWidth, float width) {
        float scale = newWidth / width;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
    
    /**
     * 获取Canvas屏幕截图
     */
    public Bitmap loadBitmapFromView(View view) {
        if (view == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        view.draw(c);
        return screenshot;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (resultBitmap != null) {
            canvas.drawBitmap(resultBitmap, 0, 0, null);
        }
    }
    
    @Override
    public boolean performClick() {
        return super.performClick();
    }
    
}
