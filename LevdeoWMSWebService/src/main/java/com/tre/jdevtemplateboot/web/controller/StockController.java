package com.tre.jdevtemplateboot.web.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.common.util.ExcelUtils;
import com.tre.jdevtemplateboot.domain.po.InventoryWork;
import com.tre.jdevtemplateboot.domain.po.StockMoveSpWork;
import com.tre.jdevtemplateboot.mapper.InventoryWorkMapper;
import com.tre.jdevtemplateboot.mapper.StockMapper;
import com.tre.jdevtemplateboot.service.IInventoryWorkService;
import com.tre.jdevtemplateboot.service.impl.StockMoveSpWorkServiceImpl;
import com.tre.jdevtemplateboot.web.annotation.PassToken;
import com.tre.jdevtemplateboot.web.pojo.ChargParameterBean;
import com.tre.jdevtemplateboot.web.pojo.StockView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 库存一览前端控制器
 */

@RestController
@RequestMapping("/stock")
public class StockController {
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StockMoveSpWorkServiceImpl stockMoveSpWorkService;
    @Autowired
    private InventoryWorkMapper inventoryWorkMapper;
    @Autowired
    private IInventoryWorkService iInventoryWorkService;

    /**
     * 初始化一览表格
     *
     * @param sv
     * @return
     */
    @RequestMapping("/serchAll")
    @ResponseBody
    public ResponseResult serchAll(StockView sv) {
        sv.setWarehouse(CommonUtils.getWarehouseCode());
        PageHelper.startPage(sv.getCurrentPage(), sv.getLimit());
        List<Map<String, Object>> list = stockMapper.serchAll(sv);
        return ResponseResult.buildOK(new PageInfo<Map<String, Object>>(list));
    }

    /**
     * 批量更新保存
     *
     * @param list
     * @return
     */
    @RequestMapping("/batchUpdate")
    @Transactional
    public ResponseResult batchUpdate(@RequestBody List<StockView> list) {

        //先删除work表中该人员的临时数据
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("operator", CommonUtils.getUserCode());
        inventoryWorkMapper.delete(wrapper);

        //往work表中批量插入vin
        List<InventoryWork> works = new ArrayList();
        for (StockView sv : list) {
            InventoryWork w = new InventoryWork();
            w.setVin(sv.getVin());
            w.setOperator(CommonUtils.getUserCode());
            works.add(w);
        }
        iInventoryWorkService.saveBatch(works);

        //关联work表和stock表，抽出不存在的vin
        String vins = stockMapper.getNotExistVins(CommonUtils.getUserCode(), CommonUtils.getWarehouseCode());
        if (StringUtils.isNotEmpty(vins)) {
            return ResponseResult.buildCheck("1", "下列Vin码不存在<br>" + vins, null);
        }
        //查询无法批量更新的vin码
        vins = stockMapper.getWrongStatusVins(CommonUtils.getUserCode(), CommonUtils.getWarehouseCode());
        if (StringUtils.isNotEmpty(vins)) {
            return ResponseResult.buildCheck("1", "下列Vin码不是在库、维护或者充电状态，无法批量更新。<br>" + vins, null);
        }

        //更新状态，插入日志
        stockMapper.updateStatusAndLog(CommonUtils.getUserCode(), list.get(0).getSubCode(), list.get(0).getReason());
        inventoryWorkMapper.delete(wrapper);
        return ResponseResult.buildOK();
    }

    /**
     * 初始化在库状态查询
     *
     * @return
     */

    @RequestMapping("/searchInCarStatus")
    @ResponseBody
    public ResponseResult searchInCarStatus() {
        List<Map<String, Object>> list = stockMapper.searchInCS();
        return ResponseResult.buildOK(list);
    }


    /**
     * 在库状态查询
     *
     * @return
     */
    @RequestMapping("/searchCarStatus")
    @ResponseBody
    public ResponseResult searchCarStatus() {
        List<Map<String, Object>> list = stockMapper.searchCS();
        return ResponseResult.buildOK(list);
    }

    /**
     * 库存一览导出
     *
     * @param sv
     * @param response
     * @throws Exception
     */
    @RequestMapping("/downloadExcel")
    @ResponseBody
    @PassToken
    public void downloadExcel(
            StockView sv,
            HttpServletResponse response
    ) throws Exception {
        sv.setWarehouse(CommonUtils.getWarehouseCode());
        List<Map<String, Object>> listData = stockMapper.serchAll(sv);
        //excel列名
        List<String> titles = new ArrayList<>();
        titles.add("VIN码");
        titles.add("物料编码");
        titles.add("物料描述");
        titles.add("库位号");
        titles.add("入库日");
        titles.add("状态");
//        titles.add("锁定");

        //字段名
        List<String> columns = new ArrayList<>();
        columns.add("vin");
        columns.add("matCode");
        columns.add("name");
        columns.add("stockName");
        columns.add("warehouseDate");
        columns.add("subName");
//        columns.add("lockName");

        //文件名前缀：库存日志
        String prefixname = "kcyl";
        //下载
        ExcelUtils.downloadExcel(listData, titles, columns, prefixname, response);
    }
    /*******************************************移库********************************************************/
    /**
     * 查询移库列表
     *
     * @param stock 库存对象
     * @return
     * @author SongGuiFan
     */
    @RequestMapping("/searchMove")
    @ResponseBody
    public ResponseResult searchMove(StockView stock) {
        stock.setWarehouse(CommonUtils.getWarehouseCode());
        PageHelper.startPage(stock.getCurrentPage(), stock.getLimit());
        List<Map<String, Object>> stockMoveList = stockMapper.searchMove(stock);
        return ResponseResult.buildOK(new PageInfo<Map<String, Object>>(stockMoveList));
    }

    /**
     * 车辆移库导出
     *
     * @param stock    库存对象
     * @param response
     * @throws Exception
     * @author SongGuiFan
     */
    @RequestMapping("/exportYK")
    @ResponseBody
    @PassToken
    public void exportYK(
            StockView stock,
            HttpServletResponse response
    ) throws Exception {
        stock.setWarehouse(CommonUtils.getWarehouseCode());
        List<Map<String, Object>> stockMoveList = stockMapper.searchMove(stock);
        //excel列名
        List<String> titles = new ArrayList<>();
        titles.add("VIN码");
        titles.add("原库位");
        titles.add("新库位");

        //字段名
        List<String> columns = new ArrayList<>();
        columns.add("vin");
        columns.add("oldPosition");
        columns.add("newPosition");
        ExcelUtils.downloadExcel(stockMoveList, titles, columns, "clyk", response);

    }

    /**
     * 导入移库excel
     *
     * @param file excel文件
     * @return
     * @author SongGuiFan
     */
    @PassToken
    @RequestMapping(value = "/importYK", produces = "text/html;charset=UTF-8")
    public String importYK(MultipartFile file) throws Exception {
        String printStr = "";
        //将excel文件解析成List集合
        List<StockMoveSpWork> list = ExcelUtils.excel2ListStockMoveSpWork(file);
        for (StockMoveSpWork stock : list) {
            printStr += stock.getNewPosition() + "," + stock.getVin() + ";";
        }
        if (StringUtils.isNotEmpty(printStr)) {
            printStr = printStr.substring(0, printStr.length() - 1);
        }
        //如果没有符合的数据，则直接返回
        if (list.size() == 0) {
            return "0";
        }
        //清除移库临时表
        stockMapper.deletePoitionWork(CommonUtils.getUserCode());
        //批量插入待移库车辆数据
        stockMoveSpWorkService.saveBatch(list);
        //更新旧库位
        stockMapper.updateOldPosition(CommonUtils.getUserCode());
        return printStr;
    }

    /**
     * 车辆批量移库， 如果有错误，返回错误信息
     *
     * @return
     * @author SongGuiFan
     */
    @RequestMapping("/bachUpdatePosition")
    @Transactional
    public ResponseResult bachUpdatePosition() {
        //核对Excel数据
        List<Map<String, String>> errList = stockMapper.checkExcel(CommonUtils.getUserCode());
        //批量移库
        if (errList.size() == 0) {
            stockMapper.bachUpdatePosition(CommonUtils.getUserCode());
        }
        //清除移库临时表
        stockMapper.deletePoitionWork(CommonUtils.getUserCode());
        return ResponseResult.buildOK(errList);
    }


    /**
     * 充电计划查询
     *
     * @param pb
     * @return
     */
    @RequestMapping("/searchChargeCar")
    @ResponseBody
    public ResponseResult searchChargeCar(ChargParameterBean pb) {
        pb.setWarehouse(CommonUtils.getWarehouseCode());
        PageHelper.startPage(pb.getCurrentPage(), pb.getLimit());
        List<Map<String, Object>> list = stockMapper.searchChargeCar(pb);
        return ResponseResult.buildOK(new PageInfo<Map<String, Object>>(list));

    }

    /**
     * 充电计划出力
     *
     * @param pb
     * @param response
     * @throws Exception
     */
    @RequestMapping("/exportExcel")
    @ResponseBody
    @PassToken
    public void exportExcel(ChargParameterBean pb, HttpServletResponse response) throws Exception {
        //

        pb.setWarehouse(CommonUtils.getWarehouseCode());

        List<Map<String, Object>> list = stockMapper.searchChargeCar(pb);

        //excel列名
        List<String> titles = new ArrayList<>();
        titles.add("VIN码");
        titles.add("物料编码");
        titles.add("物料描述");
        titles.add("库位号");
        titles.add("电池类型");
        titles.add("上次充电日期");
        titles.add("天数");

        //字段名
        List<String> columns = new ArrayList<>();
        columns.add("vin");
        columns.add("matCode");
        columns.add("matName");
        columns.add("stockPosition");
        columns.add("batteryType");
        columns.add("lastChargeDate");
        columns.add("days");

        //文件名前缀：库存日志
        String prefixname = "cdrz";
        //下载
        ExcelUtils.downloadExcel(list, titles, columns, prefixname, response);
    }


    /**
     * @param pb
     * @return
     */
    @RequestMapping("/searchOutofdateStock")
    @ResponseBody
    public ResponseResult searchOutofdateStock(ChargParameterBean pb) {

        pb.setWarehouse(CommonUtils.getWarehouseCode());
        PageHelper.startPage(pb.getCurrentPage(), pb.getLimit());
        List<Map<String, Object>> list = stockMapper.searchOutofdateStock(pb);
        return ResponseResult.buildOK(new PageInfo<Map<String, Object>>(list));

    }

    /**
     * @param pb
     * @param response
     * @throws Exception
     */
    @RequestMapping("/exportOutofdateStock")
    @ResponseBody
    @PassToken
    public void exportOutofdateStock(ChargParameterBean pb, HttpServletResponse response) throws Exception {
        //

        pb.setWarehouse(CommonUtils.getWarehouseCode());
        List<Map<String, Object>> list = stockMapper.searchOutofdateStock(pb);

        //excel列名
        List<String> titles = new ArrayList<>();
        titles.add("VIN码");
        titles.add("物料编码");
        titles.add("物料描述");
        titles.add("库位号");
        titles.add("入库日");
        titles.add("天数");

        //字段名
        List<String> columns = new ArrayList<>();
        columns.add("vin");
        columns.add("matCode");
        columns.add("matName");
        columns.add("stockPosition");
        columns.add("warehouseDate");
        columns.add("days");

        //文件名前缀：库存日志
        String prefixname = "cqc";
        //下载
        ExcelUtils.downloadExcel(list, titles, columns, prefixname, response);
    }
}

