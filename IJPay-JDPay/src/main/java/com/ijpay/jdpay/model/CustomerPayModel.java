package com.ijpay.jdpay.model;

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
 * <p>商户二维码支付接口 Model</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CustomerPayModel extends JdBaseModel {
	private String version;
	private String sign;
	private String merchant;
	private String payMerchant;
	private String device;
	private String tradeNum;
	private String tradeName;
	private String tradeDesc;
	private String tradeTime;
	private String amount;
	private String orderType;
	private String industryCategoryCode;
	private String currency;
	private String note;
	private String callbackUrl;
	private String notifyUrl;
	private String ip;
	private String expireTime;
	private String riskInfo;
	private String goodsInfo;
	private String bizTp;
}
