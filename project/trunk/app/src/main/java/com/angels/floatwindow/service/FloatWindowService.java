package com.angels.floatwindow.service;


import android.app.ActivityManager;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.angels.floatwindow.R;
import com.angels.floatwindow.actor.Rabbit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chencg on 2018/8/24.
 */

public class FloatWindowService extends Service {
    public static int screenHeight;
    public static int screenWeight;

    /**菜单*/
    private View menuView;
    /**小兔子*/
    private Rabbit rabbit;

    /**
     * 用于在线程中创建或移除悬浮窗。
     */
    private Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //2、通过Resources获取
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWeight = dm.widthPixels;
        addWindow();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        addWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 判断当前界面是否是桌面
     */
    private boolean isHome() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        return getHomes().contains(rti.get(0).topActivity.getPackageName());
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    private void addWindow(){
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

        wmParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wmParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//        wmParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL; // 窗口位置
        wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 窗口位置
//        wmParams.format = PixelFormat.TRANSPARENT; // 位图格式
        wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式，效果为背景透明

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;//andorid O之后 包括android O
        }else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 设置window type andorid O之前
        }
//        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST; // 窗口的层级关系
//        wmParams.flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 窗口的模式
        /*
        // 设置Window flag
		 * 下面的flags属性的效果形同“锁定”。
		 * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		 * wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
		 * | LayoutParams.FLAG_NOT_FOCUSABLE
		 * | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 以屏幕左上角为原点，设置x、y初始值
//        wmParams.x = 300;
//        wmParams.y = 300;
//        MyFloatView view = new MyFloatView(this,wm,wmParams);
//        wm.addView(view, wmParams);
        menuView = LayoutInflater.from(this).inflate(R.layout.float_menu, null);
        wmParams.y = screenHeight - menuView.getHeight()*2;
        wm.addView(menuView, wmParams);
        menuView.setVisibility(View.INVISIBLE);

        rabbit = new Rabbit(this,wmParams);
        rabbit.setImageResource(R.drawable.anim_rabbit_walk_left);
        rabbit.start();
        wmParams.x = screenWeight/2;
        wmParams.y = screenHeight/2;
        rabbit.setXY(wmParams.x,wmParams.y);
        wm.addView(rabbit, wmParams);


    }

}

