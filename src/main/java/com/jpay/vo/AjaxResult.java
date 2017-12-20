package com.jpay.vo;

public class AjaxResult {

	private int code = 0;

	// 返回的中文消息
	private String message;

	// 成功时携带的数据
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

	// 校验错误
	public boolean hasError() {
		return this.code != 0;
	}

	// 添加错误，用于alertError
	public AjaxResult addError(String message) {
		this.message = message;
		this.code = 1;
		return this;
	}

	/**
	 * 用于Confirm的错误信息
	 * @param message
	 * @return {AjaxResult}
	 */
	public AjaxResult addConfirmError(String message) {
		this.message = message;
		this.code = 2;
		return this;
	}

	/**
	 * 封装成功时的数据
	 * @param data
	 * @return {AjaxResult}
	 */
	public AjaxResult success(Object data) {
		this.data = data;
		return this;
	}

}
