package com.eastproject.app.client;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by TMD on 2016/8/9.
 */
public class EASTError {

    @JSONField(name = "error")
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
