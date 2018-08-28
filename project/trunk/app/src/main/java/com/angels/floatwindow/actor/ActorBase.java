package com.angels.floatwindow.actor;

import android.content.Context;
import android.widget.ImageView;

public class ActorBase extends ImageView {

    /**相对屏幕的坐标*/
    protected int x,y;

    public ActorBase(Context context) {
        super(context);
    }

    public void setXY(int x,int y){
        this.x = x;
        this.y = y;
    }
}
