package com.ijpay.servlet.jakarta;

import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试用 {@link HttpServletRequest} 桩对象，基于动态代理实现，仅实现被测方法。
 */
final class ServletRequestStub {

	private final Map<String, String> headers = new LinkedHashMap<String, String>();
	private final Map<String, String[]> parameters = new LinkedHashMap<String, String[]>();
	private final List<String> requestedHeaders = new ArrayList<String>();
	private String remoteAddr = "127.0.0.1";
	private String body = "";
	private boolean forbidGetHeaderNames;
	private boolean failOnGetReader;
	private boolean closeReaderWithError;
	private boolean readerClosed;

	ServletRequestStub header(String name, String value) {
		headers.put(name, value);
		return this;
	}

	ServletRequestStub parameter(String name, String... values) {
		parameters.put(name, values);
		return this;
	}

	ServletRequestStub remoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
		return this;
	}

	ServletRequestStub body(String body) {
		this.body = body;
		return this;
	}

	ServletRequestStub forbidGetHeaderNames() {
		this.forbidGetHeaderNames = true;
		return this;
	}

	ServletRequestStub failOnGetReader() {
		this.failOnGetReader = true;
		return this;
	}

	ServletRequestStub closeReaderWithError() {
		this.closeReaderWithError = true;
		return this;
	}

	boolean isReaderClosed() {
		return readerClosed;
	}

	List<String> getRequestedHeaders() {
		return requestedHeaders;
	}

	HttpServletRequest build() {
		return (HttpServletRequest) Proxy.newProxyInstance(
			ServletRequestStub.class.getClassLoader(),
			new Class<?>[]{HttpServletRequest.class},
			new StubHandler());
	}

	private final class StubHandler implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String name = method.getName();
			if ("getHeader".equals(name)) {
				String headerName = (String) args[0];
				requestedHeaders.add(headerName);
				return headerValue(headerName);
			}
			if ("getHeaderNames".equals(name)) {
				if (forbidGetHeaderNames) {
					throw new UnsupportedOperationException("不应遍历全部请求头");
				}
				return Collections.enumeration(headers.keySet());
			}
			if ("getRemoteAddr".equals(name)) {
				return remoteAddr;
			}
			if ("getReader".equals(name)) {
				if (failOnGetReader) {
					throw new IOException("read failed");
				}
				return trackingReader();
			}
			if ("getParameterMap".equals(name)) {
				return parameters;
			}
			if ("toString".equals(name)) {
				return "ServletRequestStub";
			}
			if ("hashCode".equals(name)) {
				return System.identityHashCode(proxy);
			}
			if ("equals".equals(name)) {
				return proxy == args[0];
			}
			return defaultValue(method.getReturnType());
		}
	}

	private String headerValue(String name) {
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(name)) {
				return entry.getValue();
			}
		}
		return null;
	}

	private BufferedReader trackingReader() {
		return new BufferedReader(new StringReader(body)) {
			@Override
			public void close() throws IOException {
				readerClosed = true;
				if (closeReaderWithError) {
					throw new IOException("close failed");
				}
				super.close();
			}
		};
	}

	private static Object defaultValue(Class<?> type) {
		if (!type.isPrimitive()) {
			return null;
		}
		if (type == boolean.class) {
			return false;
		}
		if (type == char.class) {
			return (char) 0;
		}
		if (type == byte.class) {
			return (byte) 0;
		}
		if (type == short.class) {
			return (short) 0;
		}
		if (type == int.class) {
			return 0;
		}
		if (type == long.class) {
			return 0L;
		}
		if (type == float.class) {
			return 0F;
		}
		return 0D;
	}
}
