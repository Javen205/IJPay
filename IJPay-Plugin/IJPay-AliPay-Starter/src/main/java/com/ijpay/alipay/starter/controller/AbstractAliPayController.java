package com.ijpay.alipay.starter.controller;

import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.alipay.starter.constants.IJPayConstant;
import com.ijpay.alipay.starter.properties.AliPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;


/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * @author Javen
 */
@Slf4j
public class AbstractAliPayController extends AbstractAliPayApiController {

	/**
	 * starter 自动生成的支付宝支付配置,默认取配置的第一个
	 */
	@Resource
	protected AliPayApiConfig aliPayApiConfig;

	/**
	 * yml 配置
	 */
	@Resource
	protected AliPayProperties aliPayProperties;

	/**
	 * 获取支付宝支付配置
	 *
	 * @return {@link AliPayApiConfig} 支付宝支付配置
	 */
	@Override
	public AliPayApiConfig getApiConfig() {
		AliPayApiConfig apiConfig;
		try {
			apiConfig = AliPayApiConfigKit.getAliPayApiConfig();
		} catch (Exception e) {
			apiConfig = aliPayApiConfig;
		}
		return apiConfig;
	}

	@RequestMapping(value = "/onlineContact", method = {RequestMethod.GET, RequestMethod.POST})
	public String onlineContact() {
		return IJPayConstant.ONLINE_CONTACT;
	}

	@RequestMapping(value = "/getCurrentConfig", method = {RequestMethod.GET, RequestMethod.POST})
	public AliPayApiConfig getCurrentConfig() {
		return AliPayApiConfigKit.getAliPayApiConfig();
	}

}
