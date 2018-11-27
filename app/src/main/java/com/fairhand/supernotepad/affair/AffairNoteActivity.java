package com.fairhand.supernotepad.affair;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.adapter.AffairAdapter;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.entity.Affair;
import com.fairhand.supernotepad.entity.RealmAffair;
import com.fairhand.supernotepad.entity.RealmSecretAffair;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.util.Toaster;
import com.fairhand.supernotepad.view.DiyInputDialog;
import com.fairhand.supernotepad.view.ItemView;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 事件记事 好吧。
 *
 * @author Phanton
 * @date 11/21/2018 - Saturday - 8:44 PM
 */
public class AffairNoteActivity extends AppCompatActivity {
    
    @BindView(R.id.recyclerView_affair)
    RecyclerView recyclerViewAffair;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.tv_affair_non_tip)
    TextView tvAffairNonTip;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    
    private List<Affair> affairs = new ArrayList<>();
    private List<Affair> backup = new ArrayList<>();
    private AffairAdapter adapter;
    
    private Realm realm;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affair_note);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        
        initTool();
        
        StaggeredGridLayoutManager manager
                = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewAffair.setLayoutManager(manager);
        adapter = new AffairAdapter(this, affairs);
        recyclerViewAffair.setAdapter(adapter);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
    
    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }
    
    private void initTool() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        swipeRefresh.setColorSchemeResources(R.color.colorItem);
        swipeRefresh.setOnRefreshListener(this::refresh);
    }
    
    private void setDefaultData() {
        RealmResults<RealmAffair> realmAffairs = realm.where(RealmAffair.class).findAll();
        Logger.d("大小------------" + realmAffairs.size());
        affairs.clear();
        if (realmAffairs.size() > 0) {
            for (RealmAffair item : realmAffairs) {
                Affair affair = new Affair();
                affairs.add(affair);
                affair.setKey(item.getKey());
                affair.setRemind(item.isRemind());
                affair.setBackup(item.getBackup());
                affair.setTime(item.getTime());
                affair.setTitle(item.getTitle());
                affair.setKindIcon(item.getKindIcon());
                affair.setKindName(item.getKindName());
            }
        }
    }
    
    private void setSecretData() {
        RealmResults<RealmSecretAffair> realmAffairs = realm.where(RealmSecretAffair.class).findAll();
        Logger.d("大小------------" + realmAffairs.size());
        affairs.clear();
        if (realmAffairs.size() > 0) {
            for (RealmSecretAffair item : realmAffairs) {
                Affair affair = new Affair();
                affairs.add(affair);
                affair.setKey(item.getKey());
                affair.setRemind(item.isRemind());
                affair.setBackup(item.getBackup());
                affair.setTime(item.getTime());
                affair.setTitle(item.getTitle());
                affair.setKindName(item.getKindName());
                affair.setKindIcon(item.getKindIcon());
            }
        }
    }
    
    @OnClick(R.id.fab)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab:
                // 启动添加事件Activity
                startActivity(new Intent(AffairNoteActivity.this, AddAffairActivity.class));
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
    }
    
    private void refresh() {
        if (Config.currentPad.equals(Config.DEFAULT_PAD)) {
            setDefaultData();
        } else {
            setSecretData();
        }
        backup.clear();
        backup.addAll(affairs);
        adapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
        showOrHideTip();
    }
    
    private void showOrHideTip() {
        if (affairs == null || affairs.size() <= 0) {
            tvAffairNonTip.setVisibility(View.VISIBLE);
        } else {
            tvAffairNonTip.setVisibility(View.GONE);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.it_search:
                search();
                break;
            case R.id.it_classify:
                classify();
                break;
            default:
                break;
        }
        return true;
    }
    
    private void classify() {
        EasyPopup mEasyPopup = EasyPopup.create(this)
                                       .setContentView(R.layout.popup_window_affair_classify)
                                       .setFocusAndOutsideEnable(true)
                                       .setBackgroundDimEnable(true)
                                       .setDimValue(.4f)
                                       .setDimColor(ContextCompat.getColor(this, R.color.colorDim))
                                       .apply();
        // 设置显示位置
        mEasyPopup.showAtAnchorView(toolbar, YGravity.BELOW, XGravity.ALIGN_RIGHT, 24, 24);
        
        ItemView all = mEasyPopup.findViewById(R.id.iv_affair_classify_all);
        ItemView life = mEasyPopup.findViewById(R.id.iv_affair_classify_life);
        ItemView work = mEasyPopup.findViewById(R.id.iv_affair_classify_work);
        all.setOnClickListener(v -> {
            chooseAllAffair();
            mEasyPopup.dismiss();
        });
        life.setOnClickListener(v -> {
            chooseAffair("生活");
            mEasyPopup.dismiss();
        });
        work.setOnClickListener(v -> {
            chooseAffair("工作");
            mEasyPopup.dismiss();
        });
    }
    
    private void chooseAllAffair() {
        affairs.clear();
        affairs.addAll(backup);
        adapter.notifyDataSetChanged();
        showOrHideTip();
    }
    
    private void chooseAffair(String chooseKind) {
        affairs.clear();
        for (Affair affair : backup) {
            if (chooseKind.equals(affair.getKindName())) {
                affairs.add(affair);
            }
        }
        adapter.notifyDataSetChanged();
        showOrHideTip();
    }
    
    private void search() {
        DiyInputDialog dialog = new DiyInputDialog(this, R.style.DiyDialogStyle);
        dialog.setTitle("搜索事件")
                .setOnPositiveClickedListener("搜索", v -> {
                    String content = dialog.getMessage();
                    if (TextUtils.isEmpty(content)) {
                        Toaster.showShort(AffairNoteActivity.this, "请输入搜索内容");
                    } else {
                        doSearch(content);
                        dialog.cancel();
                    }
                })
                .setOnNegativeClickListener("取消", v -> dialog.cancel())
                .show();
    }
    
    private void doSearch(String content) {
        affairs.clear();
        for (Affair affair : backup) {
            if (affair.getTitle().contains(content)
                        || affair.getBackup().contains(content)
                        || affair.getTime().contains(content)) {
                affairs.add(affair);
            }
        }
        adapter.notifyDataSetChanged();
        showOrHideTip();
    }
    
}
