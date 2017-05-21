package com.javen.jpay.alipay;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
/**
 * @Email javen205@126.com
 * @author Javen
 * 2017年5月20日
 */
public class AliPayApiInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller controller = inv.getController();
		if (controller instanceof AliPayApiController == false)
			throw new RuntimeException("控制器需要继承 AliPayApiController");
		
		try {
			AliPayApiConfigKit.setThreadLocalAliPayApiConfig(((AliPayApiController)controller).getApiConfig());
			inv.invoke();
		}
		finally {
			AliPayApiConfigKit.removeThreadLocalApiConfig();
		}
	}

}
