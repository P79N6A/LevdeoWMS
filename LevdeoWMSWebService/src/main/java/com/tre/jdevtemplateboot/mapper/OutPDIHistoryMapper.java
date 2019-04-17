package com.tre.jdevtemplateboot.mapper;

import com.tre.jdevtemplateboot.domain.po.OutPDIHistory;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * PDI检测不合格履历 Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2018-12-24
 */
public interface OutPDIHistoryMapper extends BaseMapper<OutPDIHistory> {

    /**
     * @param map
     * @return
     */
    List<Map<String, Object>> outPDITable(Map<String, Object> map);
}
