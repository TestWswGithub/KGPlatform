package com.lingjoin.auth.entity;

import java.io.Serializable;

public class RoleOperaMap implements Serializable {

    private Integer id;
    private Integer roleId;
    private Integer operaId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getOperaId() {
        return operaId;
    }

    public void setOperaId(Integer operaId) {
        this.operaId = operaId;
    }

    public RoleOperaMap(Integer id, Integer roleId, Integer operaId) {
        this.id = id;
        this.roleId = roleId;
        this.operaId = operaId;
    }

    public RoleOperaMap() {
    }
}
