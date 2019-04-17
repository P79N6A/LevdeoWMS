package com.tre.jdevtemplateboot.common.util;

import com.tre.jdevtemplateboot.common.constant.SysConstantJwt;
import com.tre.jdevtemplateboot.domain.po.SysUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: JWT token工厂
 * @author: JDev
 * @create: 2018-12-10 08:39
 **/
public class LJwtUtils {

    public static String generateToken(SysUser user, Map<String, Object> menuMap) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getUserCode());
        claims.put("warehouse", user.getWarehouse());
        claims.put("created", new Date());
        claims.put("menuMap", menuMap);
        return generateToken((claims));
    }

    private static String generateToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims)        //payload
//                .setExpiration(new Date(System.currentTimeMillis() + SysConstantJwt.JWT_TTL))  //过期时间
                .signWith(SignatureAlgorithm.HS512, SysConstantJwt.JWT_SECERT).compact();
    }
}
