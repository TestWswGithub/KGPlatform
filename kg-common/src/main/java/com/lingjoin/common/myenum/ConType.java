package com.lingjoin.common.myenum;

public enum ConType {

    MYSQL("mysql"),
    HBASE("hbase"),
    SOLR("solr");

    public String getType() {
        return type;
    }

    private final String type;

    private ConType(String type) {
        this.type = type;
    }


    public static ConType toConType(String type){
        return valueOf(type);
    }
}
