package com.lingjoin.source.entity;

import java.io.Serializable;

public class CorpusEntry implements Serializable {

    private Integer id;

    public CorpusEntry(Integer id, String content) {
        this.id = id;
        this.content = content;
    }

    private String content;

    @Override
    public String toString() {
        return "CorpusEntry{" +
                "id=" + id +
                ", content=" + content +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CorpusEntry() {
    }
}
