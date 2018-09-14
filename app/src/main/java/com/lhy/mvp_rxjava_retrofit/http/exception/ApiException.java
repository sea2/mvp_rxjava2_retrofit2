package com.lhy.mvp_rxjava_retrofit.http.exception;

/**
 *
 * @author Administrator
 * @date 2018/3/25
 */

public class ApiException extends RuntimeException{
    private int code;
    private String msg;
    public ApiException(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public ApiException(){}


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
