package com.ijpay.wxpay.enums.v3;

import com.ijpay.wxpay.enums.WxApiEnum;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 * <p>IJPay 自由交流群: 864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信支付 v3 接口-基础支付接口枚举</p>
 *
 * @author Javen
 */
public enum BasePayApiEnum implements WxApiEnum {
	/**
	 * JSAPI下单
	 */
	JS_API_PAY("/v3/pay/transactions/jsapi", "JSAPI 下单"),

	/**
	 * APP 下单
	 */
	APP_PAY("/v3/pay/transactions/app", "APP 下单"),

	/**
	 * H5 下单
	 */
	H5_PAY("/v3/pay/transactions/h5", "H5 下单"),

	/**
	 * Native 下单
	 */
	NATIVE_PAY("/v3/pay/transactions/native", "Native 下单"),

	/**
	 * 合单 APP 下单
	 */
	COMBINE_TRANSACTIONS_APP("/v3/combine-transactions/app", "合单 APP 下单"),

	/**
	 * 合单 JSAPI 下单
	 */
	COMBINE_TRANSACTIONS_JS("/v3/combine-transactions/jsapi", "合单 JSAPI 下单"),

	/**
	 * 合单 H5 下单
	 */
	COMBINE_TRANSACTIONS_H5("/v3/combine-transactions/h5", "合单 H5 下单"),

	/**
	 * 合单 Native下单
	 */
	COMBINE_TRANSACTIONS_NATIVE("/v3/combine-transactions/native", "合单 Native 下单"),

	/**
	 * 合单查询订单
	 */
	COMBINE_QUERY_BY_OUT_TRADE_NO("/v3/combine-transactions/out-trade-no/%s", "合单查询订单"),

	/**
	 * 合单关闭订单
	 */
	COMBINE_CLOSE_BY_OUT_TRADE_NO("/v3/combine-transactions/out-trade-no/%s/close", "合单关闭订单"),

	/**
	 * 合单支付-申请退款
	 */
	DOMESTIC_REFUND("/v3/refund/domestic/refunds", "合单支付-申请退款"),

	/**
	 * 合单支付-查询单笔退款
	 */
	DOMESTIC_REFUND_QUERY("/v3/refund/domestic/refunds/%s", "合单支付-查询单笔退款"),

	/**
	 * 微信支付订单号查询
	 */
	ORDER_QUERY_BY_TRANSACTION_ID("/v3/pay/transactions/id/%s", "微信支付订单号查询"),

	/**
	 * 商户订单号查询
	 */
	ORDER_QUERY_BY_OUT_TRADE_NO("/v3/pay/transactions/out-trade-no/%s", "商户订单号查询"),

	/**
	 * 关闭订单
	 */
	CLOSE_ORDER_BY_OUT_TRADE_NO("/v3/pay/transactions/out-trade-no/%s/close", "关闭订单"),

	/**
	 * 申请退款
	 */
	REFUND("/v3/refund/domestic/refunds", "申请退款"),

	/**
	 * 查询单笔退款
	 */
	REFUND_QUERY_BY_OUT_REFUND_NO("/v3/refund/domestic/refunds/%s", "查询单笔退款"),


	/**
	 * 申请交易账单
	 */
	TRADE_BILL("/v3/bill/tradebill", "申请交易账单"),

	/**
	 * 申请资金账单
	 */
	FUND_FLOW_BILL("/v3/bill/fundflowbill", "申请资金账单"),

	/**
	 * 下载账单
	 */
	BILL_DOWNLOAD("/v3/billdownload/file", "下载账单"),
	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	BasePayApiEnum(String url, String desc) {
		this.url = url;
		this.desc = desc;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public String toString() {
		return url;
	}
}
