# 2.x → 3.x 升级指南

IJPay 3.0.0 的核心变化是**把 Servlet 相关能力从支付核心拆分到独立适配模块**，同时保持支付核心与容器无关。本文逐条列出破坏性变更与替代方案。

## 1. 依赖坐标变化

3.0.0 的所有模块版本统一为 `3.0.0`，并新增两个适配模块：

```xml
<!-- 支付核心（不依赖 Servlet） -->
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-WxPay</artifactId>
    <version>3.0.0</version>
</dependency>
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-AliPay</artifactId>
    <version>3.0.0</version>
</dependency>

<!-- javax 容器（Tomcat 9- / Spring Boot 2.x） -->
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-Servlet-Javax</artifactId>
    <version>3.0.0</version>
</dependency>

<!-- jakarta 容器（Tomcat 10+ / Spring Boot 3.x） -->
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-Servlet-Jakarta</artifactId>
    <version>3.0.0</version>
</dependency>

<!-- 或者使用 jakarta 聚合依赖（pom 类型） -->
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-Jakarta</artifactId>
    <version>3.0.0</version>
    <type>pom</type>
</dependency>
```

注意：

- `IJPay-Core`、`IJPay-AliPay` 不再传递 `javax.servlet-api`，需要原生 Servlet 类型时**必须**显式引入对应适配模块。
- `IJPay-All` 仍是 javax 聚合包，但**不包含**任何 Servlet 适配类；不要期望通过 `IJPay-All` 获得 `ServletHttpKit` 等类。
- `IJPay-Jakarta` 是 pom 聚合，包含常用 trunk 模块与 `IJPay-Servlet-Jakarta`。

## 2. 包名变化

| 能力 | 2.x 位置 | 3.x 位置 |
|---|---|---|
| Servlet 请求体读取 | `com.ijpay.core.kit.HttpKit` | `com.ijpay.servlet.javax.ServletHttpKit`（javax）<br>`com.ijpay.servlet.jakarta.ServletHttpKit`（jakarta） |
| Servlet 参数转 Map | `com.ijpay.core.kit.HttpKit#toMap(HttpServletRequest)` | `ServletHttpKit#toMap(HttpServletRequest)` |
| 真实 IP 解析 | `com.ijpay.core.kit.IpKit#getRealIp(HttpServletRequest)` | `com.ijpay.servlet.javax.ServletIpKit` / `com.ijpay.servlet.jakarta.ServletIpKit` |
| 支付宝页面支付 | `com.ijpay.alipay.AliPayApi#wapPay/tradePage/...` | `com.ijpay.servlet.javax.AliPayServletApi` / `com.ijpay.servlet.jakarta.AliPayServletApi` |

## 3. 方法迁移明细

### 3.1 `HttpKit`

| 2.x 方法 | 3.x 替代 |
|---|---|
| `HttpKit.readData(HttpServletRequest)` | `ServletHttpKit.readData(HttpServletRequest)` |
| `HttpKit.toMap(HttpServletRequest)` | `ServletHttpKit.toMap(HttpServletRequest)` |
| 无 | `HttpKit.readData(Reader)`（容器无关，新增） |
| 无 | `HttpKit.toMap(Map<String, String[]>)`（容器无关，新增） |

Before（2.x）：

```java
String body = HttpKit.readData(request);
Map<String, String> params = HttpKit.toMap(request);
```

After（3.x，javax）：

```java
import com.ijpay.servlet.javax.ServletHttpKit;

String body = ServletHttpKit.readData(request);
Map<String, String> params = ServletHttpKit.toMap(request);
```

After（3.x，jakarta）：

```java
import com.ijpay.servlet.jakarta.ServletHttpKit;

String body = ServletHttpKit.readData(request);
Map<String, String> params = ServletHttpKit.toMap(request);
```

容器无关写法（不引入任何 Servlet API）：

```java
import com.ijpay.core.kit.HttpKit;

// request.getReader() 由调用方负责关闭
String body = HttpKit.readData(reader);
Map<String, String> params = HttpKit.toMap(request.getParameterMap());
```

### 3.2 `IpKit`

| 2.x 方法 | 3.x 替代 |
|---|---|
| `IpKit.getRealIp(HttpServletRequest)` | `ServletIpKit.getRealIp(HttpServletRequest)` |
| `IpKit.getRealIpV2(HttpServletRequest)` | `ServletIpKit.getRealIpV2(HttpServletRequest)` |
| 无 | `IpKit.getRealIp(Map<String, String> headers, String remoteAddr)`（新增） |
| 无 | `IpKit.getRealIpV2(Map<String, String> headers, String remoteAddr)`（新增） |

Before（2.x）：

```java
String ip = IpKit.getRealIp(request);
```

After（3.x，javax）：

```java
import com.ijpay.servlet.javax.ServletIpKit;

String ip = ServletIpKit.getRealIp(request);
```

容器无关写法：

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

### 3.3 `AliPayApi` 页面支付

以下方法从 `AliPayApi` 迁移到 `AliPayServletApi`，**方法名与参数顺序保持不变**，只需改 import：

| 2.x `AliPayApi` 方法 | 3.x `AliPayServletApi` |
|---|---|
| `wapPay(HttpServletResponse, AlipayTradeWapPayModel, String, String)` | 同名方法 |
| `wapPay(AlipayClient, HttpServletResponse, AlipayTradeWapPayModel, String, String)` | 同名方法 |
| `wapPay(..., String appAuthToken)` | 同名方法 |
| `wapPayByOutputStream(...)`（4 个重载） | 同名方法 |
| `tradePage(HttpServletResponse, AlipayTradePagePayModel, String, String)` | 同名方法 |
| `tradePage(HttpServletResponse, String method, ...)` | 同名方法 |
| `tradePageByOutputStream(...)`（4 个重载） | 同名方法 |
| `batchTrans(Map, String privateKey, String signType, HttpServletResponse)` | 同名方法 |
| `toMap(HttpServletRequest)` | 同名方法 |

Before（2.x）：

```java
AliPayApi.wapPay(response, model, returnUrl, notifyUrl);
AliPayApi.tradePage(response, model, notifyUrl, returnUrl);
Map<String, String> params = AliPayApi.toMap(request);
```

After（3.x，javax）：

```java
import com.ijpay.servlet.javax.AliPayServletApi;

AliPayServletApi.wapPay(response, model, returnUrl, notifyUrl);
AliPayServletApi.tradePage(response, model, notifyUrl, returnUrl);
Map<String, String> params = AliPayServletApi.toMap(request);
```

After（3.x，jakarta）：

```java
import com.ijpay.servlet.jakarta.AliPayServletApi;

AliPayServletApi.wapPay(response, model, returnUrl, notifyUrl);
AliPayServletApi.tradePage(response, model, notifyUrl, returnUrl);
Map<String, String> params = AliPayServletApi.toMap(request);
```

容器无关替代：

```java
// 返回支付宝页面表单 HTML，由调用方写出
String html = AliPayApi.wapPayStr(model, returnUrl, notifyUrl);

// 批量转账：返回 URL，由调用方 sendRedirect
String url = AliPayApi.batchTransUrl(params, privateKey, signType);
response.sendRedirect(url);
```

## 4. 哪些场景必须引入适配模块

| 场景 | 是否需要 `IJPay-Servlet-Javax` | 是否需要 `IJPay-Servlet-Jakarta` |
|---|---|---|
| 只做服务端签名、验签、HTTP 调用（参数用 `Map`/`Reader`） | 否 | 否 |
| 微信支付回调验签（自行读 header/body） | 否 | 否 |
| 支付宝异步通知验签（`AlipaySignature.rsaCheckV1` + `Map`） | 否 | 否 |
| 直接调用 `HttpKit.readData(HttpServletRequest)` / `IpKit.getRealIp(HttpServletRequest)` | 是 | 否 |
| 直接调用 `AliPayApi.wapPay/tradePage` 等页面支付方法 | 是 | 否 |
| 在 Spring Boot 3 / Tomcat 10+ 中直接调用上述能力 | 否 | 是 |

> 同一个应用**不要同时引入** `IJPay-Servlet-Javax` 与 `IJPay-Servlet-Jakarta`：两者包名不同、可以共存于类路径，但同时使用会让调用方困惑，且容易误用错误的命名空间。

## 5. 行为与 API 兼容性

- 支付核心的公开类、方法、字段没有删除或改名；3.0.0 的破坏性变更仅为「Servlet 方法迁移」。
- 新增的容器无关方法均为**增量**，不会影响现有调用。
- 迁移后的 `AliPayServletApi` 与 `ServletHttpKit` 行为与 2.x 完全一致（包括 `batchTrans` 的 302 语义）。
- 业务代码中如果只使用 `WxPayApi`、`WxPayV3Api`、`AliPayApi` 的非 Servlet 方法，升级到 3.0.0 不需要改代码。

## 6. 升级检查清单

1. 把 IJPay 依赖版本统一改为 `3.0.0`。
2. 全文搜索 `javax.servlet` 相关的 IJPay 调用：`HttpKit.readData`、`HttpKit.toMap`、`IpKit.getRealIp`、`IpKit.getRealIpV2`、`AliPayApi.wapPay`、`AliPayApi.tradePage`、`AliPayApi.batchTrans`。
3. 按容器选择 `IJPay-Servlet-Javax` 或 `IJPay-Servlet-Jakarta` 并修改 import。
4. 如果使用 Spring Boot 3 / Tomcat 10+，确认没有其他依赖把 `javax.servlet-api` 带进来。
5. 编译通过后，重点回归：支付宝页面支付跳转、异步通知验签、微信支付回调验签、获取真实 IP。
