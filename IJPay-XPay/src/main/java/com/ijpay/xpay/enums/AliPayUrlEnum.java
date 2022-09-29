package com.ijpay.xpay.enums;

import com.ijpay.xpay.PayUrl;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信、支付宝官方个人支付渠道，官方直连的异步回调通知，加企鹅(572839485)了解更多</p>
 *
 * <p> XPay 相关接口</p>
 *
 * @author Javen
 */
public enum AliPayUrlEnum implements PayUrl {

	/**
	 * 支付宝付款码支付
	 */
	ALI_CODE_PAY("/api/pay/alipay/codePay"),

	/**
	 * 支付宝扫码支付
	 */
	ALI_NATIVE_PAY("/api/pay/alipay/nativePay"),

	/**
	 * 支付宝 H5 支付
	 */
	ALI_WAP_PAY("/api/pay/alipay/wapPay"),

	/**
	 * 支付宝 JS 支付
	 */
	ALI_JS_PAY("/api/pay/alipay/jsPay"),

	/**
	 * H5支付
	 */
	ALI_MOBILE_PAY("/api/pay/alipay/mobilePay"),

	/**
	 * 支付宝 APP 支付
	 */
	ALI_APP_PAY("/api/pay/wxpay/appPay"),

	/**
	 * 电脑网站支付
	 */
	ALI_WEB_PAY("/api/pay/alipay/webPay"),

	/**
	 * 关闭订单
	 */
	ALI_CLOSE_ORDER("/api/pay/alipay/closeOrder"),

	/**
	 * 撤销订单
	 */
	ALI_REVERSE_ORDER("/api/pay/alipay/reverseOrder"),

	/**
	 * 支付宝退款
	 */
	ALI_PAY_REFUND_ORDER("/api/pay/alipay/refundOrder"),

	/**
	 * 支付宝查询退款
	 */
	ALI_PAY_REFUND_QUERY("/api/pay/alipay/getRefundResult"),
	;

	/**
	 * 接口 url
	 */
	private final String url;

	AliPayUrlEnum(String url) {
		this.url = url;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return url;
	}
}
