package com.ijpay.wxpay.constant.enums;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p>支付方式</p>
 *
 * @author Javen
 */
public enum TradeType {
    JSAPI("JSAPI", "微信公众号支付或者小程序支付"),
    NATIVE("NATIVE", "微信扫码支付"),
    APP("APP", "微信APP支付"),
    MICROPAY("MICROPAY", "付款码支付"),
    MWEB("MWEB", "H5支付");

    /**
     * 交易类型枚举<br/>
     * JSAPI 公众号支付、小程序支付<br/>
     * NATIVE 原生扫码支付<br/>
     * APP APP支付<br/>
     * MWEB WAP支付<br/>
     * MICROPAY 刷卡支付<br/>
     */

    TradeType(String tradeType, String description) {
        this.tradeType = tradeType;
        this.description = description;
    }

    /**
     * 交易类型
     */
    private final String tradeType;
    /**
     * 描述
     */
    private final String description;

    public String getTradeType() {
        return tradeType;
    }

    public String getDescription() {
        return description;
    }
}
