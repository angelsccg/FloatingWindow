package com.angels.floatwindow.service;

import android.annotation.SuppressLint;

import android.app.Service;

import android.content.Intent;

import android.os.Handler;

import android.os.IBinder;

import android.os.Message;

import android.os.RemoteException;

import android.widget.Toast;

import com.angels.floatwindow.utils.AppUtil;


/**

 * http://blog.csdn.net/liuzg1220

 *

 * @author henry

 *

 */

public class ServiceFloatProtect extends Service {



    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {

                case 1:

                    startService1();

                    break;



                default:

                    break;

            }



        };

    };



    /**
     * 使用aidl 启动Service1
     */
    private StrongService startS1 = new StrongService.Stub() {

        @Override
        public void stopService() throws RemoteException {
            Intent i = new Intent(getBaseContext(), ServiceFloat.class);
            getBaseContext().stopService(i);
        }

        @Override
        public void startService() throws RemoteException {
            Intent i = new Intent(getBaseContext(), ServiceFloat.class);
            getBaseContext().startService(i);
        }
    };


    /**
     * 在内存紧张的时候，系统回收内存时，会回调OnTrimMemory， 重写onTrimMemory当系统清理内存时从新启动Service1
     */

    @Override

    public void onTrimMemory(int level) {
        startService1();
    }

    @SuppressLint("NewApi")
    public void onCreate() {
        startService1();
        /*
         * 此线程用监听Service2的状态
         */
        new Thread() {
            public void run() {
                while (true) {
                    boolean isRun = AppUtil.isServiceWork(ServiceFloatProtect.this,
                            "com.angels.floatwindow.service.ServiceFloat");
                    if (!isRun) {
                        Message msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();
    }



    /**

     * 判断Service1是否还在运行，如果不是则启动Service1
     */

    private void startService1() {
        boolean isRun = AppUtil.isServiceWork(ServiceFloatProtect.this,
                "com.angels.floatwindow.service.ServiceFloat");
        if (isRun == false) {
            try {
                startS1.startService();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return (IBinder) startS1;
    }

}
