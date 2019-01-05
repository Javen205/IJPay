package com.jpay.alipay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.jpay.ext.kit.DateKit;

/**
 * @author Javen 2017年5月20日
 */
public class AliPayApi {
	/**
	 * APP支付
	 * 
	 * @param model
	 *            {AlipayTradeAppPayModel}
	 * @param notifyUrl
	 *            异步通知URL
	 * @return {AlipayTradeAppPayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeAppPayResponse appPayToResponse(AlipayTradeAppPayModel model, String notifyUrl)
			throws AlipayApiException {
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		// 这里和普通的接口调用不同，使用的是sdkExecute
		AlipayTradeAppPayResponse response = AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient()
				.sdkExecute(request);
		return response;
	}

	/**
	 * WAP支付
	 * 
	 * @param response
	 *            {HttpServletResponse}
	 * @param model
	 *            {AlipayTradeWapPayModel}
	 * @param returnUrl
	 *            异步通知URL
	 * @param notifyUrl
	 *            同步通知URL
	 * @throws {AlipayApiException}
	 * @throws {IOException}
	 */
	public static void wapPay(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl,
			String notifyUrl) throws AlipayApiException, IOException {
		String form = wapPayStr(response, model, returnUrl, notifyUrl);
		HttpServletResponse httpResponse = response;
		httpResponse.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
		httpResponse.getWriter().flush();
	}

	/**
	 * WAP支付
	 * 
	 * @param response
	 *            {HttpServletResponse}
	 * @param model
	 *            {AlipayTradeWapPayModel}
	 * @param returnUrl
	 *            异步通知URL
	 * @param notifyUrl
	 *            同步通知URL
	 * @return {String}
	 * @throws {AlipayApiException}
	 * @throws {IOException}
	 */
	public static String wapPayStr(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl,
			String notifyUrl) throws AlipayApiException, IOException {
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
		alipayRequest.setReturnUrl(returnUrl);
		alipayRequest.setNotifyUrl(notifyUrl);// 在公共参数中设置回跳和通知地址
		alipayRequest.setBizModel(model);// 填充业务参数
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
	}

	/**
	 * 统一收单交易支付接口接口 适用于:条形码支付、声波支付等
	 * 
	 * @param model
	 *            {AlipayTradePayModel}
	 * @param notifyUrl
	 *            异步通知URL
	 * @return {AlipayTradePayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradePayResponse tradePayToResponse(AlipayTradePayModel model, String notifyUrl)
			throws AlipayApiException {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		request.setBizModel(model);// 填充业务参数
		request.setNotifyUrl(notifyUrl);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 统一收单交易支付接口接口 适用于:条形码支付、声波支付等
	 * 
	 * @param model
	 *            {AlipayTradePayModel}
	 * @param notifyUrl
	 *            异步通知URL
	 * @param appAuthToken
	 *            应用授权token
	 * @return {AlipayTradePayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradePayResponse tradePayToResponse(AlipayTradePayModel model, String notifyUrl,
			String appAuthToken) throws AlipayApiException {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request, null, appAuthToken);
	}

	/**
	 * 统一收单线下交易预创建 适用于：扫码支付等
	 * 
	 * @param model
	 *            {AlipayTradePrecreateModel}
	 * @param notifyUrl
	 *            异步通知URL
	 * @return {AlipayTradePrecreateResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayTradePrecreateModel model,
			String notifyUrl) throws AlipayApiException {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 单笔转账到支付宝账户
	 * 
	 * @param model
	 *            {AlipayFundTransToaccountTransferModel}
	 * @return {Boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean transfer(AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
		AlipayFundTransToaccountTransferResponse response = transferToResponse(model);
		String result = response.getBody();
		if (response.isSuccess()) {
			return true;
		} else {
			// 调用查询接口查询数据
			JSONObject jsonObject = JSONObject.parseObject(result);
			String out_biz_no = jsonObject.getJSONObject("alipay_fund_trans_toaccount_transfer_response")
					.getString("out_biz_no");
			AlipayFundTransOrderQueryModel queryModel = new AlipayFundTransOrderQueryModel();
			model.setOutBizNo(out_biz_no);
			boolean isSuccess = transferQuery(queryModel);
			if (isSuccess) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 单笔转账到支付宝账户
	 * 
	 * @param model
	 *            {AlipayFundTransToaccountTransferModel}
	 * @return {AlipayFundTransToaccountTransferResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundTransToaccountTransferResponse transferToResponse(
			AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 转账查询接口
	 * 
	 * @param model
	 *            {AlipayFundTransOrderQueryModel}
	 * @return {Boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean transferQuery(AlipayFundTransOrderQueryModel model) throws AlipayApiException {
		AlipayFundTransOrderQueryResponse response = transferQueryToResponse(model);
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	/**
	 * 转账查询接口
	 * 
	 * @param model
	 *            {AlipayFundTransOrderQueryModel}
	 * @return {AlipayFundTransOrderQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundTransOrderQueryResponse transferQueryToResponse(AlipayFundTransOrderQueryModel model)
			throws AlipayApiException {
		AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 统一收单线下交易查询接口
	 * 
	 * @param model
	 *            {AlipayTradeQueryModel}
	 * @return {Boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean isTradeQuery(AlipayTradeQueryModel model) throws AlipayApiException {
		AlipayTradeQueryResponse response = tradeQueryToResponse(model);
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	/**
	 * 统一收单线下交易查询接口
	 * 
	 * @param model
	 *            {AlipayTradeQueryModel}
	 * @return {AlipayTradeQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeQueryResponse tradeQueryToResponse(AlipayTradeQueryModel model) throws AlipayApiException {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 统一收单交易撤销接口
	 * 
	 * @param model
	 *            {AlipayTradeCancelModel}
	 * @return {Boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean isTradeCancel(AlipayTradeCancelModel model) throws AlipayApiException {
		AlipayTradeCancelResponse response = tradeCancelToResponse(model);
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	/**
	 * 统一收单交易撤销接口
	 * 
	 * @param model
	 *            {AlipayTradeCancelModel}
	 * @return {AlipayTradeCancelResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeCancelResponse tradeCancelToResponse(AlipayTradeCancelModel model)
			throws AlipayApiException {
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizModel(model);
		AlipayTradeCancelResponse response = AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
		return response;
	}

	/**
	 * 统一收单交易关闭接口
	 * 
	 * @param model
	 *            {AlipayTradeCloseModel}
	 * @return {Boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean isTradeClose(AlipayTradeCloseModel model) throws AlipayApiException {
		AlipayTradeCloseResponse response = tradeCloseToResponse(model);
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	/**
	 * 统一收单交易关闭接口
	 * 
	 * @param model
	 *            {AlipayTradeCloseModel}
	 * @return {AlipayTradeCloseResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeCloseResponse tradeCloseToResponse(AlipayTradeCloseModel model) throws AlipayApiException {
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);

	}

	/**
	 * 统一收单交易创建接口
	 * 
	 * @param model
	 *            {AlipayTradeCreateModel}
	 * @param notifyUrl
	 *            异步通知URL
	 * @return {AlipayTradeCreateResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeCreateResponse tradeCreateToResponse(AlipayTradeCreateModel model, String notifyUrl)
			throws AlipayApiException {
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 统一收单交易退款接口
	 * 
	 * @param model
	 *            {AlipayTradeRefundModel}
	 * @return {AlipayTradeRefundResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayTradeRefundModel model)
			throws AlipayApiException {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 统一收单交易退款查询
	 * 
	 * @param model
	 *            {AlipayTradeFastpayRefundQueryModel}
	 * @return {AlipayTradeFastpayRefundQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeFastpayRefundQueryResponse tradeRefundQueryToResponse(
			AlipayTradeFastpayRefundQueryModel model) throws AlipayApiException {
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 统一收单交易退款查询
	 * 
	 * @param model
	 *            {AlipayTradeFastpayRefundQueryModel}
	 * @param app_auth_token
	 *            应用授权token
	 * @return {AlipayTradeFastpayRefundQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeFastpayRefundQueryResponse tradeRefundQueryToResponse(
			AlipayTradeFastpayRefundQueryModel model, String app_auth_token) throws AlipayApiException {
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request, null, app_auth_token);
	}

	/**
	 * 查询对账单下载地址
	 * 
	 * @param model
	 *            {AlipayDataDataserviceBillDownloadurlQueryModel}
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String billDownloadurlQuery(AlipayDataDataserviceBillDownloadurlQueryModel model)
			throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryResponse response = billDownloadurlQueryToResponse(model);
		return response.getBillDownloadUrl();
	}

	/**
	 * 查询对账单下载地址
	 * 
	 * @param model
	 *            {AlipayDataDataserviceBillDownloadurlQueryModel}
	 * @return {AlipayDataDataserviceBillDownloadurlQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadurlQueryToResponse(
			AlipayDataDataserviceBillDownloadurlQueryModel model) throws AlipayApiException {
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 统一收单交易结算接口
	 * 
	 * @param model
	 *            {AlipayTradeOrderSettleModel}
	 * @return {Boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean isTradeOrderSettle(AlipayTradeOrderSettleModel model) throws AlipayApiException {
		AlipayTradeOrderSettleResponse response = tradeOrderSettleToResponse(model);
		if (response.isSuccess()) {
			return true;
		}
		return false;
	}

	/**
	 * 统一收单交易结算接口
	 * 
	 * @param model
	 *            {AlipayTradeOrderSettleModel}
	 * @return {AlipayTradeOrderSettleResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeOrderSettleResponse tradeOrderSettleToResponse(AlipayTradeOrderSettleModel model)
			throws AlipayApiException {
		AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 电脑网站支付(PC支付)
	 * 
	 * @param httpResponse
	 *            {HttpServletResponse}
	 * @param model
	 *            {AlipayTradePagePayModel}
	 * @param notifyUrl
	 *            异步通知URL
	 * @param returnUrl
	 *            同步通知URL
	 * @throws {AlipayApiException}
	 * @throws {IOException}
	 */
	public static void tradePage(HttpServletResponse httpResponse, AlipayTradePagePayModel model, String notifyUrl,
			String returnUrl) throws AlipayApiException, IOException {
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		String form = AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().pageExecute(request).getBody();// 调用SDK生成表单
		httpResponse.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
		httpResponse.getWriter().flush();
		httpResponse.getWriter().close();
	}

	/**
	 * 资金预授权冻结接口
	 * 
	 * @param model
	 *            {AlipayFundAuthOrderFreezeModel}
	 * @return {AlipayFundAuthOrderFreezeResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOrderFreezeResponse authOrderFreezeToResponse(AlipayFundAuthOrderFreezeModel model)
			throws AlipayApiException {
		AlipayFundAuthOrderFreezeRequest request = new AlipayFundAuthOrderFreezeRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 资金授权解冻接口
	 * 
	 * @param model
	 *            {AlipayFundAuthOrderUnfreezeModel}
	 * @return {AlipayFundAuthOrderUnfreezeResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOrderUnfreezeResponse authOrderUnfreezeToResponse(
			AlipayFundAuthOrderUnfreezeModel model) throws AlipayApiException {
		AlipayFundAuthOrderUnfreezeRequest request = new AlipayFundAuthOrderUnfreezeRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 资金预授权冻结接口
	 * 
	 * @param model
	 *            {AlipayFundAuthOrderVoucherCreateModel}
	 * @return {AlipayFundAuthOrderVoucherCreateResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOrderVoucherCreateResponse authOrderVoucherCreateToResponse(
			AlipayFundAuthOrderVoucherCreateModel model) throws AlipayApiException {
		AlipayFundAuthOrderVoucherCreateRequest request = new AlipayFundAuthOrderVoucherCreateRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 资金授权撤销接口
	 * 
	 * @param model
	 *            {AlipayFundAuthOperationCancelModel}
	 * @return {AlipayFundAuthOperationCancelResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOperationCancelResponse authOperationCancelToResponse(
			AlipayFundAuthOperationCancelModel model) throws AlipayApiException {
		AlipayFundAuthOperationCancelRequest request = new AlipayFundAuthOperationCancelRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 资金授权操作查询接口
	 * 
	 * @param model
	 *            {AlipayFundAuthOperationDetailQueryModel}
	 * @return {AlipayFundAuthOperationDetailQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOperationDetailQueryResponse authOperationDetailQueryToResponse(
			AlipayFundAuthOperationDetailQueryModel model) throws AlipayApiException {
		AlipayFundAuthOperationDetailQueryRequest request = new AlipayFundAuthOperationDetailQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 红包无线支付接口
	 * 
	 * @param model
	 *            {AlipayFundCouponOrderAppPayModel}
	 * @return {AlipayFundCouponOrderAppPayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderAppPayResponse fundCouponOrderAppPayToResponse(
			AlipayFundCouponOrderAppPayModel model) throws AlipayApiException {
		AlipayFundCouponOrderAppPayRequest request = new AlipayFundCouponOrderAppPayRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 红包页面支付接口
	 * 
	 * @param model
	 *            {AlipayFundCouponOrderPagePayModel}
	 * @return {AlipayFundCouponOrderPagePayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderPagePayResponse fundCouponOrderPagePayToResponse(
			AlipayFundCouponOrderPagePayModel model) throws AlipayApiException {
		AlipayFundCouponOrderPagePayRequest request = new AlipayFundCouponOrderPagePayRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 红包协议支付接口
	 * 
	 * @param model
	 *            {AlipayFundCouponOrderAgreementPayModel}
	 * @return {AlipayFundCouponOrderAgreementPayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderAgreementPayResponse fundCouponOrderAgreementPayToResponse(
			AlipayFundCouponOrderAgreementPayModel model) throws AlipayApiException {
		AlipayFundCouponOrderAgreementPayRequest request = new AlipayFundCouponOrderAgreementPayRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 红包打款接口
	 * 
	 * @param model
	 *            {AlipayFundCouponOrderDisburseModel}
	 * @return {AlipayFundCouponOrderDisburseResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderDisburseResponse fundCouponOrderDisburseToResponse(
			AlipayFundCouponOrderDisburseModel model) throws AlipayApiException {
		AlipayFundCouponOrderDisburseRequest request = new AlipayFundCouponOrderDisburseRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 红包退回接口
	 * 
	 * @param model
	 *            {AlipayFundCouponOrderRefundModel}
	 * @return {AlipayFundCouponOrderRefundResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderRefundResponse fundCouponOrderRefundToResponse(
			AlipayFundCouponOrderRefundModel model) throws AlipayApiException {
		AlipayFundCouponOrderRefundRequest request = new AlipayFundCouponOrderRefundRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 红包退回接口
	 * 
	 * @param model
	 *            {AlipayFundCouponOperationQueryModel}
	 * @return {AlipayFundCouponOperationQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOperationQueryResponse fundCouponOperationQueryToResponse(
			AlipayFundCouponOperationQueryModel model) throws AlipayApiException {
		AlipayFundCouponOperationQueryRequest request = new AlipayFundCouponOperationQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 应用授权URL拼装
	 * 
	 * @param appId
	 * @param redirectUri
	 * @return 应用授权URL
	 * @throws {UnsupportedEncodingException}
	 */
	public static String getOauth2Url(String appId, String redirectUri) throws UnsupportedEncodingException {
		return new StringBuffer().append("https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=").append(appId)
				.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8")).toString();
	}

	/**
	 * 使用app_auth_code换取app_auth_token
	 * 
	 * @param model
	 *            {AlipayOpenAuthTokenAppModel}
	 * @return {AlipayOpenAuthTokenAppResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayOpenAuthTokenAppResponse openAuthTokenAppToResponse(AlipayOpenAuthTokenAppModel model)
			throws AlipayApiException {
		AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 查询授权信息
	 * 
	 * @param model
	 *            {AlipayOpenAuthTokenAppQueryModel}
	 * @return {AlipayOpenAuthTokenAppQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayOpenAuthTokenAppQueryResponse openAuthTokenAppQueryToResponse(
			AlipayOpenAuthTokenAppQueryModel model) throws AlipayApiException {
		AlipayOpenAuthTokenAppQueryRequest request = new AlipayOpenAuthTokenAppQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 地铁购票发码
	 * 
	 * @param model
	 *            {AlipayCommerceCityfacilitatorVoucherGenerateModel}
	 * @return {AlipayCommerceCityfacilitatorVoucherGenerateResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayCommerceCityfacilitatorVoucherGenerateResponse voucherGenerateToResponse(
			AlipayCommerceCityfacilitatorVoucherGenerateModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherGenerateRequest request = new AlipayCommerceCityfacilitatorVoucherGenerateRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 地铁购票发码退款
	 * 
	 * @param model
	 *            {AlipayCommerceCityfacilitatorVoucherRefundModel}
	 * @return {AlipayCommerceCityfacilitatorVoucherRefundResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayCommerceCityfacilitatorVoucherRefundResponse metroRefundToResponse(
			AlipayCommerceCityfacilitatorVoucherRefundModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherRefundRequest request = new AlipayCommerceCityfacilitatorVoucherRefundRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 地铁车站数据查询
	 * 
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayCommerceCityfacilitatorStationQueryResponse stationQueryToResponse(
			AlipayCommerceCityfacilitatorStationQueryModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorStationQueryRequest request = new AlipayCommerceCityfacilitatorStationQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 核销码批量查询
	 * 
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayCommerceCityfacilitatorVoucherBatchqueryResponse voucherBatchqueryToResponse(
			AlipayCommerceCityfacilitatorVoucherBatchqueryModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherBatchqueryRequest request = new AlipayCommerceCityfacilitatorVoucherBatchqueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}

	/**
	 * 支付宝提供给商户的服务接入网关URL(新)
	 */
	private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";

	public static void batchTrans(Map<String, String> params, String private_key, String sign_type,
			HttpServletResponse response) throws IOException {
		params.put("service", "batch_trans_notify");
		params.put("_input_charset", "UTF-8");
		params.put("pay_date", DateKit.toStr(new Date(), DateKit.YYYYMMDD));
		Map<String, String> param = AlipayCore.buildRequestPara(params, private_key, sign_type);
		response.sendRedirect(ALIPAY_GATEWAY_NEW.concat(AlipayCore.createLinkString(param)));
	}

	/**
	 * 将异步通知的参数转化为Map
	 * 
	 * @param request
	 *            {HttpServletRequest}
	 * @return {Map<String, String>}
	 */
	public static Map<String, String> toMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		return params;
	}

}
