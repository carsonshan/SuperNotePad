package com.fairhand.supernotepad.puzzle.affix;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.activity.NoteKindActivity;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.app.UpdateNote;
import com.fairhand.supernotepad.entity.RealmNote;
import com.fairhand.supernotepad.entity.RealmSecretNote;
import com.fairhand.supernotepad.fragment.BasePhotoAffixFragment;
import com.fairhand.supernotepad.fragment.FivePhotoAffixFragment;
import com.fairhand.supernotepad.fragment.FourPhotoAffixFragment;
import com.fairhand.supernotepad.fragment.ThreePhotoAffixFragment;
import com.fairhand.supernotepad.fragment.TwoPhotoAffixPhotoAffixFragment;
import com.fairhand.supernotepad.puzzle.affix.util.FileUtils;
import com.fairhand.supernotepad.puzzle.affix.util.PuzzleUtils;
import com.fairhand.supernotepad.puzzle.edit.EditPhotoActivity;
import com.fairhand.supernotepad.util.BitmapUtil;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.util.TimeUtil;
import com.fairhand.supernotepad.util.Toaster;
import com.fairhand.supernotepad.view.DiyCommonDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.SquarePuzzleView;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

/**
 * 拼图记事界面
 *
 * @author FairHand
 * @date 2018/11/5
 */
public class PhotoAffixNoteActivity extends AppCompatActivity
        implements View.OnClickListener, PickLayoutThemeCallBack {
    
    @BindView(R.id.iv_affix_back)
    ImageView ivAffixBack;
    @BindView(R.id.tv_template)
    TextView templateAffix;
    @BindView(R.id.tv_freedom)
    TextView freedomAffix;
    @BindView(R.id.iv_pull_down)
    ImageView ivPullDown;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.square_puzzle_view_template)
    SquarePuzzleView squarePuzzleView;
    @BindView(R.id.bottom_template_bool_bar)
    FrameLayout bottomTemplateBoolBar;
    @BindView(R.id.tv_change_photo)
    TextView tvChangePhoto;
    @BindView(R.id.tv_rotate)
    TextView tvRotate;
    @BindView(R.id.panel_photo_operate)
    ConstraintLayout panelPhotoOperate;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.et_photo_affix_content)
    EditText photoAffixContent;
    @BindView(R.id.stick_view_free)
    StickerView stickViewFree;
    
    /**
     * 选择的图片
     */
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    
    /**
     * 拼图布局
     */
    private PuzzleLayout mPuzzleLayout;
    /**
     * 图片张数
     */
    private int pieceSize;
    
    private Realm mRealm;
    
    /**
     * 当前拼图模式
     */
    private MODE currentMode = MODE.TEMPLATE;
    
    /**
     * 拼图模式
     */
    private enum MODE {
        /**
         * 模板，自由
         */
        TEMPLATE,
        FREE,
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_affix_note);
        ButterKnife.bind(this);
        mRealm = Realm.getDefaultInstance();
        
        initData();
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        ivAffixBack.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        templateAffix.setOnClickListener(this);
        freedomAffix.setOnClickListener(this);
        ivPullDown.setOnClickListener(this);
        tvChangePhoto.setOnClickListener(this);
        tvRotate.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        
        // 获取选择的图片
        Intent intent = getIntent();
        ArrayList<String> path = intent.getStringArrayListExtra(NoteKindActivity.IMAGE_PATH);
        if (path != null) {
            for (String item : path) {
                Bitmap bitmap = BitmapFactory.decodeFile(item).copy(Bitmap.Config.ARGB_8888, true);
                bitmaps.add(bitmap);
                Drawable drawable = BitmapUtil.bitmap2Drawable(this, bitmap);
                stickViewFree.addSticker(new DrawableSticker(drawable), Sticker.Position.CENTER);
            }
        }
        
        // 布局类型（0斜线 1直线）
        int type = 1;
        // 布局主题
        int themeId = 0;
        // 图片张数
        pieceSize = bitmaps.size();
        // 根据图片的张数选择显示的布局主题
        switch (pieceSize) {
            case 2:
                replaceTemplateToolBar(new TwoPhotoAffixPhotoAffixFragment());
                type = 1;
                themeId = 1;
                break;
            case 3:
                replaceTemplateToolBar(new ThreePhotoAffixFragment());
                type = 0;
                themeId = 0;
                break;
            case 4:
                replaceTemplateToolBar(new FourPhotoAffixFragment());
                type = 1;
                themeId = 2;
                break;
            case 5:
                replaceTemplateToolBar(new FivePhotoAffixFragment());
                type = 1;
                themeId = 3;
                break;
            default:
                break;
        }
        // 通过类型，图片张数，布局主题获取拼图布局
        mPuzzleLayout = PuzzleUtils.getPuzzleLayout(type, pieceSize, themeId);
        
        initView();
        
        squarePuzzleView.post(this::addPieces);
    }
    
    /**
     * 加载图片到布局中
     */
    private void addPieces() {
        squarePuzzleView.addPieces(bitmaps);
    }
    
    /**
     * 初始化视图
     */
    private void initView() {
        // 设置拼图布局
        squarePuzzleView.setPuzzleLayout(mPuzzleLayout);
        squarePuzzleView.setTouchEnable(true);
        squarePuzzleView.setNeedDrawLine(false);
        squarePuzzleView.setNeedDrawOuterLine(false);
        squarePuzzleView.setLineSize(3);
        // 选中图片的外接线颜色
        squarePuzzleView.setSelectedLineColor(ContextCompat.getColor(this, R.color.colorItem));
        // 选中图片的操作Bar颜色
        squarePuzzleView.setHandleBarColor(ContextCompat.getColor(this, R.color.colorLineBar));
        squarePuzzleView.setAnimateDuration(240);
        // 选中监听
        squarePuzzleView.setOnPieceSelectedListener((piece, position) -> {
            panelPhotoOperate.setVisibility(View.VISIBLE);
            bottomTemplateBoolBar.setVisibility(View.INVISIBLE);
            ivPullDown.setVisibility(View.GONE);
        });
        // 图片之间的padding
        squarePuzzleView.setPiecePadding(0);
        
        // 设置选择布局主题回调接口
        BasePhotoAffixFragment.setCallBack(this);
    }
    
    /**
     * 动态更改模板拼图选择布局
     */
    private void replaceTemplateToolBar(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.bottom_template_bool_bar, fragment);
        transaction.commit();
    }
    
    private boolean isDown;
    private String content;
    private boolean isSaved;
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.iv_affix_back:
                back();
                break;
            // 模板拼图
            case R.id.tv_template:
                templateAffix();
                break;
            // 自由拼图
            case R.id.tv_freedom:
                freeAffix();
                break;
            // 保存
            case R.id.tv_save:
                content = photoAffixContent.getText().toString();
                preSave();
                break;
            // 放下或显示工具栏
            case R.id.iv_pull_down:
                if (isDown) {
                    bottomTemplateBoolBar.setVisibility(View.VISIBLE);
                    ivPullDown.setImageResource(R.drawable.arrow_down);
                    ivPullDown.setImageTintList(ColorStateList.valueOf(Color.BLACK));
                    isDown = false;
                } else {
                    bottomTemplateBoolBar.setVisibility(View.GONE);
                    ivPullDown.setImageResource(R.drawable.arrow_up);
                    ivPullDown.setImageTintList(ColorStateList.valueOf(Color.BLACK));
                    isDown = true;
                }
                break;
            case R.id.tv_change_photo:
                goToSelectPhoto();
                break;
            case R.id.tv_rotate:
                squarePuzzleView.rotate(90f);
                break;
            case R.id.iv_cancel:
                panelPhotoOperate.setVisibility(View.GONE);
                bottomTemplateBoolBar.setVisibility(View.VISIBLE);
                ivPullDown.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
    
    private void freeAffix() {
        freedomAffix.setTextColor(ContextCompat.getColor(this, R.color.colorItem));
        templateAffix.setTextColor(Color.BLACK);
        bottomTemplateBoolBar.setVisibility(View.GONE);
        ivPullDown.setVisibility(View.GONE);
        panelPhotoOperate.setVisibility(View.GONE);
        squarePuzzleView.setVisibility(View.GONE);
        stickViewFree.setVisibility(View.VISIBLE);
        currentMode = MODE.FREE;
    }
    
    private void templateAffix() {
        templateAffix.setTextColor(ContextCompat.getColor(this, R.color.colorItem));
        freedomAffix.setTextColor(Color.BLACK);
        bottomTemplateBoolBar.setVisibility(View.VISIBLE);
        ivPullDown.setVisibility(View.VISIBLE);
        squarePuzzleView.setVisibility(View.VISIBLE);
        stickViewFree.setVisibility(View.GONE);
        currentMode = MODE.TEMPLATE;
    }
    
    /**
     * 选择替换照片
     */
    private void goToSelectPhoto() {
        // 进入相册
        PictureSelector.create(this)
                // 图片类型
                .openGallery(PictureMimeType.ofImage())
                // DIY主题
                .theme(R.style.picture_diy_style)
                // 最大选择1张
                .maxSelectNum(1)
                // 每行显示4个
                .imageSpanCount(4)
                // 单选模式
                .selectionMode(PictureConfig.SINGLE)
                // 可预览图片
                .previewImage(true)
                // 不显示拍照按钮
                .isCamera(false)
                // 压缩
                .compress(true)
                // 压缩质量
                .cropCompressQuality(60)
                // 图片列表点击缩放效果
                .isZoomAnim(true)
                // 不显示GIF图
                .isGif(false)
                // 结果回调onActivityResult
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
    
    private void back() {
        if (isSaved) {
            finish();
        } else {
            DiyCommonDialog dialog = new DiyCommonDialog(this, R.style.DiyDialogStyle);
            dialog.setCancelable(false);
            dialog.setTitle("提示")
                    .setMessage("确认放弃对图片的操作？")
                    .setOnPositiveClickedListener("继续编辑", v -> dialog.dismiss())
                    .setOnNegativeClickListener("放弃", v -> {
                        dialog.show();
                        finish();
                    })
                    .show();
        }
    }
    
    /**
     * 切换拼图主题
     */
    private void changeFace(int type, int themeId) {
        mPuzzleLayout = PuzzleUtils.getPuzzleLayout(type, pieceSize, themeId);
        squarePuzzleView.setPuzzleLayout(mPuzzleLayout);
        addPieces();
    }
    
    private void preSave() {
        if (!TextUtils.isEmpty(content)) {
            Observable.create(emitter -> save())
                    .subscribeOn(Schedulers.io())
                    .subscribe();
        } else {
            Toaster.showShort(this, "请先输入记事内容");
        }
    }
    
    /**
     * 保存图片
     */
    private void save() {
        File file = FileUtils.getNewFile("SuperNote_PhotoAffix");
        File saveFile = new File(Config.getPhotoAffix(), UUID.randomUUID() + "");
        if (currentMode == MODE.TEMPLATE) {
            String photoPath;
            Bitmap bitmap = null;
            if (file != null) {
                photoPath = file.getAbsolutePath();
                bitmap = FileUtils.savePuzzle(squarePuzzleView, file, 100, new PhotoSaveCallback() {
                    @Override
                    public void onSuccess() {
                        saveSuccess(photoPath, saveFile);
                    }
                    
                    @Override
                    public void onFailed() {
                        Snackbar.make(squarePuzzleView, "保存失败", Snackbar.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toaster.showShort(this, "文件创建失败");
            }
            assert bitmap != null;
            BitmapUtil.saveBitmap(bitmap, saveFile, Bitmap.CompressFormat.PNG);
        } else if (currentMode == MODE.FREE) {
            stickViewFree.save(saveFile);
            assert file != null;
            stickViewFree.save(file);
            saveSuccess(file.getAbsolutePath(), saveFile);
        }
        
        runOnUiThread(() -> {
            Logger.d("线程：", "" + Thread.currentThread());
            // 保存报数据库
            if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
                saveDefault(saveFile.getAbsolutePath());
            } else if (Config.currentPad.equals(Config.SECRET_PAD)) {
                saveSecret(saveFile.getAbsolutePath());
            }
        });
        
    }
    
    private void saveSuccess(String photoPath, File saveFile) {
        runOnUiThread(() -> {
            DiyCommonDialog dialog = new DiyCommonDialog(PhotoAffixNoteActivity.this, R.style.DiyDialogStyle);
            dialog.setCancelable(false);
            dialog.setTitle("保存成功")
                    .setMessage("是否前往编辑图片？")
                    .setOnPositiveClickedListener("前往编辑", v -> {
                        Intent intent = new Intent(PhotoAffixNoteActivity.this, EditPhotoActivity.class);
                        intent.putExtra("photo_path", photoPath);
                        intent.putExtra("database_path", saveFile.getAbsolutePath());
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
                    })
                    .setOnNegativeClickListener("不要", v -> {
                        dialog.dismiss();
                        finish();
                    })
                    .show();
            isSaved = true;
        });
    }
    
    private void saveSecret(String imagePath) {
        mRealm.executeTransaction(realm -> {
            RealmSecretNote realmNote = realm.createObject(RealmSecretNote.class);
            realmNote.setKey(Config.TYPE_PUZZLE);
            realmNote.setNoteTitle(content);
            realmNote.setNoteTime(TimeUtil.getFormatTime());
            realmNote.getPictureIds().add(imagePath);
            // 清除压缩图片缓存
            PictureFileUtils.deleteCacheDirFile(this);
            mUpdateCallBack.updateMainView();
        });
    }
    
    /**
     * 保存默认
     */
    private void saveDefault(String imagePath) {
        mRealm.executeTransaction(realm -> {
            RealmNote realmNote = realm.createObject(RealmNote.class);
            realmNote.setKey(Config.TYPE_PUZZLE);
            realmNote.setNoteTitle(content);
            realmNote.setNoteTime(TimeUtil.getFormatTime());
            realmNote.getPictureIds().add(imagePath);
            // 清除压缩图片缓存
            PictureFileUtils.deleteCacheDirFile(this);
            mUpdateCallBack.updateMainView();
        });
    }
    
    @Override
    public void template(int type, int themeId) {
        changeFace(type, themeId);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    LocalMedia localMedia = selectList.get(0);
                    if (localMedia.isCompressed()) {
                        String path = localMedia.getCompressPath();
                        if (path != null) {
                            Bitmap bitmap = BitmapFactory
                                                    .decodeFile(localMedia.getCompressPath())
                                                    .copy(Bitmap.Config.ARGB_8888, true);
                            squarePuzzleView.replace(bitmap);
                        } else {
                            Snackbar.make(squarePuzzleView, "替换失败", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    private static UpdateNote mUpdateCallBack;
    
    public static void setCallback(UpdateNote updateMainView) {
        mUpdateCallBack = updateMainView;
    }
    
    @Override
    public void onBackPressed() {
        back();
    }
    
}
