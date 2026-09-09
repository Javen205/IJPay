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
 * <p>微信支付 v3 接口-品牌商户相关接口枚举（品牌入驻、商家名片、商家名片会员、品牌门店）</p>
 *
 * @author Javen
 */
public enum BrandApiEnum implements WxApiEnum {

	/**
	 * 品牌入驻-提交入驻申请
	 */
	BRAND_APPLYMENTS("/v3/brand/applyments", "提交入驻申请"),

	/**
	 * 品牌入驻-根据申请单ID查询申请状态
	 */
	BRAND_APPLYMENT_QUERY_BY_ID("/v3/brand/applyments/applyment-id/%s", "根据申请单ID查询申请状态"),

	/**
	 * 品牌入驻-根据业务申请编号查询申请状态
	 */
	BRAND_APPLYMENT_QUERY_BY_BUSINESS_CODE("/v3/brand/applyments/business-code/%s", "根据业务申请编号查询申请状态"),

	/**
	 * 品牌入驻-撤销申请
	 */
	BRAND_APPLYMENT_CANCEL("/v3/brand/applyments/cancel-applyment", "撤销申请"),

	/**
	 * 品牌服务商-获得品牌已授权且在生效中的产品权限信息
	 */
	BRAND_PRODUCT_AUTHORITIES("/v3/brand-basic/partner/brands/%s/product-authorities", "获得品牌已授权且在生效中的产品权限信息"),

	/**
	 * 商家名片-提交商家名片配置申请
	 */
	CARD_CONFIGS("/v3/brand/card/card-configs", "提交商家名片配置申请"),

	/**
	 * 商家名片-查询商家名片配置申请状态
	 */
	CARD_CONFIGS_QUERY("/v3/brand/card/card-configs", "查询商家名片配置申请状态"),

	/**
	 * 商家名片-撤销商家名片配置申请
	 */
	CARD_CONFIGS_CANCEL("/v3/brand/card/card-configs/cancel-applyment", "撤销商家名片配置申请"),

	/**
	 * 商家名片-获取商家名片预览二维码
	 */
	CARD_CONFIGS_PREVIEW_URL("/v3/brand/card/card-configs/preview-url", "获取商家名片预览二维码"),

	/**
	 * 商家名片-发布商家名片配置
	 */
	CARD_CONFIGS_PUBLISH("/v3/brand/card/card-configs/publish", "发布商家名片配置"),

	/**
	 * 商家名片-创建交易连接名片场景
	 */
	CARD_LINKS("/v3/brand/card/card-links", "创建交易连接名片场景"),

	/**
	 * 商家名片-查询已生效交易连接名片规则
	 */
	CARD_LINKS_QUERY("/v3/brand/card/card-links", "查询已生效交易连接名片规则"),

	/**
	 * 商家名片-根据业务申请编号查询添加申请状态
	 */
	CARD_LINKS_QUERY_BY_BUSINESS_CODE("/v3/brand/card/card-links/business-code/%s", "根据业务申请编号查询添加申请状态"),

	/**
	 * 商家名片-撤销交易连接名片配置申请
	 */
	CARD_LINKS_CANCEL("/v3/brand/card/card-links/cancel-applyment", "撤销交易连接名片配置申请"),

	/**
	 * 商家名片-解除已生效交易连接名片场景
	 */
	CARD_LINKS_UNBIND("/v3/brand/card/card-links/unbind-card-link", "解除已生效交易连接名片场景"),

	/**
	 * 商家名片会员-创建会员卡模板
	 */
	MEMBER_CARD_TEMPLATE("/v3/brand/partner/card-member/cards", "创建会员卡模板"),

	/**
	 * 商家名片会员-查询会员卡模板列表
	 */
	MEMBER_CARD_TEMPLATE_QUERY("/v3/brand/partner/card-member/cards", "查询会员卡模板列表"),

	/**
	 * 商家名片会员-查询会员卡模板详情
	 */
	MEMBER_CARD_TEMPLATE_QUERY_DETAIL("/v3/brand/partner/card-member/cards/%s", "查询会员卡模板详情"),

	/**
	 * 商家名片会员-作废会员卡模板
	 */
	MEMBER_CARD_TEMPLATE_INVALIDATE("/v3/brand/partner/card-member/cards/%s/invalidate", "作废会员卡模板"),

	/**
	 * 商家名片会员-品牌会员入会组件预授权
	 */
	MEMBER_PRE_AUTH_TOKENS("/v3/brand/partner/card-member/pre-auth-tokens", "品牌会员入会组件预授权"),

	/**
	 * 商家名片会员-查询用户在品牌下所有会员卡
	 */
	MEMBER_USER_CARDS_QUERY("/v3/brand/partner/card-member/user-cards", "查询用户在品牌下所有会员卡"),

	/**
	 * 商家名片会员-根据OPENID导入用户会员卡
	 */
	MEMBER_USER_CARDS_IMPORT_BY_OPENID("/v3/brand/partner/card-member/user-cards/import-by-openid", "根据OPENID导入用户会员卡"),

	/**
	 * 商家名片会员-查询用户会员卡详情
	 */
	MEMBER_USER_CARDS_QUERY_DETAIL("/v3/brand/partner/card-member/user-cards/%s", "查询用户会员卡详情"),

	/**
	 * 商家名片会员-同步会员开通结果
	 */
	MEMBER_USER_CARDS_CONFIRM("/v3/brand/partner/card-member/user-cards/%s/confirm", "同步会员开通结果"),

	/**
	 * 商家名片会员-作废用户会员卡
	 */
	MEMBER_USER_CARDS_INVALIDATE("/v3/brand/partner/card-member/user-cards/%s/invalidate", "作废用户会员卡"),

	/**
	 * 品牌门店-创建品牌门店
	 */
	BRAND_STORES("/v3/brand/partner/store/brandstores", "创建品牌门店"),

	/**
	 * 品牌门店-查询品牌门店
	 */
	BRAND_STORES_QUERY("/v3/brand/partner/store/brandstores/%s", "查询品牌门店"),

	/**
	 * 品牌门店-绑定收款商户号
	 */
	BRAND_STORES_BIND_RECIPIENT("/v3/brand/partner/store/brandstores/%s/bindrecipient", "绑定收款商户号"),

	/**
	 * 品牌门店-暂停门店营业
	 */
	BRAND_STORES_CLOSE("/v3/brand/partner/store/brandstores/%s/close", "暂停门店营业"),

	/**
	 * 品牌门店-恢复门店营业
	 */
	BRAND_STORES_RESUME("/v3/brand/partner/store/brandstores/%s/resume", "恢复门店营业"),

	/**
	 * 品牌门店-解绑收款商户号
	 */
	BRAND_STORES_UNBIND_RECIPIENT("/v3/brand/partner/store/brandstores/%s/unbindrecipient", "解绑收款商户号"),
	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	BrandApiEnum(String url, String desc) {
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
