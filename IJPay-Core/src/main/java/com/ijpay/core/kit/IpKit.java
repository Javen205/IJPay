package com.ijpay.core.kit;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import javax.servlet.http.HttpServletRequest;

/**
 * @author Javen
 */
public class IpKit {
	public IpKit() {
	}

	public static String getRealIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		// 对于通过多个代理的情况，第一个 IP 为客户端真实 IP，多个IP按照','分割
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		return ip;
	}

	public static String getRealIpV2(HttpServletRequest request) {
		String accessIp = request.getHeader("x-forwarded-for");
		return null == accessIp ? request.getRemoteAddr() : accessIp;
	}
}
