package com.kingpivot.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public final static String DATE_FORMAT_DEFAULT = "yyyy-MM-dd";
    public final static String DATE_FORMAT_TIME = "yyyy-MM-dd HH:mm";
    public final static String DATE_FORMAT_ALL = "yyyy-MM-dd HH:mm:ss";

    public static String longToString(long time) {

        return longToString(time, DATE_FORMAT_DEFAULT);

    }

    public static String longToString(long time, String format) {
        if (StringUtils.isBlank(format)) {
            format = DATE_FORMAT_DEFAULT;
        }
        DateTime dTime = new DateTime(time);
        return dTime.toString(format);

    }

    /**
     * 获得现在年份
     *
     * @param
     * @return
     */
    public static String getCurrentYear() {
        return getCurrentYear(DATE_FORMAT_DEFAULT);
    }

    /**
     * 获得当前日期
     *
     * @return
     */
    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd");
    }

    /**
     * 获得当前时间
     *
     * @param format
     * @return
     */
    public static String getCurrentTime(String format) {
        DateTime dTime = new DateTime();
        return dTime.toString(format);
    }

    /**
     * 获得当前学年
     *
     * @param format
     * @return
     */
    public static String getCurrentYear(String format) {
        DateTime dTime = new DateTime(new DateTime().getYear(), 1, 1, 0, 0);
        return dTime.toString(format);
    }

    /**
     * 获取当前日期是礼拜几
     *
     * @param time
     * @return 1:星期一
     * 2:星期二
     * 3:星期三
     * 4:星期四
     * 5:星期五
     * 6:星期六
     * 7:星期天
     */
    public static int getWeekOfDate(long time) {
        Date date = new Date(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }


    public static long parseStringToLong(String dateStr) {
        dateStr = dateStr.trim();
        if (dateStr.length() == 19 || dateStr.length() == 21) {
            if (dateStr.length() == 21) {
                dateStr = dateStr.substring(0, 20);
            }
            try {
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(dateStr.substring(0, 4)),
                        Integer.parseInt(dateStr.substring(5, 7)) - 1,
                        Integer.parseInt(dateStr.substring(8, 10)),
                        Integer.parseInt(dateStr.substring(11, 13)),
                        Integer.parseInt(dateStr.substring(14, 16)),
                        Integer.parseInt(dateStr.substring(17, 19)));
                cal.set(Calendar.MILLISECOND, 0);
                return (cal.getTime().getTime());
            } catch (Exception e) {
                return 0;
            }

        } else if (dateStr.length() == 16) {
            try {
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(dateStr.substring(0, 4)),
                        Integer.parseInt(dateStr.substring(5, 7)) - 1,
                        Integer.parseInt(dateStr.substring(8, 10)),
                        Integer.parseInt(dateStr.substring(11, 13)),
                        Integer.parseInt(dateStr.substring(14, 16)));
                cal.set(Calendar.MILLISECOND, 0);
                return (cal.getTime().getTime());
            } catch (Exception e) {
                return 0;
            }

        } else if (dateStr.length() == 14) {
            try {
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(dateStr.substring(0, 4)),
                        Integer.parseInt(dateStr.substring(4, 6)) - 1,
                        Integer.parseInt(dateStr.substring(6, 8)),
                        Integer.parseInt(dateStr.substring(8, 10)),
                        Integer.parseInt(dateStr.substring(10, 12)),
                        Integer.parseInt(dateStr.substring(12, 14)));
                cal.set(Calendar.MILLISECOND, 0);
                return (cal.getTime().getTime());
            } catch (Exception e) {
                return 0;
            }
        } else if (dateStr.length() == 10) {
            try {
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(dateStr.substring(0, 4)),
                        Integer.parseInt(dateStr.substring(5, 7)) - 1,
                        Integer.parseInt(dateStr.substring(8, 10)), 0, 0, 0);
                cal.set(Calendar.MILLISECOND, 0);
                return (cal.getTime().getTime());
            } catch (Exception e) {
                return 0;
            }
        } else {
            try {
                return Long.parseLong(dateStr);
            } catch (Exception e) {
                return 0;
            }

        }

    }
}
