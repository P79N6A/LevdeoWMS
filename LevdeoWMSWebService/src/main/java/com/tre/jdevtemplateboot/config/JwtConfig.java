package com.tre.jdevtemplateboot.config;

import com.tre.jdevtemplateboot.web.annotation.PassToken;
import com.tre.jdevtemplateboot.web.filter.JwtFilter;

import java.lang.reflect.Method;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description: JSON WEB TOKEN 配置
 * @author: JDev
 * @create: 2018-12-07 15:59
 **/
@Configuration
public class JwtConfig {

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter() throws Exception {
        final FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtFilter());
        //注册token拦截路径
        registClass(registrationBean);
        return registrationBean;
    }

    /**
     * 	注册token拦截路径
     * @param registrationBean
     * @author SongGuiFan
     * @throws Exception 
     */
    private void registClass(FilterRegistrationBean<JwtFilter> registrationBean) throws Exception {
    	boolean hasRequestMapping;//是否有@RequestMapping
    	boolean hasPassToken;//是否有@PassToken
    	//包扫描
        ClassPathScanningCandidateComponentProvider provider = 
        		new ClassPathScanningCandidateComponentProvider(true);
        Set<BeanDefinition> beanDefinitionSet = 
        		provider.findCandidateComponents("com.tre.jdevtemplateboot.web.controller");
        
        for (BeanDefinition beanDefinition : beanDefinitionSet) {
        	
        	//类扫描
        	Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
        	hasRequestMapping = clazz.isAnnotationPresent(RequestMapping.class);
            hasPassToken = clazz.isAnnotationPresent(PassToken.class);
            
            //过滤掉没有绑定url路径或有@PassToken的类
            if(!hasRequestMapping || hasPassToken) {
            	continue;
            }
            RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
            
            //修正不规范的RequestMapping注解
            String url1 = annotation.value()[0];
            if (!url1.startsWith("/")) {
                url1 = "/" + url1;
            }
            if (url1.endsWith("/")) {
                url1 = url1.substring(0, url1.length() - 1);
            }
            
            //方法扫描
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
            	
            	//过滤掉有@PassToken的方法
                if(method.isAnnotationPresent(PassToken.class)) {
                	continue;
                }
                
                //方法路径
                String url2;
                
                //检测是否有@RequestMapping
                if(method.isAnnotationPresent(RequestMapping.class)) {
                	url2 = method.getAnnotation(RequestMapping.class).value()[0];
            	}else if(method.isAnnotationPresent(PostMapping.class)) {
            		url2 = method.getAnnotation(PostMapping.class).value()[0];
            	}else if(method.isAnnotationPresent(GetMapping.class)) {
            		url2 = method.getAnnotation(GetMapping.class).value()[0];
            	}else {
            		continue;
            	}
                
                //修正不规范的RequestMapping注解
                if (!url2.startsWith("/")) {
                    url2 = "/" + url2;
                }
                if (url2.endsWith("/")) {
                    url2 = url2.substring(0, url2.length() - 1);
                }
                
                //注册token拦截器
                registrationBean.addUrlPatterns(url1 + url2);
            }
		}
    }
}
