package com.tre.jdevtemplateboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tre.jdevtemplateboot.domain.po.SysUser;
import com.tre.jdevtemplateboot.web.pojo.UserParameterBean;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 人员表 Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2018-12-17
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 查询sysUser表数据
     *
     * @param ub
     * @return
     */
    List<Map<String, Object>> checkUser(UserParameterBean ub);

    /**
     * 获取user对应的group
     *
     * @param userCode
     * @return
     */
    List<Map<String, Object>> getUserGroup(String userCode);

    /**
     * 获取user对应的function
     *
     * @param userCode
     * @return
     */
    List<Map<String, Object>> getUserFunction(String userCode);
}
