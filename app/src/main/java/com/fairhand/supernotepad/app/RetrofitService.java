package com.fairhand.supernotepad.app;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Retrofit的简单封装
 *
 * @author Phanton
 * @date 11/29/2018 - Thursday - 10:15 PM
 */
public class RetrofitService {
    
    private volatile static RetrofitService instance;
    private Retrofit mRetrofit;
    
    /**
     * 默认超时时间
     */
    private static final int DEFAULT_TIME_OUT = 4;
    /**
     * 默认读写超时时间
     */
    private static final int DEFAULT_READ_WRITE_TIME_OUT = 8;
    
    private RetrofitService() {
        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 连接超时时间
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        // 写操作超时时间
        builder.writeTimeout(DEFAULT_READ_WRITE_TIME_OUT, TimeUnit.SECONDS);
        // 读操作超时时间
        builder.readTimeout(DEFAULT_READ_WRITE_TIME_OUT, TimeUnit.SECONDS);
        
        // 创建Retrofit
        mRetrofit = new Retrofit.Builder()
                            .baseUrl(Config.BASE_URL)
                            .client(builder.build())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .build();
    }
    
    /**
     * DCL返回单例
     */
    public static RetrofitService getInstance() {
        if (instance == null) {
            synchronized ((RetrofitService.class)) {
                if (instance == null) {
                    instance = new RetrofitService();
                }
            }
        }
        return instance;
    }
    
    /**
     * 获取对应的Service
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }
    
}
