package com.angels.floatwindow.floatActor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

public abstract class ActorBase {
    /**相对父容器的坐标*/
    protected int x,y;

    public ActorBase(){

    }

    protected abstract void draw(Canvas mCanvas);
   
}
