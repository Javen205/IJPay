---
home: true
#heroImage: /hero.png
actionText: 快速上手 →
actionLink: /guide/
features:
- title: 简洁至上
  details: 以 Module 为中心的项目结构，简洁方便易扩展，发布后总包体不超过 2M。
- title: 开箱即用
  details: 不依赖任何第三方 MVC 框架，仅仅作为工具使用，简单快速完成支付模块的开发，可轻松嵌入到任何系统里。
- title: 渠道丰富
  details: 支持微信支付、QQ钱包支付、支付宝支付、银联支付、京东支付、PayPal、Stripe、富友支付等。
- title: 极简入门
  details: IJPay 一直以简洁至上、开箱即用为核心，提供完整示例简单修改配置即可使用。交流群：723992875  
- title: 微信支付
  details: 支持多应用多商户，支持普通商户模式与服务商商模式当然也支持境外、同时支持 Api-v3 与 Api-v2 版本的接口。
- title: 支付宝支付
  details: 支持多应用，签名同时支持普通公钥方式与公钥证书方式
- title: 银联支付
  details: 全渠道扫码支付、微信 App 支付、公众号&小程序支付、银联 JS 支付、支付宝服务窗支付
- title: PayPal 支付
  details: 自动管理 AccessToken，极速接入各种常用的支付方式
- title: Stripe 支付
  details: 支持 PaymentIntent、Checkout 托管收银台、Customer、订阅等接口，Webhook 验签开箱即用
- title: 富友支付
  details: 富友聚合支付，覆盖公众号、小程序、APP、扫码、云闪付等交易方式
- title: 个人微信支付
  details: 微信个人商户，最低费率 0.38%，官方直连的异步回调通知
- title: 更多支付方式
  details: 敬请期待，接定制开发
footer: Apache License 2.0 | Copyright © 2019-present Javen
---

<p align="center">
     <a target="_blank" href="https://gitee.com/javen205/IJPay">
     	<img src="https://gitee.com/javen205/IJPay/badge/star.svg?theme=white" ></img>
     </a>
     <a target="_blank" href="https://github.com/Javen205/IJPay">
        <img src="https://img.shields.io/github/stars/Javen205/IJPay.svg?style=social&label=Stars" ></img>
     </a>
     <a target="_blank" href="https://github.com/Javen205/donate">
        <img src="https://img.shields.io/badge/Donate-WeChat-%23ff3f59.svg" ></img>
     </a> 
</p>

### 当前最新版本

IJPay 3.x 的所有模块共用同一个版本号，核心支付模块的坐标在 JDK 8 与 JDK 8 以上完全一致：

``` xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-WxPay</artifactId>
    <version>latest-version</version>
</dependency>
```

需要一次性引入所有支付方式时使用 `IJPay-All`：

``` xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-All</artifactId>
    <version>latest-version</version> 
</dependency>
```

#### JDK 8（javax.servlet，Servlet 4.0）

当代码需要 `HttpServletRequest` / `HttpServletResponse`（页面支付、读取回调报文、解析真实 IP）时，额外引入 javax 适配模块：

``` xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-Servlet-Javax</artifactId>
    <version>latest-version</version>
</dependency>
```

适用于 Spring Boot 2.x、Tomcat 9 及以下、JFinal、Solon 等 javax 容器。

#### JDK 17+（jakarta.servlet，Servlet 5.0）

同样的场景改用 jakarta 适配模块：

``` xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-Servlet-Jakarta</artifactId>
    <version>latest-version</version>
</dependency>
```

适用于 Spring Boot 3.x、Tomcat 10+、Jakarta EE 9+ 容器。也可以引入 pom 聚合依赖 `IJPay-Jakarta`（Maven 中必须声明 `<type>pom</type>`），一次带入常用支付模块与 jakarta 适配。

#### 纯验签 / 下单等容器无关场景

只做签名、验签、下单、查询时，**不需要**引入任何 Servlet 适配模块：`IJPay-Core`、`IJPay-WxPay`、`IJPay-AliPay` 等核心模块自身不依赖 Servlet API。

::: tip
完整的 JDK / Servlet 对应关系见[支持矩阵](./guide/jdk/support-matrix.md)，2.x 升级到 3.x 的变更见[升级指南](./guide/jdk/migration-2.x-to-3.x.md)，Spring Boot 2.x / 3.x 的接入示例见[使用说明](./guide/jdk/usage.md)。不同支付方式单独引用的坐标见[安装依赖](./guide/maven.md)。
:::

### 全民云计算

[阿里云主机低至2折](https://www.aliyun.com/minisite/goods?userCode=b1hkzv2x)

[腾讯云服务器限时秒杀](https://curl.qcloud.com/iXclla9J)

[新用户专属福利](https://curl.qcloud.com/3AJFwXAt)

### 谁在使用？

<p align="left">
 	<a target="_blank" href="https://dwz.cn/P730KsQg">
        <img src="https://yungouos.oss-cn-shanghai.aliyuncs.com/YunGouOS/logo/merchant/logo-system.png" width="100" height="60" alt="jetbrains">
    </a>
 </p>


<script>
// import { Notification } from 'element-ui';
export default {
  mounted () {
    // 统计
    var hm = document.createElement("script");
    hm.src = "https://hm.baidu.com/hm.js?6a929f536123c72265ba5e8d9467ab5f";
    var s = document.getElementsByTagName("script")[0]; 
    s.parentNode.insertBefore(hm, s);
    
    // 替换 latest-version
    // let xmlHttp = new XMLHttpRequest();
    // xmlHttp.open("GET", "https://img.shields.io/maven-central/v/com.github.javen205/IJPay.json", false);
    // xmlHttp.send(null);
    // let versionInfo = JSON.parse(xmlHttp.responseText).value.replace('v', '');
    // let codeNodeList = document.querySelectorAll('code');
    // for (let i = 0; i < codeNodeList.length; i++) {
    //     codeNodeList[i].innerHTML = codeNodeList[i].innerHTML.replace('latest-version', versionInfo);
    // }
    
    // 通知
    this.$notify({
          offset: 50,
          title: '在线客服答疑',
          message: '按问题付费或者加入 VIP，提供在线一对一技术支持。交流群：723992875',
          type: 'success',
          showClose: true,
          duration: 0,
          onClick: function() {
            // Notification.closeAll();
            window.open("https://javen205.gitee.io/ijpay/guide/donate/");
          }
        });
    // this.$notify({
    //   offset: 50,
    //   title: 'IJPay VIP 服务',
    //   message: 'VIP 服务可提供一对一在线答疑，加入 VIP 为您节省更多时间去陪恋人、家人以及朋友 :) 交流群：723992875',
    //   type: 'success',
    //   showClose: true,
    //   duration: 0,
    //   onClick: function() {
    //     // Notification.closeAll();
    //     window.open("https://javen205.gitee.io/ijpay/guide/donate/");
    //   }
    // });
    
    // this.$notify({
    //   offset: 200,
    //   title: 'TNWX 微信系开发脚手架',
    //   message: '同时支持微信公众号、微信小程序、企业微信、企业微信开放平台、微信支付、微信小游戏。可接入到任何 Node.js 框架(Express、Nest、Egg、Koa 等)',
    //   type: 'success',
    //   showClose: true,
    //   duration: 0,
    //   onClick: function() {
    //     window.open("https://gitee.com/Javen205/TNWX");
    //   }
    // });
  }
}
</script>


<Q url="tencent://message/?uin=572839485&Site=%E5%AE%A2%E6%9C%8D&Menu=yes" />
