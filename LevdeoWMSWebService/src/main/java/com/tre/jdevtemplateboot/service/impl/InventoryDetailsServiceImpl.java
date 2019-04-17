package com.tre.jdevtemplateboot.service.impl;

import com.tre.jdevtemplateboot.domain.po.InventoryDetails;
import com.tre.jdevtemplateboot.mapper.InventoryDetailsMapper;
import com.tre.jdevtemplateboot.service.IInventoryDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 盘点详情表 服务实现类
 * </p>
 *
 * @author JDev
 * @since 2019-02-02
 */
@Service
public class InventoryDetailsServiceImpl extends ServiceImpl<InventoryDetailsMapper, InventoryDetails> implements IInventoryDetailsService {

}
