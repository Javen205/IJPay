package com.ijpay.jdpay.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;


public class VerifySignatureUtl {

	public static String encryptMerchant(String sourceSignString, String rsaPriKey) {
		String result = "";

		try {
			String sha256SourceSignString = SHAUtil.Encrypt(sourceSignString, null);

			byte[] newsks = RSACoder.encryptByPrivateKey(sha256SourceSignString.getBytes("UTF-8"), rsaPriKey);
			result = Base64.encodeBase64String(newsks);
		} catch (Exception e) {
			throw new RuntimeException("verify signature failed.", e);
		}
		return result;
	}


	public static boolean decryptMerchant(String strSourceData, String signData, String rsaPubKey) {
		if (signData == null || signData.isEmpty()) {
			throw new IllegalArgumentException("Argument 'signData' is null or empty");
		}
		if (rsaPubKey == null || rsaPubKey.isEmpty()) {
			throw new IllegalArgumentException("Argument 'key' is null or empty");
		}

		try {
			String sha256SourceSignString = SHAUtil.Encrypt(strSourceData, null);

			byte[] signByte = Base64.decodeBase64(signData);

			byte[] decryptArr = RSACoder.decryptByPublicKey(signByte, rsaPubKey);

			String decryptStr = RSACoder.bytesToString(decryptArr);
			if (sha256SourceSignString.equals(decryptStr)) {
				return true;
			} else {
				throw new RuntimeException("Signature verification failed.");
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("verify signature failed.", e);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("verify signature failed.", e);
		}
	}


	public static void main(String[] args) throws Exception {
		String rsaPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+tBTL7NrtsYpAmwDVqIUECPpZTpxa6GzIfk85J/7BlchTVQQZsk/Ho/umuqNvG9lRpTD/qDhkUo6ybqsu6cDCajL1j8UppjI06m4vxWJiTSX2JnBB63/CcJ+TxSTdmwkHbIlyX4jKrryU4kgw1fZuNzQl3HKf8B169FENOTgGJQIDAQAB";
		String rsaPriKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL60FMvs2u2xikCbANWohQQI+llOnFrobMh+Tzkn/sGVyFNVBBmyT8ej+6a6o28b2VGlMP+oOGRSjrJuqy7pwMJqMvWPxSmmMjTqbi/FYmJNJfYmcEHrf8Jwn5PFJN2bCQdsiXJfiMquvJTiSDDV9m43NCXccp/wHXr0UQ05OAYlAgMBAAECgYEAhBrNeUKXmibtxblah6eYlWX+vtT0/QibKvxMtyRclw/CWO/Aymg6WerfzezmgHaDQcq0ObX3co+6KCL/1Jy7GP/Hk32BgfFpbp90PtQXGjVp03wUobJUBlGFfIxQjnIPUMT145z7aYN0u+ycz17IhA6K3M0QSn39VaOxpp37XcECQQDp6Xfj5dZ1TPcnPMRnSbARwo6fluMmCSRKffO032UOThZkE8un5nD5VhI3KCEllhB6LiIeG35CR5yf++lBUcbRAkEA0LYZnUu8WeNaHwAlKshvquiPzk3xugjum3Gld3wrY6neMSP1F84pbGumpIMnUuglWtKaWPD5anC8sAlF6qMHFQJAFAif8Q/lT0SZQm4M8D+6abr9FiQJLl/IEO06qzoa4J/FgSrE3Yt6D5DUnI6+UAbLQHulBmkaZjjV7EnaD3MekQJAJHOJabVugex5MuzdkOlMx3aylv959lnVAoUItyOSmGd0jPSQu8Wf6nWqtxTI62vsCj66Akqj5Pknmz8jXOV4OQJBANtNmkZH79AQl3heWHnFsr6pPyiZwVopHphzifjddHu3Mvu8/nwQvgXGRu+2vXeUGGhVRlw9W8YYRfNEFiQ+L3o=";
		String sourceData = "amount=16500&callbackUrl=http://fplocal.jd.com&currency=CNY&device=111&expireTime=20190624205928&industryCategoryCode=20JD9601&ip=192.168.178.16&merchant=22318136&note={\"merchantId\":\"22318136\",\"orderIdList\":[\"100560406493366\"]}&notifyUrl=http://jdpaydemo.jd.com/asynNotify.htm&orderType=1&tradeDesc=������&tradeName=������&tradeNum=1005604093366&tradeTime=20190625144314&version=V2.0";


		String sha256SourceSignString = SHAUtil.Encrypt(sourceData, null);
		byte[] newsks = RSACoder.encryptByPrivateKey(sha256SourceSignString.getBytes("UTF-8"), rsaPriKey);
		String sign = Base64.encodeBase64String(newsks);
		System.out.println(sign);


		boolean result = decryptMerchant(sourceData, sign, rsaPubKey);
		System.out.println(result);
	}
}
