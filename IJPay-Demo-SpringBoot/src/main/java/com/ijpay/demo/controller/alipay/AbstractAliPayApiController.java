package com.ijpay.demo.controller.alipay;

import com.alipay.api.AlipayApiException;
import com.ijpay.alipay.AliPayApiConfig;

/**
 * @author Javen
 */
public abstract class AbstractAliPayApiController {
    /**
     * 获取支付宝配置
     *
     * @return {@link AliPayApiConfig} 支付宝配置
     * @throws AlipayApiException 支付宝 Api 异常
     */
    public abstract AliPayApiConfig getApiConfig() throws AlipayApiException;
}
