package com.ijpay.wxpay.model.v3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>发起批量转账的明细列表</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TransferDetailInput {
	/**
	 * 商户系统内部区分转账批次单下不同转账明细单的唯一标识，要求此参数只能由数字、大小写字母组成
	 */
	private String out_detail_no;
	/**
	 * 转账金额单位为“分”
	 */
	private Integer transfer_amount;
	/**
	 * 单条转账备注（微信用户会收到该备注），UTF8编码，最多允许32个字符
	 */
	private String transfer_remark;
	/**
	 * 商户appid下，某用户的openid
	 */
	private String openid;
	/**
	 * 收款方真实姓名。支持标准RSA算法和国密算法，公钥由微信侧提供
	 * 明细转账金额<0.3元时，不允许填写收款用户姓名
	 * 明细转账金额 >= 2,000元时，该笔明细必须填写收款用户姓名
	 * 同一批次转账明细中的姓名字段传入规则需保持一致，也即全部填写、或全部不填写
	 * 若商户传入收款用户姓名，微信支付会校验用户openID与姓名是否一致，并提供电子回单
	 */
	private String user_name;
	/**
	 * 收款方身份证号，可不用填（采用标准RSA算法，公钥由微信侧提供）
	 * 当填入收款方身份证号时，姓名字段必须填入。
	 */
	private String user_id_card;


}
