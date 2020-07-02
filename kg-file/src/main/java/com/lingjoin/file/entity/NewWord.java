package com.lingjoin.file.entity;

import java.io.Serializable;
import java.util.Date;

public class NewWord implements Serializable {
    private Integer id;
    private Integer uid;
    private String name;
    private String time;
    private String uuid;
    private String infoe;
    private String fromDocName;

    private Date createDate;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFromDocName() {
        return fromDocName;
    }

    public void setFromDocName(String fromDocName) {
        this.fromDocName = fromDocName;
    }

    private Integer freqs;

    public Integer getFreqs() {
        return freqs;
    }

    public void setFreqs(Integer freqs) {
        this.freqs = freqs;
    }

    public Integer getFeq() {
        return feq;
    }

    public void setFeq(Integer feq) {
        this.feq = feq;
    }

    private Integer feq;



    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getInfoe() {
        return infoe;
    }

    public void setInfoe(String infoe) {
        this.infoe = infoe;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }





    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public NewWord() {

    }
}
