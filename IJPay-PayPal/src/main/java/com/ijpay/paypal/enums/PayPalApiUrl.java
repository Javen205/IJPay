package com.ijpay.paypal.enums;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>PayPal 支付接口枚举</p>
 *
 * @author Javen
 */
public enum PayPalApiUrl {
	/**
	 * 沙箱环境
	 */
	SANDBOX_GATEWAY("https://api.sandbox.paypal.com"),
	/**
	 * 线上环境
	 */
	LIVE_GATEWAY("https://api.paypal.com"),
	/**
	 * 获取 Access Token
	 */
	GET_TOKEN("/v1/oauth2/token"),
	/**
	 * 订单
	 */
	CHECKOUT_ORDERS("/v2/checkout/orders"),
	/**
	 * 确认订单
	 */
	CAPTURE_ORDER("/v2/checkout/orders/%s/capture"),
	/**
	 * 查询已确认订单
	 */
	CAPTURE_QUERY("/v2/payments/captures/%s"),
	/**
	 * 退款
	 */
	REFUND("/v2/payments/captures/%s/refund"),
	/**
	 * 退款查询
	 */
	REFUND_QUERY("/v2/payments/refunds/%s");

	/**
	 * 类型
	 */
	private final String url;

	PayPalApiUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return url;
	}
}
