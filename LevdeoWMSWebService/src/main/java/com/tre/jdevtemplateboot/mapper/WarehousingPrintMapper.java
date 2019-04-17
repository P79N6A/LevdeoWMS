package com.tre.jdevtemplateboot.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 入库管理/打印库位
 * </p>
 *
 * @author shaohui
 * @since 2018-12-15
 */
public interface WarehousingPrintMapper {

    /**
     * 获取作业员名
     */
    String getUserName(@Param("userCode") String userCode,@Param("warehouse") String warehouse);

    /**
     * 根据入库交接单号，查询信息
     */
    Map<String, Object> getWarehousingInfo(String wsTransition);

    /**
     * 保存
     */
    void saveWarehousingInfo(Map<String, Object> map);
}
