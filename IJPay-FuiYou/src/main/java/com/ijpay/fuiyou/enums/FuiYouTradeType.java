package com.ijpay.fuiyou.enums;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、富友支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>富友支付交易类型枚举（公众号/服务窗统一下单 trade_type）</p>
 *
 * @author Javen
 */
public enum FuiYouTradeType {

	/**
	 * 公众号线下支付
	 */
	JSAPI("JSAPI", "公众号线下支付"),
	/**
	 * 小程序支付
	 */
	LETPAY("LETPAY", "小程序支付"),
	/**
	 * APP 支付（暂不可用）
	 */
	APP("APP", "APP支付"),
	/**
	 * 支付宝服务窗
	 */
	FWC("FWC", "支付宝服务窗"),
	/**
	 * 微信公众号保险类
	 */
	WX_BX_JS("WXBXJS", "微信公众号保险类"),
	/**
	 * 微信小程序保险类
	 */
	WX_BX_LET("WXBXLET", "微信小程序保险类"),
	/**
	 * 微信APP保险类
	 */
	WX_BX_APP("WXBXAPP", "微信APP保险类"),
	/**
	 * 支付宝服务窗保险类
	 */
	AL_BX_JS("ALBXJS", "支付宝服务窗保险类"),
	/**
	 * 相机扫码
	 */
	APP_LE_PAY("APPLEPAY", "相机扫码"),
	/**
	 * 云闪付扫码
	 */
	UNION_PAY("UNIONPAY", "云闪付扫码"),
	/**
	 * 翼支付 js
	 */
	BEST_PAY("BESTPAY", "翼支付js");

	/**
	 * 交易类型值
	 */
	private final String type;
	/**
	 * 描述
	 */
	private final String desc;

	FuiYouTradeType(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	@Override
	public String toString() {
		return type;
	}
}
