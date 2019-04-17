package com.tre.jdevtemplateboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.jdevtemplateboot.common.constant.SysParamConstant;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.domain.po.SysStockPosition;
import com.tre.jdevtemplateboot.domain.po.SysUser;
import com.tre.jdevtemplateboot.domain.po.WarehousingSchedule;
import com.tre.jdevtemplateboot.mapper.*;
import com.tre.jdevtemplateboot.service.SysJobService;
import com.tre.jdevtemplateboot.web.pojo.StockView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysJobServiceImpl implements SysJobService {
    @Autowired
    private SysJobMapper SysJobMapper;
    @Autowired
    private WarehousingMapper WarehousingMapper;
    @Autowired
    private WarehousingScheduleMapper WarehousingScheduleMapper;
    @Autowired
    private SysSpPriorityDataMapper SysSpPriorityDataMapper;
    @Autowired
    private SysStockPositionMapper SysStockPositionMapper;
    @Autowired
    private SysUserMapper SysUserMapper;

    //使用状态-使用中
    String IN_USE_YES = SysParamConstant.P_IN_USE;
    String sysUserCode = CommonUtils.getSysManagerUserCode();
    Date dateTime;
    int successCNT;

    /**
     * 分配发货单：获取各个仓库待分配的发货单号，进行分配
     * 1、查找所有 00:待分配 的发货单
     *
     * @return
     */
    @Override
    public Map<String, Object> doSendOut() {
        //outInvoiceHead 中查找所有 00:待分配 的发货单
        List<Map<String, Object>> assignedInvoiceList = SysJobMapper.searchAssignedInvoice();

        int successCnt = 0;
        int failCnt = 0;
        String success = "";
        String fail = "";
        //循环
        Map<String, Object> map;
        String thissuccess = "";
        String thisfail = "";
        for (Map<String, Object> assignedInvoice : assignedInvoiceList) {
            map = doSendOut(assignedInvoice);
            thissuccess = (String) map.get("success");
            thisfail = (String) map.get("fail");
            if ("".equals(thissuccess)) {
                failCnt++;
                fail += thisfail;
            } else {
                successCnt++;
                success += thissuccess;
            }
        }

        Map<String, Object> resultmap = new HashMap<>();
        resultmap.put("successCnt", successCnt);
        resultmap.put("failCnt", failCnt);
        resultmap.put("success", success);
        resultmap.put("fail", fail);
        return resultmap;
    }

    /**
     * 分配发货单：获取各个仓库待分配的发货单号，进行分配
     * 2、待分配的发货单进行分配
     *
     * @param assignedInvoice 待分配的发货单
     * @return
     */
    @Transactional
    public Map<String, Object> doSendOut(Map<String, Object> assignedInvoice) {
        //库存表中，取得待分配车辆数量
        int cnt = 0;
        try {
            cnt = SysJobMapper.searchAssignedStocksCnt(assignedInvoice);
        } catch (Exception e) {
        }

        //订单需要的车辆数量
        int amount = 0;
        try {
            amount = Integer.parseInt((String) assignedInvoice.get("amount"));
        } catch (Exception e) {
        }

        //发货单号
        String invoiceCode = (String) assignedInvoice.get("invoiceCode");
        //sap发货单号
        String sapInvoiceCode = (String) assignedInvoice.get("sapInvoiceCode");
        //物料编码
        String matCode = (String) assignedInvoice.get("matCode");

        //分配成功的发货单
        String success = "";
        //分配失败的发货单
        String fail = "";

        try {
            if (amount == 0) {
                fail = "发货单号：" + invoiceCode + "(sap发货单号：" + sapInvoiceCode + ",物料编码：matCode)需求数量为0或空；";
            } else if (cnt < amount) {
                fail = "发货单号：" + invoiceCode + "(sap发货单号：" + sapInvoiceCode + ",物料编码：matCode)库存不足；";
            } else {
                //库存表中按时间从早到晚，取得待分配车辆，插入到发货单详情表outInvoiceDetails
                assignedInvoice.put("sysUserCode", sysUserCode);
                SysJobMapper.insertAssignedStocks(assignedInvoice);

                //更新 outInvoiceHead 发货单状态 00：待分配->01：待发货
                SysJobMapper.updateAssignedInvoice(assignedInvoice);

                //更新 stock 库存表中取得的待分配车辆状态 03：待分配->04：待发货
                SysJobMapper.updateAssignedStocks(assignedInvoice);

                success = "发货单号：" + invoiceCode + "(sap发货单号：" + sapInvoiceCode + ",物料编码：matCode)分配成功；";
            }
        } catch (Exception e) {
            fail = "发货单号：" + invoiceCode + "(sap发货单号：" + sapInvoiceCode + ",物料编码：matCode)" + e.getCause().getMessage() + "；";
        }

        Map<String, Object> map = new HashMap<>();
        map.put("success", success);
        map.put("fail", fail);
        return map;
    }

    /**
     * warehousingSchedule表中，所有stockPosition（库位号）为null的记录，
     * 根据库位优先级规则，更新stockPosition（库位号）字段。
     * 1、查找warehousingSchedule表中，所有stockPosition（库位号）为null的记录
     */
    @Override
    public Map<String, Object> updateStockPosition() {
        dateTime = CommonUtils.getCurrentDateTime();

        //查找warehousingSchedule表中，所有stockPosition（库位号）为空的记录
        QueryWrapper<WarehousingSchedule> wrapper = new QueryWrapper<>();
        wrapper.isNull("stockPosition")
                .or()
                .eq("LTRIM(RTRIM(stockPosition))", "");
        List<WarehousingSchedule> wareList = WarehousingScheduleMapper.selectList(wrapper);

        int successCnt = 0;
        int failCnt = 0;
        String success = "";
        String fail = "";

        if (wareList.size() == 0) {
            success = "没有库位号为空的数据。";
        } else {
            //循环
            Map<String, Object> map;
            String thissuccess = "";
            String thisfail = "";
            for (WarehousingSchedule ware : wareList) {
                map = updateStockPosition(ware);
                thissuccess = (String) map.get("success");
                thisfail = (String) map.get("fail");
                if ("".equals(thissuccess)) {
                    failCnt++;
                    fail += thisfail;
                } else {
                    successCnt++;
                    success += thissuccess;
                }
            }
        }

        Map<String, Object> resultmap = new HashMap<>();
        resultmap.put("successCnt", successCnt);
        resultmap.put("failCnt", failCnt);
        resultmap.put("success", success);
        resultmap.put("fail", fail);
        return resultmap;
    }

    /**
     * warehousingSchedule表中，所有stockPosition（库位号）为null的记录，
     * 根据库位优先级规则，更新stockPosition（库位号）字段。
     * 2、更新库位状态-使用中；更新VIN对应的库位号
     */
    @Transactional
    public Map<String, Object> updateStockPosition(WarehousingSchedule ware) {
        String success = "";
        String fail = "";
        dateTime = CommonUtils.getCurrentDateTime();
        //入库交接单
        String wsTransition = ware.getWsTransition();
        String vin = ware.getVin();
        //根据createId，查找仓库编码warehouseCode
        String createId = ware.getCreateId();

        try {
            SysUser sysUser = SysUserMapper.selectById(createId);
            //仓库编码
            String warehouseCode = sysUser.getWarehouse();

            //获取vin对应的物料编码
            StockView sv = WarehousingMapper.getMatCodeByVinFromWs(vin);
            if (sv == null) {
                fail = wsTransition + "-" + vin + "：vin不存在；";
            } else {
                //拼接-优先级规则
                String brand = sv.getMatCode().substring(0, 1);
                String color = sv.getMatCode().substring(10, 11);
                String series[] = sv.getMatDescription().split("\\.");
                String priorityRule = brand + "_" + color + "_" + series[2];

                //获取可用的库位
                List<Map<String, Object>> list = SysSpPriorityDataMapper.getSpByPrioRule(warehouseCode, priorityRule);

                if (list == null || list.size() == 0) {
                    fail = wsTransition + "-" + vin + "：没有可用的库位；";
                } else {
                    String stockPosition = list.get(0).get("stockPosition").toString();
                    SysStockPosition sysStockPosition = new SysStockPosition();
                    sysStockPosition.setStockPosition(stockPosition);
                    sysStockPosition.setInUse(IN_USE_YES);
                    sysStockPosition.setUpdateId(sysUserCode);
                    sysStockPosition.setUpdateTime(dateTime);
                    //更新库位状态-使用中
                    SysStockPositionMapper.updateById(sysStockPosition);
                    ware.setStockPosition(stockPosition);
                    ware.setUpdateId(sysUserCode);
                    ware.setUpdateTime(dateTime);
                    //更新VIN对应的库位号
                    WarehousingScheduleMapper.updateById(ware);

                    success = wsTransition + "-" + vin + "：分配库位号成功；";
                }
            }
        } catch (Exception e) {
            fail = wsTransition + "-" + vin + "：" + e.getCause().getMessage() + "；";
        }

        Map<String, Object> map = new HashMap<>();
        map.put("success", success);
        map.put("fail", fail);
        return map;
    }

    /**
     * 获取ws表对应的数据进行更新库位号
     * @param warehousingSchedules
     * @return
     */

    @Override
    public int updateStockPosition(List<WarehousingSchedule> warehousingSchedules) {

        if (warehousingSchedules.size() <= 0) {
            successCNT = warehousingSchedules.size();
        } else {
            successCNT = 0;
            for (WarehousingSchedule ware : warehousingSchedules) {
                successCNT += updateStockPositions(ware);
            }
        }
        return successCNT;
    }

    /**
     * warehousingSchedule表中，所有stockPosition（库位号）为null的记录，
     * 根据库位优先级规则，更新stockPosition（库位号）字段。
     * 2、更新库位状态-使用中；更新VIN对应的库位号
     */
    @Transactional
    public int updateStockPositions(WarehousingSchedule ware) {
        dateTime = CommonUtils.getCurrentDateTime();

        String vin = ware.getVin();
        //根据createId，查找仓库编码warehouseCode
        String createId = ware.getCreateId();

        SysUser sysUser = SysUserMapper.selectById(createId);
        if (sysUser == null) {
            return successCNT;
        }
        //仓库编码
        String warehouseCode = sysUser.getWarehouse();

        //获取vin对应的物料编码
        StockView sv = WarehousingMapper.getMatCodeByVinFromWs(vin);
        if (sv == null) {
            return successCNT;
        } else {
            //拼接-优先级规则
            String brand = sv.getMatCode().substring(0, 1);
            String color = sv.getMatCode().substring(10, 11);
            String series[] = sv.getMatDescription().split("\\.");
            String priorityRule = brand + "_" + color + "_" + series[2];

            //获取可用的库位
            List<Map<String, Object>> list = SysSpPriorityDataMapper.getSpByPrioRule(warehouseCode, priorityRule);

            if (list == null || list.size() == 0) {
                return successCNT;
            } else {
                String stockPosition = list.get(0).get("stockPosition").toString();
                SysStockPosition sysStockPosition = new SysStockPosition();
                sysStockPosition.setStockPosition(stockPosition);
                sysStockPosition.setInUse(IN_USE_YES);
                sysStockPosition.setUpdateId(sysUserCode);
                sysStockPosition.setUpdateTime(dateTime);
                //更新库位状态-使用中
                SysStockPositionMapper.updateById(sysStockPosition);
                ware.setStockPosition(stockPosition);
                ware.setUpdateId(sysUserCode);
                ware.setUpdateTime(dateTime);
                //更新VIN对应的库位号
                WarehousingScheduleMapper.updateById(ware);
                successCNT += 1;
            }
        }
        return successCNT;
    }
}
