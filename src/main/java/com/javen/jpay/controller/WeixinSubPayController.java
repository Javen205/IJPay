package com.javen.jpay.controller;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.javen.jpay.ext.kit.ZxingKit;
import com.javen.jpay.vo.AjaxResult;
import com.javen.jpay.weixin.api.WxPayApi;
import com.javen.jpay.weixin.api.WxPayApi.TradeType;
import com.javen.jpay.weixin.api.WxPayApiConfig;
import com.javen.jpay.weixin.api.WxPayApiConfig.PayModel;
import com.javen.jpay.weixin.api.WxPayApiConfigKit;
import com.javen.jpay.weixin.api.WxPayApiController;
import com.javen.jpay.weixin.utils.IpKit;
import com.javen.jpay.weixin.utils.PaymentKit;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

/**
 * @author Javen
 * 2017年4月19日
 */
public class WeixinSubPayController extends WxPayApiController {
	static Log log=Log.getLog(WeixinSubPayController.class);
	private AjaxResult ajax = new AjaxResult();
	private static final Prop prop = PropKit.use("wxsubpay.properties");
	//商户相关资料
	String appid = prop.get("appId");
	String mch_id = prop.get("mch_id");
	String sub_mch_id = prop.get("sub_mch_id");
	String sub_appid = prop.get("sub_appid");
	String paternerKey = prop.get("paternerKey");
	String notify_url = prop.get("domain")+"/wxsubpay/pay_notify";

	public WxPayApiConfig getApiConfig() {
		WxPayApiConfig apiConfig = WxPayApiConfig.New()
		.setAppId(appid)
//		.setSubAppId(sub_appid)//可以为空,如果设置了统一下单中sub_openid必须设置
		.setMchId(mch_id)
		.setSubMchId(sub_mch_id)
		.setPaternerKey(paternerKey)
		.setPayModel(PayModel.SERVICEMODE);
		return apiConfig;
	}
	
	public void index(){
		log.info("欢迎使用IJPay,服务商模式下微信支付 - by Javen");
		renderText("欢迎使用IJPay 服务商模式下微信支付  - by Javen");
	}
	
	
	/**
	 * 公众号支付
	 * 已测试 o5NJx1dVRilQI6uUVSaBDuLnM3iM
	 */
	public void webPay(){
		String openId = (String) getSession().getAttribute("openId");
		String total_fee=getPara("total_fee");
		if (StrKit.isBlank(total_fee)) {
			ajax.addError("参数错误");
			renderJson(ajax);
			return;
		}
		if (StrKit.isBlank(openId)) {
			ajax.addError("openId is null");
			renderJson(ajax);
			return;
		}
		
		String ip = IpKit.getRealIp(getRequest());
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		log.info(WxPayApiConfigKit.getAliPayApiConfig().getMchId());
		Map<String, String> params = WxPayApiConfigKit.getAliPayApiConfig()
		.setAttach("IJPay 公众号支付测试  -By Javen")
		.setBody("IJPay 公众号支付测试 -By Javen")
		.setOpenId(openId)
		.setSpbillCreateIp(ip)
		.setTotalFee(total_fee)
		.setTradeType(WxPayApi.TradeType.JSAPI)
		.setNotifyUrl(notify_url)
		.build();
		
		String xmlResult =  WxPayApi.pushOrder(params);
		
		System.out.println(xmlResult);
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
	}

	
	/**
	 * 刷卡支付
	 * 已测试
	 */
	public void micropay(){
		try {
			String total_fee=getPara("total_fee");
			String auth_code = getPara("auth_code");
			if (StrKit.isBlank(total_fee) || StrKit.isBlank(auth_code)) {
				ajax.addError("参数错误");
				renderJson(ajax);
				return;
			}
			
			log.info(WxPayApiConfigKit.getAliPayApiConfig().getMchId());
			String ip = IpKit.getRealIp(getRequest());
			if (StrKit.isBlank(ip)) {
				ip = "127.0.0.1";
			}
			
			Map<String, String> params = WxPayApiConfigKit.getAliPayApiConfig()
					.setAttach("IJPay 刷卡支付测试  -By Javen")
					.setBody("IJPay 刷卡支付测试")
					.setSpbillCreateIp(ip)
					.setTotalFee(total_fee)
					.setOutTradeNo(String.valueOf(System.currentTimeMillis()))
					.setAuthCode(auth_code)
					.setTradeType(TradeType.MICROPAY)//必须设置
					.build();
			
			String xmlResult = WxPayApi.micropay(params);
			//同步返回结果
			log.info("xmlResult:"+xmlResult);
			
			Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
			String return_code = result.get("return_code");
			String return_msg = result.get("return_msg");
			if (!PaymentKit.codeIsOK(return_code)) {
				log.info("提交刷卡支付失败>>"+xmlResult);
				ajax.addError(return_msg);
				renderJson(ajax);
				return;
			}
			
			String result_code = result.get("result_code");
			if (!PaymentKit.codeIsOK(result_code)) {
				//支付失败
				log.info("支付失败>>"+xmlResult);
				
				String err_code_des = result.get("err_code_des");
				
				ajax.addError(err_code_des);
				renderJson(ajax);
				return;
			}
			
			//支付成功 
			System.out.println(xmlResult);
			ajax.success("");
			renderJson(ajax);
		} catch (Exception e) {
			ajax.addError("系统异常");
			renderJson(ajax);
			e.printStackTrace();
		}
	}
	/**
	 * 扫码支付
	 * 已测试
	 */
	public void scanCode(){
		String total_fee=getPara("total_fee");
		if (StrKit.isBlank(total_fee)) {
			ajax.addError("参数错误");
			renderJson(ajax);
			return;
		}
		
		String ip = IpKit.getRealIp(getRequest());
		if (StrKit.isBlank(ip)) {
			ip = "127.0.0.1";
		}
		
		Map<String, String> params = WxPayApiConfigKit.getAliPayApiConfig()
				.setAttach("IJPay 测试  -By Javen")
				.setBody("IJPay 扫码支付测试")
				.setSpbillCreateIp(ip)
				.setTotalFee(total_fee)
				.setOutTradeNo(String.valueOf(System.currentTimeMillis()))
				.setTradeType(WxPayApi.TradeType.NATIVE)
				.setNotifyUrl(notify_url)
				.build();
		
		String xmlResult = WxPayApi.pushOrder(params);
		
		log.info(xmlResult);
		Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
		
		String return_code = result.get("return_code");
		String return_msg = result.get("return_msg");
		if (!PaymentKit.codeIsOK(return_code)) {
			log.debug(return_msg);
			renderText(xmlResult);
			return;
		}
		String result_code = result.get("result_code");
		if (!PaymentKit.codeIsOK(result_code)) {
			log.debug(result_code);
			renderText(xmlResult);
			return;
		}
		//生成预付订单success
		
		String qrCodeUrl = result.get("code_url");
		System.out.println("qrCodeUrl>>>"+qrCodeUrl);
		//由于使用JS不能长按扫码，所以使用Zxing
		//生成二维码保存的路径
		String name = "payQRCode.png";
		Boolean encode = ZxingKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
				PathKit.getWebRootPath()+File.separator+name );
		if (encode) {
			//在页面上显示
			ajax.success(name);
			renderJson(ajax);
		}
	}
	
	
	public void pay_notify() {
		//获取所有的参数
		StringBuffer sbf=new StringBuffer();
				 
		Enumeration<String>  en=getParaNames();
		while (en.hasMoreElements()) {
			Object o= en.nextElement();
			sbf.append(o.toString()+"="+getPara(o.toString()));
		}
		
		log.error("支付通知参数："+sbf.toString());
		
		// 支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
		String xmlMsg = HttpKit.readData(getRequest());
		System.out.println("支付通知="+xmlMsg);
		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
		
		String appid  = params.get("appid");
		//商户号
		String mch_id  = params.get("mch_id");
		String result_code  = params.get("result_code");
		String openId      = params.get("openid");
		//交易类型
		String trade_type      = params.get("trade_type");
		//付款银行
		String bank_type      = params.get("bank_type");
		// 总金额
		String total_fee     = params.get("total_fee");
		//现金支付金额
		String cash_fee     = params.get("cash_fee");
		// 微信支付订单号
		String transaction_id      = params.get("transaction_id");
		// 商户订单号
		String out_trade_no      = params.get("out_trade_no");
		// 支付完成时间，格式为yyyyMMddHHmmss
		String time_end      = params.get("time_end");
		
		/////////////////////////////以下是附加参数///////////////////////////////////
		
		String attach      = params.get("attach");
		String fee_type      = params.get("fee_type");
		String is_subscribe      = params.get("is_subscribe");
		String err_code      = params.get("err_code");
		String err_code_des      = params.get("err_code_des");
		
		
		// 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
		// 避免已经成功、关闭、退款的订单被再次更新
		if(PaymentKit.verifyNotify(params, WxPayApiConfigKit.getAliPayApiConfig().getPaternerKey())){
			if (("SUCCESS").equals(result_code)) {
				//更新订单信息
				log.warn("更新订单信息:"+attach);
				
				//发送通知等
				
				Map<String, String> xml = new HashMap<String, String>();
				xml.put("return_code", "SUCCESS");
				xml.put("return_msg", "OK");
				renderText(PaymentKit.toXml(xml));
				return;
			}
		}
	}
	
	
}


