package com.ijpay.demo.vo;

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

	/**
	 * 校验错误
	 *
	 * @return
	 */
	public boolean hasError() {
		return this.code != 0;
	}

	/**
	 * 添加错误，用于alertError
	 *
	 * @param message
	 * @return
	 */
	public AjaxResult addError(String message) {
		this.message = message;
		this.code = 1;
		return this;
	}

	/**
	 * 用于Confirm的错误信息
	 *
	 * @param message 描述消息
	 * @return {AjaxResult}
	 */
	public AjaxResult addConfirmError(String message) {
		this.message = message;
		this.code = 2;
		return this;
	}

	/**
	 * 封装成功时的数据
	 *
	 * @param data Object
	 * @return {AjaxResult}
	 */
	public AjaxResult success(Object data) {
		this.data = data;
		this.code = 0;
		return this;
	}

}
