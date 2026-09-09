package com.ijpay.alipay.v3;

import cn.hutool.core.util.StrUtil;
import com.alipay.v3.ApiClient;
import com.alipay.v3.ApiException;
import com.alipay.v3.util.model.AlipayConfig;

import java.io.Serializable;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
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
     * 支付宝配置
     */
    private AlipayConfig alipayConfig;

    /**
     * API 客户端
     */
    private ApiClient apiClient;

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

    /**
     * 创建支付宝支付配置构建器
     *
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public static AliPayApiConfig builder() {
        return new AliPayApiConfig();
    }

    /**
     * 构建支付宝 API 客户端
     *
     * @param alipayConfig {@link AlipayConfig}  支付宝配置
     * @param apiClient    {@link ApiClient}  API 客户端
     * @return {@link AliPayApiConfig}  支付宝支付配置
     * @throws ApiException 异常信息
     */
    public AliPayApiConfig build(AlipayConfig alipayConfig, ApiClient apiClient) throws ApiException {
        this.alipayConfig = alipayConfig;
        this.apiClient = apiClient;
        this.apiClient.setAlipayConfig(alipayConfig);
        return this;
    }

    /**
     * 普通公钥方式构建支付宝 API 客户端
     *
     * @return {@link AliPayApiConfig} 支付宝支付配置
     * @throws ApiException 异常信息
     */
    public AliPayApiConfig build() throws ApiException {
        alipayConfig = new AlipayConfig();
        apiClient = new ApiClient();
        alipayConfig.setAppId(getAppId());
        alipayConfig.setPrivateKey(getPrivateKey());
        alipayConfig.setAlipayPublicKey(getAliPayPublicKey());
        apiClient.setBasePath(getServiceUrl());
        apiClient.setAlipayConfig(this.alipayConfig);
        return this;
    }

    /**
     * 证书模式构建支付宝 API 客户端
     *
     * @return {@link AliPayApiConfig} 支付宝支付配置
     * @throws ApiException 异常信息
     */
    public AliPayApiConfig buildByCert() throws ApiException {
        certModel = true;
        alipayConfig = new AlipayConfig();
        apiClient = new ApiClient();
        alipayConfig.setAppId(getAppId());
        alipayConfig.setPrivateKey(getPrivateKey());
        alipayConfig.setAppCertPath(getAppCertPath());
        alipayConfig.setAlipayPublicCertPath(getAliPayCertPath());
        alipayConfig.setRootCertPath(getAliPayRootCertPath());
        apiClient.setBasePath(getServiceUrl());
        apiClient.setAlipayConfig(this.alipayConfig);
        return this;
    }

    /**
     * 获取应用私钥
     *
     * @return 应用私钥
     */
    public String getPrivateKey() {
        if (StrUtil.isBlank(privateKey)) {
            throw new IllegalStateException("privateKey 未被赋值");
        }
        return privateKey;
    }

    /**
     * 设置应用私钥
     *
     * @param privateKey 应用私钥
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setPrivateKey(String privateKey) {
        if (StrUtil.isEmpty(privateKey)) {
            throw new IllegalArgumentException("privateKey 值不能为 null");
        }
        this.privateKey = privateKey;
        return this;
    }

    /**
     * 获取支付宝公钥
     *
     * @return 支付宝公钥
     */
    public String getAliPayPublicKey() {
        return aliPayPublicKey;
    }

    /**
     * 设置支付宝公钥
     *
     * @param aliPayPublicKey 支付宝公钥
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setAliPayPublicKey(String aliPayPublicKey) {
        this.aliPayPublicKey = aliPayPublicKey;
        return this;
    }

    /**
     * 获取应用编号
     *
     * @return 应用编号
     */
    public String getAppId() {
        if (StrUtil.isEmpty(appId)) {
            throw new IllegalStateException("appId 未被赋值");
        }
        return appId;
    }

    /**
     * 设置应用编号
     *
     * @param appId 应用编号
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setAppId(String appId) {
        if (StrUtil.isEmpty(appId)) {
            throw new IllegalArgumentException("appId 值不能为 null");
        }
        this.appId = appId;
        return this;
    }

    /**
     * 获取支付宝支付网关
     *
     * @return 支付宝支付网关
     */
    public String getServiceUrl() {
        if (StrUtil.isEmpty(serviceUrl)) {
            throw new IllegalStateException("serviceUrl 未被赋值");
        }
        return serviceUrl;
    }

    /**
	 * 设置支付宝支付网关，为空时默认为 <a href="https://openapi.alipay.com/gateway.do">支付网关</a>
	 *
	 * @param serviceUrl 支付宝支付网关
	 * @return {@link AliPayApiConfig} 支付宝支付配置
	 */
    public AliPayApiConfig setServiceUrl(String serviceUrl) {
        if (StrUtil.isEmpty(serviceUrl)) {
            serviceUrl = "https://openapi.alipay.com/gateway.do";
        }
        this.serviceUrl = serviceUrl;
        return this;
    }

    /**
     * 获取字符集，为空时默认为 UTF-8
     *
     * @return 字符集
     */
    public String getCharset() {
        if (StrUtil.isEmpty(charset)) {
            charset = "UTF-8";
        }
        return charset;
    }

    /**
     * 设置字符集，为空时默认为 UTF-8
     *
     * @param charset 字符集
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setCharset(String charset) {
        if (StrUtil.isEmpty(charset)) {
            charset = "UTF-8";
        }
        this.charset = charset;
        return this;
    }

    /**
     * 获取签名方式，为空时默认为 RSA2
     *
     * @return 签名方式
     */
    public String getSignType() {
        if (StrUtil.isEmpty(signType)) {
            signType = "RSA2";
        }
        return signType;
    }

    /**
     * 设置签名方式，为空时默认为 RSA2
     *
     * @param signType 签名方式
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setSignType(String signType) {
        if (StrUtil.isEmpty(signType)) {
            signType = "RSA2";
        }
        this.signType = signType;
        return this;
    }

    /**
     * 获取数据格式，为空时默认为 json
     *
     * @return 数据格式
     */
    public String getFormat() {
        if (StrUtil.isEmpty(format)) {
            format = "json";
        }
        return format;
    }

    /**
     * 获取应用公钥证书路径
     *
     * @return 应用公钥证书路径
     */
    public String getAppCertPath() {
        return appCertPath;
    }

    /**
     * 设置应用公钥证书路径
     *
     * @param appCertPath 应用公钥证书路径
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setAppCertPath(String appCertPath) {
        this.appCertPath = appCertPath;
        return this;
    }

    /**
     * 获取应用公钥证书文本内容
     *
     * @return 应用公钥证书文本内容
     */
    public String getAppCertContent() {
        return appCertContent;
    }

    /**
     * 设置应用公钥证书文本内容
     *
     * @param appCertContent 应用公钥证书文本内容
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setAppCertContent(String appCertContent) {
        this.appCertContent = appCertContent;
        return this;
    }

    /**
     * 获取支付宝公钥证书路径
     *
     * @return 支付宝公钥证书路径
     */
    public String getAliPayCertPath() {
        return aliPayCertPath;
    }

    /**
     * 设置支付宝公钥证书路径
     *
     * @param aliPayCertPath 支付宝公钥证书路径
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setAliPayCertPath(String aliPayCertPath) {
        this.aliPayCertPath = aliPayCertPath;
        return this;
    }

    /**
     * 获取支付宝公钥证书文本内容
     *
     * @return 支付宝公钥证书文本内容
     */
    public String getAliPayCertContent() {
        return aliPayCertContent;
    }

    /**
     * 设置支付宝公钥证书文本内容
     *
     * @param aliPayCertContent 支付宝公钥证书文本内容
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setAliPayCertContent(String aliPayCertContent) {
        this.aliPayCertContent = aliPayCertContent;
        return this;
    }

    /**
     * 获取支付宝根证书路径
     *
     * @return 支付宝根证书路径
     */
    public String getAliPayRootCertPath() {
        return aliPayRootCertPath;
    }

    /**
     * 设置支付宝根证书路径
     *
     * @param aliPayRootCertPath 支付宝根证书路径
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setAliPayRootCertPath(String aliPayRootCertPath) {
        this.aliPayRootCertPath = aliPayRootCertPath;
        return this;
    }

    /**
     * 获取支付宝根证书文本内容
     *
     * @return 支付宝根证书文本内容
     */
    public String getAliPayRootCertContent() {
        return aliPayRootCertContent;
    }

    /**
     * 设置支付宝根证书文本内容
     *
     * @param aliPayRootCertContent 支付宝根证书文本内容
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setAliPayRootCertContent(String aliPayRootCertContent) {
        this.aliPayRootCertContent = aliPayRootCertContent;
        return this;
    }

    /**
     * 是否为证书模式
     *
     * @return 是否为证书模式
     */
    public boolean isCertModel() {
        return certModel;
    }

    /**
     * 设置是否为证书模式
     *
     * @param certModel 是否为证书模式
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setCertModel(boolean certModel) {
        this.certModel = certModel;
        return this;
    }

    /**
     * 获取其他附加参数
     *
     * @return 其他附加参数
     */
    public Object getExParams() {
        return exParams;
    }

    /**
     * 设置其他附加参数
     *
     * @param exParams 其他附加参数
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setExParams(Object exParams) {
        this.exParams = exParams;
        return this;
    }

    /**
     * 获取域名
     *
     * @return 域名
     */
    public String getDomain() {
        return domain;
    }

    /**
     * 设置域名
     *
     * @param domain 域名
     * @return {@link AliPayApiConfig} 支付宝支付配置
     */
    public AliPayApiConfig setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    /**
     * 获取官方支付宝配置
     *
     * @return {@link AlipayConfig} 官方支付宝配置
     */
    public AlipayConfig getAlipayConfig() {
        return alipayConfig;
    }

    /**
     * 获取 API 客户端
     *
     * @return {@link ApiClient} API 客户端
     */
    public ApiClient getApiClient() {
        return apiClient;
    }
}
