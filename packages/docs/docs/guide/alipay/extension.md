# 扩展接口

## 说明

支付宝支付中涉及到的接口非常多，如果 IJPay 中有没有实现某个接口大家可自行扩展，同时也欢迎 PR

## 封装 AliPayClient 常用的执行方法

如何使用？ 请参考源码或者看下方示例

```java ()
public static <T extends AlipayResponse> T execute(AlipayRequest<T> request) throws AlipayApiException {
    return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
}

public static <T extends AlipayResponse> T execute(AlipayRequest<T> request, String authToken) throws AlipayApiException {
    return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, authToken);
}

public static <T extends AlipayResponse> T execute(AlipayRequest<T> request, String accessToken, String appAuthToken) throws AlipayApiException {
    return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, accessToken, appAuthToken);
}

public static <T extends AlipayResponse> T execute(AlipayRequest<T> request, String accessToken, String appAuthToken, String targetAppId) throws AlipayApiException {
    return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, accessToken, appAuthToken, targetAppId);
}

public static <T extends AlipayResponse> T pageExecute(AlipayRequest<T> request) throws AlipayApiException {
    return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().pageExecute(request);
}

public static <T extends AlipayResponse> T pageExecute(AlipayRequest<T> request, String method) throws AlipayApiException {
    return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().pageExecute(request, method);
}

public static <T extends AlipayResponse> T sdkExecute(AlipayRequest<T> request) throws AlipayApiException {
    return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().sdkExecute(request);
}

public static BatchAlipayResponse execute(BatchAlipayRequest request) throws AlipayApiException {
    return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
}

public static <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request) throws AlipayApiException {
    return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().certificateExecute(request);
}

public static <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request, String authToken) throws AlipayApiException {
    return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().certificateExecute(request, authToken);
}

public static <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request, String accessToken, String appAuthToken) throws AlipayApiException {
    return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().certificateExecute(request, accessToken, appAuthToken);
}

public static <T extends AlipayResponse> T certificateExecute(AlipayRequest<T> request, String accessToken, String appAuthToken, String targetAppId) throws AlipayApiException {
    return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().certificateExecute(request, accessToken, appAuthToken, targetAppId);
}
```        

## 账户余额查询

官方文档 [API 列表](https://docs.open.alipay.com/309/106237/) 

```java ()
/**
 * 支付宝资金账户资产查询接口
 *
 * @param model        model {@link AlipayFundAccountQueryModel}
 * @param appAuthToken 应用授权token
 * @return {@link AlipayFundAccountQueryResponse}
 * @throws AlipayApiException 支付宝 Api 异常
 */
public static AlipayFundAccountQueryResponse accountQueryToResponse(AlipayFundAccountQueryModel model, String appAuthToken) throws AlipayApiException {
    AlipayFundAccountQueryRequest request = new AlipayFundAccountQueryRequest();
    request.setBizModel(model);
    if (!StringUtils.isEmpty(appAuthToken)) {
        request.putOtherTextParam("app_auth_token", appAuthToken);
    }
    if (AliPayApiConfigKit.getAliPayApiConfig().isCertModel()) {
        return certificateExecute(request);
    } else {
        return execute(request);
    }
}
```       

## 如何使用？

使用前请初始化客户端，请参考[支付宝初始化](../alipay/init.md)

```java ()
@RequestMapping(value = "/accountQuery")
@ResponseBody
public String accountQuery(@RequestParam("aliPayUserId") String aliPayUserId) {
    AlipayFundAccountQueryModel model = new AlipayFundAccountQueryModel();
    model.setAlipayUserId(aliPayUserId);
    model.setAccountType("ACCTRANS_ACCOUNT");
    try {
        return AliPayApi.accountQueryToResponse(model,null).getBody();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
```