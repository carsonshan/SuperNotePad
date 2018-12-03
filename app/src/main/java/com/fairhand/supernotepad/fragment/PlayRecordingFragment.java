package com.fairhand.supernotepad.fragment;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.entity.Note;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 播放录音文件
 *
 * @author Phanton
 * @date 12/2/2018 - Sunday - 11:19 AM
 */
public class PlayRecordingFragment extends BottomSheetDialogFragment {
    
    private static final String ARG_ITEM = "ARG_ITEM";
    
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    @BindView(R.id.tv_recording_duration)
    TextView tvRecordingDuration;
    @BindView(R.id.iv_play_recording)
    ImageView ivPlayRecording;
    Unbinder unbinder;
    
    private MediaPlayer mMediaPlayer;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        note = (Note) getArguments().getSerializable(ARG_ITEM);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_diy_play_record, container, false);
        unbinder = ButterKnife.bind(this, view);
        tvTitle.setText(note.getNoteTitle());
        
        startPlaying();
        
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mMediaPlayer != null && fromUser) {
                    mMediaPlayer.seekTo(progress);
                    handler.removeCallbacks(runnable);
                }
                updateProgress();
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mMediaPlayer != null) {
                    handler.removeCallbacks(runnable);
                }
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mMediaPlayer != null) {
                    handler.removeCallbacks(runnable);
                    mMediaPlayer.seekTo(seekBar.getProgress());
                    updateProgress();
                }
            }
        });
        return view;
    }
    
    @Override
    public void onPause() {
        if (mMediaPlayer != null) {
            pausePlaying();
        }
        super.onPause();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    
    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) {
            stopPlaying();
        }
        super.onDestroy();
    }
    
    public static PlayRecordingFragment newInstance(Note note) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, note);
        PlayRecordingFragment fragment = new PlayRecordingFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    Note note;
    
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getContext() == null) {
            return super.onCreateDialog(savedInstanceState);
        }
        return new BottomSheetDialog(getContext(), R.style.TransparentBottomSheetStyle);
    }
    
    private void stopPlaying() {
        handler.removeCallbacks(runnable);
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
        
        seekBar.setProgress(seekBar.getMax());
        isPlaying = !isPlaying;
    }
    
    private boolean isPlaying = true;
    
    @OnClick(R.id.iv_play_recording)
    public void onViewClicked() {
        if (isPlaying) {
            // 做暂停处理
            if (mMediaPlayer != null) {
                ivPlayRecording.setImageResource(R.drawable.selector_recording_play);
                mMediaPlayer.pause();
            }
        } else {
            // 做播放处理
            if (mMediaPlayer != null) {
                ivPlayRecording.setImageResource(R.drawable.selector_recording_pause);
                mMediaPlayer.start();
            } else {
                startPlaying();
            }
        }
        isPlaying = !isPlaying;
    }
    
    private void startPlaying() {
        ivPlayRecording.setImageResource(R.drawable.selector_recording_pause);
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(note.getRecordingPath());
            mMediaPlayer.prepare();
            // 格式化时长
            long duration = mMediaPlayer.getDuration();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
                                   - TimeUnit.MINUTES.toSeconds(minutes);
            tvRecordingDuration.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            seekBar.setMax((int) duration);
            mMediaPlayer.setOnPreparedListener(mp -> mMediaPlayer.start());
            mMediaPlayer.setOnCompletionListener(mp -> {
                ivPlayRecording.setImageResource(R.drawable.selector_recording_play);
                stopPlaying();
            });
            updateProgress();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 暂停播放
     */
    private void pausePlaying() {
        ivPlayRecording.setImageResource(R.drawable.selector_recording_play);
        mMediaPlayer.pause();
        handler.removeCallbacks(runnable);
    }
    
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer != null) {
                int currentPosition = mMediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition);
                updateProgress();
            }
        }
    };
    
    private Handler handler = new Handler();
    
    private void updateProgress() {
        handler.postDelayed(runnable, 10);
    }
    
}
