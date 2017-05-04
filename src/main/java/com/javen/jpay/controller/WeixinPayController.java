package com.javen.jpay.controller;

import java.util.HashMap;
import java.util.Map;

import com.javen.jpay.vo.AjaxResult;
import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.api.PaymentApi;
import com.jfinal.weixin.sdk.kit.IpKit;
import com.jfinal.weixin.sdk.kit.PaymentKit;

public class WeixinPayController extends Controller {
	static Log log=Log.getLog(WeixinPayController.class);

	private static final Prop prop = PropKit.use("wxpay.properties");
	private AjaxResult ajax = new AjaxResult();

	//商户相关资料
	String appid = prop.get("appId");
	String mch_id = prop.get("mch_id");
	String paternerKey = prop.get("paternerKey");
	String notify_url = prop.get("domain")+"/wxpay/pay_notify";

	/**
	 * 微信APP支付
	 */
	public void appPay(){
		//不用设置授权目录域名
		//统一下单地址 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1#
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", appid);
		params.put("mch_id", mch_id);
		params.put("nonce_str", System.currentTimeMillis() / 1000 + "");
		params.put("body", "Javen微信支付测试");
		String out_trade_no=System.currentTimeMillis()+"";
		params.put("attach", "custom json");
		params.put("out_trade_no", out_trade_no);
		int price=10000;
		params.put("total_fee", price+"");
		
		String ip = IpKit.getRealIp(getRequest());
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		
		params.put("spbill_create_ip", ip);
		params.put("notify_url", notify_url);
		params.put("trade_type", "APP");

		String sign = PaymentKit.createSign(params, paternerKey);
		params.put("sign", sign);
		
		String xmlResult = PaymentApi.pushOrder(params);
		
System.out.println(xmlResult);
		Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
		
		String return_code = result.get("return_code");
		String return_msg = result.get("return_msg");
		if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
			ajax.addError(return_msg);
			renderJson(ajax);
			return;
		}
		String result_code = result.get("result_code");
		if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
			ajax.addError(return_msg);
			renderJson(ajax);
			return;
		}
		// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
		String prepay_id = result.get("prepay_id");
		//封装调起微信支付的参数 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("partnerid", mch_id);
		packageParams.put("prepayid", prepay_id);
		packageParams.put("package", "Sign=WXPay");
		packageParams.put("noncestr", System.currentTimeMillis() + "");
		packageParams.put("timestamp", System.currentTimeMillis() / 1000 + "");
		String packageSign = PaymentKit.createSign(packageParams, paternerKey);
		packageParams.put("sign", packageSign);
		
		ajax.success(packageParams);
		renderJson(ajax);
	}
}
