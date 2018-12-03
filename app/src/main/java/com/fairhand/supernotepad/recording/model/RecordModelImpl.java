package com.fairhand.supernotepad.recording.model;

import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.entity.RealmNote;
import com.fairhand.supernotepad.entity.RealmSecretNote;
import com.fairhand.supernotepad.util.TimeUtil;

import java.io.File;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 实现IModel接口，负责实际的数据获取操作（数据库读取，网络加载等）<br/>
 * 然后通过自己的接口反馈出去
 *
 * @author FairHand
 * @date 2018/11/5
 */
public class RecordModelImpl implements IRecordModel {
    
    /**
     * 保存逻辑
     *
     * @param onSaveCallBack 保存结果回调接口
     * @param filePath       保存文件路径
     * @param noteTitle      记事标题
     */
    @Override
    public void save(OnSaveCallBack onSaveCallBack, String filePath, String noteTitle) {
        Realm mRealm = Realm.getDefaultInstance();
        
        // 未重名
        if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
            saveDefault(onSaveCallBack, filePath, noteTitle, mRealm);
        } else {
            saveSecret(onSaveCallBack, filePath, noteTitle, mRealm);
        }
    }
    
    /**
     * 保存私密
     */
    private void saveSecret(OnSaveCallBack onSaveCallBack, String filePath, String noteTitle, Realm mRealm) {
        // 查询是否存在
        RealmResults<RealmSecretNote> recordingNotes =
                mRealm.where(RealmSecretNote.class)
                        .equalTo("noteTitle", noteTitle)
                        .findAll();
        if (recordingNotes.size() == 0) {
            mRealm.executeTransaction(realm -> {
                RealmSecretNote realmNote = realm.createObject(RealmSecretNote.class);
                realmNote.setKind(Config.TYPE_RECORDING);
                realmNote.setKey(String.valueOf(UUID.randomUUID()));
                realmNote.setNoteTitle(noteTitle);
                realmNote.setNoteTime(TimeUtil.getFormatTime());
                realmNote.setRecordingPath(filePath);
                onSaveCallBack.saveSuccess();
            });
        } else {
            onSaveCallBack.saveFailed();
        }
    }
    
    /**
     * 保存默认
     */
    private void saveDefault(OnSaveCallBack onSaveCallBack, String filePath, String noteTitle, Realm mRealm) {
        // 查询是否存在
        RealmResults<RealmNote> recordingNotes =
                mRealm.where(RealmNote.class)
                        .equalTo("noteTitle", noteTitle)
                        .findAll();
        if (recordingNotes.size() == 0) {
            mRealm.executeTransaction(realm -> {
                RealmNote realmNote = realm.createObject(RealmNote.class);
                realmNote.setKind(Config.TYPE_RECORDING);
                realmNote.setKey(String.valueOf(UUID.randomUUID()));
                realmNote.setNoteTitle(noteTitle);
                realmNote.setNoteTime(TimeUtil.getFormatTime());
                realmNote.setRecordingPath(filePath);
                onSaveCallBack.saveSuccess();
            });
        } else {
            onSaveCallBack.saveFailed();
        }
    }
    
    /**
     * 删除逻辑
     *
     * @param filePath 文件路径
     */
    @Override
    public void drag(OnDragCallBack onDragCallBack, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            // 不存在
            onDragCallBack.fileNonentity();
        } else {
            // 存在，删除
            if (file.delete()) {
                // 删除成功
                onDragCallBack.dragSuccess();
            } else {
                // 删除失败
                onDragCallBack.dragFailed();
            }
        }
    }
    
    /**
     * 保存完回调
     */
    public interface OnSaveCallBack {
        /**
         * 保存成功
         */
        void saveSuccess();
        
        /**
         * 保存失败
         */
        void saveFailed();
    }
    
    /**
     * 删除回调接口
     */
    public interface OnDragCallBack {
        
        /**
         * 删除成功
         */
        void dragSuccess();
        
        /**
         * 删除失败
         */
        void dragFailed();
        
        /**
         * 文件不存在
         */
        void fileNonentity();
    }
    
}
