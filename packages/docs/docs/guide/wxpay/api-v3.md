# 微信支付API v3

相较于的之前微信支付API，主要区别是：

- 遵循统一的Restful的设计风格
- 使用JSON作为数据交互的格式，不再使用XML
- 使用基于非对称密钥的SHA256-RSA的数字签名算法，不再使用MD5或HMAC-SHA256
- 不再要求HTTPS客户端证书
- 使用AES-256-GCM，对回调中的关键信息进行加密保护

更多介绍情况官方文档 [WeChatPay-API-v3](https://wechatpay-api.gitbook.io/wechatpay-api-v3/)

## 获取证书序列号

```java
X509Certificate certificate = PayKit.getCertificate(FileUtil.getInputStream("apiclient_cert.pem 证书路径"));
String serialNo  = certificate.getSerialNumber().toString(16).toUpperCase();
```            

:::tip 如何验证序列号

- openssl x509 -in apiclient_cert.pem -noout -serial
- 使用证书解析工具 [https://myssl.com/cert_decode.html](https://myssl.com/cert_decode.html)
:::

## 调用 v3 接口

获取平台证书列表为例

```java 
String keyPath = "/Users/Javen/cert/apiclient_key.pem";// 私钥证书
String mchId = "商户号";
String serialNo = "公钥证书序列号";
String body = ""; // 请求报文主体,没有查询参数。  

try {
    String result = WxPayApi.v3Execution(RequestMethod.GET, WxDomain.CHINA.toString(), WxApiType.GET_CERTIFICATES.toString(), mchId, serialNo, keyPath, body);
    System.out.println(result);
} catch (Exception e) {
    e.printStackTrace();
}
``` 

返回数据格式 

```json
{
  "data": [
    {
      "effective_time": "XXX",
      "encrypt_certificate": {
        "algorithm": "AEAD_AES_256_GCM",
        "associated_data": "certificate",
        "ciphertext": "XXX",
        "nonce": "XXX"
      },
      "expire_time": "XXX",
      "serial_no": "XXX"
    }
  ]
}
```    

## 证书和回调报文解密

证书报文解密为例

```java  
String associatedData = "";
String nonce = "";
String ciphertext = "";           
String saveCertPath = "/Users/Javen/cert/platform_cert.pem";

AesUtil aesUtil = new AesUtil(apiKey3.getBytes(StandardCharsets.UTF_8));
// 平台证书密文解密
String publicKey = aesUtil.decryptToString(
        associatedData.getBytes(StandardCharsets.UTF_8),
        nonce.getBytes(StandardCharsets.UTF_8),
        ciphertext
);
System.out.println("平台证书公钥明文：" + publicKey);
// 保存证书
FileWriter writer = new FileWriter(saveCertPath);
writer.write(publicKey);
// 获取平台证书序列号
X509Certificate certificate = PayKit.getCertificate(new ByteArrayInputStream(publicKey.getBytes()));
System.out.println(certificate.getSerialNumber().toString(16).toUpperCase());
```
