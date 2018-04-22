package com.jpay.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import com.jpay.ext.kit.StrKit;

/**
 * 对HttpKit添加功能
 * @author L.cm
 *
 */
class HttpKitExt {
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";

    /**
     * 上传临时素材，本段代码来自老版本（____′↘夏悸 / wechat），致敬！
     * @param url 图片上传地址
     * @param file 需要上传的文件
     * @return ApiResult
     * @throws IOException
     */
    protected static String uploadMedia(String url, File file, String params) throws IOException {
        URL urlGet = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlGet.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent", DEFAULT_USER_AGENT);
        conn.setRequestProperty("Charsert", "UTF-8");
        // 定义数据分隔线
        String BOUNDARY = "----WebKitFormBoundaryiDGnV9zdZA1eM1yL";
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        OutputStream out = new DataOutputStream(conn.getOutputStream());
        // 定义最后数据分隔线
        StringBuilder mediaData = new StringBuilder();
        mediaData.append("--").append(BOUNDARY).append("\r\n");
        mediaData.append("Content-Disposition: form-data;name=\"media\";filename=\""+ file.getName() + "\"\r\n");
        mediaData.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] mediaDatas = mediaData.toString().getBytes();
        out.write(mediaDatas);
        DataInputStream fs = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = fs.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        IOUtils.closeQuietly(fs);
        // 多个文件时，二个文件之间加入这个
        out.write("\r\n".getBytes());
        if (StrKit.notBlank(params)) {
            StringBuilder paramData = new StringBuilder();
            paramData.append("--").append(BOUNDARY).append("\r\n");
            paramData.append("Content-Disposition: form-data;name=\"description\";");
            byte[] paramDatas = paramData.toString().getBytes();
            out.write(paramDatas);
            out.write(params.getBytes(Charsets.UTF_8));
        }
        byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
        out.write(end_data);
        out.flush();
        IOUtils.closeQuietly(out);

        // 定义BufferedReader输入流来读取URL的响应
        InputStream in = conn.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in, Charsets.UTF_8));
        String valueString = null;
        StringBuffer bufferRes = null;
        bufferRes = new StringBuffer();
        while ((valueString = read.readLine()) != null){
            bufferRes.append(valueString);
        }
        IOUtils.closeQuietly(in);
        // 关闭连接
        if (conn != null) {
            conn.disconnect();
        }
        return bufferRes.toString();
    }

    /**
     * 获取永久素材
     * @param url 素材地址
     * @return params post参数
     * @return InputStream 流，考虑到这里可能返回json或file
     * @throws IOException
     */
    protected static InputStream downloadMaterial(String url, String params) throws IOException {
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        // 连接超时
        conn.setConnectTimeout(25000);
        // 读取超时 --服务器响应比较慢，增大时间
        conn.setReadTimeout(25000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "Keep-Alive");
        conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.connect();
        if (StrKit.notBlank(params)) {
            OutputStream out = conn.getOutputStream();
            out.write(params.getBytes(Charsets.UTF_8));
            out.flush();
            IOUtils.closeQuietly(out);
        }
        return conn.getInputStream();
    }

   

    /**
     * 涉及资金回滚的接口会使用到商户证书，包括退款、撤销接口的请求
     * @param url 请求的地址
     * @param data xml数据
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return String 回调的xml信息
     */
    protected static String postSSL(String url, String data, String certPath, String certPass) {
        HttpsURLConnection conn = null;
        OutputStream out = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");
            clientStore.load(new FileInputStream(certPath), certPass.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientStore, certPass.toCharArray());
            KeyManager[] kms = kmf.getKeyManagers();
            SSLContext sslContext = SSLContext.getInstance("TLSv1");

            sslContext.init(kms, null, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            URL _url = new URL(url);
            conn = (HttpsURLConnection) _url.openConnection();

            conn.setConnectTimeout(25000);
            conn.setReadTimeout(25000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
            conn.connect();

            out = conn.getOutputStream();
            out.write(data.getBytes(Charsets.UTF_8));
            out.flush();

            inputStream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(inputStream);
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

}
