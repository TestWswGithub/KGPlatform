package com.lingjoin.common.myenum;

public enum ConType {

    MYSQL("mysql"),
    HBASE("hbase"),
    SOLR("solr");

    public String getType() {
        return type;
    }

    private final String type;

    ConType(String type) {
        this.type = type;
    }


    public static ConType toConType(String type){
        return valueOf(type);
    }


    public static void main(String[] args) {
        ConType mysql = toConType("MYSQL");
        System.out.println(mysql.type);
    }
}
