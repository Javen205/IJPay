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
 * <p>微信v3接口-微信支付分停车服务</p>
 *
 * @author Javen
 */
public enum ParkingApiEnum implements WxApiEnum {
    
    /**
     * 查询车牌服务开通信息
     */
    VEHICLE_PARKING_SERVICES_FIND("/v3/vehicle/parking/services/find", "查询车牌服务开通信息"),

    /**
     * 创建停车入场
     */
    VEHICLE_PARKING("/v3/vehicle/parking/parkings", "创建停车入场"),

    /**
     * 扣费受理
     */
    VEHICLE_TRANSACTION_PARKING("/v3/vehicle/transactions/parking", "扣费受理"),

    /**
     * 查询订单
     */
    VEHICLE_TRANSACTION_QUERY_BY_OUT_TRADE_NO("/v3/vehicle/transactions/out-trade-no/%s", "查询订单"),

    ;

    /**
     * 接口URL
     */
    private final String url;

    /**
     * 接口描述
     */
    private final String desc;

    ParkingApiEnum(String url, String desc) {
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
