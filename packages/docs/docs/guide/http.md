# 扩展 Http 请求

## 默认 Http 客户端

IJPay 使用 [HuTool](https://hutool.cn) 的 `HttpUtil` 作为默认实现，底层基于 JDK 的 `HttpUrlConnection` 封装。
[HuTool 详细介绍](https://hutool.cn/docs/#/http/%E6%A6%82%E8%BF%B0?id=%e7%94%b1%e6%9d%a5)

- 抽象代理：[AbstractHttpDelegate](https://gitee.com/javen205/IJPay/blob/master/IJPay-Core/src/main/java/com/ijpay/core/http/AbstractHttpDelegate.java)
- 默认实现：`DefaultHttpKit`（包私有，继承 `AbstractHttpDelegate`，未做额外重写）
- 入口工具类：[HttpKit](https://gitee.com/javen205/IJPay/blob/master/IJPay-Core/src/main/java/com/ijpay/core/kit/HttpKit.java)

```java
// 获取/替换全局 Http 代理
AbstractHttpDelegate delegate = HttpKit.getDelegate();
HttpKit.setDelegate(delegate);
```

以[微信支付 API](https://gitee.com/javen205/IJPay/blob/master/IJPay-WxPay/src/main/java/com/ijpay/wxpay/WxPayApi.java) 的调用为例：

```java
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

1. 继承 `AbstractHttpDelegate`，重写需要替换的方法
2. 通过 `HttpKit.setDelegate(new CustomHttpKit())` 使其生效

```java
public class CustomHttpKit extends AbstractHttpDelegate {
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

> `AbstractHttpDelegate` 还提供 `get/post/patch/delete/put` 的响应对象重载（返回 `IJPayHttpResponse`）以及 `upload(...)`，按需重写即可。

## 容器无关的 HttpKit 能力

3.0.0 起 `IJPay-Core` 不再依赖任何 Servlet API，`HttpKit` 只保留两个与请求数据相关的方法：

```java
// 读取字符流内容，不会关闭传入的 Reader，流生命周期由调用方负责
public static String readData(Reader reader)

// 将“参数名 -> 多值数组”的 Map 转为“参数名 -> 字符串”的 Map
// null 值转为空串，多值以英文逗号拼接
public static Map<String, String> toMap(Map<String, String[]> params)
```

旧的 `readData(HttpServletRequest)`、`toMap(HttpServletRequest)` 已从 Core 移除，Servlet 适配方法在 `IJPay-Servlet-Javax` / `IJPay-Servlet-Jakarta` 的 `ServletHttpKit` 中提供。

## 获取异步通知中的请求体

| 场景 | 写法 |
|---|---|
| 2.x（javax） | `HttpKit.readData(request)` |
| 3.x / JDK 8 / javax 容器 | `com.ijpay.servlet.javax.ServletHttpKit.readData(request)` |
| 3.x / JDK 17+ / jakarta 容器 | `com.ijpay.servlet.jakarta.ServletHttpKit.readData(request)` |
| 3.x / 不引适配模块 | `HttpKit.readData(request.getReader())` |

JDK 8（Spring Boot 2.x、Tomcat 9 等）：

```java
import com.ijpay.servlet.javax.ServletHttpKit;

String body = ServletHttpKit.readData(request);
```

JDK 17+（Spring Boot 3.x、Tomcat 10+ 等）：

```java
import com.ijpay.servlet.jakarta.ServletHttpKit;

String body = ServletHttpKit.readData(request);
```

不引入适配模块时，自行传入 `Reader`。注意 `HttpKit.readData(Reader)` 不会关闭流：

```java
import com.ijpay.core.kit.HttpKit;

String body;
try (Reader reader = request.getReader()) {
    body = HttpKit.readData(reader);
}
```

> `ServletHttpKit.readData` 的行为与 2.x 的 `HttpKit.readData(HttpServletRequest)` 一致：读取完成后会关闭请求的 `Reader`。

## 获取请求参数

| 场景 | 写法 |
|---|---|
| 2.x（javax） | `HttpKit.toMap(request)` |
| 3.x / javax 容器 | `com.ijpay.servlet.javax.ServletHttpKit.toMap(request)` |
| 3.x / jakarta 容器 | `com.ijpay.servlet.jakarta.ServletHttpKit.toMap(request)` |
| 3.x / 不引适配模块 | `HttpKit.toMap(request.getParameterMap())` |

Spring MVC 可以直接使用框架已解析好的参数 Map：

```java
public String notify(@RequestParam Map<String, String> params) {
    // 已经是单值 Map，可直接用于验签
    return "success";
}
```

多值参数会以英文逗号拼接，例如 `a=1&a=2` 转换后为 `a -> "1,2"`。

## 获取客户端真实 IP

3.0.0 起 `IpKit` 改为接收请求头与远端地址，不再接收容器对象：

```java
// 完整版：依次尝试 x-forwarded-for、Proxy-Client-IP、WL-Proxy-Client-IP，最后回退到 remoteAddr
public static String getRealIp(Map<String, String> headers, String remoteAddr)

// 简化版：仅取 x-forwarded-for，不存在（null）时回退到 remoteAddr
public static String getRealIpV2(Map<String, String> headers, String remoteAddr)
```

JDK 8（javax）：

```java
import com.ijpay.servlet.javax.ServletIpKit;

String ip = ServletIpKit.getRealIp(request);
```

JDK 17+（jakarta）：

```java
import com.ijpay.servlet.jakarta.ServletIpKit;

String ip = ServletIpKit.getRealIp(request);
```

不引入适配模块时，自行组装请求头：

```java
import com.ijpay.core.kit.IpKit;

Map<String, String> headers = new HashMap<>();
Enumeration<String> names = request.getHeaderNames();
while (names.hasMoreElements()) {
    String name = names.nextElement();
    headers.put(name, request.getHeader(name));
}
String ip = IpKit.getRealIp(headers, request.getRemoteAddr());
```

> 请求头名称大小写不敏感；`getRealIp` 在 `x-forwarded-for` 等头都存在且内容长度超过 15 个字符时，会按英文逗号截取第一个地址。

## 页面支付等方法已迁出核心

支付宝页面支付（`wapPay`、`wapPayByOutputStream`、`tradePage`、`tradePageByOutputStream`）以及批量转账 `batchTrans` 等依赖 `HttpServletResponse` 的方法，已从 `AliPayApi` 移到适配模块的 `AliPayServletApi`：

- `javax`：`com.ijpay.servlet.javax.AliPayServletApi`
- `jakarta`：`com.ijpay.servlet.jakarta.AliPayServletApi`

容器无关的替代写法：

```java
// 返回自动提交表单的 HTML / 跳转地址，由调用方写入响应
String form = AliPayApi.wapPayStr(model, returnUrl, notifyUrl);
String page = AliPayApi.tradePage(model, notifyUrl, returnUrl);
// 返回可直接跳转的批量转账地址
String url = AliPayApi.batchTransUrl(params, privateKey, signType);
```

## 2.x 到 3.x 的写法对照

| 2.x 写法 | 3.x 容器无关写法 | 3.x javax 适配 | 3.x jakarta 适配 |
|---|---|---|---|
| `HttpKit.readData(request)` | `HttpKit.readData(request.getReader())` | `ServletHttpKit.readData(request)` | `ServletHttpKit.readData(request)` |
| `HttpKit.toMap(request)` | `HttpKit.toMap(request.getParameterMap())` | `ServletHttpKit.toMap(request)` | `ServletHttpKit.toMap(request)` |
| `IpKit.getRealIp(request)` | `IpKit.getRealIp(headers, remoteAddr)` | `ServletIpKit.getRealIp(request)` | `ServletIpKit.getRealIp(request)` |
| `IpKit.getRealIpV2(request)` | `IpKit.getRealIpV2(headers, remoteAddr)` | `ServletIpKit.getRealIpV2(request)` | `ServletIpKit.getRealIpV2(request)` |
| `AliPayApi.wapPay(response, ...)` | `AliPayApi.wapPayStr(model, ...)` | `AliPayServletApi.wapPay(response, ...)` | `AliPayServletApi.wapPay(response, ...)` |

完整的方法迁移说明见 [2.x → 3.x 升级指南](./jdk/migration-2.x-to-3.x.md)，适配模块的选择见[支持矩阵](./jdk/support-matrix.md)。

<Q url="tencent://message/?uin=572839485&Site=%E5%AE%A2%E6%9C%8D&Menu=yes" />
