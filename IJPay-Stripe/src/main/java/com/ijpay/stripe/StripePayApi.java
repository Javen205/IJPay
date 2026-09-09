package com.ijpay.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.SetupIntent;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeSearchResult;
import com.stripe.param.ChargeCaptureParams;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.ChargeListParams;
import com.stripe.param.ChargeRetrieveParams;
import com.stripe.param.ChargeSearchParams;
import com.stripe.param.ChargeUpdateParams;
import com.stripe.param.PaymentIntentApplyCustomerBalanceParams;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.PaymentIntentCaptureParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentListParams;
import com.stripe.param.PaymentIntentRetrieveParams;
import com.stripe.param.PaymentIntentSearchParams;
import com.stripe.param.PaymentIntentUpdateParams;
import com.stripe.param.PaymentIntentVerifyMicrodepositsParams;
import com.stripe.param.RefundCancelParams;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.RefundListParams;
import com.stripe.param.RefundRetrieveParams;
import com.stripe.param.RefundUpdateParams;
import com.stripe.param.SetupIntentCancelParams;
import com.stripe.param.SetupIntentConfirmParams;
import com.stripe.param.SetupIntentCreateParams;
import com.stripe.param.SetupIntentListParams;
import com.stripe.param.SetupIntentRetrieveParams;
import com.stripe.param.SetupIntentUpdateParams;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>Stripe 收款/交易域 API 薄封装，内部统一从 {@link StripeApiConfigKit} 获取当前配置的 Stripe 客户端。</p>
 *
 * <p>参数均为 stripe-java 官方类型安全的 *Params(可使用官方 Builder 链式构建)，
 * 如需自定义请求头/幂等键/Connect 子商户，可使用 {@link StripeApi#buildRequestOptions(String, String)} 自行调用官方 service。</p>
 *
 * <p>Covered: PaymentIntents(支付意图)、Charges(收款单)、Refunds(退款)、SetupIntents(绑卡)。</p>
 *
 * @author Javen
 */
public class StripePayApi {

	private StripePayApi() {
	}

	/* ==================== PaymentIntents 支付意图 ==================== */

	/**
	 * 创建 PaymentIntent
	 *
	 * @param params {@link PaymentIntentCreateParams} 创建参数
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent createPaymentIntent(PaymentIntentCreateParams params) throws StripeException {
		return StripeApi.paymentIntents().create(params);
	}

	/**
	 * 查询 PaymentIntent
	 *
	 * @param id PaymentIntent ID(pi_xxx)
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent retrievePaymentIntent(String id) throws StripeException {
		return StripeApi.paymentIntents().retrieve(id);
	}

	/**
	 * 查询 PaymentIntent(可展开字段)
	 *
	 * @param id     PaymentIntent ID(pi_xxx)
	 * @param params {@link PaymentIntentRetrieveParams} 查询参数
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent retrievePaymentIntent(String id, PaymentIntentRetrieveParams params) throws StripeException {
		return StripeApi.paymentIntents().retrieve(id, params);
	}

	/**
	 * 更新 PaymentIntent
	 *
	 * @param id     PaymentIntent ID(pi_xxx)
	 * @param params {@link PaymentIntentUpdateParams} 更新参数
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent updatePaymentIntent(String id, PaymentIntentUpdateParams params) throws StripeException {
		return StripeApi.paymentIntents().update(id, params);
	}

	/**
	 * 确认 PaymentIntent(触发扣款，通常由客户端先完成认证)
	 *
	 * @param id PaymentIntent ID(pi_xxx)
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent confirmPaymentIntent(String id) throws StripeException {
		return StripeApi.paymentIntents().confirm(id);
	}

	/**
	 * 确认 PaymentIntent
	 *
	 * @param id     PaymentIntent ID(pi_xxx)
	 * @param params {@link PaymentIntentConfirmParams} 确认参数(可带支付方式等)
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent confirmPaymentIntent(String id, PaymentIntentConfirmParams params) throws StripeException {
		return StripeApi.paymentIntents().confirm(id, params);
	}

	/**
	 * 取消 PaymentIntent
	 *
	 * @param id PaymentIntent ID(pi_xxx)
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent cancelPaymentIntent(String id) throws StripeException {
		return StripeApi.paymentIntents().cancel(id);
	}

	/**
	 * 取消 PaymentIntent
	 *
	 * @param id     PaymentIntent ID(pi_xxx)
	 * @param params {@link PaymentIntentCancelParams} 取消参数
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent cancelPaymentIntent(String id, PaymentIntentCancelParams params) throws StripeException {
		return StripeApi.paymentIntents().cancel(id, params);
	}

	/**
	 * 捕获 PaymentIntent(自动捕获模式下确认时不会扣款，需此接口完成扣款)
	 *
	 * @param id PaymentIntent ID(pi_xxx)
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent capturePaymentIntent(String id) throws StripeException {
		return StripeApi.paymentIntents().capture(id);
	}

	/**
	 * 捕获 PaymentIntent
	 *
	 * @param id     PaymentIntent ID(pi_xxx)
	 * @param params {@link PaymentIntentCaptureParams} 捕获参数
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent capturePaymentIntent(String id, PaymentIntentCaptureParams params) throws StripeException {
		return StripeApi.paymentIntents().capture(id, params);
	}

	/**
	 * 列表查询 PaymentIntent
	 *
	 * @param params {@link PaymentIntentListParams} 列表参数
	 * @return {@link StripeCollection}&lt;{@link PaymentIntent}&gt;
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<PaymentIntent> listPaymentIntents(PaymentIntentListParams params) throws StripeException {
		return StripeApi.paymentIntents().list(params);
	}

	/**
	 * 搜索 PaymentIntent
	 *
	 * @param params {@link PaymentIntentSearchParams} 搜索参数(使用 query 表达式)
	 * @return {@link StripeSearchResult}&lt;{@link PaymentIntent}&gt;
	 * @throws StripeException Stripe 异常
	 */
	public static StripeSearchResult<PaymentIntent> searchPaymentIntents(PaymentIntentSearchParams params) throws StripeException {
		return StripeApi.paymentIntents().search(params);
	}

	/**
	 * 使用客户余额支付
	 *
	 * @param id PaymentIntent ID(pi_xxx)
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent applyCustomerBalancePaymentIntent(String id) throws StripeException {
		return StripeApi.paymentIntents().applyCustomerBalance(id);
	}

	/**
	 * 使用客户余额支付
	 *
	 * @param id     PaymentIntent ID(pi_xxx)
	 * @param params {@link PaymentIntentApplyCustomerBalanceParams} 参数
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent applyCustomerBalancePaymentIntent(String id, PaymentIntentApplyCustomerBalanceParams params) throws StripeException {
		return StripeApi.paymentIntents().applyCustomerBalance(id, params);
	}

	/**
	 * 验证银行账户存款(仅 ACH 等需要 micro-deposit 验证的场景)
	 *
	 * @param id PaymentIntent ID(pi_xxx)
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent verifyMicrodepositsPaymentIntent(String id) throws StripeException {
		return StripeApi.paymentIntents().verifyMicrodeposits(id);
	}

	/**
	 * 验证银行账户存款
	 *
	 * @param id     PaymentIntent ID(pi_xxx)
	 * @param params {@link PaymentIntentVerifyMicrodepositsParams} 验证参数
	 * @return {@link PaymentIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentIntent verifyMicrodepositsPaymentIntent(String id, PaymentIntentVerifyMicrodepositsParams params) throws StripeException {
		return StripeApi.paymentIntents().verifyMicrodeposits(id, params);
	}

	/* ==================== Charges 收款单 ==================== */

	/**
	 * 创建 Charge(老式支付 API，新项目建议使用 PaymentIntent)
	 *
	 * @param params {@link ChargeCreateParams} 创建参数
	 * @return {@link Charge}
	 * @throws StripeException Stripe 异常
	 */
	public static Charge createCharge(ChargeCreateParams params) throws StripeException {
		return StripeApi.charges().create(params);
	}

	/**
	 * 查询 Charge
	 *
	 * @param id Charge ID(ch_xxx)
	 * @return {@link Charge}
	 * @throws StripeException Stripe 异常
	 */
	public static Charge retrieveCharge(String id) throws StripeException {
		return StripeApi.charges().retrieve(id);
	}

	/**
	 * 查询 Charge(可展开字段)
	 *
	 * @param id     Charge ID(ch_xxx)
	 * @param params {@link ChargeRetrieveParams} 查询参数
	 * @return {@link Charge}
	 * @throws StripeException Stripe 异常
	 */
	public static Charge retrieveCharge(String id, ChargeRetrieveParams params) throws StripeException {
		return StripeApi.charges().retrieve(id, params);
	}

	/**
	 * 更新 Charge
	 *
	 * @param id     Charge ID(ch_xxx)
	 * @param params {@link ChargeUpdateParams} 更新参数
	 * @return {@link Charge}
	 * @throws StripeException Stripe 异常
	 */
	public static Charge updateCharge(String id, ChargeUpdateParams params) throws StripeException {
		return StripeApi.charges().update(id, params);
	}

	/**
	 * 捕获 Charge(延期捕获模式)
	 *
	 * @param id Charge ID(ch_xxx)
	 * @return {@link Charge}
	 * @throws StripeException Stripe 异常
	 */
	public static Charge captureCharge(String id) throws StripeException {
		return StripeApi.charges().capture(id);
	}

	/**
	 * 捕获 Charge
	 *
	 * @param id     Charge ID(ch_xxx)
	 * @param params {@link ChargeCaptureParams} 捕获参数
	 * @return {@link Charge}
	 * @throws StripeException Stripe 异常
	 */
	public static Charge captureCharge(String id, ChargeCaptureParams params) throws StripeException {
		return StripeApi.charges().capture(id, params);
	}

	/**
	 * 列表查询 Charge
	 *
	 * @param params {@link ChargeListParams} 列表参数
	 * @return {@link StripeCollection}&lt;{@link Charge}&gt;
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<Charge> listCharges(ChargeListParams params) throws StripeException {
		return StripeApi.charges().list(params);
	}

	/**
	 * 搜索 Charge
	 *
	 * @param params {@link ChargeSearchParams} 搜索参数
	 * @return {@link StripeSearchResult}&lt;{@link Charge}&gt;
	 * @throws StripeException Stripe 异常
	 */
	public static StripeSearchResult<Charge> searchCharges(ChargeSearchParams params) throws StripeException {
		return StripeApi.charges().search(params);
	}

	/* ==================== Refunds 退款 ==================== */

	/**
	 * 发起退款
	 *
	 * @param params {@link RefundCreateParams} 退款参数(通常需携带 payment_intent 或 charge)
	 * @return {@link Refund}
	 * @throws StripeException Stripe 异常
	 */
	public static Refund createRefund(RefundCreateParams params) throws StripeException {
		return StripeApi.refunds().create(params);
	}

	/**
	 * 查询退款
	 *
	 * @param id 退款 ID(re_xxx)
	 * @return {@link Refund}
	 * @throws StripeException Stripe 异常
	 */
	public static Refund retrieveRefund(String id) throws StripeException {
		return StripeApi.refunds().retrieve(id);
	}

	/**
	 * 查询退款(可展开字段)
	 *
	 * @param id     退款 ID(re_xxx)
	 * @param params {@link RefundRetrieveParams} 查询参数
	 * @return {@link Refund}
	 * @throws StripeException Stripe 异常
	 */
	public static Refund retrieveRefund(String id, RefundRetrieveParams params) throws StripeException {
		return StripeApi.refunds().retrieve(id, params);
	}

	/**
	 * 更新退款(仅可更新 metadata)
	 *
	 * @param id     退款 ID(re_xxx)
	 * @param params {@link RefundUpdateParams} 更新参数
	 * @return {@link Refund}
	 * @throws StripeException Stripe 异常
	 */
	public static Refund updateRefund(String id, RefundUpdateParams params) throws StripeException {
		return StripeApi.refunds().update(id, params);
	}

	/**
	 * 取消退款
	 *
	 * @param id 退款 ID(re_xxx)
	 * @return {@link Refund}
	 * @throws StripeException Stripe 异常
	 */
	public static Refund cancelRefund(String id) throws StripeException {
		return StripeApi.refunds().cancel(id);
	}

	/**
	 * 取消退款
	 *
	 * @param id     退款 ID(re_xxx)
	 * @param params {@link RefundCancelParams} 取消参数
	 * @return {@link Refund}
	 * @throws StripeException Stripe 异常
	 */
	public static Refund cancelRefund(String id, RefundCancelParams params) throws StripeException {
		return StripeApi.refunds().cancel(id, params);
	}

	/**
	 * 列表查询退款
	 *
	 * @param params {@link RefundListParams} 列表参数
	 * @return {@link StripeCollection}&lt;{@link Refund}&gt;
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<Refund> listRefunds(RefundListParams params) throws StripeException {
		return StripeApi.refunds().list(params);
	}

	/* ==================== SetupIntents 绑卡/保存支付方式 ==================== */

	/**
	 * 创建 SetupIntent(用于保存卡信息，不产生扣款)
	 *
	 * @param params {@link SetupIntentCreateParams} 创建参数
	 * @return {@link SetupIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static SetupIntent createSetupIntent(SetupIntentCreateParams params) throws StripeException {
		return StripeApi.setupIntents().create(params);
	}

	/**
	 * 查询 SetupIntent
	 *
	 * @param id SetupIntent ID(seti_xxx)
	 * @return {@link SetupIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static SetupIntent retrieveSetupIntent(String id) throws StripeException {
		return StripeApi.setupIntents().retrieve(id);
	}

	/**
	 * 查询 SetupIntent(可展开字段)
	 *
	 * @param id     SetupIntent ID(seti_xxx)
	 * @param params {@link SetupIntentRetrieveParams} 查询参数
	 * @return {@link SetupIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static SetupIntent retrieveSetupIntent(String id, SetupIntentRetrieveParams params) throws StripeException {
		return StripeApi.setupIntents().retrieve(id, params);
	}

	/**
	 * 更新 SetupIntent
	 *
	 * @param id     SetupIntent ID(seti_xxx)
	 * @param params {@link SetupIntentUpdateParams} 更新参数
	 * @return {@link SetupIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static SetupIntent updateSetupIntent(String id, SetupIntentUpdateParams params) throws StripeException {
		return StripeApi.setupIntents().update(id, params);
	}

	/**
	 * 确认 SetupIntent
	 *
	 * @param id SetupIntent ID(seti_xxx)
	 * @return {@link SetupIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static SetupIntent confirmSetupIntent(String id) throws StripeException {
		return StripeApi.setupIntents().confirm(id);
	}

	/**
	 * 确认 SetupIntent
	 *
	 * @param id     SetupIntent ID(seti_xxx)
	 * @param params {@link SetupIntentConfirmParams} 确认参数
	 * @return {@link SetupIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static SetupIntent confirmSetupIntent(String id, SetupIntentConfirmParams params) throws StripeException {
		return StripeApi.setupIntents().confirm(id, params);
	}

	/**
	 * 取消 SetupIntent
	 *
	 * @param id SetupIntent ID(seti_xxx)
	 * @return {@link SetupIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static SetupIntent cancelSetupIntent(String id) throws StripeException {
		return StripeApi.setupIntents().cancel(id);
	}

	/**
	 * 取消 SetupIntent
	 *
	 * @param id     SetupIntent ID(seti_xxx)
	 * @param params {@link SetupIntentCancelParams} 取消参数
	 * @return {@link SetupIntent}
	 * @throws StripeException Stripe 异常
	 */
	public static SetupIntent cancelSetupIntent(String id, SetupIntentCancelParams params) throws StripeException {
		return StripeApi.setupIntents().cancel(id, params);
	}

	/**
	 * 列表查询 SetupIntent
	 *
	 * @param params {@link SetupIntentListParams} 列表参数
	 * @return {@link StripeCollection}&lt;{@link SetupIntent}&gt;
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<SetupIntent> listSetupIntents(SetupIntentListParams params) throws StripeException {
		return StripeApi.setupIntents().list(params);
	}
}
