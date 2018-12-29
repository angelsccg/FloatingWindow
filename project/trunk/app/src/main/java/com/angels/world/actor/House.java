package com.angels.world.actor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceView;

import com.angels.world.R;
import com.angels.world.floatActor.ActorBase;
import com.angels.world.service.LiveWallpaperService;

public class House extends ActorBase {
    private Bitmap bitmap;
    private Context context;
    public House(Context context) {
        this.context = context;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.house_daytime01);
    }

    private Rect rectsrc,rectFdst;
    //绘图操作
    @Override
    public void draw(Canvas mCanvas) {
        // 指定图片绘制区域
        rectsrc = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        // 指定图片在屏幕上显示的区域
        rectFdst = new Rect(0,0, bitmap.getWidth(),bitmap.getWidth());
        mCanvas.drawBitmap(bitmap,rectsrc,rectFdst,null);
//        mCanvas.drawPath(mPath,mPaint);
    }
}
