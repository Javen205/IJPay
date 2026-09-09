package com.ijpay.stripe;

import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.RequestOptions;
import com.stripe.service.AccountLinkService;
import com.stripe.service.AccountService;
import com.stripe.service.BalanceService;
import com.stripe.service.ChargeService;
import com.stripe.service.CheckoutService;
import com.stripe.service.CustomerService;
import com.stripe.service.InvoiceService;
import com.stripe.service.PaymentIntentService;
import com.stripe.service.PaymentLinkService;
import com.stripe.service.PaymentMethodService;
import com.stripe.service.PayoutService;
import com.stripe.service.ProductService;
import com.stripe.service.PriceService;
import com.stripe.service.RefundService;
import com.stripe.service.SetupIntentService;
import com.stripe.service.SubscriptionService;
import com.stripe.service.TransferService;
import com.stripe.service.ApplicationFeeService;
import com.stripe.service.AccountSessionService;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>Stripe 支付 API 通用门面，负责统一管理 Stripe 客户端(Secret Key)的获取、请求选项以及 Webhook 事件验签。</p>
 *
 * <p>所有 {@code service} 入口均可直接透传 stripe-java 官方类型安全的 Params，返回官方 Model，
 * 因此本模块天然支持 stripe-java 官方 SDK 的全部能力。</p>
 *
 * <p>注意：stripe-java 新版本已将 {@link StripeClient} 上的直接 service 访问器标记为
 * {@code @Deprecated}，统一推荐通过 {@code StripeClient#v1()} 聚合入口获取，本类已按该方式适配。</p>
 *
 * @author Javen
 */
public class StripeApi {

	private StripeApi() {
	}

	/**
	 * 获取当前线程/默认配置对应的 Stripe 客户端
	 *
	 * @return {@link StripeClient} Stripe 客户端
	 */
	public static StripeClient getStripeClient() {
		return StripeApiConfigKit.getApiConfig().getStripeClient();
	}

	/**
	 * 通过指定 appId 获取 Stripe 客户端
	 *
	 * @param appId 配置标识
	 * @return {@link StripeClient} Stripe 客户端
	 */
	public static StripeClient getStripeClient(String appId) {
		return StripeApiConfigKit.getApiConfig(appId).getStripeClient();
	}

	/**
	 * 通过指定 {@link StripeApiConfig} 获取 Stripe 客户端
	 *
	 * @param config {@link StripeApiConfig} Stripe 支付配置
	 * @return {@link StripeClient} Stripe 客户端
	 */
	public static StripeClient getStripeClient(StripeApiConfig config) {
		return config.getStripeClient();
	}

	/* ==================== 官方 Service 入口(透传全部能力) ==================== */

	/**
	 * 支付意图服务(Payment Intents)
	 *
	 * @return {@link PaymentIntentService}
	 */
	public static PaymentIntentService paymentIntents() {
		return getStripeClient().v1().paymentIntents();
	}

	/**
	 * 保存支付方式服务(Setup Intents)
	 *
	 * @return {@link SetupIntentService}
	 */
	public static SetupIntentService setupIntents() {
		return getStripeClient().v1().setupIntents();
	}

	/**
	 * 收款单服务(Charges)
	 *
	 * @return {@link ChargeService}
	 */
	public static ChargeService charges() {
		return getStripeClient().v1().charges();
	}

	/**
	 * 退款服务(Refunds)
	 *
	 * @return {@link RefundService}
	 */
	public static RefundService refunds() {
		return getStripeClient().v1().refunds();
	}

	/**
	 * 客户服务(Customers)
	 *
	 * @return {@link CustomerService}
	 */
	public static CustomerService customers() {
		return getStripeClient().v1().customers();
	}

	/**
	 * 支付方式服务(Payment Methods)
	 *
	 * @return {@link PaymentMethodService}
	 */
	public static PaymentMethodService paymentMethods() {
		return getStripeClient().v1().paymentMethods();
	}

	/**
	 * Checkout 服务(Checkout Sessions)
	 *
	 * @return {@link CheckoutService}
	 */
	public static CheckoutService checkout() {
		return getStripeClient().v1().checkout();
	}

	/**
	 * 支付链接服务(Payment Links)
	 *
	 * @return {@link PaymentLinkService}
	 */
	public static PaymentLinkService paymentLinks() {
		return getStripeClient().v1().paymentLinks();
	}

	/**
	 * 订阅服务(Subscriptions)
	 *
	 * @return {@link SubscriptionService}
	 */
	public static SubscriptionService subscriptions() {
		return getStripeClient().v1().subscriptions();
	}

	/**
	 * 发票服务(Invoices)
	 *
	 * @return {@link InvoiceService}
	 */
	public static InvoiceService invoices() {
		return getStripeClient().v1().invoices();
	}

	/**
	 * 商品服务(Products)
	 *
	 * @return {@link ProductService}
	 */
	public static ProductService products() {
		return getStripeClient().v1().products();
	}

	/**
	 * 价格服务(Prices)
	 *
	 * @return {@link PriceService}
	 */
	public static PriceService prices() {
		return getStripeClient().v1().prices();
	}

	/**
	 * 账户服务(Accounts，Connect 平台管理子商户)
	 *
	 * @return {@link AccountService}
	 */
	public static AccountService accounts() {
		return getStripeClient().v1().accounts();
	}

	/**
	 * 账户链接服务(Account Links，Connect 入驻)
	 *
	 * @return {@link AccountLinkService}
	 */
	public static AccountLinkService accountLinks() {
		return getStripeClient().v1().accountLinks();
	}

	/**
	 * 账户会话服务(Account Sessions，Connect 嵌入式组件)
	 *
	 * @return {@link AccountSessionService}
	 */
	public static AccountSessionService accountSessions() {
		return getStripeClient().v1().accountSessions();
	}

	/**
	 * 转账服务(Transfers，Connect 平台向子商户打款)
	 *
	 * @return {@link TransferService}
	 */
	public static TransferService transfers() {
		return getStripeClient().v1().transfers();
	}

	/**
	 * 提现服务(Payouts，提现到银行卡)
	 *
	 * @return {@link PayoutService}
	 */
	public static PayoutService payouts() {
		return getStripeClient().v1().payouts();
	}

	/**
	 * 余额服务(Balance)
	 *
	 * @return {@link BalanceService}
	 */
	public static BalanceService balance() {
		return getStripeClient().v1().balance();
	}

	/**
	 * 平台费用服务(Application Fees)
	 *
	 * @return {@link ApplicationFeeService}
	 */
	public static ApplicationFeeService applicationFees() {
		return getStripeClient().v1().applicationFees();
	}

	/* ==================== RequestOptions 构建工具 ==================== */

	/**
	 * 构建带幂等键与 Connect 目标账户的请求选项
	 *
	 * @param idempotencyKey 幂等键，用于防止重复提交，可为 null
	 * @param stripeAccount Connect 目标子商户 ID(acct_xxx)，普通单商户可传 null
	 * @return {@link RequestOptions}
	 */
	public static RequestOptions buildRequestOptions(String idempotencyKey, String stripeAccount) {
		RequestOptions.RequestOptionsBuilder builder = RequestOptions.builder();
		if (idempotencyKey != null && !idempotencyKey.isEmpty()) {
			builder.setIdempotencyKey(idempotencyKey);
		}
		if (stripeAccount != null && !stripeAccount.isEmpty()) {
			builder.setStripeAccount(stripeAccount);
		}
		return builder.build();
	}

	/* ==================== Webhook 事件验签 ==================== */

	/**
	 * 通过当前默认配置中的 Webhook Secret 校验并构造事件对象
	 *
	 * @param payload   请求体原始内容(未序列化的 JSON 字符串)
	 * @param sigHeader 请求头 {@code Stripe-Signature} 的值
	 * @return {@link Event} 校验通过后的事件对象
	 * @throws SignatureVerificationException 验签失败
	 */
	public static Event constructEvent(String payload, String sigHeader) throws SignatureVerificationException {
		String webhookSecret = StripeApiConfigKit.getApiConfig().getWebhookSecret();
		if (webhookSecret == null || webhookSecret.isEmpty()) {
			throw new IllegalStateException("请先在 StripeApiConfig 中设置 webhookSecret");
		}
		return constructEvent(payload, sigHeader, webhookSecret);
	}

	/**
	 * 校验并构造事件对象
	 *
	 * @param payload       请求体原始内容
	 * @param sigHeader     请求头 {@code Stripe-Signature} 的值
	 * @param webhookSecret Webhook 签名密钥(whsec_ 开头)
	 * @return {@link Event} 校验通过后的事件对象
	 * @throws SignatureVerificationException 验签失败
	 */
	public static Event constructEvent(String payload, String sigHeader, String webhookSecret) throws SignatureVerificationException {
		return StripeApiConfigKit.getApiConfig().getStripeClient().constructEvent(payload, sigHeader, webhookSecret);
	}

	/**
	 * 校验并构造事件对象(可自定义时间容差)
	 *
	 * @param payload       请求体原始内容
	 * @param sigHeader     请求头 {@code Stripe-Signature} 的值
	 * @param webhookSecret Webhook 签名密钥(whsec_ 开头)
	 * @param tolerance     允许的时间偏移(秒)
	 * @return {@link Event} 校验通过后的事件对象
	 * @throws SignatureVerificationException 验签失败
	 */
	public static Event constructEvent(String payload, String sigHeader, String webhookSecret, long tolerance) throws SignatureVerificationException {
		return StripeApiConfigKit.getApiConfig().getStripeClient().constructEvent(payload, sigHeader, webhookSecret, tolerance);
	}

	/**
	 * 不校验签名直接构造事件对象(仅用于本地调试，生产环境禁止使用)
	 *
	 * @param payload 请求体原始内容
	 * @return {@link Event}
	 */
	public static Event constructEventWithoutVerification(String payload) {
		return StripeApiConfigKit.getApiConfig().getStripeClient().constructEventWithoutVerification(payload);
	}

}
