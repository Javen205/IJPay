package com.ijpay.wxpay.enums.v3;

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
 * <p>微信支付 v3 接口-微信支付分接口枚举</p>
 *
 * @author Javen
 */
public enum PayScoreApiEnum implements WxApiEnum {

    /**
     * 创建/查询支付分订单
     */
    PAY_SCORE_SERVICE_ORDER("/v3/payscore/serviceorder", "创建/查询支付分订单"),

    /**
     * 取消支付分订单
     */
    PAY_SCORE_SERVICE_ORDER_CANCEL("/v3/payscore/serviceorder/%s/cancel", "取消支付分订单"),

    /**
     * 修改订单金额
     */
    PAY_SCORE_SERVICE_ORDER_MODIFY("/v3/payscore/serviceorder/%s/modify", "修改订单金额"),

    /**
     * 完结支付分订单
     */
    PAY_SCORE_SERVICE_ORDER_COMPLETE("/v3/payscore/serviceorder/%s/complete", "完结支付分订单"),

    /**
     * 同步服务订单信息
     */
    PAY_SCORE_SERVICE_ORDER_SYNC("/v3/payscore/serviceorder/%s/sync", "同步服务订单信息"),

    /**
     * 商户预授权
     */
    PAY_SCORE_PERMISSIONS("/v3/payscore/permissions", "商户预授权"),

    /**
     * 查询与用户授权记录（授权协议号）
     */
    PAY_SCORE_PERMISSIONS_AUTHORIZATION_CODE("/v3/payscore/permissions/authorization-code/%s", "查询与用户授权记录（授权协议号）"),

    /**
     * 解除用户授权关系（授权协议号）
     */
    PAY_SCORE_PERMISSIONS_AUTHORIZATION_CODE_TERMINATE("/v3/payscore/permissions/authorization-code/%s/terminate", "解除用户授权关系（授权协议号）"),

    /**
     * 查询与用户授权记录（openid）
     */
    PAY_SCORE_PERMISSIONS_OPENID("/v3/payscore/permissions/openid/%s", "查询与用户授权记录（openid）"),

    /**
     * 解除用户授权关系（openid）
     */
    PAY_SCORE_PERMISSIONS_OPENID_TERMINATE("/v3/payscore/permissions/openid/%s/terminate", "解除用户授权关系（openid）"),
    ;

    /**
     * 接口URL
     */
    private final String url;

    /**
     * 接口描述
     */
    private final String desc;

    PayScoreApiEnum(String url, String desc) {
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

    @Override
    public String toString() {
        return url;
    }
}
