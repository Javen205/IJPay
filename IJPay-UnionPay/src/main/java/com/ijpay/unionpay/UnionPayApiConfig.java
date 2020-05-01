
package com.ijpay.unionpay;

import lombok.*;
import java.io.Serializable;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付等常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 *
 * <p>Node.js 版: https://gitee.com/javen205/TNWX</p>
 *
 * <p>云闪付常用配置</p>
 *
 * @author Javen
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnionPayApiConfig implements Serializable {
    private static final long serialVersionUID = -9025648880068727445L;

    /**
     * 商户平台分配的账号
     */
    private String mchId;
    /**
     * 连锁商户号
     */
    private String groupMchId;
    /**
     * 授权交易机构代码
     */
    private String agentMchId;
    /**
     * 商户平台分配的密钥
     */
    private String apiKey;
    /**
     * 商户平台网关
     */
    private String serverUrl;
    /**
     * 应用域名，回调中会使用此参数
     */
    private String domain;
    /**
     * 其他附加参数
     */
    private Object exParams;
}
