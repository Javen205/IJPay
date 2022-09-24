package com.ijpay.core.kit;

import com.ijpay.core.http.AbstractHttpDelegate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
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

	public static String readData(HttpServletRequest request) {
		BufferedReader br = null;
		try {
			StringBuilder result = new StringBuilder();
			br = request.getReader();
			for (String line; (line = br.readLine()) != null; ) {
				if (result.length() > 0) {
					result.append("\n");
				}
				result.append(line);
			}
			return result.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 将同步通知的参数转化为Map
	 *
	 * @param request {@link HttpServletRequest}
	 * @return 转化后的 Map
	 */
	public static Map<String, String> toMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap<>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (String name : requestParams.keySet()) {
			String[] values = requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		return params;
	}
}

/**
 * 使用 huTool 实现的 Http 工具类
 *
 * @author Javen
 */
class DefaultHttpKit extends AbstractHttpDelegate {
}
