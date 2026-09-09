package com.ijpay.fuiyou.enums;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、富友支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>富友支付 API 接口枚举</p>
 *
 * @author Javen
 */
public enum FuiYouApiType implements FuiYouApiEnum {

	/**
	 * 2.1 统一下单接口
	 */
	UNIFIED_ORDER("/aggregatePay/preCreate", "统一下单接口"),

	/**
	 * 2.2 条码支付接口
	 */
	MICRO_PAY("/aggregatePay/micropay", "条码支付接口"),

	/**
	 * 2.3 订单查询接口
	 */
	ORDER_QUERY("/aggregatePay/commonQuery", "订单查询接口"),

	/**
	 * 2.4 退款申请接口
	 */
	REFUND("/aggregatePay/commonRefund", "退款申请接口"),

	/**
	 * 2.6 公众号/服务窗统一下单接口
	 */
	WX_PRE_CREATE("/aggregatePay/wxPreCreate", "公众号/服务窗统一下单接口"),

	/**
	 * 2.8 查询可提现资金
	 */
	QUERY_WITHDRAW_AMT("/aggregatePay/queryWithdrawAmt", "查询可提现资金"),

	/**
	 * 2.9 查询提现手续费信息
	 */
	QUERY_FEE_AMT("/aggregatePay/queryFeeAmt", "查询提现手续费信息"),

	/**
	 * 2.10 发起提现
	 */
	WITHDRAW("/aggregatePay/withdraw", "发起提现"),

	/**
	 * 2.11 资金划拨查询
	 */
	QUERY_TRANSFER_AMT("/aggregatePay/queryTransferAmt", "资金划拨查询"),

	/**
	 * 2.12 历史订单查询接口
	 */
	HIS_TRADE_QUERY("/aggregatePay/hisTradeQuery", "历史订单查询接口"),

	/**
	 * 2.13 退款查询接口
	 */
	REFUND_QUERY("/aggregatePay/refundQuery", "退款查询接口"),

	/**
	 * 2.14 关闭订单接口
	 */
	CLOSE_ORDER("/aggregatePay/closeOrder", "关闭订单接口"),

	/**
	 * 2.15 撤销订单接口
	 */
	CANCEL_ORDER("/aggregatePay/cancelOrder", "撤销订单接口"),

	/**
	 * 2.16 服务商模式获取 openid 接口
	 */
	AUTH2_OPENID("/aggregatePay/auth2Openid", "服务商模式获取 openid 接口"),

	/**
	 * 2.17 银联授权码获取 user_id 接口
	 */
	GET_OPENID_BY_AUTH_CODE("/aggregatePay/getOpenidByAuthCode", "银联授权码获取 user_id 接口");

	/**
	 * 接口路径
	 */
	private final String url;
	/**
	 * 接口描述
	 */
	private final String desc;

	FuiYouApiType(String url, String desc) {
		this.url = url;
		this.desc = desc;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public String toString() {
		return url;
	}
}
