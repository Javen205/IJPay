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
 * <p>微信支付 v2 版本-小微商户相关接口枚举</p>
 *
 * @author Javen
 */
public enum MicroMchApiEnum implements WxApiEnum {

    /**
     * 申请入驻
     */
    SUBMIT("/applyment/micro/submit", "申请入驻"),

    /**
     * 查询申请状态
     */
    GET_SUBMIT_STATE("/applyment/micro/getstate", "查询申请状态"),

    /**
     * 查询提现状态
     */
    QUERY_AUTO_WITH_DRAW_BY_DATE("/fund/queryautowithdrawbydate", "查询提现状态"),

    /**
     * 修改结算银行卡
     */
    MODIFY_ARCHIVES("/applyment/micro/modifyarchives", "修改结算银行卡"),

    /**
     * 修改联系信息
     */
    MODIFY_CONTACT_INFO("/applyment/micro/modifycontactinfo", "修改联系信息"),

    /**
     * 关注配置
     */
    ADD_RECOMMEND_CONF("/secapi/mkt/addrecommendconf", "关注配置"),

    /**
     * 添加开发配置
     */
    ADD_SUB_DEV_CONFIG("/secapi/mch/addsubdevconfig", "开发配置"),

    /**
     * 开发配置查询
     */
    QUERY_SUB_DEV_CONFIG("/secapi/mch/querysubdevconfig", "开发配置查询"),

    /**
     * 提交升级申请
     */
    SUBMIT_UPGRADE("/applyment/micro/submitupgrade", "提交升级申请"),

    /**
     * 查询升级申请单状态
     */
    GET_UPGRADE_STATE("/applyment/micro/getupgradestate", "查询升级申请单状态"),

    /**
     * 服务商帮小微商户重新发起自动提现
     */
    RE_AUTO_WITH_DRAW_BY_DATE("/fund/reautowithdrawbydate", "服务商帮小微商户重新发起自动提现"),
    ;

    /**
     * 接口URL
     */
    private final String url;

    /**
     * 接口描述
     */
    private final String desc;

    MicroMchApiEnum(String url, String desc) {
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
