# IJPay 中的银联支付

银联支付对接的是[「条码支付综合前置平台」](https://up.95516.com/open)

实现原理：微信服务商模式、银行服务商模式

## 支付配置

::: tip IJPay 中微信支付需要配置的参数如下：
- union.machId = 平台分配的商户号
- union.key = 平台分配的密钥
- union.serverUrl = 请求接口 例如：https://qra.95516.com/pay/gateway
- union.domain = 外网访问项目的域名，支付通知中会使用
:::

## 如何使用？

以刷卡支付为例

```java{16,27,31,33,35,43}
/**
 * 刷卡支付
 */
@RequestMapping(value = "/microPay", method = {RequestMethod.POST, RequestMethod.GET})
@ResponseBody
public AjaxResult microPay(HttpServletRequest request, HttpServletResponse response) {
    try {
        String authCode = request.getParameter("authCode");
        String totalFee = request.getParameter("totalFee");

        String ip = IpKit.getRealIp(request);
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        // 构建请求参数
        Map<String, String> params = MicroPayModel.builder()
                .service(ServiceEnum.MICRO_PAY.toString())
                .mch_id(unionPayBean.getMachId())
                .out_trade_no(WxPayKit.generateStr())
                .body("IJPay 云闪付测试")
                .attach("聚合支付 SDK")
                .total_fee(totalFee)
                .mch_create_ip(ip)
                .auth_code(authCode)
                .nonce_str(WxPayKit.generateStr())
                .build()
                .createSign(unionPayBean.getKey(), SignType.MD5);

        logger.info("请求参数:" + JSONUtil.toJsonStr(params));

        String xmlResult = UnionPayApi.execution(unionPayBean.getServerUrl(), params);
        logger.info("xmlResult:" + xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        // 验证签名
        if (!WxPayKit.verifyNotify(result, unionPayBean.getKey(), SignType.MD5)) {
            return new AjaxResult().addError("签名异常");
        }
        String returnCode = result.get("status");
        String resultCode = result.get("result_code");
        String errMsg = result.get("err_msg");
        String errCode = result.get("err_code");
        // 判断支付状态
        if (!"0".equals(returnCode) || !"0".equals(resultCode)) {
            return new AjaxResult().addError("errCode:" + errCode + " errMsg:" + errMsg);
        }
        return new AjaxResult().success(result);
    } catch (Exception e) {
        e.printStackTrace();
        return new AjaxResult().addError(e.getMessage());
    }
}
```     
::: tip
- 16行 通过指定的 Model 构建请求参数。其他 Model 请参数 [IJPay 源码](https://gitee.com/javen205/IJPay/tree/master/IJPay-UnionPay/src/main/java/com/ijpay/unionpay/model)
- 27行 生成签名，目前银联条码前置所有接口只支持 MD5 加密
- 31行 发起请求
- 33行 将返回的 xml 转为 Map 方便获取返回参数
- 35行 验证签名
- 43行 判断支付状态，是否支付成功请参考[官方文档](https://up.95516.com/open)
:::

## 完整示例
 
- [IJPay-Demo-SpringBoot](https://gitee.com/javen205/IJPay/blob/master/IJPay-Demo-SpringBoot/src/main/java/com/ijpay/demo/controller/unionpay/UnionPayController.java)