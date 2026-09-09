package com.ijpay.demo.entity;

import com.ijpay.fuiyou.enums.FuiYouDomainEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、富友支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>富友支付配置 Bean</p>
 *
 * @author Javen
 */
@Component
@PropertySource("classpath:/fuiyou.properties")
@ConfigurationProperties(prefix = "fuiyou")
public class FuiYouPayBean {
	private String mchntCd;
	private String mchntKey;
	/**
	 * 接口环境：test 测试环境 / prod 生产环境，也可直接配置完整域名
	 */
	private String domain;

	public String getMchntCd() {
		return mchntCd;
	}

	public void setMchntCd(String mchntCd) {
		this.mchntCd = mchntCd;
	}

	public String getMchntKey() {
		return mchntKey;
	}

	public void setMchntKey(String mchntKey) {
		this.mchntKey = mchntKey;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * 根据配置切换测试/生产环境
	 *
	 * @return {@link FuiYouDomainEnum} 接口域名枚举
	 */
	public FuiYouDomainEnum getDomainEnum() {
		if ("prod".equalsIgnoreCase(domain)) {
			return FuiYouDomainEnum.PROD;
		}
		return FuiYouDomainEnum.TEST;
	}

	@Override
	public String toString() {
		return "FuiYouPayBean [mchntCd=" + mchntCd + ", mchntKey=" + mchntKey + ", domain=" + domain + "]";
	}
}
