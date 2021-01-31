package com.ijpay.wxpay;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.PayModel;
import com.ijpay.core.enums.RequestMethod;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.enums.WxApiType;
import com.ijpay.wxpay.enums.WxDomain;

import java.io.File;
import java.io.InputStream;
import java.security.PrivateKey;
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
     * @param certPath 证书文件路径
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String execution(String apiUrl, Map<String, String> params, String certPath, String certPass) {
        return doPostSsl(apiUrl, params, certPath, certPass);
    }

    /**
     * 发起请求
     *
     * @param apiUrl   接口 URL
     *                 通过 {@link WxPayApi#getReqUrl(WxApiType)}
     *                 或者 {@link WxPayApi#getReqUrl(WxApiType, WxDomain, boolean)} 来获取
     * @param params   接口请求参数
     * @param certPath 证书文件路径
     * @return {@link String} 请求返回的结果
     */
    public static String execution(String apiUrl, Map<String, String> params, String certPath) {
        return doPostSsl(apiUrl, params, certPath);
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
        return doPostSsl(apiUrl, params, certFile, certPass);
    }

    /**
     * 发起请求
     *
     * @param apiUrl   接口 URL
     *                 通过 {@link WxPayApi#getReqUrl(WxApiType)}
     *                 或者 {@link WxPayApi#getReqUrl(WxApiType, WxDomain, boolean)} 来获取
     * @param params   接口请求参数
     * @param certFile 证书文件输入流
     * @return {@link String} 请求返回的结果
     */
    public static String execution(String apiUrl, Map<String, String> params, InputStream certFile) {
        return doPostSsl(apiUrl, params, certFile);
    }

    public static String execution(String apiUrl, Map<String, String> params,
                                   String certPath, String certPass, String filePath) {
        return doUploadSsl(apiUrl, params, certPath, certPass, filePath);
    }


    /**
     * V3 接口统一执行入口
     *
     * @param method       {@link RequestMethod} 请求方法
     * @param urlPrefix    可通过 {@link WxDomain}来获取
     * @param urlSuffix    可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户Id
     * @param serialNo     商户 API 证书序列号
     * @param platSerialNo 平台序列号，接口中包含敏感信息时必传
     * @param keyPath      apiclient_key.pem 证书路径
     * @param body         接口请求参数
     * @param nonceStr     随机字符库
     * @param timestamp    时间戳
     * @param authType     认证类型
     * @param file         文件
     * @return {@link IJPayHttpResponse} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public static IJPayHttpResponse v3(RequestMethod method, String urlPrefix, String urlSuffix,
                                       String mchId, String serialNo, String platSerialNo, String keyPath,
                                       String body, String nonceStr, long timestamp, String authType,
                                       File file) throws Exception {
        // 构建 Authorization
        String authorization = WxPayKit.buildAuthorization(method, urlSuffix, mchId, serialNo,
                keyPath, body, nonceStr, timestamp, authType);

        if (StrUtil.isEmpty(platSerialNo)) {
            platSerialNo = serialNo;
        }

        if (method == RequestMethod.GET) {
            return get(urlPrefix.concat(urlSuffix), authorization, platSerialNo, null);
        } else if (method == RequestMethod.POST) {
            return post(urlPrefix.concat(urlSuffix), authorization, platSerialNo, body);
        } else if (method == RequestMethod.DELETE) {
            return delete(urlPrefix.concat(urlSuffix), authorization, platSerialNo, body);
        } else if (method == RequestMethod.UPLOAD) {
            return upload(urlPrefix.concat(urlSuffix), authorization, platSerialNo, body, file);
        } else if (method == RequestMethod.PUT) {
            return put(urlPrefix.concat(urlSuffix), authorization, platSerialNo, body);
        }
        return null;
    }

    /**
     * V3 接口统一执行入口
     *
     * @param method       {@link RequestMethod} 请求方法
     * @param urlPrefix    可通过 {@link WxDomain}来获取
     * @param urlSuffix    可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户Id
     * @param serialNo     商户 API 证书序列号
     * @param platSerialNo 平台序列号，接口中包含敏感信息时必传
     * @param privateKey   商户私钥
     * @param body         接口请求参数
     * @param nonceStr     随机字符库
     * @param timestamp    时间戳
     * @param authType     认证类型
     * @param file         文件
     * @return {@link IJPayHttpResponse} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public static IJPayHttpResponse v3(RequestMethod method, String urlPrefix, String urlSuffix,
                                       String mchId, String serialNo, String platSerialNo, PrivateKey privateKey,
                                       String body, String nonceStr, long timestamp, String authType,
                                       File file) throws Exception {
        // 构建 Authorization
        String authorization = WxPayKit.buildAuthorization(method, urlSuffix, mchId, serialNo,
                privateKey, body, nonceStr, timestamp, authType);

        if (StrUtil.isEmpty(platSerialNo)) {
            platSerialNo = serialNo;
        }

        if (method == RequestMethod.GET) {
            return get(urlPrefix.concat(urlSuffix), authorization, platSerialNo, null);
        } else if (method == RequestMethod.POST) {
            return post(urlPrefix.concat(urlSuffix), authorization, platSerialNo, body);
        } else if (method == RequestMethod.DELETE) {
            return delete(urlPrefix.concat(urlSuffix), authorization, platSerialNo, body);
        } else if (method == RequestMethod.UPLOAD) {
            return upload(urlPrefix.concat(urlSuffix), authorization, platSerialNo, body, file);
        } else if (method == RequestMethod.PUT) {
            return put(urlPrefix.concat(urlSuffix), authorization, platSerialNo, body);
        }
        return null;
    }

    /**
     * V3 接口统一执行入口
     *
     * @param method       {@link RequestMethod} 请求方法
     * @param urlPrefix    可通过 {@link WxDomain}来获取
     * @param urlSuffix    可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户Id
     * @param serialNo     商户 API 证书序列号
     * @param platSerialNo 平台序列号
     * @param keyPath      apiclient_key.pem 证书路径
     * @param body         接口请求参数
     * @return {@link IJPayHttpResponse} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public static IJPayHttpResponse v3(RequestMethod method, String urlPrefix, String urlSuffix, String mchId,
                                       String serialNo, String platSerialNo, String keyPath, String body) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String authType = "WECHATPAY2-SHA256-RSA2048";
        String nonceStr = WxPayKit.generateStr();
        return v3(method, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, keyPath, body, nonceStr, timestamp, authType, null);
    }

    /**
     * V3 接口统一执行入口
     *
     * @param method       {@link RequestMethod} 请求方法
     * @param urlPrefix    可通过 {@link WxDomain}来获取
     * @param urlSuffix    可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户Id
     * @param serialNo     商户 API 证书序列号
     * @param platSerialNo 平台序列号
     * @param privateKey   商户私钥
     * @param body         接口请求参数
     * @return {@link IJPayHttpResponse} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public static IJPayHttpResponse v3(RequestMethod method, String urlPrefix, String urlSuffix, String mchId,
                                       String serialNo, String platSerialNo, PrivateKey privateKey, String body) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String authType = "WECHATPAY2-SHA256-RSA2048";
        String nonceStr = WxPayKit.generateStr();
        return v3(method, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, privateKey, body, nonceStr, timestamp, authType, null);
    }

    /**
     * V3 接口统一执行入口
     *
     * @param method       {@link RequestMethod} 请求方法
     * @param urlPrefix    可通过 {@link WxDomain}来获取
     * @param urlSuffix    可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户Id
     * @param serialNo     商户 API 证书序列号
     * @param platSerialNo 平台序列号
     * @param keyPath      apiclient_key.pem 证书路径
     * @param params       Get 接口请求参数
     * @return {@link IJPayHttpResponse} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public static IJPayHttpResponse v3(RequestMethod method, String urlPrefix, String urlSuffix,
                                       String mchId, String serialNo, String platSerialNo, String keyPath,
                                       Map<String, String> params) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String authType = "WECHATPAY2-SHA256-RSA2048";
        String nonceStr = WxPayKit.generateStr();
        if (null != params && !params.keySet().isEmpty()) {
            urlSuffix = urlSuffix.concat("?").concat(PayKit.createLinkString(params, true));
        }
        return v3(method, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, keyPath, "", nonceStr, timestamp, authType, null);
    }

    /**
     * V3 接口统一执行入口
     *
     * @param method       {@link RequestMethod} 请求方法
     * @param urlPrefix    可通过 {@link WxDomain}来获取
     * @param urlSuffix    可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户Id
     * @param serialNo     商户 API 证书序列号
     * @param platSerialNo 平台序列号
     * @param privateKey   商户私钥
     * @param params       Get 接口请求参数
     * @return {@link IJPayHttpResponse} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public static IJPayHttpResponse v3(RequestMethod method, String urlPrefix, String urlSuffix,
                                       String mchId, String serialNo, String platSerialNo, PrivateKey privateKey,
                                       Map<String, String> params) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String authType = "WECHATPAY2-SHA256-RSA2048";
        String nonceStr = WxPayKit.generateStr();
        if (null != params && !params.keySet().isEmpty()) {
            urlSuffix = urlSuffix.concat("?").concat(PayKit.createLinkString(params, true));
        }
        return v3(method, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, privateKey, "", nonceStr, timestamp, authType, null);
    }

    /**
     * V3 接口统一执行入口
     *
     * @param urlPrefix    可通过 {@link WxDomain}来获取
     * @param urlSuffix    可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户Id
     * @param serialNo     商户 API 证书序列号
     * @param platSerialNo 平台序列号
     * @param keyPath      apiclient_key.pem 证书路径
     * @param body         接口请求参数
     * @param file         文件
     * @return {@link IJPayHttpResponse} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public static IJPayHttpResponse v3(String urlPrefix, String urlSuffix, String mchId, String serialNo, String platSerialNo, String keyPath, String body, File file) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String authType = "WECHATPAY2-SHA256-RSA2048";
        String nonceStr = WxPayKit.generateStr();
        return v3(RequestMethod.UPLOAD, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, keyPath, body, nonceStr, timestamp, authType, file);
    }

    /**
     * V3 接口统一执行入口
     *
     * @param urlPrefix    可通过 {@link WxDomain}来获取
     * @param urlSuffix    可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户Id
     * @param serialNo     商户 API 证书序列号
     * @param platSerialNo 平台序列号
     * @param privateKey   商户私钥
     * @param body         接口请求参数
     * @param file         文件
     * @return {@link IJPayHttpResponse} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public static IJPayHttpResponse v3(String urlPrefix, String urlSuffix, String mchId, String serialNo,
                                       String platSerialNo, PrivateKey privateKey, String body, File file) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String authType = "WECHATPAY2-SHA256-RSA2048";
        String nonceStr = WxPayKit.generateStr();
        return v3(RequestMethod.UPLOAD, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, privateKey, body, nonceStr, timestamp, authType, file);
    }

    /**
     * V3 接口统一执行入口
     *
     * @param method       {@link RequestMethod} 请求方法
     * @param urlPrefix    可通过 {@link WxDomain}来获取
     * @param urlSuffix    可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户Id
     * @param serialNo     商户 API 证书序列号
     * @param platSerialNo 平台序列号，接口中包含敏感信息时必传
     * @param keyPath      apiclient_key.pem 证书路径
     * @param body         接口请求参数
     * @param nonceStr     随机字符库
     * @param timestamp    时间戳
     * @param authType     认证类型
     * @param file         文件
     * @return {@link Map} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    @Deprecated
    public static Map<String, Object> v3Execution(RequestMethod method, String urlPrefix, String urlSuffix,
                                                  String mchId, String serialNo, String platSerialNo, String keyPath,
                                                  String body, String nonceStr, long timestamp, String authType,
                                                  File file) throws Exception {
        IJPayHttpResponse response = v3(method, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, keyPath, body, nonceStr, timestamp, authType, file);
        return buildResMap(response);
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
     * @return {@link Map} 请求返回的结果
     */
    @Deprecated
    public static Map<String, Object> v3Execution(RequestMethod method, String urlPrefix, String urlSuffix, String mchId,
                                                  String serialNo, String keyPath, String body) throws Exception {
        IJPayHttpResponse response = v3(method, urlPrefix, urlSuffix, mchId, serialNo, null, keyPath, body);
        return buildResMap(response);
    }

    /**
     * V3 接口统一执行入口
     *
     * @param method       {@link RequestMethod} 请求方法
     * @param urlPrefix    可通过 {@link WxDomain}来获取
     * @param urlSuffix    可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户Id
     * @param serialNo     商户 API 证书序列号
     * @param platSerialNo 平台序列号
     * @param keyPath      apiclient_key.pem 证书路径
     * @param body         接口请求参数
     * @return {@link Map} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    @Deprecated
    public static Map<String, Object> v3Execution(RequestMethod method, String urlPrefix, String urlSuffix, String mchId,
                                                  String serialNo, String platSerialNo, String keyPath, String body) throws Exception {
        IJPayHttpResponse response = v3(method, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, keyPath, body);
        return buildResMap(response);
    }

    /**
     * V3 接口统一执行入口
     *
     * @param method       {@link RequestMethod} 请求方法
     * @param urlPrefix    可通过 {@link WxDomain}来获取
     * @param urlSuffix    可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户Id
     * @param serialNo     商户 API 证书序列号
     * @param platSerialNo 平台序列号
     * @param keyPath      apiclient_key.pem 证书路径
     * @param params       Get 接口请求参数
     * @return {@link Map} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    @Deprecated
    public static Map<String, Object> v3Execution(RequestMethod method, String urlPrefix, String urlSuffix,
                                                  String mchId, String serialNo, String platSerialNo, String keyPath,
                                                  Map<String, String> params) throws Exception {
        IJPayHttpResponse response = v3(method, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, keyPath, params);
        return buildResMap(response);
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
     * @param params    Get 接口请求参数
     * @return {@link Map} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    @Deprecated
    public static Map<String, Object> v3Execution(RequestMethod method, String urlPrefix, String urlSuffix,
                                                  String mchId, String serialNo, String keyPath,
                                                  Map<String, String> params) throws Exception {
        IJPayHttpResponse response = v3(method, urlPrefix, urlSuffix, mchId, serialNo, null, keyPath, params);
        return buildResMap(response);
    }

    /**
     * V3 接口统一执行入口
     *
     * @param urlPrefix    可通过 {@link WxDomain}来获取
     * @param urlSuffix    可通过 {@link WxApiType} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户Id
     * @param serialNo     商户 API 证书序列号
     * @param platSerialNo 平台序列号
     * @param keyPath      apiclient_key.pem 证书路径
     * @param body         接口请求参数
     * @param file         文件
     * @return {@link Map} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    @Deprecated
    public static Map<String, Object> v3Upload(String urlPrefix, String urlSuffix, String mchId, String serialNo, String platSerialNo, String keyPath, String body, File file) throws Exception {
        IJPayHttpResponse response = v3(urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, keyPath, body, file);
        return buildResMap(response);
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
     * @return {@link Map} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    @Deprecated
    public static Map<String, Object> v3Upload(String urlPrefix, String urlSuffix, String mchId, String serialNo, String keyPath, String body, File file) throws Exception {
        return v3Upload(urlPrefix, urlSuffix, mchId, serialNo, null, keyPath, body, file);
    }

    /**
     * 获取验签秘钥API
     *
     * @param mchId      商户号
     * @param partnerKey API 密钥
     * @param signType   签名方式
     * @return {@link String} 请求返回的结果
     */
    public static String getSignKey(String mchId, String partnerKey, SignType signType) {
        Map<String, String> map = new HashMap<>(3);
        String nonceStr = WxPayKit.generateStr();
        map.put("mch_id", mchId);
        map.put("nonce_str", nonceStr);
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
     * @param certPath 证书文件路径
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
     * @param certPath  证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * 刷脸设备获取设备调用凭证
     *
     * @param params 请求参数
     * @return {@link String} 请求返回的结果
     */
    public static String getAuthInfo(Map<String, String> params) {
        return execution(getReqUrl(WxApiType.GET_AUTH_INFO, WxDomain.PAY_APP, false), params);
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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
     * @param certPath 证书文件路径
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

    /**
     * 发放企业红包
     *
     * @param params   请求参数
     * @param certPath 证书文件路径
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String sendWorkWxRedPack(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.SEND_WORK_WX_RED_PACK), params, certPath, certPass);
    }

    /**
     * 发放企业红包
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String sendWorkWxRedPack(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.SEND_WORK_WX_RED_PACK), params, certFile, certPass);
    }

    /**
     * 查询向员工付款记录
     *
     * @param params   请求参数
     * @param certPath 证书文件路径
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String queryWorkWxRedPack(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.QUERY_WORK_WX_RED_PACK), params, certPath, certPass);
    }

    /**
     * 查询向员工付款记录
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String queryWorkWxRedPack(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.QUERY_WORK_WX_RED_PACK), params, certFile, certPass);
    }

    /**
     * 向员工付款
     *
     * @param params   请求参数
     * @param certPath 证书文件路径
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String trans2pocket(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.PAY_WWS_TRANS_2_POCKET), params, certPath, certPass);
    }

    /**
     * 向员工付款
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String trans2pocket(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.PAY_WWS_TRANS_2_POCKET), params, certFile, certPass);
    }

    /**
     * 查询向员工付款记录
     *
     * @param params   请求参数
     * @param certPath 证书文件路径
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String queryTrans2pocket(Map<String, String> params, String certPath, String certPass) {
        return execution(getReqUrl(WxApiType.QUERY_WWS_TRANS_2_POCKET), params, certPath, certPass);
    }

    /**
     * 查询向员工付款记录
     *
     * @param params   请求参数
     * @param certFile 证书文件的 InputStream
     * @param certPass 证书密码
     * @return {@link String} 请求返回的结果
     */
    public static String queryTrans2pocket(Map<String, String> params, InputStream certFile, String certPass) {
        return execution(getReqUrl(WxApiType.QUERY_WWS_TRANS_2_POCKET), params, certFile, certPass);
    }

    /**
     * @param url    请求url
     * @param params 请求参数
     * @return {@link String}    请求返回的结果
     */
    public static String doGet(String url, Map<String, Object> params) {
        return HttpKit.getDelegate().get(url, params);
    }

    /**
     * get 请求
     *
     * @param url     请求url
     * @param params  请求参数
     * @param headers 请求头
     * @return {@link IJPayHttpResponse}    请求返回的结果
     */
    public static IJPayHttpResponse get(String url, Map<String, Object> params, Map<String, String> headers) {
        return HttpKit.getDelegate().get(url, params, headers);
    }

    /**
     * get 请求
     *
     * @param url           请求url
     * @param authorization 授权信息
     * @param serialNumber  公钥证书序列号
     * @param params        请求参数
     * @return {@link IJPayHttpResponse}    请求返回的结果
     */
    public static IJPayHttpResponse get(String url, String authorization, String serialNumber, Map<String, Object> params) {
        return get(url, params, getHeaders(authorization, serialNumber));
    }

    /**
     * post 请求
     *
     * @param url     请求url
     * @param data    请求参数
     * @param headers 请求头
     * @return {@link IJPayHttpResponse}    请求返回的结果
     */
    public static IJPayHttpResponse post(String url, String data, Map<String, String> headers) {
        return HttpKit.getDelegate().post(url, data, headers);
    }

    /**
     * post 请求
     *
     * @param url           请求url
     * @param authorization 授权信息
     * @param serialNumber  公钥证书序列号
     * @param data          请求参数
     * @return {@link IJPayHttpResponse}    请求返回的结果
     */
    public static IJPayHttpResponse post(String url, String authorization, String serialNumber, String data) {
        return post(url, data, getHeaders(authorization, serialNumber));
    }

    /**
     * delete 请求
     *
     * @param url     请求url
     * @param data    请求参数
     * @param headers 请求头
     * @return {@link IJPayHttpResponse}    请求返回的结果
     */
    public static IJPayHttpResponse delete(String url, String data, Map<String, String> headers) {
        return HttpKit.getDelegate().delete(url, data, headers);
    }

    /**
     * delete 请求
     *
     * @param url           请求url
     * @param authorization 授权信息
     * @param serialNumber  公钥证书序列号
     * @param data          请求参数
     * @return {@link IJPayHttpResponse}    请求返回的结果
     */
    public static IJPayHttpResponse delete(String url, String authorization, String serialNumber, String data) {
        return delete(url, data, getHeaders(authorization, serialNumber));
    }

    /**
     * upload 请求
     *
     * @param url     请求url
     * @param params  请求参数
     * @param headers 请求头
     * @return {@link IJPayHttpResponse}    请求返回的结果
     */
    public static IJPayHttpResponse upload(String url, Map<String, Object> params, Map<String, String> headers) {
        return HttpKit.getDelegate().post(url, params, headers);
    }

    /**
     * upload 请求
     *
     * @param url           请求url
     * @param authorization 授权信息
     * @param serialNumber  公钥证书序列号
     * @param data          请求参数
     * @param file          上传文件
     * @return {@link IJPayHttpResponse}    请求返回的结果
     */
    public static IJPayHttpResponse upload(String url, String authorization, String serialNumber, String data, File file) {
        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("file", file);
        paramMap.put("meta", data);
        return upload(url, paramMap, getUploadHeaders(authorization, serialNumber));
    }


    /**
     * put 请求
     *
     * @param url     请求url
     * @param data    请求参数
     * @param headers 请求头
     * @return {@link IJPayHttpResponse}    请求返回的结果
     */
    public static IJPayHttpResponse put(String url, String data, Map<String, String> headers) {
        return HttpKit.getDelegate().put(url, data, headers);
    }

    /**
     * put 请求
     *
     * @param url           请求url
     * @param authorization 授权信息
     * @param serialNumber  公钥证书序列号
     * @param data          请求参数
     * @return {@link IJPayHttpResponse}    请求返回的结果
     */
    public static IJPayHttpResponse put(String url, String authorization, String serialNumber, String data) {
        return put(url, data, getHeaders(authorization, serialNumber));
    }

    public static String doPost(String url, Map<String, String> params) {
        return HttpKit.getDelegate().post(url, WxPayKit.toXml(params));
    }

    public static String doPostSsl(String url, Map<String, String> params, String certPath, String certPass) {
        return HttpKit.getDelegate().post(url, WxPayKit.toXml(params), certPath, certPass);
    }

    public static String doPostSsl(String url, Map<String, String> params, InputStream certFile, String certPass) {
        return HttpKit.getDelegate().post(url, WxPayKit.toXml(params), certFile, certPass);
    }

    public static String doPostSsl(String url, Map<String, String> params, String certPath) {
        if (params.isEmpty() || !params.containsKey("mch_id")) {
            throw new RuntimeException("请求参数中必须包含 mch_id，如接口参考中不包 mch_id， 请使用其他同名构造方法。");
        }
        String certPass = params.get("mch_id");
        return doPostSsl(url, params, certPath, certPass);
    }

    public static String doPostSsl(String url, Map<String, String> params, InputStream certFile) {
        if (params.isEmpty() || !params.containsKey("mch_id")) {
            throw new RuntimeException("请求参数中必须包含 mch_id，如接口参考中不包 mch_id， 请使用其他同名构造方法。");
        }
        String certPass = params.get("mch_id");
        return doPostSsl(url, params, certFile, certPass);
    }

    public static String doUploadSsl(String url, Map<String, String> params, String certPath, String certPass, String filePath) {
        return HttpKit.getDelegate().upload(url, WxPayKit.toXml(params), certPath, certPass, filePath);
    }

    public static String doUploadSsl(String url, Map<String, String> params, String certPath, String filePath) {
        if (params.isEmpty() || !params.containsKey("mch_id")) {
            throw new RuntimeException("请求参数中必须包含 mch_id，如接口参考中不包 mch_id， 请使用其他同名构造方法。");
        }
        String certPass = params.get("mch_id");
        return doUploadSsl(url, params, certPath, certPass, filePath);
    }


    private static final String OS = System.getProperty("os.name") + "/" + System.getProperty("os.version");
    private static final String VERSION = System.getProperty("java.version");

    public static Map<String, String> getBaseHeaders(String authorization) {
        String userAgent = String.format(
                "WeChatPay-IJPay-HttpClient/%s (%s) Java/%s",
                WxPayApi.class.getPackage().getImplementationVersion(),
                OS,
                VERSION == null ? "Unknown" : VERSION);

        Map<String, String> headers = new HashMap<>(5);
        headers.put("Accept", ContentType.JSON.toString());
        headers.put("Authorization", authorization);
        headers.put("User-Agent", userAgent);
        return headers;
    }

    public static Map<String, String> getHeaders(String authorization, String serialNumber) {
        Map<String, String> headers = getBaseHeaders(authorization);
        headers.put("Content-Type", ContentType.JSON.toString());
        if (StrUtil.isNotEmpty(serialNumber)) {
            headers.put("Wechatpay-Serial", serialNumber);
        }
        return headers;
    }

    public static Map<String, String> getUploadHeaders(String authorization, String serialNumber) {
        Map<String, String> headers = getBaseHeaders(authorization);
        headers.put("Content-Type", "multipart/form-data;boundary=\"boundary\"");
        if (StrUtil.isNotEmpty(serialNumber)) {
            headers.put("Wechatpay-Serial", serialNumber);
        }
        return headers;
    }

    /**
     * 构建返回参数
     *
     * @param response {@link IJPayHttpResponse}
     * @return {@link Map}
     */
    public static Map<String, Object> buildResMap(IJPayHttpResponse response) {
        if (response == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>(6);
        String timestamp = response.getHeader("Wechatpay-Timestamp");
        String nonceStr = response.getHeader("Wechatpay-Nonce");
        String serialNo = response.getHeader("Wechatpay-Serial");
        String signature = response.getHeader("Wechatpay-Signature");
        String body = response.getBody();
        int status = response.getStatus();
        map.put("timestamp", timestamp);
        map.put("nonceStr", nonceStr);
        map.put("serialNumber", serialNo);
        map.put("signature", signature);
        map.put("body", body);
        map.put("status", status);
        return map;
    }
}
