package com.ijpay.demo.kit;

import org.noear.solon.core.handle.Context;

/**
 * @author Javen
 */
public class IpKit {
	private final static String UNKNOWN = "unknown";

	public static String getRealIp(Context ctx) {
		return ctx.realIp();
	}

	public static String getRealIpV2(Context ctx) {
		String accessIp = ctx.header("x-forwarded-for");
		if (null == accessIp) {
			return ctx.ip();
		}

		return accessIp;
	}
}
