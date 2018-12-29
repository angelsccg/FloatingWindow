package com.angels.world.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.angels.world.R;
import com.angels.world.actor.House;
import com.angels.world.actor.Bg;

/**
 * 动态壁纸
 */
public class LiveWallpaperService extends WallpaperService {

    public static int screenHeight;
    public static int screenWeight;

    private MediaPlayer mp;
    private int progress = 0;

    @Override
    public Engine onCreateEngine() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWeight = dm.widthPixels;
        return new MyEngine();
    }

    //自定义Engine
    class VideoEngine extends Engine {
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            //可以设置点击事件
            setTouchEventsEnabled(true);
        }


        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
//            floatSurfaceView.surfaceCreated(holder);

            //把视频输出到SurfaceHolder上面
            if (mp != null && mp.isPlaying()) {
                return;
            }
            //可以设置SD卡的视频
            mp = MediaPlayer.create(getApplicationContext(), R.raw.br);
            //这句话并不简单
            mp.setSurface(holder.getSurface());
//            //一般是这样设置，这里这样设置报错
//            mp.setDisplay(holder);
            //重复播放
            mp.setLooping(true);
            mp.setVolume(0,0);
            mp.start();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
        }


        //当桌面不可见的时候的处理
        @Override
        public void onVisibilityChanged(boolean visible) {
            if (visible) {
                if (mp != null) return;
                mp = MediaPlayer.create(getApplicationContext(), R.raw.br);
                mp.setSurface(getSurfaceHolder().getSurface());
                mp.setLooping(true);
                //获取进度播放
                mp.seekTo(progress);
                mp.start();
            } else {
                if (mp != null && mp.isPlaying()) {
                    //保存进度
                    progress = mp.getCurrentPosition();
                    mp.stop();
                    mp.release();
                    mp = null;
                }
            }
        }

        @Override
        public void onDestroy() {
            if (mp != null) {
                mp.stop();
                mp.release();
            }
            super.onDestroy();
        }
    }


    class MyEngine extends Engine{
        private Paint paint;
        private Bitmap bitmap;
        private Bg bg;
        private boolean mVisible;
        Handler mHandler = new Handler();
        private final Runnable drawTarget = new Runnable() {
            @Override
            public void run() {
                drawFrame();
            }
        };

        public MyEngine(){
            paint = new Paint();
            paint.setColor(Color.parseColor("#1A000000"));
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_pig);
            bg = new Bg(LiveWallpaperService.this);
        }
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            //  设置壁纸的触碰事件为true
            setTouchEventsEnabled(true);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            mVisible = visible;
            if(visible){
                drawFrame();
            }else{
                //  如果界面不可见，删除回调
                mHandler.removeCallbacks(drawTarget);
            }
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);
            drawFrame();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            //  删除回调
            mHandler.removeCallbacks(drawTarget);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            //   检测到滑动操作
            super.onTouchEvent(event);
        }

        private void drawFrame(){
            final SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try{
                canvas = holder.lockCanvas();
                if(canvas != null){
                    canvas.drawColor(0xffffffff);
                    //  在触碰点绘制图像
                    draw(canvas);
                }
            }finally {
                if(canvas != null){
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            //  调度下一次重绘
            mHandler.removeCallbacks(drawTarget);
            if(mVisible){
                //  每隔0.1秒执行drawTarget一次
                mHandler.postDelayed(drawTarget,100);
            }
        }
        private void draw(Canvas canvas){
//            // 指定图片绘制区域
//            Rect rectsrc = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
//            // 指定图片在屏幕上显示的区域
//            Rect rectFdst = new Rect(0,0,screenWeight,screenHeight);
//            canvas.drawBitmap(bitmap,rectsrc,rectFdst,null);

//            house.draw(canvas);
            bg.draw(canvas);
            canvas.drawRect(0,0,screenWeight,screenHeight,paint);
        }
    }

}
