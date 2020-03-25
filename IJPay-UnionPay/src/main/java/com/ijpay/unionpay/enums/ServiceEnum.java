package com.ijpay.unionpay.enums;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>云闪付接口类型枚举</p>
 *
 * @author Javen
 */
public enum ServiceEnum {
    /**
     * 刷卡支付
     */
    MICRO_PAY("unified.trade.micropay"),
    /**
     * 扫码支付
     */
    NATIVE("unified.trade.native"),
    /**
     * 微信公众号、小程序支付统一下单
     */
    WEI_XIN_JS_PAY("pay.weixin.jspay"),
    /**
     * 微信 App 支付
     */
    WEI_XIN_APP_PAY("pay.weixin.raw.app"),
    /**
     * 查询订单
     */
    QUERY("unified.trade.query"),
    /**
     * 申请退款
     */
    REFUND("unified.trade.refund"),
    /**
     * 退款查询
     */
    REFUND_QUERY("unified.trade.refundquery"),
    /**
     * 关闭订单
     */
    CLOSE("unified.trade.close"),
    /**
     * 撤销订单
     */
    MICRO_PAY_REVERSE("unified.micropay.reverse"),
    /**
     * 授权码查询 openid
     */
    AUTH_CODE_TO_OPENID("unified.tools.authcodetoopenid"),
    /**
     * 银联 JS 支付获取 userId
     */
    UNION_PAY_USER_ID("pay.unionpay.userid"),
    /**
     * 银联 JS 支付下单
     */
    UNION_JS_PAY("pay.unionpay.jspay"),
    /**
     * 支付宝服务窗口支付
     */
    ALI_PAY_JS_PAY("pay.alipay.jspay"),
    /**
     * 下载单个商户时的对账单
     */
    BILL_MERCHANT("pay.bill.merchant"),
    /**
     * 下载连锁商户下所有门店的对账单
     */
    BILL_BIG_MERCHANT("pay.bill.bigMerchant"),
    /**
     * 下载某内部机构/外包服务机构下所有商户的对账单
     */
    BILL_AGENT("pay.bill.agent");

    /**
     * 接口类型
     */
    private final String service;

    ServiceEnum(String domain) {
        this.service = domain;
    }

    @Override
    public String toString() {
        return service;
    }
}
