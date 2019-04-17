package com.tre.jdevtemplateboot.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author JDev
 * @since 2018-12-15
 */
public interface SysJobMapper {
    /**
     * 插入sysJobLog
     */
    void insertSysJobLog(
            @Param("jobName") String jobName,
            @Param("runtime") String runtime,
            @Param("result") String result,
            @Param("successCnt") int successCnt,
            @Param("failCnt") int failCnt,
            @Param("success") String success,
            @Param("fail") String fail,
            @Param("exception") String exception,
            @Param("userCode") String userCode
    );

    /**
     * outInvoiceHead 中查找所有 00:待分配 的发货单
     */
    List<Map<String, Object>> searchAssignedInvoice();

    /**
     * 库存表中按时间从早到晚，取得待分配车辆
     */
    List<Map<String, Object>> searchAssignedStocks(Map<String, Object> map);

    /**
     * 库存表中，根据物料编码matCode，取得待分配(03:在库、05:充电)、02:未锁定 的车辆数量
     */
    int searchAssignedStocksCnt(Map<String, Object> map);

    /**
     * 库存表中，根据物料编码matCode，按时间从早到晚，取得待分配(03:在库、05:充电)、02:未锁定 车辆，插入到发货单详情表outInvoiceDetails
     */
    void insertAssignedStocks(Map<String, Object> map);

    /**
     * 更新 outInvoiceHead 发货单状态为01：待发货
     */
    void updateAssignedInvoice(Map<String, Object> map);

    /**
     * 更新 stock 库存表中取得的待分配车辆状态 03：待分配->04：待发货
     */
    void updateAssignedStocks(Map<String, Object> map);
}
