package com.angels.floatwindow.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.TextUtils;
import android.text.format.DateFormat;

/**
 * @author chencg
 *         <p>
 *         1,将时间戳转为代表"距现在多久之前"的字符串
 *         2,判断是否同一天
 *         3,获取yyyy年MM月dd日显示日期
 * @ClassName: DateUtil
 * @Description: 功能：时间
 */
public class DateUtil {
    /**
     * 一天的长度（long是10位数的）
     */
    public static long dayLength = 86400;

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr   unix时间戳
     * @param fullDate  是否显示年月日
     * @param inChinese 年、月、日 三个字是否用中文显示
     * @return
     */
    public static String getStandardDate(String timeStr, boolean fullDate, boolean inChinese) {
        if (TextUtils.isEmpty(timeStr)) {
            return "";
        }


        long t = 0;
        try {
            t = Long.parseLong(timeStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "";
        }
        return getStandardDate(t, fullDate, inChinese);
    }

    public static String getStandardDate(long t, boolean fullDate, boolean inChinese) {
        if (t == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        long time = System.currentTimeMillis() - (t * 1000);
        long hour = (long) Math.floor(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.floor(time / 24 / 60 / 60 / 1000.0f);// 天前

        String currentYear = DateFormat.format("yyyy", new Date(System.currentTimeMillis()))
                .toString();
        String year = DateFormat.format("yyyy", new Date(t * 1000)).toString();

        if (day > 0) { // 超过一天
            if (fullDate) { // 显示年月日
                if (inChinese) {
                    sb.append(DateFormat.format("yyyy年MM月dd日", new Date(t * 1000)));
                } else {
                    sb.append(DateFormat.format("yyyy-MM-dd", new Date(t * 1000)));
                }
            } else { // 当前年度不显示年份
                if (currentYear.equals(year)) {
                    if (inChinese) {
                        sb.append(DateFormat.format("MM月dd日", new Date(t * 1000)));
                    } else {
                        sb.append(DateFormat.format("MM-dd", new Date(t * 1000)));
                    }
                } else {
                    if (inChinese) {
                        sb.append(DateFormat.format("yyyy年MM月dd日", new Date(t * 1000)));
                    } else {
                        sb.append(DateFormat.format("yyyy-MM-dd", new Date(t * 1000)));
                    }
                }
            }
        } else if (hour > 0) {
            sb.append(hour + "小时前");
        } else {
            sb.append("刚刚");
        }
        return sb.toString();
    }

    /**
     * 判断是否同一天
     *
     * @param oneTime
     * @param antherTime
     * @return
     */
    public static boolean isSameDay(long oneTime, long antherTime) {
        if (oneTime == antherTime) {
            return true;
        } else if (Math.abs(oneTime - antherTime) > 24 * 60 * 60 * 1000) {
            return false;
        }
        try {
            oneTime = oneTime * (oneTime < 10000000000L ? 1000 : 1);
            antherTime = antherTime * (antherTime < 10000000000L ? 1000 : 1);
            String oneDay = DateFormat.format("yyyy-MM-dd", new Date(oneTime)).toString();
            String antherDay = DateFormat.format("yyyy-MM-dd", new Date(antherTime)).toString();
            return oneDay.equals(antherDay);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取yyyy年MM月dd日显示日期
     *
     * @param time      时间戳
     * @param inChinese 是否用中文显示
     * @param hasDay    是否显示日
     * @return
     */
    public static String GetFullDate(long time, boolean inChinese, boolean hasDay) {
        if (time == 0) {
            return time + "";
        }
        long tempTime = time * (time < 10000000000L ? 1000 : 1);
        CharSequence year = DateFormat.format("yyyy", new Date(tempTime));
        CharSequence month = DateFormat.format("MM", new Date(tempTime));
        CharSequence day = DateFormat.format("dd", new Date(tempTime));
        if (inChinese) {
            return year + "年" + month + "月" + (hasDay ? day + "日" : "");
        }
        return year + "-" + month + (hasDay ? "-" + day : "");
    }

    /**
     * 判断时间是否小于当前时间（及：判断是否过期）
     *
     * @return
     */
    public static boolean isExpiredIn(long time) {
        long nowTime = System.currentTimeMillis() / 1000;
        if (time > nowTime) {
            return true;
        }
        return false;
    }


    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    // long类型转换为String类型
    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType) {
        if (currentTime == 0) {
            return "";
        }
        Date date = null; // long类型转成Date类型
        try {
            date = longToDate(currentTime, formatType);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // long转换为Date类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        long tempTime = currentTime * (currentTime < 10000000000L ? 1000 : 1);
        Date dateOld = new Date(tempTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    public static Date longToDate(long currentTime) {
        String formatType = "yyyy-MM-dd HH:mm:ss";
        long tempTime = currentTime * (currentTime < 10000000000L ? 1000 : 1);
        Date dateOld = new Date(tempTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = null; // 把String类型转换为Date类型
        try {
            date = stringToDate(sDateTime, formatType);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType) {
        Date date = null; // String类型转成date类型
        try {
            date = stringToDate(strTime, formatType);
            if (date == null) {
                return 0;
            } else {
                long currentTime = dateToLong(date); // date类型转成long类型
                return currentTime;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }


    /**
     * 时间格式转换
     *
     * @return返回短时间格式
     */
    public static String getformatType(String time, String fromformatType, String toformatType) {
        long result = stringToLong(time, fromformatType);
        return longToString(result, toformatType);
    }

    /**
     * 获取当天时间的凌晨0：00
     */
    public static long getTodayZero() {
        Date date = new Date();
        long l = 24 * 60 * 60 * 1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000) / 1000;
    }

    /**
     * 获取昨天时间的凌晨0：00
     */
    public static long getYesterdayZero() {
        Date date = new Date();
        long l = 24 * 60 * 60 * 1000; //一天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000) / 1000 - 86400;

        // 今天 1506355200
        // 昨天 1506268800
        // 前天 1506182400
        // 差值 86400
    }

    /**
     * 获取生日
     */
//    public static int getAge(long birthday) {
//        String birthYear = longToString(birthday, "yyyy");
//        String nowYear = longToString(birthday, "yyyy");
//        int age = Integer.parseInt(nowYear) - Integer.parseInt(birthYear);
//        return age;
//    }
    public static int getAge(long birthday){
        Date birthdayDate = longToDate(birthday);
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthdayDate)) {
            throw new IllegalArgumentException("The birthDay is before Now.It 's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthdayDate);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth)
                    age--;
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime 时间区间,半闭合,如[10:00-20:00)
     * @param curTime    需要判断的时间 如10:00
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInTime(String curTime, String sourceTime) {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            long now = sdf.parse(curTime).getTime();
            long start = sdf.parse(args[0]).getTime();
            long end = sdf.parse(args[1]).getTime();
            if (args[1].equals("00:00")) {
                args[1] = "24:00";
            }
            if (end < start) {
                if (now >= end && now < start) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (now >= start && now < end) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
    }

    public static boolean isInTimes(String curTime, String... sourceTimes) {
        if (sourceTimes == null) {
            throw new IllegalArgumentException("Illegal Argument arg: null");
        }
        for (int i = 0; i < sourceTimes.length; i++) {
            String sourceTime = sourceTimes[i];
            if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
                throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
            }
            if (curTime == null || !curTime.contains(":")) {
                throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
            }
            String[] args = sourceTime.split("-");
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            try {
                long now = sdf.parse(curTime).getTime();
                long start = sdf.parse(args[0]).getTime();
                long end = sdf.parse(args[1]).getTime();
                if (args[1].equals("00:00")) {
                    args[1] = "24:00";
                }
                if (now >= start && now <= end) {
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
            }
        }
        return false;
    }

    /**
     * 根据时间 获取对应的月份 天数
     */
    public static int getDaysByTime(long time) {
        Calendar a = Calendar.getInstance();
        a.setTimeInMillis(time);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
    /**时间差，过来多久*/
    public static long getTimeLength(long startTime,long endTime) {
        if(startTime <= 0 || endTime <= 0 || endTime<=startTime){
            return 0;
        }
        long start = startTime * (startTime < 10000000000L ? 1000 : 1);
        long end = endTime * (endTime < 10000000000L ? 1000 : 1);

        return (end - start)/1000;
    }

}

