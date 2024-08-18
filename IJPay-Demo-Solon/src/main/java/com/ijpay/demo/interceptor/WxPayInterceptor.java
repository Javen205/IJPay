package com.ijpay.demo.interceptor;

import com.ijpay.demo.controller.wxpay.AbstractWxPayApiController;
import com.ijpay.wxpay.WxPayApiConfigKit;
import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.handle.Handler;
import org.noear.solon.core.route.PathRule;
import org.noear.solon.core.route.RouterInterceptor;
import org.noear.solon.core.route.RouterInterceptorChain;

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
@Component
public class WxPayInterceptor implements RouterInterceptor {

	@Override
	public PathRule pathPatterns() {
		return new PathRule().include("/wxPay/**");
	}

	@Override
	public void doIntercept(Context ctx, Handler mainHandler, RouterInterceptorChain chain) throws Throwable {
		Object controller = ctx.controller();

		if (controller != null) {
			if (!(controller instanceof AbstractWxPayApiController)) {
				throw new RuntimeException("控制器需要继承 AbstractWxPayApiController");
			}
			WxPayApiConfigKit.setThreadLocalWxPayApiConfig(((AbstractWxPayApiController) controller).getApiConfig());

			//ctx.setHandled(true);
		}

		chain.doIntercept(ctx, mainHandler);
	}
}
