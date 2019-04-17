package com.tre.jdevtemplateboot.common.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @description: 自定义属性
 * @author: JDev
 * @create: 2018-11-16 15:15
 **/
@Component
@ConfigurationProperties(prefix = "com.tre.jdev")
public class PropertiesVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * @Description:  #操作log是否存储到DB中( 0:不开启，1：开启)
    **/
    @Value("operator-log")
    private String operatorLog;

    public String getOperatorLog() {
        return operatorLog;
    }

    public void setOperatorLog(String operatorLog) {
        this.operatorLog = operatorLog;
    }
}
