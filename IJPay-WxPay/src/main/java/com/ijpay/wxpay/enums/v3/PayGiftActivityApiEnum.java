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
 * <p>微信支付 v3 接口-支付有礼接口枚举</p>
 *
 * @author Javen
 */
public enum PayGiftActivityApiEnum implements WxApiEnum {
	/**
	 * 创建全场满额送活动
	 */
	PAY_GIFT_ACTIVITY("/v3/marketing/paygiftactivity/unique-threshold-activity", "创建全场满额送活动"),

	/**
	 * 查询活动详情接口
	 */
	PAY_GIFT_ACTIVITY_INFO("/v3/marketing/paygiftactivity/activities/%s", "查询活动详情接口"),

	/**
	 * 查询活动发券商户号
	 */
	PAY_GIFT_ACTIVITY_QUERY_MERCHANTS("/v3/marketing/paygiftactivity/activities/%s/merchants", "查询活动发券商户号"),

	/**
	 * 查询活动指定商品列表
	 */
	PAY_GIFT_ACTIVITY_QUERY_GOODS("/v3/marketing/paygiftactivity/activities/%s/goods", "查询活动指定商品列表"),

	/**
	 * 终止活动
	 */
	PAY_GIFT_ACTIVITY_TERMINATE("/v3/marketing/paygiftactivity/activities/%s/terminate", "终止活动"),

	/**
	 * 新增活动发券商户号
	 */
	PAY_GIFT_ACTIVITY_ADD_MERCHANTS("/v3/marketing/paygiftactivity/activities/%s/merchants/add", "新增活动发券商户号"),

	/**
	 * 获取支付有礼活动列表
	 */
	PAY_GIFT_ACTIVITY_LIST("/v3/marketing/paygiftactivity/activities", "获取支付有礼活动列表"),

	/**
	 * 删除活动发券商户号
	 */
	PAY_GIFT_ACTIVITY_DELETE_MERCHANTS("/v3/marketing/paygiftactivity/activities/%s/merchants/delete", "删除活动发券商户号"),
	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	PayGiftActivityApiEnum(String url, String desc) {
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
