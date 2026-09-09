package com.ijpay.wxpay;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.wxpay.enums.WxApiEnum;
import com.ijpay.wxpay.enums.v3.Apply4SubApiEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.enums.v3.BrandApiEnum;
import com.ijpay.wxpay.enums.v3.ComplianceApiEnum;
import com.ijpay.wxpay.enums.v3.ComplaintsApiEnum;
import com.ijpay.wxpay.enums.v3.CouponApiEnum;
import com.ijpay.wxpay.enums.v3.DeliveryPlanApiEnum;
import com.ijpay.wxpay.enums.v3.EcommerceApiEnum;
import com.ijpay.wxpay.enums.v3.InvoiceApiEnum;
import com.ijpay.wxpay.enums.v3.MedPayApiEnum;
import com.ijpay.wxpay.enums.v3.OtherApiEnum;
import com.ijpay.wxpay.enums.v3.ParkingReminderApiEnum;
import com.ijpay.wxpay.enums.v3.ProductCouponApiEnum;
import com.ijpay.wxpay.enums.v3.ProfitSharingApiEnum;
import com.ijpay.wxpay.enums.v3.TransferApiEnum;
import com.ijpay.wxpay.model.v3.AddReceivers;
import com.ijpay.wxpay.model.v3.BatchTransferModel;
import com.ijpay.wxpay.model.v3.ProfitSharingModel;
import com.ijpay.wxpay.model.v3.RefundModel;
import com.ijpay.wxpay.model.v3.UnifiedOrderModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信支付 APIv3 接口封装。</p>
 *
 * <p>内部复用 {@link WxPayApi#v3} 统一入口与 WxPayKit 签名机制，
 * 自动完成 URL 占位符替换、请求体 JSON 序列化以及 query 参数拼接，
 * 使调用方无需手动处理签名细节。示例：</p>
 *
 * <pre>
 * WxPayV3ApiConfig config = WxPayV3ApiConfig.builder()
 *         .mchId(mchId).appId(appId).serialNo(serialNo).keyPath(keyPath)
 *         .build();
 * WxPayV3Api api = new WxPayV3Api(config);
 * IJPayHttpResponse response = api.jsapiPay(unifiedOrderModel);
 * </pre>
 *
 * @author Javen
 */
public class WxPayV3Api {

	private final WxPayV3ApiConfig config;

	public WxPayV3Api(WxPayV3ApiConfig config) {
		this.config = config;
	}

	/**
	 * 通用执行方法，body 会被序列化为 JSON 请求体（POST 类接口）
	 *
	 * @param method    {@link RequestMethodEnum} 请求方式
	 * @param apiEnum   {@link WxApiEnum} 接口枚举
	 * @param body      请求体，可为任意对象（自动转 JSON）或 JSON 字符串，可为 null
	 * @param urlParams URL 占位符参数，按顺序替换接口枚举 URL 中的 %s
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse execute(RequestMethodEnum method, WxApiEnum apiEnum, Object body, Object... urlParams) throws Exception {
		String url = buildUrl(apiEnum, urlParams);
		String jsonBody = body == null ? null : (body instanceof String ? (String) body : JSONUtil.toJsonStr(body));
		return doExecute(method, url, jsonBody);
	}

	/**
	 * 通用执行方法，queryParams 会被拼接为 URL 查询参数（GET 类接口）
	 *
	 * @param method      {@link RequestMethodEnum} 请求方式
	 * @param apiEnum     {@link WxApiEnum} 接口枚举
	 * @param queryParams URL 查询参数，可为 null
	 * @param urlParams   URL 占位符参数，按顺序替换接口枚举 URL 中的 %s
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse executeWithQuery(RequestMethodEnum method, WxApiEnum apiEnum, Map<String, String> queryParams, Object... urlParams) throws Exception {
		String url = buildUrl(apiEnum, urlParams);
		if (queryParams == null || queryParams.isEmpty()) {
			return doExecute(method, url, (String) null);
		}
		return doExecute(method, url, queryParams);
	}

	// ==================== 基础支付 ====================

	/**
	 * JSAPI 下单
	 *
	 * @param model {@link UnifiedOrderModel} 下单参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse jsapiPay(UnifiedOrderModel model) throws Exception {
		return execute(RequestMethodEnum.POST, BasePayApiEnum.JS_API_PAY, model);
	}

	/**
	 * 小程序下单（与 JSAPI 共用同一个下单接口，仅调起方式不同）
	 *
	 * @param model {@link UnifiedOrderModel} 下单参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse miniProgramPay(UnifiedOrderModel model) throws Exception {
		return jsapiPay(model);
	}

	/**
	 * APP 下单
	 *
	 * @param model {@link UnifiedOrderModel} 下单参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse appPay(UnifiedOrderModel model) throws Exception {
		return execute(RequestMethodEnum.POST, BasePayApiEnum.APP_PAY, model);
	}

	/**
	 * Native 下单
	 *
	 * @param model {@link UnifiedOrderModel} 下单参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse nativePay(UnifiedOrderModel model) throws Exception {
		return execute(RequestMethodEnum.POST, BasePayApiEnum.NATIVE_PAY, model);
	}

	/**
	 * H5 下单
	 *
	 * @param model {@link UnifiedOrderModel} 下单参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse h5Pay(UnifiedOrderModel model) throws Exception {
		return execute(RequestMethodEnum.POST, BasePayApiEnum.H5_PAY, model);
	}

	/**
	 * 付款码支付（用户展示付款码，商户扫码扣款）
	 *
	 * @param model {@link UnifiedOrderModel} 下单参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse codePay(UnifiedOrderModel model) throws Exception {
		return execute(RequestMethodEnum.POST, BasePayApiEnum.CODE_PAY, model);
	}

	// ==================== 订单查询/关闭/撤销 ====================

	/**
	 * 微信支付订单号查询订单
	 *
	 * @param transactionId 微信支付订单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse queryByTransactionId(String transactionId) throws Exception {
		Map<String, String> params = newQueryMap();
		params.put("mchid", config.getMchId());
		return executeWithQuery(RequestMethodEnum.GET, BasePayApiEnum.ORDER_QUERY_BY_TRANSACTION_ID, params, transactionId);
	}

	/**
	 * 商户订单号查询订单
	 *
	 * @param outTradeNo 商户订单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse queryByOutTradeNo(String outTradeNo) throws Exception {
		Map<String, String> params = newQueryMap();
		params.put("mchid", config.getMchId());
		return executeWithQuery(RequestMethodEnum.GET, BasePayApiEnum.ORDER_QUERY_BY_OUT_TRADE_NO, params, outTradeNo);
	}

	/**
	 * 关闭订单（普通商户）
	 *
	 * @param outTradeNo 商户订单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse closeOrder(String outTradeNo) throws Exception {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("mchid", config.getMchId());
		return execute(RequestMethodEnum.POST, BasePayApiEnum.CLOSE_ORDER_BY_OUT_TRADE_NO, body, outTradeNo);
	}

	/**
	 * 关闭订单（服务商模式）
	 *
	 * @param outTradeNo 商户订单号
	 * @param subMchid   子商户号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse closeOrder(String outTradeNo, String subMchid) throws Exception {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("sub_mchid", subMchid);
		return execute(RequestMethodEnum.POST, BasePayApiEnum.PARTNER_CLOSE_ORDER_BY_OUT_TRADE_NO, body, outTradeNo);
	}

	/**
	 * 撤销订单（服务商模式）
	 *
	 * @param outTradeNo 商户订单号
	 * @param subMchid   子商户号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse reverseOrder(String outTradeNo, String subMchid) throws Exception {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("sub_mchid", subMchid);
		return execute(RequestMethodEnum.POST, BasePayApiEnum.REVERSE_ORDER_BY_OUT_TRADE_NO, body, outTradeNo);
	}

	// ==================== 退款 ====================

	/**
	 * 申请退款
	 *
	 * @param model {@link RefundModel} 退款参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse refund(RefundModel model) throws Exception {
		return execute(RequestMethodEnum.POST, BasePayApiEnum.REFUND, model);
	}

	/**
	 * 通过商户退款单号查询单笔退款
	 *
	 * @param outRefundNo 商户退款单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse refundQueryByOutRefundNo(String outRefundNo) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BasePayApiEnum.REFUND_QUERY_BY_OUT_REFUND_NO, null, outRefundNo);
	}

	/**
	 * 通过微信支付退款单号查询单笔退款
	 *
	 * @param refundId 微信支付退款单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse refundQueryByRefundId(String refundId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BasePayApiEnum.DOMESTIC_REFUND_QUERY, null, refundId);
	}

	// ==================== 账单 ====================

	/**
	 * 申请交易账单
	 *
	 * @param billDate 账单日期，格式 yyyy-MM-dd
	 * @param billType 账单类型，ALL/SUCCESS/REFUND/RECHARGE，默认 ALL
	 * @param tarType  压缩类型，GZIP，不填则以不压缩的方式返回
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse tradeBill(String billDate, String billType, String tarType) throws Exception {
		Map<String, String> params = newQueryMap();
		params.put("bill_date", billDate);
		if (StrUtil.isNotEmpty(billType)) {
			params.put("bill_type", billType);
		}
		if (StrUtil.isNotEmpty(tarType)) {
			params.put("tar_type", tarType);
		}
		return executeWithQuery(RequestMethodEnum.GET, BasePayApiEnum.TRADE_BILL, params);
	}

	/**
	 * 申请资金账单
	 *
	 * @param billDate 账单日期，格式 yyyy-MM-dd
	 * @param billType 账单类型，ALL/Success，默认 ALL
	 * @param tarType  压缩类型，GZIP，不填则以不压缩的方式返回
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse fundFlowBill(String billDate, String billType, String tarType) throws Exception {
		Map<String, String> params = newQueryMap();
		params.put("bill_date", billDate);
		if (StrUtil.isNotEmpty(billType)) {
			params.put("bill_type", billType);
		}
		if (StrUtil.isNotEmpty(tarType)) {
			params.put("tar_type", tarType);
		}
		return executeWithQuery(RequestMethodEnum.GET, BasePayApiEnum.FUND_FLOW_BILL, params);
	}

	// ==================== 商家转账 ====================

	/**
	 * 发起商家转账
	 *
	 * @param model {@link BatchTransferModel} 转账参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse batchTransfer(BatchTransferModel model) throws Exception {
		return execute(RequestMethodEnum.POST, TransferApiEnum.TRANSFER_BATCHES, model);
	}

	/**
	 * 通过微信支付批次单号查询批次单
	 *
	 * @param batchId         微信支付批次单号
	 * @param needQueryDetail 是否查询转账明细单，true-查询 false-不查询
	 * @param offset          查询的偏移量，默认 0
	 * @param limit           最大资源个数，默认 20
	 * @param detailStatus    明细状态，SUCCESS/RUNNING/FAIL
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse transferQueryByBatchId(String batchId, boolean needQueryDetail, Integer offset, Integer limit, String detailStatus) throws Exception {
		Map<String, String> params = buildTransferQuery(needQueryDetail, offset, limit, detailStatus);
		return executeWithQuery(RequestMethodEnum.GET, TransferApiEnum.TRANSFER_QUERY_BY_BATCH_ID, params, batchId);
	}

	/**
	 * 通过商家批次单号查询批次单
	 *
	 * @param outBatchNo      商家批次单号
	 * @param needQueryDetail 是否查询转账明细单，true-查询 false-不查询
	 * @param offset          查询的偏移量，默认 0
	 * @param limit           最大资源个数，默认 20
	 * @param detailStatus    明细状态，SUCCESS/RUNNING/FAIL
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse transferQueryByOutBatchNo(String outBatchNo, boolean needQueryDetail, Integer offset, Integer limit, String detailStatus) throws Exception {
		Map<String, String> params = buildTransferQuery(needQueryDetail, offset, limit, detailStatus);
		return executeWithQuery(RequestMethodEnum.GET, TransferApiEnum.TRANSFER_QUERY_BY_OUT_BATCH_NO, params, outBatchNo);
	}

	// ==================== 分账 ====================

	/**
	 * 请求分账
	 *
	 * @param model {@link ProfitSharingModel} 分账参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse profitSharing(ProfitSharingModel model) throws Exception {
		return execute(RequestMethodEnum.POST, ProfitSharingApiEnum.PROFIT_SHARING_ORDERS, model);
	}

	/**
	 * 查询分账结果
	 *
	 * @param transactionId 微信支付订单号
	 * @param outOrderNo    商户分账单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse profitSharingQuery(String transactionId, String outOrderNo) throws Exception {
		Map<String, String> params = newQueryMap();
		params.put("transaction_id", transactionId);
		params.put("out_order_no", outOrderNo);
		return executeWithQuery(RequestMethodEnum.GET, ProfitSharingApiEnum.PROFIT_SHARING_ORDERS_QUERY, params, outOrderNo);
	}

	/**
	 * 解冻剩余资金
	 *
	 * @param transactionId 微信支付订单号
	 * @param outOrderNo    商户分账单号
	 * @param description   分账描述
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse profitSharingUnfreeze(String transactionId, String outOrderNo, String description) throws Exception {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("transaction_id", transactionId);
		body.put("out_order_no", outOrderNo);
		body.put("description", description);
		return execute(RequestMethodEnum.POST, ProfitSharingApiEnum.PROFIT_SHARING_UNFREEZE, body);
	}

	/**
	 * 添加分账接收方
	 *
	 * @param model {@link AddReceivers} 接收方参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse profitSharingAddReceiver(AddReceivers model) throws Exception {
		return execute(RequestMethodEnum.POST, ProfitSharingApiEnum.PROFIT_SHARING_RECEIVERS_ADD, model);
	}

	/**
	 * 删除分账接收方
	 *
	 * @param model {@link AddReceivers} 接收方参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse profitSharingDeleteReceiver(AddReceivers model) throws Exception {
		return execute(RequestMethodEnum.POST, ProfitSharingApiEnum.PROFIT_SHARING_RECEIVERS_DELETE, model);
	}

	// ==================== 平台证书 ====================

	/**
	 * 获取平台证书列表
	 *
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse getCertificates() throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, OtherApiEnum.GET_CERTIFICATES, null);
	}

	/**
	 * 按证书算法类型获取平台证书列表
	 *
	 * @param algorithmType 证书算法类型，RSA 或 SM2
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse getCertificates(String algorithmType) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, OtherApiEnum.GET_CERTIFICATES_BY_ALGORITHM_TYPE, null, algorithmType);
	}

	/**
	 * 获取微信支付公钥
	 *
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse getPublicKeys() throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, OtherApiEnum.GET_PUBLIC_KEYS, null);
	}

	/**
	 * 商户签名验签、加解密测试
	 *
	 * @param body 请求体，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse securityEcho(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, OtherApiEnum.SECURITY_ECHO, body);
	}

	// ==================== 医保支付 ====================

	/**
	 * 医保自费混合收款下单
	 *
	 * @param body 下单参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse medInsOrder(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, MedPayApiEnum.MED_INS_ORDERS, body);
	}

	/**
	 * 使用医保自费混合订单号查看下单结果
	 *
	 * @param mixTradeNo 医保自费混合订单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse medInsOrderQueryByMixTradeNo(String mixTradeNo) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, MedPayApiEnum.MED_INS_ORDER_QUERY_BY_MIX_TRADE_NO, null, mixTradeNo);
	}

	/**
	 * 使用商户订单号查看下单结果
	 *
	 * @param outTradeNo 商户订单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse medInsOrderQueryByOutTradeNo(String outTradeNo) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, MedPayApiEnum.MED_INS_ORDER_QUERY_BY_OUT_TRADE_NO, null, outTradeNo);
	}

	// ==================== 委托营销 ====================

	/**
	 * 查询合作关系列表
	 *
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse partnershipsQuery() throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, CouponApiEnum.PARTNERSHIPS_QUERY, null);
	}

	/**
	 * 建立合作关系
	 *
	 * @param body 合作方参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse partnershipsBuild(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, CouponApiEnum.PARTNERSHIPS_BUILD, body);
	}

	// ==================== 异常退款 ====================

	/**
	 * 发起异常退款
	 *
	 * @param body     退款参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @param refundId 微信支付退款单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse abnormalRefund(Object body, String refundId) throws Exception {
		return execute(RequestMethodEnum.POST, BasePayApiEnum.ABNORMAL_REFUND, body, refundId);
	}

	// ==================== 商家转账-新模式 ====================

	/**
	 * 商户预授权模式发起转账
	 *
	 * @param body 转账参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse preTransferWithAuthorization(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, TransferApiEnum.PRE_TRANSFER_WITH_AUTHORIZATION, body);
	}

	/**
	 * 免确认模式发起转账
	 *
	 * @param body 转账参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse transferWithoutAuthorization(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, TransferApiEnum.TRANSFER_WITHOUT_AUTHORIZATION, body);
	}

	/**
	 * 用户确认收款
	 *
	 * @param body 确认收款参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse userConfirmAuthorization(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, TransferApiEnum.USER_CONFIRM_AUTHORIZATION, body);
	}

	/**
	 * 通过商户授权号查询授权
	 *
	 * @param outAuthorizationNo 商户授权号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse getUserConfirmAuthorization(String outAuthorizationNo) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, TransferApiEnum.GET_USER_CONFIRM_AUTHORIZATION, null, outAuthorizationNo);
	}

	/**
	 * 关闭授权
	 *
	 * @param outAuthorizationNo 商户授权号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse closeUserConfirmAuthorization(String outAuthorizationNo) throws Exception {
		return execute(RequestMethodEnum.POST, TransferApiEnum.CLOSE_USER_CONFIRM_AUTHORIZATION, null, outAuthorizationNo);
	}

	// ==================== 消费者投诉 ====================

	/**
	 * 更新退款审批进度
	 *
	 * @param complaintId 投诉单号
	 * @param body        更新参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse updateRefundProgress(String complaintId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ComplaintsApiEnum.COMPLAINTS_UPDATE_REFUND_PROGRESS, body, complaintId);
	}

	/**
	 * 回复用户（同步）
	 *
	 * @param complaintId 投诉单号
	 * @param body        回复参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse responseImmediate(String complaintId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ComplaintsApiEnum.COMPLAINTS_RESPONSE_IMMEDIATE, body, complaintId);
	}

	/**
	 * 下载图片
	 *
	 * @param mediaId 图片媒体ID
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse imagesDownload(String mediaId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, ComplaintsApiEnum.IMAGES_DOWNLOAD, null, mediaId);
	}

	// ==================== 停车缴费 ====================

	/**
	 * 提交停车场进件申请
	 *
	 * @param body 进件申请参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse parkingApplication(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ParkingReminderApiEnum.PARKING_APPLICATION, body);
	}

	/**
	 * 查询停车场进件申请单
	 *
	 * @param queryParams URL 查询参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse parkingApplicationQuery(Map<String, String> queryParams) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, ParkingReminderApiEnum.PARKING_APPLICATION_QUERY, queryParams);
	}

	/**
	 * 撤回停车场进件申请
	 *
	 * @param body 撤回参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse parkingApplicationWithdraw(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ParkingReminderApiEnum.PARKING_APPLICATION_WITHDRAW, body);
	}

	/**
	 * 查询停车场进件申请单列表
	 *
	 * @param queryParams URL 查询参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse parkingApplications(Map<String, String> queryParams) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, ParkingReminderApiEnum.PARKING_APPLICATIONS, queryParams);
	}

	/**
	 * 同步车辆入场通知
	 *
	 * @param body 入场通知参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse parkingEntry(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ParkingReminderApiEnum.PARKING_ENTRY, body);
	}

	/**
	 * 同步车辆离场通知
	 *
	 * @param body 离场通知参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse parkingExit(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ParkingReminderApiEnum.PARKING_EXIT, body);
	}

	/**
	 * 同步非临停扩展支付
	 *
	 * @param body 扩展支付参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse parkingExtPayment(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ParkingReminderApiEnum.PARKING_EXT_PAYMENT, body);
	}

	/**
	 * 查询停车费用
	 *
	 * @param body 查询参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse parkingFee(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ParkingReminderApiEnum.PARKING_FEE, body);
	}

	/**
	 * 查询停车场信息
	 *
	 * @param queryParams URL 查询参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse parkingLot(Map<String, String> queryParams) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, ParkingReminderApiEnum.PARKING_LOT, queryParams);
	}

	/**
	 * 同步支付结果通知
	 *
	 * @param body 支付结果参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse parkingPayment(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ParkingReminderApiEnum.PARKING_PAYMENT, body);
	}


	// ==================== 服务商进件 ====================

	/**
	 * 查询结算账户修改申请状态
	 *
	 * @param subMchid      特约商户号
	 * @param applicationId 申请单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse getSettlementApplyState(String subMchid, String applicationId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, Apply4SubApiEnum.GET_SETTLEMENT_APPLY_STATE, null, subMchid, applicationId);
	}

	// ==================== 电商收付通 ====================

	/**
	 * 二级商户查询预约提现状态（按商户预约提现单号查询）
	 *
	 * @param outRequestNo 商户预约提现单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse ecommerceWithdrawQueryByOutRequestNo(String outRequestNo) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, EcommerceApiEnum.WITHDRAW_QUERY_BY_OUT_REQUEST_NO, null, outRequestNo);
	}

	/**
	 * 二级商户按日终余额预约提现
	 *
	 * @param body 提现参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse ecommerceDayEndBalanceWithdraw(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, EcommerceApiEnum.DAY_END_BALANCE_WITHDRAW, body);
	}

	/**
	 * 查询二级商户按日终余额预约提现状态
	 *
	 * @param outRequestNo 商户预约提现单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse ecommerceDayEndBalanceWithdrawQuery(String outRequestNo) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, EcommerceApiEnum.DAY_END_BALANCE_WITHDRAW_QUERY, null, outRequestNo);
	}

	/**
	 * 提交注销提现申请
	 *
	 * @param body 申请参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse ecommerceApplyCancelWithdraw(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, EcommerceApiEnum.APPLY_CANCEL_WITHDRAW, body);
	}

	/**
	 * 按微信支付申请单号查询申请单状态
	 *
	 * @param applymentId 微信支付申请单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse ecommerceApplyCancelWithdrawQueryById(String applymentId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, EcommerceApiEnum.APPLY_CANCEL_WITHDRAW_QUERY_BY_ID, null, applymentId);
	}

	/**
	 * 按商户申请单号查询申请单状态
	 *
	 * @param outRequestNo 商户申请单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse ecommerceApplyCancelWithdrawQueryByNo(String outRequestNo) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, EcommerceApiEnum.APPLY_CANCEL_WITHDRAW_QUERY_BY_NO, null, outRequestNo);
	}

	/**
	 * 商户注销资格校验
	 *
	 * @param subMchid 电商二级商户号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse ecommerceValidateCancel(String subMchid) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, EcommerceApiEnum.VALIDATE_CANCEL, null, subMchid);
	}

	/**
	 * 提交注销申请单
	 *
	 * @param body 申请参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse ecommerceCancelApplications(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, EcommerceApiEnum.CANCEL_APPLICATIONS, body);
	}

	/**
	 * 注销图片上传
	 *
	 * @param body 上传参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse ecommerceCancelApplicationsMedia(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, EcommerceApiEnum.CANCEL_APPLICATIONS_MEDIA, body);
	}

	/**
	 * 查询注销单状态
	 *
	 * @param outApplyNo 商户注销申请单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse ecommerceCancelApplicationsQuery(String outApplyNo) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, EcommerceApiEnum.CANCEL_APPLICATIONS_QUERY, null, outApplyNo);
	}

	/**
	 * 发起异常退款
	 *
	 * @param refundId 微信退款单号
	 * @param body     退款参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse ecommerceRefundsAbnormal(String refundId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, EcommerceApiEnum.REFUNDS_ABNORMAL, body, refundId);
	}

	/**
	 * 查询指定主体子商户列表
	 *
	 * @param queryParams URL 查询参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse ecommerceSubjectSubMerchants(Map<String, String> queryParams) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, EcommerceApiEnum.SUBJECT_SUB_MERCHANTS, queryParams);
	}


	// ==================== 电子发票 ====================

	/**
	 * 开具通用行业电子发票
	 *
	 * @param body 开票参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse invoiceIssueGeneral(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, InvoiceApiEnum.ISSUE_GENERAL, body);
	}

	/**
	 * 开具旅客运输行业电子发票
	 *
	 * @param body 开票参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse invoiceIssuePassengerTransport(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, InvoiceApiEnum.ISSUE_PASSENGER_TRANSPORT, body);
	}

	/**
	 * 开具成品油行业电子发票
	 *
	 * @param body 开票参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse invoiceIssueRefinedOil(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, InvoiceApiEnum.ISSUE_REFINED_OIL, body);
	}

	/**
	 * 开具不动产租赁行业电子发票
	 *
	 * @param body 开票参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse invoiceIssueRealEstateLeasing(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, InvoiceApiEnum.ISSUE_REAL_ESTATE_LEASING, body);
	}

	/**
	 * 获取开通服务商电子发票能力邀请链接
	 *
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse invoiceGetSpInviteUrl() throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, InvoiceApiEnum.GET_SP_INVITE_URL, null);
	}

	/**
	 * 获取邀请开通的商户信息
	 *
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse invoiceListSpInviteMchInfo() throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, InvoiceApiEnum.LIST_SP_INVITE_MCH_INFO, null);
	}

	/**
	 * 检查子商户开票功能状态
	 *
	 * @param subMchid 子商户号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse invoiceMerchantCheckStatus(String subMchid) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, InvoiceApiEnum.MERCHANT_CHECK_STATUS, null, subMchid);
	}

	// ==================== 医保支付 ====================

	/**
	 * 医保退款结果通知
	 *
	 * @param body 通知参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse medInsRefundsNotify(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, MedPayApiEnum.MED_INS_REFUNDS_NOTIFY, body);
	}

	// ==================== 合规接口 ====================

	/**
	 * 发起不活跃商户身份核实
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse inactiveMerchantVerify(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ComplianceApiEnum.INACTIVE_MERCHANT_VERIFY, body);
	}

	/**
	 * 查询不活跃商户身份核实结果
	 *
	 * @param subMchid       商户号
	 * @param verificationId 核实单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse inactiveMerchantVerifyQuery(String subMchid, String verificationId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, ComplianceApiEnum.INACTIVE_MERCHANT_VERIFY_QUERY, null, subMchid, verificationId);
	}

	/**
	 * 查询子商户管控情况
	 *
	 * @param subMchid 子商户号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse merchantLimitations(String subMchid) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, ComplianceApiEnum.MERCHANT_LIMITATIONS, null, subMchid);
	}

	/**
	 * 提交已注销商户号可用余额提现申请单
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse riskWithdrawApply(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ComplianceApiEnum.RISK_WITHDRAW_APPLY, body);
	}

	/**
	 * 按微信支付提现申请单号查询提现申请单状态
	 *
	 * @param applymentId 微信支付提现申请单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse riskWithdrawApplyQueryById(String applymentId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, ComplianceApiEnum.RISK_WITHDRAW_APPLY_QUERY_BY_ID, null, applymentId);
	}

	/**
	 * 按商户提现申请单号查询提现申请单状态
	 *
	 * @param outRequestNo 商户提现申请单号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse riskWithdrawApplyQueryByNo(String outRequestNo) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, ComplianceApiEnum.RISK_WITHDRAW_APPLY_QUERY_BY_NO, null, outRequestNo);
	}


	// ==================== 品牌商户 ====================

	/**
	 * 品牌入驻-提交入驻申请
	 *
	 * @param body 申请参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandApplyments(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.BRAND_APPLYMENTS, body);
	}

	/**
	 * 品牌入驻-根据申请单ID查询申请状态
	 *
	 * @param applymentId 申请单ID
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandApplymentQueryById(String applymentId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BrandApiEnum.BRAND_APPLYMENT_QUERY_BY_ID, null, applymentId);
	}

	/**
	 * 品牌入驻-根据业务申请编号查询申请状态
	 *
	 * @param businessCode 业务申请编号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandApplymentQueryByBusinessCode(String businessCode) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BrandApiEnum.BRAND_APPLYMENT_QUERY_BY_BUSINESS_CODE, null, businessCode);
	}

	/**
	 * 品牌入驻-撤销申请
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandApplymentCancel(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.BRAND_APPLYMENT_CANCEL, body);
	}

	/**
	 * 品牌服务商-获得品牌已授权且在生效中的产品权限信息
	 *
	 * @param brandId 品牌ID
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandProductAuthorities(String brandId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BrandApiEnum.BRAND_PRODUCT_AUTHORITIES, null, brandId);
	}

	/**
	 * 商家名片-提交商家名片配置申请
	 *
	 * @param body 申请参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandCardConfigs(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.CARD_CONFIGS, body);
	}

	/**
	 * 商家名片-查询商家名片配置申请状态
	 *
	 * @param queryParams URL 查询参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandCardConfigsQuery(Map<String, String> queryParams) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BrandApiEnum.CARD_CONFIGS_QUERY, queryParams);
	}

	/**
	 * 商家名片-撤销商家名片配置申请
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandCardConfigsCancel(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.CARD_CONFIGS_CANCEL, body);
	}

	/**
	 * 商家名片-获取商家名片预览二维码
	 *
	 * @param queryParams URL 查询参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandCardConfigsPreviewUrl(Map<String, String> queryParams) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BrandApiEnum.CARD_CONFIGS_PREVIEW_URL, queryParams);
	}

	/**
	 * 商家名片-发布商家名片配置
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandCardConfigsPublish(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.CARD_CONFIGS_PUBLISH, body);
	}

	/**
	 * 商家名片-创建交易连接名片场景
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandCardLinks(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.CARD_LINKS, body);
	}

	/**
	 * 商家名片-查询已生效交易连接名片规则
	 *
	 * @param queryParams URL 查询参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandCardLinksQuery(Map<String, String> queryParams) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BrandApiEnum.CARD_LINKS_QUERY, queryParams);
	}

	/**
	 * 商家名片-根据业务申请编号查询添加申请状态
	 *
	 * @param businessCode 业务申请编号
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandCardLinksQueryByBusinessCode(String businessCode) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BrandApiEnum.CARD_LINKS_QUERY_BY_BUSINESS_CODE, null, businessCode);
	}

	/**
	 * 商家名片-撤销交易连接名片配置申请
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandCardLinksCancel(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.CARD_LINKS_CANCEL, body);
	}

	/**
	 * 商家名片-解除已生效交易连接名片场景
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandCardLinksUnbind(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.CARD_LINKS_UNBIND, body);
	}

	/**
	 * 商家名片会员-创建会员卡模板
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandMemberCardTemplate(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.MEMBER_CARD_TEMPLATE, body);
	}

	/**
	 * 商家名片会员-查询会员卡模板列表
	 *
	 * @param queryParams URL 查询参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandMemberCardTemplateQuery(Map<String, String> queryParams) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BrandApiEnum.MEMBER_CARD_TEMPLATE_QUERY, queryParams);
	}

	/**
	 * 商家名片会员-查询会员卡模板详情
	 *
	 * @param cardId 会员卡模板ID
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandMemberCardTemplateQueryDetail(String cardId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BrandApiEnum.MEMBER_CARD_TEMPLATE_QUERY_DETAIL, null, cardId);
	}

	/**
	 * 商家名片会员-作废会员卡模板
	 *
	 * @param cardId 会员卡模板ID
	 * @param body   请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandMemberCardTemplateInvalidate(String cardId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.MEMBER_CARD_TEMPLATE_INVALIDATE, body, cardId);
	}

	/**
	 * 商家名片会员-品牌会员入会组件预授权
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandMemberPreAuthTokens(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.MEMBER_PRE_AUTH_TOKENS, body);
	}

	/**
	 * 商家名片会员-查询用户在品牌下所有会员卡
	 *
	 * @param queryParams URL 查询参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandMemberUserCardsQuery(Map<String, String> queryParams) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BrandApiEnum.MEMBER_USER_CARDS_QUERY, queryParams);
	}

	/**
	 * 商家名片会员-根据OPENID导入用户会员卡
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandMemberUserCardsImportByOpenid(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.MEMBER_USER_CARDS_IMPORT_BY_OPENID, body);
	}

	/**
	 * 商家名片会员-查询用户会员卡详情
	 *
	 * @param userCardId 用户会员卡ID
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandMemberUserCardsQueryDetail(String userCardId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BrandApiEnum.MEMBER_USER_CARDS_QUERY_DETAIL, null, userCardId);
	}

	/**
	 * 商家名片会员-同步会员开通结果
	 *
	 * @param userCardId 用户会员卡ID
	 * @param body       请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandMemberUserCardsConfirm(String userCardId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.MEMBER_USER_CARDS_CONFIRM, body, userCardId);
	}

	/**
	 * 商家名片会员-作废用户会员卡
	 *
	 * @param userCardId 用户会员卡ID
	 * @param body       请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandMemberUserCardsInvalidate(String userCardId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.MEMBER_USER_CARDS_INVALIDATE, body, userCardId);
	}

	/**
	 * 品牌门店-创建品牌门店
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandStores(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.BRAND_STORES, body);
	}

	/**
	 * 品牌门店-查询品牌门店
	 *
	 * @param storeId 门店ID
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandStoresQuery(String storeId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, BrandApiEnum.BRAND_STORES_QUERY, null, storeId);
	}

	/**
	 * 品牌门店-绑定收款商户号
	 *
	 * @param storeId 门店ID
	 * @param body    请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandStoresBindRecipient(String storeId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.BRAND_STORES_BIND_RECIPIENT, body, storeId);
	}

	/**
	 * 品牌门店-暂停门店营业
	 *
	 * @param storeId 门店ID
	 * @param body    请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandStoresClose(String storeId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.BRAND_STORES_CLOSE, body, storeId);
	}

	/**
	 * 品牌门店-恢复门店营业
	 *
	 * @param storeId 门店ID
	 * @param body    请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandStoresResume(String storeId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.BRAND_STORES_RESUME, body, storeId);
	}

	/**
	 * 品牌门店-解绑收款商户号
	 *
	 * @param storeId 门店ID
	 * @param body    请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse brandStoresUnbindRecipient(String storeId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, BrandApiEnum.BRAND_STORES_UNBIND_RECIPIENT, body, storeId);
	}

	// ==================== 商品券 ====================

	/**
	 * 提交图片生成任务
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponImageTask(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.IMAGE_GENERATION_TASKS, body);
	}

	/**
	 * 查询图片生成任务执行结果
	 *
	 * @param taskId 图片生成任务ID
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponImageTaskQuery(String taskId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, ProductCouponApiEnum.IMAGE_GENERATION_TASKS_QUERY, null, taskId);
	}

	/**
	 * 图片上传
	 *
	 * @param body 上传参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponMediaUploadImage(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.MEDIA_UPLOAD_IMAGE, body);
	}

	/**
	 * 设置商品券事件通知地址
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponNotifyConfigs(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.NOTIFY_CONFIGS, body);
	}

	/**
	 * 创建商品券
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponCreate(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.PRODUCT_COUPONS, body);
	}
	/**
	 * 修改商品券
	 *
	 * @param couponId 商品券ID
	 * @param body     请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponUpdate(String couponId, Object body) throws Exception {
		return execute(RequestMethodEnum.PATCH, ProductCouponApiEnum.PRODUCT_COUPONS_UPDATE, body, couponId);
	}

	/**
	 * 失效商品券
	 *
	 * @param couponId 商品券ID
	 * @param body     请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponDeactivate(String couponId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.PRODUCT_COUPONS_DEACTIVATE, body, couponId);
	}

	/**
	 * 添加商品券批次组
	 *
	 * @param couponId 商品券ID
	 * @param body     请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponStockBundleAdd(String couponId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.STOCK_BUNDLES, body, couponId);
	}

	/**
	 * 修改商品券批次组
	 *
	 * @param couponId      商品券ID
	 * @param stockBundleId 商品券批次组ID
	 * @param body          请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponStockBundleUpdate(String couponId, String stockBundleId, Object body) throws Exception {
		return execute(RequestMethodEnum.PATCH, ProductCouponApiEnum.STOCK_BUNDLES_UPDATE, body, couponId, stockBundleId);
	}
	/**
	 * 批次组批量关联门店
	 *
	 * @param couponId      商品券ID
	 * @param stockBundleId 商品券批次组ID
	 * @param body          请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponStockBundleAssociateStores(String couponId, String stockBundleId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.STOCK_BUNDLES_ASSOCIATE_STORES, body, couponId, stockBundleId);
	}

	/**
	 * 批次组批量取消关联门店
	 *
	 * @param couponId      商品券ID
	 * @param stockBundleId 商品券批次组ID
	 * @param body          请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponStockBundleDisassociateStores(String couponId, String stockBundleId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.STOCK_BUNDLES_DISASSOCIATE_STORES, body, couponId, stockBundleId);
	}

	/**
	 * 修改商品券批次组发放预算
	 *
	 * @param couponId      商品券ID
	 * @param stockBundleId 商品券批次组ID
	 * @param body          请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponStockBundleUpdateBudget(String couponId, String stockBundleId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.STOCK_BUNDLES_UPDATE_BUDGET, body, couponId, stockBundleId);
	}

	/**
	 * 添加商品券批次
	 *
	 * @param couponId 商品券ID
	 * @param body     请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponStockAdd(String couponId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.STOCKS, body, couponId);
	}
	/**
	 * 查询商品券指定批次
	 *
	 * @param couponId 商品券ID
	 * @param stockId  商品券批次ID
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponStockQuery(String couponId, String stockId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, ProductCouponApiEnum.STOCKS_QUERY, null, couponId, stockId);
	}

	/**
	 * 批次关联门店
	 *
	 * @param couponId 商品券ID
	 * @param stockId  商品券批次ID
	 * @param body     请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponStockAssociateStores(String couponId, String stockId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.STOCKS_ASSOCIATE_STORES, body, couponId, stockId);
	}

	/**
	 * 查询批次关联门店列表
	 *
	 * @param couponId   商品券ID
	 * @param stockId    商品券批次ID
	 * @param queryParams URL 查询参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponStockAssociatedStores(String couponId, String stockId, Map<String, String> queryParams) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, ProductCouponApiEnum.STOCKS_ASSOCIATED_STORES, queryParams, couponId, stockId);
	}

	/**
	 * 失效商品券批次
	 *
	 * @param couponId 商品券ID
	 * @param stockId  商品券批次ID
	 * @param body     请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponStockDeactivate(String couponId, String stockId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.STOCKS_DEACTIVATE, body, couponId, stockId);
	}
	/**
	 * 批次取消关联门店
	 *
	 * @param couponId 商品券ID
	 * @param stockId  商品券批次ID
	 * @param body     请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponStockDisassociateStores(String couponId, String stockId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.STOCKS_DISASSOCIATE_STORES, body, couponId, stockId);
	}

	/**
	 * 修改商品券批次发放预算
	 *
	 * @param couponId 商品券ID
	 * @param stockId  商品券批次ID
	 * @param body     请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponStockUpdateBudget(String couponId, String stockId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.STOCKS_UPDATE_BUDGET, body, couponId, stockId);
	}

	/**
	 * 预上传券Code
	 *
	 * @param couponId 商品券ID
	 * @param stockId  商品券批次ID
	 * @param body     请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponStockUploadCouponCodes(String couponId, String stockId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.STOCKS_UPLOAD_COUPON_CODES, body, couponId, stockId);
	}

	/**
	 * 向用户发放商品券批次组
	 *
	 * @param openid 用户标识
	 * @param body   请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponUserBundleSend(String openid, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.USER_COUPON_BUNDLES, body, openid);
	}
	/**
	 * 失效用户商品券组
	 *
	 * @param openid         用户标识
	 * @param couponBundleId 商品券批次组ID
	 * @param body           请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponUserBundleDeactivate(String openid, String couponBundleId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.USER_COUPON_BUNDLES_DEACTIVATE, body, openid, couponBundleId);
	}

	/**
	 * 指定券状态查询用户商品券列表
	 *
	 * @param openid      用户标识
	 * @param queryParams URL 查询参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponUserCouponsQuery(String openid, Map<String, String> queryParams) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, ProductCouponApiEnum.USER_COUPONS_QUERY, queryParams, openid);
	}

	/**
	 * 查询用户商品券详情
	 *
	 * @param openid      用户标识
	 * @param outCouponId 商户券ID
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponUserCouponQuery(String openid, String outCouponId) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, ProductCouponApiEnum.USER_COUPONS_QUERY_DETAIL, null, openid, outCouponId);
	}
	/**
	 * 确认发放用户商品券
	 *
	 * @param openid      用户标识
	 * @param outCouponId 商户券ID
	 * @param body        请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponUserCouponConfirm(String openid, String outCouponId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.USER_COUPONS_CONFIRM, body, openid, outCouponId);
	}

	/**
	 * 失效用户商品券
	 *
	 * @param openid      用户标识
	 * @param outCouponId 商户券ID
	 * @param body        请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponUserCouponDeactivate(String openid, String outCouponId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.USER_COUPONS_DEACTIVATE, body, openid, outCouponId);
	}

	/**
	 * 退券
	 *
	 * @param openid      用户标识
	 * @param outCouponId 商户券ID
	 * @param body        请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponUserCouponReturn(String openid, String outCouponId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.USER_COUPONS_RETURN, body, openid, outCouponId);
	}
	/**
	 * 核销用户商品券
	 *
	 * @param openid      用户标识
	 * @param outCouponId 商户券ID
	 * @param body        请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponUserCouponUse(String openid, String outCouponId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.USER_COUPONS_USE, body, openid, outCouponId);
	}

	/**
	 * 向用户预发放商品券
	 *
	 * @param openid 用户标识
	 * @param body   请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponPreSendCoupon(String openid, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.USER_PRE_SEND_COUPON, body, openid);
	}

	/**
	 * 向用户预发放商品券批次组
	 *
	 * @param openid 用户标识
	 * @param body   请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse productCouponPreSendCouponBundle(String openid, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, ProductCouponApiEnum.USER_PRE_SEND_COUPON_BUNDLE, body, openid);
	}

	// ==================== 品牌营销投放计划 ====================

	/**
	 * 设置投放计划回调地址
	 *
	 * @param brandId 品牌ID
	 * @param body    请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse deliveryPlanNotifyUrl(String brandId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, DeliveryPlanApiEnum.NOTIFY_URL, body, brandId);
	}

	/**
	 * 创建投放计划
	 *
	 * @param body 请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse deliveryPlanCreate(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, DeliveryPlanApiEnum.DELIVERY_PLANS, body);
	}

	/**
	 * 更新投放计划
	 *
	 * @param deliveryPlanId 投放计划ID
	 * @param body           请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse deliveryPlanUpdate(String deliveryPlanId, Object body) throws Exception {
		return execute(RequestMethodEnum.PATCH, DeliveryPlanApiEnum.DELIVERY_PLANS_UPDATE, body, deliveryPlanId);
	}

	/**
	 * 分页查询投放计划列表
	 *
	 * @param brandId     品牌ID
	 * @param queryParams URL 查询参数
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse deliveryPlanQuery(String brandId, Map<String, String> queryParams) throws Exception {
		return executeWithQuery(RequestMethodEnum.GET, DeliveryPlanApiEnum.DELIVERY_PLANS_QUERY, queryParams, brandId);
	}

	/**
	 * 终止投放计划
	 *
	 * @param deliveryPlanId 投放计划ID
	 * @param body           请求参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse deliveryPlanTerminate(String deliveryPlanId, Object body) throws Exception {
		return execute(RequestMethodEnum.POST, DeliveryPlanApiEnum.DELIVERY_PLANS_TERMINATE, body, deliveryPlanId);
	}

	/**
	 * 商户开户意愿确认-提交申请单
	 *
	 * @param body 申请参数，可为任意对象（自动转 JSON）或 JSON 字符串
	 * @return {@link IJPayHttpResponse}
	 * @throws Exception 网络异常或签名异常
	 */
	public IJPayHttpResponse merchantOpenApplySubmit(Object body) throws Exception {
		return execute(RequestMethodEnum.POST, Apply4SubApiEnum.MER_OPEN_APPLY_SUBMIT, body);
	}

	// ==================== 私有方法 ====================

	private IJPayHttpResponse doExecute(RequestMethodEnum method, String urlSuffix, String body) throws Exception {
		if (StrUtil.isNotEmpty(config.getKeyPath())) {
			return WxPayApi.v3(method, config.getDomain(), urlSuffix, config.getMchId(), config.getSerialNo(),
					config.getPlatSerialNo(), config.getKeyPath(), body, config.getAuthType());
		}
		return WxPayApi.v3(method, config.getDomain(), urlSuffix, config.getMchId(), config.getSerialNo(),
				config.getPlatSerialNo(), config.getPrivateKey(), body);
	}

	private IJPayHttpResponse doExecute(RequestMethodEnum method, String urlSuffix, Map<String, String> queryParams) throws Exception {
		if (StrUtil.isNotEmpty(config.getKeyPath())) {
			return WxPayApi.v3(method, config.getDomain(), urlSuffix, config.getMchId(), config.getSerialNo(),
					config.getPlatSerialNo(), config.getKeyPath(), queryParams, config.getAuthType());
		}
		return WxPayApi.v3(method, config.getDomain(), urlSuffix, config.getMchId(), config.getSerialNo(),
				config.getPlatSerialNo(), config.getPrivateKey(), queryParams);
	}

	private String buildUrl(WxApiEnum apiEnum, Object... urlParams) {
		String url = apiEnum.getUrl();
		if (urlParams != null && urlParams.length > 0) {
			url = String.format(url, urlParams);
		}
		return url;
	}

	private Map<String, String> newQueryMap() {
		return new LinkedHashMap<>();
	}

	private Map<String, String> buildTransferQuery(boolean needQueryDetail, Integer offset, Integer limit, String detailStatus) {
		Map<String, String> params = newQueryMap();
		params.put("mchid", config.getMchId());
		params.put("need_query_detail", needQueryDetail ? "1" : "0");
		if (offset != null) {
			params.put("offset", offset.toString());
		}
		if (limit != null) {
			params.put("limit", limit.toString());
		}
		if (StrUtil.isNotEmpty(detailStatus)) {
			params.put("detail_status", detailStatus);
		}
		return params;
	}
}
