package com.ijpay.paypal.enums;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>PayPal 支付接口枚举</p>
 *
 * @author Javen
 */
public enum PayPalApiUrl {
    /**
     * 沙箱环境
     */
    SANDBOX_GATEWAY("https://api.sandbox.paypal.com"),
    /**
     * 线上环境
     */
    LIVE_GATEWAY("https://api.paypal.com"),
    /**
     * 获取 Access Token
     */
    GET_TOKEN("/v1/oauth2/token"),
    /**
     * 订单
     */
    CHECKOUT_ORDERS("/v2/checkout/orders");

    /**
     * 类型
     */
    private final String url;

    PayPalApiUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url;
    }
}
