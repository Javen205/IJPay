package com.jpay.ext.kit;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.jpay.util.Charsets;
import com.jpay.util.XmlHelper;
import com.jpay.weixin.api.WxPayApiConfigKit;

/**
 * 微信支付的统一下单工具类
 */
public class PaymentKit {
	
	/**
	 * 构建短链接参数
	 * @param appid
	 * @param sub_appid
	 * @param mch_id
	 * @param sub_mch_id
	 * @param long_url
	 * @param paternerKey
	 * @return <Map<String, String>>
	 */
	public static Map<String, String> buildShortUrlParasMap(String appid, String sub_appid, String mch_id, String sub_mch_id, String long_url, String paternerKey){
		Map<String, String> params =  new HashMap<String, String>();
		params.put("appid", appid);
		params.put("sub_appid", sub_appid);
		params.put("mch_id", mch_id);
		params.put("sub_mch_id", sub_mch_id);
		params.put("long_url", long_url);
		
		return buildSignAfterParasMap(params, paternerKey);
		
	}
	
	

	/**
	 * 组装签名的字段
	 * 
	 * @param params
	 *            参数
	 * @param urlEncoder
	 *            是否urlEncoder
	 * @return {String}
	 */
	public static String packageSign(Map<String, String> params, boolean urlEncoder) {
		// 先将参数以其参数名的字典序升序进行排序
		TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Entry<String, String> param : sortedParams.entrySet()) {
			String value = param.getValue();
			if (StrKit.isBlank(value)) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				sb.append("&");
			}
			sb.append(param.getKey()).append("=");
			if (urlEncoder) {
				try {
					value = urlEncode(value);
				} catch (UnsupportedEncodingException e) {
				}
			}
			sb.append(value);
		}
		return sb.toString();
	}

	/**
	 * urlEncode
	 * 
	 * @param src
	 *            微信参数
	 * @return String
	 * @throws UnsupportedEncodingException
	 *             编码错误
	 */
	public static String urlEncode(String src) throws UnsupportedEncodingException {
		return URLEncoder.encode(src, Charsets.UTF_8.name()).replace("+", "%20");
	}
	/**
	 * 构建签名之后的参数
	 * @param params
	 * @param paternerKey
	 * @return <Map<String, String>>
	 */
	public static Map<String, String> buildSignAfterParasMap(Map<String, String> params, String paternerKey) {
		params.put("nonce_str", String.valueOf(System.currentTimeMillis()));
		String sign = PaymentKit.createSign(params, paternerKey);
		params.put("sign", sign);
		return params;
	}

	/**
	 * 生成签名
	 * 
	 * @param params
	 *            参数
	 * @param partnerKey
	 *            支付密钥
	 * @return {String}
	 * 
	 */
	public static String createSign(Map<String, String> params, String partnerKey) {
		// 生成签名前先去除sign
		params.remove("sign");
		String stringA = packageSign(params, false);
		String stringSignTemp = stringA + "&key=" + partnerKey;
		return HashKit.md5(stringSignTemp).toUpperCase();
	}

	/**
	 * 支付异步通知时校验sign
	 * 
	 * @param params
	 *            参数
	 * @param paternerKey
	 *            支付密钥
	 * @return {boolean}
	 */
	public static boolean verifyNotify(Map<String, String> params, String paternerKey) {
		String sign = params.get("sign");
		String localSign = PaymentKit.createSign(params, paternerKey);
		return sign.equals(localSign);
	}
	/**
	 * 预付订单再次签名
	 * @param prepay_id
	 * @return <Map<String, String>>
	 */
	public static Map<String, String> prepayIdCreateSign(String prepay_id) {
		Map<String, String> packageParams = new HashMap<String, String>();
		packageParams.put("appId", WxPayApiConfigKit.getWxPayApiConfig().getAppId());
		packageParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
		packageParams.put("nonceStr", String.valueOf(System.currentTimeMillis()));
		packageParams.put("package", "prepay_id=" + prepay_id);
		packageParams.put("signType", "MD5");
		String packageSign = PaymentKit.createSign(packageParams, WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey());
		packageParams.put("paySign", packageSign);
		return packageParams;
	}
	
	
	/**
	 * 判断接口返回的code是否是SUCCESS
	 * @param return_code
	 * @return {boolean}
	 */
	public static boolean codeIsOK(String return_code) {
		return StrKit.notBlank(return_code) && "SUCCESS".equals(return_code);
	}
	

	/**
	 * 微信下单map to xml
	 * 
	 * @param params
	 *            参数
	 * @return {String}
	 */
	public static String toXml(Map<String, String> params) {
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>");
		for (Entry<String, String> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			// 略过空值
			if (StrKit.isBlank(value))
				continue;
			xml.append("<").append(key).append(">");
			xml.append(entry.getValue());
			xml.append("</").append(key).append(">");
		}
		xml.append("</xml>");
		return xml.toString();
	}

	/**
	 * 针对支付的xml，没有嵌套节点的简单处理
	 * 
	 * @param xmlStr
	 *            xml字符串
	 * @return <Map<String, String>>
	 */
	public static Map<String, String> xmlToMap(String xmlStr) {
		XmlHelper xmlHelper = XmlHelper.of(xmlStr);
		return xmlHelper.toMap();
	}
	/**
	 * 替换url中的参数
	 * @param str
	 * @param regex
	 * @param args
	 * @return {String}
	 */
	public static String replace(String str,String regex,String... args){
		int length = args.length;
		for (int i = 0; i < length; i++) {
			str=str.replaceFirst(regex, args[i]);
		}
		return str;
	}

}