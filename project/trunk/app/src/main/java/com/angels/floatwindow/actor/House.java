package com.angels.floatwindow.actor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.angels.floatwindow.R;
import com.angels.floatwindow.service.FloatWindowService;
import com.angels.floatwindow.utils.ACLogUtil;
import com.angels.floatwindow.utils.ACToast;

/**房子*/
public class House extends ActorBase {

    /**白天*/
    public static final int DAYTIME = 1;
    /**晚上*/
    public static final int NIGHT = 2;
    /**白天睡觉*/
    public static final int DAYTIME_SLEEP = 3;
    /**晚上睡觉*/
    public static final int NIGHT_SLEEP = 4;
    /**白天旅行*/
    public static final int DAYTIME_TOUR = 5;
    /**晚上旅行*/
    public static final int NIGHT_TOUR = 6;
    /**状态 0:白天 1：夜晚 2：白天睡觉 3：晚上睡觉 4：白天远行 5：晚上远行*/
    private int state = 0;

    /*上次改变状态的时间*/
    private long changeStateTime = 0;

    private float mTouchStartX;
    private float mTouchStartY;


//    /*****************计时器*******************/
//    private Runnable timeRunable = new Runnable() {
//        @Override
//        public void run() {
//            if(state == DAYTIME){//白天
//                walkToRight();
//            }else if(state == NIGHT){//晚上
//                walkToLeft();
//            }else if(state == DAYTIME_SLEEP){//白天睡觉
//                walkToUp();
//            }else if(state == NIGHT_SLEEP){//晚上睡觉
//                walkToDowm();
//            }
//            //递归调用本runable对象，实现每隔一秒一次执行任务
//            mhandle.postDelayed(this, 100);
//        }
//    };
//    //计时器
//    private Handler mhandle = new Handler();
//    /*****************计时器*******************/

    private WindowManager.LayoutParams wmParams;

    /*按下的时间*/
    private long downTime;
    /*拖动的当前时间*/
    private long moveTime;
    /*触摸离开动作时间*/
    private long upTime;

    public House(Context context, WindowManager.LayoutParams wmParams) {
        super(context);
        init();
        this.wmParams = wmParams;
    }

    private void init() {

    }

    public void start(){
        changeStateTime = System.currentTimeMillis();
        startAnim();
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
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int x = (int) event.getRawX();
        int y = (int) event.getRawY() - FloatWindowService.statusBarHeight;
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
                updateViewPosition((int)(x - mTouchStartX),(int)(y - mTouchStartY));
                Log.i("startP", "位置**startXxxxx" + mTouchStartX + "====startY" + mTouchStartY);
                mTouchStartX=mTouchStartY=0;

                long nowTime = System.currentTimeMillis();
                long lenTime = (nowTime - upTime);
                if(lenTime <= 200){
                    ACToast.showShort(getContext(),"敲门啦");
                }
                upTime = nowTime;
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
            case DAYTIME://白天
                initDaytime();
                break;
            case NIGHT://夜晚
                initNight();
                break;
            case DAYTIME_SLEEP://白天睡觉
                initDaytimeSleep();
                break;
            case NIGHT_SLEEP://夜晚睡觉
                initNightSleep();
                break;
            case DAYTIME_TOUR://白天旅行
                initDaytimeTour();
                break;
            case NIGHT_TOUR://夜晚旅行
                initNightTour();
                break;
        }
    }
    /**初始化白天*/
    private void initDaytime(){
        setImageResource(R.drawable.anim_house_daytime);
        startAnim();
    }

    /**初始化晚上*/
    private void initNight(){
        setImageResource(R.drawable.anim_house_daytime);
        startAnim();
    }
    /**初始化白天睡觉*/
    private void initDaytimeSleep(){
        setImageResource(R.drawable.anim_house_daytime);
        startAnim();
    }

    /**初始化晚上睡觉*/
    private void initNightSleep(){
        setImageResource(R.drawable.anim_house_daytime);
        startAnim();
    }
    /**初始化白天旅行*/
    private void initDaytimeTour(){
        setImageResource(R.drawable.anim_house_daytime);
        startAnim();
    }
    /**初始化晚上旅行*/
    private void initNightTour(){
        setImageResource(R.drawable.anim_house_daytime);
        startAnim();
    }

    /**睡觉啦*/
    public void sleep(){

    }
    /**敲门啦*/
    public void knock(){

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

}
