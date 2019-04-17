package com.tre.jdevtemplateboot.web.controller;


import com.alibaba.fastjson.JSON;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.mapper.WarehousingPrintMapper;
import com.tre.jdevtemplateboot.service.IWarehousingPrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  入库管理/打印库位
 * </p>
 * @author shaohui
 * @since 2018-12-15
 */
@RestController
@RequestMapping("/warehousingprint")
public class WarehousingPrintController {
    @Autowired
    private WarehousingPrintMapper WarehousingPrintMapper;
    @Resource
    private IWarehousingPrintService IWarehousingPrintService;

    /**
     * 获取作业员名
     * */
    @RequestMapping("/getUserName")
    @ResponseBody
    public ResponseResult getUserName(
            @RequestParam(value="userCode") String userCode
    ){
        String userName = WarehousingPrintMapper.getUserName(userCode, CommonUtils.getWarehouseCode());
        return ResponseResult.buildOK(userName);
    }

    /**
     * 根据入库交接单号，查询信息
     * */
    @RequestMapping("/getWarehousingInfo")
    @ResponseBody
    public ResponseResult getWarehousingInfo(
            String wsTransition
    ){
        Map<String, Object> wareinfo = WarehousingPrintMapper.getWarehousingInfo(wsTransition);
        return ResponseResult.buildOK(wareinfo);
    }

    /**
     * 保存
     * */
    @RequestMapping("/saveWarehousingInfo")
    @ResponseBody
    public ResponseResult saveWarehousingInfo(
            @RequestParam(value="warehousingInfos") String warehousingInfos,
            @RequestParam(value="operateCode") String operateCode,
            @RequestParam(value="userCode") String userCode
    ){
        List<Map<String, Object>> wareinfoList = (List<Map<String, Object>>) JSON.parse(warehousingInfos);

        Map<String, Object> map = new HashMap<>();
        map.put("wareinfoList", wareinfoList);
        map.put("operateCode", operateCode);
        map.put("userCode", userCode);

        WarehousingPrintMapper.saveWarehousingInfo(map);

        return ResponseResult.buildOK();
    }
}