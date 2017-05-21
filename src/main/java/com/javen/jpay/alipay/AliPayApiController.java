package com.javen.jpay.alipay;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
/**
 * @Email javen205@126.com
 * @author Javen
 * 2017年5月20日
 */
@Before(AliPayApiInterceptor.class)
public abstract class AliPayApiController extends Controller {
	public abstract AliPayApiConfig getApiConfig();

}
