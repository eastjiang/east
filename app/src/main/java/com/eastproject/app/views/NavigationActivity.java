package com.eastproject.app.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.FrameLayout;
import com.eastproject.app.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * App标题栏
 *
 * Created by TMD on 2016/8/8.
 */
public class NavigationActivity extends AppCompatActivity {
    private FrameLayout mContentView;
    private Context mContext;
    private SystemBarTintManager tintManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.navigation_activity);

        //设置沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(Color.parseColor("#CE3D3A"));
            tintManager.setStatusBarTintEnabled(true);
        }

        mContext = NavigationActivity.this;
        mContentView = (FrameLayout) findViewById(R.id.content_view);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(mContext).inflate(layoutResID, null);
        mContentView.addView(view);
    }

    @Override
    public void setContentView(View view) {
        mContentView.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentView.addView(view, params);
    }
}
