package com.ijpay.wxpay.enums.v2;

import com.ijpay.wxpay.enums.WxApiEnum;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 * <p>IJPay 自由交流群: 864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">Node.js 版</a></p>
 *
 * <p>微信支付 v2 版本-支付押金相关接口枚举</p>
 *
 * @author Javen
 */
public enum DepositApiEnum implements WxApiEnum {

    /**
     * 支付押金（JSAPI、APP 下单）
     */
    PAY("/deposit/unifiedorder", "支付押金（JSAPI、APP 下单）"),

    /**
     * 支付押金（人脸支付）
     */
    FACE_PAY("/deposit/facepay", "支付押金（人脸支付）"),

    /**
     * 支付押金（付款码支付）
     */
    MICRO_PAY("/deposit/micropay", "支付押金（付款码支付）"),

    /**
     * 查询订单
     */
    ORDER_QUERY("/deposit/orderquery", "查询订单"),

    /**
     * 撤销订单
     */
    REVERSE("/deposit/reverse", "撤销订单"),

    /**
     * 消费押金
     */
    CONSUME("/deposit/consume", "消费押金"),

    /**
     * 申请退款
     */
    REFUND("/deposit/refund", "申请退款"),

    /**
     * 查询退款
     */
    REFUND_QUERY("deposit/refundquery", "查询退款"),
    ;

    /**
     * 接口URL
     */
    private final String url;

    /**
     * 接口描述
     */
    private final String desc;

    DepositApiEnum(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    /**
     * 获取枚举URL
     *
     * @return 枚举编码
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * 获取详细的描述信息
     *
     * @return 描述信息
     */
    @Override
    public String getDesc() {
        return desc;
    }
}
