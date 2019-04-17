package com.tre.jdevtemplateboot.aop;


import com.tre.jdevtemplateboot.common.util.LJsonUtils;
import com.tre.jdevtemplateboot.common.util.LLoggerUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@Order(1)
public class OperatorLogToFileAspect {

    private Logger logger = LLoggerUtils.Logger(LLoggerUtils.LogFileName.Business);
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    //申明一个切点 里面是 execution表达式
    @Pointcut("execution(public * com.tre.jdevtemplateboot.web.controller.*Controller.*(..))")
    private void controllerAspect() {
    }

    //请求method前打印内容
    @Before(value = "controllerAspect()")
    public void methodBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //打印请求内容
        logger.info("===============Request===============");
        logger.info("RequestURL:" + request.getRequestURL().toString());
        logger.info("Args:" + Arrays.toString(joinPoint.getArgs()));
        logger.info("===============Request===============");
    }

    //在方法执行完结后打印返回内容
    @AfterReturning(returning = "o", pointcut = "controllerAspect()")
    public void methodAfterReturing(Object o) {
        logger.info("--------------Response----------------");
        logger.info("Response Contents:" + LJsonUtils.objectToJson(o));
        logger.info("Spend Time:" + (System.currentTimeMillis() - startTime.get()));
        logger.info("--------------Response----------------");
    }

}
