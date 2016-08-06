package com.eastproject.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.eastproject.app.main.HomeActivity;
import com.eastproject.app.utils.BitmapUtil;

import java.io.InputStream;

/**
 * APP启动页面
 * Created by TMD on 2016/7/21.
 */
public class SplashActivity extends AppCompatActivity {
    private ImageView mSplashImg;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSplashImg = new ImageView(this);
        mSplashImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mSplashImg.setImageBitmap(BitmapUtil.readBitMap(SplashActivity.this, R.drawable.splash));
        setContentView(mSplashImg);

        //设置缩放动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_image);
        animation.setFillAfter(true);
        mSplashImg.setAnimation(animation);
        mHandler.sendEmptyMessageDelayed(0, 3000);
    }
}
