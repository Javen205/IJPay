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
 * <p>微信支付 v3 接口-品牌营销投放计划（摇一摇有优惠）接口枚举</p>
 *
 * @author Javen
 */
public enum DeliveryPlanApiEnum implements WxApiEnum {

	/**
	 * 设置投放计划回调地址
	 */
	NOTIFY_URL("/v3/marketing/partner/delivery-plan/%s/notify-url", "设置投放计划回调地址"),

	/**
	 * 创建投放计划
	 */
	DELIVERY_PLANS("/v3/marketing/partner/delivery-plan/delivery-plans", "创建投放计划"),

	/**
	 * 更新投放计划
	 */
	DELIVERY_PLANS_UPDATE("/v3/marketing/partner/delivery-plan/delivery-plans/%s", "更新投放计划"),

	/**
	 * 分页查询投放计划列表
	 */
	DELIVERY_PLANS_QUERY("/v3/marketing/partner/delivery-plan/delivery-plans/%s/delivery-plans", "分页查询投放计划列表"),

	/**
	 * 终止投放计划
	 */
	DELIVERY_PLANS_TERMINATE("/v3/marketing/partner/delivery-plan/delivery-plans/%s/terminate", "终止投放计划"),
	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	DeliveryPlanApiEnum(String url, String desc) {
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
