package com.ijpay.wxpay.model;

import com.ijpay.core.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>分账接收方 Model</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
public class ReceiverModel extends BaseModel {
	/**
	 * 分账接收方类型
	 * MERCHANT_ID：商户号（mch_id或者sub_mch_id）
	 * PERSONAL_OPENID：个人openid
	 */
	private String type;
	/**
	 * 分账接收方帐号
	 * 类型是MERCHANT_ID时，是商户号（mch_id或者sub_mch_id）
	 * 类型是PERSONAL_OPENID时，是个人openid
	 */
	private String account;
	/**
	 * 分账接收方全称
	 * 分账接收方类型是MERCHANT_ID时，是商户全称（必传）
	 * 分账接收方类型是PERSONAL_OPENID时，是个人姓名（选传，传则校验）
	 */
	private String name;
	/**
	 * 与分账方的关系类型
	 * 子商户与接收方的关系。
	 * 本字段值为枚举：
	 * SERVICE_PROVIDER：服务商
	 * STORE：门店
	 * STAFF：员工
	 * STORE_OWNER：店主
	 * PARTNER：合作伙伴
	 * HEADQUARTER：总部
	 * BRAND：品牌方
	 * DISTRIBUTOR：分销商
	 * USER：用户
	 * SUPPLIER：供应商
	 * CUSTOM：自定义
	 */
	private String relation_type;
	/**
	 * 自定义的分账关系
	 * 子商户与接收方具体的关系，本字段最多10个字。
	 * 当字段relation_type的值为CUSTOM时，本字段必填
	 * 当字段relation_type的值不为CUSTOM时，本字段无需填写
	 */
	private String custom_relation;
	/**
	 * 账户
	 */
	private int amount;
	/**
	 * 分账描述
	 */
	private String description;
}
