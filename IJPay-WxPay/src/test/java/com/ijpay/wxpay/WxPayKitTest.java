package com.ijpay.wxpay;


import com.ijpay.core.enums.RequestMethod;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.enums.WxApiType;
import com.ijpay.wxpay.enums.WxDomain;
import org.junit.Assert;
import org.junit.Test;

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

    @Test
    public void v3Execution() {
        try {
            String mchId = "mchId";
            // 商户API证书序列号
            // 使用证书解析工具 https://myssl.com/cert_decode.html 查看
            String serialNo = "serialNo";
            String body = "";
            String result = WxPayApi.v3Execution(RequestMethod.GET, WxDomain.CHINA.toString(), WxApiType.GET_CERTIFICATES.toString(), mchId, serialNo, keyPath, body);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
