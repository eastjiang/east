package com.eastproject.app.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by jiang.dong on 2016/8/31.
 */
public class SideslipListView extends ListView {
    private int mScreenWidth;	// 屏幕宽度
    private int mDownX;			// 按下点的x值
    private int mDownY;			// 按下点的y值
    private int mDeleteBtnWidth;// 删除按钮的宽度

    private int touchSlop;//最小移动距离

    private boolean isDeleteShown = false;	// 删除按钮是否正在显示
    private boolean isMoving = false;	// 是否正在滑动
    private boolean isClick = true;	// 是否可以触发点击事件

    private ViewGroup mPointChild;	// 当前处理的item
    private ViewGroup mOldPointChild;	// 之前处理的item
    private LinearLayout.LayoutParams mLayoutParams;	// 当前处理的item的LayoutParams
    private LinearLayout.LayoutParams mOldLayoutParams;	// 之前处理的item的LayoutParams

    private long downTime;//按下时的时间
    private long upTime;//抬起时的事件
    private float speed = 1.25f;//当滑动速度大于该值时，则认为时Fling

    public SideslipListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideslipListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        
        //获取最小移动距离
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performActionDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                return performActionMove(ev);
            case MotionEvent.ACTION_UP:
                return performActionUp(ev);
        }

        return super.onTouchEvent(ev);
    }

    // 处理action_down事件
    private void performActionDown(MotionEvent ev) {
        //获取Down事件发生的时间
        downTime = ev.getEventTime();

        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();
        // 获取当前点的item
        mPointChild = (ViewGroup) getChildAt(pointToPosition(mDownX, mDownY)
                - getFirstVisiblePosition());

        //点击item时，将原先改变状态的item恢复原状，且该item不会触发click事件
        if (mOldPointChild != null && mOldPointChild != mPointChild) {
            if (isDeleteShown) {
                isClick = false;
            }
            oldTurnToNormal();
        }

        if (mOldPointChild == null) {
            mOldPointChild = mPointChild;
        }
        // 获取删除按钮的宽度
        mDeleteBtnWidth = mPointChild.getChildAt(1).getLayoutParams().width;
        mLayoutParams = (LinearLayout.LayoutParams) mPointChild.getChildAt(0)
                .getLayoutParams();

        if (mOldLayoutParams == null) {
            mOldLayoutParams = mLayoutParams;
        }
        // 为什么要重新设置layout_width 等于屏幕宽度
        // 因为match_parent时，不管你怎么滑，都不会显示删除按钮
        // why？ 因为match_parent时，ViewGroup就不去布局剩下的view
        mLayoutParams.width = mScreenWidth;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }

    // 处理action_move事件
    private boolean performActionMove(MotionEvent ev) {
        int nowX = (int) ev.getX();
        int nowY = (int) ev.getY();

        //当x轴的移动距离大于y轴的移动距离，则判定为滑动
        if(Math.abs(nowX - mDownX) > Math.abs(nowY - mDownY) && Math.abs(nowX - mDownX) > touchSlop) {
            isMoving = true;
        }

        if (isMoving) {
            isClick = false;
            // 如果向左滑动
            if (!isDeleteShown) {
                if (nowX < mDownX) {
                    // 计算要偏移的距离
                    int scroll = (nowX - mDownX) / 2;
                    // 如果大于了删除按钮的宽度， 则最大为删除按钮的宽度
                    if (-scroll >= mDeleteBtnWidth) {
                        scroll = -mDeleteBtnWidth;
                    }
                    // 重新设置leftMargin
                    mLayoutParams.leftMargin = scroll;
                    mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
                }
            }

            //deleteBtn显示，向右滑动
            if (isDeleteShown) {
                if (nowX > mDownX) {
                    // 计算要偏移的距离
                    int scroll = (nowX - mDownX) / 2;
                    if (scroll >= mDeleteBtnWidth) {
                        scroll = 0;
                    } else {
                        scroll = scroll - mDeleteBtnWidth;
                    }
                    // 重新设置leftMargin
                    mLayoutParams.leftMargin = scroll;
                    mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
                }
            }

            return true;
        }
        return super.onTouchEvent(ev);
    }

    // 处理action_up事件
    private boolean performActionUp(MotionEvent ev) {

        //获取UP事件发生的时间
        upTime = ev.getEventTime();

        int nowX = (int) ev.getX();

        //如果滑动速度大于speed，则将删除按钮隐藏
        if (nowX - mDownX > touchSlop) {
            if (upTime - downTime < 250) {
                float v = (float) (upTime - downTime) / touchSlop;
                if (v > speed) {
                    turnToNormal();
                }
            }
        }

        // 偏移量大于button的一半，则显示button
        // 否则恢复默认
        if (-mLayoutParams.leftMargin >= mDeleteBtnWidth / 2) {
//            translateAnimationRun(mPointChild, mLayoutParams, mLayoutParams.leftMargin, -mDeleteBtnWidth);
            mLayoutParams.leftMargin = -mDeleteBtnWidth;
            isDeleteShown = true;
            isClick = false;
            mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
        } else {
            turnToNormal();
        }

        //若点击的item为原先改变状态的item，则判断它是否是Move还是Down，若果是Down，则将该Item还原
        if (Math.abs(nowX - mDownX) < touchSlop) {
            oldTurnToNormal();
        }

        mOldLayoutParams = mLayoutParams;
        mOldPointChild = mPointChild;
        isMoving = false;

        //若果item为不可点击状态，则拦截事件
        if (!isClick) {
            isClick = true;
            return true;
        }
        isClick = true;

        return super.onTouchEvent(ev);
    }

    /**
     * 变为正常状态
     */
    public void turnToNormal() {
//        mLayoutParams.leftMargin = 0;
//        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
        translateAnimationRun(mPointChild, mLayoutParams, mLayoutParams.leftMargin, 0);
        isDeleteShown = false;
    }

    /**
     * 将之前的item变为正常状态
     */
    public void oldTurnToNormal() {
//        mOldLayoutParams.leftMargin = 0;
//        mOldPointChild.getChildAt(0).setLayoutParams(mOldLayoutParams);
        translateAnimationRun(mOldPointChild, mOldLayoutParams, mOldLayoutParams.leftMargin, 0);
        isDeleteShown = false;
    }

    /**
     * 当前是否可点击
     * @return 是否可点击
     */
    public boolean canClick() {
        return !isDeleteShown;
    }

    /**
     * 刪除按鈕的動畫效果
     * @param listItem 實現動畫的Item
     * @param layoutParams 實現動畫的Item的第一個子View的LayoutParams
     * @param fromX X轴的起始位置
     * @param toX X轴的最终位置
     */
    public void translateAnimationRun(final ViewGroup listItem, final LinearLayout.LayoutParams layoutParams, int fromX, int toX) {
        ObjectAnimator tAnimator = ObjectAnimator.ofInt(listItem.getChildAt(0), "translationX", fromX, toX);
        tAnimator.setDuration(100).start();
        tAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                layoutParams.leftMargin = animatedValue;
                listItem.getChildAt(0).setLayoutParams(layoutParams);
            }
        });
    }
}
