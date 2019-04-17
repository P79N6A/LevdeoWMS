package com.tre.jdevtemplateboot.config;

import com.tre.jdevtemplateboot.web.interceptor.JDevRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;



/**
 * @description: Web服务配置中心
 * @author: JDev
 * @create: 2018-11-14 11:25
 **/
@Configuration
public class WebMvcAppConfig  extends WebMvcConfigurationSupport {

    @Autowired
    private JDevRequestInterceptor jDevRequestInterceptor;

    /**
    * @Description:  请求服务资源的认证拦截
    * @Param: [registry]
    * @return: void
    * @Author: JDev
    * @Date: 2018/11/14
    **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //请求服务资源的认证拦截
        InterceptorRegistration ir = registry.addInterceptor(jDevRequestInterceptor);
            ir.addPathPatterns("/**")
                    .excludePathPatterns("/user/login");
    }

    /**
     * @Description:  访问静态资源
     * @Param: [registry]
     * @return: void
     * @Author: JDev
     * @Date: 2018/12/24
     **/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        if(!registry.hasMappingForPattern("/music/**")){
            registry.addResourceHandler("/music/**").addResourceLocations("classpath:/music/");
        }
        super.addResourceHandlers(registry);
    }
}
