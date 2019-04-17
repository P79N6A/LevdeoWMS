package com.tre.jdevtemplateboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tre.jdevtemplateboot.domain.po.WarehousingScheduleWork;

import java.util.List;

/**
 * <p>
 * SAP调JAVA，入库交接单
 * </p>
 *
 * @author JDev
 * @since 2019-02-19
 */
public interface IWarehousingScheduleWorkService extends IService<WarehousingScheduleWork> {
    /**
     * SAP调JAVA，入库交接单
     * @param wsWorks
     */
    String rfcStorageRec(List<WarehousingScheduleWork> wsWorks);
}
