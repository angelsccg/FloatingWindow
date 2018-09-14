package com.angels.floatwindow.utils;

public class AngelsFloatUtil {

    /**
     * 敲门时候的提醒
     * 睡觉还不到一个小时，被连续敲门了5次就强制醒来，敲门间隔10秒
     * @param knockCount 敲门次数
     *
     *  1，睡觉中...
     *  2，谁啊...
     *  3，让不让睡觉了
     *  4，吵什么吵
     * */
    public static String getSleep(int knockCount){
        switch (knockCount){
            case 1:
                return "睡觉中...";
            case 2:
                return "谁啊...";
            case 3:
                return "我被床铺附身了";
            case 4:
                return "不想起床啊";
            default:
                return "睡个觉都这么难";
        }
    }
}
