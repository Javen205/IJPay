package com.ijpay.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeCollection;
import com.stripe.model.StripeSearchResult;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import com.stripe.param.CustomerRetrieveParams;
import com.stripe.param.CustomerSearchParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.PaymentMethodDetachParams;
import com.stripe.param.PaymentMethodListParams;
import com.stripe.param.PaymentMethodRetrieveParams;
import com.stripe.param.PaymentMethodUpdateParams;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>Stripe 客户与支付方式 API 薄封装(Customers / Payment Methods)。</p>
 *
 * @author Javen
 */
public class StripeCustomerApi {

	private StripeCustomerApi() {
	}

	/* ==================== Customers 客户 ==================== */

	/**
	 * 创建客户
	 *
	 * @param params {@link CustomerCreateParams} 创建参数
	 * @return {@link Customer}
	 * @throws StripeException Stripe 异常
	 */
	public static Customer createCustomer(CustomerCreateParams params) throws StripeException {
		return StripeApi.customers().create(params);
	}

	/**
	 * 查询客户
	 *
	 * @param id 客户 ID(cus_xxx)
	 * @return {@link Customer}
	 * @throws StripeException Stripe 异常
	 */
	public static Customer retrieveCustomer(String id) throws StripeException {
		return StripeApi.customers().retrieve(id);
	}

	/**
	 * 查询客户(可展开字段)
	 *
	 * @param id     客户 ID(cus_xxx)
	 * @param params {@link CustomerRetrieveParams} 查询参数
	 * @return {@link Customer}
	 * @throws StripeException Stripe 异常
	 */
	public static Customer retrieveCustomer(String id, CustomerRetrieveParams params) throws StripeException {
		return StripeApi.customers().retrieve(id, params);
	}

	/**
	 * 更新客户
	 *
	 * @param id     客户 ID(cus_xxx)
	 * @param params {@link CustomerUpdateParams} 更新参数
	 * @return {@link Customer}
	 * @throws StripeException Stripe 异常
	 */
	public static Customer updateCustomer(String id, CustomerUpdateParams params) throws StripeException {
		return StripeApi.customers().update(id, params);
	}

	/**
	 * 删除客户(不影响历史交易)
	 *
	 * @param id 客户 ID(cus_xxx)
	 * @return {@link Customer}
	 * @throws StripeException Stripe 异常
	 */
	public static Customer deleteCustomer(String id) throws StripeException {
		return StripeApi.customers().delete(id);
	}

	/**
	 * 列表查询客户
	 *
	 * @param params {@link CustomerListParams} 列表参数
	 * @return {@link StripeCollection}<{@link Customer}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<Customer> listCustomers(CustomerListParams params) throws StripeException {
		return StripeApi.customers().list(params);
	}

	/**
	 * 搜索客户
	 *
	 * @param params {@link CustomerSearchParams} 搜索参数(使用 query 表达式)
	 * @return {@link StripeSearchResult}<{@link Customer}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeSearchResult<Customer> searchCustomers(CustomerSearchParams params) throws StripeException {
		return StripeApi.customers().search(params);
	}

	/* ==================== Payment Methods 支付方式 ==================== */

	/**
	 * 创建 PaymentMethod(通常建议由 Stripe.js/Elements 在客户端创建后把 id 回传)
	 *
	 * @param params {@link PaymentMethodCreateParams} 创建参数
	 * @return {@link PaymentMethod}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentMethod createPaymentMethod(PaymentMethodCreateParams params) throws StripeException {
		return StripeApi.paymentMethods().create(params);
	}

	/**
	 * 查询 PaymentMethod
	 *
	 * @param id PaymentMethod ID(pm_xxx)
	 * @return {@link PaymentMethod}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentMethod retrievePaymentMethod(String id) throws StripeException {
		return StripeApi.paymentMethods().retrieve(id);
	}

	/**
	 * 查询 PaymentMethod(可展开字段)
	 *
	 * @param id     PaymentMethod ID(pm_xxx)
	 * @param params {@link PaymentMethodRetrieveParams} 查询参数
	 * @return {@link PaymentMethod}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentMethod retrievePaymentMethod(String id, PaymentMethodRetrieveParams params) throws StripeException {
		return StripeApi.paymentMethods().retrieve(id, params);
	}

	/**
	 * 更新 PaymentMethod(如账单地址、元数据等)
	 *
	 * @param id     PaymentMethod ID(pm_xxx)
	 * @param params {@link PaymentMethodUpdateParams} 更新参数
	 * @return {@link PaymentMethod}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentMethod updatePaymentMethod(String id, PaymentMethodUpdateParams params) throws StripeException {
		return StripeApi.paymentMethods().update(id, params);
	}

	/**
	 * 将 PaymentMethod 绑定到客户
	 *
	 * @param id     PaymentMethod ID(pm_xxx)
	 * @param params {@link PaymentMethodAttachParams} 绑定参数(customer 必填)
	 * @return {@link PaymentMethod}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentMethod attachPaymentMethod(String id, PaymentMethodAttachParams params) throws StripeException {
		return StripeApi.paymentMethods().attach(id, params);
	}

	/**
	 * 将 PaymentMethod 从客户解绑
	 *
	 * @param id PaymentMethod ID(pm_xxx)
	 * @return {@link PaymentMethod}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentMethod detachPaymentMethod(String id) throws StripeException {
		return StripeApi.paymentMethods().detach(id);
	}

	/**
	 * 将 PaymentMethod 从客户解绑
	 *
	 * @param id     PaymentMethod ID(pm_xxx)
	 * @param params {@link PaymentMethodDetachParams} 解绑参数
	 * @return {@link PaymentMethod}
	 * @throws StripeException Stripe 异常
	 */
	public static PaymentMethod detachPaymentMethod(String id, PaymentMethodDetachParams params) throws StripeException {
		return StripeApi.paymentMethods().detach(id, params);
	}

	/**
	 * 列表查询 PaymentMethod
	 *
	 * @param params {@link PaymentMethodListParams} 列表参数(通常需指定 customer 与 type)
	 * @return {@link StripeCollection}<{@link PaymentMethod}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<PaymentMethod> listPaymentMethods(PaymentMethodListParams params) throws StripeException {
		return StripeApi.paymentMethods().list(params);
	}
}
