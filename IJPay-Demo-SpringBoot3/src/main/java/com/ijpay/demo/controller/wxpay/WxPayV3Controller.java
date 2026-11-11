package com.ijpay.demo.controller.wxpay;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.AuthTypeEnum;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.core.utils.DateTimeZoneUtil;
import com.ijpay.demo.entity.WxPayV3Bean;
import com.ijpay.servlet.jakarta.ServletHttpKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.model.v3.Amount;
import com.ijpay.wxpay.model.v3.UnifiedOrderModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，微信支付 Api-v3 示例（Spring Boot 3 + jakarta.servlet）。</p>
 *
 * <p>回调报文读取与响应写出使用 {@link ServletHttpKit}，不依赖 javax.servlet。</p>
 *
 * @author Javen
 */
@RestController
@RequestMapping("/v3")
public class WxPayV3Controller {
	private static final Logger log = LoggerFactory.getLogger(WxPayV3Controller.class);
	private static final int OK = 200;
	private static final String VERIFY_FAILED = "{\"code\":\"FAIL\",\"message\":\"应答签名验证失败\"}";
	private static final String SYSTEM_ERROR = "{\"code\":\"FAIL\",\"message\":\"系统繁忙，请稍后再试\"}";

	private final WxPayV3Bean wxPayV3Bean;

	public WxPayV3Controller(WxPayV3Bean wxPayV3Bean) {
		this.wxPayV3Bean = wxPayV3Bean;
	}

	@RequestMapping("")
	public String index() {
		return "欢迎使用 IJPay 中的微信支付 Api-v3（Spring Boot 3 + jakarta.servlet）-By Javen";
	}

	/**
	 * Native 下单，返回二维码链接
	 */
	@RequestMapping("/nativePay")
	public String nativePay() {
		try {
			String timeExpire = DateTimeZoneUtil.dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
			UnifiedOrderModel unifiedOrderModel = new UnifiedOrderModel()
				.setAppid(wxPayV3Bean.getAppId())
				.setMchid(wxPayV3Bean.getMchId())
				.setDescription("IJPay 让支付触手可及")
				.setOut_trade_no(PayKit.generateStr())
				.setTime_expire(timeExpire)
				.setAttach("IJPay-Demo-SpringBoot3")
				.setNotify_url(wxPayV3Bean.getDomain().concat("/v3/payNotify"))
				.setAmount(new Amount().setTotal(1));

			log.info("统一下单请求已发送");
			IJPayHttpResponse response = WxPayApi.v3(
				RequestMethodEnum.POST,
				WxDomainEnum.CHINA.toString(),
				BasePayApiEnum.NATIVE_PAY.toString(),
				wxPayV3Bean.getMchId(),
				getSerialNumber(),
				null,
				wxPayV3Bean.getKeyPath(),
				JSONUtil.toJsonStr(unifiedOrderModel),
				AuthTypeEnum.RSA.getCode()
			);
			log.info("统一下单响应 status={}", response.getStatus());
			// 使用微信支付公钥验证应答签名，验签失败视为不可信响应，直接拒绝
			boolean verifySignature = WxPayKit.verifyPublicKeySignature(response, wxPayV3Bean.getPlatformCertPath());
			if (!verifySignature) {
				log.error("统一下单应答验签失败");
				return VERIFY_FAILED;
			}
			return response.getBody();
		} catch (Exception e) {
			log.error("微信支付统一下单异常", e);
			return SYSTEM_ERROR;
		}
	}

	/**
	 * 按商户订单号查询订单
	 */
	@RequestMapping("/query")
	public String query(@RequestParam String outTradeNo) {
		try {
			Map<String, String> params = new HashMap<>(4);
			params.put("mchid", wxPayV3Bean.getMchId());

			IJPayHttpResponse response = WxPayApi.v3(
				RequestMethodEnum.GET,
				WxDomainEnum.CHINA.toString(),
				String.format(BasePayApiEnum.ORDER_QUERY_BY_OUT_TRADE_NO.toString(), URLUtil.encode(outTradeNo)),
				wxPayV3Bean.getMchId(),
				getSerialNumber(),
				null,
				wxPayV3Bean.getKeyPath(),
				params,
				AuthTypeEnum.RSA.getCode()
			);
			log.info("查询响应 status={}", response.getStatus());
			if (response.getStatus() == OK) {
				boolean verifySignature = WxPayKit.verifyPublicKeySignature(response, wxPayV3Bean.getPlatformCertPath());
				if (!verifySignature) {
					log.error("订单查询应答验签失败");
					return VERIFY_FAILED;
				}
				return response.getBody();
			}
			return JSONUtil.toJsonStr(response);
		} catch (Exception e) {
			log.error("微信支付订单查询异常", e);
			return SYSTEM_ERROR;
		}
	}

	/**
	 * 支付结果回调：验签 + 解密
	 */
	@RequestMapping("/payNotify")
	public void payNotify(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> result = new HashMap<>(4);
		try {
			String timestamp = request.getHeader("Wechatpay-Timestamp");
			String nonce = request.getHeader("Wechatpay-Nonce");
			String serialNo = request.getHeader("Wechatpay-Serial");
			String signature = request.getHeader("Wechatpay-Signature");

			String body = ServletHttpKit.readData(request);
			log.info("收到支付通知 serialNo={}", serialNo);

			String plainText = null;
			// 微信支付公钥模式：serialNo 与商户配置的公钥 ID 一致时，使用公钥验签并解密
			if (StrUtil.equals(serialNo, wxPayV3Bean.getPublicKeyId())) {
				plainText = WxPayKit.verifyPublicKeyNotify(body, signature, nonce, timestamp,
					wxPayV3Bean.getApiKey3(), wxPayV3Bean.getPlatformCertPath());
			}

			if (StrUtil.isNotEmpty(plainText)) {
				// TODO 请在这里加上商户的业务逻辑程序代码，回调可能重复，需要做去重处理
				JSONObject jsonObject = JSONUtil.parseObj(plainText);
				log.info("支付通知 tradeState={}", jsonObject.getStr("trade_state"));
				response.setStatus(OK);
				result.put("code", "SUCCESS");
				result.put("message", "SUCCESS");
			} else {
				response.setStatus(500);
				result.put("code", "ERROR");
				result.put("message", "签名错误");
			}
		} catch (Exception e) {
			log.error("微信支付回调处理异常", e);
			response.setStatus(500);
			result.put("code", "ERROR");
			result.put("message", "系统异常");
		}
		try {
			response.setHeader("Content-type", ContentType.JSON.toString());
			response.getOutputStream().write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
			response.flushBuffer();
		} catch (Exception e) {
			log.error("微信支付回调响应写出异常", e);
		}
	}

	/**
	 * 获取商户 API 证书序列号
	 */
	private String getSerialNumber() {
		X509Certificate certificate = PayKit.getCertificate(wxPayV3Bean.getCertPath());
		if (certificate == null) {
			throw new IllegalStateException("未找到商户 API 证书: " + wxPayV3Bean.getCertPath());
		}
		return certificate.getSerialNumber().toString(16).toUpperCase();
	}
}
