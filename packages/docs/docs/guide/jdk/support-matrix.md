# 支持矩阵

IJPay 3.x 采用**单分支、单套产物**策略：同一份 jar 既可以在 JDK 8 运行，也可以在 JDK 8 以上的版本运行。所有发布模块的 class 文件主版本固定为 **52（Java 8）**。

## JDK 支持矩阵

| JDK | 定位 | 编译产物 | 验证状态 |
|---|---|---|---|
| 8 | 编译基线，兼容性第一优先级 | class 52 | 本地 `clean verify` 通过，CI 必测 |
| 11 | 存量项目常用版本 | class 52 | CI 必测 |
| 17 | 推荐运行版本、发布构建 JDK | class 52 | 本地 `clean verify` 通过，CI 必测 |
| 21 | 当前主流 LTS | class 52 | 本地 `clean verify` 通过，CI 必测 |
| 25 | 新 LTS，生态跟进中 | class 52 | CI 允许失败（experimental），不阻断发布 |

说明：

- JDK 9+ 构建时使用 `release 8`，JDK 8 构建时回退到 `source/target 1.8`，两者都会产出 class 52。
- 运行时基线为 Java 8：不使用 `List.of`、`String.isBlank`、`var`、`record`、switch 表达式、文本块等 Java 9+ 语法与 API。
- 所有直接/传递依赖的基础字节码必须 ≤ 52；允许依赖自身是 multi-release jar（JDK 8 只读取 base 部分）。

## JDK 与 Servlet 规范对应关系

IJPay 3.x 的支付核心（`IJPay-Core`、`IJPay-WxPay`、`IJPay-AliPay` 等）**不依赖任何 Servlet 类型**。需要原生 `HttpServletRequest` / `HttpServletResponse` 时，按容器命名空间选择适配模块：

| 容器 / 框架 | Servlet 命名空间 | Servlet 规范 | IJPay 适配模块 | 基础字节码 |
|---|---|---|---|---|
| Tomcat 9 及以下、Spring Boot 2.x、JFinal、Solon（javax 模式） | `javax.servlet` | Servlet 4.0 | `IJPay-Servlet-Javax` | 52 |
| Tomcat 10+、Spring Boot 3.x、Jakarta EE 9+ | `jakarta.servlet` | Servlet 5.0 | `IJPay-Servlet-Jakarta` | 52 |

### 为什么 Jakarta 只能用 Servlet 5.0

- `jakarta.servlet-api:5.0.x` 的基础字节码为 **52（Java 8）**，可以在 Java 8 基线下编译。
- `jakarta.servlet-api:6.0+`（Servlet 6.0/6.1）的基础字节码为 **55（Java 11）**，一旦引入就会破坏「所有产物 class 52」的承诺。
- 因此 3.x 的 Jakarta 适配固定使用 Servlet 5.0。Servlet 6.x 能力需要等 IJPay 抬升 Java 基线后，另立模块提供。

> Servlet 5.0 与 6.x 在 IJPay 使用到的 API 范围内（请求参数、header、Reader、OutputStream、sendRedirect）行为一致，Servlet 5.0 编译的适配层可以运行在 Tomcat 10.1 / Spring Boot 3.x 等 Servlet 6.0 容器上。

## 模块与坐标（3.0.0）

| 模块 | 说明 | 是否需要 Servlet |
|---|---|---|
| `IJPay-Core` | 核心工具：签名、验签、HTTP、二维码、IP 解析等 | 否 |
| `IJPay-WxPay` | 微信支付（含 API v3） | 否 |
| `IJPay-AliPay` | 支付宝（V2 SDK） | 否 |
| `IJPay-AliPay-V3` | 支付宝 V3 SDK | 否 |
| `IJPay-Servlet-Javax` | `javax.servlet` 适配：`ServletHttpKit`、`ServletIpKit`、`AliPayServletApi` | 是（provided） |
| `IJPay-Servlet-Jakarta` | `jakarta.servlet` 适配：同名三个类 | 是（provided） |
| `IJPay-Jakarta` | Jakarta 聚合依赖（pom），包含常用 trunk 模块与 Jakarta 适配 | 是 |
| `IJPay-All` | javax 聚合包（shade），**不包含**任何 Servlet 适配类 | 否 |

## 依赖基线说明

为保持 Java 8 兼容，以下依赖停留在最后一个 Java 8 可用版本：

| 依赖 | 当前版本 | 最新版 | 停留原因 |
|---|---|---|---|
| `com.google.guava:guava` | 33.6.0-jre | 33.8.0-jre | 33.7.0 起基础字节码为 53（Java 9） |
| `org.dom4j:dom4j` | 2.1.5 | 2.2.0 | 2.2.0 基础字节码为 55（Java 11） |
| `com.squareup.okhttp3:okhttp` | 4.12.0 | 5.5.1 | 5.x 改为 KMP 拆分 artifact，且与支付宝 SDK 的 4.x 适配存在运行时风险 |
| `jakarta.servlet:jakarta.servlet-api` | 5.0.0 | 6.x | 6.x 基础字节码为 55（Java 11） |

其余依赖（支付宝 SDK、Stripe、Jackson、BouncyCastle、Hutool、fastjson、commons-lang3、SLF4J、commons-codec 等）已升级到当前最新稳定版。
