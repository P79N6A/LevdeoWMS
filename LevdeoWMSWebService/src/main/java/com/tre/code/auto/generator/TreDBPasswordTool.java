package com.tre.code.auto.generator;

import com.tre.jdevtemplateboot.common.util.LJasyptUtils;

import java.util.Scanner;

/**
 * @description: 数据库密码加密
 * @author: JDev
 * @create: 2018-11-15 13:41
 **/
public class TreDBPasswordTool {

    public static void main(String[] args) {
        System.out.println(LJasyptUtils.decyptPwd("czm6/0sHAsvYV/OfkpmsNg=="));
        System.out.println("请输入要加密的字符：");
        Scanner scanner = new Scanner(System.in);
        String password = scanner.next();
        try {
            System.out.println("加密后字符："+LJasyptUtils.encryptPwd(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
