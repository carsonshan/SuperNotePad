package com.fairhand.supernotepad.video.model;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.entity.RealmNote;
import com.fairhand.supernotepad.entity.RealmSecretNote;
import com.fairhand.supernotepad.util.BitmapUtil;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.util.TimeUtil;

import java.io.File;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author FairHand
 * @date 2018/11/6
 */
public class VideoModelImpl implements IVideoModel {
    
    /**
     * 保存逻辑
     *
     * @param onSaveCallBack 保存结果回调接口
     * @param filePath       保存文件的路径
     * @param noteTitle      记事标题
     */
    @Override
    public void save(OnSaveCallBack onSaveCallBack, String filePath, String noteTitle) {
        Logger.d("视频路径：" + filePath);
        // 视频和标题都不能为空
        if (!TextUtils.isEmpty(noteTitle) && (filePath != null)) {
            Realm mRealm = Realm.getDefaultInstance();
            // 保存缩略图
            File singleBit = new File(Config.getVideoThumb(), UUID.randomUUID() + "");
            Bitmap bitmap = BitmapUtil.getVideoThumb(filePath);
            if (bitmap != null) {
                BitmapUtil.saveBitmap(bitmap, singleBit, Bitmap.CompressFormat.PNG);
            }
            Logger.d("视频缩略图" + bitmap);
            // 未重名
            // 保存成功
            if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
                saveDefault(onSaveCallBack, filePath, noteTitle, mRealm, singleBit);
            } else {
                saveSecret(onSaveCallBack, filePath, noteTitle, mRealm, singleBit);
            }
        } else {
            // 标题为空，无视频
            onSaveCallBack.titleEmpty();
        }
    }
    
    /**
     * 保存私密
     */
    private void saveSecret(OnSaveCallBack onSaveCallBack, String filePath, String noteTitle, Realm mRealm, File singleBit) {
        // 查询是否存在
        RealmResults<RealmSecretNote> recordingNotes =
                mRealm.where(RealmSecretNote.class)
                        .equalTo("noteTitle", noteTitle)
                        .findAll();
        if (recordingNotes.size() == 0) {
            mRealm.executeTransaction(realm -> {
                RealmSecretNote realmNote = realm.createObject(RealmSecretNote.class);
                realmNote.setKind(Config.TYPE_VIDEO);
                realmNote.setKey(String.valueOf(UUID.randomUUID()));
                realmNote.setNoteTitle(noteTitle);
                realmNote.setNoteTime(TimeUtil.getFormatTime());
                realmNote.setVideoPath(filePath);
                realmNote.getPictureIds().add(singleBit.getAbsolutePath());
                onSaveCallBack.success();
            });
        } else {
            onSaveCallBack.failed();
        }
    }
    
    /**
     * 保存默认
     */
    private void saveDefault(OnSaveCallBack onSaveCallBack, String filePath, String noteTitle, Realm mRealm, File singleBit) {
        // 查询是否存在
        RealmResults<RealmNote> recordingNotes =
                mRealm.where(RealmNote.class)
                        .equalTo("noteTitle", noteTitle)
                        .findAll();
        if (recordingNotes.size() == 0) {
            mRealm.executeTransaction(realm -> {
                RealmNote realmNote = realm.createObject(RealmNote.class);
                realmNote.setKind(Config.TYPE_VIDEO);
                realmNote.setKey(String.valueOf(UUID.randomUUID()));
                realmNote.setNoteTitle(noteTitle);
                realmNote.setNoteTime(TimeUtil.getFormatTime());
                realmNote.setVideoPath(filePath);
                realmNote.getPictureIds().add(singleBit.getAbsolutePath());
                onSaveCallBack.success();
            });
        } else {
            onSaveCallBack.failed();
        }
    }
    
    /**
     * 保存结果回调
     */
    public interface OnSaveCallBack {
        
        /**
         * 保存成功回调
         */
        void success();
        
        /**
         * 保存失败回调
         */
        void failed();
        
        /**
         * 记事标题为空
         */
        void titleEmpty();
    }
    
}
