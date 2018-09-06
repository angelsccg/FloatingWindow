package com.angels.floatwindow.actor;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.angels.floatwindow.R;
import com.angels.floatwindow.service.FloatWindowService;
import com.angels.floatwindow.utils.ACLogUtil;

public class Rabbit extends ActorBase {

    /**左走*/
    public static final int WALK_LEFT = 1;
    /**右走*/
    public static final int WALK_RIGHT = 2;
    /**上走*/
    public static final int WALK_UP = 3;
    /**下走*/
    public static final int WALK_DOWN = 4;
    /**左吃*/
    public static final int EAT_LEFT = 5;
    /**右吃*/
    public static final int EAT_RIGHT = 6;
    /**状态 0:禁止 1：左走 2：右走 3：随机走*/
    private int state = 0;
    /**速度*/
    private int speed = 2;

    /*上次改变状态的时间*/
    private long changeStateTime = 0;

    private float mTouchStartX;
    private float mTouchStartY;


    /*****************计时器*******************/
    private Runnable timeRunable = new Runnable() {
        @Override
        public void run() {
            if(state == WALK_RIGHT){
                walkToRight();
            }else if(state == WALK_LEFT){
                walkToLeft();
            }else if(state == WALK_UP){
                walkToUp();
            }else if(state == WALK_DOWN){
                walkToDowm();
            }
            //5秒改一种行走方式
            long nowTime = System.currentTimeMillis();
            long lenTime = (nowTime - changeStateTime)/1000;
            if(lenTime >= 5){
                walkToRandom();
            }
            //递归调用本runable对象，实现每隔一秒一次执行任务
            mhandle.postDelayed(this, 100);
        }
    };
    //计时器
    private Handler mhandle = new Handler();
    /*****************计时器*******************/

    private WindowManager.LayoutParams wmParams;

    /*按下的时间*/
    private long downTime;
    /*拖动的当前时间*/
    private long moveTime;
    /*触摸离开动作时间*/
    private long upTime;

    /*
     *创建回调接口
     */
    public interface OnTouchEventRabbitListener {
        public void onTouchEvent(MotionEvent event);
    }

    private OnTouchEventRabbitListener onTouchEventRabbitListener;

    public Rabbit(Context context, WindowManager.LayoutParams wmParams) {
        super(context);
        init();
        this.wmParams = wmParams;
    }

    private void init() {

    }

    public void start(){
        changeStateTime = System.currentTimeMillis();
        Thread thread = new Thread(timeRunable);
        thread.start();
        startAnim();
        initRightWalk();
    }

    public void startAnim() {
        AnimationDrawable animationDrawable = (AnimationDrawable) this.getDrawable();
        animationDrawable.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        ACLogUtil.i("animal-->onSizeChanged:" + w + "--" + h);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(onTouchEventRabbitListener != null){
            onTouchEventRabbitListener.onTouchEvent(event);
        }
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        Log.i("currP", "位置**currX" + x + "====currY" + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
                downTime = System.currentTimeMillis();
                // 获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                Log.i("startP", "位置**startX" + mTouchStartX + "====startY" + mTouchStartY);
                break;
            case MotionEvent.ACTION_MOVE://捕获手指触摸移动动作
                moveTime = System.currentTimeMillis();
                updateViewPosition((int)(x - mTouchStartX),(int)(y - mTouchStartY));

                break;
            case MotionEvent.ACTION_UP://捕获手指触摸离开动作
                upTime = System.currentTimeMillis();
                updateViewPosition((int)(x - mTouchStartX),(int)(y - mTouchStartY));
                Log.i("startP", "位置**startXxxxx" + mTouchStartX + "====startY" + mTouchStartY);
                mTouchStartX=mTouchStartY=0;

                eatRandom();
                break;
            default:
                break;
        }
        return true;
    }

    /**改变状态*/
    public void changeState(int state){
        this.state = state;
        changeStateTime = System.currentTimeMillis();
        switch (state){
            case WALK_LEFT://左走
                initLeftWalk();
                break;
            case WALK_RIGHT://右走
                initRightWalk();
                break;
            case WALK_UP://上走
                initUpWalk();
                break;
            case WALK_DOWN://下走
                initDownWalk();
                break;
        }
    }
    /**初始化随机走*/
    private void walkToRandom(){
        int ran = (int) (Math.random()*4 + 1);
        ACLogUtil.i("随机状态-->" + ran );
        changeState(ran);
    }
    /**初始化左走*/
    private void initLeftWalk(){
        setImageResource(R.drawable.anim_rabbit_walk_left);
        startAnim();
    }

    /**初始化右走*/
    private void initRightWalk(){
        setImageResource(R.drawable.anim_rabbit_walk_right);
        startAnim();
    }
    /**初始化下走*/
    private void initDownWalk(){
        setImageResource(R.drawable.anim_rabbit_walk_down);
        startAnim();
    }

    /**初始化上走*/
    private void initUpWalk(){
        setImageResource(R.drawable.anim_rabbit_walk_up);
        startAnim();
    }

    /**初始化随机走*/
    private void eatRandom(){
        int ran = (int) (Math.random()*2 + 1);
        switch (ran){
            case 1://左走
                initLeftEat();
                break;
            case 2://右走
                initRightEat();
                break;
        }
    }
    /**初始化左吃*/
    private void initLeftEat(){
        setImageResource(R.drawable.anim_rabbit_eat_left);
        startAnim();
        changeState(EAT_LEFT);
    }
    /**初始化右吃*/
    private void initRightEat(){
        setImageResource(R.drawable.anim_rabbit_eat_right);
        startAnim();
        changeState(EAT_RIGHT);
    }

    /**左走*/
    private void walkToLeft(){
        if(x <= 0){
            walkToRandom();
        }else {
            updateViewPosition(x-=speed,y);
        }
    }
    /**右走*/
    private void walkToRight(){
        if(x >= FloatWindowService.screenWeight - getWidth()){
            walkToRandom();
        }else {
            updateViewPosition(x+=speed,y);
        }
    }
    /**下走*/
    private void walkToDowm(){
        if(y <= FloatWindowService.screenHeight - getHeight()){
            walkToRandom();
        }else {
            updateViewPosition(x,y+=speed);
        }
    }
    /**上走*/
    private void walkToUp(){
        if(y <= 0){
            walkToRandom();
        }else {
            updateViewPosition(x,y-=speed);
        }
    }

    private void updateViewPosition(int x,int y) {
        wmParams.x = x;
        wmParams.y = y;
        this.x = x;
        this.y = y;
        ACLogUtil.i("悬浮窗 位置：" + wmParams.x + "--" + wmParams.y);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.updateViewLayout(this, wmParams);//刷新显示
    }

    public int getState() {
        return state;
    }

    public void setOnTouchEventRabbitListener(OnTouchEventRabbitListener onTouchEventRabbitListener) {
        this.onTouchEventRabbitListener = onTouchEventRabbitListener;
    }
}
