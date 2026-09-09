package com.ijpay.fuiyou;

import cn.hutool.core.util.StrUtil;
import com.ijpay.core.kit.PayKit;

import java.util.Arrays;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、富友支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>富友支付签名工具类</p>
 *
 * <p>富友支付签名规则：参与签名的字段按固定顺序用"|"拼接，末尾追加商户密钥 mchnt_key，对拼接后的字符串做 MD5 摘要</p>
 *
 * @author Javen
 */
public class FuiYouPayCore {

	private FuiYouPayCore() {
	}

	/**
	 * 富友支付 MD5 签名
	 * <p>
	 * 示例：mchnt_cd|order_type|order_amt|mchnt_order_no|txn_begin_ts|goods_des|term_id|term_ip|notify_url|random_str|version|mchnt_key
	 *
	 * @param mchntKey 商户密钥
	 * @param values   参与签名的字段值（按官方文档约定顺序传入，末尾无需追加 mchnt_key）
	 * @return {@link String} 签名结果
	 */
	public static String createSign(String mchntKey, String... values) {
		return createSign(mchntKey, false, values);
	}

	/**
	 * 富友支付 MD5 签名
	 *
	 * @param mchntKey  商户密钥
	 * @param isToUpper 是否转为大写
	 * @param values    参与签名的字段值（按官方文档约定顺序传入，末尾无需追加 mchnt_key）
	 * @return {@link String} 签名结果
	 */
	public static String createSign(String mchntKey, boolean isToUpper, String... values) {
		StringBuilder sb = new StringBuilder();
		if (values != null && values.length > 0) {
			Arrays.stream(values).forEach(value -> sb.append(value == null ? StrUtil.EMPTY : value).append("|"));
		}
		// 末尾追加商户密钥
		sb.append(mchntKey);
		String sign = PayKit.md5(sb.toString());
		return isToUpper ? sign.toUpperCase() : sign;
	}

	/**
	 * 富友支付签名校验
	 *
	 * @param sign     待校验的签名
	 * @param mchntKey 商户密钥
	 * @param values   参与签名的字段值（按官方文档约定顺序传入，末尾无需追加 mchnt_key）
	 * @return {@code true} 校验通过
	 */
	public static boolean verifySign(String sign, String mchntKey, String... values) {
		return verifySign(sign, mchntKey, false, values);
	}

	/**
	 * 富友支付签名校验
	 *
	 * @param sign     待校验的签名
	 * @param mchntKey 商户密钥
	 * @param isToUpper 签名为大写时传 {@code true}
	 * @param values   参与签名的字段值（按官方文档约定顺序传入，末尾无需追加 mchnt_key）
	 * @return {@code true} 校验通过
	 */
	public static boolean verifySign(String sign, String mchntKey, boolean isToUpper, String... values) {
		if (StrUtil.isBlank(sign)) {
			return false;
		}
		String createSign = createSign(mchntKey, isToUpper, values);
		return sign.equals(createSign);
	}

	/**
	 * 2.5 支付结果通知-异步通知签名校验（sign）
	 * <p>
	 * 拼接 mchnt_cd + "|" + mchnt_order_no + "|" + settle_order_amt + "|" + order_amt + "|" + txn_fin_ts + "|" + reserved_fy_settle_dt + "|" + random_str + "|" + mchnt_key 并做 md5 摘要
	 *
	 * @param sign        回调报文中的签名
	 * @param mchntCd     商户号
	 * @param mchntOrderNo 商户订单号
	 * @param settleOrderAmt 应结算订单金额（单位：分）
	 * @param orderAmt    订单金额（单位：分）
	 * @param txnFinTs    交易完成时间 yyyyMMddHHmmss
	 * @param reservedFySettleDt 富友结算日期 yyyyMMdd
	 * @param randomStr   随机字符串
	 * @param mchntKey    商户密钥
	 * @return {@code true} 校验通过
	 */
	public static boolean verifyNotify(String sign, String mchntCd, String mchntOrderNo, String settleOrderAmt,
									   String orderAmt, String txnFinTs, String reservedFySettleDt, String randomStr, String mchntKey) {
		return verifySign(sign, mchntKey,
			mchntCd, mchntOrderNo, settleOrderAmt, orderAmt, txnFinTs, reservedFySettleDt, randomStr);
	}

	/**
	 * 2.5 支付结果通知-异步通知签名校验（full_sign）
	 * <p>
	 * 拼接 result_code + "|" + result_msg + "|" + mchnt_cd + "|" + mchnt_order_no + "|" + settle_order_amt + "|" + order_amt + "|" + txn_fin_ts + "|" + reserved_fy_settle_dt + "|" + random_str + "|" + mchnt_key 并做 md5 摘要
	 *
	 * @param fullSign     回调报文中的全签名
	 * @param resultCode   结果码
	 * @param resultMsg    结果描述
	 * @param mchntCd      商户号
	 * @param mchntOrderNo 商户订单号
	 * @param settleOrderAmt 应结算订单金额（单位：分）
	 * @param orderAmt     订单金额（单位：分）
	 * @param txnFinTs     交易完成时间 yyyyMMddHHmmss
	 * @param reservedFySettleDt 富友结算日期 yyyyMMdd
	 * @param randomStr    随机字符串
	 * @param mchntKey     商户密钥
	 * @return {@code true} 校验通过
	 */
	public static boolean verifyNotifyByFullSign(String fullSign, String resultCode, String resultMsg, String mchntCd,
												 String mchntOrderNo, String settleOrderAmt, String orderAmt, String txnFinTs,
												 String reservedFySettleDt, String randomStr, String mchntKey) {
		return verifySign(fullSign, mchntKey,
			resultCode, resultMsg, mchntCd, mchntOrderNo, settleOrderAmt, orderAmt, txnFinTs, reservedFySettleDt, randomStr);
	}
}
