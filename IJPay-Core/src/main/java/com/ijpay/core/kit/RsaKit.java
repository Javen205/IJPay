package com.ijpay.core.kit;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p>RSA 非对称加密工具类</p>
 *
 * @author Javen
 */
public class RsaKit {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 生成公钥和私钥
     *
     * @throws Exception
     */
    public static Map<String, String> getKeys() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        String publicKeyStr = getPublicKeyStr(publicKey);
        String privateKeyStr = getPrivateKeyStr(privateKey);

        Map<String, String> map = new HashMap<String, String>(2);
        map.put("publicKey", publicKeyStr);
        map.put("privateKey", privateKeyStr);

        System.out.println("公钥\r\n" + publicKeyStr);
        System.out.println("私钥\r\n" + privateKeyStr);
        return map;
    }

    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 公钥指数
     * @return
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus  模
     * @param exponent 指数
     * @return
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥加密
     *
     * @param data      需要加密的数据
     * @param publicKey 公钥
     * @return 加密后的数据
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String publicKey) throws Exception {
        return encryptByPublicKey(data, publicKey, "RSA/ECB/PKCS1Padding");
    }

    public static String encryptByPublicKeyByWx(String data, String publicKey) throws Exception {
        return encryptByPublicKey(data, publicKey, "RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
    }

    /**
     * 公钥加密
     *
     * @param data      需要加密的数据
     * @param publicKey 公钥
     * @param fillMode  填充模式
     * @return 加密后的数据
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String publicKey, String fillMode) throws Exception {
        byte[] dataByte = data.getBytes("UTf-8");
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key key = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(fillMode);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        int inputLen = dataByte.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(dataByte, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataByte, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return StrUtil.str(Base64.encode(encryptedData));
    }

    /**
     * 私钥签名
     *
     * @param data       需要加密的数据
     * @param privateKey 私钥
     * @return 加密后的数据
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String privateKey) throws Exception {
        PKCS8EncodedKeySpec priPkcs8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey priKey = keyFactory.generatePrivate(priPkcs8);
        java.security.Signature signature = java.security.Signature.getInstance("SHA256WithRSA");

        signature.initSign(priKey);
        signature.update(data.getBytes("UTf-8"));
        byte[] signed = signature.sign();
        return StrUtil.str(Base64.encode(signed));
    }

    /**
     * 公钥验证签名
     *
     * @param data      需要加密的数据
     * @param sign      签名
     * @param publicKey 公钥
     * @return 验证结果
     * @throws Exception
     */
    public static boolean checkByPublicKey(String data, String sign, String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        byte[] encodedKey = Base64.decode(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        java.security.Signature signature = java.security.Signature.getInstance("SHA256WithRSA");
        signature.initVerify(pubKey);
        signature.update(data.getBytes("UTf-8"));
        return signature.verify(Base64.decode(sign.getBytes("UTf-8")));
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String privateKey) throws Exception {
        return decryptByPrivateKey(data, privateKey, "RSA/ECB/PKCS1Padding");
    }

    public static String decryptByPrivateKeyByWx(String data, String privateKey) throws Exception {
        return decryptByPrivateKey(data, privateKey, "RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING");
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @param fillMode   填充模式
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String privateKey, String fillMode) throws Exception {
        byte[] encryptedData = Base64.decode(data);
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key key = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(fillMode);

        cipher.init(Cipher.DECRYPT_MODE, key);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData);
    }

    /**
     * 获取模数和密钥
     *
     * @return
     */
    public static Map<String, String> getModulusAndKeys() {

        Map<String, String> map = new HashMap<String, String>(3);

        try {
            InputStream in = RsaKit.class.getResourceAsStream("/rsa.properties");
            Properties prop = new Properties();
            prop.load(in);

            String modulus = prop.getProperty("modulus");
            String publicKey = prop.getProperty("publicKey");
            String privateKey = prop.getProperty("privateKey");

            in.close();

            map.put("modulus", modulus);
            map.put("publicKey", publicKey);
            map.put("privateKey", privateKey);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从字符串中加载私钥<br>
     * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
     *
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decode(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    public static String getPrivateKeyStr(PrivateKey privateKey) throws Exception {
        return new String(Base64.encode(privateKey.getEncoded()));
    }

    public static String getPublicKeyStr(PublicKey publicKey) throws Exception {
        return new String(Base64.encode(publicKey.getEncoded()));
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> keys = getKeys();
        String publicKey = keys.get("publicKey");
        String privateKey = keys.get("privateKey");
        String content = "我是Javen,I am Javen";
        String encrypt = encryptByPublicKey(content, publicKey);
        String decrypt = decryptByPrivateKey(encrypt, privateKey);
        System.out.println("加密之后：" + encrypt);
        System.out.println("解密之后：" + decrypt);

        System.out.println("======华丽的分割线=========");

        content = "我是Javen,I am Javen";
        encrypt = encryptByPublicKeyByWx(content, publicKey);
        decrypt = decryptByPrivateKeyByWx(encrypt, privateKey);
        System.out.println("加密之后：" + encrypt);
        System.out.println("解密之后：" + decrypt);

        //OPPO
        String sign = encryptByPrivateKey(content, privateKey);
        System.out.println("加密之后：" + sign);
        System.out.println(checkByPublicKey(content, sign, publicKey));
    }
}