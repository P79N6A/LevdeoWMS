package com.tre.jdevtemplateboot.service.impl;

import com.tre.jdevtemplateboot.domain.po.StockLog;
import com.tre.jdevtemplateboot.mapper.StockLogMapper;
import com.tre.jdevtemplateboot.service.IStockLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 库存日志 服务实现类
 * </p>
 *
 * @author JDev
 * @since 2019-02-01
 */
@Service
public class StockLogServiceImpl extends ServiceImpl<StockLogMapper, StockLog> implements IStockLogService {

}
