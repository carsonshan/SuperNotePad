package com.fairhand.supernotepad.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.app.UpdateNote;
import com.fairhand.supernotepad.entity.RealmNote;
import com.fairhand.supernotepad.entity.RealmSecretNote;
import com.fairhand.supernotepad.util.BitmapUtil;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.util.TimeUtil;
import com.fairhand.supernotepad.util.Toaster;
import com.fairhand.supernotepad.view.DiyCommonDialog;
import com.fairhand.supernotepad.view.DiyInputDialog;
import com.fairhand.supernotepad.view.DiyPenDialog;
import com.fairhand.supernotepad.view.DrawView;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.xw.repo.BubbleSeekBar;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.io.File;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 手绘记事界面
 *
 * @author FairHand
 * @date 2018/10/30
 */
public class HandPaintNoteActivity extends AppCompatActivity
        implements View.OnClickListener, DrawView.Callback {
    
    @BindView(R.id.iv_undo)
    ImageView ivUndo;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.iv_redo)
    ImageView ivRedo;
    @BindView(R.id.canvas_view)
    DrawView mDrawView;
    @BindView(R.id.ll_bottom_tool_bar)
    LinearLayout llBottomToolBar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.iv_save)
    ImageView ivSave;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    
    private Realm mRealm;
    
    private String noteTitle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_paint);
        ButterKnife.bind(this);
        mRealm = Realm.getDefaultInstance();
        
        initTab();
        initData();
        setListener();
    }
    
    @Override
    protected void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }
    
    /**
     * 设置监听
     */
    private void setListener() {
        // 设置Tab的监听
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            /**
             * 选中了tab
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    // 绘图
                    case 0:
                        mDrawView.setMode(DrawView.MODE.DRAW);
                        break;
                    // 橡皮
                    case 1:
                        mDrawView.setMode(DrawView.MODE.ERASER);
                        break;
                    // 形状
                    case 2:
                        mDrawView.setMode(DrawView.MODE.SHAPE);
                        break;
                    // 插图
                    case 3:
                        showPickPicture();
                        mDrawView.setMode(DrawView.MODE.NULL);
                    default:
                        break;
                }
            }
            
            /**
             * 未选中tab
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            
            /**
             * 再次选中tab
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    // 绘图
                    case 0:
                        mDrawView.setMode(DrawView.MODE.DRAW);
                        // 显示更改画笔属性对话框
                        new DiyPenDialog(HandPaintNoteActivity.this, R.style.DiyDialogStyle).show();
                        break;
                    // 橡皮
                    case 1:
                        mDrawView.setMode(DrawView.MODE.ERASER);
                        showPopupWindow();
                        break;
                    // 形状
                    case 2:
                        showShapeDialog();
                        mDrawView.setMode(DrawView.MODE.SHAPE);
                        break;
                    // 插图
                    case 3:
                        showPickPicture();
                        mDrawView.setMode(DrawView.MODE.NULL);
                    default:
                        break;
                }
            }
        });
    }
    
    /**
     * 进入相册选取图片
     */
    private void showPickPicture() {
        // 进入相册
        PictureSelector.create(this)
                // 图片类型
                .openGallery(PictureMimeType.ofImage())
                // DIY主题
                .theme(R.style.picture_diy_style)
                // 每行显示4个
                .imageSpanCount(4)
                // 单选模式
                .selectionMode(PictureConfig.SINGLE)
                // 可预览图片
                .previewImage(true)
                // 显示拍照按钮
                .isCamera(true)
                // 图片列表点击缩放效果
                .isZoomAnim(true)
                // 不显示GIF图
                .isGif(false)
                // 压缩
                .compress(true)
                // 小于100k不压缩
                .minimumCompressSize(100)
                // 压缩质量
                .cropCompressQuality(60)
                // 结果回调onActivityResult
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
    
    /**
     * 形状对话框
     */
    private void showShapeDialog() {
        EasyPopup mEasyPopup = EasyPopup.create(this)
                                       .setContentView(R.layout.popup_window_shpae)
                                       .setFocusAndOutsideEnable(true)
                                       .setBackgroundDimEnable(true)
                                       .setDimValue(.4f)
                                       .setDimColor(ContextCompat.getColor(this, R.color.colorDim))
                                       .apply();
        
        mEasyPopup.showAtAnchorView(tabLayout, YGravity.BELOW, XGravity.CENTER, 24, 24);
        ImageView rect = mEasyPopup.findViewById(R.id.iv_shape_rect);
        rect.setOnClickListener(v -> mEasyPopup.dismiss());
    }
    
    /**
     * 显示橡皮擦PopupWindow
     */
    private void showPopupWindow() {
        Logger.d("出现了");
        EasyPopup mEasyPopup = EasyPopup.create(this)
                                       .setContentView(R.layout.popup_window_eraser)
                                       .setFocusAndOutsideEnable(true)
                                       .setBackgroundDimEnable(true)
                                       .setDimValue(.4f)
                                       .setDimColor(ContextCompat.getColor(this, R.color.colorDim))
                                       .apply();
        // 设置显示位置
        mEasyPopup.showAtAnchorView(tabLayout, YGravity.BELOW, XGravity.ALIGN_LEFT, 24, 24);
        
        BubbleSeekBar bubbleSeekBar = mEasyPopup.findViewById(R.id.bubble_seek_bar_eraser);
        // 设置滑动条位置
        bubbleSeekBar.setProgress(mDrawView.mEraserSize);
        bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                super.onProgressChanged(bubbleSeekBar, progress, progressFloat, fromUser);
                mDrawView.setEraserSize(progress);
            }
        });
    }
    
    /**
     * 初始化Tab
     */
    private void initTab() {
        // 添加TabItem
        tabLayout.addTab(tabLayout.newTab().setText("画笔").setIcon(R.drawable.ic_pen), true);
        tabLayout.addTab(tabLayout.newTab().setText("橡皮").setIcon(R.drawable.ic_eraser));
        tabLayout.addTab(tabLayout.newTab().setText("形状").setIcon(R.drawable.ic_shape));
        tabLayout.addTab(tabLayout.newTab().setText("插图").setIcon(R.drawable.ic_illustration));
    }
    
    
    /**
     * 初始化数据
     */
    private void initData() {
        mDrawView.setCallback(this);
        ivClear.setOnClickListener(this);
        ivRedo.setOnClickListener(this);
        ivUndo.setOnClickListener(this);
        ivSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 撤销
            case R.id.iv_undo:
                if (mDrawView.canUndo()) {
                    mDrawView.undo();
                }
                break;
            // 重做
            case R.id.iv_redo:
                if (mDrawView.canRedo()) {
                    mDrawView.redo();
                }
                break;
            // 清除画布
            case R.id.iv_clear:
                mDrawView.clear();
                break;
            // 保存
            case R.id.iv_save:
                save();
                break;
            case R.id.iv_back:
                back();
                break;
            default:
                break;
        }
    }
    
    private void back() {
        DiyCommonDialog dialog = new DiyCommonDialog(this, R.style.DiyDialogStyle);
        dialog.setTitle("提示")
                .setMessage("退出后不会保存已绘制内容，确认退出？")
                .setOnNegativeClickListener("继续画", v1 -> dialog.dismiss())
                .setOnPositiveClickedListener("退出", v12 -> {
                    dialog.dismiss();
                    finish();
                })
                .show();
    }
    
    private String imagePath;
    
    /**
     * 分享
     */
    private void share() {
        DiyCommonDialog dialog = new DiyCommonDialog(this, R.style.DiyDialogStyle);
        dialog.setCancelable(false);
        dialog.setTitle("保存成功")
                .setMessage("分享一个吧？")
                .setOnPositiveClickedListener("我要分享", v -> {
                    handleShare();
                    dialog.dismiss();
                })
                .setOnNegativeClickListener("不分享", v -> dialog.dismiss())
                .show();
    }
    
    /**
     * 处理分享
     */
    private void handleShare() {
        OnekeyShare onekeyShare = new OnekeyShare();
        // 关闭sso授权
        onekeyShare.disableSSOWhenAuthorize();
        // title标题，微信、QQ和QQ空间等平台使用
        onekeyShare.setTitle(getString(R.string.app_name));
        // titleUrl QQ和QQ空间跳转链接
        onekeyShare.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        onekeyShare.setText("这是我在超级记事本手绘记事中画的，一起来加入记事吧！");
        // imagePath是图片的本地路径(确保SDCard下面存在此张图片)
        onekeyShare.setImagePath(imagePath);
        // url在微信、微博，Facebook等平台中使用
        onekeyShare.setUrl("http://sharesdk.cn");
        // 启动分享GUI
        onekeyShare.show(this);
    }
    
    /**
     * 保存
     */
    private void save() {
        // 保存提示对话框
        DiyInputDialog dialog = new DiyInputDialog(this, R.style.DiyDialogStyle);
        dialog.setCancelable(false);
        dialog.setTitle("请输入记事标题")
                .setOnPositiveClickedListener("保存", v -> {
                    // 保存
                    noteTitle = dialog.getMessage();
                    if (!TextUtils.isEmpty(noteTitle)) {
                        handleSave();
                        dialog.dismiss();
                    } else {
                        Toaster.showShort(getApplicationContext(), "记事标题不能为空");
                    }
                })
                .setOnNegativeClickListener("取消", v -> {
                    // 不保存
                    dialog.dismiss();
                    Toaster.showShort(getApplicationContext(), "取消保存");
                })
                .show();
    }
    
    /**
     * 处理保存操作
     */
    private void handleSave() {
        // 获取并保存绘制图
        Bitmap drawBitmap = mDrawView.loadBitmapFromView(mDrawView);
        File singleBit = new File(Config.getHandPaint(), UUID.randomUUID() + "");
        BitmapUtil.saveBitmap(drawBitmap, singleBit, Bitmap.CompressFormat.PNG);
        imagePath = singleBit.getAbsolutePath();
        
        if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
            saveDefault();
        } else if (Config.currentPad.equals(Config.SECRET_PAD)) {
            saveSecret();
        }
        share();
    }
    
    /**
     * 保存私密
     */
    private void saveSecret() {
        // 查询是否存在
        RealmResults<RealmSecretNote> handPaintNotes =
                mRealm.where(RealmSecretNote.class)
                        .equalTo("noteTitle", noteTitle)
                        .findAll();
        if (handPaintNotes.size() == 0) {
            mRealm.executeTransaction(realm -> {
                RealmSecretNote realmNote = realm.createObject(RealmSecretNote.class);
                realmNote.setKind(Config.TYPE_HAND_PAINT);
                realmNote.setKey(String.valueOf(UUID.randomUUID()));
                realmNote.setNoteTitle(noteTitle);
                realmNote.setNoteTime(TimeUtil.getFormatTime());
                realmNote.getPictureIds().add(imagePath);
                // 清除压缩图片缓存
                PictureFileUtils.deleteCacheDirFile(this);
                Toaster.showShort(this, "保存成功");
                mUpdateCallBack.updateMainView();
            });
        } else {
            // 重名
            Toaster.showShort(this, "记事标题已存在，换一个标题试试吧");
        }
    }
    
    /**
     * 保存默认
     */
    private void saveDefault() {
        // 查询是否存在
        RealmResults<RealmNote> handPaintNotes =
                mRealm.where(RealmNote.class)
                        .equalTo("noteTitle", noteTitle)
                        .findAll();
        if (handPaintNotes.size() == 0) {
            mRealm.executeTransaction(realm -> {
                RealmNote realmNote = realm.createObject(RealmNote.class);
                realmNote.setKind(Config.TYPE_HAND_PAINT);
                realmNote.setKey(String.valueOf(UUID.randomUUID()));
                realmNote.setNoteTitle(noteTitle);
                realmNote.setNoteTime(TimeUtil.getFormatTime());
                realmNote.getPictureIds().add(imagePath);
                // 清除压缩图片缓存
                PictureFileUtils.deleteCacheDirFile(this);
                Toaster.showShort(this, "保存成功");
                mUpdateCallBack.updateMainView();
            });
        } else {
            // 重名
            Toaster.showShort(this, "记事标题已存在，换一个标题试试吧");
        }
    }
    
    /**
     * 画布单击回调事件
     */
    @Override
    public void onSingleClicked() {
        if (llBottomToolBar.getVisibility() == View.VISIBLE
                    || tabLayout.getVisibility() == View.VISIBLE) {
            // 将工具栏隐藏
            llBottomToolBar.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
            ivSave.setVisibility(View.GONE);
            ivBack.setVisibility(View.GONE);
        } else {
            // 显示工具栏
            llBottomToolBar.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            ivSave.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
        }
    }
    
    private static UpdateNote mUpdateCallBack;
    
    /**
     * 设置回调接口
     */
    public static void setCallBack(UpdateNote updateCallBack) {
        mUpdateCallBack = updateCallBack;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 相册选择
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true(音视频除外)
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true(音视频除外)
                    Logger.d("选择的图片：" + selectList);
                    LocalMedia media = selectList.get(0);
                    if (media.isCompressed()) {
                        mDrawView.setIllustration(selectList.get(0).getCompressPath());
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    @Override
    public void onBackPressed() {
        back();
    }
    
}
