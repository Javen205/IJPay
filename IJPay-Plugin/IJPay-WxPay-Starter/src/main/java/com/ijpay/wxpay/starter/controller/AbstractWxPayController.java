package com.ijpay.wxpay.starter.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.constant.IJPayConstants;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.AesUtil;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.core.model.CertificateModel;
import com.ijpay.core.utils.DateTimeZoneUtil;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.OtherApiEnum;
import com.ijpay.wxpay.model.v3.Certificate;
import com.ijpay.wxpay.model.v3.CertificateInfo;
import com.ijpay.wxpay.model.v3.EncryptCertificate;
import com.ijpay.wxpay.starter.properties.WxPayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * @author Javen
 */
@Slf4j
@Component
public class AbstractWxPayController extends AbstractWxPayApiController {

	/**
	 * starter 自动生成的微信支付配置,默认取配置的第一个
	 */
	@Autowired
	protected WxPayApiConfig wxPayApiConfig;

	/**
	 * yml 配置
	 */
	@Autowired
	protected WxPayProperties wxPayProperties;

	/***
	 * 商户证书
	 */
	protected CertificateModel certificateModel;

	/**
	 * 平台证书
	 */
	protected CertificateModel platformCertificateModel;

	/**
	 * 获取微信支付配置
	 *
	 * @return {@link WxPayApiConfig} 微信支付配置
	 */
	@Override
	public WxPayApiConfig getApiConfig() {
		WxPayApiConfig apiConfig;
		try {
			apiConfig = WxPayApiConfigKit.getWxPayApiConfig();
		} catch (Exception e) {
			apiConfig = wxPayApiConfig;
		}
		return apiConfig;
	}

	@RequestMapping(value = "/onlineContact", method = {RequestMethod.GET, RequestMethod.POST})
	public String onlineContact() {
		return IJPayConstants.ONLINE_CONTACT;
	}

	/**
	 * 自动获取最新微信平台证书
	 *
	 * @param serialNumber 指定保存的平台证书序列号
	 * @return 微信平台证书保存结果
	 * @throws Exception 异常信息
	 */
	@GetMapping("/autoUpdateCertificate")
	public boolean autoUpdateCertificate(@RequestParam(value = "serialNumber", required = false) String serialNumber)
		throws Exception {
		return autoUpdateOrGetCertificate(serialNumber);
	}

	/**
	 * 自动更新获取证书
	 *
	 * @param serialNumber 指定保存的平台证书序列号
	 * @return 获取证书结果
	 * @throws Exception 异常信息
	 */
	public boolean autoUpdateOrGetCertificate(String serialNumber) throws Exception {
		WxPayApiConfig config = WxPayApiConfigKit.getWxPayApiConfig();
		// 获取平台证书列表
		IJPayHttpResponse response = WxPayApi.v3(
			RequestMethodEnum.GET,
			WxDomainEnum.CHINA.toString(),
			OtherApiEnum.GET_CERTIFICATES.toString(),
			config.getMchId(),
			getSerialNumber(),
			null,
			config.getKeyPath(),
			""
		);
		// 接口响应使用的证书序列号，签名会使用到
		String responseSerialNumber = response.getHeader("Wechatpay-Serial");
		String body = response.getBody();
		int status = response.getStatus();
		if (status == IJPayConstants.CODE_200) {
			Certificate model = JSONUtil.toBean(body, Certificate.class);
			if (null == model) {
				log.debug("解析返回数据失败");
				return false;
			}
			List<CertificateInfo> data = model.getData();
			if (CollUtil.isEmpty(data)) {
				log.debug("未获取到任何有效平台证书");
				return false;
			}
			log.debug("总共获取到 {} 个平台证书", data.size());
			CertificateInfo certificateInfo = null;
			// 获取指定序列号的平台证书
			if (StrUtil.isNotEmpty(serialNumber)) {
				Optional<CertificateInfo> optional = data.stream()
					.filter(item -> serialNumber.equals(item.getSerial_no()))
					.findFirst();

				if (optional.isPresent()) {
					certificateInfo = optional.get();
				}
			}
			// 指定序列号的平台证书不存在，遍历获取可用的平台证书
			if (null == certificateInfo) {
				log.debug("指定序列号 {} 的平台证书不存在，开始遍历获取可用的平台证书", serialNumber);
				for (CertificateInfo info : data) {
					if (null == info) {
						continue;
					}
					String expireTime = info.getExpire_time();
					String expireTimeStr = DateTimeZoneUtil.timeZoneDateToStr(expireTime);
					Date expireDate = DateUtil.parse(expireTimeStr);
					if (expireDate.before(new Date())) {
						log.debug("序列号 {} 对应的平台证书已过期", info.getSerial_no());
						continue;
					}
					log.debug("序列号 {} 对应的平台证书可用,忽略其他证书", info.getSerial_no());
					certificateInfo = info;
					break;
				}
			}
			if (null == certificateInfo) {
				log.debug("未获取到平台证书");
				return false;
			}
			// 保存证书的序列号
			String serialNo = certificateInfo.getSerial_no();
			EncryptCertificate encryptCertificate = certificateInfo.getEncrypt_certificate();
			String associatedData = encryptCertificate.getAssociated_data();
			String cipherText = encryptCertificate.getCiphertext();
			String nonce = encryptCertificate.getNonce();
			boolean isOk = savePlatformCert(associatedData, nonce, cipherText, config.getPlatformCertPath());
			if (isOk) {
				log.debug("平台证书保存成功,序列号为 {} 保存路径为 {}", serialNo, config.getPlatformCertPath());
				if (StrUtil.equals(responseSerialNumber, serialNo)) {
					// 根据证书序列号查询对应的证书来验证签名结果
					boolean verifySignature = WxPayKit.verifySignature(response, config.getPlatformCertPath());
					log.debug("使用序列号 {} 对应的平台证书签名验证结果 {}", serialNo, verifySignature);
					return verifySignature;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取证书信息
	 *
	 * @return {@link CertificateModel} 证书信息
	 */
	protected CertificateModel getCertificateInfo(String path) {
		return PayKit.getCertificateInfo(path);
	}

	/**
	 * 获取平台证书信息
	 *
	 * @return {@link CertificateModel} 证书信息
	 */
	public CertificateModel getPlatformCertificateInfo() {
		if (null == platformCertificateModel) {
			WxPayApiConfig config = WxPayApiConfigKit.getWxPayApiConfig();
			platformCertificateModel = getCertificateInfo(config.getPlatformCertPath());
		}
		return platformCertificateModel;
	}

	/**
	 * 获取商户证书信息
	 *
	 * @return {@link CertificateModel} 证书信息
	 */
	protected CertificateModel getCertificateInfo() {
		if (null == certificateModel) {
			WxPayApiConfig config = WxPayApiConfigKit.getWxPayApiConfig();
			certificateModel = getCertificateInfo(config.getCertPath());
		}
		return certificateModel;
	}

	/**
	 * 获取商户证书序列号
	 *
	 * @return 证书序列号
	 */
	protected String getSerialNumber() {
		CertificateModel certificateInfo = getCertificateInfo();
		if (null == certificateInfo) {
			return null;
		}
		return getCertificateInfo().getSerialNumber();
	}

	/**
	 * 获取平台证书序列号
	 *
	 * @return 证书序列号
	 */
	protected String getPlatformSerialNumber() {
		CertificateModel certificateInfo = getPlatformCertificateInfo();
		if (null == certificateInfo) {
			return null;
		}
		return getCertificateInfo().getSerialNumber();
	}

	/**
	 * 保存证书
	 *
	 * @param associatedData 关联数据
	 * @param nonce          随机数
	 * @param cipherText     加密报文
	 * @param certPath       证书路径
	 * @return 保存结果
	 * @throws Exception 异常信息
	 */
	protected boolean savePlatformCert(String associatedData, String nonce, String cipherText, String certPath) throws Exception {
		WxPayApiConfig config = WxPayApiConfigKit.getWxPayApiConfig();
		AesUtil aesUtil = new AesUtil(config.getApiKey3().getBytes(StandardCharsets.UTF_8));
		// 平台证书密文解密 encrypt_certificate 中的  associated_data nonce  ciphertext
		String publicKey = aesUtil.decryptToString(
			associatedData.getBytes(StandardCharsets.UTF_8),
			nonce.getBytes(StandardCharsets.UTF_8),
			cipherText
		);
		// 保存证书
		FileWriter writer = new FileWriter(certPath);
		File file = writer.write(publicKey);
		return file.isFile() && file.exists();
	}
}
