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
public enum WxPayUrlEnum implements PayUrl {

	/**
	 * 刷卡支付
	 */
	CODE_PAY("/api/pay/wxpay/codePay"),

	/**
	 * 扫码支付
	 */
	NATIVE_PAY("/api/pay/wxpay/nativePay"),

	/**
	 * 小程序支付
	 */
	MIN_APP_PAY("/api/pay/wxpay/minAppPay"),

	/**
	 * 公众号支付
	 */
	JS_API_PAY("/api/pay/wxpay/jsapi"),

	/**
	 * 收银台支付
	 */
	CASHIER_PAY("/api/pay/wxpay/cashierPay"),

	/**
	 * 刷脸支付
	 */
	FACE_PAY("/api/pay/wxpay/facePay"),

	/**
	 * 刷脸支付凭证
	 */
	GET_FACE_PAY_AUTH_INFO("/api/pay/wxpay/getFacePayAuthInfo"),

	/**
	 * H5 支付
	 */
	WAP_PAY("/api/pay/wxpay/wapPay"),

	/**
	 * APP 支付
	 */
	APP_PAY("/api/pay/wxpay/appPay"),

	/**
	 * QQ小程序支付
	 */
	QQ_PAY("/api/pay/wxpay/qqPay"),

	/**
	 * 退款
	 */
	REFUND_ORDER("/api/pay/wxpay/refundOrder"),

	/**
	 * 查询退款
	 */
	REFUND_QUERY("/api/pay/wxpay/getRefundResult"),

	/**
	 * 查询投诉
	 */
	GET_COMPLAINT("/api/pay/wxpay/getComplaint"),

	/**
	 * 关闭订单
	 */
	CLOSE_ORDER("/api/pay/wxpay/closeOrder"),

	/**
	 * 撤销订单
	 */
	REVERSE_ORDER("/api/pay/wxpay/reverseOrder"),

	/**
	 * 查询微信结算信息
	 */
	WX_BILL_INFO("/api/pay/wxpay/getWxBillInfo"),

	/**
	 * 下载对账单
	 */
	DOWNLOAD_BILL("/api/pay/wxpay/downloadBill"),

	/**
	 * 查询刷卡支付结果
	 */
	GET_CODE_PAY_RESULT("/api/pay/wxpay/getCodePayResult"),

	/**
	 * 获取微信授权链接
	 */
	GET_OAUTH_URL("/api/wxlogin/getOauthUrl"),

	/**
	 * 查询微信授权信息
	 */
	GET_BASE_OAUTH_INFO("/api/wxlogin/getBaseOauthInfo"),

	/**
	 * 查询订单
	 */
	GET_ORDER_INFO("/api/system/order/getPayOrderInfo"),

	/**
	 * 微信扫码登录
	 */
	GET_WEB_LOGIN("/api/wx/getWebLogin"),

	/**
	 * 查询授权信息
	 */
	GET_OAUTH_INFO("/api/wx/getOauthInfo"),

	/**
	 * 一码付
	 */
	MERGE_NATIVE_PAY("/api/pay/merge/nativePay"),
	;

	/**
	 * 接口 url
	 */
	private final String url;

	WxPayUrlEnum(String url) {
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
