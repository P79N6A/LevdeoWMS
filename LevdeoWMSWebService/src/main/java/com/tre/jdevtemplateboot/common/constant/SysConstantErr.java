package com.tre.jdevtemplateboot.common.constant;

/**
 * @description: 系统异常常量定义
 * @author: JDev
 * @create: 2018-11-13 14:14
 **/
public class SysConstantErr {

    private static String PRE_CODE = "1";
    public static String SERVER_ERROR_MSG = "SERVER_ERROR";

    public static String SERVER_ERROR_CODE = PRE_CODE + "001";
    public static String SERVER_ERROR_DATA[] = {"服务器端错误发生!"};

    public static String SERVER_ERROR_DUPLICATE_KEY_CODE = PRE_CODE + "002";
    public static String SERVER_ERROR_DUPLICATE_KEY_DATA[] = {"主键冲突!"};

    public static String SERVER_ERROR_NO_JURISDICTION_CODE = PRE_CODE + "003";
    public static String SERVER_ERROR_NO_JURISDICTION_DATA[] = {"没有访问权限!"};
}
