package com.ijpay.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.alipay.starter.controller.AbstractAliPayController;
import com.ijpay.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
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
 * <p>支付宝支付</p>
 *
 * @author Javen
 */
@Slf4j
@RestController
@RequestMapping("/aliPay")
public class AliPayController extends AbstractAliPayController {

	// 普通公钥模式
//     private final static String NOTIFY_URL = "/aliPay/notifyUrl";
	/**
	 * 证书模式
	 */
	private final static String NOTIFY_URL = "/aliPay/certNotifyUrl";
//    private final static String RETURN_URL = "/aliPay/returnUrl";
	/**
	 * 证书模式
	 */
	private final static String RETURN_URL = "/aliPay/certReturnUrl";

	@GetMapping("")
	public String index() {
		return "欢迎使用 IJPay 中的支付宝支付 -By Javen  <br/><br>  交流群：723992875、864988890";
	}

	@GetMapping("/switchConfig")
	public AliPayApiConfig switchConfig() {
		String appId = aliPayApiConfig.getAppId();
		AliPayApiConfig apiConfig = AliPayApiConfigKit.getApiConfig(appId);
		log.info("当前支付宝支付配置\n {}", apiConfig);
		if (null != aliPayProperties && CollUtil.isNotEmpty(aliPayProperties.getConfigs())) {
			List<AliPayApiConfig> configs = aliPayProperties.getConfigs().stream()
				.filter(item -> !appId.equals(item.getAppId()))
				.collect(Collectors.toList());

			if (CollUtil.isNotEmpty(configs)) {
				apiConfig = configs.get(RandomUtil.randomInt(0, configs.size()));
			}
			AliPayApiConfigKit.setThreadLocalAliPayApiConfig(apiConfig);
			aliPayApiConfig = apiConfig;
		}
		log.info("切换后支付宝支付配置\n {}", AliPayApiConfigKit.getAliPayApiConfig());
		return aliPayApiConfig;
	}

	@GetMapping("/getConfig")
	public AliPayApiConfig getWxPayApiConfig() {
		log.info("当前支付宝支付配置信息\n {}", AliPayApiConfigKit.getApiConfig(aliPayApiConfig.getAppId()));
		log.info("配置文件中支付宝支付配置信息\n {}", aliPayProperties);
		return aliPayApiConfig;
	}


	@RequestMapping(value = "/wapPay")
	public void wapPay(HttpServletResponse response) {
		AliPayApiConfig config = AliPayApiConfigKit.getAliPayApiConfig();
		String body = "我是测试数据-By Javen";
		String subject = "Javen Wap支付测试";
		String totalAmount = "1";
		String passBackParams = "1";
		String returnUrl = config.getDomain() + RETURN_URL;
		String notifyUrl = config.getDomain() + NOTIFY_URL;

		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setBody(body);
		model.setSubject(subject);
		model.setTotalAmount(totalAmount);
		model.setPassbackParams(passBackParams);
		String outTradeNo = StringUtils.getOutTradeNo();
		System.out.println("wap outTradeNo>" + outTradeNo);
		model.setOutTradeNo(outTradeNo);
		model.setProductCode("QUICK_WAP_PAY");

		try {
			AliPayApi.wapPay(response, model, returnUrl, notifyUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * PC支付
	 */
	@RequestMapping(value = "/pcPay")
	public void pcPay(HttpServletResponse response) {
		try {
			AliPayApiConfig config = AliPayApiConfigKit.getAliPayApiConfig();
			String totalAmount = "88.88";
			String outTradeNo = StringUtils.getOutTradeNo();
			log.info("pc outTradeNo>" + outTradeNo);

			String returnUrl = config.getDomain() + RETURN_URL;
			String notifyUrl = config.getDomain() + NOTIFY_URL;
			AlipayTradePagePayModel model = new AlipayTradePagePayModel();

			model.setOutTradeNo(outTradeNo);
			model.setProductCode("FAST_INSTANT_TRADE_PAY");
			model.setTotalAmount(totalAmount);
			model.setSubject("Javen PC支付测试");
			model.setBody("Javen IJPay PC支付测试");
			model.setPassbackParams("passback_params");
			/**
			 * 花呗分期相关的设置,测试环境不支持花呗分期的测试
			 * hb_fq_num代表花呗分期数，仅支持传入3、6、12，其他期数暂不支持，传入会报错；
			 * hb_fq_seller_percent代表卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种，其他比例暂不支持，传入会报错。
			 */
//            ExtendParams extendParams = new ExtendParams();
//            extendParams.setHbFqNum("3");
//            extendParams.setHbFqSellerPercent("0");
//            model.setExtendParams(extendParams);

			AliPayApi.tradePage(response, model, notifyUrl, returnUrl);
			// https://opensupport.alipay.com/support/helpcenter/192/201602488772?ant_source=antsupport
			// Alipay Easy SDK（新版）目前只支持输出form表单，不支持打印出url链接。
			// AliPayApi.tradePage(response, "GET", model, notifyUrl, returnUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/returnUrl")
	public String returnUrl(HttpServletRequest request) {
		try {
			AliPayApiConfig config = AliPayApiConfigKit.getAliPayApiConfig();

			// 获取支付宝GET过来反馈信息
			Map<String, String> map = AliPayApi.toMap(request);
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}

			boolean verifyResult = AlipaySignature.rsaCheckV1(map, config.getAliPayPublicKey(), "UTF-8",
				"RSA2");

			if (verifyResult) {
				// TODO 请在这里加上商户的业务逻辑程序代码
				System.out.println("return_url 验证成功");

				return "success";
			} else {
				System.out.println("return_url 验证失败");
				// TODO
				return "failure";
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return "failure";
		}
	}

	@RequestMapping(value = "/certReturnUrl")
	public String certReturnUrl(HttpServletRequest request) {
		try {
			AliPayApiConfig config = AliPayApiConfigKit.getAliPayApiConfig();

			// 获取支付宝GET过来反馈信息
			Map<String, String> map = AliPayApi.toMap(request);
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}

			boolean verifyResult = AlipaySignature.rsaCertCheckV1(map, config.getAliPayCertPath(), "UTF-8",
				"RSA2");

			if (verifyResult) {
				// TODO 请在这里加上商户的业务逻辑程序代码
				System.out.println("certReturnUrl 验证成功");

				return "success";
			} else {
				System.out.println("certReturnUrl 验证失败");
				// TODO
				return "failure";
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return "failure";
		}
	}


	@RequestMapping(value = "/notifyUrl")
	public String notifyUrl(HttpServletRequest request) {
		try {
			AliPayApiConfig config = AliPayApiConfigKit.getAliPayApiConfig();

			// 获取支付宝POST过来反馈信息
			Map<String, String> params = AliPayApi.toMap(request);

			for (Map.Entry<String, String> entry : params.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}

			boolean verifyResult = AlipaySignature.rsaCheckV1(params, config.getAliPayPublicKey(), "UTF-8", "RSA2");

			if (verifyResult) {
				// TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
				System.out.println("notify_url 验证成功succcess");
				return "success";
			} else {
				System.out.println("notify_url 验证失败");
				// TODO
				return "failure";
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return "failure";
		}
	}

	@RequestMapping(value = "/certNotifyUrl")
	public String certNotifyUrl(HttpServletRequest request) {
		try {
			AliPayApiConfig config = AliPayApiConfigKit.getAliPayApiConfig();

			// 获取支付宝POST过来反馈信息
			Map<String, String> params = AliPayApi.toMap(request);

			for (Map.Entry<String, String> entry : params.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}

			boolean verifyResult = AlipaySignature.rsaCertCheckV1(params, config.getAliPayCertPath(), "UTF-8", "RSA2");

			if (verifyResult) {
				// TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
				System.out.println("certNotifyUrl 验证成功succcess");
				return "success";
			} else {
				System.out.println("certNotifyUrl 验证失败");
				// TODO
				return "failure";
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			return "failure";
		}
	}
}
