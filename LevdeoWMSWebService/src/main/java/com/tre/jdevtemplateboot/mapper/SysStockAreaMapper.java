package com.tre.jdevtemplateboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tre.jdevtemplateboot.domain.po.SysStockArea;
import com.tre.jdevtemplateboot.web.pojo.StockPositionParameterBean;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库区
 * </p>
 *
 * @author JDev
 * @since 2018-12-20
 */
public interface SysStockAreaMapper extends BaseMapper<SysStockArea> {

    /**
     * 查询所有库区
     * @param pb
     * @return
     */
    List<Map<String, Object>> searchAll(StockPositionParameterBean pb);
    
    /**
     * 根据仓库查询库区
     * @param warehouseCode
     * @return
     */
    List<SysStockArea> selectListWithWarehouse(String warehouseCode);
    
    /**
     * 获取指定库区详情
     * @param code
     * @return
     */
    SysStockArea info(String code);

    /**
     * 获取最大的库区Code
     * @return
     */
    String selectMaxCode();

}
