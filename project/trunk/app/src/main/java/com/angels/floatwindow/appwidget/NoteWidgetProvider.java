package com.angels.floatwindow.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.angels.floatwindow.R;
import com.angels.floatwindow.activity.MainActivity;
import com.angels.floatwindow.activity.launcher.NoteWidgetActivity;
import com.angels.floatwindow.bd.NoteDBManager;
import com.angels.floatwindow.mode.Note;
import com.angels.floatwindow.service.AppWidgetService;
import com.angels.library.utils.AcLogUtil;
import com.angels.library.utils.AcToastUtil;

import java.util.List;

/**
 * 如果要更新桌面工具的画面
 * ComponentName componentName = new ComponentName(getApplicationContext(), ClockAppWidgetProvider.class);
 * widgetManager.updateAppWidget(componentName, remoteView);
 *
 *
 * */
public class NoteWidgetProvider extends AppWidgetProvider {

    public static final String TITLE_ACTION = "com.angels.action.TYPE_TITLE";
    public static final String LIST_ACTION = "com.angels.action.TYPE_LIST";
    public static final String CONTENT_ACTION = "com.angels.action.TYPE_CONTENT";

    public NoteWidgetProvider() {
        super();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        AcLogUtil.i("NoteWidgetProvider-->onUpdate");
        // 调用的间隔由res/xml/app_widget_info_note.xml下的updatePeriodMillis决定
        // 下面的for循环是update app widgets的标准写法
        // N是桌面上该小部件的数目
//        final int N = appWidgetIds.length;
//        for (int i = 0; i < N; i++) {
//            // 对每一个小部件进行更新
//            int appWidgetId = appWidgetIds[i];
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_note);
//            Intent skipIntent = new Intent(context, MainActivity.class);
//            PendingIntent pi = PendingIntent.getActivity(context, 200, skipIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.tv_content, pi);
//
//            // TODO 对remoteViews进行操作，比如添加点击事件跳转系统时钟
//            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
//        }

//        //ppWidgetIds.length是桌面上该小部件的数目
//        for (int i = 0; i < appWidgetIds.length; i++) {
//            //创建一个Intent对象，跳转到app界面
//            Intent intent = new Intent(context, NoteWidgetActivity.class);
//            //创建一个PendingIntent，包住intent
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//            //获取wiget布局
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_note);
//            remoteViews.setOnClickPendingIntent(R.id.lv_note, pendingIntent);
//            // 对每一个小部件进行更新
//            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
//        }

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_note);
        Note note = NoteDBManager.queryNewNote();
        AcLogUtil.i("NoteWidgetProvider-->onUpdate-->note:" + note.getContent());
        if(note.getId() != null){
            remoteViews.setTextViewText(R.id.tv_content,note.getContent());
        }
        //创建一个广播，点击按钮发送该广播
        Intent intent = new Intent();
        intent.setAction(TITLE_ACTION);
        intent.setComponent(new ComponentName(context,NoteWidgetProvider.class));//必须写
//            Intent intent = new Intent(context, NoteWidgetActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_title, pendingIntent);

        //创建一个广播，点击按钮发送该广播
        Intent intent2 = new Intent(context,NoteWidgetActivity.class);
        intent2.setAction(TITLE_ACTION);
        PendingIntent pendingIntentActivity = PendingIntent.getActivity(context,2,intent2,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_content, pendingIntentActivity);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
        //如果你添加了多个实例的情况下需要下面的处理
//        for (int i = 0; i < appWidgetIds.length; i++) {
//            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
//        }
    }
    /**
     * 接收广播
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        AcLogUtil.i("NoteWidgetProvider-->onReceive-->action:" + action);
        switch (action) {
            case TITLE_ACTION:
                Toast.makeText(context, "我是备忘录", Toast.LENGTH_SHORT).show();
                break;
            case AppWidgetManager.ACTION_APPWIDGET_UPDATE:
//                AppWidgetManager mgr = AppWidgetManager.getInstance(context);
//                mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.tv_content);

                //由AppWidgetManager处理Wiget。
                AppWidgetManager awm = AppWidgetManager.getInstance(context);
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget_note);
                ComponentName cn = new ComponentName(context, NoteWidgetProvider.class);
                Note note = NoteDBManager.queryNewNote();
                if(note.getId() != null){
                    remoteViews.setTextViewText(R.id.tv_content,note.getContent());
                }
                awm.updateAppWidget(cn, remoteViews);

//                Bundle extras = intent.getExtras();
//                AcLogUtil.i("NoteWidgetProvider-->onReceive-->extras:" + extras);
//                if (extras != null) {
//                    int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
//                    AcLogUtil.i("NoteWidgetProvider-->onReceive-->appWidgetIds:" + appWidgetIds);
//                    if (appWidgetIds != null && appWidgetIds.length > 0) {
//                        this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
//                    }
//                }
                break;
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        AcLogUtil.i("widget-->onAppWidgetOptionsChanged-->");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        // TODO 任意一个小部件被移除时调用
        AcLogUtil.i("widget-->onDeleted-->任意一个小部件被移除时调用");
    }
    @Override
    public void onEnabled(Context context) {
        AcLogUtil.i("widget-->onEnabled-->用户将widget添加桌面了");
    }
    @Override
    public void onDisabled(Context context) {
        // 所有桌面小部件被移除时调用
        // TODO 注销Service
        AcLogUtil.i("widget-->onDeleted-->所有桌面小部件被移除时调用");
    }

}

