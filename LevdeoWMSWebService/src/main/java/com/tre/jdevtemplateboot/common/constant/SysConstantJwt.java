package com.tre.jdevtemplateboot.common.constant;

/**
 * @description: Token 信息定义
 * @author: JDev
 * @create: 2018-12-10 09:11
 **/
public class SysConstantJwt {

    public static String JWT_AUTHOR_TYPE = "Bearer ";

    //token有效时间,单位毫秒, 2小时候后token失效
    public static long JWT_TTL = 10*60*60*1000;

    //密匙
    public static String JWT_SECERT = "46cc793c53dc451b8a4fe2cd0bb008471";
}
