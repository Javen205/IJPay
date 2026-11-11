package com.ijpay.servlet.javax;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 校验 {@link AliPayServletApi} 的响应写出语义、参数顺序与重定向行为。
 */
public class AliPayServletApiTest {

	private static final String FORM_BODY = "<form>pay</form>";

	@Test
	public void wapPayWritesUtf8FormToWriter() throws Exception {
		AlipayRequest<?>[] captured = new AlipayRequest<?>[1];
		ServletResponseStub response = new ServletResponseStub();
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();

		AliPayServletApi.wapPay(alipayClient(captured, FORM_BODY), response.build(), model,
			"https://merchant/return", "https://merchant/notify");

		Assert.assertEquals("text/html;charset=UTF-8", response.getContentType());
		Assert.assertEquals(FORM_BODY, response.getBody());
		Assert.assertTrue("写完后应关闭 PrintWriter", response.isWriterClosed());
		AlipayTradeWapPayRequest request = (AlipayTradeWapPayRequest) captured[0];
		Assert.assertEquals("https://merchant/return", request.getReturnUrl());
		Assert.assertEquals("https://merchant/notify", request.getNotifyUrl());
		Assert.assertSame(model, request.getBizModel());
	}

	@Test
	public void wapPayByOutputStreamWritesUtf8Bytes() throws Exception {
		AlipayRequest<?>[] captured = new AlipayRequest<?>[1];
		ServletResponseStub response = new ServletResponseStub();

		AliPayServletApi.wapPayByOutputStream(alipayClient(captured, FORM_BODY), response.build(),
			new AlipayTradeWapPayModel(), "https://merchant/return", "https://merchant/notify");

		Assert.assertEquals("text/html;charset=UTF-8", response.getContentType());
		Assert.assertEquals(FORM_BODY, new String(response.getOutputStreamBytes(), "UTF-8"));
	}

	@Test
	public void tradePageKeepsNotifyAndReturnUrlOrder() throws Exception {
		AlipayRequest<?>[] captured = new AlipayRequest<?>[1];
		ServletResponseStub response = new ServletResponseStub();
		AlipayTradePagePayModel model = new AlipayTradePagePayModel();

		AliPayServletApi.tradePage(alipayClient(captured, FORM_BODY), response.build(), model,
			"https://merchant/notify", "https://merchant/return");

		Assert.assertEquals("text/html;charset=UTF-8", response.getContentType());
		Assert.assertEquals(FORM_BODY, response.getBody());
		AlipayTradePagePayRequest request = (AlipayTradePagePayRequest) captured[0];
		Assert.assertEquals("https://merchant/notify", request.getNotifyUrl());
		Assert.assertEquals("https://merchant/return", request.getReturnUrl());
		Assert.assertSame(model, request.getBizModel());
	}

	@Test
	public void batchTransRedirectsToSignedGatewayUrl() throws Exception {
		Map<String, String> params = new LinkedHashMap<String, String>();
		params.put("batch_no", "20261111001");
		ServletResponseStub response = new ServletResponseStub();

		AliPayServletApi.batchTrans(params, generatePrivateKey(), "RSA2", response.build());

		String url = response.getRedirect();
		Assert.assertNotNull("batchTrans 必须调用 sendRedirect", url);
		Assert.assertTrue(url.startsWith("https://mapi.alipay.com/gateway.do?"));
		Assert.assertTrue(url.contains("service=batch_trans_notify"));
		Assert.assertTrue(url.contains("_input_charset=UTF-8"));
		Assert.assertTrue(url.contains("batch_no=20261111001"));
		Assert.assertTrue("跳转地址必须带签名", url.contains("sign="));
		Assert.assertEquals("batch_trans_notify", params.get("service"));
	}

	@Test
	public void toMapConvertsServletParameterMap() {
		HttpServletRequest request = new ServletRequestStub()
			.parameter("a", "1", "2")
			.parameter("empty")
			.nullParameter("nullValue")
			.build();

		Map<String, String> result = AliPayServletApi.toMap(request);

		Assert.assertEquals("1,2", result.get("a"));
		Assert.assertEquals("", result.get("empty"));
		Assert.assertEquals("", result.get("nullValue"));
	}

	private static AlipayClient alipayClient(final AlipayRequest<?>[] captured, final String body) {
		return (AlipayClient) Proxy.newProxyInstance(
			AliPayServletApiTest.class.getClassLoader(),
			new Class<?>[]{AlipayClient.class},
			new InvocationHandler() {
				@Override
				public Object invoke(Object proxy, Method method, Object[] args) {
					if ("pageExecute".equals(method.getName()) && args != null && args.length >= 1) {
						captured[0] = (AlipayRequest<?>) args[0];
						AlipayResponse response = captured[0] instanceof AlipayTradePagePayRequest
							? new AlipayTradePagePayResponse() : new AlipayTradeWapPayResponse();
						response.setBody(body);
						return response;
					}
					return defaultValue(method.getReturnType());
				}
			});
	}

	private static String generatePrivateKey() throws Exception {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		KeyPair keyPair = generator.generateKeyPair();
		return Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
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
