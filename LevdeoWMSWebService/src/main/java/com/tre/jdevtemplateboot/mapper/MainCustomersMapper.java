package com.tre.jdevtemplateboot.mapper;

import com.tre.jdevtemplateboot.domain.po.MainCustomers;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2018-12-20
 */
public interface MainCustomersMapper extends BaseMapper<MainCustomers> {
    /**
     * 获取客户下拉，带查询
     * @param searchstr
     * @return
     */
    List<Map<String, Object>> getCustomerList(@Param("searchstr") String searchstr);
}
