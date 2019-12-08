package com.ijpay.demo.controller.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayFundAuthOrderFreezeResponse;
import com.alipay.api.response.AlipayFundCouponOrderAgreementPayResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.ijpay.core.kit.PayKit;
import com.ijpay.demo.vo.AjaxResult;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Javen
 */
public class AliPayController extends AliPayApiController {
    private Log log = Log.getLog(AliPayController.class);

    private final Prop prop = PropKit.use("alipay.properties");
    private String charset = "UTF-8";
    private String privateKey = prop.get("alipay.privateKey");
    private String publicKey = prop.get("alipay.publicKey");
    private String serviceUrl = prop.get("alipay.serverUrl");
    private String appId = prop.get("alipay.appId");
    private String signType = "RSA2";
    private String domain = prop.get("alipay.domain");

    @Override
    public AliPayApiConfig getApiConfig() {
        AliPayApiConfig aliPayApiConfig;
        try {
            aliPayApiConfig = AliPayApiConfigKit.getApiConfig(appId);
        } catch (Exception e) {
            aliPayApiConfig = AliPayApiConfig.builder()
                    .setAppId(appId)
                    .setAliPayPublicKey(publicKey)
                    .setCharset(charset)
                    .setPrivateKey(privateKey)
                    .setServiceUrl(serviceUrl)
                    .setSignType(signType)
                    .build();
        }
        return aliPayApiConfig;
    }

    public void index() {
        log.info(JsonKit.toJson(AliPayApiConfigKit.getAliPayApiConfig()));
        renderText("欢迎使用IJPay 中的支付宝支付 -By Javen");
    }

    /**
     * app支付
     */
    public void appPay() {
        try {
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody("我是测试数据-By Javen");
            model.setSubject("App支付测试-By Javen");
            model.setOutTradeNo(PayKit.generateStr());
            model.setTimeoutExpress("30m");
            model.setTotalAmount("0.01");
            model.setPassbackParams("callback params");
            model.setProductCode("QUICK_MSECURITY_PAY");
            String orderInfo = AliPayApi.appPayToResponse(model, domain + "/aliPay/appPayNotify").getBody();
            renderJson(new AjaxResult().success(orderInfo));

        } catch (AlipayApiException e) {
            e.printStackTrace();
            renderJson(new AjaxResult().addError("system error"));
        }
    }

    /**
     * Wap支付
     */
    public void wapPay() {
        String body = "我是测试数据-By Javen";
        String subject = "Javen Wap支付测试";
        String totalAmount = getPara("totalAmount");
        String passbackParams = "1";
        String returnUrl = domain + "/aliPay/return_url";
        String notifyUrl = domain + "/aliPay/notifyUrl";

        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setBody(body);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setPassbackParams(passbackParams);
        String outTradeNo = PayKit.generateStr();
        System.out.println("wap outTradeNo>" + outTradeNo);
        model.setOutTradeNo(outTradeNo);
        model.setProductCode("QUICK_WAP_PAY");

        try {
            AliPayApi.wapPay(getResponse(), model, returnUrl, notifyUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        renderNull();
    }


    /**
     * PC支付
     */
    public void pcPay() {
        try {
            String totalAmount = "0.1";
            String outTradeNo = PayKit.generateStr();
            log.info("pc outTradeNo>" + outTradeNo);

            String returnUrl = domain + "/aliPay/return_url";
            String notifyUrl = domain + "/aliPay/notifyUrl";

            AlipayTradePagePayModel model = new AlipayTradePagePayModel();

            model.setOutTradeNo(outTradeNo);
            model.setProductCode("FAST_INSTANT_TRADE_PAY");
            model.setTotalAmount(totalAmount);
            model.setSubject("Javen PC支付测试");
            model.setBody("Javen IJPay PC支付测试");
            model.setPassbackParams("passback_params");
            //花呗分期相关的设置
            /**
             * 测试环境不支持花呗分期的测试
             * hb_fq_num代表花呗分期数，仅支持传入3、6、12，其他期数暂不支持，传入会报错；
             * hb_fq_seller_percent代表卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种，其他比例暂不支持，传入会报错。
             */
			ExtendParams extendParams = new ExtendParams();
			extendParams.setHbFqNum("3");
			extendParams.setHbFqSellerPercent("0");
			model.setExtendParams(extendParams);

            AliPayApi.tradePage(getResponse(), model, notifyUrl, returnUrl);
            renderNull();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 条形码支付
     * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.Yhpibd&
     * treeId=194&articleId=105170&docType=1#s4
     */
    public void tradePay() {
        String authCode = getPara("auth_code");
        String subject = "Javen 支付宝条形码支付测试";
        String totalAmount = "100";
        String notifyUrl = domain + "/aliPay/notifyUrl";

        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setAuthCode(authCode);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setOutTradeNo(PayKit.generateStr());
        model.setScene("bar_code");
        try {
            String resultStr = AliPayApi.tradePayToResponse(model, notifyUrl).getBody();
            renderText(resultStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 声波支付
     * https://doc.open.alipay.com/docs/doc.htm?treeId=194&articleId=105072&docType=1#s2
     */
    public void tradeWavePay() {
        String authCode = getPara("auth_code");
        String subject = "Javen 支付宝声波支付测试";
        String totalAmount = "100";
        String notifyUrl = domain + "/aliPay/notifyUrl";

        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setAuthCode(authCode);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setOutTradeNo(PayKit.generateStr());
        model.setScene("wave_code");
        try {
            String resultStr = AliPayApi.tradePayToResponse(model, notifyUrl).getBody();
            renderText(resultStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫码支付
     */
    public void tradePreCreatePay() {
        String subject = "Javen 支付宝扫码支付测试";
        String totalAmount = "86";
        String storeId = "123";
        String notifyUrl = domain + "/aliPay/preCreateNotifyUrl";

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setStoreId(storeId);
        model.setTimeoutExpress("5m");
        model.setOutTradeNo(PayKit.generateStr());
        try {
            String resultStr = AliPayApi.tradePrecreatePayToResponse(model, notifyUrl).getBody();
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            String qrCode = jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
            renderText(qrCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单笔转账到支付宝账户
     * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.54Ty29&
     * treeId=193&articleId=106236&docType=1
     */
    public void transfer() {
        boolean isSuccess = false;
        String totalAmount = "66";
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(PayKit.generateStr());
        model.setPayeeType("ALIPAY_LOGONID");
        model.setPayeeAccount("abpkvd0206@sandbox.com");
        model.setAmount(totalAmount);
        model.setPayerShowName("测试退款");
        model.setPayerRealName("沙箱环境");
        model.setRemark("javen测试单笔转账到支付宝");

        try {
            isSuccess = AliPayApi.transfer(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        renderJson(isSuccess);
    }

    /**
     * 资金授权冻结接口
     */
    public void authOrderFreeze() {
        try {
            String authCode = getPara("auth_code");
            AlipayFundAuthOrderFreezeModel model = new AlipayFundAuthOrderFreezeModel();
            model.setOutOrderNo(PayKit.generateStr());
            model.setOutRequestNo(PayKit.generateStr());
            model.setAuthCode(authCode);
            model.setAuthCodeType("bar_code");
            model.setOrderTitle("资金授权冻结-By IJPay");
            model.setAmount("36");
			model.setPayTimeout("50000");
            model.setProductCode("PRE_AUTH");

            AlipayFundAuthOrderFreezeResponse response = AliPayApi.authOrderFreezeToResponse(model);
            renderJson(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 红包协议支付接口
     * https://docs.open.alipay.com/301/106168/
     */
    public void agreementPay() {
        try {
            AlipayFundCouponOrderAgreementPayModel model = new AlipayFundCouponOrderAgreementPayModel();
            model.setOutOrderNo(PayKit.generateStr());
            model.setOutRequestNo(PayKit.generateStr());
            model.setOrderTitle("红包协议支付接口-By IJPay");
            model.setAmount("36");
            model.setPayerUserId("2088102180432465");


            AlipayFundCouponOrderAgreementPayResponse response = AliPayApi.fundCouponOrderAgreementPayToResponse(model);
            renderJson(response);
        } catch (Exception e) {
            e.printStackTrace();
            renderText("有异常哦!!!");
        }
    }

    /**
     * 下载对账单
     */
    public void dataDataserviceBill() {
        String para = getPara("billDate");
        try {
            AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
            model.setBillType("trade");
            model.setBillDate(para);
            String resultStr = AliPayApi.billDownloadUrlQuery(model);
            renderText(resultStr);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退款
     */
    public void tradeRefund() {

        try {
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo("081014283315023");
            model.setTradeNo("2017081021001004200200273870");
            model.setRefundAmount("86.00");
            model.setRefundReason("正常退款");
            String resultStr = AliPayApi.tradeRefundToResponse(model).getBody();
            renderText(resultStr);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }


    /**
     * 交易查询
     */
    public void tradeQuery() {
        try {
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            model.setOutTradeNo("081014283315023");
            model.setTradeNo("2017081021001004200200273870");

            boolean isSuccess = AliPayApi.tradeQueryToResponse(model).isSuccess();
            renderJson(isSuccess);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    public void tradeQueryByStr() {
        String outTradeNo = getPara("out_trade_no");
        // String tradeNo = getPara("trade_no");

        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);

        try {
            String resultStr = AliPayApi.tradeQueryToResponse(model).getBody();
            renderText(resultStr);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建订单
     * {"alipay_trade_create_response":{"code":"10000","msg":"Success","out_trade_no":"081014283315033","trade_no":"2017081021001004200200274066"},"sign":"ZagfFZntf0loojZzdrBNnHhenhyRrsXwHLBNt1Z/dBbx7cF1o7SZQrzNjRHHmVypHKuCmYifikZIqbNNrFJauSuhT4MQkBJE+YGPDtHqDf4Ajdsv3JEyAM3TR/Xm5gUOpzCY7w+RZzkHevsTd4cjKeGM54GBh0hQH/gSyhs4pEN3lRWopqcKkrkOGZPcmunkbrUAF7+AhKGUpK+AqDw4xmKFuVChDKaRdnhM6/yVsezJFXzlQeVgFjbfiWqULxBXq1gqicntyUxvRygKA+5zDTqE5Jj3XRDjVFIDBeOBAnM+u03fUP489wV5V5apyI449RWeybLg08Wo+jUmeOuXOA=="}
     */
    public void tradeCreate() {
        String outTradeNo = getPara("out_trade_no");

        String notifyUrl = domain + "/aliPay/notifyUrl";

        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount("88.88");
        model.setBody("Body");
        model.setSubject("Javen 测试统一收单交易创建接口");
        //买家支付宝账号，和buyer_id不能同时为空
        model.setBuyerLogonId("abpkvd0206@sandbox.com");
        try {

            AlipayTradeCreateResponse response = AliPayApi.tradeCreateToResponse(model, notifyUrl);
            renderJson(response.getBody());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

    }

    /**
     * 撤销订单
     */
    public void tradeCancel() {
        try {
            AlipayTradeCancelModel model = new AlipayTradeCancelModel();
            model.setOutTradeNo("081014283315033");
            model.setTradeNo("2017081021001004200200274066");

            boolean isSuccess = AliPayApi.tradeCancelToResponse(model).isSuccess();
            renderJson(isSuccess);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭订单
     */
    public void tradeClose() {
        String outTradeNo = getPara("out_trade_no");
        String tradeNo = getPara("trade_no");
        try {
            AlipayTradeCloseModel model = new AlipayTradeCloseModel();
            model.setOutTradeNo(outTradeNo);

            model.setTradeNo(tradeNo);

            String resultStr = AliPayApi.tradeCloseToResponse(model).getBody();
            renderText(resultStr);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 结算
     */
    public void tradeOrderSettle() {
        //支付宝订单号
        String tradeNo = getPara("trade_no");
        try {
            AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();
            model.setOutRequestNo(PayKit.generateStr());
            model.setTradeNo(tradeNo);

            String resultStr = AliPayApi.tradeOrderSettleToResponse(model).getBody();
            renderText(resultStr);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取应用授权URL并授权
     */
    public void toOauth() {
        try {
            String redirectUri = domain + "/aliPay/redirectUri";
            System.out.println(AliPayApiConfigKit.getAppId());
            String oauth2Url = AliPayApi.getOauth2Url(AliPayApiConfigKit.getAppId(), redirectUri);
            redirect(oauth2Url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 应用授权回调
     */
    public void redirectUri() {
        try {
            String appId = getPara("app_id");
            String appAuthCode = getPara("app_auth_code");
            System.out.println("app_id:" + appId);
            System.out.println("app_auth_code:" + appAuthCode);
            //使用app_auth_code换取app_auth_token
            AlipayOpenAuthTokenAppModel model = new AlipayOpenAuthTokenAppModel();
            model.setGrantType("authorization_code");
            model.setCode(appAuthCode);
            String result = AliPayApi.openAuthTokenAppToResponse(model).getBody();
            renderText(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询授权信息
     */
    public void openAuthTokenAppQuery() {
        try {
            String appAuthToken = getPara("app_auth_token");
            AlipayOpenAuthTokenAppQueryModel model = new AlipayOpenAuthTokenAppQueryModel();
            model.setAppAuthToken(appAuthToken);
            String result = AliPayApi.openAuthTokenAppQueryToResponse(model).getBody();
            renderText(result);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量付款到支付宝账户有密接口
     */
    public void batchTrans() {
        try {
            String signType = "MD5";
            String notifyUrl = domain + "/aliPay/notifyUrl";
            ;
            Map<String, String> params = new HashMap<>(9);
            params.put("partner", "PID");
            params.put("sign_type", signType);
            params.put("notify_url", notifyUrl);
            params.put("account_name", "xxx");
            params.put("detail_data", "流水号1^收款方账号1^收款账号姓名1^付款金额1^备注说明1|流水号2^收款方账号2^收款账号姓名2^付款金额2^备注说明2");
            params.put("batch_no", String.valueOf(System.currentTimeMillis()));
            params.put("batch_num", 1 + "");
            params.put("batch_fee", 10.00 + "");
            params.put("email", "xx@xxx.com");

            AliPayApi.batchTrans(params, AliPayApiConfigKit.getAliPayApiConfig().getPrivateKey(), signType, getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
        renderNull();
    }

    /**
     * 地铁购票核销码发码
     */
    public void voucherGenerate() {
        try {
            //需要支付成功的订单号
            String tradeNo = getPara("tradeNo");

            AlipayCommerceCityfacilitatorVoucherGenerateModel model = new AlipayCommerceCityfacilitatorVoucherGenerateModel();
            model.setCityCode("440300");
            model.setTradeNo(tradeNo);
            model.setTotalFee("8");
            model.setTicketNum("2");
            model.setTicketType("oneway");
            model.setSiteBegin("001");
            model.setSiteEnd("002");
            model.setTicketPrice("4");
            String result = AliPayApi.voucherGenerateToResponse(model).getBody();
            renderText(result);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            renderText(e.getMessage());
        }

    }

    public void returnUrl() {
        try {
            // 获取支付宝GET过来反馈信息
            Map<String, String> map = AliPayApi.toMap(getRequest());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCheckV1(map, AliPayApiConfigKit.getAliPayApiConfig().getAliPayPublicKey(), charset,
                    AliPayApiConfigKit.getAliPayApiConfig().getSignType());

            if (verifyResult) {
                // 验证成功
                // TODO 请在这里加上商户的业务逻辑程序代码
                System.out.println("return_url 验证成功");
                renderText("success");
                return;
            } else {
                System.out.println("return_url 验证失败");
                // TODO
                renderText("failure");
                return;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            renderText("failure");
        }
    }

    public void notifyUrl() {
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = AliPayApi.toMap(getRequest());

            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCheckV1(params, AliPayApiConfigKit.getAliPayApiConfig().getAliPayPublicKey(), charset,
                    AliPayApiConfigKit.getAliPayApiConfig().getSignType());

            if (verifyResult) {
                // 验证成功
                // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
                System.out.println("notify_url 验证成功succcess");
                renderText("success");
                return;
            } else {
                System.out.println("notify_url 验证失败");
                // TODO
                renderText("failure");
                return;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            renderText("failure");
        }
    }
    //=======其实异步通知实现的方法都一样  但是通知中无法区分支付的方式(没有提供支付方式的参数)======================================================================

    /**
     * App支付支付回调通知
     * https://doc.open.alipay.com/docs/doc.htm?treeId=54&articleId=106370&
     * docType=1#s3
     */
    public void appPayNotify() {
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = AliPayApi.toMap(getRequest());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
            // 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
            // boolean AlipaySignature.rsaCheckV1(Map<String, String> params,
            // String publicKey, String charset, String sign_type)
            boolean flag = AlipaySignature.rsaCheckV1(params, AliPayApiConfigKit.getAliPayApiConfig().getAliPayPublicKey(), charset,
					AliPayApiConfigKit.getAliPayApiConfig().getSignType());
            if (flag) {
                // TODO
                System.out.println("success");
                renderText("success");
                return;
            } else {
                // TODO
                System.out.println("failure");
                renderText("failure");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            renderText("failure");
        }
    }

    /**
     * 扫码支付通知
     */
    public void preCreateNotifyUrl() {
        try {
            Map<String, String> map = AliPayApi.toMap(getRequest());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
            boolean flag = AlipaySignature.rsaCheckV1(map, AliPayApiConfigKit.getAliPayApiConfig().getAliPayPublicKey(), charset,
					AliPayApiConfigKit.getAliPayApiConfig().getSignType());
            if (flag) {
                // TODO
                System.out.println("precreate_notify_url success");
                renderText("success");
                return;
            } else {
                // TODO
                System.out.println("precreate_notify_url failure");
                renderText("failure");
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            renderText("failure");
        }
    }
}