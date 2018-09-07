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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.angels.floatwindow.floatActor.FloatSurfaceView;
import com.angels.floatwindow.R;
import com.angels.floatwindow.actor.House;
import com.angels.floatwindow.actor.Pig;
import com.angels.floatwindow.actor.Rabbit;
import com.angels.floatwindow.utils.ACLogUtil;
import com.angels.floatwindow.utils.ACToast;
import com.angels.floatwindow.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chencg on 2018/8/24.
 */

public class FloatWindowService extends Service {
    public static int screenHeight;
    public static int screenWeight;
    /*状态栏高度*/
    public static int statusBarHeight;

    /**菜单*/
    private View menuView;
    /**菜单*/
    private ImageView iv01,iv02,iv03,iv04;
    /**小兔子*/
    private static Rabbit rabbit;

    /**容器(房子、太阳、月亮、呼噜)*/
    private FloatSurfaceView floatSurfaceView;

    /**
     *
     * 事件
     *
     * 0：无
     * 1：菜单1
     * 2：菜单2
     * 3：菜单3
     * 4：菜单4
     * */
    private int eveType = 0;
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
        statusBarHeight = AppUtil.getStatusBarHeight(this);
//        int result = 0;
//        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = this.getResources().getDimensionPixelSize(resourceId);
//        }
        if(rabbit == null){
            addWindow();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        addWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        ACLogUtil.i("onDestroy.................");
        stopForeground(true);
        Intent intent = new Intent("com.example.demo.destroy");
        sendBroadcast(intent);
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

//        menuView = LayoutInflater.from(this).inflate(R.layout.float_menu, null);
//        iv01 = menuView.findViewById(R.id.iv01);
//        iv02 = menuView.findViewById(R.id.iv02);
//        iv03 = menuView.findViewById(R.id.iv03);
//        iv04 = menuView.findViewById(R.id.iv04);
//        wmParams.y = screenHeight - menuView.getHeight()*2;
//        wm.addView(menuView, wmParams);
//        menuView.setVisibility(View.INVISIBLE);
        wmParams.x = 0;
        wmParams.y = 200;
        floatSurfaceView = new FloatSurfaceView(this);
        floatSurfaceView.setVisibility(View.INVISIBLE);
        wm.addView(floatSurfaceView, wmParams);
        floatSurfaceView.setOnTouchHouseRabbitListener(houseListener);

        rabbit = new Rabbit(this,wmParams);
        rabbit.setImageResource(R.drawable.anim_rabbit_eat_left);
        rabbit.start();
        wmParams.x = screenWeight/2;
        wmParams.y = screenHeight/2;
        rabbit.setXY(wmParams.x,wmParams.y);
        wm.addView(rabbit, wmParams);
//        rabbit.setOnTouchEventRabbitListener(new Rabbit.OnTouchEventRabbitListener() {
//            @Override
//            public void onTouchEvent(MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
//                        break;
//                    case MotionEvent.ACTION_MOVE://捕获手指触摸移动动作
//                        menuView.setVisibility(View.VISIBLE);
//                        iv01.getX();
//                        int [] loaction = new int[2];
//                        iv02.getLocationOnScreen(loaction);
//                        // 获取相对屏幕的坐标，即以屏幕左上角为原点
//                        int x = (int) event.getRawX();
//                        int y = (int) event.getRawY();
//                        ACLogUtil.i("控件位置-->iv02:" + loaction[0] + "--" + loaction[1]);
//                        ACLogUtil.i("控件位置-->iv01:" + iv02.getWidth() + "--" + iv02.getHeight());
//                        ACLogUtil.i("控件位置-->xy:" + x + "--" + y);
//                        if(isRrash(x,y,iv01)){
//                            selectMenu(iv01);
//                            eveType = 1;
//                        }else if(isRrash(x,y,iv02)){
//                            selectMenu(iv02);
//                            eveType = 2;
//                        }else if(isRrash(x,y,iv03)){
//                            selectMenu(iv03);
//                            eveType = 3;
//                        }else if(isRrash(x,y,iv04)){
//                            selectMenu(iv04);
//                            eveType = 4;
//                        }else{
//                            selectMenu(null);
//                            eveType = 0;
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP://捕获手指触摸离开动作
//                        menuView.setVisibility(View.GONE);
//                        if(eveType != 0){
//                            switch(eveType){
//                                case 1:
//                                    //在需要调用息屏的逻辑调用代码
//                                    boolean root = AndroidRootUtils.checkDeviceRoot();
//                                    if(root){
//                                        AndroidRootUtils.execRootCmd("input keyevent 224");
//                                    }else {
//                                        ACToast.showShort(FloatWindowService.this,"没有root权限");
//                                    }
//                                    ACLogUtil.i("是否root-->root:" + root);
//                                    break;
//                                case 2:
//                                    ACToast.showShort(FloatWindowService.this,"睡觉啦");
//                                    break;
//                                case 3:
//                                    ACToast.showShort(FloatWindowService.this,"散步啦");
//                                    break;
//                                case 4:
//                                    ACToast.showShort(FloatWindowService.this,"运动啦");
//                                    break;
//                            }
//                            eveType = 0;
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
        rabbit.setOnTouchEventRabbitListener(rabbitListener);
    }

    /**是否碰撞*/
    private boolean isRrash(int x,int y,View view){
        int [] loaction = new int[2];
        view.getLocationOnScreen(loaction);
        if(x > loaction[0] && x < (loaction[0] + view.getWidth()) && y > loaction[1] && y < (loaction[1] + view.getHeight())){
            return true;
        }
        return false;
    }
    /**选择菜单*/
    private void selectMenu(ImageView imageView){
        iv01.setImageResource(R.mipmap.ic_launcher);
        iv02.setImageResource(R.mipmap.ic_launcher);
        iv03.setImageResource(R.mipmap.ic_launcher);
        iv04.setImageResource(R.mipmap.ic_launcher);
        if(imageView != null){
            imageView.setImageResource(R.mipmap.fly_000);
        }
    }

    /**主角监听*/
    Rabbit.OnTouchEventRabbitListener rabbitListener = new Rabbit.OnTouchEventRabbitListener() {
        @Override
        public void onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
                    break;
                case MotionEvent.ACTION_MOVE://捕获手指触摸移动动作
                    floatSurfaceView.setVisibility(View.VISIBLE);
                    break;
                case MotionEvent.ACTION_UP://捕获手指触摸离开动作
                    floatSurfaceView.setVisibility(View.GONE);
                    // 获取相对屏幕的坐标，即以屏幕左上角为原点
                    int x = (int) event.getRawX();
                    int y = (int) event.getRawY();
                    ACLogUtil.i("控件位置-->xy:" + x + "--" + y);
                    if(isRrash(x,y,floatSurfaceView)){
                        ACToast.showShort(FloatWindowService.this,"睡觉去啦");
                        floatSurfaceView.sleep();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    /*触摸离开动作时间*/
    private long houseUpTime;
    /**房子监听*/
    FloatSurfaceView.OnTouchEventHouseListener houseListener = new FloatSurfaceView.OnTouchEventHouseListener() {
        @Override
        public void onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
                    break;
                case MotionEvent.ACTION_MOVE://捕获手指触摸移动动作
                    break;
                case MotionEvent.ACTION_UP://捕获手指触摸离开动作
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onWalkUp() {
            rabbit.setVisibility(View.VISIBLE);
            rabbit.updateViewPosition(floatSurfaceView.x + floatSurfaceView.getWidth()/2,floatSurfaceView.y + floatSurfaceView.getHeight()/3*2);
        }

        @Override
        public void onKnock() {

        }

        @Override
        public void onSleep() {
            rabbit.setVisibility(View.INVISIBLE);
        }
    };
}

