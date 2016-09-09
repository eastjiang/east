package com.eastproject.app.client;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eastproject.app.R;
import okhttp3.*;

import java.io.IOException;

/**
 * APP客户端
 *
 * Created by TMD on 2016/7/20.
 */
public class EASTClient  {
    private static final String URL_PATH = "http://192.168.0.100:80/php/east_app_data/app";

    private static EASTClient eastClient;
    private OkHttpClient mHttpClient;
    private Handler mHandler;
    private Context mContext;

    private EASTClient(Context context) {
        mHttpClient = new OkHttpClient();
        mHandler = new Handler(Looper.getMainLooper());
        mContext = context;
    }

    /**
     * 获取EASTClient实例 线程安全
     * @return EASTClient实例
     */
    public static EASTClient getInstance(Context context) {
        if (eastClient == null) {
            synchronized (EASTClient.class) {
                if (eastClient == null) {
                    eastClient = new EASTClient(context);
                }
            }
        }
        return eastClient;
    }

    /**
     * 网络请求（get)
     * @param <T> 泛型
     * @param url 服务器地址路径
     * @param callback 回调方法
     * @param obj 数据转要换为的类型
     */
    public <T> void queryUrl(String url, final EASTCallback<T> callback, final Class<T> obj) {
        final Request request = new Request.Builder()
                .url(URL_PATH + url)
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String message = mContext.getResources().getString(R.string.error_1);
                postCallback(callback, null, new EASTException(message));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                EASTException exception;
                if (!response.isSuccessful()) {
                    exception = new EASTException(mContext.getString(R.string.error_2));
                    postCallback(callback, null, exception);
                } else {
                    EASTResult result = JSON.parseObject(response.body().string(), EASTResult.class);
                    if (result == null) {
                        exception = new EASTException(mContext.getString(R.string.error_3));
                        postCallback(callback, null, exception);
                    } else if (result.getCode() != 200) {
                        EASTError error = result.getData(EASTError.class);
                        if (error == null) {
                            exception = new EASTException(mContext.getString(R.string.error_3));
                            postCallback(callback, null, exception);
                        } else {
                            postCallback(callback, null, new EASTException(error.getError()));
                        }
                    } else {
                        postCallback(callback, result.getData(obj), null);
                    }
                }
            }
        });
    }

    private <T> void postCallback(final EASTCallback callback, final T obj, final EASTException exception){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.done(obj, exception);
            }
        });
    }
}
