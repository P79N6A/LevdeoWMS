package com.tre.jdevtemplateboot.common.util;

import com.tre.jdevtemplateboot.exception.SysExceptionValidator;
import org.slf4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 实体对象bean验证
 * @author: JDev
 * @create: 2018-12-03 14:05
 **/
public class LPojoValidatorUtils {

    private static Logger logger = LLoggerUtils.Logger(LLoggerUtils.LogFileName.Business);


    /**
    * @Description: 校验对象
    * @Param: [errors]
    * @return: void
    * @Author: JDev
    * @Date: 2018/12/03
    **/
    public  static void  pojoValidator(Errors errors) throws SysExceptionValidator{
        Map<String,Object> errMap = new HashMap<>();
        if(errors.hasErrors()){
            errors.getAllErrors().stream().forEach(
                    error -> {
                        FieldError fe = (FieldError)error;
                        errMap.put(fe.getField(),fe.getDefaultMessage());
                        logger.info("错误字段：{} -> 错误信息：{}", fe.getField(),fe.getDefaultMessage());
                    }
            );
        }
        throw  new SysExceptionValidator(errMap);
    }
}
