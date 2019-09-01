package com.ijpay.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * @author Javen
 */
@Controller
public class IndexController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("")
    @ResponseBody
    public String index() {
        logger.error("欢迎使用 IJPay -By Javen <br/><br>  交流群：723992875");
        return "欢迎使用 IJPay -By Javen <br/><br>  交流群：723992875";
    }

    @RequestMapping("/toWxH5Pay")
    public String toWxH5Pay() {
        return "wxh5pay.html";
    }

    @RequestMapping("/toWxPay")
    public String toWxPay() {
        return "wxpay.html";
    }

    @RequestMapping("/toWxSuPay")
    public String toWxSuPay() {
        return "wxsubpay.html";
    }

    @RequestMapping(value = "/payInputMoney")
    public ModelAndView payInputMoney() {
        ModelAndView mav = new ModelAndView("pay_input_money.html");
        mav.addObject("content", "xxx");
        return mav;
    }

    @RequestMapping(value = "/payKeyBoard")
    public String payKeyBoard() {
        return "pay_keyboard.html";
    }

    @RequestMapping(value = "/paySelectMoney")
    public String paySelectMoney() {
        return "pay_select_money.html";
    }

    @RequestMapping("/success")
    public String success() {
        return "success.html";
    }

    @RequestMapping(value = "/ss/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String pa(@PathVariable("id") Integer id) {
        return "id>" + id;
    }

    @RequestMapping(value = "/xx", method = RequestMethod.GET)
    @ResponseBody
    public String param(@RequestParam("id") Integer xx) {
        return "id>" + xx;
    }

    @RequestMapping(value = "/xxx", method = RequestMethod.GET)
    @ResponseBody
    public String param2(@RequestParam(value = "id", required = false, defaultValue = "2") Integer xx) {
        return "id>" + xx;
    }

}
