package com.lingjoin.common.myenum;

public enum MarkFieldType {
    DATE("date"),
    INT("int");

    public String getType() {
        return type;
    }

    private final String type;

    MarkFieldType(String type) {
        this.type = type;
    }

    public static MarkFieldType toMarkFieldType(String type){
        return valueOf(type);
    }

}
