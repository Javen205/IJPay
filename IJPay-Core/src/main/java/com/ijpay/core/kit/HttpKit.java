package com.ijpay.core.kit;

import com.ijpay.core.http.AbstractHttpDelegate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>Http 工具类</p>
 *
 * @author Javen
 */
public class HttpKit {

	private static AbstractHttpDelegate delegate = new DefaultHttpKit();

	public static AbstractHttpDelegate getDelegate() {
		return delegate;
	}

	public static void setDelegate(AbstractHttpDelegate delegate) {
		HttpKit.delegate = delegate;
	}

	/**
	 * 读取字符流内容。本方法不会关闭传入的 {@link Reader}，流生命周期由调用方负责。
	 *
	 * @param reader 字符流
	 * @return 读取到的内容，行之间以 {@code \n} 分隔
	 */
	public static String readData(Reader reader) {
		try {
			StringBuilder result = new StringBuilder();
			BufferedReader br = new BufferedReader(reader);
			for (String line; (line = br.readLine()) != null; ) {
				if (result.length() > 0) {
					result.append("\n");
				}
				result.append(line);
			}
			return result.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将同步通知的参数转化为 Map。
	 * <p>
	 * 参数来源由调用方负责（Servlet 的 {@code getParameterMap()}、Spring 的
	 * {@code @RequestParam Map}、自建路由等），核心不再依赖任何容器类型。
	 *
	 * @param params 参数名到多值数组的 Map
	 * @return 转化后的 Map，多值以英文逗号拼接
	 */
	public static Map<String, String> toMap(Map<String, String[]> params) {
		Map<String, String> result = new HashMap<>();
		if (params == null) {
			return result;
		}
		for (Map.Entry<String, String[]> entry : params.entrySet()) {
			String[] values = entry.getValue();
			if (values == null) {
				result.put(entry.getKey(), "");
				continue;
			}
			StringBuilder valueStr = new StringBuilder();
			for (int i = 0; i < values.length; i++) {
				if (i > 0) {
					valueStr.append(",");
				}
				valueStr.append(values[i]);
			}
			result.put(entry.getKey(), valueStr.toString());
		}
		return result;
	}
}

/**
 * 使用 huTool 实现的 Http 工具类
 *
 * @author Javen
 */
class DefaultHttpKit extends AbstractHttpDelegate {
}
