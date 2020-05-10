package com.ijpay.demo.controller.xpay;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.demo.entity.XPayBean;
import com.ijpay.demo.vo.AjaxResult;
import com.ijpay.xpay.XPayApi;
import com.ijpay.xpay.enums.XPayUrl;
import com.ijpay.xpay.model.XPayModel;
import com.jfinal.kit.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>XPay</p>
 *
 * @author Javen
 */
@Controller
@RequestMapping("/XPay")
public class XPayController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    XPayBean xPayBean;

    private final String notifyUrl = "/XPay/payNotify";
    private final String returnUrl = "/XPay/returnCall";
    private final String oauthCallUrl = "/XPay/oauthCall?sdk=IJPay";

    @RequestMapping("")
    @ResponseBody
    public String index() {
        log.info("欢迎使用 IJPay 中的 XPay -By Javen  <br/><br>  交流群：723992875");
        log.info(xPayBean.toString());
        return ("欢迎使用 IJPay 中的 XPay -By Javen  <br/><br>  交流群：723992875");
    }

    /**
     * 刷码支付
     */
    @RequestMapping(value = "/microPay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult microPay(@RequestParam("auth_code") String auth_code, @RequestParam("total_fee") String total_fee) {
        try {
            if (StrKit.isBlank(total_fee)) {
                return new AjaxResult().addError("支付金额不能为空");
            }

            if (StrKit.isBlank(auth_code)) {
                return new AjaxResult().addError("auth_code 参数错误");
            }

            Map<String, String> params = XPayModel.builder()
                    .out_trade_no(PayKit.getSnowflake(1, 1).nextIdStr())
                    .total_fee(total_fee)
                    .mch_id(xPayBean.getMchId())
                    .body("IJPay-刷卡支付")
                    .auth_code(auth_code)
                    .build()
                    .createSign(xPayBean.getKey(), SignType.MD5, false);

            params.put("attach", "附加参数");
            params.put("notify_url", xPayBean.getDomain().concat(notifyUrl));

            IJPayHttpResponse result = XPayApi.exePost(xPayBean.getServerUrl(), XPayUrl.CODE_PAY, params);
            log.info("microPay result {}", result.getBody());
            JSONObject jsonObject = JSONUtil.parseObj(result.getBody());
            Integer code = jsonObject.getInt("code");
            if (code == 0) {
                JSONObject dataObject = jsonObject.getJSONObject("data");
                String status = dataObject.getStr("status");
                String outTradeNo = dataObject.getStr("outTradeNo");
                if (status.equals("SUCCESS")) {
                    return new AjaxResult().success(result.getBody());
                } else if (status.equalsIgnoreCase("paying")) {
                    while (true) {
                        Thread.sleep(5000);
                        IJPayHttpResponse queryResponse = queryCodePay(outTradeNo);
                        log.info("query microPay result {}", queryResponse.getBody());
                        JSONObject queryObject = JSONUtil.parseObj(queryResponse.getBody());
                        code = queryObject.getInt("code");
                        if (code == 0) {
                            dataObject = queryObject.getJSONObject("data");
                            status = dataObject.getStr("status");
                            log.info("query microPay status {}", status);
                            if (status.equals("SUCCESS")) {
                                return new AjaxResult().success("success");
                            } else if (status.equals("PAYERROR")) {
                                return new AjaxResult().addError("支付失败或取消支付");
                            }
                        }
                    }
                }
            } else {
                return new AjaxResult().addError(jsonObject.getStr("msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询刷码支付
     */
    @RequestMapping(value = "/getCodePay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String getCodePay(@RequestParam("orderId") String orderId) {
        return queryCodePay(orderId).getBody();
    }

    public IJPayHttpResponse queryCodePay(String orderId) {
        Map<String, String> params = XPayModel.builder()
                .out_trade_no(orderId)
                .mch_id(xPayBean.getMchId())
                .build()
                .createSign(xPayBean.getKey(), SignType.MD5, false);

        return XPayApi.exeGet(xPayBean.getServerUrl(), XPayUrl.GET_CODE_PAY_RESULT, params);
    }


    /**
     * 扫码支付
     */
    @RequestMapping(value = "/nativePay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult nativePay(@RequestParam("total_fee") String total_fee) {
        try {
            Map<String, String> params = XPayModel.builder()
                    .out_trade_no(PayKit.getSnowflake(1, 1).nextIdStr())
                    .total_fee(total_fee)
                    .mch_id(xPayBean.getMchId())
                    .body("IJPay 扫码支付")
                    .build()
                    .createSign(xPayBean.getKey(), SignType.MD5, false);

            params.put("type", "2");
            params.put("attach", "附加参数");
            params.put("notify_url", xPayBean.getDomain().concat(notifyUrl));

            IJPayHttpResponse result = XPayApi.exePost(xPayBean.getServerUrl(), XPayUrl.NATIVE_PAY, params);
            String body = result.getBody();
            log.info("nativePay body {}", body);
            JSONObject jsonObject = JSONUtil.parseObj(body);
            Integer status = jsonObject.getInt("code");
            if (status == 0) {
                return new AjaxResult().success(jsonObject.getStr("data"));
            }else {
                return new AjaxResult().success(jsonObject.getStr("msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/toOauth", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public void oauth(HttpServletResponse response) throws IOException {
        Map<String, String> params = XPayModel.builder()
                .url(xPayBean.getDomain().concat(oauthCallUrl))
                .build()
                .createSign(xPayBean.getKey(), SignType.MD5, false);

        params.put("params", "exParams");

        IJPayHttpResponse result = XPayApi.exePost(xPayBean.getServerUrl(), XPayUrl.GET_OAUTH_URL, params);
        String body = result.getBody();
        log.info("oauth {}", body);
        Map map = JSONUtil.toBean(body, Map.class);
        int code = (int) map.get("code");
        if (code == 0) {
            String url = (String) map.get("data");
            response.sendRedirect(url);
        }
    }

    @RequestMapping(value = "/oauthCall", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ModelAndView oauthCall(HttpServletRequest request) {
        try {
            Map<String, String> map = HttpKit.toMap(request);
            log.info("授权回调返回的参数: {}", map);
            String code = map.get("code");

            if (StrUtil.isNotEmpty(code)) {
                Map<String, String> params = XPayModel.builder()
                        .code(code)
                        .build()
                        .createSign(xPayBean.getKey(), SignType.MD5, false);

                IJPayHttpResponse result = XPayApi.exeGet(xPayBean.getServerUrl(), XPayUrl.GET_BASE_OAUTH_INFO, params);
                String body = result.getBody();
                log.info("get base info {}", body);
                JSONObject jsonObject = JSONUtil.parseObj(body);
                Integer status = jsonObject.getInt("code");
                if (status == 0) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    String openId = data.getStr("openId");
                    request.getSession().setAttribute("openId", openId);
                    return new ModelAndView("redirect:/toXPay");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公众号支付
     */
    @RequestMapping(value = "/jsPay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult jsPay(HttpServletRequest request, @RequestParam("total_fee") String total_fee) {
       try {
           String openId = (String) request.getSession().getAttribute("openId");

           if (StrUtil.isEmpty(openId)){
               return new AjaxResult().addError("请重新获取 openId");
           }

           Map<String, String> params = XPayModel.builder()
                   .out_trade_no(PayKit.getSnowflake(1, 1).nextIdStr())
                   .total_fee(total_fee)
                   .mch_id(xPayBean.getMchId())
                   .body("IJPay 公众号支付")
                   .openId(openId)
                   .build()
                   .createSign(xPayBean.getKey(), SignType.MD5, false);

           params.put("attach", "附加参数");
           params.put("notify_url", xPayBean.getDomain().concat(notifyUrl));

           IJPayHttpResponse result = XPayApi.exePost(xPayBean.getServerUrl(), XPayUrl.JS_API_PAY, params);
           String body = result.getBody();
           log.info("jsPay body {}", body);
           JSONObject jsonObject = JSONUtil.parseObj(body);
           Integer status = jsonObject.getInt("code");
           if (status == 0) {
               return new AjaxResult().success(jsonObject.getStr("data"));
           } else {
               return new AjaxResult().addError(jsonObject.getStr("msg"));
           }
       }catch (Exception e){
           e.printStackTrace();
       }
       return null;
    }

    /**
     * 小程序支付
     */
    @RequestMapping(value = "/minAppPay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String minAppPay() {
        Map<String, String> params = XPayModel.builder()
                .out_trade_no(PayKit.getSnowflake(1, 1).nextIdStr())
                .total_fee("0.01")
                .mch_id(xPayBean.getMchId())
                .body("IJPay 小程序支付")
                .build()
                .createSign(xPayBean.getKey(), SignType.MD5, false);

        params.put("attach", "附加参数");
        params.put("notify_url", xPayBean.getDomain().concat(notifyUrl));

        IJPayHttpResponse result = XPayApi.exePost(xPayBean.getServerUrl(), XPayUrl.MIN_APP_PAY, params);
        return result.getBody();
    }

    /**
     * H5 支付
     */
    @RequestMapping(value = "/wapPay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String wapPay() {
        Map<String, String> params = XPayModel.builder()
                .out_trade_no(PayKit.getSnowflake(1, 1).nextIdStr())
                .total_fee("0.01")
                .mch_id(xPayBean.getMchId())
                .body("IJPay H5支付")
                .build()
                .createSign(xPayBean.getKey(), SignType.MD5, false);

        params.put("attach", "附加参数");
        params.put("return_url", xPayBean.getDomain().concat(returnUrl));
        params.put("notify_url", xPayBean.getDomain().concat(notifyUrl));

        IJPayHttpResponse result = XPayApi.exePost(xPayBean.getServerUrl(), XPayUrl.WAP_PAY, params);
        return result.getBody();
    }

    /**
     * 收银台支付
     */
    @RequestMapping(value = "/cashierPay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String cashierPay() {
        Map<String, String> params = XPayModel.builder()
                .out_trade_no(PayKit.getSnowflake(1, 1).nextIdStr())
                .total_fee("0.01")
                .mch_id(xPayBean.getMchId())
                .body("IJPay 收银台")
                .build()
                .createSign(xPayBean.getKey(), SignType.MD5, false);

        params.put("attach", "附加参数");
        params.put("return_url", xPayBean.getDomain().concat(returnUrl));
        params.put("notify_url", xPayBean.getDomain().concat(notifyUrl));

        IJPayHttpResponse result = XPayApi.exePost(xPayBean.getServerUrl(), XPayUrl.CASHIER_PAY, params);
        return result.getBody();
    }

    /**
     * 刷脸支付
     */
    @RequestMapping(value = "/facePay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String facePay(@RequestParam("openId") String openId, @RequestParam("openId") String faceCode) {
        Map<String, String> params = XPayModel.builder()
                .out_trade_no(PayKit.getSnowflake(1, 1).nextIdStr())
                .total_fee("0.01")
                .mch_id(xPayBean.getMchId())
                .body("IJPay 收银台")
                .openId(openId)
                .face_code(faceCode)
                .build()
                .createSign(xPayBean.getKey(), SignType.MD5, false);

        params.put("attach", "附加参数");
        params.put("notify_url", xPayBean.getDomain().concat(notifyUrl));

        IJPayHttpResponse result = XPayApi.exePost(xPayBean.getServerUrl(), XPayUrl.FACE_PAY, params);
        return result.getBody();
    }

    /**
     * App 支付
     */
    @RequestMapping(value = "/appPay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String appPay(@RequestParam("appId") String appId) {
        Map<String, String> params = XPayModel.builder()
                .app_id(appId)
                .out_trade_no(PayKit.getSnowflake(1, 1).nextIdStr())
                .total_fee("0.01")
                .mch_id(xPayBean.getMchId())
                .body("IJPay 收银台")
                .build()
                .createSign(xPayBean.getKey(), SignType.MD5, false);

        params.put("attach", "附加参数");
        params.put("notify_url", xPayBean.getDomain().concat(notifyUrl));

        IJPayHttpResponse result = XPayApi.exePost(xPayBean.getServerUrl(), XPayUrl.FACE_PAY, params);
        return result.getBody();
    }

    /**
     * 退款
     */
    @RequestMapping(value = "/refund", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String refund(@RequestParam("orderId") String orderId) {
        Map<String, String> params = XPayModel.builder()
                .out_trade_no(orderId)
                .mch_id(xPayBean.getMchId())
                .money("0.01")
                .build()
                .createSign(xPayBean.getKey(), SignType.MD5, false);

        params.put("refund_desc", "IJPay 测试退款");

        IJPayHttpResponse result = XPayApi.exePost(xPayBean.getServerUrl(), XPayUrl.REFUND_ORDER, params);
        return result.getBody();
    }

    /**
     * 退款查询
     */
    @RequestMapping(value = "/refundQuery", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String refundQuery(@RequestParam("refundNo") String refundNo) {
        Map<String, String> params = XPayModel.builder()
                .mch_id(xPayBean.getMchId())
                .refund_no(refundNo)
                .build()
                .createSign(xPayBean.getKey(), SignType.MD5, false);

        IJPayHttpResponse result = XPayApi.exeGet(xPayBean.getServerUrl(), XPayUrl.REFUND_QUERY, params);
        return result.getBody();
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/closeOrder", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String closeOrder(@RequestParam("orderId") String orderId) {
        Map<String, String> params = XPayModel.builder()
                .out_trade_no(orderId)
                .mch_id(xPayBean.getMchId())
                .build()
                .createSign(xPayBean.getKey(), SignType.MD5, false);

        IJPayHttpResponse result = XPayApi.exePost(xPayBean.getServerUrl(), XPayUrl.CLOSE_ORDER, params);
        return result.getBody();
    }

    /**
     * 撤销订单
     */
    @RequestMapping(value = "/reverseOrder", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String reverseOrder(@RequestParam("orderId") String orderId) {
        Map<String, String> params = XPayModel.builder()
                .out_trade_no(orderId)
                .mch_id(xPayBean.getMchId())
                .build()
                .createSign(xPayBean.getKey(), SignType.MD5, false);

        IJPayHttpResponse result = XPayApi.exePost(xPayBean.getServerUrl(), XPayUrl.REVERSE_ORDER, params);
        return result.getBody();
    }

    /**
     * 同步回调
     */
    @RequestMapping(value = "/returnCall", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String returnCall(HttpServletRequest request) {
        Map<String, String> map = HttpKit.toMap(request);
        log.info("同步通知数据 {}", map);
        return "SUCCESS";
    }

    /**
     * 异步通知
     */
    @RequestMapping(value = "/payNotify", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String payNotify(HttpServletRequest request) {
        String msg = HttpKit.readData(request);
        log.info("支付通知数据 {}", msg);
        Map<String, String> map = new HashMap<>();
        String[] kv = msg.split("&");
        for (String kvStr : kv) {
            String[] kvObj = kvStr.split("=");
            map.put(kvObj[0], kvObj[1]);
        }

        Map<String, String> signMap = new HashMap<>(map);
        signMap.remove("payChannel");
        signMap.remove("time");
        signMap.remove("attach");
        signMap.remove("openId");

        log.info("回调参数：{}", map);
        log.info("参与签名参数：{}", signMap);

        boolean verify = WxPayKit.verifyNotify(signMap, xPayBean.getKey(), SignType.MD5);
        log.info("回调签名验证结果:" + verify);
        if (verify) {
            return "SUCCESS";
        }
        return "FAIL";
    }
}
