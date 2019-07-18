package com.ijpay.demo.controller.wxpay;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ijpay.demo.entity.WxPayBean;
import com.ijpay.demo.kit.IpKit;
import com.ijpay.demo.vo.AjaxResult;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.constant.enums.SignType;
import com.ijpay.wxpay.constant.enums.TradeType;
import com.ijpay.wxpay.kit.WxPayKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Javen
 */
@Controller
@RequestMapping("/wxpay")
public class WxPayController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private AjaxResult result = new AjaxResult();

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
    public AjaxResult webPay(HttpServletRequest request, @RequestParam("total_fee") String total_fee) {
        // openId，采用 网页授权获取 access_token API：SnsAccessTokenApi获取
        String openId = (String) request.getSession().getAttribute("openId");
        if (openId == null){
            openId = "11111111";
        }
        if (StrUtil.isEmpty(openId)) {
            result.addError("openId is null");
            return result;
        }
        if (StrUtil.isEmpty(total_fee)) {
            result.addError("请输入数字金额");
            return result;
        }
        String ip = IpKit.getRealIp(request);
        if (StrUtil.isEmpty(ip)) {
            ip = "127.0.0.1";
        }

        Map<String, String> params = new HashMap<>();
        params.put("appid", wxPayBean.getAppId());
        params.put("mch_id", wxPayBean.getMchId());
        params.put("body", "IJPay");
        params.put("attach", "IJPay");
        params.put("out_trade_no", WxPayKit.generateStr());
        params.put("total_fee", total_fee);
        params.put("spbill_create_ip", ip);
        params.put("notify_url", wxPayBean.getDomain().concat("/wxpay/pay_notify"));
        params.put("trade_type", TradeType.JSAPI.getTradeType());

        String xmlResult = WxPayApi.pushOrder(false,WxPayKit.buildSign(params, wxPayBean.getPartnerKey(), SignType.MD5));
        log.info(xmlResult);

        Map<String, String> resultMap = WxPayKit.xmlToMap(xmlResult);
        String return_code = resultMap.get("return_code");
        String return_msg = resultMap.get("return_msg");
        if (!WxPayKit.codeIsOK(return_code)) {
            result.addError(return_msg);
            return result;
        }
        String result_code = resultMap.get("result_code");
        if (!WxPayKit.codeIsOK(result_code)) {
            result.addError(return_msg);
            return result;
        }

        // 以下字段在return_code 和result_code都为SUCCESS的时候有返回

        String prepay_id = resultMap.get("prepay_id");

        Map<String, String> packageParams = WxPayKit.prepayIdCreateSign(prepay_id, wxPayBean.getAppId(), wxPayBean.getPartnerKey());

        String jsonStr = JSON.toJSONString(packageParams);
        result.success(jsonStr);
        return result;
    }
}
