package com.fairhand.supernotepad.affair;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.entity.Affair;
import com.fairhand.supernotepad.entity.RealmAffair;
import com.fairhand.supernotepad.entity.RealmSecretAffair;
import com.fairhand.supernotepad.util.TimeUtil;
import com.fairhand.supernotepad.util.Toaster;
import com.fairhand.supernotepad.view.DiyCommonDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmObject;

/**
 * 事件细节
 *
 * @author Phanton
 * @date 11/25/2018 - Sunday - 8:45 PM
 */
public class AffairDetailActivity extends AppCompatActivity {
    
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_kind_name)
    TextView tvAffairTitle;
    @BindView(R.id.tv_affair_time)
    TextView tvAffairTime;
    @BindView(R.id.tv_affair_backup)
    TextView tvAffairBackup;
    @BindView(R.id.tv_day_off_describe)
    TextView tvDayOffDescribe;
    @BindView(R.id.tv_day_off)
    TextView tvDayOff;
    @BindView(R.id.tv_affair_today)
    TextView tvAffairToday;
    @BindView(R.id.ll_affair_day_off)
    LinearLayout llAffairDayOff;
    @BindView(R.id.iv_delete_affair)
    ImageView ivDeleteAffair;
    @BindView(R.id.iv_edit_affair)
    ImageView ivEditAffair;
    
    private Affair affair;
    
    private Realm realm;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affair_detail);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        
        Intent intent = getIntent();
        affair = (Affair) intent.getSerializableExtra("affair_entity");
        
        tvAffairTitle.setText(affair.getTitle());
        tvAffairTime.setText(affair.getTime());
        tvAffairBackup.setText(affair.getBackup());
        
        calculateDayOff();
    }
    
    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
    
    /**
     * 计算下要显示的天数
     */
    private void calculateDayOff() {
        String currentDate = TimeUtil.getDateToString(System.currentTimeMillis());
        long dayOff = TimeUtil.getDays(affair.getTime(), currentDate);
        if (dayOff == 0) {
            // 今天
            tvAffairToday.setVisibility(View.VISIBLE);
        } else {
            llAffairDayOff.setVisibility(View.VISIBLE);
            if (dayOff > 0) {
                // 还有dayOff天
                tvDayOffDescribe.setText("还有");
                tvDayOff.setText(String.valueOf(dayOff));
            } else {
                // 已过dayOff天
                tvDayOffDescribe.setText("已过");
                tvDayOff.setText(String.valueOf(Math.abs(dayOff)));
            }
        }
    }
    
    @OnClick( {R.id.iv_back, R.id.iv_delete_affair, R.id.iv_edit_affair})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_delete_affair:
                delete();
                break;
            case R.id.iv_edit_affair:
                edit();
                break;
            default:
                break;
        }
    }
    
    private void edit() {
        Intent intent = new Intent(AffairDetailActivity.this, AddAffairActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("affair_entity", affair);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    
    private void delete() {
        DiyCommonDialog dialog = new DiyCommonDialog(this, R.style.DiyDialogStyle);
        dialog.setCancelable(false);
        dialog.setTitle("提示")
                .setMessage("确认删除？")
                .setOnPositiveClickedListener("删除", v -> {
                    doDelete();
                    dialog.cancel();
                })
                .setOnNegativeClickListener("取消", v -> dialog.cancel())
                .show();
    }
    
    private void doDelete() {
        if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
            RealmAffair realmAffair =
                    realm.where(RealmAffair.class)
                            .equalTo("key", affair.getKey())
                            .findFirst();
            helpless(realmAffair);
        } else {
            RealmSecretAffair realmAffair =
                    realm.where(RealmSecretAffair.class)
                            .equalTo("key", affair.getKey())
                            .findFirst();
            helpless(realmAffair);
        }
    }
    
    /**
     * 难      受      的      不      得
     */
    private void helpless(RealmObject object) {
        if (object == null) {
            Toaster.showShort(AffairDetailActivity.this, "删除失败了");
        } else {
            realm.executeTransaction(realm -> object.deleteFromRealm());
            Toaster.showShort(AffairDetailActivity.this, "事件已删除");
            finish();
        }
    }
    
}
