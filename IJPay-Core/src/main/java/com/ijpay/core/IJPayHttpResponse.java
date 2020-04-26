
package com.ijpay.core;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.util.StrUtil;

import java.io.Serializable;
import java.util.List;
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
 * <p>IJPay Http Response</p>
 *
 * @author Javen
 */
public class IJPayHttpResponse implements Serializable {
    private static final long serialVersionUID = 6089103955998013402L;
    private String body;
    private int status;
    private Map<String, List<String>> headers;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public String header(String name) {
        List<String> values = this.headerList(name);
        return CollectionUtil.isEmpty(values) ? null : values.get(0);
    }

    private List<String> headerList(String name) {
        if (StrUtil.isBlank(name)) {
            return null;
        } else {
            CaseInsensitiveMap<String, List<String>> headersIgnoreCase = new CaseInsensitiveMap<>(getHeaders());
            return headersIgnoreCase.get(name.trim());
        }
    }

    @Override
    public String toString() {
        return "IJPayHttpResponse{" +
                "body='" + body + '\'' +
                ", status=" + status +
                ", headers=" + headers +
                '}';
    }
}
