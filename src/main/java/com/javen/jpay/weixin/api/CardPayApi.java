package com.javen.jpay.weixin.api;
import java.util.HashMap;
import java.util.Map;
import com.jfinal.weixin.sdk.kit.PaymentKit;
/**
 * @author Javen
 * 2017年4月15日
 */
public class CardPayApi {
	private final static String MICROPAY_URL =  "https://api.mch.weixin.qq.com/pay/micropay";
	
	/**
	 * 刷卡支付
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_10&index=1
	 * 商户模式接入文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_10&index=1
	 *
	 * @param params 请求参数
	 * @return
	 */
	public static String micropay(Map<String, String> params){
		return PayApi.doPost(MICROPAY_URL, params);
	}
	
	/**
	 * 构建参数
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
	 * @param auth_code
	 * @param paternerKey 
	 * @return
	 */
	public static Map<String, String> buildParasMap(String appid, String sub_appid, String mch_id, String sub_mch_id, String device_info, String body, String detail, String attach, String out_trade_no, String total_fee, String spbill_create_ip, String auth_code, String paternerKey){
		Map<String, String> queryParas =  new HashMap<String, String>();
		queryParas.put("appid", appid);
		queryParas.put("sub_appid", sub_appid);
		queryParas.put("mch_id", mch_id);
		queryParas.put("sub_mch_id", sub_mch_id);
		queryParas.put("device_info", device_info);
		queryParas.put("nonce_str", String.valueOf(System.currentTimeMillis()));
		queryParas.put("body", body);
		queryParas.put("detail", detail);
		queryParas.put("attach", attach);
		queryParas.put("out_trade_no", out_trade_no);
		queryParas.put("total_fee", total_fee);
		queryParas.put("spbill_create_ip", spbill_create_ip);
		queryParas.put("auth_code", auth_code);
		String sign = PaymentKit.createSign(queryParas, paternerKey);
		queryParas.put("sign", sign);
		return queryParas;
	}
	
}
