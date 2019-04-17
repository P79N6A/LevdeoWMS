package com.tre.jdevtemplateboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 读取配置文件，Map<String, Object>格式
 */
@Component
@ConfigurationProperties(prefix="map")
public class MapConfiguration {
    private List<Map<String, Object>> sapCallJavaFunctions = new ArrayList<Map<String, Object>>();

    public List<Map<String, Object>> getSapCallJavaFunctions() {
        return sapCallJavaFunctions;
    }

    public void setSapCallJavaFunctions(List<Map<String, Object>> sapCallJavaFunctions) {
        this.sapCallJavaFunctions = sapCallJavaFunctions;
    }
}