/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNW</p>
 *
 * <p>付款码支付接口 Model</p>
 *
 * @author Javen
 */
package com.ijpay.jdpay.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class FkmModel extends JdBaseModel {
    private String token;
    private String version;
    private String merchant;
    private String device;
    private String tradeNum;
    private String tradeName;
    private String tradeDesc;
    private String tradeTime;
    private String amount;
    private String industryCategoryCode;
    private String currency;
    private String note;
    private String notifyUrl;
    private String orderGoodsNum;
    private String vendorId;
    private String goodsInfoList;
    private String receiverInfo;
    private String termInfo;
    private String payMerchant;
    private String sign;
    private String riskInfo;
    private String bizTp;
}
