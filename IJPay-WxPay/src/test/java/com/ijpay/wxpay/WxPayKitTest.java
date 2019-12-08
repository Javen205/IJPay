package com.ijpay.wxpay;


import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.enums.RequestMethod;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.enums.WxApiType;
import com.ijpay.wxpay.enums.WxDomain;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
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
    String publicKeyPath = "/Users/Javen/cert/apiclient_key.pem";
    String mchId = "mchId";
    // 商户API证书序列号
    // 使用证书解析工具 https://myssl.com/cert_decode.html 查看
    String serialNo = "serialNo";
    String body = "";


    @Test
    public void getCertificate() throws IOException {
        X509Certificate certificate = PayKit.getCertificate(FileUtil.getInputStream(keyPath));
        System.out.println(certificate.getPublicKey());
        System.out.println(certificate.getSerialNumber());
    }

    @Test
    public void v3Execution() {
        try {
            String result = WxPayApi.v3Execution(RequestMethod.GET, WxDomain.CHINA.toString(), WxApiType.GET_CERTIFICATES.toString(), mchId, serialNo, keyPath, body);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void userServiceState() {
        try {

            Map<String, String> params = new HashMap<>();
            params.put("service_id", "500001");
            params.put("appid", "wxd678efh567hg6787");
            params.put("openid", "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");

            String result = WxPayApi.v3Execution(RequestMethod.GET, WxDomain.CHINA.toString(), WxApiType.USER_SERVICE_STATE.toString(), mchId, serialNo, keyPath, body, params);
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
            params.put("risk_amount",29900);
            params.put("attach",PayKit.urlEncode("IJPay 测试先享后付"));

            body = JSONUtil.toJsonStr(params);

            System.out.println(body);

            String result = WxPayApi.v3Execution(RequestMethod.POST, WxDomain.CHINA.toString(), WxApiType.PAY_AFTER_ORDERS.toString(), mchId, serialNo, keyPath, body);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
