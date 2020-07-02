package com.lingjoin.auth.entity;

import java.io.Serializable;

public class Operator implements Serializable {


    private Integer id;
    private String name;
    private Integer operaNum;

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

    public Integer getOperaNum() {
        return operaNum;
    }

    public void setOperaNum(Integer operaNum) {
        this.operaNum = operaNum;
    }

    public Operator(Integer id, String name, Integer operaNum) {
        this.id = id;
        this.name = name;
        this.operaNum = operaNum;
    }

    public Operator() {
    }
}
