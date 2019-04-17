package com.tre.jdevtemplateboot.common.constant;

/**
 * @description: JWT 全局异常信息
 * @author: JDev
 * @create: 2018-12-10 09:00
 **/
public class SysConstantJwtErr {

    private static String PRE_CODE = "2";
    public static String TOKEN_ERROR_MSG = "TOKEN_ERROR";

    public static String TOKEN_ERROR_CODE = PRE_CODE + "001";
    public static String TOKEN_ERROR_DATA[] = {"token 验证失败!"};

    public static String TOKEN_ERROR_MISS_CODE = PRE_CODE + "002";
    public static String TOKEN_ERROR_MISS_DATA[] = {"Missing or invalid Authorization header!"};

    public static String TOKEN_ERROR_EXPIRED_CODE = PRE_CODE + "003";
    public static String TOKEN_ERROR_EXPIRED_DATA[] = {"token expired!"};

    public static String TOKEN_ERROR_INVALID_CODE = PRE_CODE + "004";
    public static String TOKEN_ERROR_INVALID_DATA[] = {"token invalid!"};


}
