package com.jpay.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * 字符串工具类，继承lang3字符串工具类
 */
public final class StringUtils extends org.apache.commons.lang3.StringUtils {

	public static String encode(String str){
		String encode=null;
		try {
			encode = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encode;
	}
	
	/**
	 * 获取UUID，去掉`-`的
	 * @return {String}
	 * 
	 */
	public static String getUUID () {
		return UUID.randomUUID().toString().replace("-", "");
	}

	
	/**
	 * 要求外部订单号必须唯一。
	 * @return {String}
	 */
	public  static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
		key = key + System.currentTimeMillis();
		key = key.substring(0, 15);
		return key;
	}
	 /**
	 * 将字符串中特定模式的字符转换成map中对应的值
	 * 
	 * use: format("my name is ${name}, and i like ${like}!", {"name":"L.cm", "like": "Java"})
	 * 
	 * @param s		需要转换的字符串
	 * @param map	转换所需的键值对集合
	 * @return		{String}转换后的字符串
	 */
	public static String format(String s, Map<String, String> map) {
		StringBuilder sb = new StringBuilder((int)(s.length() * 1.5));
		int cursor = 0;
		for (int start, end; (start = s.indexOf("${", cursor)) != -1 && (end = s.indexOf('}', start)) != -1;) {
			sb.append(s.substring(cursor, start));
			String key = s.substring(start + 2, end);
			sb.append(map.get(StringUtils.trim(key)));
			cursor = end + 1;
		}
		sb.append(s.substring(cursor, s.length()));
		return sb.toString();
	}
	


	/**
	 * 字符串格式化
	 * 
	 * use: format("my name is {0}, and i like {1}!", "L.cm", "java")
	 * 
	 * int long use {0,number,#}
	 * 
	 * @param s 
	 * @param args
	 * @return {String}转换后的字符串
	 */
	public static String format(String s, Object... args) {
		return MessageFormat.format(s, args);
	}
	
	/**
	 * 替换某个字符
	 * @param str
	 * @param regex
	 * @param args
	 * @return {String}
	 */
	public static String replace(String str,String regex,String... args){
		int length = args.length;
		for (int i = 0; i < length; i++) {
			str=str.replaceFirst(regex, args[i]);
		}
		return str;
	}

	/**
	 * 转义HTML用于安全过滤
	 * @param html
	 * @return {String}
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * 清理字符串，清理出某些不可见字符
	 * @param txt
	 * @return {String}
	 */
	public static String cleanChars(String txt) {
		return txt.replaceAll("[ 　	`·•�\\f\\t\\v]", "");
	}

	// 随机字符串
	private static final String _INT = "0123456789";
	private static final String _STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final String _ALL = _INT + _STR;

	private static final Random RANDOM = new Random();

	/**
	 * 生成的随机数类型
	 */
	public static enum RandomType {
		INT, STRING, ALL;
	}

	/**
	 * 随机数生成
	 * @param count
	 * @return {String}
	 */
	public static String random(int count, RandomType randomType) {
		if (count == 0) return "";
		if (count < 0) {
			throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
		}
		char[] buffer = new char[count];
		for (int i = 0; i < count; i++) {
			if (randomType.equals(RandomType.INT)) {
				buffer[i] = _INT.charAt(RANDOM.nextInt(_INT.length()));
			} else if (randomType.equals(RandomType.STRING)) {
				buffer[i] = _STR.charAt(RANDOM.nextInt(_STR.length()));
			}else {
				buffer[i] = _ALL.charAt(RANDOM.nextInt(_ALL.length()));
			}
		}
		return new String(buffer);
	}
	
	public static void main(String[] args) {
		System.out.println(random(32, RandomType.ALL));
	}
}
