package com.fairhand.supernotepad.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.fairhand.supernotepad.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 绘图View
 *
 * @author FairHand
 * @date 2018/10/30
 */
public class DrawView extends View implements PointView.OnPaintStyleChanged {
    
    private Paint mPaint;
    private Path mPath;
    
    private Bitmap mBufferBitmap;
    private Canvas mBufferCanvas;
    
    /**
     * 最大保存步数
     */
    private static final int MAX_CACHE_STEP = 24;
    
    /**
     * 存放画的每一笔
     */
    private List<AbstractDrawingInfo> mDrawingList;
    /**
     * 存放移除的每一笔
     */
    private List<AbstractDrawingInfo> mRemovedList;
    
    private Xfermode mXfermodeClear;
    private Xfermode mXfermodeDraw;
    
    private boolean mCanEraser;
    
    private Callback mCallback;
    
    private GestureDetector mGestureDetector;
    
    public int mEraserSize = 5;
    
    private float mWidth;
    private float mHeight;
    
    /**
     * 模式
     */
    public enum MODE {
        /**
         * 绘图模式
         */
        DRAW,
        /**
         * 橡皮擦模式
         */
        ERASER,
        /**
         * 什么也不是
         */
        NULL
    }
    
    private MODE currentMode = MODE.DRAW;
    
    public DrawView(Context context) {
        super(context);
        init();
    }
    
    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mHeight = getHeight();
    }
    
    public interface Callback {
        /**
         * 单击监听的回调接口
         */
        void onSingleClicked();
    }
    
    /**
     * 设置回调
     */
    public void setCallback(Callback callback) {
        mCallback = callback;
    }
    
    /**
     * 初始化画笔
     */
    private void init() {
        // 设置Paint属性改变的回调接口
        PointView.setPaintStyleChanged(this);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        // 设置图像在动画进行中会滤掉对Bitmap图像的优化操作，加快显示
        mPaint.setFilterBitmap(true);
        // 设置边角连接处为圆
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        // 设置线条末端为圆
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        // 设置画笔粗细
        mPaint.setStrokeWidth(PointView.mStroke);
        // 设置画笔透明度
        mPaint.setAlpha(PointView.mAlpha);
        // 设置画笔颜色
        mPaint.setColor(PointView.mColor);
        // 抗锯齿
        mPaint.setAntiAlias(true);
        mXfermodeDraw = new PorterDuffXfermode(PorterDuff.Mode.SRC);
        mXfermodeClear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mPaint.setXfermode(mXfermodeDraw);
        
        // 设置手势监听
        mGestureDetector = new GestureDetector(getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    /**
                     * 单击
                     */
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        Logger.d("测试", "我单击了");
                        if (mCallback != null) {
                            mCallback.onSingleClicked();
                        }
                        return super.onSingleTapConfirmed(e);
                    }
                });
    }
    
    /**
     * 初始化二级缓冲
     */
    private void initBuffer() {
        mBufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);
    }
    
    /**
     * 设置背景图
     */
    public void setBuffer(String path) {
        mBufferBitmap = BitmapFactory.decodeFile(path).copy(Bitmap.Config.ARGB_8888, true);
        mBufferCanvas = new Canvas(mBufferBitmap);
        invalidate();
    }
    
    /**
     * 背景插图
     */
    private Bitmap backgroundBitmap;
    
    /**
     * 设置插图
     */
    public void setIllustration(String path) {
        backgroundBitmap = BitmapFactory.decodeFile(path).copy(Bitmap.Config.ARGB_8888, true);
        // 获取图片的宽高
        float width = backgroundBitmap.getWidth();
        float height = backgroundBitmap.getHeight();
        Logger.d("view" + mWidth + ", " + mHeight);
        Logger.d("bitmap" + width + ", " + height);
        // 计算缩放比例
        float scaleFactor = Math.min(mWidth / width, mHeight / height);
        Logger.d("缩放比例：" + scaleFactor);
        Matrix matrix = new Matrix();
        // 缩放图片
        matrix.postScale(scaleFactor, scaleFactor);
        // matrix.postTranslate(getPivotX() - 320, getPivotY() - 320);
        backgroundBitmap = Bitmap.createBitmap(backgroundBitmap, 0, 0, (int) width, (int) height, matrix, true);
        Logger.d("屏幕中心：" + getPivotX() + ", " + getPivotY());
        matrix.reset();
        invalidate();
    }
    
    /**
     * 贴纸
     */
    private Bitmap stickerBitmap;
    
    /**
     * 设置贴纸
     */
    public void setSticker(ImageView imageView) {
        stickerBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        invalidate();
    }
    
    /**
     * 保存总的所有的绘制路径信息
     */
    private abstract static class AbstractDrawingInfo {
        Paint paint;
        Path path;
        
        /**
         * 绘制
         *
         * @param canvas 画布
         */
        abstract void draw(Canvas canvas);
    }
    
    /**
     * 这是保存单条绘制信息
     */
    private static class PathAbstractDrawingInfo extends AbstractDrawingInfo {
        @Override
        void draw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }
    }
    
    /**
     * 设置当前模式
     */
    public void setMode(MODE mode) {
        if (mode != currentMode) {
            currentMode = mode;
            if (currentMode == MODE.DRAW) {
                // 设置画笔
                mPaint.setXfermode(mXfermodeDraw);
                mPaint.setStrokeWidth(PointView.mStroke);
            } else {
                // 设置橡皮擦
                mPaint.setXfermode(mXfermodeClear);
                mPaint.setStrokeWidth(mEraserSize);
            }
        }
    }
    
    /**
     * 判断是否能够重做
     */
    public boolean canRedo() {
        return mRemovedList != null && mRemovedList.size() > 0;
    }
    
    /**
     * 判断是否能够撤销
     */
    public boolean canUndo() {
        return mDrawingList != null && mDrawingList.size() > 0;
    }
    
    /**
     * 重做
     */
    public void redo() {
        int size = mRemovedList == null ? 0 : mRemovedList.size();
        if (size > 0) {
            AbstractDrawingInfo info = mRemovedList.remove(size - 1);
            mDrawingList.add(info);
            mCanEraser = true;
            reDraw();
        }
    }
    
    /**
     * 撤销
     */
    public void undo() {
        int size = mDrawingList == null ? 0 : mDrawingList.size();
        if (size > 0) {
            AbstractDrawingInfo info = mDrawingList.remove(size - 1);
            if (mRemovedList == null) {
                mRemovedList = new ArrayList<>(MAX_CACHE_STEP);
            }
            if (size == 1) {
                // 撤销后屏幕中没有图了，设置为不可擦除
                mCanEraser = false;
            }
            mRemovedList.add(info);
            reDraw();
        }
    }
    
    /**
     * 重绘，在撤销一步和恢复一步时用
     */
    private void reDraw() {
        if (mDrawingList != null) {
            // 把bitmap擦成透明的
            // 以便重绘
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            for (AbstractDrawingInfo drawingInfo : mDrawingList) {
                drawingInfo.draw(mBufferCanvas);
            }
            invalidate();
        }
    }
    
    /**
     * 清除画布
     */
    public void clear() {
        if (mBufferBitmap != null) {
            if (mDrawingList != null) {
                mDrawingList.clear();
            }
            if (mRemovedList != null) {
                mRemovedList.clear();
            }
            mCanEraser = false;
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            invalidate();
        }
    }
    
    /**
     * 保存绘制路径
     */
    private void saveDrawingPath() {
        if (mDrawingList == null) {
            mDrawingList = new ArrayList<>(MAX_CACHE_STEP);
        } else if (mDrawingList.size() == MAX_CACHE_STEP) {
            mDrawingList.remove(0);
        }
        // 保存这一笔的信息
        Path cachePath = new Path(mPath);
        Paint cachePaint = new Paint(mPaint);
        PathAbstractDrawingInfo info = new PathAbstractDrawingInfo();
        info.path = cachePath;
        info.paint = cachePaint;
        // 添加到绘制列表
        mDrawingList.add(info);
        mCanEraser = true;
    }
    
    /**
     * 设置橡皮擦大小
     */
    public void setEraserSize(int eraserSize) {
        mEraserSize = eraserSize;
        mPaint.setStrokeWidth(eraserSize);
    }
    
    /**
     * 获取Canvas屏幕截图
     */
    public Bitmap loadBitmapFromView(View view) {
        if (view == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap((int) mWidth, (int) mHeight, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        view.draw(c);
        return screenshot;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (backgroundBitmap != null) {
            canvas.drawBitmap(backgroundBitmap, 0, 320, null);
        }
        if (stickerBitmap != null) {
            canvas.drawBitmap(stickerBitmap, 320, 320, null);
        }
        if (mBufferBitmap != null) {
            canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        }
    }
    
    float startX, startY;
    
    @Override
    public boolean performClick() {
        return super.performClick();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 将手势监听传给Detector
        mGestureDetector.onTouchEvent(event);
        if (!isEnabled()) {
            return false;
        }
        final int action = event.getActionMasked();
        if (currentMode != MODE.NULL) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    if (mPath == null) {
                        mPath = new Path();
                    }
                    mPath.moveTo(startX, startY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float endX = event.getX();
                    float endY = event.getY();
                    // 这里终点设为两点的中心点的目的在于使绘制的曲线更平滑
                    // 如果终点直接设置为x,y，效果和lineto是一样的,实际是折线效果
                    mPath.quadTo(startX, startY, (startX + endX) / 2, (startY + endY) / 2);
                    if (mBufferBitmap == null) {
                        initBuffer();
                    }
                    // 擦除模式下，画布中没有图画，并且不可擦除，不做任何操作
                    if (currentMode == MODE.ERASER && !mCanEraser) {
                        break;
                    }
                    mBufferCanvas.drawPath(mPath, mPaint);
                    invalidate();
                    startX = endX;
                    startY = endY;
                    break;
                case MotionEvent.ACTION_UP:
                    performClick();
                    if (currentMode == MODE.DRAW || mCanEraser) {
                        saveDrawingPath();
                    }
                    mPath.reset();
                    break;
                default:
                    break;
            }
        }
        return true;
    }
    
    @Override
    public void colorChanged(int color) {
        mPaint.setColor(color);
    }
    
    @Override
    public void alphaChanged(int alpha) {
        mPaint.setAlpha(alpha);
    }
    
    @Override
    public void strokeChanged(int stroke) {
        mPaint.setStrokeWidth(stroke);
    }
    
}
