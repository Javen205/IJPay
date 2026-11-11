package com.ijpay.servlet.javax;

import com.ijpay.core.kit.IpKit;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * javax.servlet 适配的 IP 工具。
 *
 * @author Javen
 */
public final class ServletIpKit {

	private ServletIpKit() {
	}

	/**
	 * 获取客户端真实 IP。
	 *
	 * @param request {@link HttpServletRequest}
	 * @return 客户端真实 IP
	 */
	public static String getRealIp(HttpServletRequest request) {
		return IpKit.getRealIp(headers(request), request.getRemoteAddr());
	}

	/**
	 * 获取客户端真实 IP（简化版）。
	 *
	 * @param request {@link HttpServletRequest}
	 * @return 客户端真实 IP
	 */
	public static String getRealIpV2(HttpServletRequest request) {
		return IpKit.getRealIpV2(headers(request), request.getRemoteAddr());
	}

	private static Map<String, String> headers(HttpServletRequest request) {
		// 只读取 IpKit 需要的请求头；Servlet 的 getHeader 本身大小写不敏感
		Map<String, String> headers = new HashMap<String, String>(4);
		headers.put("x-forwarded-for", request.getHeader("x-forwarded-for"));
		headers.put("Proxy-Client-IP", request.getHeader("Proxy-Client-IP"));
		headers.put("WL-Proxy-Client-IP", request.getHeader("WL-Proxy-Client-IP"));
		return headers;
	}
}
