package com.tre.code.auto.generator;


import com.tre.generate.generatecode.Generator_Postgresql_Auto;

public class PostgresCodeGenerator {

    public static void main(String[] args) {


		/*
		//生成数据库中所有表实体
		try {
			 new Generator_Postgresql_Auto("D://AutoGenerator//PostgreSql"
                    ,"jdbc:postgresql://172.18.5.60:5432/jdevtemplatepostgre"
                    ,"public"
                    ,"postgres"
                    ,"tcloud");
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/

        //生成数据库中指定表实体
        String[] byTables = {"u"};
        try {
            new Generator_Postgresql_Auto("D://AutoGenerator//PostgreSql"
                    ,"jdbc:postgresql://172.18.5.60:5432/jdevtemplatepostgre"
                    ,"public"
                    ,"postgres"
                    ,"tcloud"
                    ,byTables
                    ,true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
