package com.ijpay.wxpay.model.v3;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>V3 统一下单-H5 场景信息</p>
 *
 * @author Javen
 */
@Data
@Accessors(chain = true)
public class H5Info {
	/**
	 * 场景类型
	 */
	private String type;
	/**
	 * 应用名称
	 */
	private String app_name;
	/**
	 * 网站URL
	 */
	private String app_url;
	/**
	 * iOS 平台 BundleID
	 */
	private String bundle_id;
	/**
	 * Android 平台 PackageName
	 */
	private String package_name;
}
