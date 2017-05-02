package com.javen.jpay.controller;

import java.util.HashMap;
import java.util.Map;

import com.javen.jpay.vo.AjaxResult;
import com.javen.jpay.weixin.api.PayApi;
import com.javen.jpay.weixin.api.QrCodeScanPayApi;
import com.jfinal.core.Controller;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.api.PaymentApi.TradeType;
import com.jfinal.weixin.sdk.kit.IpKit;
import com.jfinal.weixin.sdk.kit.PaymentKit;

public class ApiController extends Controller {
	//商户相关资料
	String appid = PropKit.get("appId");
	String mch_id = PropKit.get("mch_id");
	String sub_mch_id = PropKit.get("sub_mch_id");
	String sub_appid = PropKit.get("sub_appid");
	String paternerKey = PropKit.get("paternerKey");
	
	public void orderQuery(){
		
		String resultStr = PayApi.orderQuery(PayApi.buildParasMap(appid, sub_appid, mch_id, sub_mch_id,
				"4009682001201704096411927414", null, paternerKey));
		renderText(resultStr);
	}
	
	public void downloadbBill(){
		String bill_date = getPara("bill_date");
		Map<String, String> params = new HashMap<>();
		params.put("appid", appid);
		params.put("mch_id", mch_id);
		params.put("sub_appid", sub_appid);
		params.put("sub_mch_id", sub_mch_id);
		params.put("nonce_str", String.valueOf(System.currentTimeMillis()));
		params.put("bill_date", bill_date);
		params.put("bill_type", "ALL");
		String sign = PaymentKit.createSign(params, paternerKey);
		params.put("sign", sign );
		String resultStr = PayApi.downloadBill(params);
		renderText(resultStr);
	}
	
	public void toShortUrlApi(){
		String long_url = getPara("long_url");
		Map<String, String> params = new HashMap<>();
		params.put("appid", appid);
		params.put("mch_id", mch_id);
		params.put("sub_appid", sub_appid);
		params.put("sub_mch_id", sub_mch_id);
		params.put("nonce_str", String.valueOf(System.currentTimeMillis()));
		params.put("long_url", long_url);
		String sign = PaymentKit.createSign(params, paternerKey);
		params.put("sign", sign );
		String resultStr = PayApi.toShortUrl(params);
		renderText(resultStr);
	}
	
	//TODO 待测试
	public void scanModeOnePay(){
		String resultStr = QrCodeScanPayApi.getCodeUrl("wx5e9360a3f46f64cd", "1322117501", "1", "5C8csBd4cG4c9039OKDnNaZmYlkAzXV3", true);
		renderText(resultStr);
	}
	
	public void scanModeTwoPay(){
		String ip = IpKit.getRealIp(getRequest());
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		AjaxResult result = QrCodeScanPayApi.scanModeTwoPay(appid, sub_appid, mch_id, sub_mch_id
				, "001", "Javen微信公众号开发", null, "javen test", 
				String.valueOf(System.currentTimeMillis()/100), "1",ip , "www.baidu.com", TradeType.NATIVE.name(), paternerKey);
		System.out.println(result.toString());
		renderText(result.toString());
	}
}
