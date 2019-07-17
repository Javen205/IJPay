package com.ijpay.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("")
    @ResponseBody
    public String index(){
        logger.error("欢迎使用 IJPay -By Javen <br/><br>  交流群：723992875");
        return "欢迎使用 IJPay -By Javen <br/><br>  交流群：723992875";
    }
}
