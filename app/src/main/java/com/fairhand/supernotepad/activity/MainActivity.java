package com.fairhand.supernotepad.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.adapter.CardAdapter;
import com.fairhand.supernotepad.adapter.ShowNoteAdapter;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.app.UpdateNote;
import com.fairhand.supernotepad.entity.Card;
import com.fairhand.supernotepad.entity.Note;
import com.fairhand.supernotepad.entity.RealmNote;
import com.fairhand.supernotepad.entity.RealmSecretNote;
import com.fairhand.supernotepad.recording.view.RecordNoteActivity;
import com.fairhand.supernotepad.util.CacheUtil;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.util.Toaster;
import com.fairhand.supernotepad.video.view.VideoNoteActivity;
import com.fairhand.supernotepad.view.DiyCommonDialog;
import com.fairhand.supernotepad.view.DiyInputDialog;
import com.fairhand.supernotepad.view.ItemView;
import com.fairhand.supernotepad.view.SlideDragView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 程序主界面
 *
 * @author FairHand
 * @date 2018/10/29
 */
public class MainActivity extends AppCompatActivity implements OnClickListener, UpdateNote {
    
    /**
     * Intent传值key
     */
    public static final String KEY_NOTE_TITLE = "KEY_NOTE_TITLE";
    public static final String KEY_NOTE_CONTENT = "KEY_NOTE_CONTENT";
    public static final String KEY_NOTE_PICTURES = "KEY_NOTE_PICTURES";
    
    @BindView(R.id.grid_view_card)
    GridView mGridViewCard;
    @BindView(R.id.grid_view_note)
    GridView mGridViewNote;
    @BindView(R.id.iv_all_note)
    ItemView ivAllNote;
    @BindView(R.id.iv_note_kind)
    ItemView ivNoteKind;
    @BindView(R.id.spinner)
    AppCompatSpinner spinner;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_search_result_non)
    TextView tvSearchResultNon;
    @BindView(R.id.tv_user_account)
    TextView tvUserAccount;
    @BindView(R.id.iv_exit_app)
    ImageView ivExitApp;
    @BindView(R.id.slide_drag_view)
    SlideDragView slideDragView;
    @BindView(R.id.iv_note_pad)
    ItemView ivNotePad;
    
    private Realm mRealm;
    
    private ShowNoteAdapter adapter;
    
    /**
     * 备份所有的记事
     */
    private ArrayList<Note> backupNotes = new ArrayList<>();
    
    /**
     * 卡片数据
     */
    private Card[] cardArray = {
            new Card("普通记事", R.drawable.iv_common_note),
            new Card("手绘记事", R.drawable.iv_hand_paint),
            new Card("事项记事", R.drawable.iv_affair_note),
            new Card("照片记事", R.drawable.iv_pictures)
    };
    private ArrayList<Card> cards = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        smoothSwitchScreen();
        setContentView(R.layout.activity_main);
        translucentBar(R.color.colorItem);
        ButterKnife.bind(this);
        // 获取Realm实例
        mRealm = Realm.getDefaultInstance();
        
        initData();
        if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
            setMainViewData();
        } else {
            setSecretData();
        }
        setLeftViewData();
    }
    
    @Override
    protected void onDestroy() {
        // 关闭Realm
        mRealm.close();
        super.onDestroy();
    }
    
    /**
     * 全屏切换到非全屏的闪屏问题
     */
    private void smoothSwitchScreen() {
        ViewGroup rootView = findViewById(android.R.id.content);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        rootView.setPadding(0, statusBarHeight, 0, 0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
    
    /**
     * 状态栏变色处理
     */
    public void translucentBar(int color) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 获取状态栏高度
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        // 绘制一个和状态栏一样高的矩形，并添加到视图中
        View rectView = new View(this);
        LinearLayout.LayoutParams params
                = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        rectView.setLayoutParams(params);
        rectView.setBackgroundColor(getResources().getColor(color));
        // 添加矩形View到布局中
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        decorView.addView(rectView);
        // 设置根布局的参数
        ViewGroup rootView
                = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }
    
    /**
     * 设置左视图的数据
     */
    private void setLeftViewData() {
        cards.addAll(Arrays.asList(cardArray));
        
        // 设置mGridViewCard的适配器、点击事件
        mGridViewCard.setAdapter(new CardAdapter(this, cards));
        mGridViewCard.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    // 跳转到普通记事界面
                    startActivity(new Intent(MainActivity.this, CommonNoteActivity.class));
                    break;
                case 1:
                    // 跳转到手绘记事
                    startActivity(new Intent(MainActivity.this, HandPaintNoteActivity.class));
                    break;
                case 2:
                    Toaster.showShort(getApplicationContext(), "事项记事");
                    break;
                case 3:
                    // 跳转到拍照记事
                    startActivity(new Intent(MainActivity.this, PictureNoteActivity.class));
                    break;
                default:
                    break;
            }
        });
    }
    
    /**
     * 设置私密数据
     */
    private void setSecretData() {
        tvSearchResultNon.setVisibility(View.GONE);
        adapter = new ShowNoteAdapter(this, Config.notes);
        Logger.d("设置主视图");
        // 查询所有记事记录
        RealmResults<RealmSecretNote> realmNotes = mRealm.where(RealmSecretNote.class).findAll();
        Logger.d("大小------------" + realmNotes.size());
        // 先清空数据
        Config.notes.clear();
        backupNotes.clear();
        adapter.updateData(Config.notes);
        if (realmNotes.size() > 0) {
            int locationFromDatabase = 0;
            // 有数据
            for (RealmSecretNote item : realmNotes) {
                Logger.d("标题------------" + item.getNoteTitle());
                Note note = new Note();
                note.setKey(item.getKey());
                note.setNoteTitle(item.getNoteTitle());
                note.setNoteContent(item.getNoteContent());
                note.setNoteTime(item.getNoteTime());
                ArrayList<String> data = new ArrayList<>(item.getPictureIds());
                note.setPictureIds(data);
                note.setVideoPath(item.getVideoPath());
                note.setLocationFromDatabase(locationFromDatabase++);
                // 获取类型
                int key = item.getKey();
                // 根据类型加载不同图片
                switch (key) {
                    // 普通记事(加载默认图片)
                    case Config.TYPE_COMMON:
                        note.setNoteImageId(R.drawable.iv_common_note);
                        break;
                    // 录音记事(加载默认图片)
                    case Config.TYPE_RECORDING:
                        note.setNoteImageId(R.drawable.iv_recording);
                        // 录音文件地址
                        note.setRecordingPath(item.getRecordingPath());
                        break;
                    default:
                        // 动态设置图片
                        note.setNoteImagePath(data.get(0));
                        break;
                }
                Config.notes.add(note);
            }
            // 备份
            backupNotes.addAll(Config.notes);
            setGridViewListener();
        }
        ivAllNote.setRightText(backupNotes.size() + "");
    }
    
    /**
     * 设置主视图的数据
     */
    private void setMainViewData() {
        tvSearchResultNon.setVisibility(View.GONE);
        adapter = new ShowNoteAdapter(this, Config.notes);
        Logger.d("设置主视图");
        // 查询所有记事记录
        RealmResults<RealmNote> realmNotes = mRealm.where(RealmNote.class).findAll();
        Logger.d("大小------------" + realmNotes.size());
        // 先清空数据
        Config.notes.clear();
        backupNotes.clear();
        adapter.updateData(Config.notes);
        if (realmNotes.size() > 0) {
            // 有数据
            int locationFromDataBase = 0;
            for (RealmNote item : realmNotes) {
                Logger.d("标题------------" + item.getNoteTitle());
                Note note = new Note();
                note.setKey(item.getKey());
                note.setNoteTitle(item.getNoteTitle());
                note.setNoteContent(item.getNoteContent());
                note.setNoteTime(item.getNoteTime());
                ArrayList<String> data = new ArrayList<>(item.getPictureIds());
                note.setPictureIds(data);
                note.setVideoPath(item.getVideoPath());
                note.setLocationFromDatabase(locationFromDataBase++);
                // 获取类型
                int key = item.getKey();
                // 根据类型加载不同图片
                switch (key) {
                    // 普通记事(加载默认图片)
                    case Config.TYPE_COMMON:
                        note.setNoteImageId(R.drawable.iv_common_note);
                        break;
                    // 录音记事(加载默认图片)
                    case Config.TYPE_RECORDING:
                        note.setNoteImageId(R.drawable.iv_recording);
                        // 录音文件地址
                        note.setRecordingPath(item.getRecordingPath());
                        break;
                    default:
                        // 动态设置图片
                        note.setNoteImagePath(data.get(0));
                        break;
                }
                Config.notes.add(note);
            }
            // 备份
            backupNotes.addAll(Config.notes);
            setGridViewListener();
        }
        ivAllNote.setRightText(backupNotes.size() + "");
    }
    
    /**
     * 设置GridView的监听
     */
    private void setGridViewListener() {
        // 设置mGridViewNote的适配器、点击打开监听、长按删除监听
        adapter.updateData(Config.notes);
        mGridViewNote.setAdapter(adapter);
        mGridViewNote.setOnItemLongClickListener((parent, view, position, id) -> {
            Logger.d("长按位置：" + position);
            delete(Config.notes.get(position).getLocationFromDatabase(), position);
            // 返回true消费掉此事件
            return true;
        });
        mGridViewNote.setOnItemClickListener((parent, view, position, id) -> {
            // 获取到记事类型关键字
            Note note = Config.notes.get(position);
            int key = note.getKey();
            switch (key) {
                case Config.TYPE_COMMON:
                    // 普通记事
                    Intent intentToCommon = new Intent(getApplicationContext(), CommonNoteActivity.class);
                    commonParam(note, intentToCommon);
                    startActivity(intentToCommon);
                    break;
                case Config.TYPE_HAND_PAINT:
                    // 手绘记事
                    Intent intent = new Intent(getApplicationContext(), HandPaintNoteActivity.class);
                    intent.putExtra(KEY_NOTE_TITLE, note.getNoteTitle());
                    intent.putExtra(KEY_NOTE_PICTURES, note.getNoteImagePath());
                    intent.putExtra(Config.KEY_FROM_READ, 1);
                    startActivity(intent);
                    break;
                case Config.TYPE_PICTURE:
                    // 照片记事
                    Intent intentToPicture = new Intent(getApplicationContext(), PictureNoteActivity.class);
                    commonParam(note, intentToPicture);
                    intentToPicture.putStringArrayListExtra(KEY_NOTE_PICTURES, (ArrayList<String>) note.getPictureIds());
                    startActivity(intentToPicture);
                    break;
                case Config.TYPE_RECORDING:
                    // 录音记事
                    startPlaying(Config.notes.get(position).getRecordingPath());
                    break;
                case Config.TYPE_VIDEO:
                    // 摄像记事
                    Intent intentToPlayVideo = new Intent(getApplicationContext(), PlayVideoActivity.class);
                    intentToPlayVideo.putExtra("name", note.getNoteTitle());
                    intentToPlayVideo.putExtra("videoPath", note.getVideoPath());
                    startActivity(intentToPlayVideo);
                    break;
                case Config.TYPE_PUZZLE:
                    // 拼图记事
                    break;
                default:
                    break;
            }
        });
    }
    
    private MediaPlayer mMediaPlayer;
    
    /**
     * 播放录音文件
     */
    private void startPlaying(String filePath) {
        Toaster.showShort(this, "开始播放");
        mMediaPlayer = new MediaPlayer();
        try {
            // 设置播放源
            mMediaPlayer.setDataSource(filePath);
            // 播放器进入准备状态
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(mp -> {
                // 准备完了开始播放
                mMediaPlayer.start();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 播放完成监听
        mMediaPlayer.setOnCompletionListener(mp -> stopPlaying());
    }
    
    /**
     * 停止播放
     */
    private void stopPlaying() {
        // 释放掉MediaPlayer
        mMediaPlayer.stop();
        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
        Toaster.showShort(this, "播放完成");
    }
    
    /**
     * 通用传参
     */
    private void commonParam(Note note, Intent intent) {
        intent.putExtra(KEY_NOTE_TITLE, note.getNoteTitle());
        intent.putExtra(KEY_NOTE_CONTENT, note.getNoteContent());
        intent.putExtra(Config.KEY_FROM_READ, 1);
    }
    
    /**
     * 删除
     */
    private void delete(int location, int position) {
        // 打开提示对话框询问是否删除
        DiyCommonDialog dialog = new DiyCommonDialog(this, R.style.DiyDialogStyle);
        dialog.setCancelable(false);
        dialog.setTitle("提示")
                .setMessage("确认删除？")
                .setOnPositiveClickedListener("删除", v -> {
                    dialog.dismiss();
                    handleDelete(location, position);
                    Toaster.showShort(getApplicationContext(), "删除成功");
                })
                .setOnNegativeClickListener("不了", v -> dialog.dismiss())
                .show();
    }
    
    /**
     * 处理删除
     *
     * @param location 数据在数据库中的位置
     * @param position 在GridView的位置
     */
    private void handleDelete(int location, int position) {
        if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
            // 先查询数据
            RealmResults<RealmNote> realmNotes = mRealm.where(RealmNote.class).findAll();
            // 再执行删除操作
            mRealm.executeTransaction(realm -> realmNotes.deleteFromRealm(location));
        } else if (Config.currentPad.equals(Config.SECRET_PAD)) {
            // 先查询数据
            RealmResults<RealmSecretNote> realmNotes = mRealm.where(RealmSecretNote.class).findAll();
            // 再执行删除操作
            mRealm.executeTransaction(realm -> realmNotes.deleteFromRealm(location));
        }
        // 移除选中item并通知刷新界面
        backupNotes.remove(location);
        Config.notes.remove(position);
        adapter.updateData(Config.notes);
        ivAllNote.setRightText(Config.notes.size() + "");
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        tvUserAccount.setText(Config.userAccount);
        
        ivAllNote.setOnClickListener(this);
        ivNotePad.setOnClickListener(this);
        ivNoteKind.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivExitApp.setOnClickListener(this);
        
        CommonNoteActivity.setCallBack(this);
        PictureNoteActivity.setCallBack(this);
        HandPaintNoteActivity.setCallBack(this);
        RecordNoteActivity.setCallBack(this);
        VideoNoteActivity.setCallBack(this);
        LockActivity.setCallBack(this);
        NotePadActivity.setCallBack(this);
        
        // 下拉框选择监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvSearchResultNon.setVisibility(View.GONE);
                // 有数据的情况下才选
                if (adapter != null) {
                    // 选择下拉框列表项的操作
                    switch (position) {
                        // 所有记事
                        case 0:
                            pickAllNote();
                            break;
                        // 普通记事
                        case 1:
                            pickCommonNote();
                            break;
                        // 手绘记事
                        case 2:
                            pickHandPaintNote();
                            break;
                        // 照片记事
                        case 3:
                            pickPictureNote();
                            break;
                        // 拍摄记事
                        case 4:
                            pickVideoNote();
                            break;
                        // 录音记事
                        case 5:
                            pickRecordNote();
                            break;
                        default:
                            break;
                    }
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 未选中时候的操作
            }
        });
    }
    
    /**
     * 选择分类（录音记事）
     */
    private void pickRecordNote() {
        Config.notes.clear();
        for (Note note : backupNotes) {
            if (note.getKey() == Config.TYPE_RECORDING) {
                // 拍摄记事，加入
                Config.notes.add(note);
            }
        }
        adapter.updateData(Config.notes);
    }
    
    /**
     * 选择分类（拍摄记事）
     */
    private void pickVideoNote() {
        Config.notes.clear();
        for (Note note : backupNotes) {
            if (note.getKey() == Config.TYPE_VIDEO) {
                // 拍摄记事，加入
                Config.notes.add(note);
            }
        }
        adapter.updateData(Config.notes);
    }
    
    /**
     * 选择分类（照片记事）
     */
    private void pickPictureNote() {
        Config.notes.clear();
        for (Note note : backupNotes) {
            if (note.getKey() == Config.TYPE_PICTURE) {
                // 照片记事，加入
                Config.notes.add(note);
            }
        }
        adapter.updateData(Config.notes);
    }
    
    /**
     * 选择分类（手绘记事）
     */
    private void pickHandPaintNote() {
        Config.notes.clear();
        for (Note note : backupNotes) {
            if (note.getKey() == Config.TYPE_HAND_PAINT) {
                // 手绘记事，加入
                Config.notes.add(note);
            }
        }
        adapter.updateData(Config.notes);
    }
    
    /**
     * 选择分类（普通记事）
     */
    private void pickCommonNote() {
        Config.notes.clear();
        for (Note note : backupNotes) {
            if (note.getKey() == Config.TYPE_COMMON) {
                // 普通记事，加入
                Config.notes.add(note);
            }
        }
        adapter.updateData(Config.notes);
    }
    
    /**
     * 选择分类（所有记事）
     */
    private void pickAllNote() {
        Config.notes.clear();
        Config.notes.addAll(backupNotes);
        adapter.updateData(Config.notes);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 所有记事
            case R.id.iv_all_note:
                // 返回主视图
                slideDragView.backMainView();
                break;
            // 记事本
            case R.id.iv_note_pad:
                startActivity(new Intent(MainActivity.this, NotePadActivity.class));
                break;
            // 记事类型
            case R.id.iv_note_kind:
                startActivity(new Intent(MainActivity.this, NoteKindActivity.class));
                break;
            // 搜索
            case R.id.iv_search:
                search();
                break;
            // 退出
            case R.id.iv_exit_app:
                exit();
                break;
            default:
                break;
        }
    }
    
    private void exit() {
        DiyCommonDialog dialog = new DiyCommonDialog(this, R.style.DiyDialogStyle);
        dialog.setCancelable(false);
        dialog.setTitle("提示")
                .setMessage("确认退出？")
                .setOnPositiveClickedListener("确认", v -> {
                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                    CacheUtil.putLoginYet(getApplicationContext(), false);
                    CacheUtil.putUser(getApplicationContext(), "");
                    Config.isLogin = false;
                    finish();
                    dialog.dismiss();
                })
                .setOnNegativeClickListener("取消", v -> dialog.dismiss())
                .show();
    }
    
    /**
     * 搜索
     */
    private void search() {
        DiyInputDialog dialog = new DiyInputDialog(this, R.style.DiyDialogStyle);
        dialog.setCancelable(false);
        dialog.setTitle("搜索")
                .setOnPositiveClickedListener("确认", v -> {
                    String message = dialog.getMessage();
                    handleSearch(message);
                    dialog.cancel();
                })
                .setOnNegativeClickListener("取消", v -> dialog.cancel())
                .show();
    }
    
    /**
     * 处理搜索
     *
     * @param message 输入的搜索内容
     */
    private void handleSearch(String message) {
        if (TextUtils.isEmpty(message)) {
            // 没有输入
            Toaster.showShort(this, "不输入内容搜什么呀 :)");
        } else {
            // 有输入
            Config.notes.clear();
            for (Note note : backupNotes) {
                if (note.getNoteTitle().contains(message)) {
                    Config.notes.add(note);
                }
            }
            if (Config.notes.size() == 0) {
                // 没找到
                Toaster.showShort(this, "没有找到相关的内容 :(");
                // 显示提示文字
                tvSearchResultNon.setVisibility(View.VISIBLE);
            } else {
                // 找到了
                tvSearchResultNon.setVisibility(View.GONE);
                adapter.updateData(Config.notes);
                Toaster.showShort(this, "已为你找到相关内容 :/");
            }
        }
    }
    
    /**
     * 点击返回键回到桌面而不是退出
     */
    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        startActivity(homeIntent);
    }
    
    @Override
    public void updateMainView() {
        if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
            setMainViewData();
        } else {
            setSecretData();
        }
    }
    
}
