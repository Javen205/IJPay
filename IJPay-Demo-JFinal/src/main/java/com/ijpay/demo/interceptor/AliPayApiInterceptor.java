package com.ijpay.demo.interceptor;

import com.alipay.api.AlipayApiException;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.demo.controller.alipay.AliPayApiController;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * @author Javen
 */
public class AliPayApiInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        if (!(controller instanceof AliPayApiController)){
            throw new RuntimeException("控制器需要继承 AliPayApiController");
        }
        try {
            AliPayApiConfigKit.setThreadLocalAliPayApiConfig(((AliPayApiController) controller).getApiConfig());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        inv.invoke();
    }
}