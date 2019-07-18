package com.ijpay.wxpay;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.ssl.SSLSocketFactoryBuilder;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;

/**
 * Http 代理类
 *
 * @author Javen
 */
public abstract class HttpDelegate {

    protected String get(String url) {
        return HttpUtil.get(url);
    }

    protected String get(String url, Map<String, Object> paramMap) {
        return HttpUtil.get(url, paramMap);
    }

    protected String post(String url, String data) {
        return HttpUtil.post(url, data);
    }

    protected  String post(String url, Map<String, Object> paramMap){
        return HttpUtil.post(url, paramMap);
    }

    protected  String postSSL(String url, String data, String certPath, String certPass){
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

    protected  String postSSL(String url, String data, InputStream certFile, String certPass){
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
        if (certFile != null) clientStore.load(certFile, certPass.toCharArray());
        else clientStore.load(new FileInputStream(certPath), certPass.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, certPass.toCharArray());
        return kmf.getKeyManagers();
    }
}
