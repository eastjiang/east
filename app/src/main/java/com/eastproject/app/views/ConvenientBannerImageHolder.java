package com.eastproject.app.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.holder.Holder;
import com.eastproject.app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 轮播图片Holder
 *
 * Created by TMD on 2016/8/6.
 */
public class ConvenientBannerImageHolder implements Holder<JSONObject> {

    private ImageView mLeftImgField;
    private ImageView mRightUpImgField;
    private ImageView mRightDownImgField;
    private TextView mLeftTitleField;
    private TextView mLeftSTitleField;
    private TextView mRightUpTitleField;
    private TextView mRightDownTitleField;
    private DisplayImageOptions mOptions;

    public ConvenientBannerImageHolder(DisplayImageOptions options) {
        this.mOptions = options;
    }

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
//        imageView = new ImageView(context);
        View view = LayoutInflater.from(context).inflate(R.layout.item_connvenien_banner, null, false);
        mLeftImgField = (ImageView) view.findViewById(R.id.banner_left_img);
        mRightUpImgField = (ImageView) view.findViewById(R.id.banner_right_up_img);
        mRightDownImgField = (ImageView) view.findViewById(R.id.banner_right_down_img);
        mLeftTitleField = (TextView) view.findViewById(R.id.banner_left_title);
        mLeftSTitleField = (TextView) view.findViewById(R.id.banner_left_stitle);
        mRightUpTitleField = (TextView) view.findViewById(R.id.banner_right_up_title);
        mRightDownTitleField = (TextView) view.findViewById(R.id.banner_right_down_title);

        mLeftImgField.setScaleType(ImageView.ScaleType.FIT_XY);
        mRightUpImgField.setScaleType(ImageView.ScaleType.FIT_XY);
        mRightDownImgField.setScaleType(ImageView.ScaleType.FIT_XY);

        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, JSONObject data) {
        //显示图片的配置
        ImageLoader.getInstance().displayImage(data.get("left_img_url").toString(), mLeftImgField, mOptions);
        ImageLoader.getInstance().displayImage(data.get("right_up_img_url").toString(), mRightUpImgField, mOptions);
        ImageLoader.getInstance().displayImage(data.get("right_down_img_path").toString(), mRightDownImgField, mOptions);

        mLeftTitleField.setText(data.get("left_img_title").toString());
        mLeftSTitleField.setText(data.get("left_img_stitle").toString());
        mRightUpTitleField.setText(data.get("right_up_img_title").toString());
        mRightDownTitleField.setText(data.get("right_down_img_title").toString());
    }
}
