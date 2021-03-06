package com.chineseall.util.model;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 17:41.
 */
public class RetMsg<T> {
    /**
     * 错误码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 具体内容
     */
    private T data;

    public RetMsg() {

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.code = 200;
        this.msg = "ok";
        this.data = data;
    }

    public void success(String msg) {
        this.code = 200;
        this.msg = msg;
    }

    public void fail() {
        this.code = 400;
        this.msg = "fail";
    }

    public void fail(String msg) {
        this.code = 400;
        this.msg = msg;
    }

}
