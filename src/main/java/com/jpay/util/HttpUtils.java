package com.jpay.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

import com.jpay.ext.kit.StrKit;


/**
 * JFinal-weixin Http请求工具类
 * @author L.cm
 */
public final class HttpUtils {
	
	private HttpUtils() {}
	
	public static String get(String url) {
		return delegate.get(url);
	}
	
	public static String get(String url, Map<String, String> queryParas) {
		return delegate.get(url, queryParas);
	}
	
	public static String post(String url, String data) {
		return delegate.post(url, data);
	}
	public static String post(String url, Map<String, String> queryParas) {
		return delegate.post(url, queryParas);
	}
	
	public static String postSSL(String url, String data, String certPath, String certPass) {
		return delegate.postSSL(url, data, certPath, certPass);
	}
	
	public static InputStream download(String url, String params){
		return delegate.download(url, params);
	}
	
	public static String upload(String url, File file, String params) {
		return delegate.upload(url, file, params);
	}
	
	/**
	 * http请求工具 委托
	 * 优先使用OkHttp 
	 * 最后使用JFinal HttpKit
	 */
	private interface HttpDelegate {
		String get(String url);
		String get(String url, Map<String, String> queryParas);
		
		String post(String url, String data);
		String post(String url, Map<String, String> queryParas);
		String postSSL(String url, String data, String certPath, String certPass);
		
		InputStream download(String url, String params);
		
		String upload(String url, File file, String params);
	}
	
	// http请求工具代理对象
	private static final HttpDelegate delegate;

	static {
		HttpDelegate delegateToUse = null;
		// okhttp3.OkHttpClient?
		if (ClassUtils.isPresent("okhttp3.OkHttpClient", HttpUtils.class.getClassLoader())) {
			delegateToUse = new OkHttp3Delegate();
		}
		// com.squareup.okhttp.OkHttpClient?
		else if (ClassUtils.isPresent("com.squareup.okhttp.OkHttpClient", HttpUtils.class.getClassLoader())) {
			delegateToUse = new OkHttpDelegate();
		}//com.jpay.ext.kit.HttpKit?
        else if (ClassUtils.isPresent("com.jpay.ext.kit.HttpKit", HttpUtils.class.getClassLoader())) {
            delegateToUse = new HttpKitDelegate();
        }
		delegate = delegateToUse;
	}
	
	/**
	 * OkHttp2代理
	 */
	private static class OkHttpDelegate implements HttpDelegate {
		private final com.squareup.okhttp.OkHttpClient httpClient;
		private final com.squareup.okhttp.OkHttpClient httpsClient;
		
		Lock lock = new ReentrantLock();
		
		public OkHttpDelegate() {
			httpClient = new com.squareup.okhttp.OkHttpClient();
			// 分别设置Http的连接,写入,读取的超时时间
			httpClient.setConnectTimeout(10, TimeUnit.SECONDS);
			httpClient.setWriteTimeout(10, TimeUnit.SECONDS);
			httpClient.setReadTimeout(30, TimeUnit.SECONDS);
			
			httpsClient = httpClient.clone();
		}
		
		private static final com.squareup.okhttp.MediaType CONTENT_TYPE_FORM = 
				com.squareup.okhttp.MediaType.parse("application/x-www-form-urlencoded");
		
		private String exec(com.squareup.okhttp.Request request) {
			try {
				com.squareup.okhttp.Response response = httpClient.newCall(request).execute();
				
				if (!response.isSuccessful()) throw new RuntimeException("Unexpected code " + response);
				
				return response.body().string();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public String get(String url) {
			com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(url).get().build();
			return exec(request);
		}
		
		@Override
		public String get(String url, Map<String, String> queryParas) {
			com.squareup.okhttp.HttpUrl.Builder urlBuilder = com.squareup.okhttp.HttpUrl.parse(url).newBuilder();
			for (Entry<String, String> entry : queryParas.entrySet()) {
				urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
			}
			com.squareup.okhttp.HttpUrl httpUrl = urlBuilder.build();
			com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(httpUrl).get().build();
			return exec(request);
		}
		
		
		
		@Override
		public String post(String url, String params) {
			com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(CONTENT_TYPE_FORM, params);
			com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
				.url(url)
				.post(body)
				.build();
			return exec(request);
		}
		
		
		@Override
		public String post(String url, Map<String, String> queryParas) {
			com.squareup.okhttp.FormEncodingBuilder builder = new com.squareup.okhttp.FormEncodingBuilder(); 
			for (Entry<String, String> entry : queryParas.entrySet()) {
				builder.add(entry.getKey(), entry.getValue());
			}
			com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
					.url(url)
					.post(builder.build())
					.build();
			return exec(request);
		}
		
		@Override
		public String postSSL(String url, String data, String certPath, String certPass) {
			com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(CONTENT_TYPE_FORM, data);
			com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
				.url(url)
				.post(body)
				.build();
			
			InputStream inputStream = null;
			try {
				// 移动到最开始，certPath io异常unlock会报错
				lock.lock();
				
				KeyStore clientStore = KeyStore.getInstance("PKCS12");
				inputStream = new FileInputStream(certPath);
				clientStore.load(inputStream, certPass.toCharArray());
				
				KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				kmf.init(clientStore, certPass.toCharArray());
				KeyManager[] kms = kmf.getKeyManagers();
				SSLContext sslContext = SSLContext.getInstance("TLSv1");
				
				sslContext.init(kms, null, new SecureRandom());
				
				httpsClient.setSslSocketFactory(sslContext.getSocketFactory());
				
				com.squareup.okhttp.Response response = httpsClient.newCall(request).execute();
				
				if (!response.isSuccessful()) throw new RuntimeException("Unexpected code " + response);
				
				return response.body().string();
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(inputStream);
				lock.unlock();
			}
		}
		
		
		
		@Override
		public InputStream download(String url, String params) {
			com.squareup.okhttp.Request request;
			if (StrKit.notBlank(params)) {
				com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(CONTENT_TYPE_FORM, params);
				request = new com.squareup.okhttp.Request.Builder().url(url).post(body).build();
			} else {
				request = new com.squareup.okhttp.Request.Builder().url(url).get().build();
			}
			try {
				com.squareup.okhttp.Response response = httpClient.newCall(request).execute();
				
				if (!response.isSuccessful()) throw new RuntimeException("Unexpected code " + response);
				
				return response.body().byteStream();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
		}
		
		@Override
		public String upload(String url, File file, String params) {
			com.squareup.okhttp.RequestBody fileBody = com.squareup.okhttp.RequestBody
					.create(com.squareup.okhttp.MediaType.parse("application/octet-stream"), file);
			
			com.squareup.okhttp.MultipartBuilder builder = new com.squareup.okhttp.MultipartBuilder()
					.type(com.squareup.okhttp.MultipartBuilder.FORM)
					.addFormDataPart("media", file.getName(), fileBody);
			
			if (StrKit.notBlank(params)) {
				builder.addFormDataPart("description", params);
			}
			
			com.squareup.okhttp.RequestBody requestBody = builder.build();
			com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
					.url(url)
					.post(requestBody)
					.build();
			
			return exec(request);
		}

		
		
	}
	
	/**
	 * OkHttp3代理
	 */
	private static class OkHttp3Delegate implements HttpDelegate {
		private okhttp3.OkHttpClient httpClient;
		
		public OkHttp3Delegate() {
			// 分别设置Http的连接,写入,读取的超时时间
			httpClient = new okhttp3.OkHttpClient().newBuilder()
					.connectTimeout(10, TimeUnit.SECONDS)
					.writeTimeout(10, TimeUnit.SECONDS)
					.readTimeout(30, TimeUnit.SECONDS)
					.build();
		}
		
		private static final okhttp3.MediaType CONTENT_TYPE_FORM = 
				okhttp3.MediaType.parse("application/x-www-form-urlencoded");
		
		private String exec(okhttp3.Request request) {
			try {
				okhttp3.Response response = httpClient.newCall(request).execute();
				
				if (!response.isSuccessful()) throw new RuntimeException("Unexpected code " + response);
				
				return response.body().string();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public String get(String url) {
			okhttp3.Request request = new okhttp3.Request.Builder().url(url).get().build();
			return exec(request);
		}
		
		@Override
		public String get(String url, Map<String, String> queryParas) {
			okhttp3.HttpUrl.Builder urlBuilder = okhttp3.HttpUrl.parse(url).newBuilder();
			for (Entry<String, String> entry : queryParas.entrySet()) {
				urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
			}
			okhttp3.HttpUrl httpUrl = urlBuilder.build();
			okhttp3.Request request = new okhttp3.Request.Builder().url(httpUrl).get().build();
			return exec(request);
		}
		
		@Override
		public String post(String url, String params) {
			okhttp3.RequestBody body = okhttp3.RequestBody.create(CONTENT_TYPE_FORM, params);
			okhttp3.Request request = new okhttp3.Request.Builder()
				.url(url)
				.post(body)
				.build();
			return exec(request);
		}
		
		@Override
		public String postSSL(String url, String data, String certPath, String certPass) {
			okhttp3.RequestBody body = okhttp3.RequestBody.create(CONTENT_TYPE_FORM, data);
			okhttp3.Request request = new okhttp3.Request.Builder()
				.url(url)
				.post(body)
				.build();
			
			InputStream inputStream = null;
			try {
				KeyStore clientStore = KeyStore.getInstance("PKCS12");
				inputStream = new FileInputStream(certPath);
				char[] passArray = certPass.toCharArray();
				clientStore.load(inputStream, passArray);
				
				KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				kmf.init(clientStore, passArray);
				KeyManager[] kms = kmf.getKeyManagers();
				SSLContext sslContext = SSLContext.getInstance("TLSv1");
				
				sslContext.init(kms, null, new SecureRandom());
				
				@SuppressWarnings("deprecation")
				okhttp3.OkHttpClient httpsClient = new okhttp3.OkHttpClient()
						.newBuilder()
						.connectTimeout(10, TimeUnit.SECONDS)
						.writeTimeout(10, TimeUnit.SECONDS)
						.readTimeout(30, TimeUnit.SECONDS)
						.sslSocketFactory(sslContext.getSocketFactory())
						.build();
				
				okhttp3.Response response = httpsClient.newCall(request).execute();
				
				if (!response.isSuccessful()) throw new RuntimeException("Unexpected code " + response);
				
				return response.body().string();
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
		}
		
		
		
		@Override
		public InputStream download(String url, String params) {
			okhttp3.Request request;
			if (StrKit.notBlank(params)) {
				okhttp3.RequestBody body = okhttp3.RequestBody.create(CONTENT_TYPE_FORM, params);
				request = new okhttp3.Request.Builder().url(url).post(body).build();
			} else {
				request = new okhttp3.Request.Builder().url(url).get().build();
			}
			try {
				okhttp3.Response response = httpClient.newCall(request).execute();
				
				if (!response.isSuccessful()) throw new RuntimeException("Unexpected code " + response);
				
				return response.body().byteStream();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
		}
		
		@Override
		public String upload(String url, File file, String params) {
			okhttp3.RequestBody fileBody = okhttp3.RequestBody
					.create(okhttp3.MediaType.parse("application/octet-stream"), file);
			
			okhttp3.MultipartBody.Builder builder = new okhttp3.MultipartBody.Builder()
					.setType(okhttp3.MultipartBody.FORM)
					.addFormDataPart("media", file.getName(), fileBody);
			
			if (StrKit.notBlank(params)) {
				builder.addFormDataPart("description", params);
			}
			
			okhttp3.RequestBody requestBody = builder.build();
			okhttp3.Request request = new okhttp3.Request.Builder()
					.url(url)
					.post(requestBody)
					.build();
			
			return exec(request);
		}

		@Override
		public String post(String url, Map<String, String> params) {
			okhttp3.FormBody.Builder builder = new okhttp3.FormBody.Builder();
			 if(params == null){
				 params = new HashMap<String, String>();
			 }
			 if(params != null){
				 Set<Map.Entry<String,String>> entries = params.entrySet();
				 for(Map.Entry<String,String> entry:entries){
					  builder.add(entry.getKey(),entry.getValue());
				 }
			 }
			okhttp3.Request request = new okhttp3.Request.Builder().url(url).post(builder.build()).build();
			return exec(request);
		}
		
	}
	
	 /**
     * HttpKit代理
     */
    private static class HttpKitDelegate implements HttpDelegate {

        @Override
        public String get(String url) {
            return com.jpay.ext.kit.HttpKit.get(url);
        }

        @Override
        public String get(String url, Map<String, String> queryParas) {
            return com.jpay.ext.kit.HttpKit.get(url, queryParas);
        }

        @Override
        public String post(String url, String data) {
            return com.jpay.ext.kit.HttpKit.post(url, data);
        }
        @Override
		public String post(String url, Map<String, String> queryParas) {
			return com.jpay.ext.kit.HttpKit.post(url, queryParas, "null");
		}

        @Override
        public String postSSL(String url, String data, String certPath, String certPass) {
            return HttpKitExt.postSSL(url, data, certPath, certPass);
        }


        @Override
        public InputStream download(String url, String params) {
            try {
                return HttpKitExt.downloadMaterial(url, params);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String upload(String url, File file, String params) {
            try {
                return HttpKitExt.uploadMedia(url, file, params);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
