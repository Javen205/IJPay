package com.ijpay.core.model;

import java.io.Serializable;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>证书详细信息 Model</p>
 *
 * @author Javen
 */
public class CertificateModel implements Serializable {

	private static final long serialVersionUID = -3066491891078735673L;

	/**
	 * 证书本身
	 */
	private X509Certificate itself;

	/**
	 * 版本号
	 */
	private int version;

	/**
	 * 证书序列号
	 */
	private String serialNumber;

	/**
	 * 签发者
	 */
	private Principal issuerDn;

	/**
	 * 主体名
	 */
	private Principal subjectDn;

	/**
	 * 有效起始日期
	 */
	private Date notBefore;

	/**
	 * 有效终止日期
	 */
	private Date notAfter;


	public X509Certificate getItself() {
		return itself;
	}

	public void setItself(X509Certificate itself) {
		this.itself = itself;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Principal getIssuerDn() {
		return issuerDn;
	}

	public void setIssuerDn(Principal issuerDn) {
		this.issuerDn = issuerDn;
	}

	public Principal getSubjectDn() {
		return subjectDn;
	}

	public void setSubjectDn(Principal subjectDn) {
		this.subjectDn = subjectDn;
	}

	public Date getNotBefore() {
		return notBefore;
	}

	public void setNotBefore(Date notBefore) {
		this.notBefore = notBefore;
	}

	public Date getNotAfter() {
		return notAfter;
	}

	public void setNotAfter(Date notAfter) {
		this.notAfter = notAfter;
	}

	@Override
	public String toString() {
		return "CertificateModel{" +
			"version='" + version + '\'' +
			", serialNumber='" + serialNumber + '\'' +
			", issuerDn=" + issuerDn +
			", subjectDn=" + subjectDn +
			", notBefore=" + notBefore +
			", notAfter=" + notAfter +
			'}';
	}
}
