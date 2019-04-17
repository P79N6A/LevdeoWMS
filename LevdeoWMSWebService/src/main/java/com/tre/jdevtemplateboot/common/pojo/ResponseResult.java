package com.tre.jdevtemplateboot.common.pojo;

import com.tre.jdevtemplateboot.common.constant.SysConstantErr;
import com.tre.jdevtemplateboot.common.constant.SysConstantInfo;
import com.tre.jdevtemplateboot.exception.SysException;

import java.io.Serializable;

/**
 *  返回值实体类
 *  <p>Title: ResponseResult</p>
 *  @author JDev
 *  @date   2018/11/13 日
 */
public class ResponseResult implements Serializable {

    private static final long serialVersionUID = 1L;    
    private static  String[] emptyData = {};
    
    private String code;

    private String msg;

    private Object data;
    

    /** 
    * @Description:  Response构造函数
    * @Param: [code, msg, data] 
    * @return:  
    * @Author: JDev
    * @Date: 2018/12/10 
    **/
    public ResponseResult(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * @Description:
     * @Param: []
     * @return: com.tre.jdevtemplateboot.common.pojo.ResponseResult
     * @Author: JDev
     * @Date: 2018/11/22
     **/
    public  static  ResponseResult buildOK(){

        return new ResponseResult(SysConstantInfo.SERVER_SUCCESS_CODE,SysConstantInfo.SERVER_SUCCESS_MSG
                ,SysConstantInfo.SERVER_SUCCESS_DATA);
    }

    /**
    * @Description:
    * @Param: [code, msg, data]
    * @return: com.tre.jdevtemplateboot.common.pojo.ResponseResult
    * @Author: JDev
    * @Date: 2018/11/22
    **/
    public static ResponseResult buildOK(Object data){
        return  new ResponseResult(SysConstantInfo.SERVER_SUCCESS_CODE, SysConstantInfo.SERVER_SUCCESS_MSG,data);
    }

        
    /** 
    * @Description:
    * @Param: [sysException] 
    * @return: com.tre.jdevtemplateboot.common.pojo.ResponseResult 
    * @Author: JDev
    * @Date: 2018/11/22 
    **/
    public  static  ResponseResult buildError(SysException sysException){

        return new ResponseResult(sysException.getCode(),sysException.getMsg(),sysException.getData());
    }


    /** 
    * @Description:  
    * @Param: [data] 
    * @return: com.tre.jdevtemplateboot.common.pojo.ResponseResult 
    * @Author: JDev
    * @Date: 2018/12/03 
    **/
    public  static  ResponseResult buildCheck(String code, String msg,Object data){
        if(data == null){
            data = emptyData;
        }
        return  new ResponseResult(code, msg, data);
    }
    
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
