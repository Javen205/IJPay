package com.ijpay.wxpay.enums.v3;

import com.ijpay.wxpay.enums.WxApiEnum;

/**
 * <p>IJPay 让支付触手可及，封装了微信支付、支付宝支付、银联支付常用的支付方式以及各种常用的接口。</p>
 *
 * <p>不依赖任何第三方 mvc 框架，仅仅作为工具使用简单快速完成支付模块的开发，可轻松嵌入到任何系统里。 </p>
 *
 * <p>IJPay 交流群: 723992875</p>
 * <p>IJPay 自由交流群: 864988890</p>
 *
 * <p>Node.js 版: <a href="https://gitee.com/javen205/TNWX">Node.js 版</a></p>
 *
 * <p>微信支付 v3 接口-消费者投诉2.0接口枚举</p>
 *
 * @author Javen
 */
public enum ComplaintsApiEnum implements WxApiEnum {

    /**
     * 查询投诉单列表
     */
    COMPLAINTS_V2("/v3/merchant-service/complaints-v2", "查询投诉单列表"),
    
    /**
     * 查询投诉单详情
     */
    COMPLAINTS_DETAIL("/v3/merchant-service/complaints-v2/%s", "查询投诉单详情"),

    /**
     * 查询投诉协商历史
     */
    COMPLAINTS_NEGOTIATION_HISTORY("/v3/merchant-service/complaints-v2/%s/negotiation-historys", "查询投诉协商历史"),

    /**
     * 创建/查询/更新/删除投诉通知回调
     */
    COMPLAINTS_NOTIFICATION("/v3/merchant-service/complaint-notifications", "创建/查询/更新/删除投诉通知回调"),

    /**
     * 提交回复
     */
    COMPLAINTS_RESPONSE("/v3/merchant-service/complaints-v2/%s/response", "提交回复"),

    /**
     * 商户上传反馈图片
     */
    IMAGES_UPLOAD("/v3/merchant-service/images/upload", "商户上传反馈图片"),

    /**
     * 反馈处理完成
     */
    COMPLAINTS_COMPLETE("/v3/merchant-service/complaints-v2/%s/complete", "反馈处理完成"),

    ;

    /**
     * 接口URL
     */
    private final String url;

    /**
     * 接口描述
     */
    private final String desc;

    ComplaintsApiEnum(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    /**
     * 获取枚举URL
     *
     * @return 枚举编码
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * 获取详细的描述信息
     *
     * @return 描述信息
     */
    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return url;
    }
}
