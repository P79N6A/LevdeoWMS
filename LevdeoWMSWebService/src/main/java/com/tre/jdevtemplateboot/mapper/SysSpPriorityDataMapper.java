package com.tre.jdevtemplateboot.mapper;

import com.tre.jdevtemplateboot.domain.po.SysSpPriorityData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2018-12-24
 */
public interface SysSpPriorityDataMapper extends BaseMapper<SysSpPriorityData> {

	/**
	 * 删除库区相关联的数据
	 *
	 * @param code
	 */
	void deleteBycode(String code);

	/**
	 * 获取库位-通过优先级规则
	 *
	 * @param priorityRule 规则
	 * @return
	 */
	List<Map<String, Object>> getSpByPrioRule(@Param("warehouseCode") String warehouseCode,
			@Param("priorityRule") String priorityRule);

}
