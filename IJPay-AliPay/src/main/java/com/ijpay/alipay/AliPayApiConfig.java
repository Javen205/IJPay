package com.ijpay.alipay;

import cn.hutool.core.util.StrUtil;
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
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWXX</p>
 *
 * <p>支付宝支付配置</p>
 *
 * @author Javen
 */
public class AliPayApiConfig implements Serializable {
    private static final long serialVersionUID = -4736760736935998953L;

    private String privateKey;
    private String aliPayPublicKey;
    private String appId;
    private String serviceUrl;
    private String charset;
    private String signType;
    private String format;
    private boolean certModel;
    private String appCertPath;
    private String aliPayCertPath;
    private String aliPayRootCertPath;
    private AlipayClient alipayClient;

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

    public String getAliPayCertPath() {
        return aliPayCertPath;
    }

    public AliPayApiConfig setAliPayCertPath(String aliPayCertPath) {
        this.aliPayCertPath = aliPayCertPath;
        return this;
    }

    public String getAliPayRootCertPath() {
        return aliPayRootCertPath;
    }

    public AliPayApiConfig setAliPayRootCertPath(String aliPayRootCertPath) {
        this.aliPayRootCertPath = aliPayRootCertPath;
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

}