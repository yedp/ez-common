package com.github.yedp.ez.common.model;

import com.github.yedp.ez.common.enums.RetCodeEnum;

import java.io.Serializable;

public class RetObj<T> implements Serializable {

    private static final long serialVersionUID = -7006019016089975751L;


    private int code;

    private String message;
    private T data;

    public RetObj() {
    }

    /**
     * 返回信息构造
     *
     * @param retCodeEnum 返回码枚举值
     * @param data        返回数据
     */
    public RetObj(RetCodeEnum retCodeEnum, T data) {
        this.data = data;
        this.code = retCodeEnum.getCode();
        this.message = retCodeEnum.getMessage();
    }


    /**
     * 返回信息构造
     *
     * @param data 返回数据
     */
    public RetObj(T data) {
        this(RetCodeEnum.SUCCESS, data);
    }


    /**
     * 返回信息构造
     *
     * @param retCodeEnum 返回码枚举值
     */
    public RetObj(RetCodeEnum retCodeEnum) {
        this.code = retCodeEnum.getCode();
        this.message = retCodeEnum.getMessage();
    }

    /**
     * 返回信息构造
     *
     * @param code    状态码
     * @param message 消息
     */
    public RetObj(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 返回信息构造
     *
     * @param code    状态码
     * @param message 消息
     * @param data    数据
     */
    public RetObj(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功
     *
     * @return retObj
     */
    public static RetObj success() {
        return new RetObj(RetCodeEnum.SUCCESS);
    }

    /**
     * 成功
     *
     * @param message 消息
     * @return retObj
     */
    public static RetObj success(String message) {
        return new RetObj(RetCodeEnum.SUCCESS.getCode(), message);
    }

    /**
     * 成功
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return retObj
     */
    public static <T> RetObj<T> success(T data) {
        return new RetObj<>(RetCodeEnum.SUCCESS, data);
    }

    /**
     * 失败
     *
     * @param retCodeEnum 失败状态枚举
     * @return retObj
     */
    public static RetObj error(RetCodeEnum retCodeEnum) {
        return new RetObj(retCodeEnum);
    }

    /**
     * 失败
     *
     * @param retCodeEnum 失败状态枚举
     * @param data        失败数据
     * @param <T>         失败数据类型
     * @return retObj
     */
    public static <T> RetObj<T> error(RetCodeEnum retCodeEnum, T data) {
        return new RetObj<>(retCodeEnum, data);
    }

    /**
     * 失败
     *
     * @param errMessage 失败消息
     * @return retObj
     */
    public static RetObj error(String errMessage) {
        return new RetObj(RetCodeEnum.BUSINESS_ERROR.getCode(), errMessage);
    }


    /**
     * 失败
     *
     * @param code       失败状态码
     * @param errMessage 失败消息
     * @return retObj
     */
    public static RetObj error(int code, String errMessage) {
        return new RetObj(code, errMessage);
    }

    /**
     * 失败
     *
     * @param code       失败状态码
     * @param errMessage 失败消息
     * @param data       失败数据
     * @param <T>        失败数据类型
     * @return retObj
     */
    public static <T> RetObj<T> error(int code, String errMessage, T data) {
        return new RetObj<>(code, errMessage, data);
    }

    /**
     * 获取返回码
     *
     * @return  返回码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置返回码
     *
     * @param code 返回码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取返回消息
     *
     * @return 返回消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置返回消息
     *
     * @param message 返回消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取返回数据
     *
     * @return 返回数据
     */
    public T getData() {
        return data;
    }

    /**
     * 设置返回数据
     *
     * @param data 返回数据
     */
    public void setData(T data) {
        this.data = data;
    }
}