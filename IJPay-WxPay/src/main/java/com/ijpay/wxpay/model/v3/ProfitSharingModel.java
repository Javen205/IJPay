package com.ijpay.wxpay.model.v3;

import com.ijpay.core.model.BaseModel;
import com.ijpay.wxpay.model.ReceiverModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>V3 分账 Model</p>
 *
 * <p>支持: 请求单次分账、请求多次分账、添加分账接收方、删除分账接收方、完结分账</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProfitSharingModel extends BaseModel {
	/**
	 * 微信支付分配的服务商商户号,兼容V2接口
	 */
	private String mch_id;
	/**
	 * 微信支付分配的子商户号，即分账的出资商户号
	 */
	private String sub_mchid;
	/**
	 * 微信分配的服务商appid
	 */
	private String appid;
	/**
	 * 微信分配的子商户公众账号ID，分账接收方类型包含PERSONAL_SUB_OPENID时必填
	 */
	private String sub_appid;
	/**
	 * 微信支付订单号
	 */
	private String transaction_id;
	/**
	 * 服务商系统内部的分账单号，在服务商系统内部唯一，同一分账单号多次请求等同一次。只能是数字、大小写字母_-|*@
	 */
	private String out_order_no;
	/**
	 * 分账接收方列表，可以设置出资商户作为分账接受方，最多可有50个分账接收方
	 */
	private List<ReceiverModel> receivers;
	/**
	 * 是否解冻剩余未分资金
	 * 1、如果为true，该笔订单剩余未分账的金额会解冻回分账方商户；
	 * 2、如果为false，该笔订单剩余未分账的金额不会解冻回分账方商户，可以对该笔订单再次进行分账。
	 */
	private boolean unfreeze_unsplit;
}
