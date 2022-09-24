package com.ijpay.demo.interceptor;

import com.ijpay.demo.controller.wxpay.WxPayApiController;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * @author Javen
 */
public class WxPayApiInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		if (!(controller instanceof WxPayApiController)) {
			throw new RuntimeException("控制器需要继承 WxPayApiController");
		}
		WxPayApiConfigKit.setThreadLocalWxPayApiConfig(((WxPayApiController) controller).getApiConfig());
		inv.invoke();
	}

}
