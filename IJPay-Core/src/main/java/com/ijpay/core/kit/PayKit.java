package com.ijpay.core.kit;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.ijpay.core.XmlHelper;
import com.ijpay.core.enums.RequestMethod;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p>IJPay 工具类</p>
 *
 * @author Javen
 */
public class PayKit {

    public static String hmacSha256(String data, String key) {
        return SecureUtil.hmac(HmacAlgorithm.HmacSHA256, key).digestHex(data, CharsetUtil.UTF_8);
    }

    public static String md5(String data) {
        return SecureUtil.md5(data);
    }

    /**
     * AES 解密
     *
     * @param base64Data 需要解密的数据
     * @param key        密钥
     * @return 解密后的数据
     */
    public static String decryptData(String base64Data, String key) {
        return SecureUtil.aes(md5(key).toLowerCase().getBytes()).decryptStr(base64Data);
    }

    /**
     * AES 加密
     *
     * @param data 需要加密的数据
     * @param key  密钥
     * @return 加密后的数据
     */
    public static String encryptData(String data, String key) {
        return SecureUtil.aes(md5(key).toLowerCase().getBytes()).encryptBase64(data.getBytes());
    }

    public static String generateStr() {
        return IdUtil.fastSimpleUUID();
    }

    /**
     * 把所有元素排序
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        return createLinkString(params, false);
    }

    /**
     * @param params 需要排序并参与字符拼接的参数组
     * @param encode 是否进行URLEncoder
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, boolean encode) {
        return createLinkString(params, "&", encode);
    }

    /**
     * @param params  需要排序并参与字符拼接的参数组
     * @param connStr 连接符号
     * @param encode  是否进行URLEncoder
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, String connStr, boolean encode) {
        return createLinkString(params, connStr, encode, false);
    }

    public static String createLinkString(Map<String, String> params, String connStr, boolean encode, boolean quotes) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        StringBuffer content = new StringBuffer();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            // 拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                if (quotes) {
                    content.append(key + "=" + '"' + (encode ? urlEncode(value) : value) + '"');
                } else {
                    content.append(key + "=" + (encode ? urlEncode(value) : value));
                }
            } else {
                if (quotes) {
                    content.append(key + "=" + '"' + (encode ? urlEncode(value) : value) + '"' + connStr);
                } else {
                    content.append(key + "=" + (encode ? urlEncode(value) : value) + connStr);
                }
            }
        }
        return content.toString();
    }


    /**
     * URL 编码
     *
     * @param src 需要编码的字符串
     * @return 编码后的字符串
     */
    public static String urlEncode(String src) {
        try {
            return URLEncoder.encode(src, CharsetUtil.UTF_8).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 遍历 Map 并构建 xml 数据
     *
     * @param params 需要遍历的 Map
     * @param prefix xml 前缀
     * @param suffix xml 后缀
     * @return
     */
    public static StringBuilder forEachMap(Map<String, String> params, String prefix, String suffix) {
        StringBuilder xml = new StringBuilder();
        if (StrUtil.isNotEmpty(prefix)) {
            xml.append(prefix);
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // 略过空值
            if (StrUtil.isEmpty(value)) {
                continue;
            }
            xml.append("<").append(key).append(">");
            xml.append(entry.getValue());
            xml.append("</").append(key).append(">");
        }
        if (StrUtil.isNotEmpty(suffix)) {
            xml.append(suffix);
        }
        return xml;
    }

    /**
     * 微信下单 map to xml
     *
     * @param params Map 参数
     * @return xml 字符串
     */
    public static String toXml(Map<String, String> params) {
        StringBuilder xml = forEachMap(params, "<xml>", "</xml>");
        return xml.toString();
    }

    /**
     * 针对支付的 xml，没有嵌套节点的简单处理
     *
     * @param xmlStr xml 字符串
     * @return 转化后的 Map
     */
    public static Map<String, String> xmlToMap(String xmlStr) {
        XmlHelper xmlHelper = XmlHelper.of(xmlStr);
        return xmlHelper.toMap();
    }

    /**
     * 构造签名串
     *
     * @param method    {@link RequestMethod} GET,POST,PUT等
     * @param url       请求接口 /v3/certificates
     * @param timestamp 获取发起请求时的系统当前时间戳
     * @param nonceStr  随机字符串
     * @param body      请求报文主体
     * @return
     */
    public static String buildSignMessage(RequestMethod method, String url, long timestamp, String nonceStr, String body) {
        return new StringBuffer()
                .append(method.toString())
                .append("\n")
                .append(url)
                .append("\n")
                .append(timestamp)
                .append("\n")
                .append(nonceStr)
                .append("\n")
                .append(body)
                .append("\n")
                .toString();
    }

    /**
     * 获取授权认证信息
     *
     * @param mchId     商户号
     * @param serialNo  商户API证书序列号
     * @param nonceStr  请求随机串
     * @param timestamp 时间戳
     * @param signature 签名值
     * @param authType  认证类型，目前为WECHATPAY2-SHA256-RSA2048
     * @return
     */
    public static String getAuthorization(String mchId, String serialNo, String nonceStr, String timestamp, String signature, String authType) {
        Map<String, String> params = new HashMap<>(5);
        params.put("mchid", mchId);
        params.put("serial_no", serialNo);
        params.put("nonce_str", nonceStr);
        params.put("timestamp", timestamp);
        params.put("signature", signature);
        return authType.concat(" ").concat(createLinkString(params, ",", false,true));
    }

    /**
     * 获取商户私钥
     *
     * @param apiClientKeyPath apiclient_key.pem 路径
     * @return 商户私钥
     * @throws Exception
     */
    public static String getPrivateKey(String apiClientKeyPath) throws Exception {
        String originalKey = FileUtil.readUtf8String(apiClientKeyPath);
        String privateKey = originalKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        return RsaKit.getPrivateKeyStr(RsaKit.loadPrivateKey(privateKey));
    }
}