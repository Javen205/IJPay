package com.ijpay.fuiyou;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.PayKit;
import com.ijpay.fuiyou.enums.FuiYouApiEnum;
import com.ijpay.fuiyou.enums.FuiYouApiType;
import com.ijpay.fuiyou.enums.FuiYouDomain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、富友支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>富友支付 API</p>
 *
 * <p>官方文档：<a href="https://fundwx.fuiou.com/doc/#/aggregatePay/api">https://fundwx.fuiou.com/doc/#/aggregatePay/api</a></p>
 *
 * @author Javen
 */
public class FuiYouPayApi {

	/**
	 * 默认版本号
	 */
	public static final String VERSION = "1.0";

	private FuiYouPayApi() {
	}

	/**
	 * 生成商户订单号
	 * <p>
	 * 官方流水号规则：订单前缀 + 日期（yyyyMMdd，8位）+ 其他随机数（8-17位），最长不超过 30 位
	 * <p>
	 * 测试环境订单前缀为 1066，生产环境为富友分配的商户号编码前缀（4 或 5 位）
	 *
	 * @param prefix 订单前缀
	 * @return {@link String} 商户订单号
	 */
	public static String generateMchntOrderNo(String prefix) {
		return prefix + DateUtil.format(new Date(), "yyyyMMdd") + RandomUtil.randomNumbers(10);
	}

	/**
	 * 获取接口请求的 URL
	 *
	 * @param fuiYouApiEnum {@link FuiYouApiEnum} 支付 API 接口枚举
	 * @return {@link String} 返回完整的接口请求 URL
	 */
	public static String getReqUrl(FuiYouApiEnum fuiYouApiEnum) {
		return getReqUrl(fuiYouApiEnum, null);
	}

	/**
	 * 获取接口请求的 URL
	 *
	 * @param fuiYouApiEnum {@link FuiYouApiEnum} 支付 API 接口枚举
	 * @param domain        {@link FuiYouDomain} 支付 API 接口域名，传 {@code null} 时使用配置中的域名（测试/生产环境）
	 * @return {@link String} 返回完整的接口请求 URL
	 */
	public static String getReqUrl(FuiYouApiEnum fuiYouApiEnum, FuiYouDomain domain) {
		String domainUrl;
		if (domain == null) {
			domainUrl = FuiYouPayApiConfigKit.getFuiYouPayApiConfig().getDomainUrl();
		} else {
			domainUrl = domain.getDomain();
		}
		return domainUrl.concat(fuiYouApiEnum.getUrl());
	}

	/**
	 * 发起请求（POST JSON）
	 *
	 * @param apiUrl 接口 URL
	 *               可通过 {@link FuiYouPayApi#getReqUrl(FuiYouApiEnum)} 获取
	 * @param json   接口请求报文（JSON 字符串）
	 * @return {@link String} 请求返回的结果
	 */
	public static String execution(String apiUrl, String json) {
		Map<String, String> headers = new HashMap<>(1);
		headers.put("Content-Type", ContentType.JSON.toString());
		IJPayHttpResponse response = HttpKit.getDelegate().post(apiUrl, json, headers);
		return response.getBody();
	}

	/**
	 * 发起请求（POST JSON）
	 *
	 * @param apiEnum {@link FuiYouApiEnum} 支付 API 接口枚举
	 * @param json    接口请求报文（JSON 字符串）
	 * @return {@link String} 请求返回的结果
	 */
	public static String execution(FuiYouApiEnum apiEnum, String json) {
		return execution(getReqUrl(apiEnum), json);
	}

	/**
	 * 发起请求（POST JSON）
	 *
	 * @param apiEnum {@link FuiYouApiEnum} 支付 API 接口枚举
	 * @param params  接口请求参数，方法内部会自动补充 {@code mchnt_cd}、{@code random_str}、{@code sign}
	 * @return {@link String} 请求返回的结果
	 */
	public static String execution(FuiYouApiEnum apiEnum, Map<String, String> params) {
		return execution(getReqUrl(apiEnum), JSONUtil.toJsonStr(params));
	}

	/**
	 * 发起 GET 请求
	 *
	 * @param apiUrl 接口 URL
	 * @param params 接口请求参数
	 * @return {@link String} 请求返回的结果
	 */
	public static String executionByGet(String apiUrl, Map<String, Object> params) {
		return HttpKit.getDelegate().get(apiUrl, params);
	}

	/**
	 * 2.1 统一下单接口
	 * <p>
	 * 请求签名：mchnt_cd|order_type|order_amt|mchnt_order_no|txn_begin_ts|goods_des|term_id|term_ip|notify_url|random_str|version|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String unifiedOrder(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		params.putIfAbsent("version", VERSION);
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("order_type"),
			params.get("order_amt"),
			params.get("mchnt_order_no"),
			params.get("txn_begin_ts"),
			params.get("goods_des"),
			params.get("term_id"),
			params.get("term_ip"),
			params.get("notify_url"),
			params.get("random_str"),
			params.get("version"));
		params.put("sign", sign);
		return execution(FuiYouApiType.UNIFIED_ORDER, JSONUtil.toJsonStr(params));
	}

	/**
	 * 2.2 条码支付接口
	 * <p>
	 * 请求签名：mchnt_cd|order_type|order_amt|mchnt_order_no|txn_begin_ts|goods_des|term_id|term_ip|auth_code|random_str|version|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String microPay(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		params.putIfAbsent("version", VERSION);
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("order_type"),
			params.get("order_amt"),
			params.get("mchnt_order_no"),
			params.get("txn_begin_ts"),
			params.get("goods_des"),
			params.get("term_id"),
			params.get("term_ip"),
			params.get("auth_code"),
			params.get("random_str"),
			params.get("version"));
		params.put("sign", sign);
		return execution(FuiYouApiType.MICRO_PAY, JSONUtil.toJsonStr(params));
	}

	/**
	 * 2.3 订单查询接口
	 * <p>
	 * 请求签名：mchnt_cd|order_type|mchnt_order_no|term_id|random_str|version|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String orderQuery(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		params.putIfAbsent("version", VERSION);
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("order_type"),
			params.get("mchnt_order_no"),
			params.get("term_id"),
			params.get("random_str"),
			params.get("version"));
		params.put("sign", sign);
		return execution(FuiYouApiType.ORDER_QUERY, JSONUtil.toJsonStr(params));
	}

	/**
	 * 2.4 退款申请接口
	 * <p>
	 * 请求签名：mchnt_cd|order_type|mchnt_order_no|refund_order_no|total_amt|refund_amt|term_id|random_str|version|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String refund(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		params.putIfAbsent("version", VERSION);
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("order_type"),
			params.get("mchnt_order_no"),
			params.get("refund_order_no"),
			params.get("total_amt"),
			params.get("refund_amt"),
			params.get("term_id"),
			params.get("random_str"),
			params.get("version"));
		params.put("sign", sign);
		return execution(FuiYouApiType.REFUND, JSONUtil.toJsonStr(params));
	}

	/**
	 * 2.6 公众号/服务窗统一下单接口
	 * <p>
	 * 请求签名：mchnt_cd|trade_type|order_amt|mchnt_order_no|txn_begin_ts|goods_des|term_id|term_ip|notify_url|random_str|version|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String wxPreCreate(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		params.putIfAbsent("version", VERSION);
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("trade_type"),
			params.get("order_amt"),
			params.get("mchnt_order_no"),
			params.get("txn_begin_ts"),
			params.get("goods_des"),
			params.get("term_id"),
			params.get("term_ip"),
			params.get("notify_url"),
			params.get("random_str"),
			params.get("version"));
		params.put("sign", sign);
		return execution(FuiYouApiType.WX_PRE_CREATE, JSONUtil.toJsonStr(params));
	}

	/**
	 * 2.8 查询可提现资金
	 * <p>
	 * 请求签名：mchnt_cd|random_str|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String queryWithdrawAmt(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("random_str"));
		params.put("sign", sign);
		return execution(FuiYouApiType.QUERY_WITHDRAW_AMT, JSONUtil.toJsonStr(params));
	}

	/**
	 * 2.9 查询提现手续费信息
	 * <p>
	 * 请求签名：mchnt_cd|amt|random_str|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String queryFeeAmt(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("amt"),
			params.get("random_str"));
		params.put("sign", sign);
		return execution(FuiYouApiType.QUERY_FEE_AMT, JSONUtil.toJsonStr(params));
	}

	/**
	 * 2.10 发起提现
	 * <p>
	 * 请求签名：mchnt_cd|amt|fee_amt|txn_type|random_str|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String withdraw(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("amt"),
			params.get("fee_amt"),
			params.get("txn_type"),
			params.get("random_str"));
		params.put("sign", sign);
		return execution(FuiYouApiType.WITHDRAW, JSONUtil.toJsonStr(params));
	}

	/**
	 * 2.11 资金划拨查询
	 * <p>
	 * 请求签名：mchnt_cd|start_date|end_date|start_index|end_index|random_str|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String queryTransferAmt(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("start_date"),
			params.get("end_date"),
			params.get("start_index"),
			params.get("end_index"),
			params.get("random_str"));
		params.put("sign", sign);
		return execution(FuiYouApiType.QUERY_TRANSFER_AMT, JSONUtil.toJsonStr(params));
	}

	/**
	 * 2.12 历史订单查询接口
	 * <p>
	 * 请求签名：mchnt_cd|order_type|term_id|random_str|version|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String hisTradeQuery(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		params.putIfAbsent("version", VERSION);
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("order_type"),
			params.get("term_id"),
			params.get("random_str"),
			params.get("version"));
		params.put("sign", sign);
		return execution(FuiYouApiType.HIS_TRADE_QUERY, JSONUtil.toJsonStr(params));
	}

	/**
	 * 2.13 退款查询接口（3 日内）
	 * <p>
	 * 请求签名：mchnt_cd|refund_order_no|term_id|random_str|version|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String refundQuery(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		params.putIfAbsent("version", VERSION);
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("refund_order_no"),
			params.get("term_id"),
			params.get("random_str"),
			params.get("version"));
		params.put("sign", sign);
		return execution(FuiYouApiType.REFUND_QUERY, JSONUtil.toJsonStr(params));
	}

	/**
	 * 2.14 关闭订单接口
	 * <p>
	 * 请求签名：mchnt_cd|mchnt_order_no|order_type|term_id|random_str|version|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String closeOrder(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		params.putIfAbsent("version", VERSION);
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("mchnt_order_no"),
			params.get("order_type"),
			params.get("term_id"),
			params.get("random_str"),
			params.get("version"));
		params.put("sign", sign);
		return execution(FuiYouApiType.CLOSE_ORDER, JSONUtil.toJsonStr(params));
	}

	/**
	 * 2.15 撤销订单接口
	 * <p>
	 * 请求签名：mchnt_cd|mchnt_order_no|order_type|cancel_order_no|term_id|random_str|version|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String cancelOrder(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		params.putIfAbsent("version", VERSION);
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("mchnt_order_no"),
			params.get("order_type"),
			params.get("cancel_order_no"),
			params.get("term_id"),
			params.get("random_str"),
			params.get("version"));
		params.put("sign", sign);
		return execution(FuiYouApiType.CANCEL_ORDER, JSONUtil.toJsonStr(params));
	}

	/**
	 * 2.16 服务商模式获取 openid 接口（GET）
	 * <p>
	 * 请求签名：mchnt_cd|redirect_uri|mchnt_key
	 * <p>
	 * 重定向响应：redirect_url?appid=xxx&amp;openid=xxx&amp;state=xxx&amp;access_token=xxx
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 拼接好的完整跳转 URL
	 */
	public static String auth2Openid(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("redirect_uri"));
		params.put("sign", sign);
		String url = getReqUrl(FuiYouApiType.AUTH2_OPENID);
		StringBuilder sb = new StringBuilder(url).append("?");
		params.forEach((k, v) -> sb.append(k).append("=").append(v).append("&"));
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	/**
	 * 2.17 银联授权码获取 user_id 接口
	 * <p>
	 * 请求签名：mchnt_cd|order_type|order_amt|auth_code|sub_appid|term_ip|random_str|version|mchnt_key
	 *
	 * @param params 接口请求参数（无需传 sign，方法内部自动生成）
	 * @return {@link String} 请求返回的结果
	 */
	public static String getOpenidByAuthCode(Map<String, String> params) {
		FuiYouPayApiConfig config = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		params.putIfAbsent("mchnt_cd", config.getMchntCd());
		params.putIfAbsent("random_str", PayKit.generateStr());
		params.putIfAbsent("version", VERSION);
		String sign = FuiYouPayCore.createSign(config.getMchntKey(),
			params.get("mchnt_cd"),
			params.get("order_type"),
			params.get("order_amt"),
			params.get("auth_code"),
			params.get("sub_appid"),
			params.get("term_ip"),
			params.get("random_str"),
			params.get("version"));
		params.put("sign", sign);
		return execution(FuiYouApiType.GET_OPENID_BY_AUTH_CODE, JSONUtil.toJsonStr(params));
	}
}
