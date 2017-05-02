package com.javen.jpay.weixin.api;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javen.jpay.vo.AjaxResult;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.weixin.sdk.kit.PaymentKit;

public class QrCodeScanPayApi {
	/**
	 * 扫码模式一之生成二维码
	 * @param appid
	 * @param mch_id
	 * @param product_id
	 * @param paternerKey
	 * @param isToShortUrl 是否转化为短连接
	 * @return
	 */
	public static String getCodeUrl(String appid, String mch_id, String product_id, String paternerKey, boolean isToShortUrl){
		String url="weixin://wxpay/bizpayurl?sign=XXXXX&appid=XXXXX&mch_id=XXXXX&product_id=XXXXX&time_stamp=XXXXX&nonce_str=XXXXX";
		String timeStamp=Long.toString(System.currentTimeMillis() / 1000);
		String nonceStr=Long.toString(System.currentTimeMillis());
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("product_id",product_id);
		packageParams.put("time_stamp", timeStamp);
		packageParams.put("nonce_str", nonceStr);
		String packageSign = PaymentKit.createSign(packageParams, paternerKey);
		String qrCodeUrl=replace(url, "XXXXX", packageSign,appid,mch_id,product_id,timeStamp,nonceStr);
		if (isToShortUrl) {
			String shortResult = PayApi.toShortUrl(PayApi.buildShortUrlParasMap(appid, null, mch_id, null, qrCodeUrl, paternerKey));
			if (PropKit.getBoolean("devMode", false)) {
				System.out.println(shortResult);
			}
			Map<String, String> shortMap = PaymentKit.xmlToMap(shortResult);
			String return_code = shortMap.get("return_code");
			if (!StrKit.isBlank(return_code) && "SUCCESS".equals(return_code)) {
				String result_code = shortMap.get("result_code");
				if (!StrKit.isBlank(result_code) && "SUCCESS".equals(result_code)) {
					qrCodeUrl = shortMap.get("short_url");
				}
			}
		}
		
		return qrCodeUrl;
	}
	
	public interface CallBackProductId{
		Map<String, String> addParams(String product_id);
	}
	
	public static void scanModeOnePayCallBack(HttpServletRequest request ,HttpServletResponse response, String paternerKey, String body,CallBackProductId a) throws Exception{
		String result = HttpKit.readData(request);
		if (PropKit.getBoolean("devMode", false)) {
			System.out.println(result);
		}
		Map<String, String> map = PaymentKit.xmlToMap(result);
		String appid=map.get("appid");
		String openid = map.get("openid");
		String mch_id = map.get("mch_id");
		String is_subscribe = map.get("is_subscribe");
		String nonce_str = map.get("nonce_str");
		String product_id = map.get("product_id");
		String sign = map.get("sign");
		
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("openid", openid);
		packageParams.put("mch_id",mch_id);
		packageParams.put("is_subscribe",is_subscribe);
		packageParams.put("nonce_str",nonce_str);
		packageParams.put("product_id", product_id);
		
		String packageSign = PaymentKit.createSign(packageParams, paternerKey);
		
		Map<String, String> params = a.addParams(product_id);
		
		String xmlResult = PayApi.pushOrder(params);
		if (PropKit.getBoolean("devMode", false)) {
			System.out.println("prepay_xml>>>"+xmlResult);
		}
		/**
         * 发送信息给微信服务器
         */
		Map<String, String> payResult = PaymentKit.xmlToMap(xmlResult);
		
		String return_code = payResult.get("return_code");
		String result_code = payResult.get("result_code");
		
		if (StrKit.notBlank(return_code) && StrKit.notBlank(result_code) && return_code.equalsIgnoreCase("SUCCESS")&&result_code.equalsIgnoreCase("SUCCESS")) {
			// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
			String prepay_id = payResult.get("prepay_id");
			
			Map<String, String> prepayParams = new HashMap<String, String>();
			prepayParams.put("return_code", "SUCCESS");
			prepayParams.put("appId", appid);
			prepayParams.put("mch_id", mch_id);
			prepayParams.put("nonceStr", System.currentTimeMillis() + "");
			prepayParams.put("prepay_id", prepay_id);
			String prepaySign = null;
			if (sign.equals(packageSign)) {
				prepayParams.put("result_code", "SUCCESS");
			}else {
				prepayParams.put("result_code", "FAIL");
				prepayParams.put("err_code_des", "订单失效");   //result_code为FAIL时，添加该键值对，value值是微信告诉客户的信息
			}
			prepaySign = PaymentKit.createSign(prepayParams, paternerKey);
			prepayParams.put("sign", prepaySign);
			String xml = PaymentKit.toXml(prepayParams);
			PrintWriter writer = response.getWriter();
			writer.write(xml);
			writer.flush();
		}
		
	}
	
	

	/**
	 * 扫码模式2 https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_5
	 * 服务商模式下扫码支付：https://pay.weixin.qq.com/wiki/doc/api/native_sl.php?chapter=6_5&index=4
	 * @param appid
	 * @param sub_appid
	 * @param mch_id
	 * @param sub_mch_id
	 * @param device_info
	 * @param body
	 * @param detail
	 * @param attach
	 * @param out_trade_no
	 * @param total_fee
	 * @param spbill_create_ip
	 * @param notify_url
	 * @param trade_type
	 * @param paternerKey
	 * @return
	 */
	public static AjaxResult scanModeTwoPay(String appid, String sub_appid, String mch_id, String sub_mch_id, String device_info, String body, String detail, String attach, String out_trade_no, String total_fee, String spbill_create_ip, String notify_url, String trade_type, String paternerKey){
		AjaxResult ajax = new AjaxResult();
		Map<String, String> params = PayApi.buildUnifiedOrderParasMap(appid, sub_appid, mch_id, sub_mch_id, device_info, body, detail, attach, out_trade_no, total_fee, spbill_create_ip, notify_url, trade_type, paternerKey,null);
		String xmlResult = PayApi.pushOrder(params);
		if (PropKit.getBoolean("devMode", false)) {
			System.out.println(xmlResult);
		}
		Map<String, String> map = PaymentKit.xmlToMap(xmlResult);
		String return_code = map.get("return_code");
		String return_msg = map.get("return_msg");
		if (StrKit.isBlank(return_code) || !"SUCCESS".equals(return_code)) {
			ajax.addError(return_msg);
			return ajax;
		}
		String result_code = map.get("result_code");
		if (StrKit.isBlank(result_code) || !"SUCCESS".equals(result_code)) {
			ajax.addError(return_msg);
			return ajax;
		}
		String qrCodeUrl = map.get("code_url");
		ajax.success(qrCodeUrl);
		return ajax;
	}
	
	private static String replace(String str,String regex,String... args){
		int length = args.length;
		for (int i = 0; i < length; i++) {
			str=str.replaceFirst(regex, args[i]);
		}
		return str;
	}
	
}
