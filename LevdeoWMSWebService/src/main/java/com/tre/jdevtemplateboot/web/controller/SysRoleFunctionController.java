package com.tre.jdevtemplateboot.web.controller;


import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.domain.po.SysRoleFunction;
import com.tre.jdevtemplateboot.mapper.SysRoleFunctionMapper;
import com.tre.jdevtemplateboot.service.ISysRoleFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author JDev
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/sys-role-function")
public class SysRoleFunctionController {
    @Autowired
    private SysRoleFunctionMapper sysRoleFunctionMapper;
    @Autowired
    private ISysRoleFunctionService iSysRoleFunctionService;

    /**
     * 查询数据
     *
     * @param roleId
     * @return
     */
    @RequestMapping("/getFunctionList")
    @ResponseBody
    public ResponseResult getFunctionList(Integer roleId) {
        return ResponseResult.buildOK(sysRoleFunctionMapper.getList(roleId));
    }

    /**
     * 新建数据
     *
     * @param newRoleId
     * @param num
     * @return
     */
    @Transactional
    @RequestMapping("/updateConnectionTable")
    @ResponseBody
    public ResponseResult updateConnectionTable(Integer newRoleId, String num) {
        List<SysRoleFunction> srfList = new ArrayList<>();

        //a删除角色对应功能
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("roleId", newRoleId);
        sysRoleFunctionMapper.delete(wrapper);

        if (StringUtils.isEmpty(num)) {
            return ResponseResult.buildOK();
        }

        //a插入角色对应功能
        String[] funcIdList = num.split(",");
        for (String funcId : funcIdList) {
            SysRoleFunction sysRoleFunction = new SysRoleFunction();
            sysRoleFunction.setRoleId(newRoleId);
            sysRoleFunction.setFunctionId(funcId);
            sysRoleFunction.setCreateId(CommonUtils.getUserCode());
            sysRoleFunction.setCreateTime(CommonUtils.getCurrentDateTime());
            sysRoleFunction.setUpdateId(CommonUtils.getUserCode());
            sysRoleFunction.setUpdateTime(CommonUtils.getCurrentDateTime());
            srfList.add(sysRoleFunction);
        }
        iSysRoleFunctionService.saveBatch(srfList);
        
        return ResponseResult.buildOK();
    }
}
