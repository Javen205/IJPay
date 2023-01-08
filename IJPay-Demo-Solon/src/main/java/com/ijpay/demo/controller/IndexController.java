package com.ijpay.demo.controller;

import org.noear.solon.annotation.*;
import org.noear.solon.core.handle.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * @author Javen
 */
@Controller
public class IndexController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Mapping("")
	public String index() {
		logger.error("欢迎使用 IJPay -By Javen  交流群：723992875、864988890");
		return "index.html";
	}

	@Mapping("/toWxH5Pay")
	public String toWxH5Pay() {
		return "wxh5pay.html";
	}

	@Mapping("/toWxPay")
	public String toWxPay() {
		return "wxpay.html";
	}

	@Mapping("/toWxSuPay")
	public String toWxSuPay() {
		return "wxsubpay.html";
	}

	@Mapping(value = "/payInputMoney")
	public ModelAndView payInputMoney() {
		ModelAndView mav = new ModelAndView("pay_input_money.html");
		mav.put("content", "xxx");
		return mav;
	}

	@Mapping(value = "/payKeyBoard")
	public String payKeyBoard() {
		return "pay_keyboard.html";
	}

	@Mapping(value = "/paySelectMoney")
	public String paySelectMoney() {
		return "pay_select_money.html";
	}

	@Mapping("/success")
	public String success() {
		return "success.html";
	}

	@Get
	@Mapping("/ss/{id}")
	public String pa(@PathVar("id") Integer id) {
		return "id>" + id;
	}

	@Get
	@Mapping("/xx")
	public String param(@Param("id") Integer xx) {
		return "id>" + xx;
	}

	@Get
	@Mapping("/xxx")
	public String param2(@Param(value = "id", required = false, defaultValue = "2") Integer xx) {
		return "id>" + xx;
	}
}
