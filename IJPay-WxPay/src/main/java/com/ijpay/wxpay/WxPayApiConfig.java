package com.ijpay.wxpay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信支付常用配置</p>
 *
 * @author Javen
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WxPayApiConfig implements Serializable {
	private static final long serialVersionUID = -9044503427692786302L;
	/**
	 * 应用编号
	 */
	private String appId;
	/**
	 * 商户号
	 */
	private String mchId;
	/**
	 * 服务商应用编号
	 */
	private String slAppId;
	/**
	 * 服务商商户号
	 */
	private String slMchId;
	/**
	 * 同 apiKey 后续版本会舍弃
	 */
	private String partnerKey;
	/**
	 * 商户平台「API安全」中的 API 密钥
	 */
	private String apiKey;
	/**
	 * 商户平台「API安全」中的 APIv3 密钥
	 */
	private String apiKey3;
	/**
	 * 应用域名，回调中会使用此参数
	 */
	private String domain;
	/**
	 * API 证书中的 p12
	 */
	private String certP12Path;
	/**
	 * API 证书中的 key.pem
	 */
	private String keyPath;
	/**
	 * API 证书中的 cert.pem
	 */
	private String certPath;
	/**
	 * 微信平台证书
	 */
	private String platformCertPath;
	/**
	 * 其他附加参数
	 */
	private Object exParams;
}
