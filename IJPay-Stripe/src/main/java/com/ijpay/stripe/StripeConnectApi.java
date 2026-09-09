package com.ijpay.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.AccountSession;
import com.stripe.model.ApplicationFee;
import com.stripe.model.Balance;
import com.stripe.model.Payout;
import com.stripe.model.StripeCollection;
import com.stripe.model.Transfer;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.AccountListParams;
import com.stripe.param.AccountRejectParams;
import com.stripe.param.AccountRetrieveCurrentParams;
import com.stripe.param.AccountRetrieveParams;
import com.stripe.param.AccountSessionCreateParams;
import com.stripe.param.AccountUpdateParams;
import com.stripe.param.ApplicationFeeListParams;
import com.stripe.param.ApplicationFeeRetrieveParams;
import com.stripe.param.PayoutCancelParams;
import com.stripe.param.PayoutCreateParams;
import com.stripe.param.PayoutListParams;
import com.stripe.param.PayoutRetrieveParams;
import com.stripe.param.PayoutReverseParams;
import com.stripe.param.PayoutUpdateParams;
import com.stripe.param.TransferCreateParams;
import com.stripe.param.TransferListParams;
import com.stripe.param.TransferRetrieveParams;
import com.stripe.param.TransferUpdateParams;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>Stripe Connect(平台经济)域 API 薄封装：账户、账户链接、转账、提现、余额、平台费用。</p>
 *
 * <p>说明：Stripe 主账户密钥(apiKey)为平台密钥；涉及子商户对象时，请在官方 Params 或
 * {@link StripeApi#buildRequestOptions(String, String)} 中设置 stripeAccount(acct_xxx)。</p>
 *
 * @author Javen
 */
public class StripeConnectApi {

	private StripeConnectApi() {
	}

	/* ==================== Accounts 账户(子商户) ==================== */

	/**
	 * 创建 Connect 子商户账户(仅平台模式可用)
	 *
	 * @param params {@link AccountCreateParams} 创建参数
	 * @return {@link Account}
	 * @throws StripeException Stripe 异常
	 */
	public static Account createAccount(AccountCreateParams params) throws StripeException {
		return StripeApi.accounts().create(params);
	}

	/**
	 * 查询 Connect 子商户账户
	 *
	 * @param id 账户 ID(acct_xxx)
	 * @return {@link Account}
	 * @throws StripeException Stripe 异常
	 */
	public static Account retrieveAccount(String id) throws StripeException {
		return StripeApi.accounts().retrieve(id);
	}

	/**
	 * 查询 Connect 子商户账户(可展开字段)
	 *
	 * @param id     账户 ID(acct_xxx)
	 * @param params {@link AccountRetrieveParams} 查询参数
	 * @return {@link Account}
	 * @throws StripeException Stripe 异常
	 */
	public static Account retrieveAccount(String id, AccountRetrieveParams params) throws StripeException {
		return StripeApi.accounts().retrieve(id, params);
	}

	/**
	 * 查询平台自身当前账户
	 *
	 * @return {@link Account}
	 * @throws StripeException Stripe 异常
	 */
	public static Account retrieveCurrentAccount() throws StripeException {
		return StripeApi.accounts().retrieveCurrent();
	}

	/**
	 * 查询平台自身当前账户(可展开字段)
	 *
	 * @param params {@link AccountRetrieveCurrentParams} 查询参数
	 * @return {@link Account}
	 * @throws StripeException Stripe 异常
	 */
	public static Account retrieveCurrentAccount(AccountRetrieveCurrentParams params) throws StripeException {
		return StripeApi.accounts().retrieveCurrent(params);
	}

	/**
	 * 更新子商户账户(修改 KYC/业务信息等)
	 *
	 * @param id     账户 ID(acct_xxx)
	 * @param params {@link AccountUpdateParams} 更新参数
	 * @return {@link Account}
	 * @throws StripeException Stripe 异常
	 */
	public static Account updateAccount(String id, AccountUpdateParams params) throws StripeException {
		return StripeApi.accounts().update(id, params);
	}

	/**
	 * 拒绝子商户账户(平台风控，删除账户并拒绝)
	 *
	 * @param id     账户 ID(acct_xxx)
	 * @param params {@link AccountRejectParams} 拒绝参数(reason 必填)
	 * @return {@link Account}
	 * @throws StripeException Stripe 异常
	 */
	public static Account rejectAccount(String id, AccountRejectParams params) throws StripeException {
		return StripeApi.accounts().reject(id, params);
	}

	/**
	 * 删除子商户账户
	 *
	 * @param id 账户 ID(acct_xxx)
	 * @return {@link Account}
	 * @throws StripeException Stripe 异常
	 */
	public static Account deleteAccount(String id) throws StripeException {
		return StripeApi.accounts().delete(id);
	}

	/**
	 * 列表查询子商户账户
	 *
	 * @param params {@link AccountListParams} 列表参数
	 * @return {@link StripeCollection}<{@link Account}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<Account> listAccounts(AccountListParams params) throws StripeException {
		return StripeApi.accounts().list(params);
	}

	/* ==================== Account Links 账户链接(入驻/管理) ==================== */

	/**
	 * 创建 Account Link(用于子商户完成入驻/管理流程，返回 on-boarding 跳转 URL)
	 *
	 * @param params {@link AccountLinkCreateParams} 创建参数(account/refresh_url/return_url/type 必填)
	 * @return {@link AccountLink}
	 * @throws StripeException Stripe 异常
	 */
	public static AccountLink createAccountLink(AccountLinkCreateParams params) throws StripeException {
		return StripeApi.accountLinks().create(params);
	}

	/* ==================== Account Sessions 账户会话(嵌入式组件) ==================== */

	/**
	 * 创建 Account Session(用于在页面上内嵌 Stripe 托管组件完成入驻)
	 *
	 * @param params {@link AccountSessionCreateParams} 创建参数(account/components 必填)
	 * @return {@link AccountSession}
	 * @throws StripeException Stripe 异常
	 */
	public static AccountSession createAccountSession(AccountSessionCreateParams params) throws StripeException {
		return StripeApi.accountSessions().create(params);
	}

	/* ==================== Transfers 转账 ==================== */

	/**
	 * 创建转账(平台向子商户打款，需在请求中标识目标账户)
	 *
	 * @param params {@link TransferCreateParams} 创建参数(amount/currency/destination 必填)
	 * @return {@link Transfer}
	 * @throws StripeException Stripe 异常
	 */
	public static Transfer createTransfer(TransferCreateParams params) throws StripeException {
		return StripeApi.transfers().create(params);
	}

	/**
	 * 查询转账
	 *
	 * @param id 转账 ID(tr_xxx)
	 * @return {@link Transfer}
	 * @throws StripeException Stripe 异常
	 */
	public static Transfer retrieveTransfer(String id) throws StripeException {
		return StripeApi.transfers().retrieve(id);
	}

	/**
	 * 查询转账(可展开字段)
	 *
	 * @param id     转账 ID(tr_xxx)
	 * @param params {@link TransferRetrieveParams} 查询参数
	 * @return {@link Transfer}
	 * @throws StripeException Stripe 异常
	 */
	public static Transfer retrieveTransfer(String id, TransferRetrieveParams params) throws StripeException {
		return StripeApi.transfers().retrieve(id, params);
	}

	/**
	 * 更新转账(如修改元数据、重新计算失败转账等)
	 *
	 * @param id     转账 ID(tr_xxx)
	 * @param params {@link TransferUpdateParams} 更新参数
	 * @return {@link Transfer}
	 * @throws StripeException Stripe 异常
	 */
	public static Transfer updateTransfer(String id, TransferUpdateParams params) throws StripeException {
		return StripeApi.transfers().update(id, params);
	}

	/**
	 * 列表查询转账
	 *
	 * @param params {@link TransferListParams} 列表参数
	 * @return {@link StripeCollection}<{@link Transfer}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<Transfer> listTransfers(TransferListParams params) throws StripeException {
		return StripeApi.transfers().list(params);
	}

	/* ==================== Payouts 提现 ==================== */

	/**
	 * 创建提现(提现到账户绑定的银行卡)
	 *
	 * @param params {@link PayoutCreateParams} 创建参数(amount/currency 必填)
	 * @return {@link Payout}
	 * @throws StripeException Stripe 异常
	 */
	public static Payout createPayout(PayoutCreateParams params) throws StripeException {
		return StripeApi.payouts().create(params);
	}

	/**
	 * 查询提现
	 *
	 * @param id 提现 ID(po_xxx)
	 * @return {@link Payout}
	 * @throws StripeException Stripe 异常
	 */
	public static Payout retrievePayout(String id) throws StripeException {
		return StripeApi.payouts().retrieve(id);
	}

	/**
	 * 查询提现(可展开字段)
	 *
	 * @param id     提现 ID(po_xxx)
	 * @param params {@link PayoutRetrieveParams} 查询参数
	 * @return {@link Payout}
	 * @throws StripeException Stripe 异常
	 */
	public static Payout retrievePayout(String id, PayoutRetrieveParams params) throws StripeException {
		return StripeApi.payouts().retrieve(id, params);
	}

	/**
	 * 更新提现(仅支持 metadata)
	 *
	 * @param id     提现 ID(po_xxx)
	 * @param params {@link PayoutUpdateParams} 更新参数
	 * @return {@link Payout}
	 * @throws StripeException Stripe 异常
	 */
	public static Payout updatePayout(String id, PayoutUpdateParams params) throws StripeException {
		return StripeApi.payouts().update(id, params);
	}

	/**
	 * 取消提现(仅 pending 状态可取消)
	 *
	 * @param id 提现 ID(po_xxx)
	 * @return {@link Payout}
	 * @throws StripeException Stripe 异常
	 */
	public static Payout cancelPayout(String id) throws StripeException {
		return StripeApi.payouts().cancel(id);
	}

	/**
	 * 取消提现
	 *
	 * @param id     提现 ID(po_xxx)
	 * @param params {@link PayoutCancelParams} 取消参数
	 * @return {@link Payout}
	 * @throws StripeException Stripe 异常
	 */
	public static Payout cancelPayout(String id, PayoutCancelParams params) throws StripeException {
		return StripeApi.payouts().cancel(id, params);
	}

	/**
	 * 逆转提现(提现失败退回余额)
	 *
	 * @param id 提现 ID(po_xxx)
	 * @return {@link Payout}
	 * @throws StripeException Stripe 异常
	 */
	public static Payout reversePayout(String id) throws StripeException {
		return StripeApi.payouts().reverse(id);
	}

	/**
	 * 逆转提现
	 *
	 * @param id     提现 ID(po_xxx)
	 * @param params {@link PayoutReverseParams} 逆转参数
	 * @return {@link Payout}
	 * @throws StripeException Stripe 异常
	 */
	public static Payout reversePayout(String id, PayoutReverseParams params) throws StripeException {
		return StripeApi.payouts().reverse(id, params);
	}

	/**
	 * 列表查询提现
	 *
	 * @param params {@link PayoutListParams} 列表参数
	 * @return {@link StripeCollection}<{@link Payout}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<Payout> listPayouts(PayoutListParams params) throws StripeException {
		return StripeApi.payouts().list(params);
	}

	/* ==================== Balance 余额 ==================== */

	/**
	 * 查询当前账户可用/待结算余额
	 *
	 * @return {@link Balance}
	 * @throws StripeException Stripe 异常
	 */
	public static Balance retrieveBalance() throws StripeException {
		return StripeApi.balance().retrieve();
	}

	/* ==================== Application Fees 平台费用 ==================== */

	/**
	 * 查询平台费用(平台模式每笔交易产生的 fee 记录)
	 *
	 * @param id 平台费用 ID(fee_xxx)
	 * @return {@link ApplicationFee}
	 * @throws StripeException Stripe 异常
	 */
	public static ApplicationFee retrieveApplicationFee(String id) throws StripeException {
		return StripeApi.applicationFees().retrieve(id);
	}

	/**
	 * 查询平台费用(可展开字段)
	 *
	 * @param id     平台费用 ID(fee_xxx)
	 * @param params {@link ApplicationFeeRetrieveParams} 查询参数
	 * @return {@link ApplicationFee}
	 * @throws StripeException Stripe 异常
	 */
	public static ApplicationFee retrieveApplicationFee(String id, ApplicationFeeRetrieveParams params) throws StripeException {
		return StripeApi.applicationFees().retrieve(id, params);
	}

	/**
	 * 列表查询平台费用
	 *
	 * @param params {@link ApplicationFeeListParams} 列表参数
	 * @return {@link StripeCollection}<{@link ApplicationFee}>
	 * @throws StripeException Stripe 异常
	 */
	public static StripeCollection<ApplicationFee> listApplicationFees(ApplicationFeeListParams params) throws StripeException {
		return StripeApi.applicationFees().list(params);
	}
}
