package com.kingpivot.common.utils;

import com.google.common.primitives.Ints;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 类描述：时间格式化工具类
 * @author cyw
 * @version
 */
public class DateFormatUtil {
	
	// 系统默认日起时间格式
    public static final String DATE_YEAR_MONTH_FORMAT="yyyy-MM";
	public static final String DATE_FORMAT = "yyyy-MM-dd"; 
	public static final String DATE_MORTH_DAY = "MMdd";
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String CTS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
	public static final String DATE_TIME_FORMAT_14 = "yyyyMMddHHmmss";
	public static final String UTC_DATE_FORMAT = "EEE MMM dd HH:mm:ss ZZZ yyyy";
    public static final String DATE_YEAR_FORMAT = "yyyy";
    public static final String DATE_FORMAT_MM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_HHMM = "HH:mm";
    public static final String DATE_FORMAT_HH = "yyyy-MM-dd HH";
	public final static String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	public final static String DATETIME_FORMAT_HHMMSS = "HHmmss";
	public static final String YY="yyyy";
	public static final String MM="MM";
	public static final String DATE ="dd";
	public static final String HH="HH";
	public static final String HMS="HH:mm:ss";

	/**
	 * 获得当前时间字符串
	 * @param formatStr 日期格式
	 * @return string yyyy-MM-dd
	 */
	public static String getNowDateStr(String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(getNowDate());
	}

	/**
	 * 获得系统当前时间
	 * @return Date
	 */
	public static Date getNowDatePlus(long s){
		long curren = System.currentTimeMillis();
		curren += s;
		return new Date(curren);
	}

	/**
	 * 获得系统当前时间+x秒后的时间
	 * @return Date
	 */
	public static Date getNowDate(){
		Calendar c = Calendar.getInstance();
		return c.getTime();
	}

    /**
	 * 获得添加后时间 
	 * @param date
	 * @param hours
	 * @return
	 */
	public static String getAddHoursDate(Date date,int hours,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		long Time=(date.getTime()/1000)+60*60*hours;		
		date.setTime(Time*1000);
		String strDate=sdf.format(date);
		return strDate;
	}
	
	/**
	 * 根据相加的天数计算日期
	 * @throws ParseException
	 *
	 */
	public static String getDateAfterAddDay(String date, int days) throws ParseException {
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(sdf.parse(date));
	    calendar.add(Calendar.DATE, days);

	    return sdf.format(calendar.getTime());
	}


	/**
	 * 根据相加的天数计算日期
	 * @throws ParseException
	 *
	 */
	public static Date getDateAfterDay(String date, int days) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(date));
		calendar.add(Calendar.DATE,days);
		return calendar.getTime();
	}
	/**
     * 根据相加的月数计算日期
     * @throws ParseException
     *
     */
    public static String getDateAfterAddMonth(String date, int month) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(date));
        calendar.add(Calendar.MONTH,month);

        return sdf.format(calendar.getTime());
    }

    /**
     * 根据相加的月数计算日期
     * @throws ParseException
     *
     */
    public static String getDateAfterAddMonthlong(String date, int month) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(date));
        calendar.add(Calendar.MONTH,month);

        return sdf.format(calendar.getTime());
    }

	/**
	 * @param repeatType 间隔类型
	 * @param repeatLength 间隔距离
	 * @param startTime 开始日期
	 * @param endTime 结束日期
	 * @param lastTime 最后执行日期
	 * @return
	 */
    public static boolean isPerform(int repeatType,int repeatLength,String startTime,String endTime,String lastTime){
    	//首先判断开始日期和结束日期是否满足当前时间
		long nowTimes = new Timestamp(System.currentTimeMillis()).getTime();
		long startTimes = TimeTest.strToTime(startTime).getTime();
		long endTimes = TimeTest.strToDate(endTime).getTime();
		if(nowTimes<startTimes||nowTimes>endTimes){
			return false;
		}
		long lastTimes = TimeTest.strToTime(lastTime).getTime();
		long repeatTimes = (nowTimes - lastTimes)/1000/60;//分
		long repeatLengths = 0;
		if(repeatType == 1){//每分钟
			repeatLengths = repeatTimes;
		}else if(repeatType == 2){//每小时
			repeatLengths = repeatTimes/60;
		}else if(repeatType == 3){//每天
			repeatLengths = repeatTimes/60/24;
		}else if(repeatType == 4){//每周
			repeatLengths = repeatTimes/60/24/7;
		}else if(repeatType == 5){//每月
			int day = dayOfMonth();
			repeatLengths = repeatTimes/60/24/day;
		}
		if(repeatLengths>=repeatLength){
			return true;
		}
		return false;
	}

	public static int dayOfMonth(){
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = (now.get(Calendar.MONTH) + 1);
		Calendar c = Calendar.getInstance();
		c.set(year, month, 0); //输入类型为int类型
		int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
		return dayOfMonth;
	}

    /**
	 * 比较时间大小
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compare_date(String date1, String date2,String pattern) {

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		try {
			Date dt1 = sdf.parse(date1);
			Date dt2 = sdf.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				//System.out.println("dt1 在dt2前(d1<d2)");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				//System.out.println("dt1在dt2后(d1>d2)");
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 比较时间大小
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compare_date(String date1, String date2) {

		    SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);

	        try {
	            Date dt1 = sdf.parse(date1);
	            Date dt2 = sdf.parse(date2);
	            if (dt1.getTime() > dt2.getTime()) {
	                //System.out.println("dt1 在dt2前(d1<d2)");
	                return 1;
	            } else if (dt1.getTime() < dt2.getTime()) {
	                //System.out.println("dt1在dt2后(d1>d2)");
	                return -1;
	            } else {
	                return 0;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return 0;
	    }

    /**
     * 比较compareDate日期是否在beginDate与endDate中
     * @param beginDate
     * @param endDate
     * @param compareDate
     * @return
     */
    public static boolean compareDate(String beginDate, String endDate,String compareDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
            Date be = sdf.parse(beginDate);
            Date en = sdf.parse(endDate);
            Date co = sdf.parse(compareDate);
           if(co.compareTo(be) >= 0 && co.compareTo(en) <= 0){
               return true;
           }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

	/**
	 * 比较compareDate日期是否在beginDate与endDate中 HH:mm
	 * @param beginDate
	 * @param endDate
	 * @param compareDate
	 * @return
	 */
	public static boolean compareDate_1(String beginDate, String endDate,String compareDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_HHMM);
		try {
			Date be = sdf.parse(beginDate);
			Date en = sdf.parse(endDate);
			Date co = sdf.parse(compareDate);
			if(co.compareTo(be) >= 0 && co.compareTo(en) <= 0){
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}


	public static String[] getFirstAndEndByDate(String date){
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
			int d = 0;
			if(cal.get(Calendar.DAY_OF_WEEK)==1){
				d = -6;
			}else{
				d = 2-cal.get(Calendar.DAY_OF_WEEK);
			}
			cal.add(Calendar.DAY_OF_WEEK, d);
			//所在周开始日期
			String start = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			cal.add(Calendar.DAY_OF_WEEK, 6);
			String end = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
			//所在周结束日期
			return new String[]{start,end};
		}catch (Exception e){
			return null;
		}

	}

	/**
	 * 获得系统当前日期时间，以默认格式显示
	 * @return e.g.2006-10-12 10:55:06
	 */
	public static String getCurrentFormatDateTime() {
		Date currentDate = getCurrentDate();
		SimpleDateFormat dateFormator = new SimpleDateFormat(
				DateFormatUtil.DATE_TIME_FORMAT);

		return dateFormator.format(currentDate);
	}

	/**
	 * 获得系统当前日期时间，以默认格式显示
	 * @return e.g.pattern
	 */
	public static String getCurrentFormatDateTime(String pattern) {
		Date currentDate = getCurrentDate();
		SimpleDateFormat dateFormator = new SimpleDateFormat(pattern);

		return dateFormator.format(currentDate);
	}

    /**
     * 获得系统当前日期时间，以默认格式显示
     * @return e.g.2006
     */
    public static String getCurrentFormatYear() {
        Date currentDate = getCurrentDate();
        SimpleDateFormat dateFormator = new SimpleDateFormat(
                DateFormatUtil.DATE_YEAR_FORMAT);

        return dateFormator.format(currentDate);
    }

	/**
	 * 获得系统当前日期month，以默认格式显示
	 * @return e.g.2006
	 */
	public static String getCurrentFormatMonth() {
		Date currentDate = getCurrentDate();
		SimpleDateFormat dateFormator = new SimpleDateFormat(
				DateFormatUtil.DATE_YEAR_MONTH_FORMAT);

		return dateFormator.format(currentDate);
	}

	public static int getCurrentFormatWeek() {
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(new Date());
		return g.get(Calendar.WEEK_OF_YEAR);//获得周数
	}

	public static String[] getCurrentWeekStartEnd(){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		Calendar can = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		c.setTime(can.getTime());
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		String start = sdf.format(c.getTime());
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		String end = sdf.format(c.getTime());
		return new String[]{start,end};
	}
	/**
	 * 获得系统当前日期时间，以默认格式显示
	 * @return e.g.2006-10-12
	 */
	public static String getCurrFormatDateTime() {
		Date currentDate = getCurrentDate();
		SimpleDateFormat dateFormator = new SimpleDateFormat(
				DateFormatUtil.DATE_FORMAT);

		return dateFormator.format(currentDate);
	}

	/**
	 * 获得系统当前日期时间，以默认格式显示
	 * @return DATE_TIME_FORMAT_17
	 */
	public static String getCurrFormatStampTime() {
		Date currentDate = getCurrentDate();
		SimpleDateFormat dateFormator = new SimpleDateFormat(
				DateFormatUtil.DATE_TIME_FORMAT_14);

		return dateFormator.format(currentDate);
	}


	public static String getCurrFormatStampTime10() {
		return String.valueOf(System.currentTimeMillis()).toString().substring(0,10);
	}


	/**
	 * 获得系统的当前时间
	 * @return e.g.Thu Oct 12 10:25:14 CST 2006
	 */
	public static Date getCurrentDate() {
		return new Date(getCurrentTimeMillis());
	}

	/**
	 * 获取昨天日期按指定格式返回(yyyy-MM-dd 00:00:00)
	 * @return
	 */
	public  static Timestamp getYesterDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getCurrentDate());
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);

		String strDate = DateFormatUtil.formatDate(calendar.getTime(),DATE_TIME_FORMAT);

		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);

		return new Timestamp(strtodate.getTime());
	}

	/**
	 * 获取今天日期按指定格式返回(yyyy-MM-dd 00:00:00)
	 * @return
     */
	public  static Timestamp getToday(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getCurrentDate());
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE));

		String strDate = DateFormatUtil.formatDate(calendar.getTime(),DATE_TIME_FORMAT);

		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);

		return new Timestamp(strtodate.getTime());
	}

	/**
	 * 获取前/后day天的日期，按指定格式返回(yyyy-MM-dd 00:00:00)
	 * @return
	 */
	public  static Timestamp getTodayByNumber(int day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getCurrentDate());
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);

		String strDate = DateFormatUtil.formatDate(calendar.getTime(),DATE_TIME_FORMAT);

		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);

		return new Timestamp(strtodate.getTime());
	}

	/**
	 * 获取明天日期按指定格式返回(yyyy-MM-dd 00:00:00)
	 * @return
	 */
	public  static Timestamp getTomorrow(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getCurrentDate());
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);

		String strDate = DateFormatUtil.formatDate(calendar.getTime(),DATE_TIME_FORMAT);

		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);

		return new Timestamp(strtodate.getTime());
	}

	/**
	 * 获取指定日期的明天日期
	 * @param date
	 * @return
     */
	public static Date getTomorrow(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 获取指定日期的昨天日期
	 * @param date
	 * @return
     */
	public static Date getYesterDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}
	/**
	 * 获得系统的当前时间，毫秒.
	 * @return
	 */
	public static long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * 根据指定日期(yyyy-MM-dd)返回指定日期的昨天日期(yyyy-MM-dd 00:00:00)
	 * @param date
	 * @return
     */
	public  static Timestamp getYesterDay(String date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(TimeTest.strToDate(date));
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) -1);

		String strDate = DateFormatUtil.formatDate(calendar.getTime(),DATE_TIME_FORMAT);

		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);

		return new Timestamp(strtodate.getTime());
	}

	/**
	 * 根据指定日期(yyyy-MM-dd)返回指定日期的今天日期(yyyy-MM-dd 00:00:00)
	 * @param date
	 * @return
	 */
	public  static Timestamp getToday(String date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(TimeTest.strToDate(date));
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE));

		String strDate = DateFormatUtil.formatDate(calendar.getTime(),DATE_TIME_FORMAT);

		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);

		return new Timestamp(strtodate.getTime());
	}

	/**
	 * 根据指定日期(yyyy-MM-dd)返回指定日期的今天日期(yyyy-MM-dd  23:59:59)
	 * @param date
	 * @return
     */
	public  static Timestamp getTodayLast(String date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(TimeTest.strToDate(date));
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE));

		String strDate = DateFormatUtil.formatDate(calendar.getTime(),DATE_FORMAT);

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(strDate);
		stringBuffer.append(" 23:59:59");

		return  TimeTest.strToTime(stringBuffer.toString());
	}

	/**
	 * 根据指定日期(yyyy-MM-dd)返回指定日期的今天日期(yyyy-MM-dd 00:00:00)
	 * @param date
	 * @return
     */
	public  static String getTodayString(String date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(TimeTest.strToDate(date));
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
		return DateFormatUtil.formatDate(calendar.getTime(),DATE_TIME_FORMAT);
	}

	/**
	 * 根据指定日期(yyyy-MM-dd)返回指定日期的今天日期(yyyy-MM-dd 23:59:59)
	 * @param date
	 * @return
	 */
	public  static String getTodayLastString(String date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(TimeTest.strToDate(date));
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE));

		String strDate = DateFormatUtil.formatDate(calendar.getTime(),DATE_FORMAT);
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(strDate);
		stringBuffer.append(" 23:59:59");
		return stringBuffer.toString();
	}

	/**
	 * 根据指定日期(yyyy-MM-dd)返回指定日期的明天日期(yyyy-MM-dd 00:00:00)
	 * @param date
	 * @return
	 */
	public  static Timestamp getTomorrow(String date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(TimeTest.strToDate(date));
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) +1);

		String strDate = DateFormatUtil.formatDate(calendar.getTime(),DATE_TIME_FORMAT);

		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);

		return new Timestamp(strtodate.getTime());
	}

	/**
	 * 获取指定时间的上个月最后一天的前/后day天
	 * @param date 指定日期 yyyy-MM-dd
	 * @param day 可为负数(前day天)
     * @return
     */
	public static String getLateMonthLateDay(String date,Integer day){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		if (day==null){ day = 0; }
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateFormatUtil.getToday(date));
		calendar.set(Calendar.DAY_OF_MONTH, day);

		return simpleDateFormat.format(calendar.getTime());
	}

	/**
	 * 获取指定时间前/后month个月最后一天的前/后day天
	 * @param date yyyy-MM-dd
	 * @param month
	 * @param day
     * @return
     */
	public static String getDateByMonth(String date,Integer month,Integer day){
		if (date==null) return null;
		if (month==null){ month = 0; }
		if (day==null){ day = 0; }
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateFormatUtil.getToday(date)); //设置时间
		calendar.add(Calendar.MONTH, month); //设置这个时间的哪一月
		calendar.set(Calendar.DAY_OF_MONTH, day); //设置这个月的哪一天

		return simpleDateFormat.format(calendar.getTime());
	}
	/**
	 * 输入日期，按照指定格式返回
	 *
	 * @param date
	 * @param pattern
	 *            e.g.DATE_FORMAT_8 = "yyyyMMdd"; DATE_TIME_FORMAT_14 =
	 *            "yyyyMMddHHmmss"; 或者类似于二者的格式,e.g."yyyyMMddHH"，"yyyyMM"
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		if (checkPara(pattern) || checkPara(date)) {
			return "";
		}
		SimpleDateFormat dateFormator = new SimpleDateFormat(pattern);
		return dateFormator.format(date);
	}

	public static String formatDateStr(String date, String pattern){
		if (checkPara(pattern) || checkPara(date)) {
			return "";
		}
		SimpleDateFormat dateFormator = new SimpleDateFormat(pattern);
		try {
			return dateFormator.format(str2Date(date,pattern));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将时间字符串按照默认格式DATE_TIME_FORMAT ="yyyy-MM-dd HH:mm:ss",转换为Date
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date parseStrToDateTime(String dateStr) {
		if (checkPara(dateStr)) {
			return null;
		}
		SimpleDateFormat dateFormator = new SimpleDateFormat(DATE_TIME_FORMAT);
		Date resDate = dateFormator.parse(dateStr, new ParsePosition(0));

		return resDate;
	}

	/**
	 * 将时间字符串按照默认格式DATE_TIME_FORMAT ="yyyy-MM-dd HH:mm",转换为Date
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date parseStrTMMTime(String dateStr) {
		if (checkPara(dateStr)) {
			return null;
		}
		SimpleDateFormat dateFormator = new SimpleDateFormat(DATE_FORMAT_MM);
		Date resDate = dateFormator.parse(dateStr, new ParsePosition(0));

		return resDate;
	}


	/**
	 * 将时间字符串按照默认格式DATE_TIME_FORMAT ="yyyy-MM-dd",转换为Date
	 *
	 * @param dateStr
	 * @return
	 */
	public static Date parseStringToDate(String dateStr) {
		if (checkPara(dateStr)) {
			return null;
		}
		SimpleDateFormat dateFormator = new SimpleDateFormat(DATE_FORMAT);
		Date resDate = dateFormator.parse(dateStr, new ParsePosition(0));

		return resDate;
	}

	/**
	 * 解析utc时间字符串为DATE类型
	 * @param ctsStr	e.g. Mon Jan 27 00:00:00 UTC+0800 2014
	 * @return
	 * @throws ParseException
	 * @throws ParseException
	 */
	public static Date parseUTCStrToDate(String ctsStr) throws ParseException {

		if (checkPara(ctsStr)) {
			return null;
		}
		ctsStr = ctsStr.replace("UTC", "");
		ctsStr = ctsStr.replace("GMT", "");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(UTC_DATE_FORMAT, Locale.US);
        Date date = null;
		try {
			date = simpleDateFormat.parse(ctsStr);
		} catch (ParseException e) {
			simpleDateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss ZZZ", Locale.US);
			date = simpleDateFormat.parse(ctsStr);
		}

		return date;
	}

	/**
	 * 解析CTS时间字符串为DATE类型
	 * @param ctsStr	e.g. Wed Sep 07 14:57:28 CST 2011
	 * @return
	 * @throws ParseException
	 */
	public static Date parseCTSStrToDate(String ctsStr) throws ParseException {
		if (checkPara(ctsStr)) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CTS_DATE_FORMAT, Locale.US);
        Date date = simpleDateFormat.parse(ctsStr);

		return date;
	}

	/**
	 * 解析时间字符串为DATE类型
	 * @param str	格式yyyyMMddHHmmss
	 * 				e.g. 20111010084617
	 * @return
	 * @throws ParseException
	 */
	public static Date str2Date(String str,String format) throws ParseException {
		if (checkPara(str)) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date = simpleDateFormat.parse(str);
		
		return date;
	}
	
	
	public static Date str2Date(String str) throws ParseException {
		if (checkPara(str)) {
			return null;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_14);
		Date date = simpleDateFormat.parse(str);
		
		return date;
	}
	/**
	 * 判断参数是否等于null或者空
	 * 
	 * @param para
	 * @return
	 */
	private static boolean checkPara(Object para) {
		if (null == para || "".equals(para)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * 获取两个时间之间的时间差(时分秒时间) 
	 * @param oldDate 
	 * @param newDate
	 * @return
	 */
	public static String getTimeDifference(Date oldDate,Date newDate){
		long between = (newDate.getTime() - oldDate.getTime()) / 1000;// 除以1000是为了转换成秒

		long day1 = between / (24 * 3600);
		long hour1 = between % (24 * 3600) / 3600;
		long minute1 = between % 3600 / 60;
		long second1 = between % 60;
//		System.out.println("" + day1 + "天" + hour1 + "小时" + minute1 + "分"
//		+ second1 + "秒");
		return "" + day1 + "天" + hour1 + "小时" + minute1 + "分"
		+ second1 + "秒";
	}
	
	/**
	 * 按类型获取日期
	 * 1，本周第一天
	 * 2，上周第一天
	 * 3，上周最后一天
	 * 4，上月第一天
	 * 5，上月最后一天
	 * 6，本月第一天
	 * **/
	public static String getDate(int type){
		 Calendar c = Calendar.getInstance();
		 
		 switch (type) {
			case 1:
				c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				return formatDate(c.getTime(), "yyyy-MM-dd");
			case 2:
				c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				c.add(Calendar.WEEK_OF_MONTH, -1);
				return formatDate(c.getTime(), "yyyy-MM-dd");
			case 3:
				c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				c.add(Calendar.WEEK_OF_MONTH, -1);
				c.add(Calendar.DAY_OF_WEEK, 6);
				return formatDate(c.getTime(), "yyyy-MM-dd");
			case 4:
				c.set(Calendar.DAY_OF_MONTH, 1);
			    c.add(Calendar.DAY_OF_MONTH, -1);
			    c.set(Calendar.DAY_OF_MONTH, 1);
				return formatDate(c.getTime(), "yyyy-MM-dd");
			case 5:
				c.set(Calendar.DAY_OF_MONTH, 1);
			    c.add(Calendar.DAY_OF_MONTH, -1);
				return formatDate(c.getTime(), "yyyy-MM-dd");
			case 6:
				c.set(Calendar.DAY_OF_MONTH, 1);
				return formatDate(c.getTime(), "yyyy-MM-dd");
			default:
				break;
		}
		 
		 return formatDate(new Date(), "yyyy-MM-dd");
	}
	
	/**
	 * 获取与当前日期差值的日期
	 * type 与当前日期差值
	 * **/
	public static String getDateByDiff(int diff){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -diff);
		Date date = calendar.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}
	
	/**
     * 获取与当前日期差值的日期
     * type 与当前日期差值
     * **/
    public static String getDateByDiff2(int diff){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -diff + 1);
        Date date = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }


	/**
	 * 获取两个时间之间的分钟数/
	 * @param curdate 判断curdate》olddate
	 * @param olddate
	 * @return
	 */
	public static long getQuotMin(String curdate, String olddate) {
		if(compare_date(curdate,olddate) <= 0){
			return -1l;
		}
		long m_intervalday = 0;//初始化时间间隔的值为0
		SimpleDateFormat m_simpledateformat = new SimpleDateFormat(DATE_TIME_FORMAT);
		try {
			Date date1 = m_simpledateformat.parse(curdate);
			Date date2 = m_simpledateformat.parse(olddate);
			m_intervalday = date1.getTime() - date2.getTime();//计算所得为微秒数
			m_intervalday = m_intervalday / 1000 / 60;//计算所得的分钟数
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return m_intervalday;
	}
	

	/**
	 * 获取两个时间之间的天数
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	public static long getQuot(String startdate, String enddate) {
		
		long m_intervalday = 0;//初始化时间间隔的值为0		
		SimpleDateFormat m_simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
		try {				
			Date date1 = m_simpledateformat.parse(startdate);
			Date date2 = m_simpledateformat.parse(enddate);
			m_intervalday = date2.getTime() - date1.getTime();//计算所得为微秒数
			m_intervalday = m_intervalday / 1000 / 60 / 60 / 24;//计算所得的天数
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return m_intervalday;
	}
	
	/** 
     * 返回一个二维数组，单位分别是月和日，代表两个Date之差。 <br> 
     * 本方法忽略小时和分钟。 <br> 
     * <br> 
     * 例： <br> 
     * 1，2012年6月1日到2012年6月3日，返回值是[0,2] （2天） <br> 
     * 2，2012年6月15日到2012年8月4日，返回值是[1,20] （1个月零20天） <br> 
     * 3，2011年11月3日到2013年1月14日，返回值是[14,11] （14个月零11天） 
     *  
     * @param olderDate  较早的日期 
     * @param newerDate  较晚的日期 
     */   
	public static int[] getDateDifferenceInMonthAndDay(Date olderDate, Date newerDate) throws  IllegalArgumentException{  
        
		int[] differenceInMonthAndDay = new int[2];  
        int years = 0;  
        int months = 0;  
        int days = 0;  
  
        Calendar older = Calendar.getInstance();  
        Calendar newer = Calendar.getInstance();  
        older.setTime(olderDate);  
        newer.setTime(newerDate);  
          
        if(olderDate.getTime()>newerDate.getTime()){  
            throw new IllegalArgumentException();  
        }else{  
            years = newer.get(Calendar.YEAR)-older.get(Calendar.YEAR);  
            if(years>=0){  
                  
                months = newer.get(Calendar.MONTH)-older.get(Calendar.MONTH)+12*years;  
                older.add(Calendar.MONTH,months);  
                days = newer.get(Calendar.DATE)-older.get(Calendar.DATE);  
                  
                if(days<0){  
                    months = months - 1;  
                    older.add(Calendar.MONTH,-1);  
                }  
                  
                days = daysBetween(newer.getTime(),older.getTime());  
                differenceInMonthAndDay[0] = months;  
                differenceInMonthAndDay[1] = days;  
            }  
              
        }    
        return differenceInMonthAndDay;  
    } 
	
	/** 
     * 取两个Date之间的天数差<br> 
     * <br>例：newerDate是6月3日，olderDate是5月31日，则应返回3 
     * <br>一个更极端的列子：newerDate是6月3日 00:01，olderDate是6月2日 23:59，则应返回1，说明相差一天，即便实际上只差2分钟 
     * @param newerDate 
     * @param olderDate 
     * @return 
     */  
    public static int daysBetween(Date newerDate, Date olderDate) {  
        Calendar cNow = Calendar.getInstance();  
        Calendar cReturnDate = Calendar.getInstance();  
        cNow.setTime(newerDate);  
        cReturnDate.setTime(olderDate);  
        setTimeToMidnight(cNow);  
        setTimeToMidnight(cReturnDate);  
        long todayMs = cNow.getTimeInMillis();  
        long returnMs = cReturnDate.getTimeInMillis();  
        long intervalMs = todayMs - returnMs;  
        return millisecondsToDays(intervalMs);  
    }  

    private static int millisecondsToDays(long intervalMs) {  
        return (int) (intervalMs / (1000 * 86400));  
    }  
  
    private static void setTimeToMidnight(Calendar calendar){  
        calendar.set(Calendar.HOUR_OF_DAY, 0);  
        calendar.set(Calendar.MINUTE, 0);  
        calendar.set(Calendar.SECOND, 0);  
    }  
    
    
    public static boolean judgeYear(int year){   	  
    	
    	 if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
    	  return true;
    	 }else{
    		 return false;
    	} 
    }

    /*
    * 计算两个时间相差天数
    * */
    public static int daysBetween(String startDate,String endDate) throws ParseException{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(startDate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(endDate));
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    public static Date formatDate(Date date) throws ParseException{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(sdf.format(date));
    }
    /*
    * 获取当前月的第一天
    * */
    public static Date getCurMonthFirst(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        return c.getTime();
    }

    /*
    * 获取当前月的最后一天
    * */
    public static Date getCurMonthEnd(){
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));//获取当前月最后一天
        return ca.getTime();
    }

    /**
     * 设置时间
     * @param year
     * @param month
     * @param date
     * @return
     */
    public static Calendar setCalendar(int year,int month,int date){
        Calendar cl = Calendar.getInstance();
        cl.set(year, month-1, date);
        return cl;
    }
    /**
     * 获取当前时间的i天时间
     * @param cl
     * @return
     */
    public static Date getDiffDay(Calendar cl,int i){
        //使用roll方法进行向前回滚
        //cl.roll(Calendar.DATE, -1);
        //使用set方法直接进行设置
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day+i);
        return cl.getTime();
    }

    /**
     * 将timestamp转换成date
     * @param timestamp
     * @return
     */
    public static String formatDate(Timestamp timestamp, String pattern) {
        if (checkPara(pattern) || checkPara(timestamp)) {
            return "";
        }
        SimpleDateFormat dateFormator = new SimpleDateFormat(pattern);

        return dateFormator.format(timestamp.getTime());
    }

    /**
     * 两时间相差的小时数
     * */
    public static long getHours(Date date1,Date date2){
        long seconds = Math.abs(date2.getTime() - date1.getTime())/1000;
        return seconds/(60*60);
    }

	/**
	 * 两个时间相差距离多少天多少小时多少分多少秒
	 * @param startDate 时间参数 1 格式：1990-01-01 12:00:00
	 * @param endDate 时间参数 2 格式：2009-01-01 12:00:00
	 * @return long[] 返回值为：{天, 时, 分, 秒}
	 */
	public static long getDateDiffMin(String startDate, String endDate) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date one;
		Date two;
		long min = 0;
		try {
			one = df.parse(startDate);
			two = df.parse(endDate);
			long time1 = one.getTime();
			long time2 = two.getTime();
			long diff = time2 - time1;
			min = diff/(1000*60);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return min;
	}

	/**
	 * 获取格林威治时间
	 * */
	public static String getGMTDateStr(Date date,String pattern){
		final int msInMin = 60000;
		final int minInHr = 60;
		int Hours, Minutes;
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		TimeZone zone = dateFormat.getTimeZone();
		Minutes = zone.getOffset(date.getTime()) / msInMin;
		Hours = Minutes / minInHr;
		zone = zone.getTimeZone("GMT Time" + (Hours >= 0 ? "+" : "") + Hours
				+ ":" + Minutes);
		dateFormat.setTimeZone(zone);
		return dateFormat.format(date);
	}

	public static String getDateRandomBir(int startYear,int endYear){
		Calendar calendar = Calendar.getInstance();
		calendar.set(startYear,1,1);
		Date minDate = calendar.getTime();  //最小时间
		calendar.set(endYear,12,31);
		Date maxDate = calendar.getTime();//最大时间
		//计算两个时间点相隔多少天
		int totalDays = Ints.checkedCast((maxDate.getTime() - minDate.getTime()) / (1000 * 60 * 60 * 24));
		calendar.setTime(minDate);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		calendar.add(Calendar.DAY_OF_MONTH,randomInt(totalDays));
		return formatDate(calendar.getTime(),DATE_FORMAT);
	}

	private static int randomInt(int max){
		int min=1;
		Random random = new Random();
		int s = random.nextInt(max)%(max-min+1) + min;
		return s;
	}
}
