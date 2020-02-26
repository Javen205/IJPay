package com.ijpay.wxpay;

import com.ijpay.core.enums.PayModel;
import com.ijpay.core.enums.RequestMethod;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.RsaKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.enums.WxApiType;
import com.ijpay.wxpay.enums.WxDomain;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>微信支付相关接口</p>
 *
 * @author Javen
 */
public class WxPayApi {

    private WxPayApi() {
    }

    /**
     * 获取接口请求的 URL
     *
     * @param wxApiType {@link WxApiType} 支付 API 接口枚举
     * @return {@link String} 返回完整的接口请求URL
     */
    public static String getReqUrl(WxApiType wxApiType) {
        return getReqUrl(wxApiType, null, false);
    }

    /**
     * 获取接口请求的 URL
     *
     * @param wxApiType {@link WxApiType} 支付 API 接口枚举
     * @param isSandBox 是否是沙箱环境
     * @return {@link String} 返回完整的接口请求URL
     */
    public static String getReqUrl(WxApiType wxApiType, boolean isSandBox) {
        return getReqUrl(wxApiType, null, isSandBox);
    }

    /**
     * 获取接口请求的 URL
     *
     * @param wxApiType {@link WxApiType} 支付 API 接口枚举
     * @param wxDomain  {@link WxDomain} 支付 API 接口域名枚举
     * @param isSandBox 是否是沙箱环境
     * @return {@link String} 返回完整的接口请求URL
     */
    public static String getReqUrl(WxApiType wxApiType, WxDomain wxDomain, boolean isSandBox) {
        if (wxDomain == null) {
            wxDomain = WxDomain.CHINA;
        }
        return wxDomain.getType()
                .concat(isSandBox ? WxApiType.SAND_BOX_NEW.getType() : "")
                .concat(wxApiType.getType());
    }

    /**
     * 发起请求
     *
     * @param apiUrl 接口 URL
     *               通过 {@link WxPayApi#getReqUrl(WxApiType)}
     *               或者 {@link WxPayApi#getReqUrl(WxApiType, WxDomain, boolean)} 来获取
     * @param params 接口请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String execution(String apiUrl, Map<String, String> params) {
        return doPost(apiUrl, params);
    }

    /**
     * 发起请求
     *
     * @param apiUrl 接口 URL
     *               通过 {@link WxPayApi#getReqUrl(WxApiType)}
     *               或者 {@link WxPayApi#getReqUrl(WxApiType, WxDomain, boolean)} 来获取
     * @param params 接口请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String executionByGet(String apiUrl, Map<String, Object> params) {
        return doGet(apiUrl, params);
    }

    /**
     * 发起请求
     *
     * @param apiUrl   接口 URL
     *                 通过 {@link WxPayApi#getReqUrl(WxApiType)}
     *                 或者 {@link WxPayApi#getReqUrl(WxApiType, WxDomain, boolean)} 来获取
     * @param params   接口请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String execution(String apiUrl, Map<String, String> params, String certPath, String certPass) {
        return doPostSSL(apiUrl, params, certPath, certPass);
    }

    /**
     * 发起请求
     *
     * @param apiUrl   接口 URL
     *                 通过 {@link WxPayApi#getReqUrl(WxApiType)}
     *                 或者 {@link WxPayApi#getReqUrl(WxApiType, WxDomain, boolean)} 来获取
     * @param params   接口请求参数
     * @param certFile 证书文件输入流
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String execution(String apiUrl, Map<String, String> params, InputStream certFile, String certPass) {
        return doPostSSL(apiUrl, params, certFile, certPass);
    }

    /**
     * V3 接口统一执行入口
     *
     * @param method    {@link RequestMethod} 请求方法
     * @param urlPrefix 可通过 {@link WxDomain}来获取
     * @param urlSuffix 可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId     商户Id
     * @param serialNo  商户 API 证书序列号
     * @param keyPath   apiclient_key.pem 证书路径
     * @param body      接口请求参数
     * @param nonceStr  随机字符库
     * @param timestamp 时间戳
     * @param authType  认证类型
     * @param file      文件
     * @return {@link String} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public static String v3Execution(RequestMethod method, String urlPrefix, String urlSuffix, String mchId, String serialNo, String keyPath, String body, String nonceStr, long timestamp, String authType, File file) throws Exception {
        // 构建签名参数
        String buildSignMessage = PayKit.buildSignMessage(method, urlSuffix, timestamp, nonceStr, body);
        // 获取商户私钥
        String key = PayKit.getPrivateKey(keyPath);
        // 生成签名
        String signature = RsaKit.encryptByPrivateKey(buildSignMessage, key);
        // 根据平台规则生成请求头 authorization
        String authorization = PayKit.getAuthorization(mchId, serialNo, nonceStr, String.valueOf(timestamp), signature, authType);

        if (method == RequestMethod.GET) {
            return doGet(urlPrefix.concat(urlSuffix), authorization, null);
        } else if (method == RequestMethod.POST) {
            return doPost(urlPrefix.concat(urlSuffix), authorization, body);
        } else if (method == RequestMethod.UPLOAD) {
            return doUpload(urlPrefix.concat(urlSuffix), authorization, body, file);
        }
        return null;
    }

    /**
     * V3 接口统一执行入口
     *
     * @param method    {@link RequestMethod} 请求方法
     * @param urlPrefix 可通过 {@link WxDomain}来获取
     * @param urlSuffix 可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId     商户Id
     * @param serialNo  商户 API 证书序列号
     * @param keyPath   apiclient_key.pem 证书路径
     * @param body      接口请求参数
     * @return {@link String} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public static String v3Execution(RequestMethod method, String urlPrefix, String urlSuffix, String mchId, String serialNo, String keyPath, String body) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String authType = "WECHATPAY2-SHA256-RSA2048";
        String nonceStr = PayKit.generateStr();
        return v3Execution(method, urlPrefix, urlSuffix, mchId, serialNo, keyPath, body, nonceStr, timestamp, authType, null);
    }

    /**
     * V3 接口统一执行入口
     *
     * @param urlPrefix 可通过 {@link WxDomain}来获取
     * @param urlSuffix 可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId     商户Id
     * @param serialNo  商户 API 证书序列号
     * @param keyPath   apiclient_key.pem 证书路径
     * @param body      接口请求参数
     * @param file      文件
     * @return {@link String} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public static String v3Upload(String urlPrefix, String urlSuffix, String mchId, String serialNo, String keyPath, String body, File file) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String authType = "WECHATPAY2-SHA256-RSA2048";
        String nonceStr = PayKit.generateStr();
        return v3Execution(RequestMethod.UPLOAD, urlPrefix, urlSuffix, mchId, serialNo, keyPath, body, nonceStr, timestamp, authType, file);
    }

    /**
     * V3 接口统一执行入口
     *
     * @param method    {@link RequestMethod} 请求方法
     * @param urlPrefix 可通过 {@link WxDomain}来获取
     * @param urlSuffix 可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId     商户Id
     * @param serialNo  商户 API 证书序列号
     * @param keyPath   apiclient_key.pem 证书路径
     * @param body      接口请求参数
     * @param params    Get 接口请求参数
     * @return {@link String} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public static String v3Execution(RequestMethod method, String urlPrefix, String urlSuffix, String mchId, String serialNo, String keyPath, String body, Map<String, String> params) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String authType = "WECHATPAY2-SHA256-RSA2048";
        String nonceStr = PayKit.generateStr();
        urlSuffix = urlSuffix.concat("?").concat(PayKit.createLinkString(params, true));
        return v3Execution(method, urlPrefix, urlSuffix, mchId, serialNo, keyPath, body, nonceStr, timestamp, authType, null);
    }

    /**
     * 获取验签秘钥API
     *
     * @param mch_id     商户号
     * @param partnerKey API 密钥
     * @param signType   签名方式
     * @return {@link String} 请求返回的结果
     */
    public static String getSignKey(String mch_id, String partnerKey, SignType signType) {
        Map<String, String> map = new HashMap<String, String>(3);
        String nonce_str = WxPayKit.generateStr();
        map.put("mch_id", mch_id);
        map.put("nonce_str", nonce_str);
        map.put("sign", WxPayKit.createSign(map, partnerKey, signType));
        return execution(getReqUrl(WxApiType.GET_SIGN_KEY), map);
    }

    /**
     * 统一下单
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String pushOrder(Map<String, String> params) {
        return pushOrder(false, null, params);
    }

    /**
     * 统一下单
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String pushOrder(boolean isSandbox, Map<String, String> params) {
        return pushOrder(isSandbox, null, params);
    }

    /**
     * 统一下单
     *
     * @param isSandbox 是否是沙盒环境
     * @param wxDomain  {@link WxDomain} 支付 API 接口域名枚举
     * @param params    请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String pushOrder(boolean isSandbox, WxDomain wxDomain, Map<String, String> params) {
        return execution(getReqUrl(WxApiType.UNIFIED_ORDER, wxDomain, isSandbox), params);
    }

    /**
     * 订单查询
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String orderQuery(Map<String, String> params) {
        return orderQuery(false, null, params);
    }

    /**
     * 订单查询
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String orderQuery(boolean isSandbox, Map<String, String> params) {
        return execution(getReqUrl(WxApiType.ORDER_QUERY, null, isSandbox), params);
    }

    /**
     * 订单查询
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String orderQuery(boolean isSandbox, WxDomain wxDomain, Map<String, String> params) {
        return execution(getReqUrl(WxApiType.ORDER_QUERY, wxDomain, isSandbox), params);
    }

    /**
     * 关闭订单
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String closeOrder(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.CLOSE_ORDER), params);
    }

    /**
     * 撤销订单
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String orderReverse(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.REVERSE), params, certPath, certPass);
    }

    /**
     * 撤销订单
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String orderReverse(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.REVERSE), params, certFile, certPass);
    }

    /**
     * 申请退款
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @param certPath  证书文件目录
     * @param certPass  证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String orderRefund(boolean isSandbox, Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.REFUND, null, isSandbox), params, certPath, certPass);
    }

    /**
     * 申请退款
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @param certFile  证书文件的 InputStream
     * @param certPass  证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String orderRefund(boolean isSandbox, Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.REFUND, null, isSandbox), params, certFile, certPass);
    }

    /**
     * 查询退款
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String orderRefundQuery(boolean isSandbox, Map<String, String> params) {
        return execution(getReqUrl(WxApiType.REFUND_QUERY, null, isSandbox), params);
    }

    /**
     * 下载对账单
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String downloadBill(boolean isSandbox, Map<String, String> params) {
        return execution(getReqUrl(WxApiType.DOWNLOAD_BILL, null, isSandbox), params);
    }

    /**
     * 交易保障
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String orderReport(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.REPORT, null, false), params);
    }

    /**
     * 转换短链接
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String toShortUrl(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.SHORT_URL, null, false), params);
    }

    /**
     * 授权码查询 openId
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String authCodeToOpenid(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.AUTH_CODE_TO_OPENID, null, false), params);
    }

    /**
     * 刷卡支付
     *
     * @param isSandbox 是否是沙盒环境
     * @param params    请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String microPay(boolean isSandbox, Map<String, String> params) {
        return execution(getReqUrl(WxApiType.MICRO_PAY, null, isSandbox), params);
    }

    /**
     * 企业付款到零钱
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String transfers(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.TRANSFER, null, false), params, certPath, certPass);
    }

    /**
     * 企业付款到零钱
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String transfers(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.TRANSFER, null, false), params, certFile, certPass);
    }

    /**
     * 查询企业付款到零钱
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String getTransferInfo(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.GET_TRANSFER_INFO, null, false), params, certPath, certPass);
    }

    /**
     * 查询企业付款到零钱
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String getTransferInfo(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.GET_TRANSFER_INFO, null, false), params, certFile, certPass);
    }

    /**
     * 企业付款到银行
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String payBank(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.TRANSFER_BANK, null, false), params, certPath, certPass);
    }

    /**
     * 企业付款到银行
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String payBank(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.TRANSFER_BANK, null, false), params, certFile, certPass);
    }

    /**
     * 查询企业付款到银行
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String queryBank(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.GET_TRANSFER_BANK_INFO, null, false), params, certPath, certPass);
    }

    /**
     * 查询企业付款到银行
     *
     * @param params   请求参数
     * @param certFile 证书文件的  InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String queryBank(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.GET_TRANSFER_BANK_INFO, null, false), params, certFile, certPass);
    }

    /**
     * 获取 RSA 加密公钥
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String getPublicKey(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.GET_PUBLIC_KEY, null, false), params, certPath, certPass);
    }

    /**
     * 获取 RSA 加密公钥
     *
     * @param params   请求参数
     * @param certFile 证书文件的   InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String getPublicKey(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.GET_PUBLIC_KEY, null, false), params, certFile, certPass);
    }

    /**
     * 公众号纯签约
     *
     * @param params   请求参数
     * @param payModel 商户平台模式
     * @return {@link String} 请求返回的结果
     */
    public static String entrustWeb(Map<String, Object> params, PayModel payModel) {
        if (payModel == PayModel.BUSINESS_MODEL) {
            return executionByGet(getReqUrl(WxApiType.ENTRUST_WEB), params);
        } else {
            return executionByGet(getReqUrl(WxApiType.PARTNER_ENTRUST_WEB), params);
        }
    }


    /**
     * APP 纯签约
     *
     * @param params   请求参数
     * @param payModel 商户平台模式
     * @return {@link String} 请求返回的结果
     */
    public static String preEntrustWeb(Map<String, Object> params, PayModel payModel) {
        if (payModel == PayModel.BUSINESS_MODEL) {
            return executionByGet(getReqUrl(WxApiType.PRE_ENTRUST_WEB), params);
        } else {
            return executionByGet(getReqUrl(WxApiType.PARTNER_PRE_ENTRUST_WEB), params);
        }
    }


    /**
     * H5 纯签约
     *
     * @param params   请求参数
     * @param payModel 商户平台模式
     * @return {@link String} 请求返回的结果
     */
    public static String h5EntrustWeb(Map<String, Object> params, PayModel payModel) {
        if (payModel == PayModel.BUSINESS_MODEL) {
            return executionByGet(getReqUrl(WxApiType.H5_ENTRUST_WEB), params);
        } else {
            return executionByGet(getReqUrl(WxApiType.PARTNER_H5_ENTRUST_WEB), params);
        }
    }

    /**
     * 支付中签约
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String contractOrder(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.PAY_CONTRACT_ORDER), params);
    }

    /**
     * 查询签约关系
     *
     * @param params   请求参数
     * @param payModel 商户平台模式
     * @return {@link String} 请求返回的结果
     */
    public static String queryContract(Map<String, String> params, PayModel payModel) {
        if (payModel == PayModel.BUSINESS_MODEL) {
            return execution(getReqUrl(WxApiType.QUERY_ENTRUST_CONTRACT), params);
        } else {
            return execution(getReqUrl(WxApiType.PARTNER_QUERY_ENTRUST_CONTRACT), params);
        }
    }

    /**
     * 申请扣款
     *
     * @param params   请求参数
     * @param payModel 商户平台模式
     * @return {@link String} 请求返回的结果
     */
    public static String papPayApply(Map<String, String> params, PayModel payModel) {
        if (payModel == PayModel.BUSINESS_MODEL) {
            return execution(getReqUrl(WxApiType.PAP_PAY_APPLY), params);
        } else {
            return execution(getReqUrl(WxApiType.PARTNER_PAP_PAY_APPLY), params);
        }
    }

    /**
     * 申请解约
     *
     * @param params   请求参数
     * @param payModel 商户平台模式
     * @return {@link String} 请求返回的结果
     */
    public static String deleteContract(Map<String, String> params, PayModel payModel) {
        if (payModel == PayModel.BUSINESS_MODEL) {
            return execution(getReqUrl(WxApiType.DELETE_ENTRUST_CONTRACT), params);
        } else {
            return execution(getReqUrl(WxApiType.PARTNER_DELETE_ENTRUST_CONTRACT), params);
        }
    }

    /**
     * 查询签约关系对账单
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String contractBill(Map<String, String> params, PayModel payModel) {
        if (payModel == PayModel.BUSINESS_MODEL) {
            return execution(getReqUrl(WxApiType.QUERY_ENTRUST_CONTRACT), params);
        } else {
            return execution(getReqUrl(WxApiType.PARTNER_QUERY_ENTRUST_CONTRACT), params);
        }
    }

    /**
     * 查询代扣订单
     *
     * @param params   请求参数
     * @param payModel 商户平台模式
     * @return {@link String} 请求返回的结果
     */
    public static String papOrderQuery(Map<String, String> params, PayModel payModel) {
        if (payModel == PayModel.BUSINESS_MODEL) {
            return execution(getReqUrl(WxApiType.PAP_ORDER_QUERY), params);
        } else {
            return execution(getReqUrl(WxApiType.PARTNER_PAP_ORDER_QUERY), params);
        }
    }

    /**
     * 请求单次分账
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharing(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.PROFIT_SHARING), params, certPath, certPass);
    }

    /**
     * 请求单次分账
     *
     * @param params   请求参数
     * @param certFile 证书文件的  InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharing(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.PROFIT_SHARING), params, certFile, certPass);
    }

    /**
     * 请求多次分账
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String multiProfitSharing(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.MULTI_PROFIT_SHARING), params, certPath, certPass);
    }

    /**
     * 请求多次分账
     *
     * @param params   请求参数
     * @param certFile 证书文件的  InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String multiProfitSharing(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.MULTI_PROFIT_SHARING), params, certFile, certPass);
    }

    /**
     * 查询分账结果
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharingQuery(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.PROFIT_SHARING_QUERY), params);
    }

    /**
     * 添加分账接收方
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharingAddReceiver(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.PROFITS_HARING_ADD_RECEIVER), params);
    }

    /**
     * 删除分账接收方
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharingRemoveReceiver(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.PROFIT_SHARING_REMOVE_RECEIVER), params);
    }

    /**
     * 完结分账
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharingFinish(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.PROFIT_SHARING_FINISH), params, certPath, certPass);
    }

    /**
     * 完结分账
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharingFinish(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.PROFIT_SHARING_FINISH), params, certFile, certPass);
    }

    /**
     * 分账回退
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharingReturn(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.PROFIT_SHARING_RETURN), params, certPath, certPass);
    }

    /**
     * 分账回退
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharingReturn(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.PROFIT_SHARING_RETURN), params, certFile, certPass);
    }

    /**
     * 分账回退结果查询
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String profitSharingReturnQuery(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.PROFIT_SHARING_RETURN_QUERY), params);
    }

    /**
     * 发放代金券
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String sendCoupon(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.SEND_COUPON), params, certPath, certPass);
    }

    /**
     * 发放代金券
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String sendCoupon(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.SEND_COUPON), params, certFile, certPass);
    }

    /**
     * 查询代金券批次
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String queryCouponStock(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.QUERY_COUPON_STOCK), params);
    }

    /**
     * 查询代金券信息
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String queryCouponsInfo(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.QUERY_COUPONS_INFO), params);
    }

    /**
     * 拉取订单评价数据
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String batchQueryComment(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.BATCH_QUERY_COMMENT), params, certPath, certPass);
    }

    /**
     * 拉取订单评价数据
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String batchQueryComment(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.BATCH_QUERY_COMMENT), params, certFile, certPass);
    }

    /**
     * 支付押金（人脸支付）
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String depositFacePay(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.DEPOSIT_FACE_PAY), params);
    }

    /**
     * 支付押金（付款码支付）
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String depositMicroPay(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.DEPOSIT_MICRO_PAY), params);
    }

    /**
     * 查询订单
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String depositOrderQuery(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.DEPOSIT_ORDER_QUERY), params);
    }

    /**
     * 撤销订单
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String depositReverse(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.DEPOSIT_REVERSE), params, certPath, certPass);
    }

    /**
     * 撤销订单
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String depositReverse(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.DEPOSIT_REVERSE), params, certFile, certPass);
    }

    /**
     * 消费押金
     *
     * @param params   请求参数
     * @param certPath 证书文件的目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String depositConsume(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.DEPOSIT_CONSUME), params, certPath, certPass);
    }

    /**
     * 消费押金
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String depositConsume(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.DEPOSIT_CONSUME), params, certFile, certPass);
    }

    /**
     * 申请退款（押金）
     *
     * @param params   请求参数
     * @param certPath 证书文件的目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String depositRefund(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.DEPOSIT_REFUND), params, certPath, certPass);
    }

    /**
     * 申请退款（押金）
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String depositRefund(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.DEPOSIT_REFUND), params, certFile, certPass);
    }

    /**
     * 查询退款（押金）
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String depositRefundQuery(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.DEPOSIT_REFUND_QUERY), params);
    }

    /**
     * 下载资金账单
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String downloadFundFlow(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.DOWNLOAD_FUND_FLOW), params, certPath, certPass);
    }

    /**
     * 下载资金账单
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String downloadFundFlow(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.DOWNLOAD_FUND_FLOW), params, certFile, certPass);
    }

    /**
     * 刷脸支付
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String facePay(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.FACE_PAY), params);
    }

    /**
     * 查询刷脸支付订单
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String facePayQuery(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.FACE_PAY_QUERY), params);
    }

    /**
     * 刷脸支付撤销订单
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String facePayReverse(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.FACE_PAY_REVERSE), params, certPath, certPass);
    }

    /**
     * 刷脸支付撤销订单
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String facePayReverse(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.FACE_PAY_REVERSE), params, certFile, certPass);
    }

    /**
     * 发放普通红包
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String sendRedPack(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.SEND_RED_PACK), params, certPath, certPass);
    }

    /**
     * 发放普通红包
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String sendRedPack(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.SEND_RED_PACK), params, certFile, certPass);
    }

    /**
     * 发放裂变红包
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String sendGroupRedPack(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.SEND_GROUP_RED_PACK), params, certPath, certPass);
    }

    /**
     * 发放裂变红包
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String sendGroupRedPack(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.SEND_GROUP_RED_PACK), params, certFile, certPass);
    }

    /**
     * 查询红包记录
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String getHbInfo(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.GET_HB_INFO), params, certPath, certPass);
    }

    /**
     * 查询红包记录
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String getHbInfo(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.GET_HB_INFO), params, certFile, certPass);
    }

    /**
     * 小程序发放红包接口
     *
     * @param params   请求参数
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String sendMiniProgramRedPack(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.SEND_MINI_PROGRAM_HB), params, certPath, certPass);
    }

    /**
     * 小程序发放红包接口
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String sendMiniProgramRedPack(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.SEND_MINI_PROGRAM_HB), params, certFile, certPass);
    }

    public static String doGet(String url, Map<String, Object> params) {
        return HttpKit.getDelegate().get(url, params);
    }

    public static String doGet(String url, String authorization, Map<String, Object> params) {
        return HttpKit.getDelegate().get(url, authorization, params);
    }

    public static String doPost(String url, String authorization, String data) {
        return HttpKit.getDelegate().post(url, authorization, data);
    }

    public static String doUpload(String url, String authorization, String data, File file) {
        return HttpKit.getDelegate().upload(url, authorization, data, file);
    }

    public static String doPost(String url, Map<String, String> params) {
        return HttpKit.getDelegate().post(url, WxPayKit.toXml(params));
    }

    public static String doPostSSL(String url, Map<String, String> params, String certPath, String certPass) {
        return HttpKit.getDelegate().post(url, WxPayKit.toXml(params), certPath, certPass);
    }

    public static String doPostSSL(String url, Map<String, String> params, InputStream certFile, String certPass) {
        return HttpKit.getDelegate().post(url, WxPayKit.toXml(params), certFile, certPass);
    }

}
