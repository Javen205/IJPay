
package com.ijpay.unionpay;

import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>云闪付常用配置 Kit</p>
 *
 * @author Javen
 */
public class UnionPayApiConfigKit {
    private static final ThreadLocal<String> TL = new ThreadLocal<String>();

    private static final Map<String, UnionPayApiConfig> CFG_MAP = new ConcurrentHashMap<String, UnionPayApiConfig>();
    private static final String DEFAULT_CFG_KEY = "_default_key_";

    /**
     * 添加云闪付配置，每个 mchId 只需添加一次，相同 mchId 将被覆盖
     *
     * @param UnionPayApiConfig 云闪付配置
     * @return {@link UnionPayApiConfig} 云闪付配置
     */
    public static UnionPayApiConfig putApiConfig(UnionPayApiConfig UnionPayApiConfig) {
        if (CFG_MAP.size() == 0) {
            CFG_MAP.put(DEFAULT_CFG_KEY, UnionPayApiConfig);
        }
        return CFG_MAP.put(UnionPayApiConfig.getMchId(), UnionPayApiConfig);
    }

    public static UnionPayApiConfig setThreadLocalApiConfig(UnionPayApiConfig UnionPayApiConfig) {
        if (StrUtil.isNotEmpty(UnionPayApiConfig.getMchId())) {
            setThreadLocalMchId(UnionPayApiConfig.getMchId());
        }
        return putApiConfig(UnionPayApiConfig);
    }

    public static UnionPayApiConfig removeApiConfig(UnionPayApiConfig UnionPayApiConfig) {
        return removeApiConfig(UnionPayApiConfig.getMchId());
    }

    public static UnionPayApiConfig removeApiConfig(String mchId) {
        return CFG_MAP.remove(mchId);
    }

    public static void setThreadLocalMchId(String mchId) {
        if (StrUtil.isEmpty(mchId)) {
            mchId = CFG_MAP.get(DEFAULT_CFG_KEY).getMchId();
        }
        TL.set(mchId);
    }

    public static void removeThreadLocalMchId() {
        TL.remove();
    }

    public static String getMchId() {
        String appId = TL.get();
        if (StrUtil.isEmpty(appId)) {
            appId = CFG_MAP.get(DEFAULT_CFG_KEY).getMchId();
        }
        return appId;
    }

    public static UnionPayApiConfig getApiConfig() {
        String appId = getMchId();
        return getApiConfig(appId);
    }

    public static UnionPayApiConfig getApiConfig(String appId) {
        UnionPayApiConfig cfg = CFG_MAP.get(appId);
        if (cfg == null) {
            throw new IllegalStateException("需事先调用 UnionPayApiConfigKit.putApiConfig(UnionPayApiConfig) 将 mchId 对应的 UnionPayApiConfig 对象存入，才可以使用 UnionPayApiConfigKit.getUnionPayApiConfig() 的系列方法");
        }
        return cfg;
    }
}
