package com.tre.jdevtemplateboot.service;

import com.tre.jdevtemplateboot.domain.po.WarehousingSchedule;

import java.util.List;
import java.util.Map;

/**
 * @author JDev
 * @since 2018-12-19
 */
public interface SysJobService {
    /**
     * 分配发货单：获取各个仓库待分配的发货单号，进行分配
     */
     Map<String, Object> doSendOut();

    /**
     * warehousingSchedule表中，所有stockPosition（库位号）为null的记录，
     * 根据库位优先级规则，更新stockPosition（库位号）字段。
     */
     Map<String, Object> updateStockPosition();

    /**
     * 获取ws表对应的数据进行更新库位号
     * @param warehousingSchedules
     * @return
     */
     int updateStockPosition(List<WarehousingSchedule> warehousingSchedules);
}
