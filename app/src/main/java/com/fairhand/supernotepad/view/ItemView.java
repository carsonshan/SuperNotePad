package com.fairhand.supernotepad.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fairhand.supernotepad.R;

/**
 * item选项卡
 *
 * @author FairHand
 * @date 2018/9/24
 */
public class ItemView extends LinearLayout {
    
    /**
     * 是否显示左侧图标
     */
    private boolean isShowLeftIcon;
    
    /**
     * 是否显示底部分隔线
     */
    private boolean isShowDivideLine;
    
    /**
     * 是否显示右侧箭头
     */
    private boolean isShowRightArrow;
    
    /**
     * 左侧文字
     */
    private String leftText;
    private TextView tvLeftText;
    
    /**
     * 右侧文字
     */
    private String rightText;
    private TextView tvRightText;
    
    /**
     * 左侧图标
     */
    private Drawable leftIcon;
    private ImageView ivLeftIcon;
    
    /**
     * 右侧箭头
     */
    private ImageView rightArrow;
    
    /**
     * 分隔线
     */
    private View divideLine;
    
    public ItemView(Context context) {
        super(context);
    }
    
    public ItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public ItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.item_option_menu, this);
        
        initView();
        
        // 获取设置的属性
        TypedArray typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        isShowDivideLine = typedArray.getBoolean(
                R.styleable.ItemView_show_divide_line,
                false);
        isShowLeftIcon = typedArray.getBoolean(R.styleable.ItemView_show_left_icon, false);
        isShowRightArrow = typedArray.getBoolean(R.styleable.ItemView_show_right_arrow,
                false);
        leftText = typedArray.getString(R.styleable.ItemView_left_text);
        rightText = typedArray.getString(R.styleable.ItemView_right_text);
        leftIcon = typedArray.getDrawable(R.styleable.ItemView_left_icon);
        
        typedArray.recycle();
        
        setView();
    }
    
    /**
     * 初始化视图
     */
    private void initView() {
        tvLeftText = findViewById(R.id.tv_left_text);
        ivLeftIcon = findViewById(R.id.iv_left_icon);
        tvRightText = findViewById(R.id.tv_right_text);
        rightArrow = findViewById(R.id.iv_right_arrow);
        divideLine = findViewById(R.id.view_divide_line);
    }
    
    /**
     * 设置视图
     */
    private void setView() {
        ivLeftIcon.setVisibility(isShowLeftIcon ? View.VISIBLE : View.GONE);
        ivLeftIcon.setImageDrawable(leftIcon);
        tvLeftText.setText(leftText);
        tvRightText.setText(rightText);
        rightArrow.setVisibility(isShowRightArrow ? View.VISIBLE : View.GONE);
        divideLine.setVisibility(isShowDivideLine ? View.VISIBLE : View.GONE);
    }
    
    /**
     * 设置右边文本
     */
    public void setRightText(String rightText) {
        tvRightText.setText(rightText);
    }
    
}
