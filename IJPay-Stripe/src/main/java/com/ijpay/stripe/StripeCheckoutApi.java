package com.ijpay.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.StripeCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PaymentLinkListParams;
import com.stripe.param.PaymentLinkRetrieveParams;
import com.stripe.param.PaymentLinkUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionExpireParams;
import com.stripe.param.checkout.SessionListParams;
import com.stripe.param.checkout.SessionRetrieveParams;
import com.stripe.param.checkout.SessionUpdateParams;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>Stripe 托管结账 API 薄封装：Checkout Sessions 与 Payment Links 两种无需自建收银台的收款方式。</p>
 *
 * @author Javen
 */
public class StripeCheckoutApi {

	private StripeCheckoutApi() {
	}

	/* ==================== Checkout Sessions 托管结账会话 ==================== */

	/**
	 * 创建 Checkout Session(Stripe 托管支付页，用户跳转 url 或通过 Embedded mode 内嵌)
	 *
	 * @param params {@link SessionCreateParams} 创建参数(LineItems、SuccessUrl、Mode 等)
	 * @return {@link Session}
	 * @throws StripeException Stripe 异常
	 */
	public static Session createSession(SessionCreateParams params) throws StripeException {
		return StripeApi.checkout().sessions().create(params);
	}

	/**
	 * 查询 Checkout Session
	 *
	 * @param id Session ID(cs_test_xxx / cs_live_xxx)
	 * @return {@link Session}
	 * @throws StripeException Stripe 异常
	 */
	public static Session retrieveSession(String id) throws StripeException {
		return StripeApi.checkout().sessions().retrieve(id);
	}

	/**
	 * 查询 Checkout Session(可展开字段)
	 *
	 * @param id     Session ID
	 * @param params {@link SessionRetrieveParams} 查询参数
	 * @return {@link Session}
	 * @throws StripeException Stripe 异常
	 */
	public static Session retrieveSession(String id, SessionRetrieveParams params) throws StripeException {
		return StripeApi.checkout().sessions().retrieve(id, params);
	}

	/**
	 * 更新 Checkout Session(仅在未支付前可更新部分字段)
	 *
	 * @param id     Session ID
	 * @param params {@link SessionUpdateParams} 更新参数
	 * @return {@link Session}
	 * @throws StripeException Stripe 异常
	 */
	public static Session updateSession(String id, SessionUpdateParams params) throws StripeException {
		return StripeApi.checkout().sessions().update(id, params);
	}

	/**
	 * 使 Checkout Session 过期(未支付前生效)
	 *
	 * @param id Session ID
	 * @return {@link Session}
	 * @throws StripeException Stripe 异常
	 */
	public static Session expireSession(String id) throws StripeException {
		return StripeApi.checkout().sessions().expire(id);
	}

	/**
	 * 使 Checkout Session 过期
	 *
	 * @param id     Session ID
	 * @param params {@link SessionExpireParams} 过期参数
	 * @return {@link Session}
	 * @throws StripeException Stripe 异常
	 */
	public static Session expireSession(String id, SessionExpireParams params) throws StripeException {
		return StripeApi.checkout().sessions().expire(id, params);
	}

	/**
	 * 列表查询 Checkout Session
	 *
	 * @param params {@link SessionListParams} 列表参数
	 * @return {@link StripeCollection}<{@link Session}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<Session> listSessions(SessionListParams params) throws StripeException {
		return StripeApi.checkout().sessions().list(params);
	}

	/* ==================== Payment Links 支付链接 ==================== */

	/**
	 * 创建 Payment Link(生成一个可重复使用的收款链接)
	 *
	 * @param params {@link PaymentLinkCreateParams} 创建参数
	 * @return {@link PaymentLink}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentLink createPaymentLink(PaymentLinkCreateParams params) throws StripeException {
		return StripeApi.paymentLinks().create(params);
	}

	/**
	 * 查询 Payment Link
	 *
	 * @param id Payment Link ID(plink_xxx)
	 * @return {@link PaymentLink}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentLink retrievePaymentLink(String id) throws StripeException {
		return StripeApi.paymentLinks().retrieve(id);
	}

	/**
	 * 查询 Payment Link(可展开字段)
	 *
	 * @param id     Payment Link ID
	 * @param params {@link PaymentLinkRetrieveParams} 查询参数
	 * @return {@link PaymentLink}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentLink retrievePaymentLink(String id, PaymentLinkRetrieveParams params) throws StripeException {
		return StripeApi.paymentLinks().retrieve(id, params);
	}

	/**
	 * 更新 Payment Link
	 *
	 * @param id     Payment Link ID
	 * @param params {@link PaymentLinkUpdateParams} 更新参数
	 * @return {@link PaymentLink}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentLink updatePaymentLink(String id, PaymentLinkUpdateParams params) throws StripeException {
		return StripeApi.paymentLinks().update(id, params);
	}

	/**
	 * 列表查询 Payment Link
	 *
	 * @param params {@link PaymentLinkListParams} 列表参数
	 * @return {@link StripeCollection}<{@link PaymentLink}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<PaymentLink> listPaymentLinks(PaymentLinkListParams params) throws StripeException {
		return StripeApi.paymentLinks().list(params);
	}
}
