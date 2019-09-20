package com.ijpay.demo.controller.wxpay;

import com.ijpay.demo.interceptor.WxPayApiInterceptor;
import com.ijpay.wxpay.WxPayApiConfig;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * @author Javen
 */
@Before(WxPayApiInterceptor.class)
public abstract class WxPayApiController extends Controller {
    public abstract WxPayApiConfig getApiConfig();
}