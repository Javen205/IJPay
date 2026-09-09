package com.ijpay.fuiyou.enums;

import lombok.Getter;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、富友支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>富友支付交易状态枚举</p>
 *
 * @author Javen
 */
@Getter
public enum FuiYouTransStat {

	/**
	 * 交易成功
	 */
	SUCCESS("SUCCESS", "交易成功"),
	/**
	 * 交易退款
	 */
	REFUND("REFUND", "交易退款"),
	/**
	 * 未支付
	 */
	NOT_PAY("NOTPAY", "未支付"),
	/**
	 * 已关闭
	 */
	CLOSED("CLOSED", "已关闭"),
	/**
	 * 已撤销
	 */
	REVOKED("REVOKED", "已撤销"),
	/**
	 * 用户支付中
	 */
	USER_PAYING("USERPAYING", "用户支付中"),
	/**
	 * 支付失败
	 */
	PAY_ERROR("PAYERROR", "支付失败");

	/**
	 * 交易状态值
	 */
	private final String stat;
	/**
	 * 描述
	 */
	private final String desc;

	FuiYouTransStat(String stat, String desc) {
		this.stat = stat;
		this.desc = desc;
	}

	@Override
	public String toString() {
		return stat;
	}
}
