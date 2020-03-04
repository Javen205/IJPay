# 微信支付 API v3

## v2 与 v3 区别

相较于的之前微信支付API，主要区别是：

- 遵循统一的Restful的设计风格
- 使用JSON作为数据交互的格式，不再使用XML
- 使用基于非对称密钥的SHA256-RSA的数字签名算法，不再使用MD5或HMAC-SHA256
- 不再要求HTTPS客户端证书
- 使用AES-256-GCM，对回调中的关键信息进行加密保护

更多证书和签名的详细内容请参官方文档 [WeChatPay-API-v3](https://wechatpay-api.gitbook.io/wechatpay-api-v3/)

## API密钥设置

API密钥的详细内容请参见：[API证书及密钥](https://kf.qq.com/faq/180830E36vyQ180830AZFZvu.html)

## 下载API证书

请在商户平台下载证书。具体操作请参见：[如何获取API证书](https://kf.qq.com/faq/161222NneAJf161222U7fARv.html)

## 获取证书序列号

```java
X509Certificate certificate = PayKit.getCertificate(FileUtil.getInputStream("apiclient_cert.pem 证书路径"));
String serialNo  = certificate.getSerialNumber().toString(16).toUpperCase();
```            

:::tip 如何验证序列号

- openssl x509 -in apiclient_cert.pem -noout -serial
- 使用证书解析工具 [https://myssl.com/cert_decode.html](https://myssl.com/cert_decode.html)
:::

## 构建 Authorization

```java
String authorization = WxPayKit.buildAuthorization(method, urlSuffix, mchId, serialNo, keyPath, body, nonceStr, timestamp, authType);                                                                                                                                          
// 或者
String authorization = WxPayKit.buildAuthorization(method, urlSuffix, mchId, serialNo, keyPath, body);
```     

参数说明

- method    请求方法 RequestMethod
- urlSuffix 可通过 WxApiType 来获取，URL挂载参数需要自行拼接
- mchId     商户Id
- serialNo  商户 API 证书序列号
- keyPath   key.pem 证书路径
- body      接口请求参数
- nonceStr  随机字符库
- timestamp 时间戳
- authType  认证类型

## 调用 v3 接口

获取平台证书列表为例

```java {28}
String keyPath = "/Users/Javen/cert/apiclient_key.pem";// 私钥证书
String mchId = "商户号";
String serialNo = "公钥证书序列号";
String body = ""; // 请求报文主体,没有查询参数。 
String platformCertPath = "";//微信平台证书 验证签名时需要使用 

@Test
public void v3Get() {
    // 获取平台证书列表
    try {
        Map<String, Object> result = WxPayApi.v3Execution(
                RequestMethod.GET,
                WxDomain.CHINA.toString(),
                WxApiType.GET_CERTIFICATES.toString(),
                mchId,
                serialNo,
                keyPath,
                body
        );

        String serialNumber = MapUtil.getStr(result, "serialNumber");
        String body = MapUtil.getStr(result, "body");
        int status = MapUtil.getInt(result, "status");

        System.out.println("serialNumber:" + serialNumber);
        System.out.println("status:" + status);
        // 根据证书序列号查询对应的证书来验证签名结果
        boolean verifySignature = WxPayKit.verifySignature(result, platformCertPath);

        System.out.println("verifySignature:" + verifySignature + "\nbody:" + body);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

``` 

返回数据 body 格式 

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

## 验证签名

根据证书序列号查询对应微信平台的证书来验证签名结果

```java
WxPayKit.verifySignature(Map<String, Object> map, String certPath);
WxPayKit.verifySignature(Map<String, Object> map, InputStream certInputStream)  
WxPayKit.verifySignature(String signature, String body, String nonce, String timestamp, String publicKey) 
WxPayKit.verifySignature(String signature, String body, String nonce, String timestamp, PublicKey publicKey)
WxPayKit.verifySignature(String signature, String body, String nonce, String timestamp, InputStream certInputStream)
```                      
:::tip
- map v3 接口返回的结果
- certPath 微信支付平台证书
:::

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
