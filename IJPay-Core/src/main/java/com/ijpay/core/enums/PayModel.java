package com.ijpay.core.enums;


/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>商户平台模式</p>
 *
 * @author Javen
 */
public enum PayModel {
	/**
	 * 商户模式
	 */
	BUSINESS_MODEL("BUSINESS_MODEL"),
	/**
	 * 服务商模式
	 */
	SERVICE_MODE("SERVICE_MODE");

	PayModel(String payModel) {
		this.payModel = payModel;
	}

	/**
	 * 商户模式
	 */
	private final String payModel;


	public String getPayModel() {
		return payModel;
	}
}
