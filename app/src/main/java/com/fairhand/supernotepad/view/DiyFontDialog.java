package com.fairhand.supernotepad.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.adapter.FontAdapter;
import com.fairhand.supernotepad.entity.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义文字修改Dialog
 *
 * @author Phanton
 * @date 11/23/2018 - Friday - 9:01 AM
 */
public class DiyFontDialog extends BaseDialog implements View.OnClickListener {
    
    private TextView tvDisplayText;
    private EditText etInputText;
    private ListView listView;
    
    private Context mContext;
    public static int currentFont;
    private List<Font> fonts;
    private int currentColor = Color.WHITE;
    
    public DiyFontDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }
    
    @Override
    protected int setView() {
        return R.layout.dialog_diy_font;
    }
    
    @Override
    protected void initData() {
        init();
        etInputText.setSelection(etInputText.getText().length());
        fonts = new ArrayList<>();
        fonts.add(new Font("默认字体", null));
        fonts.add(new Font("记事", Typeface.createFromAsset(mContext.getAssets(), "font/font1.ttf")));
        fonts.add(new Font("记事", Typeface.createFromAsset(mContext.getAssets(), "font/font2.ttf")));
        FontAdapter adapter = new FontAdapter(fonts, mContext);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener((parent, view, position, id) -> {
            tvDisplayText.setTypeface(fonts.get(position).getTypeface());
            currentFont = position;
            adapter.notifyDataSetChanged();
        });
        
        etInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvDisplayText.setText(s);
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    
    private void init() {
        View fontRed = findViewById(R.id.font_red);
        View fontBlue = findViewById(R.id.font_blue);
        View fontGreen = findViewById(R.id.font_green);
        tvDisplayText = findViewById(R.id.tv_display_text);
        Button btnInputComplete = findViewById(R.id.btn_input_complete);
        etInputText = findViewById(R.id.et_input_text);
        listView = findViewById(R.id.list_view);
        
        fontRed.setOnClickListener(this);
        fontBlue.setOnClickListener(this);
        fontGreen.setOnClickListener(this);
        btnInputComplete.setOnClickListener(this);
        if (onConfirmListener != null) {
            btnInputComplete.setOnClickListener(onConfirmListener);
        }
    }
    
    private View.OnClickListener onConfirmListener;
    
    public void setOnConfirmListener(View.OnClickListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }
    
    public String getInputText() {
        return tvDisplayText.getText().toString();
    }
    
    public Typeface getTextTypeface() {
        return fonts.get(currentFont).getTypeface();
    }
    
    public int getTextColor() {
        return currentColor;
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.font_red:
                currentColor = ContextCompat.getColor(mContext, R.color.colorRedPen);
                tvDisplayText.setTextColor(currentColor);
                break;
            case R.id.font_green:
                currentColor = ContextCompat.getColor(mContext, R.color.colorGreenPen);
                tvDisplayText.setTextColor(currentColor);
                break;
            case R.id.font_blue:
                currentColor = ContextCompat.getColor(mContext, R.color.colorBluePen);
                tvDisplayText.setTextColor(currentColor);
                break;
            default:
                break;
        }
    }
    
}
