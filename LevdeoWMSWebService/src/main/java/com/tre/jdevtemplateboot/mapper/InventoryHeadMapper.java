package com.tre.jdevtemplateboot.mapper;

import com.tre.jdevtemplateboot.common.pojo.ParameterBean;
import com.tre.jdevtemplateboot.domain.po.InventoryHead;
import com.tre.jdevtemplateboot.web.pojo.InventoryParameterBean;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 盘点计划 Mapper 接口
 *
 * @author SongGuiFan
 */
public interface InventoryHeadMapper extends BaseMapper<InventoryHead> {
    /**
     * 盘点计划-查询盘点计划列表
     *
     * @param params Inventory对象
     * @return
     */
    List<Map<String, Object>> getPlanList(InventoryParameterBean params);

    /**
     * 新建盘点计划-查询在库车辆列表
     *
     * @param params Parameter对象
     * @return
     */
    List<Map<String, Object>> getAllCarList(ParameterBean params);

    /**
     * 新建盘点计划-保存计划主表
     *
     * @param map 盘点实体
     * @return
     */
    int saveInventory(Map<String,Object> map);

    /**
     * 计划详情- 查询全部人员列表
     *
     * @return
     */
    List<Map<String, Object>> getAllUserList(String warehouseCode);

}
