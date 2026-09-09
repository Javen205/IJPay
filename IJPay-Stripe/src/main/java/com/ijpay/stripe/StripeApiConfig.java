package com.ijpay.stripe;

import com.stripe.StripeClient;

import java.io.Serializable;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875、864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a></p>
 *
 * <p>Stripe 支付配置</p>
 *
 * @author Javen
 */
public class StripeApiConfig implements Serializable {

	private static final long serialVersionUID = -7068916394956694648L;

	/**
	 * 配置标识，用于 {@link StripeApiConfigKit} 多账户缓存，可自定义（如商户名称、账号备注）
	 */
	private String appId;

	/**
	 * 密钥（Secret Key，sk_test_ 或 sk_live_ 开头）
	 */
	private String apiKey;

	/**
	 * Webhook 签名密钥（whsec_ 开头），用于回调验签
	 */
	private String webhookSecret;

	/**
	 * 是否沙箱环境（使用 sk_test_ 密钥时置为 true）
	 */
	private boolean sandBox;

	/**
	 * Stripe API 客户端（延迟构建，无需手动设置）
	 */
	private transient volatile StripeClient stripeClient;

	public String getAppId() {
		return appId;
	}

	public StripeApiConfig setAppId(String appId) {
		this.appId = appId;
		return this;
	}

	public String getApiKey() {
		if (apiKey == null || apiKey.isEmpty()) {
			throw new IllegalStateException("apiKey 未被赋值");
		}
		return apiKey;
	}

	public StripeApiConfig setApiKey(String apiKey) {
		if (apiKey == null || apiKey.isEmpty()) {
			throw new IllegalArgumentException("apiKey 值不能为空");
		}
		this.apiKey = apiKey;
		return this;
	}

	public String getWebhookSecret() {
		return webhookSecret;
	}

	public StripeApiConfig setWebhookSecret(String webhookSecret) {
		this.webhookSecret = webhookSecret;
		return this;
	}

	public boolean isSandBox() {
		return sandBox;
	}

	public StripeApiConfig setSandBox(boolean sandBox) {
		this.sandBox = sandBox;
		return this;
	}

	/**
	 * 获取 Stripe API 客户端，若未构建则使用 {@link #apiKey} 延迟构建并缓存。
	 * 线程安全：仅首次构建时同步，后续直接返回缓存实例。
	 *
	 * @return {@link StripeClient} Stripe API 客户端
	 */
	public StripeClient getStripeClient() {
		if (stripeClient == null) {
			synchronized (this) {
				if (stripeClient == null) {
					stripeClient = new StripeClient(getApiKey());
				}
			}
		}
		return stripeClient;
	}
}
