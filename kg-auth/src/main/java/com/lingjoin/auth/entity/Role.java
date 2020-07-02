package com.lingjoin.auth.entity;

import java.io.Serializable;

public class Role implements Serializable {


    private Integer id;
    private String roleName;
    private Integer roleNum;
    private Integer uid;
    private String creatorName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleNum() {
        return roleNum;
    }

    public void setRoleNum(Integer roleNum) {
        this.roleNum = roleNum;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Role(Integer id, String roleName, Integer roleNum, Integer uid) {
        this.id = id;
        this.roleName = roleName;
        this.roleNum = roleNum;
        this.uid = uid;
    }

    public Role() {
    }
}
