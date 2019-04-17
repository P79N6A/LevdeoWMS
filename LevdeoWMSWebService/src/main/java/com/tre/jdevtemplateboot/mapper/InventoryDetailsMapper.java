package com.tre.jdevtemplateboot.mapper;

import com.tre.jdevtemplateboot.domain.po.InventoryDetails;
import com.tre.jdevtemplateboot.web.pojo.InventoryParameterBean;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 盘点详情 Mapper 接口
 *
 * @author SongGuiFan
 */
public interface InventoryDetailsMapper extends BaseMapper<InventoryDetails> {

    /**
     * 新建盘点计划-保存计划详情表
     *
     * @param map 盘点实体
     * @return
     */
    int saveInventoryDetails(Map<String,Object> map);

    /**
     * 计划详情-查询盘点详情列表
     *
     * @param params 盘点实体
     * @return
     */
    List<Map<String, Object>> getPlanDetailsList(InventoryParameterBean params);

    /**
     * 核对PAD数据
     *
     * @param operator 操作人
     * @param warehouseCode 仓库编码
     * @return
     */
    List<String> checkPDA(@Param("operator")String operator, @Param("warehouseCode")String warehouseCode);

    /**
     * 批量更新盘点详情
     *
     * @param planId   盘点单号
     * @param operator 操作人
     * @return
     */
    void bachUpdateDetails(@Param("planId") String planId, @Param("operator") String operator);

    /**
     * 获取错误信息
     *
     * @param code 错误代码
     * @return
     */
    String getMsg(String code);

    /**
     * 删除计划外车辆
     * @param planId 计划单号
	 * @param vin VIN码
     */
	void deleteByVin(@Param("planId")String planId, @Param("vin")String vin);
}
