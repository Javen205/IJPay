---
home: true
#heroImage: /hero.png
actionText: 快速上手 →
actionLink: /guide/
features:
- title: 简洁至上
  details: 以 Module 为中心的项目结构，简洁方便易扩展，总包体不超过 2M。
- title: 开箱即用
  details: 不依赖任何第三方 MVC 框架，仅仅作为工具使用，简单快速完成支付模块的开发，可轻松嵌入到任何系统里。
- title: 渠道丰富
  details: 支持微信支付、QQ钱包支付、支付宝支付、银联支付、京东支付等。
footer: Apache License 2.0 | Copyright © 2019-present Javen
---

<p align="center">
     <a target="_blank" href="https://gitee.com/javen205/IJPay">
     	<img src="https://gitee.com/javen205/IJPay/badge/star.svg?theme=white" ></img>
     </a>
     <a target="_blank" href="https://github.com/Javen205/IJPay">
        <img src="https://img.shields.io/github/stars/Javen205/IJPay.svg?style=social&label=Stars" ></img>
     </a>
     <a target="_blank" href="https://www.apache.org/licenses/LICENSE-2.0">
        <img src="https://img.shields.io/badge/License-Apache--2.0-brightgreen.svg" ></img>
     </a>
     <a target="_blank" href="https://maven-badges.herokuapp.com/maven-central/com.github.javen205/IJPay">
        <img src="https://maven-badges.herokuapp.com/maven-central/com.github.javen205/IJPay/badge.svg" ></img>
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
 当然，不同的支付方式也可以单独添加相关依赖 [了解更多](./guide/maven.md)
:::

### 全民云计算

[云主机低至2折](https://promotion.aliyun.com/ntms/yunparter/invite.html?userCode=aam58met)

### 广而告之

- `TNW` 微信公众号开发脚手架：[https://gitee.com/javen205/TNW](https://gitee.com/javen205/TNW)
- SpringBoot 微服务高效开发 `mica` 工具集：[https://gitee.com/596392912/mica](https://gitee.com/596392912/mica)
- `Avue` 一款基于 vue 可配置化的神奇框架：[https://gitee.com/smallweigit/avue](https://gitee.com/smallweigit/avue)
- `pig` 宇宙最强微服务（架构师必备）：[https://gitee.com/log4j/pig](https://gitee.com/log4j/pig)
- `SpringBlade` 完整的线上解决方案（企业开发必备）：[https://gitee.com/smallc/SpringBlade](https://gitee.com/smallc/SpringBlade)


<script>
export default {
  mounted () {
    let xmlHttp = new XMLHttpRequest();
    xmlHttp.open("GET", "https://img.shields.io/maven-central/v/com.github.javen205/IJPay.json", false);
    xmlHttp.send(null);
    let versionInfo = JSON.parse(xmlHttp.responseText).value.replace('v', '');
    let codeNodeList = document.querySelectorAll('code');
    for (let i = 0; i < codeNodeList.length; i++) {
        codeNodeList[i].innerHTML = codeNodeList[i].innerHTML.replace('latest-version', versionInfo);
    }
  }
}
</script>