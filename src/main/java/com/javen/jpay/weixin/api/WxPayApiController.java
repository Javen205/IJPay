package com.javen.jpay.weixin.api;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
/**
 * @Email javen205@126.com
 * @author Javen
 * 2017年5月22日
 */
@Before(WxPayApiInterceptor.class)
public abstract class WxPayApiController extends Controller {
	public abstract WxPayApiConfig getApiConfig();

}
