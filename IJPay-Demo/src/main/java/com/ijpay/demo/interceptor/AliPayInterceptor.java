package com.ijpay.demo.interceptor;

import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.demo.controller.alipay.AliPayApiController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AliPayInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler){
        if (HandlerMethod.class.equals(handler.getClass())) {
            HandlerMethod method = (HandlerMethod) handler;
            Object controller = method.getBean();
            if (!(controller instanceof AliPayApiController)) {
                throw new RuntimeException("控制器需要继承 AliPayApiController");
            }
            try {
                AliPayApiConfigKit.setThreadLocalAliPayApiConfig(((AliPayApiController) controller).getApiConfig());
                return true;
            } finally {
                
            }
        }
        return false;
    }
}