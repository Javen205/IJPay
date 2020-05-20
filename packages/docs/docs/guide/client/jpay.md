# Android-简易而不简单的支付 SDK

<p align="center">
	<a target="_blank" href="https://javen205.github.io"><img src="https://gitee.com/javen205/JPay/raw/master/assets/img/JPay.png" width="410"></a>
</p>

<p>
	<strong>对微信App支付、支付宝App支付、银联App支付的二次封装,对外提供一个相对简单的接口以及支付结果的回调。</strong>
</p>


<p align="center">
    <a href="https://www.apache.org/licenses/LICENSE-2.0"><img src="https://img.shields.io/badge/license-Apache%202-green.svg" alt="License" /></a>
    <a href="https://jitpack.io/#javen205/JPay"><img src="https://jitpack.io/v/Javen205/JPay.svg" alt="" /></a>
    <a href="http://javen.blog.csdn.net"><img src="https://img.shields.io/badge/JPay%20OR%20IJPay%20Author-Javen-ff69b4.svg" alt="JPay Author" /></a>
</p>


## 客户端与服务端

- GitHub: [https://github.com/Javen205/JPay](https://github.com/Javen205/JPay)
- Gitee: [http://gitee.com/Javen205/JPay](http://gitee.com/Javen205/JPay)

- GitHub: [https://github.com/Javen205/IJPay](https://github.com/Javen205/IJPay)
- Gitee: [http://gitee.com/Javen205/IJPay](http://gitee.com/Javen205/IJPay)

**联系方式**

[![QQ0Group][qq0GroupSvg]][qq0Group]
[![Email](https://img.shields.io/badge/Email-javendev%40126.com-yellowgreen.svg)](http://javen.blog.csdn.net)

[qq0GroupSvg]: https://img.shields.io/badge/QQ群-723992875-fba7f9.svg
[qq0Group]: http://t.cn/R17bes8

**使用方法**

## 1、坐标

Step 1. Add it in your root build.gradle at the end of repositories:
```bash
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency

```bash
implementation 'com.github.javen205:JPay:AliPay:latest.release.here'
implementation 'com.github.javen205:JPay:WxPay:latest.release.here'
```

例如：版本号为`0.0.5`

```bash
implementation 'com.github.javen205.JPay:AliPay:0.0.5'
implementation 'com.github.javen205.JPay:WxPay:0.0.5'
```
## 2. Android Manifest配置

### 2.1 权限声明

```xml
<!-- 支付宝支付必须-->
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<!-- 支付宝支付必须-->
<!-- 微信支付必须-->
<uses-permission android:name="android.permission.INTERNET"/>
<!-- 微信支付必须-->
```

### 2.2 注册activity

`application` 节点添加如下类容
```xml
<!-- 微信支付 -->
<activity
    android:name="com.jpay.weixin.WXPayEntryActivity"
    android:configChanges="orientation|keyboardHidden|navigation|screenSize"
    android:launchMode="singleTop"
    android:theme="@android:style/Theme.Translucent.NoTitleBar" />
<activity-alias
    android:name=".wxapi.WXPayEntryActivity"
    android:exported="true"
    android:targetActivity="com.jpay.weixin.WXPayEntryActivity" />
<!-- 微信支付 end -->


<!-- 支付宝支付使用了 aar 包则如需额外配置-->
```

## 3. 发起支付

### 3.1 微信支付


```java
com.jpay.wxpay.JPay.getIntance(mContext).toWxPay(appId, partnerId, prepayId, nonceStr, timeStamp, sign, new com.jpay.wxpay.JPay.WxPayListener() {
    @Override
    public void onPaySuccess() {
        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPayError(int error_code, String message) {
        Toast.makeText(mContext, "支付失败>" + error_code + " " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPayCancel() {
        Toast.makeText(mContext, "取消了支付", Toast.LENGTH_SHORT).show();
    }
});
```

或者

```java
com.jpay.wxpay.JPay.getIntance(mContext).toWxPay(payParameters, new com.jpay.wxpay.JPay.WxPayListener() {
    @Override
    public void onPaySuccess() {
        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPayError(int error_code, String message) {
        Toast.makeText(mContext, "支付失败>" + error_code + " " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPayCancel() {
        Toast.makeText(mContext, "取消了支付", Toast.LENGTH_SHORT).show();
    }
});

``` 

`payParameters` 为JSON字符串格式如下：
```json
{
  "appId": "",
  "partnerId": "",
  "prepayId": "",
  "sign": "",
  "nonceStr" : "",
  "timeStamp": ""
}
```

或者

```java
JPay.getIntance(mContext).toWxPay(appId, partnerId, prepayId, nonceStr, timeStamp, sign, new JPay.JPayListener() {
    @Override
    public void onPaySuccess() {
        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPayError(int error_code, String message) {
        Toast.makeText(mContext, "支付失败>"+error_code+" "+ message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPayCancel() {
        Toast.makeText(mContext, "取消了支付", Toast.LENGTH_SHORT).show();
    }
});
```
### 3.2 支付宝支付

```java
JPay.getIntance(mContext).toAliPay(orderInfo, new JPay.AliPayListener() {
    @Override
    public void onPaySuccess() {
        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPayError(int error_code, String message) {
        Toast.makeText(mContext, "支付失败>" + error_code + " " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPayCancel() {
        Toast.makeText(mContext, "取消了支付", Toast.LENGTH_SHORT).show();
    }
});
```


## 4.案例的使用


> appId 以及相关的 key 我们都从服务端获取

### 客户端使用说明
 
 1. 将`AndroidManifest.xml` 的包名修改为申请应用的包名
 2. 将应用中的 `build.gradle` 的 `applicationId` 修改为申请应用的包名
 3. 测试的时候修改默认的签名 key

> 将key复制到项目的根目录(app)中并修改 `buildTypes` 配置如下

```bash
 signingConfigs {
        release {
            storeFile file("wxkey")
            storePassword '123456'
            keyAlias '1'
            keyPassword '123456'
        }
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
        debug {
            signingConfig signingConfigs.release
        }
    }
```


## 5.参考资料

微信、支付宝APP支付详细介绍参考资料 [博客地址](http://javen.blog.csdn.net)

[10分钟搭建属于自己的ngork服务器，实现内网穿透](http://javen.blog.csdn.net/article/details/70341106)
[Android版-微信APP支付](http://javen.blog.csdn.net/article/details/54024232)

[Android版-支付宝APP支付](http://javen.blog.csdn.net/article/details/54024238)

[支付宝Wap支付你了解多少？](http://javen.blog.csdn.net/article/details/54024253)

**安利**

[微信公众号开发：订阅号、服务号](https://gitee.com/javen205/weixin_guide)

[AndroidStudio多渠道打包](http://javen.blog.csdn.net/article/details/61420290)

[Android依赖管理与私服搭建](http://javen.blog.csdn.net/article/details/60336030)

[Android Studio 上传aar(Library)到JCenter](http://javen.blog.csdn.net/article/details/60336189)


<Q url="tencent://message/?uin=572839485&Site=%E5%AE%A2%E6%9C%8D&Menu=yes" />
