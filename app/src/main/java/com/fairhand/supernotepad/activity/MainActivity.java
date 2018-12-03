package com.fairhand.supernotepad.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.adapter.CardAdapter;
import com.fairhand.supernotepad.adapter.ShowNoteAdapter;
import com.fairhand.supernotepad.affair.AffairNoteActivity;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.app.UpdateNote;
import com.fairhand.supernotepad.entity.Card;
import com.fairhand.supernotepad.entity.Note;
import com.fairhand.supernotepad.entity.RealmNote;
import com.fairhand.supernotepad.entity.RealmSecretNote;
import com.fairhand.supernotepad.fragment.PlayRecordingFragment;
import com.fairhand.supernotepad.puzzle.affix.PhotoAffixNoteActivity;
import com.fairhand.supernotepad.recording.view.RecordNoteActivity;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.util.Toaster;
import com.fairhand.supernotepad.video.view.VideoNoteActivity;
import com.fairhand.supernotepad.view.DiyCommonDialog;
import com.fairhand.supernotepad.view.DiyInputDialog;
import com.fairhand.supernotepad.view.DiyObserveCommonDialog;
import com.fairhand.supernotepad.view.DiyObservePictureDialog;
import com.fairhand.supernotepad.view.DiyObservePuzzleDialog;
import com.fairhand.supernotepad.view.ItemView;
import com.fairhand.supernotepad.view.SlideDragView;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * MAIN_MAIN_MAIN_MAIN_MAIN_MAIN_MAIN_MAIN_MAIN_MAIN
 *
 * @author FairHand
 * @date 2018/10/29
 */
public class MainActivity extends AppCompatActivity implements OnClickListener, UpdateNote {
    
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
    @BindView(R.id.tv_note_non)
    TextView tvNoteNon;
    @BindView(R.id.tv_user_account)
    TextView tvUserAccount;
    @BindView(R.id.iv_avatar_main)
    ImageView ivAvatar;
    @BindView(R.id.slide_drag_view)
    SlideDragView slideDragView;
    @BindView(R.id.iv_note_pad)
    ItemView ivNotePad;
    @BindView(R.id.iv_arrange)
    ImageView ivArrange;
    @BindView(R.id.rl_top_tool)
    RelativeLayout rlTopTool;
    
    private Realm mRealm;
    
    private ShowNoteAdapter adapter;
    /**
     * 备份所有的记事
     */
    private ArrayList<Note> backupNotes = new ArrayList<>();
    private ArrayList<Note> notes = new ArrayList<>();
    
    /**
     * 卡片数据
     */
    private Card[] cardArray = {
            new Card("普通记事", R.drawable.iv_common_note),
            new Card("手绘记事", R.drawable.ic_paint),
            new Card("事件记事", R.drawable.iv_affair_note),
            new Card("照片记事", R.drawable.iv_pictures)
    };
    private ArrayList<Card> cards = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
     * 设置左视图的数据
     */
    private void setLeftViewData() {
        cards.addAll(Arrays.asList(cardArray));
        
        // 设置mGridViewCard的适配器、点击事件
        mGridViewCard.setAdapter(new CardAdapter(this, cards));
        mGridViewCard.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(MainActivity.this, CommonNoteActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(MainActivity.this, HandPaintNoteActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(MainActivity.this, AffairNoteActivity.class));
                    break;
                case 3:
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
        adapter = new ShowNoteAdapter(this, notes);
        // 查询所有记事记录
        RealmResults<RealmSecretNote> realmNotes = mRealm.where(RealmSecretNote.class).findAll();
        Logger.d("大小------------" + realmNotes.size());
        // 先清空数据
        notes.clear();
        backupNotes.clear();
        adapter.updateData(notes);
        if (realmNotes.size() > 0) {
            tvNoteNon.setVisibility(View.GONE);
            // 有数据
            for (RealmSecretNote item : realmNotes) {
                Logger.d("标题------------" + item.getNoteTitle());
                Note note = new Note();
                note.setKind(item.getKind());
                note.setNoteTitle(item.getNoteTitle());
                note.setNoteContent(item.getNoteContent());
                note.setNoteTime(item.getNoteTime());
                ArrayList<String> data = new ArrayList<>(item.getPictureIds());
                note.setPictureIds(data);
                note.setVideoPath(item.getVideoPath());
                // 获取类型
                int key = item.getKind();
                // 根据类型加载不同图片
                switch (key) {
                    // 普通记事(加载默认图片)
                    case Config.TYPE_COMMON:
                        note.setNoteImageId(R.drawable.iv_common_note);
                        break;
                    // 录音记事(加载默认图片)
                    case Config.TYPE_RECORDING:
                        note.setNoteImageId(R.drawable.ic_record);
                        // 录音文件地址
                        note.setRecordingPath(item.getRecordingPath());
                        break;
                    default:
                        // 动态设置图片
                        note.setNoteImagePath(data.get(0));
                        break;
                }
                notes.add(note);
            }
            // 备份
            backupNotes.addAll(notes);
            setGridViewListener();
        } else {
            tvNoteNon.setVisibility(View.VISIBLE);
        }
        ivAllNote.setRightText(backupNotes.size() > 0 ? backupNotes.size() + "" : "暂无");
    }
    
    /**
     * 设置主视图的数据
     */
    private void setMainViewData() {
        Logger.d("位置", "偷偷设置视图");
        adapter = new ShowNoteAdapter(this, notes);
        // 查询所有记事记录
        RealmResults<RealmNote> realmNotes = mRealm.where(RealmNote.class).findAll();
        Logger.d("大小------------" + realmNotes.size());
        // 先清空数据
        notes.clear();
        backupNotes.clear();
        adapter.updateData(notes);
        if (realmNotes.size() > 0) {
            tvNoteNon.setVisibility(View.GONE);
            // 有数据
            for (RealmNote item : realmNotes) {
                Logger.d("标题------------" + item.getNoteTitle());
                Note note = new Note();
                note.setKind(item.getKind());
                note.setKey(item.getKey());
                note.setNoteTitle(item.getNoteTitle());
                note.setNoteContent(item.getNoteContent());
                note.setNoteTime(item.getNoteTime());
                ArrayList<String> data = new ArrayList<>(item.getPictureIds());
                note.setPictureIds(data);
                note.setVideoPath(item.getVideoPath());
                // 获取类型
                int key = item.getKind();
                // 根据类型加载不同图片
                switch (key) {
                    // 普通记事(加载默认图片)
                    case Config.TYPE_COMMON:
                        note.setNoteImageId(R.drawable.iv_common_note);
                        break;
                    // 录音记事(加载默认图片)
                    case Config.TYPE_RECORDING:
                        note.setNoteImageId(R.drawable.ic_record);
                        // 录音文件地址
                        note.setRecordingPath(item.getRecordingPath());
                        break;
                    default:
                        // 动态设置图片
                        note.setNoteImagePath(data.get(0));
                        break;
                }
                notes.add(note);
            }
            // 备份
            backupNotes.addAll(notes);
            setGridViewListener();
        } else {
            tvNoteNon.setVisibility(View.VISIBLE);
        }
        ivAllNote.setRightText(backupNotes.size() > 0 ? backupNotes.size() + "" : "暂无");
    }
    
    /**
     * 设置GridView的监听
     */
    private void setGridViewListener() {
        // 设置mGridViewNote的适配器、点击打开监听、长按删除监听
        adapter.updateData(notes);
        mGridViewNote.setAdapter(adapter);
        mGridViewNote.setOnItemLongClickListener((parent, view, position, id) -> {
            delete(position);
            // 返回true消费掉此事件
            return true;
        });
        mGridViewNote.setOnItemClickListener((parent, view, position, id) -> {
            // 获取到记事类型关键字
            Note note = notes.get(position);
            int key = note.getKind();
            switch (key) {
                case Config.TYPE_COMMON:
                    // 普通记事
                    observeCommonNote(note);
                    break;
                case Config.TYPE_PICTURE:
                    // 照片记事
                    observePictureNote(note);
                    break;
                case Config.TYPE_RECORDING:
                    // 录音记事
                    observeRecordingNote(note);
                    break;
                case Config.TYPE_VIDEO:
                    // 摄像记事
                    observeVideoNote(note);
                    break;
                case Config.TYPE_HAND_PAINT:
                    // 手绘记事
                case Config.TYPE_AFFIX:
                    // 拼图记事
                    observeImageNote(note);
                    break;
                default:
                    break;
            }
        });
    }
    
    private void observePictureNote(Note note) {
        DiyObservePictureDialog dialog = new DiyObservePictureDialog(this, R.style.DiyDialogStyle);
        dialog.setTvTitle(note.getNoteTitle())
                .setContent(note.getNoteContent())
                .setPictureData(note.getPictureIds())
                .show();
    }
    
    private void observeVideoNote(Note note) {
        Intent intentToPlayVideo = new Intent(getApplicationContext(), PlayVideoActivity.class);
        intentToPlayVideo.putExtra("name", note.getNoteTitle());
        intentToPlayVideo.putExtra("videoPath", note.getVideoPath());
        startActivity(intentToPlayVideo);
    }
    
    private void observeImageNote(Note note) {
        DiyObservePuzzleDialog dialog = new DiyObservePuzzleDialog(this, R.style.DiyDialogStyle);
        Bitmap bitmap = BitmapFactory.decodeFile(note.getNoteImagePath());
        dialog.setTvContentShow(note.getNoteTitle())
                .setIvPhotoShow(bitmap)
                .show();
    }
    
    private void observeCommonNote(Note note) {
        DiyObserveCommonDialog dialog = new DiyObserveCommonDialog(this, R.style.DiyDialogStyle);
        dialog.setTvTitle(note.getNoteTitle())
                .setContent(note.getNoteContent())
                .show();
    }
    
    private void observeRecordingNote(Note note) {
        PlayRecordingFragment fragment = PlayRecordingFragment.newInstance(note);
        fragment.show(getSupportFragmentManager(), "recording");
    }
    
    /**
     * 删除
     */
    private void delete(int position) {
        // 打开提示对话框询问是否删除
        DiyCommonDialog dialog = new DiyCommonDialog(this, R.style.DiyDialogStyle);
        dialog.setCancelable(false);
        dialog.setTitle("提示")
                .setMessage("确认删除？")
                .setOnPositiveClickedListener("删除", v -> {
                    dialog.dismiss();
                    handleDelete(position);
                    Toaster.showShort(getApplicationContext(), "删除成功");
                })
                .setOnNegativeClickListener("不了", v -> dialog.dismiss())
                .show();
    }
    
    /**
     * 难      受      的      不      得
     */
    private void helpless(RealmObject object) {
        if (object == null) {
            Toaster.showShort(MainActivity.this, "删除失败了");
        } else {
            mRealm.executeTransaction(realm -> object.deleteFromRealm());
            Toaster.showShort(MainActivity.this, "事件已删除");
        }
    }
    
    /**
     * 处理删除
     */
    private void handleDelete(int position) {
        if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
            // 先查询数据
            RealmNote realmNote = mRealm.where(RealmNote.class).equalTo("key", notes.get(position).getKey()).findFirst();
            // 再执行删除操作
            helpless(realmNote);
        } else if (Config.currentPad.equals(Config.SECRET_PAD)) {
            // 先查询数据
            RealmSecretNote realmNote = mRealm.where(RealmSecretNote.class).equalTo("key", notes.get(position).getKey()).findFirst();
            // 再执行删除操作
            helpless(realmNote);
        }
        // 移除选中item并通知刷新界面
        backupNotes.remove(position);
        notes.remove(position);
        adapter.updateData(notes);
        ivAllNote.setRightText(notes.size() + "");
        showOrHideTip();
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        tvUserAccount.setText(Config.isTourist ? "游客" : Config.user.getNickName());
        
        ivAllNote.setOnClickListener(this);
        ivNotePad.setOnClickListener(this);
        ivNoteKind.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        tvUserAccount.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        ivArrange.setOnClickListener(this);
        
        CommonNoteActivity.setCallBack(this);
        PictureNoteActivity.setCallBack(this);
        HandPaintNoteActivity.setCallBack(this);
        RecordNoteActivity.setCallBack(this);
        VideoNoteActivity.setCallBack(this);
        LockActivity.setCallBack(this);
        NotePadActivity.setCallBack(this);
        PhotoAffixNoteActivity.setCallback(this);
        
        // 下拉框选择监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                            pickNote(Config.TYPE_COMMON);
                            break;
                        // 手绘记事
                        case 2:
                            pickNote(Config.TYPE_HAND_PAINT);
                            break;
                        // 照片记事
                        case 3:
                            pickNote(Config.TYPE_PICTURE);
                            break;
                        // 拍摄记事
                        case 4:
                            pickNote(Config.TYPE_VIDEO);
                            break;
                        // 录音记事
                        case 5:
                            pickNote(Config.TYPE_RECORDING);
                            break;
                        // 拼图记事
                        case 6:
                            pickNote(Config.TYPE_AFFIX);
                            pickAffixNote();
                            break;
                        default:
                            break;
                    }
                } else {
                    tvNoteNon.setVisibility(View.VISIBLE);
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 未选中时候的操作
            }
        });
    }
    
    /**
     * 分类选择
     *
     * @param noteKind 要选择的记事类型
     */
    private void pickNote(int noteKind) {
        notes.clear();
        for (Note note : backupNotes) {
            if (note.getKind() == noteKind) {
                notes.add(note);
            }
        }
        adapter.updateData(notes);
        showOrHideTip();
    }
    
    /**
     * 选择分类（拼图记事）
     */
    private void pickAffixNote() {
        notes.clear();
        for (Note note : backupNotes) {
            if (note.getKind() == Config.TYPE_AFFIX) {
                // 拼图记事，加入
                notes.add(note);
            }
        }
        adapter.updateData(notes);
        showOrHideTip();
    }
    
    /**
     * 选择分类（所有记事）
     */
    private void pickAllNote() {
        notes.clear();
        notes.addAll(backupNotes);
        adapter.updateData(notes);
        showOrHideTip();
    }
    
    private void showOrHideTip() {
        if (notes.size() == 0) {
            tvNoteNon.setVisibility(View.VISIBLE);
        } else {
            tvNoteNon.setVisibility(View.GONE);
        }
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
            // 跳转个人中心
            case R.id.iv_avatar_main:
            case R.id.tv_user_account:
                startActivity(new Intent(MainActivity.this, PersonalCenterActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(
                                this,
                                new Pair<>(ivAvatar, "share_avatar"),
                                new Pair<>(tvUserAccount, "share_nickname")
                        ).toBundle());
                break;
            // 更换排列方式
            case R.id.iv_arrange:
                showAllArrange();
                break;
            default:
                break;
        }
    }
    
    private void showAllArrange() {
        EasyPopup mEasyPopup = EasyPopup.create(this)
                                       .setContentView(R.layout.popup_window_show_arrange_way)
                                       .setFocusAndOutsideEnable(true)
                                       .setBackgroundDimEnable(true)
                                       .setDimValue(.4f)
                                       .setDimColor(ContextCompat.getColor(this, R.color.colorDim))
                                       .apply();
        // 设置显示位置
        mEasyPopup.showAtAnchorView(rlTopTool, YGravity.BELOW, XGravity.ALIGN_RIGHT, 24, 24);
        ItemView grid = mEasyPopup.findViewById(R.id.iv_arrange_grid);
        ItemView timeline = mEasyPopup.findViewById(R.id.iv_arrange_timeline);
        grid.setOnClickListener(v -> {
            mEasyPopup.dismiss();
            changeArrange(0);
        });
        timeline.setOnClickListener(v -> {
            mEasyPopup.dismiss();
            changeArrange(1);
        });
    }
    
    /**
     * 切换布局
     */
    private void changeArrange(int witchArrange) {
        switch (witchArrange) {
            case 0:
                break;
            case 1:
                break;
            default:
                break;
        }
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
                    handleSearch(message, dialog);
                })
                .setOnNegativeClickListener("取消", v -> dialog.cancel())
                .show();
    }
    
    /**
     * 处理搜索
     *
     * @param message 输入的搜索内容
     */
    private void handleSearch(String message, DiyInputDialog dialog) {
        if (TextUtils.isEmpty(message)) {
            // 没有输入
            Toaster.showShort(this, "请输入搜索内容");
        } else {
            dialog.dismiss();
            // 有输入
            notes.clear();
            for (Note note : backupNotes) {
                if (note.getNoteTitle().contains(message)) {
                    notes.add(note);
                }
            }
            if (notes.size() == 0) {
                // 没找到
                Toaster.showShort(this, "没有找到相关的内容");
                // 显示提示文字
                tvNoteNon.setVisibility(View.VISIBLE);
            } else {
                // 找到了
                tvNoteNon.setVisibility(View.GONE);
                adapter.updateData(notes);
                Toaster.showShort(this, "已为你找到相关内容");
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
