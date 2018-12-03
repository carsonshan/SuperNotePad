package com.fairhand.supernotepad.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.app.UpdateNote;
import com.fairhand.supernotepad.entity.RealmNote;
import com.fairhand.supernotepad.entity.RealmSecretNote;
import com.fairhand.supernotepad.util.TimeUtil;
import com.fairhand.supernotepad.util.Toaster;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 普通记事界面
 *
 * @author FairHand
 * @date 2018/10/30
 */
public class CommonNoteActivity extends AppCompatActivity {
    
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.iv_save)
    ImageView ivSave;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    
    private Realm mRealm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_note);
        ButterKnife.bind(this);
        // 获取Realm实例
        mRealm = Realm.getDefaultInstance();
        
        ivSave.setOnClickListener(v -> save());
        ivBack.setOnClickListener(v -> finish());
    }
    
    @Override
    protected void onDestroy() {
        // 关闭Realm
        mRealm.close();
        super.onDestroy();
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
        RealmResults<RealmSecretNote> realmNotes =
                mRealm.where(RealmSecretNote.class)
                        .equalTo("noteTitle", title)
                        .findAll();
        if (realmNotes.size() == 0) {
            mRealm.executeTransaction(realm -> {
                RealmSecretNote realmNote = realm.createObject(RealmSecretNote.class);
                realmNote.setKind(Config.TYPE_COMMON);
                realmNote.setKey(String.valueOf(UUID.randomUUID()));
                realmNote.setNoteTitle(title);
                realmNote.setNoteContent(content);
                realmNote.setNoteTime(TimeUtil.getFormatTime());
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
    private void saveDefault(String title, String content) {
        RealmResults<RealmNote> realmNotes =
                mRealm.where(RealmNote.class)
                        .equalTo("noteTitle", title)
                        .findAll();
        if (realmNotes.size() == 0) {
            mRealm.executeTransaction(realm -> {
                RealmNote realmNote = realm.createObject(RealmNote.class);
                realmNote.setKind(Config.TYPE_COMMON);
                realmNote.setKey(String.valueOf(UUID.randomUUID()));
                realmNote.setNoteTitle(title);
                realmNote.setNoteContent(content);
                realmNote.setNoteTime(TimeUtil.getFormatTime());
                Toaster.showShort(this, "保存成功");
                mUpdateCallBack.updateMainView();
            });
        } else {
            // 重名
            Toaster.showShort(this, "记事标题已存在，换一个标题试试吧");
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
