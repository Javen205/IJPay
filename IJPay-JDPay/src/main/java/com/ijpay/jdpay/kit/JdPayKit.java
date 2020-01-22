/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>京东支付 Kit</p>
 *
 * @author Javen
 */
package com.ijpay.jdpay.kit;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.crypto.SecureUtil;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.jdpay.util.SignUtil;
import com.ijpay.jdpay.util.ThreeDesUtil;
import com.ijpay.jdpay.util.XmlEncryptUtil;
import org.w3c.dom.Document;

import javax.xml.xpath.XPathConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdPayKit {
    /**
     * MD5 加密
     *
     * @param data 需要加密的数据
     * @return 加密后的数据
     */
    public static String md5LowerCase(String data) {
        return SecureUtil.md5(data).toLowerCase();
    }

    /**
     * 请求参数 Map 转化为京东支付 xml
     *
     * @param params 请求参数
     * @return
     */
    public static String toJdXml(Map<String, String> params) {
        return WxPayKit.forEachMap(params, "<jdpay>", "</jdpay>").toString();
    }

    /**
     * 请求参数签名
     *
     * @param rsaPrivateKey RSA 私钥
     * @param strDesKey     DES 密钥
     * @param genSignStr    xml 数据
     * @return 签名后的数据
     */
    public static String encrypt(String rsaPrivateKey, String strDesKey, String genSignStr) {
        return XmlEncryptUtil.encrypt(rsaPrivateKey, strDesKey, genSignStr);
    }

    /**
     * 解密接口返回的 xml 数据
     *
     * @param rsaPubKey RSA 公钥
     * @param strDesKey DES 密钥
     * @param encrypt   加密的 xml 数据
     * @return 解密后的数据
     */
    public static String decrypt(String rsaPubKey, String strDesKey, String encrypt) {
        return XmlEncryptUtil.decrypt(rsaPubKey, strDesKey, encrypt);
    }

    /**
     * 明文验证签名
     * @param rsaPubKey RSA 公钥
     * @param reqBody xml 数据
     * @return 明文数据
     */
    public static String decrypt(String rsaPubKey, String reqBody) {
        return XmlEncryptUtil.decrypt(rsaPubKey, reqBody);
    }

    public static String signRemoveSelectedKeys(Map<String, String> map, String rsaPriKey, List<String> unSignKeyList) {
        return SignUtil.signRemoveSelectedKeys(map, rsaPriKey, unSignKeyList);
    }

    /**
     * 3DES加密
     *
     * @param desKey     DES 密钥
     * @param sourceData 需要加密的字符串
     * @return 加密后的字符串
     */
    public static String threeDesEncrypt(String desKey, String sourceData) {
        byte[] key = Base64.decode(desKey);
        return ThreeDesUtil.encrypt2HexStr(key, sourceData);
    }

    /**
     * 3DES解密
     *
     * @param desKey     DES 密钥
     * @param sourceData 需要解密的字符串
     * @return 解密后的字符串
     */
    public static String threeDecDecrypt(String desKey, String sourceData) {
        byte[] key = Base64.decode(desKey);
        return ThreeDesUtil.decrypt4HexStr(key, sourceData);
    }


    /**
     * <p>在线支付接口</p>
     * <p>除了merchant（商户号）、version（版本号）、sign(签名)以外，其余字段全部采用3DES进行加密。</p>
     *
     * @return 转化后的 Map
     */
    public static Map<String, String> threeDesToMap(Map<String, String> map, String desKey) {

        HashMap<String, String> tempMap = new HashMap<String, String>(map.size());

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (StrUtil.isNotEmpty(value)) {
                if ("merchant".equals(name) || "version".equals(name) || "sign".equals(name)) {
                    tempMap.put(name, value);
                } else {
                    tempMap.put(name, threeDesEncrypt(desKey, value));
                }
            }
        }
        return tempMap;
    }

    /**
     * 将支付接口返回的 xml 数据转化为 Map
     *
     * @param xml 接口返回的 xml 数据
     * @return 解析后的数据
     */
    public static Map<String, String> parseResp(String xml) {
        if (StrUtil.isEmpty(xml)) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>(3);
        Document docResult = XmlUtil.parseXml(xml);
        String code = (String) XmlUtil.getByXPath("//jdpay/result/code", docResult, XPathConstants.STRING);
        String desc = (String) XmlUtil.getByXPath("//jdpay/result/desc", docResult, XPathConstants.STRING);
        map.put("code", code);
        map.put("desc", desc);
        if ("000000".equals(code)) {
            String encrypt = (String) XmlUtil.getByXPath("//jdpay/encrypt", docResult, XPathConstants.STRING);
            map.put("encrypt", encrypt);
        }
        return map;
    }
}
