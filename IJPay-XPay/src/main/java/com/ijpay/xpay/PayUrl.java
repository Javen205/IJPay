package com.ijpay.xpay;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信官方个人支付渠道，有稳定的异步通知，加企鹅(572839485)了解更多</p>
 *
 * <p>XPay Api</p>
 *
 * @author Javen
 */
public interface PayUrl {
	/**
	 * 获取接口地址
	 *
	 * @return 地址
	 */
	String getUrl();
}
