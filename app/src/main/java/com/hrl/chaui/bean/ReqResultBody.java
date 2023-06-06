package com.hrl.chaui.bean;

import com.hrl.chaui.util.GsonUtils;

public class ReqResultBody<T> {
    private int code;
    private String message;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return GsonUtils.objectToJsonString(data);
    }

    public void setData(Object data) {
        if(data != null) {
            this.data = data;
        }
    }
}
