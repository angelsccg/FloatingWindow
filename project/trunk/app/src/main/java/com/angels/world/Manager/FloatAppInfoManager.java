package com.angels.world.Manager;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.angels.library.utils.AcAppInfoUtil;
import com.angels.library.utils.AcLogUtil;
import com.angels.world.R;

import java.util.Calendar;

public class FloatAppInfoManager {

    private static View view;

    public static void addWindow(Context context){
        if(view != null){
            return;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wmParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 窗口位置
//        wmParams.format = PixelFormat.TRANSPARENT; // 位图格式
        wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式，效果为背景透明

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;//andorid O之后 包括android O
        }else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 设置window type andorid O之前
        }
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

        view = LayoutInflater.from(context).inflate(R.layout.float_appinfo, null);
        TextView tvInfo = view.findViewById(R.id.tv_info);
//        TextView tvInfo = new TextView(context);
        tvInfo.setText(AcAppInfoUtil.getAppPackageName(context));
        AcLogUtil.d("AppinfoService-->addView");
        wm.addView(view, wmParams);
    }

    public static void removeWindow(Context context){
        if(view != null){
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.removeViewImmediate(view);
            view = null;
        }
    }

    /**
     * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。
     *
     * @param context
     *            可传入应用程序上下文。
     */
//    public static void updateUsedPercent(Context context) {
//        if (smallWindow != null) {
//            TextView percentView = (TextView) smallWindow.findViewById(R.id.percent);
//            percentView.setText(getUsedPercentValue(context));
//        }
//    }
    /**
     * 更新小悬浮窗的TextView上的数据
     *
     * @param context
     *            可传入应用程序上下文。
     */
    public static void update(Context context,String content) {
        if (view != null) {
            TextView tvInfo = view.findViewById(R.id.tv_info);
            tvInfo.setText(content);
        }
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isWindowShowing() {
        return view != null;
    }


    /**
     * 用于控制在屏幕上添加或移除悬浮窗
     */
    private static WindowManager mWindowManager;

    /**
     * 用于获取手机可用内存
     */
    private static ActivityManager mActivityManager;



    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context
     *            必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
     *
     * @param context
     *            可传入应用程序上下文。
     * @return ActivityManager的实例，用于获取手机可用内存。
     */
    private static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

    /**
     * 计算已使用内存的百分比，并返回。
     *
     * @param context
     *            可传入应用程序上下文。
     * @return 已使用内存的百分比，以字符串形式返回。
     */
    public static String getUsedPercentValue(Context context) {
        String dir = "/proc/meminfo";
        try {
            /*FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
            long availableSize = getAvailableMemory(context) / 1024;
            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
            return percent + "%";*/
            Calendar Cld=Calendar.getInstance();
            int YY=Cld.get(Calendar.YEAR);
            int MM=Cld.get(Calendar.MONTH)+1;
            int DD=Cld.get(Calendar.DATE);
            int HH=Cld.get(Calendar.HOUR_OF_DAY);
            int mm=Cld.get(Calendar.MINUTE);
            int SS=Cld.get(Calendar.SECOND);
            int MI=Cld.get(Calendar.MILLISECOND);
            String curTime=YY+" "+MM+" "+DD+" "+HH+" "+mm+" "+SS+" "+MI+"";
            return curTime;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "悬浮窗";
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。
     *
     * @param context
     *            可传入应用程序上下文。
     * @return 当前可用内存。
     */
    private static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(mi);
        return mi.availMem;
    }

}
