package com.ijpay.servlet.javax;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 测试用 {@link HttpServletResponse} 桩对象，基于动态代理实现，记录状态码、Content-Type、跳转地址与响应体。
 */
final class ServletResponseStub {

	private final StringWriter body = new StringWriter();
	private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private final PrintWriter writer = new PrintWriter(body) {
		@Override
		public void close() {
			writerClosed = true;
			super.close();
		}
	};
	private String contentType;
	private String redirect;
	private int status;
	private boolean writerClosed;
	private boolean flushed;

	HttpServletResponse build() {
		return (HttpServletResponse) Proxy.newProxyInstance(
			ServletResponseStub.class.getClassLoader(),
			new Class<?>[]{HttpServletResponse.class},
			new StubHandler());
	}

	String getContentType() {
		return contentType;
	}

	String getBody() {
		return body.toString();
	}

	byte[] getOutputStreamBytes() {
		return outputStream.toByteArray();
	}

	String getRedirect() {
		return redirect;
	}

	int getStatus() {
		return status;
	}

	boolean isWriterClosed() {
		return writerClosed;
	}

	boolean isFlushed() {
		return flushed;
	}

	private final class StubHandler implements InvocationHandler {

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String name = method.getName();
			if ("setContentType".equals(name)) {
				contentType = (String) args[0];
				return null;
			}
			if ("getWriter".equals(name)) {
				return writer;
			}
			if ("getOutputStream".equals(name)) {
				return new ServletOutputStream() {
					@Override
					public boolean isReady() {
						return true;
					}

					@Override
					public void setWriteListener(WriteListener writeListener) {
					}

					@Override
					public void write(int b) {
						outputStream.write(b);
					}
				};
			}
			if ("sendRedirect".equals(name)) {
				redirect = (String) args[0];
				return null;
			}
			if ("setStatus".equals(name)) {
				status = (Integer) args[0];
				return null;
			}
			if ("flushBuffer".equals(name)) {
				flushed = true;
				return null;
			}
			if ("toString".equals(name)) {
				return "ServletResponseStub";
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
