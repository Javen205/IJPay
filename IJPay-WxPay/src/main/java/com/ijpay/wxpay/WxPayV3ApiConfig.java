package com.ijpay.wxpay;

import com.ijpay.core.enums.AuthTypeEnum;
import com.ijpay.wxpay.enums.WxDomainEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.security.PrivateKey;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信支付 APIv3 配置</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class WxPayV3ApiConfig {
	/**
	 * 商户号
	 */
	private String mchId;
	/**
	 * 应用ID
	 */
	private String appId;
	/**
	 * 商户 API 证书序列号
	 */
	private String serialNo;
	/**
	 * 平台证书序列号，接口中包含敏感信息时必传
	 */
	private String platSerialNo;
	/**
	 * 商户 API 私钥 pem 文件路径（与 privateKey 二选一）
	 */
	private String keyPath;
	/**
	 * 商户 API 私钥对象（与 keyPath 二选一）
	 */
	private PrivateKey privateKey;
	/**
	 * 请求域名，默认 {@link WxDomainEnum#CHINA}
	 */
	@Builder.Default
	private String domain = WxDomainEnum.CHINA.toString();
	/**
	 * 认证类型，默认 {@link AuthTypeEnum#RSA}
	 */
	@Builder.Default
	private String authType = AuthTypeEnum.RSA.getCode();
}
