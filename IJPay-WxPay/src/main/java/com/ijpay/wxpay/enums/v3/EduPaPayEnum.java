package com.ijpay.wxpay.enums.v3;

import com.ijpay.wxpay.enums.WxApiEnum;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信支付 v3 接口-教培续费通相关接口枚举</p>
 *
 * @author Javen
 */
public enum EduPaPayEnum implements WxApiEnum {
	/**
	 * 通过协议号查询签约
	 */
	QUERY_CONTRACTS_BY_ID("/v3/edu-papay/contracts/id/%s", "通过协议号查询签约"),

	/**
	 * 预签约
	 */
	PRE_SIGN("/v3/edu-papay/contracts/presign", "预签约"),

	/**
	 * 解约
	 */
	DELETE_CONTRACTS("/v3/edu-papay/contracts/%s", "解约"),

	/**
	 * 通过用户标识查询签约
	 */
	QUERY_CONTRACTS_BY_USER("/v3/edu-papay/user/%s/contracts", "通过用户标识查询签约"),

	/**
	 * 受理扣款
	 */
	TRANSACTIONS("/v3/edu-papay/transactions", "受理扣款"),

	/**
	 * 通过微信订单号查询订单
	 */
	QUERY_TRANSACTIONS_BY_TRANSACTION_ID("/v3/edu-papay/transactions/id/%s", "通过微信订单号查询订单"),

	/**
	 * 通过商户订单号查询订单
	 */
	QUERY_TRANSACTIONS_BY_OUT_TRADE_NO("/v3/edu-papay/transactions/out-trade-no/%s", "通过商户订单号查询订单"),

	/**
	 * 发送扣款预通知
	 */
	SEND_NOTIFICATION("/v3/edu-papay/user-notifications/%s/send", "发送扣款预通知"),
	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	EduPaPayEnum(String url, String desc) {
		this.url = url;
		this.desc = desc;
	}

	/**
	 * 获取枚举URL
	 *
	 * @return 枚举编码
	 */
	@Override
	public String getUrl() {
		return url;
	}

	/**
	 * 获取详细的描述信息
	 *
	 * @return 描述信息
	 */
	@Override
	public String getDesc() {
		return desc;
	}
}
