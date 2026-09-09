package com.ijpay.fuiyou;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、富友支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>富友支付 2.5 支付结果通知-异步通知报文</p>
 *
 * @author Javen
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FuiYouNotify implements Serializable {
	private static final long serialVersionUID = -8935718669519520064L;

	/**
	 * 结果码 0000：成功 其他：失败
	 */
	private String result_code;
	/**
	 * 结果描述
	 */
	private String result_msg;
	/**
	 * 商户号
	 */
	private String mchnt_cd;
	/**
	 * 商户订单号
	 */
	private String mchnt_order_no;
	/**
	 * 应结算订单金额（单位：分）
	 */
	private String settle_order_amt;
	/**
	 * 订单金额（单位：分）
	 */
	private String order_amt;
	/**
	 * 交易完成时间 yyyyMMddHHmmss
	 */
	private String txn_fin_ts;
	/**
	 * 富友交易日期 yyyyMMdd
	 */
	private String reserved_fy_settle_dt;
	/**
	 * 富友订单号
	 */
	private String reserved_fy_trace_no;
	/**
	 * 随机字符串
	 */
	private String random_str;
	/**
	 * 签名
	 */
	private String sign;
	/**
	 * 全签名
	 */
	private String full_sign;
}
