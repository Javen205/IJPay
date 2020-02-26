package com.ijpay.core.http;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
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
     * @param paramMap      请求参数
     * @return {@link String} 请求返回的结果
     */
    public String get(String url, String authorization, Map<String, Object> paramMap) {
        return HttpRequest.get(url)
                .addHeaders(getHeaders(authorization))
                .form(paramMap)
                .execute()
                .body();
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
     * @param url      请求url
     * @param jsonData 请求参数
     * @return {@link String} 请求返回的结果
     */
    public String post(String url, String authorization, String jsonData) {
        return HttpRequest.post(url)
                .addHeaders(getHeaders(authorization))
                .body(jsonData)
                .execute()
                .body();
    }

    public String upload(String url, String authorization, String jsonData, File file) {

        return HttpRequest.post(url)
                .form("file", file)
                .addHeaders(getUploadHeaders(authorization))
                .body(jsonData)
                .execute()
                .body();
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

    private Map<String, String> getHeaders(String authorization) {
        String userAgent = String.format(
                "WeChatPay-IJPay-HttpClient/%s (%s) Java/%s",
                getClass().getPackage().getImplementationVersion(),
                OS,
                VERSION == null ? "Unknown" : VERSION);

        Map<String, String> headers = new HashMap<>(4);
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        headers.put("Authorization", authorization);
        headers.put("User-Agent", userAgent);
        return headers;
    }

    private Map<String, String> getUploadHeaders(String authorization) {
        String userAgent = String.format(
                "WeChatPay-IJPay-HttpClient/%s (%s) Java/%s",
                getClass().getPackage().getImplementationVersion(),
                OS,
                VERSION == null ? "Unknown" : VERSION);

        Map<String, String> headers = new HashMap<>(4);
        headers.put("Content-Type", ContentType.MULTIPART.toString());
        headers.put("Accept", ContentType.JSON.toString());
        headers.put("Authorization", authorization);
        headers.put("User-Agent", userAgent);
        return headers;
    }
}
