package com.jpay.alipay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jpay.ext.kit.HashKit;

public class AlipayCore {
	
	/**
	 * 生成签名结果
	 * @param params 要签名的数组
	 * @param key
	 * @param signType
	 * @return 签名结果字符串
	 */
	public static String buildRequestMysign(Map<String, String> params,String key,String signType) {
		String prestr = createLinkString(params); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        if(signType.equals("MD5") ) {
        		return  HashKit.md5(prestr.concat(key));
        }
        return null;
    }
	
    /**
     * 生成要请求给支付宝的参数数组
     * @param params 请求前的参数数组
     * @param key 商户的私钥
     * @param signType 签名类型
     * @return 要请求的参数数组
     */
    public static Map<String, String> buildRequestPara(Map<String, String> params,String key,String signType) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = paraFilter(params);
        //生成签名结果
        String mysign = buildRequestMysign(params,key,signType);

        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", signType);

        return sPara;
    }

	/**
	 * 除去数组中的空值和签名参数
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {
		Map<String, String> result = new HashMap<String, String>();
		if (sArray == null || sArray.size() <= 0) {
			return result;
		}
		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
					|| key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				content.append(key + "=" + value);
			} else {
				content.append(key + "=" + value + "&");
			}
		}
		return content.toString();
	}
}
