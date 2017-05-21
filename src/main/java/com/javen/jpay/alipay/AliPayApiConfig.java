package com.javen.jpay.alipay;

import java.io.Serializable;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
/**
 * @Email javen205@126.com
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
		this.alipayClient = new DefaultAlipayClient(serviceUrl, appId, privateKey, format, charset, alipayPublicKey,signType);
		return this;
	}
	
	

	public String getPrivateKey() {
		if (privateKey == null)
			throw new IllegalStateException("privateKey 未被赋值");
		return privateKey;
	}
	public AliPayApiConfig setPrivateKey(String privateKey) {
		if (privateKey == null)
			throw new IllegalArgumentException("privateKey 值不能为 null");
		this.privateKey = privateKey;
		return this;
	}

	public String getAlipayPublicKey() {
		if (alipayPublicKey == null)
			throw new IllegalStateException("alipayPublicKey 未被赋值");
		return alipayPublicKey;
	}
	public AliPayApiConfig setAlipayPublicKey(String alipayPublicKey) {
		if (alipayPublicKey == null)
			throw new IllegalArgumentException("alipayPublicKey 值不能为 null");
		this.alipayPublicKey = alipayPublicKey;
		return this;
	}

	public String getAppId() {
		if (appId == null)
			throw new IllegalStateException("appId 未被赋值");
		return appId;
	}
	public AliPayApiConfig setAppId(String appId) {
		if (appId == null)
			throw new IllegalArgumentException("appId 值不能为 null");
		this.appId = appId;
		return this;
	}

	public String getServiceUrl() {
		if (serviceUrl == null)
			throw new IllegalStateException("serviceUrl 未被赋值");
		return serviceUrl;
	}
	

	public AliPayApiConfig setServiceUrl(String serviceUrl) {
		if (serviceUrl == null)
			serviceUrl = "https://openapi.alipay.com/gateway.do";
		this.serviceUrl = serviceUrl;
		return this;
	}

	public String getCharset() {
		if (charset == null)
			charset = "UTF-8";
		return charset;
	}

	public AliPayApiConfig setCharset(String charset) {
		if (serviceUrl == null)
			serviceUrl = "UTF-8";
		this.charset = charset;
		return this;
	}

	public String getSignType() {
		if (signType == null)
			signType = "RSA2";
		return signType;
	}

	public AliPayApiConfig setSignType(String signType) {
		if (signType == null)
			signType = "RSA2";
		this.signType = signType;
		return this;
	}

	public String getFormat() {
		if (format == null)
			format = "json";
		return format;
	}

	public AlipayClient getAlipayClient() {
		if (alipayClient == null)
			throw new IllegalStateException("alipayClient 未被初始化");
		return alipayClient;
	}

}
