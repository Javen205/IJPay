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
  details: 支持微信支付(支持 Api v3)、QQ钱包支付、支付宝支付、银联支付、京东支付等。
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

``` xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-All</artifactId>
    <version>latest-version</version> 
</dependency>
```

::: tip
 2.3.2 版本开始 IJPay-All 中不会自动下载银联支付相关依赖，如有银联支付需求请单独手动添加 IJPay-UnionPay 相关依赖。
 
 当然，不同的支付方式也可以单独添加相关依赖 [了解更多](./guide/maven.md) 
:::

### 全民云计算

[阿里云主机低至2折](https://promotion.aliyun.com/ntms/yunparter/invite.html?userCode=b1hkzv2x)

[腾讯云服务器首年 88](https://cloud.tencent.com/act/cps/redirect?redirect=1048&cps_key=a21676d22e4b11a883893d54e158c1d3&from=console)

[华为云购买享受红利](https://activity.huaweicloud.com/discount_area_v5/index.html?&fromuser=aHcxMTc2NTU3MQ==&utm_source=aHcxMTc2NTU3MQ==&utm_medium=cps&utm_campaign=201905)

### 谁在使用？

<p align="left">
    <a left="100" target="_blank" href="https://pig4cloud.com?from=IJPay">
        <img src="https://pig4cloud.com/images/123123123.png" width="100" alt="">
    </a>
 	<a target="_blank" href="https://www.t-io.org?from=IJPay">
 	    <img src="https://res.t-io.org/img/logo/t-io.png" width="60" height="40" alt="jetbrains">
 	</a>
 	<a target="_blank" href="https://w.url.cn/s/ApeggTn">
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
    let xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "https://img.shields.io/maven-central/v/com.github.javen205/IJPay.json", false);
    xmlHttp.send(null);
    let versionInfo = JSON.parse(xmlHttp.responseText).value.replace('v', '');
    let codeNodeList = document.querySelectorAll('code');
    for (let i = 0; i < codeNodeList.length; i++) {
        codeNodeList[i].innerHTML = codeNodeList[i].innerHTML.replace('latest-version', versionInfo);
    }
    
    // 通知
    this.$notify({
      offset: 50,
      title: 'IJPay VIP 服务',
      message: 'VIP 服务可提供一对一在线答疑，加入 VIP 为您节省更多时间去陪恋人、家人以及朋友 :) 交流群：723992875',
      type: 'success',
      showClose: true,
      duration: 0,
      onClick: function() {
        // Notification.closeAll();
        window.open("https://javen205.gitee.io/ijpay/guide/donate/");
      }
    });
    
    this.$notify({
      offset: 200,
      title: 'TNWX 微信系开发脚手架',
      message: '同时支持微信公众号、微信小程序、企业微信、企业微信开放平台、微信支付、微信小游戏。可接入到任何 Node.js 框架(Express、Nest、Egg、Koa 等)',
      type: 'success',
      showClose: true,
      duration: 0,
      onClick: function() {
        window.open("https://gitee.com/Javen205/TNWX");
      }
    });
  }
}
</script>