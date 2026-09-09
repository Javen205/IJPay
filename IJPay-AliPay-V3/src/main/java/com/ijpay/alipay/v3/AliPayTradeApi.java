package com.ijpay.alipay.v3;

import com.alipay.v3.ApiClient;
import com.alipay.v3.ApiException;
import com.alipay.v3.Configuration;
import com.alipay.v3.api.AlipayTradeApi;
import com.alipay.v3.model.AlipayTradeCancelModel;
import com.alipay.v3.model.AlipayTradeCancelResponseModel;
import com.alipay.v3.model.AlipayTradeCloseModel;
import com.alipay.v3.model.AlipayTradeCloseResponseModel;
import com.alipay.v3.model.AlipayTradeCreateModel;
import com.alipay.v3.model.AlipayTradeCreateResponseModel;
import com.alipay.v3.model.AlipayTradePayModel;
import com.alipay.v3.model.AlipayTradePayResponseModel;
import com.alipay.v3.model.AlipayTradePrecreateModel;
import com.alipay.v3.model.AlipayTradePrecreateResponseModel;
import com.alipay.v3.model.AlipayTradeQueryModel;
import com.alipay.v3.model.AlipayTradeQueryResponseModel;
import com.alipay.v3.model.AlipayTradeRefundModel;
import com.alipay.v3.model.AlipayTradeRefundResponseModel;
import com.alipay.v3.util.model.CustomizedParams;

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
 * Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a>
 * </p>
 *
 * <p>
 * 支付宝 V3 统一收单交易接口封装，基于官方 SDK {@link AlipayTradeApi}，签名、验签、请求构建均由官方
 * SDK 完成，此处仅封装请求参数与调用入口。支持自定义 {@link ApiClient}，未指定时使用 {@link Configuration}
 * 的默认客户端。
 * </p>
 *
 * @author Javen
 */
public class AliPayTradeApi {

	private final AlipayTradeApi tradeApi;

	/**
	 * 默认构造函数，使用 {@link Configuration} 的默认客户端
	 */
	public AliPayTradeApi() {
		this(Configuration.getDefaultApiClient());
	}

	/**
	 * 自定义 ApiClient 构造，可配合多商户配置使用
	 *
	 * @param apiClient {@link ApiClient}
	 */
	public AliPayTradeApi(ApiClient apiClient) {
		this.tradeApi = new AlipayTradeApi(apiClient);
	}

	/**
	 * 统一收单交易创建
	 * <p>POST /v3/alipay/trade/create</p>
	 *
	 * @param model {@link AlipayTradeCreateModel} 请求参数
	 * @return {@link AlipayTradeCreateResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradeCreateResponseModel create(AlipayTradeCreateModel model) throws ApiException {
		return tradeApi.create(model);
	}

	/**
	 * 统一收单交易创建（支持自定义参数）
	 *
	 * @param model     {@link AlipayTradeCreateModel} 请求参数
	 * @param customizedParams {@link CustomizedParams} 自定义参数
	 * @return {@link AlipayTradeCreateResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradeCreateResponseModel create(AlipayTradeCreateModel model, CustomizedParams customizedParams) throws ApiException {
		return tradeApi.create(model, customizedParams);
	}

	/**
	 * 统一收单交易支付（付款码支付）
	 * <p>POST /v3/alipay/trade/pay</p>
	 *
	 * @param model {@link AlipayTradePayModel} 请求参数
	 * @return {@link AlipayTradePayResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradePayResponseModel pay(AlipayTradePayModel model) throws ApiException {
		return tradeApi.pay(model);
	}

	/**
	 * 统一收单交易支付（支持自定义参数）
	 *
	 * @param model     {@link AlipayTradePayModel} 请求参数
	 * @param customizedParams {@link CustomizedParams} 自定义参数
	 * @return {@link AlipayTradePayResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradePayResponseModel pay(AlipayTradePayModel model, CustomizedParams customizedParams) throws ApiException {
		return tradeApi.pay(model, customizedParams);
	}

	/**
	 * 统一收单线下交易预创建（扫码支付）
	 * <p>POST /v3/alipay/trade/precreate</p>
	 *
	 * @param model {@link AlipayTradePrecreateModel} 请求参数
	 * @return {@link AlipayTradePrecreateResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradePrecreateResponseModel precreate(AlipayTradePrecreateModel model) throws ApiException {
		return tradeApi.precreate(model);
	}

	/**
	 * 统一收单线下交易预创建（支持自定义参数）
	 *
	 * @param model     {@link AlipayTradePrecreateModel} 请求参数
	 * @param customizedParams {@link CustomizedParams} 自定义参数
	 * @return {@link AlipayTradePrecreateResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradePrecreateResponseModel precreate(AlipayTradePrecreateModel model, CustomizedParams customizedParams) throws ApiException {
		return tradeApi.precreate(model, customizedParams);
	}

	/**
	 * 统一收单交易查询
	 * <p>POST /v3/alipay/trade/query</p>
	 *
	 * @param model {@link AlipayTradeQueryModel} 请求参数
	 * @return {@link AlipayTradeQueryResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradeQueryResponseModel query(AlipayTradeQueryModel model) throws ApiException {
		return tradeApi.query(model);
	}

	/**
	 * 统一收单交易查询（支持自定义参数）
	 *
	 * @param model     {@link AlipayTradeQueryModel} 请求参数
	 * @param customizedParams {@link CustomizedParams} 自定义参数
	 * @return {@link AlipayTradeQueryResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradeQueryResponseModel query(AlipayTradeQueryModel model, CustomizedParams customizedParams) throws ApiException {
		return tradeApi.query(model, customizedParams);
	}

	/**
	 * 统一收单交易退款
	 * <p>POST /v3/alipay/trade/refund</p>
	 *
	 * @param model {@link AlipayTradeRefundModel} 请求参数
	 * @return {@link AlipayTradeRefundResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradeRefundResponseModel refund(AlipayTradeRefundModel model) throws ApiException {
		return tradeApi.refund(model);
	}

	/**
	 * 统一收单交易退款（支持自定义参数）
	 *
	 * @param model     {@link AlipayTradeRefundModel} 请求参数
	 * @param customizedParams {@link CustomizedParams} 自定义参数
	 * @return {@link AlipayTradeRefundResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradeRefundResponseModel refund(AlipayTradeRefundModel model, CustomizedParams customizedParams) throws ApiException {
		return tradeApi.refund(model, customizedParams);
	}

	/**
	 * 统一收单交易关闭
	 * <p>POST /v3/alipay/trade/close</p>
	 *
	 * @param model {@link AlipayTradeCloseModel} 请求参数
	 * @return {@link AlipayTradeCloseResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradeCloseResponseModel close(AlipayTradeCloseModel model) throws ApiException {
		return tradeApi.close(model);
	}

	/**
	 * 统一收单交易关闭（支持自定义参数）
	 *
	 * @param model     {@link AlipayTradeCloseModel} 请求参数
	 * @param customizedParams {@link CustomizedParams} 自定义参数
	 * @return {@link AlipayTradeCloseResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradeCloseResponseModel close(AlipayTradeCloseModel model, CustomizedParams customizedParams) throws ApiException {
		return tradeApi.close(model, customizedParams);
	}

	/**
	 * 统一收单交易撤销
	 * <p>POST /v3/alipay/trade/cancel</p>
	 *
	 * @param model {@link AlipayTradeCancelModel} 请求参数
	 * @return {@link AlipayTradeCancelResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradeCancelResponseModel cancel(AlipayTradeCancelModel model) throws ApiException {
		return tradeApi.cancel(model);
	}

	/**
	 * 统一收单交易撤销（支持自定义参数）
	 *
	 * @param model     {@link AlipayTradeCancelModel} 请求参数
	 * @param customizedParams {@link CustomizedParams} 自定义参数
	 * @return {@link AlipayTradeCancelResponseModel}
	 * @throws ApiException 异常信息
	 */
	public AlipayTradeCancelResponseModel cancel(AlipayTradeCancelModel model, CustomizedParams customizedParams) throws ApiException {
		return tradeApi.cancel(model, customizedParams);
	}
}
