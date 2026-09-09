package com.ijpay.demo.controller.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.v3.ApiException;
import com.alipay.v3.model.*;
import com.alipay.v3.util.AlipaySignature;
import com.ijpay.alipay.v3.AliPayApi;
import com.ijpay.alipay.v3.AliPayApiConfig;
import com.ijpay.alipay.v3.AliPayApiConfigKit;
import com.ijpay.alipay.v3.AliPayTradeApi;
import com.ijpay.demo.entity.AliPayBean;
import com.ijpay.demo.utils.StringUtils;
import com.ijpay.demo.vo.AjaxResult;
import enums.SignType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>支付宝支付 V3 示例，基于官方 SDK alipay-sdk-java-v3，签名、验签由官方 SDK 完成，仅需关注请求参数。</p>
 *
 * @author Javen
 */
@Controller
@RequestMapping("/v3/aliPay")
public class AliPayV3Controller extends AbstractAliPayV3ApiController {

	private static final Logger log = LoggerFactory.getLogger(AliPayV3Controller.class);

	@Autowired
	private AliPayBean aliPayBean;

	private final AjaxResult result = new AjaxResult();

	private final static String NOTIFY_URL = "/aliPayV3/notify_url";
	private final static String RETURN_URL = "/aliPayV3/return_url";

	@Override
	public AliPayApiConfig getApiConfig() throws ApiException {
		AliPayApiConfig aliPayApiConfig;
		try {
			aliPayApiConfig = AliPayApiConfigKit.getApiConfig(aliPayBean.getAppId());
		} catch (Exception e) {
			aliPayApiConfig = AliPayApiConfig.builder()
				.setAppId(aliPayBean.getAppId())
				.setAliPayPublicKey(aliPayBean.getPublicKey())
				.setAppCertPath(aliPayBean.getAppCertPath())
				.setAliPayCertPath(aliPayBean.getAliPayCertPath())
				.setAliPayRootCertPath(aliPayBean.getAliPayRootCertPath())
				.setCharset("UTF-8")
				.setPrivateKey(aliPayBean.getPrivateKey())
				.setServiceUrl(getV3BaseUrl())
				.setSignType(SignType.RSA2.getType())
				// 普通公钥方式
				// .build();
				// 证书模式
				.buildByCert();
		}
		// 同步配置到全局默认客户端，供 AliPayApi 静态方法、AliPayTradeApi 使用
		AliPayApi.setBaseUrl(aliPayApiConfig.getServiceUrl());
		AliPayApi.setAlipayConfig(aliPayApiConfig.getAlipayConfig());
		return aliPayApiConfig;
	}

	/**
	 * V3 网关地址不带 /gateway.do，RESTful 与页面支付均基于该地址拼接
	 *
	 * @return V3 网关地址
	 */
	private String getV3BaseUrl() {
		String serverUrl = aliPayBean.getServerUrl();
		if (serverUrl.endsWith("/gateway.do")) {
			return serverUrl.substring(0, serverUrl.length() - "/gateway.do".length());
		}
		return serverUrl;
	}

	@RequestMapping("")
	@ResponseBody
	public String index() {
		return "欢迎使用 IJPay 中的支付宝支付 V3 -By Javen <br/><br> 交流群：723992875、864988890";
	}

	@RequestMapping("/test")
	@ResponseBody
	public AliPayApiConfig test() throws ApiException {
		AliPayApiConfig aliPayApiConfig = getApiConfig();
		log.info("charset>" + aliPayApiConfig.getCharset());
		return aliPayApiConfig;
	}

	/**
	 * 电脑网站支付
	 *
	 * @return 支付表单 HTML
	 */
	@RequestMapping(value = "/tradePagePay")
	@ResponseBody
	public AjaxResult tradePagePay() {
		try {
			getApiConfig();
			String notifyUrl = aliPayBean.getDomain() + NOTIFY_URL;
			String returnUrl = aliPayBean.getDomain() + RETURN_URL;

			Map<String, Object> bizParams = new HashMap<>();
			bizParams.put("out_trade_no", StringUtils.getOutTradeNo());
			bizParams.put("total_amount", "88.88");
			bizParams.put("subject", "Javen PC 支付测试");
			bizParams.put("body", "Javen IJPay V3 PC 支付测试");
			bizParams.put("product_code", "FAST_INSTANT_TRADE_PAY");
			bizParams.put("notify_url", notifyUrl);
			bizParams.put("return_url", returnUrl);
			String form = AliPayApi.tradePagePay("GET", bizParams);
			result.success(form);
		} catch (ApiException e) {
			log.error("电脑网站支付异常", e);
			result.addError(resolveErrorMsg(e));
		}
		return result;
	}

	/**
	 * 手机网站支付
	 *
	 * @return 支付表单 HTML
	 */
	@RequestMapping(value = "/tradeWapPay")
	@ResponseBody
	public AjaxResult tradeWapPay() {
		try {
			getApiConfig();
			String notifyUrl = aliPayBean.getDomain() + NOTIFY_URL;
			String returnUrl = aliPayBean.getDomain() + RETURN_URL;

			Map<String, Object> bizParams = new HashMap<>();
			bizParams.put("out_trade_no", StringUtils.getOutTradeNo());
			bizParams.put("total_amount", "0.01");
			bizParams.put("subject", "Javen Wap 支付测试");
			bizParams.put("product_code", "QUICK_WAP_PAY");
			bizParams.put("notify_url", notifyUrl);
			bizParams.put("return_url", returnUrl);
			String form = AliPayApi.tradeWapPay("GET", bizParams);
			result.success(form);
		} catch (ApiException e) {
			log.error("手机网站支付异常", e);
			result.addError(resolveErrorMsg(e));
		}
		return result;
	}

	/**
	 * App 支付
	 *
	 * @return orderInfo 字符串，由 App 端调起支付宝收银台
	 */
	@RequestMapping(value = "/tradeAppPay")
	@ResponseBody
	public AjaxResult tradeAppPay() {
		try {
			getApiConfig();
			Map<String, Object> bizParams = new HashMap<>();
			bizParams.put("out_trade_no", StringUtils.getOutTradeNo());
			bizParams.put("total_amount", "0.01");
			bizParams.put("subject", "Javen App 支付测试");
			bizParams.put("product_code", "QUICK_MSECURITY_PAY");
			String orderInfo = AliPayApi.tradeAppPay(bizParams);
			result.success(orderInfo);
		} catch (ApiException e) {
			log.error("App 支付异常", e);
			result.addError(resolveErrorMsg(e));
		}
		return result;
	}

	/**
	 * 统一收单交易创建
	 *
	 * @param buyerLogonId 买家支付宝账号
	 * @return 创建结果
	 */
	@RequestMapping(value = "/tradeCreate")
	@ResponseBody
	public AjaxResult tradeCreate(@RequestParam("buyerLogonId") String buyerLogonId) {
		try {
			getApiConfig();
			String notifyUrl = aliPayBean.getDomain() + NOTIFY_URL;

			AlipayTradeCreateModel model = new AlipayTradeCreateModel();
			model.setOutTradeNo(StringUtils.getOutTradeNo());
			model.setTotalAmount("0.01");
			model.setSubject("Javen 统一收单交易创建测试");
			model.setBody("我是测试数据-By Javen");
			model.setBuyerLogonId(buyerLogonId);
			model.setNotifyUrl(notifyUrl);
			model.setTimeoutExpress("5m");

			AlipayTradeCreateResponseModel responseModel = new AliPayTradeApi().create(model);
			result.success(responseModel);
		} catch (ApiException e) {
			log.error("统一收单交易创建异常", e);
			result.addError(resolveErrorMsg(e));
		}
		return result;
	}

	/**
	 * 统一收单线下交易预创建（扫码支付）
	 *
	 * @return 二维码链接 qr_code
	 */
	@RequestMapping(value = "/tradePreCreate")
	@ResponseBody
	public AjaxResult tradePreCreate() {
		try {
			getApiConfig();

			AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
			model.setOutTradeNo(StringUtils.getOutTradeNo());
			model.setTotalAmount("0.01");
			model.setSubject("Javen 扫码支付测试");
			model.setStoreId("123");
			model.setTimeoutExpress("5m");

			AlipayTradePrecreateResponseModel responseModel = new AliPayTradeApi().precreate(model);
			JSONObject json = new JSONObject();
			json.put("qrCode", responseModel.getQrCode());
			json.put("response", responseModel);
			result.success(json);
		} catch (ApiException e) {
			log.error("统一收单线下交易预创建异常", e);
			result.addError(resolveErrorMsg(e));
		}
		return result;
	}

	/**
	 * 统一收单交易查询
	 *
	 * @param outTradeNo 商户订单号
	 * @return 查询结果
	 */
	@RequestMapping(value = "/tradeQuery")
	@ResponseBody
	public AjaxResult tradeQuery(@RequestParam("outTradeNo") String outTradeNo) {
		try {
			getApiConfig();

			AlipayTradeQueryModel model = new AlipayTradeQueryModel();
			model.setOutTradeNo(outTradeNo);

			AlipayTradeQueryResponseModel responseModel = new AliPayTradeApi().query(model);
			result.success(responseModel);
		} catch (ApiException e) {
			log.error("统一收单交易查询异常", e);
			result.addError(resolveErrorMsg(e));
		}
		return result;
	}

	/**
	 * 统一收单交易退款
	 *
	 * @param outTradeNo   商户订单号
	 * @param refundAmount 退款金额
	 * @return 退款结果
	 */
	@RequestMapping(value = "/tradeRefund")
	@ResponseBody
	public AjaxResult tradeRefund(@RequestParam("outTradeNo") String outTradeNo,
								  @RequestParam("refundAmount") String refundAmount) {
		try {
			getApiConfig();

			AlipayTradeRefundModel model = new AlipayTradeRefundModel();
			model.setOutTradeNo(outTradeNo);
			model.setRefundAmount(refundAmount);
			model.setRefundReason("IJPay V3 正常退款测试");
			model.setOutRequestNo(StringUtils.getOutTradeNo());

			AlipayTradeRefundResponseModel responseModel = new AliPayTradeApi().refund(model);
			result.success(responseModel);
		} catch (ApiException e) {
			log.error("统一收单交易退款异常", e);
			result.addError(resolveErrorMsg(e));
		}
		return result;
	}

	/**
	 * 统一收单交易关闭
	 *
	 * @param outTradeNo 商户订单号
	 * @return 关闭结果
	 */
	@RequestMapping(value = "/tradeClose")
	@ResponseBody
	public AjaxResult tradeClose(@RequestParam("outTradeNo") String outTradeNo) {
		try {
			getApiConfig();

			AlipayTradeCloseModel model = new AlipayTradeCloseModel();
			model.setOutTradeNo(outTradeNo);

			AlipayTradeCloseResponseModel responseModel = new AliPayTradeApi().close(model);
			result.success(responseModel);
		} catch (ApiException e) {
			log.error("统一收单交易关闭异常", e);
			result.addError(resolveErrorMsg(e));
		}
		return result;
	}

	/**
	 * 统一收单交易撤销
	 *
	 * @param outTradeNo 商户订单号
	 * @return 撤销结果
	 */
	@RequestMapping(value = "/tradeCancel")
	@ResponseBody
	public AjaxResult tradeCancel(@RequestParam("outTradeNo") String outTradeNo) {
		try {
			getApiConfig();

			AlipayTradeCancelModel model = new AlipayTradeCancelModel();
			model.setOutTradeNo(outTradeNo);

			AlipayTradeCancelResponseModel responseModel = new AliPayTradeApi().cancel(model);
			result.success(responseModel);
		} catch (ApiException e) {
			log.error("统一收单交易撤销异常", e);
			result.addError(resolveErrorMsg(e));
		}
		return result;
	}

	/**
	 * 异步通知回调
	 *
	 * @param request 请求
	 * @return success/failure
	 */
	@RequestMapping(value = "/notify_url")
	@ResponseBody
	public String notifyUrl(HttpServletRequest request) {
		try {
			AliPayApiConfig aliPayApiConfig = getApiConfig();

			Map<String, String> params = new HashMap<>();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (String name : requestParams.keySet()) {
				params.put(name, request.getParameter(name));
			}
			log.info("支付宝 V3 回调参数:{}", params);

			// 验签：证书模式使用支付宝公钥证书路径验签
			boolean check = AlipaySignature.certVerifyV1(params, aliPayBean.getAliPayCertPath(),
				aliPayApiConfig.getCharset(), aliPayApiConfig.getSignType());
			log.info("验签结果:{}", check);
			if (check) {
				// TODO 业务处理：核对金额、订单号，更新订单状态等
				String tradeStatus = params.get("trade_status");
				if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
					// 支付成功
				}
				return "success";
			}
			return "failure";
		} catch (ApiException e) {
			log.error("支付宝 V3 异步通知处理异常", e);
			return "failure";
		}
	}

	/**
	 * 同步跳转回调
	 *
	 * @param request 请求
	 * @return 跳转参数
	 */
	@RequestMapping(value = "/return_url")
	@ResponseBody
	public AjaxResult returnUrl(HttpServletRequest request) {
		try {
			getApiConfig();

			Map<String, String> params = new HashMap<>();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (String name : requestParams.keySet()) {
				params.put(name, request.getParameter(name));
			}
			log.info("支付宝 V3 同步跳转参数:{}", params);
			result.success(params);
		} catch (ApiException e) {
			log.error("支付宝 V3 同步跳转处理异常", e);
			result.addError(resolveErrorMsg(e));
		}
		return result;
	}

	/**
	 * 从 ApiException 中提取友好错误信息，优先取响应体中的 code 与 message
	 *
	 * @param e ApiException
	 * @return 友好错误信息
	 */
	private String resolveErrorMsg(ApiException e) {
		String body = e.getResponseBody();
		if (body != null && !body.trim().isEmpty()) {
			try {
				JSONObject json = JSONObject.parseObject(body);
				String code = json.getString("code");
				String msg = json.getString("message");
				if (msg != null && !msg.trim().isEmpty()) {
					return (code != null && !code.trim().isEmpty()) ? code + ":" + msg : msg;
				}
			} catch (Exception ignored) {
				// 响应体不是合法 JSON 时忽略
			}
		}
		String message = e.getMessage();
		return (message != null && !message.trim().isEmpty()) ? message : "支付宝接口调用失败";
	}
}
