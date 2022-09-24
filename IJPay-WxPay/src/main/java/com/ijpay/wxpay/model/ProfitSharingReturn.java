package com.ijpay.wxpay.model;

import com.ijpay.core.model.BaseModel;
import lombok.*;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>分账回退 Model</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
public class ProfitSharingReturn extends BaseModel {
	private String appid;
	private String sub_appid;
	private String mch_id;
	private String sub_mch_id;
	private String nonce_str;
	private String sign;
	private String sign_type;
	private String order_id;
	private String out_order_no;
	private String out_return_no;
	private String return_account_type;
	private String return_account;
	private String return_amount;
	private String description;
}
