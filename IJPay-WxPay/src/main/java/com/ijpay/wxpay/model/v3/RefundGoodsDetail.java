package com.ijpay.wxpay.model.v3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>V3 微信申请退款-退款商品</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class RefundGoodsDetail {
	/**
	 * 商户侧商品编码
	 */
	private String merchant_goods_id;
	/**
	 * 微信侧商品编码
	 */
	private String wechatpay_goods_id;
	/**
	 * 商品名称
	 */
	private String goods_name;
	/**
	 * 商品单价
	 */
	private int unit_price;
	/**
	 * 商品退款金额
	 */
	private int refund_amount;
	/**
	 * 商品退货数量
	 */
	private int refund_quantity;
}
