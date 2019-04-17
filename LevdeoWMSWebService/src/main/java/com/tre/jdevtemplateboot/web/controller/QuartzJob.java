package com.tre.jdevtemplateboot.web.controller;

import com.tre.jdevtemplateboot.service.SAPInterfaceService;
import com.tre.jdevtemplateboot.web.annotation.PassToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Date;

//public class QuartzJob extends QuartzJobBean {
//
//    @Override
//    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//        System.out.println("quartz task Test 99999999999999999999999999999999");
//    }
//}

@Component
@Configurable
@EnableScheduling
@PassToken
public class QuartzJob {
    @Autowired
    private SAPInterfaceService sapInterfaceService;

    /**
     * sap同步：物料主数据、客户主数据
     */
    public void sapSyncWms(){
        Date date = new Date();
        
        //同步物料主数据
        sapInterfaceService.sapSyncWms_mainMat(date);
        //同步客户主数据
        sapInterfaceService.sapSyncWms_mainCustomers(date);
    }
}

