package com.ijpay.alipay;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;

import java.io.Serializable;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWXX</p>
 *
 * <p>支付宝支付配置</p>
 *
 * @author Javen
 */
public class AliPayApiConfig implements Serializable {
	private static final long serialVersionUID = -4736760736935998953L;

	/**
	 * 应用私钥
	 */
	private String privateKey;

	/**
	 * 支付宝公钥
	 */
	private String aliPayPublicKey;

	/**
	 * 应用编号
	 */
	private String appId;

	/**
	 * 支付宝支付网关
	 */
	private String serviceUrl;

	/**
	 * 字符集，为空默认为 UTF-8
	 */
	private String charset;

	/**
	 * 为空默认为 RSA2
	 */
	private String signType;

	/**
	 * 为空默认为 JSON
	 */
	private String format;

	/**
	 * 是否为证书模式
	 */
	private boolean certModel;

	/**
	 * 应用公钥证书 (证书模式必须)
	 */
	private String appCertPath;

	/**
	 * 应用公钥证书文本内容
	 */
	private String appCertContent;

	/**
	 * 支付宝公钥证书 (证书模式必须)
	 */
	private String aliPayCertPath;

	/**
	 * 支付宝公钥证书文本内容
	 */
	private String aliPayCertContent;

	/**
	 * 支付宝根证书 (证书模式必须)
	 */
	private String aliPayRootCertPath;

	/**
	 * 支付宝根证书文本内容
	 */
	private String aliPayRootCertContent;

	/**
	 * 支付宝客户端
	 */
	private AlipayClient alipayClient;

	/**
	 * 其他附加参数
	 */
	private Object exParams;

	/**
	 * 域名
	 */
	private String domain;

	private AliPayApiConfig() {
	}

	public static AliPayApiConfig builder() {
		return new AliPayApiConfig();
	}

	/**
	 * 普通公钥方式
	 *
	 * @return AliPayApiConfig 支付宝配置
	 */
	public AliPayApiConfig build() {
		this.alipayClient = new DefaultAlipayClient(getServiceUrl(), getAppId(), getPrivateKey(), getFormat(),
			getCharset(), getAliPayPublicKey(), getSignType());
		return this;
	}

	/**
	 * 证书模式
	 *
	 * @return AliPayApiConfig 支付宝配置
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public AliPayApiConfig buildByCert() throws AlipayApiException {
		return build(getAppCertPath(), getAliPayCertPath(), getAliPayRootCertPath());
	}

	/**
	 * 证书模式
	 *
	 * @return AliPayApiConfig 支付宝配置
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public AliPayApiConfig buildByCertContent() throws AlipayApiException {
		return buildByCertContent(getAppCertContent(), getAliPayCertContent(), getAliPayRootCertContent());
	}

	/**
	 * @param appCertPath        应用公钥证书路径
	 * @param aliPayCertPath     支付宝公钥证书文件路径
	 * @param aliPayRootCertPath 支付宝CA根证书文件路径
	 * @return {@link AliPayApiConfig}  支付宝支付配置
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public AliPayApiConfig build(String appCertPath, String aliPayCertPath, String aliPayRootCertPath) throws AlipayApiException {
		CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
		certAlipayRequest.setServerUrl(getServiceUrl());
		certAlipayRequest.setAppId(getAppId());
		certAlipayRequest.setPrivateKey(getPrivateKey());
		certAlipayRequest.setFormat(getFormat());
		certAlipayRequest.setCharset(getCharset());
		certAlipayRequest.setSignType(getSignType());
		certAlipayRequest.setCertPath(appCertPath);
		certAlipayRequest.setAlipayPublicCertPath(aliPayCertPath);
		certAlipayRequest.setRootCertPath(aliPayRootCertPath);
		this.alipayClient = new DefaultAlipayClient(certAlipayRequest);
		this.certModel = true;
		return this;
	}

	/**
	 * @param appCertContent        应用公钥证书
	 * @param aliPayCertContent     支付宝公钥证书
	 * @param aliPayRootCertContent 支付宝CA根证书
	 * @return {@link AliPayApiConfig}  支付宝支付配置
	 * @throws AlipayApiException 支付宝 Api 异常
	 */
	public AliPayApiConfig buildByCertContent(String appCertContent, String aliPayCertContent, String aliPayRootCertContent) throws AlipayApiException {
		CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
		certAlipayRequest.setServerUrl(getServiceUrl());
		certAlipayRequest.setAppId(getAppId());
		certAlipayRequest.setPrivateKey(getPrivateKey());
		certAlipayRequest.setFormat(getFormat());
		certAlipayRequest.setCharset(getCharset());
		certAlipayRequest.setSignType(getSignType());
		certAlipayRequest.setCertContent(appCertContent);
		certAlipayRequest.setAlipayPublicCertContent(aliPayCertContent);
		certAlipayRequest.setRootCertContent(aliPayRootCertContent);
		this.alipayClient = new DefaultAlipayClient(certAlipayRequest);
		this.certModel = true;
		return this;
	}

	public String getPrivateKey() {
		if (StrUtil.isBlank(privateKey)) {
			throw new IllegalStateException("privateKey 未被赋值");
		}
		return privateKey;
	}

	public AliPayApiConfig setPrivateKey(String privateKey) {
		if (StrUtil.isEmpty(privateKey)) {
			throw new IllegalArgumentException("privateKey 值不能为 null");
		}
		this.privateKey = privateKey;
		return this;
	}

	public String getAliPayPublicKey() {
		return aliPayPublicKey;
	}

	public AliPayApiConfig setAliPayPublicKey(String aliPayPublicKey) {
		this.aliPayPublicKey = aliPayPublicKey;
		return this;
	}

	public String getAppId() {
		if (StrUtil.isEmpty(appId)) {
			throw new IllegalStateException("appId 未被赋值");
		}
		return appId;
	}

	public AliPayApiConfig setAppId(String appId) {
		if (StrUtil.isEmpty(appId)) {
			throw new IllegalArgumentException("appId 值不能为 null");
		}
		this.appId = appId;
		return this;
	}

	public String getServiceUrl() {
		if (StrUtil.isEmpty(serviceUrl)) {
			throw new IllegalStateException("serviceUrl 未被赋值");
		}
		return serviceUrl;
	}

	public AliPayApiConfig setServiceUrl(String serviceUrl) {
		if (StrUtil.isEmpty(serviceUrl)) {
			serviceUrl = "https://openapi.alipay.com/gateway.do";
		}
		this.serviceUrl = serviceUrl;
		return this;
	}

	public String getCharset() {
		if (StrUtil.isEmpty(charset)) {
			charset = "UTF-8";
		}
		return charset;
	}

	public AliPayApiConfig setCharset(String charset) {
		if (StrUtil.isEmpty(charset)) {
			charset = "UTF-8";
		}
		this.charset = charset;
		return this;
	}

	public String getSignType() {
		if (StrUtil.isEmpty(signType)) {
			signType = "RSA2";
		}
		return signType;
	}

	public AliPayApiConfig setSignType(String signType) {
		if (StrUtil.isEmpty(signType)) {
			signType = "RSA2";
		}
		this.signType = signType;
		return this;
	}

	public String getFormat() {
		if (StrUtil.isEmpty(format)) {
			format = "json";
		}
		return format;
	}

	public String getAppCertPath() {
		return appCertPath;
	}

	public AliPayApiConfig setAppCertPath(String appCertPath) {
		this.appCertPath = appCertPath;
		return this;
	}

	public String getAppCertContent() {
		return appCertContent;
	}

	public AliPayApiConfig setAppCertContent(String appCertContent) {
		this.appCertContent = appCertContent;
		return this;
	}

	public String getAliPayCertPath() {
		return aliPayCertPath;
	}

	public AliPayApiConfig setAliPayCertPath(String aliPayCertPath) {
		this.aliPayCertPath = aliPayCertPath;
		return this;
	}

	public String getAliPayCertContent() {
		return aliPayCertContent;
	}

	public AliPayApiConfig setAliPayCertContent(String aliPayCertContent) {
		this.aliPayCertContent = aliPayCertContent;
		return this;
	}

	public String getAliPayRootCertPath() {
		return aliPayRootCertPath;
	}

	public AliPayApiConfig setAliPayRootCertPath(String aliPayRootCertPath) {
		this.aliPayRootCertPath = aliPayRootCertPath;
		return this;
	}

	public String getAliPayRootCertContent() {
		return aliPayRootCertContent;
	}

	public AliPayApiConfig setAliPayRootCertContent(String aliPayRootCertContent) {
		this.aliPayRootCertContent = aliPayRootCertContent;
		return this;
	}

	public boolean isCertModel() {
		return certModel;
	}

	public AliPayApiConfig setCertModel(boolean certModel) {
		this.certModel = certModel;
		return this;
	}

	public AlipayClient getAliPayClient() {
		if (alipayClient == null) {
			throw new IllegalStateException("aliPayClient 未被初始化");
		}
		return alipayClient;
	}

	public Object getExParams() {
		return exParams;
	}

	public AliPayApiConfig setExParams(Object exParams) {
		this.exParams = exParams;
		return this;
	}

	public String getDomain() {
		return domain;
	}

	public AliPayApiConfig setDomain(String domain) {
		this.domain = domain;
		return this;
	}

	@Override
	public String toString() {
		return JSONUtil.toJsonStr(this);
	}
}
