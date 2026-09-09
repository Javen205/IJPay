package com.ijpay.fuiyou.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、富友支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>富友支付 API 枚举接口</p>
 *
 * @author Javen
 */
public interface FuiYouApiEnum {

	/**
	 * 获取枚举 URL
	 *
	 * @return {@link String} 接口路径
	 */
	String getUrl();

	/**
	 * 获取详细的描述信息
	 *
	 * @return {@link String} 描述信息
	 */
	String getDesc();

	/**
	 * 根据 url 获取枚举值
	 *
	 * @param enumClass 枚举 class
	 * @param url       url
	 * @param <E>       枚举类
	 * @return 枚举值
	 */
	static <E extends Enum<?> & FuiYouApiEnum> Optional<E> urlOf(Class<E> enumClass, String url) {
		return Arrays.stream(enumClass.getEnumConstants()).filter(e -> e.getUrl().equals(url)).findFirst();
	}
}
