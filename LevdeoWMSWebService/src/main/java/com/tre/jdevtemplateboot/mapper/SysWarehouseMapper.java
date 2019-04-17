package com.tre.jdevtemplateboot.mapper;

import com.tre.jdevtemplateboot.domain.po.SysWarehouse;
import com.tre.jdevtemplateboot.web.pojo.ChargParameterBean;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2018-12-20
 */
public interface SysWarehouseMapper extends BaseMapper<SysWarehouse> {
    /**
     * @param pb
     * @return
     */
    List<Map<String, Object>> searchAll(ChargParameterBean pb);

    /**
     * @param house
     */
    void update(SysWarehouse house);

    /**
     * @param code
     * @return
     */
    SysWarehouse info(String code);

    /**
     * @param code
     * @return
     */
    Integer selectCount(String code);

    /**
     * @param code
     */
    void delete(String code);

    /**
     * @return
     */
    String selectMaxCode();

    /**
     * @param house
     */
    void saveHouse(SysWarehouse house);

    /**
     * @return
     */
    List<Map<String, Object>> getWareHouseList();

}
