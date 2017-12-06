package com.jxlg.app.store.common;


import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 这个类用于json的返回请求,对响应的封装,正确和错误响应的
 * @author zhouboxi
 * @create 2017-11-27 18:56
 **/
public class ServerResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status){
        this.status=status;
    }
    private ServerResponse(int status,T data){
        this.status=status;
        this.data=data;
    }
    private ServerResponse(int status,String msg){
        this.status=status;
        this.msg=msg;
    }
    private ServerResponse(int status,String msg,T data){
        this.status=status;
        this.msg=msg;
        this.data=data;
    }

    //json序列化都是get方法所以如果不想序列化加上
    @JSONField(serialize=false)
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS.getCode();
    }

    public int getStatus(){
        return  status;
    }
    public T getData(){
        return  data;
    }
    public String getMsg(){
        return msg;
    }

    public static <T> ServerResponse createBySuccess(){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServerResponse createBySuccessMsg(String msg){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServerResponse createBySuccess(T data){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerResponse createBySuccess(String msg,T data){
        return  new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerResponse createByERROR(){
        return  new ServerResponse<T>(ResponseCode.ERROR.getCode());
    }
    public static <T> ServerResponse createByERRORMsg(String errorMsg){
        return  new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMsg);
    }

    public static <T> ServerResponse createByERRORMsg(int errorCode,String errorMsg){
        return  new ServerResponse<T>(errorCode,errorMsg);
    }

}

