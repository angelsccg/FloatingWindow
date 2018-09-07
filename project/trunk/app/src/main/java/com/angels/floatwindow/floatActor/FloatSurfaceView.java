package com.angels.floatwindow.floatActor;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.angels.floatwindow.R;
import com.angels.floatwindow.actor.Rabbit;
import com.angels.floatwindow.service.FloatWindowService;
import com.angels.floatwindow.utils.ACLogUtil;
import com.angels.floatwindow.utils.ACToast;


public class FloatSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    /**宽度*/
    public static final int WITCH = 200;
    /**高度*/
    public static final int HEIGHT = 200;
    /**每50帧刷新一次屏幕  0.05秒刷一次*/
    public static final int TIME_IN_FRAME = 50;
    // SurfaceHolder
    private SurfaceHolder mHolder;
    // 用于绘图的Canvas
    private Canvas mCanvas;
    // 子线程标志位
    private boolean mIsDrawing;

    private Paint mPaint;


    /*按下的时间*/
    private long downTime;
    /*拖动的当前时间*/
    private long moveTime;
    /*触摸离开动作时间*/
    private long upTime;

    /**相对屏幕的坐标*/
    public int x,y;
    /**触摸的相对坐标*/
    private float mTouchStartX;
    private float mTouchStartY;

    private WindowManager.LayoutParams wmParams;

//    private Bitmap bitmap;
    /**睡眠 入睡时间，0的时候表示没有在睡觉
     * 1，睡眠 至少要一个小时才能被敲醒
     * 2，白天睡觉睡两个小时就会自动醒来
     * 3，晚上睡觉不会自动醒来
     * 4，睡一个小时后可以被敲醒
     * */
    private long startSleep = 0;

    /**房子*/
    private House house;
    /**太阳*/
    private Sun sun;
    /**呼噜*/
    private Snore snore;


    /*
     *创建回调接口
     */
    public interface OnTouchEventHouseListener {
        public void onTouchEvent(MotionEvent event);
        //醒来
        public void onWalkUp();
        //敲门
        public void onKnock();
        //睡觉
        public void onSleep();
    }
    private OnTouchEventHouseListener onTouchEventHouseListener;
    public void setOnTouchHouseRabbitListener(OnTouchEventHouseListener onTouchEventHouseListener) {
        this.onTouchEventHouseListener = onTouchEventHouseListener;
    }

    public FloatSurfaceView(Context context) {
        super(context);
        init();
    }

    public FloatSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        wmParams = new WindowManager.LayoutParams();
        wmParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wmParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 窗口位置
        wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式，效果为背景透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;//andorid O之后 包括android O
        }else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 设置window type andorid O之前
        }
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;


        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mHolder = getHolder();
        mHolder.setFixedSize(WITCH,HEIGHT);
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        //mHolder.setFormat(PixelFormat.OPAQUE);

        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);

        // 从资源文件中生成位图bitmap
//        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.house_daytime01);

        initActor();
    }

    private void initActor() {
        house = new House(this);
        sun = new Sun(this);
        snore = new Snore(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder,int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }


    @Override
    public void run() {
        while (mIsDrawing) {
            /**取得更新之前的时间**/
            long startTime = System.currentTimeMillis();
            /**在这里加上线程安全锁**/
            synchronized (mHolder) {
                /**拿到当前画布 然后锁定**/
                mCanvas =mHolder.lockCanvas();
                draw();
                /**绘制结束后解锁显示在屏幕上**/
                mHolder.unlockCanvasAndPost(mCanvas);
            }

            /**取得更新结束的时间**/
            long endTime = System.currentTimeMillis();

            /**计算出一次更新的毫秒数**/
            int diffTime  = (int)(endTime - startTime);

            /**确保每次更新时间为30帧**/
            while(diffTime <=TIME_IN_FRAME) {
                diffTime = (int)(System.currentTimeMillis() - startTime);
                /**线程等待**/
                Thread.yield();
            }
        }

    }

    //绘图操作

    private void draw() {
//        //SurfaceView背景
//        mCanvas.drawColor(Color.RED);
//        // 指定图片绘制区域
//        Rect rectsrc = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
//        // 指定图片在屏幕上显示的区域
//        Rect rectFdst = new Rect(0,0,getWidth(),getHeight());
//        mCanvas.drawBitmap(bitmap,rectsrc,rectFdst,null);
        mCanvas.drawColor(Color.WHITE);
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
        house.draw(mCanvas);
        sun.draw(mCanvas);
        if(startSleep != 0){
            snore.draw(mCanvas);
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(onTouchEventHouseListener != null){
            onTouchEventHouseListener.onTouchEvent(event);
        }
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int x = (int) event.getRawX();
        int y = (int) event.getRawY() - FloatWindowService.statusBarHeight;
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
                if(lenTime <= 300){
                    knock();
                }
                upTime = nowTime;
                break;
            default:
                break;
        }
        return true;//表示此View拦截处理触摸事件
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

    /**睡觉*/
    public void sleep(){
        if(onTouchEventHouseListener != null){
            onTouchEventHouseListener.onWalkUp();
        }
        this.setVisibility(VISIBLE);
        startSleep = System.currentTimeMillis();
        if(onTouchEventHouseListener != null){
            onTouchEventHouseListener.onSleep();
        }
    }
    /**醒来*/
    public void wakeUp(){
        if(onTouchEventHouseListener != null){
            onTouchEventHouseListener.onWalkUp();
        }
        this.setVisibility(GONE);
        startSleep = 0;
    }
    /**敲门*/
    public void knock(){
        if(onTouchEventHouseListener != null){
            onTouchEventHouseListener.onKnock();
        }
        long nowTime = System.currentTimeMillis();
        //睡觉中
        if(startSleep != 0){
            long len = nowTime - startSleep;
            //TODO
            wakeUp();
        }
//        ACToast.showShort(getContext(),"睡觉中。。。");
    }




}
