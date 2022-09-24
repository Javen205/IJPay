package com.ijpay.wxpay.enums.v3;

import com.ijpay.wxpay.enums.WxApiEnum;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 * <p>IJPay 自由交流群: 864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>微信支付 v3 接口-商家转账到零钱接口枚举</p>
 *
 * @author Javen
 */
public enum TransferApiEnum implements WxApiEnum {

	/**
	 * 发起商家转账
	 */
	TRANSFER_BATCHES("/v3/transfer/batches", "发起商家转账"),

	/**
	 * 微信支付批次单号查询批次单
	 */
	TRANSFER_QUERY_BY_BATCH_ID("/v3/transfer/batches/batch-id/%s", "微信支付批次单号查询批次单"),

	/**
	 * 微信支付明细单号查询明细单
	 */
	TRANSFER_QUERY_BY_DETAIL_ID("/v3/transfer/batches/batch-id/%s/details/detail-id/%s", "微信支付明细单号查询明细单"),

	/**
	 * 商家批次单号查询批次单
	 */
	TRANSFER_QUERY_BY_OUT_BATCH_NO("/v3/transfer/batches/out-batch-no/%s", "商家批次单号查询批次单"),

	/**
	 * 商家明细单号查询明细单
	 */
	TRANSFER_QUERY_DETAIL_BY_OUT_BATCH_NO("/v3/transfer/batches/out-batch-no/%s/details/out-detail-no/%s", "商家明细单号查询明细单"),

	/**
	 * 转账电子回单申请受理
	 */
	TRANSFER_BILL_RECEIPT("/v3/transfer/bill-receipt", "转账电子回单申请受理"),

	/**
	 * 查询转账电子回单
	 */
	TRANSFER_BILL_RECEIPT_QUERY("/v3/transfer/bill-receipt/%s", "查询转账电子回单"),

	/**
	 * 转账明细电子回单受理/查询转账明细电子回单受理结果
	 */
	TRANSFER_ELECTRONIC_RECEIPTS("/v3/transfer-detail/electronic-receipts", "转账明细电子回单受理"),
	;

	/**
	 * 接口URL
	 */
	private final String url;

	/**
	 * 接口描述
	 */
	private final String desc;

	TransferApiEnum(String url, String desc) {
		this.url = url;
		this.desc = desc;
	}

	/**
	 * 获取枚举URL
	 *
	 * @return 枚举编码
	 */
	@Override
	public String getUrl() {
		return url;
	}

	/**
	 * 获取详细的描述信息
	 *
	 * @return 描述信息
	 */
	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public String toString() {
		return url;
	}
}
