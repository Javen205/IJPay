package com.ijpay.demo.controller.wxpay;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.*;
import com.ijpay.demo.entity.H5SceneInfo;
import com.ijpay.demo.vo.AjaxResult;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.*;
import com.jfinal.kit.*;
import com.jfinal.log.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Javen
 */
public class WxPayController extends WxPayApiController {
    private static Log log = Log.getLog(WxPayController.class);

    private static final Prop PROP = PropKit.use("wxpay.properties");
    private static final String USER_PAYING = "USERPAYING";

    private String appId = PROP.get("wxpay.appId");
    private String mchId = PROP.get("wxpay.mchId");
    private String partnerKey = PROP.get("wxpay.partnerKey");
    private String certPath = PROP.get("wxpay.certPath");
    private String domain = PROP.get("wxpay.domain");
    private String notifyUrl = domain.concat("/wxPay/payNotify");
    private String refundNotifyUrl = domain.concat("/wxPay/refundNotify");

    @Override
    public WxPayApiConfig getApiConfig() {
        WxPayApiConfig apiConfig;

        try {
            apiConfig = WxPayApiConfigKit.getApiConfig(appId);
        } catch (Exception e) {
            apiConfig = WxPayApiConfig.builder()
                    .appId(appId)
                    .mchId(mchId)
                    .partnerKey(partnerKey)
                    .certPath(certPath)
                    .domain(domain)
                    .build();
        }
        return apiConfig;
    }


    public void index() {
        log.info("欢迎使用 IJPay 中的微信支付 -By Javen  <br/><br>  交流群：723992875");
        log.error(JsonKit.toJson(WxPayApiConfigKit.getWxPayApiConfig()));
        renderHtml("欢迎使用 IJPay 中的微信支付 -By Javen  <br/><br>  交流群：723992875");
    }

    public void getKey() {
        renderText(WxPayApi.getSignKey(WxPayApiConfigKit.getWxPayApiConfig().getMchId(),
                WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.MD5));
    }

    /**
     * 微信H5 支付
     * 注意：必须再web页面中发起支付且域名已添加到开发配置中
     */
    public void wapPay() {
        String ip = IpKit.getRealIp(getRequest());
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        H5SceneInfo sceneInfo = new H5SceneInfo();

        H5SceneInfo.H5 h5_info = new H5SceneInfo.H5();
        h5_info.setType("Wap");
        //此域名必须在商户平台--"产品中心"--"开发配置"中添加
        h5_info.setWap_url("https://gitee.com/javen205/IJPay");
        h5_info.setWap_name("IJPay VIP 充值");
        sceneInfo.setH5Info(h5_info);

        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("IJPay 让支付触手可及-H5支付")
                .attach("Node.js 版:https://gitee.com/javen205/TNWX")
                .out_trade_no(WxPayKit.generateStr())
                .total_fee("1000")
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.MWEB.getTradeType())
                .scene_info(JsonKit.toJson(sceneInfo))
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info(xmlResult);

        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String return_code = result.get("return_code");
        String return_msg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(return_code)) {
            renderJson(new AjaxResult().addError(return_msg));
            return;
        }
        String result_code = result.get("result_code");
        if (!WxPayKit.codeIsOk(result_code)) {
            renderJson(new AjaxResult().addError(return_msg));
            return;
        }
        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回

        String prepayId = result.get("prepay_id");
        String webUrl = result.get("mweb_url");

        log.info("prepay_id:" + prepayId + " mweb_url:" + webUrl);
        redirect(webUrl);
    }

    /**
     * 公众号支付
     */
    public void webPay() {
        String totalFee = getPara("total_fee");
        // openId，采用 网页授权获取 access_token API：SnsAccessTokenApi获取
        String openId = (String) getRequest().getSession().getAttribute("openId");
        if (openId == null) {
            openId = "11111111";
        }
        if (StrUtil.isEmpty(openId)) {
            renderJson(new AjaxResult().addError("openId is null"));
            return;
        }
        if (StrUtil.isEmpty(totalFee)) {
            renderJson(new AjaxResult().addError("请输入数字金额"));
            return;
        }
        String ip = IpKit.getRealIp(getRequest());
        if (StrUtil.isEmpty(ip)) {
            ip = "127.0.0.1";
        }

        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("IJPay 让支付触手可及-公众号支付")
                .attach("Node.js 版:https://gitee.com/javen205/TNWX")
                .out_trade_no(WxPayKit.generateStr())
                .total_fee("1000")
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(openId)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info(xmlResult);

        Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);
        String returnCode = resultMap.get("return_code");
        String returnMsg = resultMap.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            renderJson(new AjaxResult().addError(returnMsg));
            return;
        }
        String resultCode = resultMap.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            renderJson(new AjaxResult().addError(returnMsg));
            return;
        }

        // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回

        String prepayId = resultMap.get("prepay_id");

        Map<String, String> packageParams = WxPayKit.prepayIdCreateSign(prepayId, wxPayApiConfig.getAppId(),
                wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String jsonStr = JSON.toJSONString(packageParams);
        renderJson(new AjaxResult().success(jsonStr));
    }

    /**
     * 扫码模式一
     */
    public void scanCode1() {
        try {
            String productId = getPara("productId");
            if (StrKit.isBlank(productId)) {
                renderJson(new AjaxResult().addError("productId is null"));
                return;
            }
            WxPayApiConfig config = WxPayApiConfigKit.getWxPayApiConfig();
            //获取扫码支付（模式一）url
            String qrCodeUrl = WxPayKit.bizPayUrl(config.getPartnerKey(), config.getAppId(), config.getMchId(), productId);
            log.info(qrCodeUrl);
            //生成二维码保存的路径
            String name = "payQRCode1.png";
            boolean encode = QrCodeKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
                    PathKit.getWebRootPath() + File.separator + name);
            if (encode) {
                //在页面上显示
                renderJson(new AjaxResult().success(name));
            }
        } catch (Exception e) {
            renderJson(new AjaxResult().addError("系统异常：" + e.getMessage()));
            e.printStackTrace();
        }
    }

    /**
     * 扫码支付模式一回调
     */
    public void scanCodeNotify() {
        try {
            String result = HttpKit.readData(getRequest());
            log.info("scanCodeNotify>>>" + result);
            /**
             * 获取返回的信息内容中各个参数的值
             */
            Map<String, String> map = WxPayKit.xmlToMap(result);
            for (String key : map.keySet()) {
                log.info("key= " + key + " and value= " + map.get(key));
            }

            String appId = map.get("appid");
            String openId = map.get("openid");
            String mchId = map.get("mch_id");
            String isSubscribe = map.get("is_subscribe");
            String nonceStr = map.get("nonce_str");
            String productId = map.get("product_id");
            String sign = map.get("sign");

            Map<String, String> packageParams = new HashMap<String, String>(6);
            packageParams.put("appid", appId);
            packageParams.put("openid", openId);
            packageParams.put("mch_id", mchId);
            packageParams.put("is_subscribe", isSubscribe);
            packageParams.put("nonce_str", nonceStr);
            packageParams.put("product_id", productId);

            WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

            String packageSign = WxPayKit.createSign(packageParams, wxPayApiConfig.getPartnerKey(), SignType.MD5);

            String ip = IpKit.getRealIp(getRequest());
            if (StrKit.isBlank(ip)) {
                ip = "127.0.0.1";
            }
            Map<String, String> params = UnifiedOrderModel
                    .builder()
                    .appid(wxPayApiConfig.getAppId())
                    .mch_id(wxPayApiConfig.getMchId())
                    .nonce_str(WxPayKit.generateStr())
                    .body("IJPay 让支付触手可及-扫码支付模式一")
                    .attach("Node.js 版:https://gitee.com/javen205/TNWX")
                    .out_trade_no(WxPayKit.generateStr())
                    .total_fee("1")
                    .spbill_create_ip(ip)
                    .notify_url(notifyUrl)
                    .trade_type(TradeType.NATIVE.getTradeType())
                    .openid(openId)
                    .build()
                    .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
            String xmlResult = WxPayApi.pushOrder(false, params);
            log.info("统一下单:" + xmlResult);
            /**
             * 发送信息给微信服务器
             */
            Map<String, String> payResult = WxPayKit.xmlToMap(xmlResult);
            String returnCode = payResult.get("return_code");
            String resultCode = payResult.get("result_code");
            if (WxPayKit.codeIsOk(returnCode) && WxPayKit.codeIsOk(resultCode)) {
                // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
                String prepayId = payResult.get("prepay_id");

                Map<String, String> prepayParams = new HashMap<String, String>(10);
                prepayParams.put("return_code", "SUCCESS");
                prepayParams.put("appid", appId);
                prepayParams.put("mch_id", mchId);
                prepayParams.put("nonce_str", System.currentTimeMillis() + "");
                prepayParams.put("prepay_id", prepayId);
                String prepaySign = null;
                if (sign.equals(packageSign)) {
                    prepayParams.put("result_code", "SUCCESS");
                } else {
                    prepayParams.put("result_code", "FAIL");
                    //result_code为FAIL时，添加该键值对，value值是微信告诉客户的信息
                    prepayParams.put("err_code_des", "订单失效");
                }
                prepaySign = WxPayKit.createSign(prepayParams, wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
                prepayParams.put("sign", prepaySign);
                String xml = WxPayKit.toXml(prepayParams);
                log.error(xml);
                renderText(xml);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫码支付模式二
     */
    public void scanCode2() {
        String totalFee = getPara("total_fee");

        if (StrKit.isBlank(totalFee)) {
            renderJson(new AjaxResult().addError("支付金额不能为空"));
            return;
        }

        String ip = IpKit.getRealIp(getRequest());
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("IJPay 让支付触手可及-扫码支付模式二")
                .attach("Node.js 版:https://gitee.com/javen205/TNWX")
                .out_trade_no(WxPayKit.generateStr())
                .total_fee("1")
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.NATIVE.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);
        log.info("统一下单:" + xmlResult);

        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            System.out.println(returnMsg);
            renderText(xmlResult);
            return;
        }
        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            System.out.println(returnMsg);
            renderText(xmlResult);
            return;
        }
        //生成预付订单success

        String qrCodeUrl = result.get("code_url");
        String name = "payQRCode2.png";

        Boolean encode = QrCodeKit.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 3, ErrorCorrectionLevel.H, "png", 200, 200,
                PathKit.getWebRootPath() + File.separator + name);
        if (encode) {
            //在页面上显示
            renderJson(new AjaxResult().success(name));
        }
    }

    /**
     * 刷卡支付
     */
    public void microPay() {
        String authCode = getPara("auth_code");
        String totalFee = getPara("total_fee");
        if (StrKit.isBlank(totalFee)) {
            renderJson(new AjaxResult().addError("支付金额不能为空"));
            return;
        }
        if (StrKit.isBlank(authCode)) {
            renderJson(new AjaxResult().addError("auth_code参数错误"));
            return;
        }
        String ip = IpKit.getRealIp(getRequest());
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        Map<String, String> params = MicroPayModel.builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("IJPay 让支付触手可及-刷卡支付")
                .attach("Node.js 版:https://gitee.com/javen205/TNWX")
                .out_trade_no(WxPayKit.generateStr())
                .total_fee("1")
                .spbill_create_ip(ip)
                .auth_code(authCode)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.microPay(false, params);
        //同步返回结果
        log.info("xmlResult:" + xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            //通讯失败
            String errCode = result.get("err_code");
            if (StrKit.notBlank(errCode)) {
                //用户支付中，需要输入密码
                if (USER_PAYING.equals(errCode)) {
                    //等待5秒后调用【查询订单API】
                }
            }
            log.info("提交刷卡支付失败>>" + xmlResult);
            renderJson(new AjaxResult().addError(returnMsg));
            return;
        }

        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            log.info("支付失败>>" + xmlResult);
            String errCodeDes = result.get("err_code_des");
            renderJson(new AjaxResult().addError(errCodeDes));
            return;
        }
        //支付成功
        renderText(xmlResult);
    }

    /**
     * 微信APP支付
     */
    public void appPay() {

        String ip = IpKit.getRealIp(getRequest());
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("IJPay 让支付触手可及-App支付")
                .attach("Node.js 版:https://gitee.com/javen205/TNWX")
                .out_trade_no(WxPayKit.generateStr())
                .total_fee("1000")
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.APP.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        log.info(xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            renderJson(new AjaxResult().addError(returnMsg));
            return;
        }
        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            renderJson(new AjaxResult().addError(returnMsg));
            return;
        }
        // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
        String prepayId = result.get("prepay_id");

        Map<String, String> packageParams = WxPayKit.appPrepayIdCreateSign(wxPayApiConfig.getAppId(), wxPayApiConfig.getMchId(), prepayId,
                wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String jsonStr = JsonKit.toJson(packageParams);
        log.info("返回apk的参数:" + jsonStr);
        renderJson(jsonStr);
    }

    /**
     * 微信小程序支付
     */
    public void miniAppPay() {
        //需要通过授权来获取openId
        String openId = (String) getSession().getAttribute("openId");

        String ip = IpKit.getRealIp(getRequest());
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("IJPay 让支付触手可及-小程序支付")
                .attach("Node.js 版:https://gitee.com/javen205/TNWX")
                .out_trade_no(WxPayKit.generateStr())
                .total_fee("1000")
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(openId)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);

        String xmlResult = WxPayApi.pushOrder(false, params);

        log.info(xmlResult);
        Map<String, String> result = WxPayKit.xmlToMap(xmlResult);

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            renderJson(new AjaxResult().addError(returnMsg));
            return;
        }
        String resultCode = result.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            renderJson(new AjaxResult().addError(returnMsg));
            return;
        }
        // 以下字段在 return_code 和 result_code 都为 SUCCESS 的时候有返回
        String prepayId = result.get("prepay_id");
        Map<String, String> packageParams = WxPayKit.miniAppPrepayIdCreateSign(wxPayApiConfig.getAppId(), prepayId,
                wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
        String jsonStr = JsonKit.toJson(packageParams);
        log.info("小程序支付的参数:" + jsonStr);
        renderJson(jsonStr);
    }

    /**
     * 企业付款到零钱
     */
    public void transfer() {
        String openId = getSessionAttr("openId");
        openId = "oRMVFv8zhSH--EhJiu3z9G3kNX-o";

        String ip = IpKit.getRealIp(getRequest());
        if (StrKit.isBlank(ip)) {
            ip = "127.0.0.1";
        }

        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        Map<String, String> params = TransferModel.builder()
                .mch_appid(wxPayApiConfig.getAppId())
                .mchid(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .partner_trade_no(WxPayKit.generateStr())
                .openid(openId)
                .check_name("NO_CHECK")
                .amount("100")
                .desc("IJPay 让支付触手可及-企业付款")
                .spbill_create_ip(ip)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(),  SignType.MD5,false);

        // 提现
        String transfers = WxPayApi.transfers(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
        log.info("提现结果:" + transfers);
        Map<String, String> map = WxPayKit.xmlToMap(transfers);
        String returnCode = map.get("return_code");
        String resultCode = map.get("result_code");
        if (WxPayKit.codeIsOk(returnCode) && WxPayKit.codeIsOk(resultCode)) {
            // 提现成功
        } else {
            // 提现失败
        }
        renderText(transfers);
    }

    /**
     * 查询企业付款到零钱
     */
    public void transferInfo() {
        try {
            String partnerTradeNo = getPara("partner_trade_no");

            WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

            Map<String, String> params = GetTransferInfoModel.builder()
                    .nonce_str(WxPayKit.generateStr())
                    .partner_trade_no(partnerTradeNo)
                    .mch_id(wxPayApiConfig.getMchId())
                    .appid(wxPayApiConfig.getAppId())
                    .build()
                    .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5,false);

            String transferInfo = WxPayApi.getTransferInfo(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
            renderText(transferInfo);
        } catch (Exception e) {
            e.printStackTrace();
            renderText(e.getMessage());
        }
    }


    /**
     * 获取RSA加密公钥
     */
    public void getPublicKey() {
        try {
            WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

            Map<String, String> params = new HashMap<String, String>(4);
            params.put("mch_id", wxPayApiConfig.getMchId());
            params.put("nonce_str", String.valueOf(System.currentTimeMillis()));
            params.put("sign_type", "MD5");
            String createSign = WxPayKit.createSign(params, wxPayApiConfig.getPartnerKey(), SignType.MD5);
            params.put("sign", createSign);
            String publicKeyStr = WxPayApi.getPublicKey(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
            renderText(publicKeyStr);
        } catch (Exception e) {
            e.printStackTrace();
            renderText(e.getMessage());
        }
    }

    /**
     * 企业付款到银行卡
     */
    public void payBank() {
        try {
            WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

            //通过WxPayApi.getPublicKey接口获取RSA加密公钥
            //假设获取到的RSA加密公钥为PUBLIC_KEY(PKCS#8格式)
            final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6Bl76IwSvBTiibZ+CNRUA6BfahMshZ0WJpHD1GpmvcQjeN6Yrv6c9eIl6gB4nU3isN7bn+LmoVTpH1gHViaV2YyG/zXj4z4h7r+V+ezesMqqorEg38BCNUHNmhnw4/C0I4gBAQ4x0SJOGnfKGZKR9yzvbkJtvEn732JcEZCbdTZmaxkwlenXvM+mStcJaxBCB/h5xJ5VOF5nDbTPzLphIpzddr3zx/Jxjna9QB1v/YSKYXn+iuwruNUXGCvvxBWaBGKrjOdRTRy9adWOgNmtuYDQJ2YOfG8PtPe06ELKjmr2CfaAGrKKUroyaGvy3qxAV0PlT+UQ4ADSXWt/zl0o5wIDAQAB";

            Map<String, String> params = new HashMap<String, String>(10);
            params.put("mch_id", wxPayApiConfig.getMchId());
            params.put("partner_trade_no", System.currentTimeMillis() + "");
            params.put("nonce_str", System.currentTimeMillis() + "");
            //收款方银行卡号
            params.put("enc_bank_no", RsaKit.encryptByPublicKeyByWx("银行卡号", PUBLIC_KEY));
            //收款方用户名
            params.put("enc_true_name", RsaKit.encryptByPublicKeyByWx("银行卡持有人姓名", PUBLIC_KEY));
            //收款方开户行
            params.put("bank_code", "1001");
            params.put("amount", "1");
            params.put("desc", "IJPay 让支付触手可及-付款到银行卡");
            params.put("sign", WxPayKit.createSign(params, wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256));
            String payBank = WxPayApi.payBank(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
            renderText(payBank);
        } catch (Exception e) {
            e.printStackTrace();
            renderText(e.getMessage());
        }
    }

    /**
     * 查询企业付款到银行
     */
    public void queryBank() {
        try {
            WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

            String partnerTradeNo = getPara("partner_trade_no");
            Map<String, String> params = new HashMap<String, String>(4);
            params.put("mch_id", wxPayApiConfig.getMchId());
            params.put("partner_trade_no", partnerTradeNo);
            params.put("nonce_str", System.currentTimeMillis() + "");
            params.put("sign", WxPayKit.createSign(params, wxPayApiConfig.getPartnerKey(), SignType.MD5));
            String queryBank = WxPayApi.queryBank(params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
            renderText(queryBank);
        } catch (Exception e) {
            e.printStackTrace();
            renderText(e.getMessage());
        }
    }

    /**
     * 微信退款
     */
    public void refund() {
        String transactionId = getPara("transactionId");
        String outTradeNo = getPara("out_trade_no");

        if (StrKit.isBlank(outTradeNo) && StrKit.isBlank(transactionId)) {
            renderText("transactionId、out_trade_no二选一");
            return;
        }
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        Map<String, String> params = RefundModel.builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .transaction_id(transactionId)
                .out_trade_no(outTradeNo)
                .out_refund_no(WxPayKit.generateStr())
                .total_fee("1")
                .refund_fee("1")
                .notify_url(refundNotifyUrl)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5);

        String refund = WxPayApi.orderRefund(false, params, wxPayApiConfig.getCertPath(), wxPayApiConfig.getMchId());
        renderText(refund);
    }

    /**
     * 微信退款查询
     */
    public void refundQuery() {
        String transactionId = getPara("transactionId");
        String outTradeNo = getPara("out_trade_no");
        String outRefundNo = getPara("out_refund_no");
        String refundId = getPara("refund_id");

        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();

        Map<String, String> params = RefundQueryModel.builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .transaction_id(transactionId)
                .out_trade_no(outTradeNo)
                .out_refund_no(outRefundNo)
                .refund_id(refundId)
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5);

        String refund = WxPayApi.orderRefundQuery(false, params);
        renderText(refund);
    }
    /**
     * 退款通知
     */
    public String refundNotify() {
        String xmlMsg = HttpKit.readData(getRequest());
        log.info("退款通知=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

        String returnCode = params.get("return_code");
        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
        if (WxPayKit.codeIsOk(returnCode)) {
            String reqInfo = Base64Kit.decodeToStr(params.get("req_info"));
            String decryptData = WxPayKit.decryptData(reqInfo, WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey());
            log.info("退款通知解密后的数据=" + decryptData);
            // 更新订单信息
            // 发送通知等
            Map<String, String> xml = new HashMap<String, String>(2);
            xml.put("return_code", "SUCCESS");
            xml.put("return_msg", "OK");
            return WxPayKit.toXml(xml);
        }
        return null;
    }
    /**
     * 异步通知
     */
    public void payNotify() {
        String xmlMsg = HttpKit.readData(getRequest());
        System.out.println("支付通知=" + xmlMsg);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);

        String resultCode = params.get("result_code");

        // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态

        if (WxPayKit.verifyNotify(params, WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey())) {
            if (WxPayKit.codeIsOk(resultCode)) {
                // 更新订单信息
                // 发送通知等
                Map<String, String> xml = new HashMap<String, String>(2);
                xml.put("return_code", "SUCCESS");
                xml.put("return_msg", "OK");
                renderText(WxPayKit.toXml(xml));
                return;
            }
        }
        renderText("");
    }

}
