package com.ijpay.wxpay.enums.v2;

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
 * <p>微信支付 v2 版本-现金红包接口枚举</p>
 *
 * @author Javen
 */
public enum RedPackApiEnum implements WxApiEnum {

	/**
	 * 发放红包
	 */
	SEND_RED_PACK("/mmpaymkttransfers/sendredpack", "发放红包"),

	/**
	 * 发放裂变红包
	 */
	SEND_GROUP_RED_PACK("/mmpaymkttransfers/sendgroupredpack", "发放裂变红包"),

	/**
	 * 查询红包记录
	 */
	GET_HB_INFO("/mmpaymkttransfers/gethbinfo", "查询红包记录"),

	/**
	 * 小程序发红包
	 */
	SEND_MINI_PROGRAM_HB("/mmpaymkttransfers/sendminiprogramhb", "小程序发红包"),

	/**
	 * 发放企业红包
	 */
	SEND_WORK_WX_RED_PACK("/mmpaymkttransfers/sendworkwxredpack", "发放企业红包"),

	/**
	 * 查询企业红包记录
	 */
	QUERY_WORK_WX_RED_PACK("/mmpaymkttransfers/queryworkwxredpack", "查询企业红包记录"),

	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	RedPackApiEnum(String url, String desc) {
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
