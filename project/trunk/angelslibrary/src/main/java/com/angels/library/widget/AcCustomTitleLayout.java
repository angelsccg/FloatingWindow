package com.angels.library.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.angels.library.R;


/**
 * Name: CommonTitleView
 * Author: ccg
 * Comment: 自定义通用标题栏控件（待扩展）
 * Date: 2017-08-23 11:40
 */

public class AcCustomTitleLayout extends RelativeLayout {
    /**左边控件*/
    private TextView tvLeft;
    /**左边ImageView控件*/
    private ImageView ivLeft;
    /**右边控件*/
    private ImageView ivRight;
    /**右边控件*/
    private TextView tvRight;
    /**右边的控件容器*/
    private LinearLayout llRight;
    /**中间title*/
    private TextView tvTitle;
    /**标题容器*/
    private RelativeLayout rlContainer;
    /**加载（菊花状）*/
    private ProgressBar progress;



    public AcCustomTitleLayout(Context context) {
        this(context,null);
    }

    public AcCustomTitleLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AcCustomTitleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniView(context);
    }

    private void iniView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.ac_part_custom_title, this, true);
        tvLeft = (TextView) view.findViewById(R.id.tv_left);
        tvTitle = (TextView) view.findViewById(R.id.tv_content);
        ivRight = (ImageView) view.findViewById(R.id.iv_right);
        tvRight = (TextView) view.findViewById(R.id.tv_right);
        llRight = (LinearLayout) view.findViewById(R.id.ll_right);
        ivLeft = (ImageView) view.findViewById(R.id.iv_left);
        rlContainer = (RelativeLayout) view.findViewById(R.id.rl_container);

        progress = (ProgressBar) view.findViewById(R.id.progress);
    }

    public void setRightProgress(int visibility){
        progress.setVisibility(visibility);
    }
    public void setRightProgressClick(OnClickListener onClickListener){
        progress.setOnClickListener(onClickListener);
    }

    public void setTitle(String title){
        tvTitle.setVisibility(VISIBLE);
        tvTitle.setText(title);
    }
    public void setTitle(String title,int color){
        tvTitle.setVisibility(VISIBLE);
        tvTitle.setText(title);
        tvTitle.setTextColor(color);
    }
    public void setIvLeft(int ivId,OnClickListener listener){
        ivLeft.setVisibility(VISIBLE);
        ivLeft.setImageResource(ivId);
        ivLeft.setOnClickListener(listener);
    }
    public void setIvRight(int ivId,OnClickListener listener){
        ivRight.setVisibility(VISIBLE);
        ivRight.setImageResource(ivId);
        if(listener != null){
            ivRight.setOnClickListener(listener);
        }
    }
    public void setTvRight(String text,OnClickListener listener){
        tvRight.setVisibility(VISIBLE);
        tvRight.setText(text);
        if(listener != null){
            tvRight.setOnClickListener(listener);
        }
    }
    public void setLlRight(String text,int ivId,OnClickListener listener){
        llRight.setVisibility(VISIBLE);
        ivRight.setVisibility(VISIBLE);
        tvRight.setVisibility(VISIBLE);
        tvRight.setText(text);
        ivRight.setImageResource(ivId);
        llRight.setOnClickListener(listener);
    }
    public void setTvRightVisibility(int visibility){
        tvRight.setVisibility(visibility);
    }

    /**
     * 设置返回按钮的回调函数
     *
     * @param listener 监听器
     */
    public void setTvLeft(String text,OnClickListener listener){
        tvLeft.setText(text);
        tvLeft.setVisibility(VISIBLE);
        tvLeft.setOnClickListener(listener);
    }
    public void setTvLeft(String text){
        tvLeft.setText(text);
        tvLeft.setVisibility(VISIBLE);
    }
    public TextView getTvLeft(){
        return tvLeft;
    }

    public ImageView getIvLeft() {
        return ivLeft;
    }

    public void setBackgroundResource(int color_id){
        rlContainer.setBackgroundResource(color_id);
    }
    public void setBackground(Drawable drawable){
        rlContainer.setBackground(drawable);
    }
    public void setBackgroundColor(int color){
        rlContainer.setBackgroundColor(color);
    }
}
