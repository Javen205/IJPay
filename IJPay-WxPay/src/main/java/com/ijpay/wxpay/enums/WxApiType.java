package com.ijpay.wxpay.enums;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p>微信支付接口枚举</p>
 *
 * @author Javen
 */
public enum WxApiType {
    /**
     * 沙箱环境
     */
    SAND_BOX_NEW("/sandboxnew"),
    /**
     * 获取沙箱环境验签秘钥
     */
    GET_SIGN_KEY("/sandboxnew/pay/getsignkey"),
    /**
     * 统一下单
     */
    UNIFIED_ORDER("/pay/unifiedorder"),
    /**
     * 提交付款码支付
     */
    MICRO_PAY("/pay/micropay"),
    /**
     * 查询订单
     */
    ORDER_QUERY("/pay/orderquery"),
    /**
     * 关闭订单
     */
    CLOSE_ORDER("/pay/closeorder"),
    /**
     * 撤销订单
     */
    REVERSE("/secapi/pay/reverse"),
    /**
     * 申请退款
     */
    REFUND("/secapi/pay/refund"),
    /**
     * 查询退款
     */
    REFUND_QUERY("/pay/refundquery"),
    /**
     * 下载对账单
     */
    DOWNLOAD_BILL("/pay/downloadbill"),
    /**
     * 下载资金对账单
     */
    DOWNLOAD_FUND_FLOW("/pay/downloadfundflow"),
    /**
     * 交易保障
     */
    REPORT("/payitil/report"),
    /**
     * 转换短链接
     */
    SHORT_URL("/tools/shorturl"),
    /**
     * 授权码查询 openId
     */
    AUTH_CODE_TO_OPENID("/tools/authcodetoopenid"),
    /**
     * 拉取订单评价数据
     */
    BATCH_QUERY_COMMENT("/billcommentsp/batchquerycomment"),
    /**
     * 企业付款
     */
    TRANSFER("/mmpaymkttransfers/promotion/transfers"),
    /**
     * 查询企业付款
     */
    GET_TRANSFER_INFO("/mmpaymkttransfers/gettransferinfo"),
    /**
     * 企业付款到银行卡
     */
    TRANSFER_BANK("/mmpaysptrans/pay_bank"),
    /**
     * 查询企业付款到银行卡
     */
    GET_TRANSFER_BANK_INFO("/mmpaysptrans/query_bank"),
    /**
     * 获取 RSA 加密公钥
     */
    GET_PUBLIC_KEY("/risk/getpublickey"),
    /**
     * 发放红包
     */
    SEND_RED_PACK("/mmpaymkttransfers/sendredpack"),
    /**
     * 发放裂变红包
     */
    SEND_GROUP_RED_PACK("/mmpaymkttransfers/sendgroupredpack"),
    /**
     * 查询红包记录
     */
    GET_HB_INFO("/mmpaymkttransfers/gethbinfo"),
    /**
     * 小程序发红包
     */
    SEND_MINI_PROGRAM_HB("/mmpaymkttransfers/sendminiprogramhb"),
    /**
     * 发放代金券
     */
    SEND_COUPON("/mmpaymkttransfers/send_coupon"),
    /**
     * 查询代金券批次
     */
    QUERY_COUPON_STOCK("/mmpaymkttransfers/query_coupon_stock"),
    /**
     * 查询代金券信息
     */
    QUERY_COUPONS_INFO("/mmpaymkttransfers/querycouponsinfo"),
    /**
     * 请求单次分账
     */
    PROFIT_SHARING("/secapi/pay/profitsharing"),
    /**
     * 请求多次分账
     */
    MULTI_PROFIT_SHARING("/secapi/pay/multiprofitsharing"),
    /**
     * 查询分账结果
     */
    PROFIT_SHARING_QUERY("/pay/profitsharingquery"),
    /**
     * 添加分账接收方
     */
    PROFITS_HARING_ADD_RECEIVER("/pay/profitsharingaddreceiver"),
    /**
     * 删除分账接收方
     */
    PROFIT_SHARING_REMOVE_RECEIVER("/pay/profitsharingremovereceiver"),
    /**
     * 完结分账
     */
    PROFIT_SHARING_FINISH("/secapi/pay/profitsharingfinish"),
    /**
     * 分账回退
     */
    PROFIT_SHARING_RETURN("/secapi/pay/profitsharingreturn"),
    /**
     * 分账回退结果查询
     */
    PROFIT_SHARING_RETURN_QUERY("/pay/profitsharingreturnquery"),
    /**
     * 支付押金（人脸支付）
     */
    DEPOSIT_FACE_PAY("/deposit/facepay"),
    /**
     * 支付押金（付款码支付）
     */
    DEPOSIT_MICRO_PAY("/deposit/micropay"),
    /**
     * 查询订单（押金）
     */
    DEPOSIT_ORDER_QUERY("/deposit/orderquery"),
    /**
     * 撤销订单（押金）
     */
    DEPOSIT_REVERSE("/deposit/reverse"),
    /**
     * 消费押金
     */
    DEPOSIT_CONSUME("/deposit/consume"),
    /**
     * 申请退款（押金）
     */
    DEPOSIT_REFUND("/deposit/refund"),
    /**
     * 查询退款（押金）
     */
    DEPOSIT_REFUND_QUERY("deposit/refundquery"),
    /**
     * 公众号纯签约
     */
    ENTRUST_WEB("/papay/entrustweb"),
    /**
     * 公众号纯签约（服务商模式）
     */
    PARTNER_ENTRUST_WEB("/papay/partner/entrustweb"),
    /**
     * APP纯签约
     */
    PRE_ENTRUST_WEB("/papay/preentrustweb"),
    /**
     * APP纯签约（服务商模式）
     */
    PARTNER_PRE_ENTRUST_WEB("/papay/partner/preentrustweb"),
    /**
     * H5纯签约
     */
    H5_ENTRUST_WEB("/papay/h5entrustweb"),
    /**
     * H5纯签约（服务商模式）
     */
    PARTNER_H5_ENTRUST_WEB("/papay/partner/h5entrustweb"),
    /**
     * 支付中签约
     */
    PAY_CONTRACT_ORDER("/pay/contractorder"),
    /**
     * 查询签约关系
     */
    QUERY_ENTRUST_CONTRACT("/papay/querycontract"),
    /**
     * 查询签约关系（服务商模式）
     */
    PARTNER_QUERY_ENTRUST_CONTRACT("/papay/partner/querycontract"),
    /**
     * 代扣申请扣款
     */
    PAP_PAY_APPLY("/pay/pappayapply"),
    /**
     * 代扣申请扣款（服务商模式）
     */
    PARTNER_PAP_PAY_APPLY("/pay/partner/pappayapply"),
    /**
     * 查询代扣订单
     */
    PAP_ORDER_QUERY("/pay/paporderquery"),
    /**
     * 查询代扣订单
     */
    PARTNER_PAP_ORDER_QUERY("/pay/partner/paporderquery"),
    /**
     * 代扣申请解约
     */
    DELETE_ENTRUST_CONTRACT("/papay/deletecontract"),
    /**
     * 代扣申请解约（服务商模式）
     */
    PARTNER_DELETE_ENTRUST_CONTRACT("/papay/partner/deletecontract"),
    /**
     * 刷脸支付
     */
    FACE_PAY("/pay/facepay"),
    /**
     * 查询刷脸支付订单
     */
    FACE_PAY_QUERY("/pay/facepayqueryy"),
    /**
     * 撤销刷脸支付订单
     */
    FACE_PAY_REVERSE("/secapi/pay/facepayreverse"),
    /**
     * 小微商户申请入驻
     */
    MICRO_SUBMIT("/applyment/micro/submit"),
    /**
     * 查询申请状态
     */
    GET_MICRO_SUBMIT_STATE("/applyment/micro/getstate"),
    /**
     * 提交升级申请
     */
    MICRO_SUBMIT_UPGRADE("/applyment/micro/submitupgrade"),
    /**
     * 查询升级申请单状态
     */
    GET_MICRO_UPGRADE_STATE("/applyment/micro/getupgradestate"),
    /**
     * 查询提现状态
     */
    QUERY_AUTO_WITH_DRAW_BY_DATE("/fund/queryautowithdrawbydate"),
    /**
     * 修改结算银行卡
     */
    MICRO_MODIFY_ARCHIVES("/applyment/micro/modifyarchives"),
    /**
     * 重新发起提现
     */
    RE_AUTO_WITH_DRAW_BY_DATE("/fund/reautowithdrawbydate"),
    /**
     * 修改联系信息
     */
    MICRO_MODIFY_CONTACT_INFO("/applyment/micro/modifycontactinfo"),
    /**
     * 小微商户关注功能配置
     */
    ADD_RECOMMEND_CONF("/secapi/mkt/addrecommendconf"),
    /**
     * 小微商户开发配置新增支付目录
     */
    ADD_SUB_DEV_CONFIG("/secapi/mch/addsubdevconfig"),
    /**
     * 小微商户开发配置查询
     */
    QUERY_SUB_DEV_CONFIG("/secapi/mch/querysubdevconfig"),

    /**
     * 查询用户支付分开启状态
     */
    USER_SERVICE_STATE("/v3/payscore/user-service-state"),
    /**
     * 创建先享后付订单 OR 查询先享后付订单
     */
    PAY_AFTER_ORDERS("/v3/payscore/payafter-orders"),
    /**
     * 撤销先享后付订单
     */
    CANCEL_PAY_AFTER_ORDERS("/v3/payscore/payafter-orders/out_order_no/cancel"),
    /**
     * 完结先享后付订单
     */
    COMPLETE_PAY_AFTER_ORDERS("/v3/payscore/payafter-orders/out_order_no/complete"),
    CANCEL1_PAY_AFTER_ORDERS("/v3/payscore/payafter-orders/out_order_no/cancel");


    /**
     * 类型
     */
    private final String type;

    WxApiType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
