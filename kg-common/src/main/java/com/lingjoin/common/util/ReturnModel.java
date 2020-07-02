package com.lingjoin.common.util;

import java.io.Serializable;

public class ReturnModel implements Serializable {



    private Object data;

    private String message;

    private Integer status;

    public ReturnModel() {
    }

    public ReturnModel(Object data, String message, Integer status) {
        this.data = data;
        this.message = message;
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ReturnModel{" +"status="+status+" data=" + data +", message='" + message + '\'' +'}';

    }
}
