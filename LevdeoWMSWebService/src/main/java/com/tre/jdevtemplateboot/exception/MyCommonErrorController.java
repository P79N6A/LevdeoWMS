package com.tre.jdevtemplateboot.exception;

import com.tre.jdevtemplateboot.common.constant.SysConstantErr;
import com.tre.jdevtemplateboot.common.constant.SysConstantJwtErr;
import org.springframework.boot.autoconfigure.web.ErrorProperties;

import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 统一处理filter抛出的token相关的异常 返回给前端标准格式的json和装填码
 * @author: JDev
 * @create: 2018-12-07 16:09
 **/
@RestController
public class MyCommonErrorController extends BasicErrorController {

    //统一处理filter抛出的token相关的异常 返回给前端标准格式的json和装填码
    public MyCommonErrorController() {
        super(new DefaultErrorAttributes(), new ErrorProperties());
    }

    @Override
    @RequestMapping(produces = {"application/json"})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        //加入跨域相关内容
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Expose-Headers", "X-Total-Count");
        response.setHeader("Access-Control-Allow-Headers", "origin, x-requested-with, x-http-method-override, content-type, Authentication, Authorization, hospital");
        response.setHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS, HEAD, PATCH");

        HttpStatus status = this.getStatus(request);
        Map<String, Object> errorAttributes = this.getErrorAttributes(request, true);
        String message = (String)errorAttributes.get("message");

        return new ResponseEntity(getTokenErrEntity(message), status);
    }

    /** 
    * @Description:  获取token code和data
    * @Param: [msg] 
    * @return: java.util.Map<java.lang.String,java.lang.Object> 
    * @Author: JDev
    * @Date: 2018/12/10 
    **/
    private Map<String, Object> getTokenErrEntity(String code){
        Map<String, Object> tokenErr = new HashMap<>();

        if(SysConstantJwtErr.TOKEN_ERROR_MISS_CODE.equals(code)){
            tokenErr.put("code",SysConstantJwtErr.TOKEN_ERROR_MISS_CODE);
            tokenErr.put("msg",SysConstantJwtErr.TOKEN_ERROR_MSG);
            tokenErr.put("data",SysConstantJwtErr.TOKEN_ERROR_MISS_DATA);
        }
        else if(SysConstantJwtErr.TOKEN_ERROR_EXPIRED_CODE.equals(code)){
            tokenErr.put("code",SysConstantJwtErr.TOKEN_ERROR_EXPIRED_CODE);
            tokenErr.put("msg",SysConstantJwtErr.TOKEN_ERROR_MSG);
            tokenErr.put("data",SysConstantJwtErr.TOKEN_ERROR_EXPIRED_DATA);
        }
        else if(SysConstantJwtErr.TOKEN_ERROR_INVALID_CODE.equals(code)){
            tokenErr.put("code",SysConstantJwtErr.TOKEN_ERROR_INVALID_CODE);
            tokenErr.put("msg",SysConstantJwtErr.TOKEN_ERROR_MSG);
            tokenErr.put("data",SysConstantJwtErr.TOKEN_ERROR_INVALID_DATA);
        }else
        {
            tokenErr.put("code",SysConstantJwtErr.TOKEN_ERROR_CODE);
            tokenErr.put("msg",SysConstantJwtErr.TOKEN_ERROR_MSG);
            tokenErr.put("data",SysConstantJwtErr.TOKEN_ERROR_DATA);
        }

        return  tokenErr;
    }

}
