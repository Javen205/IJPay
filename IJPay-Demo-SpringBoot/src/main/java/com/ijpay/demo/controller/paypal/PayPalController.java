/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>PayPal 支付示例</p>
 *
 * @author Javen
 */
package com.ijpay.demo.controller.paypal;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.PayKit;
import com.ijpay.demo.entity.PayPalBean;
import com.ijpay.paypal.PayPalApi;
import com.ijpay.paypal.PayPalApiConfig;
import com.ijpay.paypal.PayPalApiConfigKit;
import com.ijpay.paypal.accesstoken.AccessToken;
import com.ijpay.paypal.accesstoken.AccessTokenKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payPal")
public class PayPalController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    PayPalBean payPalBean;

    private final static String RETURN_URL = "/payPal/return";
    private final static String CANCEL_URL = "/payPal/cancel";


    @RequestMapping("")
    @ResponseBody
    public String index() {
        log.info("欢迎使用 IJPay 中的 PayPal 支付 -By Javen  <br/><br>  交流群：723992875");
        log.info(payPalBean.toString());
        return ("欢迎使用 IJPay 中的 PayPal 支付 -By Javen  <br/><br>  交流群：723992875");
    }

    @RequestMapping("test")
    @ResponseBody
    public PayPalBean test() {
        return payPalBean;
    }

    public PayPalApiConfig getConfig() {
        PayPalApiConfig config = new PayPalApiConfig();
        config.setClientId(payPalBean.getClientId());
        config.setSecret(payPalBean.getSecret());
        config.setSandBox(payPalBean.getSandBox());
        config.setDomain(payPalBean.getDomain());
        PayPalApiConfigKit.setThreadLocalApiConfig(config);
        return config;
    }

    @RequestMapping(value = "/getAccessToken")
    @ResponseBody
    public AccessToken getAccessToken() {
        try {
            getConfig();
            return AccessTokenKit.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/createOrder")
    @ResponseBody
    public void createOrder(HttpServletResponse response) {
        try {
            PayPalApiConfig config = getConfig();

            //参数请求参数文档 https://developer.paypal.com/docs/api/orders/v2/#orders_create
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("intent", "CAPTURE");

            ArrayList<Map<String, Object>> list = new ArrayList<>();

            Map<String, Object> amount = new HashMap<>();
            amount.put("currency_code", "USD");
            amount.put("value", "100.00");

            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("amount", amount);

            list.add(itemMap);

            dataMap.put("purchase_units", list);

            Map<String, String> applicationContext = new HashMap<>();
            applicationContext.put("cancel_url", config.getDomain().concat(CANCEL_URL));
            applicationContext.put("return_url", config.getDomain().concat(RETURN_URL));

            dataMap.put("application_context", applicationContext);

            String data = JSONUtil.toJsonStr(dataMap);
            log.info(data);
            IJPayHttpResponse resData = PayPalApi.createOrder(config, data);
            log.info(resData.toString());
            if (resData.getStatus() == 201) {
                String resultStr = resData.getBody();

                JSONObject jsonObject = JSONUtil.parseObj(resultStr);
                JSONArray links = jsonObject.getJSONArray("links");
                for (int i = 0; i < links.size(); i++) {
                    JSONObject item = links.getJSONObject(i);
                    String rel = item.getStr("rel");
                    String href = item.getStr("href");
                    if ("approve".equalsIgnoreCase(rel)) {
                        response.sendRedirect(href);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/updateOrder")
    @ResponseBody
    public String updateOrder(@RequestParam("id") String id) {
        try {
            PayPalApiConfig config = getConfig();
            // https://developer.paypal.com/docs/api/orders/v2/#orders_patch

            ArrayList<Map<String, Object>> updateData = new ArrayList<>();

            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("op", "replace");
            itemMap.put("path", "/purchase_units/@reference_id=='default'/amount");

            Map<String, Object> amount = new HashMap<>();
            amount.put("currency_code", "USD");
            amount.put("value", "199.00");

            itemMap.put("value", amount);

            updateData.add(itemMap);

            String data = JSONUtil.toJsonStr(updateData);
            log.info(data);
            IJPayHttpResponse resData = PayPalApi.updateOrder(config, id, data);
            log.info(resData.toString());
            if (resData.getStatus() == 204) {
                return "success";
            }
            return "接口请求错误码为："+resData.getStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/queryOrder")
    @ResponseBody
    public String queryOrder(@RequestParam("id") String id) {
        try {
            PayPalApiConfig config = getConfig();
            IJPayHttpResponse response = PayPalApi.queryOrder(config, id);
            log.info(response.toString());
            if (response.getStatus() == 200) {
                return response.getBody();
            } else {
                return "接口请求错误码为：" + response.getStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/captureOrder")
    @ResponseBody
    public String captureOrder(@RequestParam("id") String id) {
        try {
            PayPalApiConfig config = getConfig();
            IJPayHttpResponse response = PayPalApi.captureOrder(config, id, "");
            log.info(response.toString());
            if (response.getStatus() == 200 || response.getStatus() == 201) {
                return response.getBody();
            } else {
                return "接口请求错误码为：" + response.getStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/captureQuery")
    @ResponseBody
    public String captureQuery(@RequestParam("captureId") String captureId) {
        try {
            PayPalApiConfig config = getConfig();
            IJPayHttpResponse response = PayPalApi.captureQuery(config, captureId);
            log.info(response.toString());
            if (response.getStatus() == 200) {
                return response.getBody();
            } else {
                return "接口请求错误码为：" + response.getStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/refund")
    @ResponseBody
    public String refund(@RequestParam("id") String id) {
        try {
            PayPalApiConfig config = getConfig();
            System.out.println("id>" + id);

            Map<String, Object> map = new HashMap<>();
            map.put("invoice_id", PayKit.generateStr());
            map.put("note_to_payer", "test product");

            Map<String, String> amount = new HashMap<>();
            amount.put("value", "1.00");
            amount.put("currency_code", "USD");

            map.put("amount", amount);

            String data = JSONUtil.toJsonStr(map);
            log.info("refund data：" + data);
            IJPayHttpResponse response = PayPalApi.refund(config, id, data);
            log.info(response.toString());
            if (response.getStatus() == 201) {
                return response.getBody();
            } else {
                return "接口请求错误码为：" + response.getStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/refundQuery")
    @ResponseBody
    public String refundQuery(@RequestParam("id") String id) {
        try {
            PayPalApiConfig config = getConfig();
            IJPayHttpResponse response = PayPalApi.refundQuery(config, id);
            log.info(response.toString());
            if (response.getStatus() == 200) {
                return response.getBody();
            } else {
                return "接口请求错误码为：" + response.getStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/return")
    @ResponseBody
    public String returnUrl(HttpServletRequest request) {
        try {
            String token = request.getParameter("token");
            String payerId = request.getParameter("PayerID");
            log.info("token:" + token);
            log.info("payerId:" + payerId);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/cancel")
    @ResponseBody
    public String cancelUrl(HttpServletRequest request, HttpServletResponse response) {
        String readData = HttpKit.readData(request);
        System.out.println(readData);
        return readData;
    }
}
