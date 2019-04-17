package com.tre.jdevtemplateboot.mapper;

import com.tre.jdevtemplateboot.domain.po.StockLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库存日志 Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2018-12-15
 */
public interface StockLogMapper extends BaseMapper<StockLog> {

    /**
     * 查询
     */
    List<Map<String, Object>> searchLogData(Map<String, Object> map);
}
