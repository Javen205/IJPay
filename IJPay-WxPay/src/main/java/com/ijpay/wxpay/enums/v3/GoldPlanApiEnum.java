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
 * <p>微信支付 v3 接口-点金计划接口枚举</p>
 *
 * @author Javen
 */
public enum GoldPlanApiEnum implements WxApiEnum {

	/**
	 * 点金计划管理
	 */
	CHANGE_GOLD_PLAN_STATUS("/v3/goldplan/merchants/changegoldplanstatus", "点金计划管理"),

	/**
	 * 商家小票管理
	 */
	CHANGE_CUSTOM_PAGE_STATUS("/v3/goldplan/merchants/changecustompagestatus", "商家小票管理"),

	/**
	 * 商家小票管理
	 */
	SET_ADVERTISING_INDUSTRY_FILTER("/v3/goldplan/merchants/set-advertising-industry-filter", "商家小票管理"),

	/**
	 * 开通广告展示
	 */
	OPEN_ADVERTISING_SHOW("/v3/goldplan/merchants/open-advertising-show", "开通广告展示"),

	/**
	 * 关闭广告展示
	 */
	CLOSE_ADVERTISING_SHOW("/v3/goldplan/merchants/close-advertising-show", "关闭广告展示"),

	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	GoldPlanApiEnum(String url, String desc) {
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
