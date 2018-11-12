package com.fairhand.supernotepad.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.util.CacheUtil;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.util.Toaster;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * DIY密码面板
 *
 * @author Phanton
 * @date 2018/11/11 - 星期日 - 10:43
 */
public class DiyLockPanel extends LinearLayout implements View.OnClickListener {
    
    @BindView(R.id.spv_1)
    SinglePasswordView spv1;
    @BindView(R.id.spv_2)
    SinglePasswordView spv2;
    @BindView(R.id.spv_3)
    SinglePasswordView spv3;
    @BindView(R.id.spv_4)
    SinglePasswordView spv4;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.tv_5)
    TextView tv5;
    @BindView(R.id.tv_6)
    TextView tv6;
    @BindView(R.id.tv_7)
    TextView tv7;
    @BindView(R.id.tv_8)
    TextView tv8;
    @BindView(R.id.tv_9)
    TextView tv9;
    @BindView(R.id.tv_0)
    TextView tv0;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_psd_tip)
    TextView tvPsdTip;
    
    /**
     * 当前输入密码
     */
    private StringBuilder currentInputPsd = new StringBuilder();
    
    private String finalPsd;
    
    private boolean isSecondInput;
    
    public DiyLockPanel(Context context) {
        this(context, null);
    }
    
    public DiyLockPanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public DiyLockPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        // 加载布局
        LayoutInflater.from(getContext()).inflate(R.layout.diy_lock_panel, this);
        ButterKnife.bind(this);
        
        if (Config.mSecret != null) {
            tvPsdTip.setText("输入密码");
        }
        
        tv0.setOnClickListener(this);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tv8.setOnClickListener(this);
        tv9.setOnClickListener(this);
        
        tv0.setTag(0);
        tv1.setTag(1);
        tv2.setTag(2);
        tv3.setTag(3);
        tv4.setTag(4);
        tv5.setTag(5);
        tv6.setTag(6);
        tv7.setTag(7);
        tv8.setTag(8);
        tv9.setTag(9);
        
        tvDelete.setOnClickListener(v -> delPsd());
    }
    
    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        addPsd(tag);
    }
    
    /**
     * 删除一位密码
     */
    private void delPsd() {
        if (currentInputPsd.length() > 0) {
            setPsd(currentInputPsd.length(), false);
            currentInputPsd.deleteCharAt(currentInputPsd.length() - 1);
        }
    }
    
    /**
     * 设置一位密码
     */
    private void addPsd(int tag) {
        int psdCount = 4;
        if (isSecondInput) {
            // 第二次输入
            currentInputPsd.append(tag);
            setPsd(currentInputPsd.length(), true);
            if (currentInputPsd.length() == psdCount) {
                // 输入完成了
                if (!finalPsd.equals(String.valueOf(currentInputPsd))) {
                    // 两次不一致
                    Toaster.showShort(getContext(), "两次输入不一致，请重新输入");
                    clear();
                } else {
                    Toaster.showShort(getContext(), "设置完成，再次输入密码开启私密记事本");
                    CacheUtil.putPassword(getContext(), finalPsd);
                    Config.mSecret = finalPsd;
                    isSecondInput = false;
                    tvPsdTip.setText("输入密码");
                    clear();
                }
            }
        } else {
            // 第一次输入
            currentInputPsd.append(tag);
            setPsd(currentInputPsd.length(), true);
            if (currentInputPsd.length() == psdCount) {
                finalPsd = String.valueOf(currentInputPsd);
                // 输入完成了
                if (Config.mSecret != null) {
                    // 设置过密码
                    if (Config.mSecret.equals(finalPsd)) {
                        // 密码正确
                        mOnPasswordSetComplete.onCheckSuccess();
                        Toaster.showShort(getContext(), "密码输入正确");
                    } else {
                        // 密码错误
                        clear();
                        Logger.d("正确密码：" + Config.mSecret);
                        Toaster.showShort(getContext(), "密码错误");
                    }
                } else {
                    // 未设置过密码
                    clear();
                    isSecondInput = true;
                    Logger.d("最终密码：" + finalPsd);
                    tvPsdTip.setText("确认密码");
                }
            }
        }
    }
    
    /**
     * 设置密码位的填充
     */
    private void setPsd(int position, boolean isAdd) {
        switch (position) {
            case 1:
                if (isAdd) {
                    spv1.setFillStyle();
                } else {
                    spv1.setStrokeStyle();
                }
                break;
            case 2:
                if (isAdd) {
                    spv2.setFillStyle();
                } else {
                    spv2.setStrokeStyle();
                }
                break;
            case 3:
                if (isAdd) {
                    spv3.setFillStyle();
                } else {
                    spv3.setStrokeStyle();
                }
                break;
            case 4:
                if (isAdd) {
                    spv4.setFillStyle();
                } else {
                    spv4.setStrokeStyle();
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 清空密码位
     */
    public void clear() {
        spv1.setStrokeStyle();
        spv2.setStrokeStyle();
        spv3.setStrokeStyle();
        spv4.setStrokeStyle();
        currentInputPsd.delete(0, currentInputPsd.length());
    }
    
    private OnPasswordSetComplete mOnPasswordSetComplete;
    
    public void setCallBack(OnPasswordSetComplete onPasswordSetComplete) {
        mOnPasswordSetComplete = onPasswordSetComplete;
    }
    
    /**
     * 密码设置完成回调接口
     */
    public interface OnPasswordSetComplete {
        
        /**
         * 密码正确
         */
        void onCheckSuccess();
    }
    
}
