package com.ijpay.alipay.v3;

import cn.hutool.core.map.MapUtil;
import com.alipay.v3.ApiClient;
import com.alipay.v3.ApiException;
import com.alipay.v3.ApiResponse;
import com.alipay.v3.Configuration;
import com.alipay.v3.util.GenericExecuteApi;
import com.alipay.v3.util.model.AlipayConfig;
import com.alipay.v3.util.model.OpenApiGenericRequest;
import com.google.gson.reflect.TypeToken;
import com.ijpay.alipay.v3.enums.AliPayMethodEnum;

import java.io.File;
import java.util.Map;

/**
 * <p>
 * IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。
 * </p>
 *
 * <p>
 * 不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。
 * </p>
 *
 * <p>
 * IJPay 交流群: 723992875、864988890
 * </p>
 *
 * <p>
 * Node.js 版: <a href="https://gitee.com/javen205/TNWX">https://gitee.com/javen205/TNWX</a>
 * </p>
 *
 * <p>
 * 支付宝支付相关接口
 * </p>
 *
 * @author Javen
 */
public class AliPayApi {

    /**
     * 获取默认客户端
     *
     * @return {@link ApiClient}
     */
    public static ApiClient getAliClient() {
        return Configuration.getDefaultApiClient();
    }

    /**
     * 设置网关地址
     *
     * @param baseUrl 网关地址
     */
    public static void setBaseUrl(String baseUrl) {
        getAliClient().setBasePath(baseUrl);
    }

    /**
     * 设置 alipayConfig 参数（全局设置一次）
     *
     * @param alipayConfig {@link AlipayConfig} 支付宝配置
     * @throws ApiException 异常信息
     */
    public static void setAlipayConfig(AlipayConfig alipayConfig) throws ApiException {
        getAliClient().setAlipayConfig(alipayConfig);
    }

    /**
     * 页面类接口执行（表单/URL 方式，用于电脑网站支付、手机网站支付等）
     *
     * @param method     业务接口 method，如 {@link AliPayMethodEnum#TRADE_PAGE_PAY}
     * @param httpMethod HTTP 方式，GET 或 POST
     * @param bizParams  业务参数
     * @return 跳转 URL（GET 模式）或自动提交表单 HTML（POST 模式）
     * @throws ApiException 异常信息
     */
    public static String pageExecute(AliPayMethodEnum method, String httpMethod, Map<String, Object> bizParams) throws ApiException {
        GenericExecuteApi api = new GenericExecuteApi();
        return api.pageExecute(method.getValue(), httpMethod, bizParams);
    }

    /**
     * SDK 类接口执行（返回带签名参数的字符串，用于 App 支付）
     *
     * @param method    业务接口 method，如 {@link AliPayMethodEnum#TRADE_APP_PAY}
     * @param bizParams 业务参数
     * @return 带签名参数的 URL
     * @throws ApiException 异常信息
     */
    public static String sdkExecute(AliPayMethodEnum method, Map<String, Object> bizParams) throws ApiException {
        GenericExecuteApi api = new GenericExecuteApi();
        return api.sdkExecute(method.getValue(), bizParams);
    }

    /**
     * 通用接口调用（V3 RESTful 风格）
     * <p>path 形如 /v3/alipay/trade/create，method 为 GET/POST/PUT/DELETE 等 HTTP 方法</p>
     *
     * @param path    接口路径，如 /v3/alipay/trade/create
     * @param method  HTTP 方法，如 POST
     * @param request {@link OpenApiGenericRequest} 通用请求对象
     * @return {@link ApiResponse}
     * @throws ApiException 异常信息
     */
    public static ApiResponse<Object> execute(String path, String method, OpenApiGenericRequest request) throws ApiException {
        GenericExecuteApi api = new GenericExecuteApi();
        return api.execute(path, method, request);
    }

    /**
     * 通用接口调用（V3 RESTful 风格），支持指定泛型返回类型
     *
     * @param path     接口路径，如 /v3/alipay/trade/create
     * @param method   HTTP 方法，如 POST
     * @param request  {@link OpenApiGenericRequest} 通用请求对象
     * @param type     {@link TypeToken} 泛型返回类型，如 new TypeToken&lt;AlipayTradeCreateResponseModel&gt;() {}
     * @param <T>      返回类型
     * @return {@link ApiResponse}
     * @throws ApiException 异常信息
     */
    public static <T> ApiResponse<T> execute(String path, String method, OpenApiGenericRequest request,
                                             TypeToken<T> type) throws ApiException {
        GenericExecuteApi api = new GenericExecuteApi();
        return api.execute(path, method, request, type);
    }

    /**
     * 通用接口调用（V3 RESTful 风格）
     *
     * @param path        接口路径，如 /v3/alipay/trade/create
     * @param method      HTTP 方法，如 POST
     * @param pathParams  path 参数
     * @param queryParams query 参数
     * @param bodyParams  body 参数
     * @param fileParams  文件参数
     * @return {@link ApiResponse}
     * @throws ApiException 异常信息
     */
    public static ApiResponse<Object> execute(String path, String method, Map<String, Object> pathParams,
                                              Map<String, Object> queryParams, Map<String, Object> bodyParams,
                                              Map<String, File> fileParams) throws ApiException {
        return execute(path, method, buildRequest(pathParams, queryParams, bodyParams, fileParams));
    }

    /**
     * 通用接口调用（V3 RESTful 风格），支持指定泛型返回类型
     *
     * @param path        接口路径，如 /v3/alipay/trade/create
     * @param method      HTTP 方法，如 POST
     * @param pathParams  path 参数
     * @param queryParams query 参数
     * @param bodyParams  body 参数
     * @param fileParams  文件参数
     * @param type        {@link TypeToken} 泛型返回类型
     * @param <T>         返回类型
     * @return {@link ApiResponse}
     * @throws ApiException 异常信息
     */
    public static <T> ApiResponse<T> execute(String path, String method, Map<String, Object> pathParams,
                                             Map<String, Object> queryParams, Map<String, Object> bodyParams,
                                             Map<String, File> fileParams, TypeToken<T> type) throws ApiException {
        return execute(path, method, buildRequest(pathParams, queryParams, bodyParams, fileParams), type);
    }

    /**
     * 电脑网站支付（PC 端），返回跳转 URL 或自动提交表单 HTML
     *
     * @param bizParams 业务参数
     * @return 跳转 URL（GET 模式）或表单 HTML（POST 模式）
     * @throws ApiException 异常信息
     */
    public static String tradePagePay(Map<String, Object> bizParams) throws ApiException {
        return pageExecute(AliPayMethodEnum.TRADE_PAGE_PAY, "POST", bizParams);
    }

    /**
     * 电脑网站支付（PC 端），自定义 HTTP 方式
     *
     * @param httpMethod GET 或 POST
     * @param bizParams  业务参数
     * @return 跳转 URL 或表单 HTML
     * @throws ApiException 异常信息
     */
    public static String tradePagePay(String httpMethod, Map<String, Object> bizParams) throws ApiException {
        return pageExecute(AliPayMethodEnum.TRADE_PAGE_PAY, httpMethod, bizParams);
    }

    /**
     * 手机网站支付（H5 端）
     *
     * @param bizParams 业务参数
     * @return 跳转 URL 或表单 HTML
     * @throws ApiException 异常信息
     */
    public static String tradeWapPay(Map<String, Object> bizParams) throws ApiException {
        return pageExecute(AliPayMethodEnum.TRADE_WAP_PAY, "POST", bizParams);
    }

    /**
     * 手机网站支付（H5 端），自定义 HTTP 方式
     *
     * @param httpMethod GET 或 POST
     * @param bizParams  业务参数
     * @return 跳转 URL 或表单 HTML
     * @throws ApiException 异常信息
     */
    public static String tradeWapPay(String httpMethod, Map<String, Object> bizParams) throws ApiException {
        return pageExecute(AliPayMethodEnum.TRADE_WAP_PAY, httpMethod, bizParams);
    }

    /**
     * App 支付，返回带签名参数的调起支付 URL
     *
     * @param bizParams 业务参数
     * @return 带签名参数的 URL
     * @throws ApiException 异常信息
     */
    public static String tradeAppPay(Map<String, Object> bizParams) throws ApiException {
        return sdkExecute(AliPayMethodEnum.TRADE_APP_PAY, bizParams);
    }

    /**
     * 构建通用请求对象
     */
    private static OpenApiGenericRequest buildRequest(Map<String, Object> pathParams, Map<String, Object> queryParams,
                                                      Map<String, Object> bodyParams, Map<String, File> fileParams) {
        OpenApiGenericRequest request = new OpenApiGenericRequest();
        if (MapUtil.isNotEmpty(pathParams)) {
            request.setPathParams(pathParams);
        }
        if (MapUtil.isNotEmpty(queryParams)) {
            request.setQueryParams(queryParams);
        }
        if (MapUtil.isNotEmpty(bodyParams)) {
            request.setBodyParams(bodyParams);
        }
        if (MapUtil.isNotEmpty(fileParams)) {
            request.setFileParams(fileParams);
        }
        return request;
    }

}
