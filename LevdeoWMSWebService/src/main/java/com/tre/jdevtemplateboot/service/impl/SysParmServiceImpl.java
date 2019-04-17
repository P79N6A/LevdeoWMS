package com.tre.jdevtemplateboot.service.impl;

import com.tre.jdevtemplateboot.domain.po.SysParm;
import com.tre.jdevtemplateboot.mapper.SysParmMapper;
import com.tre.jdevtemplateboot.service.ISysParmService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tre.jdevtemplateboot.web.pojo.MainData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JDev
 * @since 2018-12-17
 */
@Service
public class SysParmServiceImpl extends ServiceImpl<SysParmMapper, SysParm> implements ISysParmService {

   @Autowired
   private SysParmMapper sysParmMapper;

    /**
     * 获取主数据的详情
     * @param subCode
     * @return
     */
    @Override
    public List<Map<String, Object>> getMainDataInfo(String subCode) {

        return sysParmMapper.getMainDataInfos(sysParmMapper.getTableName(subCode));
    }

    @Override
    public int updateMainDataById(MainData md) {
        return sysParmMapper.updateMainMatById(md.getTbName(),md.getCode(),md.getName(),md.getUpdateId());
    }
}
