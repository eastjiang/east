package com.eastproject.app;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.eastproject.app.utils.BitmapUtil;

/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity {
    private ImageView mLoginBGField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}