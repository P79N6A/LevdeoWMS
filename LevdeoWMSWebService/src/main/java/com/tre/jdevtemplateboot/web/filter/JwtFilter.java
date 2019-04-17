package com.tre.jdevtemplateboot.web.filter;

import com.tre.jdevtemplateboot.common.constant.SysConstantErr;
import com.tre.jdevtemplateboot.common.constant.SysConstantJwt;
import com.tre.jdevtemplateboot.common.constant.SysConstantJwtErr;
import com.tre.jdevtemplateboot.exception.SysException;
import com.tre.jdevtemplateboot.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @description: Jwtconfig中配置的filter 用于Jwt token的验证工作 配置时可以指定对应的路径
 * @author: JDev
 * @create: 2018-12-07 16:01
 **/
public class JwtFilter  extends GenericFilterBean {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authHeader = request.getHeader("Authorization");

        //规避探测性质的 OPTIONS请求
        String optionsString = "OPTIONS";
        if (optionsString.equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(servletRequest, servletResponse);
        }else {
            //验证token
            if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith(SysConstantJwt.JWT_AUTHOR_TYPE)){
                throw new ServletException(new TokenException(SysConstantJwtErr.TOKEN_ERROR_MISS_CODE));
            }else {
                String token = authHeader.substring(SysConstantJwt.JWT_AUTHOR_TYPE.length());
                try {
                    //使用jwt paser来验证签名
                    Claims claims = Jwts.parser().setSigningKey(SysConstantJwt.JWT_SECERT).parseClaimsJws(token).getBody();

                    //所拥有的菜单权限
                    Map<String, Object> menuMap = (Map<String, Object>) claims.get("menuMap");
                    //当前请求的页面url
                    String htmlurl = request.getParameter("htmlurl");
                    if(htmlurl!=null && !menuMap.containsValue(htmlurl)){
                        throw new SysException(SysConstantErr.SERVER_ERROR_NO_JURISDICTION_CODE, SysConstantErr.SERVER_ERROR_NO_JURISDICTION_DATA);
                    }

                    request.setAttribute("claims", claims);
                }catch (ExpiredJwtException e){
                    throw new ServletException(new TokenException(SysConstantJwtErr.TOKEN_ERROR_EXPIRED_CODE));
                }catch (SignatureException e){
                    throw new ServletException(new TokenException(SysConstantJwtErr.TOKEN_ERROR_INVALID_CODE));
                }catch (Exception e){
                    //throw new ServletException(new TokenException(SysConstantJwtErr.TOKEN_ERROR_CODE));
                    throw new ServletException(e);
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }
}
