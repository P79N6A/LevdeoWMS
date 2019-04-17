package com.tre.code.auto.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.tre.generate.generatecode.Generator_Auto;

public class MsSQLCodeGenerator {

    /**
     * 代码生成
     * <p>
     * Generator 类
     * 参数1   生成路径
     * 参数2   数据库类型 （支持MYSQL，ORACEL，POSTGRE，SQLSERVER ）
     * 参数3 DriverName
     * 参数4   数据库主机Url + 端口 + 数据库名
     * 参数5   数据库用户
     * 参数6   数据库密码
     * 参数7 生成命名策略枚举(enum)
     * </p>
     */
    public static void main(String[] args) {

        //SQLSERVER


        try {
            new Generator_Auto(
                    "D://AutoGenerator//sqlserver"
                    , DbType.SQL_SERVER
                    ,"com.microsoft.sqlserver.jdbc.SQLServerDriver"
                    ,"jdbc:sqlserver://172.17.4.210:1433;DatabaseName=wms"
                    ,"wmspj"
                    ,"wmspj"
            ,new String[]{"outInvoiceDetails"});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
