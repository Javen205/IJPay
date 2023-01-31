package com.ijpay.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * @author Javen
 */
@Order(1)
public class StartupRunner implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void run(String... args) {
		logger.info("startup runner");
		String[] beanNames = applicationContext.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String name : beanNames) {
			logger.info(name);
		}
	}

}
