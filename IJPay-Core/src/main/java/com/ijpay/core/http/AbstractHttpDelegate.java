package com.ijpay.core.http;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.SSLContextBuilder;
import cn.hutool.core.net.SSLProtocols;
import cn.hutool.http.HttpInterceptor;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.ijpay.core.IJPayHttpResponse;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.InputStream;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>Http 代理类</p>
 *
 * @author Javen
 */
public abstract class AbstractHttpDelegate {

	/**
	 * 代理
	 * @return
	 */
	public Proxy getProxy(){
		return null;
	};

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
	 * @param url      请求url
	 * @param paramMap 请求参数
	 * @return {@link String} 请求返回的结果
	 */
	public String get(String url, Map<String, Object> paramMap) {
		return HttpUtil.get(url, paramMap);
	}

	/**
	 * get 请求
	 *
	 * @param url      请求url
	 * @param paramMap 请求参数
	 * @param headers  请求头
	 * @return {@link IJPayHttpResponse} 请求返回的结果
	 */
	public IJPayHttpResponse get(String url, Map<String, Object> paramMap, Map<String, String> headers) {
		IJPayHttpResponse response = new IJPayHttpResponse();
		HttpResponse httpResponse = getToResponse(url, paramMap, headers);
		if (httpResponse.isGzip()) {
			response.setBodyByte(httpResponse.bodyBytes());
		} else {
			response.setBody(httpResponse.body());
		}
		response.setStatus(httpResponse.getStatus());
		response.setHeaders(httpResponse.headers());
		return response;
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
	 * @param paramMap 请求参数
	 * @param headers  请求头
	 * @return {@link IJPayHttpResponse}  请求返回的结果
	 */
	public IJPayHttpResponse post(String url, Map<String, Object> paramMap, Map<String, String> headers) {
		IJPayHttpResponse response = new IJPayHttpResponse();
		HttpResponse httpResponse = postToResponse(url, headers, paramMap);
		response.setBody(httpResponse.body());
		response.setStatus(httpResponse.getStatus());
		response.setHeaders(httpResponse.headers());
		return response;
	}

	/**
	 * post 请求
	 *
	 * @param url     请求url
	 * @param data    请求参数
	 * @param headers 请求头
	 * @return {@link IJPayHttpResponse}  请求返回的结果
	 */
	public IJPayHttpResponse post(String url, String data, Map<String, String> headers) {
		IJPayHttpResponse response = new IJPayHttpResponse();
		HttpResponse httpResponse = postToResponse(url, headers, data);
		response.setBody(httpResponse.body());
		response.setStatus(httpResponse.getStatus());
		response.setHeaders(httpResponse.headers());
		return response;
	}

	/**
	 * patch 请求
	 *
	 * @param url      请求url
	 * @param paramMap 请求参数
	 * @param headers  请求头
	 * @return {@link IJPayHttpResponse}  请求返回的结果
	 */
	public IJPayHttpResponse patch(String url, Map<String, Object> paramMap, Map<String, String> headers) {
		IJPayHttpResponse response = new IJPayHttpResponse();
		HttpResponse httpResponse = patchToResponse(url, headers, paramMap);
		response.setBody(httpResponse.body());
		response.setStatus(httpResponse.getStatus());
		response.setHeaders(httpResponse.headers());
		return response;
	}

	/**
	 * patch 请求
	 *
	 * @param url     请求url
	 * @param data    请求参数
	 * @param headers 请求头
	 * @return {@link IJPayHttpResponse}  请求返回的结果
	 */
	public IJPayHttpResponse patch(String url, String data, Map<String, String> headers) {
		IJPayHttpResponse response = new IJPayHttpResponse();
		HttpResponse httpResponse = patchToResponse(url, headers, data);
		response.setBody(httpResponse.body());
		response.setStatus(httpResponse.getStatus());
		response.setHeaders(httpResponse.headers());
		return response;
	}

	/**
	 * delete 请求
	 *
	 * @param url      请求url
	 * @param paramMap 请求参数
	 * @param headers  请求头
	 * @return {@link IJPayHttpResponse}  请求返回的结果
	 */
	public IJPayHttpResponse delete(String url, Map<String, Object> paramMap, Map<String, String> headers) {
		IJPayHttpResponse response = new IJPayHttpResponse();
		HttpResponse httpResponse = deleteToResponse(url, headers, paramMap);
		response.setBody(httpResponse.body());
		response.setStatus(httpResponse.getStatus());
		response.setHeaders(httpResponse.headers());
		return response;
	}

	/**
	 * delete 请求
	 *
	 * @param url     请求url
	 * @param data    请求参数
	 * @param headers 请求头
	 * @return {@link IJPayHttpResponse}  请求返回的结果
	 */
	public IJPayHttpResponse delete(String url, String data, Map<String, String> headers) {
		IJPayHttpResponse response = new IJPayHttpResponse();
		HttpResponse httpResponse = deleteToResponse(url, headers, data);
		response.setBody(httpResponse.body());
		response.setStatus(httpResponse.getStatus());
		response.setHeaders(httpResponse.headers());
		return response;
	}

	/**
	 * put 请求
	 *
	 * @param url      请求url
	 * @param paramMap 请求参数
	 * @param headers  请求头
	 * @return {@link IJPayHttpResponse}  请求返回的结果
	 */
	public IJPayHttpResponse put(String url, Map<String, Object> paramMap, Map<String, String> headers) {
		IJPayHttpResponse response = new IJPayHttpResponse();
		HttpResponse httpResponse = putToResponse(url, headers, paramMap);
		response.setBody(httpResponse.body());
		response.setStatus(httpResponse.getStatus());
		response.setHeaders(httpResponse.headers());
		return response;
	}

	/**
	 * put 请求
	 *
	 * @param url     请求url
	 * @param data    请求参数
	 * @param headers 请求头
	 * @return {@link IJPayHttpResponse}  请求返回的结果
	 */
	public IJPayHttpResponse put(String url, String data, Map<String, String> headers) {
		IJPayHttpResponse response = new IJPayHttpResponse();
		HttpResponse httpResponse = putToResponse(url, headers, data);
		response.setBody(httpResponse.body());
		response.setStatus(httpResponse.getStatus());
		response.setHeaders(httpResponse.headers());
		return response;
	}

	/**
	 * 上传文件
	 *
	 * @param url      请求url
	 * @param data     请求参数
	 * @param certPath 证书路径
	 * @param certPass 证书密码
	 * @param filePath 上传文件路径
	 * @param protocol 协议
	 * @return {@link String}  请求返回的结果
	 */
	public String upload(String url, String data, String certPath, String certPass, String filePath, String protocol) {
		try {
			File file = FileUtil.newFile(filePath);
			SSLSocketFactory sslSocketFactory = getSslSocketFactory(certPath, null, certPass, protocol);
			return HttpRequest.post(url)
				.setProxy(getProxy())
				.setSSLSocketFactory(sslSocketFactory)
				.header("Content-Type", "multipart/form-data;boundary=\"boundary\"")
				.form("file", file)
				.form("meta", data)
				.execute()
				.body();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 上传文件
	 *
	 * @param url      请求url
	 * @param data     请求参数
	 * @param certPath 证书路径
	 * @param certPass 证书密码
	 * @param filePath 上传文件路径
	 * @return {@link String}  请求返回的结果
	 */
	public String upload(String url, String data, String certPath, String certPass, String filePath) {
		return upload(url, data, certPath, certPass, filePath, SSLProtocols.TLSv1);
	}

	/**
	 * post 请求
	 *
	 * @param url      请求url
	 * @param data     请求参数
	 * @param certPath 证书路径
	 * @param certPass 证书密码
	 * @param protocol 协议
	 * @return {@link String} 请求返回的结果
	 */
	public String post(String url, String data, String certPath, String certPass, String protocol) {
		try {
			SSLSocketFactory socketFactory = getSslSocketFactory(certPath, null, certPass, protocol);
			return HttpRequest.post(url)
				.setProxy(getProxy())
				.setSSLSocketFactory(socketFactory)
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
	 * @param certPath 证书路径
	 * @param certPass 证书密码
	 * @return {@link String} 请求返回的结果
	 */
	public String post(String url, String data, String certPath, String certPass) {
		return post(url, data, certPath, certPass, SSLProtocols.TLSv1);
	}

	/**
	 * post 请求
	 *
	 * @param url      请求url
	 * @param data     请求参数
	 * @param certFile 证书文件输入流
	 * @param certPass 证书密码
	 * @param protocol 协议
	 * @return {@link String} 请求返回的结果
	 */
	public String post(String url, String data, InputStream certFile, String certPass, String protocol) {
		try {
			SSLSocketFactory sslSocketFactory = getSslSocketFactory(null, certFile, certPass, protocol);
			return HttpRequest.post(url)
				.setProxy(getProxy())
				.setSSLSocketFactory(sslSocketFactory)
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
		return post(url, data, certFile, certPass, SSLProtocols.TLSv1);
	}

	/**
	 * get 请求
	 *
	 * @param url      请求url
	 * @param paramMap 请求参数
	 * @param headers  请求头
	 * @return {@link HttpResponse} 请求返回的结果
	 */
	private HttpResponse getToResponse(String url, Map<String, Object> paramMap, Map<String, String> headers) {
		return HttpRequest.get(url)
			.setProxy(getProxy())
			.addHeaders(headers)
			.form(paramMap)
			.execute();
	}

	/**
	 * post 请求
	 *
	 * @param url     请求url
	 * @param headers 请求头
	 * @param data    请求参数
	 * @return {@link HttpResponse} 请求返回的结果
	 */
	private HttpResponse postToResponse(String url, Map<String, String> headers, String data) {
		return HttpRequest.post(url)
			.setProxy(getProxy())
			.addHeaders(headers)
			.body(data)
			.execute();
	}

	/**
	 * post 请求
	 *
	 * @param url      请求url
	 * @param headers  请求头
	 * @param paramMap 请求参数
	 * @return {@link HttpResponse} 请求返回的结果
	 */
	private HttpResponse postToResponse(String url, Map<String, String> headers, Map<String, Object> paramMap) {
		return HttpRequest.post(url)
			.setProxy(getProxy())
			.addHeaders(headers)
			.form(paramMap)
			.execute();
	}

	/**
	 * patch 请求
	 *
	 * @param url      请求url
	 * @param headers  请求头
	 * @param paramMap 请求参数
	 * @return {@link HttpResponse} 请求返回的结果
	 */
	private HttpResponse patchToResponse(String url, Map<String, String> headers, Map<String, Object> paramMap) {
		return HttpRequest.patch(url)
			.setProxy(getProxy())
			.addHeaders(headers)
			.form(paramMap)
			.execute();
	}

	/**
	 * patch 请求
	 *
	 * @param url     请求url
	 * @param headers 请求头
	 * @param data    请求参数
	 * @return {@link HttpResponse} 请求返回的结果
	 */
	private HttpResponse patchToResponse(String url, Map<String, String> headers, String data) {
		return HttpRequest.patch(url)
			.setProxy(getProxy())
			.addHeaders(headers)
			.body(data)
			.execute();
	}

	/**
	 * delete 请求
	 *
	 * @param url     请求url
	 * @param headers 请求头
	 * @param data    请求参数
	 * @return {@link HttpResponse} 请求返回的结果
	 */
	private HttpResponse deleteToResponse(String url, Map<String, String> headers, String data) {
		return HttpRequest.delete(url)
			.setProxy(getProxy())
			.addHeaders(headers)
			.body(data)
			.execute();
	}

	/**
	 * delete 请求
	 *
	 * @param url      请求url
	 * @param headers  请求头
	 * @param paramMap 请求参数
	 * @return {@link HttpResponse} 请求返回的结果
	 */
	private HttpResponse deleteToResponse(String url, Map<String, String> headers, Map<String, Object> paramMap) {
		return HttpRequest.delete(url)
			.setProxy(getProxy())
			.addHeaders(headers)
			.form(paramMap)
			.execute();
	}

	/**
	 * put 请求
	 *
	 * @param url     请求url
	 * @param headers 请求头
	 * @param data    请求参数
	 * @return {@link HttpResponse} 请求返回的结果
	 */
	private HttpResponse putToResponse(String url, Map<String, String> headers, String data) {
		return HttpRequest.put(url)
			.setProxy(getProxy())
			.addHeaders(headers)
			.body(data)
			.execute();
	}

	/**
	 * put 请求
	 *
	 * @param url      请求url
	 * @param headers  请求头
	 * @param paramMap 请求参数
	 * @return {@link HttpResponse} 请求返回的结果
	 */
	private HttpResponse putToResponse(String url, Map<String, String> headers, Map<String, Object> paramMap) {
		return HttpRequest.put(url)
			.setProxy(getProxy())
			.addHeaders(headers)
			.form(paramMap)
			.execute();
	}


	private KeyManager[] getKeyManager(String certPass, String certPath, InputStream certFile) throws Exception {
		KeyStore clientStore = KeyStore.getInstance("PKCS12");
		if (certFile != null) {
			clientStore.load(certFile, certPass.toCharArray());
		} else {
			clientStore.load(Files.newInputStream(Paths.get(certPath)), certPass.toCharArray());
		}
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(clientStore, certPass.toCharArray());
		return kmf.getKeyManagers();
	}

	private SSLSocketFactory getSslSocketFactory(String certPath, InputStream certFile, String certPass, String protocol) throws Exception {
		SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
		sslContextBuilder.setProtocol(protocol);
		sslContextBuilder.setKeyManagers(getKeyManager(certPass, certPath, certFile));
		sslContextBuilder.setSecureRandom(new SecureRandom());
		return sslContextBuilder.buildChecked().getSocketFactory();
	}
}
