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
 * <p>微信支付 v3 版本接口-电子发票接口枚举</p>
 *
 * @author Javen
 */
public enum InvoiceApiEnum implements WxApiEnum {

	/**
	 * 创建电子发票卡券模板
	 */
	CARD_TEMPLATE("/v3/new-tax-control-fapiao/card-template", "创建电子发票卡券模板"),

	/**
	 * 开具电子发票
	 */
	INVOICING("/v3/new-tax-control-fapiao/fapiao-applications", "开具电子发票"),

	/**
	 * 上传电子发票文件
	 */
	UPDATE_INVOICE_FILE("/v3/new-tax-control-fapiao/fapiao-applications/upload-fapiao-file", "上传电子发票文件"),

	/**
	 * 查询电子发票
	 */
	QUERY_INVOICE("/v3/new-tax-control-fapiao/fapiao-applications/%s", "查询电子发票"),

	/**
	 * 获取发票下载信息
	 */
	QUERY_INVOICE_DOWNLOAD_INFO("/v3/new-tax-control-fapiao/fapiao-applications/%s/fapiao-files", "获取发票下载信息"),

	/**
	 * 将电子发票插入微信用户卡包
	 */
	INSERT_CARDS("/v3/new-tax-control-fapiao/fapiao-applications/%s/insert-cards", "将电子发票插入微信用户卡包"),

	/**
	 * 冲红电子发票
	 */
	REVERSE("/v3/new-tax-control-fapiao/fapiao-applications/%s/reverse", "冲红电子发票"),

	/**
	 * 获取商户开票基础信息
	 */
	MERCHANT_BASE_INFO("/v3/new-tax-control-fapiao/merchant/base-information", "获取商户开票基础信息"),

	/**
	 * 查询商户配置的开发选项/配置开发选项
	 */
	MERCHANT_DEVELOPMENT_CONFIG("/v3/new-tax-control-fapiao/merchant/development-config", "查询商户配置的开发选项/配置开发选项"),

	/**
	 * 获取商户可开具的商品和服务税收分类编码对照表
	 */
	MERCHANT_TAX_CODES("/v3/new-tax-control-fapiao/merchant/tax-codes", "获取商户可开具的商品和服务税收分类编码对照表"),

	/**
	 * 获取用户填写的抬头
	 */
	USER_TITLE("/v3/new-tax-control-fapiao/user-title", "获取用户填写的抬头"),

	/**
	 * 获取抬头填写链接
	 */
	USER_TITLE_URL("/v3/new-tax-control-fapiao/user-title/title-url", "获取抬头填写链接"),

	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	InvoiceApiEnum(String url, String desc) {
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
