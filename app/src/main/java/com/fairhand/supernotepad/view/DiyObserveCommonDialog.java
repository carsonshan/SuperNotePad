package com.fairhand.supernotepad.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.fairhand.supernotepad.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Phanton
 * @date 12/1/2018 - Saturday - 1:22 PM
 */
public class DiyObserveCommonDialog extends Dialog {
    
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    
    private String title;
    private String content;
    
    public DiyObserveCommonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_diy_observe_common);
        ButterKnife.bind(this);
        
        Window window = getWindow();
        assert window != null;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvTitle.setText(title);
        tvContent.setText(content);
    }
    
    public DiyObserveCommonDialog setTvTitle(String title) {
        this.title = title;
        return this;
    }
    
    public DiyObserveCommonDialog setContent(String content) {
        this.content = content;
        return this;
    }
    
}
