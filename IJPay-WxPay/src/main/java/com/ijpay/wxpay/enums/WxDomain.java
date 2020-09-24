package com.ijpay.wxpay.enums;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>微信支付可用域名枚举</p>
 *
 * @author Javen
 */
public enum WxDomain {
    /**
     * 中国国内
     */
    CHINA("https://api.mch.weixin.qq.com"),
    /**
     * 中国国内(备用域名)
     */
    CHINA2("https://api2.mch.weixin.qq.com"),
    /**
     * 东南亚
     */
    HK("https://apihk.mch.weixin.qq.com"),
    /**
     * 其它
     */
    US("https://apius.mch.weixin.qq.com"),
    /**
     * 获取公钥
     */
    FRAUD("https://fraud.mch.weixin.qq.com"),
    /**
     * 活动
     */
    ACTION("https://action.weixin.qq.com"),
    /**
     * PAYAPP
     */
    PAYAPP("https://payapp.weixin.qq.com");


    /**
     * 域名
     */
    private final String domain;

    WxDomain(String domain) {
        this.domain = domain;
    }

    public String getType() {
        return domain;
    }

    @Override
    public String toString() {
        return domain;
    }
}
