package com.angels.floatwindow.actor;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.angels.floatwindow.R;
import com.angels.library.utils.AcLogUtil;

public class Animal extends ActorBase {

    /**状态 0:走了 1：飞*/
    private int state = 0;
    private float mTouchStartX;
    private float mTouchStartY;
    /**相对屏幕的坐标*/
    private float x;
    private float y;

    /*****************计时器*******************/
    private Runnable timeRunable = new Runnable() {
        @Override
        public void run() {
            if(state == 0){
                x += 2;
                y += 2;
                updateViewPosition((int)x,(int)y);
            }
            //递归调用本runable对象，实现每隔一秒一次执行任务
            mhandle.postDelayed(this, 200);
        }
    };
    //计时器
    private Handler mhandle = new Handler();
    /*****************计时器*******************/

    private WindowManager.LayoutParams wmParams;

    public Animal(Context context,WindowManager.LayoutParams wmParams) {
        super(context);
        init();
        this.wmParams = wmParams;
    }

    private void init() {
        Thread thread = new Thread(timeRunable);
        thread.start();
    }

    public void startAnim() {
        AnimationDrawable animationDrawable = (AnimationDrawable) this.getDrawable();
        animationDrawable.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        AcLogUtil.i("animal-->onSizeChanged:" + w + "--" + h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*按下的时间*/
        long downTime;
        /*拖动的当前时间*/
        long moveTime;
        /*触摸离开动作时间*/
        long upTime;
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY() - 35;
        Log.i("currP", "currX" + x + "====currY" + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
                downTime = System.currentTimeMillis();
                // 获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                Log.i("startP", "startX" + mTouchStartX + "====startY" + mTouchStartY);
                break;
            case MotionEvent.ACTION_MOVE://捕获手指触摸移动动作
                moveTime = System.currentTimeMillis();
                updateViewPosition((int)(x - mTouchStartX),(int)(y - mTouchStartY));
                break;
            case MotionEvent.ACTION_UP://捕获手指触摸离开动作
                upTime = System.currentTimeMillis();
                updateViewPosition((int)(x - mTouchStartX),(int)(y - mTouchStartY));
                mTouchStartX=mTouchStartY=0;

                if(state == 0){
                    setImageResource(R.drawable.anim_fly);
                    startAnim();
                    state = 1;
                }else if(state == 1){
                    setImageResource(R.drawable.anim_walk);
                    startAnim();
                    state = 0;
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void updateViewPosition(int x,int y) {
        wmParams.x = x;
        wmParams.y = y;
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.updateViewLayout(this, wmParams);//刷新显示
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
