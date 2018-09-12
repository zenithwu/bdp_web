package com.stylefeng.guns.core.common.exception;


import com.stylefeng.guns.core.exception.ServiceExceptionEnum;

public class BizException implements ServiceExceptionEnum {

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public BizException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

    @Override
    public String getMessage() {
        return message;
    }



}
