package com.javen.jpay.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;

public class IndexController extends Controller {
	static Log log=Log.getLog(IndexController.class);

	public void index(){
		log.info("欢迎使用JPay服务端IJPay - by Javen");
		renderText("欢迎使用JPay服务端IJPay  - by Javen");
	}

	// 跳转到授权页面
	public void toOauth() {
		String state = getPara("state");
		String calbackUrl = PropKit.get("domain") + "/oauth";
		System.out.println("calbackUrl>"+calbackUrl);
		String url = SnsAccessTokenApi.getAuthorizeURL(PropKit.get("appId"), calbackUrl, state, true);
		redirect(url);
	}
	/**
	 * 服务商模式下微信支付
	 */
	public void toWxSubPay() {
		render("wxsubpay.jsp");
	}
	public void toWxPay() {
		render("wxpay.jsp");
	}
	
	
}
