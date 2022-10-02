package com.ijpay.wxpay.starter.config;


import cn.hutool.core.collection.CollUtil;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.starter.properties.WxPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>自动配置</p>
 *
 * @author Javen
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(WxPayProperties.class)
@ConditionalOnClass(WxPayApiConfig.class)
@ConditionalOnProperty(prefix = "wx.pay", value = "enabled", matchIfMissing = true)
public class WxPayAutoConfiguration {

	/**
	 * 微信支付配置
	 */
	private final WxPayProperties wxPayProperties;

	@Autowired
	public WxPayAutoConfiguration(WxPayProperties wxPayProperties) {
		this.wxPayProperties = wxPayProperties;
	}

	/**
	 * 构造微信支付常用配置
	 *
	 * @return {@link WxPayApiConfig}  微信支付常用配置
	 */
	@Bean
	@ConditionalOnMissingBean(WxPayApiConfig.class)
	public WxPayApiConfig wxPayApiConfig() {
		if (null == wxPayProperties || CollUtil.isEmpty(wxPayProperties.getConfigs())) {
			log.error("未获取到任何微信支付的配置信息,如有疑问请联系 723992875、864988890");
			return new WxPayApiConfig();
		}
		WxPayApiConfig config = wxPayProperties.getConfigs().get(0);
		log.debug("自动注入的微信支付配置信息为 {}", config);
		return config;
	}

}
