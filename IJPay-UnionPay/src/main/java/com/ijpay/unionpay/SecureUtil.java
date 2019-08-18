
package com.ijpay.unionpay;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.crypto.digests.SM3Digest;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * @author UnionPay
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

    private static byte[] sm3(byte[] data) {
        SM3Digest sm3 = new SM3Digest();
        sm3.update(data, 0, data.length);
        byte[] result = new byte[sm3.getDigestSize()];
        sm3.doFinal(result, 0);
        return result;
    }

    private static byte[] sha1(String datas, String encoding) {
        try {
            return sha1(datas.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeErrorLog("SHA1计算失败", e);
            return null;
        }
    }

    private static byte[] sha256(String datas, String encoding) {
        try {
            return sha256(datas.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeErrorLog("SHA256计算失败", e);
            return null;
        }
    }

    private static byte[] sm3(String data, String encoding) {
        try {
            return sm3(data.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            LogUtil.writeErrorLog("SM3计算失败", e);
            return null;
        }
    }

    public static byte[] signBySoft(PrivateKey privateKey, byte[] data)
            throws Exception {
        byte[] result = null;
        Signature st = Signature.getInstance(BC_PROV_ALGORITHM_SHA1RSA, "BC");
        st.initSign(privateKey);
        st.update(data);
        result = st.sign();
        return result;
    }

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

    public static String encryptPin(String accNo, String pin, String encoding,
                                    PublicKey key) {
        /** 使用公钥对密码加密 **/
        byte[] data = null;
        try {
            data = pin2PinBlockWithCardNo(pin, accNo);
            data = encryptData(key, data);
            return new String(SecureUtil.base64Encode(data), encoding);
        } catch (Exception e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
            return "";
        }
    }

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

    public static byte[] base64Decode(byte[] inputByte) throws IOException {
        return Base64.decodeBase64(inputByte);
    }

    public static byte[] base64Encode(byte[] inputByte) throws IOException {
        return Base64.encodeBase64(inputByte);
    }

    private static byte[] encryptData(PublicKey publicKey, byte[] plainData)
            throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(plainData);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private static byte[] decryptData(PrivateKey privateKey, byte[] data)
            throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            LogUtil.writeErrorLog("解密失败", e);
        }
        return null;
    }

    private static byte[] pin2PinBlock(String aPin) {
        int tTemp = 1;
        int tPinLen = aPin.length();

        byte[] tByte = new byte[8];
        try {
            tByte[0] = (byte) Integer.parseInt(Integer.toString(tPinLen), 10);
            if (tPinLen % 2 == 0) {
                for (int i = 0; i < tPinLen; ) {
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
                for (int i = 0; i < tPinLen - 1; ) {
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

    private static byte[] pin2PinBlockWithCardNo(String aPin, String aCardNo) {
        byte[] tPinByte = pin2PinBlock(aPin);
        if (aCardNo.length() == 11) {
            aCardNo = "00" + aCardNo;
        } else if (aCardNo.length() == 12) {
            aCardNo = "0" + aCardNo;
        }
        byte[] tPanByte = formatPan(aCardNo);
        byte[] tByte = new byte[8];
        for (int i = 0; i < 8; i++) {
            tByte[i] = (byte) (tPinByte[i] ^ tPanByte[i]);
        }
        return tByte;
    }

    public static int genLuHn(String number) {
        number = number + "0";
        int s1 = 0, s2 = 0;
        String reverse = new StringBuffer(number).reverse().toString();
        for (int i = 0; i < reverse.length(); i++) {
            int digit = Character.digit(reverse.charAt(i), 10);
            // this is for odd digits, they are 1-indexed in //
            if (i % 2 == 0) {
                // the algorithm
                s1 += digit;
            } else {
                // add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
                s2 += 2 * digit;
                if (digit >= 5) {
                    s2 -= 9;
                }
            }
        }
        int check = 10 - ((s1 + s2) % 10);
        if (check == 10) {
            check = 0;
        }
        return check;
    }
}
