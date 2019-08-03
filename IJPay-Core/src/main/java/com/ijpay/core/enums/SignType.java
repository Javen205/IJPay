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
 * <p>签名方式</p>
 *
 * @author Javen
 */
public enum SignType {
    /**
     * HMAC-SHA256 加密
     */
    HMACSHA256("HMAC-SHA256"),
    /**
     *  MD5 加密
     */
    MD5("MD5");

    SignType(String type) {
        this.type = type;
    }

    private final String type;

    public String getType() {
        return type;
    }
}
