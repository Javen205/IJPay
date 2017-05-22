package com.javen.jpay.weixin.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.javen.jpay.weixin.api.WxPayApi.TradeType;
import com.javen.jpay.weixin.utils.PaymentKit;
import com.jfinal.kit.StrKit;
/**
 * @Email javen205@126.com
 * @author Javen
 */
public class WxPayApiConfig implements Serializable {

	private static final long serialVersionUID = -6447075676732210047L;
	
	private String appId;
	private String mchId;
	private String subAppId;
	private String subMchId;
	private String paternerKey;
	private String nonceStr;
	private String body;
	private String attach;
	private String outTradeNo;
	private String totalFee;
	private String spbillCreateIp;
	private String notifyUrl;
	private TradeType tradeType;
	/**
	 * openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid
	 */
	private String openId;
	private String subOpenId;
	
	
	private WxPayApiConfig() {
	}

	public static WxPayApiConfig New() {
		return new WxPayApiConfig();
	}
	
	public Map<String, String> build(){
		Map<String, String> map =new HashMap<String, String>();
		if (StrKit.isBlank(appId)) {
			throw new IllegalStateException("appId 未被赋值");
		}
		if (StrKit.isBlank(mchId)) {
			throw new IllegalStateException("mchId 未被赋值");
		}
		if (StrKit.isBlank(nonceStr)) {
			nonceStr = String.valueOf(System.currentTimeMillis());
		}
		if (StrKit.isBlank(paternerKey)) {
			nonceStr = String.valueOf(System.currentTimeMillis());
		}
		if (StrKit.isBlank(body)) {
			throw new IllegalStateException("body 未被赋值");
		}
		if (StrKit.isBlank(outTradeNo)) {
			outTradeNo = String.valueOf(System.currentTimeMillis());
		}
		if (StrKit.isBlank(totalFee)) {
			throw new IllegalStateException("totalFee 未被赋值");
		}
		if (StrKit.isBlank(spbillCreateIp)) {
			throw new IllegalStateException("spbillCreateIp 未被赋值");
		}
		
		if (StrKit.isBlank(tradeType.name())) {
			throw new IllegalStateException("tradeType 未被赋值");
		}
		
		if (StrKit.isBlank(tradeType.name()) && tradeType.equals(TradeType.JSAPI)) {
			
		}
		
		if (StrKit.notBlank(subAppId)) {
			map.put("sub_appid", subAppId);
			if (StrKit.isBlank(subOpenId)) {
				throw new IllegalStateException("subAppId赋值了  subOpenId 未被赋值");
			}else {
				map.put("sub_openid", subOpenId);
			}
		}
		
		
		map.put("appid", appId);
		map.put("mch_id", mchId);
		map.put("appid", appId);
		map.put("appid", appId);
		map.put("appid", appId);
		
		
		map.put("sign", PaymentKit.createSign(map, paternerKey));
		return map;
	}

	public String getAppId() {
		return appId;
	}

	public WxPayApiConfig setAppId(String appId) {
		this.appId = appId;
		return this;
	}

	public String getMchId() {
		return mchId;
	}

	public WxPayApiConfig setMchId(String mchId) {
		this.mchId = mchId;
		return this;
	}

	public String getSubAppId() {
		return subAppId;
	}

	public WxPayApiConfig setSubAppId(String subAppId) {
		this.subAppId = subAppId;
		return this;
	}

	public String getSubMchId() {
		return subMchId;
	}

	public WxPayApiConfig setSubMchId(String subMchId) {
		this.subMchId = subMchId;
		return this;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public WxPayApiConfig setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
		return this;
	}

	public String getBody() {
		return body;
	}

	public WxPayApiConfig setBody(String body) {
		this.body = body;
		return this;
	}

	public String getAttach() {
		return attach;
	}

	public WxPayApiConfig setAttach(String attach) {
		this.attach = attach;
		return this;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public WxPayApiConfig setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
		return this;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public WxPayApiConfig setTotalFee(String totalFee) {
		this.totalFee = totalFee;
		return this;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public WxPayApiConfig setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
		return this;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public WxPayApiConfig setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
		return this;
	}

	public TradeType getTradeType() {
		return tradeType;
	}

	public WxPayApiConfig setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
		return this;
	}

	public String getOpenId() {
		return openId;
	}

	public WxPayApiConfig setOpenId(String openId) {
		this.openId = openId;
		return this;
	}

	public String getSubOpenId() {
		return subOpenId;
	}

	public WxPayApiConfig setSubOpenId(String subOpenId) {
		this.subOpenId = subOpenId;
		return this;
	}

	public String getPaternerKey() {
		return paternerKey;
	}

	public WxPayApiConfig setPaternerKey(String paternerKey) {
		this.paternerKey = paternerKey;
		return this;
	}
	
	
}
