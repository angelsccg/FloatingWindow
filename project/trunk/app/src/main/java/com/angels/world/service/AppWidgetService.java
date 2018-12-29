package com.angels.world.service;


import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViewsService;

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

