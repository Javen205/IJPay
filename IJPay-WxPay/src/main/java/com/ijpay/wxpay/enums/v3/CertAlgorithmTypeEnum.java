package com.ijpay.wxpay.enums.v3;

import com.ijpay.wxpay.enums.WxDomainEnum;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>获取微信平台证书算法类型枚举</p>
 *
 * @author Javen
 */
public enum CertAlgorithmTypeEnum {
	/**
	 * 国密算法的平台证书
	 */
	SM2("SM2", "国密算法的平台证书"),
	/**
	 * RSA算法平台证书
	 */
	RSA("RSA", "RSA算法平台证书"),
	/**
	 * 所有类型的平台证书
	 */
	ALL("ALL", "所有类型的平台证书"),
	/**
	 * 默认RSA算法的平台证书
	 */
	NONE("NONE", "默认RSA算法的平台证书"),
	;

	/**
	 * 类型
	 */
	private final String code;

	/**
	 * 名称
	 */
	private final String name;

	CertAlgorithmTypeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * 获取证书地址
	 *
	 * @param domainEnum 域名枚举
	 * @param typeEnum   算法类型枚举
	 * @return 返回结果
	 */
	public static String getCertUrl(WxDomainEnum domainEnum, CertAlgorithmTypeEnum typeEnum) {
		return domainEnum.getDomain().concat(getCertSuffixUrl(typeEnum));
	}

	/**
	 * 获取证书地址后缀
	 *
	 * @param code 算法类型
	 * @return 返回结果
	 */
	public static String getCertSuffixUrl(String code) {
		for (CertAlgorithmTypeEnum value : CertAlgorithmTypeEnum.values()) {
			if (value.getCode().equals(code) && value != CertAlgorithmTypeEnum.NONE) {
				return getCertSuffixUrl(value);
			}
		}
		return OtherApiEnum.GET_CERTIFICATES.getUrl();
	}

	/**
	 * 获取证书地址后缀
	 *
	 * @param typeEnum 算法类型
	 * @return 返回结果
	 */
	public static String getCertSuffixUrl(CertAlgorithmTypeEnum typeEnum) {
		if (typeEnum != CertAlgorithmTypeEnum.NONE) {
			return String.format(OtherApiEnum.GET_CERTIFICATES_BY_ALGORITHM_TYPE.getUrl(), typeEnum.getCode());
		}
		return OtherApiEnum.GET_CERTIFICATES.getUrl();
	}

}
