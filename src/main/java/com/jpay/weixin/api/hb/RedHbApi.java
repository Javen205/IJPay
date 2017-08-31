package com.jpay.weixin.api.hb;


import java.util.Map;

import com.jpay.util.HttpUtils;
import com.jpay.ext.kit.PaymentKit;

/**
 * 微信红包
 * 1、服务商版必须传sub_mch_id、msgappid
 * 2、红包金额大于200时scene_id必传
 * 3、查询红包接口参数一样的
 * @author Javen
 */
public class RedHbApi {
	
	/**
	 * 商户版文档地址：https://pay.weixin.qq.com/wiki/doc/api/tools/cash_coupon.php?chapter=13_4&index=3
	 * 服务商版文档地址：https://pay.weixin.qq.com/wiki/doc/api/tools/cash_coupon_sl.php?chapter=13_4&index=3
	 */
	private static String sendRedPackUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
	private static String sendGroupRedPackUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendgroupredpack";
	private static String getHbInfo = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo ";
			
	/**
	 * 发送红包
	 * @param params 请求参数
	 * @param certPath 证书文件目录
	 * @param partnerKey 证书密码
	 * @return {String}
	 */
	public static String sendRedPack(Map<String, String> params, String certPath, String partnerKey) {
		return HttpUtils.postSSL(sendRedPackUrl, PaymentKit.toXml(params), certPath, partnerKey);
	}
	
	
	/**
	 * 根据商户订单号查询信息
	 * @param params 请求参数
	 * @param certPath 证书文件目录
	 * @param certPassword 证书密码
	 * @return {String}
	 */
	public static String getHbInfo(Map<String, String> params, String certPath, String certPassword) {
		return HttpUtils.postSSL(getHbInfo, PaymentKit.toXml(params), certPath, certPassword);
	}
	
	/**
	 * 发送裂变红包
	 * @param params 请求参数
	 * @param certPath 证书文件目录
	 * @param certPassword 证书密码
	 * @return {String}
	 */
	public static String sendGroupRedPack(Map<String, String> params, String certPath, String certPassword) {
		return HttpUtils.postSSL(sendGroupRedPackUrl, PaymentKit.toXml(params), certPath, certPassword);
	}
}
