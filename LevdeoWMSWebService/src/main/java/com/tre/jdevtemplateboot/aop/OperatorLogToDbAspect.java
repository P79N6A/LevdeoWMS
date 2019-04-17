package com.tre.jdevtemplateboot.aop;

import com.tre.jdevtemplateboot.common.pojo.PropertiesVO;
import com.tre.jdevtemplateboot.common.util.LHttpUtils;
import com.tre.jdevtemplateboot.common.util.LIPUtils;
import com.tre.jdevtemplateboot.common.util.LJsonUtils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;


/**
 * @description: 操作log存储到数据库
 * @author: JDev
 * @create: 2018-11-16 14:42
 **/
/*
@Aspect
@Component
public class OperatorLogToDbAspect {

    @Resource
    private SysLogMapper sysLogMapper;

    @Resource
    private PropertiesVO propertiesVO;


    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void logPointCut() {

    }

    @Before("logPointCut()")
    public void saveSysLog(JoinPoint joinPoint) throws Exception{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        if("0".equals(propertiesVO.getOperatorLog())) {
            return;
        }

        HttpServletRequest request = LHttpUtils.getHttpServletRequest();
        SysLog tsyslog = new SysLog();
        Object[] args = null;

        //请求的参数
        String reqMethod = request.getMethod();
        if("GET".equals(reqMethod.toUpperCase())) {
            tsyslog.setParams(request.getQueryString());
        }else {
            args = joinPoint.getArgs();
            if(args !=null && args.length>0) {
                String params = LJsonUtils.objectToJson(args[0]);
                tsyslog.setParams(params);
            }
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        tsyslog.setMethod(className + "." + methodName + "()");

        //获取IP
        tsyslog.setIp(LIPUtils.getIpAddr(request));

        tsyslog.setCreateDate(new Date());

        //save log to table
        sysLogMapper.insert(tsyslog);

    }
}
*/
