package com.ijpay.wxpay.model;

import com.ijpay.core.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>报关接口-订单附加信息提交/查询/重推 Model</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
public class CustomDeclareModel extends BaseModel {
	private String sign;
	private String sign_type;
	private String appid;
	private String mch_id;
	private String out_trade_no;
	private String transaction_id;
	private String customs;
	private String mch_customs_no;
	private String duty;
	private String action_type;
	private String sub_order_no;
	private String sub_order_id;
	private String fee_type;
	private String order_fee;
	private String transport_fee;
	private String product_fee;
	private String cert_type;
	private String cert_id;
	private String name;
}
