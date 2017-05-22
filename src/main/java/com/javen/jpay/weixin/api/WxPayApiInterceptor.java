package com.javen.jpay.weixin.api;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
/**
 * @Email javen205@126.com
 * @author Javen
 * 2017年5月22日
 */
public class WxPayApiInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		if (controller instanceof WxPayApiController == false)
			throw new RuntimeException("控制器需要继承 WxPayApiController");
		
		try {
			WxPayApiConfigKit.setThreadLocalWxPayApiConfig(((WxPayApiController)controller).getApiConfig());
			inv.invoke();
		}
		finally {
			WxPayApiConfigKit.removeThreadLocalApiConfig();
		}
	}

}
