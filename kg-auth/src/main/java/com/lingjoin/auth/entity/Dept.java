package com.lingjoin.auth.entity;

import java.io.Serializable;

/**
 * 使用新设计的权限逻辑  此类弃用
 *
 * 使用新的权限系统实现部门逻辑  可由超级管理员创建一个拥有  新增用户  权限的账号（类似于部门管理员）。
 *
 * 再由部门管理员去创建子账号，那么这些子账号就可以由部门管理员管理。
 *
 */

@Deprecated
public class Dept implements Serializable {


    private Integer id;
    private String name;
    private Integer deptNum;

    public Integer getId() {
        return id;
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

    public Integer getDeptNum() {
        return deptNum;
    }

    public void setDeptNum(Integer deptNum) {
        this.deptNum = deptNum;
    }

    public Dept() {
    }
}
