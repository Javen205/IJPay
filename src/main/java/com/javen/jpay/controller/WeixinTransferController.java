package com.javen.jpay.controller;

import java.util.HashMap;
import java.util.Map;

import com.javen.jpay.weixin.api.WxPayApi;
import com.javen.jpay.weixin.utils.IpKit;
import com.javen.jpay.weixin.utils.PaymentKit;
import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

public class WeixinTransferController extends Controller {
	static Log log=Log.getLog(ReadHbController.class);
	private static final Prop prop = PropKit.use("wxpay.properties");
	
	//商户相关资料
	String appid = prop.get("appId");
	String mch_id = prop.get("mch_id");
	String partnerKey = prop.get("partnerKey");
	String certPath = prop.get("certPath");
	
	public void transfers(){
		String ns = String.valueOf(System.currentTimeMillis());
log.info(ns);
		Map<String, String> map = new HashMap<String, String>();
		map.put("mch_appid", appid);
		map.put("mchid", mch_id);
		map.put("nonce_str", ns);
		map.put("partner_trade_no", ns);
		map.put("openid", "o5NJx1dVRilQI6uUVSaBDuLnM3iM");
		map.put("check_name", "FORCE_CHECK");//NO_CHECK
		map.put("re_user_name", "Javen");
		map.put("amount", "888");
		map.put("desc", "IJPay 企业付款测试 -By Javen");
		String ip = IpKit.getRealIp(getRequest());
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		map.put("spbill_create_ip",ip);
		
		map.put("sign", PaymentKit.createSign(map, partnerKey));
		
		String transfers = WxPayApi.transfers(map, certPath, partnerKey);
		renderText(transfers);
	}
	
	public void getTransferInfo(){
		String tradeNo = getPara("tradeNo");
		Map<String, String> map = new HashMap<String, String>();
		String ns = String.valueOf(System.currentTimeMillis());
		map.put("nonce_str", ns);
		map.put("partner_trade_no", tradeNo);
		map.put("mch_id", mch_id);
		map.put("appid", appid);
		map.put("sign", PaymentKit.createSign(map, partnerKey));

		String transferInfo = WxPayApi.getTransferInfo(map, certPath, partnerKey);
		renderText(transferInfo);
	}
}
