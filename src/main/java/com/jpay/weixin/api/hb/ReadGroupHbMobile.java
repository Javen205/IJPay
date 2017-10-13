package com.jpay.weixin.api.hb;

import java.util.HashMap;
import java.util.Map;

import com.jpay.ext.kit.PaymentKit;
import com.jpay.ext.kit.StrKit;
import com.jpay.weixin.api.WxPayApiConfig.PayModel;
/**
 * 微信分裂红包实体类
 * @author dujianyang
 */
public class ReadGroupHbMobile {

	private String nonceStr;//随机字符串
	private String mchBillNo;//商户订单号
	private String mchId;//商户号
	private String subMchId;//子商户号  微信支付分配的子商户号，服务商模式下必填
	private String wxAppId;//公众账号appid
	private String msgAppId;//触达用户appid 服务商模式下必填 可填服务商自己的appid或子商户的appid
	private String sendName;//商户名称
	private String reOpenId;//用户openid
	private long totalAmount;//付款金额
	private int totalNum;//红包发放总人数
	private String wishing;//红包祝福语
	private String actName;//活动名称
	private String remark;//备注
	private SceneId sceneId;//场景id 发放红包使用场景，红包金额大于200时必传
	private String riskInfo;//活动信息 否
	private String consumeMchId;//扣钱方mchid 否 常规模式下无效，服务商模式下选填，服务商模式下不填默认扣子商户的钱
	private AmtType amtType;//红包金额设置方式
	
	private PayModel payModel;
	private String paternerKey;
	
	public enum SceneId{
		PRODUCT_1,PRODUCT_2,PRODUCT_3,PRODUCT_4,PRODUCT_5,PRODUCT_6,PRODUCT_7,PRODUCT_8
	}
	
	public enum AmtType{
		ALL_RAND
	}
	
	private ReadGroupHbMobile() {
	}
	
	public static ReadGroupHbMobile Builder(){
		return new ReadGroupHbMobile();
	}
	
	public Map<String, String> build(){
		Map<String, String> map = new HashMap<String,String>();
		
		if (getPayModel().equals(PayModel.SERVICEMODE)) {
			map.put("sub_mch_id", getSubMchId());
			map.put("msgappid", getMsgAppId());
		}
		
		if (StrKit.notBlank(getRiskInfo())) {
			map.put("risk_info",getRiskInfo());
		}
		if (StrKit.notBlank(getConsumeMchId())) {
			map.put("consume_mch_id",getConsumeMchId());
		}
		
		
		map.put("nonce_str", getNonceStr());
		map.put("mch_billno", getMchBillNo());
		map.put("mch_id", getMchId());
		map.put("wxappid", getWxAppId());
		map.put("send_name", getSendName());
		map.put("re_openid", getReOpenId());
		map.put("total_amount", String.valueOf(getTotalAmount()));
		map.put("total_num", String.valueOf(getTotalNum()));
		map.put("amt_type", getAmtType().name());
		map.put("wishing", getWishing());
		map.put("act_name", getActName());
		map.put("remark", getRemark());
		map.put("sign", PaymentKit.createSign(map, getPaternerKey()));
		
		return map;
	}
	
	public String getNonceStr() {
		if (StrKit.isBlank(nonceStr))
			nonceStr = String.valueOf(System.currentTimeMillis());
		return nonceStr;
	}
	public ReadGroupHbMobile setNonceStr(String nonceStr) {
		if (StrKit.isBlank(nonceStr))
			throw new IllegalArgumentException("nonceStr 值不能为空");
		this.nonceStr = nonceStr;
		return this;
	}
	public String getMchBillNo() {
		if (StrKit.isBlank(mchBillNo))
			mchBillNo = String.valueOf(System.currentTimeMillis());
		return mchBillNo;
	}
	public ReadGroupHbMobile setMchBillNo(String mchBillNo) {
		if (StrKit.isBlank(mchBillNo))
			throw new IllegalArgumentException("mchBillNo 值不能为空");
		this.mchBillNo = mchBillNo;
		return this;
	}
	public String getMchId() {
		if (StrKit.isBlank(mchId))
			throw new IllegalArgumentException("mchId 未被赋值");
		return mchId;
	}
	public ReadGroupHbMobile setMchId(String mchId) {
		if (StrKit.isBlank(mchId))
			throw new IllegalArgumentException("mchId 值不能为空");
		this.mchId = mchId;
		return this;
	}
	public String getSubMchId() {
		return subMchId;
	}
	public ReadGroupHbMobile setSubMchId(String subMchId) {
		if (StrKit.isBlank(subMchId))
			throw new IllegalArgumentException("subMchId 值不能为空");
		this.subMchId = subMchId;
		return this;
	}
	public String getWxAppId() {
		if (StrKit.isBlank(wxAppId))
			throw new IllegalArgumentException("wxAppId 未被赋值");
		return wxAppId;
	}
	public ReadGroupHbMobile setWxAppId(String wxAppId) {
		if (StrKit.isBlank(wxAppId))
			throw new IllegalArgumentException("wxAppId 值不能为空");
		this.wxAppId = wxAppId;
		return this;
	}
	public String getMsgAppId() {
		return msgAppId;
	}
	public ReadGroupHbMobile setMsgAppId(String msgAppId) {
		if (StrKit.isBlank(msgAppId))
			throw new IllegalArgumentException("msgAppId 值不能为空");
		this.msgAppId = msgAppId;
		return this;
	}
 
	public String getSendName() {
		if (StrKit.isBlank(sendName))
			throw new IllegalArgumentException("sendName 未被赋值");
		return sendName;
	}
	public ReadGroupHbMobile setSendName(String sendName) {
		if (StrKit.isBlank(sendName))
			throw new IllegalArgumentException("sendName 值不能为空");
		this.sendName = sendName;
		return this;
	}
	public String getReOpenId() {
		if (StrKit.isBlank(reOpenId))
			throw new IllegalArgumentException("reOpenId 未被赋值");
		return reOpenId;
	}
	public ReadGroupHbMobile setReOpenId(String reOpenId) {
		if (StrKit.isBlank(reOpenId))
			throw new IllegalArgumentException("reOpenId 值不能为空");
		this.reOpenId = reOpenId;
		return this;
	}
	public long getTotalAmount() {
		if (totalAmount<=0)
			totalAmount = 1;
		return totalAmount;
	}
	public ReadGroupHbMobile setTotalAmount(long totalAmount) {
		if (totalAmount<=0)
			totalAmount = 1;
		this.totalAmount = totalAmount;
		return this;
	}
	public int getTotalNum() {
		if (totalNum<=0)
			totalNum = 1;
		return totalNum;
	}
	public ReadGroupHbMobile setTotalNum(int totalNum) {
		if (totalNum<=0)
			totalNum = 1;
		this.totalNum = totalNum;
		return this;
	}
	public String getWishing() {
		if (StrKit.isBlank(wishing))
			throw new IllegalArgumentException("wishing 未被赋值");
		return wishing;
	}
	public ReadGroupHbMobile setWishing(String wishing) {
		if (StrKit.isBlank(wishing))
			throw new IllegalArgumentException("wishing 值不能为空");
		this.wishing = wishing;
		return this;
	}
 
	public String getActName() {
		if (StrKit.isBlank(actName))
			throw new IllegalArgumentException("actName 未被赋值");
		return actName;
	}
	public ReadGroupHbMobile setActName(String actName) {
		if (StrKit.isBlank(actName))
			throw new IllegalArgumentException("actName 值不能为空");
		this.actName = actName;
		return this;
	}
	public String getRemark() {
		if (StrKit.isBlank(remark))
			throw new IllegalArgumentException("remark 未被赋值");
		return remark;
	}
	public ReadGroupHbMobile setRemark(String remark) {
		if (StrKit.isBlank(remark))
			throw new IllegalArgumentException("remark 值不能为空");
		this.remark = remark;
		return this;
	}
	public SceneId getSceneId() {
		if (sceneId == null)
			throw new IllegalArgumentException("sceneId 未被赋值");
		return sceneId;
	}
	public ReadGroupHbMobile setSceneId(SceneId sceneId) {
		if (sceneId == null)
			throw new IllegalArgumentException("sceneId 值不能为空");
		this.sceneId = sceneId;
		return this;
	}
	public String getRiskInfo() {
		return riskInfo;
	}
	public ReadGroupHbMobile setRiskInfo(String riskInfo) {
		if (StrKit.isBlank(riskInfo))
			throw new IllegalArgumentException("riskInfo 值不能为空");
		this.riskInfo = riskInfo;
		return this;
	}
	public String getConsumeMchId() {
		return consumeMchId;
	}
	public ReadGroupHbMobile setConsumeMchId(String consumeMchId) {
		if (StrKit.isBlank(consumeMchId))
			throw new IllegalArgumentException("consumeMchId 值不能为空");
		this.consumeMchId = consumeMchId;
		return this;
	}

	public PayModel getPayModel() {
		if (payModel == null)
			throw new IllegalArgumentException("payModel 未被赋值");
		return payModel;
	}

	public ReadGroupHbMobile setPayModel(PayModel payModel) {
		if (payModel == null)
			throw new IllegalArgumentException("payModel 值不能为空");
		this.payModel = payModel;
		return this;
	}

	public String getPaternerKey() {
		if (StrKit.isBlank(paternerKey))
			throw new IllegalArgumentException("paternerKey 未被赋值");
		return paternerKey;
	}

	public ReadGroupHbMobile setPaternerKey(String paternerKey) {
		if (StrKit.isBlank(paternerKey))
			throw new IllegalArgumentException("paternerKey 值不能为空");
		this.paternerKey = paternerKey;
		return this;
	}
	
	public AmtType getAmtType() {
		if (amtType == null)
			throw new IllegalArgumentException("amtType 未被赋值");
		return amtType;
	}
	public ReadGroupHbMobile setAmtType(AmtType amtType) {
		if (amtType == null)
			throw new IllegalArgumentException("amtType 值不能为空");
		this.amtType = amtType;
		return this;
	}
	
}
