package com.ijpay.demo.controller.alipay;

import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.demo.interceptor.AliPayApiInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * @author Javen
 */
@Before(AliPayApiInterceptor.class)
public abstract class AliPayApiController extends Controller {
	public abstract AliPayApiConfig getApiConfig();
}