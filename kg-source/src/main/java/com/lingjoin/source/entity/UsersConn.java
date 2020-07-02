package com.lingjoin.source.entity;

import java.io.Serializable;

public class UsersConn implements Serializable {


    private Integer id;
    private Integer conId;
    private Integer uid;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getConId() {
        return conId;
    }

    public void setConId(Integer conId) {
        this.conId = conId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public UsersConn() {
    }

    public UsersConn(Integer id, Integer conId, Integer uid) {
        this.id = id;
        this.conId = conId;
        this.uid = uid;
    }
}
