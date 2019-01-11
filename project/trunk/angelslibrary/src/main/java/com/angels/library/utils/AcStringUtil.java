package com.angels.library.utils;

import android.text.TextUtils;

public class AcStringUtil {
    /**
     *  @param  str
     *  @param  separator 分隔符  如：‘,’
     * */
    public static byte[] stringToByte(String str,String separator){
        if(str == null || TextUtils.isEmpty(str)){
            return null;
        }
        String[] strArray = str.replace(" ","").split(separator);
        byte[] data = new byte[strArray.length];
        for (int i = 0; i < strArray.length; i++) {
//            data[i] = (byte) Integer.parseInt(strArray[i]);
            data[i] = Byte.parseByte(strArray[i]);
        }
        return data;
    }
}
