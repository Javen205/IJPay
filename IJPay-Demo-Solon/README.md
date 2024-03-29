# IJPay-Demo-Solon

如果该库对你有帮助不妨右上角点点 Star 或者任意赞助支持，我更喜欢你 Fork PR 成为项目贡献者。


IJPay 一直以简洁至上、开箱即用为核心。提供的 IJPay-Demo 也在简化使用流程做到简单修改配置就能用，同时也鼓励用户与 IJPay 「亲密接触」。

如果项目周期短不想投入过多时间和精力在接入上面或者实在为技术感到头疼，那么请考虑一下 IJPay 提供的 VIP 服务。

[IJPay VIP 服务说明](https://javen205.gitee.io/ijpay/guide/donate)


## IJPay-Demo 使用步骤

- 默认使用 `resources/dev` 下的配置，如果没有请复制 `resources/production` 并修改为 `dev`
  如果不使用对环境配置可以直接删除直接将配置文件放在 resources 根目录中，并删除 pom.xml 中的 profiles 以及 resources
- 修改 dev 下不同支付方式的属性文件。
  1. 属性文件配置详细介绍请参考 [IJPay 文档](https://javen205.gitee.io/ijpay/guide/config/alipay_config.html)
  2. 属性文件乱码解决方案 [IDE中显示 *.properties 为中文](https://javen.blog.csdn.net/article/details/77487645)
- 运行 `com.ijpay.demo.DemoApplication` 中的 `main` 方法启动项目
- 前端页面 `com.ijpay.demo.controller.IndexController`

>注意: 如果你有使用银联支付就需要单独添加银联支付的相关依赖。 
>为什么要这么做？请参考 [引入依赖 Maven 打包报错](https://gitee.com/javen205/IJPay/issues/I12WOD)


## 全民云计算

[云主机低至2折](https://promotion.aliyun.com/ntms/yunparter/invite.html?userCode=b1hkzv2x)

[云服务首年 88](https://cloud.tencent.com/act/cps/redirect?redirect=1048&cps_key=a21676d22e4b11a883893d54e158c1d3&from=console)


## 官方文档

IJPay 更多使用技巧请参考 [IJPay 文档](https://javen205.gitee.io/ijpay)
