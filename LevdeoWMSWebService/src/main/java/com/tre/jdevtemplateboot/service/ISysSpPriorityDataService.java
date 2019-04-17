package com.tre.jdevtemplateboot.service;

import com.tre.jdevtemplateboot.domain.po.SysSpPriorityData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author JDev
 * @since 2018-12-24
 */
public interface ISysSpPriorityDataService extends IService<SysSpPriorityData> {

    /**
     * @param vin
     * @return
     */
    public List<Map<String, Object>> getStockPosition(String vin);

    /**
     * @param vinList
     * @return
     */
    public String updateStockPosition(List<String> vinList);
}
