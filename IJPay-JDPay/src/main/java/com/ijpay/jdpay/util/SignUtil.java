package com.ijpay.jdpay.util;

import org.apache.commons.codec.binary.Base64;

import java.lang.reflect.Field;
import java.util.*;

public class SignUtil {
	private static List<String> unSignKeyList = Arrays.asList(new String[]{"merchantSign", "token", "version"});

	public static String sign4SelectedKeys(Object object, String rsaPriKey, List<String> signKeyList) {
		String result = "";

		try {
			String sourceSignString = signString4SelectedKeys(object, signKeyList);
			String sha256SourceSignString = SHAUtil.Encrypt(sourceSignString, null);

			byte[] newsks = RSACoder.encryptByPrivateKey(sha256SourceSignString.getBytes("UTF-8"), rsaPriKey);
			result = Base64.encodeBase64String(newsks);
		} catch (Exception e) {
			throw new RuntimeException("sign4SelectedKeys>error", e);
		}
		return result;
	}


	public static String signRemoveSelectedKeys(Object object, String rsaPriKey, List<String> signKeyList) {
		String result = "";

		try {
			String sourceSignString = signString(object, signKeyList);

			String sha256SourceSignString = SHAUtil.Encrypt(sourceSignString, null);

			byte[] newK = RSACoder.encryptByPrivateKey(sha256SourceSignString.getBytes("UTF-8"), rsaPriKey);
			result = Base64.encodeBase64String(newK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public static String signString(Object object, List<String> unSignKeyList) throws IllegalArgumentException, IllegalAccessException {
		TreeMap<String, Object> map = objectToMap(object);


		StringBuilder sb = new StringBuilder();

		for (String str : unSignKeyList) {
			map.remove(str);
		}

		Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = (Map.Entry) iterator.next();
			if (entry.getValue() == null) {
				continue;
			}
			String value = (String) entry.getValue();
			if (value.trim().length() > 0) {
				sb.append((String) entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}

		String result = sb.toString();
		if (result.endsWith("&")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}


	private static String signString4SelectedKeys(Object object, List<String> signedKeyList) throws IllegalArgumentException, IllegalAccessException {
		TreeMap<String, Object> map = objectToMap(object);
		if (map == null || map.isEmpty() || signedKeyList == null || signedKeyList.isEmpty()) {
			return null;
		}

		TreeMap<String, Object> signMap = new TreeMap<String, Object>();

		StringBuilder sb = new StringBuilder();

		for (String str : signedKeyList) {
			Object o = map.get(str);
			if (o != null) {
				signMap.put(str, o);
				continue;
			}
			signMap.put(str, "");
		}


		Iterator iterator = signMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			sb.append(entry.getKey() + "=" + (
				(entry.getValue() == null) ? "" : entry.getValue()) + "&");
		}

		String result = sb.toString();
		if (result.endsWith("&")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}


	public static TreeMap<String, Object> objectToMap(Object object) throws IllegalArgumentException, IllegalAccessException {
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		if (object instanceof Map) {
			Map<String, Object> objectMap = (Map) object;
			for (String str : objectMap.keySet()) {
				map.put(str, objectMap.get(str));
			}
			return map;
		}

		for (Class<?> cls = object.getClass(); cls != Object.class; cls = cls.getSuperclass()) {

			Field[] fields = cls.getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(true);
				map.put(f.getName(), f.get(object));
			}
		}
		return map;
	}


	public static String sign4PCString(Object object, List<String> unSignKeyList) throws IllegalArgumentException, IllegalAccessException {
		TreeMap<String, Object> map = objectToMap(object);

		StringBuilder sb = new StringBuilder();

		for (String str : unSignKeyList) {
			map.remove(str);
		}


		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			sb.append(entry.getKey() + "=" + ((entry.getValue() == null) ? "" : entry.getValue()) + "&");
		}

		String result = sb.toString();
		if (result.endsWith("&")) {
			result = result.substring(0, result.length() - 1);
		}

		return result;
	}
}
