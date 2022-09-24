package com.ijpay.demo.kit;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Javen
 */
public class IpKit {
	private final static String UNKNOWN = "unknown";

	public static String getRealIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String getRealIpV2(HttpServletRequest request) {
		String accessIp = request.getHeader("x-forwarded-for");
		if (null == accessIp) {
			return request.getRemoteAddr();
		}
		return accessIp;
	}
}
