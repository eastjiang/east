package com.eastproject.app.example;


import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by TMD on 2016/7/20.
 */
public class EASTClient  {
    OkHttpClient mHttpClient = new OkHttpClient();
    Request request = new Request.Builder()
                            .url("http://192.168.0.106:80/php/east_app_data/app/car_banner.php")
                            .build();
    Call call = mHttpClient.newCall(request);

    public Call getCall() {
        return call;
    }
}
