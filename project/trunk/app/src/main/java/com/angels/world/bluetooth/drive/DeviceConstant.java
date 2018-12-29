package com.angels.world.bluetooth.drive;

import android.bluetooth.le.ScanFilter;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 手环
 * Created by chencg on 2018/1/9.
 */

public class DeviceConstant {
    /*传递drive的key */
    public static final String DRIVE_KEY = "drive";

    /**
     * 获取过滤器 把filterDriveName过滤出来
     * */
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public static List<ScanFilter> getFilter(){
//        List<ScanFilter> filters = new ArrayList<>();
//        for (int i = 0; i < filterDriveName.length; i++) {
//            ScanFilter filter = new ScanFilter.Builder().setDeviceName(filterDriveName[i]).build();
//            filters.add(filter);
//        }
//        return filters;
//    }
}
