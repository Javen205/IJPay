package com.ijpay.stripe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>Stripe 支付配置管理，支持单商户/多账户（含 Connect 平台模式）</p>
 *
 * @author Javen
 */
public class StripeApiConfigKit {

	private static final ThreadLocal<String> TL = new ThreadLocal<>();

	private static final Map<String, StripeApiConfig> CFG_MAP = new ConcurrentHashMap<>();

	private static final String DEFAULT_CFG_KEY = "_default_key_";

	/**
	 * 向缓存中设置 {@link StripeApiConfig}
	 * 每个 appId 只需添加一次，相同 appId 将被覆盖；首次添加的配置会作为默认配置
	 *
	 * @param config Stripe 支付配置
	 * @return {@link StripeApiConfig} Stripe 支付配置
	 */
	public static StripeApiConfig putApiConfig(StripeApiConfig config) {
		if (CFG_MAP.isEmpty()) {
			CFG_MAP.put(DEFAULT_CFG_KEY, config);
		}
		String key = resolveKey(config);
		return CFG_MAP.put(key, config);
	}

	/**
	 * 向当前线程中设置 {@link StripeApiConfig}
	 *
	 * @param config {@link StripeApiConfig} Stripe 支付配置
	 * @return {@link StripeApiConfig} Stripe 支付配置
	 */
	public static StripeApiConfig setThreadLocalApiConfig(StripeApiConfig config) {
		setThreadLocalKey(resolveKey(config));
		return putApiConfig(config);
	}

	/**
	 * 通过 {@link StripeApiConfig} 移除支付配置
	 *
	 * @param config {@link StripeApiConfig} Stripe 支付配置
	 * @return {@link StripeApiConfig} 被移除的配置，不存在返回 null
	 */
	public static StripeApiConfig removeApiConfig(StripeApiConfig config) {
		return removeApiConfig(resolveKey(config));
	}

	/**
	 * 通过 appId 移除支付配置
	 *
	 * @param appId 配置标识
	 * @return {@link StripeApiConfig} 被移除的配置，不存在返回 null
	 */
	public static StripeApiConfig removeApiConfig(String appId) {
		if (appId == null || appId.isEmpty()) {
			return CFG_MAP.remove(DEFAULT_CFG_KEY);
		}
		return CFG_MAP.remove(appId);
	}

	/**
	 * 移除所有支付配置并清空当前线程上下文
	 */
	public static void removeAllApiConfig() {
		CFG_MAP.clear();
		removeThreadLocalKey();
	}

	/**
	 * 向当前线程中设置配置标识 appId，为空时使用默认配置的 appId
	 *
	 * @param appId 配置标识
	 */
	public static void setThreadLocalKey(String appId) {
		if (appId == null || appId.isEmpty()) {
			appId = CFG_MAP.get(DEFAULT_CFG_KEY).getAppId();
		}
		TL.set(appId);
	}

	/**
	 * 移除当前线程中的配置标识
	 */
	public static void removeThreadLocalKey() {
		TL.remove();
	}

	/**
	 * 获取当前线程中的配置标识，为空时返回默认配置的 appId
	 *
	 * @return 配置标识
	 */
	public static String getKey() {
		String appId = TL.get();
		if (appId == null || appId.isEmpty()) {
			return CFG_MAP.get(DEFAULT_CFG_KEY).getAppId();
		}
		return appId;
	}

	/**
	 * 获取当前线程中的 {@link StripeApiConfig}，未设置线程上下文时返回默认配置
	 *
	 * @return {@link StripeApiConfig} Stripe 支付配置
	 */
	public static StripeApiConfig getApiConfig() {
		return getApiConfig(getKey());
	}

	/**
	 * 通过 appId 获取 {@link StripeApiConfig}
	 *
	 * @param appId 配置标识
	 * @return {@link StripeApiConfig} Stripe 支付配置
	 */
	public static StripeApiConfig getApiConfig(String appId) {
		StripeApiConfig cfg = CFG_MAP.get(appId);
		if (cfg == null) {
			throw new IllegalStateException("需事先调用 StripeApiConfigKit.putApiConfig(stripeApiConfig) 将 appId 对应的 StripeApiConfig 对象存入，才可以使用 StripeApiConfigKit.getApiConfig() 的系列方法");
		}
		return cfg;
	}

	/**
	 * 解析配置缓存键，优先使用 appId，为空时使用 apiKey
	 *
	 * @param config {@link StripeApiConfig} Stripe 支付配置
	 * @return 缓存键
	 */
	private static String resolveKey(StripeApiConfig config) {
		String appId = config.getAppId();
		if (appId == null || appId.isEmpty()) {
			appId = config.getApiKey();
		}
		return appId;
	}
}
