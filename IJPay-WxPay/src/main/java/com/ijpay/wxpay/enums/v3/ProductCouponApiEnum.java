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
 * <p>微信支付 v3 接口-商品券接口枚举</p>
 *
 * @author Javen
 */
public enum ProductCouponApiEnum implements WxApiEnum {

	/**
	 * 提交图片生成任务
	 */
	IMAGE_GENERATION_TASKS("/v3/marketing/partner/product-coupon/image-generation-tasks", "提交图片生成任务"),

	/**
	 * 查询图片生成任务执行结果
	 */
	IMAGE_GENERATION_TASKS_QUERY("/v3/marketing/partner/product-coupon/image-generation-tasks/%s", "查询图片生成任务执行结果"),

	/**
	 * 图片上传
	 */
	MEDIA_UPLOAD_IMAGE("/v3/marketing/partner/product-coupon/media/upload-image", "图片上传"),

	/**
	 * 获取商品券事件通知地址
	 */
	NOTIFY_CONFIGS("/v3/marketing/partner/product-coupon/notify-configs", "设置商品券事件通知地址"),

	/**
	 * 创建商品券
	 */
	PRODUCT_COUPONS("/v3/marketing/partner/product-coupon/product-coupons", "创建商品券"),

	/**
	 * 修改商品券
	 */
	PRODUCT_COUPONS_UPDATE("/v3/marketing/partner/product-coupon/product-coupons/%s", "修改商品券"),

	/**
	 * 失效商品券
	 */
	PRODUCT_COUPONS_DEACTIVATE("/v3/marketing/partner/product-coupon/product-coupons/%s/deactivate", "失效商品券"),

	/**
	 * 添加商品券批次组
	 */
	STOCK_BUNDLES("/v3/marketing/partner/product-coupon/product-coupons/%s/stock-bundles", "添加商品券批次组"),

	/**
	 * 修改商品券批次组
	 */
	STOCK_BUNDLES_UPDATE("/v3/marketing/partner/product-coupon/product-coupons/%s/stock-bundles/%s", "修改商品券批次组"),

	/**
	 * 批次组批量关联门店
	 */
	STOCK_BUNDLES_ASSOCIATE_STORES("/v3/marketing/partner/product-coupon/product-coupons/%s/stock-bundles/%s/associate-stores", "批次组批量关联门店"),

	/**
	 * 批次组批量取消关联门店
	 */
	STOCK_BUNDLES_DISASSOCIATE_STORES("/v3/marketing/partner/product-coupon/product-coupons/%s/stock-bundles/%s/disassociate-stores", "批次组批量取消关联门店"),

	/**
	 * 修改商品券批次组发放预算
	 */
	STOCK_BUNDLES_UPDATE_BUDGET("/v3/marketing/partner/product-coupon/product-coupons/%s/stock-bundles/%s/update-budget", "修改商品券批次组发放预算"),

	/**
	 * 查询商品券批次列表
	 */
	STOCKS("/v3/marketing/partner/product-coupon/product-coupons/%s/stocks", "添加商品券批次"),

	/**
	 * 修改商品券批次
	 */
	STOCKS_QUERY("/v3/marketing/partner/product-coupon/product-coupons/%s/stocks/%s", "查询商品券指定批次"),

	/**
	 * 批次关联门店
	 */
	STOCKS_ASSOCIATE_STORES("/v3/marketing/partner/product-coupon/product-coupons/%s/stocks/%s/associate-stores", "批次关联门店"),

	/**
	 * 查询批次关联门店列表
	 */
	STOCKS_ASSOCIATED_STORES("/v3/marketing/partner/product-coupon/product-coupons/%s/stocks/%s/associated-stores", "查询批次关联门店列表"),

	/**
	 * 失效商品券批次
	 */
	STOCKS_DEACTIVATE("/v3/marketing/partner/product-coupon/product-coupons/%s/stocks/%s/deactivate", "失效商品券批次"),

	/**
	 * 批次取消关联门店
	 */
	STOCKS_DISASSOCIATE_STORES("/v3/marketing/partner/product-coupon/product-coupons/%s/stocks/%s/disassociate-stores", "批次取消关联门店"),

	/**
	 * 修改商品券批次发放预算
	 */
	STOCKS_UPDATE_BUDGET("/v3/marketing/partner/product-coupon/product-coupons/%s/stocks/%s/update-budget", "修改商品券批次发放预算"),

	/**
	 * 预上传券Code
	 */
	STOCKS_UPLOAD_COUPON_CODES("/v3/marketing/partner/product-coupon/product-coupons/%s/stocks/%s/upload-coupon-codes", "预上传券Code"),

	/**
	 * 向用户发放商品券批次组
	 */
	USER_COUPON_BUNDLES("/v3/marketing/partner/product-coupon/users/%s/coupon-bundles", "向用户发放商品券批次组"),

	/**
	 * 失效用户商品券组
	 */
	USER_COUPON_BUNDLES_DEACTIVATE("/v3/marketing/partner/product-coupon/users/%s/coupon-bundles/%s/deactivate", "失效用户商品券组"),

	/**
	 * 指定券状态查询用户商品券列表
	 */
	USER_COUPONS_QUERY("/v3/marketing/partner/product-coupon/users/%s/coupons", "指定券状态查询用户商品券列表"),

	/**
	 * 查询用户商品券详情
	 */
	USER_COUPONS_QUERY_DETAIL("/v3/marketing/partner/product-coupon/users/%s/coupons/%s", "查询用户商品券详情"),

	/**
	 * 确认发放用户商品券
	 */
	USER_COUPONS_CONFIRM("/v3/marketing/partner/product-coupon/users/%s/coupons/%s/confirm", "确认发放用户商品券"),

	/**
	 * 失效用户商品券
	 */
	USER_COUPONS_DEACTIVATE("/v3/marketing/partner/product-coupon/users/%s/coupons/%s/deactivate", "失效用户商品券"),

	/**
	 * 退券
	 */
	USER_COUPONS_RETURN("/v3/marketing/partner/product-coupon/users/%s/coupons/%s/return", "退券"),

	/**
	 * 核销用户商品券
	 */
	USER_COUPONS_USE("/v3/marketing/partner/product-coupon/users/%s/coupons/%s/use", "核销用户商品券"),

	/**
	 * 向用户预发放商品券
	 */
	USER_PRE_SEND_COUPON("/v3/marketing/partner/product-coupon/users/%s/pre-send-coupon", "向用户预发放商品券"),

	/**
	 * 向用户预发放商品券批次组
	 */
	USER_PRE_SEND_COUPON_BUNDLE("/v3/marketing/partner/product-coupon/users/%s/pre-send-coupon-bundle", "向用户预发放商品券批次组"),
	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	ProductCouponApiEnum(String url, String desc) {
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
