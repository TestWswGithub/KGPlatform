package com.lingjoin.file.entity;


import java.io.Serializable;
import java.util.Date;

public class Doc implements Serializable {
    public Doc() {
    }

    private Integer docId;

    private String uuid;
    /**
     * 不带后缀的名字
     *
     */

    private String name;

    public String getSuffixName() {
        return suffixName;
    }

    public void setSuffixName(String suffixName) {
        this.suffixName = suffixName;
    }

    /**
     * 带后缀的名字
     */
    private String suffixName;




    private String path;

    private Integer uid;

    private String userName;

    private String type;
    private String timeStamp;

    private Date uploadTime;


    private Boolean delete;

    private String summary;
    private String keyWords;
    private String text;

    public Integer getKvCount() {
        return kvCount;
    }

    public void setKvCount(Integer kvCount) {
        this.kvCount = kvCount;
    }

    private Integer kvCount;



    private String nlpResult;


    private String wordfeq;

    public String getWordfeq() {
        return wordfeq;
    }

    public void setWordfeq(String wordfeq) {
        this.wordfeq = wordfeq;
    }

    public String getNlpResult() {
        return nlpResult;
    }

    public void setNlpResult(String nlpResult) {
        this.nlpResult = nlpResult;
    }

    public Integer getDocId() {

        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }







}
