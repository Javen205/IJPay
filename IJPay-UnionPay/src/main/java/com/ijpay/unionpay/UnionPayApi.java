/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>云闪付接口</p>
 *
 * @author Javen
 */
package com.ijpay.unionpay;

import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;

import java.util.Map;

public class UnionPayApi {
    public static String authUrl = "https://qr.95516.com/qrcGtwWeb-web/api/userAuth?version=1.0.0&redirectUrl=%s";

    public static String execution(String url, Map<String, String> params) {
        return HttpKit.getDelegate().post(url, WxPayKit.toXml(params));
    }

    /**
     * 获取用户授权 API
     *
     * @param url 回调地址，可以自定义参数 https://pay.javen.com/callback?sdk=ijpay
     * @return 银联重定向 Url
     */
    public static String buildAuthUrl(String url) {
        return String.format(authUrl, url);
    }

}
