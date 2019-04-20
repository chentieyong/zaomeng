package com.kingpivot.common.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Ma-Company on 2016/4/20.
 */
public class TimeTest {

    //用来全局控制 上一周，本周，下一周的周数变化
    private static int weeks = 0;
    private int MaxDate;//一月最大天数
    private int MaxYear;//一年最大天数

    /**
     * 得到二个日期间的间隔天数
     */
    public static long getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat(DateFormatUtil.DATE_FORMAT);
        long day = 0;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return day;
        }
        return day;
    }

    /**
     * 得到二个日期间的间隔分钟
     */
    public static long getTwoMinutes(Timestamp time1, Timestamp time2) {
        long minutes = 0;
        try {
            minutes = (time1.getTime() - time2.getTime()) / (60 * 1000);
        } catch (Exception e) {
            return minutes;
        }
        return minutes;
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = TimeTest.strToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    /**
     * 将短时间格式字符串转换为时间 HH:mm
     */
    public static Date strToDateHHmm(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateFormatUtil.DATE_FORMAT_HHMM);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 把当前时间转换为时间 HH:mm
     */
    public static Date getDateHHmm() {
        SimpleDateFormat formatter = new SimpleDateFormat(DateFormatUtil.DATE_FORMAT_HHMM);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(toDateTimeFormat(getTime()), pos);
        return strtodate;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     */
    public static Timestamp strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateFormatUtil.DATE_FORMAT);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return new Timestamp(strtodate.getTime());
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     */
    public static Timestamp strToTime(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateFormatUtil.DATE_TIME_FORMAT);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return new Timestamp(strtodate.getTime());
    }

    public static Timestamp timeToDate(Date dateTime) {
        String timeStr = TimeTest.toDateFormat(dateTime);
        Date timestamp = TimeTest.strToDateMM(timeStr + " 00:00:00.0");
        long time = timestamp.getTime();
        return new Timestamp(time);
    }

    public static Timestamp timeToEndDate(Date dateTime) {
        String timeStr = TimeTest.toDateFormat(dateTime);
        Date timestamp = TimeTest.strToDateMM(timeStr + " 23:59:59.0");
        long time = timestamp.getTime();
        return new Timestamp(time);
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd HH:mm
     */
    public static Date strToDateMM(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateFormatUtil.DATE_FORMAT_MM);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd HH:mm 然后在转换成string类型
     */
    public static String strToStrMM(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateFormatUtil.DATE_FORMAT_MM);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return toDateFormatMM(strtodate);
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd HH:mm 然后在转换成string类型
     */
    public static String strToStr(String strDate, String pa) {
        SimpleDateFormat formatter = new SimpleDateFormat(pa);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return DateFormatUtil.formatDate(strtodate, pa);
    }

    /**
     * 两个时间之间的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays(String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat(DateFormatUtil.DATE_FORMAT);
        Date date = null;
        Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }

    // 计算当月最后一天,返回字符串
    public String getDefaultDay() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);//加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);//减去一天，变为当月最后一天
        return toDateFormat(lastDate.getTime());
    }

    // 上月第一天
    public String getPreviousMonthFirst() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1号
        lastDate.add(Calendar.MONTH, -1);//减一个月，变为下月的1号
        return toDateFormat(lastDate.getTime());
    }

    //获取当月第一天
    public String getFirstDayOfMonth() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);//设为当前月的1号
        return toDateFormat(lastDate.getTime());
    }

    // 获得本周星期日的日期
    public static String getCurrentWeekday() {
        weeks = 0;
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        return toDateFormat(currentDate.getTime());
    }

    public static Timestamp strToTimes(String strDate) {
        return new Timestamp(DateUtils.parseStringToLong(strDate));
    }

    /**
     * 获取当天开始时间
     *
     * @return
     */
    public static String getNowStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        return toDateTimeFormat(todayStart.getTime());
    }


    /**
     * 获取当天结束时间
     */
    public static String getNowEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        return toDateTimeFormat(todayEnd.getTime());
    }

    //获取当天时间
    public static String getNowTime(String dateformat) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);//可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        return hehe;
    }

    // 获得当前日期与本周日相差的天数
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;         //因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    public static int getToWeek() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek == 0) {
            dayOfWeek = 7;
        }
        return dayOfWeek;
    }

    //获得本周一的日期
    public String getMondayOFWeek() {
        weeks = 0;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        return toDateFormat(currentDate.getTime());
    }

    //获得相应周的周六的日期
    public static String getSaturday() {
        SimpleDateFormat myFormatter = new SimpleDateFormat(DateFormatUtil.DATE_FORMAT);
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 6;
        c.add(Calendar.DATE, -day_of_week + 6);
        return myFormatter.format(c.getTime());
    }

    // 获得上周星期日的日期
    public String getPreviousWeekSunday() {
        weeks = 0;
        weeks--;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        return toDateFormat(currentDate.getTime());
    }

    // 获得上周星期一的日期
    public String getPreviousWeekday() {
        weeks--;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        return toDateFormat(currentDate.getTime());
    }

    // 获得下周星期一的日期
    public String getNextMonday() {
        weeks++;
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        return toDateFormat(currentDate.getTime());
    }

    // 获得下周星期日的日期
    public String getNextSunday() {
        int mondayPlus = this.getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
        return toDateFormat(currentDate.getTime());
    }

    private int getMonthPlus() {
        Calendar cd = Calendar.getInstance();
        int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
        cd.set(Calendar.DATE, 1);//把日期设置为当月第一天
        cd.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        MaxDate = cd.get(Calendar.DATE);
        if (monthOfNumber == 1) {
            return -MaxDate;
        } else {
            return 1 - monthOfNumber;
        }
    }

    //获得上月最后一天的日期
    public String getPreviousMonthEnd() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, -1);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        return toDateFormat(lastDate.getTime());
    }

    //获得下个月第一天的日期
    public String getNextMonthFirst() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);//减一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        return toDateFormat(lastDate.getTime());
    }

    //获得下个月最后一天的日期
    public String getNextMonthEnd() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.MONTH, 1);//加一个月
        lastDate.set(Calendar.DATE, 1);//把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);//日期回滚一天，也就是本月最后一天
        return toDateFormat(lastDate.getTime());
    }

    //获得明年最后一天的日期
    public String getNextYearEnd() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);//加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        return toDateFormat(lastDate.getTime());
    }

    public static String getAddYearEnd(String nowTime, int year) {
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTime(strToDate(nowTime));//设置起时间
        lastDate.add(Calendar.YEAR, year);//加一个年
        return toDateFormat(lastDate.getTime());
    }

    //获得明年第一天的日期
    public String getNextYearFirst() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.add(Calendar.YEAR, 1);//加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        return toDateFormat(lastDate.getTime());
    }

    //获得本年有多少天
    private int getMaxYear() {
        Calendar cd = Calendar.getInstance();
        cd.set(Calendar.DAY_OF_YEAR, 1);//把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);//把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    private int getYearPlus() {
        Calendar cd = Calendar.getInstance();
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);//获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);//把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);//把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }

    //获得本年第一天的日期
    public String getCurrentYearFirst() {
        int yearPlus = this.getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        return toDateFormat(currentDate.getTime());
    }

    //获得本年最后一天的日期 *
    public String getCurrentYearEnd() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格式
        String years = dateFormat.format(date);
        return years + "-12-31";
    }

    //获得上年第一天的日期 *
    public String getPreviousYearFirst() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return years_value + "-01-01";
    }

    //获得上年最后一天的日期
    public String getPreviousYearEnd() {
        weeks--;
        int yearPlus = this.getYearPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks + (MaxYear - 1));
        Date yearDay = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String preYearDay = df.format(yearDay);
        getThisSeasonTime(11);
        return preYearDay;
    }

    //获得本季度
    public String getThisSeasonTime(int month) {
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");//可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        int start_days = 1;//years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days + ";" + years_value + "-" + end_month + "-" + end_days;
        return seasonDate;
    }

    /**
     * 获取某年某月的最后一天
     *
     * @param year  年
     * @param month 月
     * @return 最后一天
     */
    private int getLastDayOfMonth(int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }

    /**
     * 是否闰年
     *
     * @param year 年
     * @return
     */
    public boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 计算时间加天数
     *
     * @param data 时间
     * @param day  天数
     * @return 计算后的时间
     */
    public static Timestamp timeAddDay(Timestamp data, Integer day) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(data);
        calendar.add(calendar.DATE, day);
        return new Timestamp(calendar.getTimeInMillis());
    }

    //计算时间加年数
    public static Timestamp timeAddYear(Timestamp data, Integer year) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(data);
        calendar.add(calendar.YEAR, year);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取当月最大天数
     *
     * @return
     */
    public Integer getMonthMaxDay() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        Integer last = ca.getTime().getDate();
        return last;
    }

    /**
     * 转换格式 --> 年月
     */
    public String toDateYearMonthFormat(Date date) {
        return DateFormatUtil.formatDate(date, DateFormatUtil.DATE_YEAR_MONTH_FORMAT);
    }

    /**
     * 转换格式 --> 年月日
     */
    public static String toDateFormat(Date date) {
        return DateFormatUtil.formatDate(date, DateFormatUtil.DATE_FORMAT);
    }

    /**
     * 获取当天时间-年月日
     */
    public static String getNowDateFormat() {
        return DateFormatUtil.formatDate(new Date(), DateFormatUtil.DATE_FORMAT);
    }

    /**
     * 转换格式 --> 年月日时分
     */
    public static String toDateFormatMM(Date date) {
        return DateFormatUtil.formatDate(date, DateFormatUtil.DATE_FORMAT_MM);
    }

    /**
     * 转换格式 --> 年月日时分秒
     */
    public static String toDateTimeFormat(Date date) {
        return DateFormatUtil.formatDate(date, DateFormatUtil.DATE_TIME_FORMAT);
    }

    /**
     * 转换格式 --> 年月日时分秒
     */
    public static String toDateTimeFormat(Timestamp date) {
        return DateFormatUtil.formatDate(date, DateFormatUtil.DATE_TIME_FORMAT);
    }

    public static String toString(Timestamp date) {
        return DateFormatUtil.formatDate(date, DateFormatUtil.DATE_TIME_FORMAT_14);
    }

    /**
     * 获取当前时间
     */
    public static Timestamp getTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static String getTimeStr() {
        Timestamp time = getTime();
        return toDateTimeFormat(time);
    }

    /**
     * 通过类型来动态生成参数
     */
    public Map<String, Object> getCodeToParam(String code) {
        Map<String, Object> param = new HashMap<>();
        Integer numMax = 0;//最大数
        param.put("code", code);
        TimeTest time = new TimeTest();
        switch (code) {
            case "lastMonth":
                //上个月
                numMax = strToDate(getPreviousMonthEnd()).getDate();//获取上个月最大数
                param.put("code1", "%d");//日
                param.put("code2", "%Y-%m-%d");//年 月 日
                param.put("code3", "%Y-%m-%d");//年 月 日
                param.put("code4", "%d");//年 月 日
                param.put("startDate", time.getPreviousMonthFirst());//上个月第一天
                param.put("endDate", time.getPreviousMonthEnd());//上个月最后一天
                break;
            case "toMonth":
                //本月
                numMax = strToDate(getPreviousMonthEnd()).getDate();//获取上个月最大数
                param.put("code1", "%d");//日
                param.put("code2", "%Y-%m-%d");//年 月 日
                param.put("code3", "%Y-%m-%d");//年 月 日
                param.put("code4", "%d");//年 月 日
                param.put("startDate", time.getFirstDayOfMonth());//本月第一天
                param.put("endDate", time.getDefaultDay());//本月最后一天
                break;
            case "lastWeek":
                //上周
                numMax = 7;//每个星期最大天数
                param.put("code1", "%w");//天
                param.put("code2", "%Y-%m-%d");//年 月 日
                param.put("code3", "%Y-%m-%d");//年 月 日
                param.put("code4", "%w");//年 月 日
                param.put("startDate", time.getPreviousWeekday());//上周第一天
                param.put("endDate", time.getPreviousWeekSunday());//上周最后一天
                break;
            case "toWeek":
                //本周
                numMax = 7;//每个星期最大天数
                param.put("code1", "%w");//天
                param.put("code2", "%Y-%m-%d");//年 月 日
                param.put("code3", "%Y-%m-%d");//年 月 日
                param.put("code4", "%w");//年 月 日
                param.put("startDate", time.getMondayOFWeek());//本周第一天
                param.put("endDate", time.getCurrentWeekday());//本周最后一天
                break;
            case "toDay":
                //当天
                numMax = 24;//每天最大小时数
                param.put("code1", "%H");//天
                param.put("code2", "%Y-%m-%d %H:%i:%S");//年 月 日 时 分 秒
                param.put("code3", "%Y-%m-%d %H:%i:%S");//年 月 日 时 分 秒
                param.put("code4", "%H");//年 月 日
                param.put("startDate", time.getNowStartTime());//当天开始时间
                param.put("endDate", time.getNowEndTime());//当天结束时间
                break;
        }
        param.put("numMax", numMax);
        return param;
    }

    //转换成中文的年月日
    public static String getString(String time) {
        return formatStr(time);
    }

    //将源字符串中的阿拉伯数字格式化为汉字
    public static char formatDigit(char sign){
        if(sign == '0')
            sign = '〇';
        if(sign == '1')
            sign = '一';
        if(sign == '2')
            sign = '二';
        if(sign == '3')
            sign = '三';
        if(sign == '4')
            sign = '四';
        if(sign == '5')
            sign = '五';
        if(sign == '6')
            sign = '六';
        if(sign == '7')
            sign = '七';
        if(sign == '8')
            sign = '八';
        if(sign == '9')
            sign = '九';
        return sign;
    }

    //获得月份字符串的长度
    public static int getMidLen(String str,int pos1,int pos2){
        return str.substring(pos1+1, pos2).length();
    }

    //获得日期字符串的长度
    public static int getLastLen(String str,int pos2){
        return str.substring(pos2+1).length();
    }

    //格式化日期
    public static String formatStr(String str){
        StringBuffer sb = new StringBuffer();
        int pos1 = str.indexOf("-");
        int pos2 = str.lastIndexOf("-");
        for(int i = 0; i < 4; i++){
            sb.append(formatDigit(str.charAt(i)));
        }
        sb.append('年');
        if(getMidLen(str,pos1,pos2) == 1){
            sb.append(formatDigit(str.charAt(5))+"月");
            if(str.charAt(7) != '0'){
                if(getLastLen(str, pos2) == 1){
                    sb.append(formatDigit(str.charAt(7))+"日");
                }
                if(getLastLen(str, pos2) == 2){
                    if(str.charAt(7) != '1' && str.charAt(8) != '0'){
                        sb.append(formatDigit(str.charAt(7))+"十"+formatDigit(str.charAt(8))+"日");
                    }
                    else if(str.charAt(7) != '1' && str.charAt(8) == '0'){
                        sb.append(formatDigit(str.charAt(7))+"十日");
                    }
                    else if(str.charAt(7) == '1' && str.charAt(8) != '0'){
                        sb.append("十"+formatDigit(str.charAt(8))+"日");
                    }
                    else{
                        sb.append("十日");
                    }
                }
            }
            else{
                sb.append(formatDigit(str.charAt(8))+"日");
            }
        }
        if(getMidLen(str,pos1,pos2) == 2){
            if(str.charAt(5) != '0' && str.charAt(6) != '0'){
                sb.append("十"+formatDigit(str.charAt(6))+"月");
                if(getLastLen(str, pos2) == 1){
                    sb.append(formatDigit(str.charAt(8))+"日");
                }
                if(getLastLen(str, pos2) == 2){
                    if(str.charAt(8) != '0'){
                        if(str.charAt(8) != '1' && str.charAt(9) != '0'){
                            sb.append(formatDigit(str.charAt(8))+"十"+formatDigit(str.charAt(9))+"日");
                        }
                        else if(str.charAt(8) != '1' && str.charAt(9) == '0'){
                            sb.append(formatDigit(str.charAt(8))+"十日");
                        }
                        else if(str.charAt(8) == '1' && str.charAt(9) != '0'){
                            sb.append("十"+formatDigit(str.charAt(9))+"日");
                        }
                        else{
                            sb.append("十日");
                        }
                    }
                    else{
                        sb.append(formatDigit(str.charAt(9))+"日");
                    }
                }
            }
            else if(str.charAt(5) != '0' && str.charAt(6) == '0'){
                sb.append("十月");
                if(getLastLen(str, pos2) == 1){
                    sb.append(formatDigit(str.charAt(8))+"日");
                }
                if(getLastLen(str, pos2) == 2){
                    if(str.charAt(8) != '0'){
                        if(str.charAt(8) != '1' && str.charAt(9) != '0'){
                            sb.append(formatDigit(str.charAt(8))+"十"+formatDigit(str.charAt(9))+"日");
                        }
                        else if(str.charAt(8) != '1' && str.charAt(9) == '0'){
                            sb.append(formatDigit(str.charAt(8))+"十日");
                        }
                        else if(str.charAt(8) == '1' && str.charAt(9) != '0'){
                            sb.append("十"+formatDigit(str.charAt(9))+"日");
                        }
                        else{
                            sb.append("十日");
                        }
                    }
                    else{
                        sb.append(formatDigit(str.charAt(9))+"日");
                    }
                }
            }
            else{
                sb.append(formatDigit(str.charAt(6))+"月");
                if(getLastLen(str, pos2) == 1){
                    sb.append(formatDigit(str.charAt(8))+"日");
                }
                if(getLastLen(str, pos2) == 2){
                    if(str.charAt(8) != '0'){
                        if(str.charAt(8) != '1' && str.charAt(9) != '0'){
                            sb.append(formatDigit(str.charAt(8))+"十"+formatDigit(str.charAt(9))+"日");
                        }
                        else if(str.charAt(8) != '1' && str.charAt(9) == '0'){
                            sb.append(formatDigit(str.charAt(8))+"十日");
                        }
                        else if(str.charAt(8) == '1' && str.charAt(9) != '0'){
                            sb.append("十"+formatDigit(str.charAt(9))+"日");
                        }
                        else{
                            sb.append("十日");
                        }
                    }
                    else{
                        sb.append(formatDigit(str.charAt(9))+"日");
                    }
                }
            }
        }
        return sb.toString();
    }

    //判断日期格式是否正确 年 月 日
    public static boolean dateIsPass(String date) {
        Map<String,Object> rsMap = new HashMap<String,Object>();
        //年月日的正则表达式，此次没有理会2月份闰年
        String regex  = "^((19|20)[0-9]{2})-((0?2-((0?[1-9])|([1-2][0-9])))|(0?(1|3|5|7|8|10|12)-((0?[1-9])|([1-2][0-9])|(3[0-1])))|(0?(4|6|9|11)-((0?[1-9])|([1-2][0-9])|30)))$";
        //开始判断,且符合正则表达式
        if(date.matches(regex)) {
            return true;
        }
        return false;
    }
    public static int getAgeByBirth(Date birthday){
        //Calendar：日历
        /*从Calendar对象中或得一个Date对象*/
        Calendar cal = Calendar.getInstance();
        /*把出生日期放入Calendar类型的bir对象中，进行Calendar和Date类型进行转换*/
        Calendar bir = Calendar.getInstance();
        bir.setTime(birthday);
        /*如果生日大于当前日期，则抛出异常：出生日期不能大于当前日期*/
        if(cal.before(birthday)){
            throw new IllegalArgumentException("The birthday is before Now,It's unbelievable");
        }

        /*取出当前年月日*/
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayNow = cal.get(Calendar.DAY_OF_MONTH);
        /*取出出生年月日*/
        int yearBirth = bir.get(Calendar.YEAR);
        int monthBirth = bir.get(Calendar.MONTH);
        int dayBirth = bir.get(Calendar.DAY_OF_MONTH);
        /*大概年龄是当前年减去出生年*/
        int age = yearNow - yearBirth;
        /*如果出当前月小与出生月，或者当前月等于出生月但是当前日小于出生日，那么年龄age就减一岁*/
        if(monthNow < monthBirth || (monthNow == monthBirth && dayNow < dayBirth)){
            age--;
        }
        return age;
    }

}
