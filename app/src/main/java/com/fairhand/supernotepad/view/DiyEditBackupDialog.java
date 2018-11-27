package com.fairhand.supernotepad.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fairhand.supernotepad.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 事件记事，编辑备注对话框
 *
 * @author Phanton
 * @date 11/25/2018 - Sunday - 11:41 AM
 */
public class DiyEditBackupDialog extends Dialog {
    
    @BindView(R.id.et_backup)
    EditText etBackup;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    
    private String backup;
    private View.OnClickListener onClickListener;
    
    public DiyEditBackupDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_diy_backup);
        ButterKnife.bind(this);
        
        etBackup.setText(backup);
        etBackup.setSelection(backup == null ? 0 : backup.length());
        if (onClickListener != null) {
            tvConfirm.setOnClickListener(onClickListener);
        }
    }
    
    @OnClick(R.id.tv_cancel)
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                cancel();
                break;
            default:
                break;
        }
    }
    
    public void setTvConfirmListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    
    public void setEtBackup(String backup) {
        this.backup = backup;
    }
    
    public String getEtBackup() {
        return etBackup.getText().toString();
    }
    
}
