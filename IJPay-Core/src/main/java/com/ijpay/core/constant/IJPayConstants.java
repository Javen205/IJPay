package com.ijpay.core.constant;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>IJPay 常量</p>
 *
 * @author Javen
 */
public interface IJPayConstants {

	/**
	 * 证书序列号固定40字节的字符串
	 */
	int SERIAL_NUMBER_LENGTH = 40;

	/**
	 * 证书颁发者
	 */
	String ISSUER = "CN=Tenpay.com Root CA";

	/**
	 * 证书CN字段
	 */
	String CN = "CN=";

	/**
	 * 处理成功
	 */
	int CODE_200 = 200;

	/**
	 * 服务器已接受请求，但尚未处理
	 */
	int CODE_202 = 202;

	/**
	 * 处理成功，无返回Body
	 */
	int CODE_204 = 204;

	/**
	 * 协议或者参数非法
	 */
	int CODE_400 = 400;

	/**
	 * 签名验证失败
	 */
	int CODE_401 = 401;

	/**
	 * 权限异常
	 */
	int CODE_403 = 403;

	/**
	 * 请求的资源不存在
	 */
	int CODE_404 = 404;

	/**
	 * 请求超过频率限制
	 */
	int CODE_429 = 429;

	/**
	 * 系统错误
	 */
	int CODE_500 = 500;

	/**
	 * 服务下线，暂时不可用
	 */
	int CODE_502 = 502;

	/**
	 * 服务不可用，过载保护
	 */
	int CODE_503 = 503;
}
