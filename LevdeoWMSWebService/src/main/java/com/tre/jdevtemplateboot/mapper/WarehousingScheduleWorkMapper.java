package com.tre.jdevtemplateboot.mapper;

import com.tre.jdevtemplateboot.domain.po.WarehousingSchedule;
import com.tre.jdevtemplateboot.domain.po.WarehousingScheduleWork;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 入库交接工作表 Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2019-02-19
 */
public interface WarehousingScheduleWorkMapper extends BaseMapper<WarehousingScheduleWork> {

    /**
     * 获取插入后的VIN码
     * @param workId
     * @return
     */
    List<WarehousingSchedule> getInsertedVin(@Param("workId")String workId);

    /**
     * 更新work表与入库交接表的内容
     * @param workId
     */
    void updateWarehousingWork(@Param("workId")String workId);

    /**
     * 通过sap账号获取本地usercode
     * @param workId
     * @return
     */
    String getUserCode(@Param("workId")String workId);
}
