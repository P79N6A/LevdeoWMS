package com.tre.jdevtemplateboot.exception;

import com.tre.jdevtemplateboot.common.constant.SysConstantErr;
import com.tre.jdevtemplateboot.common.constant.SysConstantValidatorErr;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.LLoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 全局异常处理
 * @author: JDev
 * @create: 2018-11-22 10:02
 **/
@RestControllerAdvice
public class SysExceptionHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(SysExceptionHandler.class);

    private Logger logBusiness = LLoggerUtils.Logger(LLoggerUtils.LogFileName.Business);

    @ExceptionHandler
    public ResponseResult processException(Exception ex, HttpServletRequest request, HttpServletResponse response){

        SysException sysException = null;

        if (ex instanceof DuplicateKeyException) {
            sysException = new SysException(SysConstantErr.SERVER_ERROR_DUPLICATE_KEY_CODE, SysConstantErr.SERVER_ERROR_DUPLICATE_KEY_DATA);
            LOGGER.error("异常:" + ex.getMessage(), ex);
            return  ResponseResult.buildError(sysException);
        }
        else if(ex instanceof  SysExceptionValidator){
            return  ResponseResult.buildCheck(SysConstantValidatorErr.VALIDATOR_ERROR_CODE,
                    SysConstantValidatorErr.VALIDATOR_ERROR_MSG,((SysExceptionValidator) ex).getData());
        }
        else if (ex instanceof SysException) {

            sysException = (SysException) ex;
            logBusiness.info("--------------Response----------------");
            logBusiness.info("异常:" + sysException.getMsg());
            logBusiness.info("--------------Response----------------");

            sysException = new SysException(SysConstantErr.SERVER_ERROR_CODE, SysConstantErr.SERVER_ERROR_DATA);
            LOGGER.error("异常:" + ex.getMessage(), ex);
            return  ResponseResult.buildError(sysException);
        }
        else {
            // 如果抛出的不是系统自定义异常则重新构造一个系统错误异常。
            sysException = new SysException(SysConstantErr.SERVER_ERROR_CODE, SysConstantErr.SERVER_ERROR_DATA);
            LOGGER.error("异常:" + ex.getMessage(), ex);
            return  ResponseResult.buildError(sysException);
        }
    }

}
