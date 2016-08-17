package com.eastproject.app.main;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.eastproject.app.R;
import com.eastproject.app.client.EASTCallback;
import com.eastproject.app.client.EASTClient;
import com.eastproject.app.client.EASTException;
import com.eastproject.app.utils.PopUtils;
import com.eastproject.app.views.ConvenientBannerImageHolder;
import com.eastproject.app.views.NavigationActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页Activity
 *
 * Created by TMD on 2016/8/4.
 */
public class HomeActivity extends NavigationActivity {
    private DisplayImageOptions mOptions;
    private ConvenientBanner mConvenientBanner;
    private List<JSONObject> mNetworkImagesList = new ArrayList<>();
//    private FrameLayout mContentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        //创建默认ImageLoader配置参数
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        mOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.prestrain)//加载前的图片
                .showImageOnFail(R.drawable.prestrain)//加载失败的图片
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        mConvenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
//        mContentView = (FrameLayout) findViewById(R.id.home_content_view);
        //在home_content_view中添加fragment
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.home_content_view, new HomeFragment());
        ft.commit();

        loadImages();
    }

    /**
     * 从服务器获取图片
     */
    private void loadImages() {
        EASTClient.getInstance(HomeActivity.this).queryUrl("/app/car_banner.php", new EASTCallback<JSONArray>() {
            @Override
            public void done(JSONArray array, EASTException e) {
                if (e != null) {
                    PopUtils.showToast(HomeActivity.this, e.getMessage());
                } else {
                    if (array != null) {
                        for (int i = 0; i < array.size(); i++) {
                            mNetworkImagesList.add(array.getJSONObject(i));
                        }
                        mConvenientBanner.setPages(new CBViewHolderCreator<ConvenientBannerImageHolder>() {

                            @Override
                            public ConvenientBannerImageHolder createHolder() {
                                return new ConvenientBannerImageHolder(mOptions);
                            }
                        }, mNetworkImagesList)
                                .setPageIndicator(new int[]{R.drawable.dot_unselected, R.drawable.dot_selected})
                                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                    }
                }
            }
        }, JSONArray.class);
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
