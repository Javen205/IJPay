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
 * <p>微信支付 v3 接口-优惠券接口枚举</p>
 *
 * @author Javen
 */
public enum CouponApiEnum implements WxApiEnum {
    /**
     * 创建代金券批次
     */
    CREATE_COUPON_STOCKS("/v3/marketing/favor/coupon-stocks", "创建代金券批次"),

    /**
     * 激活代金券批次
     */
    START_COUPON_STOCKS("/v3/marketing/favor/stocks/%s/start", "激活代金券批次"),

    /**
     * 发放代金券
     */
    SEND_COUPON("/v3/marketing/favor/users/%s/coupons", "发放代金券"),

    /**
     * 暂停代金券批次
     */
    PAUSE_COUPON_STOCKS("/v3/marketing/favor/stocks/%s/pause", "暂停代金券批次"),

    /**
     * 重启代金券批次
     */
    RESTART_COUPON_STOCKS("/v3/marketing/favor/stocks/%s/restart", "重启代金券批次"),
    /**
     * 条件查询批次列表
     */
    QUERY_COUPON_STOCKS("/v3/marketing/favor/stocks", "条件查询批次列表"),
    /**
     * 查询批次详情
     */
    QUERY_COUPON_STOCKS_INFO("/v3/marketing/favor/stocks/%s", "查询批次详情"),
    /**
     * 查询代金券详情
     */
    QUERY_COUPON_INFO("/v3/marketing/favor/users/%s/coupons/%s", "查询代金券详情"),

    /**
     * 查询代金券可用商户
     */
    QUERY_COUPON_MERCHANTS("/v3/marketing/favor/stocks/%s/merchants", "查询代金券可用商户"),

    /**
     * 查询代金券可用单品
     */
    QUERY_COUPON_ITEMS("/v3/marketing/favor/stocks/%s/items", "查询代金券可用单品"),

    /**
     * 根据商户号查用户的券
     */
    QUERY_USER_COUPON("/v3/marketing/favor/users/%s/coupons", "根据商户号查用户的券"),

    /**
     * 下载批次核销明细
     */
    DOWNLOAD_COUPON_STOCKS_USER_FLOW("/v3/marketing/favor/stocks/%s/use-flow", "下载批次核销明细"),

    /**
     * 下载批次退款明细
     */
    DOWNLOAD_COUPON_STOCKS_REFUND_FLOW("/v3/marketing/favor/stocks/%s/refund-flow", "下载批次退款明细"),

    /**
     * 设置消息通知地址
     */
    SETTING_COUPON_CALLBACKS("/v3/marketing/favor/callbacks", "设置消息通知地址"),

    /**
     * 创建商家券
     */
    CREATE_BUSINESS_COUPON("/v3/marketing/busifavor/stocks", "创建商家券"),

    /**
     * 查询商家券批次详情
     */
    QUERY_BUSINESS_COUPON_STOCKS_INFO("/v3/marketing/busifavor/stocks/%s", "查询商家券批次详情"),

    /**
     * 核销用户券
     */
    USE_BUSINESS_COUPON("/v3/marketing/busifavor/coupons/use", "核销用户券"),

    /**
     * 根据过滤条件查询用户券
     */
    QUERY_BUSINESS_USER_COUPON("/v3/marketing/busifavor/users/%s/coupons", "根据过滤条件查询用户券"),

    /**
     * 查询用户单张券详情
     */
    QUERY_BUSINESS_USER_COUPON_INFO("/v3/marketing/busifavor/users/%s/coupons/%s/appids/%s", "查询用户单张券详情"),

    /**
     * 上传预存code
     */
    BUSINESS_COUPON_UPLOAD_CODE("/v3/marketing/busifavor/stocks/%s/couponcodes", "上传预存code"),

    /**
     * 设置/查询商家券事件通知地址
     */
    BUSINESS_COUPON_CALLBACKS("/v3/marketing/busifavor/callbacks", "设置/查询商家券事件通知地址"),

    /**
     * 关联订单信息
     */
    BUSINESS_COUPON_ASSOCIATE("/v3/marketing/busifavor/coupons/associate", "关联订单信息"),

    /**
     * 取消关联订单信息
     */
    BUSINESS_COUPON_DISASSOCIATE("/v3/marketing/busifavor/coupons/disassociate", "取消关联订单信息"),

    /**
     * 修改批次预算
     */
    MODIFY_BUSINESS_COUPON_STOCKS_BUDGET("/v3/marketing/busifavor/stocks/%s/budget", "修改批次预算"),

    /**
     * 修改商家券基本信息
     */
    MODIFY_BUSINESS_COUPON_INFO("/v3/marketing/busifavor/stocks/%s", "修改商家券基本信息"),

    /**
     * 申请退券
     */
    APPLY_REFUND_COUPONS("/v3/marketing/busifavor/coupons/return", "申请退券"),

    /**
     * 使券失效
     */
    COUPON_DEACTIVATE("/v3/marketing/busifavor/coupons/deactivate", "使券失效"),

    /**
     * 营销补差付款
     */
    COUPON_SUBSIDY_PAY("/v3/marketing/busifavor/subsidy/pay-receipts", "营销补差付款"),

    /**
     * 查询营销补差付款单详情
     */
    COUPON_SUBSIDY_PAY_INFO("/v3/marketing/busifavor/subsidy/pay-receipts/%s", "查询营销补差付款单详情"),


    /**
     * 委托营销-建立合作关系
     */
    PARTNERSHIPS_BUILD("/v3/marketing/partnerships/build", "建立合作关系"),

    /**
     * 委托营销-终止合作关系
     */
    PARTNERSHIPS_TERMINATE("/v3/marketing/partnerships/terminate", "终止合作关系"),

    /**
     * 发放消费卡
     */
    SEND_BUSINESS_COUPON("/v3/marketing/busifavor/coupons/%s/send", "发放消费卡"),
    ;

    /**
     * 接口URL
     */
    private final String url;

    /**
     * 接口描述
     */
    private final String desc;

    CouponApiEnum(String url, String desc) {
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
