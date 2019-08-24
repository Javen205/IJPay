package com.ijpay.demo.controller.jdpay;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.XmlUtil;
import com.ijpay.demo.entity.JdPayBean;
import com.ijpay.demo.vo.AjaxResult;
import com.ijpay.jdpay.JdPayApi;
import com.ijpay.jdpay.model.*;
import com.ijpay.jdpay.kit.JdPayKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
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
@RequestMapping("/JDPay")
public class JdPayController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JdPayBean jdPayBean;

    @RequestMapping("")
    @ResponseBody
    public String index() {
        log.info("欢迎使用 IJPay 中的京东支付 -By Javen  <br/><br>  交流群：723992875");
        log.info(jdPayBean.toString());
        return ("欢迎使用 IJPay 中的京东支付 -By Javen  <br/><br>  交流群：723992875");
    }

    @GetMapping("/test")
    @ResponseBody
    public JdPayBean test() {
        return jdPayBean;
    }

    /**
     * App 支付
     *
     * @return
     */
    @RequestMapping(value = "/appPay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult appPay() {
        String reqXml = UniOrderModel.builder()
                .version("V2.0")
                .merchant(jdPayBean.getMchId())
                .tradeNum(System.currentTimeMillis() + "")
                .tradeName("IJPay 让支付触手可及")
                .tradeDesc("https://gitee.com/javen205/IJPay")
                .tradeTime(DateUtil.format(new Date(), "yyyyMMddHHmmss"))
                .amount("1")
                .orderType("1")
                .currency("CNY")
                .note("备注")
                .notifyUrl("http://jdpaydemo.jd.com/asynNotify.htm")
                .tradeType("GEN")
                .build()
                .genReqXml(jdPayBean.getRsaPrivateKey(), jdPayBean.getDesKey(), "V2.0", jdPayBean.getMchId());

        // 执行请求
        String resultData = JdPayApi.uniOrder(reqXml);

        log.info("resultData:" + resultData);

        // 解析响应的 xml 数据
        Map<String, String> map = JdPayKit.parseResp(resultData);

        String code = map.get("code");
        String encrypt = map.get("encrypt");
        if (!"000000".equals(code)) {
            String desc = map.get("desc");
            return new AjaxResult().addError(desc);
        }
        // 解密并验证签名
        String decrypt = JdPayKit.decrypt(jdPayBean.getRsaPublicKey(), jdPayBean.getDesKey(), encrypt);

        log.info("decrypt>" + decrypt);

        // 将 xml 转化为 map
        Map<String, Object> toMap = XmlUtil.xmlToMap(decrypt);

        log.info("result toMap>" + toMap);


        String orderId = (String) toMap.get("orderId");

        StringBuilder sb = new StringBuilder();
        sb.append("merchant=").append(jdPayBean.getMchId());
        sb.append("&orderId=").append(orderId);
        sb.append("&key=").append("test");
        String sign = JdPayKit.md5LowerCase(sb.toString());

        return new AjaxResult().success(new AppParams(orderId, jdPayBean.getMchId(), "123456789",
                sign, "123456789"));
    }

    /**
     * PC H5 支付
     *
     * @param payType
     * @return
     */
    @RequestMapping(value = "/saveOrder", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView saveOrder(@RequestParam("payType") String payType) {

        ModelAndView mv = new ModelAndView();

        Map<String, String> map = SaveOrderModel.builder()
                .version("V2.0")
                .merchant(jdPayBean.getMchId())
                .tradeNum(System.currentTimeMillis()+"")
                .tradeName("IJPay")
                .tradeDesc("IJPay 让支付触手可及")
                .tradeTime(DateUtil.format(new Date(), "yyyyMMddHHmmss"))
                .amount("10000")
                .orderType("0")
                .currency("CNY")
                .note("IJPay 了解一下")
                .callbackUrl("https://jdpay.com")
                .notifyUrl("https://jdpay.com")
                .userId("IJPay001")
                .build()
                .createSign(jdPayBean.getRsaPrivateKey(), jdPayBean.getDesKey());

        mv.addObject("map", map);
        mv.addObject("payUrl", payType.equals("pc") ? JdPayApi.PC_SAVE_ORDER_URL : JdPayApi.H5_SAVE_ORDER_URL);
        mv.setViewName("jd_pc_h5.html");
        return mv;
    }

    /**
     * 商户二维码支付
     */
    @RequestMapping(value = "/customerPay", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView customerPay() {

        ModelAndView mv = new ModelAndView();

        Map<String, String> map = CustomerPayModel.builder()
                .version("V2.0")
                .merchant(jdPayBean.getMchId())
                .tradeNum(System.currentTimeMillis()+"")
                .tradeName("IJPay")
                .tradeDesc("IJPay 让支付触手可及")
                .tradeTime(DateUtil.format(new Date(), "yyyyMMddHHmmss"))
//                .amount("1000")
                .orderType("0")
                .currency("CNY")
                .note("IJPay 了解一下")
                .notifyUrl("https://jdpay.com")
                .build()
                .createSign(jdPayBean.getRsaPrivateKey(), jdPayBean.getDesKey());

        mv.addObject("map", map);
        mv.addObject("payUrl", JdPayApi.CUSTOMER_PAY_URL);
        mv.setViewName("jd_customer_pay.html");
        return mv;
    }

    @RequestMapping(value = "/queryOrder", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult queryOrder(@RequestParam("tradeType") String tradeType,
                                 @RequestParam("oTradeNum") String oTradeNum,
                                 @RequestParam("tradeNum") String tradeNum) {
        String reqXml = QueryOrderModel.builder()
                .version("V2.0")
                .merchant(jdPayBean.getMchId())
                .tradeNum(tradeNum)
                .tradeType(tradeType)
                .oTradeNum(oTradeNum)
                .build()
                .genReqXml(jdPayBean.getRsaPrivateKey(), jdPayBean.getDesKey(), "V2.0", jdPayBean.getMchId());
        String queryResult = JdPayApi.queryOrder(reqXml);
        log.info("queryResult:" + queryResult);

        // 解析响应的 xml 数据
        Map<String, String> map = JdPayKit.parseResp(queryResult);

        String code = map.get("code");
        String encrypt = map.get("encrypt");
        if (!"000000".equals(code)) {
            String desc = map.get("desc");
            return new AjaxResult().addError(desc);
        }
        // 解密并验证签名
        String decrypt = JdPayKit.decrypt(jdPayBean.getRsaPublicKey(), jdPayBean.getDesKey(), encrypt);

        log.info("decrypt>" + decrypt);

        return new AjaxResult().success(decrypt);
    }

    @RequestMapping(value = "/fkmPay", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult fkmPay(@RequestParam("token") String token,
                             @RequestParam("amount") String amount) {
        String reqXml = FkmModel.builder()
                .token(token)
                .version("V2.0")
                .merchant(jdPayBean.getMchId())
                .device("IJPay Dev")
                .tradeNum(System.currentTimeMillis()+"")
                .tradeName("IJPay 刷卡支付")
                .tradeDesc("IJPay 了解一下")
                .tradeTime(DateUtil.format(new Date(),"yyyyMMddHHmmss"))
                .amount(amount)
                .currency("CNY")
                .note("备注")
                .notifyUrl("https://gitee.com/javen205/IJPay")
                .build()
                .genReqXml(jdPayBean.getRsaPrivateKey(), jdPayBean.getDesKey(), "V2.0", jdPayBean.getMchId());
        String queryResult = JdPayApi.fkmPay(reqXml);
        log.info("queryResult:" + queryResult);

        // 解析响应的 xml 数据
        Map<String, String> map = JdPayKit.parseResp(queryResult);

        String code = map.get("code");
        String encrypt = map.get("encrypt");
        if (!"000000".equals(code)) {
            String desc = map.get("desc");
            return new AjaxResult().addError(desc);
        }
        // 解密并验证签名
        String decrypt = JdPayKit.decrypt(jdPayBean.getRsaPublicKey(), jdPayBean.getDesKey(), encrypt);

        log.info("decrypt>" + decrypt);

        return new AjaxResult().success(decrypt);
    }

    @RequestMapping(value = "/userRelation", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult userRelation(@RequestParam("userId") String userId,
                                   @RequestParam("type") String type) {
        String reqXml = UserRelationModel.builder()
                .version("V2.0")
                .merchant(jdPayBean.getMchId())
                .userId(userId)
                .build()
                .genReqXml(jdPayBean.getRsaPrivateKey(), jdPayBean.getDesKey(), "V2.0", jdPayBean.getMchId());

        String result = null;

        if ("get".equals(type)) {
            result = JdPayApi.getUserRelation(reqXml);
        } else {
            result = JdPayApi.cancelUserRelation(reqXml);
        }

        log.info(result);

        // 解析响应的 xml 数据
        Map<String, String> map = JdPayKit.parseResp(result);

        String code = map.get("code");
        String encrypt = map.get("encrypt");
        if (!"000000".equals(code)) {
            String desc = map.get("desc");
            return new AjaxResult().addError(desc);
        }
        // 解密并验证签名
        String decrypt = JdPayKit.decrypt(jdPayBean.getRsaPublicKey(), jdPayBean.getDesKey(), encrypt);

        log.info("decrypt>" + decrypt);

        Map<String, Object> toMap = XmlUtil.xmlToMap(decrypt);
        System.out.println(toMap);
        return new AjaxResult().success(decrypt);
    }

    @RequestMapping(value = "/refund", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult refund(@RequestParam("amount") String amount,
                             @RequestParam("oTradeNum") String oTradeNum,
                             @RequestParam("tradeNum") String tradeNum) {

        System.out.println(Base64.encode(FileUtil.readBytes(jdPayBean.getCertPath())));
        String reqXml = RefundModel.builder()
                .version("V2.0")
                .merchant(jdPayBean.getMchId())
                .tradeNum(tradeNum)
                .oTradeNum(oTradeNum)
                .amount(amount)
                .currency("CNY")
                .build()
                .genReqXml(jdPayBean.getRsaPrivateKey(), jdPayBean.getDesKey(), "V2.0", jdPayBean.getMchId());
        String queryResult = JdPayApi.refund(reqXml);
        log.info("queryResult:" + queryResult);

        // 解析响应的 xml 数据
        Map<String, String> map = JdPayKit.parseResp(queryResult);

        String code = map.get("code");
        String encrypt = map.get("encrypt");
        if (!"000000".equals(code)) {
            String desc = map.get("desc");
            return new AjaxResult().addError(desc);
        }
        // 解密并验证签名
        String decrypt = JdPayKit.decrypt(jdPayBean.getRsaPublicKey(), jdPayBean.getDesKey(), encrypt);

        log.info("decrypt>" + decrypt);

        return new AjaxResult().success(decrypt);
    }


    @RequestMapping(value = "/queryBaiTiaoFq", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public AjaxResult queryBaiTiaoFq(@RequestParam("amount") String amount) {
        String reqXml = QueryBaiTiaoFqModel.builder()
                .version("V2.0")
                .merchant(jdPayBean.getMchId())
                .tradeNum(System.currentTimeMillis() + "")
                .amount(amount)
                .build()
                .genReqXml(jdPayBean.getRsaPrivateKey(), jdPayBean.getDesKey(), "V2.0", jdPayBean.getMchId());

        String baiTiaoResult = JdPayApi.queryBaiTiaoFq(reqXml);

        log.info(baiTiaoResult);

        // 解析响应的 xml 数据
        Map<String, String> map = JdPayKit.parseResp(baiTiaoResult);

        String code = map.get("code");
        String encrypt = map.get("encrypt");
        if (!"000000".equals(code)) {
            String desc = map.get("desc");
            return new AjaxResult().addError(desc);
        }
        // 解密并验证签名
        String decrypt = JdPayKit.decrypt(jdPayBean.getRsaPublicKey(), jdPayBean.getDesKey(), encrypt);

        log.info("decrypt>" + decrypt);

        Map<String, Object> toMap = XmlUtil.xmlToMap(decrypt);
        System.out.println(toMap);
        return new AjaxResult().success(decrypt);
    }
}
