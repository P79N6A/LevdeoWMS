package com.tre.jdevtemplateboot.common.util;

import com.tre.jdevtemplateboot.common.constant.SysConstant;
import com.tre.jdevtemplateboot.common.constant.SysConstantJwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @description: 通用常量
 * @author: songguifan
 * @create: 2018-12-15
 **/
public class CommonUtils {

    /**
     * 获取当前登录用户的userCode
     *
     * @return
     */
    public static String getUserCode() {
        return getClaims().getSubject();
    }

    /**
     * 获取系统管理员ID
     *
     * @return
     */
    public static String getSysManagerUserCode() {
        return SysConstant.SYS_MANAGER_ID;
    }

    /**
     * 获取当前登录用户的仓库
     *
     * @return
     */
    public static String getWarehouseCode() {
        return getClaims().get("warehouse").toString();
    }

    /**
     * 获取权限
     *
     * @return
     */
    public static Map<String, Object> getMenuMap() {
        return (Map<String, Object>) getClaims().get("menuMap");
    }

    /**
     * 获取Claims对象
     *
     * @return
     */
    private static Claims getClaims() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Claims claims = (Claims) request.getAttribute("claims");
        if (claims == null) {
            String token = request.getParameter("token");
            if (token == null) {
                String Authorization = request.getHeader("Authorization");
                if (Authorization != null && Authorization.length() > 7) {
                    token = Authorization.substring(6);
                } else {
                    return null;
                }
            }
            claims = Jwts.parser().setSigningKey(SysConstantJwt.JWT_SECERT).parseClaimsJws(token).getBody();
            request.setAttribute("claims", claims);
        }
        return claims;
    }

    /**
     * 返回当前日期+时间
     *
     * @return
     */
    public static Date getCurrentDateTime() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 将String转换为java.util.Date
     *
     * @param dateString 日期字符串
     * @param pattern    日期格式
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String dateString, String pattern) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.parse(dateString);
    }

    /**
     * 将java.util.Date转为String
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 将字符串转成ASCII
     *
     * @param value
     * @return
     */
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }

    /**
     * 将ASCII转成字符串
     *
     * @param
     * @return
     */
    public static String asciiToString(byte[] bt, int startIndex, int length) {
        StringBuffer sbu = new StringBuffer();
        for (int i = startIndex; i < length; i++) {
            sbu.append(bt[i]);
        }
        return sbu.toString();
    }

    public static String asciiToStringBK(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    /**
     * 毫秒转时分秒毫秒
     *
     * @param millSeconds
     * @return
     */
    public static String MillisecondsToHms(long millSeconds) {
        long hours = millSeconds / 3600000;
        long minutes = millSeconds % 3600000 / 60000;
        long seconds = millSeconds % 60000 / 1000;
        long millseconds = millSeconds % 1000;

        String time = (hours == 0 ? "" : hours + "小时") + (minutes == 0 ? "" : minutes + "分钟") + (seconds == 0 ? "" : seconds + "秒") + (millseconds == 0 ? "" : millseconds + "毫秒");
        return time;
    }
}
