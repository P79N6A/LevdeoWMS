package com.tre.jdevtemplateboot.web.controller;

import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.LJwtUtils;
import com.tre.jdevtemplateboot.domain.po.SysUser;
import com.tre.jdevtemplateboot.mapper.SysRoleFunctionMapper;
import com.tre.jdevtemplateboot.mapper.SysUserMapper;
import com.tre.jdevtemplateboot.web.annotation.PassToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 登陆接口
 * @author: JDev
 * @create: 2018-12-07 16:17
 **/
@RestController
@PassToken
public class LoginController {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleFunctionMapper SysRoleFunctionMapper;

    //登陆接口
    @PostMapping("/login")
    public ResponseResult login(String userCode, String password) {
        //查询用户是否存在
        SysUser sysUser = sysUserMapper.selectById(userCode);
        if (sysUser == null) {
            return ResponseResult.buildCheck("-1", "用户名或密码错误，请重新输入。", null);
        }
        //MD5加密
        String passwordMD5 = DigestUtils.md5DigestAsHex(password.getBytes()).toUpperCase();
        //验证密码与停用状态
        if (!sysUser.getPassword().equals(passwordMD5)) {
            return ResponseResult.buildCheck("-1", "用户名或密码错误，请重新输入。", null);
        } else {
            if ("1".equals(sysUser.getIsDel())) {
                return ResponseResult.buildCheck("-1", "该账号已停用。", null);
            }
        }

        //获取menu菜单权限
        String roleId = sysUser.getRoleId();
        List<Map<String, Object>> menuList = SysRoleFunctionMapper.getMenuJuris(roleId);
        Map<String, Object> menuMap = new HashMap<>();
        for (Map<String, Object> map : menuList) {
            menuMap.put((String) map.get("functionId"), (String) map.get("url"));
        }

        // Create Twt token
        return ResponseResult.buildOK(LJwtUtils.generateToken(sysUser, menuMap));
    }
}
