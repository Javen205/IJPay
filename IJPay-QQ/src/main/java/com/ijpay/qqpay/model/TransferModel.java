/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>企业付款到余额 Model</p>
 *
 * @author Javen
 */
package com.ijpay.qqpay.model;

import com.ijpay.core.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class TransferModel extends BaseModel {
	private String input_charset;
	private String appid;
	private String openid;
	private String uin;
	private String mch_id;
	private String nonce_str;
	private String sign;
	private String out_trade_no;
	private String fee_type;
	private String total_fee;
	private String memo;
	private String check_name;
	private String re_user_name;
	private String check_real_name;
	private String op_user_id;
	private String op_user_passwd;
	private String spbill_create_ip;
	private String notify_url;
}
