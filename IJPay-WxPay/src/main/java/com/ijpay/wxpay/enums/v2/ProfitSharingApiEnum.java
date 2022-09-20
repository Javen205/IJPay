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
 * <p>微信支付 v2 版本-分账接口枚举</p>
 *
 * @author Javen
 */
public enum ProfitSharingApiEnum implements WxApiEnum {

    /**
     * 请求单次分账
     */
    PROFIT_SHARING("/secapi/pay/profitsharing", "请求单次分账"),

    /**
     * 请求多次分账
     */
    MULTI_PROFIT_SHARING("/secapi/pay/multiprofitsharing", "请求多次分账"),

    /**
     * 查询分账结果
     */
    PROFIT_SHARING_QUERY("/pay/profitsharingquery", "查询分账结果"),

    /**
     * 添加分账接收方
     */
    PROFIT_SHARING_ADD_RECEIVER("/pay/profitsharingaddreceiver", "添加分账接收方"),

    /**
     * 删除分账接收方
     */
    PROFIT_SHARING_REMOVE_RECEIVER("/pay/profitsharingremovereceiver", "删除分账接收方"),

    /**
     * 完结分账
     */
    PROFIT_SHARING_FINISH("/secapi/pay/profitsharingfinish", "完结分账"),

    /**
     * 查询订单待分账金额
     */
    PROFIT_SHARING_ORDER_AMOUNT_QUERY("/pay/profitsharingorderamountquery", "查询订单待分账金额"),

    /**
     * 分账回退
     */
    PROFIT_SHARING_RETURN("/secapi/pay/profitsharingreturn", "分账回退"),

    /**
     * 分账回退结果查询
     */
    PROFIT_SHARING_RETURN_QUERY("/pay/profitsharingreturnquery", "分账回退结果查询"),
    ;

    /**
     * 接口URL
     */
    private final String url;

    /**
     * 接口描述
     */
    private final String desc;

    ProfitSharingApiEnum(String url, String desc) {
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
