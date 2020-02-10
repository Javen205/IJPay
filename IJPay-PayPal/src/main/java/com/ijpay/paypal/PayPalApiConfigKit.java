package com.ijpay.paypal;

import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class PayPalApiConfigKit {
    private static final ThreadLocal<String> TL = new ThreadLocal<String>();

    private static final Map<String, PayPalApiConfig> CFG_MAP = new ConcurrentHashMap<String, PayPalApiConfig>();
    private static final String DEFAULT_CFG_KEY = "_default_key_";

    /**
     * <p>向缓存中设置 PayPalApiConfig </p>
     * <p>每个 clientId 只需添加一次，相同 clientId 将被覆盖</p>
     *
     * @param payPalApiConfig PayPal 支付配置
     * @return {@link PayPalApiConfig}
     */
    public static PayPalApiConfig putApiConfig(PayPalApiConfig payPalApiConfig) {
        if (CFG_MAP.size() == 0) {
            CFG_MAP.put(DEFAULT_CFG_KEY, payPalApiConfig);
        }
        return CFG_MAP.put(payPalApiConfig.getClientId(), payPalApiConfig);
    }

    /**
     * 向当前线程中设置 {@link PayPalApiConfig}
     *
     * @param payPalApiConfig {@link PayPalApiConfig} PayPal 支付配置
     * @return {@link PayPalApiConfig}
     */
    public static PayPalApiConfig setThreadLocalPayPalApiConfig(PayPalApiConfig payPalApiConfig) {
        if (StrUtil.isNotEmpty(payPalApiConfig.getClientId())) {
            setThreadLocalClientId(payPalApiConfig.getClientId());
        }
        return putApiConfig(payPalApiConfig);
    }

    /**
     * 通过 PayPalApiConfig 移除支付配置
     *
     * @param payPalApiConfig {@link PayPalApiConfig} PayPal 支付配置
     * @return {@link PayPalApiConfig}
     */
    public static PayPalApiConfig removeApiConfig(PayPalApiConfig payPalApiConfig) {
        return removeApiConfig(payPalApiConfig.getClientId());
    }

    /**
     * 通过 clientId 移除支付配置
     *
     * @param clientId 应用编号
     * @return {@link PayPalApiConfig}
     */
    public static PayPalApiConfig removeApiConfig(String clientId) {
        return CFG_MAP.remove(clientId);
    }

    /**
     * 向当前线程中设置 clientId
     *
     * @param clientId 应用编号
     */
    public static void setThreadLocalClientId(String clientId) {
        if (StrUtil.isEmpty(clientId)) {
            clientId = CFG_MAP.get(DEFAULT_CFG_KEY).getClientId();
        }
        TL.set(clientId);
    }

    /**
     * 移除当前线程中的 clientId
     */
    public static void removeThreadLocalClientId() {
        TL.remove();
    }

    /**
     * 获取当前线程中的  clientId
     *
     * @return 应用编号 clientId
     */
    public static String getClientId() {
        String clientId = TL.get();
        if (StrUtil.isEmpty(clientId)) {
            clientId = CFG_MAP.get(DEFAULT_CFG_KEY).getClientId();
        }
        return clientId;
    }

    /**
     * 获取当前线程中的 PayPalApiConfig
     *
     * @return {@link PayPalApiConfig}
     */
    public static PayPalApiConfig getPayPalApiConfig() {
        String clientId = getClientId();
        return getApiConfig(clientId);
    }

    /**
     * 通过 clientId 获取 PayPalApiConfig
     *
     * @param clientId 应用编号
     * @return {@link PayPalApiConfig}
     */
    public static PayPalApiConfig getApiConfig(String clientId) {
        PayPalApiConfig cfg = CFG_MAP.get(clientId);
        if (cfg == null) {
            throw new IllegalStateException("需事先调用 PayPalApiConfigKit.putApiConfig(payPalApiConfig) 将 clientId 对应的 payPalApiConfig 对象存入，才可以使用 PayPalApiConfigKit.getPayPalApiConfig() 的系列方法");
        }
        return cfg;
    }
}