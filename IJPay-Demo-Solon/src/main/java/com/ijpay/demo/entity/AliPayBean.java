package com.ijpay.demo.entity;

import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Import;
import org.noear.solon.annotation.Inject;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>支付宝配置 Bean</p>
 *
 * @author Javen
 */
@Inject("${alipay}")
@Import(profiles = "classpath:/alipay.properties")
@Configuration
public class AliPayBean {
	private String appId;
	private String privateKey;
	private String publicKey;
	private String appCertPath;
	private String aliPayCertPath;
	private String aliPayRootCertPath;
	private String serverUrl;
	private String domain;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getAppCertPath() {
		return appCertPath;
	}

	public void setAppCertPath(String appCertPath) {
		this.appCertPath = appCertPath;
	}

	public String getAliPayCertPath() {
		return aliPayCertPath;
	}

	public void setAliPayCertPath(String aliPayCertPath) {
		this.aliPayCertPath = aliPayCertPath;
	}

	public String getAliPayRootCertPath() {
		return aliPayRootCertPath;
	}

	public void setAliPayRootCertPath(String aliPayRootCertPath) {
		this.aliPayRootCertPath = aliPayRootCertPath;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}


	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public String toString() {
		return "AliPayBean{" +
			"appId='" + appId + '\'' +
			", privateKey='" + privateKey + '\'' +
			", publicKey='" + publicKey + '\'' +
			", appCertPath='" + appCertPath + '\'' +
			", aliPayCertPath='" + aliPayCertPath + '\'' +
			", aliPayRootCertPath='" + aliPayRootCertPath + '\'' +
			", serverUrl='" + serverUrl + '\'' +
			", domain='" + domain + '\'' +
			'}';
	}
}
