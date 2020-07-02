package com.lingjoin.source.entity;

import java.io.Serializable;
import java.util.Date;

public class Connection implements Serializable {

    private Integer id;
    private String connType;
    private String sourceType;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private String host;
    private Integer port;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    private String conn;
    private String database;
    private String table;
    private String field;
    private String user;
    private String password;
    private String databaseType;
    private String driver;
    private Date createDate;
    private String markType;
    private String markField;
    private String headField;
    private String relField;
    private String tailField;

    @Override
    public String toString() {
        return "Connection{" +
                "id=" + id +
                ", connType='" + connType + '\'' +
                ", conn='" + conn + '\'' +
                ", database='" + database + '\'' +
                ", table='" + table + '\'' +
                ", field='" + field + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", databaseType='" + databaseType + '\'' +
                ", driver='" + driver + '\'' +
                ", createDate=" + createDate +
                ", markType='" + markType + '\'' +
                ", markField='" + markField + '\'' +
                ", headField='" + headField + '\'' +
                ", relField='" + relField + '\'' +
                ", tailField='" + tailField + '\'' +
                '}';
    }

    public Connection() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConnType() {
        return connType;
    }

    public void setConnType(String connType) {
        this.connType = connType;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
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

    public String getConn() {
        return conn;
    }

    public void setConn(String conn) {
        this.conn = conn;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getMarkType() {
        return markType;
    }

    public void setMarkType(String markType) {
        this.markType = markType;
    }

    public String getMarkField() {
        return markField;
    }

    public void setMarkField(String markField) {
        this.markField = markField;
    }
}
