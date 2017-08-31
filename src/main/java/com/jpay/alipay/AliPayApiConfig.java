package com.jpay.alipay;

import java.io.Serializable;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.jpay.ext.kit.StrKit;
/**
 * @author Javen
 * 2017年5月20日
 */
public class AliPayApiConfig implements Serializable {
	private static final long serialVersionUID = -4736760736935998953L;

	private String privateKey;
	private String alipayPublicKey;
	private String appId;
	private String serviceUrl;
	private String charset;
	private String signType;
	private String format;
	private AlipayClient alipayClient;
	
	private AliPayApiConfig() {
	}

	public static AliPayApiConfig New() {
		return new AliPayApiConfig();
	}
	
	public AliPayApiConfig build(){
		this.alipayClient = new DefaultAlipayClient(getServiceUrl(), getAppId(), getPrivateKey(), getFormat(), getCharset(), getAlipayPublicKey(),getSignType());
		return this;
	}

	public String getPrivateKey() {
		if (StrKit.isBlank(privateKey))
			throw new IllegalStateException("privateKey 未被赋值");
		return privateKey;
	}
	public AliPayApiConfig setPrivateKey(String privateKey) {
		if (StrKit.isBlank(privateKey))
			throw new IllegalArgumentException("privateKey 值不能为 null");
		this.privateKey = privateKey;
		return this;
	}

	public String getAlipayPublicKey() {
		if (StrKit.isBlank(alipayPublicKey))
			throw new IllegalStateException("alipayPublicKey 未被赋值");
		return alipayPublicKey;
	}
	public AliPayApiConfig setAlipayPublicKey(String alipayPublicKey) {
		if (StrKit.isBlank(alipayPublicKey))
			throw new IllegalArgumentException("alipayPublicKey 值不能为 null");
		this.alipayPublicKey = alipayPublicKey;
		return this;
	}

	public String getAppId() {
		if (StrKit.isBlank(appId))
			throw new IllegalStateException("appId 未被赋值");
		return appId;
	}
	public AliPayApiConfig setAppId(String appId) {
		if (StrKit.isBlank(appId))
			throw new IllegalArgumentException("appId 值不能为 null");
		this.appId = appId;
		return this;
	}

	public String getServiceUrl() {
		if (StrKit.isBlank(serviceUrl))
			throw new IllegalStateException("serviceUrl 未被赋值");
		return serviceUrl;
	}
	

	public AliPayApiConfig setServiceUrl(String serviceUrl) {
		if (StrKit.isBlank(serviceUrl))
			serviceUrl = "https://openapi.alipay.com/gateway.do";
		this.serviceUrl = serviceUrl;
		return this;
	}

	public String getCharset() {
		if (StrKit.isBlank(charset))
			charset = "UTF-8";
		return charset;
	}

	public AliPayApiConfig setCharset(String charset) {
		if (StrKit.isBlank(charset))
			charset = "UTF-8";
		this.charset = charset;
		return this;
	}

	public String getSignType() {
		if (StrKit.isBlank(signType))
			signType = "RSA2";
		return signType;
	}

	public AliPayApiConfig setSignType(String signType) {
		if (StrKit.isBlank(signType))
			signType = "RSA2";
		this.signType = signType;
		return this;
	}

	public String getFormat() {
		if (StrKit.isBlank(format))
			format = "json";
		return format;
	}

	public AlipayClient getAlipayClient() {
		if (alipayClient == null)
			throw new IllegalStateException("alipayClient 未被初始化");
		return alipayClient;
	}

}
