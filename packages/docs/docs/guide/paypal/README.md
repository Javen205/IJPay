# IJPay 中的 PayPal 支付

## 官方参考文档

- [创建沙箱应用与账号](https://developer.paypal.com/docs/api/overview/#create-sandbox-accounts)
- [获取 AccessToken](https://developer.paypal.com/docs/api/overview/#get-an-access-token)


## 添加模块依赖

[添加模块依赖](../maven.md)

## 配置说明 

::: tip IJPay 中 PayPal 支付需要配置的参数如下：
- clientId: 应用编号
- secret: 应用密钥 
- sandBox: 是否是沙箱环境
- domain: 外网访问项目的域名，回调中会使用
:::

## 实例化配置

```java {}
public PayPalApiConfig getConfig() {
    PayPalApiConfig config = new PayPalApiConfig();
    config.setClientId(payPalBean.getClientId());
    config.setClientSecret(payPalBean.getSecret());
    config.setSandBox(payPalBean.getSandBox());
    config.setDomain(payPalBean.getDomain());
    PayPalApiConfigKit.setThreadLocalApiConfig(config);
    return config;
}
```
## 获取 AccessToken 

::: tip 默认获取 AccessToken 策略如下:
- 如获取失败将会重试，三次失败后将返回空
- 默认使用 Map 保存在内存中
:::

### 修改缓存策略

```java {}
AccessTokenKit.setCache(new IAccessTokenCache() {
    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public void set(String key, String jsonValue) {

    }

    @Override
    public void remove(String key) {

    }
});
```

### 强制刷新与缓存 

```java {}  
// 从当前线程中获取应用配置并获取 AccessToken
AccessToken accessToken = AccessTokenKit.get(); 
// 从当前线程中获取应用配置并强制刷新 AccessToken
AccessToken accessToken = AccessTokenKit.get(true);
// 通过应用编号获取应用配置并获取 AccessToken
AccessToken accessToken = AccessTokenKit.get(config.getClientId());
// 通过应用编号获取应用配置并强制刷新 AccessToken 
AccessToken accessToken = AccessTokenKit.get(config.getClientId(),true); 
```

## 如何使用？

请参考 [JavaDoc 文档](https://apidoc.gitee.com/javen205/IJPay)或者看以下完整示例

## 完整示例
 
- [IJPay-Demo-SpringBoot](https://gitee.com/javen205/IJPay/tree/master/IJPay-Demo-SpringBoot)

<Q url="tencent://message/?uin=572839485&Site=%E5%AE%A2%E6%9C%8D&Menu=yes" />

