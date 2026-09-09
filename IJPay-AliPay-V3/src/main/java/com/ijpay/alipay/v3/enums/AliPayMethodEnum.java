package com.ijpay.alipay.v3.enums;

/**
 * <p>
 * IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。
 * </p>
 *
 * <p>
 * 不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。
 * </p>
 *
 * <p>
 * IJPay 交流群: 723992875、864988890
 * </p>
 *
 * <p>
 * Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a>
 * </p>
 *
 * <p>
 * 支付宝 V3 业务接口 method 枚举，用于 {@code pageExecute}/{@code sdkExecute} 等走 gateway.do
 * 方式的接口调用，其中 value 为官方接口的 method 参数。
 * </p>
 *
 * @author Javen
 */
public enum AliPayMethodEnum {

	/**
	 * 电脑网站支付
	 */
	TRADE_PAGE_PAY("alipay.trade.page.pay"),

	/**
	 * 手机网站支付
	 */
	TRADE_WAP_PAY("alipay.trade.wap.pay"),

	/**
	 * App 支付
	 */
	TRADE_APP_PAY("alipay.trade.app.pay"),

	/**
	 * 统一收单交易创建
	 */
	TRADE_CREATE("alipay.trade.create"),

	/**
	 * 统一收单交易支付（付款码支付）
	 */
	TRADE_PAY("alipay.trade.pay"),

	/**
	 * 统一收单线下交易预创建（扫码支付）
	 */
	TRADE_PRE_CREATE("alipay.trade.precreate"),

	/**
	 * 统一收单交易查询
	 */
	TRADE_QUERY("alipay.trade.query"),

	/**
	 * 统一收单交易退款
	 */
	TRADE_REFUND("alipay.trade.refund"),

	/**
	 * 统一收单退款查询
	 */
	TRADE_FAST_PAY_REFUND_QUERY("alipay.trade.fastpay.refund.query"),

	/**
	 * 统一收单交易关闭
	 */
	TRADE_CLOSE("alipay.trade.close"),

	/**
	 * 统一收单交易撤销
	 */
	TRADE_CANCEL("alipay.trade.cancel"),

	/**
	 * 统一收单交易结算
	 */
	TRADE_SETTLE("alipay.trade.settle"),

	/**
	 * 单笔转账到支付宝账户
	 */
	FUND_TRANS_TO_ACCOUNT_TRANSFER("alipay.fund.trans.toaccount.transfer"),

	/**
	 * 单笔转账到支付宝账户接口（新）
	 */
	FUND_TRANS_UNI_TRANSFER("alipay.fund.trans.uni.transfer"),

	/**
	 * 转账查询
	 */
	FUND_TRANS_COMMON_QUERY("alipay.fund.trans.common.query");

	/**
	 * method 值
	 */
	private final String value;

	AliPayMethodEnum(String value) {
		this.value = value;
	}

	/**
	 * 获取 method 值
	 *
	 * @return method 值
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 根据 method 值获取枚举
	 *
	 * @param value method 值
	 * @return {@link AliPayMethodEnum}，未匹配到返回 {@code null}
	 */
	public static AliPayMethodEnum of(String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		for (AliPayMethodEnum methodEnum : values()) {
			if (methodEnum.value.equals(value)) {
				return methodEnum;
			}
		}
		return null;
	}

	/**
	 * 转换为字符串（即 method 值）
	 *
	 * @return method 值
	 */
	@Override
	public String toString() {
		return value;
	}
}
