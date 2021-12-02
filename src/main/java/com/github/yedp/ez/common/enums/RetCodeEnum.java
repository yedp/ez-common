package com.github.yedp.ez.common.enums;

public enum RetCodeEnum {

    /**
     * 请求成功
     */
    SUCCESS(200, "请求成功"),
    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权"),
    /**
     * 系统异常
     */
    SYSTEM_ERROR(500, "系统异常"),
    /**
     * 业务异常，一般错误使用该code
     */
    BUSINESS_ERROR(1001, "业务异常"),
    /**
     * 操作太频繁，请稍后再试
     */
    REQUEST_FREQUENTLY(1002, "操作太频繁，请稍后再试");

    private int code;
    private String message;

    /**
     * 返回码枚举
     *
     * @param code    返回码
     * @param message 返回消息
     */
    RetCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取返回码
     *
     * @return 返回码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取消息
     *
     * @return 消息
     */
    public String getMessage() {
        return message;
    }
}