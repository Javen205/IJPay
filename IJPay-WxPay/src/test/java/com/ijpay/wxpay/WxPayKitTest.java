package com.ijpay.wxpay;


import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.enums.v3.CertAlgorithmTypeEnum;
import com.ijpay.wxpay.enums.v3.OtherApiEnum;
import org.junit.Assert;
import org.junit.Test;

public class WxPayKitTest {

	@Test
	public void hmacSHA256() {
		Assert.assertEquals("8ae6af1a6f6e75f20b8240f320f33e1a376105c5668f1b57a591cd61fe409ee3",
			WxPayKit.hmacSha256("IJPay", "123"));
	}

	@Test
	public void md5() {
		Assert.assertEquals("cbfc2149d454ecf4ab0f32e58430fcdd",
			WxPayKit.md5("IJPay"));
	}

	@Test
	public void encryptData() {
		Assert.assertEquals("K8fdh/6THGfTKio8pxXS6Q==",
			WxPayKit.encryptData("IJPay", "42cc1d91bab89b65ff55b19e28fff4f0"));
	}

	@Test
	public void decryptData() {
		Assert.assertEquals("IJPay",
			WxPayKit.decryptData(
				WxPayKit.encryptData("IJPay", "42cc1d91bab89b65ff55b19e28fff4f0"),
				"42cc1d91bab89b65ff55b19e28fff4f0"));
	}

	@Test
	public void StringFormat() {
		Assert.assertEquals(WxDomainEnum.CHINA.toString().concat(String.format(BasePayApiEnum.ORDER_QUERY_BY_TRANSACTION_ID.toString(), "123456789")),
			String.format(WxDomainEnum.CHINA.toString().concat(BasePayApiEnum.ORDER_QUERY_BY_TRANSACTION_ID.toString()), "123456789"));
	}

	@Test
	public void certAlgorithmTypeEnum() {
		Assert.assertEquals("/v3/certificates?algorithm_type=SM2", CertAlgorithmTypeEnum.getCertSuffixUrl(CertAlgorithmTypeEnum.SM2));
	}

	@Test
	public void certAlgorithmTypeEnumNone() {
		Assert.assertEquals("/v3/certificates", CertAlgorithmTypeEnum.getCertSuffixUrl(CertAlgorithmTypeEnum.NONE));
	}

	@Test
	public void certAlgorithmTypeEnumOther() {
		Assert.assertEquals("/v3/certificates", CertAlgorithmTypeEnum.getCertSuffixUrl("OTHER"));
	}

	@Test
	public void getCertUrl() {
		Assert.assertEquals(WxDomainEnum.CHINA.getDomain() + String.format(OtherApiEnum.GET_CERTIFICATES.getUrl()),
			CertAlgorithmTypeEnum.getCertUrl(WxDomainEnum.CHINA, CertAlgorithmTypeEnum.NONE));
	}

	@Test
	public void getCertUrlBySm2() {
		Assert.assertEquals(WxDomainEnum.CHINA.getDomain() + String.format(OtherApiEnum.GET_CERTIFICATES_BY_ALGORITHM_TYPE.getUrl(), CertAlgorithmTypeEnum.SM2.getCode()),
			CertAlgorithmTypeEnum.getCertUrl(WxDomainEnum.CHINA, CertAlgorithmTypeEnum.SM2));
	}
}
