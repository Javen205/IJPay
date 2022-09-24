
package com.ijpay.qqpay;

import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;

import java.io.InputStream;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>QQ 钱包支付 API</p>
 *
 * @author Javen
 */
public class QqPayApi {
	/**
	 * 提交付款码支付
	 */
	private static final String MICRO_PAY_URL = "https://qpay.qq.com/cgi-bin/pay/qpay_micro_pay.cgi";
	/**
	 * 统一下单
	 */
	private static final String UNIFIED_ORDER_URL = "https://qpay.qq.com/cgi-bin/pay/qpay_unified_order.cgi";

	/**
	 * 订单查询
	 */
	private static final String ORDER_QUERY_URL = "https://qpay.qq.com/cgi-bin/pay/qpay_order_query.cgi";

	/**
	 * 关闭订单
	 */
	private static final String CLOSE_ORDER_URL = "https://qpay.qq.com/cgi-bin/pay/qpay_close_order.cgi";

	/**
	 * 撤销订单
	 */
	private static final String ORDER_REVERSE_URL = "https://api.qpay.qq.com/cgi-bin/pay/qpay_reverse.cgi";

	/**
	 * 申请退款
	 */
	private static final String ORDER_REFUND_URL = "https://api.qpay.qq.com/cgi-bin/pay/qpay_refund.cgi";

	/**
	 * 退款查询
	 */
	private static final String REFUND_QUERY_URL = "https://qpay.qq.com/cgi-bin/pay/qpay_refund_query.cgi";

	/**
	 * 对账单下载
	 */
	private static final String DOWNLOAD_BILL_URL = "https://qpay.qq.com/cgi-bin/sp_download/qpay_mch_statement_down.cgi";

	/**
	 * 创建现金红包
	 */
	private static final String CREATE_READ_PACK_URL = "https://api.qpay.qq.com/cgi-bin/hongbao/qpay_hb_mch_send.cgi";
	/**
	 * 查询红包详情
	 */
	private static final String GET_HB_INFO_URL = "https://qpay.qq.com/cgi-bin/mch_query/qpay_hb_mch_list_query.cgi";
	/**
	 * 红包对账单下载
	 */
	private static final String DOWNLOAD_HB_BILL_URL = "https://api.qpay.qq.com/cgi-bin/hongbao/qpay_hb_mch_down_list_file.cgi";

	/**
	 * 企业付款到余额
	 */
	private static final String TRANSFER_URL = "https://api.qpay.qq.com/cgi-bin/epay/qpay_epay_b2c.cgi";
	/**
	 * 查询企业付款
	 */
	private static final String GET_TRANSFER_INFO_URL = "https://qpay.qq.com/cgi-bin/pay/qpay_epay_query.cgi";
	/**
	 * 企业付款对账单下载
	 */
	private static final String DOWNLOAD_TRANSFER_BILL_URL = "https://qpay.qq.com/cgi-bin/pay/qpay_epay_statement_down.cgi";


	/**
	 * 提交付款码支付
	 *
	 * @param params 请求参数
	 * @return {@link String} 请求返回的结果
	 */
	public static String microPay(Map<String, String> params) {
		return doPost(MICRO_PAY_URL, params);
	}

	/**
	 * 统一下单
	 *
	 * @param params 请求参数
	 * @return {@link String} 请求返回的结果
	 */
	public static String unifiedOrder(Map<String, String> params) {
		return doPost(UNIFIED_ORDER_URL, params);
	}

	/**
	 * 订单查询
	 *
	 * @param params 请求参数
	 * @return {@link String} 请求返回的结果
	 */
	public static String orderQuery(Map<String, String> params) {
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
	 * @param cerPath  证书文件目录
	 * @param certPass 证书密码
	 * @return {@link String} 请求返回的结果
	 */
	public static String orderReverse(Map<String, String> params, String cerPath, String certPass) {
		return doPost(ORDER_REVERSE_URL, params, cerPath, certPass);
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
		return doPost(ORDER_REVERSE_URL, params, certFile, certPass);
	}

	/**
	 * 申请退款
	 *
	 * @param params   请求参数
	 * @param cerPath  证书文件目录
	 * @param certPass 证书密码
	 * @return {@link String} 请求返回的结果
	 */
	public static String orderRefund(Map<String, String> params, String cerPath, String certPass) {
		return doPost(ORDER_REFUND_URL, params, cerPath, certPass);
	}

	/**
	 * 申请退款
	 *
	 * @param params   请求参数
	 * @param certFile 证书文件的 InputStream
	 * @param certPass 证书密码
	 * @return {@link String} 请求返回的结果
	 */
	public static String orderRefund(Map<String, String> params, InputStream certFile, String certPass) {
		return doPost(ORDER_REFUND_URL, params, certFile, certPass);
	}

	/**
	 * 退款查询
	 *
	 * @param params 请求参数
	 * @return {@link String} 请求返回的结果
	 */
	public static String refundQuery(Map<String, String> params) {
		return doPost(REFUND_QUERY_URL, params);
	}

	/**
	 * 对账单下载
	 *
	 * @param params 请求参数
	 * @return {@link String} 请求返回的结果
	 */
	public static String downloadBill(Map<String, String> params) {
		return doPost(DOWNLOAD_BILL_URL, params);
	}

	/**
	 * 创建现金红包
	 *
	 * @param params   请求参数
	 * @param cerPath  证书文件目录
	 * @param certPass 证书密码
	 * @return {@link String} 请求返回的结果
	 */
	public static String createReadPack(Map<String, String> params, String cerPath, String certPass) {
		return doPost(CREATE_READ_PACK_URL, params, cerPath, certPass);
	}

	/**
	 * 创建现金红包
	 *
	 * @param params   请求参数
	 * @param certFile 证书文件的 InputStream
	 * @param certPass 证书密码
	 * @return {@link String} 请求返回的结果
	 */
	public static String createReadPack(Map<String, String> params, InputStream certFile, String certPass) {
		return doPost(CREATE_READ_PACK_URL, params, certFile, certPass);
	}

	/**
	 * 查询红包详情
	 *
	 * @param params 请求参数
	 * @return {@link String} 请求返回的结果
	 */
	public static String getHbInfo(Map<String, String> params) {
		return doPost(GET_HB_INFO_URL, params);
	}

	/**
	 * 下载红包对账单
	 *
	 * @param params 请求参数
	 * @return {@link String} 请求返回的结果
	 */
	public static String downloadHbBill(Map<String, String> params) {
		return doPost(DOWNLOAD_HB_BILL_URL, params);
	}


	/**
	 * 企业付款到余额
	 *
	 * @param params   请求参数
	 * @param cerPath  证书文件目录
	 * @param certPass 证书密码
	 * @return {@link String} 请求返回的结果
	 */
	public static String transfer(Map<String, String> params, String cerPath, String certPass) {
		return doPost(TRANSFER_URL, params, cerPath, certPass);
	}

	/**
	 * 企业付款到余额
	 *
	 * @param params   请求参数
	 * @param certFile 证书文件的 InputStream
	 * @param certPass 证书密码
	 * @return {@link String} 请求返回的结果
	 */
	public static String transfer(Map<String, String> params, InputStream certFile, String certPass) {
		return doPost(TRANSFER_URL, params, certFile, certPass);
	}

	/**
	 * 查询企业付款
	 *
	 * @param params 请求参数
	 * @return {@link String} 请求返回的结果
	 */
	public static String getTransferInfo(Map<String, String> params) {
		return doPost(GET_TRANSFER_INFO_URL, params);
	}

	/**
	 * 下载企业付款对账单
	 *
	 * @param params 请求参数
	 * @return {@link String} 请求返回的结果
	 */
	public static String downloadTransferBill(Map<String, String> params) {
		return doPost(DOWNLOAD_TRANSFER_BILL_URL, params);
	}


	public static String doPost(String url, Map<String, String> params) {
		return HttpKit.getDelegate().post(url, WxPayKit.toXml(params));
	}

	public static String doPost(String url, Map<String, String> params, String certPath, String certPass) {
		return HttpKit.getDelegate().post(url, WxPayKit.toXml(params), certPath, certPass);
	}

	public static String doPost(String url, Map<String, String> params, InputStream certFile, String certPass) {
		return HttpKit.getDelegate().post(url, WxPayKit.toXml(params), certFile, certPass);
	}
}
