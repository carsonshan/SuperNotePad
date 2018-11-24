package com.fairhand.supernotepad.http.service;

import com.fairhand.supernotepad.http.entity.User;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 用户登录接口对象
 *
 * @author FairHand
 * @date 2018/11/1
 */
public interface LoginService {
    
    /**
     * 登录<br/>
     * POST使用Map多参数
     *
     * @param user 存放用户登录输入的手机号和密码
     * @return Flowable<User>
     */
    @FormUrlEncoded
    @POST("login")
    Flowable<User> login(@FieldMap Map<String, String> user);
}
