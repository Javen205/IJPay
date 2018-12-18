package com.jpay.ext.kit;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesKit {

	/**
	 * 密钥算法
	 */
	private static final String ALGORITHM = "AES";
	/**
	 * 加解密算法/工作模式/填充方式
	 */
	private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";

	/**
	 * AES加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptData(String data, String key) throws Exception {
		// 创建密码器
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
		// 初始化
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(HashKit.md5(key).toLowerCase().getBytes(), ALGORITHM));
		return Base64Kit.encode(cipher.doFinal(data.getBytes()));
	}

	/**
	 * AES解密
	 * 
	 * @param base64Data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decryptData(String base64Data, String key) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(HashKit.md5(key).toLowerCase().getBytes(), ALGORITHM));
		return new String(cipher.doFinal(Base64Kit.decode(base64Data)));
	}
}
