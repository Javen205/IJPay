
### 官方接入文档

1. [公众号支付异步通知文档](https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7)
2. [小程序支付异步通知文档](https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=9_7)
3. [扫码支付异步通知文档](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7)
4. [App支付服务器异步通知文档](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_7&index=3)

### 异步通知处理步骤

1. 验证签名
2. 校验订单去重
3. 处理业务逻辑
4. 响应处理结果


### 验证签名

参考 `公用API`

### 代码示例

https://github.com/Javen205/IJPay-Demo

[Spring Boot版本Demo](https://gitee.com/javen205/IJPay-Demo/blob/SpringBoot/src/main/java/com/ijpay/controller/wxpay/WxPayController.java)
```
	@RequestMapping(value = "/pay_notify",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String pay_notify(HttpServletRequest request) {
		// 支付结果通用通知文档: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
		String xmlMsg = HttpKit.readData(request);
		System.out.println("支付通知="+xmlMsg);
		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
//		String appid  = params.get("appid");
//		//商户号
//		String mch_id  = params.get("mch_id");
		String result_code  = params.get("result_code");
//		String openId      = params.get("openid");
//		//交易类型
//		String trade_type      = params.get("trade_type");
//		//付款银行
//		String bank_type      = params.get("bank_type");
//		// 总金额
//		String total_fee     = params.get("total_fee");
//		//现金支付金额
//		String cash_fee     = params.get("cash_fee");
//		// 微信支付订单号
//		String transaction_id      = params.get("transaction_id");
//		// 商户订单号
//		String out_trade_no      = params.get("out_trade_no");
//		// 支付完成时间，格式为yyyyMMddHHmmss
//		String time_end      = params.get("time_end");	
		/////////////////////////////以下是附加参数///////////////////////////////////
		
		String attach      = params.get("attach");
//		String fee_type      = params.get("fee_type");
//		String is_subscribe      = params.get("is_subscribe");
//		String err_code      = params.get("err_code");
//		String err_code_des      = params.get("err_code_des");
		// 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
		// 避免已经成功、关闭、退款的订单被再次更新
//		Order order = Order.dao.getOrderByTransactionId(transaction_id);
//		if (order==null) {
			if(PaymentKit.verifyNotify(params, WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey())){
				if (("SUCCESS").equals(result_code)) {
					//更新订单信息
					log.warn("更新订单信息:"+attach);
					//发送通知等
					Map<String, String> xml = new HashMap<String, String>();
					xml.put("return_code", "SUCCESS");
					xml.put("return_msg", "OK");
					return PaymentKit.toXml(xml);
				}
			}
//		}
		return null;
	}
```


[Jfinal版本Demo](https://gitee.com/javen205/IJPay-Demo/blob/master/src/main/java/com/ijpay/controller/weixin/WxPayController.java)

```
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
		
//		String appid  = params.get("appid");
//		//商户号
//		String mch_id  = params.get("mch_id");
		String result_code  = params.get("result_code");
//		String openId      = params.get("openid");
//		//交易类型
//		String trade_type      = params.get("trade_type");
//		//付款银行
//		String bank_type      = params.get("bank_type");
//		// 总金额
//		String total_fee     = params.get("total_fee");
//		//现金支付金额
//		String cash_fee     = params.get("cash_fee");
//		// 微信支付订单号
//		String transaction_id      = params.get("transaction_id");
//		// 商户订单号
//		String out_trade_no      = params.get("out_trade_no");
//		// 支付完成时间，格式为yyyyMMddHHmmss
//		String time_end      = params.get("time_end");
		
		/////////////////////////////以下是附加参数///////////////////////////////////
		
		String attach      = params.get("attach");
//		String fee_type      = params.get("fee_type");
//		String is_subscribe      = params.get("is_subscribe");
//		String err_code      = params.get("err_code");
//		String err_code_des      = params.get("err_code_des");
		
		
		// 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
		// 避免已经成功、关闭、退款的订单被再次更新
//		Order order = Order.dao.getOrderByTransactionId(transaction_id);
//		if (order==null) {
			if(PaymentKit.verifyNotify(params, WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey())){
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
//		}
		renderText("");
	}
```