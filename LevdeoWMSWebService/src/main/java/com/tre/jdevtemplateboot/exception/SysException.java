package com.tre.jdevtemplateboot.exception;

import com.tre.jdevtemplateboot.common.constant.SysConstantErr;

import java.io.Serializable;
import java.lang.reflect.Array;

/**
 * @description: JDev自定义系统异常
 * @author: JDev
 * @create: 2018-11-22 09:58
 **/
public class SysException extends Exception {

    private String code;

    private String msg;

    private Object data;


    public SysException(String errorCode, String[] data) {
        super();
        this.code = errorCode;
        this.msg = SysConstantErr.SERVER_ERROR_MSG;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
