package com.fairhand.supernotepad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fairhand.supernotepad.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 摄像视频播放界面
 *
 * @author FairHand
 * @date 2018/11/6
 */
public class PlayVideoActivity extends AppCompatActivity {
    
    @BindView(R.id.jc_video)
    JCVideoPlayerStandard jcVideo;
    
    private String title;
    private String path;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        ButterKnife.bind(this);
        
        getData();
        setJCVideo();
    }
    
    /**
     * 获取传递来的数据
     */
    private void getData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("name");
        path = intent.getStringExtra("videoPath");
    }
    
    /**
     * 设置播放器
     */
    private void setJCVideo() {
        jcVideo.setUp(path, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, title);
        jcVideo.fullscreenButton.setVisibility(View.GONE);
        jcVideo.backButton.setVisibility(View.VISIBLE);
        jcVideo.backButton.setOnClickListener(v -> onBackPressed());
        jcVideo.startVideo();
    }
    
    @Override
    protected void onPause() {
        JCVideoPlayer.releaseAllVideos();
        super.onPause();
    }
    
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    
}
