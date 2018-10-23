package com.nyq.projecttreasure.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc:(时间帮助类)
 * @company:中国电信甘肃万维
 * @projectName:jkgs
 * @author:liufx
 * @CreateTime:16/9/8 16:35
 */
public class TimeHelper {
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";
    public static final String FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
    private static final String TAG = "TimeHelper";
    private static final String HHMM = "HH:mm";
    private static final String YMDHMS = "yyyyMMddHHmmss";
    public static final String YMD = "yyyy-MM-dd";
    public static final String YM = "yyyy-MM";
    public static final String YMDHM = "yyyy-MM-dd HH:mm";

    private static String currentTime;
    private static String currentDate;

    private TimeHelper() {
        //私有化构造方法 隐藏对象
    }

    public static String format(Date date) {
        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    public static String getDTFormat(String sdate) {
        String format = "";
        try {
            Date current = new Date();
            long microCurrent = current.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_STR);
            Date date = sdf.parse(sdate);
            long microDate = date.getTime();
            long secPre = 60 * 60 * 1000L;//X分钟前
            long hourPre = 6 * 60 * 60 * 1000L;//6小时前
            String currentYear = new SimpleDateFormat("yyyy").format(current);//取得当前年
            String juageYear = new SimpleDateFormat("yyyy").format(date);
            String currentDay = getPreDay(0);//取得当前天
            String yesterdayPre = getPreDay(-1);//获取昨天
            String beforeYesterdayPre = getPreDay(-2);//获取前天
            long difference = microCurrent - microDate;
            if (difference < secPre) {
                long sec = difference / (60 * 1000);
                return sec == 0L ? "刚刚" : sec + "分钟前";
            } else if (difference < hourPre) {
                long sec = difference / (60 * 60 * 1000);
                return sec == 0L ? "1" : sec + "小时前";
            } else if (sdate.substring(0, 10).equals(currentDay)) {
                return new SimpleDateFormat(HHMM).format(date);
            } else if (sdate.substring(0, 10).equals(yesterdayPre)) {
                return "昨天 " + new SimpleDateFormat(HHMM).format(date);
            } else if (sdate.substring(0, 10).equals(beforeYesterdayPre)) {
                return "前天" + new SimpleDateFormat(HHMM).format(date);
            } else if (currentYear.equals(juageYear)) {
                return new SimpleDateFormat("M-dd HH:mm").format(date);
            } else {
                return new SimpleDateFormat("yyyy-M-dd").format(date);
            }
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return format;
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }


    /**
     * @param obj
     * @return
     * @desc(object �?String)
     */
    public static String convertToString(Object obj) {
        if (obj == null)
            return "";
        String str = obj.toString().trim();
        if ("null".equals(str) || "NULL".equals(str))
            return "";
        return str;
    }

    public static String getYear() {
        Calendar c = Calendar.getInstance();
        return Integer.toString(c.get(Calendar.YEAR));
    }

    public static String getMonth() {
        Calendar c = Calendar.getInstance();
        return Integer.toString(c.get(Calendar.MONTH) + 1);
    }

    public static String getDay() {
        Calendar c = Calendar.getInstance();
        return Integer.toString(c.get(Calendar.DATE));
    }

    /**
     * 得到当前的年份返回格式:yyyy
     *
     * @return String
     */
    public static String getCurrentYear() {
        Date nowDate = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        return formatter.format(nowDate);
    }

    /**
     * 得到当前的月份返回格式:MM
     *
     * @return String
     */
    public static String getCurrentMonth() {
        Date nowDate = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        return formatter.format(nowDate);
    }

    /**
     * 得到当前的日期返回格式:dd
     *
     * @return String
     */
    public static String getCurrentDay() {
        Date nowDate = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        return formatter.format(nowDate);
    }

    /**
     * 得到当前的时间，精确到毫秒 返回格式:yyyy-MM-dd HH:mm:ss
     *
     * @return String
     */
    public static String getCurrentTime() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_STR);
        currentTime = formatter.format(nowDate);
        return currentTime;
    }

    public static String getCurrentDateTimeHM() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(YMDHM);
        currentTime = formatter.format(nowDate);
        return currentTime;
    }

    public static String getCurrentCompactTime() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(YMDHMS);
        currentTime = formatter.format(nowDate);
        return currentTime;
    }

    public static String getYesterdayCompactTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat formatter = new SimpleDateFormat(YMDHMS);
        currentTime = formatter.format(cal.getTime());
        return currentTime;
    }

    public static String getYesterdayCompactTimeForFileName() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_STR);
        currentTime = formatter.format(cal.getTime());
        return currentTime;
    }

    /**
     * 得到当前的日期返回格式：yyyy-MM-dd
     *
     * @return String
     */
    public static String getCurrentDate() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(YMD);
        currentDate = formatter.format(nowDate);
        return currentDate;
    }

    /**
     * 得到当月的第一天 返回格式：yyyy-MM-dd
     *
     * @return String
     */
    public static String getCurrentFirstDate() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-01");
        currentDate = formatter.format(nowDate);
        return currentDate;
    }

    /**
     * 得到当月的最后一天 返回格式：yyyy-MM-dd
     *
     * @return String
     * @throws ParseException
     */
    public static String getCurrentLastDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(YMD);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = formatter.parse(getCurrentFirstDate());
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return formatDate(calendar.getTime());

    }

    /*
     * 取昨天日期
    */
    public static String getYesterdayDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat formatter = new SimpleDateFormat(YMD);
        currentTime = formatter.format(cal.getTime());
        return currentTime;
    }

    /**
     * 常用的格式化日期
     *
     * @param date Date
     * @return String
     */
    public static String formatDate(Date date) {
        return formatDateByFormat(date, YMD);
    }

    /**
     * @param date
     * @param format
     * @return
     * @desc(字符串格式化为日期)
     */
    public static String formatStringToDate(String date, String format) {
        try {
            if (date == null || "".equals(date)) {
                return formatDateByFormat(new Date(), format);
            } else {
                SimpleDateFormat sdf1 = new SimpleDateFormat(FORMAT_STR);
                SimpleDateFormat sdf2 = new SimpleDateFormat(format);
                if (date.length() > 11) {
                    Date dateValue = sdf1.parse(date);
                    return sdf2.format(dateValue);
                } else {
                    return date;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return formatDateByFormat(new Date(), format);
        }
    }

    /**
     * 格式化制定格式日期
     *
     * @param date
     * @param format
     * @return
     * @desc(字符串格式化为日期)
     */
    public static String formatString(String date, String format) {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat(YMD);
            SimpleDateFormat sdf2 = new SimpleDateFormat(format);
            Date d = sdf1.parse(date);
            return sdf2.format(d);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 格式化制定格式日期
     *
     * @param date
     * @param format
     * @return
     * @desc(字符串格式化为日期)
     */
    public static String formatDateString(String date, String format) {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat(YMD);
            SimpleDateFormat sdf2 = new SimpleDateFormat(format);
            Date d = sdf1.parse(date);
            return sdf2.format(d);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }


    /**
     * 以指定的格式来格式化日期
     *
     * @param date   Date
     * @param format String
     * @return String
     */
    public static String formatDateByFormat(Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ex) {
                Log.i(TAG, ex.getMessage());
            }
        }
        return result;
    }

    /**
     * 得到 前几天就负数  未来的就正数
     * int 返回格式：yyyy-MM-dd
     */
    public static String getPreDay(int day) {
        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, day);    //得到前一天
        return new SimpleDateFormat(YMD).format(calendar.getTime());
    }

    /**
     * 得到当前日期加上某一个整数的日期，整数代表天�?输入参数：currentDate : String 格式 yyyy-MM-dd addDay :
     * int 返回格式：yyyy-MM-dd
     */
    public static String addDay(String currentDate, int addDay) {
        GregorianCalendar gc = null;
        SimpleDateFormat formatter = new SimpleDateFormat(YMD);
        int year;
        int month;
        int day;
        try {
            year = Integer.parseInt(currentDate.substring(0, 4));
            month = Integer.parseInt(currentDate.substring(5, 7)) - 1;
            day = Integer.parseInt(currentDate.substring(8, 10));

            gc = new GregorianCalendar(year, month, day);
            gc.add(GregorianCalendar.DATE, addDay);

            return formatter.format(gc.getTime());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 得到当前日期加上某一个整数的日期，整数代表月�?输入参数：currentDate : String 格式 yyyy-MM-dd addMonth :
     * int 返回格式：yyyy-MM-dd
     */
    public static String addMonth(String currentDate, int addMonth) {
        GregorianCalendar gc = null;
        SimpleDateFormat formatter = new SimpleDateFormat(YMD);
        int year;
        int month;
        int day;
        try {
            year = Integer.parseInt(currentDate.substring(0, 4));
            month = Integer.parseInt(currentDate.substring(5, 7)) - 1;
            day = Integer.parseInt(currentDate.substring(8, 10));
            gc = new GregorianCalendar(year, month, day);
            gc.add(GregorianCalendar.MONTH, addMonth);

            return formatter.format(gc.getTime());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 给定字符串和格式，返回加一个月的串
     * 输入："1998-11-10"，YMD
     * 输出："1998-12-10"
     *
     * @param d
     * @param format
     * @return
     * @author yuzx
     */
    public static String getStrByaddMonth(String d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(d);
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            gc.add(Calendar.MONTH, 1);
            return sdf.format(gc.getTime());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 给定字符串和格式，返回加一个星期的串
     * 输入："1998-11-10"，YMD
     * 输出："1998-12-10"
     *
     * @param d
     * @param format
     * @return
     * @author yuzx
     */
    public static String getStrByaddWeek(String d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(d);
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            gc.add(Calendar.WEEK_OF_YEAR, 1);
            return sdf.format(gc.getTime());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * 得到endTime比beforeTime晚几个月，整数代表月�?输入参数：endTime、beforeTime : String 格式�?位的格式�?yyyy-MM
     */
    public static int monthDiff(String beforeTime, String endTime) {
        if (beforeTime == null || endTime == null) {
            return 0;
        }
        int beforeYear;
        int endYear;
        int beforeMonth;
        int endMonth;
        try {
            beforeYear = Integer.parseInt(beforeTime.substring(0, 4));
            endYear = Integer.parseInt(endTime.substring(0, 4));
            beforeMonth = Integer.parseInt(beforeTime.substring(5, 7)) - 1;
            endMonth = Integer.parseInt(endTime.substring(5, 7)) - 1;
            return (endYear - beforeYear) * 12 + (endMonth - beforeMonth);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return 0;
    }

    /**
     * 得到当前日期加上某一个整数的分钟 输入参数：currentDatetime : String 格式 yyyy-MM-dd HH:mm:ss
     * addMinute : int 返回格式：yyyy-MM-dd HH:mm:ss
     */
    public static String addMinute(String currentDatetime, int addMinute) {
        GregorianCalendar gc = null;
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_STR);
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int second;
        try {
            year = Integer.parseInt(currentDatetime.substring(0, 4));
            month = Integer.parseInt(currentDatetime.substring(5, 7)) - 1;
            day = Integer.parseInt(currentDatetime.substring(8, 10));

            hour = Integer.parseInt(currentDatetime.substring(11, 13));
            minute = Integer.parseInt(currentDatetime.substring(14, 16));
            second = Integer.parseInt(currentDatetime.substring(17, 19));

            gc = new GregorianCalendar(year, month, day, hour, minute, second);
            gc.add(GregorianCalendar.MINUTE, addMinute);

            return formatter.format(gc.getTime());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 得到当前日期加上某一个整数的�?输入参数：currentDatetime : String 格式 yyyy-MM-dd HH:mm:ss
     * addSecond : int 返回格式：yyyy-MM-dd HH:mm:ss
     */
    public static String addSecond(String currentDatetime, int addSecond) {
        GregorianCalendar gc = null;
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_STR);
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int second;
        try {
            year = Integer.parseInt(currentDatetime.substring(0, 4));
            month = Integer.parseInt(currentDatetime.substring(5, 7)) - 1;
            day = Integer.parseInt(currentDatetime.substring(8, 10));

            hour = Integer.parseInt(currentDatetime.substring(11, 13));
            minute = Integer.parseInt(currentDatetime.substring(14, 16));
            second = Integer.parseInt(currentDatetime.substring(17, 19));

            gc = new GregorianCalendar(year, month, day, hour, minute, second);
            gc.add(GregorianCalendar.SECOND, addSecond);

            return formatter.format(gc.getTime());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public static String addMinute1(String currentDatetime, int addMinute) {
        GregorianCalendar gc = null;
        SimpleDateFormat formatter = new SimpleDateFormat(YMDHMS);
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int second;
        try {
            year = Integer.parseInt(currentDatetime.substring(0, 4));
            month = Integer.parseInt(currentDatetime.substring(5, 7)) - 1;
            day = Integer.parseInt(currentDatetime.substring(8, 10));

            hour = Integer.parseInt(currentDatetime.substring(8, 10));
            minute = Integer.parseInt(currentDatetime.substring(8, 10));
            second = Integer.parseInt(currentDatetime.substring(8, 10));

            gc = new GregorianCalendar(year, month, day, hour, minute, second);
            gc.add(GregorianCalendar.MINUTE, addMinute);

            return formatter.format(gc.getTime());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public static Date parseDate(String sDate) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat(YMD);
        try {
            return bartDateFormat.parse(sDate);
        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());
        }
        return null;
    }

    /**
     * 解析日期及时时间
     *
     * @param sDateTime 日期及时间字符串
     * @return 日期
     */
    public static Date parseDateTime(String sDateTime) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat(FORMAT_STR);
        try {
            return bartDateFormat.parse(sDateTime);
        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());
        }
        return null;
    }

    /**
     * 取得当前月的天数 date:yyyy-MM-dd
     *
     * @throws ParseException
     */
    public static int getTotalDaysOfMonth(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        Calendar calendar = new GregorianCalendar();

        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());
        }
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 天数
    }


    public static long getDateSubDay(String startDate, String endDate) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        long theday = 0;
        try {
            calendar.setTime(sdf.parse(startDate));
            long timethis = calendar.getTimeInMillis();
            calendar.setTime(sdf.parse(endDate));
            long timeend = calendar.getTimeInMillis();
            theday = (timethis - timeend) / (1000 * 60 * 60 * 24);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return theday;
    }

    public static Map getDaysBirthday(String birthday) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        long theday = 0;
        Map valueMap = new HashMap();
        try {
            long timethis = calendar.getTimeInMillis();
            calendar.setTime(sdf.parse(birthday));
            long timeend = calendar.getTimeInMillis();
            theday = (timethis - timeend) / (1000 * 60 * 60 * 24);
            String strTmp = "";
            if (theday > 100) {
                strTmp = String.valueOf(theday / 30);
                valueMap.put("NVALUE", strTmp);
                valueMap.put("NUNIT", "月");
            } else {
                strTmp = String.valueOf(theday);
                valueMap.put("NVALUE", strTmp);
                valueMap.put("NUNIT", "天");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return valueMap;
    }

    //根据日期获得星期
    public static String getWeekDay(String dateString) {
        final String[] dayNames = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        SimpleDateFormat sdfInput = new SimpleDateFormat(YMD);
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = sdfInput.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0)
            dayOfWeek = 0;
        return dayNames[dayOfWeek];
    }

    //根据日期获得星期
    public static String getWeekName(String dateString) {
        final String[] dayNames = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        SimpleDateFormat sdfInput = new SimpleDateFormat(YMD);
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = sdfInput.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayOfWeek < 0)
            dayOfWeek = 0;
        return dayNames[dayOfWeek];
    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static String getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        c.add(Calendar.DATE, -dayofweek + 1);
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        return sdf.format(c.getTime());
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static String getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        c.add(Calendar.DATE, -dayofweek + 7);
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        return sdf.format(c.getTime());
    }

    /**
     * 得到指定日期周一
     *
     * @return yyyy-MM-dd
     */
    public static String getPreMondayOfDateWeek(String dateInfo) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        try {
            calendar.setTime(sdf.parse(dateInfo));
            calendar.add(Calendar.DATE, -7);
            int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayofweek == 0)
                dayofweek = 7;
            calendar.add(Calendar.DATE, -dayofweek + 1);
        } catch (ParseException e) {
            // @desc Auto-generated catch block
            Log.e(TAG, e.getMessage());
        }
        return sdf.format(calendar.getTime());
    }

    /**
     * 得到指定日期周日
     *
     * @return yyyy-MM-dd
     */
    public static String getPreSundayOfDateWeek(String dateInfo) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        try {
            calendar.setTime(sdf.parse(dateInfo));
            calendar.add(Calendar.DATE, -7);
            int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayofweek == 0)
                dayofweek = 7;
            calendar.add(Calendar.DATE, -dayofweek + 7);
        } catch (ParseException e) {
            // @desc Auto-generated catch block
            Log.e(TAG, e.getMessage());
        }
        return sdf.format(calendar.getTime());
    }

    /**
     * 得到指定日期周一
     *
     * @return yyyy-MM-dd
     */
    public static String getNextMondayOfDateWeek(String dateInfo) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        try {
            calendar.setTime(sdf.parse(dateInfo));
            calendar.add(Calendar.DATE, 7);
            int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayofweek == 0)
                dayofweek = 7;
            calendar.add(Calendar.DATE, -dayofweek + 1);
        } catch (ParseException e) {
            // @desc Auto-generated catch block
            Log.e(TAG, e.getMessage());
        }
        return sdf.format(calendar.getTime());
    }

    /**
     * 得到指定日期周日
     *
     * @return yyyy-MM-dd
     */
    public static String getNextSundayOfDateWeek(String dateInfo) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        try {
            calendar.setTime(sdf.parse(dateInfo));
            calendar.add(Calendar.DATE, 7);
            int dayofweek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (dayofweek == 0)
                dayofweek = 7;
            calendar.add(Calendar.DATE, -dayofweek + 7);
        } catch (ParseException e) {
            // @desc Auto-generated catch block
            Log.e(TAG, e.getMessage());
        }
        return sdf.format(calendar.getTime());
    }

    /**
     * 得到两个时间的月日
     *
     * @return yyyy-MM-dd
     */
    public static String getTwoTimeMonthAndDay(String startTime, String endTime) {
        String str1 = startTime.substring(5, 7) + "月" + startTime.substring(8, 10) + "日";
        String str2 = endTime.substring(5, 7) + "月" + endTime.substring(8, 10) + "日";
        return str1 + " - " + str2;
    }

    public static Date convertToDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(YMD);
        try {
            return formatter.parse(date);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param year  年
     * @param month 月
     * @return void
     * @description 获取某年某月的天数
     */
    public static Integer getMaxDayByYearMonth(Integer year, Integer month) {
        GregorianCalendar date = new GregorianCalendar(year, month - 1, 1);
        return date.getActualMaximum(GregorianCalendar.DATE);
    }

    /**
     * 根据出生日期获取年龄
     *
     * @return yyyy-MM-dd
     */
    public static String getAgeByBirthdy(String birthday) {
        Date birthDay = parseDate(birthday);
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                //monthNow>monthBirth
                age--;
            }
        }
        if (age == 0) {
            age++;
        }

        return Integer.toString(age);
    }

    /**
     * @param year  年
     * @param month 月
     * @param day   日
     * @return Date
     * @throws
     * @description 将分别为整形的年月日转换为一个Date对象
     */
    public static Date parseDate(Integer year, Integer month, Integer day) {
        return parseDate(new StringBuffer(10).append(year).append("-").append(month).append("-").append(day).toString());
    }

    /**
     * @param time
     * @param formate
     * @return
     * @desc(根据制定格式获取时间字符串)
     */
    public static String getDateFormatForString(long time, String formate) {
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**
     * @param month
     * @return
     * @desc(整数月转换位字符串)
     */
    public static String getFormatMonthString(int month) {
        if (month < 10) {
            return "0" + month;
        } else {
            return Integer.toString(month);
        }
    }

    /**
     * @param day
     * @return
     * @desc(整数日转换位字符串)
     */
    public static String getFormatDayString(int day) {
        if (day < 10) {
            return "0" + day;
        } else {
            return Integer.toString(day);
        }
    }

    /**
     * @param specifiedDate
     * @return
     * @desc(获取指定日期月份的第一天)
     */
    public static String getSpecifiedMonthFirstDay(String specifiedDate) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YMD);
            calendar.setTime(sdf.parse(specifiedDate));
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        } catch (ParseException e) {
            // @desc Auto-generated catch block
            Log.e(TAG, e.getMessage());
        }
        return new SimpleDateFormat(YMD).format(calendar.getTime());
    }

    /**
     * @param timeDate
     * @return
     * @desc(获取上个月第一天的方法)
     */
    public static String getPreMonthFirstDay(String timeDate) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YMD);
            calendar.setTime(simpleDateFormat.parse(timeDate));
            calendar.add(Calendar.MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return new SimpleDateFormat(YMD).format(calendar.getTime());
    }

    /**
     * @param timeDate
     * @return
     * @desc(获取上个月最后一天的方法)
     */
    public static String getPreMonthLastDay(String timeDate) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YMD);
            calendar.setTime(simpleDateFormat.parse(timeDate));
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return new SimpleDateFormat(YMD).format(calendar.getTime());
    }

    /**
     * @param timeDate
     * @return
     * @desc(获取下个月第一天的方法)
     */
    public static String getNextMonthFirstDay(String timeDate) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YMD);
            calendar.setTime(simpleDateFormat.parse(timeDate));
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return new SimpleDateFormat(YMD).format(calendar.getTime());
    }

    /**
     * @param timeDate
     * @return
     * @desc(获取下个月最后一天的方法)
     */
    public static String getNextMonthLastDay(String timeDate) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YMD);
            calendar.setTime(simpleDateFormat.parse(getSpecifiedMonthFirstDay(timeDate)));
            calendar.add(Calendar.MONTH, 2);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return new SimpleDateFormat(YMD).format(calendar.getTime());
    }

    /**
     * @param specifiedDate
     * @return
     * @desc(获取指定月的最后一天)
     */
    public static String getSpecifiedMonthLastDay(String specifiedDate) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YMD);
            calendar.setTime(sdf.parse(specifiedDate));
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        } catch (ParseException e) {
            // @desc Auto-generated catch block
            Log.e(TAG, e.getMessage());
        }
        return new SimpleDateFormat(YMD).format(calendar.getTime());
    }

    /**
     * @param currentTime
     * @return
     * @desc(获取季度的开始月份)
     */
    public static String getStartMonth(String currentTime) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YMD);
            calendar.setTime(sdf.parse(currentTime));
            int month = getQuarterInMonth(calendar.get(Calendar.MONTH) + 1);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        } catch (ParseException e) {
            // @desc Auto-generated catch block
            Log.e(TAG, e.getMessage());
        }
        return new SimpleDateFormat(YMD).format(calendar.getTime());
    }

    /**
     * @param currentTime
     * @return
     * @desc(获取季度的结束月份)
     */
    public static String getEndMonth(String currentTime) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(YMD);
            calendar.setTime(sdf.parse(currentTime));
            int month = getQuarterEndMonth(calendar.get(Calendar.MONTH) + 1);
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        } catch (ParseException e) {
            // @desc Auto-generated catch block
            Log.e(TAG, e.getMessage());
        }
        return new SimpleDateFormat(YMD).format(calendar.getTime());
    }

    /**
     * @param currentTime
     * @return
     * @desc(获取下一个季度)
     */
    public static String getNextQuarter(String currentTime) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YMD);
            calendar.setTime(simpleDateFormat.parse(currentTime));
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 3);
        } catch (ParseException e) {
            // @desc Auto-generated catch block
            Log.e(TAG, e.getMessage());
        }
        return new SimpleDateFormat(YMD).format(calendar.getTime());
    }

    /**
     * @param currentTime
     * @return
     * @desc(获取前一个季度)
     */
    public static String getPrevioisQuarter(String currentTime) {
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YMD);
            calendar.setTime(simpleDateFormat.parse(currentTime));
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 3);
        } catch (ParseException e) {
            // @desc Auto-generated catch block
            Log.e(TAG, e.getMessage());
        }
        return new SimpleDateFormat(YMD).format(calendar.getTime());
    }

    /**
     * 返回指定日期的季度
     *
     * @param timeDate
     * @return
     */
    public static int getQuarterOfYear(String timeDate) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YMD);
        try {
            calendar.setTime(simpleDateFormat.parse(timeDate));
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return calendar.get(Calendar.MONTH) / 3 + 1;
    }

    public static boolean isAfterDay(String t1, String t2) throws ParseException {
        DateFormat format = new SimpleDateFormat(YMD);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Date d1;
        Date d2;
        try {
            d1 = format.parse(t1);
            d2 = format.parse(t2);
            c1.setTime(d1);
            c2.setTime(d2);
        } catch (ParseException e) {
            // @desc Auto-generated catch block
            Log.e(TAG, e.getMessage());
        }
        return c1.before(c2);
    }

    private static int getQuarterInMonth(int month) {
        int[] months = {1, 4, 7, 10};
        if (month >= 1 && month <= 3)
            return months[0];
        else if (month >= 4 && month <= 6)
            return months[1];
        else if (month >= 7 && month <= 9)
            return months[2];
        else
            return months[3];
    }

    private static int getQuarterEndMonth(int month) {
        int[] months = {3, 6, 9, 12};
        if (month >= 1 && month <= 3)
            return months[0];
        else if (month >= 4 && month <= 6)
            return months[1];
        else if (month >= 7 && month <= 9)
            return months[2];
        else
            return months[3];
    }

    /**
     * 输入参数：currentDate : String 格式 yyyy-MM-dd HH:mm:ss
     * int 返回格式：yyyy年M月d日
     */
    public static String getYTD(String currentDate) {
        GregorianCalendar gc = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年M月d日");
        int year;
        int month;
        int day;
        try {
            year = Integer.parseInt(currentDate.substring(0, 4));
            month = Integer.parseInt(currentDate.substring(5, 7)) - 1;
            day = Integer.parseInt(currentDate.substring(8, 10));
            gc = new GregorianCalendar(year, month, day);
            return formatter.format(gc.getTime());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * 输入参数：currentDate : String 格式 yyyy-MM-dd HH:mm:ss
     * int 返回格式：M月d日
     */
    public static String getTD(String currentDate) {
        GregorianCalendar gc = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
        int year;
        int month;
        int day;
        try {
            year = Integer.parseInt(currentDate.substring(0, 4));
            month = Integer.parseInt(currentDate.substring(5, 7)) - 1;
            day = Integer.parseInt(currentDate.substring(8, 10));
            gc = new GregorianCalendar(year, month, day);
            return formatter.format(gc.getTime());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /**
     * @param hour
     * @param min
     * @param sec
     * @return
     * @desc(获取符合格式的时间)
     */
    public static String getFormatTime(long hour, long min, long sec) {
        // 0 代表前面补充0
        // 2 代表长度为2
        // d 代表参数为正数型
        return String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec);
    }

    /**
     * @param date
     * @return
     * @desc(获取指定日期所在周的周一)
     */
    public static String getFirstDayOfWeek(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(date));
            // Calendar默认周日为第一天, 所以设置为1
            cal.set(Calendar.DAY_OF_WEEK, 1);
            // 如果要返回00点0分0秒
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return sdf.format(cal.getTime());
    }

    /**
     * @param date
     * @return
     * @desc(获取指定日期所在周的日期)
     */
    public static String[] getDaysOfWeek(String date) {
        String[] days = new String[7];
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(date));
            // Calendar默认周日为第一天, 所以设置为2
            for (int i = 0; i < 6; i++) {
                cal.set(Calendar.DAY_OF_WEEK, 2 + i);
                days[i] = sdf.format(cal.getTime());
            }
            days[6] = addDay(days[5], 1);
            // 如果要返回00点0分0秒
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return days;
    }

    /**
     * @param date
     * @return
     * @desc(获取指定日期所在周的周日)
     */
    public static String getSevenDayOfWeek(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(date));
            // Calendar默认周日为第一天, 所以设置为1
            cal.set(Calendar.DAY_OF_WEEK, 7);
            // 如果要返回00点0分0秒
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        return sdf.format(cal.getTime());
    }

    /**
     * getCurrentCompactTimeToMillisecond:(获取当前时间到毫秒数，格式：yyyyMMddHHmmssSSS). <br/>
     *
     * @return
     * @author Administrator
     */
    public static String getCurrentCompactTimeToMillisecond() {
        Date nowDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        currentTime = formatter.format(nowDate);
        return currentTime;
    }

    /**
     * 获取宝宝年龄
     *
     * @param s
     * @return
     */
    public static String getAge(String s) {
        String[] ss = s.split("-");

        Calendar birthday = new GregorianCalendar(Integer.valueOf(ss[0]), Integer.valueOf(ss[1]) - 1, Integer.valueOf(ss[2]));

        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH) - birthday.get(Calendar.DAY_OF_MONTH);
        int month = now.get(Calendar.MONTH) - birthday.get(Calendar.MONTH);
        int year = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
        //按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。

        if (day < 0) {
            month -= 1;
            now.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数。

            day = day + now.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        if (month < 0) {
            month = (month + 12) % 12;
            year--;
        }
        if (year == 0) {
            if (month == 0) {
                return day + "日";
            } else {
                return month + "月" + day + "天";
            }
        }
        return year + "年" + month + "月" + day + "天";
    }

    /**
     * 输入参数：currentDate : String 格式 yyyy-MM-dd HH:mm:ss
     * int 返回格式：MM-dd
     */
    public static String getYD(String currentDate) {
        GregorianCalendar gc = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        int year;
        int month;
        int day;
        try {
            year = Integer.parseInt(currentDate.substring(0, 4));
            month = Integer.parseInt(currentDate.substring(5, 7)) - 1;
            day = Integer.parseInt(currentDate.substring(8, 10));
            gc = new GregorianCalendar(year, month, day);
            return formatter.format(gc.getTime());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    /*
    * @param date1 <String>
     * @param date2 <String>
     * @return 两个日期相差天数
     * @throws ParseException
     */
    public static int getDateSpace(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();

        try {
            calst.setTime(sdf.parse(date1));
            caled.setTime(sdf.parse(date2));
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        //设置时间为0时
        calst.set(Calendar.HOUR_OF_DAY, 0);
        calst.set(Calendar.MINUTE, 0);
        calst.set(Calendar.SECOND, 0);
        caled.set(Calendar.HOUR_OF_DAY, 0);
        caled.set(Calendar.MINUTE, 0);
        caled.set(Calendar.SECOND, 0);
        //得到两个日期相差的天数
        return ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
    }

    /*
     * @param date1 <String>
     * @return 获取两个日期间的月数
     * @throws ParseException
     */
    public static int getMosByBetween(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //开始时间
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        //结束时间
        Calendar end = Calendar.getInstance();
        int subMonthCount = -1;
        if (!start.after(end)){

            subMonthCount = (end.get(Calendar.YEAR) - start.get(Calendar.YEAR) == 0)
                            ? end.get(Calendar.MONTH) - start.get(Calendar.MONTH)  //同一年
                            :   ((end.get(Calendar.YEAR) - start.get(Calendar.YEAR) >= 2) //年数差超过2年
                            ? (end.get(Calendar.YEAR) - start.get(Calendar.YEAR) - 1)
                            * 12 + start.getActualMaximum(Calendar.MONTH) - start.get(Calendar.MONTH)
                            + end.get(Calendar.MONTH) + 1
                            : start.getActualMaximum(Calendar.MONTH) - start.get(Calendar.MONTH)
                            + end.get(Calendar.MONTH) + 1);  //年数差为1，Calendar.get(MONTH) 第一月是0，所以+1
            System.out.println(subMonthCount);
        }
        return subMonthCount;
    }
    /**
     * 根据日期或月日
     *
     * @param dataStr
     * @return
     */
    public static String getMd(String dataStr) {
        String temp = "";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(YMD);
        try {
            calendar.setTime(sdf.parse(dataStr));
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            temp = month + "-" + day;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

}
