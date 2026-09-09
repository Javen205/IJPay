package com.ijpay.demo.controller.alipay;

import com.ijpay.alipay.v3.AliPayApiConfig;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>支付宝支付 V3 配置抽象类</p>
 *
 * @author Javen
 */
public abstract class AbstractAliPayV3ApiController {

	/**
	 * 获取支付宝 V3 支付配置
	 *
	 * @return {@link AliPayApiConfig} 支付宝 V3 支付配置
	 * @throws com.alipay.v3.ApiException 异常信息
	 */
	public abstract AliPayApiConfig getApiConfig() throws com.alipay.v3.ApiException;
}
