# 微信支付常见问题

## 本地开发与调试

:::tip
本地是可以直接调试的，如果要处理异步通知可以做本地端口映射，将本地服务映射到外网。

常用的方案 [Ngrok](https://ngrok.com/)、[FRP](https://github.com/fatedier/frp)

如果自己搭建([参考资料](https://www.jianshu.com/p/c0d7cb4cb00f))，首页需要一台自己的云服务器([云主机低至2折](https://promotion.aliyun.com/ntms/yunparter/invite.html?userCode=b1hkzv2x))
再按照官方提供的文档搭建即可。当然市面上也有一些基于 [Ngrok](https://ngrok.com/)、[FRP](https://github.com/fatedier/frp)搭建的完整的收费的产品
:::

## 提示大量的 class 找不到

:::tip
解决方案请参考此 [issues](https://gitee.com/javen205/IJPay/issues/I13NOO)
:::

## 空指针异常

:::tip
这种情况一般是没有初始化导致的，请参考[支付宝初始化](../alipay/init.md)
:::

## 微信异步退款解密异常

:::warning
java.security.InvalidKeyException: Illegal key size
:::

:::tip 解决方案
去官方下载 JCE 无限制权限策略文件。

- JDK6的[下载地址](http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html)
- JDK7的[下载地址](http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html)
- JDK8的[下载地址](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)

下载后解压，可以看到 `local_policy.jar ` 和 `US_export_policy.jar `以及 `readme.txt`。
如果安装了JRE，将两个 jar 文件放到 `%JRE_HOME%\lib\security` 下覆盖原来文件，记得先备份。
如果安装了JDK，将两个 jar 文件也放到 `%JDK_HOME%\jre\lib\security` 下。
:::


## 微信 H5 支付常见问题

[官方常见问题说明](https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=15_4)

:::tip
更多讨论，参考之前写的博客 [微信H5支付](https://javen.blog.csdn.net/article/details/77507835)
:::

## 扫码支付常见问题


## 微信公众号支付常见问题

[官方常见问题说明](https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_9&index=8)

:::warning 
特别注意第三点！！！最容易忘记配置 JSAPI 支付的授权目录！！！扫码模式一也是如此，需要配置「扫码回调链接」
配置步骤请参考官方文档
- [公众号支付](https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=7_3)
- [扫码模式一](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_3)
:::






