package com.ijpay.wxpay.enums.xpay;

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
 * <p>微信小程序虚拟支付接口枚举</p>
 *
 * @author Javen
 */
public enum XPayApiEnum implements WxApiEnum {
	/**
	 * 查询用户代币余额
	 */
	QUERY_USER_BALANCE("/xpay/query_user_balance", "查询用户代币余额"),

	/**
	 * 扣减代币(一般用于代币支付)
	 */
	CURRENCY_PAY("/xpay/currency_pay", "扣减代币(一般用于代币支付)"),

	/**
	 * 查询创建的订单(现金单，非代币单)
	 */
	QUERY_ORDER("/xpay/query_order", "查询创建的订单(现金单，非代币单)"),

	/**
	 * 代币支付退款(currency_pay接口的逆操作)
	 */
	CANCEL_CURRENCY_PAY("/xpay/cancel_currency_pay", "代币支付退款(currency_pay接口的逆操作)"),

	/**
	 * 通知已经发货完成(只能通知现金单)
	 */
	NOTIFY_PROVIDE_GOODS("/xpay/notify_provide_goods", "通知已经发货完成(只能通知现金单)"),

	/**
	 * 代币赠送接口
	 */
	PRESENT_CURRENCY("/xpay/present_currency", "代币赠送接口"),

	/**
	 * 下载小程序账单
	 */
	DOWNLOAD_BILL("/xpay/download_bill", "下载小程序账单"),

	/**
	 * 对使用jsapi接口下的单进行退款，此接口只是启动退款任务成功，启动后需要调用query_order接口来查询退款单状态，等状态变成退款完成后即为最终成功
	 */
	REFUND_ORDER("/xpay/refund_order", "退款"),

	/**
	 * 创建提现单
	 */
	CREATE_WITHDRAW_ORDER("/xpay/create_withdraw_order", "创建提现单"),

	/**
	 * 查询提现单
	 */
	QUERY_WITHDRAW_ORDER("/xpay/query_withdraw_order", "查询提现单"),

	/**
	 * 启动批量上传道具任务
	 */
	START_UPLOAD_GOODS("/xpay/start_upload_goods", "启动批量上传道具任务"),

	/**
	 * 查询批量上传道具任务
	 */
	QUERY_UPLOAD_GOODS("/xpay/query_upload_goods", "查询批量上传道具任务"),

	/**
	 * 启动批量发布道具任务
	 */
	START_PUBLISH_GOODS("/xpay/start_publish_goods", "启动批量发布道具任务"),

	/**
	 * 查询批量发布道具任务
	 */
	QUERY_PUBLISH_GOODS("/xpay/query_publish_goods", "查询批量发布道具任务"),

	/**
	 * 查询商家账户里的可提现余额
	 */
	query_biz_balance("/xpay/query_biz_balance", "查询商家账户里的可提现余额"),

	/**
	 * 查询广告金充值账户
	 */
	QUERY_TRANSFER_ACCOUNT("/xpay/query_transfer_account", "查询广告金充值账户"),

	/**
	 * 查询广告金发放记录
	 */
	QUERY_ADVER_FUNDS("/xpay/query_adver_funds", "查询广告金发放记录"),

	/**
	 * 充值广告金
	 */
	CREATE_FUNDS_BILL("/xpay/create_funds_bill", "充值广告金"),
	/**
	 * 绑定广告金充值账户
	 */
	BIND_TRANSFER_ACCOUNT("/xpay/bind_transfer_accout", "绑定广告金充值账户"),
	;


	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	XPayApiEnum(String url, String desc) {
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
		return this.url;
	}

	/**
	 * 获取详细的描述信息
	 *
	 * @return 描述信息
	 */
	@Override
	public String getDesc() {
		return this.desc;
	}
}
