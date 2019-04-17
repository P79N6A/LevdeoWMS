package com.tre.jdevtemplateboot;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.jdevtemplateboot.domain.po.SysParm;
import com.tre.jdevtemplateboot.mapper.SysParmMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysParamConstantGenerator {

    @Autowired
    private SysParmMapper sysParmMapper;

    /**
     * 代码生成
     */
    @Test
    public void sysParamConstantGenerator() {

        PrintWriter javaWriter = null;
        PrintWriter jsWriter = null;
        String TAB = "    ";
        try {
            Wrapper wrapper = new QueryWrapper();
            ((QueryWrapper) wrapper).orderByAsc(new String[]{"code", "subCode"});
            List<SysParm> plist = sysParmMapper.selectList(wrapper);

            /****java****/
            javaWriter = new PrintWriter("D://AutoGenerator//SysParamConstant.java", "UTF-8");
            javaWriter.println("package com.tre.jdevtemplateboot.common.constant;");
            javaWriter.println();
            javaWriter.println("/**");
            javaWriter.println(" * 系统参数常量");
            javaWriter.println(" */");
            javaWriter.println("public final class SysParamConstant {");
            javaWriter.println(TAB + "//禁止实例化");
            javaWriter.println(TAB + "private SysParamConstant() {}");

            /****js****/
            jsWriter = new PrintWriter("D://AutoGenerator//sysParamConstant.js", "UTF-8");
            jsWriter.println("/**");
            jsWriter.println(" * 系统参数常量");
            jsWriter.println(" */");
            jsWriter.println("SysParamConstant = {");

            Map<String, String> hm = new HashMap<>();
            for (SysParm p : plist) {
                if (!hm.containsKey(p.getCode())) {
                    hm.put(p.getCode(), p.getCode());

                    /****java****/
                    javaWriter.println();
                    javaWriter.println(TAB + "/**");
                    javaWriter.println(TAB + " * " + p.getName());
                    javaWriter.println(TAB + " */");
                    javaWriter.println(TAB + "public static final String " + p.getNamep() + " = \"" + p.getCode() + "\";");

                    /****js****/
                    jsWriter.println();
                    jsWriter.println(TAB + "/**");
                    jsWriter.println(TAB + " * " + p.getName());
                    jsWriter.println(TAB + " */");
                    jsWriter.println(TAB + p.getNamep() + ": \"" + p.getCode() + "\",");
                }
                /****java****/
                javaWriter.println(TAB + "//" + p.getSubName());
                javaWriter.println(TAB + "public static final String " + p.getSubNamep() + " = \"" + p.getSubCode() + "\";");
                /****js****/
                jsWriter.println(TAB + "//" + p.getSubName());
                jsWriter.println(TAB + p.getSubNamep() + ": \"" + p.getSubCode() + "\",");
            }
            javaWriter.println("}");
            jsWriter.println("}");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (javaWriter != null)
                javaWriter.close();
            if (jsWriter != null)
                jsWriter.close();
        }
    }
}
