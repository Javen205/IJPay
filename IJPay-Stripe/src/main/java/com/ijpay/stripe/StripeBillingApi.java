package com.ijpay.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeSearchResult;
import com.stripe.model.Subscription;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceFinalizeInvoiceParams;
import com.stripe.param.InvoiceListParams;
import com.stripe.param.InvoiceMarkUncollectibleParams;
import com.stripe.param.InvoicePayParams;
import com.stripe.param.InvoiceRetrieveParams;
import com.stripe.param.InvoiceSearchParams;
import com.stripe.param.InvoiceSendInvoiceParams;
import com.stripe.param.InvoiceUpdateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.PriceListParams;
import com.stripe.param.PriceRetrieveParams;
import com.stripe.param.PriceSearchParams;
import com.stripe.param.PriceUpdateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductListParams;
import com.stripe.param.ProductRetrieveParams;
import com.stripe.param.ProductSearchParams;
import com.stripe.param.ProductUpdateParams;
import com.stripe.param.SubscriptionCancelParams;
import com.stripe.param.SubscriptionCreateParams;
import com.stripe.param.SubscriptionListParams;
import com.stripe.param.SubscriptionRetrieveParams;
import com.stripe.param.SubscriptionSearchParams;
import com.stripe.param.SubscriptionUpdateParams;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>Stripe Billing 计费域 API 薄封装：商品、价格、订阅、发票。</p>
 *
 * @author Javen
 */
public class StripeBillingApi {

	private StripeBillingApi() {
	}

	/* ==================== Products 商品 ==================== */

	/**
	 * 创建商品
	 *
	 * @param params {@link ProductCreateParams} 创建参数
	 * @return {@link Product}
	 * @throws StripeException Stripe 异常
	 */
	public static Product createProduct(ProductCreateParams params) throws StripeException {
		return StripeApi.products().create(params);
	}

	/**
	 * 查询商品
	 *
	 * @param id 商品 ID(prod_xxx)
	 * @return {@link Product}
	 * @throws StripeException Stripe 异常
	 */
	public static Product retrieveProduct(String id) throws StripeException {
		return StripeApi.products().retrieve(id);
	}

	/**
	 * 查询商品(可展开字段)
	 *
	 * @param id     商品 ID(prod_xxx)
	 * @param params {@link ProductRetrieveParams} 查询参数
	 * @return {@link Product}
	 * @throws StripeException Stripe 异常
	 */
	public static Product retrieveProduct(String id, ProductRetrieveParams params) throws StripeException {
		return StripeApi.products().retrieve(id, params);
	}

	/**
	 * 更新商品
	 *
	 * @param id     商品 ID(prod_xxx)
	 * @param params {@link ProductUpdateParams} 更新参数
	 * @return {@link Product}
	 * @throws StripeException Stripe 异常
	 */
	public static Product updateProduct(String id, ProductUpdateParams params) throws StripeException {
		return StripeApi.products().update(id, params);
	}

	/**
	 * 删除/归档商品
	 *
	 * @param id 商品 ID(prod_xxx)
	 * @return {@link Product}
	 * @throws StripeException Stripe 异常
	 */
	public static Product deleteProduct(String id) throws StripeException {
		return StripeApi.products().delete(id);
	}

	/**
	 * 列表查询商品
	 *
	 * @param params {@link ProductListParams} 列表参数
	 * @return {@link StripeCollection}<{@link Product}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<Product> listProducts(ProductListParams params) throws StripeException {
		return StripeApi.products().list(params);
	}

	/**
	 * 搜索商品
	 *
	 * @param params {@link ProductSearchParams} 搜索参数
	 * @return {@link StripeSearchResult}<{@link Product}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeSearchResult<Product> searchProducts(ProductSearchParams params) throws StripeException {
		return StripeApi.products().search(params);
	}

	/* ==================== Prices 价格 ==================== */

	/**
	 * 创建价格(绑定商品)
	 *
	 * @param params {@link PriceCreateParams} 创建参数
	 * @return {@link Price}
	 * @throws StripeException Stripe 异常
	 */
	public static Price createPrice(PriceCreateParams params) throws StripeException {
		return StripeApi.prices().create(params);
	}

	/**
	 * 查询价格
	 *
	 * @param id 价格 ID(price_xxx)
	 * @return {@link Price}
	 * @throws StripeException Stripe 异常
	 */
	public static Price retrievePrice(String id) throws StripeException {
		return StripeApi.prices().retrieve(id);
	}

	/**
	 * 查询价格(可展开字段)
	 *
	 * @param id     价格 ID(price_xxx)
	 * @param params {@link PriceRetrieveParams} 查询参数
	 * @return {@link Price}
	 * @throws StripeException Stripe 异常
	 */
	public static Price retrievePrice(String id, PriceRetrieveParams params) throws StripeException {
		return StripeApi.prices().retrieve(id, params);
	}

	/**
	 * 更新价格
	 *
	 * @param id     价格 ID(price_xxx)
	 * @param params {@link PriceUpdateParams} 更新参数
	 * @return {@link Price}
	 * @throws StripeException Stripe 异常
	 */
	public static Price updatePrice(String id, PriceUpdateParams params) throws StripeException {
		return StripeApi.prices().update(id, params);
	}

	/**
	 * 列表查询价格
	 *
	 * @param params {@link PriceListParams} 列表参数
	 * @return {@link StripeCollection}<{@link Price}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<Price> listPrices(PriceListParams params) throws StripeException {
		return StripeApi.prices().list(params);
	}

	/**
	 * 搜索价格
	 *
	 * @param params {@link PriceSearchParams} 搜索参数
	 * @return {@link StripeSearchResult}<{@link Price}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeSearchResult<Price> searchPrices(PriceSearchParams params) throws StripeException {
		return StripeApi.prices().search(params);
	}

	/* ==================== Subscriptions 订阅 ==================== */

	/**
	 * 创建订阅(需提供 customer 与 items)
	 *
	 * @param params {@link SubscriptionCreateParams} 创建参数
	 * @return {@link Subscription}
	 * @throws StripeException Stripe 异常
	 */
	public static Subscription createSubscription(SubscriptionCreateParams params) throws StripeException {
		return StripeApi.subscriptions().create(params);
	}

	/**
	 * 查询订阅
	 *
	 * @param id 订阅 ID(sub_xxx)
	 * @return {@link Subscription}
	 * @throws StripeException Stripe 异常
	 */
	public static Subscription retrieveSubscription(String id) throws StripeException {
		return StripeApi.subscriptions().retrieve(id);
	}

	/**
	 * 查询订阅(可展开字段)
	 *
	 * @param id     订阅 ID(sub_xxx)
	 * @param params {@link SubscriptionRetrieveParams} 查询参数
	 * @return {@link Subscription}
	 * @throws StripeException Stripe 异常
	 */
	public static Subscription retrieveSubscription(String id, SubscriptionRetrieveParams params) throws StripeException {
		return StripeApi.subscriptions().retrieve(id, params);
	}

	/**
	 * 更新订阅(如更换价格、调整数量等)
	 *
	 * @param id     订阅 ID(sub_xxx)
	 * @param params {@link SubscriptionUpdateParams} 更新参数
	 * @return {@link Subscription}
	 * @throws StripeException Stripe 异常
	 */
	public static Subscription updateSubscription(String id, SubscriptionUpdateParams params) throws StripeException {
		return StripeApi.subscriptions().update(id, params);
	}

	/**
	 * 取消订阅(账单期结束后停止扣款)
	 *
	 * @param id 订阅 ID(sub_xxx)
	 * @return {@link Subscription}
	 * @throws StripeException Stripe 异常
	 */
	public static Subscription cancelSubscription(String id) throws StripeException {
		return StripeApi.subscriptions().cancel(id);
	}

	/**
	 * 取消订阅(可指定立即取消或账单期结束后取消)
	 *
	 * @param id     订阅 ID(sub_xxx)
	 * @param params {@link SubscriptionCancelParams} 取消参数
	 * @return {@link Subscription}
	 * @throws StripeException Stripe 异常
	 */
	public static Subscription cancelSubscription(String id, SubscriptionCancelParams params) throws StripeException {
		return StripeApi.subscriptions().cancel(id, params);
	}

	/**
	 * 列表查询订阅
	 *
	 * @param params {@link SubscriptionListParams} 列表参数
	 * @return {@link StripeCollection}<{@link Subscription}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<Subscription> listSubscriptions(SubscriptionListParams params) throws StripeException {
		return StripeApi.subscriptions().list(params);
	}

	/**
	 * 搜索订阅
	 *
	 * @param params {@link SubscriptionSearchParams} 搜索参数
	 * @return {@link StripeSearchResult}<{@link Subscription}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeSearchResult<Subscription> searchSubscriptions(SubscriptionSearchParams params) throws StripeException {
		return StripeApi.subscriptions().search(params);
	}

	/* ==================== Invoices 发票 ==================== */

	/**
	 * 创建发票(通常与订阅配合，也可创建独立发票后再添加发票明细)
	 *
	 * @param params {@link InvoiceCreateParams} 创建参数
	 * @return {@link Invoice}
	 * @throws StripeException Stripe 异常
	 */
	public static Invoice createInvoice(InvoiceCreateParams params) throws StripeException {
		return StripeApi.invoices().create(params);
	}

	/**
	 * 查询发票
	 *
	 * @param id 发票 ID(in_xxx)
	 * @return {@link Invoice}
	 * @throws StripeException Stripe 异常
	 */
	public static Invoice retrieveInvoice(String id) throws StripeException {
		return StripeApi.invoices().retrieve(id);
	}

	/**
	 * 查询发票(可展开字段)
	 *
	 * @param id     发票 ID(in_xxx)
	 * @param params {@link InvoiceRetrieveParams} 查询参数
	 * @return {@link Invoice}
	 * @throws StripeException Stripe 异常
	 */
	public static Invoice retrieveInvoice(String id, InvoiceRetrieveParams params) throws StripeException {
		return StripeApi.invoices().retrieve(id, params);
	}

	/**
	 * 更新发票
	 *
	 * @param id     发票 ID(in_xxx)
	 * @param params {@link InvoiceUpdateParams} 更新参数
	 * @return {@link Invoice}
	 * @throws StripeException Stripe 异常
	 */
	public static Invoice updateInvoice(String id, InvoiceUpdateParams params) throws StripeException {
		return StripeApi.invoices().update(id, params);
	}

	/**
	 * 删除草稿状态发票
	 *
	 * @param id 发票 ID(in_xxx)
	 * @return {@link Invoice}
	 * @throws StripeException Stripe 异常
	 */
	public static Invoice deleteInvoice(String id) throws StripeException {
		return StripeApi.invoices().delete(id);
	}

	/**
	 * 终结发票(草稿 -> 待支付)
	 *
	 * @param id 发票 ID(in_xxx)
	 * @return {@link Invoice}
	 * @throws StripeException Stripe 异常
	 */
	public static Invoice finalizeInvoice(String id) throws StripeException {
		return StripeApi.invoices().finalizeInvoice(id);
	}

	/**
	 * 终结发票
	 *
	 * @param id     发票 ID(in_xxx)
	 * @param params {@link InvoiceFinalizeInvoiceParams} 终结参数
	 * @return {@link Invoice}
	 * @throws StripeException Stripe 异常
	 */
	public static Invoice finalizeInvoice(String id, InvoiceFinalizeInvoiceParams params) throws StripeException {
		return StripeApi.invoices().finalizeInvoice(id, params);
	}

	/**
	 * 支付发票(使用客户默认支付方式扣款)
	 *
	 * @param id 发票 ID(in_xxx)
	 * @return {@link Invoice}
	 * @throws StripeException Stripe 异常
	 */
	public static Invoice payInvoice(String id) throws StripeException {
		return StripeApi.invoices().pay(id);
	}

	/**
	 * 支付发票
	 *
	 * @param id     发票 ID(in_xxx)
	 * @param params {@link InvoicePayParams} 支付参数
	 * @return {@link Invoice}
	 * @throws StripeException Stripe 异常
	 */
	public static Invoice payInvoice(String id, InvoicePayParams params) throws StripeException {
		return StripeApi.invoices().pay(id, params);
	}

	/**
	 * 发送发票邮件给客户(需要配置邮寄发票)
	 *
	 * @param id 发票 ID(in_xxx)
	 * @return {@link Invoice}
	 * @throws StripeException Stripe 异常
	 */
	public static Invoice sendInvoice(String id) throws StripeException {
		return StripeApi.invoices().sendInvoice(id);
	}

	/**
	 * 发送发票邮件给客户
	 *
	 * @param id     发票 ID(in_xxx)
	 * @param params {@link InvoiceSendInvoiceParams} 发送参数
	 * @return {@link Invoice}
	 * @throws StripeException Stripe 异常
	 */
	public static Invoice sendInvoice(String id, InvoiceSendInvoiceParams params) throws StripeException {
		return StripeApi.invoices().sendInvoice(id, params);
	}

	/**
	 * 标记发票为无法收回(催收场景)
	 *
	 * @param id 发票 ID(in_xxx)
	 * @return {@link Invoice}
	 * @throws StripeException Stripe 异常
	 */
	public static Invoice markUncollectibleInvoice(String id) throws StripeException {
		return StripeApi.invoices().markUncollectible(id);
	}

	/**
	 * 标记发票为无法收回
	 *
	 * @param id     发票 ID(in_xxx)
	 * @param params {@link InvoiceMarkUncollectibleParams} 参数
	 * @return {@link Invoice}
	 * @throws StripeException Stripe 异常
	 */
	public static Invoice markUncollectibleInvoice(String id, InvoiceMarkUncollectibleParams params) throws StripeException {
		return StripeApi.invoices().markUncollectible(id, params);
	}

	/**
	 * 列表查询发票
	 *
	 * @param params {@link InvoiceListParams} 列表参数
	 * @return {@link StripeCollection}<{@link Invoice}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<Invoice> listInvoices(InvoiceListParams params) throws StripeException {
		return StripeApi.invoices().list(params);
	}

	/**
	 * 搜索发票
	 *
	 * @param params {@link InvoiceSearchParams} 搜索参数
	 * @return {@link StripeSearchResult}<{@link Invoice}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeSearchResult<Invoice> searchInvoices(InvoiceSearchParams params) throws StripeException {
		return StripeApi.invoices().search(params);
	}
}
