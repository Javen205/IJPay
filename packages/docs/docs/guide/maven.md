# 安装依赖

## 版本与 JDK 基线

- 所有模块的 `groupId` 统一为 `com.github.javen205`，版本号也保持一致（3.x 起所有模块同版本发布）。
- 所有模块产物的 class 文件主版本为 **52（Java 8）**，同一份 jar 可直接运行在 JDK 8 及以上版本。
- 需要 Servlet 能力的适配模块同样固定在该版本线上：`IJPay-Servlet-Javax` 使用 Servlet 4.0（`javax.servlet`），`IJPay-Servlet-Jakarta` 使用 Servlet 5.0（`jakarta.servlet`），两者与主版本号一一对应。
- 更完整的 JDK / Servlet 对应关系见[支持矩阵](./jdk/support-matrix.md)。

> 下文示例统一用 `latest-version` 表示最新发布版本，可替换为实际版本号（当前工作区为 `3.0.0`）。

## 模块一览

| artifactId | 用途 | 依赖的 IJPay 模块 | 是否需要 Servlet |
|---|---|---|---|
| `IJPay-Core` | 核心工具：签名/验签、HTTP 代理、二维码、IP 解析等 | 无 | 否 |
| `IJPay-WxPay` | 微信支付（Api-v2 / Api-v3） | `IJPay-Core` | 否 |
| `IJPay-AliPay` | 支付宝（`alipay-sdk-java` 4.x） | 无 | 否 |
| `IJPay-AliPay-V3` | 支付宝 V3 SDK（`alipay-sdk-java-v3`） | 无 | 否 |
| `IJPay-QQ` | QQ 钱包支付 | `IJPay-Core` | 否 |
| `IJPay-UnionPay` | 银联条码支付综合前置平台 | `IJPay-Core` | 否 |
| `IJPay-JDPay` | 京东支付 | `IJPay-Core` | 否 |
| `IJPay-PayPal` | PayPal 支付 | `IJPay-Core` | 否 |
| `IJPay-Stripe` | Stripe 支付 | 无 | 否 |
| `IJPay-XPay` | XPay（个人收款） | `IJPay-Core` | 否 |
| `IJPay-FuiYou` | 富友支付 | `IJPay-Core` | 否 |
| `IJPay-Servlet-Javax` | `javax.servlet` 适配（Servlet 4.0） | `IJPay-Core`、`IJPay-AliPay` | 是（provided） |
| `IJPay-Servlet-Jakarta` | `jakarta.servlet` 适配（Servlet 5.0） | `IJPay-Core`、`IJPay-AliPay` | 是（provided） |
| `IJPay-Jakarta` | Jakarta 聚合依赖（`packaging=pom`） | 上述常用支付模块 + `IJPay-Servlet-Jakarta` | 是 |
| `IJPay-All` | shade 聚合 jar | `IJPay-WxPay`、`IJPay-AliPay`、`IJPay-JDPay`、`IJPay-QQ`、`IJPay-UnionPay`、`IJPay-PayPal`、`IJPay-XPay`、`IJPay-FuiYou` | 否 |

### 关于 IJPay-All

`IJPay-All` 通过 `maven-shade-plugin` 把上表列出的 8 个支付模块（以及它们传递的 `IJPay-Core`）打进同一个 jar：

- `artifactSet` 只包含 `com.github.javen205` 自身坐标，**hutool、各渠道官方 SDK 等外部依赖不会被打进 jar**，仍按普通传递依赖引入。
- 显式排除了 `IJPay-Servlet-Javax`、`IJPay-Servlet-Jakarta`、`IJPay-Jakarta`，避免 `javax` / `jakarta` 命名空间混入聚合包。需要页面支付或回调读取时，仍要按容器单独引入对应适配模块。
- **不包含** `IJPay-Stripe` 与 `IJPay-AliPay-V3`，使用这两个渠道需要单独添加依赖。
- shade 会生成 dependency-reduced pom，被聚合的模块不会作为独立依赖再次传递；但如果你同时显式引入了 `IJPay-WxPay` 等模块，就会出现同名类重复，二者选其一即可。

## Maven

### 一次性引入（IJPay-All）

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-All</artifactId>
    <version>latest-version</version>
</dependency>
```

### 按支付方式单独引入

支付宝：

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-AliPay</artifactId>
    <version>latest-version</version>
</dependency>
```

支付宝 V3 SDK：

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-AliPay-V3</artifactId>
    <version>latest-version</version>
</dependency>
```

微信支付：

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-WxPay</artifactId>
    <version>latest-version</version>
</dependency>
```

QQ 支付：

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-QQ</artifactId>
    <version>latest-version</version>
</dependency>
```

京东支付：

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-JDPay</artifactId>
    <version>latest-version</version>
</dependency>
```

银联支付-条码支付综合前置平台：

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-UnionPay</artifactId>
    <version>latest-version</version>
</dependency>
```

PayPal 支付：

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-PayPal</artifactId>
    <version>latest-version</version>
</dependency>
```

Stripe 支付：

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-Stripe</artifactId>
    <version>latest-version</version>
</dependency>
```

富友支付：

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-FuiYou</artifactId>
    <version>latest-version</version>
</dependency>
```

### Servlet 适配模块

JDK 8 / javax 容器（Spring Boot 2.x、Tomcat 9 及以下、JFinal、Solon）：

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-Servlet-Javax</artifactId>
    <version>latest-version</version>
</dependency>
```

JDK 17+ / jakarta 容器（Spring Boot 3.x、Tomcat 10+）：

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-Servlet-Jakarta</artifactId>
    <version>latest-version</version>
</dependency>
```

也可以使用 Jakarta 聚合依赖，一次带入常用支付模块与 jakarta 适配。注意 `IJPay-Jakarta` 是 `packaging=pom` 的聚合依赖，Maven 中**必须**声明 `<type>pom</type>`：

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-Jakarta</artifactId>
    <version>latest-version</version>
    <type>pom</type>
</dependency>
```

> 同一个工程只应引入一个适配模块，不要同时引入 javax 与 jakarta 版本。

## Gradle

```groovy
dependencies {
    implementation 'com.github.javen205:IJPay-WxPay:latest-version'
    implementation 'com.github.javen205:IJPay-AliPay:latest-version'
    // JDK 8 / javax 容器
    implementation 'com.github.javen205:IJPay-Servlet-Javax:latest-version'
}
```

Spring Boot 3 / jakarta 容器：

```groovy
dependencies {
    implementation 'com.github.javen205:IJPay-WxPay:latest-version'
    implementation 'com.github.javen205:IJPay-AliPay:latest-version'
    implementation 'com.github.javen205:IJPay-Servlet-Jakarta:latest-version'
}
```

`IJPay-Jakarta` 是 Maven pom 聚合，Gradle 下建议直接声明具体模块与 `IJPay-Servlet-Jakarta`，不要依赖 pom 聚合的传递解析。

## 常见依赖冲突与传递依赖说明

- **Servlet API 是 provided**：`IJPay-Servlet-*` 对 `javax.servlet-api` / `jakarta.servlet-api` 都使用 `provided` 作用域，不会传递给使用方。运行时由容器提供（Spring Boot 由内嵌 Tomcat 提供），打 war 时也不要把 Servlet API 打进 `WEB-INF/lib`。
- **适配模块不要混用**：`IJPay-Servlet-Javax` 与 `IJPay-Servlet-Jakarta` 的类名相同、包名不同（`com.ijpay.servlet.javax` / `com.ijpay.servlet.jakarta`），同时引入只会增加体积和误用风险。
- **IJPay-All 不含适配模块和 Stripe / AliPay-V3**：使用页面支付或回调读取时按容器补一个适配模块；使用 Stripe 或支付宝 V3 时单独引入对应模块。
- **支付宝 V3 不再传递旧 dom4j / bcprov**：`IJPay-AliPay-V3` 排除了 `alipay-sdk-java-v3` 传递的 `dom4j:dom4j` 与 `bcprov-jdk15on`，改为 `org.dom4j:dom4j:2.1.5` 与 `org.bouncycastle:bcprov-jdk18on:1.86`。两者与旧坐标的包名相同，工程中不要再显式引入 `bcprov-jdk15on`，否则会出现同名类冲突。
- **okhttp 版本**：支付宝 SDK 传递的是 okhttp 3.12.13，`IJPay-AliPay` 已排除并统一为 `com.squareup.okhttp3:okhttp:4.12.0`（同时需要 `kotlin-stdlib`）。如果工程本身也使用 okhttp，请与 4.12.0 对齐，避免 Maven 就近仲裁到旧版本。
- **guava 停留在 Java 8 可用版本**：统一为 `33.6.0-jre`（33.7.0 起要求 Java 9+）；如果你的工程显式引用了更高版本的 guava，需要自行确认 JDK 8 兼容性。
- **fastjson 坐标**：`IJPay-AliPay` 使用 `com.alibaba:fastjson:2.0.66`（fastjson2 的兼容坐标），与旧版 1.2.x 的 `com.alibaba:fastjson` 是同一坐标的升级，不需要额外排除。

## 更多依赖方式

- [在 Maven Central 搜索 IJPay](https://search.maven.org/search?q=IJPay)

<script>
// export default {
//   mounted () {
//     let xmlHttp = new XMLHttpRequest();
//     xmlHttp.open("GET", "https://img.shields.io/maven-central/v/com.github.javen205/IJPay.json", false);
//     xmlHttp.send(null);
//     let versionInfo = JSON.parse(xmlHttp.responseText).value.replace('v', '');
//     let codeNodeList = document.querySelectorAll('code');
//     for (let i = 0; i < codeNodeList.length; i++) {
//         codeNodeList[i].innerHTML = codeNodeList[i].innerHTML.replace('latest-version', versionInfo);
//     }
//   }
// }
</script>

<Q url="tencent://message/?uin=572839485&Site=%E5%AE%A2%E6%9C%8D&Menu=yes" />
