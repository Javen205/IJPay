package com.ijpay.demo.config;

import com.ijpay.demo.interceptor.AliPayInterceptor;
import com.ijpay.demo.interceptor.WxPayInterceptor;
import org.noear.solon.Solon;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;

@Configuration
public class IJPayConfigurer {
	@Bean
	public void addInterceptors() {
		Solon.app().before("/aliPay/**", new AliPayInterceptor());
		Solon.app().before("/wxPay/**", new WxPayInterceptor());
	}
}
