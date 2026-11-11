package com.ijpay.demo.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * <p>IJPay 让支付触手可及，微信支付 Api-v3 配置 Bean。</p>
 *
 * @author Javen
 */
@Data
@Component
@PropertySource("classpath:/wxpay_v3.properties")
@ConfigurationProperties(prefix = "v3")
public class WxPayV3Bean {
	private String appId;
	private String keyPath;
	private String publicKeyPath;
	private String certPath;
	private String platformCertPath;
	private String mchId;
	private String apiKey3;
	private String domain;
	private String publicKeyId;
}
