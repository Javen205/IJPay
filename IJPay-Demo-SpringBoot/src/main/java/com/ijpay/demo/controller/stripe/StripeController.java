package com.ijpay.demo.controller.stripe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.demo.entity.StripeBean;
import com.ijpay.stripe.StripeApi;
import com.ijpay.stripe.StripeApiConfig;
import com.ijpay.stripe.StripeApiConfigKit;
import com.ijpay.stripe.StripeCheckoutApi;
import com.ijpay.stripe.StripePayApi;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import com.ijpay.servlet.javax.ServletHttpKit;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>Stripe 支付示例
 * <ul>
 *     <li>方式一 PaymentIntent + Stripe.js Elements：前端卡片直接收款（对应演示页 /stripe.html）</li>
 *     <li>方式二 Checkout 托管收银台：后端创建 Session，跳转 Stripe 托管页面完成收款</li>
 * </ul>
 * 演示页：{@code http://localhost/stripe.html}（静态页由浏览器直接访问）
 * Webhook 本地调试：{@code stripe listen --forward-to localhost:80/stripe/webhook}</p>
 *
 * @author Javen
 */
@Controller
@RequestMapping("/stripe")
public class StripeController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	StripeBean stripeBean;

	@RequestMapping("")
	@ResponseBody
	public String index() {
		return "欢迎使用 IJPay 中的 Stripe 支付 -By Javen  <br/><br>  交流群：723992875、864988890";
	}

	@RequestMapping("/test")
	@ResponseBody
	public StripeBean test() {
		return stripeBean;
	}

	/**
	 * 组装当前请求使用的 StripeApiConfig 并放入配置中心
	 *
	 * @return {@link StripeApiConfig}
	 */
	private StripeApiConfig getConfig() {
		StripeApiConfig config = new StripeApiConfig();
		config.setAppId(stripeBean.getAppId());
		config.setApiKey(stripeBean.getApiKey());
		config.setWebhookSecret(stripeBean.getWebhookSecret());
		config.setSandBox(Boolean.TRUE.equals(stripeBean.getSandBox()));
		StripeApiConfigKit.putApiConfig(config);
		return config;
	}

	/**
	 * 前端页面所需的公开配置（publishableKey 可在浏览器安全使用）
	 */
	@RequestMapping(value = "/config", method = RequestMethod.GET)
	@ResponseBody
	public String config() {
		Map<String, Object> map = new HashMap<>();
		map.put("publishableKey", stripeBean.getPublishableKey());
		map.put("sandBox", stripeBean.getSandBox());
		return JSON.toJSONString(map);
	}

	/**
	 * 方式一：创建 PaymentIntent（金额单位：分），返回 client_secret 供前端 Stripe.js 确认支付
	 *
	 * @param unitAmount 支付金额（分）
	 */
	@RequestMapping(value = "/createPaymentIntent", method = RequestMethod.POST)
	@ResponseBody
	public String createPaymentIntent(@RequestParam(value = "unitAmount", defaultValue = "99") Long unitAmount) {
		JSONObject result = new JSONObject();
		try {
			getConfig();

			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
				.setAmount(unitAmount)
				.setCurrency("usd")
				// 启用自动支付方式后，将自动启用该账户已开启的全部支付方式（card、alipay、wechat_pay 等）
				.setAutomaticPaymentMethods(PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
					.setEnabled(true)
					.build())
				.putMetadata("product", "IJPay Stripe Demo")
				.build();

			PaymentIntent paymentIntent = StripePayApi.createPaymentIntent(params);
			log.info("createPaymentIntent response:{}", paymentIntent);
			result.put("code", 0);
			result.put("id", paymentIntent.getId());
			result.put("clientSecret", paymentIntent.getClientSecret());
		} catch (Exception e) {
			log.error("createPaymentIntent error", e);
			result.put("code", -1);
			result.put("msg", e.getMessage());
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 方式二：创建 Checkout Session（托管收银台），返回托管页面地址
	 *
	 * @param unitAmount 支付金额（分）
	 */
	@RequestMapping(value = "/createCheckoutSession", method = RequestMethod.POST)
	@ResponseBody
	public String createCheckoutSession(@RequestParam(value = "unitAmount", defaultValue = "99") Long unitAmount) {
		JSONObject result = new JSONObject();
		try {
			getConfig();
			String successUrl = stripeBean.getDomain() + "/stripe/success?session_id={CHECKOUT_SESSION_ID}";
			String cancelUrl = stripeBean.getDomain() + "/stripe/cancel";

			SessionCreateParams params = SessionCreateParams.builder()
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl(successUrl)
				.setCancelUrl(cancelUrl)
				.addLineItem(SessionCreateParams.LineItem.builder()
					.setQuantity(1L)
					.setPriceData(SessionCreateParams.LineItem.PriceData.builder()
						.setCurrency("usd")
						.setUnitAmount(unitAmount)
						.setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
							.setName("IJPay Stripe Demo Product")
							.build())
						.build())
					.build())
				.build();

			Session session = StripeCheckoutApi.createSession(params);
			log.info("createCheckoutSession response:{}", session);
			result.put("code", 0);
			result.put("sessionId", session.getId());
			result.put("url", session.getUrl());
		} catch (Exception e) {
			log.error("createCheckoutSession error", e);
			result.put("code", -1);
			result.put("msg", e.getMessage());
		}
		return JSON.toJSONString(result);
	}

	/**
	 * 查询 PaymentIntent（可用金额单位分核对实际支付结果）
	 *
	 * @param paymentIntentId PaymentIntent ID（pi_xxx）
	 */
	@RequestMapping(value = "/retrievePaymentIntent", method = RequestMethod.GET)
	@ResponseBody
	public String retrievePaymentIntent(@RequestParam("paymentIntentId") String paymentIntentId) {
		Map<String, Object> map = new HashMap<>();
		try {
			getConfig();
			PaymentIntent paymentIntent = StripePayApi.retrievePaymentIntent(paymentIntentId);
			map.put("code", 0);
			map.put("data", paymentIntent);
		} catch (Exception e) {
			log.error("retrievePaymentIntent error", e);
			map.put("code", -1);
			map.put("msg", e.getMessage());
		}
		return JSON.toJSONString(map);
	}

	/**
	 * 全额退款
	 *
	 * @param paymentIntentId PaymentIntent ID（pi_xxx）
	 */
	@RequestMapping(value = "/refund")
	@ResponseBody
	public String refund(@RequestParam("paymentIntentId") String paymentIntentId) {
		Map<String, Object> map = new HashMap<>();
		try {
			getConfig();
			RefundCreateParams params = RefundCreateParams.builder()
				.setPaymentIntent(paymentIntentId)
				.build();
			Refund refund = StripePayApi.createRefund(params);
			map.put("code", 0);
			map.put("data", refund);
		} catch (Exception e) {
			log.error("refund error", e);
			map.put("code", -1);
			map.put("msg", e.getMessage());
		}
		return JSON.toJSONString(map);
	}

	/**
	 * Checkout 支付成功回跳页，展示支付结果
	 *
	 * @param sessionId Checkout Session ID（cs_test_xxx）
	 */
	@RequestMapping(value = "/success", method = RequestMethod.GET)
	@ResponseBody
	public String success(@RequestParam(value = "session_id", required = false) String sessionId) {
		try {
			getConfig();
			if (sessionId == null || sessionId.isEmpty()) {
				return "缺少 session_id 参数";
			}
			Session session = StripeCheckoutApi.retrieveSession(sessionId);
			log.info("Checkout success session:{}", session);
			// 建议以 payment_status == paid 为准（或由 Webhook 通知驱动），此处仅做演示展示
			return "支付完成，payment_status:" + session.getPaymentStatus() + "，amount_total:"
				+ session.getAmountTotal() + "，currency:" + session.getCurrency();
		} catch (Exception e) {
			log.error("success error", e);
			return "查询支付结果异常：" + e.getMessage();
		}
	}

	/**
	 * Checkout 支付取消回跳页
	 */
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	@ResponseBody
	public String cancel() {
		return "您已取消支付";
	}

	/**
	 * Stripe Webhook 回调（在 Stripe 控制台配置 endpoint 指向该地址）
	 * <p>本地调试可使用 Stripe CLI：
	 * <pre>stripe listen --forward-to localhost:80/stripe/webhook</pre>
	 */
	@RequestMapping(value = "/webhook", method = RequestMethod.POST)
	@ResponseBody
	public String webhook(HttpServletRequest request, HttpServletResponse response) {
		String payload = ServletHttpKit.readData(request);
		String sigHeader = request.getHeader("Stripe-Signature");
		try {
			Event event = StripeApi.constructEvent(payload, sigHeader);
			log.info("Stripe Webhook 收到事件 eventId:{}, type:{}", event.getId(), event.getType());
			// 根据事件类型处理业务，如：payment_intent.succeeded 更新订单为已支付
			switch (event.getType()) {
				case "payment_intent.succeeded":
					log.info("payment_intent.succeeded 支付成功");
					break;
				case "payment_intent.payment_failed":
					log.warn("payment_intent.payment_failed 支付失败");
					break;
				case "checkout.session.completed":
					log.info("checkout.session.completed 结账会话完成");
					break;
				default:
					log.info("暂未处理的事件类型：{}", event.getType());
			}
			return "success";
		} catch (SignatureVerificationException e) {
			log.error("Stripe Webhook 验签失败", e);
			response.setStatus(400);
			return "验签失败";
		} catch (Exception e) {
			log.error("Stripe Webhook 处理异常", e);
			response.setStatus(500);
			return "处理异常";
		}
	}
}
