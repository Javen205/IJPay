package com.jpay.alipay;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jpay.ext.kit.StrKit;

/**
 * @author Javen
 * 2017年5月20日
 */
public class AliPayApiConfigKit {
	private static final ThreadLocal<String> TL = new ThreadLocal<String>();
	
	private static final Map<String, AliPayApiConfig> CFG_MAP = new ConcurrentHashMap<String, AliPayApiConfig>();
	private static final String DEFAULT_CFG_KEY = "_default_ijpay_key_";
	
	/**
	 * 添加支付宝支付配置，每个appId只需添加一次，相同appId将被覆盖
	 * @param aliPayApiConfig 支付宝支付配置
	 * @return {AliPayApiConfig} 支付宝支付配置
	 */
	public static AliPayApiConfig putApiConfig(AliPayApiConfig aliPayApiConfig) {
        if (CFG_MAP.size() == 0) {
        	CFG_MAP.put(DEFAULT_CFG_KEY, aliPayApiConfig);
        }
        return CFG_MAP.put(aliPayApiConfig.getAppId(), aliPayApiConfig);
    }
	
	public static AliPayApiConfig setThreadLocalAliPayApiConfig(AliPayApiConfig aliPayApiConfig) {
        return putApiConfig(aliPayApiConfig);
    }

    public static AliPayApiConfig removeApiConfig(AliPayApiConfig apiConfig) {
        return removeApiConfig(apiConfig.getAppId());
    }

    public static AliPayApiConfig removeApiConfig(String appId) {
        return CFG_MAP.remove(appId);
    }

    public static void setThreadLocalAppId(String appId) {
        if (StrKit.isBlank(appId)) {
            appId = CFG_MAP.get(DEFAULT_CFG_KEY).getAppId();
        }
        TL.set(appId);
    }

    public static void removeThreadLocalAppId() {
        TL.remove();
    }
	
    public static String getAppId() {
        String appId = TL.get();
        if (StrKit.isBlank(appId)) {
            appId = CFG_MAP.get(DEFAULT_CFG_KEY).getAppId();
        }
        return appId;
    }

    public static AliPayApiConfig getAliPayApiConfig() {
        String appId = getAppId();
        return getApiConfig(appId);
    }

    public static AliPayApiConfig getApiConfig(String appId) {
    	AliPayApiConfig cfg = CFG_MAP.get(appId);
        if (cfg == null)
            throw new IllegalStateException("需事先调用 AliPayApiConfigKit.putApiConfig(aliPayApiConfig) 将 appId对应的 aliPayApiConfig 对象存入，" +
                    "才可以使用 AliPayApiConfigKit.getAliPayApiConfig() 的系列方法");
        return cfg;
    }
}
