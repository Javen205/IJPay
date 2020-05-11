package com.ijpay.xpay.controller;

import com.ijpay.xpay.entity.PayBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Javen
 */
@Controller
@Slf4j
public class IndexController {

    private PayBean payBean;

    @Autowired
    public void setPayBean (PayBean payBean) {
        this.payBean = payBean;
    }

    @RequestMapping("")
    @ResponseBody
    public String index() {
        return "欢迎使用 IJPay 中的 XPay-交流群:723992875";
    }

    @RequestMapping("/test")
    @ResponseBody
    public PayBean test() {
        return payBean;
    }

    @RequestMapping("/toPay")
    public String toPay() {
        return "xpay.html";
    }

    @RequestMapping("/success")
    public String success() {
        return "success.html";
    }
}
