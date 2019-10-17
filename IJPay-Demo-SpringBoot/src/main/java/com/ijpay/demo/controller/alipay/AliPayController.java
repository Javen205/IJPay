package com.ijpay.demo.controller.alipay;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
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
import com.ijpay.core.kit.RsaKit;
import com.ijpay.demo.entity.AliPayBean;
import com.ijpay.demo.utils.StringUtils;
import com.ijpay.demo.vo.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p>支付宝支付 Demo</p>
 *
 * @author Javen
 */
@Controller
@RequestMapping("/aliPay")
public class AliPayController extends AbstractAliPayApiController {
    private static final Logger log = LoggerFactory.getLogger(AliPayController.class);

    @Autowired
    private AliPayBean aliPayBean;

    private AjaxResult result = new AjaxResult();

    @Override
    public AliPayApiConfig getApiConfig() {
        AliPayApiConfig aliPayApiConfig;
        try {
            aliPayApiConfig = AliPayApiConfigKit.getApiConfig(aliPayBean.getAppId());
        } catch (Exception e) {
            aliPayApiConfig = AliPayApiConfig.builder()
                    .setAppId(aliPayBean.getAppId())
                    .setAliPayPublicKey(aliPayBean.getPublicKey())
                    .setCharset("UTF-8")
                    .setPrivateKey(aliPayBean.getPrivateKey())
                    .setServiceUrl(aliPayBean.getServerUrl())
                    .setSignType("RSA2")
                    .build();
        }
        return aliPayApiConfig;
    }

    @RequestMapping("")
    @ResponseBody
    public String index() {
        return "欢迎使用 IJPay 中的支付宝支付 -By Javen  <br/><br>  交流群：723992875";
    }

    @RequestMapping("/test")
    @ResponseBody
    public AliPayApiConfig test() {
        AliPayApiConfig aliPayApiConfig = AliPayApiConfigKit.getAliPayApiConfig();
        String charset = aliPayApiConfig.getCharset();
        log.info("charset>" + charset);
        return aliPayApiConfig;
    }


    /**
     * app支付
     */
    @RequestMapping(value = "/appPay")
    @ResponseBody
    public AjaxResult appPay() {
        try {
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setBody("我是测试数据-By Javen");
            model.setSubject("App支付测试-By Javen");
            model.setOutTradeNo(StringUtils.getOutTradeNo());
            model.setTimeoutExpress("30m");
            model.setTotalAmount("0.01");
            model.setPassbackParams("callback params");
            model.setProductCode("QUICK_MSECURITY_PAY");
            String orderInfo = AliPayApi.appPayToResponse(model, aliPayBean.getDomain() + "/aliPay/notify_url").getBody();
            result.success(orderInfo);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            result.addError("system error:" + e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/wapPayNoSdk")
    @ResponseBody
    public void wapPayNoSdk(HttpServletResponse response) {
        try {
            AliPayApiConfig aliPayApiConfig = AliPayApiConfigKit.getAliPayApiConfig();
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("app_id", aliPayApiConfig.getAppId());
            paramsMap.put("method", "alipay.trade.wap.pay");
            paramsMap.put("return_url", aliPayBean.getDomain() + "aliPay/return_url");
            paramsMap.put("charset", aliPayApiConfig.getCharset());
            paramsMap.put("sign_type", aliPayApiConfig.getSignType());
            paramsMap.put("timestamp", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            paramsMap.put("version", "1.0");
            paramsMap.put("notify_url", aliPayBean.getDomain() + "/aliPay/notify_url");

            Map<String, String> bizMap = new HashMap<>();
            bizMap.put("body", "IJPay 聚合支付-H5");
            bizMap.put("subject", "IJPay 让支付触手可及");
            bizMap.put("out_trade_no", StringUtils.getOutTradeNo());
            bizMap.put("total_amount", "6.66");
            bizMap.put("product_code", "QUICK_WAP_WAY");

            paramsMap.put("biz_content", JSON.toJSONString(bizMap));

            String content = PayKit.createLinkString(paramsMap);

            System.out.println(content);

            String encrypt = RsaKit.encryptByPrivateKey(content, aliPayApiConfig.getPrivateKey());
            System.out.println(encrypt);
//            encrypt = AlipaySignature.rsaSign(content,aliPayApiConfig.getPrivateKey(), "UTF-8","RSA2");
//            System.out.println(encrypt);
            paramsMap.put("sign", encrypt);

            String url = aliPayApiConfig.getServiceUrl() + "?" + PayKit.createLinkString(paramsMap,true);
            System.out.println(url);
            response.sendRedirect(url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/wapPay")
    @ResponseBody
    public void wapPay(HttpServletResponse response) {
        String body = "我是测试数据-By Javen";
        String subject = "Javen Wap支付测试";
        String totalAmount = "1";
        String passbackParams = "1";
        String returnUrl = aliPayBean.getDomain() + "/aliPay/return_url";
        String notifyUrl = aliPayBean.getDomain() + "/aliPay/notify_url";

        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setBody(body);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setPassbackParams(passbackParams);
        String outTradeNo = StringUtils.getOutTradeNo();
        System.out.println("wap outTradeNo>" + outTradeNo);
        model.setOutTradeNo(outTradeNo);
        model.setProductCode("QUICK_WAP_PAY");

        try {
            AliPayApi.wapPay(response, model, returnUrl, notifyUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * PC支付
     */
    @RequestMapping(value = "/pcPay")
    @ResponseBody
    public void pcPay(HttpServletResponse response) {
        try {
            String totalAmount = "88.88";
            String outTradeNo = StringUtils.getOutTradeNo();
            log.info("pc outTradeNo>" + outTradeNo);

            String returnUrl = aliPayBean.getDomain() + "/aliPay/return_url";
            String notifyUrl = aliPayBean.getDomain() + "/aliPay/notify_url";
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();

            model.setOutTradeNo(outTradeNo);
            model.setProductCode("FAST_INSTANT_TRADE_PAY");
            model.setTotalAmount(totalAmount);
            model.setSubject("Javen PC支付测试");
            model.setBody("Javen IJPay PC支付测试");
            model.setPassbackParams("passback_params");
            /**
             * 花呗分期相关的设置,测试环境不支持花呗分期的测试
             * hb_fq_num代表花呗分期数，仅支持传入3、6、12，其他期数暂不支持，传入会报错；
             * hb_fq_seller_percent代表卖家承担收费比例，商家承担手续费传入100，用户承担手续费传入0，仅支持传入100、0两种，其他比例暂不支持，传入会报错。
             */
            ExtendParams extendParams = new ExtendParams();
            extendParams.setHbFqNum("3");
            extendParams.setHbFqSellerPercent("0");
            model.setExtendParams(extendParams);

            AliPayApi.tradePage(response, model, notifyUrl, returnUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/tradePay")
    @ResponseBody
    public String tradePay(@RequestParam("auth_code") String authCode, @RequestParam("scene") String scene) {
        String subject = null;
        String waveCode = "wave_code";
        String barCode = "bar_code";
        if (scene.equals(waveCode)) {
            subject = "Javen 支付宝声波支付测试";
        } else if (scene.equals(barCode)) {
            subject = "Javen 支付宝条形码支付测试";
        }
        String totalAmount = "100";
        String notifyUrl = aliPayBean.getDomain() + "/aliPay/notify_url";

        AlipayTradePayModel model = new AlipayTradePayModel();
        model.setAuthCode(authCode);
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setOutTradeNo(StringUtils.getOutTradeNo());
        model.setScene(scene);
        try {
            return AliPayApi.tradePayToResponse(model, notifyUrl).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 扫码支付
     */
    @RequestMapping(value = "/tradePrecreatePay")
    @ResponseBody
    public String tradePrecreatePay() {
        String subject = "Javen 支付宝扫码支付测试";
        String totalAmount = "86";
        String storeId = "123";
        String notifyUrl = aliPayBean.getDomain() + "/aliPay/notify_url";

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(subject);
        model.setTotalAmount(totalAmount);
        model.setStoreId(storeId);
        model.setTimeoutExpress("5m");
        model.setOutTradeNo(StringUtils.getOutTradeNo());
        try {
            String resultStr = AliPayApi.tradePrecreatePayToResponse(model, notifyUrl).getBody();
            JSONObject jsonObject = JSONObject.parseObject(resultStr);
            return jsonObject.getJSONObject("alipay_trade_precreate_response").getString("qr_code");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 单笔转账到支付宝账户
     * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.54Ty29&
     * treeId=193&articleId=106236&docType=1
     */
    @RequestMapping(value = "/transfer")
    @ResponseBody
    public boolean transfer() {
        boolean isSuccess = false;
        String totalAmount = "66";
        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(StringUtils.getOutTradeNo());
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
        return isSuccess;
    }

    /**
     * 资金授权冻结接口
     */
    @RequestMapping(value = "/authOrderFreeze")
    @ResponseBody
    public AlipayFundAuthOrderFreezeResponse authOrderFreeze(@RequestParam("auth_code") String authCode) {
        try {
            AlipayFundAuthOrderFreezeModel model = new AlipayFundAuthOrderFreezeModel();
            model.setOutOrderNo(StringUtils.getOutTradeNo());
            model.setOutRequestNo(StringUtils.getOutTradeNo());
            model.setAuthCode(authCode);
            model.setAuthCodeType("bar_code");
            model.setOrderTitle("资金授权冻结-By IJPay");
            model.setAmount("36");
            model.setProductCode("PRE_AUTH");

            return AliPayApi.authOrderFreezeToResponse(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 红包协议支付接口
     * https://docs.open.alipay.com/301/106168/
     */
    @RequestMapping(value = "/agreementPay")
    @ResponseBody
    public AlipayFundCouponOrderAgreementPayResponse agreementPay() {
        try {
            AlipayFundCouponOrderAgreementPayModel model = new AlipayFundCouponOrderAgreementPayModel();
            model.setOutOrderNo(StringUtils.getOutTradeNo());
            model.setOutRequestNo(StringUtils.getOutTradeNo());
            model.setOrderTitle("红包协议支付接口-By IJPay");
            model.setAmount("36");
            model.setPayerUserId("2088102180432465");

            return AliPayApi.fundCouponOrderAgreementPayToResponse(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 下载对账单
     */
    @RequestMapping(value = "/dataDataserviceBill")
    @ResponseBody
    public String dataDataserviceBill(@RequestParam("billDate") String billDate) {
        try {
            AlipayDataDataserviceBillDownloadurlQueryModel model = new AlipayDataDataserviceBillDownloadurlQueryModel();
            model.setBillType("trade");
            model.setBillDate(billDate);
            return AliPayApi.billDownloadurlQuery(model);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 退款
     */
    @RequestMapping(value = "/tradeRefund")
    @ResponseBody
    public String tradeRefund() {

        try {
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo("081014283315023");
            model.setTradeNo("2017081021001004200200273870");
            model.setRefundAmount("86.00");
            model.setRefundReason("正常退款");
            return AliPayApi.tradeRefundToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 交易查询
     */
    @RequestMapping(value = "/tradeQuery")
    @ResponseBody
    public boolean tradeQuery() {
        boolean isSuccess = false;
        try {
            AlipayTradeQueryModel model = new AlipayTradeQueryModel();
            model.setOutTradeNo("081014283315023");
            model.setTradeNo("2017081021001004200200273870");

            isSuccess = AliPayApi.tradeQueryToResponse(model).isSuccess();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    @RequestMapping(value = "/tradeQueryByStr")
    @ResponseBody
    public String tradeQueryByStr(@RequestParam("out_trade_no") String outTradeNo) {
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(outTradeNo);

        try {
            return AliPayApi.tradeQueryToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 创建订单
     * {"alipay_trade_create_response":{"code":"10000","msg":"Success","out_trade_no":"081014283315033","trade_no":"2017081021001004200200274066"},"sign":"ZagfFZntf0loojZzdrBNnHhenhyRrsXwHLBNt1Z/dBbx7cF1o7SZQrzNjRHHmVypHKuCmYifikZIqbNNrFJauSuhT4MQkBJE+YGPDtHqDf4Ajdsv3JEyAM3TR/Xm5gUOpzCY7w+RZzkHevsTd4cjKeGM54GBh0hQH/gSyhs4pEN3lRWopqcKkrkOGZPcmunkbrUAF7+AhKGUpK+AqDw4xmKFuVChDKaRdnhM6/yVsezJFXzlQeVgFjbfiWqULxBXq1gqicntyUxvRygKA+5zDTqE5Jj3XRDjVFIDBeOBAnM+u03fUP489wV5V5apyI449RWeybLg08Wo+jUmeOuXOA=="}
     */
    @RequestMapping(value = "/tradeCreate")
    @ResponseBody
    public String tradeCreate(@RequestParam("out_trade_no") String outTradeNo) {

        String notifyUrl = aliPayBean.getDomain() + "/aliPay/notify_url";

        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(outTradeNo);
        model.setTotalAmount("88.88");
        model.setBody("Body");
        model.setSubject("Javen 测试统一收单交易创建接口");
        //买家支付宝账号，和buyer_id不能同时为空
        model.setBuyerLogonId("abpkvd0206@sandbox.com");
        try {
            AlipayTradeCreateResponse response = AliPayApi.tradeCreateToResponse(model, notifyUrl);
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 撤销订单
     */
    @RequestMapping(value = "/tradeCancel")
    @ResponseBody
    public boolean tradeCancel() {
        boolean isSuccess = false;
        try {
            AlipayTradeCancelModel model = new AlipayTradeCancelModel();
            model.setOutTradeNo("081014283315033");
            model.setTradeNo("2017081021001004200200274066");

            isSuccess = AliPayApi.tradeCancelToResponse(model).isSuccess();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/tradeClose")
    @ResponseBody
    public String tradeClose(@RequestParam("out_trade_no") String outTradeNo, @RequestParam("trade_no") String tradeNo) {
        try {
            AlipayTradeCloseModel model = new AlipayTradeCloseModel();
            model.setOutTradeNo(outTradeNo);

            model.setTradeNo(tradeNo);

            return AliPayApi.tradeCloseToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 结算
     */
    @RequestMapping(value = "/tradeOrderSettle")
    @ResponseBody
    public String tradeOrderSettle(@RequestParam("trade_no") String tradeNo) {
        try {
            AlipayTradeOrderSettleModel model = new AlipayTradeOrderSettleModel();
            model.setOutRequestNo(StringUtils.getOutTradeNo());
            model.setTradeNo(tradeNo);

            return AliPayApi.tradeOrderSettleToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用授权URL并授权
     */
    @RequestMapping(value = "/toOauth")
    @ResponseBody
    public void toOauth(HttpServletResponse response) {
        try {
            String redirectUri = aliPayBean.getDomain() + "/aliPay/redirect_uri";
            String oauth2Url = AliPayApi.getOauth2Url(aliPayBean.getAppId(), redirectUri);
            response.sendRedirect(oauth2Url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 应用授权回调
     */
    @RequestMapping(value = "/redirect_uri")
    @ResponseBody
    public String redirectUri(@RequestParam("app_id") String appId, @RequestParam("app_auth_code") String appAuthCode) {
        try {
            System.out.println("app_id:" + appId);
            System.out.println("app_auth_code:" + appAuthCode);
            //使用app_auth_code换取app_auth_token
            AlipayOpenAuthTokenAppModel model = new AlipayOpenAuthTokenAppModel();
            model.setGrantType("authorization_code");
            model.setCode(appAuthCode);
            return AliPayApi.openAuthTokenAppToResponse(model).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询授权信息
     */
    @RequestMapping(value = "/openAuthTokenAppQuery")
    @ResponseBody
    public String openAuthTokenAppQuery(@RequestParam("app_auth_token") String appAuthToken) {
        try {
            AlipayOpenAuthTokenAppQueryModel model = new AlipayOpenAuthTokenAppQueryModel();
            model.setAppAuthToken(appAuthToken);
            return AliPayApi.openAuthTokenAppQueryToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量付款到支付宝账户有密接口
     */
    @RequestMapping(value = "/batchTrans")
    @ResponseBody
    public void batchTrans(HttpServletResponse response) {
        try {
            String signType = "MD5";
            String notifyUrl = aliPayBean.getDomain() + "/aliPay/notify_url";
            Map<String, String> params = new HashMap<>(15);
            params.put("partner", "PID");
            params.put("sign_type", signType);
            params.put("notify_url", notifyUrl);
            params.put("account_name", "xxx");
            params.put("detail_data", "流水号1^收款方账号1^收款账号姓名1^付款金额1^备注说明1|流水号2^收款方账号2^收款账号姓名2^付款金额2^备注说明2");
            params.put("batch_no", String.valueOf(System.currentTimeMillis()));
            params.put("batch_num", 1 + "");
            params.put("batch_fee", 10.00 + "");
            params.put("email", "xx@xxx.com");

            AliPayApi.batchTrans(params, aliPayBean.getPrivateKey(), signType, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 地铁购票核销码发码
     */
    @RequestMapping(value = "/voucherGenerate")
    @ResponseBody
    public String voucherGenerate(@RequestParam("tradeNo") String tradeNo) {
        try {
            //需要支付成功的订单号
//			String tradeNo = getPara("tradeNo");

            AlipayCommerceCityfacilitatorVoucherGenerateModel model = new AlipayCommerceCityfacilitatorVoucherGenerateModel();
            model.setCityCode("440300");
            model.setTradeNo(tradeNo);
            model.setTotalFee("8");
            model.setTicketNum("2");
            model.setTicketType("oneway");
            model.setSiteBegin("001");
            model.setSiteEnd("002");
            model.setTicketPrice("4");
            return AliPayApi.voucherGenerateToResponse(model).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/return_url")
    @ResponseBody
    public String returnUrl(HttpServletRequest request) {
        try {
            // 获取支付宝GET过来反馈信息
            Map<String, String> map = AliPayApi.toMap(request);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCheckV1(map, aliPayBean.getPublicKey(), "UTF-8",
                    "RSA2");

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码
                System.out.println("return_url 验证成功");

                return "success";
            } else {
                System.out.println("return_url 验证失败");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }


    @RequestMapping(value = "/notify_url")
    @ResponseBody
    public String notifyUrl(HttpServletRequest request) {
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = AliPayApi.toMap(request);

            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCheckV1(params, aliPayBean.getPublicKey(), "UTF-8", "RSA2");

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
                System.out.println("notify_url 验证成功succcess");
                return "success";
            } else {
                System.out.println("notify_url 验证失败");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
    }
}