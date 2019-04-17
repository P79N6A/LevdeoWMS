package com.tre.jdevtemplateboot.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.domain.po.SysUser;
import com.tre.jdevtemplateboot.mapper.SysUserMapper;
import com.tre.jdevtemplateboot.web.pojo.UserParameterBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 人员表 前端控制器
 * </p>
 *
 * @author JDev
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/sys-user")

public class SysUserController {
    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询sysUser
     *
     * @param ub
     * @return
     */
    @RequestMapping("/getOperateInfo")
    @ResponseBody
    public ResponseResult getOperateInfo(UserParameterBean ub) {
        PageHelper.startPage(ub.getCurrentPage(), ub.getLimit());
        List<Map<String, Object>> list = sysUserMapper.checkUser(ub);
        return ResponseResult.buildOK(new PageInfo<>(list));
    }

    /**
     * 查询用户信息
     *
     * @param userCode
     * @return
     */
    @RequestMapping("/getUserInfo")
    public ResponseResult getUserInfo(@RequestParam(value = "userCode") String userCode) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("userCode", userCode);
        return ResponseResult.buildOK(sysUserMapper.selectOne(wrapper));
    }

    /**
     * 新建作业员
     *
     * @param user
     * @return
     */
    @RequestMapping("/insertUser")
    @ResponseBody
    public ResponseResult insertUser(SysUser user) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("userCode", user.getUserCode());
        int cnt = sysUserMapper.selectCount(wrapper);
        if (cnt != 0) {
            return ResponseResult.buildCheck("1", "此作业员已存在", null);
        }

        wrapper = new QueryWrapper();
        wrapper.eq("sapUserCode", user.getSapUserCode());
        int sapCnt=sysUserMapper.selectCount(wrapper);
        if(sapCnt!=0)
        {
            return ResponseResult.buildCheck("1", "sap账号已存在", null);
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getUserCode().getBytes()).toUpperCase());
        user.setCreateId(CommonUtils.getUserCode());
        user.setCreateTime(CommonUtils.getCurrentDateTime());
        user.setUpdateId(CommonUtils.getUserCode());
        user.setUpdateTime(CommonUtils.getCurrentDateTime());
        sysUserMapper.insert(user);
        return ResponseResult.buildOK();
    }

    /**
     * 修改作业员
     *
     * @param user
     * @return
     */
    @RequestMapping("/updateUser")
    @ResponseBody
    public ResponseResult updateUser(SysUser user) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("sapUserCode", user.getSapUserCode());
        wrapper.ne("userCode",user.getUserCode());

        int sapCnt=sysUserMapper.selectCount(wrapper);
        if(sapCnt!=0)
        {
            return ResponseResult.buildCheck("1", "sap账号已存在", null);
        }
        user.setUpdateId(CommonUtils.getUserCode());
        user.setUpdateTime(CommonUtils.getCurrentDateTime());
        sysUserMapper.updateById(user);
        return ResponseResult.buildOK();
    }

    /**
     * 重置密码
     *
     * @param userCode
     * @return
     */
    @RequestMapping("/resetPassword")
    public ResponseResult resetPassword(@RequestParam(value = "userCode") String userCode) {
        SysUser sysUser = new SysUser();
        sysUser.setUserCode(userCode);
        sysUser.setPassword(DigestUtils.md5DigestAsHex(userCode.getBytes()).toUpperCase());
        sysUser.setUpdateId(CommonUtils.getUserCode());
        sysUser.setUpdateTime(CommonUtils.getCurrentDateTime());
        sysUserMapper.updateById(sysUser);
        return ResponseResult.buildOK();
    }

    /**
     * 获取user对应的menu
     *
     * @return
     */
    @RequestMapping("/showMenu")
    @ResponseBody
    public ResponseResult showMenu() {
        //a 获取该用户对应的Group与Function
        String userCode = CommonUtils.getUserCode();
        List<Map<String, Object>> groupList = sysUserMapper.getUserGroup(userCode);
        List<Map<String, Object>> functionList = sysUserMapper.getUserFunction(userCode);
        //a 封装数据,返回前台
        for (Map<String, Object> groupMap : groupList) {

            if (groupMap.get("groupCode") == null) {
                return ResponseResult.buildCheck("0", "您还没设置权限。", groupList);
            }

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> functionMap : functionList) {
                if (groupMap.get("groupCode").equals(functionMap.get("groupCode"))) {
                    list.add(functionMap);
                }
            }
            groupMap.put("functionList", list);
        }
        return ResponseResult.buildOK(groupList);
    }

    /**
     * 修改用户密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    @RequestMapping("/updateUserPwd")
    @ResponseBody
    public ResponseResult updateUserPwd(String oldPassword, String newPassword) {
        //a 验证用户是否存在
        String userCode = CommonUtils.getUserCode();
        SysUser sysUser = sysUserMapper.selectById(userCode);
        if (sysUser == null) {
            return ResponseResult.buildCheck("-1", "当前用户不存在或状态异常,请重新操作", null);
        }
        //a 验证旧密码
        String passwordMD5 = DigestUtils.md5DigestAsHex(oldPassword.getBytes()).toUpperCase();
        if (!sysUser.getPassword().equals(passwordMD5)) {
            return ResponseResult.buildCheck("-1", "原密码错误,请重新输入.", null);
        }
        //a 更新密码
        String password = DigestUtils.md5DigestAsHex(newPassword.getBytes()).toUpperCase();
        sysUser.setPassword(password);
        sysUser.setUpdateId(userCode);
        sysUser.setUpdateTime(CommonUtils.getCurrentDateTime());
        sysUserMapper.updateById(sysUser);

        return ResponseResult.buildOK();
    }

    /**
     * 判断页面是否有访问权限
     * @param htmlurl
     * @return
     * @throws Exception
     */
    @PostMapping("/menuJurisdiction")
    public ResponseResult menuJurisdiction(String htmlurl) throws Exception {
        return ResponseResult.buildOK();
    }
}
