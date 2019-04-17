package com.tre.jdevtemplateboot.service.impl;

import com.tre.jdevtemplateboot.domain.po.Stock;
import com.tre.jdevtemplateboot.mapper.StockMapper;
import com.tre.jdevtemplateboot.service.IStockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 库存 服务实现类
 * </p>
 *
 * @author JDev
 * @since 2018-12-21
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements IStockService {

}
