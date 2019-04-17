package com.tre.jdevtemplateboot.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description: 加密方法
 * @author: JDev
 * @create: 2018-11-22 09:37
 **/
public class LMD5Utils {

    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String encrypt32(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    public static String encrypt32Up(String strObj) {
        return encrypt32(strObj).toUpperCase();
    }

    public static String encrypt16(String strObj) {
        return encrypt32(strObj).substring(8, 24);
    }

    public static String encrypt16Up(String strObj) {
        return encrypt32Up(strObj).substring(8, 24);
    }


    /**
     * 转换字节数组为16进制字串
     */
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * 返回形式为数字跟字符串
     */
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

}
