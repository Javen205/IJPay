/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>云闪付-授权码查询 openId</p>
 *
 * @author Javen
 */
package com.ijpay.unionpay.model;

import com.ijpay.core.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AuthCodeToOpenIdModel extends BaseModel {
	private String service;
	private String version;
	private String charset;
	private String sign_type;
	private String mch_id;
	private String sub_appid;
	private String auth_code;
	private String nonce_str;
	private String sign;
	private String sign_agentno;
	private String groupno;
}
