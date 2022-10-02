# IJPay 中的微信支付

## 官方参考文档

- [普通商户模式接入](https://pay.weixin.qq.com/wiki/doc/api/index.html)
- [服务商模式接入](https://pay.weixin.qq.com/wiki/doc/api/sl.html)
- [微信委托扣款相关 API-服务商模式](https://pay.weixin.qq.com/wiki/doc/api/pap_sl.php?chapter=17_1)
- [微信委托扣款相关 API-普通商户模式](https://pay.weixin.qq.com/wiki/doc/api/pap.php?chapter=17_1)
- [微信刷脸支付 API](https://pay.weixin.qq.com/wiki/doc/wxfacepay/develop/backend.html)
- [微信酒店押金相关 API](https://pay.weixin.qq.com/wiki/doc/api/deposit_sl.php?chapter=26_1)

##  支付方式与支付工具

::: tip
IJPay 中支持的支付方式以及支付工具
:::

1. 付款码支付【[商户模式](https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=5_1)】【[服务商模式](https://pay.weixin.qq.com/wiki/doc/api/micropay_sl.php?chapter=5_1)】
2. 公众号支付(JSAPI 支付)【[商户模式](https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_1)】【[服务商模式](https://pay.weixin.qq.com/wiki/doc/api/jsapi_sl.php?chapter=7_1)】
3. 扫码支付(Native 支付)【[商户模式](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_1)】【[服务商模式](https://pay.weixin.qq.com/wiki/doc/api/native_sl.php?chapter=6_1)】
4. APP支付【[商户模式](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_1)】【[服务商模式](https://pay.weixin.qq.com/wiki/doc/api/app/app_sl.php?chapter=8_1)】
5. H5 支付【[商户模式](https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=15_1)】【[服务商模式](https://pay.weixin.qq.com/wiki/doc/api/H5_sl.php?chapter=15_1)】
6. 小程序支付【[商户模式](https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_3&index=1)】【[服务商模式](https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_sl_api.php?chapter=7_3&index=1)】
7. 人脸支付   【[服务商模式](https://pay.weixin.qq.com/wiki/doc/wxfacepay/)】
8. 企业付款到零钱/银行卡 【[商户模式](https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_1)】
9. 现金红包  【[商户模式](https://pay.weixin.qq.com/wiki/doc/api/tools/cash_coupon.php?chapter=13_1)】【[服务商模式](https://pay.weixin.qq.com/wiki/doc/api/tools/cash_coupon_sl.php?chapter=13_1)】

## 使用步骤

1. [添加相关依赖](../maven.md)
2. 找到相关支付方式的接口文档。构建请求参数 Model，并使用 https 执行请求
3. 调用支付接口唤起支付并完成支付逻辑


## 微信支付 Model

IJPay 中常用支付方式涉及到的 [Model](https://gitee.com/javen205/IJPay/blob/master/IJPay-WxPay/src/main/java/com/ijpay/wxpay/model)
都是使用 `builder` 模式来构建，**其中 Model 每个字段与官方接口文档保持一致**，同时支持商户模式、服务商模式。

 ::: warning
 为什么要使用 Lombok 来构建接口的请求参数？

1、使用 Lombok 来构建请求参数是为了方便，不用写太多的冗余 get set。

2、避免手动设置误写参数导致的低级错误。

外界对 Lombok 的评价也不太一致，喜欢的非常喜欢不喜欢的就使劲吐槽，那么 IJPay 中有其他的替代方案吗？

当然是有的，最简单粗暴的方法使用 Map 来构建请求参数，再使用 `WxPayKit.buildSign` 来构建签名即可。 IJPay 1.x 版本就是这么做的  
 :::

## 扩展 Model

由于支付方式的不同涉及到的接口非常多，如果某些接口的 Model 在 IJPay 没有提供封装，大家可以继承 [BaseModel](https://gitee.com/javen205/IJPay/blob/master/IJPay-Core/src/main/java/com/ijpay/core/model/BaseModel.java) 
**所有属性字段名称与接口参数保持一致且必须为 `String` 类型**，最后添加如下注解来实现 `builder` 模式。

```java{5}
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CloseOrderModel extends BaseModel {
    // 属性字段名称与接口参数保持一致 
    private String appid;
    // 省略接口中的其他参数...
}
```  

::: warning 前方高能
1. 扩展 Model 必须继承  BaseModel
2. 所有属性字段名称与接口参数保持一致且必须为 `String` 类型
3. 必须添加 [Lombok](https://projectlombok.org) 注解 
`@Builder`  
`@AllArgsConstructor(access = AccessLevel.PRIVATE)` 
`@Getter`
4. IDEA 必须安装 [Lombok](https://projectlombok.org) 插件
:::


## 构建参数并执行请求

1. 根据接口需要的参数，使用 Model 构建除 `sign` 以外的参数
2. `build` 后根据接口的签名方式创建签名 `creatSign`
3. 使用 [WxPayApi](https://gitee.com/javen205/IJPay/blob/master/IJPay-WxPay/src/main/java/com/ijpay/wxpay/WxPayApi.java) 提供的接口执行 https 请求
4. 构建支付参数唤起支付 

例如公众号支付(JSAPI 支付)的统一下单

```java{2,3,15,17,19,21,36,37} 
// 统一下单构建请求参数
Map<String, String> params = UnifiedOrderModel
    .builder()
    .appid(wxPayBean.getAppId())
    .mch_id(wxPayBean.getMchId())
    .nonce_str(WxPayKit.generateStr())
    .body("IJPay 让支付触手可及")
    .attach("Node.js 版:https://gitee.com/javen205/TNWX")
    .out_trade_no(WxPayKit.generateStr())
    .total_fee("1000")
    .spbill_create_ip(ip)
    .notify_url(wxPayBean.getDomain().concat("/wxpay/pay_notify"))
    .trade_type(TradeType.JSAPI.getTradeType())
    .openid(openId)
    .build()
    // 同时支持 SignType.MD5、SignType.HMACSHA256
    .creatSign(wxPayBean.getPartnerKey(), SignType.HMACSHA256);  
// 发送请求
String xmlResult = WxPayApi.pushOrder(false,params); 
// 将请求返回的 xml 数据转为 Map，方便后面逻辑获取数据
Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);
// 判断返回的结果
String returnCode = resultMap.get("return_code");
String returnMsg = resultMap.get("return_msg");
if (!WxPayKit.codeIsOk(returnCode)) {
    return new AjaxResult().addError(returnMsg);
}
String resultCode = resultMap.get("result_code");
if (!WxPayKit.codeIsOk(resultCode)) {
    return new AjaxResult().addError(returnMsg);
}

// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
String prepayId = resultMap.get("prepay_id");
// 二次签名，构建公众号唤起支付的参数,这里的签名方式要与上面统一下单请求签名方式保持一致
Map<String, String> packageParams = WxPayKit.prepayIdCreateSign(prepayId, 
    wxPayBean.getAppId(),wxPayBean.getPartnerKey(),SignType.HMACSHA256);
// 将二次签名构建的数据返回给前端并唤起公众号支付
String jsonStr = JSON.toJSONString(packageParams);
return new AjaxResult().success(jsonStr);
``` 

::: tip 完整示例
公众号[如何唤起支付](https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_7&index=6)，完整示例请参考 [IJPay-Demo](https://gitee.com/javen205/IJPay/blob/master/IJPay-Demo-SpringBoot/src/main/java/com/ijpay/demo/controller/wxpay/WxPayController.java)
:::

## 多应用支持

微信支付的多应用支持方式与支付宝的多应用无缝切换一致，请参考 [支付宝初始化](../alipay/init.md)


## 完整示例

 ::: tip
 IJPay 2.x 版本示例
 :::
 
- [IJPay-Demo-SpringBoot](https://gitee.com/javen205/IJPay/tree/master/IJPay-Demo-SpringBoot)
- [IJPay-Demo-JFinal](https://gitee.com/javen205/IJPay/tree/master/IJPay-Demo-JFinal)

 ::: tip
 IJPay 1.x 版本示例
 :::
 
- [IJPay-Demo](https://gitee.com/javen205/IJPay-Demo)

<Q url="tencent://message/?uin=572839485&Site=%E5%AE%A2%E6%9C%8D&Menu=yes" />

