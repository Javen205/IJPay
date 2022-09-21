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
 * <p>微信支付 v3 版本接口-特约商户进件接口枚举</p>
 *
 * @author Javen
 */
public enum Apply4SubApiEnum implements WxApiEnum {

    /**
     * 提交申请单
     */
    APPLY_4_SUB("/v3/applyment4sub/applyment/", "提交申请单"),

    /**
     * 查询申请单状态
     */
    GET_APPLY_STATE("/v3/applyment4sub/applyment/business_code/%s", "查询申请单状态"),

    /**
     * 通过申请单号查询申请状态
     */
    GET_APPLY_STATE_BY_ID("/v3/applyment4sub/applyment/applyment_id/%s", "通过申请单号查询申请状态"),

    /**
     * 修改结算账号
     */
    MODIFY_SETTLEMENT("/v3/apply4sub/sub_merchants/%s/modify-settlement", "修改结算账号"),

    /**
     * 查询结算账户
     */
    GET_SETTLEMENT("/v3/apply4sub/sub_merchants/%s/settlement", "查询结算账户"),
    ;

    /**
     * 接口URL
     */
    private final String url;

    /**
     * 接口描述
     */
    private final String desc;

    Apply4SubApiEnum(String url, String desc) {
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
