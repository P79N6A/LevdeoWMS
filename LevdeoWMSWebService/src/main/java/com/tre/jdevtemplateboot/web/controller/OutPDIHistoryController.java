package com.tre.jdevtemplateboot.web.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.common.util.ExcelUtils;
import com.tre.jdevtemplateboot.mapper.OutPDIHistoryMapper;
import com.tre.jdevtemplateboot.mapper.StockMapper;
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
 * PDI检测不合格履历 前端控制器
 * </p>
 *
 * @author JDev
 * @since 2018-12-24
 */
@RestController
@RequestMapping("/out-pdihistory")
public class OutPDIHistoryController {
    /**
     * PDI生成表
     */
    @Autowired
    private OutPDIHistoryMapper outPDIHistoryMapper;
    @Autowired
    private StockMapper stockMapper;

    /**
     * 查询
     *
     * @param invoiceCode
     * @param vin
     * @param orderColumn
     * @param orderDir
     * @param currentPage
     * @param limit
     * @return
     */
    @RequestMapping("/PDITable")
    @ResponseBody
    public ResponseResult PDITable(@RequestParam(value = "invoiceCode") String invoiceCode, @RequestParam(value = "vin") String vin, @RequestParam(value = "orderColumn") String orderColumn, @RequestParam(value = "orderDir") String orderDir, @RequestParam(value = "currentPage") String currentPage, @RequestParam(value = "limit") String limit) {
        Map<String, Object> map = new HashMap();
        map.put("invoiceCode", invoiceCode);
        map.put("vin", vin);
        map.put("orderColumn", orderColumn);
        map.put("orderDir", orderDir);
        map.put("warehouse", CommonUtils.getWarehouseCode());
        PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(limit));
        List<Map<String, Object>> list = outPDIHistoryMapper.outPDITable(map);
        return ResponseResult.buildOK(new PageInfo<Map<String, Object>>(list));
    }

    /**
     * 导出
     *
     * @param invoiceCode
     * @param vin
     * @param response
     * @throws Exception
     */
    @RequestMapping("/downloadExcel")
    @ResponseBody
    @PassToken
    public void downloadExcel(@RequestParam(value = "invoiceCode") String invoiceCode, @RequestParam(value = "vin") String vin, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("invoiceCode", invoiceCode);
        map.put("vin", vin);
        map.put("warehouse", CommonUtils.getWarehouseCode());
        //查询
        List<Map<String, Object>> listData = outPDIHistoryMapper.outPDITable(map);

        //excel列名
        List<String> titles = new ArrayList<>();
        titles.add("发货单号");
        titles.add("VIN码");
        titles.add("物料编码");
        titles.add("物料描述");
        titles.add("检测日");
        titles.add("检测人");
        titles.add("不合格理由");

        //字段名
        List<String> columns = new ArrayList<>();
        columns.add("invoiceCode");
        columns.add("vin");
        columns.add("matCode");
        columns.add("name");
        columns.add("createtime");
        columns.add("inspector");
        columns.add("reason");

        //文件名前缀：库存日志
        String prefixname = "outPDIHistory";
        //下载
        ExcelUtils.downloadExcel(listData, titles, columns, prefixname, response);
    }
}
