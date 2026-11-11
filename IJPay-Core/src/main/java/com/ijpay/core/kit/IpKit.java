package com.ijpay.core.kit;

import java.util.Map;

/**
 * IP 工具类。
 * <p>
 * 核心不依赖任何容器类型，调用方把请求头与远端地址传入即可；
 * Servlet/Jakarta 适配由 {@code IJPay-Servlet-*} 模块提供。
 *
 * @author Javen
 */
public class IpKit {
	public IpKit() {
	}

	/**
	 * 获取客户端真实 IP。
	 *
	 * @param headers   请求头（大小写不敏感）
	 * @param remoteAddr 容器解析出的远端地址，通常为 {@code request.getRemoteAddr()}
	 * @return 客户端真实 IP
	 */
	public static String getRealIp(Map<String, String> headers, String remoteAddr) {
		String ip = getHeader(headers, "x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = getHeader(headers, "Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = getHeader(headers, "WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = remoteAddr;
		}

		// 对于通过多个代理的情况，第一个 IP 为客户端真实 IP，多个IP按照','分割
		if (ip != null && ip.length() > 15) {
			if (ip.indexOf(",") > 0) {
				ip = ip.substring(0, ip.indexOf(","));
			}
		}
		return ip;
	}

	/**
	 * 获取客户端真实 IP（简化版）。
	 *
	 * @param headers    请求头（大小写不敏感）
	 * @param remoteAddr 容器解析出的远端地址
	 * @return 客户端真实 IP
	 */
	public static String getRealIpV2(Map<String, String> headers, String remoteAddr) {
		String accessIp = getHeader(headers, "x-forwarded-for");
		return null == accessIp ? remoteAddr : accessIp;
	}

	private static String getHeader(Map<String, String> headers, String name) {
		if (headers == null) {
			return null;
		}
		String value = headers.get(name);
		if (value != null) {
			return value;
		}
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			if (entry.getKey() != null && entry.getKey().equalsIgnoreCase(name)) {
				return entry.getValue();
			}
		}
		return null;
	}
}
