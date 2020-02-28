package com.ijpay.wxpay;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.RequestMethod;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.AesUtil;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.enums.WxApiType;
import com.ijpay.wxpay.enums.WxDomain;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WxPayKitTest {

    @Test
    public void hmacSHA256() {
        Assert.assertEquals("8ae6af1a6f6e75f20b8240f320f33e1a376105c5668f1b57a591cd61fe409ee3",
                WxPayKit.hmacSha256("IJPay", "123"));
    }

    @Test
    public void mad5() {
        Assert.assertEquals("cbfc2149d454ecf4ab0f32e58430fcdd",
                WxPayKit.md5("IJPay"));
    }

    @Test
    public void encryptData() {
        Assert.assertEquals("K8fdh/6THGfTKio8pxXS6Q==",
                WxPayKit.encryptData("IJPay", "42cc1d91bab89b65ff55b19e28fff4f0"));
    }

    @Test
    public void decryptData() {
        Assert.assertEquals("IJPay",
                WxPayKit.decryptData(
                        WxPayKit.encryptData("IJPay", "42cc1d91bab89b65ff55b19e28fff4f0"),
                        "42cc1d91bab89b65ff55b19e28fff4f0"));
    }

    String keyPath = "/Users/Javen/cert/apiclient_key.pem";
    String certPath = "/Users/Javen/cert/apiclient_cert.pem";
    String certPath2 = "/Users/Javen/cert/apiclient_cert.p12";

    String mchId = "xxx";
    // 商户API证书序列号
    // 使用证书解析工具 https://myssl.com/cert_decode.html 查看
    // openssl x509 -in apiclient_cert.pem -noout -serial 查看
    String serialNo = "xxx";
    String body = "";
    String apiKey3 = "api key 3";
    String saveCertPath = "/Users/Javen/cert/platform_cert.pem";


    @Test
    public void getCertificate() {
        // 获取证书序列号
        X509Certificate certificate = PayKit.getCertificate(FileUtil.getInputStream(certPath));
        System.out.println(certificate.getSerialNumber().toString(16).toUpperCase());
    }

    @Test
    public void platformCert() throws Exception {
        AesUtil aesUtil = new AesUtil(apiKey3.getBytes(StandardCharsets.UTF_8));
        // 平台证书密文解密
        // encrypt_certificate 中的  associated_data nonce  ciphertext
        String publicKey = aesUtil.decryptToString(
                "certificate".getBytes(StandardCharsets.UTF_8),
                "80d28946a64a".getBytes(StandardCharsets.UTF_8),
                "DwAqW4+4TeUaOEylfKEXhw+XqGh/YTRhUmLw/tBfQ5nM9DZ9d+9aGEghycwV1jwo52vXb/t6ueBvBRHRIW5JgDRcXmTHw9IMTrIK6HxTt2qiaGTWJU9whsF+GGeQdA7gBCHZm3AJUwrzerAGW1mclXBTvXqaCl6haE7AOHJ2g4RtQThi3nxOI63/yc3WaiAlSR22GuCpy6wJBfljBq5Bx2xXDZXlF2TNbDIeodiEnJEG2m9eBWKuvKPyUPyClRXG1fdOkKnCZZ6u+ipb4IJx28n3MmhEtuc2heqqlFUbeONaRpXv6KOZmH/IdEL6nqNDP2D7cXutNVCi0TtSfC7ojnO/+PKRu3MGO2Z9q3zyZXmkWHCSms/C3ACatPUKHIK+92MxjSQDc1E/8faghTc9bDgn8cqWpVKcL3GHK+RfuYKiMcdSkUDJyMJOwEXMYNUdseQMJ3gL4pfxuQu6QrVvJ17q3ZjzkexkPNU4PNSlIBJg+KX61cyBTBumaHy/EbHiP9V2GeM729a0h5UYYJVedSo1guIGjMZ4tA3WgwQrlpp3VAMKEBLRJMcnHd4pH5YQ/4hiUlHGEHttWtnxKFwnJ6jHr3OmFLV1FiUUOZEDAqR0U1KhtGjOffnmB9tymWF8FwRNiH2Tee/cCDBaHhNtfPI5129SrlSR7bZc+h7uzz9z+1OOkNrWHzAoWEe3XVGKAywpn5HGbcL+9nsEVZRJLvV7aOxAZBkxhg8H5Fjt1ioTJL+qXgRzse1BX1iiwfCR0fzEWT9ldDTDW0Y1b3tb419MhdmTQB5FsMXYOzqp5h+Tz1FwEGsa6TJsmdjJQSNz+7qPSg5D6C2gc9/6PkysSu/6XfsWXD7cQkuZ+TJ/Xb6Q1Uu7ZB90SauA8uPQUIchW5zQ6UfK5dwMkOuEcE/141/Aw2rlDqjtsE17u1dQ6TCax/ZQTDQ2MDUaBPEaDIMPcgL7fCeijoRgovkBY92m86leZvQ+HVbxlFx5CoPhz4a81kt9XJuEYOztSIKlm7QNfW0BvSUhLmxDNCjcxqwyydtKbLzA+EBb2gG4ORiH8IOTbV0+G4S6BqetU7RrO+/nKt21nXVqXUmdkhkBakLN8FUcHygyWnVxbA7OI2RGnJJUnxqHd3kTbzD5Wxco4JIQsTOV6KtO5c960oVYUARZIP1SdQhqwELm27AktEN7kzg/ew/blnTys/eauGyw78XCROb9F1wbZBToUZ7L+8/m/2tyyyqNid+sC9fYqJoIOGfFOe6COWzTI/XPytCHwgHeUxmgk7NYfU0ukR223RPUOym6kLzSMMBKCivnNg68tbLRJHEOpQTXFBaFFHt2qpceJpJgw5sKFqx3eQnIFuyvA1i8s2zKLhULZio9hpsDJQREOcNeHVjEZazdCGnbe3Vjg7uqOoVHdE/YbNzJNQEsB3/erYJB+eGzyFwFmdAHenG5RE6FhCutjszwRiSvW9F7wvRK36gm7NnVJZkvlbGwh0UHr0pbcrOmxT81xtNSvMzT0VZNLTUX2ur3AGLwi2ej8BIC0H41nw4ToxTnwtFR1Xy55+pUiwpB7JzraA08dCXdFdtZ72Tw/dNBy5h1P7EtQYiKzXp6rndfOEWgNOsan7e1XRpCnX7xoAkdPvy40OuQ5gNbDKry5gVDEZhmEk/WRuGGaX06CG9m7NfErUsnQYrDJVjXWKYuARd9R7W0aa5nUXqz/Pjul/LAatJgWhZgFBGXhNr9iAoade/0FPpBj0QWa8SWqKYKiOqXqhfhppUq35FIa0a1Vvxcn3E38XYpVZVTDEXcEcD0RLCu/ezdOa6vRcB7hjgXFIRZQAka0aXnQxwOZwE2Rt3yWXqc+Q1ah2oOrg8Lg3ETc644X9QP4FxOtDwz/A=="
        );
        System.out.println("平台证书公钥明文：" + publicKey);
        // 保存证书
        FileWriter writer = new FileWriter(saveCertPath);
        writer.write(publicKey);
        // 获取平台证书序列号
        X509Certificate certificate = PayKit.getCertificate(new ByteArrayInputStream(publicKey.getBytes()));
        System.out.println(certificate.getSerialNumber().toString(16).toUpperCase());
    }

    @Test
    public void v3Get() {
        // 获取平台证书列表
        try {
            String result = WxPayApi.v3Execution(
                    RequestMethod.GET,
                    WxDomain.CHINA.toString(),
                    WxApiType.GET_CERTIFICATES.toString(),
                    mchId,
                    serialNo,
                    keyPath,
                    body
            );
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void v3Delete() {
        // 创建/查询/更新/删除投诉通知回调
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("url", "https://qq.com");
            String result = WxPayApi.v3Execution(
                    RequestMethod.POST,
                    WxDomain.CHINA.toString(),
                    WxApiType.MERCHANT_SERVICE_COMPLAINTS_NOTIFICATIONS.toString(),
                    mchId,
                    serialNo,
                    keyPath,
                    JSONUtil.toJsonStr(hashMap)
            );
            System.out.println(result);

            result = WxPayApi.v3Execution(
                    RequestMethod.DELETE,
                    WxDomain.CHINA.toString(),
                    WxApiType.MERCHANT_SERVICE_COMPLAINTS_NOTIFICATIONS.toString(),
                    mchId,
                    serialNo,
                    keyPath,
                    ""
            );
            // 如果返回的为 204 表示删除成功
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void upload() {
        //TODO 待测试 证书上传文件
        try {
            String filePath = "/Users/Javen/Documents/pic/cat.png";

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("mch_id", mchId);
            hashMap.put("media_hash", PayKit.md5(FileUtil.readUtf8String(filePath)).toLowerCase());
            hashMap.put("sign_type", SignType.HMACSHA256.toString());

            String sign = WxPayKit.createSign(hashMap, apiKey3, SignType.HMACSHA256);
            hashMap.put("sign", sign);

            System.out.println(hashMap);

            String result = WxPayApi.execution(
                    WxDomain.CHINA.toString().concat(WxApiType.MCH_UPLOAD_MEDIA.toString()),
                    hashMap,
                    certPath2,
                    mchId,
                    filePath
            );
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void v3Upload() {
        // v3 接口上传文件
        try {
            String filePath = "/Users/Javen/Documents/pic/cat.png";

            File file = FileUtil.newFile(filePath);
            String sha256 = SecureUtil.sha256(file);

            HashMap<Object, Object> map = new HashMap<>();
            map.put("filename", file.getName());
            map.put("sha256", sha256);
            body = JSONUtil.toJsonStr(map);

            System.out.println(body);

            String result = WxPayApi.v3Upload(
                    WxDomain.CHINA.toString(),
                    WxApiType.MERCHANT_UPLOAD_MEDIA.toString(),
                    mchId,
                    serialNo,
                    keyPath,
                    body,
                    file
            );
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void payScoreUserServiceState() {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("service_id", "500001");
            params.put("appid", "wxd678efh567hg6787");
            params.put("openid", "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");

            String result = WxPayApi.v3Execution(
                    RequestMethod.GET,
                    WxDomain.CHINA.toString(),
                    WxApiType.PAY_SCORE_USER_SERVICE_STATE.toString(),
                    mchId,
                    serialNo,
                    keyPath,
                    params
            );
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void payAfterOrders() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("appid", "wxd678efh567hg6787");
            params.put("out_order_no", PayKit.generateStr());
            params.put("service_id", "500001");
//            params.put("service_start_time", DateUtil.format(new Date(), "yyyyMMddHHmmss"));
            params.put("service_start_time", "OnAccept");
            params.put("service_start_location", "订阅 IJPay VIP");
            params.put("service_introduction", "VIP 会员服务费");
            ArrayList<Object> feesList = new ArrayList<>();
            Map<String, Object> fee = new HashMap<>();
            fee.put("fee_name", "服务费");
            fee.put("fee_amount", 29900);
            fee.put("fee_desc", "每年");
            fee.put("fee_count", 1);
            feesList.add(fee);
            params.put("fees", feesList);
            params.put("risk_amount", 29900);
            params.put("attach", PayKit.urlEncode("IJPay 测试先享后付"));

            body = JSONUtil.toJsonStr(params);

            System.out.println(body);

            String result = WxPayApi.v3Execution(
                    RequestMethod.POST,
                    WxDomain.CHINA.toString(),
                    WxApiType.PAY_AFTER_ORDERS.toString(),
                    mchId,
                    serialNo,
                    keyPath,
                    body
            );
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void payGiftActivity() {
        // 支付有礼-终止活动
        try {
            String urlSuffix = String.format(WxApiType.PAY_GIFT_ACTIVITY_TERMINATE.toString(), "10028001");
            System.out.println(urlSuffix);
            String result = WxPayApi.v3Execution(
                    RequestMethod.POST,
                    WxDomain.CHINA.toString(),
                    urlSuffix,
                    mchId,
                    serialNo,
                    keyPath,
                    ""
            );
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
