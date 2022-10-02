package com.ijpay.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.ijpay.core.model.CertificateModel;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.starter.controller.AbstractWxPayController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
@Slf4j
@RestController
@RequestMapping("/wxPay")
public class WxPayController extends AbstractWxPayController {

	@GetMapping("/switchConfig")
	public WxPayApiConfig switchConfig() {
		String appId = wxPayApiConfig.getAppId();
		WxPayApiConfig apiConfig = WxPayApiConfigKit.getApiConfig(appId);
		log.info("当前微信支付配置\n {}", apiConfig);
		if (null != wxPayProperties && CollUtil.isNotEmpty(wxPayProperties.getConfigs())) {
			List<WxPayApiConfig> configs = wxPayProperties.getConfigs().stream()
				.filter(item -> !appId.equals(item.getAppId()))
				.collect(Collectors.toList());

			if (CollUtil.isNotEmpty(configs)) {
				apiConfig = configs.get(RandomUtil.randomInt(0, configs.size()));
			}
			WxPayApiConfigKit.setThreadLocalWxPayApiConfig(apiConfig);
			wxPayApiConfig = apiConfig;
		}
		log.info("切换后微信支付配置\n {}", WxPayApiConfigKit.getWxPayApiConfig());
		return wxPayApiConfig;
	}

	@GetMapping("/getConfig")
	public WxPayApiConfig getWxPayApiConfig() {
		log.info("微信支付配置\n {}", WxPayApiConfigKit.getApiConfig(wxPayApiConfig.getAppId()));
		log.info("yml支付配置\n {}", wxPayProperties);
		return wxPayApiConfig;
	}

	/**
	 * 获取并保存证书,默认取有效期内第一个
	 *
	 * @return 自动更新证书
	 * @throws Exception 异常信息
	 */
	@GetMapping("/autoUpdate")
	public boolean autoUpdate() throws Exception {
		return autoUpdateOrGetCertificate(null);
	}

	/**
	 * 获取平台证书序列号
	 *
	 * @return 平台证书序列号
	 */
	@GetMapping("/getPlatSerialNumber")
	public String getPlatSerialNumber() {
		CertificateModel info = getPlatformCertificateInfo();
		if (null == info) {
			return null;
		}
		return info.getSerialNumber();
	}
}
