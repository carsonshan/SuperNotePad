package com.fairhand.supernotepad.recording.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.activity.BaseActivity;
import com.fairhand.supernotepad.app.UpdateNote;
import com.fairhand.supernotepad.recording.presenter.IRecordPresenter;
import com.fairhand.supernotepad.recording.presenter.RecordPresenterImpl;
import com.fairhand.supernotepad.recording.service.RecordingService;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.util.Toaster;
import com.fairhand.supernotepad.view.DiyInputDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 录音记事Activity
 *
 * @author FairHand
 * @date 2018/11/5
 */
public class RecordNoteActivity extends BaseActivity implements View.OnClickListener, IRecordView {
    
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_start_recording)
    ImageView ivStartRecording;
    @BindView(R.id.iv_stop)
    ImageView ivStop;
    @BindView(R.id.chronometer)
    Chronometer chronometer;
    
    private IRecordPresenter presenter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_note);
        ButterKnife.bind(this);
        
        initData();
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        presenter = new RecordPresenterImpl(this);
        
        ivBack.setOnClickListener(this);
        ivStartRecording.setOnClickListener(this);
        ivStop.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.iv_back:
                onBackPressed();
                break;
            // 开始录音
            case R.id.iv_start_recording:
                // 检查有没有权限
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 申请获取权限
                    ActivityCompat.requestPermissions(this,
                            new String[] {
                                    Manifest.permission.RECORD_AUDIO,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    presenter.record(true);
                }
                break;
            // 停止
            case R.id.iv_stop:
                presenter.record(false);
                break;
            default:
                break;
        }
    }
    
    @Override
    public void handleRecord(boolean isStart) {
        Intent intentRecording = new Intent(RecordNoteActivity.this, RecordingService.class);
        if (isStart) {
            // 启动录音服务
            startService(intentRecording);
            ivStartRecording.setVisibility(View.GONE);
            ivStop.setVisibility(View.VISIBLE);
            Toaster.showShort(this, "录音开始，再次点击结束录音");
            // 计时器清0
            chronometer.setBase(SystemClock.elapsedRealtime());
            // 开始计时
            chronometer.start();
            // 屏幕常亮
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            // 停止录音服务
            stopService(intentRecording);
            ivStartRecording.setVisibility(View.VISIBLE);
            ivStop.setVisibility(View.GONE);
            Logger.d("时长text：" + chronometer.getText());
            // 停止录音
            chronometer.stop();
            // 清0
            chronometer.setBase(SystemClock.elapsedRealtime());
            // 移除屏幕常亮
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            presenter.preSave();
        }
    }
    
    private DiyInputDialog dialog;
    
    @Override
    public void showSaveDialog() {
        Logger.d("提示保存对话框");
        dialog = new DiyInputDialog(this, R.style.DiyDialogStyle);
        dialog.setCancelable(false);
        dialog.setTitle("保存")
                .setOnPositiveClickedListener("保存", v -> {
                    // 获取输入的记事标题
                    String noteTitle = dialog.getMessage();
                    if (!TextUtils.isEmpty(noteTitle)) {
                        // 保存
                        presenter.save(noteTitle);
                    } else {
                        Toaster.showShort(getApplicationContext(), "记事标题不能为空");
                    }
                })
                .setOnNegativeClickListener("扔了", v -> {
                    // 丢弃
                    presenter.drag();
                    dialog.dismiss();
                })
                .show();
    }
    
    @Override
    public void saveSuccess() {
        Toaster.showShort(getApplicationContext(), "保存成功");
        dialog.dismiss();
        mUpdateCallBack.updateMainView();
    }
    
    @Override
    public void saveFailed() {
        Logger.d("当前线程：" + Thread.currentThread());
        Toaster.showShort(getApplicationContext(), "记事标题已存在，换个标题试试吧");
    }
    
    @Override
    public void dragSuccess() {
        Toaster.showShort(getApplicationContext(), "丢弃成功");
    }
    
    @Override
    public void dragFailed() {
        Toaster.showShort(getApplicationContext(), "丢弃失败");
    }
    
    @Override
    public void canNotFindFile() {
        Toaster.showShort(getApplicationContext(), "找不到文件");
    }
    
    private static UpdateNote mUpdateCallBack;
    
    /**
     * 设置更新视图回调接口
     */
    public static void setCallBack(UpdateNote updateCallBack) {
        mUpdateCallBack = updateCallBack;
    }
    
    /**
     * 申请权限回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    presenter.record(true);
                } else {
                    Toaster.showShort(this, "无法获取录音权限");
                }
                break;
            default:
                break;
        }
    }
    
}
