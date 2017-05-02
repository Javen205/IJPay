package com.javen.jpay.util;  
  
import java.io.Serializable;  
import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.Calendar;  
import java.util.Date;  
import java.util.TimeZone;  
  
/** 
 * 日期时间处理封装 
 */  
public class DateTime implements Comparable<DateTime>, Serializable {  
  
    private static final long serialVersionUID = 4715414577633839197L;  
    private Calendar calendar = Calendar.getInstance();  
    private SimpleDateFormat sdf = new SimpleDateFormat();  
  
    private final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";  
    private final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";  
    private final String DEFAULT_TIME_PATTERN = "HH:mm:ss";  
  
    public DateTime() {  
    }  
  
    public DateTime(String dateStr) {  
        try {  
            parse(dateStr);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public DateTime(String dateStr, TimeZone timeZone) {  
        this(dateStr);  
        calendar.setTimeZone(timeZone);  
    }  
  
    public DateTime(String dateStr, String pattern) {  
        try {  
            parse(dateStr, pattern);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public DateTime(String dateStr, String pattern, TimeZone timeZone) {  
        this(dateStr, pattern);  
        calendar.setTimeZone(timeZone);  
    }  
  
    public DateTime(int year, int month, int day, int hour, int minute, int second) {  
        calendar.set(year, month, day, hour, minute, second);  
    }  
  
    public DateTime(int year, int month, int day, int hour, int minute, int second, TimeZone timeZone) {  
        this(year, month, day, hour, minute, second);  
        calendar.setTimeZone(timeZone);  
    }  
  
    public DateTime(long milliSeconds) {  
        calendar.setTimeInMillis(milliSeconds);  
    }  
  
    public Calendar getCalendar() {  
        return calendar;  
    }  
  
    public void setCalendar(Calendar calendar) {  
        this.calendar = calendar;  
    }  
  
    public Date getDate() {  
        return calendar.getTime();  
    }  
  
    public void setDate(Date date) {  
        calendar.setTime(date);  
    }  
  
    public int getYear() {  
        return calendar.get(Calendar.YEAR);  
    }  
  
    public void setYear(int year) {  
        calendar.set(Calendar.YEAR, year);  
    }  
  
    public int getMonth() {  
        return calendar.get(Calendar.MONTH);  
    }  
  
    public void setMonth(int month) {  
        calendar.set(Calendar.MONTH, month);  
    }  
  
    public int getDay() {  
        return calendar.get(Calendar.DAY_OF_MONTH);  
    }  
  
    public void setDay(int dayOfMonth) {  
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);  
    }  
  
    public int getHour() {  
        return calendar.get(Calendar.HOUR_OF_DAY);  
    }  
  
    public void setHour(int hour) {  
        calendar.set(Calendar.HOUR_OF_DAY, hour);  
    }  
  
    public int getMinute() {  
        return calendar.get(Calendar.MINUTE);  
    }  
  
    public void setMinute(int minute) {  
        calendar.set(Calendar.MINUTE, minute);  
    }  
  
    public int getSecond() {  
        return calendar.get(Calendar.SECOND);  
    }  
  
    public void setSecond(int second) {  
        calendar.set(Calendar.SECOND, second);  
    }  
  
    public long getTimeInMilliSeconds() {  
        return calendar.getTimeInMillis();  
    }  
  
    public void setTimeInMilliSeconds(long milliSeconds) {  
        calendar.setTimeInMillis(milliSeconds);  
    }  
  
    public TimeZone getTimeZone() {  
        return calendar.getTimeZone();  
    }  
  
    public void setTimeZone(TimeZone timeZone) {  
        calendar.setTimeZone(timeZone);  
    }  
  
    /** 
     * 使用默认格式解析日期字符串 
     *  
     * @param dateStr 
     * @throws Exception 
     */  
    public void parse(String dateStr) throws Exception {  
        try {  
            parse(dateStr, DEFAULT_DATETIME_PATTERN);  
        } catch (Exception e) {  
            try {  
                parse(dateStr, DEFAULT_DATE_PATTERN);  
            } catch (Exception e1) {  
                try {  
                    parse(dateStr, DEFAULT_TIME_PATTERN);  
                } catch (Exception e2) {  
                    throw new Exception("the date string [" + dateStr + "] could not be parsed");  
                }  
            }  
        }  
  
    }  
  
    /** 
     * 使用给定模式解析日期字符串 
     *  
     * @param dateStr 
     * @param pattern 
     * @throws Exception 
     */  
    public void parse(String dateStr, String pattern) throws Exception {  
        if (dateStr == null) {  
            throw new NullPointerException("date string could not be null");  
        }  
  
        if (pattern == null) {  
            throw new NullPointerException("the pattern string could not be null");  
        }  
  
        try {  
            sdf.applyPattern(pattern);  
            sdf.parse(dateStr);  
        } catch (ParseException e) {  
            throw new Exception("the date string [" + dateStr + "] could not be parsed with the pattern [" + pattern + "]");  
        }  
        calendar = sdf.getCalendar();  
    }  
  
    /** 
     * 格式化当前DateTime对象代表的时间 
     *  
     * @param pattern 
     * @return 
     */  
    public String format(String pattern) {  
        sdf.applyPattern(pattern);  
        return sdf.format(calendar.getTime());  
    }  
  
    /** 
     * 获取默认格式日期字符串 
     *  
     * @return 
     */  
    public String toDateTimeString() {  
        sdf.applyPattern(DEFAULT_DATETIME_PATTERN);  
        return sdf.format(calendar.getTime());  
    }  
  
    @Override  
    public int compareTo(DateTime otherDateTime) {  
        return calendar.compareTo(otherDateTime.getCalendar());  
    }  
  
    /** 
     * 是否闰年 
     *  
     * @return 
     */  
    public boolean isLeapYear() {  
        int year = getYear();  
        boolean result = false;  
        if (year % 100 == 0) {  
            if (year % 400 == 0) {  
                result = true;  
            }  
        } else if (year % 4 == 0) {  
            result = true;  
        }  
        return result;  
    }  
  
    /** 
     * 获取星期 
     *  
     * @return 
     */  
    public int getDayOfWeek() {  
        return calendar.get(Calendar.DAY_OF_WEEK);  
    }  
  
    /** 
     * 是否周末 
     *  
     * @return 
     */  
    public boolean isWeekend() {  
        int dayOfWeek = getDayOfWeek();  
        return dayOfWeek == 1 || dayOfWeek == 7;  
    }  
  
    /** 
     * 当前DateTime对象月份天数 
     *  
     * @return 
     */  
    public int getDayNumsInMonth() {  
        Calendar c = (Calendar) calendar.clone();  
        c.set(Calendar.DAY_OF_MONTH, 1);  
        c.roll(Calendar.DAY_OF_MONTH, -1);  
        return c.get(Calendar.DAY_OF_MONTH);  
    }  
  
    /** 
     * 两个日期之间间隔天数 
     *  
     * @param otherDateTime 
     * @return 
     */  
    public int dayNumFrom(DateTime otherDateTime) {  
        long millis = Math.abs(getTimeInMilliSeconds() - otherDateTime.getTimeInMilliSeconds());  
        int days = (int) Math.floor(millis / 86400000);  
        return days;  
    }  
  
    public boolean lessThan(DateTime otherDateTime) {  
        return compareTo(otherDateTime) < 0;  
    }  
  
    public boolean greaterThan(DateTime otherDateTime) {  
        return compareTo(otherDateTime) > 0;  
    }  
  
    public boolean equal(DateTime otherDateTime) {  
        return compareTo(otherDateTime) == 0;  
    }  
  
    /** 
     * 当前时间基础上增加秒数(负数时为减) 
     *  
     * @param amount 
     */  
    public void plusSecond(int amount) {  
        calendar.add(Calendar.SECOND, amount);  
    }  
  
    public void plusMinute(int amount) {  
        calendar.add(Calendar.MINUTE, amount);  
    }  
  
    public void plusHour(int amount) {  
        calendar.add(Calendar.HOUR, amount);  
    }  
  
    public void plusDays(int amount) {  
        calendar.add(Calendar.DAY_OF_MONTH, amount);  
    }  
  
    public void plusMonth(int amount) {  
        calendar.add(Calendar.MONTH, amount);  
    }  
  
    public void plusYear(int amount) {  
        calendar.add(Calendar.YEAR, amount);  
    }  
  
    public void plus(int year, int month, int day, int hour, int minute, int second) {  
        plusYear(year);  
        plusMonth(month);  
        plusDays(day);  
        plusHour(hour);  
        plusMinute(minute);  
        plusSecond(second);  
    }  
  
}  
