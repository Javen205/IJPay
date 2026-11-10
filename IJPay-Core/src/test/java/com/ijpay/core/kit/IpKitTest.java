package com.ijpay.core.kit;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class IpKitTest {

	private static final String REMOTE_ADDR = "10.10.10.10";

	@Test
	public void getRealIpPrefersXForwardedFor() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("x-forwarded-for", "1.1.1.1");
		headers.put("Proxy-Client-IP", "2.2.2.2");
		headers.put("WL-Proxy-Client-IP", "3.3.3.3");

		Assert.assertEquals("1.1.1.1", IpKit.getRealIp(headers, REMOTE_ADDR));
	}

	@Test
	public void getRealIpFallsBackInOrder() {
		Map<String, String> proxyClient = new HashMap<String, String>();
		proxyClient.put("Proxy-Client-IP", "2.2.2.2");
		Assert.assertEquals("2.2.2.2", IpKit.getRealIp(proxyClient, REMOTE_ADDR));

		Map<String, String> wlProxy = new HashMap<String, String>();
		wlProxy.put("WL-Proxy-Client-IP", "3.3.3.3");
		Assert.assertEquals("3.3.3.3", IpKit.getRealIp(wlProxy, REMOTE_ADDR));
	}

	@Test
	public void getRealIpFallsBackToRemoteAddrWhenHeaderMissingOrUnknown() {
		Assert.assertEquals(REMOTE_ADDR, IpKit.getRealIp(new HashMap<String, String>(), REMOTE_ADDR));
		Assert.assertEquals(REMOTE_ADDR, IpKit.getRealIp(null, REMOTE_ADDR));

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("x-forwarded-for", "unknown");
		headers.put("Proxy-Client-IP", "UNKNOWN");
		headers.put("WL-Proxy-Client-IP", "Unknown");
		Assert.assertEquals(REMOTE_ADDR, IpKit.getRealIp(headers, REMOTE_ADDR));
	}

	@Test
	public void getRealIpHeaderNamesAreCaseInsensitive() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("X-Forwarded-For", "4.4.4.4");

		Assert.assertEquals("4.4.4.4", IpKit.getRealIp(headers, REMOTE_ADDR));
	}

	@Test
	public void getRealIpTakesFirstIpOfMultiProxyChain() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("x-forwarded-for", "192.168.1.1, 10.0.0.1, 172.16.0.1");

		Assert.assertEquals("192.168.1.1", IpKit.getRealIp(headers, REMOTE_ADDR));
	}

	@Test
	public void getRealIpKeepsShortValueWithComma() {
		// 长度不超过 15 时不做逗号截取，保持 2.x 行为
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("x-forwarded-for", "1.1.1.1,2.2.2.2");

		Assert.assertEquals("1.1.1.1,2.2.2.2", IpKit.getRealIp(headers, REMOTE_ADDR));
	}

	@Test
	public void getRealIpV2OnlyUsesXForwardedFor() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("x-forwarded-for", "5.5.5.5");
		headers.put("Proxy-Client-IP", "6.6.6.6");
		Assert.assertEquals("5.5.5.5", IpKit.getRealIpV2(headers, REMOTE_ADDR));

		Map<String, String> proxyOnly = new HashMap<String, String>();
		proxyOnly.put("Proxy-Client-IP", "6.6.6.6");
		Assert.assertEquals(REMOTE_ADDR, IpKit.getRealIpV2(proxyOnly, REMOTE_ADDR));
		Assert.assertEquals(REMOTE_ADDR, IpKit.getRealIpV2(null, REMOTE_ADDR));

		Map<String, String> emptyForwarded = new HashMap<String, String>();
		emptyForwarded.put("x-forwarded-for", "");
		Assert.assertEquals("", IpKit.getRealIpV2(emptyForwarded, REMOTE_ADDR));
	}
}
