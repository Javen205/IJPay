package com.ijpay.wxpay.model;

import com.ijpay.core.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>企业付款到零钱 Model</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
public class TransferModel extends BaseModel {
    private String mch_appid;
    private String mchid;
    private String device_info;
    private String nonce_str;
    private String sign;
    private String partner_trade_no;
    private String openid;
    private String check_name;
    private String re_user_name;
    private String amount;
    private String desc;
    private String spbill_create_ip;
}
