package com.angels.library.utils;

import android.util.Log;

import com.angels.library.BuildConfig;

/**
 * Name: LogUtil
 * Author: ccg
 * Comment: //TODO
 * Date: 2017-08-24 14:16
 */

public class AcLogUtil {
    static String className;//类名
    static String methodName;//方法名
    static int lineNumber;//行数

    private AcLogUtil(){
        /* Protect from instantiations */
    }

    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }

    private static String createLog( String log ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(methodName);
        buffer.append("(").append(className).append(":").append(lineNumber).append(")");
        buffer.append(log);
        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements){
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }


    public static void e(String message){
        if (!isDebuggable())
            return;

        // Throwable instance must be created before any methods
        getMethodNames(new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }


    public static void i(String message){
        if (!isDebuggable())
            return;
        getMethodNames(new Throwable().getStackTrace());
        Log.i(className, createLog(message));

//        if(message.length() > 4000) {
//            for(int i=0;i<message.length();i+=4000){
//                if(i+4000<message.length())
//                    Log.i(className,"http-->"+i+"--"+message.substring(i, i+4000));
//                else
//                    Log.i(className+i,"http-->"+i+"--"+message.substring(i, message.length()));
//            }
//        } else{
//            Log.i(className,message);
//        }
//        Log.i("health", message);
    }

    public static void d(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void v(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(String message){
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }
}
