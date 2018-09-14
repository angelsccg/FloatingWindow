package com.angels.world.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.angels.world.service.AppWidgetService;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.angels.floatwindow.destroy")){
            Intent sevice = new Intent(context, AppWidgetService.class);
            context.startService(sevice);
        }
    }
}
