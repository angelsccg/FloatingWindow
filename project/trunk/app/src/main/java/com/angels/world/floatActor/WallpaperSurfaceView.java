package com.angels.world.floatActor;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.angels.world.R;
import com.angels.world.service.WorldService;


public class WallpaperSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
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
    public boolean mIsDrawing;

    private Paint mPaint;


    /*按下的时间*/
    private long downTime;
    /*拖动的当前时间*/
    private long moveTime;
    /*触摸离开动作时间*/
    private long upTime;

    /**相对屏幕的坐标*/
    public int x,y;

    /**房子*/
    private House house;
    /**太阳*/
    private Sun sun;
    /**月亮*/
    private Moon moon;
    /**呼噜*/
    private Snore snore;


    public WallpaperSurfaceView(Context context) {
        super(context);
        init();
    }

    public WallpaperSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WallpaperSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

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
        moon = new Moon(this);
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

    long startTime;
    long endTime;
    int diffTime;
    @Override
    public void run() {
        while (mIsDrawing) {
            /**取得更新之前的时间**/
            startTime = System.currentTimeMillis();
            /**在这里加上线程安全锁**/
            synchronized (mHolder) {
                /**拿到当前画布 然后锁定**/
                mCanvas =mHolder.lockCanvas();
                draw();
                /**绘制结束后解锁显示在屏幕上**/
                mHolder.unlockCanvasAndPost(mCanvas);
            }

            /**取得更新结束的时间**/
            endTime = System.currentTimeMillis();

            /**计算出一次更新的毫秒数**/
            diffTime  = (int)(endTime - startTime);

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
        mCanvas.drawColor(Color.WHITE);
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
//        house.draw(mCanvas);
//        moon.draw(mCanvas);
//        sun.draw(mCanvas);
        snore.draw(mCanvas);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.house_daytime01);
        mCanvas.drawBitmap(bitmap,0,0,null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int x = (int) event.getRawX();
        int y = (int) event.getRawY() - WorldService.statusBarHeight;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
                break;
            case MotionEvent.ACTION_MOVE://捕获手指触摸移动动作
                break;
            case MotionEvent.ACTION_UP://捕获手指触摸离开动作
                break;
            default:
                break;
        }
        return true;//表示此View拦截处理触摸事件
    }
}
