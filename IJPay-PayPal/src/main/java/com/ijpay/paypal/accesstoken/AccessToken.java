
package com.ijpay.paypal.accesstoken;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ijpay.core.utils.RetryUtils;

import java.io.Serializable;
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
 * @author Javen
 */
public class AccessToken implements Serializable, RetryUtils.ResultCheck {
    private static final long serialVersionUID = 150495825818051646L;
    private String access_token;
    private String token_type;
    private String app_id;
    private Integer expires_in;
    private Long expiredTime;
    private String json;
    /**
     * http 请求状态码
     */
    private Integer status;

    public AccessToken(String json, int httpCode) {
        this.json = json;
        this.status = httpCode;
        try {
            JSONObject jsonObject = JSONUtil.parseObj(json);
            this.access_token = jsonObject.getStr("access_token");
            this.expires_in = jsonObject.getInt("expires_in");
            this.app_id = jsonObject.getStr("app_id");
            this.token_type = jsonObject.getStr("token_type");
            if (expires_in != null) {
                this.expiredTime = System.currentTimeMillis() + ((expires_in - 9) * 1000);
            }
            if (jsonObject.containsKey("expiredTime")) {
                this.expiredTime = jsonObject.getLong("expiredTime");
            }
            if (jsonObject.containsKey("status")) {
                this.status = jsonObject.getInt("status");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAvailable() {
        if (status != 200) {
            return false;
        }
        if (expiredTime == null) {
            return false;
        }
        if (expiredTime < System.currentTimeMillis()) {
            return false;
        }
        return StrUtil.isNotEmpty(access_token);
    }

    public String getCacheJson() {
        Map<String, Object> temp = JSONUtil.toBean(json, Map.class);
        temp.put("expiredTime", expiredTime);
        temp.remove("expires_in");
        temp.remove("scope");
        temp.remove("nonce");
        return JSONUtil.toJsonStr(temp);
    }

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String accessToken) {
        this.access_token = accessToken;
    }

    public String getTokenType() {
        return token_type;
    }

    public void setTokenType(String tokenType) {
        this.token_type = tokenType;
    }

    public String getAppId() {
        return app_id;
    }

    public void setAppId(String appId) {
        this.app_id = appId;
    }

    public Integer getExpiresIn() {
        return expires_in;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expires_in = expiresIn;
    }

    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }

    @Override
    public boolean matching() {
        return isAvailable();
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
