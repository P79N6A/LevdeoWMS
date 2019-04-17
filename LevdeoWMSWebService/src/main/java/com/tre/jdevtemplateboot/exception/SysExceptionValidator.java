package com.tre.jdevtemplateboot.exception;

/**
 * @description: 系统参数验证bean
 * @author: JDev
 * @create: 2018-12-03 14:01
 **/
public class SysExceptionValidator extends Exception {

    private Object data;

    public SysExceptionValidator(Object data) {
        this.data = data;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

}
