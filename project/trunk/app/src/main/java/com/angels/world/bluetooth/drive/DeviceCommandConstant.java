package com.angels.world.bluetooth.drive;

import java.util.Calendar;

/**
 * Created by chencg on 2018/1/10.
 */

public class DeviceCommandConstant {
    public static String DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";
















    /*==============================================================================*/
    /*##################################小米2#######################################*/
    /*==============================================================================*/


     /*==================================UUID=======================================*/
    /**
     *
     * 1,身份认证
     * */
    public static String MI2_SERVICE_01 = "0000fee1-0000-1000-8000-00805f9b34fb";
    public static String MI2_CHARACTER_01 = "00000009-0000-3512-2118-0009af100700";
    public static String MI2_DESCRIPTOR_01 = "00002902-0000-1000-8000-00805f9b34fb";
    /**
     *
     * 1,实时心率监测
     * */
    public static String MI2_SERVICE_02 = "0000180d-0000-1000-8000-00805f9b34fb";
    public static String MI2_CHARACTER_02_01 = "00002a37-0000-1000-8000-00805f9b34fb";
    public static String MI2_CHARACTER_02_02 = "00002a39-0000-1000-8000-00805f9b34fb";
    public static String MI2_DESCRIPTOR_02_01 = "00002902-0000-1000-8000-00805f9b34fb";
    /**
     *  1.同步数据
     * */
    public static String MI2_SERVICE_03 = "0000fee0-0000-1000-8000-00805f9b34fb";
    public static String MI2_CHARACTER_03_01 = "00000004-0000-3512-2118-0009af100700";
    public static String MI2_CHARACTER_03_02 = "00000005-0000-3512-2118-0009af100700";
    public static String MI2_DESCRIPTOR_03_01 = "00002902-0000-1000-8000-00805f9b34fb";
    public static String MI2_DESCRIPTOR_03_02 = "00002902-0000-1000-8000-00805f9b34fb";

    /**
     *
     * 1,获取历史心率
     * */
    public static String MI2_SERVICE_04 = "0000fee0-0000-1000-8000-00805f9b34fb";
    public static String MI2_CHARACTER_04_01 = "00000004-0000-3512-2118-0009af100700";
    public static String MI2_CHARACTER_04_02 = "00000005-0000-3512-2118-0009af100700";
    public static String MI2_DESCRIPTOR_04_01 = "00002902-0000-1000-8000-00805f9b34fb";
    /*==================================指令=======================================*/


    /**
     * 身份认证指令 三步
    * 指令
    *
    * */
    /**密钥 SecretKey*/
    public static byte[] MI2_COMMAND_KEY = new byte[]{0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,0x40,0x41,0x42,0x43,0x44,0x45};
    /**第一步*/
    public static byte[] MI2_COMMAND_STEP_01 = new byte[]{0x01,0x08,0x30,0x31,0x32,0x33,0x34,0x35,0x36,0x37,0x38,0x39,0x40,0x41,0x42,0x43,0x44,0x45};
    /**第二步*/
    public static byte[] MI2_COMMAND_STEP_02 = new byte[]{0x02,0x08};
    /**第三步*/
    public static byte[] MI2_COMMAND_STEP_03 = new byte[]{0x03,0x08};
    /**
     * 实时心率监测 指令
     * */
    public static byte[] MI2_COMMAND_HEART = new byte[]{0x15,0x02,0x01};
    /**
     * 同步 指令
     *
     * */
    /** type 0x01：同步 步数  0x02：同步 历史心率 */
    public static byte MI2_COMMAND_SYNC_01 = 0x01;
    public static byte MI2_COMMAND_SYNC_02 = 0x02;
    public static byte[] mi2CommandSync(byte type){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR); // 获取当前年份
        int month = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int day = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        int way = c.get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期
        int hour = c.get(Calendar.HOUR_OF_DAY);//时
        int minute = c.get(Calendar.MINUTE);//分
        byte[] yearBytes = fromUint16(year);
//                byte[] bytes = new byte[]{0x01,0x01, (byte) 0xe2,0x07,0x01,0x12,0x00,0x00,0x00,0x20};
//        byte[] bytes = new byte[]{0x01,type, (byte) 0xe2,0x07, 0x02, 0x08,0x00,0x00,0x00,0x30};
        byte[] bytes = new byte[]{0x01,type, yearBytes[0],yearBytes[1], (byte) month, (byte) day,0x00,0x00,0x00,0x20};
//        byte[] bytes = new byte[]{0x01,0x01, yearBytes[0],yearBytes[1], (byte) month, (byte) day,(byte)hour,(byte)(minute-10),0x00,0x20};
        return bytes;
    }
    /**
     * 同步指令 最后一步开始同步
     * */
    public static byte[] MI2_COMMAND_SYNC_END = new byte[]{0x02};


     /*==============================================================================*/
    /*##################################GetFit2.0#######################################*/
    /*==============================================================================*/

      /*==================================UUID=======================================*/
    /**
     * GetFit2.0
     * */
//    public static String GF_SERVICE_01 = "78770001-6765-7466-6974-322e61706901";
//    public static String GF_CHARACTER_01 = "78770002-6765-7466-6974-322e61706901";
//    public static String GF_CHARACTER_02 = "78770003-6765-7466-6974-322e61706901";
//    public static String GF_DESCRIPTOR_02 = "00002902-0000-1000-8000-00805f9b34fb";
    public static String GF_SERVICE_01 = "6e400001-b5a3-f393-e0a9-e50e24dcca9e";
    public static String GF_CHARACTER_01 = "6e400002-b5a3-f393-e0a9-e50e24dcca9e";
    public static String GF_CHARACTER_02 = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";
    public static String GF_CHARACTER_03 = "6e400004-b5a3-f393-e0a9-e50e24dcca9e";
    public static String GF_DESCRIPTOR_02 = "00002902-0000-1000-8000-00805f9b34fb";
    public static String GF_DESCRIPTOR_03 = "00002902-0000-1000-8000-00805f9b34fb";
//    public static String GF_SERVICE_01 = "00001800-0000-1000-8000-00805f9b34fb";
//    public static String GF_CHARACTER_01 = "00002a00-0000-1000-8000-00805f9b34fb";
//    public static String GF_CHARACTER_02 = "00002a01-0000-1000-8000-00805f9b34fb";
//    public static String GF_DESCRIPTOR_02 = "00002902-0000-1000-8000-00805f9b34fb";

    /**开启心率指令*/
    public static byte[] GF_HEART_START = new byte[]{0x24,0x48,0x31};
//    public static byte[] GF_HEART_START = "$H1".getBytes();
    /**关闭心率指令*/
    public static byte[] GF_HEART_STOP = new byte[]{0x24,0x48,0x30};
//    public static byte[] GF_HEART_STOP = "$H0".getBytes();
    /**震动*/
    public static byte[] GF_SHAKE = new byte[]{0x24,0x50};
    /**同步数据*/
    public static byte[] GF_SYNC_DATA = new byte[]{0x24,0x53};
//    public static byte[] GF_SYNC_DATA = new byte[]{0x24,0x5A};

      /*==============================================================================*/
    /*##################################温度计#######################################*/
    /*==============================================================================*/
  /*==================================UUID=======================================*/
    /**
     *
     * Thermometer
     * */
    public static String THER_SERVICE_01 = "0000fff0-0000-1000-8000-00805f9b34fb";
    public static String THER_CHARACTER_01 = "0000fff1-0000-1000-8000-00805f9b34fb";
    public static String THER_CHARACTER_02 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static String THER_DESCRIPTOR_01 = "00002902-0000-1000-8000-00805f9b34fb";




       /*==============================================================================*/
    /*##################################血糖仪#######################################*/
    /*==============================================================================*/
  /*==================================UUID=======================================*/
    /**
     *
     * Bioland-BGM
     * */
    public static String BGM_SERVICE_01 = "00001000-0000-1000-8000-00805f9b34fb";
    public static String BGM_CHARACTER_01 = "00001001-0000-1000-8000-00805f9b34fb";
    public static String BGM_CHARACTER_02 = "00001002-0000-1000-8000-00805f9b34fb";
    public static String BGM_DESCRIPTOR_02 = "00002902-0000-1000-8000-00805f9b34fb";

     /*==================================指令=======================================*/
    /**连接确定  0x5A 包头  0x0A长度  0x0B类型 */
//    public static byte[] BGM_OK = new byte[]{0x5A,0x0A,0x00,0x12,0x03,0x10,0x09,0x16,0x19, (byte) 0xc3};
    /**获取设备信息指令  0x5A 包头  0x0A长度  0x0B类型 */
    public static byte[] getOk(){
        return getSystemdate((byte)0x5A,(byte)0x0A,(byte)0x00);
    }

    public static byte[] getSystemdate(byte cmdStart, byte cmdLength, byte cmdSort) {
        byte cmdCheck = 0;
        byte[] cmdData = new byte[] {};
        Calendar c = Calendar.getInstance();//
        cmdData = new byte[cmdLength];
        cmdData[0] =  cmdStart;
        cmdData[1] =  cmdLength;
        cmdData[2] =  cmdSort;
        cmdData[3] = (byte) (c.get(Calendar.YEAR) - 2000);
        cmdData[4] = (byte) (c.get(Calendar.MONTH) + 1);
        cmdData[5] = (byte) c.get(Calendar.DAY_OF_MONTH);
        cmdData[6] = (byte) c.get(Calendar.HOUR_OF_DAY);//时
        cmdData[7] = (byte)  c.get(Calendar.MINUTE);//分
        cmdData[8] = (byte)  c.get(Calendar.SECOND);//秒

        for (int i = 0; i < cmdData.length; i++) {
            cmdCheck += cmdData[i];
        }
        cmdData[9] = (byte) (cmdCheck + 2);
        // cmdData[9] = 0;
        // cmdData[10] = 0;cmdData= new byte[] {90, 11, 5, 15, 6, 1, 8, 56,
        // (byte) 192,0,0};
        return cmdData;
    }

    /*========================================特有的工具方法==============================================*/
    public static int toUint16(byte... bytes) {
        return (bytes[0] & 0xff) | ((bytes[1] & 0xff) << 8);
    }
    public static byte[] fromUint16(int value) {
        return new byte[] {
                (byte) (value & 0xff),
                (byte) ((value >> 8) & 0xff),
        };
    }
}
