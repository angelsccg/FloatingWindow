package com.angels.floatwindow.constant;

/**
 * Name: RequestUrl
 * Author: ccg
 * Comment: 接口
 * Date: 2017-08-17 14:42
 */

public class RequestUrl {
    public static final String[] APP_ENVI_NAME = {"测试",""};
    /**测试环境*/
    static public final int CONFIG_TYPE_IN = 0;
    /**线上环境*/
    static public final int CONFIG_TYPE_OUT = 1;

    /**测试内网*/
    static private final String IN_API = "http://192.168.50.145:8080/SIZHEN/";
    /**线上环境*/
    static private final String OUT_API = "http://sizhen.shinoa.cn:18091/SIZHEN/";
    /**
     * 测试环境和线上环境的切换只需要修改这个字段就行了 [测试环境 =CONFIG_TYPE_IN ，线上环境 =CONFIG_TYPE_OUT,演示环境 =CONFIG_TYPE_SHOW]，
     */
    public static final int configType = CONFIG_TYPE_IN;
//    public static final int configType = CONFIG_TYPE_OUT;

    //接口
    static public String URL_API = getApi();

    /**测试 到时候删掉*/
    static public final String TEST2 = URL_API + "adviser.getCommentList";
    /*
     * 获取API地址
     *
     * @return
     */
    static public final String getApi() {
        switch (configType) {
            case CONFIG_TYPE_IN:
                return RequestUrl.IN_API;
            case CONFIG_TYPE_OUT:
                return RequestUrl.OUT_API;
            default:
                return RequestUrl.OUT_API;
        }
    }
}
