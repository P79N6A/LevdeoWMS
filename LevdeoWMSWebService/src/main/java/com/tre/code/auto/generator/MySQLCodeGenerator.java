package com.tre.code.auto.generator;


import com.baomidou.mybatisplus.annotation.DbType;
import com.tre.generate.generatecode.Generator_Auto;

public class MySQLCodeGenerator {

    public static void main(String[] args) {

        //生成数据库中所有表实体
        try {
            new Generator_Auto
                    ("D://AutoGenerator//mysql"
                    , DbType.MYSQL
                    ,"com.mysql.jdbc.Driver",
                    "jdbc:mysql://172.17.5.87:3306/jdevtemplate?characterEncoding=utf8"
                    ,"root"
                    ,"");
        } catch (Exception e) {
            e.printStackTrace();
        }

		/*
		//生成数据库中指定表实体
		String[] byTables = {"student", "sys_log", "sys_menu_test"};
		try {
			new Generator_Auto("D://AutoGenerator//mysql"
					,DbType.MYSQL
					,"com.mysql.jdbc.Driver",
					"jdbc:mysql://xxx.xxx.xxx.xxx:3306/jdevtemplate?characterEncoding=utf8"
					,""
					,""
					,byTables);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
    }

}
