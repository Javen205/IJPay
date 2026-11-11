package com.ijpay.demo.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * <p>IJPay 让支付触手可及，支付宝配置 Bean。</p>
 *
 * @author Javen
 */
@Data
@Component
@PropertySource("classpath:/alipay.properties")
@ConfigurationProperties(prefix = "alipay")
public class AliPayBean {
	private String appId;
	private String privateKey;
	private String publicKey;
	private String serverUrl;
	private String domain;
}
