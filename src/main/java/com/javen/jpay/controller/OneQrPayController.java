/**
 * Copyright (c) 2015-2017, Javen Zhou  (javen205@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.javen.jpay.controller;

import java.util.Map;

import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.javen.jpay.alipay.AliPayApi;
import com.javen.jpay.alipay.AliPayApiConfig;
import com.javen.jpay.alipay.AliPayApiConfigKit;
import com.javen.jpay.util.StringUtils;
import com.javen.jpay.vo.AjaxResult;
import com.javen.jpay.weixin.api.WxPayApi;
import com.javen.jpay.weixin.api.WxPayApiConfig;
import com.javen.jpay.weixin.api.WxPayApiConfigKit;
import com.javen.jpay.weixin.api.WxPayApi.TradeType;
import com.javen.jpay.weixin.api.WxPayApiConfig.PayModel;
import com.javen.jpay.weixin.utils.IpKit;
import com.javen.jpay.weixin.utils.PaymentKit;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

/**
 * @author Javen http://blog.csdn.net/zyw_java/article/details/54630880
 */
public class OneQrPayController extends Controller {
	static Log log = Log.getLog(OneQrPayController.class);
	private AjaxResult ajax = new AjaxResult();
	
	private static final Prop prop = PropKit.use("wxpay.properties");
	//商户相关资料
	String appid = prop.get("appId");
	String mch_id = prop.get("mch_id");
	String partnerKey = prop.get("partnerKey");
	String notify_url = prop.get("domain")+"/wxpay/pay_notify";
	
	private  final Prop AliProp = PropKit.use("alipay.properties");
	private  String charset = "UTF-8";
	private  String private_key = AliProp.get("privateKey");
	private  String alipay_public_key = AliProp.get("alipayPulicKey");
	private  String service_url = AliProp.get("serverUrl");
	private  String app_id = AliProp.get("appId");
	private  String sign_type = "RSA2";
	private  String notify_domain = AliProp.get("notify_domain");
	
	public WxPayApiConfig getWxApiConfig() {
		WxPayApiConfig apiConfig = WxPayApiConfig.New()
				.setAppId(appid)
				.setMchId(mch_id)
				.setPaternerKey(partnerKey)
				.setPayModel(PayModel.BUSINESSMODEL);
		return apiConfig;
	}
	
	public AliPayApiConfig getAlApiConfig() {
		AliPayApiConfig aliPayApiConfig = AliPayApiConfig.New()
		.setAppId(app_id)
		.setAlipayPublicKey(alipay_public_key)
		.setCharset(charset)
		.setPrivateKey(private_key)
		.setServiceUrl(service_url)
		.setSignType(sign_type)
		.build();
		return aliPayApiConfig;
	}

	public enum OneQrPayModel {
		OTHER("其他", 0), WXPAY("微信支付", 1), ALIPAY("支付宝支付", 2);
		private String name;
		private int payModel;

		// 构造方法
		private OneQrPayModel(String name, int payModel) {
			this.name = name;
			this.payModel = payModel;
		}

		// 普通方法
		public static String getName(int payModel) {
			for (OneQrPayModel c : OneQrPayModel.values()) {
				if (c.getPayModel() == payModel) {
					return c.name;
				}
			}
			return null;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getPayModel() {
			return payModel;
		}

		public void setPayModel(int payModel) {
			this.payModel = payModel;
		}
	}
	/**
	 * 生成支付的二维码
	 */
	public void index(){
		StringBuffer sbf = new StringBuffer();
		sbf.append(PropKit.get("domain"))
		.append("/oneqrpay/scand")
		.append("?partnerId=")
		.append(StringUtils.getUUID());
		renderQrCode(sbf.toString(), 168, 168);
	}
	/**
	 * 授权之后现在支付页
	 */
	public void toPage(){
		String partnerId = getPara("partnerId");
		setAttr("payModel", OneQrPayModel.WXPAY.getPayModel());
		setSessionAttr("partnerId", partnerId);
		render("onqrpay.jsp");
	}

	/**
	 * 扫码访问的地址
	 */
	public void scand() {
		String partnerId = getPara("partnerId");
		String userAgent = getRequest().getHeader("User-Agent");
		int payModel = OneQrPayModel.OTHER.getPayModel();
		
		String agent = userAgent.toLowerCase();
		if (agent.indexOf("micromessenger") > 0) {
			// 用户使用微信访问页面
			payModel = OneQrPayModel.WXPAY.getPayModel();
			log.info("微信...");
			//授权获取openId
			redirect("/toOauth?state="+partnerId);
			return;

		} else if (agent.indexOf("alipayclient") > 0) {
			// 用户使用支付宝访问页面
			payModel = OneQrPayModel.ALIPAY.getPayModel();
			log.info("支付宝...");
		} else {
			log.info("其他客户端...");
		}
		log.info("payModel>"+payModel+"  "+userAgent);
		setSessionAttr("partnerId", partnerId);
		setAttr("payModel", payModel);
		render("onqrpay.jsp");
	}
	/**
	 * 支付
	 */
	public void toPay() {
		//可以通过ID查询商户相关的配置,这里为了方便直接从properties文件中读取
		String partnerId = (String) getSession().getAttribute("partnerId");
		
		log.info("获取到的商户编号："+partnerId);
		
		String total_fee = getPara("total_fee");
		int payModel = getParaToInt("payModel");
		
		if (StrKit.isBlank(partnerId)) {
			ajax.addError("商户编号为空,请联系管理员");
			renderJson(ajax);
			return;
		}
		if (StrKit.isBlank(total_fee)) {
			ajax.addError("请输入支付的金额");
			renderJson(ajax);
			return;
		}

		if (payModel == OneQrPayModel.WXPAY.getPayModel()) {
			// 微信支付o5NJx1SvzB1Rw-b-cYyrP97QsprQ
			String openId = (String) getSession().getAttribute("openId");
			if (StrKit.isBlank(openId)) {
				ajax.addError("openId is null");
				renderJson(ajax);
				return;
			}
			
			String ip = IpKit.getRealIp(getRequest());
			if (StrKit.isBlank(ip)) {
				ip = "127.0.0.1";
			}
			WxPayApiConfigKit.setThreadLocalWxPayApiConfig(getWxApiConfig()); 
			Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig()
					.setAttach("IJPay 公众号支付测试  -By Javen")
					.setBody("IJPay 公众号支付测试  -By Javen")
					.setOpenId(openId)
					.setSpbillCreateIp(ip)
					.setTotalFee(total_fee)
					.setTradeType(TradeType.JSAPI)
					.setNotifyUrl(notify_url)
					.build();
			
			String xmlResult = WxPayApi.pushOrder(params);
	log.info(xmlResult);
			Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
			
			String return_code = result.get("return_code");
			String return_msg = result.get("return_msg");
			if (!PaymentKit.codeIsOK(return_code)) {
				ajax.addError(return_msg);
				renderJson(ajax);
				return;
			}
			String result_code = result.get("result_code");
			if (!PaymentKit.codeIsOK(result_code)) {
				ajax.addError(return_msg);
				renderJson(ajax);
				return;
			}
			// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
			String prepay_id = result.get("prepay_id");
			
			Map<String, String> packageParams = PaymentKit.prepayIdCreateSign(prepay_id);
			
			String jsonStr = JsonKit.toJson(packageParams);
			ajax.success(jsonStr);
			renderJson(ajax);
			
		} else if (payModel ==  OneQrPayModel.ALIPAY.getPayModel()) {
			AliPayApiConfigKit.setThreadLocalAliPayApiConfig(getAlApiConfig());
			//支付宝支付
			String body = "我是测试数据-By Javen";
			String subject = "Javen Wap支付测试";
			String totalAmount = total_fee;
			String passbackParams = "1";
			String returnUrl = notify_domain + "/alipay/return_url";
			String notifyUrl = notify_domain + "/alipay/notify_url";

			AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
			model.setBody(body);
			model.setSubject(subject);
			model.setTotalAmount(totalAmount);
			model.setPassbackParams(passbackParams);
			String outTradeNo = StringUtils.getOutTradeNo();
			System.out.println("wap outTradeNo>"+outTradeNo);
			model.setOutTradeNo(outTradeNo);
			model.setProductCode("QUICK_WAP_PAY");

			try {
				AliPayApi.wapPay(getResponse(), model, returnUrl, notifyUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			renderNull();
		}
	}
}
