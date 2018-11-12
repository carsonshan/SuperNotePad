package com.fairhand.supernotepad.video.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.activity.BaseActivity;
import com.fairhand.supernotepad.app.UpdateNote;
import com.fairhand.supernotepad.util.BitmapUtil;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.util.Toaster;
import com.fairhand.supernotepad.video.presenter.IVideoPresenter;
import com.fairhand.supernotepad.video.presenter.VideoPresenterImpl;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 摄像记事Activity
 *
 * @author FairHand
 * @date 2018/11/5
 */
public class VideoNoteActivity extends BaseActivity implements View.OnClickListener, IVideoView {
    
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.jc_video)
    JCVideoPlayerStandard jcVideo;
    @BindView(R.id.iv_add_video)
    ImageView ivAddVideo;
    @BindView(R.id.tv_add_video_hint)
    TextView tvAddVideoHint;
    @BindView(R.id.tv_save)
    TextView tvSave;
    
    private IVideoPresenter presenter;
    
    /**
     * 录制视频存放路径
     */
    public static String videoPath;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_note);
        ButterKnife.bind(this);
        
        initData();
    }
    
    @Override
    protected void onPause() {
        // 释放视频
        JCVideoPlayer.releaseAllVideos();
        super.onPause();
    }
    
    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        presenter = new VideoPresenterImpl(this);
        
        ivBack.setOnClickListener(this);
        ivAddVideo.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.iv_back:
                onBackPressed();
                break;
            // 添加视频
            case R.id.iv_add_video:
                presenter.preShooting();
                break;
            // 保存
            case R.id.tv_save:
                presenter.save(editText.getText().toString());
                break;
            default:
                break;
        }
    }
    
    @Override
    public void saveSuccess() {
        Toaster.showShort(getApplicationContext(), "保存成功");
        mUpdateCallBack.updateMainView();
    }
    
    @Override
    public void saveFailed() {
        Toaster.showShort(getApplicationContext(), "保存失败，记事标题已存在");
    }
    
    @Override
    public void titleEmpty() {
        Toaster.showShort(getApplicationContext(), "记事标题为空或未拍摄视频");
    }
    
    @Override
    public void startShooting() {
        // 开始录制
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofVideo())
                .forResult(PictureConfig.REQUEST_CAMERA);
    }
    
    @Override
    public void shootComplete() {
        ivAddVideo.setVisibility(View.GONE);
        tvAddVideoHint.setVisibility(View.GONE);
        jcVideo.setVisibility(View.VISIBLE);
        // 录制完成
        // 设置视频(无标题)
        jcVideo.setUp(videoPath, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
        // 设置缩略图
        jcVideo.thumbImageView.setImageBitmap(BitmapUtil.getVideoThumb(videoPath));
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.REQUEST_CAMERA:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    Logger.d("拍摄：" + selectList.get(0).getPath());
                    videoPath = selectList.get(0).getPath();
                    if (!TextUtils.isEmpty(videoPath)) {
                        presenter.addComplete();
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    private static UpdateNote mUpdateCallBack;
    
    /**
     * 设置更新视图回调接口
     */
    public static void setCallBack(UpdateNote updateCallBack) {
        mUpdateCallBack = updateCallBack;
    }
    
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    
}
