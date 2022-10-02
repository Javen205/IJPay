package com.ijpay.wxpay.model.v3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
 * <p>V3 统一下单-场景信息</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class SceneInfo {
	/**
	 * 用户终端IP
	 */
	private String payer_client_ip;
	/**
	 * 商户端设备号
	 */
	private String device_id;
	/**
	 * 商户门店信息
	 */
	private StoreInfo store_info;
	/**
	 * H5 场景信息
	 */
	private H5Info h5_info;
}
