package com.angels.world.actor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceView;

import com.angels.world.R;
import com.angels.world.floatActor.ActorBase;
import com.angels.world.floatActor.FloatSurfaceView;
import com.angels.world.service.LiveWallpaperService;

/**太阳*/
public class Bg extends ActorBase {
    /**动画时间 每200毫秒*/
    public static final int ANIM_TIME = 250;
    /**太阳图片*/
    private int[] bitmapIds = {R.mipmap.ic_pig01,R.mipmap.ic_pig02,R.mipmap.ic_pig03,R.mipmap.ic_pig04,R.mipmap.ic_pig05,R.mipmap.ic_pig06,R.mipmap.ic_pig07,R.mipmap.ic_pig08};
    private Bitmap[] bitmaps = new Bitmap[bitmapIds.length];
    private Context context;

    /**当前绘制第几张图*/
    private int position = 0;
    public Bg(Context context) {
        this.context = context;
        initBitmap();
    }

    private void initBitmap() {
        for (int i = 0; i < bitmapIds.length; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(context.getResources(), bitmapIds[i]);
        }
    }
    /**次数*/
    private int times = 0;
    private Rect rectsrc,rectFdst;
    //绘图操作
    @Override
    public void draw(Canvas mCanvas) {
        // 指定图片绘制区域
        rectsrc = new Rect(0,0,bitmaps[position].getWidth(),bitmaps[position].getHeight());
        // 指定图片在屏幕上显示的区域
        rectFdst = new Rect(0,0, LiveWallpaperService.screenWeight,LiveWallpaperService.screenHeight);
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
