package com.angels.world.floatActor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceView;

import com.angels.world.R;
/**太阳*/
public class Sun extends ActorBase {
    /**动画时间 每200毫秒*/
    public static final int ANIM_TIME = 250;
    /**太阳图片*/
    private int[] bitmapIds = {R.mipmap.sun01,R.mipmap.sun02};
    private Bitmap[] bitmaps = new Bitmap[bitmapIds.length];
    private SurfaceView surfaceView;

    /**当前绘制第几张图*/
    private int position = 0;
    public Sun(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        initBitmap();
    }

    private void initBitmap() {
        for (int i = 0; i < bitmapIds.length; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(surfaceView.getResources(), bitmapIds[i]);
        }
    }
    /**次数*/
    private int times = 0;
    //绘图操作
    @Override
    protected void draw(Canvas mCanvas) {
        // 指定图片绘制区域
        Rect rectsrc = new Rect(0,0,bitmaps[position].getWidth(),bitmaps[position].getHeight());
        // 指定图片在屏幕上显示的区域
        int pointX = surfaceView.getWidth() - bitmaps[position].getWidth();
        Rect rectFdst = new Rect(pointX,0,bitmaps[position].getWidth() + pointX,bitmaps[position].getHeight());
        mCanvas.drawBitmap(bitmaps[position],rectsrc,rectFdst,null);
        int a = ++times%(ANIM_TIME/FloatSurfaceView.TIME_IN_FRAME);
        if(a == 0){
            times = 0;
            position ++;
            if(position >= bitmaps.length){
                position = 0;
            }
        }
//        mCanvas.drawPath(mPath,mPaint);
    }
}
