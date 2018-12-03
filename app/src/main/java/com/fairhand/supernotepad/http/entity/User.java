package com.fairhand.supernotepad.http.entity;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * 用户实体类
 *
 * @author Phanton
 * @date 2018/11/18 - 星期日 - 15:16
 */
public class User extends RealmObject {
    
    @Ignore
    private int loginResult;
    @PrimaryKey
    private String account;
    @Required
    private String password;
    private String nickName;
    private String avatar;
    
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
    
}
