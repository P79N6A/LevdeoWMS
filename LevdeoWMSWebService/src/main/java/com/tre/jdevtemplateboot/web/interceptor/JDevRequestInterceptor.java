package com.tre.jdevtemplateboot.web.interceptor;

import com.tre.jdevtemplateboot.common.util.LLoggerUtils;
import org.slf4j.Logger;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: 请求服务资源的认证拦截
 * @author: JDev
 * @create: 2018-11-14 11:22
 **/
@Component
public class JDevRequestInterceptor implements HandlerInterceptor {

    private Logger logger = LLoggerUtils.Logger(LLoggerUtils.LogFileName.Business);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info(String.format("===自定义拦截器【%s】=== 处理器前方法",this.getClass().getSimpleName()));
        return true;
    }

    @Override
    public  void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        logger.info(String.format("===自定义拦截器【%s】=== 处理器后方法",this.getClass().getSimpleName()));
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        logger.info(String.format("===自定义拦截器【%s】=== 处理器完成方法",this.getClass().getSimpleName()));
    }

}
