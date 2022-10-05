package com.ijpay.alipay.starter.interceptor;

import com.alipay.api.AlipayApiException;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.alipay.starter.controller.AbstractAliPayApiController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>支付宝支付拦截器</p>
 *
 * @author Javen
 */
public class AliPayInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws AlipayApiException {
		if (HandlerMethod.class.equals(handler.getClass())) {
			HandlerMethod method = (HandlerMethod) handler;
			Object controller = method.getBean();
			if (!(controller instanceof AbstractAliPayApiController)) {
				throw new RuntimeException("控制器需要继承 AbstractAliPayApiController");
			}
			AliPayApiConfig apiConfig = ((AbstractAliPayApiController) controller).getApiConfig();
			if (null == apiConfig || null == apiConfig.getAliPayClient()) {
				throw new RuntimeException("支付宝支付配置信息错误，请检查配置文件，如有疑问请联系 723992875、864988890");
			}
			AliPayApiConfigKit.setThreadLocalAliPayApiConfig(apiConfig);
			return true;
		}
		return false;
	}
}
