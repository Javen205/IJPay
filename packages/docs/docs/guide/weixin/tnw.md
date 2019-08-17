# Node.js 版本微信公众号开发脚手架

<p align="center">
  <a href="https://www.npmjs.com/package/tnw" target="blank"><img src="https://gitee.com/javen205/TNW/raw/master/docs/img/logo.png" width="410" alt="TNW Logo" /></a>
</p>
<p align="center">
    <a href="https://www.npmjs.com/package/tnw" target="_blank"><img src="https://img.shields.io/npm/l/tnw.svg?style=flat-square" alt="Package License" /></a>
    <a href="https://www.npmjs.com/package/tnw" target="_blank"><img src="https://img.shields.io/npm/v/tnw.svg?style=flat-square" alt="NPM Version" /></a>
    <a href="https://www.npmjs.com/package/tnw" target="_blank"><img src="https://img.shields.io/npm/dt/tnw.svg?style=flat-square" alt="NPM Downloads" /></a>
    <a href="https://github.com/Javen205/donate" target="_blank"><img src="https://img.shields.io/badge/Donate-WeChat-%23ff3f59.svg?style=flat-square" alt="Donate" /></a>
</p>


## 简介

**TNW: TypeScript(The) + Node.js(Next) + WeChat 微信公众号开发脚手架，支持 http 模块扩展、支持任何 Node.js 的服务端框架(Express、Nest、Egg、Koa 等)**

<!-- 本项目是我研究 TypeScript + Node.js 的阶段性总结，因为我对 [微信支付](https://gitee.com/javen205/IJPay)、[公众号](https://mp.weixin.qq.com/wiki)、[小程序/小游戏](https://developers.weixin.qq.com/miniprogram/dev/index.html) 有一定的研究，之前也参与过 [jfinal-weixin](https://gitee.com/jfinal/jfinal-weixin) 的开发，所以以微信系为载体再好不过了，后面也会扩展对支付、小程序甚至小游戏相关接口的支持，感谢您的关注:) -->

## 开发进度

- 多公众号支持
- 各种消息交互
- 自定义菜单
- 模板消息
- 客服消息
- 用户管理
- 生带参数的二维码
- 长连接转短连接
- 微信网页授权
- 一次性订阅消息
- 素材管理
- 群发消息
- 数据统计
- 微信卡券
- 微信门店
- 微信摇一摇
- 微信支付
- [待完成] 微信连WiFi
- [待完成] 微信扫一扫
- [待完成] 微信发票
- [待完成] 微信设备功能
- [ ] ....

## TNW 博客

- [TNW-开启公众号开发者模式](https://my.oschina.net/zyw205/blog/3038343)
- [TNW-微信公众号各种消息交互](https://my.oschina.net/zyw205/blog/3043428)
- [TNW-获取公众号的 access_token](https://my.oschina.net/zyw205/blog/3044608)
- [TNW-微信公众号发送模板消息](https://my.oschina.net/zyw205/blog/3044716)
- [TNW-微信公众号自定义菜单](https://my.oschina.net/zyw205/blog/3045638)
- [TNW-微信公众号中如何使用JSSDK](https://my.oschina.net/zyw205/blog/3046798)
- [TNW-授权获取用户信息](https://my.oschina.net/zyw205/blog/3093807)

 [开源中国](https://www.oschina.net/p/TNWX)
 [CSDN](https://javen.blog.csdn.net/article/category/6665009)
 [掘金](https://juejin.im/user/57caa559a22b9d006b95af93/posts)
 [简书](https://www.jianshu.com/u/9be31238fda1)

## 安装与运行

### NPM 依赖方式

1、下载

```bash
$ npm init -y
$ npm i tnw
```

2、完整示例

- [Gitee 请点击这里](https://gitee.com/Javen205/TNW/tree/master/example/js)
- [Github 请点击这里](https://github.com/Javen205/TNW/tree/master/example/js)

### 源码方式

1、下载项目并安装依赖

```bash
$ git clone https://github.com/Javen205/TNW.git
或者
$ git clone https://gitee.com/Javen205/TNW.git
$ cd TNW
$ npm install
```

2、编译并运行

```bash
$ npm run build
$ npm run dev
```

3、完整示例

- [Gitee 请点击这里](https://gitee.com/Javen205/TNW/tree/master/src/example)
- [GitHub 请点击这里](https://github.com/Javen205/TNW/tree/master/src/example)

4、 Nest 示例

- [Gitee 请点击这里](https://gitee.com/Javen205/TNW/tree/master/example/nest)
- [GitHub 请点击这里](https://github.com/Javen205/TNW/tree/master/example/nest)


## 交流群

群号:[ 114196246](https:shang.qq.com/wpa/qunwpa?idkey=a1e4fd8c71008961bd4fc8eeea224e726afd5e5eae7bf1d96d3c77897388bf24)

## 开源推荐

- `IJPay` 让支付触手可及：https://gitee.com/javen205/IJPay
- SpringBoot 微服务高效开发 `mica` 工具集：https://gitee.com/596392912/mica
- `Avue` 一款基于 vue 可配置化的神奇框架：https://gitee.com/smallweigit/avue
- `pig` 宇宙最强微服务（架构师必备）：https://gitee.com/log4j/pig
- `SpringBlade` 完整的线上解决方案（企业开发必备）：https://gitee.com/smallc/SpringBlade
