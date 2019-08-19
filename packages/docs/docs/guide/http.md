# 扩展 Http 请求

## 默认 Http 客户端

在 IJPay 中是使用 [HuTool](https://hutool.cn) 中的 Http 客户端工具类 HttpUtil 来实现，此模块基于JDK 的 HttpUrlConnection 封装完成。
[HuTool 详细介绍](https://hutool.cn/docs/#/http/%E6%A6%82%E8%BF%B0?id=%e7%94%b1%e6%9d%a5)


## 简单封装

抽象 Http 代理 [AbstractHttpDelegate](https://gitee.com/javen205/IJPay/blob/master/IJPay-Core/src/main/java/com/ijpay/core/http/AbstractHttpDelegate.java)，
IJPay 中经常使用到的 [HttpKit](https://gitee.com/javen205/IJPay/blob/master/IJPay-Core/src/main/java/com/ijpay/core/kit/HttpKit.java) 工具类。
使用也非常简单以[微信支付 API](https://gitee.com/javen205/IJPay/blob/master/IJPay-WxPay/src/main/java/com/ijpay/wxpay/WxPayApi.java) 调用为例 

```java{2,6,10,14}
public static String doGet(String url, Map<String, Object> params) {
    return HttpKit.getDelegate().get(url, params);
}

public static String doPost(String url, Map<String, String> params) {
    return HttpKit.getDelegate().post(url, WxPayKit.toXml(params));
}

public static String doPostSSL(String url, Map<String, String> params, String certPath, String certPass) {
    return HttpKit.getDelegate().post(url, WxPayKit.toXml(params), certPath, certPass);
}

public static String doPostSSL(String url, Map<String, String> params, InputStream certFile, String certPass) {
    return HttpKit.getDelegate().post(url, WxPayKit.toXml(params), certFile, certPass);
}
```


## 自定义 Http 客户端

1. 继承 `AbstractHttpDelegate` 重写 http 客户端中相关方法 
2. 设置新的 Http 客户端使其生效 `HttpKit.setDelegate(new xxHttpKit())`

```java
public class xxHttpKit extends AbstractHttpDelegate {
    @Override
    public String get(String url) {
        // 替换具体实现
        return super.get(url);
    }

    @Override
    public String post(String url, String data) {
        // 替换具体实现
        return super.post(url, data);
    }

    @Override
    public String post(String url, String data, String certPath, String certPass) {
        // 替换具体实现
        return super.post(url, data, certPath, certPass);
    }

    @Override
    public String post(String url, Map<String, Object> paramMap) {
        // 替换具体实现
        return super.post(url, paramMap);
    }

    @Override
    public String post(String url, String data, InputStream certFile, String certPass) {
        // 替换具体实现
        return super.post(url, data, certFile, certPass);
    }

}
``` 








