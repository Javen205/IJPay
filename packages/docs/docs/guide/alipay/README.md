# IJPay 中的支付宝支付

## 简介

IJPay 中的支付宝支付是对 [alipay-sdk-java](https://github.com/alipay/alipay-sdk-java-all) 的二次封装，
简化 `DefaultAlipayClient` 的配置并支持多应用无缝切换。

## 使用步骤

1. [添加相关依赖](../maven.md)
2. [获取支付宝应用配置参数](../config/alipay_config.md)
3. [初始化支付宝](init.md)
4. 调用 [AliPayApi](https://gitee.com/javen205/IJPay/blob/master/IJPay-AliPay/src/main/java/com/ijpay/alipay/AliPayApi.java) 
中的接口
5. 根据接口传入对应的 Model 来构建请求参数，Model 对应的接口参数下面有提供列表参考

## 特殊情况

蚂蚁金服开放平台提供的 [Java SDK](https://github.com/alipay/alipay-sdk-java-all) 里面接口非常多，而在 IJPay 中只封装了常用支付方式涉及的接口，如果你有其他接口需求 IJPay 中恰好并没有提供封装时你可以这么做。

::: tip 
1、获取 DefaultAlipayClient 
::: 

通过 `AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient()` 来获取当前线程中 appId 对应的 `DefaultAlipayClient`

::: tip 
2、执行请求完成接口调用
:::

再使用获取到的 `DefaultAlipayClient` 来执行请求调用，使用方法可以参考 
[IJPay 源码中的 AliPayApi](https://gitee.com/Javen205/IJPay/blob/master/IJPay-AliPay/src/main/java/com/ijpay/alipay/AliPayApi.java)
以及下方的示例

::: tip 
[Java SDK](https://github.com/alipay/alipay-sdk-java-all)  中常用的接口
:::

- 功能：执行请求调用（适用于不需要授权接口调用）
- 输入：request 接口请求对象
- 输出：T 请求返回对象
```java
public <T extends AlipayResponse> T execute(AlipayRequest<T> request);
```

- 功能：执行请求调用（适用于需要授权接口调用）
- 输入：
    - request 接口请求对象
    - accessToken 接口令牌
    - appAuthToken  商户授权令牌
- 输出：T 请求返回对象
```java
public <T extends AlipayResponse> T execute(AlipayRequest<T> request, String accessToken,String appAuthToken)
```


## 官方 API 列表

[官方支付 API 列表](https://docs.open.alipay.com/api_1)

## 支付常用 API 列表

| API列表                                                      | 描述                       | Model                              |
| :----------------------------------------------------------- | :------------------------- | ---------------------------------- |
| [alipay.trade.close](https://docs.open.alipay.com/api_1/alipay.trade.close) | 统一收单交易关闭接口       | AlipayTradeCloseModel              |
| [alipay.trade.create](https://docs.open.alipay.com/api_1/alipay.trade.create) | 统一收单交易创建接口       | AlipayTradeCreateModel             |
| [alipay.trade.pay](https://docs.open.alipay.com/api_1/alipay.trade.pay) | 统一收单交易支付接口       | AlipayTradePayModel                |
| [alipay.trade.refund](https://docs.open.alipay.com/api_1/alipay.trade.refund) | 统一收单交易退款接口       | AlipayTradeRefundModel             |
| [alipay.trade.fastpay.refund.query](https://docs.open.alipay.com/api_1/alipay.trade.fastpay.refund.query) | 统一收单交易退款查询       | AlipayTradeFastpayRefundQueryModel |
| [alipay.trade.page.refund](https://docs.open.alipay.com/api_1/alipay.trade.page.refund) | 统一收单退款页面接口       | AlipayTradePageRefundModel         |
| [alipay.trade.wap.pay](https://docs.open.alipay.com/api_1/alipay.trade.wap.pay) | 手机网站支付接口2.0        | AlipayTradeWapPayModel             |
| [alipay.trade.precreate](https://docs.open.alipay.com/api_1/alipay.trade.precreate) | 统一收单线下交易预创建     | AlipayTradePrecreateModel          |
| [alipay.trade.cancel](https://docs.open.alipay.com/api_1/alipay.trade.cancel) | 统一收单交易撤销接口       | AlipayTradeCancelModel             |
| [alipay.trade.order.settle](https://docs.open.alipay.com/api_1/alipay.trade.order.settle) | 统一收单交易结算接口       | AlipayTradeOrderSettleModel        |
| [alipay.trade.query](https://docs.open.alipay.com/api_1/alipay.trade.query) | 统一收单线下交易查询       | AlipayTradeQueryModel              |
| [alipay.trade.app.pay](https://docs.open.alipay.com/api_1/alipay.trade.app.pay) | app支付接口2.0             | AlipayTradeAppPayModel             |
| [alipay.trade.page.pay](https://docs.open.alipay.com/api_1/alipay.trade.page.pay) | 统一收单下单并支付页面接口 | AlipayTradePagePayModel            |


::: tip 
有规律可循: 由上列表不难得出以下规律
:::

接口名称「首字母」转为大写后面添加 Model 或者 Request 就是接口参数对应的 Model 以及 Request

例如：[alipay.trade.close](https://docs.open.alipay.com/api_1/alipay.trade.close) 
对应的 Model 为 `AlipayTradeCloseModel` ,对应的 request 为 `AlipayTradeCloseRequest`

最终封装的接口

```java{5,8}
public static AlipayTradeCloseResponse tradeCloseToResponse(AlipayTradeCloseModel model, String appAuthToken) throws AlipayApiException {
    AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
    request.setBizModel(model);
    if (StrUtil.isBlank(appAuthToken)) {
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    } else {
        // 支持第三方代理调用
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, null, appAuthToken);
    }
}
```



## 常用支付方式

| API列表                                                      | 支付方式             |
| ------------------------------------------------------------ | -------------------- |
| [alipay.trade.app.pay](https://docs.open.alipay.com/api_1/alipay.trade.app.pay) | APP 支付             |
| [alipay.trade.page.pay](https://docs.open.alipay.com/api_1/alipay.trade.page.pay) | PC 网站支付          |
| [alipay.trade.wap.pay](https://docs.open.alipay.com/api_1/alipay.trade.wap.pay) | 手机网站支付         |
| [alipay.trade.precreate](https://docs.open.alipay.com/api_1/alipay.trade.precreate) | 扫码支付             |
| [alipay.trade.pay](https://docs.open.alipay.com/api_1/alipay.trade.pay) | 条形码支付、声波支付 |


## 总结

看到这里我想大家都理解了 IJPay 中对支付宝封装的原理。至于如何调用接口进行支付应该不用再过多介绍了吧。

示例源码请参考 
- [IJPay-Demo](https://gitee.com/javen205/IJPay/tree/master/IJPay-Demo)
- [2.0.0 以下版本示例](https://gitee.com/javen205/IJPay-Demo)


