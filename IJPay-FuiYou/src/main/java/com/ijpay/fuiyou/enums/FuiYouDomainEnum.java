package com.ijpay.fuiyou.enums;

import lombok.Getter;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、富友支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>富友支付 API 接口域名枚举</p>
 *
 * @author Javen
 */
public enum FuiYouDomainEnum implements FuiYouDomain {

	/**
	 * 测试环境
	 */
	TEST("https://aipaytest.fuioupay.com", "富友支付测试环境"),

	/**
	 * 生产环境
	 */
	PROD("https://aipay-cloud.fuioupay.com", "富友支付生产环境");

	/**
	 * 域名
	 */
	private final String domain;
	/**
	 * 描述
	 */
	@Getter
	private final String desc;

	FuiYouDomainEnum(String domain, String desc) {
		this.domain = domain;
		this.desc = desc;
	}

	@Override
	public String getDomain() {
		return domain;
	}

	@Override
	public String toString() {
		return domain;
	}
}
