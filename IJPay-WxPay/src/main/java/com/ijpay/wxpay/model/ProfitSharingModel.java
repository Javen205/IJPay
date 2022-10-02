package com.ijpay.wxpay.model;

import com.ijpay.core.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>分账 Model</p>
 *
 * <p>支持: 请求单次分账、请求多次分账、添加分账接收方、删除分账接收方、完结分账</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProfitSharingModel extends BaseModel {
	private String appid;
	private String sub_appid;
	private String mch_id;
	private String sub_mch_id;
	private String nonce_str;
	private String sign;
	private String sign_type;
	private String transaction_id;
	private String out_order_no;
	private String receivers;
	private String receiver;
	private String description;
}
