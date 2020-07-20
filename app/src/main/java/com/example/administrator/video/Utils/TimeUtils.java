package com.example.administrator.video.Utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wood on 2018/4/24.
 */

public class TimeUtils {


    public static final int SECOND = 1000;

    public static final int MINUTE = SECOND * 60;

    public static final int HOUR = MINUTE * 60;


    public static final long getCurrentTimeStamp() {
        return System.currentTimeMillis();
    }

    public static final String seconds2HourDecimal(int seconds) {
        float num = (float) seconds / (60 * 60);
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(num);
    }


    public static final int seconds2Minute(int seconds) {
        return seconds / 60;
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }


    /**
     * 毫秒时间戳(13位)转字符串
     *
     * @param timeStamp   13位毫秒时间戳
     * @param dividerChar 年月日分隔符
     * @return
     */
    public static final String timeStampToString(long timeStamp, char dividerChar) {
        String format = "yyyy" + dividerChar + "MM" + dividerChar + "dd HH:mm:ss";
        return timeStampToString(timeStamp, format);
    }


    /**
     * 毫秒时间戳(13位)转字符串
     *
     * @param timeStamp 13位毫秒时间戳
     * @param format    日期格式
     * @return
     */
    public static final String timeStampToString(long timeStamp, String format) {
        if (timeStamp == -1) {
            return "";
        }
        if (format == null || format.trim().equals("")) {
            format = "yyyy/MM/dd HH:mm:ss";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(new Date(timeStamp));
        } catch (Exception e) {
        }

        return "";
    }


    public static final int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static final int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static final int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
            return sdf.parse(date_str).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0l;
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @return
     */
    public static int date2year(String date_str) {
        try {

            if (date_str.length() > 4) {
                return Integer.parseInt(date_str.substring(0, 4));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 根据毫秒时间戳来格式化字符串
     * 今天显示今天、昨天显示昨天、前天显示前天.
     * 早于前天的显示具体年-月-日，如2017-06-12；
     *
     * @param timeStamp 毫秒值
     * @return 今天 昨天 前天 或者 yyyy-MM-dd HH:mm:ss类型字符串
     */
    public static String friendlyTime(long timeStamp) {
        long curTimeMillis = System.currentTimeMillis();
        Date curDate = new Date(curTimeMillis);
        int todayHoursSeconds = curDate.getHours() * 60 * 60;
        int todayMinutesSeconds = curDate.getMinutes() * 60;
        int todaySeconds = curDate.getSeconds();
        int todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000;
        long todayStartMillis = curTimeMillis - todayMillis;
        if (timeStamp >= todayStartMillis) {
            return "今天";
        }
        int oneDayMillis = 24 * 60 * 60 * 1000;
        long yesterdayStartMilis = todayStartMillis - oneDayMillis;
        if (timeStamp >= yesterdayStartMilis) {
            return "昨天";
        }
        long yesterdayBeforeStartMilis = yesterdayStartMilis - oneDayMillis;
        if (timeStamp >= yesterdayBeforeStartMilis) {
            return "前天";
        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(new Date(timeStamp));
    }

}
