package com.lingjoin.graph.entity;

import java.io.Serializable;

public class Tuple implements Serializable {


    private Integer id;
    private Integer uid;
    private String source;
    private String name;
    private String attr;
    private String attrValue;

    public Tuple(Integer id, Integer uid, String source, String name, String attr, String attrValue) {
        this.id = id;
        this.uid = uid;
        this.source = source;
        this.name = name;
        this.attr = attr;
        this.attrValue = attrValue;
    }

    public Tuple() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }
}
