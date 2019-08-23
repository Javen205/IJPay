package com.ijpay.jdpay.util;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


public class ThreeDesUtil {
    public static final byte[] DEFAULT_KEY = {
            49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52};


    public static byte[] encrypt(byte[] keybyte, byte[] src) {
        try {
            SecretKey deskey = new SecretKeySpec(keybyte, "DESede");

            Cipher c1 = Cipher.getInstance("DESede/ECB/NoPadding");
            c1.init(1, deskey);
            return c1.doFinal(src);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }


    private static byte[] decrypt(byte[] keybyte, byte[] src) {
        try {
            SecretKey deskey = new SecretKeySpec(keybyte, "DESede");

            Cipher c1 = Cipher.getInstance("DESede/ECB/NoPadding");
            c1.init(2, deskey);
            return c1.doFinal(src);
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }


    private static String byte2Hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) hs = hs + ":";
        }
        return hs.toUpperCase();
    }


    public static String encrypt2HexStr(byte[] keys, String sourceData) {
        try {
            byte[] source = sourceData.getBytes("UTF-8");
            int merchantData = source.length;
            int x = (merchantData + 4) % 8;
            int y = (x == 0) ? 0 : (8 - x);
            byte[] sizeByte = intToByteArray(merchantData);
            byte[] resultByte = new byte[merchantData + 4 + y];
            resultByte[0] = sizeByte[0];
            resultByte[1] = sizeByte[1];
            resultByte[2] = sizeByte[2];
            resultByte[3] = sizeByte[3];
            for (int i = 0; i < merchantData; i++) {
                resultByte[4 + i] = source[i];
            }
            for (int i = 0; i < y; i++) {
                resultByte[merchantData + 4 + i] = 0;
            }
            byte[] desdata = encrypt(keys, resultByte);
            return bytes2Hex(desdata);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

            return null;
        }
    }


    public static String decrypt4HexStr(byte[] keys, String data) {
        byte[] hexSourceData = new byte[0];
        try {
            hexSourceData = hex2byte(data.getBytes("UTF-8"));
            byte[] unDesResult = decrypt(keys, hexSourceData);
            byte[] dataSizeByte = new byte[4];
            dataSizeByte[0] = unDesResult[0];
            dataSizeByte[1] = unDesResult[1];
            dataSizeByte[2] = unDesResult[2];
            dataSizeByte[3] = unDesResult[3];
            int dsb = byteArrayToInt(dataSizeByte, 0);
            if (dsb > 16384) {
                throw new RuntimeException("msg over MAX_MSG_LENGTH or msg error");
            }
            byte[] tempData = new byte[dsb];
            for (int i = 0; i < dsb; i++) {
                tempData[i] = unDesResult[4 + i];
            }
            return hex2bin(toHexString(tempData));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String hex2bin(String hex) throws UnsupportedEncodingException {
        String digital = "0123456789abcdef";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            int temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xFF);
        }

        return new String(bytes, "UTF-8");
    }


    private static String toHexString(byte[] ba) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ba.length; i++) {
            str.append(String.format("%x", new Object[]{Byte.valueOf(ba[i])}));
        }
        return str.toString();
    }


    private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = Integer.toHexString(bts[i] & 0xFF);
            if (tmp.length() == 1) {
                des = des + "0";
            }
            des = des + tmp;
        }
        return des;
    }

    public static byte[] hex2byte(byte[] b) {
        if (b.length % 2 != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);

            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        b = null;
        return b2;
    }


    private static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) (i >> 24 & 0xFF);
        result[1] = (byte) (i >> 16 & 0xFF);
        result[2] = (byte) (i >> 8 & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }


    private static int byteArrayToInt(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += ((b[i + offset] & 0xFF) << shift);
        }
        return value;
    }


    public static void main(String[] args) {
        String szSrc = "This is a 3DES test. 测试abcdf";
        System.out.println("加密前的字符串:" + szSrc);
        byte[] encoded = new byte[0];
        try {
            System.out.println("加密前长度:" + szSrc.getBytes("UTF-8").length);
            System.out.println("加密前HEX:" + bytes2Hex(szSrc.getBytes("UTF-8")));
            encoded = encrypt(DEFAULT_KEY, szSrc.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            System.out.println("加密后长度:" + encoded.length);
            System.out.println("加密后的字符串:" + new String(encoded, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] srcBytes = decrypt(DEFAULT_KEY, encoded);

        System.out.println("解密后HEX:" + bytes2Hex(srcBytes));
        System.out.println("解密后的字符串:" + new String(srcBytes));
    }
}
