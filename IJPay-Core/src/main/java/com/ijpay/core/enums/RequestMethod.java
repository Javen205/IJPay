package com.ijpay.core.enums;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p>HTTP 请求的方法</p>
 *
 * @author Javen
 */
public enum RequestMethod {
    /**
     * post 请求
     */
    POST("POST"),
    /**
     * get 请求
     */
    GET("GET"),
    /**
     * put 请求
     */
    PUT("PUT"),
    /**
     * delete 请求
     */
    DELETE("DELETE"),
    /**
     * options 请求
     */
    OPTIONS("OPTIONS"),
    /**
     * head 请求
     */
    HEAD("HEAD"),
    /**
     * trace 请求
     */
    TRACE("TRACE"),
    /**
     * connect 请求
     */
    CONNECT("CONNECT");

    private final String method;

    RequestMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return this.method;
    }
}
