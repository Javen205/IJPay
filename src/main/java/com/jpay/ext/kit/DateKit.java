package com.jpay.ext.kit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateKit.
 */
public class DateKit {

	public static String datePattern = "yyyy-MM-dd";
	public static String timeStampPattern = "yyyy-MM-dd HH:mm:ss";
	public static String UnionTimeStampPattern = "YYYYMMddHHmmss";
	public static String YYYYMMDD = "YYYYMMDD";
	public static String UnionDateStampPattern = "MMdd";

	public static void setDatePattern(String datePattern) {
		if (StrKit.isBlank(datePattern)) {
			throw new IllegalArgumentException("datePattern can not be blank");
		}
		DateKit.datePattern = datePattern;
	}

	public static void setTimeStampPattern(String timeStampPattern) {
		if (StrKit.isBlank(timeStampPattern)) {
			throw new IllegalArgumentException("timeStampPattern can not be blank");
		}
		DateKit.timeStampPattern = timeStampPattern;
	}

	public static Date toDate(String dateStr) {
		if (StrKit.isBlank(dateStr)) {
			return null;
		}

		dateStr = dateStr.trim();
		int length = dateStr.length();
		try {
			if (length == timeStampPattern.length()) {
				SimpleDateFormat sdf = new SimpleDateFormat(timeStampPattern);
				try {
					return sdf.parse(dateStr);
				} catch (ParseException e) {
					dateStr = dateStr.replace(".", "-");
					dateStr = dateStr.replace("/", "-");
					return sdf.parse(dateStr);
				}
			} else if (length == datePattern.length()) {
				SimpleDateFormat sdfDate = new SimpleDateFormat(datePattern);
				try {
					return sdfDate.parse(dateStr);
				} catch (ParseException e) {
					dateStr = dateStr.replace(".", "-");
					dateStr = dateStr.replace("/", "-");
					return sdfDate.parse(dateStr);
				}
			} else {
				throw new IllegalArgumentException("The date format is not supported for the time being");
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("The date format is not supported for the time being");
		}
	}

	public static String toStr(Date date) {
		return toStr(date, DateKit.datePattern);
	}

	public static String toStr(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	/**
	 * 当前时间延后一段时间
	 * @param date
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static String toStr(Date date,long time, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date.getTime()+time);
	}
	public static void main(String[] args) {
		System.out.println(toStr(new Date(), UnionDateStampPattern));
	}
}
