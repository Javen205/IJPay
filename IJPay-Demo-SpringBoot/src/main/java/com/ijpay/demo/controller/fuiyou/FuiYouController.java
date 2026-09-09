package com.ijpay.demo.controller.fuiyou;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.demo.entity.FuiYouPayBean;
import com.ijpay.demo.vo.AjaxResult;
import com.ijpay.fuiyou.*;
import com.ijpay.fuiyou.enums.FuiYouOrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
 * <p>富友支付 Demo</p>
 *
 * <p>官方文档：<a href="https://fundwx.fuiou.com/doc/#/aggregatePay/api">https://fundwx.fuiou.com/doc/#/aggregatePay/api</a></p>
 *
 * <p>接口域名通过 fuiyou.properties 中 fuiyou.domain 配置切换（test 测试环境 / prod 生产环境）</p>
 *
 * @author Javen
 */
@Controller
@RequestMapping("/fuiYou")
public class FuiYouController {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	FuiYouPayBean fuiYouPayBean;

	/**
	 * 测试环境订单前缀（生产环境为富友分配的商户号编码前缀，4 或 5 位）
	 */
	private static final String ORDER_PREFIX = "1066";

	/**
	 * 获取富友支付配置，未初始化时根据配置文件自动构建（支持测试/生产环境切换）
	 */
	private FuiYouPayApiConfig getApiConfig() {
		FuiYouPayApiConfig fuiYouPayApiConfig;
		try {
			fuiYouPayApiConfig = FuiYouPayApiConfigKit.getFuiYouPayApiConfig();
		} catch (Exception e) {
			fuiYouPayApiConfig = FuiYouPayApiConfig.builder()
				.mchntCd(fuiYouPayBean.getMchntCd())
				.mchntKey(fuiYouPayBean.getMchntKey())
				.domain(fuiYouPayBean.getDomainEnum())
				.build();
			FuiYouPayApiConfigKit.putApiConfig(fuiYouPayApiConfig);
		}
		return fuiYouPayApiConfig;
	}

	@RequestMapping("")
	@ResponseBody
	public String index() {
		log.info("欢迎使用 IJPay 中的富友支付 -By Javen  <br/><br>  交流群：723992875、864988890");
		return ("欢迎使用 IJPay 中的富友支付 -By Javen  <br/><br>  交流群：723992875、864988890");
	}

	/**
	 * 查看当前配置（商户号、接口环境）
	 */
	@RequestMapping(value = "/test", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public FuiYouPayBean test() {
		getApiConfig();
		return fuiYouPayBean;
	}

	/**
	 * 2.1 统一下单接口
	 * <p>
	 * 请求签名：mchnt_cd|order_type|order_amt|mchnt_order_no|txn_begin_ts|goods_des|term_id|term_ip|notify_url|random_str|version|mchnt_key
	 *
	 * @param orderAmt  订单金额（单位：分）
	 * @param orderType 订单类型，默认 ALIPAY
	 */
	@RequestMapping(value = "/unifiedOrder", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult unifiedOrder(@RequestParam(value = "orderAmt", defaultValue = "1") String orderAmt,
								   @RequestParam(value = "orderType", defaultValue = "ALIPAY") String orderType) {
		getApiConfig();
		Map<String, String> params = new HashMap<>(16);
		params.put("order_type", orderType);
		params.put("order_amt", orderAmt);
		params.put("mchnt_order_no", FuiYouPayApi.generateMchntOrderNo(ORDER_PREFIX));
		params.put("txn_begin_ts", DateUtil.format(new Date(), "yyyyMMddHHmmss"));
		params.put("goods_des", "IJPay 富友支付-统一下单");
		params.put("term_id", "88888888");
		params.put("term_ip", "127.0.0.1");
		params.put("notify_url", "http://localhost:8888/FuiYou/payNotify");

		String resultData = FuiYouPayApi.unifiedOrder(params);
		log.info("2.1 统一下单 resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}

	/**
	 * 2.2 条码支付接口
	 * <p>
	 * 请求签名：mchnt_cd|order_type|order_amt|mchnt_order_no|txn_begin_ts|goods_des|term_id|term_ip|auth_code|random_str|version|mchnt_key
	 *
	 * @param authCode 授权码/条码
	 * @param orderAmt 订单金额（单位：分）
	 */
	@RequestMapping(value = "/microPay", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult microPay(@RequestParam("authCode") String authCode,
							   @RequestParam(value = "orderAmt", defaultValue = "1") String orderAmt) {
		getApiConfig();
		Map<String, String> params = new HashMap<>(16);
		params.put("order_type", FuiYouOrderType.ALI_PAY.getType());
		params.put("order_amt", orderAmt);
		params.put("mchnt_order_no", FuiYouPayApi.generateMchntOrderNo(ORDER_PREFIX));
		params.put("txn_begin_ts", DateUtil.format(new Date(), "yyyyMMddHHmmss"));
		params.put("goods_des", "IJPay 富友支付-条码支付");
		params.put("term_id", "88888888");
		params.put("term_ip", "127.0.0.1");
		params.put("auth_code", authCode);

		String resultData = FuiYouPayApi.microPay(params);
		log.info("2.2 条码支付 resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}

	/**
	 * 2.3 订单查询接口
	 * <p>
	 * 请求签名：mchnt_cd|order_type|mchnt_order_no|term_id|random_str|version|mchnt_key
	 *
	 * @param mchntOrderNo 商户订单号
	 */
	@RequestMapping(value = "/orderQuery", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult orderQuery(@RequestParam("mchntOrderNo") String mchntOrderNo) {
		getApiConfig();
		Map<String, String> params = new HashMap<>(16);
		params.put("order_type", FuiYouOrderType.ALI_PAY.getType());
		params.put("mchnt_order_no", mchntOrderNo);
		params.put("term_id", "88888888");

		String resultData = FuiYouPayApi.orderQuery(params);
		log.info("2.3 订单查询 resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}

	/**
	 * 2.4 退款申请接口
	 * <p>
	 * 请求签名：mchnt_cd|order_type|mchnt_order_no|refund_order_no|total_amt|refund_amt|term_id|random_str|version|mchnt_key
	 *
	 * @param mchntOrderNo 商户订单号
	 * @param totalAmt     订单总金额（单位：分）
	 * @param refundAmt    退款金额（单位：分）
	 */
	@RequestMapping(value = "/refund", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult refund(@RequestParam("mchntOrderNo") String mchntOrderNo,
							 @RequestParam(value = "totalAmt", defaultValue = "1") String totalAmt,
							 @RequestParam(value = "refundAmt", defaultValue = "1") String refundAmt) {
		getApiConfig();
		Map<String, String> params = new HashMap<>(16);
		params.put("order_type", FuiYouOrderType.ALI_PAY.getType());
		params.put("mchnt_order_no", mchntOrderNo);
		params.put("refund_order_no", FuiYouPayApi.generateMchntOrderNo(ORDER_PREFIX));
		params.put("total_amt", totalAmt);
		params.put("refund_amt", refundAmt);
		params.put("term_id", "88888888");

		String resultData = FuiYouPayApi.refund(params);
		log.info("2.4 退款申请 resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}

	/**
	 * 2.5 支付结果通知-异步通知
	 * <p>
	 * 富友最多通知 5 次，间隔 30 秒，商户应答返回 "1" 后停止通知
	 */
	@RequestMapping(value = "/payNotify", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public String payNotify(HttpServletRequest request) {
		String readData = HttpKit.readData(request);
		log.info("支付通知数据:{}", readData);
		FuiYouNotify notify = JSONUtil.toBean(readData, FuiYouNotify.class);
		log.info("支付通知解析结果:{}", notify);
		FuiYouPayApiConfig apiConfig = getApiConfig();

		// 验签 sign：mchnt_cd|mchnt_order_no|settle_order_amt|order_amt|txn_fin_ts|reserved_fy_settle_dt|random_str
		boolean verifyNotify = FuiYouPayCore.verifyNotify(notify.getSign(), notify.getMchnt_cd(), notify.getMchnt_order_no(),
			notify.getSettle_order_amt(), notify.getOrder_amt(), notify.getTxn_fin_ts(),
			notify.getReserved_fy_settle_dt(), notify.getRandom_str(), apiConfig.getMchntKey());
		log.info("sign 验签结果:{}", verifyNotify);

		// 验签 full_sign：result_code|result_msg|mchnt_cd|mchnt_order_no|settle_order_amt|order_amt|txn_fin_ts|reserved_fy_settle_dt|random_str
		boolean verifyFullSign = FuiYouPayCore.verifyNotifyByFullSign(notify.getFull_sign(), notify.getResult_code(), notify.getResult_msg(),
			notify.getMchnt_cd(), notify.getMchnt_order_no(), notify.getSettle_order_amt(), notify.getOrder_amt(),
			notify.getTxn_fin_ts(), notify.getReserved_fy_settle_dt(), notify.getRandom_str(), apiConfig.getMchntKey());
		log.info("full_sign 验签结果:{}", verifyFullSign);

		// 验签通过且支付成功后，处理业务逻辑（注意判断重复通知，同一订单号可能收到多次通知）
		if (verifyNotify && "0000".equals(notify.getResult_code())) {
			log.info("支付成功，商户订单号:{}", notify.getMchnt_order_no());
			// TODO 处理业务逻辑，更新订单状态等
			return "1";
		}
		return "0";
	}

	/**
	 * 2.6 公众号/服务窗统一下单接口
	 * <p>
	 * 请求签名：mchnt_cd|trade_type|order_amt|mchnt_order_no|txn_begin_ts|goods_des|term_id|term_ip|notify_url|random_str|version|mchnt_key
	 *
	 * @param orderAmt  订单金额（单位：分）
	 * @param tradeType 交易类型，默认 JSAPI
	 */
	@RequestMapping(value = "/wxPreCreate", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult wxPreCreate(@RequestParam(value = "orderAmt", defaultValue = "1") String orderAmt,
								  @RequestParam(value = "tradeType", defaultValue = "JSAPI") String tradeType) {
		// 交易类型也可直接使用枚举：FuiYouTradeType.JSAPI.getType()
		getApiConfig();
		Map<String, String> params = new HashMap<>(16);
		params.put("trade_type", tradeType);
		params.put("order_amt", orderAmt);
		params.put("mchnt_order_no", FuiYouPayApi.generateMchntOrderNo(ORDER_PREFIX));
		params.put("txn_begin_ts", DateUtil.format(new Date(), "yyyyMMddHHmmss"));
		params.put("goods_des", "IJPay 富友支付-公众号下单");
		params.put("term_id", "88888888");
		params.put("term_ip", "127.0.0.1");
		params.put("notify_url", "http://localhost:8888/FuiYou/payNotify");

		String resultData = FuiYouPayApi.wxPreCreate(params);
		log.info("2.6 公众号/服务窗统一下单 resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}

	/**
	 * 2.8 查询可提现资金
	 * <p>
	 * 请求签名：mchnt_cd|random_str|mchnt_key
	 */
	@RequestMapping(value = "/queryWithdrawAmt", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult queryWithdrawAmt() {
		getApiConfig();
		Map<String, String> params = new HashMap<>(4);
		String resultData = FuiYouPayApi.queryWithdrawAmt(params);
		log.info("2.8 查询可提现资金 resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}

	/**
	 * 2.9 查询提现手续费
	 * <p>
	 * 请求签名：mchnt_cd|amt|random_str|mchnt_key
	 *
	 * @param amt 提现金额（单位：分）
	 */
	@RequestMapping(value = "/queryFeeAmt", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult queryFeeAmt(@RequestParam(value = "amt", defaultValue = "1") String amt) {
		getApiConfig();
		Map<String, String> params = new HashMap<>(4);
		params.put("amt", amt);
		String resultData = FuiYouPayApi.queryFeeAmt(params);
		log.info("2.9 查询提现手续费 resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}

	/**
	 * 2.10 发起提现
	 * <p>
	 * 请求签名：mchnt_cd|amt|fee_amt|txn_type|random_str|mchnt_key
	 *
	 * @param amt    提现金额（单位：分）
	 * @param feeAmt 手续费（单位：分）
	 */
	@RequestMapping(value = "/withdraw", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult withdraw(@RequestParam(value = "amt", defaultValue = "1") String amt,
							   @RequestParam(value = "feeAmt", defaultValue = "0") String feeAmt) {
		getApiConfig();
		Map<String, String> params = new HashMap<>(4);
		params.put("amt", amt);
		params.put("fee_amt", feeAmt);
		params.put("txn_type", "0");
		String resultData = FuiYouPayApi.withdraw(params);
		log.info("2.10 发起提现 resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}

	/**
	 * 2.11 资金划拨查询
	 * <p>
	 * 请求签名：mchnt_cd|start_date|end_date|start_index|end_index|random_str|mchnt_key
	 *
	 * @param startDate 起始日期 yyyyMMdd
	 * @param endDate   结束日期 yyyyMMdd
	 */
	@RequestMapping(value = "/queryTransferAmt", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult queryTransferAmt(@RequestParam("startDate") String startDate,
									   @RequestParam("endDate") String endDate) {
		getApiConfig();
		Map<String, String> params = new HashMap<>(4);
		params.put("start_date", startDate);
		params.put("end_date", endDate);
		params.put("start_index", "0");
		params.put("end_index", "20");
		String resultData = FuiYouPayApi.queryTransferAmt(params);
		log.info("2.11 资金划拨查询 resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}

	/**
	 * 2.12 历史订单查询接口
	 * <p>
	 * 请求签名：mchnt_cd|order_type|term_id|random_str|version|mchnt_key
	 */
	@RequestMapping(value = "/hisTradeQuery", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult hisTradeQuery() {
		getApiConfig();
		Map<String, String> params = new HashMap<>(4);
		params.put("order_type", FuiYouOrderType.ALI_PAY.getType());
		params.put("term_id", "88888888");
		String resultData = FuiYouPayApi.hisTradeQuery(params);
		log.info("2.12 历史订单查询 resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}

	/**
	 * 2.13 退款查询接口（3 日内）
	 * <p>
	 * 请求签名：mchnt_cd|refund_order_no|term_id|random_str|version|mchnt_key
	 *
	 * @param refundOrderNo 退款订单号
	 */
	@RequestMapping(value = "/refundQuery", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult refundQuery(@RequestParam("refundOrderNo") String refundOrderNo) {
		getApiConfig();
		Map<String, String> params = new HashMap<>(4);
		params.put("refund_order_no", refundOrderNo);
		params.put("term_id", "88888888");
		String resultData = FuiYouPayApi.refundQuery(params);
		log.info("2.13 退款查询 resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}

	/**
	 * 2.14 关闭订单接口
	 * <p>
	 * 请求签名：mchnt_cd|mchnt_order_no|order_type|term_id|random_str|version|mchnt_key
	 *
	 * @param mchntOrderNo 商户订单号
	 */
	@RequestMapping(value = "/closeOrder", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult closeOrder(@RequestParam("mchntOrderNo") String mchntOrderNo) {
		getApiConfig();
		Map<String, String> params = new HashMap<>(4);
		params.put("mchnt_order_no", mchntOrderNo);
		params.put("order_type", FuiYouOrderType.ALI_PAY.getType());
		params.put("term_id", "88888888");
		String resultData = FuiYouPayApi.closeOrder(params);
		log.info("2.14 关闭订单 resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}

	/**
	 * 2.15 撤销订单接口
	 * <p>
	 * 请求签名：mchnt_cd|mchnt_order_no|order_type|cancel_order_no|term_id|random_str|version|mchnt_key
	 *
	 * @param mchntOrderNo 商户订单号
	 */
	@RequestMapping(value = "/cancelOrder", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult cancelOrder(@RequestParam("mchntOrderNo") String mchntOrderNo) {
		getApiConfig();
		Map<String, String> params = new HashMap<>(4);
		params.put("mchnt_order_no", mchntOrderNo);
		params.put("order_type", FuiYouOrderType.ALI_PAY.getType());
		params.put("cancel_order_no", FuiYouPayApi.generateMchntOrderNo(ORDER_PREFIX));
		params.put("term_id", "88888888");
		String resultData = FuiYouPayApi.cancelOrder(params);
		log.info("2.15 撤销订单 resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}

	/**
	 * 2.16 获取 openid 接口（GET）
	 * <p>
	 * 请求签名：mchnt_cd|redirect_uri|mchnt_key
	 *
	 * @param redirectUri 重定向地址
	 */
	@RequestMapping(value = "/auth2Openid", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult auth2Openid(@RequestParam("redirectUri") String redirectUri) {
		getApiConfig();
		Map<String, String> params = new HashMap<>(4);
		params.put("redirect_uri", redirectUri);
		String resultUrl = FuiYouPayApi.auth2Openid(params);
		log.info("2.16 获取 openid 跳转地址:{}", resultUrl);
		return new AjaxResult().success(resultUrl);
	}

	/**
	 * 2.17 银联授权码获取 user_id 接口
	 * <p>
	 * 请求签名：mchnt_cd|order_type|order_amt|auth_code|sub_appid|term_ip|random_str|version|mchnt_key
	 *
	 * @param authCode 授权码
	 * @param orderAmt 订单金额（单位：分）
	 */
	@RequestMapping(value = "/getOpenidByAuthCode", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public AjaxResult getOpenidByAuthCode(@RequestParam("authCode") String authCode,
										  @RequestParam(value = "orderAmt", defaultValue = "1") String orderAmt) {
		getApiConfig();
		Map<String, String> params = new HashMap<>(4);
		params.put("order_type", FuiYouOrderType.UNION_PAY.getType());
		params.put("order_amt", orderAmt);
		params.put("auth_code", authCode);
		params.put("term_ip", "127.0.0.1");
		String resultData = FuiYouPayApi.getOpenidByAuthCode(params);
		log.info("2.17 银联授权码获取 user_id resultData:{}", resultData);
		return new AjaxResult().success(JSONUtil.parseObj(resultData));
	}
}
