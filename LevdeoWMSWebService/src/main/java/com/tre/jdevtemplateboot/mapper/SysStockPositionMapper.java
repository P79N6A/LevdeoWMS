package com.tre.jdevtemplateboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tre.jdevtemplateboot.domain.po.SysStockPosition;
import com.tre.jdevtemplateboot.web.pojo.StockPositionParameterBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库位Mapper 接口
 * </p>
 *
 * @author SongGuiFan
 * @since 2018-12-21
 */
public interface SysStockPositionMapper extends BaseMapper<SysStockPosition> {

    /**
     * 库位列表查询
     *
     * @param params 库位对象
     * @return
     */
    List<Map<String, Object>> getPositionList(StockPositionParameterBean params);

    /**
     * 查询仓库优先级
     *
     * @param warehouse
     * @return
     */
    Map<String, Object> getPrioritys(String warehouse);

    /**
     * 查询颜色优先级
     *
     * @return
     */
    List<Map<String, Object>> getColorPriority();

    /**
     * 查询品牌优先级
     *
     * @return
     */
    List<Map<String, Object>> getBrandPriority();

    /**
     * 查询系列优先级
     *
     * @return
     */
    List<Map<String, Object>> getSeriesPriority();

    /**
     * 查询仓库列表
     *
     * @return
     */
    List<Map<String, Object>> getWarehouse();

    /**
     * 查询库区列表
     *
     * @param warehouseCode
     * @return
     */
    List<Map<String, Object>> getStockArea(@Param("warehouseCode") String warehouseCode);
}
