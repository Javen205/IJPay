package com.ijpay.jdpay.util;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.codec.binary.Base64;


public class XmlEncryptUtil {
    private static String XML_JDPAY_END = "</jdpay>";
    private static String XML_SIGN_START = "<sign>";
    private static String XML_SIGN_END = "</sign>";
    private static String SIGN = "sign";
    private static String RESULT = "result";


    public static String encrypt(String rsaPrivateKey, String strDesKey, String genSignStr) {
        System.out.println("genSignStr>" + genSignStr);
        String encrypt = null;
        if (StrUtil.isNotEmpty(rsaPrivateKey) && StrUtil.isNotEmpty(strDesKey) && StrUtil.isNotEmpty(genSignStr)) {

            try {
                genSignStr = JdPayXmlUtil.addXmlHeadAndElJdPay(genSignStr);

                genSignStr = JdPayXmlUtil.fomatXmlStr(genSignStr);

                genSignStr = JdPayXmlUtil.delXmlElm(genSignStr, SIGN);

                String sign = VerifySignatureUtl.encryptMerchant(genSignStr, rsaPrivateKey);
                System.out.println("sign>" + sign);
                String data = genSignStr.substring(0, genSignStr.length() - XML_JDPAY_END.length()) + XML_SIGN_START + sign + XML_SIGN_END + XML_JDPAY_END;

                encrypt = Base64.encodeBase64String(ThreeDesUtil.encrypt2HexStr(RsaUtil.decryptBASE64(strDesKey), data).getBytes("UTF-8"));
            } catch (Exception e) {
                throw new RuntimeException("signature failed");
            }
        }
        return encrypt;
    }

    public static String decrypt(String rsaPubKey, String strDesKey, String encrypt) {
        String reqBody = "";

        try {
            reqBody = ThreeDesUtil.decrypt4HexStr(RsaUtil.decryptBASE64(strDesKey), new String(Base64.decodeBase64(encrypt), "UTF-8"));

            String inputSign = JdPayXmlUtil.getXmlElm(reqBody, SIGN);

            reqBody = JdPayXmlUtil.addXmlHead(reqBody);

            reqBody = JdPayXmlUtil.fomatXmlStr(reqBody);

            String genSignStr = JdPayXmlUtil.delXmlElm(reqBody, SIGN);

            boolean verifyResult = VerifySignatureUtl.decryptMerchant(genSignStr, inputSign, rsaPubKey);
            if (!verifyResult) {
                throw new RuntimeException("verify signature failed");
            }
        } catch (Exception e) {
            throw new RuntimeException("data decrypt failed");
        }
        return reqBody;
    }


    public static String decrypt(String rsaPubKey, String reqBody) {
        String req = "";

        try {
            String inputSign = JdPayXmlUtil.getXmlElm(reqBody, SIGN);

            req = JdPayXmlUtil.addXmlHead(reqBody);

            req = JdPayXmlUtil.fomatXmlStr(req);

            String genSignStr = JdPayXmlUtil.delXmlElm(req, SIGN);

            boolean verifyResult = VerifySignatureUtl.decryptMerchant(genSignStr, inputSign, rsaPubKey);
            if (!verifyResult) {
                throw new RuntimeException("verify signature failed");
            }
        } catch (Exception e) {
            throw new RuntimeException("data decrypt failed");
        }
        return req;
    }


    public static void main(String[] args) {
        String rsaPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3Q7knFjJBC0ZzQep5Wq3G+LcGtvPrCfUv6Wfo8Tz1rIfGvHFYjDz2z2iDCP6b5tRSePWfVjdz4O8OEdF+fQWFhJhAPNDIbT8wOALuKQ2MvpDRtZL9hOOr1K4eZNiRw2ppCXKPmi/obSf6maCP7URfhAa6rUixfcPJ5QCEaCWlteuRNYbWFRhORFGIrCOw4pULY42E2yXbgD+N7ORdNxzRZFCrdcwMyIDQ8dmFeWc17mHo/ZTxbVZxMgUQ1m3eBDc+5OGd7jiqSisohli7DvMt+VIwm1f0S5c7QtEFS0FBeul3sgLOM55mz5VlOZOdB61lDw+dh3RHf2ex6BABOb6ywIDAQAB";
        String rsaPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDdDuScWMkELRnNB6nlarcb4twa28+sJ9S/pZ+jxPPWsh8a8cViMPPbPaIMI/pvm1FJ49Z9WN3Pg7w4R0X59BYWEmEA80MhtPzA4Au4pDYy+kNG1kv2E46vUrh5k2JHDamkJco+aL+htJ/qZoI/tRF+EBrqtSLF9w8nlAIRoJaW165E1htYVGE5EUYisI7DilQtjjYTbJduAP43s5F03HNFkUKt1zAzIgNDx2YV5ZzXuYej9lPFtVnEyBRDWbd4ENz7k4Z3uOKpKKyiGWLsO8y35UjCbV/RLlztC0QVLQUF66XeyAs4znmbPlWU5k50HrWUPD52HdEd/Z7HoEAE5vrLAgMBAAECggEAbZfTmPufdPWTJAXXogh9DVM0QhCV7ci1fenzsjKTnO4j46zXaa3RR/FPZGt13l0HOPW+wdgL57Rs3Q3g0GHFjV3BP8JaltxuroSk6v5mbHGMZxMZB798bsk48fUytP0+DEY79SLjVp0A5ym5CzKOoIwFfDUfLzwkBEApommWHuOUeW12yXWa4xEttB+JAoARBgg2mhGRN0s1N6x5XWLrc0epYNs0syx58YhetQ3X77aXvgyig0haHDOG671UAB+gPCatByR3u7DgzqGDfJp9s6MXZgSp2KmfYl4JLec065XNbThMURgqKpiZgpE3Mx6EChzO9dUXExbMnbsUvf3XCQKBgQD+OU8xtdrs9Het1oaKEMwnP9xJPVIr3j7E+WIi3zhqW2IQ+4gkhvAmnsHsy/f2bM0rYckeZZWz1iWOkqzAAFnPONJiVx4jIwQ86/1s+mbX1MlreyT+9AphXadjTc1QLyAgjqWwiabiAmuFc83Ql+LEK+jLGTc9PnhVhx1Jv4dX9QKBgQDemkPuQJ7ooRGSZMjSFydRanVysTY/Ng9C74eLnLzS4t0gyKxGUFa6MHCOixOIauM5k9cuFIBEPBrl8nLOH1aMMtHV5aw5l/3XMQdTDRX6/hCc26dWolnR+Wp7+11yTTha0B3d5lQHPOZaM47+xouTa60BBCpy7+L0jCU+g5EPvwKBgQDFO3cqjPlNjxjuwJnescuBw/TGyZFfwWwXa5dskJv3P/CkVlE4bYwRmme/rDszbxP6TUI4l/196W134GmwCFWlBGOMsiQKhJc8IKacDuUNG+Qsw/xe5LzM71j3HRxl0jntqF35ycG0ZMZAYijSZZQkOCDCuUx28mlviYT6e2KopQKBgQCIFMZyYA7FJ7IWTIZ36K+glfQ2qR8AhYvO3599OdQ1F3sXD5ZBZdue9v3YJi1KuA0wpbBl+yJulE/dQtnsKDxAeNDOchlXHBOR+ecAXn+RcL+3JJCn5ZgDRPZT1NbLiWlqGtAnVycHRbOMcPh5x+aLuMeKV4Gbwgp8dTBPhx6nAQKBgAHNFHusDx5zFIkS13mlN+7rG9oDJKwr+gLp0zqGOfzLznslXGS9dze56cmWRhHQdSQBYji51Bcb5TP6Pgwv18d6M0g8NiXanIktc3OtdCw9K1lB2nZpJP0hKxkBni15wURzN5Kj0MRtPoe5vXhKF/uDu9IUwY9/x2jzJgh2o8o9";

        String strDesKey = "GX/CH6HIAT2Ubtn3ZKjfYdbxa6HgSVca";


        String param = "<jdpay><version>2.0</version><merchant>110025845001</merchant><tradeNum>201604080000055</tradeNum><tradeType>0</tradeType></jdpay>";
        String encrypt = encrypt(rsaPrivateKey, strDesKey, param);
        System.out.println("encrypt:" + encrypt);


        String decrypt = decrypt(rsaPubKey, strDesKey, encrypt);
        System.out.println("decrypt:" + decrypt);
    }
}
