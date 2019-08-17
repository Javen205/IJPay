# 获取支付宝相关配置

## 支付宝配置说明

::: tip IJPay 中支付宝支付需要配置的参数如下：
- appId: 应用编号
- privateKey: 应用私钥
- publicKey: 支付宝公钥，通过应用公钥上传到支付宝开放平台换取支付宝公钥。
- serverUrl: 支付宝支付网关
- domain: 外网访问项目的域名，支付通知中会使用
:::

1、[创建应用](https://docs.open.alipay.com/200/105310)，如果使用 [支付宝沙箱环境](https://docs.open.alipay.com/200/105311/) 
可以跳过此步骤，系统已经自动为你创建一个应用。

2、生成 RSA 密钥 (应用私钥、应用公钥)，请务必使用 2048 位。
 
- [工具下载以及工具使用步骤](https://docs.open.alipay.com/291/105971)
- [官方演示视频](https://docs.open.alipay.com/291/106103)
    
3、使用沙箱环境时将 `serverUrl` 设置为 `https://openapi.alipaydev.com/gateway.do` 
使用正式环境时设置为 `https://openapi.alipay.com/gateway.do`

4、如果你是本地调试 `domain 外网访问项目的域名` 可以使用外网端口映射相关的工具来处理

- [10分钟搭建属于自己的ngork服务器，实现内网穿透](https://www.jianshu.com/p/b81bb6a3c0b9)
- [搭建属于自己的网穿透工具](https://www.jianshu.com/p/c0d7cb4cb00f)


## 扩展阅读

[RSA 和 RSA2 签名算法区别](https://docs.open.alipay.com/291/106115)

