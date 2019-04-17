package com.tre.jdevtemplateboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tre.jdevtemplateboot.domain.po.SysParm;
import com.tre.jdevtemplateboot.web.pojo.MainData;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author JDev
 * @since 2018-12-17
 */
public interface ISysParmService extends IService<SysParm> {

    /**
     * 获取主数据详情
     * @param subCode
     * @return
     */
    List<Map<String,Object>> getMainDataInfo(String subCode);

    /**
     *
     * @param md
     * @return
     */
    int updateMainDataById(MainData md);

}
