package com.ijpay.alipay;

import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

import java.io.Serializable;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
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
    private AlipayClient alipayClient;

    private AliPayApiConfig() {
    }

    public static AliPayApiConfig New() {
        return new AliPayApiConfig();
    }

    public AliPayApiConfig build() {
        this.alipayClient = new DefaultAlipayClient(getServiceUrl(), getAppId(), getPrivateKey(), getFormat(),
                getCharset(), getAliPayPublicKey(), getSignType());
        return this;
    }

    public String getPrivateKey() {
        if (StrUtil.isBlank(privateKey))
            throw new IllegalStateException("privateKey 未被赋值");
        return privateKey;
    }

    public AliPayApiConfig setPrivateKey(String privateKey) {
        if (StrUtil.isEmpty(privateKey))
            throw new IllegalArgumentException("privateKey 值不能为 null");
        this.privateKey = privateKey;
        return this;
    }

    public String getAliPayPublicKey() {
        if (StrUtil.isEmpty(aliPayPublicKey))
            throw new IllegalStateException("aliPayPublicKey 未被赋值");
        return aliPayPublicKey;
    }

    public AliPayApiConfig setAliPayPublicKey(String aliPayPublicKey) {
        if (StrUtil.isEmpty(aliPayPublicKey))
            throw new IllegalArgumentException("aliPayPublicKey 值不能为 null");
        this.aliPayPublicKey = aliPayPublicKey;
        return this;
    }

    public String getAppId() {
        if (StrUtil.isEmpty(appId))
            throw new IllegalStateException("appId 未被赋值");
        return appId;
    }

    public AliPayApiConfig setAppId(String appId) {
        if (StrUtil.isEmpty(appId))
            throw new IllegalArgumentException("appId 值不能为 null");
        this.appId = appId;
        return this;
    }

    public String getServiceUrl() {
        if (StrUtil.isEmpty(serviceUrl))
            throw new IllegalStateException("serviceUrl 未被赋值");
        return serviceUrl;
    }

    public AliPayApiConfig setServiceUrl(String serviceUrl) {
        if (StrUtil.isEmpty(serviceUrl))
            serviceUrl = "https://openapi.alipay.com/gateway.do";
        this.serviceUrl = serviceUrl;
        return this;
    }

    public String getCharset() {
        if (StrUtil.isEmpty(charset))
            charset = "UTF-8";
        return charset;
    }

    public AliPayApiConfig setCharset(String charset) {
        if (StrUtil.isEmpty(charset))
            charset = "UTF-8";
        this.charset = charset;
        return this;
    }

    public String getSignType() {
        if (StrUtil.isEmpty(signType))
            signType = "RSA2";
        return signType;
    }

    public AliPayApiConfig setSignType(String signType) {
        if (StrUtil.isEmpty(signType))
            signType = "RSA2";
        this.signType = signType;
        return this;
    }

    public String getFormat() {
        if (StrUtil.isEmpty(format))
            format = "json";
        return format;
    }

    public AlipayClient getAliPayClient() {
        if (alipayClient == null)
            throw new IllegalStateException("aliPayClient 未被初始化");
        return alipayClient;
    }

}