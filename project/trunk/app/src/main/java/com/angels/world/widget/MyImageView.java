package com.angels.world.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends ImageView {
    private Paint paint;
    public MyImageView(Context context) {
        super(context);
        init();
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        Drawable drawable = getDrawable();
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bm= bd.getBitmap();
        int rateBm = bm.getWidth()/bm.getHeight();
        int rateV = getWidth()/getHeight();
        Rect src = new Rect(0,0,bm.getWidth(),bm.getHeight());
        Rect dst;
        if(rateBm > rateV){
            dst = new Rect(0,Math.abs(getHeight()-bm.getHeight())/2,getWidth(),getWidth()*rateBm);
        }else {
            dst = new Rect(Math.abs(getWidth()-bm.getWidth())/2,0,getHeight()/rateBm,getHeight());
        }
        canvas.drawBitmap(bm,src,dst,paint);
    }
}
