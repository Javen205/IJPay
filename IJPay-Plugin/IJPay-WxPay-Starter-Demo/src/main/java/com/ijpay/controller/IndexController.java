package com.ijpay.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信支付 SpringBoot3 示例</p>
 *
 * @author Javen
 */
@Slf4j
@RestController
@RequestMapping("/")
public class IndexController {
	@GetMapping("")
	public String index() {
		return "欢迎使用 IJPay 中的支付宝支付 -By Javen  <br/><br>  交流群：723992875、864988890";
	}
}
