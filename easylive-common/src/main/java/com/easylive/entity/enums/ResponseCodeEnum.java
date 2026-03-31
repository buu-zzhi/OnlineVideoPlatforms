package com.easylive.entity.enums;

/**
 * @Description: 响应参数
 * @Author: KunSpireUp
 * @Date: 3/27/2024 12:00 AM
 */
public enum ResponseCodeEnum {

	CODE_200(200, "请求成功"),
	CODE_404(404, "请求地址不能存在"),
	CODE_600(600, "请求参数错误"),
	CODE_601(601, "信息已存在"),
	CODE_500(500, "服务器返回错误，请联系管理员"),
    CODE_901(901, "未登录或登陆超时"),
    CODE_429(429, "服务繁忙，请稍后重试");

	private Integer code;

	private String msg;

	ResponseCodeEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
