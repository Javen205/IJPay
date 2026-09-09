package com.ijpay.fuiyou;

import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、富友支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>富友支付常用配置 Kit</p>
 *
 * @author Javen
 */
public class FuiYouPayApiConfigKit {

	private static final ThreadLocal<String> TL = new ThreadLocal<String>();

	private static final Map<String, FuiYouPayApiConfig> CFG_MAP = new ConcurrentHashMap<String, FuiYouPayApiConfig>();
	private static final String DEFAULT_CFG_KEY = "_default_key_";

	/**
	 * 添加富友支付配置，每个商户号只需添加一次，相同商户号将被覆盖
	 *
	 * @param fuiYouPayApiConfig 富友支付配置
	 * @return {@link FuiYouPayApiConfig} 富友支付配置
	 */
	public static FuiYouPayApiConfig putApiConfig(FuiYouPayApiConfig fuiYouPayApiConfig) {
		return putApiConfig(fuiYouPayApiConfig.getMchntCd(), fuiYouPayApiConfig);
	}

	public static FuiYouPayApiConfig putApiConfig(String key, FuiYouPayApiConfig fuiYouPayApiConfig) {
		if (CFG_MAP.size() == 0) {
			CFG_MAP.put(DEFAULT_CFG_KEY, fuiYouPayApiConfig);
		}
		return CFG_MAP.put(key, fuiYouPayApiConfig);
	}

	public static FuiYouPayApiConfig setThreadLocalFuiYouPayApiConfig(FuiYouPayApiConfig fuiYouPayApiConfig) {
		return setThreadLocalFuiYouPayApiConfig(fuiYouPayApiConfig.getMchntCd(), fuiYouPayApiConfig);
	}

	public static FuiYouPayApiConfig setThreadLocalFuiYouPayApiConfig(String key, FuiYouPayApiConfig fuiYouPayApiConfig) {
		if (StrUtil.isEmpty(key)) {
			key = fuiYouPayApiConfig.getMchntCd();
		}
		setThreadLocalMchntCd(key);
		return putApiConfig(key, fuiYouPayApiConfig);
	}

	public static FuiYouPayApiConfig removeApiConfig(FuiYouPayApiConfig fuiYouPayApiConfig) {
		return removeApiConfig(fuiYouPayApiConfig.getMchntCd());
	}

	public static FuiYouPayApiConfig removeApiConfig(String mchntCd) {
		return CFG_MAP.remove(mchntCd);
	}

	public static void setThreadLocalMchntCd(String mchntCd) {
		if (StrUtil.isEmpty(mchntCd)) {
			mchntCd = CFG_MAP.get(DEFAULT_CFG_KEY).getMchntCd();
		}
		TL.set(mchntCd);
	}

	public static void removeThreadLocalMchntCd() {
		TL.remove();
	}

	public static String getMchntCd() {
		String mchntCd = TL.get();
		if (StrUtil.isEmpty(mchntCd)) {
			mchntCd = CFG_MAP.get(DEFAULT_CFG_KEY).getMchntCd();
		}
		return mchntCd;
	}

	public static FuiYouPayApiConfig getFuiYouPayApiConfig() {
		String mchntCd = getMchntCd();
		return getApiConfig(mchntCd);
	}

	public static FuiYouPayApiConfig getApiConfig(String mchntCd) {
		FuiYouPayApiConfig cfg = CFG_MAP.get(mchntCd);
		if (cfg == null) {
			throw new IllegalStateException("需事先调用 FuiYouPayApiConfigKit.putApiConfig(fuiYouPayApiConfig) 将商户号对应的 FuiYouPayApiConfig 对象存入，才可以使用 FuiYouPayApiConfigKit.getFuiYouPayApiConfig() 的系列方法");
		}
		return cfg;
	}
}
