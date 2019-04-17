package com.tre.jdevtemplateboot.config;

import com.tre.jdevtemplateboot.web.controller.QuartzJob;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {
    //同步 物料主数据、客户主数据 job执行时间每个整点执行一次
    @Value("${my.sapSyncWmsScheduleconn}")
    private String sapSyncWmsScheduleconn;


//    @Bean
//    public JobDetail QuartzDetail(){
//        return JobBuilder.newJob(QuartzJob.class).withIdentity("quartzJob").storeDurably().build();
//    }
//
//    @Bean
//    public Trigger QuartzTrigger() throws SchedulerException {
//        Trigger trigger = TriggerBuilder.newTrigger().forJob(QuartzDetail())
//                .withDescription("分配发货单")    //触发器的描述
//                .withIdentity("doSendOutTrigger", "group1")  //触发器的名称, 触发器的组
//                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleconn)) //job执行时间
//                .build();
//
//        return trigger;
//    }

    /**
     * sap同步：物料主数据、客户主数据
     */
    @Bean(name="sapSyncWmsJobDetail")
    public MethodInvokingJobDetailFactoryBean sapSyncMaterielJobDetail(QuartzJob quartzJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        //是否并发执行
        //例如每5s执行一次任务，但是当前任务还没有执行完，就已经过了5s了，
        //如果此处为true，则下一个任务会执行，如果此处为false，则下一个任务会等待上一个任务执行完后，再开始执行
        jobDetail.setConcurrent(false);
        //设置任务的名字
        jobDetail.setName("sapSyncWmsJobDetail");
        //设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
        jobDetail.setGroup("group1");
        //需要执行的类
        jobDetail.setTargetObject(quartzJob);
        //需要执行的类(方法上必须加static)
        //jobDetail.setTargetClass(QuartzJob.class);
        //需要执行的方法
        jobDetail.setTargetMethod("sapSyncWms");
        return jobDetail;
    }

    /**
     * sap同步：物料主数据、客户主数据
     */
    @Bean(name="sapSyncWmsTrigger")
    public CronTriggerFactoryBean sapSyncWmsTrigger(@Qualifier("sapSyncWmsJobDetail") MethodInvokingJobDetailFactoryBean sapSyncWmsJobDetail) {
        CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
        trigger.setJobDetail(sapSyncWmsJobDetail.getObject());
        //job执行时间
        trigger.setCronExpression(sapSyncWmsScheduleconn);
        //trigger的name
        trigger.setName("sapSyncWmsTrigger");
        return trigger;
    }

    /**
     * Details：定义quartz调度工厂
     */
    @Bean
    public SchedulerFactoryBean schedulerFactory(Trigger doSendOutTrigger, Trigger updateStockPositionTrigger) {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        bean.setOverwriteExistingJobs(true);
        // 延时启动，应用启动1秒后
        bean.setStartupDelay(1);
        // 注册触发器
        bean.setTriggers(doSendOutTrigger, updateStockPositionTrigger);
        return bean;
    }
}
