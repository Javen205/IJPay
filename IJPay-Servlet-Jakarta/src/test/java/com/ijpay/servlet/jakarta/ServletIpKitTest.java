package com.ijpay.servlet.jakarta;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ServletIpKitTest {

	@Test
	public void getRealIpUsesStandardHeaderOrder() {
		Assert.assertEquals("1.1.1.1", ServletIpKit.getRealIp(new ServletRequestStub()
			.header("x-forwarded-for", "1.1.1.1")
			.header("Proxy-Client-IP", "2.2.2.2")
			.header("WL-Proxy-Client-IP", "3.3.3.3")
			.remoteAddr("9.9.9.9")
			.build()));

		Assert.assertEquals("2.2.2.2", ServletIpKit.getRealIp(new ServletRequestStub()
			.header("Proxy-Client-IP", "2.2.2.2")
			.header("WL-Proxy-Client-IP", "3.3.3.3")
			.remoteAddr("9.9.9.9")
			.build()));

		Assert.assertEquals("3.3.3.3", ServletIpKit.getRealIp(new ServletRequestStub()
			.header("WL-Proxy-Client-IP", "3.3.3.3")
			.remoteAddr("9.9.9.9")
			.build()));

		Assert.assertEquals("9.9.9.9", ServletIpKit.getRealIp(new ServletRequestStub()
			.remoteAddr("9.9.9.9")
			.build()));
	}

	@Test
	public void getRealIpHeaderLookupIsCaseInsensitive() {
		Assert.assertEquals("4.4.4.4", ServletIpKit.getRealIp(new ServletRequestStub()
			.header("X-Forwarded-For", "4.4.4.4")
			.build()));
	}

	@Test
	public void getRealIpOnlyReadsRequiredHeaders() {
		ServletRequestStub stub = new ServletRequestStub()
			.header("x-forwarded-for", "1.1.1.1")
			.header("User-Agent", "test")
			.forbidGetHeaderNames();

		Assert.assertEquals("1.1.1.1", ServletIpKit.getRealIp(stub.build()));

		List<String> requested = stub.getRequestedHeaders();
		Assert.assertFalse("不应读取无关请求头：" + requested, requested.contains("User-Agent"));
	}

	@Test
	public void getRealIpV2FallsBackToRemoteAddr() {
		Assert.assertEquals("5.5.5.5", ServletIpKit.getRealIpV2(new ServletRequestStub()
			.header("X-Forwarded-For", "5.5.5.5")
			.remoteAddr("9.9.9.9")
			.build()));

		Assert.assertEquals("9.9.9.9", ServletIpKit.getRealIpV2(new ServletRequestStub()
			.remoteAddr("9.9.9.9")
			.build()));
	}
}
