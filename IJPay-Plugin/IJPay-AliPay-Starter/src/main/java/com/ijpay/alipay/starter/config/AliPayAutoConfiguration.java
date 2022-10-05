package com.ijpay.alipay.starter.config;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.starter.properties.AliPayProperties;
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
@EnableConfigurationProperties(AliPayProperties.class)
@ConditionalOnClass(AliPayApiConfig.class)
@ConditionalOnProperty(prefix = "alipay.pay", value = "enabled", matchIfMissing = true)
public class AliPayAutoConfiguration {

	/**
	 * 支付宝支付配置
	 */
	private final AliPayProperties aliPayProperties;

	@Autowired
	public AliPayAutoConfiguration(AliPayProperties aliPayProperties) {
		this.aliPayProperties = aliPayProperties;
	}

	/**
	 * 构造支付宝支付常用配置
	 *
	 * @return {@link AliPayApiConfig}  支付宝支付常用配置
	 */
	@Bean
	@ConditionalOnMissingBean(AliPayApiConfig.class)
	public AliPayApiConfig aliPayApiConfig() {
		if (null == aliPayProperties || CollUtil.isEmpty(aliPayProperties.getConfigs())) {
			log.error("未获取到任何支付宝支付的配置信息,如有疑问请联系 723992875、864988890");
			return null;
		}
		AliPayApiConfig config = aliPayProperties.getConfigs().get(0);
		if (config.isCertModel()) {
			log.debug("即将尝试使用「证书模式」来构建 AlipayClient");
			try {
				if (StrUtil.isAllNotEmpty(config.getAppCertPath(), config.getAliPayCertPath(), config.getAliPayRootCertPath())) {
					log.debug("使用证书文件路径来构建 AlipayClient");
					config = config.buildByCert();
				} else {
					log.debug("支付宝支付证书文件路径不全，即将尝试使用证书文件内容来构建 AlipayClient");
					if (StrUtil.isAllNotEmpty(config.getAppCertPath(), config.getAliPayCertPath(), config.getAliPayRootCertPath())) {
						log.debug("使用证书文件内容来构建 AlipayClient");
						config = config.buildByCertContent();
					}
				}
			} catch (AlipayApiException e) {
				throw new RuntimeException("构建 AlipayClient 失败请检查支付宝支付配置信息", e);
			}
		} else {
			log.debug("使用「普通公钥方式」来构建 AlipayClient");
			config = config.build();
		}
		log.debug("自动注入的支付宝支付配置信息为 {}", config);
		return config;
	}

}
