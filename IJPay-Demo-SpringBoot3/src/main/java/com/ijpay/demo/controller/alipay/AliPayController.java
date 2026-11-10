package com.ijpay.demo.controller.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.demo.entity.AliPayBean;
import com.ijpay.servlet.jakarta.AliPayServletApi;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，支付宝支付示例（Spring Boot 3 + jakarta.servlet）。</p>
 *
 * <p>页面支付方法已迁移至 {@link AliPayServletApi}，不再依赖 javax.servlet。</p>
 *
 * @author Javen
 */
@RestController
@RequestMapping("/aliPay")
public class AliPayController {
	private static final Logger log = LoggerFactory.getLogger(AliPayController.class);

	private final AliPayBean aliPayBean;

	/**
	 * 同步跳转地址
	 */
	private final static String RETURN_URL = "/aliPay/returnUrl";
	/**
	 * 异步通知地址
	 */
	private final static String NOTIFY_URL = "/aliPay/notifyUrl";

	public AliPayController(AliPayBean aliPayBean) {
		this.aliPayBean = aliPayBean;
	}

	@RequestMapping("")
	public String index() {
		return "欢迎使用 IJPay 中的支付宝支付（Spring Boot 3 + jakarta.servlet）-By Javen";
	}

	/**
	 * 手机网站支付
	 */
	@RequestMapping("/wapPay")
	public void wapPay(HttpServletResponse response) throws Exception {
		getApiConfig();
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setBody("我是测试数据-By Javen");
		model.setSubject("Javen Wap支付测试");
		model.setTotalAmount("1");
		model.setPassbackParams("1");
		model.setOutTradeNo(String.valueOf(System.currentTimeMillis()));
		model.setProductCode("QUICK_WAP_PAY");

		AliPayServletApi.wapPay(response, model, aliPayBean.getDomain() + RETURN_URL, aliPayBean.getDomain() + NOTIFY_URL);
	}

	/**
	 * 电脑网站支付
	 */
	@RequestMapping("/pcPay")
	public void pcPay(HttpServletResponse response) throws Exception {
		getApiConfig();
		AlipayTradePagePayModel model = new AlipayTradePagePayModel();
		model.setOutTradeNo(String.valueOf(System.currentTimeMillis()));
		model.setProductCode("FAST_INSTANT_TRADE_PAY");
		model.setTotalAmount("88.88");
		model.setSubject("Javen PC支付测试");
		model.setBody("Javen IJPay PC支付测试");

		AliPayServletApi.tradePage(response, model, aliPayBean.getDomain() + NOTIFY_URL, aliPayBean.getDomain() + RETURN_URL);
	}

	/**
	 * 异步通知验签
	 */
	@RequestMapping("/notifyUrl")
	public String notifyUrl(HttpServletRequest request) {
		try {
			Map<String, String> params = AliPayServletApi.toMap(request);
			boolean verifyResult = AlipaySignature.rsaCheckV1(params, aliPayBean.getPublicKey(), "UTF-8", "RSA2");
			if (verifyResult) {
				// TODO 请在这里加上商户的业务逻辑程序代码，异步通知可能重复，需要做去重处理
				log.info("notifyUrl 验签成功 outTradeNo={}", params.get("out_trade_no"));
				return "success";
			}
			log.warn("notifyUrl 验签失败");
			return "failure";
		} catch (AlipayApiException e) {
			log.error("notifyUrl 验签异常", e);
			return "failure";
		}
	}

	/**
	 * 同步跳转验签
	 */
	@RequestMapping("/returnUrl")
	public String returnUrl(HttpServletRequest request) {
		try {
			Map<String, String> params = AliPayServletApi.toMap(request);
			boolean verifyResult = AlipaySignature.rsaCheckV1(params, aliPayBean.getPublicKey(), "UTF-8", "RSA2");
			if (verifyResult) {
				log.info("returnUrl 验签成功 outTradeNo={}", params.get("out_trade_no"));
				return "success";
			}
			log.warn("returnUrl 验签失败");
			return "failure";
		} catch (AlipayApiException e) {
			log.error("returnUrl 验签异常", e);
			return "failure";
		}
	}

	/**
	 * 构建并使用当前应用的支付宝配置
	 */
	private AliPayApiConfig getApiConfig() throws AlipayApiException {
		AliPayApiConfig config;
		try {
			config = AliPayApiConfigKit.getApiConfig(aliPayBean.getAppId());
		} catch (Exception e) {
			config = AliPayApiConfig.builder()
				.setAppId(aliPayBean.getAppId())
				.setAliPayPublicKey(aliPayBean.getPublicKey())
				.setCharset("UTF-8")
				.setPrivateKey(aliPayBean.getPrivateKey())
				.setServiceUrl(aliPayBean.getServerUrl())
				.setSignType("RSA2")
				.build();
		}
		AliPayApiConfigKit.setThreadLocalAliPayApiConfig(config);
		return config;
	}
}
