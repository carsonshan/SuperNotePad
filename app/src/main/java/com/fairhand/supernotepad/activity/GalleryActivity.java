package com.fairhand.supernotepad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.adapter.PictureAdapter;
import com.fairhand.supernotepad.fragment.GalleryFragment;
import com.fairhand.supernotepad.view.NumberIndicator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static java.lang.Thread.sleep;

/**
 * 浏览已选图片
 *
 * @author FairHand
 * @date 2018/11/2
 */
public class GalleryActivity extends AppCompatActivity {
    
    @BindView(R.id.number_indicator)
    NumberIndicator numberIndicator;
    @BindView(R.id.view_pager_gallery)
    ViewPager viewPagerGallery;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        
        Intent intent = getIntent();
        // 获取传递过来的所有已选图片数据
        ArrayList<String> data = intent.getStringArrayListExtra(PictureAdapter.KEY_ALL_PICK_IMAGE);
        // 获取传递过来的点击位置
        final int position = intent.getIntExtra(PictureAdapter.KEY_PICK_POSITION, 0);
        numberIndicator.setTvCurrentNumber(String.valueOf(position + 1));
        numberIndicator.setTvTotalNumber(String.valueOf(data.size()));
        startCountIndicator();
        
        // 初始化Fragment
        final ArrayList<Fragment> fragments = new ArrayList<>();
        for (String path : data) {
            fragments.add(GalleryFragment.newInstance(path));
        }
        
        // 设置ViewPager适配器
        viewPagerGallery.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
            
            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        
        viewPagerGallery.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                numberIndicator.setVisibility(View.VISIBLE);
            }
            
            @Override
            public void onPageSelected(int position) {
                numberIndicator.setTvCurrentNumber(String.valueOf(position + 1));
            }
            
            @Override
            public void onPageScrollStateChanged(int state) {
                int up = 2;
                int end = 0;
                if (state == up || state == end) {
                    startCountIndicator();
                }
            }
        });
        
        // 设置当前位置为点击的位置
        viewPagerGallery.setCurrentItem(position);
    }
    
    boolean shouldExecute = true;
    
    private void startCountIndicator() {
        if (shouldExecute) {
            Observable.create(emitter -> {
                sleep(3200);
                shouldExecute = false;
                emitter.onNext("");
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Object>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }
                        
                        @Override
                        public void onNext(Object o) {
                            numberIndicator.setVisibility(View.GONE);
                            shouldExecute = true;
                        }
                        
                        @Override
                        public void onError(Throwable e) {
                        }
                        
                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }
    
}
