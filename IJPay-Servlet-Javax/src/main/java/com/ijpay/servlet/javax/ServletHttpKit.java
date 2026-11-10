package com.ijpay.servlet.javax;

import com.ijpay.core.kit.HttpKit;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

/**
 * javax.servlet 适配的 Http 工具。
 * <p>
 * IJPay 核心已容器无关，这里仅把 {@link HttpServletRequest} 转成核心可消费的
 * {@link Reader} 与参数 Map。使用方需要提供 servlet API（容器自带，或用 provided 引入）。
 *
 * @author Javen
 */
public final class ServletHttpKit {

	private ServletHttpKit() {
	}

	/**
	 * 读取请求体。与旧版 {@code HttpKit.readData(HttpServletRequest)} 行为一致：
	 * 读取完成后关闭请求的 Reader。
	 *
	 * @param request {@link HttpServletRequest}
	 * @return 请求体内容
	 */
	public static String readData(HttpServletRequest request) {
		Reader reader;
		try {
			reader = request.getReader();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			return HttpKit.readData(reader);
		} finally {
			try {
				reader.close();
			} catch (IOException ignored) {
				// 请求体已读取完成，关闭失败不影响返回值
			}
		}
	}

	/**
	 * 将请求参数转化为 Map。
	 *
	 * @param request {@link HttpServletRequest}
	 * @return 参数 Map，多值以英文逗号拼接
	 */
	public static Map<String, String> toMap(HttpServletRequest request) {
		return HttpKit.toMap(request.getParameterMap());
	}
}
