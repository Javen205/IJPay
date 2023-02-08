package com.ijpay.wxpay.starter.interceptor;

import cn.hutool.core.util.StrUtil;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.starter.controller.AbstractWxPayApiController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信支付拦截器</p>
 *
 * @author Javen
 */
public class WxPayInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) {
		if (HandlerMethod.class.equals(handler.getClass())) {
			HandlerMethod method = (HandlerMethod) handler;
			Object controller = method.getBean();
			if (!(controller instanceof AbstractWxPayApiController)) {
				throw new RuntimeException("控制器需要继承 AbstractWxPayApiController");
			}
			WxPayApiConfig apiConfig = ((AbstractWxPayApiController) controller).getApiConfig();
			if (null == apiConfig || StrUtil.isEmpty(apiConfig.getMchId())) {
				throw new RuntimeException("微信支付配置信息错误，请检查配置文件，如有疑问请联系 723992875、864988890");
			}
			WxPayApiConfigKit.setThreadLocalWxPayApiConfig(apiConfig);
			return true;
		}
		return false;
	}
}
