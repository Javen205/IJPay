package com.javen.jpay.weixin.api;

import java.util.HashMap;
import java.util.Map;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.HttpUtils;
/**
 * 
 * @author Javen
 * 2017年4月15日
 */
public class PayApi {
	//统一下单接口
	private static final String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	//订单查询
	private static final String ORDERQUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	//关闭订单
	private static final String CLOSEORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	//撤销订单
	private static final String REVERSE_URL = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
	//申请退款
	private static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	//查询退款
	private static final String REFUNDQUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	//下载对账单
	private static final String DOWNLOADBILLY_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	//交易保障
	private static final String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";
	//转换短链接
	private static final String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
	//授权码查询openId接口
	private static final String AUTHCODETOOPENID_URL = "https://api.mch.weixin.qq.com/tools/authcodetoopenid";
	
	
	private PayApi() {}
	/**
	 * 交易类型枚举
	 * @author Javen
	 * 2017年4月15日
	 */
	public static enum TradeType {
		JSAPI, NATIVE, APP, WAP
	}
	
	/**
	 * 统一下单
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/native_sl.php?chapter=9_1
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_1
	 * @param params
	 * @return
	 */
	public static String pushOrder(Map<String, String> params){
		return doPost(UNIFIEDORDER_URL, params);
	}
	
	/**
	 * 订单查询
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_2
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_2
	 * @param params 请求参数
	 * @return
	 */
	public static String orderQuery(Map<String, String> params){
		return doPost(ORDERQUERY_URL, params);
	}
	/**
	 * 关闭订单
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/jsapi_sl.php?chapter=9_3
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_3
	 * @param params
	 * @return
	 */
	public static String closeOrder(Map<String, String> params){
		return doPost(CLOSEORDER_URL, params);
	}
	
	/**
	 * 撤销订单
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_11&index=3
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_11&index=3
	 * @param params 请求参数
	 * @param certPath 证书文件目录
	 * @param certPass 证书密码
	 * @return
	 */
	public static String orderReverse(Map<String, String> params, String certPath, String certPass){
		return doPostSSL(REVERSE_URL,params , certPath, certPass);
	}
	/**
	 * 申请退款
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_4
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_4
	 * @param params 请求参数
	 * @param certPath 证书文件目录
	 * @param certPass 证书密码
	 * @return
	 */
	public static String orderRefund(Map<String, String> params, String certPath, String certPass){
		return doPostSSL(REFUND_URL,params , certPath, certPass);
	}
	/**
	 * 查询退款
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_5
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_5
	 * @param params 请求参数
	 * @return
	 */
	public static String orderRefundQuery(Map<String, String> params){
		return doPost(REFUNDQUERY_URL,params);
	}
	/**
	 * 下载对账单
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_6
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_6
	 * @param params 请求参数
	 * @return
	 */
	public static String downloadBill(Map<String, String> params){
		return doPost(DOWNLOADBILLY_URL,params);
	}
	
	/**
	 * 交易保障
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_14&index=7
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_14&index=7
	 * @param params 请求参数
	 * @return
	 */
	public static String orderReport(Map<String, String> params){
		return doPost(REPORT_URL,params);
	}
	/**
	 * 转换短链接
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_9&index=8
	 * 商户模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_9&index=8
	 * @param params 请求参数
	 * @return
	 */
	public static String toShortUrl(Map<String, String> params){
		return doPost(SHORT_URL,params);
	}
	/**
	 * 授权码查询openId
	 * 服务商模式接入文档:https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=9_12&index=9
	 * 商户模式接入文档: https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_13&index=9
	 * @param params 请求参数
	 * @return
	 */
	public static String authCodeToOpenid(Map<String, String> params){
		return doPost(AUTHCODETOOPENID_URL,params);
	}
	
	public static String doPost(String url, Map<String, String> params){
		return HttpUtils.post(url, PaymentKit.toXml(params));
	}
	
	public static String doPostSSL(String url, Map<String, String> params, String certPath, String certPass ){
		return HttpUtils.postSSL(url, PaymentKit.toXml(params), certPath, certPass);
	}
	
	public static Map<String, String> buildParasMap(Map<String, String> params, String paternerKey) {
		params.put("nonce_str", String.valueOf(System.currentTimeMillis()));
		String sign = PaymentKit.createSign(params, paternerKey);
		params.put("sign", sign);
		return params;
	}
	
	/**
	 * 封装查询请求参数 参考代码
	 * @param appid
	 * @param sub_appid
	 * @param mch_id
	 * @param sub_mch_id
	 * @param transaction_id
	 * @param out_trade_no
	 * @param paternerKey
	 * @return
	 */
	public static Map<String, String> buildParasMap(String appid, String sub_appid, String mch_id, String sub_mch_id, String transaction_id, String out_trade_no, String paternerKey){
		Map<String, String> params =  new HashMap<String, String>();
		
		params.put("appid", appid);
		params.put("sub_appid", sub_appid);
		params.put("mch_id", mch_id);
		params.put("sub_mch_id", sub_mch_id);
		params.put("transaction_id", transaction_id);
		params.put("out_trade_no", out_trade_no);
		
		return buildParasMap(params, paternerKey);
	}
	
	/**
	 * 构建统一下单参数
	 * @param appid
	 * @param sub_appid 否
	 * @param mch_id
	 * @param sub_mch_id 服务商模式下必须
	 * @param device_info 否
	 * @param body
	 * @param detail 否
	 * @param attach 否
	 * @param out_trade_no
	 * @param total_fee
	 * @param spbill_create_ip
	 * @param paternerKey
	 * @param notify_url
	 * @param trade_type
	 * @param product_id  扫码支付必传
	 * @return
	 */
	public static Map<String, String> buildUnifiedOrderParasMap(String appid, String sub_appid, String mch_id, String sub_mch_id, String device_info, String body, String detail, String attach, String out_trade_no, String total_fee, String spbill_create_ip, String notify_url, String trade_type, String paternerKey, String product_id){
		Map<String, String> params =  new HashMap<String, String>();
		params.put("appid", appid);
		params.put("sub_appid", sub_appid);
		params.put("mch_id", mch_id);
		params.put("sub_mch_id", sub_mch_id);
		params.put("device_info", device_info);
		params.put("body", body);
		params.put("detail", detail);
		params.put("attach", attach);
		
		params.put("total_fee", total_fee);
		params.put("spbill_create_ip", spbill_create_ip);
		params.put("notify_url", notify_url);
		params.put("trade_type", trade_type);
		params.put("product_id", product_id);
		
		return buildParasMap(params, paternerKey);
	}
	/**
	 * 构建短链接参数
	 * @param appid
	 * @param sub_appid
	 * @param mch_id
	 * @param sub_mch_id
	 * @param long_url
	 * @param paternerKey
	 * @return
	 */
	public static Map<String, String> buildShortUrlParasMap(String appid, String sub_appid, String mch_id, String sub_mch_id, String long_url, String paternerKey){
		Map<String, String> params =  new HashMap<String, String>();
		params.put("appid", appid);
		params.put("sub_appid", sub_appid);
		params.put("mch_id", mch_id);
		params.put("sub_mch_id", sub_mch_id);
		params.put("long_url", long_url);
		
		return buildParasMap(params, paternerKey);
		
	}
	
	
}
