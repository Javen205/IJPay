package com.ijpay.alipay;

import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AliPayApiConfigKit {
    private static final ThreadLocal<String> TL = new ThreadLocal<String>();

    private static final Map<String, AliPayApiConfig> CFG_MAP = new ConcurrentHashMap<String, AliPayApiConfig>();
    private static final String DEFAULT_CFG_KEY = "_default_key_";

    /**
     * 向缓存中设置 AliPayApiConfig <br/>
     * 每个 appId 只需添加一次，相同 appId 将被覆盖 <br/>
     *
     * @param aliPayApiConfig 支付宝支付配置
     * @return {@link AliPayApiConfig}
     */
    public static AliPayApiConfig putApiConfig(AliPayApiConfig aliPayApiConfig) {
        if (CFG_MAP.size() == 0) {
            CFG_MAP.put(DEFAULT_CFG_KEY, aliPayApiConfig);
        }
        return CFG_MAP.put(aliPayApiConfig.getAppId(), aliPayApiConfig);
    }

    /**
     * 向当前线程中设置 {@link AliPayApiConfig}
     *
     * @param aliPayApiConfig {@link AliPayApiConfig} 支付宝配置对象
     * @return {@link AliPayApiConfig}
     */
    public static AliPayApiConfig setThreadLocalAliPayApiConfig(AliPayApiConfig aliPayApiConfig) {
        return putApiConfig(aliPayApiConfig);
    }

    /**
     * 通过 AliPayApiConfig 移除支付配置
     *
     * @param aliPayApiConfig {@link AliPayApiConfig} 支付宝配置对象
     * @return {@link AliPayApiConfig}
     */
    public static AliPayApiConfig removeApiConfig(AliPayApiConfig aliPayApiConfig) {
        return removeApiConfig(aliPayApiConfig.getAppId());
    }

    /**
     * 通过 appId 移除支付配置
     *
     * @param appId 支付宝应用编号
     * @return {@link AliPayApiConfig}
     */
    public static AliPayApiConfig removeApiConfig(String appId) {
        return CFG_MAP.remove(appId);
    }

    /**
     * 向当前线程中设置 appId
     *
     * @param appId 支付宝应用编号
     */
    public static void setThreadLocalAppId(String appId) {
        if (StrUtil.isEmpty(appId)) {
            appId = CFG_MAP.get(DEFAULT_CFG_KEY).getAppId();
        }
        TL.set(appId);
    }

    /**
     * 移除当前线程中的 appId
     */
    public static void removeThreadLocalAppId() {
        TL.remove();
    }

    /**
     * 获取当前线程中的  appId
     *
     * @return 支付宝应用编号 appId
     */
    public static String getAppId() {
        String appId = TL.get();
        if (StrUtil.isEmpty(appId)) {
            appId = CFG_MAP.get(DEFAULT_CFG_KEY).getAppId();
        }
        return appId;
    }

    /**
     * 获取当前线程中的 AliPayApiConfig
     *
     * @return {@link AliPayApiConfig}
     */
    public static AliPayApiConfig getAliPayApiConfig() {
        String appId = getAppId();
        return getApiConfig(appId);
    }

    /**
     * 通过 appId 获取 AliPayApiConfig
     *
     * @param appId 支付宝应用编号
     * @return {@link AliPayApiConfig}
     */
    public static AliPayApiConfig getApiConfig(String appId) {
        AliPayApiConfig cfg = CFG_MAP.get(appId);
        if (cfg == null)
            throw new IllegalStateException(
                    "需事先调用 AliPayApiConfigKit.putApiConfig(aliPayApiConfig) 将 appId对应的 aliPayApiConfig 对象存入，才可以使用 AliPayApiConfigKit.getAliPayApiConfig() 的系列方法");
        return cfg;
    }
}