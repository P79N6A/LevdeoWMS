package com.tre.jdevtemplateboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tre.jdevtemplateboot.domain.po.SysWarehouse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author JDev
 * @since 2018-12-20
 */
public interface ISysWarehouseService extends IService<SysWarehouse> {

    /**
     * @param code
     * @return
     */
    Boolean delete(String code);

    /**
     * @param house
     */
    void saveHouse(SysWarehouse house);

}
