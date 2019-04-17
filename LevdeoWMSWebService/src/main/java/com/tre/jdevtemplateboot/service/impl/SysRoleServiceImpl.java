package com.tre.jdevtemplateboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tre.jdevtemplateboot.domain.po.SysRole;
import com.tre.jdevtemplateboot.mapper.SysRoleMapper;
import com.tre.jdevtemplateboot.service.ISysRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author JDev
 * @since 2018-12-14
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

}