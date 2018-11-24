package com.fairhand.supernotepad.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairhand.supernotepad.R;
import com.fairhand.supernotepad.app.Config;
import com.fairhand.supernotepad.http.service.SignUpService;
import com.fairhand.supernotepad.util.Logger;
import com.fairhand.supernotepad.util.RegularExpressionUtil;
import com.fairhand.supernotepad.util.Toaster;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 注册界面
 *
 * @author FairHand
 * @date 2018/10/31
 */
public class SignUpActivity extends BaseActivity implements View.OnClickListener {
    
    @BindView(R.id.tv_pass)
    TextView tvPass;
    @BindView(R.id.et_pass)
    EditText etPass;
    @BindView(R.id.et_mob_code)
    EditText etMobCode;
    @BindView(R.id.et_mob_num)
    EditText etMobNum;
    @BindView(R.id.tv_mob)
    TextView tvMob;
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    @BindView(R.id.btn_send_code)
    Button btnSendCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.iv_closes)
    ImageView ivClose;
    
    private TimeCount mTimeCount;
    
    private Retrofit retrofit;
    
    /**
     * Intent传值key
     */
    public static final String KEY_INTENT_PHONE_NUMBER = "KEY_INTENT_PHONE_NUMBER";
    public static final String KEY_INTENT_PASSWORD = "KEY_INTENT_PASSWORD";
    
    /**
     * 手机号和密码
     */
    private String phoneNumber;
    private String password;
    
    private Integer signInResult;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        // 注册短信回调
        SMSSDK.registerEventHandler(mEventHandler);
        
        initView();
        initData();
        setAnimation();
    }
    
    /**
     * 初始化数据
     */
    private void initData() {
        // 60秒倒计时，速度1秒
        mTimeCount = new TimeCount(60 * 1000, 1000);
        ivClose.setOnClickListener(this);
        btnSendCode.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        
        // 初始化Retrofit
        retrofit = new Retrofit.Builder()
                           .baseUrl(Config.BASE_URL)
                           .addConverterFactory(ScalarsConverterFactory.create())
                           .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                           .build();
    }
    
    @Override
    protected void onDestroy() {
        // 取消注册短信回调
        SMSSDK.unregisterAllEventHandler();
        // 取消计时器
        mTimeCount.cancel();
        super.onDestroy();
    }
    
    /**
     * 初始化视图
     */
    private void initView() {
        // 改状态栏字体颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        
        // 设置验证码编辑框焦点监听
        etMobCode.setOnFocusChangeListener((v, hasFocus) -> {
            Logger.d("SignUpActivity测试", "高度" + v.getHeight());
            Logger.d("SignUpActivity测试", "top" + v.getTop());
            // 计算平移距离
            int translateDis = v.getTop() + v.getHeight() / 2 - tvCode.getTop() - tvCode.getHeight() / 2;
            Logger.d("SignUpActivity测试", "平移距离" + translateDis);
            if (hasFocus) {
                // EditText获得焦点
                if (TextUtils.isEmpty(etMobCode.getText().toString())) {
                    etMobCode.setHint("");
                    tvCode.setVisibility(View.VISIBLE);
                    // 设置平移动画
                    TranslateAnimation animation = new TranslateAnimation(
                            0, 0, translateDis, 0);
                    animation.setDuration(360);
                    tvCode.startAnimation(animation);
                }
            } else {
                // EditText失去焦点
                if (TextUtils.isEmpty(etMobCode.getText().toString())) {
                    tvCode.setVisibility(View.INVISIBLE);
                    etMobCode.setHint(R.string.code);
                }
            }
        });
    }
    
    /**
     * 设置动画
     */
    private void setAnimation() {
        // 从屏幕底部进入
        TranslateAnimation appear = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 1.0f,
                Animation.RELATIVE_TO_PARENT, 0f);
        appear.setDuration(888);
        // 设置开始结束缓慢，中间加速的插值器
        appear.setInterpolator(new AccelerateDecelerateInterpolator());
        // 启动动画
        tvPass.startAnimation(appear);
        etPass.startAnimation(appear);
        etMobCode.startAnimation(appear);
        etMobNum.startAnimation(appear);
        tvMob.startAnimation(appear);
        btnSignUp.startAnimation(appear);
        btnSendCode.startAnimation(appear);
    }
    
    /**
     * Mob发送验证码的事件处理类
     */
    EventHandler mEventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                // 回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    // 提交验证码成功
                    Logger.i("EventHandler", "提交验证码成功");
                    runOnUiThread(() -> submitRequest());
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 获取验证码成功
                    Logger.i("EventHandler", "获取验证码成功");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    // 返回支持发送验证码的国家列表
                    Logger.i("EventHandler", "返回支持发送验证码的国家列表");
                }
            } else {
                Logger.i("EventHandler", "回调失败：" + ((Throwable) data).getMessage());
            }
        }
    };
    
    /**
     * 向服务器提交请求
     */
    private void submitRequest() {
        // 设置用户数据（手机号、密码）
        Map<String, String> user = new HashMap<>(1);
        user.put("account", phoneNumber);
        user.put("password", password);
        
        SignUpService service = retrofit.create(SignUpService.class);
        service.enroll(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    Subscription subscription;
                    
                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        subscription.request(1);
                    }
                    
                    @Override
                    public void onNext(Integer result) {
                        Logger.d("返回的数据" + result);
                        signInResult = result;
                        subscription.request(1);
                        onComplete();
                    }
                    
                    @Override
                    public void onError(Throwable t) {
                        Logger.d("返回的出错" + t.getMessage());
                    }
                    
                    @Override
                    public void onComplete() {
                        handleSignIn();
                    }
                });
    }
    
    /**
     * 处理注册
     */
    private void handleSignIn() {
        if (signInResult == 0) {
            Toaster.showShort(this, "注册成功");
            // 跳转登录界面
            Intent intentToLogin = new Intent(SignUpActivity.this, SignInActivity.class);
            // 将手机号和密码传过去
            intentToLogin.putExtra(KEY_INTENT_PHONE_NUMBER, phoneNumber);
            intentToLogin.putExtra(KEY_INTENT_PASSWORD, password);
            startActivity(intentToLogin);
            finish();
        } else {
            Toaster.showShort(this, "该手机号已被注册，请换一个");
        }
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 关闭界面
            case R.id.iv_closes:
                onBackPressed();
                break;
            // 发送验证码
            case R.id.btn_send_code:
                sendCode();
                break;
            // 注册
            case R.id.btn_sign_up:
                signUp();
                break;
            default:
                break;
        }
    }
    
    /**
     * 注册
     */
    private void signUp() {
        judgeEnable(1);
    }
    
    /**
     * 发送验证码
     */
    private void sendCode() {
        judgeEnable(0);
    }
    
    /**
     * 判断输入是否可用
     *
     * @param kind 类型：0发送验证码 1提交注册
     */
    private void judgeEnable(int kind) {
        // 获取到输入的手机号和密码
        phoneNumber = etMobNum.getText().toString();
        password = etPass.getText().toString();
        // 判断手机号是否为空
        if (TextUtils.isEmpty(phoneNumber)) {
            Toaster.showShort(getApplicationContext(), "请输入手机号");
        }
        // 判断输入的手机号是否合法
        else if (RegularExpressionUtil.isMobileExact(phoneNumber)) {
            // 手机号合法
            if (TextUtils.isEmpty(password)) {
                // 没有设置密码
                Toaster.showShort(getApplicationContext(), "请设置密码");
            } else {
                if (kind == 0) {
                    // 设置了密码
                    // 调用发送短信接口
                    // 参数说明：国家代码，手机号码
                    // 发送成功后
                    // 回调EventHandler的afterEvent方法(event == SMSSDK.EVENT_GET_VERIFICATION_CODE)
                    SMSSDK.getVerificationCode("86", phoneNumber);
                    // 开始倒计时
                    mTimeCount.start();
                } else if (kind == 1) {
                    // 调用验证码验证接口
                    // 参数说明：国家代码，手机号码，验证码
                    // 验证成功后
                    // 回调EventHandler的afterEvent方法(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE)
                    SMSSDK.submitVerificationCode("86",
                            etMobNum.getText().toString(),
                            etMobCode.getText().toString());
                }
            }
        } else {
            // 手机号不合法
            Toaster.showShort(getApplicationContext(), "请输入有效的手机号后再重试");
        }
    }
    
    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {
        
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        
        /**
         * 正在计时
         */
        @Override
        public void onTick(long millisUntilFinished) {
            // 设置按钮不可用，开始倒计时
            btnSendCode.setClickable(false);
            btnSendCode.setText(getString(R.string.regain, (int) (millisUntilFinished / 1000)));
        }
        
        /**
         * 计时完毕触发
         */
        @Override
        public void onFinish() {
            // 设置按钮可用
            btnSendCode.setClickable(true);
            btnSendCode.setText("重新获取");
        }
    }
    
}
