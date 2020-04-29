
package com.ijpay.paypal;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.PayKit;
import com.ijpay.paypal.accesstoken.AccessToken;
import com.ijpay.paypal.accesstoken.AccessTokenKit;
import com.ijpay.paypal.enums.PayPalApiUrl;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>PayPal Api</p>
 *
 * @author Javen
 */
public class PayPalApi {
    /**
     * 获取接口请求的 URL
     *
     * @param payPalApiUrl {@link PayPalApiUrl} 支付 API 接口枚举
     * @param isSandBox    是否是沙箱环境
     * @return {@link String} 返回完整的接口请求URL
     */
    public static String getReqUrl(PayPalApiUrl payPalApiUrl, boolean isSandBox) {
        return (isSandBox ? PayPalApiUrl.SANDBOX_GATEWAY.getUrl() : PayPalApiUrl.LIVE_GATEWAY.getUrl())
                .concat(payPalApiUrl.getUrl());
    }

    /**
     * 获取 AccessToken
     *
     * @param config {@link PayPalApiConfig} 支付配置
     * @return {@link IJPayHttpResponse} 请求返回的结果
     */
    public static IJPayHttpResponse getToken(PayPalApiConfig config) {
        Map<String, String> headers = new HashMap<>(3);
        headers.put("Accept", ContentType.JSON.toString());
        headers.put("Content-Type", ContentType.FORM_URLENCODED.toString());
        headers.put("Authorization", "Basic ".concat(Base64.encode((config.getClientId().concat(":").concat(config.getClientSecret())).getBytes(StandardCharsets.UTF_8))));
        Map<String, Object> params = new HashMap<>(1);
        params.put("grant_type", "client_credentials");
        return post(getReqUrl(PayPalApiUrl.GET_TOKEN, config.isSandBox()), params, headers);
    }

    /**
     * 创建订单
     *
     * @param config {@link PayPalApiConfig} 支付配置
     * @param data   请求参数
     * @return {@link IJPayHttpResponse} 请求返回的结果
     */
    public static IJPayHttpResponse createOrder(PayPalApiConfig config, String data) {
        AccessToken accessToken = AccessTokenKit.get(config.getClientId());
        return post(getReqUrl(PayPalApiUrl.CHECKOUT_ORDERS, config.isSandBox()), data, getBaseHeaders(accessToken));
    }

    /**
     * 更新订单
     *
     * @param config {@link PayPalApiConfig} 支付配置
     * @param id     订单号
     * @param data   请求参数
     * @return {@link IJPayHttpResponse} 请求返回的结果
     */
    public static IJPayHttpResponse updateOrder(PayPalApiConfig config, String id, String data) {
        AccessToken accessToken = AccessTokenKit.get(config.getClientId());
        String url = getReqUrl(PayPalApiUrl.CHECKOUT_ORDERS, config.isSandBox()).concat("/").concat(id);
        System.out.println(url);
        return patch(url, data, getBaseHeaders(accessToken));
    }

    /**
     * 查询订单
     *
     * @param config  {@link PayPalApiConfig} 支付配置
     * @param orderId 订单号
     * @return {@link IJPayHttpResponse} 请求返回的结果
     */
    public static IJPayHttpResponse queryOrder(PayPalApiConfig config, String orderId) {
        AccessToken accessToken = AccessTokenKit.get(config.getClientId());
        String url = getReqUrl(PayPalApiUrl.CHECKOUT_ORDERS, config.isSandBox()).concat("/").concat(orderId);
        return get(url, null, getBaseHeaders(accessToken));
    }

    /**
     * 确认订单
     *
     * @param config {@link PayPalApiConfig} 支付配置
     * @param id     订单号
     * @param data   请求参数
     * @return {@link IJPayHttpResponse} 请求返回的结果
     */
    public static IJPayHttpResponse captureOrder(PayPalApiConfig config, String id, String data) {
        AccessToken accessToken = AccessTokenKit.get(config.getClientId());
        String url = String.format(getReqUrl(PayPalApiUrl.CAPTURE_ORDER, config.isSandBox()), id);
        return post(url, data, getBaseHeaders(accessToken));
    }

    /**
     * 确认订单
     *
     * @param config    {@link PayPalApiConfig} 支付配置
     * @param captureId 订单号
     * @return {@link IJPayHttpResponse} 请求返回的结果
     */
    public static IJPayHttpResponse captureQuery(PayPalApiConfig config, String captureId) {
        AccessToken accessToken = AccessTokenKit.get(config.getClientId());
        String url = String.format(getReqUrl(PayPalApiUrl.CAPTURE_QUERY, config.isSandBox()), captureId);
        return get(url, null, getBaseHeaders(accessToken));
    }

    /**
     * 退款
     *
     * @param config    {@link PayPalApiConfig} 支付配置
     * @param captureId 订单号
     * @param data      请求参数
     * @return {@link IJPayHttpResponse} 请求返回的结果
     */
    public static IJPayHttpResponse refund(PayPalApiConfig config, String captureId, String data) {
        AccessToken accessToken = AccessTokenKit.get(config.getClientId());
        String url = String.format(getReqUrl(PayPalApiUrl.REFUND, config.isSandBox()), captureId);
        return post(url, data, getBaseHeaders(accessToken));
    }

    /**
     * 退款
     *
     * @param config {@link PayPalApiConfig} 支付配置
     * @param id     订单号
     * @return {@link IJPayHttpResponse} 请求返回的结果
     */
    public static IJPayHttpResponse refundQuery(PayPalApiConfig config, String id) {
        AccessToken accessToken = AccessTokenKit.get(config.getClientId());
        String url = String.format(getReqUrl(PayPalApiUrl.REFUND_QUERY, config.isSandBox()), id);
        return get(url, null, getBaseHeaders(accessToken));
    }

    /**
     * post 请求
     *
     * @param url     请求 url
     * @param params  {@link Map} 请求参数
     * @param headers {@link Map} 请求头
     * @return {@link IJPayHttpResponse} 请求返回的结果
     */
    public static IJPayHttpResponse post(String url, Map<String, Object> params, Map<String, String> headers) {
        return HttpKit.getDelegate().post(url, params, headers);
    }

    /**
     * get 请求
     *
     * @param url     请求 url
     * @param params  {@link Map} 请求参数
     * @param headers {@link Map} 请求头
     * @return {@link IJPayHttpResponse} 请求返回的结果
     */
    public static IJPayHttpResponse get(String url, Map<String, Object> params, Map<String, String> headers) {
        return HttpKit.getDelegate().get(url, params, headers);
    }

    /**
     * post 请求
     *
     * @param url     请求 url
     * @param data    {@link String} 请求参数
     * @param headers {@link Map} 请求头
     * @return {@link IJPayHttpResponse} 请求返回的结果
     */
    public static IJPayHttpResponse post(String url, String data, Map<String, String> headers) {
        return HttpKit.getDelegate().post(url, data, headers);
    }

    /**
     * patch 请求
     *
     * @param url     请求 url
     * @param data    {@link String} 请求参数
     * @param headers {@link Map} 请求头
     * @return {@link IJPayHttpResponse} 请求返回的结果
     */
    public static IJPayHttpResponse patch(String url, String data, Map<String, String> headers) {
        return HttpKit.getDelegate().patch(url, data, headers);
    }

    public static Map<String, String> getBaseHeaders(AccessToken accessToken) {
        if (accessToken == null ||
                StrUtil.isEmpty(accessToken.getTokenType()) ||
                StrUtil.isEmpty(accessToken.getAccessToken())) {
            throw new RuntimeException("accessToken is null");
        }
        Map<String, String> headers = new HashMap<>(3);
        headers.put("Content-Type", ContentType.JSON.toString());
        headers.put("Authorization", accessToken.getTokenType().concat(" ").concat(accessToken.getAccessToken()));
        headers.put("PayPal-Request-Id", PayKit.generateStr());
        return headers;
    }
}
