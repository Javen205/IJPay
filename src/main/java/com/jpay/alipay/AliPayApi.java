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
import com.alipay.api.request.AlipayCommerceCityfacilitatorStationQueryRequest;
import com.alipay.api.request.AlipayCommerceCityfacilitatorVoucherBatchqueryRequest;
import com.alipay.api.request.AlipayCommerceCityfacilitatorVoucherGenerateRequest;
import com.alipay.api.request.AlipayCommerceCityfacilitatorVoucherRefundRequest;
import com.alipay.api.request.AlipayDataDataserviceBillDownloadurlQueryRequest;
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
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayOpenAuthTokenAppQueryRequest;
import com.alipay.api.request.AlipayOpenAuthTokenAppRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeOrderSettleRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayCommerceCityfacilitatorStationQueryResponse;
import com.alipay.api.response.AlipayCommerceCityfacilitatorVoucherBatchqueryResponse;
import com.alipay.api.response.AlipayCommerceCityfacilitatorVoucherGenerateResponse;
import com.alipay.api.response.AlipayCommerceCityfacilitatorVoucherRefundResponse;
import com.alipay.api.response.AlipayDataDataserviceBillDownloadurlQueryResponse;
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
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayOpenAuthTokenAppQueryResponse;
import com.alipay.api.response.AlipayOpenAuthTokenAppResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeOrderSettleResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.jpay.ext.kit.DateKit;
/**
 * @author Javen
 * 2017年5月20日
 */
public class AliPayApi {

	/**
	 * APP支付
	 * @param model
	 * @param notifyUrl
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String startAppPay(AlipayTradeAppPayModel model, String notifyUrl) throws AlipayApiException{
		AlipayTradeAppPayResponse response = appPayToResponse(model,notifyUrl);
		return response.getBody();
	}
	
	/**
	 * APP支付
	 * https://doc.open.alipay.com/docs/doc.htm?treeId=54&articleId=106370&docType=1
	 * @param model
	 * @param notifyUrl
	 * @return {AlipayTradeAppPayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeAppPayResponse appPayToResponse(AlipayTradeAppPayModel model, String notifyUrl) throws AlipayApiException{
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		//这里和普通的接口调用不同，使用的是sdkExecute
        AlipayTradeAppPayResponse response = AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().sdkExecute(request);
		return response;
	}
	
	/**
	 * APP支付
	 * https://doc.open.alipay.com/docs/doc.htm?treeId=54&articleId=106370&docType=1
	 * @param model
	 * @param notifyUrl
	 * @return {AlipayTradeAppPayResponse}
	 * @throws {AlipayApiException}
	 */
	@Deprecated
	public static AlipayTradeAppPayResponse appPay(AlipayTradeAppPayModel model, String notifyUrl) throws AlipayApiException{
		return appPayToResponse(model, notifyUrl);
	}

	/**
	 * WAP支付
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.dfHHR3&treeId=203&articleId=105285&docType=1
	 * @param response
	 * @param model
	 * @param returnUrl
	 * @param notifyUrl
	 * @throws {AlipayApiException}
	 * @throws IOException
	 */
	public static void wapPay(HttpServletResponse response,AlipayTradeWapPayModel model,String returnUrl,String notifyUrl) throws AlipayApiException, IOException {
		String form = wapPayStr(response, model, returnUrl, notifyUrl);
		HttpServletResponse httpResponse = response;
		httpResponse.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
		httpResponse.getWriter().flush();
	}
	/**
	 * WAP支付
	 * @param response
	 * @param model
	 * @param returnUrl
	 * @param notifyUrl
	 * @return {String}
	 * @throws {AlipayApiException}
	 * @throws IOException
	 */
	public static String wapPayStr(HttpServletResponse response,AlipayTradeWapPayModel model,String returnUrl,String notifyUrl) throws AlipayApiException, IOException {
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
		alipayRequest.setReturnUrl(returnUrl);
		alipayRequest.setNotifyUrl(notifyUrl);// 在公共参数中设置回跳和通知地址
		alipayRequest.setBizModel(model);// 填充业务参数
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
	}
	
	/**
	 * WAP支付
	 * @param response
	 * @param model
	 * @param returnUrl
	 * @param notifyUrl
	 * @return {String}
	 * @throws {AlipayApiException}
	 * @throws IOException
	 */
	@Deprecated
	public static String wapPayToString(HttpServletResponse response,AlipayTradeWapPayModel model,String returnUrl,String notifyUrl) throws AlipayApiException, IOException {
		return wapPayStr(response, model, returnUrl, notifyUrl);				
	}

	/**
	 * 条形码支付、声波支付
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.XVqALk&apiId=850&docType=4
	 * @param model
	 * @param notifyUrl
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String tradePay(AlipayTradePayModel model, String notifyUrl) throws AlipayApiException {
		AlipayTradePayResponse response = tradePayToResponse(model,notifyUrl);
		return response.getBody();
	}
	/**
	 * 交易支付接口
	 * 条形码支付、声波支付
	 * @param model
	 * @param notifyUrl
	 * @return {AlipayTradePayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradePayResponse tradePayToResponse(AlipayTradePayModel model, String notifyUrl) throws AlipayApiException{
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		request.setBizModel(model);// 填充业务参数
		request.setNotifyUrl(notifyUrl);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request); // 通过AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient()调用API，获得对应的response类
	}
	
	
	
	/**
	 * 统一收单线下交易预创建
	 * 扫码支付
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.i0UVZn&treeId=193&articleId=105170&docType=1#s4
	 * @param model
	 * @param notifyUrl
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String tradePrecreatePay(AlipayTradePrecreateModel model, String notifyUrl) throws AlipayApiException{
		AlipayTradePrecreateResponse response = tradePrecreatePayToResponse(model,notifyUrl);
		return response.getBody();
	}
	/**
	 * 扫码支付
	 * @param model
	 * @param notifyUrl
	 * @return {AlipayTradePrecreateResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayTradePrecreateModel model, String notifyUrl) throws AlipayApiException{
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 单笔转账到支付宝账户
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.54Ty29&treeId=193&articleId=106236&docType=1
	 * @param model
	 * @return {boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean transfer(AlipayFundTransToaccountTransferModel model) throws AlipayApiException{
		AlipayFundTransToaccountTransferResponse response = transferToResponse(model);
		String result = response.getBody();
		if (response.isSuccess()) {
			return true;
		} else {
			//调用查询接口查询数据
			JSONObject jsonObject = JSONObject.parseObject(result);
			String out_biz_no = jsonObject.getJSONObject("alipay_fund_trans_toaccount_transfer_response").getString("out_biz_no");
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
	 * @param model
	 * @return {AlipayFundTransToaccountTransferResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundTransToaccountTransferResponse transferToResponse(AlipayFundTransToaccountTransferModel model) throws AlipayApiException{
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 转账查询接口
	 * @param model
	 * @return {boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean transferQuery(AlipayFundTransOrderQueryModel model) throws AlipayApiException{
		AlipayFundTransOrderQueryResponse response = transferQueryToResponse(model);
		if(response.isSuccess()){
			return true;
		}
		return false;
	}
	/**
	 * 转账查询接口
	 * @param model
	 * @return {AlipayFundTransOrderQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundTransOrderQueryResponse transferQueryToResponse(AlipayFundTransOrderQueryModel model) throws AlipayApiException{
		AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 交易查询接口
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.8H2JzG&docType=4&apiId=757
	 * @param model
	 * @return {boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean isTradeQuery(AlipayTradeQueryModel model) throws AlipayApiException{
		AlipayTradeQueryResponse response = tradeQueryToResponse(model);
		if(response.isSuccess()){
			return true;
		}
		return false;
	}
	/**
	 * 交易查询接口
	 * @param model
	 * @return {AlipayTradeQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeQueryResponse  tradeQueryToResponse(AlipayTradeQueryModel model) throws AlipayApiException{
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 交易查询接口
	 * @param model
	 * @return {AlipayTradeQueryResponse}
	 * @throws {AlipayApiException}
	 */
	@Deprecated
	public static AlipayTradeQueryResponse  tradeQuery(AlipayTradeQueryModel model) throws AlipayApiException{
		return tradeQueryToResponse(model);
	}
	
	
	/**
	 * 交易撤销接口
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.XInh6e&docType=4&apiId=866
	 * @param model
	 * @return {boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean isTradeCancel(AlipayTradeCancelModel model) throws AlipayApiException{
		AlipayTradeCancelResponse response = tradeCancelToResponse(model);
		if(response.isSuccess()){
			return true;
		}
		return false;
	}
	/**
	 * 交易撤销接口
	 * @param model
	 * @return {AlipayTradeCancelResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeCancelResponse tradeCancelToResponse(AlipayTradeCancelModel model) throws AlipayApiException{
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.setBizModel(model);
		AlipayTradeCancelResponse response = AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
		return response;
	}
	/**
	 * 交易撤销接口
	 * @param model
	 * @return {AlipayTradeCancelResponse}
	 * @throws {AlipayApiException}
	 */
	@Deprecated
	public static AlipayTradeCancelResponse tradeCancel(AlipayTradeCancelModel model) throws AlipayApiException{
		return tradeCancelToResponse(model);
	}
	/**
	 * 关闭订单
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.21yRUe&apiId=1058&docType=4
	 * @param model
	 * @return {boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean isTradeClose(AlipayTradeCloseModel model) throws AlipayApiException{
		AlipayTradeCloseResponse response = tradeCloseToResponse(model);
		if(response.isSuccess()){
			return true;
		}
		return false;
	}
	/**
	 * 关闭订单
	 * @param model
	 * @return {AlipayTradeCloseResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeCloseResponse tradeCloseToResponse(AlipayTradeCloseModel model) throws AlipayApiException{
		AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
		
	}
	/**
	 * 关闭订单
	 * @param model
	 * @return {AlipayTradeCloseResponse}
	 * @throws {AlipayApiException}
	 */
	@Deprecated
	public static AlipayTradeCloseResponse tradeClose(AlipayTradeCloseModel model) throws AlipayApiException{
		return tradeCloseToResponse(model);
		
	}
	/**
	 * 统一收单交易创建接口
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.21yRUe&apiId=1046&docType=4
	 * @param model
	 * @param notifyUrl
	 * @return {AlipayTradeCreateResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeCreateResponse tradeCreateToResponse(AlipayTradeCreateModel model, String notifyUrl) throws AlipayApiException{
		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	
	/**
	 * 退款
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.SAyEeI&docType=4&apiId=759
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String tradeRefund(AlipayTradeRefundModel model) throws AlipayApiException{
		AlipayTradeRefundResponse response = tradeRefundToResponse(model);
		return response.getBody();
	}
	/**
	 * 退款
	 * @param model
	 * @return {AlipayTradeRefundResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayTradeRefundModel model) throws AlipayApiException{
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 退款查询
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7629065.0.0.KQeTSa&apiId=1049&docType=4
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String tradeRefundQuery(AlipayTradeFastpayRefundQueryModel model) throws AlipayApiException{
		AlipayTradeFastpayRefundQueryResponse response = tradeRefundQueryToResponse(model);
		return response.getBody();
	}
	/**
	 * 退款查询
	 * @param model
	 * @return {AlipayTradeFastpayRefundQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeFastpayRefundQueryResponse tradeRefundQueryToResponse(AlipayTradeFastpayRefundQueryModel model) throws AlipayApiException{
		AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	
	/**
	 * 查询对账单下载地址
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String billDownloadurlQuery(AlipayDataDataserviceBillDownloadurlQueryModel model) throws AlipayApiException{
		AlipayDataDataserviceBillDownloadurlQueryResponse response =  billDownloadurlQueryToResponse(model);
		return response.getBillDownloadUrl();
	}
	/**
	 * 查询对账单下载地址
	 * @param model
	 * @return {AlipayDataDataserviceBillDownloadurlQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayDataDataserviceBillDownloadurlQueryResponse  billDownloadurlQueryToResponse (AlipayDataDataserviceBillDownloadurlQueryModel model) throws AlipayApiException{
		AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	
	/**
	 * 交易结算接口
	 * https://doc.open.alipay.com/docs/api.htm?spm=a219a.7395905.0.0.nl0RS3&docType=4&apiId=1147
	 * @param model
	 * @return {boolean}
	 * @throws {AlipayApiException}
	 */
	public static boolean isTradeOrderSettle(AlipayTradeOrderSettleModel model) throws AlipayApiException{
		AlipayTradeOrderSettleResponse  response  = tradeOrderSettleToResponse(model);
		if(response.isSuccess()){
			return true;
		}
		return false;
	}
	/**
	 * 交易结算接口
	 * @param model
	 * @return {AlipayTradeOrderSettleResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayTradeOrderSettleResponse tradeOrderSettleToResponse(AlipayTradeOrderSettleModel model) throws AlipayApiException{
		AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 交易结算接口
	 * @param model
	 * @return {AlipayTradeOrderSettleResponse}
	 * @throws {AlipayApiException}
	 */
	@Deprecated
	public static AlipayTradeOrderSettleResponse tradeOrderSettle(AlipayTradeOrderSettleModel model) throws AlipayApiException{
		return tradeOrderSettleToResponse(model);
	}
	
	/**
	 * 电脑网站支付(PC支付)
	 * @param httpResponse
	 * @param model
	 * @param notifyUrl
	 * @param returnUrl
	 * @throws {AlipayApiException}
	 * @throws IOException
	 */
	public static void tradePage(HttpServletResponse httpResponse, AlipayTradePagePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException{
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setBizModel(model);
		request.setNotifyUrl(notifyUrl);
		request.setReturnUrl(returnUrl);
		String form  = AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().pageExecute(request).getBody();//调用SDK生成表单
		httpResponse.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
		httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
	    httpResponse.getWriter().flush();
	    httpResponse.getWriter().close();
	}
	/**
	 * 资金预授权冻结接口
	 * https://docs.open.alipay.com/318/106384/
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String authOrderFreeze(AlipayFundAuthOrderFreezeModel model) throws AlipayApiException{
		AlipayFundAuthOrderFreezeResponse response = authOrderFreezeToResponse(model);
		return response.getBody();
	}
	/**
	 * 资金预授权冻结接口
	 * @param model
	 * @return {AlipayFundAuthOrderFreezeResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOrderFreezeResponse authOrderFreezeToResponse(AlipayFundAuthOrderFreezeModel model) throws AlipayApiException{
		AlipayFundAuthOrderFreezeRequest request = new AlipayFundAuthOrderFreezeRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 资金授权解冻接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String authOrderUnfreeze(AlipayFundAuthOrderUnfreezeModel model) throws AlipayApiException{
		AlipayFundAuthOrderUnfreezeResponse response = authOrderUnfreezeToResponse(model);
		return response.getBody();
	}
	
	/**
	 * 资金授权解冻接口
	 * @param model
	 * @return {AlipayFundAuthOrderUnfreezeResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOrderUnfreezeResponse authOrderUnfreezeToResponse(AlipayFundAuthOrderUnfreezeModel model) throws AlipayApiException{
		AlipayFundAuthOrderUnfreezeRequest request = new AlipayFundAuthOrderUnfreezeRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 资金授权发码接口
	 * https://docs.open.alipay.com/318/106384/
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String authOrderVoucherCreate(AlipayFundAuthOrderVoucherCreateModel model) throws AlipayApiException{
		AlipayFundAuthOrderVoucherCreateResponse response = authOrderVoucherCreateToResponse(model);
		return response.getBody();
	}
	/**
	 * 资金预授权冻结接口
	 * @param model
	 * @return {AlipayFundAuthOrderVoucherCreateResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOrderVoucherCreateResponse authOrderVoucherCreateToResponse(AlipayFundAuthOrderVoucherCreateModel model) throws AlipayApiException{
		AlipayFundAuthOrderVoucherCreateRequest request = new AlipayFundAuthOrderVoucherCreateRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 资金授权撤销接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String authOperationCancel(AlipayFundAuthOperationCancelModel model) throws AlipayApiException{
		AlipayFundAuthOperationCancelResponse response = authOperationCancelToResponse(model);
		return response.getBody();
	}
	
	/**
	 * 资金授权撤销接口
	 * @param model
	 * @return {AlipayFundAuthOperationCancelResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOperationCancelResponse authOperationCancelToResponse(AlipayFundAuthOperationCancelModel model) throws AlipayApiException{
		AlipayFundAuthOperationCancelRequest request = new AlipayFundAuthOperationCancelRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	
	/**
	 * 资金授权操作查询接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String authOperationDetailQuery(AlipayFundAuthOperationDetailQueryModel model) throws AlipayApiException{
		AlipayFundAuthOperationDetailQueryResponse response = authOperationDetailQueryToResponse(model);
		return response.getBody();
	}
	
	/**
	 * 资金授权操作查询接口
	 * @param model
	 * @return {AlipayFundAuthOperationDetailQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundAuthOperationDetailQueryResponse authOperationDetailQueryToResponse(AlipayFundAuthOperationDetailQueryModel model) throws AlipayApiException{
		AlipayFundAuthOperationDetailQueryRequest request = new AlipayFundAuthOperationDetailQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 红包无线支付接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String fundCouponOrderAppPay(AlipayFundCouponOrderAppPayModel model) throws AlipayApiException{
		AlipayFundCouponOrderAppPayResponse response = fundCouponOrderAppPayToResponse(model);
		return response.getBody();
	}
	/**
	 * 红包无线支付接口
	 * @param model
	 * @return {AlipayFundCouponOrderAppPayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderAppPayResponse fundCouponOrderAppPayToResponse(AlipayFundCouponOrderAppPayModel model) throws AlipayApiException{
		AlipayFundCouponOrderAppPayRequest request = new AlipayFundCouponOrderAppPayRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	
	/**
	 * 红包页面支付接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String fundCouponOrderPagePay(AlipayFundCouponOrderPagePayModel model) throws AlipayApiException{
		AlipayFundCouponOrderPagePayResponse response = fundCouponOrderPagePayToResponse(model);
		return response.getBody();
	}
	/**
	 * 红包页面支付接口
	 * @param model
	 * @return {AlipayFundCouponOrderPagePayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderPagePayResponse fundCouponOrderPagePayToResponse(AlipayFundCouponOrderPagePayModel model) throws AlipayApiException{
		AlipayFundCouponOrderPagePayRequest request = new AlipayFundCouponOrderPagePayRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 红包协议支付接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String fundCouponOrderAgreementPay(AlipayFundCouponOrderAgreementPayModel model) throws AlipayApiException{
		AlipayFundCouponOrderAgreementPayResponse response = fundCouponOrderAgreementPayToResponse(model);
		return response.getBody();
	}
	/**
	 * 红包协议支付接口
	 * @param model
	 * @return {AlipayFundCouponOrderAgreementPayResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderAgreementPayResponse fundCouponOrderAgreementPayToResponse(AlipayFundCouponOrderAgreementPayModel model) throws AlipayApiException{
		AlipayFundCouponOrderAgreementPayRequest request = new AlipayFundCouponOrderAgreementPayRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 红包打款接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String fundCouponOrderDisburse(AlipayFundCouponOrderDisburseModel model) throws AlipayApiException{
		AlipayFundCouponOrderDisburseResponse response = fundCouponOrderDisburseToResponse(model);
		return response.getBody();
	}
	/**
	 * 红包打款接口
	 * @param model
	 * @return {AlipayFundCouponOrderDisburseResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderDisburseResponse fundCouponOrderDisburseToResponse(AlipayFundCouponOrderDisburseModel model) throws AlipayApiException{
		AlipayFundCouponOrderDisburseRequest request = new AlipayFundCouponOrderDisburseRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 红包退回接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String fundCouponOrderRefund(AlipayFundCouponOrderRefundModel model) throws AlipayApiException{
		AlipayFundCouponOrderRefundResponse response = fundCouponOrderRefundToResponse(model);
		return response.getBody();
	}
	/**
	 * 红包退回接口
	 * @param model
	 * @return {AlipayFundCouponOrderRefundResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOrderRefundResponse fundCouponOrderRefundToResponse(AlipayFundCouponOrderRefundModel model) throws AlipayApiException{
		AlipayFundCouponOrderRefundRequest request = new AlipayFundCouponOrderRefundRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 红包退回接口
	 * @param model
	 * @return {String}
	 * @throws {AlipayApiException}
	 */
	public static String fundCouponOperationQuery(AlipayFundCouponOperationQueryModel model) throws AlipayApiException{
		AlipayFundCouponOperationQueryResponse response = fundCouponOperationQueryToResponse(model);
		return response.getBody();
	}
	/**
	 * 红包退回接口
	 * @param model
	 * @return {AlipayFundCouponOperationQueryResponse}
	 * @throws {AlipayApiException}
	 */
	public static AlipayFundCouponOperationQueryResponse fundCouponOperationQueryToResponse(AlipayFundCouponOperationQueryModel model) throws AlipayApiException{
		AlipayFundCouponOperationQueryRequest request = new AlipayFundCouponOperationQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 应用授权URL拼装
	 * @param appId
	 * @param redirectUri
	 * @return 应用授权URL
	 * @throws UnsupportedEncodingException
	 */
	public static String getOauth2Url(String appId,String redirectUri) throws UnsupportedEncodingException{
		return new StringBuffer().append("https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=")
				.append(appId)
				.append("&redirect_uri=")
				.append(URLEncoder.encode(redirectUri, "UTF-8"))
				.toString();
	}
	/**
	 * 使用app_auth_code换取app_auth_token
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayOpenAuthTokenAppResponse openAuthTokenAppToResponse(AlipayOpenAuthTokenAppModel model) throws AlipayApiException {
		AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 使用app_auth_code换取app_auth_token
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static String openAuthTokenApp(AlipayOpenAuthTokenAppModel model) throws AlipayApiException {
		AlipayOpenAuthTokenAppResponse response = openAuthTokenAppToResponse(model);
		return response.getBody();
	}
	/**
	 * 查询授权信息
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayOpenAuthTokenAppQueryResponse openAuthTokenAppQueryToResponse(AlipayOpenAuthTokenAppQueryModel model) throws AlipayApiException {
		AlipayOpenAuthTokenAppQueryRequest request = new AlipayOpenAuthTokenAppQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 查询授权信息
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static String openAuthTokenAppQuery(AlipayOpenAuthTokenAppQueryModel model) throws AlipayApiException {
		AlipayOpenAuthTokenAppQueryResponse response = openAuthTokenAppQueryToResponse(model);
		return response.getBody();
	}
	/**
	 * 地铁购票发码
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayCommerceCityfacilitatorVoucherGenerateResponse voucherGenerateToResponse(AlipayCommerceCityfacilitatorVoucherGenerateModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherGenerateRequest request = new AlipayCommerceCityfacilitatorVoucherGenerateRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 地铁购票发码
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static String voucherGenerate(AlipayCommerceCityfacilitatorVoucherGenerateModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherGenerateResponse response = voucherGenerateToResponse(model);
		return response.getBody();
	}
	/**
	 * 地铁购票发码退款
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayCommerceCityfacilitatorVoucherRefundResponse metroRefundToResponse(AlipayCommerceCityfacilitatorVoucherRefundModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherRefundRequest request = new AlipayCommerceCityfacilitatorVoucherRefundRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 地铁购票发码退款
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static String metroRefund(AlipayCommerceCityfacilitatorVoucherRefundModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherRefundResponse response = metroRefundToResponse(model);
		return response.getBody();
	}
	
	/**
	 * 地铁车站数据查询
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayCommerceCityfacilitatorStationQueryResponse stationQueryToResponse(AlipayCommerceCityfacilitatorStationQueryModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorStationQueryRequest request = new AlipayCommerceCityfacilitatorStationQueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 地铁车站数据查询
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static String stationQuery(AlipayCommerceCityfacilitatorStationQueryModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorStationQueryResponse response = stationQueryToResponse(model);
		return response.getBody();
	}
	/**
	 * 核销码批量查询
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static AlipayCommerceCityfacilitatorVoucherBatchqueryResponse voucherBatchqueryToResponse(AlipayCommerceCityfacilitatorVoucherBatchqueryModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherBatchqueryRequest request = new AlipayCommerceCityfacilitatorVoucherBatchqueryRequest();
		request.setBizModel(model);
		return AliPayApiConfigKit.getAliPayApiConfig().getAlipayClient().execute(request);
	}
	/**
	 * 核销码批量查询
	 * @param model
	 * @return
	 * @throws AlipayApiException
	 */
	public static String voucherBatchquery(AlipayCommerceCityfacilitatorVoucherBatchqueryModel model) throws AlipayApiException {
		AlipayCommerceCityfacilitatorVoucherBatchqueryResponse response = voucherBatchqueryToResponse(model);
		return response.getBody();
	}
	
	
	/**
     * 支付宝提供给商户的服务接入网关URL(新)
     */
    private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
	
	public static void batchTrans(Map<String, String> params, String private_key, String sign_type, HttpServletResponse response) throws IOException {
		params.put("service", "batch_trans_notify");
		params.put("_input_charset", "UTF-8");
		params.put("pay_date", DateKit.toStr(new Date(), DateKit.YYYYMMDD));
		Map<String, String> param = AlipayCore.buildRequestPara(params,private_key,sign_type);
		response.sendRedirect(ALIPAY_GATEWAY_NEW.concat(AlipayCore.createLinkString(param)));
	}
	/**
	 * 将异步通知的参数转化为Map
	 * @param request
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
