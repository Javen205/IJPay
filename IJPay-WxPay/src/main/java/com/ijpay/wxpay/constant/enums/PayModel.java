package com.ijpay.wxpay.constant.enums;

public enum PayModel {
    BUSINESS_MODEL("BUSINESS_MODEL","商户模式"),
    SERVICE_MODE("SERVICE_MODE","服务商模式");

    PayModel(String payModel, String description) {
        this.payModel = payModel;
        this.description = description;
    }

    /**
     * 商户模式
     */
    private final String payModel;

    /**
     * 描述
     */
    private final String description;

    public String getPayModel() {
        return payModel;
    }

    public String getDescription() {
        return description;
    }
}
