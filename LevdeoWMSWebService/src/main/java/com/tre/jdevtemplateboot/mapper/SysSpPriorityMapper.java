package com.tre.jdevtemplateboot.mapper;

import com.tre.jdevtemplateboot.domain.po.SysSpPriority;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2018-12-25
 */
public interface SysSpPriorityMapper extends BaseMapper<SysSpPriority> {

    /**
     * 批量插入库位优先策略
     *
     * @param stockPosition 模板库位
     * @param startSp       开始库位
     * @param endSp         终止库位
     * @param radioChoose   是否覆盖
     * @param userCode
     * @return
     */
    Integer insertSpPriority(@Param("stockPosition") String stockPosition,
                             @Param("startSp") String startSp,
                             @Param("endSp") String endSp,
                             @Param("radioChoose") String radioChoose,
                             @Param("userCode") String userCode);

    /**
     * 删除库区相关联的数据
     *
     * @param code
     */
    void deleteBycode(String code);

    /**
     * 系列
     *
     * @param stockPosition
     * @param priorityCode
     * @return
     */
    List<Map<String, Object>> getSPLVSeries(@Param("stockPosition") String stockPosition,
                                            @Param("priorityCode") String priorityCode);

    /**
     * 品牌
     *
     * @param stockPosition
     * @param priorityCode
     * @return
     */
    List<Map<String, Object>> getSPLVBrand(@Param("stockPosition") String stockPosition,
                                           @Param("priorityCode") String priorityCode);

    /**
     * 颜色
     *
     * @param stockPosition
     * @param priorityCode
     * @return
     */
    List<Map<String, Object>> getSPLVColor(@Param("stockPosition") String stockPosition,
                                           @Param("priorityCode") String priorityCode);

}
