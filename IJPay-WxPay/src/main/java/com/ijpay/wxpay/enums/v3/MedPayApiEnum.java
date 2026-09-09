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
 * <p>微信支付 v3 接口-医保支付接口枚举</p>
 *
 * @author Javen
 */
public enum MedPayApiEnum implements WxApiEnum {

	/**
	 * 医保自费混合收款下单
	 */
	MED_INS_ORDERS("/v3/med-ins/orders", "医保自费混合收款下单"),

	/**
	 * 使用医保自费混合订单号查看下单结果
	 */
	MED_INS_ORDER_QUERY_BY_MIX_TRADE_NO("/v3/med-ins/orders/mix-trade-no/%s", "医保自费混合订单号查询下单结果"),

	/**
	 * 使用商户订单号查看下单结果
	 */
	MED_INS_ORDER_QUERY_BY_OUT_TRADE_NO("/v3/med-ins/orders/out-trade-no/%s", "商户订单号查询下单结果"),

	/**
	 * 医保退款结果通知
	 */
	MED_INS_REFUNDS_NOTIFY("/v3/med-ins/refunds/notify", "医保退款结果通知"),
	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	MedPayApiEnum(String url, String desc) {
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

	@Override
	public String toString() {
		return url;
	}
}
