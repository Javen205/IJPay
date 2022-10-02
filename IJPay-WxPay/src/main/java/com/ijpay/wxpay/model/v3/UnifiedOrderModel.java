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
 * <p>V3 统一下单 Model</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class UnifiedOrderModel {
	/**
	 * 公众号ID
	 */
	private String appid;
	/**
	 * 服务商公众号ID
	 */
	private String sp_appid;
	/**
	 * 直连商户号
	 */
	private String mchid;
	/**
	 * 服务商户号
	 */
	private String sp_mchid;
	/**
	 * 子商户公众号ID
	 */
	private String sub_appid;
	/**
	 * 子商户号
	 */
	private String sub_mchid;
	/**
	 * 商品描述
	 */
	private String description;
	/**
	 * 商户订单号
	 */
	private String out_trade_no;
	/**
	 * 交易结束时间
	 */
	private String time_expire;
	/**
	 * 附加数据
	 */
	private String attach;
	/**
	 * 通知地址
	 */
	private String notify_url;
	/**
	 * 订单优惠标记
	 */
	private String goods_tag;
	/**
	 * 结算信息
	 */
	private SettleInfo settle_info;
	/**
	 * 订单金额
	 */
	private Amount amount;
	/**
	 * 支付者
	 */
	private Payer payer;
	/**
	 * 优惠功能
	 */
	private Detail detail;
	/**
	 * 场景信息
	 */
	private SceneInfo scene_info;
}


