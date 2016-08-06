package com.eastproject.app.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.eastproject.app.R;
import com.eastproject.app.example.EASTClient;
import com.eastproject.app.views.ConvenientBannerImageHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主页Activity
 *
 * Created by TMD on 2016/8/4.
 */
public class HomeActivity extends AppCompatActivity {
    private DisplayImageOptions mOptions;
    private ConvenientBanner mConvenientBanner;
//    private List<String> mNetworkImagesList;
    private List<JSONObject> mNetworkImagesList = new ArrayList<>();
    private String[] images = {
            "http://192.168.0.106:80/php/east_app_data/images/banner/crousel_1_1.jpg",
            "http://192.168.0.106:80/php/east_app_data/images/banner/crousel_2_1.jpg",
            "http://192.168.0.106:80/php/east_app_data/images/banner/crousel_3_1.jpg",
            "http://192.168.0.106:80/php/east_app_data/images/banner/crousel_4_1.jpg",
            "http://192.168.0.106:80/php/east_app_data/images/banner/crousel_5_1.jpg"
    };

    private JSONArray mData;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONArray data = (JSONArray) msg.obj;
            for (int i = 0; i < data.size(); i++) {
                mNetworkImagesList.add(data.getJSONObject(i));
            }
            mConvenientBanner.setPages(new CBViewHolderCreator<ConvenientBannerImageHolder>() {

                @Override
                public ConvenientBannerImageHolder createHolder() {
                    return new ConvenientBannerImageHolder(mOptions);
                }
            }, mNetworkImagesList)
                    .setPageIndicator(new int[]{R.drawable.dot_unselected, R.drawable.dot_selected})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        //创建默认ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.prestrain)
                .showImageOnFail(R.drawable.prestrain)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        mConvenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);

        //从数据库中获取数据
        Call call = new EASTClient().getCall();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mData = (JSONArray) JSON.parseObject(response.body().string()).get("data");
                Message message = Message.obtain(mHandler);
                message.obj = mData;
                message.sendToTarget();
            }
        });
    }

    // 开始自动翻页
    @Override
    protected void onResume() {
        super.onResume();
        //开始自动翻页
        mConvenientBanner.startTurning(5000);
    }
    // 停止自动翻页
    @Override
    protected void onPause() {
        super.onPause();
        //停止翻页
        mConvenientBanner.stopTurning();
    }
}
