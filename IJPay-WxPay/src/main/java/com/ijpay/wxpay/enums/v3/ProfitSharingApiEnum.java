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
 * <p>微信v3接口-分账接口枚举</p>
 *
 * @author Javen
 */
public enum ProfitSharingApiEnum implements WxApiEnum {

    /**
     * 请求分账
     */
    PROFIT_SHARING_ORDERS("/v3/profitsharing/orders", "请求分账"),

    /**
     * 查询分账结果
     */
    PROFIT_SHARING_ORDERS_QUERY("/v3/profitsharing/orders/%s", "查询分账结果"),

    /**
     * 请求分账回退
     */
    PROFIT_SHARING_RETURN_ORDERS("/v3/profitsharing/return-orders", "请求分账回退"),

    /**
     * 查询分账回退结果
     */
    PROFIT_SHARING_RETURN_ORDERS_QUERY("/v3/profitsharing/return-orders/%s", "查询分账回退结果"),

    /**
     * 解冻剩余资金
     */
    PROFIT_SHARING_UNFREEZE("/v3/profitsharing/orders/unfreeze", "解冻剩余资金"),

    /**
     * 查询剩余待分金额
     */
    PROFIT_SHARING_UNFREEZE_QUERY("/v3/profitsharing/transactions/%s/amounts", "查询剩余待分金额"),

    /**
     * 查询最大分账比例
     */
    PROFIT_SHARING_MERCHANT_CONFIGS("/v3/profitsharing/merchant-configs/%s", "查询最大分账比例"),

    /**
     * 添加分账接收方
     */
    PROFIT_SHARING_RECEIVERS_ADD("/v3/profitsharing/receivers/add", "添加分账接收方"),

    /**
     * 删除分账接收方
     */
    PROFIT_SHARING_RECEIVERS_DELETE("/v3/profitsharing/receivers/delete", "删除分账接收方"),

    /**
     * 申请分账账单
     */
    PROFIT_SHARING_BILLS("/v3/profitsharing/bills", "申请分账账单"),

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
