package com.ijpay.xpay.vo;

import java.io.Serializable;

/**
 * @author Javen
 */
public class AjaxResult implements Serializable {

    private static final long serialVersionUID = 6439646269084700779L;

    private int code = 0;

    /**
     * 返回的中文消息
     */
    private String message;

    /**
     * 成功时携带的数据
     */
    private Object data;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public boolean hasError() {
        return this.code != 0;
    }

    public AjaxResult addError(String message) {
        this.message = message;
        this.code = 1;
        return this;
    }

    public AjaxResult addConfirmError(String message) {
        this.message = message;
        this.code = 2;
        return this;
    }

    public AjaxResult success(Object data) {
        this.data = data;
        this.code = 0;
        return this;
    }

}