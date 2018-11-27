package com.fairhand.supernotepad.affair;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.entity.Affair;
import com.fairhand.supernotepad.entity.RealmAffair;
import com.fairhand.supernotepad.entity.RealmSecretAffair;
import com.fairhand.supernotepad.util.TimeUtil;
import com.fairhand.supernotepad.util.Toaster;
import com.fairhand.supernotepad.view.DiyEditBackupDialog;
import com.fairhand.supernotepad.view.DiyRemindDialog;
import com.fairhand.supernotepad.view.ItemView;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmAsyncTask;

/**
 * 添加事件
 *
 * @author Phanton
 * @date 11/21/2018 - Saturday - 8:44 PM
 */
public class AddAffairActivity extends AppCompatActivity {
    
    @BindView(R.id.et_input_affair)
    EditText etInputAffair;
    @BindView(R.id.iv_kind)
    ItemView ivKind;
    @BindView(R.id.iv_calender)
    ItemView ivCalender;
    @BindView(R.id.iv_remind)
    ItemView ivRemind;
    @BindView(R.id.iv_backup)
    ItemView ivBackup;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.iv_complete)
    ImageView ivComplete;
    @BindView(R.id.tv_affair_title)
    TextView tvAffairTitle;
    
    private String affairTime;
    private String backup;
    private TimePickerDialog dialog;
    private boolean affairRemind = true;
    
    public int currentPosition;
    private Affair affairKind;
    private List<Affair> kinds = new ArrayList<>();
    private Affair[] kindArray = {
            new Affair("生活", R.drawable.ic_life_kind),
            new Affair("工作", R.drawable.ic_work_kind)};
    
    private Realm realm;
    
    private Affair affair;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_affair);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        
        init();
        getData();
        pickAffairCalender();
    }
    
    @Override
    protected void onStop() {
        // 防止销毁时crash
        if (asyncTask != null && !asyncTask.isCancelled()) {
            asyncTask.cancel();
        }
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
    
    private void getData() {
        Intent intent = getIntent();
        affair = (Affair) intent.getSerializableExtra("affair_entity");
        if (affair != null) {
            tvAffairTitle.setText("编辑事件");
            ivKind.setRightText(affair.getKindName());
            String affairTitle = affair.getTitle();
            etInputAffair.setText(affairTitle);
            etInputAffair.setSelection(affairTitle.length());
            ivCalender.setRightText(affair.getTime());
            backup = affair.getBackup();
            ivBackup.setRightText(backup);
            affairRemind = affair.isRemind();
            ivRemind.setRightText(affairRemind ? "整点提醒" : "不提醒");
        }
    }
    
    private void init() {
        kinds.addAll(Arrays.asList(kindArray));
        affairKind = kinds.get(0);
        affairTime = TimeUtil.getDateToString(System.currentTimeMillis());
        ivCalender.setRightText(affairTime);
    }
    
    @OnClick( {R.id.iv_cancel, R.id.iv_complete, R.id.iv_kind, R.id.iv_calender, R.id.iv_remind, R.id.iv_backup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_cancel:
                finish();
                break;
            case R.id.iv_complete:
                if (affair != null) {
                    update();
                } else {
                    if (!TextUtils.isEmpty(etInputAffair.getText().toString())) {
                        save();
                    } else {
                        Toaster.showShort(this, "请先输入事件名称");
                    }
                }
                break;
            case R.id.iv_kind:
                pickAffairKind();
                break;
            case R.id.iv_calender:
                dialog.show(getSupportFragmentManager(), "all");
                break;
            case R.id.iv_remind:
                pickAffairRemind();
                break;
            case R.id.iv_backup:
                pickAffairBackup();
                break;
            default:
                break;
        }
    }
    
    private void update() {
        realm.executeTransaction(realm -> {
            if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
                updateDefault(realm);
            } else if (Config.currentPad.equals(Config.SECRET_PAD)) {
                updateSecret(realm);
            }
        });
    }
    
    private void updateSecret(Realm realm) {
        RealmSecretAffair realmAffair = realm.where(RealmSecretAffair.class).equalTo("key", affair.getKey()).findFirst();
        if (realmAffair != null) {
            realmAffair.setTitle(etInputAffair.getText().toString());
            realmAffair.setTime(affairTime);
            realmAffair.setKindIcon(affairKind.getKindIcon());
            realmAffair.setKindName(affairKind.getKindName());
            realmAffair.setBackup(backup);
            realmAffair.setRemind(affairRemind);
            Toaster.showShort(this, "更新完成");
            finish();
        } else {
            Toaster.showShort(this, "更新失败了");
        }
    }
    
    private void updateDefault(Realm realm) {
        RealmAffair realmAffair = realm.where(RealmAffair.class).equalTo("key", affair.getKey()).findFirst();
        if (realmAffair != null) {
            realmAffair.setTitle(etInputAffair.getText().toString());
            realmAffair.setTime(affairTime);
            realmAffair.setBackup(backup);
            realmAffair.setKindIcon(affairKind.getKindIcon());
            realmAffair.setKindName(affairKind.getKindName());
            realmAffair.setRemind(affairRemind);
            Toaster.showShort(this, "更新完成");
            finish();
        } else {
            Toaster.showShort(this, "更新失败了");
        }
    }
    
    RealmAsyncTask asyncTask;
    
    private void save() {
        asyncTask = realm.executeTransactionAsync(realm -> {
            if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
                saveDefault(realm);
            } else if (Config.currentPad.equals(Config.SECRET_PAD)) {
                saveSecret(realm);
            }
        }, () -> {
            Toaster.showShort(AddAffairActivity.this, "保存成功");
            finish();
        }, error -> Toaster.showShort(AddAffairActivity.this, "保存失败" + error.getMessage()));
    }
    
    private void saveDefault(Realm realm) {
        RealmAffair realmAffair = realm.createObject(RealmAffair.class);
        realmAffair.setKey(UUID.randomUUID() + "");
        realmAffair.setTitle(etInputAffair.getText().toString());
        realmAffair.setTime(affairTime);
        realmAffair.setKindIcon(affairKind.getKindIcon());
        realmAffair.setKindName(affairKind.getKindName());
        realmAffair.setBackup(backup);
        realmAffair.setRemind(affairRemind);
    }
    
    private void saveSecret(Realm realm) {
        RealmSecretAffair secretAffair = realm.createObject(RealmSecretAffair.class);
        secretAffair.setKey(UUID.randomUUID() + "");
        secretAffair.setTitle(etInputAffair.getText().toString());
        secretAffair.setTime(affairTime);
        secretAffair.setKindIcon(affairKind.getKindIcon());
        secretAffair.setKindName(affairKind.getKindName());
        secretAffair.setBackup(backup);
        secretAffair.setRemind(affairRemind);
    }
    
    private void pickAffairBackup() {
        DiyEditBackupDialog dialog = new DiyEditBackupDialog(this, R.style.DiyDialogStyle);
        dialog.setCancelable(false);
        dialog.setTvConfirmListener(v -> {
            backup = dialog.getEtBackup();
            ivBackup.setRightText(backup);
            dialog.cancel();
        });
        dialog.setEtBackup(backup);
        dialog.show();
    }
    
    private void pickAffairRemind() {
        DiyRemindDialog dialog = new DiyRemindDialog(this, R.style.DiyDialogStyle);
        dialog.setCancelable(false);
        dialog.setOnNotRemindListener(v -> {
            ivRemind.setRightText("不提醒");
            affairRemind = false;
            dialog.cancel();
        }).setOnRemindPunctualityListener(v -> {
            ivRemind.setRightText("整点提醒");
            dialog.cancel();
            affairRemind = true;
        }).show();
    }
    
    private void pickAffairCalender() {
        long threeYears = 3L * 365 * 1000 * 60 * 60 * 24L;
        dialog = new TimePickerDialog.Builder()
                         .setCallBack((timePickerView, millseconds) -> {
                             affairTime = TimeUtil.getDateToString(millseconds);
                             ivCalender.setRightText(affairTime);
                         })
                         .setCancelStringId("取消")
                         .setSureStringId("确定")
                         .setTitleStringId("选择日期")
                         .setYearText("年")
                         .setMonthText("月")
                         .setMinMillseconds(System.currentTimeMillis() - threeYears)
                         .setMaxMillseconds(System.currentTimeMillis() + threeYears)
                         .setCurrentMillseconds(System.currentTimeMillis())
                         .setDayText("日")
                         .setHourText("时")
                         .setMinuteText("分")
                         .setCyclic(false)
                         .setThemeColor(ContextCompat.getColor(this, R.color.colorItem))
                         .setWheelItemTextNormalColor(ContextCompat.getColor(this, R.color.timetimepicker_default_text_color))
                         .setWheelItemTextSelectorColor(ContextCompat.getColor(this, R.color.colorItem))
                         .setType(Type.ALL)
                         .setWheelItemTextSize(12)
                         .build();
    }
    
    private void pickAffairKind() {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.TransparentBottomSheetStyle);
        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_bottom_sheet, rootView, false);
        dialog.setContentView(view);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_bottom_sheet);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AffairKindAdapter adapter = new AffairKindAdapter(this, kinds);
        recyclerView.setAdapter(adapter);
        dialog.show();
    }
    
    /**
     * 事件类型RecyclerView的适配器，由于要用到某些字段，索性写成内部类了
     */
    class AffairKindAdapter extends RecyclerView.Adapter<AffairKindAdapter.ViewHolder> {
        
        private Context context;
        private List<Affair> kinds;
        
        AffairKindAdapter(Context context, List<Affair> kinds) {
            this.context = context;
            this.kinds = kinds;
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_view_affair_kind, viewGroup, false);
            ViewHolder holder = new ViewHolder(view);
            holder.cardView.setOnClickListener(v -> {
                currentPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
                ivKind.setRightText(kinds.get(currentPosition).getKindName());
                affairKind = kinds.get(currentPosition);
            });
            return holder;
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            Affair kind = this.kinds.get(i);
            viewHolder.kindName.setText(kind.getKindName());
            viewHolder.kindImage.setImageResource(kind.getKindIcon());
            
            if (i == currentPosition) {
                viewHolder.currentKind.setVisibility(View.VISIBLE);
            } else {
                viewHolder.currentKind.setVisibility(View.GONE);
            }
        }
        
        @Override
        public int getItemCount() {
            return kinds.size();
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView kindName;
            ImageView kindImage;
            ImageView currentKind;
            CardView cardView;
            
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                cardView = itemView.findViewById(R.id.cardView_affair_kind);
                kindName = itemView.findViewById(R.id.tv_kind_name);
                kindImage = itemView.findViewById(R.id.iv_kind_icon);
                currentKind = itemView.findViewById(R.id.iv_current_affair_kind);
            }
        }
    }
    
}
