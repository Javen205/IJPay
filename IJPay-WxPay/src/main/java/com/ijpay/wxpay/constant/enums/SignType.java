package com.ijpay.wxpay.constant.enums;

/**
 * 签名方式
 *
 * @author Javen
 */
public enum SignType {
    HMACSHA256("HMAC-SHA256"),

    MD5("MD5");

    SignType(String type) {
        this.type = type;
    }

    private final String type;

    public String getType() {
        return type;
    }
}
