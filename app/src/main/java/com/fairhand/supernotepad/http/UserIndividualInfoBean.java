package com.fairhand.supernotepad.http;

import java.io.Serializable;

public class UserIndividualInfoBean implements Serializable {
    
    private int error;//1没有此账号， 2密码错误, 3登录， 4学号姓名不匹配， 5学号已注册
    private int id;
    private String username; //手机号
    private String tname;   //真实姓名
    private String name;
    private String vname = "";   //用户名
    private String age;
    private String sex;
    private String xuehao;
    private String jifen;
    private String school;
    private int quanxian;   //1学生， 2辅导员， 3体育教师，4学校
    private String licheng;  //计入成绩
    private String zonglicheng; //总里程
    private Double Xueqilicheng; //学期目标
    private String time;
    private String touxiang;
    private int stautus;  //状态
    private int zongpaiming;
    private int gidcount; //关注
    private int fidcount; //粉丝
    private String dengji;
    private String shebei;
    private String team;   //战队
    private String TeamPermission; //战队职务
    
    /**
     * error
     * 0账号登录成功
     * 1账号不存在
     * 2 密码错误
     **/
    
    public UserIndividualInfoBean() {
    }
    
    
    public UserIndividualInfoBean(int id, String tname, String vname
            , String age, String sex, String xuehao, String jifen, String school, int quanxian, String time, String touxiang
            , int status, String licheng, String zonglicheng, String shebei, int paiming) {
        this.id = id;
        this.tname = tname;
        this.vname = vname;
        this.age = age;
        this.sex = sex;
        this.xuehao = xuehao;
        this.jifen = jifen;
        this.school = school;
        this.quanxian = quanxian;
        this.time = time;
        this.touxiang = touxiang;
        this.stautus = status;
        this.licheng = licheng;
        this.zonglicheng = zonglicheng;
        this.shebei = shebei;
        this.zongpaiming = paiming;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getTname() {
        return tname;
    }
    
    public void setTname(String tname) {
        this.tname = tname;
    }
    
    public String getVname() {
        return vname;
    }
    
    public void setVname(String vname) {
        this.vname = vname;
    }
    
    public String getAge() {
        return age;
    }
    
    public void setAge(String age) {
        this.age = age;
    }
    
    public String getSex() {
        return sex;
    }
    
    public void setSex(String sex) {
        this.sex = sex;
    }
    
    public String getXuehao() {
        return xuehao;
    }
    
    public void setXuehao(String xuehao) {
        this.xuehao = xuehao;
    }
    
    public String getJifen() {
        return jifen;
    }
    
    public void setJifen(String jifen) {
        this.jifen = jifen;
    }
    
    public String getSchool() {
        return school;
    }
    
    public void setSchool(String school) {
        this.school = school;
    }
    
    public int getQuanxian() {
        return quanxian;
    }
    
    public void setQuanxian(int quanxian) {
        this.quanxian = quanxian;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public int getError() {
        return error;
    }
    
    public void setError(int error) {
        this.error = error;
    }
    
    public Double getXueqilicheng() {
        return Xueqilicheng;
    }
    
    public void setXueqilicheng(Double xueqilicheng) {
        Xueqilicheng = xueqilicheng;
    }
    
    
    public int getStautus() {
        return stautus;
    }
    
    public void setStautus(int stautus) {
        this.stautus = stautus;
    }
    
    public int getZongpaiming() {
        return zongpaiming;
    }
    
    public void setZongpaiming(int zongpaiming) {
        this.zongpaiming = zongpaiming;
    }
    
    public int getGidcount() {
        return gidcount;
    }
    
    public void setGidcount(int gidcount) {
        this.gidcount = gidcount;
    }
    
    public int getFidcount() {
        return fidcount;
    }
    
    public void setFidcount(int fidcount) {
        this.fidcount = fidcount;
    }
    
    
    public String getDengji() {
        return dengji;
    }
    
    
    public void setDengji(String dengji) {
        this.dengji = dengji;
    }
    
    
    public String getShebei() {
        return shebei;
    }
    
    
    public void setShebei(String shebei) {
        this.shebei = shebei;
    }
    
    
    public String getTouxiang() {
        return touxiang;
    }
    
    
    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }
    
    
    public String getLicheng() {
        return licheng;
    }
    
    
    public void setLicheng(String licheng) {
        this.licheng = licheng;
    }
    
    
    public String getZonglicheng() {
        return zonglicheng;
    }
    
    
    public void setZonglicheng(String zonglicheng) {
        this.zonglicheng = zonglicheng;
    }
    
    
    public String getTeam() {
        return team;
    }
    
    public void setTeam(String team) {
        this.team = team;
    }
    
    public String getTeamPermission() {
        return TeamPermission;
    }
    
    public void setTeamPermission(String teamPermission) {
        TeamPermission = teamPermission;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
