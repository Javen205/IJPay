package com.ijpay.alipay;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.BatchAlipayRequest;
import com.alipay.api.BatchAlipayResponse;
import com.alipay.api.domain.AlipayCommerceCityfacilitatorStationQueryModel;
import com.alipay.api.domain.AlipayCommerceCityfacilitatorVoucherBatchqueryModel;
import com.alipay.api.domain.AlipayCommerceCityfacilitatorVoucherGenerateModel;
import com.alipay.api.domain.AlipayCommerceCityfacilitatorVoucherRefundModel;
import com.alipay.api.domain.AlipayDataDataserviceBillDownloadurlQueryModel;
import com.alipay.api.domain.AlipayFundAccountQueryModel;
import com.alipay.api.domain.AlipayFundAuthOperationCancelModel;
import com.alipay.api.domain.AlipayFundAuthOperationDetailQueryModel;
import com.alipay.api.domain.AlipayFundAuthOrderFreezeModel;
import com.alipay.api.domain.AlipayFundAuthOrderUnfreezeModel;
import com.alipay.api.domain.AlipayFundAuthOrderVoucherCreateModel;
import com.alipay.api.domain.AlipayFundCouponOperationQueryModel;
import com.alipay.api.domain.AlipayFundCouponOrderAgreementPayModel;
import com.alipay.api.domain.AlipayFundCouponOrderAppPayModel;
import com.alipay.api.domain.AlipayFundCouponOrderDisburseModel;
import com.alipay.api.domain.AlipayFundCouponOrderPagePayModel;
import com.alipay.api.domain.AlipayFundCouponOrderRefundModel;
import com.alipay.api.domain.AlipayFundTransCommonQueryModel;
import com.alipay.api.domain.AlipayFundTransOrderQueryModel;
import com.alipay.api.domain.AlipayFundTransToaccountTransferModel;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayOpenAuthTokenAppModel;
import com.alipay.api.domain.AlipayOpenAuthTokenAppQueryModel;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeCancelModel;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeOrderSettleModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePageRefundModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationBatchqueryModel;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationBindModel;
import com.alipay.api.domain.AlipayTradeRoyaltyRelationUnbindModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.domain.ZolozAuthenticationCustomerFacemanageCreateModel;
import com.alipay.api.domain.ZolozAuthenticationCustomerFacemanageDeleteModel;
import com.alipay.api.domain.ZolozAuthenticationCustomerFtokenQueryModel;
import com.alipay.api.domain.ZolozAuthenticationCustomerSmilepayInitializeModel;
import com.alipay.api.domain.ZolozAuthenticationSmilepayInitializeModel;
import com.alipay.api.domain.ZolozIdentificationUserWebInitializeModel;
import com.alipay.api.domain.ZolozIdentificationUserWebQueryModel;
import com.alipay.api.internal.util.StringUtils;
import com.alipay.api.request.AlipayCommerceAdContractSignRequest;
import com.alipay.api.request.AlipayCommerceCityfacilitatorStationQueryRequest;
import com.alipay.api.request.AlipayCommerceCityfacilitatorVoucherBatchqueryRequest;
import com.alipay.api.request.AlipayCommerceCityfacilitatorVoucherGenerateRequest;
import com.alipay.api.request.AlipayCommerceCityfacilitatorVoucherRefundRequest;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
import com.alipay.api.request.AlipayEbppBillGetRequest;
import com.alipay.api.request.AlipayFundAccountQueryRequest;
import com.alipay.api.request.AlipayFundAuthOperationCancelRequest;
import com.alipay.api.request.AlipayFundAuthOperationDetailQueryRequest;
import com.alipay.api.request.AlipayFundAuthOrderFreezeRequest;
import com.alipay.api.request.AlipayFundAuthOrderUnfreezeRequest;
import com.alipay.api.request.AlipayFundAuthOrderVoucherCreateRequest;
import com.alipay.api.request.AlipayFundCouponOperationQueryRequest;
import com.alipay.api.request.AlipayFundCouponOrderAgreementPayRequest;
import com.alipay.api.request.AlipayFundCouponOrderAppPayRequest;
import com.alipay.api.request.AlipayFundCouponOrderDisburseRequest;
import com.alipay.api.request.AlipayFundCouponOrderPagePayRequest;
import com.alipay.api.request.AlipayFundCouponOrderRefundRequest;
import com.alipay.api.request.AlipayFundTransCommonQueryRequest;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayFundTransUniTransferRequest;
import com.alipay.api.request.AlipayOpenAuthTokenAppQueryRequest;
import com.alipay.api.request.AlipayOpenAuthTokenAppRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeOrderSettleRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePageRefundRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeRoyaltyRelationBatchqueryRequest;
import com.alipay.api.request.AlipayTradeRoyaltyRelationBindRequest;
import com.alipay.api.request.AlipayTradeRoyaltyRelationUnbindRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.request.ZolozAuthenticationCustomerFacemanageCreateRequest;
import com.alipay.api.request.ZolozAuthenticationCustomerFacemanageDeleteRequest;
import com.alipay.api.request.ZolozAuthenticationCustomerFtokenQueryRequest;
import com.alipay.api.request.ZolozAuthenticationCustomerSmilepayInitializeRequest;
import com.alipay.api.request.ZolozAuthenticationSmilepayInitializeRequest;
import com.alipay.api.request.ZolozIdentificationUserWebInitializeRequest;
import com.alipay.api.request.ZolozIdentificationUserWebQueryRequest;
import com.alipay.api.response.AlipayCommerceAdContractSignResponse;
import com.alipay.api.response.AlipayCommerceCityfacilitatorStationQueryResponse;
import com.alipay.api.response.AlipayCommerceCityfacilitatorVoucherBatchqueryResponse;
import com.alipay.api.response.AlipayCommerceCityfacilitatorVoucherGenerateResponse;
import com.alipay.api.response.AlipayCommerceCityfacilitatorVoucherRefundResponse;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
import com.alipay.api.response.AlipayEbppBillGetResponse;
import com.alipay.api.response.AlipayFundAccountQueryResponse;
import com.alipay.api.response.AlipayFundAuthOperationCancelResponse;
import com.alipay.api.response.AlipayFundAuthOperationDetailQueryResponse;
import com.alipay.api.response.AlipayFundAuthOrderFreezeResponse;
import com.alipay.api.response.AlipayFundAuthOrderUnfreezeResponse;
import com.alipay.api.response.AlipayFundAuthOrderVoucherCreateResponse;
import com.alipay.api.response.AlipayFundCouponOperationQueryResponse;
import com.alipay.api.response.AlipayFundCouponOrderAgreementPayResponse;
import com.alipay.api.response.AlipayFundCouponOrderAppPayResponse;
import com.alipay.api.response.AlipayFundCouponOrderDisburseResponse;
import com.alipay.api.response.AlipayFundCouponOrderPagePayResponse;
import com.alipay.api.response.AlipayFundCouponOrderRefundResponse;
import com.alipay.api.response.AlipayFundTransCommonQueryResponse;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayFundTransUniTransferResponse;
import com.alipay.api.response.AlipayOpenAuthTokenAppQueryResponse;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import com.alipay.api.response.AlipayTradePageRefundResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.api.response.AlipayTradeRoyaltyRelationBatchqueryResponse;
import com.alipay.api.response.AlipayTradeRoyaltyRelationBindResponse;
import com.alipay.api.response.AlipayTradeRoyaltyRelationUnbindResponse;
import com.alipay.api.response.ZolozAuthenticationCustomerFacemanageCreateResponse;
import com.alipay.api.response.ZolozAuthenticationCustomerFacemanageDeleteResponse;
import com.alipay.api.response.ZolozAuthenticationCustomerFtokenQueryResponse;
import com.alipay.api.response.ZolozAuthenticationCustomerSmilepayInitializeResponse;
import com.alipay.api.response.ZolozAuthenticationSmilepayInitializeResponse;
import com.alipay.api.response.ZolozIdentificationUserWebInitializeResponse;
import com.alipay.api.response.ZolozIdentificationUserWebQueryResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。
 * </p>
 *
 * <p>
 * 不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。
 * </p>
 *
 * <p>
 * IJPay 交流群: 723992875、864988890
 * </p>
 *
 * <p>
 * Node.js 版: https://gitee.com/javen205/TNWX
 * </p>
 *
 * <p>
 * 支付宝支付相关接口
 * </p>
 *
 * @author Javen
 */
public class AliPayApi {

	/**
	 * 支付宝提供给商户的服务接入网关URL(新)
	 */
	private static final String GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";

	public static <T extends AlipayResponse> T doExecute(AlipayRequest<T> request) throws AlipayApiException {
		if (AliPayApiConfigKit.getAliPayApiConfig().isCertModel()) {
			return certificateExecute(request);
		} else {
			return execute(request);
		}
	}

	public static <T extends AlipayResponse> T doExecute(AlipayClient alipayClient, Boolean certModel, AlipayRequest<T> request) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		if (certModel) {
			return certificateExecute(alipayClient, request);
		} else {
			return execute(alipayClient, request);
		}
	}


	public static <T extends AlipayResponse> T doExecute(AlipayClient alipayClient, Boolean certModel, AlipayRequest<T> request, String authToken) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		if (certModel) {
			return certificateExecute(alipayClient, request, authToken);
		} else {
			return execute(alipayClient, request, authToken);
		}
	}


	public static <T extends AlipayResponse> T doExecute(AlipayRequest<T> request, String authToken) throws AlipayApiException {
		if (AliPayApiConfigKit.getAliPayApiConfig().isCertModel()) {
			return certificateExecute(request, authToken);
		} else {
			return execute(request, authToken);
		}
	}


	public static <T extends AlipayResponse> T doExecute(AlipayClient alipayClient, AlipayRequest<T> request, String authToken) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		if (AliPayApiConfigKit.getAliPayApiConfig().isCertModel()) {
			return certificateExecute(alipayClient, request, authToken);
		} else {
			return execute(alipayClient, request, authToken);
		}
	}

	public static <T extends AlipayResponse> T execute(AlipayRequest<T> request) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
	}


	public static <T extends AlipayResponse> T execute(AlipayClient alipayClient, AlipayRequest<T> request) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.execute(request);
	}

	public static <T extends AlipayResponse> T execute(AlipayRequest<T> request, String authToken) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, authToken);
	}

	public static <T extends AlipayResponse> T execute(AlipayClient alipayClient, AlipayRequest<T> request, String authToken) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.execute(request, authToken);
	}

	public static <T extends AlipayResponse> T execute(AlipayRequest<T> request, String accessToken, String appAuthToken) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, accessToken, appAuthToken);
	}

	public static <T extends AlipayResponse> T execute(AlipayClient alipayClient, AlipayRequest<T> request, String accessToken, String appAuthToken) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.execute(request, accessToken, appAuthToken);
	}

	public static <T extends AlipayResponse> T execute(AlipayRequest<T> request, String accessToken, String appAuthToken, String targetAppId) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, accessToken, appAuthToken, targetAppId);
	}

	public static <T extends AlipayResponse> T execute(AlipayClient alipayClient, AlipayRequest<T> request, String accessToken, String appAuthToken, String targetAppId) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.execute(request, accessToken, appAuthToken, targetAppId);
	}

	public static <T extends AlipayResponse> T pageExecute(AlipayRequest<T> request) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().pageExecute(request);
	}

	public static <T extends AlipayResponse> T pageExecute(AlipayClient alipayClient, AlipayRequest<T> request) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.pageExecute(request);
	}

	public static <T extends AlipayResponse> T pageExecute(AlipayRequest<T> request, String method) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().pageExecute(request, method);
	}

	public static <T extends AlipayResponse> T pageExecute(AlipayClient alipayClient, AlipayRequest<T> request, String method) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.pageExecute(request, method);
	}

	public static <T extends AlipayResponse> T sdkExecute(AlipayRequest<T> request) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().sdkExecute(request);
	}

	public static <T extends AlipayResponse> T sdkExecute(AlipayClient alipayClient, AlipayRequest<T> request) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.sdkExecute(request);
	}

	public static BatchAlipayResponse execute(BatchAlipayRequest request) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
	}

	public static BatchAlipayResponse execute(AlipayClient alipayClient, BatchAlipayRequest request) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.execute(request);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().certificateExecute(request);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayClient alipayClient, AlipayRequest<T> request) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.certificateExecute(request);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request, String authToken) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().certificateExecute(request, authToken);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayClient alipayClient, AlipayRequest<T> request, String authToken) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.certificateExecute(request, authToken);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request, String accessToken, String appAuthToken) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().certificateExecute(request, accessToken, appAuthToken);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayClient alipayClient, AlipayRequest<T> request, String accessToken, String appAuthToken) throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.certificateExecute(request, accessToken, appAuthToken);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request, String accessToken, String appAuthToken, String targetAppId) throws AlipayApiException {
		return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().certificateExecute(request, accessToken, appAuthToken, targetAppId);
	}

	public static <T extends AlipayResponse> T certificateExecute(AlipayClient alipayClient, AlipayRequest<T> request, String accessToken, String appAuthToken, String targetAppId)
		throws AlipayApiException {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient.certificateExecute(request, accessToken, appAuthToken, targetAppId);
	}

	/**
	 * APP支付
	 *
	 * @param model     {@link AlipayTradeAppPayModel}
	 * @param notifyUrl 异步通知 URL
	 * @return {@link AlipayTradeAppPayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeAppPayResponse appPayToResponse(AlipayTradeAppPayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return sdkExecute(request);
	}


	/**
	 * APP支付
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param model        {@link AlipayTradeAppPayModel}
	 * @param notifyUrl    异步通知 URL
	 * @return {@link AlipayTradeAppPayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeAppPayResponse appPayToResponse(AlipayClient alipayClient, AlipayTradeAppPayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return sdkExecute(alipayClient, request);
	}

	/**
	 * APP支付
	 *
	 * @param model        {@link AlipayTradeAppPayModel}
	 * @param notifyUrl    异步通知 URL
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeAppPayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeAppPayResponse appPayToResponse(AlipayTradeAppPayModel model, String notifyUrl, String appAuthToken) throws AlipayApiException {
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.putOtherTextParam("app_auth_token", appAuthToken);
		return sdkExecute(request);
	}


	/**
	 * APP支付
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param model        {@link AlipayTradeAppPayModel}
	 * @param notifyUrl    异步通知 URL
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeAppPayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeAppPayResponse appPayToResponse(AlipayClient alipayClient, AlipayTradeAppPayModel model, String notifyUrl, String appAuthToken) throws AlipayApiException {
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.putOtherTextParam("app_auth_token", appAuthToken);
		return sdkExecute(alipayClient, request);
	}

	/**
	 * WAP支付
	 *
	 * @param response  {@link HttpServletResponse}
	 * @param model     {@link AlipayTradeWapPayModel}
	 * @param returnUrl 同步通知URL
	 * @param notifyUrl 异步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPay(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
		String form = wapPayStr(model, returnUrl, notifyUrl);
		response.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		PrintWriter out = response.getWriter();
		out.write(form);
		out.flush();
		out.close();
	}


	/**
	 * WAP支付
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    异步通知URL
	 * @param notifyUrl    同步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPay(AlipayClient alipayClient, HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
		String form = wapPayStr(alipayClient, model, returnUrl, notifyUrl);
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.write(form);
		out.flush();
		out.close();
	}


	/**
	 * WAP支付
	 *
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    异步通知URL
	 * @param notifyUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPay(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl, String appAuthToken) throws AlipayApiException, IOException {
		String form = wapPayStr(model, returnUrl, notifyUrl, appAuthToken);
		response.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		PrintWriter out = response.getWriter();
		out.write(form);
		out.flush();
		out.close();
	}


	/**
	 * WAP支付
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    异步通知URL
	 * @param notifyUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPay(AlipayClient alipayClient, HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl, String appAuthToken) throws AlipayApiException, IOException {
		String form = wapPayStr(alipayClient, model, returnUrl, notifyUrl, appAuthToken);
		String charset = "UTF-8";
		response.setContentType("text/html;charset=" + charset);
		PrintWriter out = response.getWriter();
		out.write(form);
		out.flush();
		out.close();
	}

	/**
	 * <p>
	 * WAP支付
	 * </p>
	 *
	 * <p>
	 * 为了解决 Filter 中使用 OutputStream getOutputStream() 和 PrintWriter getWriter() 冲突异常问题
	 * </p>
	 *
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    异步通知URL
	 * @param notifyUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPayByOutputStream(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl, String appAuthToken)
		throws AlipayApiException, IOException {
		String form = wapPayStr(model, returnUrl, notifyUrl, appAuthToken);
		response.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		OutputStream out = response.getOutputStream();
		out.write(form.getBytes(AliPayApiConfigKit.getAliPayApiConfig().getCharset()));
		response.getOutputStream().flush();
	}


	/**
	 * <p>
	 * WAP支付
	 * </p>
	 *
	 * <p>
	 * 为了解决 Filter 中使用 OutputStream getOutputStream() 和 PrintWriter getWriter() 冲突异常问题
	 * </p>
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    异步通知URL
	 * @param notifyUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPayByOutputStream(AlipayClient alipayClient, HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl, String appAuthToken)
		throws AlipayApiException, IOException {
		String form = wapPayStr(alipayClient, model, returnUrl, notifyUrl, appAuthToken);
		String charset = "UTF-8";
		response.setContentType("text/html;charset=" + charset);
		OutputStream out = response.getOutputStream();
		out.write(form.getBytes(charset));
		response.getOutputStream().flush();
	}

	/**
	 * <p>
	 * WAP支付
	 * </p>
	 *
	 * <p>
	 * 为了解决 Filter 中使用 OutputStream getOutputStream() 和 PrintWriter getWriter() 冲突异常问题
	 * </p>
	 *
	 * @param response  {@link HttpServletResponse}
	 * @param model     {@link AlipayTradeWapPayModel}
	 * @param returnUrl 异步通知URL
	 * @param notifyUrl 同步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPayByOutputStream(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
		String form = wapPayStr(model, returnUrl, notifyUrl);
		response.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		OutputStream out = response.getOutputStream();
		out.write(form.getBytes(AliPayApiConfigKit.getAliPayApiConfig().getCharset()));
		response.getOutputStream().flush();
	}


	/**
	 * <p>
	 * WAP支付
	 * </p>
	 *
	 * <p>
	 * 为了解决 Filter 中使用 OutputStream getOutputStream() 和 PrintWriter getWriter() 冲突异常问题
	 * </p>
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    异步通知URL
	 * @param notifyUrl    同步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void wapPayByOutputStream(AlipayClient alipayClient, HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
		String form = wapPayStr(alipayClient, model, returnUrl, notifyUrl);
		String charset = "UTF-8";
		response.setContentType("text/html;charset=" + charset);
		OutputStream out = response.getOutputStream();
		out.write(form.getBytes(charset));
		response.getOutputStream().flush();
	}

	/**
	 * WAP支付
	 *
	 * @param model     {@link AlipayTradeWapPayModel}
	 * @param returnUrl 异步通知URL
	 * @param notifyUrl 同步通知URL
	 * @return {String}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static String wapPayStr(AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException {
		AlipayTradeWapPayRequest aliPayRequest = new AlipayTradeWapPayRequest();
		aliPayRequest.setReturnUrl(returnUrl);
		aliPayRequest.setNotifyUrl(notifyUrl);
		aliPayRequest.setBizModel(model);
		return pageExecute(aliPayRequest).getBody();
	}

	/**
	 * WAP支付
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    异步通知URL
	 * @param notifyUrl    同步通知URL
	 * @return {String}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static String wapPayStr(AlipayClient alipayClient, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException {
		AlipayTradeWapPayRequest aliPayRequest = new AlipayTradeWapPayRequest();
		aliPayRequest.setReturnUrl(returnUrl);
		aliPayRequest.setNotifyUrl(notifyUrl);
		aliPayRequest.setBizModel(model);
		return pageExecute(alipayClient, aliPayRequest).getBody();
	}

	/**
	 * WAP支付
	 *
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    异步通知URL
	 * @param notifyUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @return {String}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static String wapPayStr(AlipayTradeWapPayModel model, String returnUrl, String notifyUrl, String appAuthToken) throws AlipayApiException {
		AlipayTradeWapPayRequest aliPayRequest = new AlipayTradeWapPayRequest();
		aliPayRequest.setReturnUrl(returnUrl);
		aliPayRequest.setNotifyUrl(notifyUrl);
		aliPayRequest.setBizModel(model);
		aliPayRequest.putOtherTextParam("app_auth_token", appAuthToken);
		return pageExecute(aliPayRequest).getBody();
	}

	/**
	 * WAP支付
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param model        {@link AlipayTradeWapPayModel}
	 * @param returnUrl    异步通知URL
	 * @param notifyUrl    同步通知URL
	 * @param appAuthToken 应用授权token
	 * @return {String}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static String wapPayStr(AlipayClient alipayClient, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl, String appAuthToken) throws AlipayApiException {
		AlipayTradeWapPayRequest aliPayRequest = new AlipayTradeWapPayRequest();
		aliPayRequest.setReturnUrl(returnUrl);
		aliPayRequest.setNotifyUrl(notifyUrl);
		aliPayRequest.setBizModel(model);
		aliPayRequest.putOtherTextParam("app_auth_token", appAuthToken);
		return pageExecute(alipayClient, aliPayRequest).getBody();
	}

	/**
	 * 统一收单交易支付接口接口 <br>
	 * 适用于:条形码支付、声波支付等 <br>
	 *
	 * @param model     {@link AlipayTradePayModel}
	 * @param notifyUrl 异步通知URL
	 * @return {@link AlipayTradePayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradePayResponse tradePayToResponse(AlipayTradePayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		// 填充业务参数
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return doExecute(request);
	}

	/**
	 * 统一收单交易支付接口接口 <br>
	 * 适用于:条形码支付、声波支付等 <br>
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradePayModel}
	 * @param notifyUrl    异步通知URL
	 * @return {@link AlipayTradePayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradePayResponse tradePayToResponse(AlipayClient alipayClient, Boolean certModel, AlipayTradePayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		// 填充业务参数
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 统一收单交易支付接口接口 <br>
	 * 适用于:条形码支付、声波支付等 <br>
	 *
	 * @param model        {AlipayTradePayModel}
	 * @param notifyUrl    异步通知URL
	 * @param appAuthToken 应用授权token
	 * @return {AlipayTradePayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradePayResponse tradePayToResponse(AlipayTradePayModel model, String notifyUrl, String appAuthToken) throws AlipayApiException {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.putOtherTextParam("app_auth_token", appAuthToken);
		return doExecute(request);
	}

	/**
	 * 统一收单交易支付接口接口 <br>
	 * 适用于:条形码支付、声波支付等 <br>
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {AlipayTradePayModel}
	 * @param notifyUrl    异步通知URL
	 * @param appAuthToken 应用授权token
	 * @return {AlipayTradePayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradePayResponse tradePayToResponse(AlipayClient alipayClient, Boolean certModel, AlipayTradePayModel model, String notifyUrl, String appAuthToken) throws AlipayApiException {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.putOtherTextParam("app_auth_token", appAuthToken);
		return doExecute(alipayClient, certModel, request);
	}


	/**
	 * 统一收单线下交易预创建 <br>
	 * 适用于：扫码支付等 <br>
	 *
	 * @param model     {@link AlipayTradePrecreateModel}
	 * @param notifyUrl 异步通知URL
	 * @return {@link AlipayTradePrecreateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayTradePrecreateModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return doExecute(request);
	}


	/**
	 * 统一收单线下交易预创建 <br>
	 * 适用于：扫码支付等 <br>
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradePrecreateModel}
	 * @param notifyUrl    异步通知URL
	 * @return {@link AlipayTradePrecreateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayClient alipayClient, Boolean certModel, AlipayTradePrecreateModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return doExecute(alipayClient, certModel, request);
	}


	/**
	 * 统一收单线下交易预创建 <br>
	 * 适用于：扫码支付等 <br>
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradePrecreateModel}
	 * @param notifyUrl    异步通知URL
	 * @return {@link AlipayTradePrecreateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayClient alipayClient, Boolean certModel, AlipayTradePrecreateModel model, String notifyUrl, String appAuthToken) throws AlipayApiException {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return doExecute(alipayClient, certModel, request, appAuthToken);
	}

	/**
	 * 统一收单线下交易预创建 <br>
	 * 适用于：扫码支付等 <br>
	 *
	 * @param model        {@link AlipayTradePrecreateModel}
	 * @param notifyUrl    异步通知URL
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradePrecreateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayTradePrecreateModel model, String notifyUrl, String appAuthToken) throws AlipayApiException {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单线下交易预创建 <br>
	 * 适用于：扫码支付等 <br>
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param model        {@link AlipayTradePrecreateModel}
	 * @param notifyUrl    异步通知URL
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradePrecreateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayClient alipayClient, AlipayTradePrecreateModel model, String notifyUrl, String appAuthToken) throws AlipayApiException {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return execute(alipayClient, request, null, appAuthToken);
	}

	/**
	 * 单笔转账到支付宝账户
	 *
	 * @param model {@link AlipayFundTransToaccountTransferModel}
	 * @return 转账是否成功
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	@Deprecated
	public static boolean transfer(AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
		AlipayFundTransToaccountTransferResponse response = transferToResponse(model);
		String result = response.getBody();
		if (response.isSuccess()) {
			return true;
		} else {
			// 调用查询接口查询数据
			JSONObject jsonObject = JSONObject.parseObject(result);
			String outBizNo = jsonObject.getJSONObject("alipay_fund_trans_toaccount_transfer_response").getString("out_biz_no");
			AlipayFundTransOrderQueryModel queryModel = new AlipayFundTransOrderQueryModel();
			model.setOutBizNo(outBizNo);
			return transferQuery(queryModel);
		}
	}


	/**
	 * 单笔转账到支付宝账户
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundTransToaccountTransferModel}
	 * @return 转账是否成功
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	@Deprecated
	public static boolean transfer(AlipayClient alipayClient, Boolean certModel, AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
		AlipayFundTransToaccountTransferResponse response = transferToResponse(model);
		String result = response.getBody();
		if (response.isSuccess()) {
			return true;
		} else {
			// 调用查询接口查询数据
			JSONObject jsonObject = JSONObject.parseObject(result);
			String outBizNo = jsonObject.getJSONObject("alipay_fund_trans_toaccount_transfer_response").getString("out_biz_no");
			AlipayFundTransOrderQueryModel queryModel = new AlipayFundTransOrderQueryModel();
			model.setOutBizNo(outBizNo);
			return transferQuery(alipayClient, certModel, queryModel);
		}
	}

	/**
	 * 单笔转账到支付宝账户
	 *
	 * @param model {@link AlipayFundTransToaccountTransferModel}
	 * @return {@link AlipayFundTransToaccountTransferResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundTransToaccountTransferResponse transferToResponse(AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizModel(model);
		return doExecute(request);
	}


	/**
	 * 单笔转账到支付宝账户
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundTransToaccountTransferModel}
	 * @return {@link AlipayFundTransToaccountTransferResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundTransToaccountTransferResponse transferToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 转账查询接口
	 *
	 * @param model {@link AlipayFundTransOrderQueryModel}
	 * @return 是否存在此
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	@Deprecated
	public static boolean transferQuery(AlipayFundTransOrderQueryModel model) throws AlipayApiException {
		AlipayFundTransOrderQueryResponse response = transferQueryToResponse(model);
		return response.isSuccess();
	}

	/**
	 * 转账查询接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundTransOrderQueryModel}
	 * @return 是否存在此
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	@Deprecated
	public static boolean transferQuery(AlipayClient alipayClient, Boolean certModel, AlipayFundTransOrderQueryModel model) throws AlipayApiException {
		AlipayFundTransOrderQueryResponse response = transferQueryToResponse(alipayClient, certModel, model);
		return response.isSuccess();
	}

	/**
	 * 转账查询接口
	 *
	 * @param model {@link AlipayFundTransOrderQueryModel}
	 * @return {@link AlipayFundTransOrderQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundTransOrderQueryResponse transferQueryToResponse(AlipayFundTransOrderQueryModel model) throws AlipayApiException {
		AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 转账查询接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundTransOrderQueryModel}
	 * @return {@link AlipayFundTransOrderQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundTransOrderQueryResponse transferQueryToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundTransOrderQueryModel model) throws AlipayApiException {
		AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 统一转账接口
	 *
	 * @param model        model {@link AlipayFundTransUniTransferModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayFundTransUniTransferResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundTransUniTransferResponse uniTransferToResponse(AlipayFundTransUniTransferModel model, String appAuthToken) throws AlipayApiException {
		AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
		request.setBizModel(model);
		if (!StringUtils.isEmpty(appAuthToken)) {
			request.putOtherTextParam("app_auth_token", appAuthToken);
		}
		return doExecute(request);
	}

	/**
	 * 统一转账接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        model {@link AlipayFundTransUniTransferModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayFundTransUniTransferResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundTransUniTransferResponse uniTransferToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundTransUniTransferModel model, String appAuthToken) throws AlipayApiException {
		AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
		request.setBizModel(model);
		if (!StringUtils.isEmpty(appAuthToken)) {
			request.putOtherTextParam("app_auth_token", appAuthToken);
		}
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 转账业务单据查询接口
	 *
	 * @param model        model {@link AlipayFundTransCommonQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayFundTransCommonQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundTransCommonQueryResponse transCommonQueryToResponse(AlipayFundTransCommonQueryModel model, String appAuthToken) throws AlipayApiException {
		AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
		request.setBizModel(model);
		if (!StringUtils.isEmpty(appAuthToken)) {
			request.putOtherTextParam("app_auth_token", appAuthToken);
		}
		return doExecute(request);
	}

	/**
	 * 转账业务单据查询接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        model {@link AlipayFundTransCommonQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayFundTransCommonQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundTransCommonQueryResponse transCommonQueryToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundTransCommonQueryModel model, String appAuthToken) throws AlipayApiException {
		AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
		request.setBizModel(model);
		if (!StringUtils.isEmpty(appAuthToken)) {
			request.putOtherTextParam("app_auth_token", appAuthToken);
		}
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 支付宝资金账户资产查询接口
	 *
	 * @param model        model {@link AlipayFundAccountQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayFundAccountQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundAccountQueryResponse accountQueryToResponse(AlipayFundAccountQueryModel model, String appAuthToken) throws AlipayApiException {
		AlipayFundAccountQueryRequest request = new AlipayFundAccountQueryRequest();
		request.setBizModel(model);
		if (!StringUtils.isEmpty(appAuthToken)) {
			request.putOtherTextParam("app_auth_token", appAuthToken);
		}
		return doExecute(request);
	}


	/**
	 * 支付宝资金账户资产查询接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        model {@link AlipayFundAccountQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayFundAccountQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundAccountQueryResponse accountQueryToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundAccountQueryModel model, String appAuthToken) throws AlipayApiException {
		AlipayFundAccountQueryRequest request = new AlipayFundAccountQueryRequest();
		request.setBizModel(model);
		if (!StringUtils.isEmpty(appAuthToken)) {
			request.putOtherTextParam("app_auth_token", appAuthToken);
		}
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 统一收单线下交易查询接口
	 *
	 * @param model {@link AlipayTradeQueryModel}
	 * @return {@link AlipayTradeQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeQueryResponse tradeQueryToResponse(AlipayTradeQueryModel model) throws AlipayApiException {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 统一收单线下交易查询接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradeQueryModel}
	 * @return {@link AlipayTradeQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeQueryResponse tradeQueryToResponse(AlipayClient alipayClient, Boolean certModel, AlipayTradeQueryModel model) throws AlipayApiException {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 统一收单线下交易查询接口
	 *
	 * @param model        {@link AlipayTradeQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeQueryResponse tradeQueryToResponse(AlipayTradeQueryModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单线下交易查询接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param model        {@link AlipayTradeQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeQueryResponse tradeQueryToResponse(AlipayClient alipayClient, AlipayTradeQueryModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		return execute(alipayClient, request, null, appAuthToken);
	}

	/**
	 * 统一收单交易撤销接口
	 *
	 * @param model        {@link AlipayTradeCancelModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeCancelResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeCancelResponse tradeCancelToResponse(AlipayTradeCancelModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizModel(model);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单交易撤销接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param model        {@link AlipayTradeCancelModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeCancelResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeCancelResponse tradeCancelToResponse(AlipayClient alipayClient, AlipayTradeCancelModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizModel(model);
		return execute(alipayClient, request, null, appAuthToken);
	}

	/**
	 * 统一收单交易撤销接口
	 *
	 * @param model {@link AlipayTradeCancelModel}
	 * @return {@link AlipayTradeCancelResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeCancelResponse tradeCancelToResponse(AlipayTradeCancelModel model) throws AlipayApiException {
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 统一收单交易撤销接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradeCancelModel}
	 * @return {@link AlipayTradeCancelResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeCancelResponse tradeCancelToResponse(AlipayClient alipayClient, Boolean certModel, AlipayTradeCancelModel model) throws AlipayApiException {
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 统一收单交易关闭接口
	 *
	 * @param model        {@link AlipayTradeCloseModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeCloseResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeCloseResponse tradeCloseToResponse(AlipayTradeCloseModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizModel(model);
		return execute(request, null, appAuthToken);

	}

	/**
	 * 统一收单交易关闭接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param model        {@link AlipayTradeCloseModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeCloseResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeCloseResponse tradeCloseToResponse(AlipayClient alipayClient, AlipayTradeCloseModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizModel(model);
		return execute(alipayClient, request, null, appAuthToken);

	}

	/**
	 * 统一收单交易关闭接口
	 *
	 * @param model {@link AlipayTradeCloseModel}
	 * @return {@link AlipayTradeCloseResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeCloseResponse tradeCloseToResponse(AlipayTradeCloseModel model) throws AlipayApiException {
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 统一收单交易关闭接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradeCloseModel}
	 * @return {@link AlipayTradeCloseResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeCloseResponse tradeCloseToResponse(AlipayClient alipayClient, Boolean certModel, AlipayTradeCloseModel model) throws AlipayApiException {
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 统一收单交易创建接口
	 *
	 * @param model     {@link AlipayTradeCreateModel}
	 * @param notifyUrl 异步通知URL
	 * @return {@link AlipayTradeCreateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeCreateResponse tradeCreateToResponse(AlipayTradeCreateModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return doExecute(request);
	}

	/**
	 * 统一收单交易创建接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradeCreateModel}
	 * @param notifyUrl    异步通知URL
	 * @return {@link AlipayTradeCreateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeCreateResponse tradeCreateToResponse(AlipayClient alipayClient, Boolean certModel, AlipayTradeCreateModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 统一收单交易创建接口
	 *
	 * @param model        {@link AlipayTradeCreateModel}
	 * @param notifyUrl    异步通知URL
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeCreateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeCreateResponse tradeCreateToResponse(AlipayTradeCreateModel model, String notifyUrl, String appAuthToken) throws AlipayApiException {
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单交易创建接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param model        {@link AlipayTradeCreateModel}
	 * @param notifyUrl    异步通知URL
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeCreateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeCreateResponse tradeCreateToResponse(AlipayClient alipayClient, AlipayTradeCreateModel model, String notifyUrl, String appAuthToken) throws AlipayApiException {
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return execute(alipayClient, request, null, appAuthToken);
	}

	/**
	 * 统一收单交易退款接口
	 *
	 * @param model {@link AlipayTradeRefundModel}
	 * @return {@link AlipayTradeRefundResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayTradeRefundModel model) throws AlipayApiException {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 统一收单交易退款接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradeRefundModel}
	 * @return {@link AlipayTradeRefundResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayClient alipayClient, Boolean certModel, AlipayTradeRefundModel model) throws AlipayApiException {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 统一收单交易退款接口
	 *
	 * @param model        {@link AlipayTradeRefundModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeRefundResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayTradeRefundModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单交易退款接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param model        {@link AlipayTradeRefundModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeRefundResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayClient alipayClient, AlipayTradeRefundModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		return execute(alipayClient, request, null, appAuthToken);
	}

	/**
	 * 统一收单退款页面接口
	 *
	 * @param model {@link AlipayTradePageRefundModel}
	 * @return {@link AlipayTradePageRefundResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradePageRefundResponse tradeRefundToResponse(AlipayTradePageRefundModel model) throws AlipayApiException {
		AlipayTradePageRefundRequest request = new AlipayTradePageRefundRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 统一收单退款页面接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradePageRefundModel}
	 * @return {@link AlipayTradePageRefundResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradePageRefundResponse tradeRefundToResponse(AlipayClient alipayClient, Boolean certModel, AlipayTradePageRefundModel model) throws AlipayApiException {
		AlipayTradePageRefundRequest request = new AlipayTradePageRefundRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}


	/**
	 * 统一收单退款页面接口
	 *
	 * @param model        {@link AlipayTradePageRefundModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradePageRefundResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradePageRefundResponse tradeRefundToResponse(AlipayTradePageRefundModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradePageRefundRequest request = new AlipayTradePageRefundRequest();
		request.setBizModel(model);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单退款页面接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param model        {@link AlipayTradePageRefundModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradePageRefundResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradePageRefundResponse tradeRefundToResponse(AlipayClient alipayClient, AlipayTradePageRefundModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradePageRefundRequest request = new AlipayTradePageRefundRequest();
		request.setBizModel(model);
		return execute(alipayClient, request, null, appAuthToken);
	}

	/**
	 * 统一收单交易退款查询
	 *
	 * @param model {@link AlipayTradeFastpayRefundQueryModel}
	 * @return {@link AlipayTradeFastpayRefundQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeFastpayRefundQueryResponse tradeRefundQueryToResponse(AlipayTradeFastpayRefundQueryModel model) throws AlipayApiException {
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 统一收单交易退款查询
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradeFastpayRefundQueryModel}
	 * @return {@link AlipayTradeFastpayRefundQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeFastpayRefundQueryResponse tradeRefundQueryToResponse(AlipayClient alipayClient, Boolean certModel, AlipayTradeFastpayRefundQueryModel model) throws AlipayApiException {
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 统一收单交易退款查询
	 *
	 * @param model        {@link AlipayTradeFastpayRefundQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeFastpayRefundQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeFastpayRefundQueryResponse tradeRefundQueryToResponse(AlipayTradeFastpayRefundQueryModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单交易退款查询
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param model        {@link AlipayTradeFastpayRefundQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeFastpayRefundQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeFastpayRefundQueryResponse tradeRefundQueryToResponse(AlipayClient alipayClient, AlipayTradeFastpayRefundQueryModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		return execute(alipayClient, request, null, appAuthToken);
	}

	/**
	 * 查询对账单下载地址
	 *
	 * @param model {@link AlipayDataDataserviceBillDownloadurlQueryModel}
	 * @return 对账单下载地址
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static String billDownloadUrlQuery(AlipayDataDataserviceBillDownloadurlQueryModel model) throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryResponse response = billDownloadUrlQueryToResponse(model);
		return response.getBillDownloadUrl();
	}

	/**
	 * 查询对账单下载地址
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayDataDataserviceBillDownloadurlQueryModel}
	 * @return 对账单下载地址
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static String billDownloadUrlQuery(AlipayClient alipayClient, Boolean certModel, AlipayDataDataserviceBillDownloadurlQueryModel model) throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryResponse response = billDownloadUrlQueryToResponse(alipayClient, certModel, model);
		return response.getBillDownloadUrl();
	}

	/**
	 * 查询对账单下载地址
	 *
	 * @param model {@link AlipayDataDataserviceBillDownloadurlQueryModel}
	 * @return {@link AlipayDataDataserviceBillDownloadurlQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadUrlQueryToResponse(AlipayDataDataserviceBillDownloadurlQueryModel model) throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 查询对账单下载地址
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayDataDataserviceBillDownloadurlQueryModel}
	 * @return {@link AlipayDataDataserviceBillDownloadurlQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadUrlQueryToResponse(AlipayClient alipayClient, Boolean certModel, AlipayDataDataserviceBillDownloadurlQueryModel model) throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 查询对账单下载地址
	 *
	 * @param model        {@link AlipayDataDataserviceBillDownloadurlQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayDataDataserviceBillDownloadurlQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadUrlQueryToResponse(AlipayDataDataserviceBillDownloadurlQueryModel model, String appAuthToken)
		throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizModel(model);
		request.putOtherTextParam("app_auth_token", appAuthToken);
		return doExecute(request);
	}

	/**
	 * 查询对账单下载地址
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayDataDataserviceBillDownloadurlQueryModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayDataDataserviceBillDownloadurlQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadUrlQueryToResponse(AlipayClient alipayClient, Boolean certModel, AlipayDataDataserviceBillDownloadurlQueryModel model, String appAuthToken)
		throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizModel(model);
		request.putOtherTextParam("app_auth_token", appAuthToken);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 统一收单交易结算接口
	 *
	 * @param model        {@link AlipayTradeOrderSettleModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeOrderSettleResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeOrderSettleResponse tradeOrderSettleToResponse(AlipayTradeOrderSettleModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
		request.setBizModel(model);
		return execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单交易结算接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param model        {@link AlipayTradeOrderSettleModel}
	 * @param appAuthToken 应用授权token
	 * @return {@link AlipayTradeOrderSettleResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeOrderSettleResponse tradeOrderSettleToResponse(AlipayClient alipayClient, AlipayTradeOrderSettleModel model, String appAuthToken) throws AlipayApiException {
		AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
		request.setBizModel(model);
		return execute(alipayClient, request, null, appAuthToken);
	}

	/**
	 * 统一收单交易结算接口
	 *
	 * @param model {@link AlipayTradeOrderSettleModel}
	 * @return {@link AlipayTradeOrderSettleResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeOrderSettleResponse tradeOrderSettleToResponse(AlipayTradeOrderSettleModel model) throws AlipayApiException {
		AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 统一收单交易结算接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradeOrderSettleModel}
	 * @return {@link AlipayTradeOrderSettleResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeOrderSettleResponse tradeOrderSettleToResponse(AlipayClient alipayClient, Boolean certModel, AlipayTradeOrderSettleModel model) throws AlipayApiException {
		AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 电脑网站支付(PC支付)
	 *
	 * @param response  {@link HttpServletResponse}
	 * @param model     {@link AlipayTradePagePayModel}
	 * @param notifyUrl 异步通知URL
	 * @param returnUrl 同步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePage(HttpServletResponse response, AlipayTradePagePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException {
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		String form = pageExecute(request).getBody();
		response.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		PrintWriter out = response.getWriter();
		out.write(form);
		out.flush();
		out.close();
	}

	/**
	 * 电脑网站支付(PC支付)
	 *
	 * @param response  {@link HttpServletResponse}
	 * @param method    GET/POST GET 返回url,POST 返回 FORM <a href="https://opensupport.alipay.com/support/helpcenter/192/201602488772?ant_source=antsupport">参考文章</a>
	 * @param model     {@link AlipayTradePagePayModel}
	 * @param notifyUrl 异步通知URL
	 * @param returnUrl 同步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePage(HttpServletResponse response, String method, AlipayTradePagePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException {
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		String form = pageExecute(request, method).getBody();
		response.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		PrintWriter out = response.getWriter();
		out.write(form);
		out.flush();
		out.close();
	}

	/**
	 * 电脑网站支付(PC支付)
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
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		String form = pageExecute(alipayClient, request).getBody();
		String charset = "UTF-8";
		response.setContentType("text/html;charset=" + charset);
		PrintWriter out = response.getWriter();
		out.write(form);
		out.flush();
		out.close();
	}

	/**
	 * 电脑网站支付(PC支付)
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param response     {@link HttpServletResponse}
	 * @param method       GET/POST GET 返回url,POST 返回 FORM <a href="https://opensupport.alipay.com/support/helpcenter/192/201602488772?ant_source=antsupport">参考文章</a>
	 * @param model        {@link AlipayTradePagePayModel}
	 * @param notifyUrl    异步通知URL
	 * @param returnUrl    同步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePage(AlipayClient alipayClient, HttpServletResponse response, String method, AlipayTradePagePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException {
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		String form = pageExecute(alipayClient, request, method).getBody();
		String charset = "UTF-8";
		response.setContentType("text/html;charset=" + charset);
		PrintWriter out = response.getWriter();
		out.write(form);
		out.flush();
		out.close();
	}


	/**
	 * 电脑网站支付(PC支付)
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
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		request.putOtherTextParam("app_auth_token", appAuthToken);
		String form = pageExecute(request).getBody();
		response.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		PrintWriter out = response.getWriter();
		out.write(form);
		out.flush();
		out.close();
	}

	/**
	 * 电脑网站支付(PC支付)
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
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		request.putOtherTextParam("app_auth_token", appAuthToken);
		String form = pageExecute(alipayClient, request).getBody();
		String charset = "UTF-8";
		response.setContentType("text/html;charset=" + charset);
		PrintWriter out = response.getWriter();
		out.write(form);
		out.flush();
		out.close();
	}

	/**
	 * 电脑网站支付(PC支付)
	 *
	 * @param response  {@link HttpServletResponse}
	 * @param model     {@link AlipayTradePagePayModel}
	 * @param notifyUrl 异步通知URL
	 * @param returnUrl 同步通知URL
	 * @throws AlipayApiException 支付宝 Api 异常
	 * @throws IOException        IO 异常
	 */
	public static void tradePageByOutputStream(HttpServletResponse response, AlipayTradePagePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException {
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		String form = pageExecute(request).getBody();
		response.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		OutputStream out = response.getOutputStream();
		out.write(form.getBytes(AliPayApiConfigKit.getAliPayApiConfig().getCharset()));
		response.getOutputStream().flush();
	}

	/**
	 * 电脑网站支付(PC支付)
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
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		String form = pageExecute(alipayClient, request).getBody();
		String charset = "UTF-8";
		response.setContentType("text/html;charset=" + charset);
		OutputStream out = response.getOutputStream();
		out.write(form.getBytes(charset));
		response.getOutputStream().flush();
	}


	/**
	 * 电脑网站支付(PC支付)
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
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		request.putOtherTextParam("app_auth_token", appAuthToken);
		String form = pageExecute(request).getBody();
		response.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		OutputStream out = response.getOutputStream();
		out.write(form.getBytes(AliPayApiConfigKit.getAliPayApiConfig().getCharset()));
		response.getOutputStream().flush();
	}

	/**
	 * 电脑网站支付(PC支付)
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
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		request.putOtherTextParam("app_auth_token", appAuthToken);
		String form = pageExecute(alipayClient, request).getBody();
		String charset = "UTF-8";
		response.setContentType("text/html;charset=" + charset);
		OutputStream out = response.getOutputStream();
		out.write(form.getBytes(charset));
		response.getOutputStream().flush();
	}

	/**
	 * 资金预授权冻结接口
	 *
	 * @param model {@link AlipayFundAuthOrderFreezeModel}
	 * @return {@link AlipayFundAuthOrderFreezeResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundAuthOrderFreezeResponse authOrderFreezeToResponse(AlipayFundAuthOrderFreezeModel model) throws AlipayApiException {
		AlipayFundAuthOrderFreezeRequest request = new AlipayFundAuthOrderFreezeRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 资金预授权冻结接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundAuthOrderFreezeModel}
	 * @return {@link AlipayFundAuthOrderFreezeResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundAuthOrderFreezeResponse authOrderFreezeToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundAuthOrderFreezeModel model) throws AlipayApiException {
		AlipayFundAuthOrderFreezeRequest request = new AlipayFundAuthOrderFreezeRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}


	/**
	 * 资金授权解冻接口
	 *
	 * @param model {@link AlipayFundAuthOrderUnfreezeModel}
	 * @return {@link AlipayFundAuthOrderUnfreezeResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundAuthOrderUnfreezeResponse authOrderUnfreezeToResponse(AlipayFundAuthOrderUnfreezeModel model) throws AlipayApiException {
		AlipayFundAuthOrderUnfreezeRequest request = new AlipayFundAuthOrderUnfreezeRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 资金授权解冻接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundAuthOrderUnfreezeModel}
	 * @return {@link AlipayFundAuthOrderUnfreezeResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundAuthOrderUnfreezeResponse authOrderUnfreezeToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundAuthOrderUnfreezeModel model) throws AlipayApiException {
		AlipayFundAuthOrderUnfreezeRequest request = new AlipayFundAuthOrderUnfreezeRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 资金预授权冻结接口
	 *
	 * @param model {@link AlipayFundAuthOrderVoucherCreateModel}
	 * @return {@link AlipayFundAuthOrderVoucherCreateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundAuthOrderVoucherCreateResponse authOrderVoucherCreateToResponse(AlipayFundAuthOrderVoucherCreateModel model) throws AlipayApiException {
		AlipayFundAuthOrderVoucherCreateRequest request = new AlipayFundAuthOrderVoucherCreateRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 资金预授权冻结接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundAuthOrderVoucherCreateModel}
	 * @return {@link AlipayFundAuthOrderVoucherCreateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundAuthOrderVoucherCreateResponse authOrderVoucherCreateToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundAuthOrderVoucherCreateModel model) throws AlipayApiException {
		AlipayFundAuthOrderVoucherCreateRequest request = new AlipayFundAuthOrderVoucherCreateRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}


	/**
	 * 资金授权撤销接口
	 *
	 * @param model {@link AlipayFundAuthOperationCancelModel}
	 * @return {@link AlipayFundAuthOperationCancelResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundAuthOperationCancelResponse authOperationCancelToResponse(AlipayFundAuthOperationCancelModel model) throws AlipayApiException {
		AlipayFundAuthOperationCancelRequest request = new AlipayFundAuthOperationCancelRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 资金授权撤销接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundAuthOperationCancelModel}
	 * @return {@link AlipayFundAuthOperationCancelResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundAuthOperationCancelResponse authOperationCancelToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundAuthOperationCancelModel model) throws AlipayApiException {
		AlipayFundAuthOperationCancelRequest request = new AlipayFundAuthOperationCancelRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 资金授权操作查询接口
	 *
	 * @param model {@link AlipayFundAuthOperationDetailQueryModel}
	 * @return {@link AlipayFundAuthOperationDetailQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundAuthOperationDetailQueryResponse authOperationDetailQueryToResponse(AlipayFundAuthOperationDetailQueryModel model) throws AlipayApiException {
		AlipayFundAuthOperationDetailQueryRequest request = new AlipayFundAuthOperationDetailQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 资金授权操作查询接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundAuthOperationDetailQueryModel}
	 * @return {@link AlipayFundAuthOperationDetailQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundAuthOperationDetailQueryResponse authOperationDetailQueryToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundAuthOperationDetailQueryModel model) throws AlipayApiException {
		AlipayFundAuthOperationDetailQueryRequest request = new AlipayFundAuthOperationDetailQueryRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 红包无线支付接口
	 *
	 * @param model {@link AlipayFundCouponOrderAppPayModel}
	 * @return {@link AlipayFundCouponOrderAppPayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundCouponOrderAppPayResponse fundCouponOrderAppPayToResponse(AlipayFundCouponOrderAppPayModel model) throws AlipayApiException {
		AlipayFundCouponOrderAppPayRequest request = new AlipayFundCouponOrderAppPayRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 红包无线支付接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundCouponOrderAppPayModel}
	 * @return {@link AlipayFundCouponOrderAppPayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundCouponOrderAppPayResponse fundCouponOrderAppPayToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundCouponOrderAppPayModel model) throws AlipayApiException {
		AlipayFundCouponOrderAppPayRequest request = new AlipayFundCouponOrderAppPayRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}


	/**
	 * 红包页面支付接口
	 *
	 * @param model {@link AlipayFundCouponOrderPagePayModel}
	 * @return {@link AlipayFundCouponOrderPagePayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundCouponOrderPagePayResponse fundCouponOrderPagePayToResponse(AlipayFundCouponOrderPagePayModel model) throws AlipayApiException {
		AlipayFundCouponOrderPagePayRequest request = new AlipayFundCouponOrderPagePayRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 红包页面支付接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundCouponOrderPagePayModel}
	 * @return {@link AlipayFundCouponOrderPagePayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundCouponOrderPagePayResponse fundCouponOrderPagePayToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundCouponOrderPagePayModel model) throws AlipayApiException {
		AlipayFundCouponOrderPagePayRequest request = new AlipayFundCouponOrderPagePayRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}


	/**
	 * 红包协议支付接口
	 *
	 * @param model {@link AlipayFundCouponOrderAgreementPayModel}
	 * @return {@link AlipayFundCouponOrderAgreementPayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundCouponOrderAgreementPayResponse fundCouponOrderAgreementPayToResponse(AlipayFundCouponOrderAgreementPayModel model) throws AlipayApiException {
		AlipayFundCouponOrderAgreementPayRequest request = new AlipayFundCouponOrderAgreementPayRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 红包协议支付接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundCouponOrderAgreementPayModel}
	 * @return {@link AlipayFundCouponOrderAgreementPayResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundCouponOrderAgreementPayResponse fundCouponOrderAgreementPayToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundCouponOrderAgreementPayModel model) throws AlipayApiException {
		AlipayFundCouponOrderAgreementPayRequest request = new AlipayFundCouponOrderAgreementPayRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 红包打款接口
	 *
	 * @param model {@link AlipayFundCouponOrderDisburseModel}
	 * @return {@link AlipayFundCouponOrderDisburseResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundCouponOrderDisburseResponse fundCouponOrderDisburseToResponse(AlipayFundCouponOrderDisburseModel model) throws AlipayApiException {
		AlipayFundCouponOrderDisburseRequest request = new AlipayFundCouponOrderDisburseRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 红包打款接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundCouponOrderDisburseModel}
	 * @return {@link AlipayFundCouponOrderDisburseResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundCouponOrderDisburseResponse fundCouponOrderDisburseToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundCouponOrderDisburseModel model) throws AlipayApiException {
		AlipayFundCouponOrderDisburseRequest request = new AlipayFundCouponOrderDisburseRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 红包退回接口
	 *
	 * @param model {@link AlipayFundCouponOrderRefundModel}
	 * @return {@link AlipayFundCouponOrderRefundResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundCouponOrderRefundResponse fundCouponOrderRefundToResponse(AlipayFundCouponOrderRefundModel model) throws AlipayApiException {
		AlipayFundCouponOrderRefundRequest request = new AlipayFundCouponOrderRefundRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 红包退回接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundCouponOrderRefundModel}
	 * @return {@link AlipayFundCouponOrderRefundResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundCouponOrderRefundResponse fundCouponOrderRefundToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundCouponOrderRefundModel model) throws AlipayApiException {
		AlipayFundCouponOrderRefundRequest request = new AlipayFundCouponOrderRefundRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 红包退回接口
	 *
	 * @param model {@link AlipayFundCouponOperationQueryModel}
	 * @return {@link AlipayFundCouponOperationQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundCouponOperationQueryResponse fundCouponOperationQueryToResponse(AlipayFundCouponOperationQueryModel model) throws AlipayApiException {
		AlipayFundCouponOperationQueryRequest request = new AlipayFundCouponOperationQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 红包退回接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayFundCouponOperationQueryModel}
	 * @return {@link AlipayFundCouponOperationQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayFundCouponOperationQueryResponse fundCouponOperationQueryToResponse(AlipayClient alipayClient, Boolean certModel, AlipayFundCouponOperationQueryModel model) throws AlipayApiException {
		AlipayFundCouponOperationQueryRequest request = new AlipayFundCouponOperationQueryRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 应用授权 URL 拼装
	 *
	 * @param appId       应用编号
	 * @param redirectUri 回调 URI
	 * @return 应用授权 URL
	 * @throws UnsupportedEncodingException 编码异常
	 */
	public static String getOauth2Url(String appId, String redirectUri) throws UnsupportedEncodingException {
		return new StringBuffer().append("https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=").append(appId).append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"))
			.toString();
	}

	/**
	 * 使用 app_auth_code 换取 app_auth_token
	 *
	 * @param model {@link AlipayOpenAuthTokenAppModel}
	 * @return {@link AlipayOpenAuthTokenAppResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayOpenAuthTokenAppResponse openAuthTokenAppToResponse(AlipayOpenAuthTokenAppModel model) throws AlipayApiException {
		AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 使用 app_auth_code 换取 app_auth_token
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayOpenAuthTokenAppModel}
	 * @return {@link AlipayOpenAuthTokenAppResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayOpenAuthTokenAppResponse openAuthTokenAppToResponse(AlipayClient alipayClient, Boolean certModel, AlipayOpenAuthTokenAppModel model) throws AlipayApiException {
		AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 查询授权信息
	 *
	 * @param model {@link AlipayOpenAuthTokenAppQueryModel}
	 * @return {@link AlipayOpenAuthTokenAppQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayOpenAuthTokenAppQueryResponse openAuthTokenAppQueryToResponse(AlipayOpenAuthTokenAppQueryModel model) throws AlipayApiException {
		AlipayOpenAuthTokenAppQueryRequest request = new AlipayOpenAuthTokenAppQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}


	/**
	 * 查询授权信息
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayOpenAuthTokenAppQueryModel}
	 * @return {@link AlipayOpenAuthTokenAppQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayOpenAuthTokenAppQueryResponse openAuthTokenAppQueryToResponse(AlipayClient alipayClient, Boolean certModel, AlipayOpenAuthTokenAppQueryModel model) throws AlipayApiException {
		AlipayOpenAuthTokenAppQueryRequest request = new AlipayOpenAuthTokenAppQueryRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 地铁购票发码
	 *
	 * @param model {@link AlipayCommerceCityfacilitatorVoucherGenerateModel}
	 * @return {@link AlipayCommerceCityfacilitatorVoucherGenerateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayCommerceCityfacilitatorVoucherGenerateResponse voucherGenerateToResponse(AlipayCommerceCityfacilitatorVoucherGenerateModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherGenerateRequest request = new AlipayCommerceCityfacilitatorVoucherGenerateRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 地铁购票发码
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayCommerceCityfacilitatorVoucherGenerateModel}
	 * @return {@link AlipayCommerceCityfacilitatorVoucherGenerateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayCommerceCityfacilitatorVoucherGenerateResponse voucherGenerateToResponse(AlipayClient alipayClient, Boolean certModel, AlipayCommerceCityfacilitatorVoucherGenerateModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherGenerateRequest request = new AlipayCommerceCityfacilitatorVoucherGenerateRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}


	/**
	 * 地铁购票发码退款
	 *
	 * @param model {@link AlipayCommerceCityfacilitatorVoucherRefundModel}
	 * @return {@link AlipayCommerceCityfacilitatorVoucherRefundResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayCommerceCityfacilitatorVoucherRefundResponse metroRefundToResponse(AlipayCommerceCityfacilitatorVoucherRefundModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherRefundRequest request = new AlipayCommerceCityfacilitatorVoucherRefundRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 地铁购票发码退款
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayCommerceCityfacilitatorVoucherRefundModel}
	 * @return {@link AlipayCommerceCityfacilitatorVoucherRefundResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayCommerceCityfacilitatorVoucherRefundResponse metroRefundToResponse(AlipayClient alipayClient, Boolean certModel, AlipayCommerceCityfacilitatorVoucherRefundModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherRefundRequest request = new AlipayCommerceCityfacilitatorVoucherRefundRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 地铁车站数据查询
	 *
	 * @param model {@link AlipayCommerceCityfacilitatorStationQueryModel}
	 * @return {@link AlipayCommerceCityfacilitatorStationQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayCommerceCityfacilitatorStationQueryResponse stationQueryToResponse(AlipayCommerceCityfacilitatorStationQueryModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorStationQueryRequest request = new AlipayCommerceCityfacilitatorStationQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}


	/**
	 * 地铁车站数据查询
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayCommerceCityfacilitatorStationQueryModel}
	 * @return {@link AlipayCommerceCityfacilitatorStationQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayCommerceCityfacilitatorStationQueryResponse stationQueryToResponse(AlipayClient alipayClient, Boolean certModel, AlipayCommerceCityfacilitatorStationQueryModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorStationQueryRequest request = new AlipayCommerceCityfacilitatorStationQueryRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}


	/**
	 * 核销码批量查询
	 *
	 * @param model {@link AlipayCommerceCityfacilitatorVoucherBatchqueryModel}
	 * @return {@link AlipayCommerceCityfacilitatorVoucherBatchqueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayCommerceCityfacilitatorVoucherBatchqueryResponse voucherBatchqueryToResponse(AlipayCommerceCityfacilitatorVoucherBatchqueryModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherBatchqueryRequest request = new AlipayCommerceCityfacilitatorVoucherBatchqueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 核销码批量查询
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayCommerceCityfacilitatorVoucherBatchqueryModel}
	 * @return {@link AlipayCommerceCityfacilitatorVoucherBatchqueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayCommerceCityfacilitatorVoucherBatchqueryResponse voucherBatchqueryToResponse(AlipayClient alipayClient, Boolean certModel, AlipayCommerceCityfacilitatorVoucherBatchqueryModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherBatchqueryRequest request = new AlipayCommerceCityfacilitatorVoucherBatchqueryRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}


	public static void batchTrans(Map<String, String> params, String privateKey, String signType, HttpServletResponse response) throws IOException {
		params.put("service", "batch_trans_notify");
		params.put("_input_charset", "UTF-8");
		params.put("pay_date", DateUtil.format(new Date(), "YYYYMMDD"));
		Map<String, String> param = AliPayCore.buildRequestPara(params, privateKey, signType);
		response.sendRedirect(GATEWAY_NEW.concat(AliPayCore.createLinkString(param)));
	}

	/**
	 * 将异步通知的参数转化为Map
	 *
	 * @param request {HttpServletRequest}
	 * @return 转化后的Map
	 */
	public static Map<String, String> toMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
			String name = iter.next();
			String[] values = requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		return params;
	}

	/**
	 * 生活缴费查询账单
	 *
	 * @param orderType       支付宝订单类型
	 * @param merchantOrderNo 业务流水号
	 * @return {@link AlipayEbppBillGetResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayEbppBillGetResponse ebppBillGet(String orderType, String merchantOrderNo) throws AlipayApiException {
		AlipayEbppBillGetRequest request = new AlipayEbppBillGetRequest();
		request.setOrderType(orderType);
		request.setMerchantOrderNo(merchantOrderNo);
		return doExecute(request);
	}

	/**
	 * 生活缴费查询账单
	 *
	 * @param alipayClient    {@link AlipayClient}
	 * @param certModel       是否证书模式
	 * @param orderType       支付宝订单类型
	 * @param merchantOrderNo 业务流水号
	 * @return {@link AlipayEbppBillGetResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayEbppBillGetResponse ebppBillGet(AlipayClient alipayClient, Boolean certModel, String orderType, String merchantOrderNo) throws AlipayApiException {
		AlipayEbppBillGetRequest request = new AlipayEbppBillGetRequest();
		request.setOrderType(orderType);
		request.setMerchantOrderNo(merchantOrderNo);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * H5刷脸认证初始化
	 *
	 * @param model {@link ZolozIdentificationUserWebInitializeModel}
	 * @return {@link ZolozIdentificationUserWebInitializeResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozIdentificationUserWebInitializeResponse identificationUserWebInitialize(ZolozIdentificationUserWebInitializeModel model) throws AlipayApiException {
		ZolozIdentificationUserWebInitializeRequest request = new ZolozIdentificationUserWebInitializeRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * H5刷脸认证初始化
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link ZolozIdentificationUserWebInitializeModel}
	 * @return {@link ZolozIdentificationUserWebInitializeResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozIdentificationUserWebInitializeResponse identificationUserWebInitialize(AlipayClient alipayClient, Boolean certModel, ZolozIdentificationUserWebInitializeModel model) throws AlipayApiException {
		ZolozIdentificationUserWebInitializeRequest request = new ZolozIdentificationUserWebInitializeRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * H5刷脸认证查询
	 *
	 * @param model {@link ZolozIdentificationUserWebQueryModel}
	 * @return {@link ZolozIdentificationUserWebQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozIdentificationUserWebQueryResponse identificationUserWebInitialize(ZolozIdentificationUserWebQueryModel model) throws AlipayApiException {
		ZolozIdentificationUserWebQueryRequest request = new ZolozIdentificationUserWebQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * H5刷脸认证查询
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link ZolozIdentificationUserWebQueryModel}
	 * @return {@link ZolozIdentificationUserWebQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozIdentificationUserWebQueryResponse identificationUserWebInitialize(AlipayClient alipayClient, Boolean certModel, ZolozIdentificationUserWebQueryModel model) throws AlipayApiException {
		ZolozIdentificationUserWebQueryRequest request = new ZolozIdentificationUserWebQueryRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 热脸入库
	 *
	 * @param model {@link ZolozAuthenticationCustomerFacemanageCreateModel}
	 * @return {@link ZolozAuthenticationCustomerFacemanageCreateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozAuthenticationCustomerFacemanageCreateResponse authenticationCustomerFaceManageCreate(ZolozAuthenticationCustomerFacemanageCreateModel model) throws AlipayApiException {
		ZolozAuthenticationCustomerFacemanageCreateRequest request = new ZolozAuthenticationCustomerFacemanageCreateRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 热脸入库
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link ZolozAuthenticationCustomerFacemanageCreateModel}
	 * @return {@link ZolozAuthenticationCustomerFacemanageCreateResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozAuthenticationCustomerFacemanageCreateResponse authenticationCustomerFaceManageCreate(AlipayClient alipayClient, Boolean certModel, ZolozAuthenticationCustomerFacemanageCreateModel model) throws AlipayApiException {
		ZolozAuthenticationCustomerFacemanageCreateRequest request = new ZolozAuthenticationCustomerFacemanageCreateRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 热脸出库
	 *
	 * @param model {@link ZolozAuthenticationCustomerFacemanageDeleteModel}
	 * @return {@link ZolozAuthenticationCustomerFacemanageDeleteResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozAuthenticationCustomerFacemanageDeleteResponse authenticationCustomerFaceManageDelete(ZolozAuthenticationCustomerFacemanageDeleteModel model) throws AlipayApiException {
		ZolozAuthenticationCustomerFacemanageDeleteRequest request = new ZolozAuthenticationCustomerFacemanageDeleteRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 热脸出库
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link ZolozAuthenticationCustomerFacemanageDeleteModel}
	 * @return {@link ZolozAuthenticationCustomerFacemanageDeleteResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozAuthenticationCustomerFacemanageDeleteResponse authenticationCustomerFaceManageDelete(AlipayClient alipayClient, Boolean certModel, ZolozAuthenticationCustomerFacemanageDeleteModel model) throws AlipayApiException {
		ZolozAuthenticationCustomerFacemanageDeleteRequest request = new ZolozAuthenticationCustomerFacemanageDeleteRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 人脸 ftoken 查询消费接口
	 *
	 * @param model {@link ZolozAuthenticationCustomerFtokenQueryModel}
	 * @return {@link ZolozAuthenticationCustomerFtokenQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozAuthenticationCustomerFtokenQueryResponse authenticationCustomerFTokenQuery(ZolozAuthenticationCustomerFtokenQueryModel model) throws AlipayApiException {
		ZolozAuthenticationCustomerFtokenQueryRequest request = new ZolozAuthenticationCustomerFtokenQueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 人脸 ftoken 查询消费接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link ZolozAuthenticationCustomerFtokenQueryModel}
	 * @return {@link ZolozAuthenticationCustomerFtokenQueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozAuthenticationCustomerFtokenQueryResponse authenticationCustomerFTokenQuery(AlipayClient alipayClient, Boolean certModel, ZolozAuthenticationCustomerFtokenQueryModel model) throws AlipayApiException {
		ZolozAuthenticationCustomerFtokenQueryRequest request = new ZolozAuthenticationCustomerFtokenQueryRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}


	/**
	 * 人脸初始化刷脸付
	 *
	 * @param model {@link ZolozAuthenticationSmilepayInitializeModel}
	 * @return {@link ZolozAuthenticationSmilepayInitializeResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozAuthenticationSmilepayInitializeResponse authenticationSmilePayInitialize(ZolozAuthenticationSmilepayInitializeModel model) throws AlipayApiException {
		ZolozAuthenticationSmilepayInitializeRequest request = new ZolozAuthenticationSmilepayInitializeRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 人脸初始化刷脸付
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link ZolozAuthenticationSmilepayInitializeModel}
	 * @return {@link ZolozAuthenticationSmilepayInitializeResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozAuthenticationSmilepayInitializeResponse authenticationSmilePayInitialize(AlipayClient alipayClient, Boolean certModel, ZolozAuthenticationSmilepayInitializeModel model) throws AlipayApiException {
		ZolozAuthenticationSmilepayInitializeRequest request = new ZolozAuthenticationSmilepayInitializeRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 人脸初始化唤起zim
	 *
	 * @param model {@link ZolozAuthenticationCustomerSmilepayInitializeModel}
	 * @return {@link ZolozAuthenticationCustomerSmilepayInitializeResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozAuthenticationCustomerSmilepayInitializeResponse authenticationCustomerSmilePayInitialize(ZolozAuthenticationCustomerSmilepayInitializeModel model) throws AlipayApiException {
		ZolozAuthenticationCustomerSmilepayInitializeRequest request = new ZolozAuthenticationCustomerSmilepayInitializeRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 人脸初始化唤起zim
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link ZolozAuthenticationCustomerSmilepayInitializeModel}
	 * @return {@link ZolozAuthenticationCustomerSmilepayInitializeResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static ZolozAuthenticationCustomerSmilepayInitializeResponse authenticationCustomerSmilePayInitialize(AlipayClient alipayClient, Boolean certModel, ZolozAuthenticationCustomerSmilepayInitializeModel model) throws AlipayApiException {
		ZolozAuthenticationCustomerSmilepayInitializeRequest request = new ZolozAuthenticationCustomerSmilepayInitializeRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 生态激励项目ISV代签约接口
	 *
	 * @return {@link AlipayCommerceAdContractSignResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayCommerceAdContractSignResponse commerceAdContractSign() throws AlipayApiException {
		AlipayCommerceAdContractSignRequest request = new AlipayCommerceAdContractSignRequest();
		return doExecute(request);
	}

	/**
	 * 生态激励项目ISV代签约接口
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @return {@link AlipayCommerceAdContractSignResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayCommerceAdContractSignResponse commerceAdContractSign(AlipayClient alipayClient, Boolean certModel) throws AlipayApiException {
		AlipayCommerceAdContractSignRequest request = new AlipayCommerceAdContractSignRequest();
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 分账关系绑定
	 *
	 * @param model {@link AlipayTradeRoyaltyRelationBindModel}
	 * @return {@link AlipayTradeRoyaltyRelationBindResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeRoyaltyRelationBindResponse tradeRoyaltyRelationBind(AlipayTradeRoyaltyRelationBindModel model) throws AlipayApiException {
		AlipayTradeRoyaltyRelationBindRequest request = new AlipayTradeRoyaltyRelationBindRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 分账关系绑定
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradeRoyaltyRelationBindModel}
	 * @return {@link AlipayTradeRoyaltyRelationBindResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeRoyaltyRelationBindResponse tradeRoyaltyRelationBind(AlipayClient alipayClient, Boolean certModel, AlipayTradeRoyaltyRelationBindModel model) throws AlipayApiException {
		AlipayTradeRoyaltyRelationBindRequest request = new AlipayTradeRoyaltyRelationBindRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 分账关系解绑
	 *
	 * @param model {@link AlipayTradeRoyaltyRelationUnbindModel}
	 * @return {@link AlipayTradeRoyaltyRelationUnbindResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeRoyaltyRelationUnbindResponse tradeRoyaltyRelationUnBind(AlipayTradeRoyaltyRelationUnbindModel model) throws AlipayApiException {
		AlipayTradeRoyaltyRelationUnbindRequest request = new AlipayTradeRoyaltyRelationUnbindRequest();
		request.setBizModel(model);
		return doExecute(request);
	}

	/**
	 * 分账关系解绑
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradeRoyaltyRelationUnbindModel}
	 * @return {@link AlipayTradeRoyaltyRelationUnbindResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeRoyaltyRelationUnbindResponse tradeRoyaltyRelationUnBind(AlipayClient alipayClient, Boolean certModel, AlipayTradeRoyaltyRelationUnbindModel model) throws AlipayApiException {
		AlipayTradeRoyaltyRelationUnbindRequest request = new AlipayTradeRoyaltyRelationUnbindRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

	/**
	 * 分账关系查询
	 *
	 * @param model {@link AlipayTradeRoyaltyRelationBatchqueryModel}
	 * @return {@link AlipayTradeRoyaltyRelationBatchqueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeRoyaltyRelationBatchqueryResponse tradeRoyaltyRelationBatchQuery(AlipayTradeRoyaltyRelationBatchqueryModel model) throws AlipayApiException {
		AlipayTradeRoyaltyRelationBatchqueryRequest request = new AlipayTradeRoyaltyRelationBatchqueryRequest();
		request.setBizModel(model);
		return doExecute(request);
	}


	/**
	 * 分账关系查询
	 *
	 * @param alipayClient {@link AlipayClient}
	 * @param certModel    是否证书模式
	 * @param model        {@link AlipayTradeRoyaltyRelationBatchqueryModel}
	 * @return {@link AlipayTradeRoyaltyRelationBatchqueryResponse}
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public static AlipayTradeRoyaltyRelationBatchqueryResponse tradeRoyaltyRelationBatchQuery(AlipayClient alipayClient, Boolean certModel, AlipayTradeRoyaltyRelationBatchqueryModel model) throws AlipayApiException {
		AlipayTradeRoyaltyRelationBatchqueryRequest request = new AlipayTradeRoyaltyRelationBatchqueryRequest();
		request.setBizModel(model);
		return doExecute(alipayClient, certModel, request);
	}

}
