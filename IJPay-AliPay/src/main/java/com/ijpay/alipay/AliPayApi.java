package com.ijpay.alipay;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p>支付宝支付相关接口</p>
 *
 * @author Javen
 */
public class AliPayApi {

    /**
     * 支付宝提供给商户的服务接入网关URL(新)
     */
    private static final String GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";

    /**
     * APP支付
     *
     * @param model     {@link AlipayTradeAppPayModel}
     * @param notifyUrl 异步通知 URL
     * @return {@link AlipayTradeAppPayResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeAppPayResponse appPayToResponse(AlipayTradeAppPayModel model, String notifyUrl) throws AlipayApiException {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().sdkExecute(request);
    }

    /**
     * WAP支付
     *
     * @param response  {@link HttpServletResponse}
     * @param model     {@link AlipayTradeWapPayModel}
     * @param returnUrl 异步通知URL
     * @param notifyUrl 同步通知URL
     * @throws AlipayApiException 支付宝 Api 异常
     * @throws IOException        IO 异常
     */
    public static void wapPay(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
        String form = wapPayStr(model, returnUrl, notifyUrl);
        response.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
        PrintWriter out = response.getWriter();
        out.write(form);
        out.flush();
        out.close();
    }

    /**
     * <p>WAP支付</p>
     *
     * <p>为了解决 Filter 中使用 OutputStream getOutputStream() 和 PrintWriter getWriter() 冲突异常问题</p>
     *
     * @param response  {@link HttpServletResponse}
     * @param model     {@link AlipayTradeWapPayModel}
     * @param returnUrl 异步通知URL
     * @param notifyUrl 同步通知URL
     * @throws AlipayApiException 支付宝 Api 异常
     * @throws IOException        IO 异常
     */
    public static void wapPayByOutputStream(HttpServletResponse response, AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
        String form = wapPayStr(model, returnUrl, notifyUrl);
        response.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
        OutputStream out = response.getOutputStream();
        out.write(form.getBytes(AliPayApiConfigKit.getAliPayApiConfig().getCharset()));
        response.getOutputStream().flush();
    }


    /**
     * WAP支付
     *
     * @param model     {@link AlipayTradeWapPayModel}
     * @param returnUrl 异步通知URL
     * @param notifyUrl 同步通知URL
     * @return {String}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static String wapPayStr(AlipayTradeWapPayModel model, String returnUrl, String notifyUrl) throws AlipayApiException {
        AlipayTradeWapPayRequest aliPayRequest = new AlipayTradeWapPayRequest();
        aliPayRequest.setReturnUrl(returnUrl);
        aliPayRequest.setNotifyUrl(notifyUrl);
        aliPayRequest.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().pageExecute(aliPayRequest).getBody();
    }

    /**
     * 统一收单交易支付接口接口 <br>
     * 适用于:条形码支付、声波支付等 <br>
     *
     * @param model     {@link AlipayTradePayModel}
     * @param notifyUrl 异步通知URL
     * @return {@link AlipayTradePayResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradePayResponse tradePayToResponse(AlipayTradePayModel model, String notifyUrl) throws AlipayApiException {
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        // 填充业务参数
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 统一收单交易支付接口接口 <br>
     * 适用于:条形码支付、声波支付等 <br>
     *
     * @param model        {AlipayTradePayModel}
     * @param notifyUrl    异步通知URL
     * @param appAuthToken 应用授权token
     * @return {AlipayTradePayResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradePayResponse tradePayToResponse(AlipayTradePayModel model, String notifyUrl, String appAuthToken) throws AlipayApiException {
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, null, appAuthToken);
    }

    /**
     * 统一收单线下交易预创建 <br>
     * 适用于：扫码支付等 <br>
     *
     * @param model     {@link AlipayTradePrecreateModel}
     * @param notifyUrl 异步通知URL
     * @return {@link AlipayTradePrecreateResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayTradePrecreateModel model, String notifyUrl) throws AlipayApiException {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 统一收单线下交易预创建 <br>
     * 适用于：扫码支付等 <br>
     *
     * @param model        {@link AlipayTradePrecreateModel}
     * @param notifyUrl    异步通知URL
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradePrecreateResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradePrecreateResponse tradePrecreatePayToResponse(AlipayTradePrecreateModel model,
                                                                           String notifyUrl, String appAuthToken) throws AlipayApiException {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, null, appAuthToken);
    }

    /**
     * 单笔转账到支付宝账户
     *
     * @param model {@link AlipayFundTransToaccountTransferModel}
     * @return 转账是否成功
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static boolean transfer(AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
        AlipayFundTransToaccountTransferResponse response = transferToResponse(model);
        String result = response.getBody();
        if (response.isSuccess()) {
            return true;
        } else {
            // 调用查询接口查询数据
            JSONObject jsonObject = JSONObject.parseObject(result);
            String outBizNo = jsonObject.getJSONObject("alipay_fund_trans_toaccount_transfer_response").getString("out_biz_no");
            AlipayFundTransOrderQueryModel queryModel = new AlipayFundTransOrderQueryModel();
            model.setOutBizNo(outBizNo);
            return transferQuery(queryModel);
        }
    }

    /**
     * 单笔转账到支付宝账户
     *
     * @param model {@link AlipayFundTransToaccountTransferModel}
     * @return {@link AlipayFundTransToaccountTransferResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayFundTransToaccountTransferResponse transferToResponse(AlipayFundTransToaccountTransferModel model) throws AlipayApiException {
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 转账查询接口
     *
     * @param model {@link AlipayFundTransOrderQueryModel}
     * @return 是否存在此
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static boolean transferQuery(AlipayFundTransOrderQueryModel model) throws AlipayApiException {
        AlipayFundTransOrderQueryResponse response = transferQueryToResponse(model);
        return response.isSuccess();
    }

    /**
     * 转账查询接口
     *
     * @param model {@link AlipayFundTransOrderQueryModel}
     * @return {@link AlipayFundTransOrderQueryResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayFundTransOrderQueryResponse transferQueryToResponse(AlipayFundTransOrderQueryModel model) throws AlipayApiException {
        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 统一收单线下交易查询接口
     *
     * @param model {@link AlipayTradeQueryModel}
     * @return {@link AlipayTradeQueryResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeQueryResponse tradeQueryToResponse(AlipayTradeQueryModel model) throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 统一收单线下交易查询接口
     *
     * @param model        {@link AlipayTradeQueryModel}
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradeQueryResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeQueryResponse tradeQueryToResponse(AlipayTradeQueryModel model, String appAuthToken) throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, null, appAuthToken);
    }

    /**
     * 统一收单交易撤销接口
     *
     * @param model        {@link AlipayTradeCancelModel}
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradeCancelResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeCancelResponse tradeCancelToResponse(AlipayTradeCancelModel model, String appAuthToken) throws AlipayApiException {
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, null, appAuthToken);
    }

    /**
     * 统一收单交易撤销接口
     *
     * @param model {@link AlipayTradeCancelModel}
     * @return {@link AlipayTradeCancelResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeCancelResponse tradeCancelToResponse(AlipayTradeCancelModel model)
            throws AlipayApiException {
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 统一收单交易关闭接口
     *
     * @param model        {@link AlipayTradeCloseModel}
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradeCloseResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeCloseResponse tradeCloseToResponse(AlipayTradeCloseModel model, String appAuthToken) throws AlipayApiException {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, null, appAuthToken);

    }

    /**
     * 统一收单交易关闭接口
     *
     * @param model {@link AlipayTradeCloseModel}
     * @return {@link AlipayTradeCloseResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeCloseResponse tradeCloseToResponse(AlipayTradeCloseModel model) throws AlipayApiException {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 统一收单交易创建接口
     *
     * @param model     {@link AlipayTradeCreateModel}
     * @param notifyUrl 异步通知URL
     * @return {@link AlipayTradeCreateResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeCreateResponse tradeCreateToResponse(AlipayTradeCreateModel model, String notifyUrl) throws AlipayApiException {
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 统一收单交易创建接口
     *
     * @param model        {@link AlipayTradeCreateModel}
     * @param notifyUrl    异步通知URL
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradeCreateResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeCreateResponse tradeCreateToResponse(AlipayTradeCreateModel model, String notifyUrl, String appAuthToken) throws AlipayApiException {
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, null, appAuthToken);
    }

    /**
     * 统一收单交易退款接口
     *
     * @param model {@link AlipayTradeRefundModel}
     * @return {@link AlipayTradeRefundResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayTradeRefundModel model) throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 统一收单交易退款接口
     *
     * @param model        {@link AlipayTradeRefundModel}
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradeRefundResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeRefundResponse tradeRefundToResponse(AlipayTradeRefundModel model, String appAuthToken) throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, null, appAuthToken);
    }

    /**
     * 统一收单交易退款查询
     *
     * @param model {@link AlipayTradeFastpayRefundQueryModel}
     * @return {@link AlipayTradeFastpayRefundQueryResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeFastpayRefundQueryResponse tradeRefundQueryToResponse(AlipayTradeFastpayRefundQueryModel model) throws AlipayApiException {
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 统一收单交易退款查询
     *
     * @param model        {@link AlipayTradeFastpayRefundQueryModel}
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradeFastpayRefundQueryResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeFastpayRefundQueryResponse tradeRefundQueryToResponse(AlipayTradeFastpayRefundQueryModel model, String appAuthToken) throws AlipayApiException {
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, null, appAuthToken);
    }

    /**
     * 查询对账单下载地址
     *
     * @param model {@link AlipayDataDataserviceBillDownloadurlQueryModel}
     * @return 对账单下载地址
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static String billDownloadurlQuery(AlipayDataDataserviceBillDownloadurlQueryModel model) throws AlipayApiException {
        AlipayDataDataserviceBillDownloadurlQueryResponse response = billDownloadUrlQueryToResponse(model);
        return response.getBillDownloadUrl();
    }

    /**
     * 查询对账单下载地址
     *
     * @param model {@link AlipayDataDataserviceBillDownloadurlQueryModel}
     * @return {@link AlipayDataDataserviceBillDownloadurlQueryResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayDataDataserviceBillDownloadurlQueryResponse billDownloadUrlQueryToResponse(AlipayDataDataserviceBillDownloadurlQueryModel model) throws AlipayApiException {
        AlipayDataDataserviceBillDownloadurlQueryRequest request = new AlipayDataDataserviceBillDownloadurlQueryRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 统一收单交易结算接口
     *
     * @param model        {@link AlipayTradeOrderSettleModel}
     * @param appAuthToken 应用授权token
     * @return {@link AlipayTradeOrderSettleResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeOrderSettleResponse tradeOrderSettleToResponse(AlipayTradeOrderSettleModel model, String appAuthToken) throws AlipayApiException {
        AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request, null, appAuthToken);
    }

    /**
     * 统一收单交易结算接口
     *
     * @param model {@link AlipayTradeOrderSettleModel}
     * @return {@link AlipayTradeOrderSettleResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayTradeOrderSettleResponse tradeOrderSettleToResponse(AlipayTradeOrderSettleModel model) throws AlipayApiException {
        AlipayTradeOrderSettleRequest request = new AlipayTradeOrderSettleRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 电脑网站支付(PC支付)
     *
     * @param response  {@link HttpServletResponse}
     * @param model     {@link AlipayTradePagePayModel}
     * @param notifyUrl 异步通知URL
     * @param returnUrl 同步通知URL
     * @throws AlipayApiException 支付宝 Api 异常
     * @throws IOException        IO 异常
     */
    public static void tradePage(HttpServletResponse response, AlipayTradePagePayModel model, String notifyUrl, String returnUrl) throws AlipayApiException, IOException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);
        String form = AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().pageExecute(request).getBody();// 调用SDK生成表单
        response.setContentType("text/html;charset=" + AliPayApiConfigKit.getAliPayApiConfig().getCharset());
        PrintWriter out = response.getWriter();
        out.write(form);
        out.flush();
        out.close();
    }

    /**
     * 资金预授权冻结接口
     *
     * @param model {@link AlipayFundAuthOrderFreezeModel}
     * @return {@link AlipayFundAuthOrderFreezeResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayFundAuthOrderFreezeResponse authOrderFreezeToResponse(AlipayFundAuthOrderFreezeModel model) throws AlipayApiException {
        AlipayFundAuthOrderFreezeRequest request = new AlipayFundAuthOrderFreezeRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 资金授权解冻接口
     *
     * @param model {@link AlipayFundAuthOrderUnfreezeModel}
     * @return {@link AlipayFundAuthOrderUnfreezeResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayFundAuthOrderUnfreezeResponse authOrderUnfreezeToResponse(AlipayFundAuthOrderUnfreezeModel model) throws AlipayApiException {
        AlipayFundAuthOrderUnfreezeRequest request = new AlipayFundAuthOrderUnfreezeRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 资金预授权冻结接口
     *
     * @param model {@link AlipayFundAuthOrderVoucherCreateModel}
     * @return {@link AlipayFundAuthOrderVoucherCreateResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayFundAuthOrderVoucherCreateResponse authOrderVoucherCreateToResponse(AlipayFundAuthOrderVoucherCreateModel model) throws AlipayApiException {
        AlipayFundAuthOrderVoucherCreateRequest request = new AlipayFundAuthOrderVoucherCreateRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 资金授权撤销接口
     *
     * @param model {@link AlipayFundAuthOperationCancelModel}
     * @return {@link AlipayFundAuthOperationCancelResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayFundAuthOperationCancelResponse authOperationCancelToResponse(AlipayFundAuthOperationCancelModel model) throws AlipayApiException {
        AlipayFundAuthOperationCancelRequest request = new AlipayFundAuthOperationCancelRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 资金授权操作查询接口
     *
     * @param model {@link AlipayFundAuthOperationDetailQueryModel}
     * @return {@link AlipayFundAuthOperationDetailQueryResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayFundAuthOperationDetailQueryResponse authOperationDetailQueryToResponse(AlipayFundAuthOperationDetailQueryModel model) throws AlipayApiException {
        AlipayFundAuthOperationDetailQueryRequest request = new AlipayFundAuthOperationDetailQueryRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 红包无线支付接口
     *
     * @param model {@link AlipayFundCouponOrderAppPayModel}
     * @return {@link AlipayFundCouponOrderAppPayResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayFundCouponOrderAppPayResponse fundCouponOrderAppPayToResponse(AlipayFundCouponOrderAppPayModel model) throws AlipayApiException {
        AlipayFundCouponOrderAppPayRequest request = new AlipayFundCouponOrderAppPayRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 红包页面支付接口
     *
     * @param model {@link AlipayFundCouponOrderPagePayModel}
     * @return {@link AlipayFundCouponOrderPagePayResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayFundCouponOrderPagePayResponse fundCouponOrderPagePayToResponse(AlipayFundCouponOrderPagePayModel model) throws AlipayApiException {
        AlipayFundCouponOrderPagePayRequest request = new AlipayFundCouponOrderPagePayRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 红包协议支付接口
     *
     * @param model {@link AlipayFundCouponOrderAgreementPayModel}
     * @return {@link AlipayFundCouponOrderAgreementPayResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayFundCouponOrderAgreementPayResponse fundCouponOrderAgreementPayToResponse(AlipayFundCouponOrderAgreementPayModel model) throws AlipayApiException {
        AlipayFundCouponOrderAgreementPayRequest request = new AlipayFundCouponOrderAgreementPayRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 红包打款接口
     *
     * @param model {@link AlipayFundCouponOrderDisburseModel}
     * @return {@link AlipayFundCouponOrderDisburseResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayFundCouponOrderDisburseResponse fundCouponOrderDisburseToResponse(AlipayFundCouponOrderDisburseModel model) throws AlipayApiException {
        AlipayFundCouponOrderDisburseRequest request = new AlipayFundCouponOrderDisburseRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 红包退回接口
     *
     * @param model {@link AlipayFundCouponOrderRefundModel}
     * @return {@link AlipayFundCouponOrderRefundResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayFundCouponOrderRefundResponse fundCouponOrderRefundToResponse(AlipayFundCouponOrderRefundModel model) throws AlipayApiException {
        AlipayFundCouponOrderRefundRequest request = new AlipayFundCouponOrderRefundRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 红包退回接口
     *
     * @param model {@link AlipayFundCouponOperationQueryModel}
     * @return {@link AlipayFundCouponOperationQueryResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayFundCouponOperationQueryResponse fundCouponOperationQueryToResponse(AlipayFundCouponOperationQueryModel model) throws AlipayApiException {
        AlipayFundCouponOperationQueryRequest request = new AlipayFundCouponOperationQueryRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 应用授权 URL 拼装
     *
     * @param appId       应用编号
     * @param redirectUri 回调 URI
     * @return 应用授权 URL
     * @throws UnsupportedEncodingException 编码异常
     */
    public static String getOauth2Url(String appId, String redirectUri) throws UnsupportedEncodingException {
        return new StringBuffer().append("https://openauth.alipay.com/oauth2/appToAppAuth.htm?app_id=").append(appId).append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8")).toString();
    }

    /**
     * 使用 app_auth_code 换取 app_auth_token
     *
     * @param model {@link AlipayOpenAuthTokenAppModel}
     * @return {@link AlipayOpenAuthTokenAppResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayOpenAuthTokenAppResponse openAuthTokenAppToResponse(AlipayOpenAuthTokenAppModel model) throws AlipayApiException {
        AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 查询授权信息
     *
     * @param model {@link AlipayOpenAuthTokenAppQueryModel}
     * @return {@link AlipayOpenAuthTokenAppQueryResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayOpenAuthTokenAppQueryResponse openAuthTokenAppQueryToResponse(AlipayOpenAuthTokenAppQueryModel model) throws AlipayApiException {
        AlipayOpenAuthTokenAppQueryRequest request = new AlipayOpenAuthTokenAppQueryRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 地铁购票发码
     *
     * @param model {@link AlipayCommerceCityfacilitatorVoucherGenerateModel}
     * @return {@link AlipayCommerceCityfacilitatorVoucherGenerateResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayCommerceCityfacilitatorVoucherGenerateResponse voucherGenerateToResponse(AlipayCommerceCityfacilitatorVoucherGenerateModel model) throws AlipayApiException {
        AlipayCommerceCityfacilitatorVoucherGenerateRequest request = new AlipayCommerceCityfacilitatorVoucherGenerateRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 地铁购票发码退款
     *
     * @param model {@link AlipayCommerceCityfacilitatorVoucherRefundModel}
     * @return {@link AlipayCommerceCityfacilitatorVoucherRefundResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayCommerceCityfacilitatorVoucherRefundResponse metroRefundToResponse(AlipayCommerceCityfacilitatorVoucherRefundModel model) throws AlipayApiException {
        AlipayCommerceCityfacilitatorVoucherRefundRequest request = new AlipayCommerceCityfacilitatorVoucherRefundRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 地铁车站数据查询
     *
     * @param model {@link AlipayCommerceCityfacilitatorStationQueryModel}
     * @return {@link AlipayCommerceCityfacilitatorStationQueryResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayCommerceCityfacilitatorStationQueryResponse stationQueryToResponse(AlipayCommerceCityfacilitatorStationQueryModel model) throws AlipayApiException {
        AlipayCommerceCityfacilitatorStationQueryRequest request = new AlipayCommerceCityfacilitatorStationQueryRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    /**
     * 核销码批量查询
     *
     * @param model {@link AlipayCommerceCityfacilitatorVoucherBatchqueryModel}
     * @return {@link AlipayCommerceCityfacilitatorVoucherBatchqueryResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayCommerceCityfacilitatorVoucherBatchqueryResponse voucherBatchqueryToResponse(AlipayCommerceCityfacilitatorVoucherBatchqueryModel model) throws AlipayApiException {
        AlipayCommerceCityfacilitatorVoucherBatchqueryRequest request = new AlipayCommerceCityfacilitatorVoucherBatchqueryRequest();
        request.setBizModel(model);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

    public static void batchTrans(Map<String, String> params, String privateKey, String signType, HttpServletResponse response) throws IOException {
        params.put("service", "batch_trans_notify");
        params.put("_input_charset", "UTF-8");
        params.put("pay_date", DateUtil.format(new Date(), "YYYYMMDD"));
        Map<String, String> param = AliPayCore.buildRequestPara(params, privateKey, signType);
        response.sendRedirect(GATEWAY_NEW.concat(AliPayCore.createLinkString(param)));
    }

    /**
     * 将异步通知的参数转化为Map
     *
     * @param request {HttpServletRequest}
     * @return 转化后的Map
     */
    public static Map<String, String> toMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 生活缴费查询账单
     *
     * @param orderType       支付宝订单类型
     * @param merchantOrderNo 业务流水号
     * @return {@link AlipayEbppBillGetResponse}
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public static AlipayEbppBillGetResponse ebppBillGet(String orderType, String merchantOrderNo) throws AlipayApiException {
        AlipayEbppBillGetRequest request = new AlipayEbppBillGetRequest();
        request.setOrderType(orderType);
        request.setMerchantOrderNo(merchantOrderNo);
        return AliPayApiConfigKit.getAliPayApiConfig().getAliPayClient().execute(request);
    }

}