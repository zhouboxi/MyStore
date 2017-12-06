package com.jxlg.app.store.common;

/**
 * 这是一个枚举,对不同的的状态进行封装
 * @author zhouboxi
 * @create 2017-11-27 19:03
 **/
public enum ResponseCode {
    //请求成功
    SUCCESS(0, "SUCCESS"),
    //请求失败
    ERROR(1, "ERROR"),
    //未登录
    NEED_LOGIN(10, "NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int getCode(){
        return  code;
    }
    public String getDesc(){
        return desc;
    }
}

