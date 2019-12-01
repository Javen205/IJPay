package com.ijpay.wxpay;


import com.ijpay.core.enums.RequestMethod;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.RsaKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.enums.WxApiType;
import org.junit.Assert;
import org.junit.Test;

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

    @Test
    public void payKit() {
        try {
            String mchId = "mchId";
            // 商户API证书序列号
            // 使用证书解析工具 https://myssl.com/cert_decode.html 查看
            String serialNo = "serialNo";
            // 商户私钥
            String key = PayKit.getPrivateKey(keyPath);
            String nonceStr = PayKit.generateStr();
            long timestamp = System.currentTimeMillis() / 1000;
            String body = "";
            String authType = "WECHATPAY2-SHA256-RSA2048";

            Map<String, String> params = new HashMap<>();
            params.put("service_id", "500001");
            params.put("appid", "500001");
            params.put("openid", "500001");

            String url = WxApiType.USER_SERVICE_STATE.getType().concat("?").concat(PayKit.createLinkString(params, true));

            String buildSignMessage = PayKit.buildSignMessage(RequestMethod.GET, url, timestamp, nonceStr, body);

            System.out.println(buildSignMessage);

            String signature = RsaKit.encryptByPrivateKey(buildSignMessage, key);

            String authorization = PayKit.getAuthorization(mchId, serialNo, nonceStr, String.valueOf(timestamp), signature, authType);
            System.out.println(authorization);

            Map<String, Object> temp = new HashMap<String, Object>(params.size());
            temp.putAll(params);
            String result = WxPayApi.userServiceState(authorization, temp);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void test() throws Exception {

        String key = PayKit.getPrivateKey(keyPath);

        System.out.println(key);
    }
}
