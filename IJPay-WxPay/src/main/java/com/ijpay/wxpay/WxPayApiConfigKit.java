package com.ijpay.wxpay;

import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.Set;
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
 * <p>微信支付常用配置 Kit</p>
 *
 * @author Javen
 */
public class WxPayApiConfigKit {

    private static final ThreadLocal<String> TL = new ThreadLocal<String>();

    private static final Map<String, WxPayApiConfig> CFG_MAP = new ConcurrentHashMap<String, WxPayApiConfig>();
    private static final String DEFAULT_CFG_KEY = "_default_key_";

    /**
     * 添加微信支付配置，每个appId只需添加一次，相同appId将被覆盖
     *
     * @param wxPayApiConfig 微信支付配置
     * @return {WxPayApiConfig} 微信支付配置
     */
    public static WxPayApiConfig putApiConfig(WxPayApiConfig wxPayApiConfig) {
        if (CFG_MAP.size() == 0) {
            CFG_MAP.put(DEFAULT_CFG_KEY, wxPayApiConfig);
        }
        return CFG_MAP.put(wxPayApiConfig.getAppId(), wxPayApiConfig);
    }

    public static WxPayApiConfig setThreadLocalWxPayApiConfig(WxPayApiConfig wxPayApiConfig) {
        if (StrUtil.isNotEmpty(wxPayApiConfig.getAppId())) {
            setThreadLocalAppId(wxPayApiConfig.getAppId());
        }
        return putApiConfig(wxPayApiConfig);
    }

    public static WxPayApiConfig removeApiConfig(WxPayApiConfig wxPayApiConfig) {
        return removeApiConfig(wxPayApiConfig.getAppId());
    }

    public static WxPayApiConfig removeApiConfig(String appId) {
        return CFG_MAP.remove(appId);
    }

    /**
     * 移除所有支付配置
     *
     * @return 是否移除成功
     */
    public static boolean removeAllApiConfig() {
        Set<String> keySet = CFG_MAP.keySet();
        for (String str : keySet) {
            System.out.println(str);
            CFG_MAP.remove(str);
        }
        removeThreadLocalAppId();
        return true;
    }

    public static void setThreadLocalAppId(String appId) {
        if (StrUtil.isEmpty(appId)) {
            appId = CFG_MAP.get(DEFAULT_CFG_KEY).getAppId();
        }
        TL.set(appId);
    }

    public static void removeThreadLocalAppId() {
        TL.remove();
    }

    public static String getAppId() {
        String appId = TL.get();
        if (StrUtil.isEmpty(appId)) {
            appId = CFG_MAP.get(DEFAULT_CFG_KEY).getAppId();
        }
        return appId;
    }

    public static WxPayApiConfig getWxPayApiConfig() {
        String appId = getAppId();
        return getApiConfig(appId);
    }

    public static WxPayApiConfig getApiConfig(String appId) {
        WxPayApiConfig cfg = CFG_MAP.get(appId);
        if (cfg == null) {
            throw new IllegalStateException("需事先调用 WxPayApiConfigKit.putApiConfig(wxPayApiConfig) 将 appId 对应的 WxPayApiConfig 对象存入，才可以使用 WxPayApiConfigKit.getWxPayApiConfig() 的系列方法");
        }
        return cfg;
    }
}