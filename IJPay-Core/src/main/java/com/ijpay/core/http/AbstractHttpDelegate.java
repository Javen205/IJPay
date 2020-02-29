package com.ijpay.core.http;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.ssl.SSLSocketFactoryBuilder;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>Http 代理类</p>
 *
 * @author Javen
 */
public abstract class AbstractHttpDelegate {
    static final String OS = System.getProperty("os.name") + "/" + System.getProperty("os.version");
    static final String VERSION = System.getProperty("java.version");

    /**
     * get 请求
     *
     * @param url 请求url
     * @return {@link String} 请求返回的结果
     */
    public String get(String url) {
        return HttpUtil.get(url);
    }

    /**
     * get 请求
     *
     * @param url           请求url
     * @param authorization 授权信息
     * @param serialNumber  公钥证书序列号
     * @param paramMap      请求参数
     * @return {@link Map} 请求返回的结果
     */
    public Map<String, Object> get(String url, String authorization, String serialNumber, Map<String, Object> paramMap) {
        HttpResponse httpResponse = getToResponse(url, authorization, serialNumber, paramMap);
        return buildResMap(httpResponse);
    }

    /**
     * get 请求
     *
     * @param url           请求url
     * @param authorization 授权信息
     * @param serialNumber  公钥证书序列号
     * @param paramMap      请求参数
     * @return {@link HttpResponse} 请求返回的结果
     */
    private HttpResponse getToResponse(String url, String authorization, String serialNumber, Map<String, Object> paramMap) {
        return HttpRequest.get(url)
                .addHeaders(getHeaders(authorization, serialNumber))
                .form(paramMap)
                .execute();
    }


    /**
     * get 请求
     *
     * @param url      请求url
     * @param paramMap 请求参数
     * @return {@link String} 请求返回的结果
     */
    public String get(String url, Map<String, Object> paramMap) {
        return HttpUtil.get(url, paramMap);
    }

    /**
     * post 请求
     *
     * @param url  请求url
     * @param data 请求参数
     * @return {@link String} 请求返回的结果
     */
    public String post(String url, String data) {
        return HttpUtil.post(url, data);
    }

    /**
     * post 请求
     *
     * @param url      请求url
     * @param paramMap 请求参数
     * @return {@link String} 请求返回的结果
     */
    public String post(String url, Map<String, Object> paramMap) {
        return HttpUtil.post(url, paramMap);
    }

    /**
     * post 请求
     *
     * @param url           请求url
     * @param authorization 授权信息
     * @param serialNumber  公钥证书序列号
     * @param jsonData      请求参数
     * @return {@link Map} 请求返回的结果
     */
    public Map<String, Object> postBySafe(String url, String authorization, String serialNumber, String jsonData) {
        HttpResponse httpResponse = postBySafeToResponse(url, authorization, serialNumber, jsonData);
        return buildResMap(httpResponse);
    }

    /**
     * @param url           请求url
     * @param authorization 授权信息
     * @param serialNumber  公钥证书序列号
     * @param jsonData      请求参数
     * @return {@link HttpResponse} 请求返回的结果
     */
    private HttpResponse postBySafeToResponse(String url, String authorization, String serialNumber, String jsonData) {
        return HttpRequest.post(url)
                .addHeaders(getHeaders(authorization, serialNumber))
                .body(jsonData)
                .execute();
    }

    /**
     * delete 请求
     *
     * @param url           请求url
     * @param authorization 授权信息
     * @param serialNumber  公钥证书序列号
     * @param jsonData      请求参数
     * @return {@link Map}  请求返回的结果
     */
    public Map<String, Object> delete(String url, String authorization, String serialNumber, String jsonData) {
        HttpResponse httpResponse = deleteToResponse(url, authorization, serialNumber, jsonData);
        return buildResMap(httpResponse);
    }

    /**
     * delete 请求
     *
     * @param url           请求url
     * @param authorization 授权信息
     * @param serialNumber  公钥证书序列号
     * @param jsonData      请求参数
     * @return {@link HttpResponse} 请求返回的结果
     */
    private HttpResponse deleteToResponse(String url, String authorization, String serialNumber, String jsonData) {
        return HttpRequest.delete(url)
                .addHeaders(getHeaders(authorization, serialNumber))
                .body(jsonData)
                .execute();
    }

    /**
     * 上传文件
     *
     * @param url           请求url
     * @param authorization 授权信息
     * @param serialNumber  公钥证书序列号
     * @param jsonData      请求参数
     * @param file          上传的文件
     * @return {@link Map}  请求返回的结果
     */
    public Map<String, Object> upload(String url, String authorization, String serialNumber, String jsonData, File file) {
        HttpResponse httpResponse = uploadToResponse(url, authorization, serialNumber, jsonData, file);
        return buildResMap(httpResponse);
    }

    /**
     * @param url           请求url
     * @param authorization 授权信息
     * @param serialNumber  公钥证书序列号
     * @param jsonData      请求参数
     * @param file          上传的文件
     * @return {@link HttpResponse} 请求返回的结果
     */
    private HttpResponse uploadToResponse(String url, String authorization, String serialNumber, String jsonData, File file) {
        return HttpRequest.post(url)
                .addHeaders(getUploadHeaders(authorization, serialNumber))
                .form("file", file)
                .form("meta", jsonData)
                .execute();
    }

    /**
     * 构建返回参数
     *
     * @param httpResponse {@link HttpResponse}
     * @return {@link Map}
     */
    private Map<String, Object> buildResMap(HttpResponse httpResponse) {
        Map<String, Object> map = new HashMap<>();
        String timestamp = httpResponse.header("Wechatpay-Timestamp");
        String nonceStr = httpResponse.header("Wechatpay-Nonce");
        String serialNo = httpResponse.header("Wechatpay-Serial");
        String signature = httpResponse.header("Wechatpay-Signature");
        String body = httpResponse.body();
        int status = httpResponse.getStatus();

        map.put("timestamp", timestamp);
        map.put("nonceStr", nonceStr);
        map.put("serialNumber", serialNo);
        map.put("signature", signature);
        map.put("body", body);
        map.put("status", status);

        return map;
    }

    public String upload(String url, String data, String certPath, String certPass, String filePath) {
        try {
            File file = FileUtil.newFile(filePath);
            return HttpRequest.post(url)
                    .setSSLSocketFactory(SSLSocketFactoryBuilder
                            .create()
                            .setProtocol(SSLSocketFactoryBuilder.TLSv1)
                            .setKeyManagers(getKeyManager(certPass, certPath, null))
                            .setSecureRandom(new SecureRandom())
                            .build()
                    )
                    .header("Content-Type", "multipart/form-data;boundary=\"boundary\"")
                    .form("file", file)
                    .form("meta", data)
//                    .body(data)
                    .execute()
                    .body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * post 请求
     *
     * @param url      请求url
     * @param data     请求参数
     * @param certPath 证书路径
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public String post(String url, String data, String certPath, String certPass) {
        try {
            return HttpRequest.post(url)
                    .setSSLSocketFactory(SSLSocketFactoryBuilder
                            .create()
                            .setProtocol(SSLSocketFactoryBuilder.TLSv1)
                            .setKeyManagers(getKeyManager(certPass, certPath, null))
                            .setSecureRandom(new SecureRandom())
                            .build()
                    )
                    .body(data)
                    .execute()
                    .body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * post 请求
     *
     * @param url      请求url
     * @param data     请求参数
     * @param certFile 证书文件输入流
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public String post(String url, String data, InputStream certFile, String certPass) {
        try {
            return HttpRequest.post(url)
                    .setSSLSocketFactory(SSLSocketFactoryBuilder
                            .create()
                            .setProtocol(SSLSocketFactoryBuilder.TLSv1)
                            .setKeyManagers(getKeyManager(certPass, null, certFile))
                            .setSecureRandom(new SecureRandom())
                            .build()
                    )
                    .body(data)
                    .execute()
                    .body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private KeyManager[] getKeyManager(String certPass, String certPath, InputStream certFile) throws Exception {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        if (certFile != null) {
            clientStore.load(certFile, certPass.toCharArray());
        } else {
            clientStore.load(new FileInputStream(certPath), certPass.toCharArray());
        }
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, certPass.toCharArray());
        return kmf.getKeyManagers();
    }

    private Map<String, String> getBaseHeaders(String authorization) {
        String userAgent = String.format(
                "WeChatPay-IJPay-HttpClient/%s (%s) Java/%s",
                getClass().getPackage().getImplementationVersion(),
                OS,
                VERSION == null ? "Unknown" : VERSION);

        Map<String, String> headers = new HashMap<>(3);
        headers.put("Accept", ContentType.JSON.toString());
        headers.put("Authorization", authorization);
        headers.put("User-Agent", userAgent);
        return headers;
    }

    private Map<String, String> getHeaders(String authorization, String serialNumber) {
        Map<String, String> headers = getBaseHeaders(authorization);
        headers.put("Content-Type", ContentType.JSON.toString());
        if (StrUtil.isNotEmpty(serialNumber)) {
            headers.put("Wechatpay-Serial", serialNumber);
        }
        return headers;
    }

    private Map<String, String> getUploadHeaders(String authorization, String serialNumber) {
        Map<String, String> headers = getBaseHeaders(authorization);
        headers.put("Content-Type", "multipart/form-data;boundary=\"boundary\"");
        if (StrUtil.isNotEmpty(serialNumber)) {
            headers.put("Wechatpay-Serial", serialNumber);
        }
        return headers;
    }
}
