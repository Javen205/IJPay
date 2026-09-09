package com.ijpay.fuiyou;

import com.ijpay.fuiyou.enums.FuiYouDomain;
import com.ijpay.fuiyou.enums.FuiYouDomainEnum;
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
 * <p>富友支付常用配置</p>
 *
 * @author Javen
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FuiYouPayApiConfig implements Serializable {
	private static final long serialVersionUID = -9044503427692786302L;
	/**
	 * 商户号
	 */
	private String mchntCd;
	/**
	 * 商户密钥
	 */
	private String mchntKey;
	/**
	 * 接口域名，可通过 {@link FuiYouDomainEnum} 选择测试/生产环境，也可自定义
	 */
	private FuiYouDomain domain;
	/**
	 * 其他附加参数
	 */
	private Object exParams;

	/**
	 * 获取接口域名
	 *
	 * @return {@link String} 域名
	 */
	public String getDomainUrl() {
		if (domain == null) {
			return FuiYouDomainEnum.PROD.getDomain();
		}
		return domain.getDomain();
	}
}
