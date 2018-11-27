package com.fairhand.supernotepad.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.adapter.PictureAdapter;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.app.UpdateNote;
import com.fairhand.supernotepad.entity.RealmNote;
import com.fairhand.supernotepad.entity.RealmSecretNote;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.util.TimeUtil;
import com.fairhand.supernotepad.util.Toaster;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 照片记事
 *
 * @author FairHand
 * @date 2018/11/1
 */
public class PictureNoteActivity extends AppCompatActivity implements View.OnClickListener {
    
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.iv_save)
    ImageView ivSave;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.iv_camera)
    ImageView chooseCamera;
    @BindView(R.id.iv_album)
    ImageView chooseAlbum;
    @BindView(R.id.grid_view_picture)
    GridView gridViewImage;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    
    private Realm mRealm;
    
    private PictureAdapter mPictureAdapter;
    /**
     * 返回选择的图片的地址
     */
    private List<String> mData = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        mRealm = Realm.getDefaultInstance();
        
        initData();
    }
    
    @Override
    protected void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        chooseCamera.setOnClickListener(this);
        chooseAlbum.setOnClickListener(this);
        ivSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        
        mPictureAdapter = new PictureAdapter(this);
        // 设置GridView的适配器
        gridViewImage.setAdapter(mPictureAdapter);
        
        Intent intent = getIntent();
        int type = intent.getIntExtra(Config.KEY_FROM_READ, 0);
        if (type == 1) {
            // 当type=1时说明是查看保存的记录，获取值
            String title = intent.getStringExtra(MainActivity.KEY_NOTE_TITLE);
            String content = intent.getStringExtra(MainActivity.KEY_NOTE_CONTENT);
            mData = intent.getStringArrayListExtra(MainActivity.KEY_NOTE_PICTURES);
            if (title != null) {
                etTitle.setText(title);
                etTitle.setSelection(title.length());
            }
            if (content != null) {
                etContent.setText(content);
                etContent.setSelection(content.length());
            }
            if (mData != null) {
                mPictureAdapter.updateData(mData);
            }
        }
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 拍照
            case R.id.iv_camera:
                goToTakePhoto();
                break;
            // 相册
            case R.id.iv_album:
                // 检查有没有权限
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 申请获取权限
                    ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    goToPickPicture();
                }
                break;
            // 保存
            case R.id.iv_save:
                save();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
    
    /**
     * 保存
     */
    private void save() {
        // 获取到输入的标题和文本
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        // 先对输入做判断
        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            Toaster.showShort(this, "还什么都没有记呢");
        } else if (TextUtils.isEmpty(title)) {
            Toaster.showShort(this, "请输入记事标题");
        } else if (TextUtils.isEmpty(content)) {
            Toaster.showShort(this, "请输入记事内容");
        } else if (mData.size() == 0) {
            Toaster.showShort(this, "至少保存一张图片");
        } else {
            // 输入没问题，但保存之前还要检查是否重名
            // 先从数据库中查询noteTitle为title的数据
            
            // 未重名
            // 插入数据
            if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
                saveDefault(title, content);
            } else if (Config.currentPad.equals(Config.SECRET_PAD)) {
                saveSecret(title, content);
            }
        }
    }
    
    /**
     * 保存私密
     */
    private void saveSecret(String title, String content) {
        RealmResults<RealmNote> pictureNotes =
                mRealm.where(RealmNote.class)
                        .equalTo("noteTitle", title)
                        .findAll();
        if (pictureNotes.size() == 0) {
            mRealm.executeTransaction(realm -> {
                RealmSecretNote realmNote = realm.createObject(RealmSecretNote.class);
                realmNote.setKey(Config.TYPE_PICTURE);
                realmNote.setNoteTitle(title);
                realmNote.setNoteContent(content);
                realmNote.setNoteTime(TimeUtil.getFormatTime());
                for (String item : mData) {
                    realmNote.getPictureIds().add(item);
                }
                Toaster.showShort(this, "保存成功");
                // 清除压缩缓存
                PictureFileUtils.deleteCacheDirFile(this);
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
    private void saveDefault(String title, String content) {
        RealmResults<RealmNote> pictureNotes =
                mRealm.where(RealmNote.class)
                        .equalTo("noteTitle", title)
                        .findAll();
        if (pictureNotes.size() == 0) {
            mRealm.executeTransaction(realm -> {
                RealmNote realmNote = realm.createObject(RealmNote.class);
                realmNote.setKey(Config.TYPE_PICTURE);
                realmNote.setNoteTitle(title);
                realmNote.setNoteContent(content);
                realmNote.setNoteTime(TimeUtil.getFormatTime());
                for (String item : mData) {
                    realmNote.getPictureIds().add(item);
                }
                Toaster.showShort(this, "保存成功");
                // 清除压缩缓存
                PictureFileUtils.deleteCacheDirFile(this);
                mUpdateCallBack.updateMainView();
            });
        } else {
            // 重名
            Toaster.showShort(this, "记事标题已存在，换一个标题试试吧");
        }
    }
    
    /**
     * 拍照
     */
    private void goToTakePhoto() {
        // 打开相机
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .forResult(PictureConfig.REQUEST_CAMERA);
    }
    
    /**
     * 选择照片
     */
    private void goToPickPicture() {
        // 进入相册
        PictureSelector.create(this)
                // 图片类型
                .openGallery(PictureMimeType.ofImage())
                // DIY主题
                .theme(R.style.picture_diy_style)
                // 最大选择9张
                .maxSelectNum(9)
                // 每行显示4个
                .imageSpanCount(4)
                // 多选模式
                .selectionMode(PictureConfig.MULTIPLE)
                // 可预览图片
                .previewImage(true)
                // 不显示拍照按钮
                .isCamera(false)
                // 压缩
                .compress(true)
                // 压缩质量
                .cropCompressQuality(36)
                // 图片列表点击缩放效果
                .isZoomAnim(true)
                // 显示GIF图
                .isGif(true)
                // 结果回调onActivityResult
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
    
    /**
     * 申请权限回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToPickPicture();
                } else {
                    Toaster.showShort(getApplicationContext(), "无法获取权限");
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 选择图片结果回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    for (LocalMedia localMedia : selectList) {
                        if (localMedia.isCompressed()) {
                            mData.add(localMedia.getCompressPath());
                        }
                    }
                    // 更新数据
                    mPictureAdapter.updateData(mData);
                    break;
                // 相机拍摄
                case PictureConfig.REQUEST_CAMERA:
                    // 获取拍摄的图片
                    List<LocalMedia> tookPicture = PictureSelector.obtainMultipleResult(data);
                    mData.add(tookPicture.get(0).getPath());
                    // 更新数据
                    mPictureAdapter.updateData(mData);
                    break;
                default:
                    break;
            }
        }
    }
    
    private static UpdateNote mUpdateCallBack;
    
    /**
     * 设置回调接口
     */
    public static void setCallBack(UpdateNote updateCallBack) {
        mUpdateCallBack = updateCallBack;
    }
    
}
