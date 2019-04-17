package com.tre.jdevtemplateboot.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * @description: TRE HTTP Client Access
 * @author: JDev
 * @create: 2018-11-16 13:26
 **/
public class LHttpClientAPI {

    /** 4种访问方式 - POST */
    private static final String SERVLET_POST = "POST";
    /** 4种访问方式 - GET */
    private static final String SERVLET_GET = "GET";
    /** 4种访问方式 - DELETE */
    private static final String SERVLET_DELETE = "DELETE";
    /** 4种访问方式 - PUT */
    private static final String SERVLET_PUT = "PUT";

    /** 可用的输入、输出类型 - html */
    private static final String ACCEPT_HTML = "text/html";
    /** 可用的输入、输出类型 - xml */
    private static final String ACCEPT_XML = "text/xml";
    /** 可用的输入、输出类型 - json */
    private static final String ACCEPT_JSON = "application/json";
    /** 可用的输入、输出类型 - x-protobuf */
    private static final String ACCEPT_PROTOBUF = "application/x-protobuf";
    /** 可用的输入、输出类型 - octet-stream */
    private static final String ACCEPT_STREAM = "application/octet-stream";



    /**
     * 执行REST的post方法
     * @param urlStr
     * @param param
     * @return {state:(int)状态码,data:(String)返回值}
     * @throws Exception
     *
     */
    public static Map<String, Object> doPost(String urlStr, String param) throws Exception {
        return doExecPost_Put(urlStr,param,SERVLET_POST);
    }

    /**
     * 执行REST的get方法
     * @param urlStr
     * @return {state:状态码,data:返回值}
     * @throws Exception
     *
     */
    public static Map<String, Object> doGet(String urlStr) throws Exception{
        Map<String, Object> success = new HashMap<>();
        String state = "";

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置发送请求的方式
        conn.setRequestMethod(SERVLET_GET);
        // 设置返回信息的格式类型
        conn.setRequestProperty("Accept", ACCEPT_JSON);
        conn.connect();
        // 正常时返回的状态码为200
        success.put("state", conn.getResponseCode());
        // 获取返回的内容
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            // 输出返回的信息
            while ((line = br.readLine()) != null) {
                state += line;
            }
            br.close();
            success.put("data", state);
        }catch(Exception e){
            success.put("data",e.toString());
        }

        return success;
    }

    /**
     * 执行REST的put方法
     * @param urlStr
     * @param param
     * @return {state:状态码,data:返回值}
     * @throws Exception
     *
     */
    public static Map<String, Object> doPut(String urlStr, String param) throws Exception{
        return doExecPost_Put(urlStr,param,SERVLET_PUT);
    }

    /**
     * 执行REST的delete方法
     * @param urlStr
     * @return {state:状态码,data:返回值}
     * @throws Exception
     *
     */
    public static Map<String, Object> doDelete(String urlStr) throws Exception{
        Map<String, Object> success = new HashMap<>();
        String state = "";

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置发送请求的方式
            conn.setRequestMethod(SERVLET_DELETE);
            // 设置返回信息的格式类型
            conn.setRequestProperty("Accept", ACCEPT_JSON);
            // 正常时返回的状态码为200
            success.put("state", conn.getResponseCode());
            // 获取返回的内容
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            // 输出返回的信息
            while ((line = br.readLine()) != null) {
                state += line;
            }
            br.close();
            success.put("data", state);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return success;
    }


    /**
     * 执行REST的DELETE方法
     * @param urlStr
     * @param param
     * @return {state:状态码,data:返回值}
     * @throws Exception
     *
     */
    public static Map<String, Object> doDelete(String urlStr, String param) throws Exception{
        Map<String, Object> success = new HashMap<>();
        String state = "";

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置发送请求的方式
            conn.setRequestMethod(SERVLET_DELETE);
            // 设置参数的格式类型
            conn.setRequestProperty("Content-Type", ACCEPT_JSON);
            // 设置参数的长度
            conn.setRequestProperty("Content-Length", String.valueOf(param.length()));
            // 打开输入输出，在output中传输参数
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            // 把参数写入output流，使用UTF-8编码
            os.write(param.getBytes("utf-8"));
            os.close();
            // 正常时返回的状态码为200
            success.put("state", conn.getResponseCode());
            // 获取返回的内容
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            // 输出返回的信息
            while ((line = br.readLine()) != null) {
                state += line;
            }
            br.close();
            success.put("data", state);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return success;
    }

    /**
    * @Description:  exec doPost and doPut method
    * @Param: [urlStr, param, post_put]
    * @return: java.util.Map<java.lang.String,java.lang.Object>
    * @Author: JDev
    * @Date: 2018/11/16
    **/
    private static   Map<String, Object>  doExecPost_Put( String urlStr, String param, String post_put) throws  Exception{

        Map<String, Object> success = new HashMap<>();
        String state = "";

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置发送请求的方式
        conn.setRequestMethod(post_put);
        // 设置参数的格式类型
        conn.setRequestProperty("Content-Type", ACCEPT_JSON);
        // 设置参数的长度
        conn.setRequestProperty("Content-Length", String.valueOf(param.length()));
        // 打开输入输出，在output中传输参数
        conn.setDoInput(true);
        conn.setDoOutput(true);
        OutputStream os = conn.getOutputStream();
        // 把参数写入output流，使用UTF-8编码
        os.write(param.getBytes("utf-8"));
        os.close();
        // 正常时返回的状态码为200
        success.put("state", conn.getResponseCode());
        // 获取返回的内容
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        // 输出返回的信息
        while ((line = br.readLine()) != null) {
            state += line;
        }
        br.close();
        success.put("data", state);

        return success;

    }

}
