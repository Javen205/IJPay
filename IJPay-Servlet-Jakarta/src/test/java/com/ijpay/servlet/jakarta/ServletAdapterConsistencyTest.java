package com.ijpay.servlet.jakarta;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

	@Test
	public void alipayServletApiExposesSameMethods() {
		Assert.assertEquals(publicSignatures(com.ijpay.servlet.javax.AliPayServletApi.class),
			publicSignatures(AliPayServletApi.class));
	}

	@Test
	public void alipayServletApiToMapIsConsistent() {
		Map<String, String> headers = new LinkedHashMap<String, String>();
		Map<String, String[]> params = new LinkedHashMap<String, String[]>();
		params.put("a", new String[]{"1", "2"});
		params.put("empty", new String[0]);
		params.put("nullValue", null);

		Assert.assertEquals(com.ijpay.servlet.javax.AliPayServletApi.toMap(javaxRequest(headers, params)),
			AliPayServletApi.toMap(jakartaRequest(headers, params)));
		Assert.assertEquals("1,2", AliPayServletApi.toMap(jakartaRequest(headers, params)).get("a"));
	}

	@Test
	public void alipayServletApiWapPayIsConsistent() throws Exception {
		AlipayRequest<?>[] javaxCaptured = new AlipayRequest<?>[1];
		AlipayRequest<?>[] jakartaCaptured = new AlipayRequest<?>[1];
		ResponseCapture javaxCapture = new ResponseCapture();
		ResponseCapture jakartaCapture = new ResponseCapture();
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();

		com.ijpay.servlet.javax.AliPayServletApi.wapPay(alipayClient(javaxCaptured),
			(javax.servlet.http.HttpServletResponse) javaxCapture.proxy(javax.servlet.http.HttpServletResponse.class),
			model, "https://merchant/return", "https://merchant/notify");
		AliPayServletApi.wapPay(alipayClient(jakartaCaptured),
			(jakarta.servlet.http.HttpServletResponse) jakartaCapture.proxy(jakarta.servlet.http.HttpServletResponse.class),
			model, "https://merchant/return", "https://merchant/notify");

		Assert.assertEquals("text/html;charset=UTF-8", jakartaCapture.contentType);
		Assert.assertEquals(javaxCapture.contentType, jakartaCapture.contentType);
		Assert.assertEquals("FORM", jakartaCapture.body.toString());
		Assert.assertEquals(javaxCapture.body.toString(), jakartaCapture.body.toString());
		AlipayTradeWapPayRequest request = (AlipayTradeWapPayRequest) jakartaCaptured[0];
		Assert.assertEquals("https://merchant/return", request.getReturnUrl());
		Assert.assertEquals("https://merchant/notify", request.getNotifyUrl());
	}

	private static Set<String> publicSignatures(Class<?> type) {
		Set<String> signatures = new TreeSet<String>();
		for (Method method : type.getDeclaredMethods()) {
			if (!Modifier.isPublic(method.getModifiers())) {
				continue;
			}
			StringBuilder signature = new StringBuilder(method.getName()).append('(');
			Class<?>[] parameterTypes = method.getParameterTypes();
			for (int i = 0; i < parameterTypes.length; i++) {
				if (i > 0) {
					signature.append(',');
				}
				signature.append(normalize(parameterTypes[i].getName()));
			}
			signatures.add(signature.append(')').toString());
		}
		return signatures;
	}

	private static String normalize(String typeName) {
		return typeName.replace("javax.servlet.", "servlet.").replace("jakarta.servlet.", "servlet.");
	}

	private static AlipayClient alipayClient(final AlipayRequest<?>[] captured) {
		return (AlipayClient) Proxy.newProxyInstance(
			ServletAdapterConsistencyTest.class.getClassLoader(),
			new Class<?>[]{AlipayClient.class},
			new InvocationHandler() {
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) {
					if ("pageExecute".equals(method.getName()) && args != null && args.length >= 1) {
						captured[0] = (AlipayRequest<?>) args[0];
						AlipayTradeWapPayResponse response = new AlipayTradeWapPayResponse();
						response.setBody("FORM");
						return response;
					}
					return defaultValue(method.getReturnType());
				}
			});
	}

	private static final class ResponseCapture implements InvocationHandler {

		private final StringWriter body = new StringWriter();
		private final PrintWriter writer = new PrintWriter(body);
		private String contentType;

		private Object proxy(Class<?> type) {
			return Proxy.newProxyInstance(ServletAdapterConsistencyTest.class.getClassLoader(),
				new Class<?>[]{type}, this);
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) {
			String name = method.getName();
			if ("setContentType".equals(name)) {
				contentType = (String) args[0];
				return null;
			}
			if ("getWriter".equals(name)) {
				return writer;
			}
			if ("toString".equals(name)) {
				return "ResponseCapture";
			}
			if ("hashCode".equals(name)) {
				return System.identityHashCode(proxy);
			}
			if ("equals".equals(name)) {
				return proxy == args[0];
			}
			return defaultValue(method.getReturnType());
		}
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
