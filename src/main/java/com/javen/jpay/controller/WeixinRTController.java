package com.javen.jpay.controller;

import java.util.HashMap;
import java.util.Map;

import com.javen.jpay.weixin.api.WxPayApi;
import com.javen.jpay.weixin.api.WxPayApiConfig.PayModel;
import com.javen.jpay.weixin.api.hb.ReadHbModle;
import com.javen.jpay.weixin.api.hb.RedHbApi;
import com.javen.jpay.weixin.utils.IpKit;
import com.javen.jpay.weixin.utils.PaymentKit;
import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

public class WeixinRTController extends Controller {
	static Log log=Log.getLog(WeixinSubRTController.class);
	private static final Prop prop = PropKit.use("wxpay.properties");
	
	//商户相关资料
	String appId = prop.get("appId");
	String mchId = prop.get("mch_id");
	String partnerKey = prop.get("partnerKey");
	String certPath = prop.get("certPath");
	
	/**
	 * 发送红包
	 */
	public void sendRedPack(){
		String ns = String.valueOf(System.currentTimeMillis());
		
		log.info(ns);
		
		Map<String, String> params = ReadHbModle.Builder()
		.setPayModel(PayModel.BUSINESSMODEL)
		.setNonceStr(ns)//有默认值可不设置 
		.setMchBillNo(ns)//有默认值可不设置  
		.setMchId(mchId)
		.setWxAppId(appId)
		.setSendName("感谢您使用IJpay!")
		.setReOpenId("o5NJx1dVRilQI6uUVSaBDuLnM3iM")//根据实际的值修改
		.setTotalAmount(100)//单位分 红包的金额必须在1.00元到200.00元之间
		.setTotalNum(1)
		.setWishing("IJPay 发送红包测试 -By Javen")
		.setClientIp(IpKit.getRealIp(getRequest()))
		.setActName("测试")
		.setRemark("快来抢!")
		.setPaternerKey(partnerKey)
		.build();
		
		
		String sendRedPack = RedHbApi.sendRedPack(params, certPath, mchId);
		renderText(sendRedPack);
	}
	/**
	 * 发送裂变红包
	 */
	public void sendGroupRedPack(){
		String ns = String.valueOf(System.currentTimeMillis());
		
		log.info(ns);
		
		Map<String, String> params = ReadHbModle.Builder()
		.setPayModel(PayModel.BUSINESSMODEL)
		.setNonceStr(ns)//有默认值可不设置 
		.setMchBillNo(ns)//有默认值可不设置 
		.setMchId(mchId)
		.setWxAppId(appId)
		.setSendName("感谢您使用IJpay!")
		.setReOpenId("o5NJx1dVRilQI6uUVSaBDuLnM3iM")
		.setTotalAmount(3000)//单位分 每个红包的平均金额必须在1.00元到200.00元之间.
		.setTotalNum(3)//total_num必须介于(包括)3到20之间
		
		.setWishing("IJPay 发送红包测试 -By Javen")
		.setActName("测试")
		.setRemark("快来抢!")
		.setClientIp(IpKit.getRealIp(getRequest()))
		.setPaternerKey(partnerKey)
		.build();
		
		params.put("amt_type", "ALL_RAND");
		params.put("sign", PaymentKit.createSign(params, partnerKey));
		
		String sendGroupRedPack = RedHbApi.sendGroupRedPack(params, certPath, mchId);
		renderText(sendGroupRedPack);
	}
	
	
	/**
	 * 查询
	 */
	public void gethbinfo(){
		
		Map<String, String> params = new HashMap<String,String>();
		
		params.put("nonce_str",String.valueOf(System.currentTimeMillis()));
		params.put("mch_billno", "");
		params.put("mch_id", mchId);
		params.put("appid",appId);
		params.put("bill_type", "MCHT");
		params.put("sign", PaymentKit.createSign(params, partnerKey));
		
		String hbInfo = RedHbApi.getHbInfo(params, certPath, mchId);
		renderText(hbInfo);
	}
	/**
	 * 企业付款
	 */
	public void transfers(){
		String ns = String.valueOf(System.currentTimeMillis());
log.info(ns);
		Map<String, String> map = new HashMap<String, String>();
		map.put("mch_appid", appId);
		map.put("mchid", mchId);
		map.put("nonce_str", ns);
		map.put("partner_trade_no", ns);
		map.put("openid", "o5NJx1dVRilQI6uUVSaBDuLnM3iM");
		map.put("check_name", "FORCE_CHECK");//NO_CHECK
		map.put("re_user_name", "Javen");
		map.put("amount", "888");
		map.put("desc", "IJPay 企业付款测试 -By Javen");
		String ip = IpKit.getRealIp(getRequest());
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		map.put("spbill_create_ip",ip);
		
		map.put("sign", PaymentKit.createSign(map, partnerKey));
		
		String transfers = WxPayApi.transfers(map, certPath, mchId);
		renderText(transfers);
	}
	/**
	 * 查询企业付款
	 */
	public void getTransferInfo(){
		String tradeNo = getPara("tradeNo");
		Map<String, String> map = new HashMap<String, String>();
		String ns = String.valueOf(System.currentTimeMillis());
		map.put("nonce_str", ns);
		map.put("partner_trade_no", tradeNo);
		map.put("mch_id", mchId);
		map.put("appid", appId);
		map.put("sign", PaymentKit.createSign(map, partnerKey));

		String transferInfo = WxPayApi.getTransferInfo(map, certPath, mchId);
		renderText(transferInfo);
	}
}
