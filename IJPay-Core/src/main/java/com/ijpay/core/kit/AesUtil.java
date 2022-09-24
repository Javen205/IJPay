package com.ijpay.core.kit;

import cn.hutool.core.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>工具类 AesUtil</p>
 *
 * @author 微信
 */
public class AesUtil {

	static final int KEY_LENGTH_BYTE = 32;
	static final int TAG_LENGTH_BIT = 128;
	private final byte[] aesKey;

	/**
	 * @param key APIv3 密钥
	 */
	public AesUtil(byte[] key) {
		if (key.length != KEY_LENGTH_BYTE) {
			throw new IllegalArgumentException("无效的ApiV3Key，长度必须为32个字节");
		}
		this.aesKey = key;
	}

	/**
	 * 证书和回调报文解密
	 *
	 * @param associatedData associated_data
	 * @param nonce          nonce
	 * @param cipherText     ciphertext
	 * @return {String} 平台证书明文
	 * @throws GeneralSecurityException 异常
	 */
	public String decryptToString(byte[] associatedData, byte[] nonce, String cipherText) throws GeneralSecurityException {
		try {
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

			SecretKeySpec key = new SecretKeySpec(aesKey, "AES");
			GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce);

			cipher.init(Cipher.DECRYPT_MODE, key, spec);
			cipher.updateAAD(associatedData);

			return new String(cipher.doFinal(Base64.decode(cipherText)), StandardCharsets.UTF_8);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new IllegalStateException(e);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
