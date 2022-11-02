package com.ijpay.core.kit;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.ijpay.core.XmlHelper;
import com.ijpay.core.constant.IJPayConstants;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.model.CertificateModel;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>IJPay 工具类</p>
 *
 * @author Javen
 */
public class PayKit {

	/**
	 * 生成16进制的 sha256 字符串
	 *
	 * @param data 数据
	 * @param key  密钥
	 * @return sha256 字符串
	 */
	public static String hmacSha256(String data, String key) {
		return SecureUtil.hmac(HmacAlgorithm.HmacSHA256, key).digestHex(data);
	}

	/**
	 * SHA1加密文件，生成16进制SHA1字符串<br>
	 *
	 * @param dataFile 被加密文件
	 * @return SHA1 字符串
	 */
	public static String sha1(File dataFile) {
		return SecureUtil.sha1(dataFile);
	}

	/**
	 * SHA1加密，生成16进制SHA1字符串<br>
	 *
	 * @param data 数据
	 * @return SHA1 字符串
	 */
	public static String sha1(InputStream data) {
		return SecureUtil.sha1(data);
	}

	/**
	 * SHA1加密，生成16进制SHA1字符串<br>
	 *
	 * @param data 数据
	 * @return SHA1 字符串
	 */
	public static String sha1(String data) {
		return SecureUtil.sha1(data);
	}

	/**
	 * 生成16进制 MD5 字符串
	 *
	 * @param data 数据
	 * @return MD5 字符串
	 */
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

	/**
	 * 简化的UUID，去掉了横线，使用性能更好的 ThreadLocalRandom 生成UUID
	 *
	 * @return 简化的 UUID，去掉了横线
	 */
	public static String generateStr() {
		return IdUtil.fastSimpleUUID();
	}

	/**
	 * 雪花算法
	 *
	 * @param workerId     终端ID
	 * @param dataCenterId 数据中心ID
	 * @return {@link Snowflake}
	 */
	public static Snowflake getSnowflake(long workerId, long dataCenterId) {
		return IdUtil.getSnowflake(workerId, dataCenterId);
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
		List<String> keys = new ArrayList<>(params.keySet());
		Collections.sort(keys);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			// 拼接时，不包括最后一个&字符
			if (i == keys.size() - 1) {
				if (quotes) {
					content.append(key).append("=").append('"').append(encode ? urlEncode(value) : value).append('"');
				} else {
					content.append(key).append("=").append(encode ? urlEncode(value) : value);
				}
			} else {
				if (quotes) {
					content.append(key).append("=").append('"').append(encode ? urlEncode(value) : value).append('"').append(connStr);
				} else {
					content.append(key).append("=").append(encode ? urlEncode(value) : value).append(connStr);
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
	 * @return xml 字符串
	 */
	public static StringBuffer forEachMap(Map<String, String> params, String prefix, String suffix) {
		StringBuffer xml = new StringBuffer();
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
		StringBuffer xml = forEachMap(params, "<xml>", "</xml>");
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
	 * @param method    {@link RequestMethodEnum} GET,POST,PUT等
	 * @param url       请求接口 /v3/certificates
	 * @param timestamp 获取发起请求时的系统当前时间戳
	 * @param nonceStr  随机字符串
	 * @param body      请求报文主体
	 * @return 待签名字符串
	 */
	public static String buildSignMessage(RequestMethodEnum method, String url, long timestamp, String nonceStr, String body) {
		ArrayList<String> arrayList = new ArrayList<>();
		arrayList.add(method.toString());
		arrayList.add(url);
		arrayList.add(String.valueOf(timestamp));
		arrayList.add(nonceStr);
		arrayList.add(body);
		return buildSignMessage(arrayList);
	}

	/**
	 * 构造签名串
	 *
	 * @param timestamp 应答时间戳
	 * @param nonceStr  应答随机串
	 * @param body      应答报文主体
	 * @return 应答待签名字符串
	 */
	public static String buildSignMessage(String timestamp, String nonceStr, String body) {
		ArrayList<String> arrayList = new ArrayList<>();
		arrayList.add(timestamp);
		arrayList.add(nonceStr);
		arrayList.add(body);
		return buildSignMessage(arrayList);
	}

	/**
	 * 构造签名串
	 *
	 * @param signMessage 待签名的参数
	 * @return 构造后带待签名串
	 */
	public static String buildSignMessage(ArrayList<String> signMessage) {
		if (signMessage == null || signMessage.size() <= 0) {
			return null;
		}
		StringBuilder sbf = new StringBuilder();
		for (String str : signMessage) {
			sbf.append(str).append("\n");
		}
		return sbf.toString();
	}

	/**
	 * v3 接口创建签名
	 *
	 * @param signMessage 待签名的参数
	 * @param keyPath     key.pem 证书路径
	 * @return 生成 v3 签名
	 * @throws Exception 异常信息
	 */
	public static String createSign(ArrayList<String> signMessage, String keyPath) throws Exception {
		return createSign(buildSignMessage(signMessage), keyPath);
	}

	/**
	 * v3 接口创建签名
	 *
	 * @param signMessage 待签名的参数
	 * @param privateKey  商户私钥
	 * @return 生成 v3 签名
	 * @throws Exception 异常信息
	 */
	public static String createSign(ArrayList<String> signMessage, PrivateKey privateKey) throws Exception {
		return createSign(buildSignMessage(signMessage), privateKey);
	}


	/**
	 * v3 接口创建签名
	 *
	 * @param signMessage 待签名的参数
	 * @param keyPath     key.pem 证书路径
	 * @return 生成 v3 签名
	 * @throws Exception 异常信息
	 */
	public static String createSign(String signMessage, String keyPath) throws Exception {
		if (StrUtil.isEmpty(signMessage)) {
			return null;
		}
		// 获取商户私钥
		PrivateKey privateKey = PayKit.getPrivateKey(keyPath);
		// 生成签名
		return RsaKit.encryptByPrivateKey(signMessage, privateKey);
	}

	/**
	 * v3 接口创建签名
	 *
	 * @param signMessage 待签名的参数
	 * @param privateKey  商户私钥
	 * @return 生成 v3 签名
	 * @throws Exception 异常信息
	 */
	public static String createSign(String signMessage, PrivateKey privateKey) throws Exception {
		if (StrUtil.isEmpty(signMessage)) {
			return null;
		}
		// 生成签名
		return RsaKit.encryptByPrivateKey(signMessage, privateKey);
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
	 * @return 请求头 Authorization
	 */
	public static String getAuthorization(String mchId, String serialNo, String nonceStr, String timestamp, String signature, String authType) {
		Map<String, String> params = new HashMap<>(5);
		params.put("mchid", mchId);
		params.put("serial_no", serialNo);
		params.put("nonce_str", nonceStr);
		params.put("timestamp", timestamp);
		params.put("signature", signature);
		return authType.concat(" ").concat(createLinkString(params, ",", false, true));
	}

	/**
	 * 获取商户私钥
	 *
	 * @param keyPath 商户私钥证书路径
	 * @return {@link String} 商户私钥
	 * @throws Exception 异常信息
	 */
	public static String getPrivateKeyStr(String keyPath) throws Exception {
		return RsaKit.getPrivateKeyStr(getPrivateKey(keyPath));
	}

	/**
	 * 获取商户私钥
	 *
	 * @param keyPath 商户私钥证书路径
	 * @return {@link PrivateKey} 商户私钥
	 * @throws Exception 异常信息
	 */
	public static PrivateKey getPrivateKey(String keyPath) throws Exception {
		String originalKey = getCertFileContent(keyPath);
		if (StrUtil.isEmpty(originalKey)) {
			throw new RuntimeException("商户私钥证书获取失败");
		}
		return getPrivateKeyByKeyContent(originalKey);
	}

	/**
	 * 获取商户私钥
	 *
	 * @param originalKey 私钥文本内容
	 * @return {@link PrivateKey} 商户私钥
	 * @throws Exception 异常信息
	 */
	public static PrivateKey getPrivateKeyByKeyContent(String originalKey) throws Exception {
		String privateKey = originalKey
			.replace("-----BEGIN PRIVATE KEY-----", "")
			.replace("-----END PRIVATE KEY-----", "")
			.replaceAll("\\s+", "");
		return RsaKit.loadPrivateKey(privateKey);
	}

	/**
	 * 获取证书
	 *
	 * @param inputStream 证书文件
	 * @return {@link X509Certificate} 获取证书
	 */
	public static X509Certificate getCertificate(InputStream inputStream) {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
			cert.checkValidity();
			return cert;
		} catch (CertificateExpiredException e) {
			throw new RuntimeException("证书已过期", e);
		} catch (CertificateNotYetValidException e) {
			throw new RuntimeException("证书尚未生效", e);
		} catch (CertificateException e) {
			throw new RuntimeException("无效的证书", e);
		}
	}

	/**
	 * 获取证书
	 *
	 * @param path 证书路径，支持相对路径以及绝得路径
	 * @return {@link X509Certificate} 获取证书
	 */
	public static X509Certificate getCertificate(String path) {
		if (StrUtil.isEmpty(path)) {
			return null;
		}
		InputStream inputStream;
		try {
			inputStream = getCertFileInputStream(path);
		} catch (IOException e) {
			throw new RuntimeException("请检查证书路径是否正确", e);
		}
		return getCertificate(inputStream);
	}

	/**
	 * 获取证书详细信息
	 *
	 * @param certificate {@link X509Certificate} 证书
	 * @return {@link CertificateModel}  获取证书详细信息
	 */
	public static CertificateModel getCertificateInfo(X509Certificate certificate) {
		if (null == certificate) {
			return null;
		}
		CertificateModel model = new CertificateModel();
		model.setItself(certificate);
		model.setIssuerDn(certificate.getIssuerDN());
		model.setSubjectDn(certificate.getSubjectDN());
		model.setVersion(certificate.getVersion());
		model.setNotBefore(certificate.getNotBefore());
		model.setNotAfter(certificate.getNotAfter());
		model.setSerialNumber(certificate.getSerialNumber().toString(16));
		return model;
	}

	/**
	 * 获取证书详细信息
	 *
	 * @param path 证书路径，支持相对路径以及绝得路径
	 * @return {@link CertificateModel}  获取证书详细信息
	 */
	public static CertificateModel getCertificateInfo(String path) {
		X509Certificate certificate = getCertificate(path);
		return getCertificateInfo(certificate);
	}

	/**
	 * 检查证书是否可用
	 *
	 * @param model     {@link CertificateModel} 证书详细 model
	 * @param mchId     商户号
	 * @param offsetDay 偏移天数，正数向未来偏移，负数向历史偏移
	 * @return true 有效 false 无效
	 */
	public static boolean checkCertificateIsValid(CertificateModel model, String mchId, int offsetDay) {
		if (null == model) {
			return false;
		}
		Date notAfter = model.getNotAfter();
		if (null == notAfter) {
			return false;
		}
		// 证书颁发者
		Principal issuerDn = model.getIssuerDn();
		if (null == issuerDn || !issuerDn.getName().contains(IJPayConstants.ISSUER)) {
			return false;
		}
		// 证书CN字段
		if (StrUtil.isNotEmpty(mchId)) {
			Principal subjectDn = model.getSubjectDn();
			if (null == subjectDn || !subjectDn.getName().contains(IJPayConstants.CN.concat(mchId.trim()))) {
				return false;
			}
		}
		// 证书序列号固定40字节的字符串
		String serialNumber = model.getSerialNumber();
		if (StrUtil.isEmpty(serialNumber) || serialNumber.length() != IJPayConstants.SERIAL_NUMBER_LENGTH) {
			return false;
		}
		// 偏移后的时间
		DateTime dateTime = DateUtil.offsetDay(notAfter, offsetDay);
		DateTime now = DateUtil.date();
		int compare = DateUtil.compare(dateTime, now);
		return compare >= 0;
	}

	/**
	 * 检查证书是否可用
	 *
	 * @param certificate {@link X509Certificate} 证书
	 * @param mchId       商户号
	 * @param offsetDay   偏移天数，正数向未来偏移，负数向历史偏移
	 * @return true 有效 false 无效
	 */
	public static boolean checkCertificateIsValid(X509Certificate certificate, String mchId, int offsetDay) {
		if (null == certificate) {
			return false;
		}
		CertificateModel model = getCertificateInfo(certificate);
		return checkCertificateIsValid(model, mchId, offsetDay);
	}

	/**
	 * 检查证书是否可用
	 *
	 * @param path      证书路径，支持相对路径以及绝得路径
	 * @param mchId     商户号
	 * @param offsetDay 偏移天数，正数向未来偏移，负数向历史偏移
	 * @return true 有效 false 无效
	 */
	public static boolean checkCertificateIsValid(String path, String mchId, int offsetDay) {
		return checkCertificateIsValid(getCertificateInfo(path), mchId, offsetDay);
	}

	/**
	 * 公钥加密
	 *
	 * @param data        待加密数据
	 * @param certificate 平台公钥证书
	 * @return 加密后的数据
	 * @throws Exception 异常信息
	 */
	public static String rsaEncryptOAEP(String data, X509Certificate certificate) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());

			byte[] dataByte = data.getBytes(StandardCharsets.UTF_8);
			byte[] cipherData = cipher.doFinal(dataByte);
			return Base64.encode(cipherData);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
		} catch (InvalidKeyException e) {
			throw new IllegalArgumentException("无效的证书", e);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new IllegalBlockSizeException("加密原串的长度不能超过214字节");
		}
	}

	/**
	 * 私钥解密
	 *
	 * @param cipherText 加密字符
	 * @param privateKey 私钥
	 * @return 解密后的数据
	 * @throws Exception 异常信息
	 */
	public static String rsaDecryptOAEP(String cipherText, PrivateKey privateKey) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] data = Base64.decode(cipherText);
			return new String(cipher.doFinal(data), StandardCharsets.UTF_8);
		} catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
			throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
		} catch (InvalidKeyException e) {
			throw new IllegalArgumentException("无效的私钥", e);
		} catch (BadPaddingException | IllegalBlockSizeException e) {
			throw new BadPaddingException("解密失败");
		}
	}

	/**
	 * 传入 classPath 静态资源路径返回文件输入流
	 *
	 * @param classPath 静态资源路径
	 * @return InputStream
	 */
	public static InputStream getFileToStream(String classPath) {
		Resource resource = new ClassPathResource(classPath);
		return resource.getStream();
	}

	/**
	 * 传入 classPath 静态资源路径返回绝对路径
	 *
	 * @param classPath 静态资源路径
	 * @return 绝对路径
	 */
	public static String getAbsolutePath(String classPath) {
		return new ClassPathResource(classPath).getAbsolutePath();
	}

	/**
	 * 通过路径获取证书文件的输入流
	 *
	 * @param path 文件路径
	 * @return 文件流
	 * @throws IOException 异常信息
	 */
	public static InputStream getCertFileInputStream(String path) throws IOException {
		if (StrUtil.isBlank(path)) {
			return null;
		}
		// 绝对地址
		File file = new File(path);
		if (file.exists()) {
			return Files.newInputStream(file.toPath());
		}
		// 相对地址
		return getFileToStream(path);
	}

	/**
	 * 通过路径获取证书文件的内容
	 *
	 * @param path 文件路径
	 * @return 文件内容
	 */
	public static String getCertFileContent(String path) {
		if (StrUtil.isBlank(path)) {
			return null;
		}
		// 绝对地址
		File file = new File(path);
		if (!file.exists()) {
			path = getAbsolutePath(path);
		}
		return FileUtil.readUtf8String(path);
	}
}
