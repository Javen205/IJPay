package com.ijpay.wxpay.kit;

import com.ijpay.wxpay.HttpDelegate;

/**
 * Http 工具类
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
