package com.tre.jdevtemplateboot.mapper;

import com.tre.jdevtemplateboot.domain.po.InventoryWork;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2019-01-14
 */
public interface InventoryWorkMapper extends BaseMapper<InventoryWork> {

    /**
     * 根据操作人清空临时表
     *
     * @param userCode 操作人编码
     */
    void deleteInventoryWork(String userCode);

}
