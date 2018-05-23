package com.jpay.weixin.api;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.jpay.ext.kit.PaymentKit;
import com.jpay.ext.kit.StrKit;
import com.jpay.weixin.api.WxPayApi.TradeType;

/**
 * @author Javen 2017年5月22日
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
	private String transactionId;
	private String outTradeNo;
	private String totalFee;
	private String spbillCreateIp;
	private String notifyUrl;
	private TradeType tradeType;
	private String openId;
	private String subOpenId;
	private String authCode;
	private String sceneInfo;

	private String planId;
	private String contractCode;
	private String requestSerial;
	private String contractDisplayAccount;
	private String version;
	private String timestamp;
	private String returnApp;
	private String returnWeb;

	private String contractNotifyUrl;
	private String contractId;

	private PayModel payModel;

	/**
	 * 分别对应商户模式、服务商模式
	 */
	public static enum PayModel {
		BUSINESSMODEL, SERVICEMODE
	}

	private WxPayApiConfig() {
	}

	public static WxPayApiConfig New() {
		return new WxPayApiConfig();
	}

	/**
	 * 构建请求参数
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> build() {
		Map<String, String> map = new HashMap<String, String>();

		if (getPayModel().equals(PayModel.SERVICEMODE)) {
			map.put("sub_mch_id", getSubMchId());
			if (StrKit.notBlank(getSubAppId())) {
				map.put("sub_appid", subAppId);
			}
		}

		/**
		 * openId和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid
		 */
		if (getTradeType().equals(TradeType.JSAPI)) {
			if (StrKit.notBlank(getSubAppId())) {
				map.put("sub_appid", subAppId);
				map.put("sub_openid", getSubOpenId());
			} else {
				map.put("openid", getOpenId());
			}
		}
		/**
		 * H5支付必填scene_info
		 */
		if (getTradeType().equals(TradeType.MWEB)) {
			if (StrKit.isBlank(getSceneInfo())) {
				throw new IllegalArgumentException("微信H5支付 scene_info 不能同时为空");
			}
			map.put("scene_info", getSceneInfo());
		}

		map.put("appid", getAppId());
		map.put("mch_id", getMchId());
		map.put("nonce_str", getNonceStr());
		map.put("body", getBody());
		map.put("out_trade_no", getOutTradeNo());
		map.put("total_fee", getTotalFee());
		map.put("spbill_create_ip", getSpbillCreateIp());

		map.put("trade_type", getTradeType().name());

		map.put("attach", getAttach());
		if (getTradeType().equals(TradeType.MICROPAY)) {
			map.put("auth_code", getAuthCode());
			map.remove("trade_type");
		} else {
			map.put("notify_url", getNotifyUrl());
		}

		map.put("sign", PaymentKit.createSign(map, getPaternerKey()));

		return map;
	}

	/**
	 * 构建查询订单参数
	 * 
	 * @return <Map<String, String>>
	 */
	public Map<String, String> orderQueryBuild() {
		Map<String, String> map = new HashMap<String, String>();
		if (getPayModel().equals(PayModel.SERVICEMODE)) {
			map.put("sub_mch_id", getSubMchId());
			map.put("sub_appid", getSubAppId());
		}

		map.put("appid", getAppId());
		map.put("mch_id", getMchId());

		if (StrKit.notBlank(getTransactionId())) {
			map.put("transaction_id", getTransactionId());
		} else {
			if (StrKit.isBlank(getOutTradeNo())) {
				throw new IllegalArgumentException("out_trade_no,transaction_id 不能同时为空");
			}
			map.put("out_trade_no", getOutTradeNo());
		}
		map.put("nonce_str", String.valueOf(System.currentTimeMillis()));
		map.put("sign", PaymentKit.createSign(map, getPaternerKey()));
		return map;
	}

	/**
	 * 构建申请签约Map
	 * 
	 * @return 申请签约Map
	 * @throws UnsupportedEncodingException
	 */
	public Map<String, String> entrustwebBuild() throws UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", getAppId());
		map.put("mch_id", getMchId());
		map.put("plan_id", getPlanId());
		map.put("contract_code", getContractCode());
		map.put("request_serial", getRequestSerial());
		map.put("contract_display_account", getContractDisplayAccount());
		map.put("notify_url", getNotifyUrl());
		map.put("version", getVersion());
		map.put("timestamp", getTimestamp());
		map.put("sign", PaymentKit.createSign(map, getPaternerKey()));

		for (Entry<String, String> param : map.entrySet()) {
			String key = param.getKey();
			String value = param.getValue();
			value = PaymentKit.urlEncode(value);
			map.put(key, value);
		}

		return map;
	}

	/**
	 * 构建支付中签约Map
	 * 
	 * @return 支付中签约Map
	 */
	public Map<String, String> contractorderBuild() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("appid", getAppId());
		map.put("mch_id", getMchId());
		map.put("contract_appid", getAppId());
		map.put("contract_mchid", getMchId());
		map.put("out_trade_no", getOutTradeNo());
		map.put("nonce_str", getNonceStr());
		map.put("body", getBody());
		map.put("attach", getAttach());
		map.put("notify_url", getNotifyUrl());
		map.put("total_fee", getTotalFee());
		map.put("spbill_create_ip", getSpbillCreateIp());
		map.put("trade_type", getTradeType().name());
		if (getTradeType().equals(TradeType.JSAPI)) {
			map.put("openid", getOpenId());
		}
		map.put("plan_id", getPlanId());
		map.put("contract_code", getContractCode());
		map.put("request_serial", getRequestSerial());
		map.put("contract_display_account", getContractDisplayAccount());
		map.put("contract_notify_url", getContractNotifyUrl());

		map.put("sign", PaymentKit.createSign(map, getPaternerKey()));

		return map;
	}

	/**
	 * 构建查询签约关系的Map
	 * 
	 * @return 查询签约关系的Map
	 */
	public Map<String, String> querycontractBuild() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("appid", getAppId());
		map.put("mch_id", getMchId());

		if (StrKit.notBlank(getPlanId())) {
			map.put("plan_id", getPlanId());
			map.put("contract_code", getContractCode());
		} else {
			map.put("contract_id", getContractId());
		}
		map.put("version", getVersion());
		map.put("sign", PaymentKit.createSign(map, getPaternerKey()));

		return map;
	}

	/**
	 * 构建申请扣款的Map
	 * 
	 * @return 申请扣款的Map
	 */
	public Map<String, String> pappayapplyBuild() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", getAppId());
		map.put("mch_id", getMchId());
		map.put("nonce_str", getNonceStr());
		map.put("body", getBody());
		map.put("attach", getAttach());
		map.put("out_trade_no", getOutTradeNo());
		map.put("total_fee", getTotalFee());
		map.put("spbill_create_ip", getSpbillCreateIp());
		map.put("notify_url", getNotifyUrl());
		map.put("trade_type", getTradeType().name());
		map.put("contract_id", getContractId());
		map.put("sign", PaymentKit.createSign(map, getPaternerKey()));
		return map;
	}

	public String getAppId() {
		if (StrKit.isBlank(appId))
			throw new IllegalArgumentException("appId 未被赋值");
		return appId;
	}

	public WxPayApiConfig setAppId(String appId) {
		if (StrKit.isBlank(appId))
			throw new IllegalArgumentException("appId 值不能为空");
		this.appId = appId;
		return this;
	}

	public String getMchId() {
		if (StrKit.isBlank(mchId))
			throw new IllegalArgumentException("mchId 未被赋值");
		return mchId;
	}

	public WxPayApiConfig setMchId(String mchId) {
		if (StrKit.isBlank(mchId))
			throw new IllegalArgumentException("mchId 值不能为空");
		this.mchId = mchId;
		return this;
	}

	public String getSubAppId() {
		return subAppId;
	}

	public WxPayApiConfig setSubAppId(String subAppId) {
		if (StrKit.isBlank(subAppId))
			throw new IllegalArgumentException("subAppId 值不能为空");
		this.subAppId = subAppId;
		return this;
	}

	public String getSubMchId() {
		if (StrKit.isBlank(subMchId))
			throw new IllegalArgumentException("subMchId 未被赋值");
		return subMchId;
	}

	public WxPayApiConfig setSubMchId(String subMchId) {
		if (StrKit.isBlank(subMchId))
			throw new IllegalArgumentException("subMchId 值不能为空");
		this.subMchId = subMchId;
		return this;
	}

	public String getNonceStr() {
		if (StrKit.isBlank(nonceStr))
			nonceStr = String.valueOf(System.currentTimeMillis());
		return nonceStr;
	}

	public WxPayApiConfig setNonceStr(String nonceStr) {
		if (StrKit.isBlank(nonceStr))
			throw new IllegalArgumentException("nonceStr 值不能为空");
		this.nonceStr = nonceStr;
		return this;
	}

	public String getBody() {
		if (StrKit.isBlank(body))
			throw new IllegalArgumentException("body 未被赋值");
		return body;
	}

	public WxPayApiConfig setBody(String body) {
		if (StrKit.isBlank(body))
			throw new IllegalArgumentException("body 值不能为空");
		this.body = body;
		return this;
	}

	public String getAttach() {
		return attach;
	}

	public WxPayApiConfig setAttach(String attach) {
		if (StrKit.isBlank(attach))
			throw new IllegalArgumentException("attach 值不能为空");
		this.attach = attach;
		return this;
	}

	public String getOutTradeNo() {
		if (StrKit.isBlank(outTradeNo))
			throw new IllegalArgumentException("outTradeNo 未被赋值");
		return outTradeNo;
	}

	public WxPayApiConfig setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
		return this;
	}

	public String getTotalFee() {
		if (StrKit.isBlank(totalFee))
			throw new IllegalArgumentException("totalFee 未被赋值");
		return totalFee;
	}

	public WxPayApiConfig setTotalFee(String totalFee) {
		if (StrKit.isBlank(totalFee))
			throw new IllegalArgumentException("totalFee 值不能为空");
		this.totalFee = totalFee;
		return this;
	}

	public String getSpbillCreateIp() {
		if (StrKit.isBlank(spbillCreateIp))
			throw new IllegalArgumentException("spbillCreateIp 未被赋值");
		return spbillCreateIp;
	}

	public WxPayApiConfig setSpbillCreateIp(String spbillCreateIp) {
		if (StrKit.isBlank(spbillCreateIp))
			throw new IllegalArgumentException("spbillCreateIp 值不能为空");
		this.spbillCreateIp = spbillCreateIp;
		return this;
	}

	public String getNotifyUrl() {
		if (StrKit.isBlank(notifyUrl))
			throw new IllegalArgumentException("notifyUrl 未被赋值");
		return notifyUrl;
	}

	public WxPayApiConfig setNotifyUrl(String notifyUrl) {
		if (StrKit.isBlank(notifyUrl))
			throw new IllegalArgumentException("notifyUrl 值不能为空");
		this.notifyUrl = notifyUrl;
		return this;
	}

	public TradeType getTradeType() {
		if (tradeType == null)
			throw new IllegalArgumentException("tradeType 未被赋值");
		return tradeType;
	}

	public WxPayApiConfig setTradeType(TradeType tradeType) {
		if (tradeType == null)
			throw new IllegalArgumentException("mchId 值不能为空");
		this.tradeType = tradeType;
		return this;
	}

	public String getOpenId() {
		if (StrKit.isBlank(openId))
			throw new IllegalArgumentException("openId 未被赋值");
		return openId;
	}

	public WxPayApiConfig setOpenId(String openId) {
		if (StrKit.isBlank(openId))
			throw new IllegalArgumentException("openId 值不能为空");
		this.openId = openId;
		return this;
	}

	public String getSubOpenId() {
		if (StrKit.isBlank(subOpenId))
			throw new IllegalArgumentException("subOpenId 未被赋值");
		return subOpenId;
	}

	public WxPayApiConfig setSubOpenId(String subOpenId) {
		if (StrKit.isBlank(subOpenId))
			throw new IllegalArgumentException("subOpenId 值不能为空");
		this.subOpenId = subOpenId;
		return this;
	}

	public String getPaternerKey() {
		if (StrKit.isBlank(paternerKey))
			throw new IllegalArgumentException("paternerKey 未被赋值");
		return paternerKey;
	}

	public WxPayApiConfig setPaternerKey(String paternerKey) {
		if (StrKit.isBlank(paternerKey))
			throw new IllegalArgumentException("paternerKey 值不能为空");
		this.paternerKey = paternerKey;
		return this;
	}

	public PayModel getPayModel() {
		if (payModel == null)
			payModel = PayModel.BUSINESSMODEL;
		return payModel;
	}

	public WxPayApiConfig setPayModel(PayModel payModel) {
		if (payModel == null)
			payModel = PayModel.BUSINESSMODEL;
		this.payModel = payModel;
		return this;
	}

	public String getAuthCode() {
		if (StrKit.isBlank(authCode))
			throw new IllegalArgumentException("authCode 未被赋值");
		return authCode;
	}

	public WxPayApiConfig setAuthCode(String authCode) {
		if (StrKit.isBlank(paternerKey))
			throw new IllegalArgumentException("authCode 值不能为空");
		this.authCode = authCode;
		return this;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public WxPayApiConfig setTransactionId(String transactionId) {
		if (StrKit.isBlank(transactionId))
			throw new IllegalArgumentException("transactionId 值不能为空");
		this.transactionId = transactionId;
		return this;
	}

	public String getSceneInfo() {
		return sceneInfo;
	}

	public WxPayApiConfig setSceneInfo(String sceneInfo) {
		this.sceneInfo = sceneInfo;
		return this;
	}

	public String getPlanId() {
		if (StrKit.isBlank(planId))
			throw new IllegalArgumentException("planId 未被赋值");
		return planId;
	}

	public WxPayApiConfig setPlanId(String planId) {
		if (StrKit.isBlank(planId))
			throw new IllegalArgumentException("planId 值不能为空");
		this.planId = planId;
		return this;
	}

	public String getContractCode() {
		if (StrKit.isBlank(contractCode))
			throw new IllegalArgumentException("contractCode 未被赋值");
		return contractCode;
	}

	public WxPayApiConfig setContractCode(String contractCode) {
		if (StrKit.isBlank(contractCode))
			throw new IllegalArgumentException("contractCode 值不能为空");
		this.contractCode = contractCode;
		return this;
	}

	public String getRequestSerial() {
		if (StrKit.isBlank(requestSerial))
			throw new IllegalArgumentException("requestSerial 未被赋值");
		return requestSerial;
	}

	public WxPayApiConfig setRequestSerial(String requestSerial) {
		if (StrKit.isBlank(requestSerial))
			throw new IllegalArgumentException("requestSerial 值不能为空");
		this.requestSerial = requestSerial;
		return this;
	}

	public String getContractDisplayAccount() {
		if (StrKit.isBlank(contractDisplayAccount))
			throw new IllegalArgumentException("contractDisplayAccount 未被赋值");
		return contractDisplayAccount;
	}

	public WxPayApiConfig setContractDisplayAccount(String contractDisplayAccount) {
		if (StrKit.isBlank(contractDisplayAccount))
			throw new IllegalArgumentException("contractDisplayAccount 值不能为空");
		this.contractDisplayAccount = contractDisplayAccount;
		return this;
	}

	public String getVersion() {
		if (StrKit.isBlank(version))
			version = "1.0";
		return version;
	}

	public WxPayApiConfig setVersion(String version) {
		if (StrKit.isBlank(version))
			throw new IllegalArgumentException("version 值不能为空");
		this.version = version;
		return this;
	}

	public String getTimestamp() {
		if (StrKit.isBlank(timestamp)) {
			timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		}
		return timestamp;
	}

	public WxPayApiConfig setTimestamp(String timestamp) {
		if (StrKit.isBlank(timestamp))
			throw new IllegalArgumentException("timestamp 值不能为空");
		if (timestamp.length() != 10)
			throw new IllegalArgumentException("timestamp 值必须为10位");
		this.timestamp = timestamp;
		return this;
	}

	public String getReturnApp() {
		return returnApp;
	}

	public WxPayApiConfig setReturnApp(String returnApp) {
		this.returnApp = returnApp;
		return this;
	}

	public String getReturnWeb() {
		return returnWeb;
	}

	public WxPayApiConfig setReturnWeb(String returnWeb) {
		this.returnWeb = returnWeb;
		return this;
	}

	public String getContractNotifyUrl() {
		if (StrKit.isBlank(contractNotifyUrl))
			throw new IllegalArgumentException("contractNotifyUrl 未被赋值");
		return contractNotifyUrl;
	}

	public WxPayApiConfig setContractNotifyUrl(String contractNotifyUrl) {
		this.contractNotifyUrl = contractNotifyUrl;
		return this;
	}

	public String getContractId() {
		if (StrKit.isBlank(contractId))
			throw new IllegalArgumentException("contractId 未被赋值");
		return contractId;
	}

	public WxPayApiConfig setContractId(String contractId) {
		this.contractId = contractId;
		return this;
	}

}
