# 安装依赖

## Maven

::: tip
一次性添加所有支付方式的依赖
:::

在项目的 pom.xml 的 dependencies 中加入以下内容:

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-All</artifactId>
    <version>latest-version</version>
</dependency>
```   

::: tip
支付宝支付
:::

在项目的 pom.xml 的 dependencies 中加入以下内容:

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-AliPay</artifactId>
    <version>latest-version</version>
</dependency>
```      

::: tip
微信支付
:::

在项目的 pom.xml 的 dependencies 中加入以下内容:

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-WxPay</artifactId>
    <version>latest-version</version>
</dependency>
```  

::: tip
QQ 支付
:::

在项目的 pom.xml 的 dependencies 中加入以下内容:

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-QQ</artifactId>
    <version>latest-version</version>
</dependency>
``` 

::: tip
京东支付
:::

在项目的 pom.xml 的 dependencies 中加入以下内容:

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-JDPay</artifactId>
    <version>latest-version</version>
</dependency>
```    

::: tip
银联支付
:::

在项目的 pom.xml 的 dependencies 中加入以下内容:

```xml
<dependency>
    <groupId>com.github.javen205</groupId>
    <artifactId>IJPay-UnionPay</artifactId>
    <version>latest-version</version>
</dependency>
```       

## 更多依赖方式

- [了解更多](https://search.maven.org/search?q=IJPay)


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