/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p> 京东支付 Api</p>
 *
 * @author Javen
 */
package com.ijpay.jdpay;

import com.ijpay.core.kit.HttpKit;

public class JdPayApi {
    /**
     * PC 在线支付接口
     */
    public static String PC_SAVE_ORDER_URL = "https://wepay.jd.com/jdpay/saveOrder";
    /**
     * H5 在线支付接口
     */
    public static String H5_SAVE_ORDER_URL = "https://h5pay.jd.com/jdpay/saveOrder";
    /**
     * 统一下单接口
     */
    public static String UNI_ORDER_URL = "https://paygate.jd.com/service/uniorder";
    /**
     * 商户二维码支付接口
     */
    public static String CUSTOMER_PAY_URL = "https://h5pay.jd.com/jdpay/customerPay";
    /**
     * 付款码支付接口
     */
    public static String FKM_PAY_URL = "https://paygate.jd.com/service/fkmPay";

    /**
     * 白条分期策略查询接口
     */
    public static String QUERY_BAI_TIAO_FQ_URL = "https://paygate.jd.com/service/queryBaiTiaoFQ";

    /**
     * 交易查询接口
     */
    public static String QUERY_ORDER_URL = "https://paygate.jd.com/service/query";
    /**
     * 退款申请接口
     */
    public static String REFUND_URL = "https://paygate.jd.com/service/refund";
    /**
     * 撤销申请接口
     */
    public static String REVOKE_URL = "https://paygate.jd.com/service/revoke";
    public static String GET_USER_RELATION_URL = "https://paygate.jd.com/service/getUserRelation";

    /**
     * 统一下单
     *
     * @param xml 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String uniOrder(String xml) {
        return doPost(UNI_ORDER_URL, xml);
    }

    /**
     * 付款码支付
     *
     * @param xml 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String fkmPay(String xml) {
        return doPost(FKM_PAY_URL, xml);
    }

    /**
     * 白条分期策略查询
     *
     * @param xml 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String queryBaiTiaoFq(String xml) {
        return doPost(QUERY_BAI_TIAO_FQ_URL, xml);
    }

    /**
     * 查询订单
     *
     * @param xml 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String queryOrder(String xml) {
        return doPost(QUERY_ORDER_URL, xml);
    }

    /**
     * 退款申请
     *
     * @param xml 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String refund(String xml) {
        return doPost(REFUND_URL, xml);
    }

    /**
     * 撤销申请
     *
     * @param xml 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String revoke(String xml) {
        return doPost(REVOKE_URL, xml);
    }

    public static String doPost(String url, String reqXml) {
        return HttpKit.getDelegate().post(url, reqXml);
    }

}
