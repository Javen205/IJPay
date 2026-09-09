package com.ijpay.wxpay;

import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信支付 APIv3 配置管理</p>
 *
 * @author Javen
 */
public class WxPayV3ApiConfigKit {
	private static final ThreadLocal<String> TL = new ThreadLocal<>();

	private static final Map<String, WxPayV3ApiConfig> CFG_MAP = new ConcurrentHashMap<>();
	private static final String DEFAULT_CFG_KEY = "_default_key_";

	/**
	 * <p>向缓存中设置 {@link WxPayV3ApiConfig} </p>
	 * <p>每个 mchId 只需添加一次，相同 mchId 将被覆盖</p>
	 *
	 * @param wxPayV3ApiConfig 微信支付配置
	 * @return {@link WxPayV3ApiConfig}
	 */
	public static WxPayV3ApiConfig putApiConfig(WxPayV3ApiConfig wxPayV3ApiConfig) {
		if (CFG_MAP.isEmpty()) {
			CFG_MAP.put(DEFAULT_CFG_KEY, wxPayV3ApiConfig);
		}
		CFG_MAP.put(wxPayV3ApiConfig.getMchId(), wxPayV3ApiConfig);
		return wxPayV3ApiConfig;
	}

	/**
	 * 向当前线程中设置 {@link WxPayV3ApiConfig}
	 *
	 * @param wxPayV3ApiConfig {@link WxPayV3ApiConfig} 微信支付配置对象
	 * @return {@link WxPayV3ApiConfig}
	 */
	public static WxPayV3ApiConfig setThreadLocalWxPayV3ApiConfig(WxPayV3ApiConfig wxPayV3ApiConfig) {
		if (StrUtil.isNotEmpty(wxPayV3ApiConfig.getMchId())) {
			setThreadLocalMchId(wxPayV3ApiConfig.getMchId());
		}
		return putApiConfig(wxPayV3ApiConfig);
	}

	/**
	 * 通过 {@link WxPayV3ApiConfig} 移除支付配置
	 *
	 * @param wxPayV3ApiConfig {@link WxPayV3ApiConfig} 微信支付配置对象
	 * @return {@link WxPayV3ApiConfig}
	 */
	public static WxPayV3ApiConfig removeApiConfig(WxPayV3ApiConfig wxPayV3ApiConfig) {
		return removeApiConfig(wxPayV3ApiConfig.getMchId());
	}

	/**
	 * 通过 mchId 移除支付配置
	 *
	 * @param mchId 商户号
	 * @return {@link WxPayV3ApiConfig}
	 */
	public static WxPayV3ApiConfig removeApiConfig(String mchId) {
		return CFG_MAP.remove(mchId);
	}

	/**
	 * 向当前线程中设置 mchId
	 *
	 * @param mchId 商户号
	 */
	public static void setThreadLocalMchId(String mchId) {
		if (StrUtil.isEmpty(mchId)) {
			mchId = CFG_MAP.get(DEFAULT_CFG_KEY).getMchId();
		}
		TL.set(mchId);
	}

	/**
	 * 移除当前线程中的 mchId
	 */
	public static void removeThreadLocalMchId() {
		TL.remove();
	}

	/**
	 * 获取当前线程中的 mchId
	 *
	 * @return 商户号
	 */
	public static String getMchId() {
		String mchId = TL.get();
		if (StrUtil.isEmpty(mchId)) {
			mchId = CFG_MAP.get(DEFAULT_CFG_KEY).getMchId();
		}
		return mchId;
	}

	/**
	 * 获取当前线程中的 {@link WxPayV3ApiConfig}
	 *
	 * @return {@link WxPayV3ApiConfig}
	 */
	public static WxPayV3ApiConfig getWxPayV3ApiConfig() {
		String mchId = getMchId();
		return getApiConfig(mchId);
	}

	/**
	 * 通过 mchId 获取 {@link WxPayV3ApiConfig}
	 *
	 * @param mchId 商户号
	 * @return {@link WxPayV3ApiConfig}
	 */
	public static WxPayV3ApiConfig getApiConfig(String mchId) {
		WxPayV3ApiConfig cfg = CFG_MAP.get(mchId);
		if (cfg == null) {
			throw new IllegalStateException("需事先调用 WxPayV3ApiConfigKit.putApiConfig(wxPayV3ApiConfig) 将 mchId 对应的 wxPayV3ApiConfig 对象存入，才可以使用 WxPayV3ApiConfigKit.getWxPayV3ApiConfig() 的系列方法");
		}
		return cfg;
	}
}
