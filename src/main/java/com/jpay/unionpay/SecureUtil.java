/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28     报文加密解密等操作的工具类
 * =============================================================================
 */
package com.jpay.unionpay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.digests.SM3Digest;
/**
 * 
 * @ClassName SecureUtil
 * @Description acpsdk安全算法工具类
 * @date 2016-7-22 下午4:08:32
 * 声明：以下代码只是为了方便接入方测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
public class SecureUtil {
	/**
	 * 算法常量： SHA1
	 */
	private static final String ALGORITHM_SHA1 = "SHA-1";
	/**
	 * 算法常量： SHA256
	 */
	private static final String ALGORITHM_SHA256 = "SHA-256";
	/**
	 * 算法常量：SHA1withRSA
	 */
	private static final String BC_PROV_ALGORITHM_SHA1RSA = "SHA1withRSA";
	/**
	 * 算法常量：SHA256withRSA
	 */
	private static final String BC_PROV_ALGORITHM_SHA256RSA = "SHA256withRSA";

	/**
	 * sm3计算后进行16进制转换
	 * 
	 * @param data
	 *            待计算的数据
	 * @param encoding
	 *            编码
	 * @return 计算结果
	 */
	public static String sm3X16Str(String data, String encoding) {
		byte[] bytes = sm3(data, encoding);
		StringBuilder sm3StrBuff = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
				sm3StrBuff.append("0").append(
						Integer.toHexString(0xFF & bytes[i]));
			} else {
				sm3StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}
		return sm3StrBuff.toString();
	}

	/**
	 * sha1计算后进行16进制转换
	 * 
	 * @param data
	 *            待计算的数据
	 * @param encoding
	 *            编码
	 * @return 计算结果
	 */
	public static byte[] sha1X16(String data, String encoding) {
		byte[] bytes = sha1(data, encoding);
		StringBuilder sha1StrBuff = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
				sha1StrBuff.append("0").append(
						Integer.toHexString(0xFF & bytes[i]));
			} else {
				sha1StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}
		try {
			return sha1StrBuff.toString().getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
			return null;
		}
	}
	
	
	/**
	 * sha256计算后进行16进制转换
	 * 
	 * @param data
	 *            待计算的数据
	 * @param encoding
	 *            编码
	 * @return 计算结果
	 */
	public static String sha256X16Str(String data, String encoding) {
		byte[] bytes = sha256(data, encoding);
		StringBuilder sha256StrBuff = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
				sha256StrBuff.append("0").append(
						Integer.toHexString(0xFF & bytes[i]));
			} else {
				sha256StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}
		return sha256StrBuff.toString();
	}
	
	/**
	 * sha256计算后进行16进制转换
	 * 
	 * @param data
	 *            待计算的数据
	 * @param encoding
	 *            编码
	 * @return 计算结果
	 */
	public static byte[] sha256X16(String data, String encoding) {
		byte[] bytes = sha256(data, encoding);
		StringBuilder sha256StrBuff = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
				sha256StrBuff.append("0").append(
						Integer.toHexString(0xFF & bytes[i]));
			} else {
				sha256StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}
		try {
			return sha256StrBuff.toString().getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * sha1计算.
	 * 
	 * @param datas
	 *            待计算的数据
	 * @return 计算结果
	 */
	private static byte[] sha1(byte[] data) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(ALGORITHM_SHA1);
			md.reset();
			md.update(data);
			return md.digest();
		} catch (Exception e) {
			LogUtil.writeErrorLog("SHA1计算失败", e);
			return null;
		}
	}
	
	/**
	 * sha256计算.
	 * 
	 * @param datas
	 *            待计算的数据
	 * @return 计算结果
	 */
	private static byte[] sha256(byte[] data) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(ALGORITHM_SHA256);
			md.reset();
			md.update(data);
			return md.digest();
		} catch (Exception e) {
			LogUtil.writeErrorLog("SHA256计算失败", e);
			return null;
		}
	}
	
	/**
	 * SM3计算.
	 * 
	 * @param datas
	 *            待计算的数据
	 * @return 计算结果
	 */
	private static byte[] sm3(byte[] data) {
		SM3Digest sm3 = new SM3Digest();
		sm3.update(data, 0, data.length);
		byte[] result = new byte[sm3.getDigestSize()];
		sm3.doFinal(result, 0);
		return result;
	}
	
	/**
	 * sha1计算
	 * 
	 * @param datas
	 *            待计算的数据
	 * @param encoding
	 *            字符集编码
	 * @return
	 */
	private static byte[] sha1(String datas, String encoding) {
		try {
			return sha1(datas.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			LogUtil.writeErrorLog("SHA1计算失败", e);
			return null;
		}
	}
	
	/**
	 * sha256计算
	 * 
	 * @param datas
	 *            待计算的数据
	 * @param encoding
	 *            字符集编码
	 * @return
	 */
	private static byte[] sha256(String datas, String encoding) {
		try {
			return sha256(datas.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			LogUtil.writeErrorLog("SHA256计算失败", e);
			return null;
		}
	}

	/**
	 * sm3计算
	 * 
	 * @param datas
	 *            待计算的数据
	 * @param encoding
	 *            字符集编码
	 * @return
	 */
	private static byte[] sm3(String datas, String encoding) {
		try {
			return sm3(datas.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			LogUtil.writeErrorLog("SM3计算失败", e);
			return null;
		}
	}

	/**
	 * 
	 * @param privateKey
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] signBySoft(PrivateKey privateKey, byte[] data)
			throws Exception {
		byte[] result = null;
		Signature st = Signature.getInstance(BC_PROV_ALGORITHM_SHA1RSA, "BC");
		st.initSign(privateKey);
		st.update(data);
		result = st.sign();
		return result;
	}
	
	/**
	 * @param privateKey
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] signBySoft256(PrivateKey privateKey, byte[] data)
			throws Exception {
		byte[] result = null;
		Signature st = Signature.getInstance(BC_PROV_ALGORITHM_SHA256RSA, "BC");
		st.initSign(privateKey);
		st.update(data);
		result = st.sign();
		return result;
	}

	public static boolean validateSignBySoft(PublicKey publicKey,
			byte[] signData, byte[] srcData) throws Exception {
		Signature st = Signature.getInstance(BC_PROV_ALGORITHM_SHA1RSA, "BC");
		st.initVerify(publicKey);
		st.update(srcData);
		return st.verify(signData);
	}
	
	public static boolean validateSignBySoft256(PublicKey publicKey,
			byte[] signData, byte[] srcData) throws Exception {
		Signature st = Signature.getInstance(BC_PROV_ALGORITHM_SHA256RSA, "BC");
		st.initVerify(publicKey);
		st.update(srcData);
		return st.verify(signData);
	}

	/**
	 * 对数据通过公钥进行加密，并进行base64计算
	 * 
	 * @param dataString
	 *            待处理数据
	 * @param encoding
	 *            字符编码
	 * @param key
	 *            公钥
	 * @return
	 */
	public static String encryptData(String dataString, String encoding,
			PublicKey key) {
		/** 使用公钥对密码加密 **/
		byte[] data = null;
		try {
			data = encryptData(key, dataString.getBytes(encoding));
			return new String(SecureUtil.base64Encode(data), encoding);
		} catch (Exception e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * 对数据通过公钥进行加密，并进行base64计算
	 * 
	 * @param dataString
	 *            待处理数据
	 * @param encoding
	 *            字符编码
	 * @param key
	 *            公钥
	 * @return
	 */
	public static String encryptPin(String accNo, String pin, String encoding,
			PublicKey key) {
		/** 使用公钥对密码加密 **/
		byte[] data = null;
		try {
			data = pin2PinBlockWithCardNO(pin, accNo);
			data = encryptData(key, data);
			return new String(SecureUtil.base64Encode(data), encoding);
		} catch (Exception e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
			return "";
		}
	}
	
	/**
	 * 通过私钥解密
	 * 
	 * @param dataString
	 *            base64过的数据
	 * @param encoding
	 *            编码
	 * @param key
	 *            私钥
	 * @return 解密后的数据
	 */
	public static String decryptData(String dataString, String encoding,
			PrivateKey key) {
		byte[] data = null;
		try {
			data = SecureUtil.base64Decode(dataString.getBytes(encoding));
			data = decryptData(key, data);
			return new String(data, encoding);
		} catch (Exception e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * BASE64解码
	 * 
	 * @param inputByte
	 *            待解码数据
	 * @return 解码后的数据
	 * @throws IOException
	 */
	public static byte[] base64Decode(byte[] inputByte) throws IOException {
		return Base64.decodeBase64(inputByte);
	}

	/**
	 * BASE64编码
	 * 
	 * @param inputByte
	 *            待编码数据
	 * @return 解码后的数据
	 * @throws IOException
	 */
	public static byte[] base64Encode(byte[] inputByte) throws IOException {
		return Base64.encodeBase64(inputByte);
	}

	/**
	 * 加密除pin之外的其他信息
	 * 
	 * @param publicKey
	 * @param plainData
	 * @return
	 * @throws Exception
	 */
	private static byte[] encryptData(PublicKey publicKey, byte[] plainData)
			throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(plainData);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * @param privateKey
	 * @param cryptPin
	 * @return
	 * @throws Exception
	 */
	private static byte[] decryptData(PrivateKey privateKey, byte[] data)
			throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			LogUtil.writeErrorLog("解密失败", e);
		}
		return null;
	}

	/**
	 * 
	 * @param aPin
	 * @return
	 */
	private static byte[] pin2PinBlock(String aPin) {
		int tTemp = 1;
		int tPinLen = aPin.length();

		byte[] tByte = new byte[8];
		try {
			/*******************************************************************
			 * if (tPinLen > 9) { tByte[0] = (byte) Integer.parseInt(new
			 * Integer(tPinLen) .toString(), 16); } else { tByte[0] = (byte)
			 * Integer.parseInt(new Integer(tPinLen) .toString(), 10); }
			 ******************************************************************/
//			tByte[0] = (byte) Integer.parseInt(new Integer(tPinLen).toString(),
//					10);
			tByte[0] = (byte) Integer.parseInt(Integer.toString(tPinLen), 10);
			if (tPinLen % 2 == 0) {
				for (int i = 0; i < tPinLen;) {
					String a = aPin.substring(i, i + 2);
					tByte[tTemp] = (byte) Integer.parseInt(a, 16);
					if (i == (tPinLen - 2)) {
						if (tTemp < 7) {
							for (int x = (tTemp + 1); x < 8; x++) {
								tByte[x] = (byte) 0xff;
							}
						}
					}
					tTemp++;
					i = i + 2;
				}
			} else {
				for (int i = 0; i < tPinLen - 1;) {
					String a;
					a = aPin.substring(i, i + 2);
					tByte[tTemp] = (byte) Integer.parseInt(a, 16);
					if (i == (tPinLen - 3)) {
						String b = aPin.substring(tPinLen - 1) + "F";
						tByte[tTemp + 1] = (byte) Integer.parseInt(b, 16);
						if ((tTemp + 1) < 7) {
							for (int x = (tTemp + 2); x < 8; x++) {
								tByte[x] = (byte) 0xff;
							}
						}
					}
					tTemp++;
					i = i + 2;
				}
			}
		} catch (Exception e) {
		}

		return tByte;
	}

	/**
	 * 
	 * @param aPan
	 * @return
	 */
	private static byte[] formatPan(String aPan) {
		int tPanLen = aPan.length();
		byte[] tByte = new byte[8];
		;
		int temp = tPanLen - 13;
		try {
			tByte[0] = (byte) 0x00;
			tByte[1] = (byte) 0x00;
			for (int i = 2; i < 8; i++) {
				String a = aPan.substring(temp, temp + 2);
				tByte[i] = (byte) Integer.parseInt(a, 16);
				temp = temp + 2;
			}
		} catch (Exception e) {
		}
		return tByte;
	}

	/**
	 * 
	 * @param aPin
	 * @param aCardNO
	 * @return
	 */
	private static byte[] pin2PinBlockWithCardNO(String aPin, String aCardNO) {
		byte[] tPinByte = pin2PinBlock(aPin);
		if (aCardNO.length() == 11) {
			aCardNO = "00" + aCardNO;
		} else if (aCardNO.length() == 12) {
			aCardNO = "0" + aCardNO;
		}
		byte[] tPanByte = formatPan(aCardNO);
		byte[] tByte = new byte[8];
		for (int i = 0; i < 8; i++) {
			tByte[i] = (byte) (tPinByte[i] ^ tPanByte[i]);
		}
		return tByte;
	}
	
	 /**
    * luhn算法
    * 
    * @param number
    * @return
    */
   public static int genLuhn(String number) {
       number = number + "0";
       int s1 = 0, s2 = 0;
       String reverse = new StringBuffer(number).reverse().toString();
       for (int i = 0; i < reverse.length(); i++) {
           int digit = Character.digit(reverse.charAt(i), 10);
           if (i % 2 == 0) {// this is for odd digits, they are 1-indexed in //
                            // the algorithm
               s1 += digit;
           } else {// add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
               s2 += 2 * digit;
               if (digit >= 5) {
                   s2 -= 9;
               }
           }
       }
       int check = 10 - ((s1 + s2) % 10);
       if (check == 10)
           check = 0;
       return check;
   }
}
