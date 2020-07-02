package com.lingjoin.web.util;

public class KeyWordVO {

    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public KeyWordVO(String name, int value) {
        this.name = name;
        this.value = value;
    }
}
