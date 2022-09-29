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
 * <p>微信支付 v2 版本-支付相关接口枚举</p>
 *
 * @author Javen
 */
public enum PayApiEnum implements WxApiEnum {

	/**
	 * 沙箱环境
	 */
	SAND_BOX_NEW("/sandboxnew", "沙箱环境"),

	/**
	 * V2 版本沙箱环境
	 */
	API_V2_SANDBOX("/xdc/apiv2sandbox", "V2 版本沙箱环境"),

	/**
	 * 获取沙箱环境验签秘钥
	 */
	GET_SIGN_KEY("/xdc/apiv2getsignkey/sign/getsignkey", "获取沙箱环境验签秘钥"),

	/**
	 * 统一下单
	 */
	UNIFIED_ORDER("/pay/unifiedorder", "统一下单"),

	/**
	 * 付款码支付
	 */
	MICRO_PAY("/pay/micropay", "付款码支付"),

	/**
	 * 查询订单
	 */
	ORDER_QUERY("/pay/orderquery", "查询订单"),

	/**
	 * 关闭订单
	 */
	CLOSE_ORDER("/pay/closeorder", "关闭订单"),

	/**
	 * 撤销订单
	 */
	REVERSE("/secapi/pay/reverse", "撤销订单"),

	/**
	 * 申请退款
	 */
	REFUND("/secapi/pay/refund", "申请退款"),

	/**
	 * 单品优惠-申请退款 <a href="https://pay.weixin.qq.com/wiki/doc/api/danpin.php?chapter=9_103&index=3">官方文档</a>
	 */
	REFUND_V2("/secapi/pay/refundv2", "单品优惠-申请退款"),

	/**
	 * 查询退款
	 */
	REFUND_QUERY("/pay/refundquery", "查询退款"),

	/**
	 * 单品优惠-查询退款
	 */
	REFUND_QUERY_V2("/pay/refundqueryv2", "单品优惠-查询退款"),

	/**
	 * 下载对账单
	 */
	DOWNLOAD_BILL("/pay/downloadbill", "下载对账单"),

	/**
	 * 下载资金对账单
	 */
	DOWNLOAD_FUND_FLOW("/pay/downloadfundflow", "下载资金对账单"),

	/**
	 * 交易保障
	 */
	REPORT("/payitil/report", "交易保障"),

	/**
	 * 付款码查询openid
	 */
	AUTH_CODE_TO_OPENID("/tools/authcodetoopenid", "付款码查询openid"),

	/**
	 * 转换短链接
	 */
	SHORT_URL("/tools/shorturl", "转换短链接"),
	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	PayApiEnum(String url, String desc) {
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
