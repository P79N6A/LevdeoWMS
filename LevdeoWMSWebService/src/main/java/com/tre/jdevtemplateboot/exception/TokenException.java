package com.tre.jdevtemplateboot.exception;

import java.lang.reflect.Array;

/**
 * @description: token 验证异常时
 * @author: JDev
 * @create: 2018-12-07 16:26
 **/
public class TokenException extends Exception{

    private String code;

    public TokenException() {
        super();
    }

    public TokenException(String code) {
        super(code);
        this.code = code;
    }

}
