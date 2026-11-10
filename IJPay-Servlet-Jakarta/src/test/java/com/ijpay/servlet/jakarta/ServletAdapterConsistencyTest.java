package com.ijpay.servlet.jakarta;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 校验 javax 与 jakarta 两套适配实现面对同样的请求输入时输出完全一致。
 */
public class ServletAdapterConsistencyTest {

	private static final String REMOTE_ADDR = "10.10.10.10";
	private static final String BODY = "{\"a\":1}\n{\"b\":2}";

	@Test
	public void readDataIsConsistentAcrossVariants() {
		Map<String, String> headers = new LinkedHashMap<String, String>();
		Map<String, String[]> params = new LinkedHashMap<String, String[]>();

		Assert.assertEquals(com.ijpay.servlet.javax.ServletHttpKit.readData(javaxRequest(headers, params)),
			ServletHttpKit.readData(jakartaRequest(headers, params)));
	}

	@Test
	public void toMapIsConsistentAcrossVariants() {
		Map<String, String> headers = new LinkedHashMap<String, String>();
		Map<String, String[]> params = new LinkedHashMap<String, String[]>();
		params.put("a", new String[]{"1", "2"});
		params.put("b", new String[]{"3"});
		params.put("empty", new String[0]);
		params.put("nullValue", null);

		Assert.assertEquals(com.ijpay.servlet.javax.ServletHttpKit.toMap(javaxRequest(headers, params)),
			ServletHttpKit.toMap(jakartaRequest(headers, params)));
	}

	@Test
	public void getRealIpIsConsistentAcrossVariants() {
		Map<String, String> headers = new LinkedHashMap<String, String>();
		headers.put("X-Forwarded-For", "192.168.1.1, 10.0.0.1");
		headers.put("Proxy-Client-IP", "2.2.2.2");
		headers.put("WL-Proxy-Client-IP", "3.3.3.3");
		Map<String, String[]> params = new LinkedHashMap<String, String[]>();

		Assert.assertEquals(
			com.ijpay.servlet.javax.ServletIpKit.getRealIp(javaxRequest(headers, params)),
			ServletIpKit.getRealIp(jakartaRequest(headers, params)));
		Assert.assertEquals("192.168.1.1",
			ServletIpKit.getRealIp(jakartaRequest(headers, params)));
	}

	@Test
	public void getRealIpV2IsConsistentAcrossVariants() {
		Map<String, String> headers = new LinkedHashMap<String, String>();
		headers.put("X-Forwarded-For", "5.5.5.5");
		Map<String, String[]> params = new LinkedHashMap<String, String[]>();

		Assert.assertEquals(
			com.ijpay.servlet.javax.ServletIpKit.getRealIpV2(javaxRequest(headers, params)),
			ServletIpKit.getRealIpV2(jakartaRequest(headers, params)));
	}

	private static javax.servlet.http.HttpServletRequest javaxRequest(Map<String, String> headers,
																	 Map<String, String[]> params) {
		return (javax.servlet.http.HttpServletRequest) Proxy.newProxyInstance(
			ServletAdapterConsistencyTest.class.getClassLoader(),
			new Class<?>[]{javax.servlet.http.HttpServletRequest.class},
			new StubHandler(headers, params));
	}

	private static jakarta.servlet.http.HttpServletRequest jakartaRequest(Map<String, String> headers,
																		 Map<String, String[]> params) {
		return (jakarta.servlet.http.HttpServletRequest) Proxy.newProxyInstance(
			ServletAdapterConsistencyTest.class.getClassLoader(),
			new Class<?>[]{jakarta.servlet.http.HttpServletRequest.class},
			new StubHandler(headers, params));
	}

	private static final class StubHandler implements InvocationHandler {

		private final Map<String, String> headers;
		private final Map<String, String[]> params;

		private StubHandler(Map<String, String> headers, Map<String, String[]> params) {
			this.headers = headers;
			this.params = params;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) {
			String name = method.getName();
			if ("getHeader".equals(name)) {
				return headerValue((String) args[0]);
			}
			if ("getHeaderNames".equals(name)) {
				return Collections.enumeration(headers.keySet());
			}
			if ("getRemoteAddr".equals(name)) {
				return REMOTE_ADDR;
			}
			if ("getReader".equals(name)) {
				return new BufferedReader(new StringReader(BODY));
			}
			if ("getParameterMap".equals(name)) {
				return params;
			}
			if ("toString".equals(name)) {
				return "ServletAdapterConsistencyTest";
			}
			if ("hashCode".equals(name)) {
				return System.identityHashCode(proxy);
			}
			if ("equals".equals(name)) {
				return proxy == args[0];
			}
			return defaultValue(method.getReturnType());
		}

		private String headerValue(String name) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				if (entry.getKey().equalsIgnoreCase(name)) {
					return entry.getValue();
				}
			}
			return null;
		}
	}

	private static Object defaultValue(Class<?> type) {
		if (!type.isPrimitive()) {
			return null;
		}
		if (type == boolean.class) {
			return false;
		}
		if (type == char.class) {
			return (char) 0;
		}
		if (type == byte.class) {
			return (byte) 0;
		}
		if (type == short.class) {
			return (short) 0;
		}
		if (type == int.class) {
			return 0;
		}
		if (type == long.class) {
			return 0L;
		}
		if (type == float.class) {
			return 0F;
		}
		return 0D;
	}
}
