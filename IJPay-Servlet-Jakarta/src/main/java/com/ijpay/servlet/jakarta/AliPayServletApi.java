package com.ijpay.servlet.jakarta;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfigKit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 支付宝页面支付与批量转账的 jakarta.servlet 适配。
 * <p>
 * 3.0.0 起这些方法从 {@link AliPayApi} 迁出：核心模块不再依赖任何 Servlet API。
 * 新代码建议直接使用容器无关的 {@link AliPayApi#wapPayStr}、
 * {@link AliPayApi#tradePage}、{@link AliPayApi#batchTransUrl}，自行写响应或跳转。
 *
 * @author Javen
 */
public final class AliPayServletApi {

	private AliPayServletApi() {
	}

	/**
	 * WAP支付。
	 *
	 * @param response  {@link HttpServletResponse}
	 * @param model     {@link AlipayTradeWapPayModel}
	 * @param returnUrl 同步通知URL
	 * @param notifyUrl 异步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPay(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
		write(response, configCharset(), AliPayApi.wapPayStr(model, returnUrl, notifyUrl));
	}

	/**
	 * WAP支付。
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    同步通知URL
	 * @param notifyUrl    异步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPay(AlipayClient alipayClient, HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
		write(response, "UTF-8", AliPayApi.wapPayStr(alipayClient, model, returnUrl, notifyUrl));
	}

	/**
	 * WAP支付。
	 *
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    同步通知URL
	 * @param notifyUrl    异步通知URL
	 * @param appAuthToken 应用授权token
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPay(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl, String appAuthToken) throws AlipayApiException, IOException {
		write(response, configCharset(), AliPayApi.wapPayStr(model, returnUrl, notifyUrl, appAuthToken));
	}

	/**
	 * WAP支付。
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    同步通知URL
	 * @param notifyUrl    异步通知URL
	 * @param appAuthToken 应用授权token
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPay(AlipayClient alipayClient, HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl, String appAuthToken) throws AlipayApiException, IOException {
		write(response, "UTF-8", AliPayApi.wapPayStr(alipayClient, model, returnUrl, notifyUrl, appAuthToken));
	}

	/**
	 * WAP支付：使用 OutputStream 输出，避免 Filter 中 getOutputStream() 与 getWriter() 冲突。
	 *
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    同步通知URL
	 * @param notifyUrl    异步通知URL
	 * @param appAuthToken 应用授权token
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPayByOutputStream(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl, String appAuthToken)
		throws AlipayApiException, IOException {
		String charset = configCharset();
		writeByOutputStream(response, charset, AliPayApi.wapPayStr(model, returnUrl, notifyUrl, appAuthToken), charset);
	}

	/**
	 * WAP支付：使用 OutputStream 输出。
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    同步通知URL
	 * @param notifyUrl    异步通知URL
	 * @param appAuthToken 应用授权token
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPayByOutputStream(AlipayClient alipayClient, HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl, String appAuthToken)
		throws AlipayApiException, IOException {
		writeByOutputStream(response, "UTF-8", AliPayApi.wapPayStr(alipayClient, model, returnUrl, notifyUrl, appAuthToken), "UTF-8");
	}

	/**
	 * WAP支付：使用 OutputStream 输出。
	 *
	 * @param response  {@link HttpServletResponse}
	 * @param model     {@link AlipayTradeWapPayModel}
	 * @param returnUrl 同步通知URL
	 * @param notifyUrl 异步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPayByOutputStream(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
		String charset = configCharset();
		writeByOutputStream(response, charset, AliPayApi.wapPayStr(model, returnUrl, notifyUrl), charset);
	}

	/**
	 * WAP支付：使用 OutputStream 输出。
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    同步通知URL
	 * @param notifyUrl    异步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPayByOutputStream(AlipayClient alipayClient, HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
		writeByOutputStream(response, "UTF-8", AliPayApi.wapPayStr(alipayClient, model, returnUrl, notifyUrl), "UTF-8");
	}

	/**
	 * 电脑网站支付(PC支付)。
	 *
	 * @param response  {@link HttpServletResponse}
	 * @param model     {@link AlipayTradePagePayModel}
	 * @param notifyUrl 异步通知URL
	 * @param returnUrl 同步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePage(HttpServletResponse response, AlipayTradePagePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException {
		write(response, configCharset(), AliPayApi.tradePage(model, notifyUrl, returnUrl));
	}

	/**
	 * 电脑网站支付(PC支付)。
	 *
	 * @param response  {@link HttpServletResponse}
	 * @param method    GET/POST
	 * @param model     {@link AlipayTradePagePayModel}
	 * @param notifyUrl 异步通知URL
	 * @param returnUrl 同步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePage(HttpServletResponse response, String method, AlipayTradePagePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException {
		write(response, configCharset(), AliPayApi.tradePage(method, model, notifyUrl, returnUrl));
	}

	/**
	 * 电脑网站支付(PC支付)。
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradePagePayModel}
	 * @param notifyUrl    异步通知URL
	 * @param returnUrl    同步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePage(AlipayClient alipayClient, HttpServletResponse response, AlipayTradePagePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException {
		write(response, "UTF-8", AliPayApi.tradePage(alipayClient, model, notifyUrl, returnUrl));
	}

	/**
	 * 电脑网站支付(PC支付)。
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param method       GET/POST
	 * @param model        {@link AlipayTradePagePayModel}
	 * @param notifyUrl    异步通知URL
	 * @param returnUrl    同步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePage(AlipayClient alipayClient, HttpServletResponse response, String method, AlipayTradePagePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException {
		write(response, "UTF-8", AliPayApi.tradePage(alipayClient, method, model, notifyUrl, returnUrl));
	}

	/**
	 * 电脑网站支付(PC支付)。
	 *
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradePagePayModel}
	 * @param notifyUrl    异步通知URL
	 * @param returnUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePage(HttpServletResponse response, AlipayTradePagePayModel model, String notifyUrl, String returnUrl, String appAuthToken) throws AlipayApiException, IOException {
		write(response, configCharset(), AliPayApi.tradePage(model, notifyUrl, returnUrl, appAuthToken));
	}

	/**
	 * 电脑网站支付(PC支付)。
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradePagePayModel}
	 * @param notifyUrl    异步通知URL
	 * @param returnUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePage(AlipayClient alipayClient, HttpServletResponse response, AlipayTradePagePayModel model, String notifyUrl, String returnUrl, String appAuthToken) throws AlipayApiException, IOException {
		write(response, "UTF-8", AliPayApi.tradePage(alipayClient, model, notifyUrl, returnUrl, appAuthToken));
	}

	/**
	 * 电脑网站支付(PC支付)：使用 OutputStream 输出。
	 *
	 * @param response  {@link HttpServletResponse}
	 * @param model     {@link AlipayTradePagePayModel}
	 * @param notifyUrl 异步通知URL
	 * @param returnUrl 同步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePageByOutputStream(HttpServletResponse response, AlipayTradePagePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException {
		String charset = configCharset();
		writeByOutputStream(response, charset, AliPayApi.tradePage(model, notifyUrl, returnUrl), charset);
	}

	/**
	 * 电脑网站支付(PC支付)：使用 OutputStream 输出。
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradePagePayModel}
	 * @param notifyUrl    异步通知URL
	 * @param returnUrl    同步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePageByOutputStream(AlipayClient alipayClient, HttpServletResponse response, AlipayTradePagePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException {
		writeByOutputStream(response, "UTF-8", AliPayApi.tradePage(alipayClient, model, notifyUrl, returnUrl), "UTF-8");
	}

	/**
	 * 电脑网站支付(PC支付)：使用 OutputStream 输出。
	 *
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradePagePayModel}
	 * @param notifyUrl    异步通知URL
	 * @param returnUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePageByOutputStream(HttpServletResponse response, AlipayTradePagePayModel model, String notifyUrl, String returnUrl, String appAuthToken)
		throws AlipayApiException, IOException {
		String charset = configCharset();
		writeByOutputStream(response, charset, AliPayApi.tradePage(model, notifyUrl, returnUrl, appAuthToken), charset);
	}

	/**
	 * 电脑网站支付(PC支付)：使用 OutputStream 输出。
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradePagePayModel}
	 * @param notifyUrl    异步通知URL
	 * @param returnUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePageByOutputStream(AlipayClient alipayClient, HttpServletResponse response, AlipayTradePagePayModel model, String notifyUrl, String returnUrl, String appAuthToken)
		throws AlipayApiException, IOException {
		writeByOutputStream(response, "UTF-8", AliPayApi.tradePage(alipayClient, model, notifyUrl, returnUrl, appAuthToken), "UTF-8");
	}

	/**
	 * 批量转账：直接重定向到支付宝。
	 *
	 * @param params     参数
	 * @param privateKey 商户私钥
	 * @param signType   签名类型
	 * @param response   {@link HttpServletResponse}
	 * @throws IOException IO 异常
	 */
	public static void batchTrans(Map<String, String> params, String privateKey, String signType, HttpServletResponse response) throws IOException {
		response.sendRedirect(AliPayApi.batchTransUrl(params, privateKey, signType));
	}

	/**
	 * 将异步通知的参数转化为Map。
	 *
	 * @param request {@link HttpServletRequest}
	 * @return 转化后的Map
	 */
	public static Map<String, String> toMap(HttpServletRequest request) {
		return AliPayApi.toMap(request.getParameterMap());
	}

	private static String configCharset() {
		return AliPayApiConfigKit.getAliPayApiConfig().getCharset();
	}

	private static void write(HttpServletResponse response, String charset, String form) throws IOException {
		response.setContentType("text/html;charset=" + charset);
		PrintWriter out = response.getWriter();
		out.write(form);
		out.flush();
		out.close();
	}

	private static void writeByOutputStream(HttpServletResponse response, String contentTypeCharset, String form, String charset) throws IOException {
		response.setContentType("text/html;charset=" + contentTypeCharset);
		OutputStream out = response.getOutputStream();
		out.write(form.getBytes(charset));
		response.getOutputStream().flush();
	}
}
