package com.angels.world.actor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ContainerBase extends RelativeLayout {

    /**相对屏幕的坐标*/
    protected int x,y;

    public ContainerBase(Context context) {
        super(context);
    }

}
