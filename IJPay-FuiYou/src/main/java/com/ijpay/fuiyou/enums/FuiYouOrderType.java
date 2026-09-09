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
 * <p>富友支付订单类型枚举</p>
 *
 * @author Javen
 */
public enum FuiYouOrderType {

	/**
	 * 支付宝（统一下单、条码支付、服务窗支付）
	 */
	ALI_PAY("ALIPAY", "支付宝"),
	/**
	 * 微信（统一下单、条码支付、公众号支付、小程序）
	 */
	WE_CHAT("WECHAT", "微信"),
	/**
	 * 微信 APP
	 */
	WX_APP("WXAPP", "微信APP"),
	/**
	 * 支付宝 APP
	 */
	ALI_APP("ALIAPP", "支付宝APP"),
	/**
	 * 微信 H5
	 */
	WX_H5("WXH5", "微信H5"),
	/**
	 * 支付宝 H5
	 */
	ALI_H5("ALIH5", "支付宝H5"),
	/**
	 * 微信保险类
	 */
	WX_BX("WXBX", "微信保险类"),
	/**
	 * 支付宝保险类
	 */
	AL_BX("ALBX", "支付宝保险类"),
	/**
	 * 微信线上所有交易
	 */
	WX_XS("WXXS", "微信线上"),
	/**
	 * 银联二维码
	 */
	UNION_PAY("UNIONPAY", "银联二维码"),
	/**
	 * 京东钱包
	 */
	JD("JD", "京东钱包"),
	/**
	 * QQ 钱包
	 */
	QQ("QQ", "QQ钱包"),
	/**
	 * 翼支付
	 */
	BEST_PAY("BESTPAY", "翼支付"),
	/**
	 * 数字货币
	 */
	DI_GI_CCY("DIGICCY", "数字货币");

	/**
	 * 订单类型值
	 */
	private final String type;
	/**
	 * 描述
	 */
	private final String desc;

	FuiYouOrderType(String type, String desc) {
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
