package com.ijpay.xpay.model;

import com.ijpay.core.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>微信官方个人支付渠道，有稳定的异步通知，加企鹅(572839485)了解更多</p>
 *
 * <p>XPay Model</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
public class XPayModel extends BaseModel {
    private String out_trade_no;
    private String total_fee;
    private String mch_id;
    private String body;
    private String type;
    private String openId;
    private String face_code;
    private String title;
    private String auth_code;
    private String attach;
    private String receipt;
    private String notify_url;
    private String return_url;
    private String config_no;
    private String auto;
    private String auto_node;
    private String sign;
    private String money;
    private String refund_no;
    private String refund_desc;
    private String status;
    private String order_no;
    private String pay_no;
    private String start_time;
    private String end_time;
    private String date;
    private String app_id;
    private String url;
    private String params;
    private String code;
}
