package com.ijpay.wxpay;

import com.ijpay.wxpay.constant.enums.SignType;
import com.ijpay.wxpay.kit.HttpKit;
import com.ijpay.wxpay.kit.WxPayKit;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p>微信支付相关接口</p>
 *
 * @author Javen
 */
public class WxPayApi {
    // 统一下单接口
    private static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    // 订单查询
    private static final String ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    // 关闭订单
    private static final String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
    // 撤销订单
    private static final String REVERSE_URL = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
    // 申请退款
    private static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    // 查询退款
    private static final String REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
    // 下载对账单
    private static final String DOWNLOAD_BILLY_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    // 交易保障
    private static final String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";
    // 转换短链接
    private static final String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
    // 授权码查询openId接口
    private static final String AUTH_CODE_TO_OPENID_URL = "https://api.mch.weixin.qq.com/tools/authcodetoopenid";
    // 刷卡支付
    private static final String MICRO_PAY_URL = "https://api.mch.weixin.qq.com/pay/micropay";
    // 企业付款
    private static final String TRANSFERS_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
    // 查询企业付款
    private static final String GET_TRANSFER_INFO_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gettransferinfo";
    // 企业付款到银行
    private static final String PAY_BANK_URL = "https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank";
    // 查询企业付款
    private static final String QUERY_BANK_URL = "https://api.mch.weixin.qq.com/mmpaysptrans/query_bank";
    // 获取RSA加密公钥
    private static final String GET_PUBLIC_KEY_URL = "https://fraud.mch.weixin.qq.com/risk/getpublickey";
    // 申请签约
    private static final String ENTRUST_WEB_URL = "https://api.mch.weixin.qq.com/papay/entrustweb";
    // 支付中签约接口
    private static final String CONTRACT_ORDER_URL = "https://api.mch.weixin.qq.com/pay/contractorder";
    // 查询签约关系
    private static final String QUERY_CONTRACT_URL = "https://api.mch.weixin.qq.com/papay/querycontract";
    // 申请扣款
    private static final String PAP_PAY_APPLY_URL = "https://api.mch.weixin.qq.com/pay/pappayapply";
    // 申请解约
    private static final String DELETE_CONTRACT_URL = "https://api.mch.weixin.qq.com/papay/deletecontract";
    // 查询签约关系对账单
    private static final String CONTRACT_BILL_URL = "https://api.mch.weixin.qq.com/papay/contractbill";
    // 代扣查询订单
    private static final String PAP_ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/paporderquery";
    // 分账请求
    private static final String PROFIT_SHARING_URL = "https://api.mch.weixin.qq.com/secapi/pay/profitsharing";
    // 查询分账结果
    private static final String PROFIT_SHARING_QUERY_URL = "https://api.mch.weixin.qq.com/pay/profitsharingquery";
    // 添加分账接收方
    private static final String PROFIT_SHARING_ADD_RECEIVER_URL = "https://api.mch.weixin.qq.com/pay/profitsharingaddreceiver";
    // 删除分账接收方
    private static final String PROFIT_SHARING_REMOVE_RECEIVER_URL = "https://api.mch.weixin.qq.com/pay/profitsharingremovereceiver";
    // 完结分账
    private static final String PROFIT_SHARING_FINISH_URL = "https://api.mch.weixin.qq.com/secapi/pay/profitsharingfinish";
    // 发放代金券
    private static final String SEND_COUPON_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/send_coupon";
    // 查询代金券批次
    private static final String QUERY_COUPON_STOCK_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/query_coupon_stock";
    // 查询代金券信息
    private static final String QUERY_COUPONS_INFO_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/querycouponsinfo";
    // 拉取订单评价数据
    private static final String BATCH_QUERY_COMMENT_URL = "https://api.mch.weixin.qq.com/billcommentsp/batchquerycomment";
    // 支付押金（人脸支付）
    private static final String DEPOSIT_FACE_PAY_URL = "https://api.mch.weixin.qq.com/deposit/facepay";
    // 支付押金（付款码支付）
    private static final String DEPOSIT_MICRO_PAY_URL = "https://api.mch.weixin.qq.com/deposit/micropay";
    // 查询订单
    private static final String DEPOSIT_ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/deposit/orderquery";
    // 撤销订单
    private static final String DEPOSIT_REVERSE_URL = "https://api.mch.weixin.qq.com/deposit/reverse";
    // 消费押金
    private static final String DEPOSIT_CONSUME_URL = "https://api.mch.weixin.qq.com/deposit/consume";
    // 申请退款（押金）
    private static final String DEPOSIT_REFUND_URL = "https://api.mch.weixin.qq.com/deposit/refund";
    // 查询退款（押金）
    private static final String DEPOSIT_REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/deposit/refundquery";

    // 获取沙箱秘钥
    private static final String GET_SING_KEY_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";
    // 统一下单接口
    private static final String UNIFIED_ORDER_SANDBOX_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";
    // 刷卡支付
    private static final String MICRO_PAY_SANDBOX_RUL = "https://api.mch.weixin.qq.com/sandboxnew/pay/micropay";
    // 订单查询
    private static final String ORDER_QUERY_SANDBOX_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/orderquery";
    // 申请退款
    private static final String REFUND_SANDBOX_URL = "https://api.mch.weixin.qq.com/sandboxnew/secapi/pay/refund";
    // 查询退款
    private static final String REFUND_QUERY_SANDBOX_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/refundquery";
    // 下载对账单
    private static final String DOWNLOAD_BILL_SANDBOX_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/downloadbill";

    private WxPayApi() {
    }

    /**
     * 获取验签秘钥API
     *
     * @param mch_id     商户号
     * @param partnerKey API 密钥
     * @param signType   签名方式
     * @return {@link String} 请求返回的结果
     */
    public static String getSignKey(String mch_id, String partnerKey, SignType signType) {
        Map<String, String> map = new HashMap<String, String>();
        String nonce_str = WxPayKit.generateStr();
        map.put("mch_id", mch_id);
        map.put("nonce_str", nonce_str);
        map.put("sign", WxPayKit.createSign(map, partnerKey, signType));
        return doPost(GET_SING_KEY_URL, map);
    }

    /**
     * 统一下单
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    统一下单请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String pushOrder(boolean isSandbox, Map<String, String> params) {
        if (isSandbox)
            return doPost(UNIFIED_ORDER_SANDBOX_URL, params);
        return doPost(UNIFIED_ORDER_URL, params);
    }

    /**
     * 订单查询
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String orderQuery(boolean isSandbox, Map<String, String> params) {
        if (isSandbox)
            return doPost(ORDER_QUERY_SANDBOX_URL, params);
        return doPost(ORDER_QUERY_URL, params);
    }

    /**
     * 关闭订单
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String closeOrder(Map<String, String> params) {
        return doPost(CLOSE_ORDER_URL, params);
    }

    /**
     * 撤销订单
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String orderReverse(Map<String, String> params, String certPath, String certPass) {
        return doPostSSL(REVERSE_URL, params, certPath, certPass);
    }

    /**
     * 撤销订单
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String orderReverse(Map<String, String> params, InputStream certFile, String certPass) {
        return doPostSSL(REVERSE_URL, params, certFile, certPass);
    }

    /**
     * 申请退款
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @param certPath  证书文件目录
     * @param certPass  证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String orderRefund(boolean isSandbox, Map<String, String> params, String certPath, String certPass) {
        if (isSandbox)
            return doPostSSL(REFUND_SANDBOX_URL, params, certPath, certPass);
        return doPostSSL(REFUND_URL, params, certPath, certPass);
    }

    /**
     * 申请退款
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @param certFile  证书文件的 InputStream
     * @param certPass  证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String orderRefund(boolean isSandbox, Map<String, String> params, InputStream certFile, String certPass) {
        if (isSandbox)
            return doPostSSL(REFUND_SANDBOX_URL, params, certFile, certPass);
        return doPostSSL(REFUND_URL, params, certFile, certPass);
    }

    /**
     * 查询退款
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String orderRefundQuery(boolean isSandbox, Map<String, String> params) {
        if (isSandbox)
            return doPost(REFUND_QUERY_SANDBOX_URL, params);
        return doPost(REFUND_QUERY_URL, params);
    }

    /**
     * 下载对账单
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String downloadBill(boolean isSandbox, Map<String, String> params) {
        if (isSandbox)
            return doPost(DOWNLOAD_BILL_SANDBOX_URL, params);
        return doPost(DOWNLOAD_BILLY_URL, params);
    }

    /**
     * 交易保障
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String orderReport(Map<String, String> params) {
        return doPost(REPORT_URL, params);
    }

    /**
     * 转换短链接
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String toShortUrl(Map<String, String> params) {
        return doPost(SHORT_URL, params);
    }

    /**
     * 授权码查询 openId
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String authCodeToOpenid(Map<String, String> params) {
        return doPost(AUTH_CODE_TO_OPENID_URL, params);
    }

    /**
     * 刷卡支付
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String micropay(boolean isSandbox, Map<String, String> params) {
        if (isSandbox)
            return WxPayApi.doPost(MICRO_PAY_SANDBOX_RUL, params);
        return WxPayApi.doPost(MICRO_PAY_URL, params);
    }

    /**
     * 企业付款到零钱
     *
     * @param params       请求参数
     * @param certPath     证书文件目录
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String transfers(Map<String, String> params, String certPath, String certPassword) {
        return WxPayApi.doPostSSL(TRANSFERS_URL, params, certPath, certPassword);
    }

    /**
     * 企业付款到零钱
     *
     * @param params       请求参数
     * @param certFile     证书文件的 InputStream
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String transfers(Map<String, String> params, InputStream certFile, String certPassword) {
        return WxPayApi.doPostSSL(TRANSFERS_URL, params, certFile, certPassword);
    }

    /**
     * 查询企业付款到零钱
     *
     * @param params       请求参数
     * @param certPath     证书文件目录
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String getTransferInfo(Map<String, String> params, String certPath, String certPassword) {
        return WxPayApi.doPostSSL(GET_TRANSFER_INFO_URL, params, certPath, certPassword);
    }

    /**
     * 查询企业付款到零钱
     *
     * @param params       请求参数
     * @param certFile     证书文件的 InputStream
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String getTransferInfo(Map<String, String> params, InputStream certFile, String certPassword) {
        return WxPayApi.doPostSSL(GET_TRANSFER_INFO_URL, params, certFile, certPassword);
    }

    /**
     * 企业付款到银行
     *
     * @param params       请求参数
     * @param certPath     证书文件目录
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String payBank(Map<String, String> params, String certPath, String certPassword) {
        return WxPayApi.doPostSSL(PAY_BANK_URL, params, certPath, certPassword);
    }

    /**
     * 企业付款到银行
     *
     * @param params       请求参数
     * @param certFile     证书文件的 InputStream
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String payBank(Map<String, String> params, InputStream certFile, String certPassword) {
        return WxPayApi.doPostSSL(PAY_BANK_URL, params, certFile, certPassword);
    }

    /**
     * 查询企业付款到银行
     *
     * @param params       请求参数
     * @param certPath     证书文件目录
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String queryBank(Map<String, String> params, String certPath, String certPassword) {
        return WxPayApi.doPostSSL(QUERY_BANK_URL, params, certPath, certPassword);
    }

    /**
     * 查询企业付款到银行
     *
     * @param params       请求参数
     * @param certFile     证书文件的  InputStream
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String queryBank(Map<String, String> params, InputStream certFile, String certPassword) {
        return WxPayApi.doPostSSL(QUERY_BANK_URL, params, certFile, certPassword);
    }

    /**
     * 获取 RSA 加密公钥
     *
     * @param params       请求参数
     * @param certPath     证书文件目录
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String getPublicKey(Map<String, String> params, String certPath, String certPassword) {
        return WxPayApi.doPostSSL(GET_PUBLIC_KEY_URL, params, certPath, certPassword);
    }

    /**
     * 获取 RSA 加密公钥
     *
     * @param params       请求参数
     * @param certFile     证书文件的   InputStream
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String getPublicKey(Map<String, String> params, InputStream certFile, String certPassword) {
        return WxPayApi.doPostSSL(GET_PUBLIC_KEY_URL, params, certFile, certPassword);
    }

    /**
     * 申请签约 https://pay.weixin.qq.com/wiki/doc/api/pap.php?chapter=18_1&index=1
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String entrustweb(Map<String, Object> params) {
        return doGet(ENTRUST_WEB_URL, params);
    }

    /**
     * 支付中签约 https://pay.weixin.qq.com/wiki/doc/api/pap.php?chapter=18_13&index=2
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String contractorder(Map<String, String> params) {
        return doPost(CONTRACT_ORDER_URL, params);
    }

    /**
     * 查询签约关系 https://pay.weixin.qq.com/wiki/doc/api/pap.php?chapter=18_2&index=3
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String querycontract(Map<String, String> params) {
        return doPost(QUERY_CONTRACT_URL, params);
    }

    /**
     * 申请扣款
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String papPayApply(Map<String, String> params) {
        return doPost(PAP_PAY_APPLY_URL, params);
    }

    /**
     * 申请解约
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String deleteContract(Map<String, String> params) {
        return doPost(DELETE_CONTRACT_URL, params);
    }

    /**
     * 查询签约关系对账单
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String contractBill(Map<String, String> params) {
        return doPost(CONTRACT_BILL_URL, params);
    }

    /**
     * 代扣订单查询
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String papOrderQuery(Map<String, String> params) {
        return doPost(PAP_ORDER_QUERY_URL, params);
    }

    /**
     * 分账请求
     *
     * @param params       请求参数
     * @param certPath     证书文件目录
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharing(Map<String, String> params, String certPath, String certPassword) {
        return doPostSSL(PROFIT_SHARING_URL, params, certPath, certPassword);
    }

    /**
     * 分账请求
     *
     * @param params       请求参数
     * @param certFile     证书文件的  InputStream
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharing(Map<String, String> params, InputStream certFile, String certPassword) {
        return doPostSSL(PROFIT_SHARING_URL, params, certFile, certPassword);
    }

    /**
     * 查询分账结果
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharingQuery(Map<String, String> params) {
        return doPost(PROFIT_SHARING_QUERY_URL, params);
    }

    /**
     * 添加分账接收方
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharingAddReceiver(Map<String, String> params) {
        return doPost(PROFIT_SHARING_ADD_RECEIVER_URL, params);
    }

    /**
     * 删除分账接收方
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharingRemoveReceiver(Map<String, String> params) {
        return doPost(PROFIT_SHARING_REMOVE_RECEIVER_URL, params);
    }

    /**
     * 完结分账
     *
     * @param params       请求参数
     * @param certPath     证书文件目录
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharingFinish(Map<String, String> params, String certPath, String certPassword) {
        return doPostSSL(PROFIT_SHARING_FINISH_URL, params, certPath, certPassword);
    }

    /**
     * 完结分账
     *
     * @param params       请求参数
     * @param certFile     证书文件的 InputStream
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharingFinish(Map<String, String> params, InputStream certFile, String certPassword) {
        return doPostSSL(PROFIT_SHARING_FINISH_URL, params, certFile, certPassword);
    }

    /**
     * 发放代金券
     *
     * @param params       请求参数
     * @param certPath     证书文件目录
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String sendCoupon(Map<String, String> params, String certPath, String certPassword) {
        return doPostSSL(SEND_COUPON_URL, params, certPath, certPassword);
    }

    /**
     * 发放代金券
     *
     * @param params       请求参数
     * @param certFile     证书文件的 InputStream
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String sendCoupon(Map<String, String> params, InputStream certFile, String certPassword) {
        return doPostSSL(SEND_COUPON_URL, params, certFile, certPassword);
    }

    /**
     * 查询代金券批次
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String queryCouponStock(Map<String, String> params) {
        return doPost(QUERY_COUPON_STOCK_URL, params);
    }

    /**
     * 查询代金券信息
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String queryCouponsInfo(Map<String, String> params) {
        return doPost(QUERY_COUPONS_INFO_URL, params);
    }

    /**
     * 拉取订单评价数据
     *
     * @param params       请求参数
     * @param certPath     证书文件目录
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String batchQueryComment(Map<String, String> params, String certPath, String certPassword) {
        return doPostSSL(BATCH_QUERY_COMMENT_URL, params, certPath, certPassword);
    }

    /**
     * 拉取订单评价数据
     *
     * @param params       请求参数
     * @param certFile     证书文件的 InputStream
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String batchQueryComment(Map<String, String> params, InputStream certFile, String certPassword) {
        return doPostSSL(BATCH_QUERY_COMMENT_URL, params, certFile, certPassword);
    }

    /**
     * 支付押金（人脸支付）
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String depositFacepay(Map<String, String> params) {
        return doPost(DEPOSIT_FACE_PAY_URL, params);
    }

    /**
     * 支付押金（付款码支付）
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String depositMicropay(Map<String, String> params) {
        return doPost(DEPOSIT_MICRO_PAY_URL, params);
    }

    /**
     * 查询订单
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String depositOrderQuery(Map<String, String> params) {
        return doPost(DEPOSIT_ORDER_QUERY_URL, params);
    }

    /**
     * 撤销订单
     *
     * @param params       请求参数
     * @param certFile     证书文件的 InputStream
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String depositReverse(Map<String, String> params, InputStream certFile, String certPassword) {
        return doPostSSL(DEPOSIT_REVERSE_URL, params, certFile, certPassword);
    }

    /**
     * 消费押金
     *
     * @param params       请求参数
     * @param certFile     证书文件的 InputStream
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String depositConsume(Map<String, String> params, InputStream certFile, String certPassword) {
        return doPostSSL(DEPOSIT_CONSUME_URL, params, certFile, certPassword);
    }

    /**
     * 申请退款（押金）
     *
     * @param params       请求参数
     * @param certFile     证书文件的 InputStream
     * @param certPassword 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String depositRefund(Map<String, String> params, InputStream certFile, String certPassword) {
        return doPostSSL(DEPOSIT_REFUND_URL, params, certFile, certPassword);
    }

    /**
     * 查询退款（押金）
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String depositRefundQuery(Map<String, String> params) {
        return doPost(DEPOSIT_REFUND_QUERY_URL, params);
    }

    public static String doGet(String url, Map<String, Object> params) {
        return HttpKit.getDelegate().get(url, params);
    }

    public static String doPost(String url, Map<String, String> params) {
        return HttpKit.getDelegate().post(url, WxPayKit.toXml(params));
    }

    public static String doPostSSL(String url, Map<String, String> params, String certPath, String certPass) {
        return HttpKit.getDelegate().postSSL(url, WxPayKit.toXml(params), certPath, certPass);
    }

    public static String doPostSSL(String url, Map<String, String> params, InputStream certFile, String certPass) {
        return HttpKit.getDelegate().postSSL(url, WxPayKit.toXml(params), certFile, certPass);
    }

}
