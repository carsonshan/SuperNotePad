package com.fairhand.supernotepad.http.service;

import java.util.Map;

import io.reactivex.Flowable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 用户注册接口对象
 *
 * @author Phanton
 * @date 2018/11/15 - 星期四 - 12:06
 */
public interface SignUpService {
    
    /**
     * 注册<br/>
     * POST使用Map多参数
     *
     * @param user 存放用户登录输入的手机号和密码
     * @return Flowable<Integer>
     */
    @FormUrlEncoded
    @POST("enroll")
    Flowable<Integer> enroll(@FieldMap Map<String, String> user);
}
