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
 * <p>微信支付 v3 接口-连锁品牌分账接口枚举</p>
 *
 * @author Javen
 */
public enum BrandProfitSharingApiEnum implements WxApiEnum {

	/**
	 * 分账/查询分账
	 */
	ORDERS("/v3/brand/profitsharing/orders", "分账/查询分账"),

	/**
	 * 分账回退-查询分账回退
	 */
	RETURN_ORDERS("/v3/brand/profitsharing/returnorders", "分账回退-查询分账回退"),

	/**
	 * 完结分账
	 */
	FINISH_ORDER("/v3/brand/profitsharing/finish-order", "完结分账"),

	/**
	 * 查询订单剩余待分金额
	 */
	QUERY("/v3/brand/profitsharing/orders/%s/amounts", "查询订单剩余待分金额"),

	/**
	 * 查询最大分账比例
	 */
	BRAND__CONFIGS("/v3/brand/profitsharing/brand-configs/%s", "查询最大分账比例"),

	/**
	 * 添加分账接收方
	 */
	RECEIVERS_ADD("/v3/brand/profitsharing/receivers/add", "添加分账接收方"),

	/**
	 * 删除分账接收方
	 */
	RECEIVERS_DELETE("/v3/brand/profitsharing/receivers/delete", "删除分账接收方"),

	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	BrandProfitSharingApiEnum(String url, String desc) {
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
