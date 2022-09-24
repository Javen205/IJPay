package com.ijpay.wxpay.enums.v3;

import com.ijpay.wxpay.enums.WxApiEnum;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信支付 v3 接口-电商收付通相关接口枚举</p>
 *
 * @author Javen
 */
public enum EcommerceApiEnum implements WxApiEnum {
	/**
	 * 二级商户进件
	 */
	APPLY("/v3/ecommerce/applyments/", "二级商户进件"),

	/**
	 * 查询进件申请状态
	 */
	APPLY_STATE("/v3/ecommerce/applyments/%s", "查询进件申请状态"),

	/**
	 * 通过业务申请编号查询申请状态
	 */
	APPLY_STATE_BY_NO("/v3/ecommerce/applyments/out-request-no/%s", "通过业务申请编号查询申请状态"),

	/**
	 * 分账接口-请求分账/查询分账结果
	 */
	PROFIT_SHARING_ORDERS("/v3/ecommerce/profitsharing/orders", "请求分账/查询分账结果"),

	/**
	 * 分账接口-查询分账回退结果
	 */
	PROFIT_SHARING_RETURN_ORDERS("/v3/ecommerce/profitsharing/returnorders", "查询分账回退结果"),

	/**
	 * 分账接口-完结分账
	 */
	PROFIT_SHARING_FINISH_ORDER("/v3/ecommerce/profitsharing/finish-order", "完结分账"),

	/**
	 * 查询订单剩余待分金额
	 */
	PROFIT_SHARING_QUERY("/v3/ecommerce/profitsharing/orders/%s/amounts", "查询订单剩余待分金额"),

	/**
	 * 添加分账接收方
	 */
	PROFIT_SHARING_RECEIVERS_ADD("/v3/ecommerce/profitsharing/receivers/add", "添加分账接收方"),

	/**
	 * 删除分账接收方
	 */
	PROFIT_SHARING_RECEIVERS_DELETE("/v3/ecommerce/profitsharing/receivers/delete", "删除分账接收方"),

	/**
	 * 补差接口-请求补差
	 */
	CREATE_SUBSIDIES("/v3/ecommerce/subsidies/create", "请求补差"),

	/**
	 * 补差接口-请求补差
	 */
	RETURN_SUBSIDIES("/v3/ecommerce/subsidies/return", "请求补差"),

	/**
	 * 补差接口-取消补差
	 */
	CANCEL_SUBSIDIES("/v3/ecommerce/subsidies/cancel", "取消补差"),

	/**
	 * 退款接口-申请退款
	 */
	REFUNDS("/v3/ecommerce/refunds/apply", "申请退款"),

	/**
	 * 退款接口-通过微信支付退款单号查询退款
	 */
	QUERY_REFUND("/v3/ecommerce/refunds/id/%s", "通过微信支付退款单号查询退款"),

	/**
	 * 退款接口-通过商户退款单号查询退款
	 */
	QUERY_REFUNDS_BY_REFUND_NO("/v3/ecommerce/refunds/out-refund-no/%s", "通过商户退款单号查询退款"),

	/**
	 * 退款接口-查询/垫付退款回补
	 */
	RETURN_ADVANCE_OR_QUERY("/v3/ecommerce/refunds/%s/return-advance", "查询/垫付退款回补"),

	/**
	 * 查询二级商户账户实时余额
	 */
	QUERY_BALANCE("/v3/ecommerce/fund/balance/%s", "查询二级商户账户实时余额"),

	/**
	 * 查询二级商户账户日终余额
	 */
	QUERY_END_DAY_BALANCE("/v3/ecommerce/fund/enddaybalance/%s", "查询二级商户账户日终余额"),

	/**
	 * 查询电商平台账户实时余额
	 */
	QUERY_MERCHANT_BALANCE("/v3/merchant/fund/balance/%s", "查询电商平台账户实时余额"),

	/**
	 * 查询电商平台账户日终余额
	 */
	QUERY_MERCHANT_END_DAY_BALANCE("/v3/merchant/fund/dayendbalance/%s", "查询电商平台账户日终余额"),

	/**
	 * 提现接口-二级商户预约提现
	 */
	WITHDRAW("/v3/ecommerce/fund/withdraw", "二级商户预约提现"),

	/**
	 * 提现接口-二级商户查询预约提现状态
	 */
	WITHDRAW_QUERY("/v3/ecommerce/fund/withdraw/%s", "二级商户查询预约提现状态"),

	/**
	 * 提现接口-电商平台预约提现
	 */
	MERCHANT_WITHDRAW("/v3/merchant/fund/withdraw", "电商平台预约提现"),

	/**
	 * 提现接口-电商平台查询预约提现状态
	 */
	MERCHANT_WITHDRAW_QUERY("/v3/merchant/fund/withdraw/withdraw-id/%s", "电商平台查询预约提现状态"),

	/**
	 * 提现接口-商户预约提现单号查询
	 */
	MERCHANT_WITHDRAW_QUERY_BY_OUT_REQUEST_NO("/v3/merchant/fund/withdraw/out-request-no/%s", "商户预约提现单号查询"),

	/**
	 * 提现接口-按日下载提现异常文件
	 */
	WITHDRAW_BILL("/v3/merchant/fund/withdraw/bill-type/%s", "按日下载提现异常文件"),

	/**
	 * 查询订单剩余可出境余额
	 */
	AVAILABLE_ABROAD_AMOUNTS("/v3/funds-to-oversea/transactions/%s/available_abroad_amounts", "查询订单剩余可出境余额"),

	/**
	 * 申请资金出境
	 */
	FUNDS_TO_OVERSEA("/v3/funds-to-oversea/orders", "申请资金出境"),

	/**
	 * 查询出境结果
	 */
	FUNDS_TO_OVERSEA_QUERY("/v3/funds-to-oversea/orders/%s", "查询出境结果"),

	/**
	 * 获取购付汇账单文件下载链接
	 */
	FUNDS_TO_OVERSEA_BILL("/v3/funds-to-oversea/bill-download-url", "获取购付汇账单文件下载链接"),

	/**
	 * 申请二级商户资金账单
	 */
	FUND_FLOW_BILL("/v3/ecommerce/bill/fundflowbill", "申请二级商户资金账单"),
	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	EcommerceApiEnum(String url, String desc) {
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
