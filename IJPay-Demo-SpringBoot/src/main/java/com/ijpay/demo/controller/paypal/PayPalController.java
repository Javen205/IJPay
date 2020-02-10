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

import com.ijpay.core.kit.HttpKit;
import com.ijpay.demo.entity.PayPalBean;
import com.ijpay.paypal.PayPalApiConfig;
import com.ijpay.paypal.PayPalApiConfigKit;
import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping(value = "/toPay")
    @ResponseBody
    public void toPay(HttpServletResponse response) {
        PayPalApiConfig config = PayPalApiConfig.builder()
                .setClientId(payPalBean.getClientId())
                .setClientSecret(payPalBean.getClientSecret())
                .setMode(payPalBean.getMode())
                .build();

        // Set payer details
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        // Set redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(payPalBean.getDomain().concat(CANCEL_URL));
        redirectUrls.setReturnUrl(payPalBean.getDomain().concat(RETURN_URL));

        // Set payment details
        Details details = new Details();
        details.setShipping("1");
        details.setSubtotal("5");
        details.setTax("1");

        // Payment amount
        Amount amount = new Amount();
        amount.setCurrency("USD");
        // Total must be equal to sum of shipping, tax and subtotal.
        amount.setTotal("7");
        amount.setDetails(details);

        // Transaction information
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("IJPay 让支付触手可及 PayPal 支付");

        // Add transaction to a list
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        // Add payment details
        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setRedirectUrls(redirectUrls);
        payment.setTransactions(transactions);

        try {
            Payment createdPayment = payment.create(config.getApiContext());

            for (Links link : createdPayment.getLinks()) {
                System.out.println(link);
                if (link.getRel().equalsIgnoreCase("approval_url")) {
                    // Redirect the customer to link.getHref()
                    response.sendRedirect(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/return")
    @ResponseBody
    public void returnUrl(HttpServletRequest request, HttpServletResponse response) {
        String paymentId = request.getParameter("paymentId");
        String payerId = request.getParameter("PayerID");
        log.info("paymentId:" + paymentId);
        log.info("payerId:" + payerId);
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        try {
            Payment createdPayment = payment.execute(PayPalApiConfigKit.getPayPalApiConfig().getApiContext(),
                    paymentExecution);
            System.out.println(createdPayment);
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
    }

    @RequestMapping(value = "/cancel")
    @ResponseBody
    public String cancelUrl(HttpServletRequest request, HttpServletResponse response) {
        String readData = HttpKit.readData(request);
        System.out.println(readData);
        return readData;
    }
}
