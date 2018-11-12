package com.fairhand.supernotepad.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.fairhand.supernotepad.R;
import com.xw.repo.BubbleSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 自定义画笔属性选择对话框
 *
 * @author FairHand
 * @date 2018/11/7
 */
public class DiyPenDialog extends Dialog implements View.OnClickListener {
    
    @BindView(R.id.pointView)
    PointView pointView;
    @BindView(R.id.bubble_seek_bar_stroke)
    BubbleSeekBar bubbleSeekBarStroke;
    @BindView(R.id.bubble_seek_bar_alpha)
    BubbleSeekBar bubbleSeekBarAlpha;
    @BindView(R.id.pen_color_black)
    View penColorBlack;
    @BindView(R.id.pen_color_red)
    View penColorRed;
    @BindView(R.id.pen_color_blue)
    View penColorBlue;
    @BindView(R.id.pen_color_purple)
    View penColorPurple;
    @BindView(R.id.pen_color_yellow)
    View penColorYellow;
    @BindView(R.id.pen_color_green)
    View penColorGreen;
    
    private Context context;
    
    public DiyPenDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_diy_pen);
        ButterKnife.bind(this);
        init();
    }
    
    private void init() {
        // 设置为当前粗细
        bubbleSeekBarStroke.setProgress(PointView.mStroke);
        // 监听粗细拖动
        bubbleSeekBarStroke.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser);
                pointView.setPaintStroke(progress);
            }
        });
        
        // 设置为当前透明度
        bubbleSeekBarAlpha.setProgress(PointView.mAlpha);
        // 监听透明度拖动
        bubbleSeekBarAlpha.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser);
                pointView.setPaintAlpha(progress);
            }
        });
        
        penColorBlack.setOnClickListener(this);
        penColorRed.setOnClickListener(this);
        penColorBlue.setOnClickListener(this);
        penColorPurple.setOnClickListener(this);
        penColorYellow.setOnClickListener(this);
        penColorGreen.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 黑笔
            case R.id.pen_color_black:
                pointView.setPaintColor(ContextCompat.getColor(context, R.color.colorBlackPen));
                break;
            // 红笔
            case R.id.pen_color_red:
                pointView.setPaintColor(ContextCompat.getColor(context, R.color.colorRedPen));
                break;
            // 蓝笔
            case R.id.pen_color_blue:
                pointView.setPaintColor(ContextCompat.getColor(context, R.color.colorBluePen));
                break;
            // 紫笔
            case R.id.pen_color_purple:
                pointView.setPaintColor(ContextCompat.getColor(context, R.color.colorPurplePen));
                break;
            // 黄笔
            case R.id.pen_color_yellow:
                pointView.setPaintColor(ContextCompat.getColor(context, R.color.colorYellowPen));
                break;
            // 绿笔
            case R.id.pen_color_green:
                pointView.setPaintColor(ContextCompat.getColor(context, R.color.colorGreenPen));
                break;
            default:
                break;
        }
    }
    
}
