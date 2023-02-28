package com.ijpay.wxpay.model.v3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
 * <p>发起商家转账 Model</p>
 *
 * @author Javen
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class BatchTransferModel {

	/**
	 * 申请商户号的appid或商户号绑定的appid（企业号corpid即为此appid）
	 */
	private String appid;

	/**
	 * 商户系统内部的商家批次单号，要求此参数只能由数字、大小写字母组成，在商户系统内部唯一
	 */
	private String out_batch_no;

	/**
	 * 该笔批量转账的名称
	 */
	private String batch_name;
	/**
	 * 转账说明，UTF8编码，最多允许32个字符
	 */
	private String batch_remark;
	/**
	 * 转账金额单位为“分”。转账总金额必须与批次内所有明细转账金额之和保持一致，否则无法发起转账操作
	 */
	private Integer total_amount;
	/**
	 * 一个转账批次单最多发起一千笔转账。转账总笔数必须与批次内所有明细之和保持一致，否则无法发起转账操作
	 */
	private Integer total_num;
	/**
	 * 发起批量转账的明细列表，最多一千笔
	 */
	private List<TransferDetailInput> transfer_detail_list;

	/**
	 * 指定该笔转账使用的转账场景ID
	 */
	private String transfer_scene_id;

}
