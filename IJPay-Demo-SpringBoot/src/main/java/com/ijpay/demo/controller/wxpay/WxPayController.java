package com.ijpay.demo.controller.wxpay;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ijpay.demo.entity.WxPayBean;
import com.ijpay.demo.kit.IpKit;
import com.ijpay.demo.vo.AjaxResult;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
 * <p>微信支付 Demo</p>
 *
 * @author Javen
 */
@Controller
@RequestMapping("/wxpay")
public class WxPayController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    WxPayBean wxPayBean;

    @RequestMapping("")
    @ResponseBody
    public String index() {
        log.info("欢迎使用 IJPay 中的微信支付 -By Javen  <br/><br>  交流群：723992875");
        log.info(wxPayBean.toString());
        return ("欢迎使用 IJPay 中的微信支付 -By Javen  <br/><br>  交流群：723992875");
    }

    @GetMapping("/test")
    @ResponseBody
    public WxPayBean test() {
        return wxPayBean;
    }

    @GetMapping("/getKey")
    @ResponseBody
    public String getKey() {
        return WxPayApi.getSignKey(wxPayBean.getMchId(), wxPayBean.getPartnerKey(), SignType.MD5);
    }

    /**
     * 公众号支付
     */
    @RequestMapping(value = "/webPay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult webPay(HttpServletRequest request, @RequestParam("total_fee") String totalFee) {
        // openId，采用 网页授权获取 access_token API：SnsAccessTokenApi获取
        String openId = (String) request.getSession().getAttribute("openId");
        if (openId == null){
            openId = "11111111";
        }
        if (StrUtil.isEmpty(openId)) {
            return new AjaxResult().addError("openId is null");
        }
        if (StrUtil.isEmpty(totalFee)) {
            return new AjaxResult().addError("请输入数字金额");
        }
        String ip = IpKit.getRealIp(request);
        if (StrUtil.isEmpty(ip)) {
            ip = "127.0.0.1";
        }


        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayBean.getAppId())
                .mch_id(wxPayBean.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body("IJPay 让支付触手可及")
                .attach("Node.js 版:https://gitee.com/javen205/TNW")
                .out_trade_no(WxPayKit.generateStr())
                .total_fee("1000")
                .spbill_create_ip(ip)
                .notify_url(wxPayBean.getDomain().concat("/wxpay/pay_notify"))
                .trade_type(TradeType.JSAPI.getTradeType())
                .openid(openId)
                .build()
                .createSign(wxPayBean.getPartnerKey(), SignType.HMACSHA256);


        String xmlResult = WxPayApi.pushOrder(false,params);
        log.info(xmlResult);

        Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);
        String returnCode = resultMap.get("return_code");
        String returnMsg = resultMap.get("return_msg");
        if (!WxPayKit.codeIsOk(returnCode)) {
            return new AjaxResult().addError(returnMsg);
        }
        String resultCode = resultMap.get("result_code");
        if (!WxPayKit.codeIsOk(resultCode)) {
            return new AjaxResult().addError(returnMsg);
        }

        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回

        String prepayId = resultMap.get("prepay_id");

        Map<String, String> packageParams = WxPayKit.prepayIdCreateSign(prepayId, wxPayBean.getAppId(), wxPayBean.getPartnerKey(),SignType.HMACSHA256);

        String jsonStr = JSON.toJSONString(packageParams);
        return new AjaxResult().success(jsonStr);
    }


}
