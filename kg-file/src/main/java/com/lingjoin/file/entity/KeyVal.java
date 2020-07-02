package com.lingjoin.file.entity;

import java.io.Serializable;

public class KeyVal implements Serializable {

    private Integer id;
    private Integer uid;
    private String  uuid;
    private String time;
    private String name;
    private String val;
    private String src;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyVal keyVal = (KeyVal) o;

        if (name != null ? !name.equals(keyVal.name) : keyVal.name != null) return false;
        return val != null ? val.equals(keyVal.val) : keyVal.val == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (val != null ? val.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "KeyVal{" +
                "id=" + id +
                ", uid=" + uid +
                ", uuid='" + uuid + '\'' +
                ", time='" + time + '\'' +
                ", name='" + name + '\'' +
                ", val='" + val + '\'' +
                ", src='" + src + '\'' +
                '}';
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public KeyVal(Integer uid, String uuid, String time, String name, String val, String src) {

        this.uid = uid;
        this.uuid = uuid;
        this.time = time;
        this.name = name;
        this.val = val;
        this.src = src;
    }

    public KeyVal() {

    }
}
