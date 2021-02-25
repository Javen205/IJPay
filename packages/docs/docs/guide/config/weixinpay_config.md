# 获取微信支付相关配置

## 支付账户

[支付账户以及商户专有名词介绍](https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=3_1)

## 普通商户模式与服务商模式区别

相对于开发而言，普通商户模式与服务商模式最大的区别就是接口的部分请求参数不同而接口是相同的。

## 普通商户模式

::: tip IJPay 中微信支付需要配置的参数如下：
- appId: 应用编号
- appSecret: appSecret 是 appId 对应的接口密码，微信公众号授权获取用户 openId 时使用
- mchId: 微信支付商户号
- partnerKey: API 密钥，微信商户后台配置
- certPath: `apiclient_cert.p12` 证书绝对路径，在微信商户后台下载
- domain: 外网访问项目的域名，支付通知、回调中会使用
:::

## 服务商模式

::: tip IJPay 中微信支付需要配置的参数如下：
- appId: 服务商的应用编号
- subAppId: 子商户号的应用编号
- appSecret: 服务商的 appSecret
- subAppSecret: 子商户的appSecret
- mchId: 微信支付商户号
- subMchId: 子商户号
- partnerKey: API 密钥，服务商微信商户后台配置
- certPath: `apiclient_cert.p12` 证书绝对路径，在服务商微信商户后台下载
- domain: 外网访问项目的域名，支付通知、回调中会使用
:::

以上配置并非全部必须，可以根据实际情况来决定。服务商模式下接口请求参数
`openid` 与 `sub_openid` 可以二选一，如果使用了 `openid` 即使用服务商下的应用进行授权就需要提供服务商的应用配置 `appId` 以及 `appSecret`，
反之就使用子商户对应的应用配置

## v3 版本配置说明

::: tip IJPay 中微信支付需要配置的参数如下：
- keyPath= apiclient_key.pem  商户 key 绝对路径
- certPath= capiclient_cert.pem 商户证书绝对路径
- certP12Path= apiclient_cert.p12 证书绝对路径，v3 暂未使用到，退款 v3 暂未支持需使用 v2 接口
- platformCertPath= wx_cert.pem  微信平台证书绝对路径，证书需要通过接口获取，请参考 [v3 文档](../wxpay/api-v3.md)
- mchId= 微信商户号
- apiKey3= Api-v3 密钥
- apiKey= Api 密钥 备用参数 v3 暂未使用到
- domain= 外网访问项目的域名，支付通知、回调中会使用
:::

<Q url="tencent://message/?uin=572839485&Site=%E5%AE%A2%E6%9C%8D&Menu=yes" />


