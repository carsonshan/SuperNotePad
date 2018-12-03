package com.fairhand.supernotepad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.app.RetrofitService;
import com.fairhand.supernotepad.http.service.UserService;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.util.Toaster;
import com.fairhand.supernotepad.view.DiyShowAvatarDialog;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.File;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 个人中心
 *
 * @author Phanton
 * @date 11/27/2018 - Tuesday - 9:00 PM
 */
public class PersonalCenterActivity extends AppCompatActivity {
    
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);
        ButterKnife.bind(this);
        tvNickname.setText(Config.isTourist ? "游客" : Config.user.getNickName());
    }
    
    @Override
    public void onBackPressed() {
        finish();
    }
    
    @OnClick( {R.id.iv_back, R.id.iv_setting, R.id.iv_avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_setting:
                startActivity(new Intent(PersonalCenterActivity.this, SettingActivity.class));
                break;
            case R.id.iv_avatar:
                showAvatar();
                break;
            default:
                break;
        }
    }
    
    private void showAvatar() {
        DiyShowAvatarDialog dialog = new DiyShowAvatarDialog(this, R.style.DiyDialogStyle);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnChangeAvatarClickListener(v -> {
            goToPickPicture();
            dialog.dismiss();
        }).show();
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
                // 最大选择1张
                .maxSelectNum(1)
                // 每行显示3个
                .imageSpanCount(3)
                // 多选模式
                .selectionMode(PictureConfig.MULTIPLE)
                // 可预览图片
                .previewImage(true)
                // 显示拍照按钮
                .isCamera(true)
                // 压缩
                .compress(true)
                // 压缩质量
                .cropCompressQuality(36)
                // 图片列表点击缩放效果
                .isZoomAnim(true)
                // 不显示GIF图
                .isGif(false)
                // 结果回调onActivityResult
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }
    
    File avatar;
    
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
                        avatar = new File(localMedia.getCompressPath());
                        updateAvatar();
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    private void updateAvatar() {
        UserService avatarService = RetrofitService.getInstance().create(UserService.class);
        // 传入头像图片
        // multipart/form-data表示将数据以二进制的形式传入服务端中的request中
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), avatar);
        MultipartBody.Part multipart = MultipartBody.Part.createFormData(
                "file ", getUniqueFileName(avatar.getName()), requestFile);
        // 传入用户名
        // String userAccount = Config.user.getAccount();
        RequestBody userId = RequestBody.create(MediaType.parse("multipart/form-data"), "15189114774");
        avatarService.updateAvatar(userId, multipart)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Logger.d("subscribe");
                    }
                    
                    @Override
                    public void onNext(Integer integer) {
                        doUpload(integer);
                    }
                    
                    @Override
                    public void onError(Throwable e) {
                        Logger.d("onError" + e.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        Logger.d("onComplete");
                        // 删除缓存
                        PictureFileUtils.deleteCacheDirFile(PersonalCenterActivity.this);
                    }
                });
    }
    
    private String getUniqueFileName(String name) {
        return String.valueOf(UUID.randomUUID()) + name.substring(name.lastIndexOf('.'));
    }
    
    private void doUpload(Integer result) {
        if (result == 1) {
            Toaster.showShort(this, "上传成功");
        } else {
            Toaster.showShort(this, "上传失败");
        }
    }
    
}
