package com.ijpay.demo.controller.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayFundAuthOrderFreezeResponse;
import com.alipay.api.response.AlipayFundCouponOrderAgreementPayResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.core.kit.PayKit;
import com.ijpay.demo.vo.AjaxResult;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Javen
 */
public class AliPayController extends AliPayApiController {
	private Log log = Log.getLog(AliPayController.class);

	private final Prop prop = PropKit.use("alipay.properties");
	private String charset = "UTF-8";
	private String privateKey = prop.get("alipay.privateKey");
	private String publicKey = prop.get("alipay.publicKey");
	private String appCertPath = prop.get("alipay.appCertPath");
	private String aliPayCertPath = prop.get("alipay.aliPayCertPath");
	private String aliPayRootCertPath = prop.get("alipay.aliPayRootCertPath");
	private String serviceUrl = prop.get("alipay.serverUrl");
	private String appId = prop.get("alipay.appId");
	private String signType = "RSA2";
	private String domain = prop.get("alipay.domain");

	/**
	 * 普通公钥模式
	 */
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

	@Override
	public AliPayApiConfig getApiConfig() throws AlipayApiException {
		AliPayApiConfig aliPayApiConfig;
		try {
			aliPayApiConfig = AliPayApiConfigKit.getApiConfig(appId);
		} catch (Exception e) {
			aliPayApiConfig = AliPayApiConfig.builder()
				.setAppId(appId)
				.setAliPayPublicKey(publicKey)
				.setAppCertPath(appCertPath)
				.setAliPayCertPath(aliPayCertPath)
				.setAliPayRootCertPath(aliPayRootCertPath)
				.setCharset("UTF-8")
				.setPrivateKey(privateKey)
				.setServiceUrl(serviceUrl)
				.setSignType(signType)
				// 普通公钥方式
				//.build();
				// 证书模式
				.buildByCert();

		}
		return aliPayApiConfig;
	}

	public void index() {
		log.info(JsonKit.toJson(AliPayApiConfigKit.getAliPayApiConfig()));
		renderText("欢迎使用 IJPay 中的支付宝支付 -By Javen   交流群：723992875、864988890");
	}

	/**
	 * app支付
	 */
	public void appPay() {
		try {
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setBody("我是测试数据-By Javen");
			model.setSubject("App支付测试-By Javen");
			model.setOutTradeNo(PayKit.generateStr());
			model.setTimeoutExpress("30m");
			model.setTotalAmount("0.01");
			model.setPassbackParams("callback params");
			model.setProductCode("QUICK_MSECURITY_PAY");
			String orderInfo = AliPayApi.appPayToResponse(model, domain + NOTIFY_URL).getBody();
			renderJson(new AjaxResult().success(orderInfo));

		} catch (AlipayApiException e) {
			e.printStackTrace();
			renderJson(new AjaxResult().addError("system error"));
		}
	}

	/**
	 * Wap支付
	 */
	public void wapPay() {
		String body = "我是测试数据-By Javen";
		String subject = "Javen Wap支付测试";
		String totalAmount = getPara("totalAmount");
		String passBackParams = "1";
		String returnUrl = domain + RETURN_URL;
		String notifyUrl = domain + NOTIFY_URL;

		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setBody(body);
		model.setSubject(subject);
		model.setTotalAmount(totalAmount);
		model.setPassbackParams(passBackParams);
		String outTradeNo = PayKit.generateStr();
		System.out.println("wap outTradeNo>" + outTradeNo);
		model.setOutTradeNo(outTradeNo);
		model.setProductCode("QUICK_WAP_PAY");

		try {
			AliPayApi.wapPay(getResponse(), model, returnUrl, notifyUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderNull();
	}


	/**
	 * PC支付
	 */
	public void pcPay() {
		try {
			String totalAmount = "0.1";
			String outTradeNo = PayKit.generateStr();
			log.info("pc outTradeNo>" + outTradeNo);

			String returnUrl = domain + RETURN_URL;
			String notifyUrl = domain + NOTIFY_URL;

			AlipayTradePagePayModel model = new AlipayTradePagePayModel();

			model.setOutTradeNo(outTradeNo);
			model.setProductCode("FAST_INSTANT_TRADE_PAY");
			model.setTotalAmount(totalAmount);
			model.setSubject("Javen PC支付测试");
			model.setBody("Javen IJPay PC支付测试");
			model.setPassbackParams("passback_params");
			//花呗分期相关的设置
			/**
			 * 测试环境不支持花呗分期的测试
			 * hb_fq_num代表花呗分期数，仅支持传入3、6、12，其他期数暂不支持，传入会报错；
			 * hb_fq_seller_percent代表卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种，其他比例暂不支持，传入会报错。
			 */
			ExtendParams extendParams = new ExtendParams();
			extendParams.setHbFqNum("3");
			extendParams.setHbFqSellerPercent("0");
			model.setExtendParams(extendParams);

			AliPayApi.tradePage(getResponse(), model, notifyUrl, returnUrl);
			renderNull();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void tradePay() {
		String authCode = getPara("authCode");
		String scene = getPara("scene");
		String subject = null;
		String waveCode = "wave_code";
		String barCode = "bar_code";
		if (scene.equals(waveCode)) {
			subject = "Javen 支付宝声波支付测试";
		} else if (scene.equals(barCode)) {
			subject = "Javen 支付宝条形码支付测试";
		}
		String totalAmount = "100";
		String notifyUrl = domain + NOTIFY_URL;

		AlipayTradePayModel model = new AlipayTradePayModel();
		model.setAuthCode(authCode);
		model.setSubject(subject);
		model.setTotalAmount(totalAmount);
		model.setOutTradeNo(PayKit.generateStr());
		model.setScene(scene);
		try {
			String resultStr = AliPayApi.tradePayToResponse(model, notifyUrl).getBody();
			renderText(resultStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 扫码支付
	 */
	public void tradePreCreatePay() {
		String subject = "Javen 支付宝扫码支付测试";
		String totalAmount = "86";
		String storeId = "123";
		String notifyUrl = domain + NOTIFY_URL;

		AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
		model.setSubject(subject);
		model.setTotalAmount(totalAmount);
		model.setStoreId(storeId);
		model.setTimeoutExpress("5m");
		model.setOutTradeNo(PayKit.generateStr());
		try {
			String resultStr = AliPayApi.tradePrecreatePayToResponse(model, notifyUrl).getBody();
			JSONObject jsonObject = JSONObject.parseObject(resultStr);
			String qrCode = jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
			renderText(qrCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 单笔转账到支付宝账户
	 * https://docs.open.alipay.com/309/106235/
	 */
	public void transfer() {
		String totalAmount = "66";
		AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
		model.setOutBizNo(PayKit.generateStr());
		model.setPayeeType("ALIPAY_LOGONID");
		model.setPayeeAccount("abpkvd0206@sandbox.com");
		model.setAmount(totalAmount);
		model.setPayerShowName("测试退款");
		model.setPayerRealName("沙箱环境");
		model.setRemark("javen测试单笔转账到支付宝");

		try {
			renderJson(AliPayApi.transferToResponse(model));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void transferQuery() {
		String outBizNo = getPara("outBizNo");
		String orderId = getPara("orderId");

		AlipayFundTransOrderQueryModel model = new AlipayFundTransOrderQueryModel();
		if (StrKit.notBlank(outBizNo)) {
			model.setOutBizNo(outBizNo);
		}
		if (StrKit.notBlank(orderId)) {
			model.setOrderId(orderId);
		}

		try {
			renderJson(AliPayApi.transferQueryToResponse(model));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uniTransfer() {
		String totalAmount = "1";
		AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
		model.setOutBizNo(PayKit.generateStr());
		model.setTransAmount(totalAmount);
		model.setProductCode("TRANS_ACCOUNT_NO_PWD");
		model.setBizScene("DIRECT_TRANSFER");
		model.setOrderTitle("统一转账-转账至支付宝账户");
		model.setRemark("IJPay 测试统一转账");

		Participant payeeInfo = new Participant();
		payeeInfo.setIdentity("gxthqd7606@sandbox.com");
		payeeInfo.setIdentityType("ALIPAY_LOGON_ID");
		payeeInfo.setName("沙箱环境");
		model.setPayeeInfo(payeeInfo);

		try {
			renderJson(AliPayApi.uniTransferToResponse(model, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uniTransferQuery() {
		String outBizNo = getPara("outBizNo");
		String orderId = getPara("orderId");
		AlipayFundTransCommonQueryModel model = new AlipayFundTransCommonQueryModel();
		if (StrKit.notBlank(outBizNo)) {
			model.setOutBizNo(outBizNo);
		}
		if (StrKit.notBlank(orderId)) {
			model.setOrderId(orderId);
		}

		try {
			renderJson(AliPayApi.transCommonQueryToResponse(model, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 账户余额查询
	 */
	public void accountQuery() {
		String aliPayUserId = getPara("aliPayUserId");
		AlipayFundAccountQueryModel model = new AlipayFundAccountQueryModel();
		model.setAlipayUserId(aliPayUserId);
		model.setAccountType("ACCTRANS_ACCOUNT");
		try {
			renderJson(AliPayApi.accountQueryToResponse(model, null));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 资金授权冻结接口
	 */
	public void authOrderFreeze() {
		try {
			String authCode = getPara("authCode");
			AlipayFundAuthOrderFreezeModel model = new AlipayFundAuthOrderFreezeModel();
			model.setOutOrderNo(PayKit.generateStr());
			model.setOutRequestNo(PayKit.generateStr());
			model.setAuthCode(authCode);
			model.setAuthCodeType("bar_code");
			model.setOrderTitle("资金授权冻结-By IJPay");
			model.setAmount("36");
			model.setPayTimeout("50000");
			model.setProductCode("PRE_AUTH");

			AlipayFundAuthOrderFreezeResponse response = AliPayApi.authOrderFreezeToResponse(model);
			renderJson(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 红包协议支付接口
	 * https://docs.open.alipay.com/301/106168/
	 */
	public void agreementPay() {
		try {
			AlipayFundCouponOrderAgreementPayModel model = new AlipayFundCouponOrderAgreementPayModel();
			model.setOutOrderNo(PayKit.generateStr());
			model.setOutRequestNo(PayKit.generateStr());
			model.setOrderTitle("红包协议支付接口-By IJPay");
			model.setAmount("36");
			model.setPayerUserId("2088102180432465");


			AlipayFundCouponOrderAgreementPayResponse response = AliPayApi.fundCouponOrderAgreementPayToResponse(model);
			renderJson(response);
		} catch (Exception e) {
			e.printStackTrace();
			renderText("有异常哦!!!");
		}
	}

	/**
	 * 下载对账单
	 */
	public void dataDataserviceBill() {
		String para = getPara("billDate");
		try {
			AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
			model.setBillType("trade");
			model.setBillDate(para);
			String resultStr = AliPayApi.billDownloadUrlQuery(model);
			renderText(resultStr);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 退款
	 */
	public void tradeRefund() {

		try {
			AlipayTradeRefundModel model = new AlipayTradeRefundModel();
			model.setOutTradeNo("081014283315023");
			model.setTradeNo("2017081021001004200200273870");
			model.setRefundAmount("86.00");
			model.setRefundReason("正常退款");
			String resultStr = AliPayApi.tradeRefundToResponse(model).getBody();
			renderText(resultStr);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 交易查询
	 */
	public void tradeQuery() {
		try {
			String outTradeNo = getPara("outTradeNo");
			String tradeNo = getPara("tradeNo");
			AlipayTradeQueryModel model = new AlipayTradeQueryModel();
			if (StrKit.notBlank(outTradeNo)) {
				model.setOutTradeNo(outTradeNo);
			}
			if (StrKit.notBlank(tradeNo)) {
				model.setTradeNo(tradeNo);
			}
			renderJson(AliPayApi.tradeQueryToResponse(model));
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}

	public void tradeQueryByStr() {
		try {
			String outTradeNo = getPara("outTradeNo");
			String tradeNo = getPara("tradeNo");

			AlipayTradeQueryModel model = new AlipayTradeQueryModel();
			if (StrKit.notBlank(outTradeNo)) {
				model.setOutTradeNo(outTradeNo);
			}
			if (StrKit.notBlank(tradeNo)) {
				model.setTradeNo(tradeNo);
			}
			String resultStr = AliPayApi.tradeQueryToResponse(model).getBody();
			renderText(resultStr);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建订单
	 * {"alipay_trade_create_response":{"code":"10000","msg":"Success","out_trade_no":"081014283315033","trade_no":"2017081021001004200200274066"},"sign":"ZagfFZntf0loojZzdrBNnHhenhyRrsXwHLBNt1Z/dBbx7cF1o7SZQrzNjRHHmVypHKuCmYifikZIqbNNrFJauSuhT4MQkBJE+YGPDtHqDf4Ajdsv3JEyAM3TR/Xm5gUOpzCY7w+RZzkHevsTd4cjKeGM54GBh0hQH/gSyhs4pEN3lRWopqcKkrkOGZPcmunkbrUAF7+AhKGUpK+AqDw4xmKFuVChDKaRdnhM6/yVsezJFXzlQeVgFjbfiWqULxBXq1gqicntyUxvRygKA+5zDTqE5Jj3XRDjVFIDBeOBAnM+u03fUP489wV5V5apyI449RWeybLg08Wo+jUmeOuXOA=="}
	 */
	public void tradeCreate() {
		String outTradeNo = getPara("outTradeNo");

		String notifyUrl = domain + NOTIFY_URL;

		AlipayTradeCreateModel model = new AlipayTradeCreateModel();
		model.setOutTradeNo(outTradeNo);
		model.setTotalAmount("88.88");
		model.setBody("Body");
		model.setSubject("Javen 测试统一收单交易创建接口");
		//买家支付宝账号，和buyer_id不能同时为空
		model.setBuyerLogonId("abpkvd0206@sandbox.com");
		try {

			AlipayTradeCreateResponse response = AliPayApi.tradeCreateToResponse(model, notifyUrl);
			renderJson(response.getBody());
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 撤销订单
	 */
	public void tradeCancel() {
		try {
			String outTradeNo = getPara("outTradeNo");
			String tradeNo = getPara("tradeNo");

			AlipayTradeCancelModel model = new AlipayTradeCancelModel();
			if (StrKit.notBlank(outTradeNo)) {
				model.setOutTradeNo(outTradeNo);
			}
			if (StrKit.notBlank(tradeNo)) {
				model.setTradeNo(tradeNo);
			}

			renderJson(AliPayApi.tradeCancelToResponse(model));
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭订单
	 */
	public void tradeClose() {
		String outTradeNo = getPara("outTradeNo");
		String tradeNo = getPara("tradeNo");
		try {
			AlipayTradeCloseModel model = new AlipayTradeCloseModel();
			if (StrKit.notBlank(outTradeNo)) {
				model.setOutTradeNo(outTradeNo);
			}
			if (StrKit.notBlank(tradeNo)) {
				model.setTradeNo(tradeNo);
			}

			String resultStr = AliPayApi.tradeCloseToResponse(model).getBody();
			renderText(resultStr);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 结算
	 */
	public void tradeOrderSettle() {
		//支付宝订单号
		String tradeNo = getPara("tradeNo");
		try {
			AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();
			model.setOutRequestNo(PayKit.generateStr());
			model.setTradeNo(tradeNo);

			String resultStr = AliPayApi.tradeOrderSettleToResponse(model).getBody();
			renderText(resultStr);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取应用授权URL并授权
	 */
	public void toOauth() {
		try {
			String redirectUri = domain + "/aliPay/redirectUri";
			System.out.println(AliPayApiConfigKit.getAppId());
			String oauth2Url = AliPayApi.getOauth2Url(AliPayApiConfigKit.getAppId(), redirectUri);
			redirect(oauth2Url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 应用授权回调
	 */
	public void redirectUri() {
		try {
			String appId = getPara("appId");
			String appAuthCode = getPara("appAuthCode");
			System.out.println("appId:" + appId);
			System.out.println("appAuthCode:" + appAuthCode);
			//使用app_auth_code换取app_auth_token
			AlipayOpenAuthTokenAppModel model = new AlipayOpenAuthTokenAppModel();
			model.setGrantType("authorization_code");
			model.setCode(appAuthCode);
			String result = AliPayApi.openAuthTokenAppToResponse(model).getBody();
			renderText(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询授权信息
	 */
	public void openAuthTokenAppQuery() {
		try {
			String appAuthToken = getPara("appAuthToken");
			AlipayOpenAuthTokenAppQueryModel model = new AlipayOpenAuthTokenAppQueryModel();
			model.setAppAuthToken(appAuthToken);
			String result = AliPayApi.openAuthTokenAppQueryToResponse(model).getBody();
			renderText(result);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量付款到支付宝账户有密接口
	 */
	public void batchTrans() {
		try {
			String signType = "MD5";
			String notifyUrl = domain + NOTIFY_URL;
			;
			Map<String, String> params = new HashMap<>(9);
			params.put("partner", "PID");
			params.put("sign_type", signType);
			params.put("notify_url", notifyUrl);
			params.put("account_name", "xxx");
			params.put("detail_data", "流水号1^收款方账号1^收款账号姓名1^付款金额1^备注说明1|流水号2^收款方账号2^收款账号姓名2^付款金额2^备注说明2");
			params.put("batch_no", String.valueOf(System.currentTimeMillis()));
			params.put("batch_num", 1 + "");
			params.put("batch_fee", 10.00 + "");
			params.put("email", "xx@xxx.com");

			AliPayApi.batchTrans(params, AliPayApiConfigKit.getAliPayApiConfig().getPrivateKey(), signType, getResponse());
		} catch (IOException e) {
			e.printStackTrace();
		}
		renderNull();
	}

	/**
	 * 地铁购票核销码发码
	 */
	public void voucherGenerate() {
		try {
			//需要支付成功的订单号
			String tradeNo = getPara("tradeNo");

			AlipayCommerceCityfacilitatorVoucherGenerateModel model = new AlipayCommerceCityfacilitatorVoucherGenerateModel();
			model.setCityCode("440300");
			model.setTradeNo(tradeNo);
			model.setTotalFee("8");
			model.setTicketNum("2");
			model.setTicketType("oneway");
			model.setSiteBegin("001");
			model.setSiteEnd("002");
			model.setTicketPrice("4");
			String result = AliPayApi.voucherGenerateToResponse(model).getBody();
			renderText(result);
		} catch (AlipayApiException e) {
			e.printStackTrace();
			renderText(e.getMessage());
		}

	}

	public void returnUrl() {
		try {
			// 获取支付宝GET过来反馈信息
			Map<String, String> map = AliPayApi.toMap(getRequest());
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}

			boolean verifyResult = AlipaySignature.rsaCheckV1(map, AliPayApiConfigKit.getAliPayApiConfig().getAliPayPublicKey(), charset,
				AliPayApiConfigKit.getAliPayApiConfig().getSignType());

			if (verifyResult) {
				// 验证成功
				// TODO 请在这里加上商户的业务逻辑程序代码
				System.out.println("return_url 验证成功");
				renderText("success");
			} else {
				System.out.println("return_url 验证失败");
				// TODO
				renderText("failure");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			renderText("failure");
		}
	}

	public void certReturnUrl() {
		try {
			// 获取支付宝GET过来反馈信息
			Map<String, String> map = AliPayApi.toMap(getRequest());
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}

			boolean verifyResult = AlipaySignature.rsaCertCheckV1(map, aliPayCertPath, charset,
				"RSA2");

			if (verifyResult) {
				// 验证成功
				// TODO 请在这里加上商户的业务逻辑程序代码
				System.out.println("return_url 验证成功");
				renderText("success");
			} else {
				System.out.println("return_url 验证失败");
				// TODO
				renderText("failure");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			renderText("failure");
		}
	}

	public void notifyUrl() {
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = AliPayApi.toMap(getRequest());

			for (Map.Entry<String, String> entry : params.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}

			boolean verifyResult = AlipaySignature.rsaCheckV1(params, AliPayApiConfigKit.getAliPayApiConfig().getAliPayPublicKey(), charset,
				AliPayApiConfigKit.getAliPayApiConfig().getSignType());

			if (verifyResult) {
				// 验证成功
				// TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
				System.out.println("notify_url 验证成功succcess");
				renderText("success");
			} else {
				System.out.println("notify_url 验证失败");
				// TODO
				renderText("failure");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			renderText("failure");
		}
	}

	public void certNotifyUrl() {
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = AliPayApi.toMap(getRequest());

			for (Map.Entry<String, String> entry : params.entrySet()) {
				System.out.println(entry.getKey() + " = " + entry.getValue());
			}

			boolean verifyResult = AlipaySignature.rsaCertCheckV1(params, AliPayApiConfigKit.getAliPayApiConfig().getAliPayCertPath(), charset,
				AliPayApiConfigKit.getAliPayApiConfig().getSignType());

			if (verifyResult) {
				// 验证成功
				// TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
				System.out.println("notify_url 验证成功succcess");
				renderText("success");
			} else {
				System.out.println("notify_url 验证失败");
				// TODO
				renderText("failure");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
			renderText("failure");
		}
	}
}
