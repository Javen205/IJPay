package com.jpay.oppo;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jpay.ext.kit.HttpKit;
import com.jpay.ext.kit.PaymentKit;
import com.jpay.secure.RSAUtils;

/**
 * @author Javen 2019年1月12日
 * 
 *         OPPO 小游戏支付
 *         https://cdofs.oppomobile.com/cdo-activity/static/201810/26/quickgame/documentation/pay/pay-introduction.html
 */
public class OppoPayApi {
	// 统一下单接口
	private static final String PERORDER = "https://jits.open.oppomobile.com/jitsopen/api/pay/v1.0/preOrder";

	/**
	 * 统一下单接口
	 * 
	 * @param params
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String preOrder(Map<String, String> params, String privateKey) throws Exception {
		// 生成签名前先去除sign
		params.remove("sign");
		params.put("sign", RSAUtils.encryptByPrivateKey(PaymentKit.packageSign(params, false), privateKey));
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		return HttpKit.post(PERORDER, JSON.toJSONString(params), headers);
	}
}
