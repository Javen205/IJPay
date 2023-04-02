package com.ijpay.core.enums;


/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信支付 v3 接口授权认证类型枚举</p>
 *
 * @author Javen
 */
public enum AuthTypeEnum {
	/**
	 * 国密
	 */
	SM("WECHATPAY2-SM2-WITH-SM3", "国密算法"),
	/**
	 * RSA
	 */
	RSA("WECHATPAY2-SHA256-RSA2048", "RSA算法"),
	;

	private final String url;

	private final String desc;

	AuthTypeEnum(String url, String desc) {
		this.url = url;
		this.desc = desc;
	}

	/**
	 * 获取枚举URL
	 *
	 * @return 枚举编码
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 获取详细的描述信息
	 *
	 * @return 描述信息
	 */
	public String getDesc() {
		return desc;
	}
}
