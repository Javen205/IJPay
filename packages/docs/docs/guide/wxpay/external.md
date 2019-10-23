# 微信境外支付与跨城冗灾

::: tip
IJPay 2.3.0 版本开始支持微信境外支付
:::

## 境外支付与国内支付区别

仅仅只是域名不一样，接口以及签名方式完全一样。所以就有以下的封装。

## 微信支付域名 

- apihk.mch.weixin.qq.com（建议接入点：东南亚）
- apius.mch.weixin.qq.com（建议接入点：其它）
- api.mch.weixin.qq.com（建议接入点：中国国内）
- api2.mch.weixin.qq.com（建议接入点：中国国内备用）

IJPay 中是通过枚举 [WxDomain](https://gitee.com/javen205/IJPay/blob/master/IJPay-WxPay/src/main/java/com/ijpay/wxpay/enums/WxDomain.java) 来实现，
所有支付方式接口是通过枚举 [WxApiType](https://gitee.com/javen205/IJPay/blob/master/IJPay-WxPay/src/main/java/com/ijpay/wxpay/enums/WxApiType.java) 来实现，如有遗漏欢迎反馈 PR

## 实现原理
 
::: tip
实现原理非常简单粗暴：字符串拼接(微信支付域名+沙箱环境(仿真测试系统)+接口URL)
:::

例如：统一下单
- 东南亚: https://apihk.mch.weixin.qq.com/pay/unifiedorder
- 其它: https://apius.mch.weixin.qq.com/pay/unifiedorder 
- 中国国内: https://api.mch.weixin.qq.com/pay/unifiedorder
- 中国国内备用: https://api2.mch.weixin.qq.com/pay/unifiedorder
- 中国国内仿真系统: https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder


## 获取接口URL并发起请求

::: tip
获取接口URL有三个重载方法：
:::
 
 - `WxPayApi.getReqUrl(wxApiType)` 国内正式环境 
 - `WxPayApi.getReqUrl(wxApiType,isSandBox)` 国内正式环境与沙箱环境
 - `WxPayApi.getReqUrl(wxApiType,wxDomain,isSandBox)` 自定义
 
::: tip
通过接口发起请求有四个方法：
:::

- `WxPayApi.executionByGet(apiUrl, params)`  发起 Get 请求
- `WxPayApi.execution(apiUrl, params)`  发起 POST 请求
- `WxPayApi.execution(apiUrl, params, certPath, certPass)` 双向证书发起 POST 请求
- `WxPayApi.execution(apiUrl, params, certFileInputStream, certPass)` 双向证书发起 POST 请求

::: warning
IJPay 默认 Http 客户端是使用 [HuTool](https://hutool.cn) 中的 Http 客户端工具类 HttpUtil 来实现。
如需扩展请参考 [扩展 HTTP 请求](../http.md) ，自定定义 HTTP 请求也很容易实现 [跨城冗灾](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=23_6&index=4)
:::

```java
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
```   

## 如何使用？


::: tip
统一下单为例：
:::


```java
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
``` 

