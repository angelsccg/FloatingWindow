package com.angels.floatwindow.service;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RemoteViewsService;

import com.angels.floatwindow.floatActor.FloatSurfaceView;
import com.angels.floatwindow.R;
import com.angels.floatwindow.actor.Rabbit;
import com.angels.floatwindow.utils.AppUtil;
import com.angels.library.utils.AcLogUtil;
import com.angels.library.utils.AcToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chencg on 2018/8/24.
 */

public class AppWidgetService extends RemoteViewsService {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}

