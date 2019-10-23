
# 支付宝初始化

## 初始化客户端

```java{8,9,11,12}
AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
    .setAppId(aliPayBean.getAppId())
    .setAliPayPublicKey(aliPayBean.getPublicKey())
    .setCharset("UTF-8")
    .setPrivateKey(aliPayBean.getPrivateKey())
    .setServiceUrl(aliPayBean.getServerUrl())
    .setSignType("RSA2")
    .build(); // 普通公钥方式 
    .build(appCertPath, aliPayCertPath, aliPayRootCertPath) // 2.3.0 公钥证书方式  

AliPayApiConfigKit.setThreadLocalAppId(aliPayBean.getAppId()); // 2.1.2 之后的版本，可以不用单独设置 
AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
```

当然支付接口中也不用每次都设置 AliPayApiConfig 可以通过 `AliPayApiConfigKit.getApiConfig` 来获取 AliPayApiConfig


::: warning 注意
2.1.2 之后的版本，可以不用单独设置 `AliPayApiConfigKit.setThreadLocalAppId(aliPayBean.getAppId());` 
:::


## 多应用无缝切换

IJPay 中默认是使用当前线程中的 appId 对应的配置，如果要切换应用可以调用 `AliPayApiConfigKit.setThreadLocalAppId` 来设置当前线程中的
appId 实现应用的切换进而达到多应用的支持。

::: warning 注意
调用 `AliPayApiConfigKit.setThreadLocalAppId` 之前需要确保缓存中存在此 appId 对应的配置 AliPayApiConfig ，如果没有可以通过
`AliPayApiConfigKit.putApiConfig(aliPayApiConfig)` 或者 `AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig)` 
来添加，后者同时设置了当前线程中的 appId
:::



## 代码示例

多应用支持，JFinal 完整版示例代码

```java{20,26,29,30,31,32,33,34,35,36,37,42,43}
/**
 * @Email javendev@126.com
 * @author Javen
 */
public class AliPayApiInterceptor implements Interceptor {
    AlipayService alipayService = new AlipayService();
    AppService appService = new AppService();
    AjaxResult ajax = new AjaxResult();

    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        String appId = controller.getPara("appId");
        if (StrKit.isBlank(appId)) {
            controller.renderJson(ajax.addError("应用的编号不能为空"));
            return;
        }
        //判断应用是否存在
        App app = appService.getAppByAppId(appId, 1);
        AliPayApiConfig aliPayApiConfig = null;
        if (app !=null) {
            // 通过应用的appId查询支付宝的配置
            Alipay alipay = alipayService.getAlipayByAppId(appId);
            if (alipay != null) {
                try {
                    aliPayApiConfig = AliPayApiConfigKit.getApiConfig(alipay.getAppId());
                } catch (Exception e) {
                    LogKit.error("实例化AliPayApiConfig...");
                    // 如果Map中没有当前支付宝的实例就初始化并添加到Map中
                    aliPayApiConfig = AliPayApiConfig.New()
                            .setAppId(alipay.getAppId())
                            .setAlipayPublicKey(alipay.getPublicKey())
                            .setCharset(IJPayConsts.CHARSET)
                            .setPrivateKey(alipay.getPrivateKey())
                            .setServiceUrl(alipay.getServerUrl())
                            .setSignType(IJPayConsts.SIGN_TYPE)
                            .build();
                }
            }
        }
        if (aliPayApiConfig != null) {
            AliPayApiConfigKit.setThreadLocalAppId(aliPayApiConfig.getAppId());// 2.1.2 之后的版本，可以不用单独设置 
            AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
            controller.setAttr("app", app);
            inv.invoke();
        } else {
            LogKit.error("aliPayApiConfig is null");
            controller.renderJson(ajax.addError("此应用暂未配置支付宝支付的参数"));
        }
    }
}

```
