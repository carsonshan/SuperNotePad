package com.fairhand.supernotepad.http.service;

import com.fairhand.supernotepad.http.entity.User;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 用户接口对象
 *
 * @author Phanton
 * @date 11/29/2018 - Thursday - 10:06 PM
 */
public interface UserService {
    
    /**
     * 登录<br/>
     * POST使用Map多参数
     * FormUrlEncoded表示请求体是一个Form表单
     * 每个键值对需要用@Filed来注解键名，随后的对象需要提供值
     *
     * @param user 存放用户登录输入的手机号和密码
     * @return Flowable<User>
     */
    @FormUrlEncoded
    @POST("user/login")
    Flowable<User> login(@FieldMap Map<String, String> user);
    
    /**
     * 注册<br/>
     * POST使用Map多参数
     * FormUrlEncoded表示请求体是一个Form表单
     * 每个键值对需要用@Filed来注解键名，随后的对象需要提供值
     *
     * @param user 存放用户登录输入的手机号和密码
     * @return Flowable<Integer>
     */
    @FormUrlEncoded
    @POST("user/enroll")
    Flowable<Integer> enroll(@FieldMap Map<String, String> user);
    
    
    /**
     * 上传头像<br/>
     * Multipart文件上传
     *
     * @param userAccount user id
     * @param file        the file to upload
     * @return upload results
     */
    @Multipart
    @POST("user/upload_avatar")
    Observable<Integer> updateAvatar(@Part("user_account") RequestBody userAccount, @Part MultipartBody.Part file);
    
}
