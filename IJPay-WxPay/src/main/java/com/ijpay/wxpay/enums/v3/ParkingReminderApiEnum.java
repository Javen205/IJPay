package com.ijpay.wxpay.enums.v3;

import com.ijpay.wxpay.enums.WxApiEnum;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 * <p>IJPay 自由交流群: 864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信支付 v3 接口-停车缴费服务接口枚举（服务商/平台商户）</p>
 *
 * @author Javen
 */
public enum ParkingReminderApiEnum implements WxApiEnum {

	/**
	 * 停车缴费-提交停车场进件申请
	 */
	PARKING_APPLICATION("/v3/parking/reminders/application", "提交停车场进件申请"),

	/**
	 * 停车缴费-查询停车场进件申请单
	 */
	PARKING_APPLICATION_QUERY("/v3/parking/reminders/application/query", "查询停车场进件申请单"),

	/**
	 * 停车缴费-撤回停车场进件申请
	 */
	PARKING_APPLICATION_WITHDRAW("/v3/parking/reminders/application/withdraw", "撤回停车场进件申请"),

	/**
	 * 停车缴费-查询停车场进件申请单列表
	 */
	PARKING_APPLICATIONS("/v3/parking/reminders/applications", "查询停车场进件申请单列表"),

	/**
	 * 停车缴费-同步车辆入场通知
	 */
	PARKING_ENTRY("/v3/parking/reminders/entry", "同步车辆入场通知"),

	/**
	 * 停车缴费-同步车辆离场通知
	 */
	PARKING_EXIT("/v3/parking/reminders/exit", "同步车辆离场通知"),

	/**
	 * 停车缴费-同步非临停扩展支付
	 */
	PARKING_EXT_PAYMENT("/v3/parking/reminders/ext-payment", "同步非临停扩展支付"),

	/**
	 * 停车缴费-查询停车费用
	 */
	PARKING_FEE("/v3/parking/reminders/parking-fee", "查询停车费用"),

	/**
	 * 停车缴费-查询停车场信息
	 */
	PARKING_LOT("/v3/parking/reminders/parking-lot", "查询停车场信息"),

	/**
	 * 停车缴费-同步支付结果通知
	 */
	PARKING_PAYMENT("/v3/parking/reminders/payment", "同步支付结果通知"),
	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	ParkingReminderApiEnum(String url, String desc) {
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
