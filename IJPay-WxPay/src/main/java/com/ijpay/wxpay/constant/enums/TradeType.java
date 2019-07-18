package com.ijpay.wxpay.constant.enums;

public enum TradeType {
    JSAPI("JSAPI","微信公众号支付或者小程序支付）"),
    NATIVE("NATIVE","微信扫码支付"),
    APP("APP","微信APP支付"),
    MICROPAY("MICROPAY","付款码支付"),
    MWEB("MWEB","H5支付");

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
