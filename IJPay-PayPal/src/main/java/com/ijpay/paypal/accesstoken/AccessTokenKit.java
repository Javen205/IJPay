
package com.ijpay.paypal.accesstoken;

import cn.hutool.core.util.StrUtil;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.utils.RetryUtils;
import com.ijpay.paypal.PayPalApi;
import com.ijpay.paypal.PayPalApiConfig;
import com.ijpay.paypal.PayPalApiConfigKit;
import com.ijpay.paypal.cache.DefaultAccessTokenCache;
import com.ijpay.paypal.cache.IAccessTokenCache;

import java.util.concurrent.Callable;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * @author Javen
 */
public class AccessTokenKit {
    private static IAccessTokenCache cache = new DefaultAccessTokenCache();

    public static IAccessTokenCache getCache() {
        return cache;
    }

    public static void setCache(IAccessTokenCache cache) {
        AccessTokenKit.cache = cache;
    }

    /**
     * 获取当前线程中的 AccessToken
     *
     * @return {@link AccessToken}
     */
    public static AccessToken get() {
        return get(PayPalApiConfigKit.getApiConfig().getClientId(), false);
    }

    /**
     * 获取当前线程中的 AccessToken
     *
     * @param forceRefresh 是否强制刷新
     * @return {@link AccessToken}
     */
    public static AccessToken get(boolean forceRefresh) {
        return get(PayPalApiConfigKit.getApiConfig().getClientId(), forceRefresh);
    }

    /**
     * 通过 clientId 来获取  AccessToken
     *
     * @param clientId 应用编号
     * @return {@link AccessToken}
     */
    public static AccessToken get(String clientId) {
        return get(clientId, false);
    }

    /**
     * 通过 clientId 来获取  AccessToken
     *
     * @param clientId     应用编号
     * @param forceRefresh 是否强制刷新
     * @return {@link AccessToken}
     */
    public static AccessToken get(String clientId, boolean forceRefresh) {
        IAccessTokenCache accessTokenCache = AccessTokenKit.getCache();
        // 从缓存中获取 AccessToken
        if (!forceRefresh) {
            String json = accessTokenCache.get(clientId);
            if (StrUtil.isNotEmpty(json)) {
                AccessToken accessToken = new AccessToken(json, 200);
                if (accessToken.isAvailable()) {
                    return accessToken;
                }
            }
        }

        PayPalApiConfig apiConfig = PayPalApiConfigKit.getApiConfig(clientId);

        AccessToken result = RetryUtils.retryOnException(3, new Callable<AccessToken>() {
            @Override
            public AccessToken call() {
                IJPayHttpResponse response = PayPalApi.getToken(apiConfig);
                return new AccessToken(response.getBody(), response.getStatus());
            }
        });

        // 三次请求如果仍然返回了不可用的 AccessToken 仍然 put 进去，便于上层通过 AccessToken 中的属性判断底层的情况
        if (null != result) {
            // 利用 clientId 与 accessToken 建立关联，支持多账户
            accessTokenCache.set(clientId, result.getCacheJson());
        }
        return result;
    }
}
