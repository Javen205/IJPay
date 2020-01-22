package com.ijpay.demo.controller;

import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Javen
 */
public class IndexController extends Controller {
    private Log log = Log.getLog(IndexController.class);

    public void index() {
        log.error("欢迎使用 IJPay -By Javen <br/><br>  交流群：723992875");
        render("index.html");
    }

    public void toWxH5Pay() {
        render("wxh5pay.html");
    }

    public void toWxPay() {
        render("wxpay.html");
    }

    public void toWxSuPay() {
        render("wxsubpay.html");
    }

    public void payInputMoney() {
        render("pay_input_money.html");
    }

    public void payKeyBoard() {
        render("pay_keyboard.html");
    }

    public void paySelectMoney() {
        render("pay_select_money.html");
    }

    public void success() {
        render("success.html");
    }

    public void json() {
        Map<String, Object> map = new HashMap<>();
        map.put("IJPay", "https://gitee.com/javen205/IJPay");
        map.put("IJPay Doc", "https://javen205.gitee.io/ijpay");
        map.put("author", "https://javen.blog.csdn.net");
        map.put("TNWX", "微信公众号开发脚手架 https://gitee.com/javen205/TNWX");

        log.info(JsonKit.toJson(map));

        renderJson(map);
    }

    public void test() {
        String jsonStr = "{\"access_token\":\"24_Yu_egoPZUfah7MaLRUKRFq7Zm_k5eMeDoNEfTSqsa_zI5DDJcC_pGykWsXWn3ywIWv_efZtuS194qysxKYGiRQ\",\"expires_in\":7200,\"refresh_token\":\"24_NHNbiHbsjkz6tPtkY7b7UyX_glaYrVHuWQ-KBU1KFt9SMGhHlY6UelStVO6BRm6w23vYPSB0Twr4DLI1x67UiQ\",\"openid\":\"oMOBxuAE9oK6sTSx3ntILJvXHDzU\",\"scope\":\"snsapi_base\"}";
        System.out.println(jsonStr);
        Map<String, Object> temp = (Map) JsonUtils.parse(jsonStr, Map.class);
        System.out.println(temp);
        renderJson(temp);
    }
}