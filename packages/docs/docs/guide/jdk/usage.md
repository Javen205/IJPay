# 在 JDK 8 与 JDK 17+ 下使用 IJPay 3.x

IJPay 3.x 的 jar 同时支持 JDK 8 与 JDK 8 以上版本。选择接入方式的依据不是 JDK 版本，而是**容器使用的 Servlet 命名空间**：

| 运行环境 | Servlet | 选择适配模块 |
|---|---|---|
| JDK 8 / 11 / 17 / 21 + Spring Boot 2.x / Tomcat 9- / JFinal / Solon | `javax.servlet` | `IJPay-Servlet-Javax` |
| JDK 17+ + Spring Boot 3.x / Tomcat 10+ | `jakarta.servlet` | `IJPay-Servlet-Jakarta` 或 `IJPay-Jakarta` |

## 1. 通用依赖

### 1.1 Spring Boot 2.x（javax）

```xml
<properties>
    <ijpay.version>3.0.0</ijpay.version>
</properties>

<dependencies>
    <dependency>
        <groupId>com.github.javen205</groupId>
        <artifactId>IJPay-WxPay</artifactId>
        <version>${ijpay.version}</version>
    </dependency>
    <dependency>
        <groupId>com.github.javen205</groupId>
        <artifactId>IJPay-AliPay</artifactId>
        <version>${ijpay.version}</version>
    </dependency>
    <!-- 需要 HttpServletRequest / HttpServletResponse 时引入 -->
    <dependency>
        <groupId>com.github.javen205</groupId>
        <artifactId>IJPay-Servlet-Javax</artifactId>
        <version>${ijpay.version}</version>
    </dependency>
</dependencies>
```

### 1.2 Spring Boot 3.x（jakarta，JDK 17+）

```xml
<properties>
    <ijpay.version>3.0.0</ijpay.version>
</properties>

<dependencies>
    <dependency>
        <groupId>com.github.javen205</groupId>
        <artifactId>IJPay-Jakarta</artifactId>
        <version>${ijpay.version}</version>
        <!-- IJPay-Jakarta 是 pom 聚合依赖，必须声明 type -->
        <type>pom</type>
    </dependency>
    <!-- 或者按需引入 -->
    <!--
    <dependency>
        <groupId>com.github.javen205</groupId>
        <artifactId>IJPay-WxPay</artifactId>
        <version>${ijpay.version}</version>
    </dependency>
    <dependency>
        <groupId>com.github.javen205</groupId>
        <artifactId>IJPay-AliPay</artifactId>
        <version>${ijpay.version}</version>
    </dependency>
    <dependency>
        <groupId>com.github.javen205</groupId>
        <artifactId>IJPay-Servlet-Jakarta</artifactId>
        <version>${ijpay.version}</version>
    </dependency>
    -->
</dependencies>
```

> Spring Boot 3.x 自带 `jakarta.servlet-api`，`IJPay-Servlet-Jakarta` 中的 Servlet API 依赖是 `provided`，不会重复传递。

## 2. 支付宝：页面支付 + 异步通知验签

### 2.1 配置 `AliPayApiConfig`

```java
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;

AliPayApiConfig config = AliPayApiConfig.builder()
        .setAppId("your-app-id")
        .setPrivateKey("your-app-private-key")
        .setAliPayPublicKey("alipay-public-key")
        .setCharset("UTF-8")
        .setServiceUrl("https://openapi.alipay.com/gateway.do")
        .setSignType("RSA2")
        .build();

AliPayApiConfigKit.setThreadLocalAliPayApiConfig(config);
```

### 2.2 Spring Boot 2.x（javax）页面支付

```java
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.ijpay.servlet.javax.AliPayServletApi;

import javax.servlet.http.HttpServletResponse;

@GetMapping("/aliPay/wapPay")
public void wapPay(HttpServletResponse response) throws Exception {
    AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
    model.setOutTradeNo("ORDER_" + System.currentTimeMillis());
    model.setTotalAmount("0.01");
    model.setSubject("IJPay 测试");
    model.setProductCode("QUICK_WAP_PAY");

    AliPayServletApi.wapPay(response, model,
            "https://your-domain.com/aliPay/return",
            "https://your-domain.com/aliPay/notify");
}
```

### 2.3 Spring Boot 3.x（jakarta）页面支付

```java
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.ijpay.servlet.jakarta.AliPayServletApi;

import jakarta.servlet.http.HttpServletResponse;

@GetMapping("/aliPay/wapPay")
public void wapPay(HttpServletResponse response) throws Exception {
    AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
    model.setOutTradeNo("ORDER_" + System.currentTimeMillis());
    model.setTotalAmount("0.01");
    model.setSubject("IJPay 测试");
    model.setProductCode("QUICK_WAP_PAY");

    AliPayServletApi.wapPay(response, model,
            "https://your-domain.com/aliPay/return",
            "https://your-domain.com/aliPay/notify");
}
```

### 2.4 异步通知验签

Spring Boot 2.x（javax）：

```java
import com.alipay.api.internal.util.AlipaySignature;
import com.ijpay.servlet.javax.AliPayServletApi;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@PostMapping("/aliPay/notify")
public String notify(HttpServletRequest request) throws Exception {
    Map<String, String> params = AliPayServletApi.toMap(request);
    boolean ok = AlipaySignature.rsaCheckV1(
            params, "alipay-public-key", "UTF-8", "RSA2");
    return ok ? "success" : "failure";
}
```

Spring Boot 3.x（jakarta）：把 import 换成 `com.ijpay.servlet.jakarta.AliPayServletApi` 与 `jakarta.servlet.http.HttpServletRequest`，其余不变。

容器无关写法（不引入适配模块）：

```java
import com.alipay.api.internal.util.AlipaySignature;
import com.ijpay.core.kit.HttpKit;

import java.util.Map;

public String notify(Map<String, String[]> parameterMap) throws Exception {
    Map<String, String> params = HttpKit.toMap(parameterMap);
    boolean ok = AlipaySignature.rsaCheckV1(
            params, "alipay-public-key", "UTF-8", "RSA2");
    return ok ? "success" : "failure";
}
```

## 3. 微信支付 V3：统一下单 + 回调验签

### 3.1 统一下单（容器无关）

```java
import cn.hutool.json.JSONUtil;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.AuthTypeEnum;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.model.v3.Amount;
import com.ijpay.wxpay.model.v3.UnifiedOrderModel;

public String nativePay() throws Exception {
    UnifiedOrderModel model = new UnifiedOrderModel()
            .setAppid("your-app-id")
            .setMchid("your-mch-id")
            .setDescription("IJPay 让支付触手可及")
            .setOut_trade_no(PayKit.generateStr())
            .setNotify_url("https://your-domain.com/v3/payNotify")
            .setAmount(new Amount().setTotal(1));

    IJPayHttpResponse response = WxPayApi.v3(
            RequestMethodEnum.POST,
            WxDomainEnum.CHINA.toString(),
            BasePayApiEnum.NATIVE_PAY.toString(),
            "your-mch-id",
            "merchant-cert-serial-no",
            null,
            "/path/to/apiclient_key.pem",
            JSONUtil.toJsonStr(model),
            AuthTypeEnum.RSA.getCode());

    // 平台证书模式：使用平台证书验签；若使用微信支付公钥，则改用 verifyPublicKeySignature
    boolean ok = WxPayKit.verifySignature(response, "/path/to/platform_cert.pem");
    return response.getBody();
}
```

### 3.2 回调验签（javax）

```java
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.servlet.javax.ServletHttpKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@PostMapping("/v3/payNotify")
public void payNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String timestamp = request.getHeader("Wechatpay-Timestamp");
    String nonce = request.getHeader("Wechatpay-Nonce");
    String serialNo = request.getHeader("Wechatpay-Serial");
    String signature = request.getHeader("Wechatpay-Signature");
    String body = ServletHttpKit.readData(request);

    String plainText = WxPayKit.verifyPublicKeyNotify(
            body, signature, nonce, timestamp,
            "your-api-v3-key", "/path/to/platform_cert.pem");

    boolean ok = plainText != null && !plainText.isEmpty();
    response.setStatus(ok ? 200 : 500);
    response.setHeader("Content-Type", "application/json");
    response.getWriter().write(ok
            ? "{\"code\":\"SUCCESS\",\"message\":\"SUCCESS\"}"
            : "{\"code\":\"ERROR\",\"message\":\"签名错误\"}");
}
```

### 3.3 回调验签（jakarta）

把 import 换成：

```java
import com.ijpay.servlet.jakarta.ServletHttpKit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
```

其余代码不变。

## 4. 只使用核心能力（不接入 Servlet）

如果支付逻辑位于非 Servlet 环境（定时任务、消息队列消费者、RPC 服务），只依赖 trunk 模块即可：

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-WxPay</artifactId>
    <version>3.0.0</version>
</dependency>
```

此时可使用：

- `WxPayApi` / `WxPayV3Api` 的 HTTP 调用
- `WxPayKit` 的签名、验签、回调解密
- `AliPayApi` 的 `wapPayStr`、`tradePage(String method, ...)`、`batchTransUrl`、`toMap(Map)`
- `HttpKit.readData(Reader)`、`HttpKit.toMap(Map)`
- `IpKit.getRealIp(Map, remoteAddr)`

## 5. 常见问题

### 5.1 编译报 `程序包 javax.servlet.http 不存在`

说明代码仍在调用 2.x 的 Servlet 方法，但只引入了 trunk 模块。二选一：

- 引入 `IJPay-Servlet-Javax`，并把 import 改为 `com.ijpay.servlet.javax.*`；
- 改用容器无关 API（`Reader` / `Map`）。

### 5.2 Spring Boot 3 启动报 `ClassNotFoundException: javax.servlet.http.HttpServletRequest`

说明类路径中仍存在依赖 `javax.servlet` 的库（可能是旧版 IJPay 或其他组件）。检查：

```bash
mvn dependency:tree | grep javax.servlet
```

确保只使用 `IJPay-Servlet-Jakarta` / `IJPay-Jakarta`。

### 5.3 能否同时使用 javax 和 jakarta 适配模块

类路径上可以共存（包名不同），但业务代码应只选择一种命名空间。混用会导致同一请求对象无法直接传递。

### 5.4 JDK 8 下能否使用 Jakarta 适配模块

可以编译与运行（Servlet 5.0，class 52），但实际部署需要支持 `jakarta.servlet` 的容器；JDK 8 环境通常搭配 Tomcat 9（javax），此时应使用 `IJPay-Servlet-Javax`。
