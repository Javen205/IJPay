package com.javen.jpay.controller;


import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.javen.jpay.alipay.AliPayApi;
import com.javen.jpay.alipay.BizContent;
import com.javen.jpay.alipay.OrderInfoUtil2_0;
import com.javen.jpay.vo.AjaxResult;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

public class AliPayController extends Controller {
	private static final Prop prop = PropKit.use("alipay.properties");
	private Log log = Log.getLog(AliPayController.class);
	private AjaxResult result = new AjaxResult();
	private boolean isDebug = true;

	public void index() {
		renderText("test");
	}

	/**
	 * App支付
	 */
	public void appPay() {
		String orderInfo;
		try {

			String body = "我是测试数据";
			String passback_params = "123";
			String subject = "1";
			String total_amount = "0.01";
			String notify_url = "http://javentech.tunnel.qydev.com/alipay/pay_notify";

			String appId;
			String rsa_private;
			if (isDebug) {
				appId = prop.get("test_appId").trim();
				rsa_private = prop.get("test_rsa_private").trim();
				System.out.println("test。。。。");
			} else {
				appId = prop.get("appId").trim();
				rsa_private = prop.get("rsa_private").trim();
			}
			System.out.println("appId:" + appId);
			System.out.println("rsa_private:" + rsa_private);

			BizContent content = new BizContent();
			content.setBody(body);
			content.setOut_trade_no(OrderInfoUtil2_0.getOutTradeNo());
			content.setPassback_params(passback_params);
			content.setSubject(subject);

			content.setTotal_amount(total_amount);
			content.setProduct_code("QUICK_MSECURITY_PAY");

			Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(appId, notify_url, content);
			String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
			String sign = OrderInfoUtil2_0.getSign(params, rsa_private);
			orderInfo = orderParam + "&" + sign;
			log.info("orderInfo>" + orderInfo);
			result.success(orderInfo);
			renderJson(result);

		} catch (Exception e) {
			e.printStackTrace();
			result.addError("system error");
		}
	}

	


	/**
	 * Wap支付
	 */
	public void wapPay() {
		String body = "我是测试数据";
		String subject = "Javen 测试";
		String total_amount = "0.01";
		String passback_params = "1";

		BizContent content = new BizContent();
		content.setBody(body);
		content.setOut_trade_no(OrderInfoUtil2_0.getOutTradeNo());
		content.setPassback_params(passback_params);
		content.setSubject(subject);
		content.setTotal_amount(total_amount);
		content.setProduct_code("QUICK_WAP_PAY");
		String returnUrl = "http://javentech.tunnel.qydev.com/alipay/return_url";
		String notifyUrl = "http://javentech.tunnel.qydev.com/alipay/notify_url";

		try {
			AliPayApi.wapPay(getResponse(), JsonKit.toJson(content),returnUrl,notifyUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderNull();
	}

	public void return_url() {
		// total_amount=0.01&timestamp=2016-12-02+18%3A11%3A42&sign=vPhxaI5bf7uSab9HuqQ4fvjLOggzpnnLK9svOdZCZ9N1mge4qm63R4k%2FowlTHbwyGCNG0%2F4PthfYbjFx22%2B2WpBNvccxajw%2Btba1Aab6EKPOAW8BoLLFFwgExtLB9ydhWL5kpP8YSLolO%2F9pkGBy5TNldz7HxdB2j6vISrD8qCs%3D&trade_no=2016120221001004200200187882&sign_type=RSA&auth_app_id=2016102000727659&charset=UTF-8&seller_id=2088102180432465&method=alipay.trade.wap.pay.return&app_id=2016102000727659&out_trade_no=120218111214806&version=1.0
		String params = getRequest().getQueryString();
		System.out.println("return_url回调参数：" + params);
		log.info("return_url回调参数：" + params);
		renderText("return_url回调参数：" + params);

	}

	/**
	 * 条形码支付
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.Yhpibd&treeId=194&articleId=105170&docType=1#s4
	 */
	public void tradePay() {
		String auth_code = getPara("auth_code");
		String subject = "Javen 支付宝条形码支付测试";
		String total_amount = "100";
		BizContent content = new BizContent();

		content.setOut_trade_no(OrderInfoUtil2_0.getOutTradeNo());
		content.setScene("bar_code");
		content.setAuth_code(auth_code);
		content.setSubject(subject);
		content.setStore_id("123");
		content.setTimeout_express("2m");
		content.setTotal_amount(total_amount);

		try {
			String resultStr = AliPayApi.tradePay(JsonKit.toJson(content));
			renderText(resultStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 扫码支付
	 */
	public void tradePrecreatePay() {
		String subject = "Javen 支付宝扫码支付测试";
		String total_amount = "86";
		BizContent content = new BizContent();

		content.setOut_trade_no(OrderInfoUtil2_0.getOutTradeNo());
		content.setSubject(subject);
		content.setStore_id("123");
		content.setTimeout_express("2m");
		content.setTotal_amount(total_amount);

		try {
			String resultStr = AliPayApi.tradePrecreatePay(JsonKit.toJson(content));
			JSONObject jsonObject = JSONObject.parseObject(resultStr);
			String qr_code = jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
			renderText(qr_code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 单笔转账到支付宝账户
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.54Ty29&treeId=193&articleId=106236&docType=1
	 */
	public void transfer() {
		boolean isSuccess = false;
		String total_amount = "100";

		BizContent content = new BizContent();

		content.setOut_biz_no(OrderInfoUtil2_0.getOutTradeNo());
		content.setPayee_type("ALIPAY_LOGONID");
		content.setPayee_account("abpkvd0206@sandbox.com");
		content.setAmount(total_amount);
		content.setPayer_show_name("测试退款");
		content.setPayee_real_name("沙箱环境");
		content.setRemark("javen测试单笔转账到支付宝");
		try {
			isSuccess = AliPayApi.transfer(JsonKit.toJson(content));
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderJson(isSuccess);
	}
	/**
	 * 下载对账单
	 */
	public void dataDataserviceBill(){
		
		try {
			String resultStr = AliPayApi.billDownloadurlQuery("{" +
					"    \"bill_type\":\"trade\"," +
					"    \"bill_date\":\"2017-04-24\"}");
			renderText(resultStr);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 退款
	 */
	public void tradeRefund(){
		
		String bizContent = "{" +
				"    \"out_trade_no\":\"042517111114931\"," +
				"    \"trade_no\":\"2017042521001004200200236813\"," +
				"    \"refund_reason\":\"正常退款\"," +
				"    \"refund_amount\":\"86.00\"}";
		try {
			String resultStr = AliPayApi.tradeRefund(bizContent);
			renderText(resultStr);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}
	
	public void tradeCancel(){
		String bizContent = "{" +
				"    \"out_trade_no\":\"042518024814931\"," +
				"    \"trade_no\":\"2017042521001004200200236814\"" +
				"  }";
		try {
			boolean isSuccess = AliPayApi.isTradeCancel(bizContent);
			renderJson(isSuccess);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}
	public void tradeQuery(){
		String bizContent = "{" +
				"    \"out_trade_no\":\"042518024814931\"," +
				"    \"trade_no\":\"2017042521001004200200236814\"" +
				"  }";
		try {
			boolean isSuccess = AliPayApi.isTradeQuery(bizContent);
			renderJson(isSuccess);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}
	public void tradeQueryByStr(){
		String out_trade_no = getPara("out_trade_no");
//		String trade_no = getPara("trade_no");
		
		BizContent bizContent = new BizContent();
		bizContent.setOut_trade_no(out_trade_no);
		
		try {
			String resultStr = AliPayApi.tradeQuery(JsonKit.toJson(bizContent)).getBody();
			renderText(resultStr);;
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}

	public void notify_url() {
		String params = HttpKit.readData(getRequest());
		log.debug("notify_url readData>>"+params);
		System.out.println("notify_url readData>>"+params);
		try {
			Map<String, String> paramsMap = stringToMap(params);// 将异步通知中收到的所有参数都存放到map中
			for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
				System.out.println(entry.getKey()+"= "+entry.getValue());
			}
			String alipayPulicKey = prop.get("wap_alipayPulicKey");
			System.out.println("alipayPulicKey>"+alipayPulicKey);
			boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, alipayPulicKey, "UTF-8" ,"RSA2"); // 调用SDK验证签名
			if (signVerified) {
				// TODO
				// 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
				renderText("success");
			} else {
				// TODO 验签失败则记录异常日志，并在response中返回failure.
				renderText("failure");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		renderText("failure");
	}
	
	
	/**
	 * App支付支付回调通知
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.TxBJbS&
	 * treeId=193&articleId=105301&docType=1#s3
	 */
	public void pay_notify() {
		String queryString = getRequest().getQueryString();
		System.out.println("支付宝回调参数：" + queryString);
		log.debug("支付宝回调参数：" + queryString);

		String notify_time = getPara("notify_time");
		String notify_type = getPara("notify_type");
		String notify_id = getPara("notify_id");
		String app_id = getPara("app_id");
		String charset = getPara("charset");
		String version = getPara("version");
		String sign_type = getPara("sign_type");
		String sign = getPara("sign");
		String trade_no = getPara("trade_no");
		String out_trade_no = getPara("out_trade_no");

		//////////////// 一下是可选参数//////////////////////////

		String buyer_id = getPara("buyer_id");
		String buyer_logon_id = getPara("buyer_logon_id");
		String trade_status = getPara("trade_status");
		String total_amount = getPara("total_amount");
		String receipt_amount = getPara("receipt_amount");

		String passback_params = getPara("passback_params");// 附加参数

		renderText(queryString);
	}

	/**
	 * 从request中获得参数Map，并返回可读的Map
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, String> stringToMap(String params) {
		// 返回值Map
		Map<String, String> returnMap = null;
		if (!StrKit.isBlank(params)) {
			returnMap = new HashMap<String, String>();
			String[] KeyValues = params.split("&");
			for (int i = 0; i < KeyValues.length; i++) {
				String[] keyValue = KeyValues[i].split("=");
				String key = keyValue[0];
				String value = keyValue[1];
				returnMap.put(key, value);
			}
		}
		return returnMap;
	}

	public static void main(String[] args) {
		System.out.println("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuPkP2VJMR6vWCX8RwSFqNIa3klCdvRFJbuS1PN1anzQeeL9eOwtU7kGdI85yxb0dcdPzOYlG+jf9go8W9hBlgjxSRoXxLx03Yfl7cLmzJO9l9vIM1+HmNF0Ctm+el4Yi9dGs/P6q7lcHPUqs/RXGfeLrg33GMVwJbLmRcDZYeIcqPAA1OVF/4SHYr+f+O7glDOd60z+veOOexyoHmvUzYWlEz5+R4kOCNM/Z0w7KGgEYvHbZopexuTuFgUWy/9tYlNrnX+cZUWXVTskLUgD1UGWM1dS5+qfriqY9MPEwJjetcPJkoCK7A4IReE4q1DffUY9KS50/1ML+7na3R/p/UQIDAQAB"
				.equals("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuPkP2VJMR6vWCX8RwSFqNIa3klCdvRFJbuS1PN1anzQeeL9eOwtU7kGdI85yxb0dcdPzOYlG+jf9go8W9hBlgjxSRoXxLx03Yfl7cLmzJO9l9vIM1+HmNF0Ctm+el4Yi9dGs/P6q7lcHPUqs/RXGfeLrg33GMVwJbLmRcDZYeIcqPAA1OVF/4SHYr+f+O7glDOd60z+veOOexyoHmvUzYWlEz5+R4kOCNM/Z0w7KGgEYvHbZopexuTuFgUWy/9tYlNrnX+cZUWXVTskLUgD1UGWM1dS5+qfriqY9MPEwJjetcPJkoCK7A4IReE4q1DffUY9KS50/1ML+7na3R/p/UQIDAQAB"));
	}
}
