package com.lingjoin.auth.entity;

import java.io.Serializable;

public class User implements Serializable {

    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 用户账号
     */
    private String account;
    /**
     * 用户密码
     */
    private String password;

    /**
     * 创建者ID
     *
     * 超级管理员无创建者
     *
     */
    private Integer creatorId;
    @Deprecated
    private Integer deptId;
    @Deprecated
    private String deptName;
    /**
     * 用户类型，为了前端展示 增加的成员变量
     * 数据库中无此字段
     *
     */
    private String userType;
    /**
     * 用户类型id
     *
     * 类似于 角色ID
     *
     */
    private Integer userTypeInt;


    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }
    public Integer getId() {
        return id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getUserTypeInt() {
        return userTypeInt;
    }

    public void setUserTypeInt(Integer userTypeInt) {
        this.userTypeInt = userTypeInt;
    }

    public User() {
    }

    public User(Integer id, String name, String account, String password, Integer creatorId, Integer userTypeInt) {
        this.id = id;
        this.name = name;
        this.account = account;
        this.password = password;
        this.creatorId = creatorId;
        this.userTypeInt = userTypeInt;
    }
}
