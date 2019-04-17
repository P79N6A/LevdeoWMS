package com.tre.jdevtemplateboot.web.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.common.util.ExcelUtils;
import com.tre.jdevtemplateboot.mapper.StockLogMapper;
import com.tre.jdevtemplateboot.web.annotation.PassToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库存日志 前端控制器
 * </p>
 *
 * @author JDev
 * @since 2018-12-15
 */
@RestController
@RequestMapping("/stocklog")
public class StockLogController {
    @Autowired
    private StockLogMapper StockLogMapper;

    /**
     * 查询
     * */
    @RequestMapping("/searchLogData")
    @ResponseBody
    public ResponseResult searchLogData(
            @RequestParam(value="vin") String vin,
            @RequestParam(value="matName") String matName,
            @RequestParam(value="operator") String operator,
            @RequestParam(value="operateDate") String operateDate,
            @RequestParam(value = "operateCode")String operateCode,
            @RequestParam(value="orderColumn") String orderColumn,
            @RequestParam(value="orderDir") String orderDir,
            @RequestParam(value="currentPage",required=true) int currentPage,
            @RequestParam(value="limit",required=true) int limit
    ){
        Map<String,Object> map = new HashMap<>();
        map.put("vin", vin);
        map.put("matName", matName);
        map.put("operator", operator);
        map.put("operateDate", operateDate);
        map.put("operateCode", operateCode);
        map.put("orderColumn", orderColumn);
        map.put("orderDir", orderDir);
        map.put("currentPage", currentPage);
        map.put("limit", limit);
        map.put("warehouse",CommonUtils.getWarehouseCode() );
       
        PageHelper.startPage(currentPage, limit);
        List<Map<String,Object>> listData  = StockLogMapper.searchLogData(map);
        return ResponseResult.buildOK(new PageInfo<>(listData));
    }

    /**
     * 导出
     * */
    @RequestMapping(value = "/downloadExcel")
    @ResponseBody
    @PassToken
    public void downloadExcel(
            @RequestParam(value="vin") String vin,
            @RequestParam(value="matName") String matName,
            @RequestParam(value="operator") String operator,
            @RequestParam(value="operateDate") String operateDate,
            HttpServletResponse response
    ) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("vin", vin);
        map.put("matName", matName);
        map.put("operator", operator);
        map.put("operateDate", operateDate);
        map.put("warehouse",CommonUtils.getWarehouseCode() );
        //查询
        List<Map<String,Object>> listData = StockLogMapper.searchLogData(map);

        //excel列名
        List<String> titles = new ArrayList<>();
        titles.add("VIN码");
        titles.add("物料编码");
        titles.add("物料描述");
        titles.add("操作");
        titles.add("原库位号");
        titles.add("新库位号");
        titles.add("操作人");
        titles.add("操作日");
        titles.add("理由");

        //字段名
        List<String> columns = new ArrayList<>();
        columns.add("vin");
        columns.add("matCode");
        columns.add("name");
        columns.add("operateName");
        columns.add("oldStorageNo");
        columns.add("newStorageNo");
        columns.add("operator");
        columns.add("updateTime");
        columns.add("reason");

        //文件名前缀：库存日志
        String prefixname = "kcrz";
        //下载
        ExcelUtils.downloadExcel(listData, titles, columns, prefixname, response);
    }
}

