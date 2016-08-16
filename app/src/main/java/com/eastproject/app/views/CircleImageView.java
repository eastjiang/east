package com.eastproject.app.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import com.eastproject.app.R;

/**
 * Created by jiang.dong on 2016/8/16.
 */
public class CircleImageView extends ImageView {
    private Bitmap mImage;    //图片
    private boolean haveBoder;//是否有边框
    private int mBoderWidth;  //边框的宽度
    private int mBoderColor;  //边框的颜色
    private Paint mPaint;
    private Paint mBoderPaint;
    private BitmapShader mBitmapShader;
    private int mWidth;
    private int mHeight;
    private int mRadius;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0);
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.CircleImageView_image:
                    mImage = BitmapFactory.decodeResource(getResources(), array.getResourceId(attr, 0));
                    break;
                case R.styleable.CircleImageView_boder:
                    haveBoder = array.getBoolean(attr, false);
                    break;
                case R.styleable.CircleImageView_boder_width:
                    mBoderWidth = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CircleImageView_boder_color:
                    mBoderColor = array.getColor(attr, getResources().getColor(R.color.black));
                    break;
            }
        }

        array.recycle();

        //初始化画笔
        mPaint = new Paint();
        mBoderPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            int imgWidth = getPaddingLeft() + mImage.getWidth() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                mWidth = Math.min(imgWidth, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            int imgHeight = getPaddingTop() + mImage.getHeight() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(imgHeight, heightSize);
            }
        }


        mRadius = Math.min(mWidth, mHeight) / 2;
        if (haveBoder) {
            setMeasuredDimension((mRadius + mBoderWidth) * 2, (mRadius + mBoderWidth) * 2);
        } else {
            setMeasuredDimension(mRadius * 2, mRadius * 2);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        mBitmapShader = new BitmapShader(mImage, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setAntiAlias(true);
        mPaint.setShader(mBitmapShader);
        if (haveBoder) {
//            canvas.drawCircle(mRadius + mBoderWidth, mRadius + mBoderWidth, mRadius, mPaint);
            canvas.drawOval(getPaddingLeft(), getPaddingTop(),
                    (mRadius + mBoderWidth) * 2 - getPaddingRight(),
                    (mRadius + mBoderWidth) * 2 - getPaddingBottom(), mPaint);

            mBoderPaint.setStyle(Paint.Style.STROKE);
            mBoderPaint.setStrokeWidth(mBoderWidth);
            mBoderPaint.setColor(mBoderColor);
            mBoderPaint.setAntiAlias(true);
//            canvas.drawCircle(mRadius + mBoderWidth, mRadius + mBoderWidth, mRadius, mBoderPaint);
            canvas.drawOval(getPaddingLeft(), getPaddingTop(),
                    (mRadius + mBoderWidth) * 2 - getPaddingRight(),
                    (mRadius + mBoderWidth) * 2 - getPaddingBottom(), mBoderPaint);
        } else {
//            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
            canvas.drawOval(getPaddingLeft(), getPaddingTop(),
                    mRadius * 2 - getPaddingRight(),
                    mRadius * 2 - getPaddingBottom(), mPaint);
        }
    }
}
