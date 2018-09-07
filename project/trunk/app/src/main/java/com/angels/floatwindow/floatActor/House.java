package com.angels.floatwindow.floatActor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.SurfaceView;

import com.angels.floatwindow.R;

public class House extends ActorBase {
    private Bitmap bitmap;
    private SurfaceView surfaceView;
    public House(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
        bitmap = BitmapFactory.decodeResource(surfaceView.getResources(), R.mipmap.house_daytime01);
    }

    //绘图操作
    @Override
    protected void draw(Canvas mCanvas) {
        // 指定图片绘制区域
        Rect rectsrc = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        // 指定图片在屏幕上显示的区域
        Rect rectFdst = new Rect(0,0,surfaceView.getWidth(),surfaceView.getHeight());
        mCanvas.drawBitmap(bitmap,rectsrc,rectFdst,null);
//        mCanvas.drawPath(mPath,mPaint);
    }
}
