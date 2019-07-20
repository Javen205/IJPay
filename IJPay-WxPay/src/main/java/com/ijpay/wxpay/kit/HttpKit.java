package com.ijpay.wxpay.kit;

import com.ijpay.wxpay.HttpDelegate;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>Http 工具类</p>
 *
 * @author Javen
 */
public class HttpKit {

    private static HttpDelegate delegate = new DefaultHttpKit();

    public static HttpDelegate getDelegate() {
        return delegate;
    }

    public static void setDelegate(HttpDelegate delegate) {
        HttpKit.delegate = delegate;
    }
}

/**
 * 使用 hutool 实现的 Http 工具类
 *
 * @author Javen
 */
class DefaultHttpKit extends HttpDelegate {
}
