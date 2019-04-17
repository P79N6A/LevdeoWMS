package com.tre.jdevtemplateboot.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: 共通Log工具定义
 * @author: JDev
 * @create: 2018-11-14 09:28
 **/
public class LLoggerUtils {

    public static <T> Logger Logger(Class<T> clazz){
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * @Description: 打印到指定的文件下
     * @Param: [desc]  日志文件名称
     * @return:
     * @Author: JDev
     * @Date: 2018/11/14
     **/
    public  static Logger Logger(LogFileName desc) {
        return LoggerFactory.getLogger(desc.getLogFileName());
    }

    public enum LogFileName{

        //配置到logback-boot.xml中的logger name="business"
        Business("business");

        private String logFileName;

        LogFileName(String fileName) {
            this.logFileName = fileName;
        }

        public String getLogFileName() {
            return logFileName;
        }

        public void setLogFileName(String logFileName) {
            this.logFileName = logFileName;
        }

        public static LogFileName getAwardTypeEnum(String value) {
            LogFileName[] arr = values();
            for (LogFileName item : arr) {
                if (null != item && LStringUtils.isNotBlank(item.logFileName)) {
                    return item;
                }
            }
            return null;
        }
    }
}
