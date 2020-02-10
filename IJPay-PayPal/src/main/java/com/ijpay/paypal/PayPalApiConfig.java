/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>PayPal支付配置</p>
 *
 * @author Javen
 */
package com.ijpay.paypal;

import com.paypal.base.rest.APIContext;

import java.io.Serializable;

public class PayPalApiConfig implements Serializable {

    private static final long serialVersionUID = -6012811778236113584L;

    private String clientId;
    private String clientSecret;
    private String mode;
    private APIContext apiContext;

    private PayPalApiConfig() {
    }

    public static PayPalApiConfig builder() {
        return new PayPalApiConfig();
    }

    public PayPalApiConfig build() {
        this.apiContext = new APIContext(getClientId(), getClientSecret(),getMode());
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public PayPalApiConfig setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public PayPalApiConfig setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public String getMode() {
        return mode;
    }

    public PayPalApiConfig setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public APIContext getApiContext() {
        return apiContext;
    }

    public PayPalApiConfig setApiContext(APIContext apiContext) {
        this.apiContext = apiContext;
        return this;
    }
}
