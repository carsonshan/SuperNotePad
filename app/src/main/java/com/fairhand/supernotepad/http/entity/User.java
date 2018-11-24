package com.fairhand.supernotepad.http.entity;

import java.io.Serializable;

/**
 * 用户实体类
 *
 * @author Phanton
 * @date 2018/11/18 - 星期日 - 15:16
 */
public class User implements Serializable {
    
    private int id;
    private int loginResult;
    private String account;
    private String password;
    private String nickName;
    private String msg;
    private int code;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getLoginResult() {
        return loginResult;
    }
    
    public void setLoginResult(int loginResult) {
        this.loginResult = loginResult;
    }
    
    public String getAccount() {
        return account;
    }
    
    public void setAccount(String account) {
        this.account = account;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getNickName() {
        return nickName;
    }
    
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    
    public String getMsg() {
        return msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
}
