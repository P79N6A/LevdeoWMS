package com.tre.jdevtemplateboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tre.jdevtemplateboot.domain.po.SysRoleFunction;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2018-12-17
 */
public interface SysRoleFunctionMapper extends BaseMapper<SysRoleFunction> {
    /**
     * 查询数据
     *
     * @param roleId
     * @return
     */
    List<Map<String, Object>> getList(Integer roleId);

    /**
     * 通过角色id，获取菜单权限list
     * @param roleId
     * @return
     */
    List<Map<String, Object>> getMenuJuris(@Param("roleId") String roleId);
}
