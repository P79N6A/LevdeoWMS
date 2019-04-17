package com.tre.jdevtemplateboot.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.domain.po.SysRole;
import com.tre.jdevtemplateboot.mapper.SysRoleFunctionMapper;
import com.tre.jdevtemplateboot.mapper.SysRoleMapper;
import com.tre.jdevtemplateboot.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author JDev
 * @since 2018-12-14
 */
@RestController
@RequestMapping("/sys-role")
public class SysRoleController {

    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    SysRoleFunctionMapper sysRoleFunctionMapper;

    /**
     * 查询数据
     *
     * @return
     */
    @RequestMapping("/getPlanList")
    @ResponseBody
    public ResponseResult getPlanList() {
        return ResponseResult.buildOK(sysRoleMapper.selectList(null));
    }

    /**
     * 添加角色
     * @param sr
     * @return
     */
    @RequestMapping(value = "/regList", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult insertRole(@RequestBody SysRole sr) {

        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("roleName", sr.getRoleName());
        int cnt = sysRoleMapper.selectCount(wrapper);
        if (cnt > 0) {
            return ResponseResult.buildCheck("1", "该角色已经存在。", null);
        }
        sr.setCreateId(CommonUtils.getUserCode());
        sr.setCreateTime(CommonUtils.getCurrentDateTime());
        sr.setUpdateId(CommonUtils.getUserCode());
        sr.setUpdateTime(CommonUtils.getCurrentDateTime());
        sysRoleMapper.insert(sr);
        
        return ResponseResult.buildOK();
    }

    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/delList", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ResponseResult delList(String roleId) {
    	
        QueryWrapper wrapper = new QueryWrapper();
        ((QueryWrapper) wrapper).eq("roleId", roleId);
        int cnt = sysUserMapper.selectCount(wrapper);
        if (cnt > 0) {
            return ResponseResult.buildCheck("1", "该角色已使用，无法删除。", null);
        }
        sysRoleMapper.delete(wrapper);
        sysRoleFunctionMapper.delete(wrapper);
        return ResponseResult.buildOK();
    }

    /**
     * 修改角色名
     * @param sr
     * @return
     */
    @RequestMapping("/updateList")
    @ResponseBody
    public ResponseResult updateRole(@RequestBody SysRole sr) {

        QueryWrapper wrapper = new QueryWrapper();
        ((QueryWrapper) wrapper).eq("roleName", sr.getRoleName());
        int cnt = sysRoleMapper.selectCount(wrapper);
        if (cnt > 0) {
            return ResponseResult.buildCheck("1", "该角色已经存在。", null);
        }
        sr.setUpdateId(CommonUtils.getUserCode());
        sr.setUpdateTime(CommonUtils.getCurrentDateTime());
        sysRoleMapper.updateById(sr);
        return ResponseResult.buildOK();
    }
}
