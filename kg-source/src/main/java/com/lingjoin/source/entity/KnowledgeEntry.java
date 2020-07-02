package com.lingjoin.source.entity;

import java.io.Serializable;

public class KnowledgeEntry implements Serializable {

    private Integer id;
    private String headField;
    private String relField;
    private String tailField;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHeadField() {
        return headField;
    }

    public void setHeadField(String headField) {
        this.headField = headField;
    }

    public String getRelField() {
        return relField;
    }

    public void setRelField(String relField) {
        this.relField = relField;
    }

    public String getTailField() {
        return tailField;
    }

    public void setTailField(String tailField) {
        this.tailField = tailField;
    }

    public KnowledgeEntry() {
    }

    public KnowledgeEntry(Integer id, String headField, String relField, String tailField) {
        this.id = id;
        this.headField = headField;
        this.relField = relField;
        this.tailField = tailField;
    }
}
