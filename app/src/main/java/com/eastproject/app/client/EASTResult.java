package com.eastproject.app.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by TMD on 2016/8/9.
 */
public class EASTResult {
    @JSONField(name = "code")
    private String code;

    @JSONField(name = "data")
    private Object data;

    public int getCode() {
        return Integer.parseInt(this.code);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public <T> T getData(Class<T> zClass) {
        if (data != null) {
            if (zClass.isAssignableFrom(data.getClass())) {
                return zClass.cast(data);
            } else if (data instanceof JSONObject){
                return JSONObject.toJavaObject((JSONObject) data, zClass);
            }
        }
        return null;
    }
}
