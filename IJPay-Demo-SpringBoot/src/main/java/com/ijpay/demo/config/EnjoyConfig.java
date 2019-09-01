package com.ijpay.demo.config;

import com.jfinal.template.ext.spring.JFinalViewResolver;
import com.jfinal.template.source.ClassPathSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class EnjoyConfig {
	@Bean(name = "jfinalViewResolver")
	public JFinalViewResolver getJFinalViewResolver() {
		JFinalViewResolver jfr = new JFinalViewResolver();
		// setDevMode 配置放在最前面
		jfr.setDevMode(true);

		// 使用 ClassPathSourceFactory 从 class path 与 jar 包中加载模板文件
		jfr.setSourceFactory(new ClassPathSourceFactory());

		// 在使用 ClassPathSourceFactory 时要使用 setBaseTemplatePath
		// 代替 jfr.setPrefix("/view/")
		JFinalViewResolver.engine.setBaseTemplatePath("/view/");

		//jfr.setSuffix(".html");
		jfr.setContentType("text/html;charset=UTF-8");
		jfr.setOrder(0);
		return jfr;
	}
}
