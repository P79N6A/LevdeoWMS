package com.tre.jdevtemplateboot.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tre.jdevtemplateboot.common.constant.SysConstant;
import com.tre.jdevtemplateboot.common.constant.SysConstantInfo;
import com.tre.jdevtemplateboot.common.constant.SysParamConstant;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.common.util.ExcelUtils;
import com.tre.jdevtemplateboot.common.util.PrintableUtils;
import com.tre.jdevtemplateboot.domain.po.*;
import com.tre.jdevtemplateboot.mapper.*;
import com.tre.jdevtemplateboot.service.*;
import com.tre.jdevtemplateboot.web.annotation.PassToken;
import com.tre.jdevtemplateboot.web.pojo.AsnReportBean;
import com.tre.jdevtemplateboot.web.pojo.OutBoundOrderBean;
import com.tre.jdevtemplateboot.web.pojo.OutPDICheckBean;
import com.tre.jdevtemplateboot.web.pojo.OutUpdateVinBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.servlet.http.HttpServletResponse;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * <p>
 * 出库单
 * </p>
 *
 * @author JDev
 * @since 2018-12-19
 */
@RestController
@RequestMapping("/outInvoiceDetails")
public class OutInvoiceDetailsController {

    @Autowired
    private OutInvoiceDetailsMapper outInvoiceDetailsMapper;
    @Autowired
    private ISysSpPriorityDataService iSysSpPriorityDataService;
    @Autowired
    private IOutInvoiceHeadService outInvoiceHeadService;
    @Autowired
    private IOutInvoiceDetailsService iOutInvoiceDetailsService;
    @Autowired
    private OutInvoiceHeadMapper outInvoiceHeadMapper;
    @Autowired
    private OutPDIHistoryMapper outPDIHistoryMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private IStockService iStockService;
    @Autowired
    private SAPInterfaceService sapInterfaceService;
    @Autowired
    private ISysInterfaceLogService sysInterfaceLogService;
    //系统管理员
    private String sysManagerId = CommonUtils.getSysManagerUserCode();

    //10097454 20190415 add begin
    @Autowired
    private IInventoryWorkService iInventoryWorkService;
    @Resource
    private InventoryWorkMapper inventoryWorkMapper;
    //10097454 20190415 add end

    /**
     * 查询发货单VIN 备货Page
     *
     * @param thisInvoiceCode 发货单号
     * @param orderColumn     排序
     * @param orderDir        排序
     * @return
     */
    @RequestMapping("/getThisInvoiceVIN")
    public ResponseResult getThisInvoiceVIN(@RequestParam("thisInvoiceCode") String thisInvoiceCode,
                                            @RequestParam("orderColumn") String orderColumn, @RequestParam("orderDir") String orderDir) {
        return ResponseResult.buildOK(outInvoiceDetailsMapper.getThisInvoiceVIN(thisInvoiceCode, orderColumn, orderDir));
    }

    /**
     * 更换-发货单中的VIN 备货Page
     *
     * @param outUpdateVinBean
     * @return
     */
    @RequestMapping("/updateInvoiceDetailsVIN")
    @Transactional
    public ResponseResult updateInvoiceDetailsVIN(OutUpdateVinBean outUpdateVinBean) {

        String updateId = CommonUtils.getUserCode();
        Date updateTime = CommonUtils.getCurrentDateTime();

        String oldVIN = outUpdateVinBean.getOldVIN();
        String newVIN = outUpdateVinBean.getNewVIN();

        // 验证新VIN
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("vin", newVIN);
        wrapper.eq("status", SysParamConstant.P_CAR_STATUS_INSTORAGE);// 03在库
        Integer selectCount = stockMapper.selectCount(wrapper);
        if (selectCount < 1) {
            return ResponseResult.buildCheck("-1", "新VIN码有误,请重新输入.", null);
        }

        // 更换-发货单中的VIN
        outInvoiceDetailsMapper.updateInvoiceDetailsVIN(oldVIN, newVIN, updateId, updateTime);

        Stock stock = new Stock();
        stock.setVin(newVIN);
        stock.setStatus(SysParamConstant.P_CAR_STATUS_SENDING);// 出库中
        stock.setUpdateId(updateId);
        stock.setUpdateTime(updateTime);
        // 更改stock-VIN状态-newVIN
        stockMapper.updateById(stock);
        // oldVIN
        stock.setVin(oldVIN);
        stock.setStatus(SysParamConstant.P_CAR_STATUS_INSTORAGE);// 在库
        stockMapper.updateById(stock);

        return ResponseResult.buildOK();
    }

    /**
     * 更换-出库单中的VIN 出库Page
     *
     * @param outUpdateVinBean
     * @return
     */
    @RequestMapping("/updateOutBoundVIN")
    @Transactional
    public ResponseResult updateOutBoundVIN(OutUpdateVinBean outUpdateVinBean) {

        String oldVIN = outUpdateVinBean.getOldVIN();
        String newVIN = outUpdateVinBean.getNewVIN();

        //更新发货单中的VIN
        //增加一条新的VIN在发货单表内
        //如果发货单中旧的VIN的PDI值为1（不合格）的话则换下的车的状态是维修，否则是状态为在库
        //重新修改新的VIN的状态，从在库状态改为发货状态。
        //以上修改完成后将旧的VIN从发货表中删除掉
        outInvoiceDetailsMapper.changeCarsVin(outUpdateVinBean.getInvoiceCode(), oldVIN, newVIN, CommonUtils.getUserCode());

        //库位为空的话分配库位
        Stock s = stockMapper.selectById(oldVIN);
        if (StringUtils.isEmpty(s.getStockPosition())) {
            List<String> vinList = new ArrayList<String>();
            vinList.add(oldVIN);
            iSysSpPriorityDataService.updateStockPosition(vinList);
        }
        return ResponseResult.buildOK();
    }

    /**
     * 查询出库单 出库单一览Page-查询按钮
     *
     * @param outBoundOrderBean
     * @return
     */
    @RequestMapping("/getOutBoundOrder")
    public ResponseResult getOutBoundOrder(OutBoundOrderBean outBoundOrderBean) {

        outBoundOrderBean.setWarehouse(CommonUtils.getWarehouseCode());
        //首先剔除特殊符号
        outBoundOrderBean.setOutStatusList(outBoundOrderBean.getOutStatus().split(","));

        PageHelper.startPage(outBoundOrderBean.getCurrentPage(), outBoundOrderBean.getLimit());
        // a获取出库单
        List<Map<String, Object>> list = outInvoiceDetailsMapper.getOutBoundOrder(outBoundOrderBean);

        return ResponseResult.buildOK(new PageInfo<Map<String, Object>>(list));
    }

    /**
     * 获取-状态为 待确认、完成 的出库单
     *
     * @param outBoundOrderBean
     * @return
     */
    @RequestMapping("/getConfirmAndCompleteOrders")
    public ResponseResult getConfirmAndCompleteOrders(OutBoundOrderBean outBoundOrderBean) {
        outBoundOrderBean.setWarehouse(CommonUtils.getWarehouseCode());
        outBoundOrderBean.setParamCode(SysParamConstant.PARAM_TYPE_ASN_STATUS);
        PageHelper.startPage(outBoundOrderBean.getCurrentPage(), outBoundOrderBean.getLimit());
        //获取 待确认、完成 的出库单
        List<Map<String, Object>> list = outInvoiceDetailsMapper.getConfirmAndCompleteOrders(outBoundOrderBean);

        return ResponseResult.buildOK(new PageInfo<Map<String, Object>>(list));
    }

    /**
     * 查询出库单详情 出库Page
     *
     * @param thisOutBoundCode 出库单号
     * @return
     */
    @RequestMapping("/getThisOutBoundOrder")
    public ResponseResult getThisOutBoundOrder(String thisOutBoundCode) {
        return ResponseResult.buildOK(outInvoiceDetailsMapper.getThisOutBoundOrder(thisOutBoundCode));
    }

    /**
     * 取得没有pdi检测的车辆个数
     *
     * @param sapInvoiceCode 发货单号
     * @return
     */
    @RequestMapping("/getNoPdiCheckCnt")
    public ResponseResult getNoPdiCheckCnt(@RequestParam("thisOutBoundCode") String sapInvoiceCode) {

        int cnt = outInvoiceDetailsMapper.getVinCntBySapInvoiceCode(sapInvoiceCode);
        if (cnt == 0) {
            return ResponseResult.buildCheck("1", "请添加车辆。", null);
        }

        cnt = outInvoiceDetailsMapper.getNoPdiCheckCnt(sapInvoiceCode);
        if (cnt > 0) {
            return ResponseResult.buildCheck("1", "全部车辆PDI检测合格后才能出库。", null);
        }
        return ResponseResult.buildOK();
    }

    /**
     * 查询出库单VIN 出库Page
     *
     * @param thisOutBoundCode 出库单号
     * @param orderColumn      排序
     * @param orderDir         排序
     * @return
     */
    @RequestMapping("/getThisOutBoundVIN")
    public ResponseResult getThisOutBoundVIN(@RequestParam("thisOutBoundCode") String thisOutBoundCode,
                                             @RequestParam("orderColumn") String orderColumn, @RequestParam("orderDir") String orderDir) {
        return ResponseResult.buildOK(outInvoiceDetailsMapper.getThisOutBoundVIN(thisOutBoundCode, orderColumn, orderDir));
    }

    /**
     * 车辆出库-mode1
     *
     * @param sapInvoiceCode     发货单号
     * @param logisticsParking   物流车位
     * @param logisticsVehicleNo 物流车号
     * @param giftName           随车物料
     * @param shipType           货运方式
     * @param logisCompany       物流公司
     * @return
     */
    @RequestMapping("/outBoundOperation")
    @Transactional
    public ResponseResult outBoundOperation(@RequestParam("sapInvoiceCode") String sapInvoiceCode,
                                            @RequestParam("logisticsParking") String logisticsParking,
                                            @RequestParam("logisticsVehicleNo") String logisticsVehicleNo,
                                            @RequestParam("giftName") String giftName,
                                            @RequestParam("shipType") String shipType,
                                            @RequestParam("logisCompany") String logisCompany,
                                            @RequestParam("orderTypeCode") String orderTypeCode) throws Exception {

        // 出库单信息重复-取一个
        Map<String, Object> map;
        //调用sap接口：出库单
        map = sapInterfaceService.stock_out(sapInvoiceCode, "");
        //RTN_CODE==>S:成功；E:失败
        String RTN_CODE = (String) map.get("RTN_CODE");
        String RTN_MSG = (String) map.get("RTN_MSG");

        //接口日志
        SysInterfaceLog sysInterfaceLog = new SysInterfaceLog();
        sysInterfaceLog.setInterfaceName("ZWMS_RFC_STOCK_OUT:出库单");
        sysInterfaceLog.setParam("SAP出库单号：" + sapInvoiceCode);
        sysInterfaceLog.setCreateId(sysManagerId);
        sysInterfaceLog.setUpdateId(sysManagerId);

        //成功
        if ("S".equals(RTN_CODE)) {


            String updateId = CommonUtils.getUserCode();
            Date updateTime = CommonUtils.getCurrentDateTime();

            //修改发货单-物流等字段内容
            OutInvoiceHead outHead = new OutInvoiceHead();
            outHead.setLogisticsParking(logisticsParking);
            outHead.setLogisticsVehicleNo(logisticsVehicleNo);
            outHead.setGiftName(giftName);
            outHead.setShipType(shipType);
            //出库单的话，待确认；退货单的话，已确认
            outHead.setStatus("601".equals(orderTypeCode) ? SysParamConstant.P_ASN_STATUS_WAITCONFIRM : SysParamConstant.P_ASN_STATUS_CONFIRMED);
            outHead.setLogisCompanyCode(logisCompany);
            outHead.setOutStockUser(updateId);
            outHead.setOutStockDate(updateTime);
            outHead.setUpdateId(updateId);
            outHead.setUpdateTime(updateTime);
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("sapInvoiceCode", sapInvoiceCode);
            outInvoiceHeadMapper.update(outHead, wrapper);

            //该出库单中的车辆状态由 待出库 改为 出库中
            outInvoiceDetailsMapper.chgCarStatusOfASN(sapInvoiceCode, updateId, SysParamConstant.P_CAR_STATUS_SENDING);

            sysInterfaceLog.setFlag("Y");
            sysInterfaceLogService.save(sysInterfaceLog);
        } //失败
        else {
            sysInterfaceLog.setFlag("N");
            sysInterfaceLog.setFailmsg(RTN_MSG);
            sysInterfaceLogService.save(sysInterfaceLog);
            return new ResponseResult(SysConstantInfo.SERVER_SUCCESS_CODE, RTN_MSG, null);
        }
        return ResponseResult.buildOK();
    }

    /**
     * 出库-更新giftStatus
     * 随车配件确认-确认
     *
     * @param thisOutBoundCode 出库单号
     * @return
     */
    @RequestMapping("/updateGiftStatus")
    @Transactional
    public ResponseResult updateGiftStatus(String thisOutBoundCode) {
        //随车配件状态更新：1确认。随车配件、宣传品，都已经确认过，更新出库单状态->已确认
        outInvoiceDetailsMapper.updateStatusByOutBoundCode(thisOutBoundCode, "giftChecker", "giftStatus", CommonUtils.getUserCode());
        return ResponseResult.buildOK();
    }

    /**
     * 出库-更新proStatus
     * 随车宣传品确认
     *
     * @param thisOutBoundCode 出库单号
     * @return
     */
    @RequestMapping("/updateProStatus")
    @Transactional
    public ResponseResult updateProStatus(String thisOutBoundCode) {
        //宣传品状态更新：1确认。随车配件、宣传品，都已经确认过，更新出库单状态->已确认
        outInvoiceDetailsMapper.updateStatusByOutBoundCode(thisOutBoundCode, "proChecker", "proStatus", CommonUtils.getUserCode());
        return ResponseResult.buildOK();
    }

    /**
     * 打印备货单-前台实现
     *
     * @param thisInvoiceCode 发货单单号
     * @return
     */
    @RequestMapping("/invoicecodePrint")
    public ResponseResult invoicecodePrint(@RequestParam("thisOutBoundCode") String thisInvoiceCode) {
        // a根据发货单单号查询
        Map<String, Object> headData = outInvoiceHeadMapper.getHeadDataByInvoiceCode(thisInvoiceCode);
        // a表格打印数据检索
        List<Map<String, Object>> dataList = outInvoiceDetailsMapper.getThisInvoiceVIN(thisInvoiceCode, null, null);

        if (dataList.size() == 0) {
            return ResponseResult.buildCheck(null, "没有打印数据", null);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("headData", headData);
        map.put("dataList", dataList);

        return ResponseResult.buildOK(map);
    }

    /**
     * 打印出库单-前台实现
     *
     * @param sapInvoiceCode sap出库单单号
     * @return
     */
    @RequestMapping("/outBoundPrint")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult outBoundPrint(@RequestParam("thisOutBoundCode") String sapInvoiceCode) throws Exception {
        // 根据出库单单号查询
        List<Map<String, Object>> headList = outInvoiceDetailsMapper.getThisOutBoundOrder(sapInvoiceCode);
        // 出库单信息重复-取一个
        Map<String, Object> headData = headList.get(0);
        //出库单状态
        String status = (String) headData.get("asnStatus");

        //出库单是否已经被打印
        if ("1".equals((String) headData.get("isPrint"))) {
            return ResponseResult.buildCheck("-1", "已经打印过一次不予许再次打印", null);
        }


        //获取退货单状态
        String orderTypeCode = (String) headData.get("orderTypeCode");

        //如果出库单状态为 已确定：1、调用sap接口：出库单；2、状态更新为 已出库
        Map<String, Object> map;
        if (SysParamConstant.P_ASN_STATUS_CONFIRMED.equals(status)) {
            //调用sap接口：出库单
            map = sapInterfaceService.stock_out(sapInvoiceCode, "X");
            //RTN_CODE==>S:成功；E:失败
            String RTN_CODE = (String) map.get("RTN_CODE");
            String RTN_MSG = (String) map.get("RTN_MSG");

            //接口日志
            SysInterfaceLog sysInterfaceLog = new SysInterfaceLog();
            sysInterfaceLog.setInterfaceName("ZWMS_RFC_STOCK_OUT:出库单");
            sysInterfaceLog.setParam("SAP出库单号(打印)：" + sapInvoiceCode);
            sysInterfaceLog.setCreateId(sysManagerId);
            sysInterfaceLog.setUpdateId(sysManagerId);

            //成功
            if ("S".equals(RTN_CODE)) {
                //更新出库单状态为 已出库
                OutInvoiceHead head = new OutInvoiceHead();
                head.setStatus(SysParamConstant.P_ASN_STATUS_SENDED);
                head.setSapInvoiceCode(sapInvoiceCode);
                head.setUpdateId(CommonUtils.getUserCode());
                head.setUpdateTime(new Date());
                QueryWrapper<OutInvoiceHead> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sapInvoiceCode", sapInvoiceCode);
                outInvoiceHeadService.update(head, queryWrapper);

                //当订单类型为653销售退货单时，分配库位号，车辆入库
                if ("653".equals(orderTypeCode)) {
                    //分配库位
                    List<Map<String, Object>> detailList = outInvoiceDetailsMapper.getThisInvoiceVIN(sapInvoiceCode, null, null);
                    List<String> vinList = new ArrayList<>();
                    for (Map<String, Object> m : detailList) {
                        vinList.add(m.get("vin").toString());
                    }
                    iSysSpPriorityDataService.updateStockPosition(vinList);

                    //检索当前出库单所有的VIN码 并修改为在库状态
                    outInvoiceDetailsMapper.outBoundForReturn(sapInvoiceCode, CommonUtils.getUserCode());

                    //当订单类型是601销售出库单时，释放库位，车辆出库
                } else if ("601".equals(orderTypeCode)) {
                    outInvoiceDetailsMapper.outBound(sapInvoiceCode, CommonUtils.getUserCode());
                }

                sysInterfaceLog.setFlag("Y");
                sysInterfaceLogService.save(sysInterfaceLog);
            }
            //失败
            else {
                sysInterfaceLog.setFlag("N");
                sysInterfaceLog.setFailmsg(RTN_MSG);
                sysInterfaceLogService.save(sysInterfaceLog);

                return new ResponseResult(SysConstantInfo.SERVER_SUCCESS_CODE, RTN_MSG, null);
            }
        }

        // a表格打印数据检索
        List<Map<String, Object>> dataList = outInvoiceDetailsMapper.getThisOutBoundVIN(sapInvoiceCode, null, null);
        if (dataList.size() == 0 || headList.size() == 0) {
            return ResponseResult.buildCheck("-1", "没有打印数据", null);
        }

        map = new HashMap<>();
        map.put("headData", headData);
        map.put("dataList", dataList);

        //全部执行成功后将打印出库单的状态改为已打印
        outInvoiceHeadMapper.printInvoice((String) headData.get("sapInvoiceCode"));

        return ResponseResult.buildOK(map);
    }

    /**
     * 更新出库单PDI检测
     *
     * @param outPDICheckBean
     * @return
     */
    @PostMapping("/updatePDICheck")
    @Transactional
    @PassToken
    public ResponseResult updatePDICheck(@RequestBody OutPDICheckBean outPDICheckBean) {

        String vin = outPDICheckBean.getVin();
        String pdiResult = outPDICheckBean.getPdiResult();
        String inspector = outPDICheckBean.getInspector();
        String reason = outPDICheckBean.getReason();
        //a 验证前台传入参数
        if (("").equals(vin) || ("").equals(pdiResult) || ("").equals(inspector)) {
            return ResponseResult.buildCheck("-1", "数据不可为空.", null);
        }

        String userCode = inspector;
        /**
         * 获取warehouse-By userCode
         */
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("userCode", userCode);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        if (sysUser == null) {
            return ResponseResult.buildCheck("-1", "该工号不存在.", null);
        }
        String warehouse = sysUser.getWarehouse();
        Date updatetime = CommonUtils.getCurrentDateTime();
        /**
         * 通过VIN-查询发货单 是否存中发货单中-且状态为 02出库中
         */
        Map<String, Object> map = outInvoiceDetailsMapper.getInvoiceByVin(vin, warehouse);
        if (map == null) {
            return ResponseResult.buildCheck("-1", "当前VIN码未在出库单中.", null);
        }

        /**
         * 判断-合格-0合格-1不合格 不合格-更新PDIHistory表
         *
         */
        String invoiceCode = map.get("sapInvoiceCode").toString();
        if (("1").equals(pdiResult)) {
            OutPDIHistory outPDIHistory = new OutPDIHistory();
            outPDIHistory.setInvoiceCode(invoiceCode);
            outPDIHistory.setOutBoundCode(map.get("outBoundCode").toString());
            outPDIHistory.setVin(vin);
            outPDIHistory.setInspector(inspector);
            outPDIHistory.setReason(reason);
            outPDIHistory.setCreateId(userCode);
            outPDIHistory.setCreateTime(updatetime);
            outPDIHistory.setUpdateId(userCode);
            outPDIHistory.setUpdateTime(updatetime);
            /**
             * 删除已存在的记录
             */
            QueryWrapper wrapperDel = new QueryWrapper();
            wrapperDel.eq("vin", vin);
            wrapperDel.eq("invoiceCode", invoiceCode);
            outPDIHistoryMapper.delete(wrapperDel);
            /**
             * 更新PDI履历表
             */
            outPDIHistoryMapper.insert(outPDIHistory);

        } else {
            //a 合格-清空理由参数
            reason = "";
        }

        OutInvoiceDetails outDetailPo = new OutInvoiceDetails();
        outDetailPo.setPdi(pdiResult);
        outDetailPo.setInspector(inspector);
        outDetailPo.setReason(reason);
        outDetailPo.setUpdateId(userCode);
        outDetailPo.setUpdateTime(updatetime);
        /**
         * 更新-outDetails表
         */
        QueryWrapper wrapper2 = new QueryWrapper();
        wrapper2.eq("vin", vin);
        outInvoiceDetailsMapper.update(outDetailPo, wrapper2);

        return ResponseResult.buildOK();
    }

    /**
     * 打印备货单-后台实现
     *
     * @param thisInvoiceCode 发货单单号
     * @return
     */
    @RequestMapping("/invoicecodePrintBK")
    public ResponseResult invoicecodePrintBK(@RequestParam("thisInvoiceCode") String thisInvoiceCode) {
        // 根据发货单单号查询
        Map<String, Object> headData = outInvoiceHeadMapper.getHeadDataByInvoiceCode(thisInvoiceCode);
        // 表格打印数据检索
        List<Map<String, Object>> dataList = outInvoiceDetailsMapper.getThisInvoiceVIN(thisInvoiceCode, null, null);

        if (dataList.size() == 0) {
            return ResponseResult.buildCheck(null, "没有打印数据", null);
        }
        // 每页显示数据条数
        int rowsperpage = 5;
        // 创建打印文档
        Book book = new Book();
        // 设置成横打
        PageFormat pf = new PageFormat();
        pf.setOrientation(PageFormat.PORTRAIT);
        // 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
        Paper p = new Paper();
        p.setSize(683.15, 396.85);// 纸张大小 (单位 1/72 英寸) 24.1mm*140mm
        p.setImageableArea(5, 5, 673.15, 386.85);// A4(595 X 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72
        pf.setPaper(p);
        // 把 PageFormat 和 Printable 添加到书中，组成一个页面
        PrintableUtils ptu = new PrintableUtils(headData, dataList, rowsperpage);
        for (int i = 0; i <= dataList.size() / rowsperpage; i++) {
            book.append(ptu, pf);
        }

        // 获取打印服务对象
        PrinterJob job = PrinterJob.getPrinterJob();
        // 设置打印类
        job.setPageable(book);
        // 指定打印机
        HashAttributeSet hs = new HashAttributeSet();
//		String printerName = "Aisino SK-820";
        String printerName = "Adobe PDF";
        hs.add(new PrinterName(printerName, null));
        PrintService[] pss = PrintServiceLookup.lookupPrintServices(null, hs);
        if (pss.length == 0) {
            return ResponseResult.buildCheck(null, "无法找到打印机:" + printerName, null);
        }
        try {
            // 指定打印机
            job.setPrintService(pss[0]);
            // printDialog显示打印对话框，在用户确认后打印；也可以直接打印
            boolean a = job.printDialog();
            if (a) {
                job.print();
            } else {
                job.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseResult.buildOK();
    }


    /**
     * 冲销
     *
     * @param sapInvoiceCode sap发货单号
     * @param orderTypeCode
     * @return
     */
    @RequestMapping("/doWash")
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult doWash(@RequestParam("thisOutBoundCode") String sapInvoiceCode, @RequestParam("orderTypeCode") String orderTypeCode) throws Exception {
        //调用sap冲销接口
        Map<String, Object> map = sapInterfaceService.wash_trans(sapInvoiceCode);
        String RTN_CODE = (String) map.get("RTN_CODE");
        String RTN_MSG = (String) map.get("RTN_MSG");

        //接口日志
        SysInterfaceLog sysInterfaceLog = new SysInterfaceLog();
        sysInterfaceLog.setInterfaceName("ZWMS_RFC_WASH_TRANS:出库单冲销");
        sysInterfaceLog.setParam("SAP出库单号：" + sapInvoiceCode);
        sysInterfaceLog.setCreateId(sysManagerId);
        sysInterfaceLog.setUpdateId(sysManagerId);

        //成功：进行冲销
        if ("S".equals(RTN_CODE)) {
            //销售出库单
            if ("601".equals(orderTypeCode)) {
                outInvoiceHeadMapper.doWash(sapInvoiceCode, CommonUtils.getUserCode(),
                        SysParamConstant.P_ASN_STATUS_WAITSEND, SysParamConstant.P_CAR_STATUS_WAITSEND, SysParamConstant.P_STOCKLOG_WASH);

                //退货单
            } else if ("653".equals(orderTypeCode)) {
                outInvoiceHeadMapper.doWashForReturn(sapInvoiceCode, CommonUtils.getUserCode());
            }

            sysInterfaceLog.setFlag("Y");
            sysInterfaceLogService.save(sysInterfaceLog);
            //全部执行成功后将重置打印次数
            outInvoiceHeadMapper.cancelPrintInvoice(sapInvoiceCode);
        }
        //失败
        else {
            sysInterfaceLog.setFlag("N");
            sysInterfaceLog.setFailmsg(RTN_MSG);
            sysInterfaceLogService.save(sysInterfaceLog);

            return new ResponseResult(SysConstantInfo.SERVER_SUCCESS_CODE, RTN_MSG, null);
        }
        return ResponseResult.buildOK();
    }

    /**
     * 出库单报表
     *
     * @param bean
     * @return
     */
    @RequestMapping("/getAsnReport")
    public ResponseResult getAsnReport(AsnReportBean bean) {
        bean.setWarehouse(CommonUtils.getWarehouseCode());
        bean.setSaleCompanyList(bean.getSaleCompany().split(","));
        PageHelper.startPage(bean.getCurrentPage(), bean.getLimit());
        List<Map<String, Object>> list = outInvoiceDetailsMapper.getAsnReport(bean);
        return ResponseResult.buildOK(new PageInfo<Map<String, Object>>(list));
    }

    /**
     * 出库单报表出力
     *
     * @param bean
     * @param response
     * @throws Exception
     */
    @RequestMapping("/exportAsnReport")
    @ResponseBody
    @PassToken
    public void exportAsnReport(AsnReportBean bean, HttpServletResponse response) throws Exception {
        bean.setWarehouse(CommonUtils.getWarehouseCode());
        bean.setSaleCompanyList(bean.getSaleCompany() == null ? null : bean.getSaleCompany().split(","));
        List<Map<String, Object>> list = outInvoiceDetailsMapper.getAsnReport(bean);

        //excel列名
        List<String> titles = new ArrayList<>();
        titles.add("客户名称");
        titles.add("销售公司");
        titles.add("发货单号");
        titles.add("行项目号");
        titles.add("物料编码");
        titles.add("物料描述");
        titles.add("数量");
        titles.add("VIN码");
        titles.add("出库仓库");
        titles.add("库位");
        titles.add("出库日期");
        titles.add("销售单类型");
        titles.add("状态");
        titles.add("已打印次数");

        //字段名
        List<String> columns = new ArrayList<>();
        columns.add("customerName");
        columns.add("companyName");
        columns.add("sapInvoiceCode");
        columns.add("itemNo");
        columns.add("matCode");
        columns.add("matName");
        columns.add("amount");
        columns.add("vin");
        columns.add("productStockName");
        columns.add("stockPositionName");
        columns.add("outBoundDate");
        columns.add("orderType");
        columns.add("outStatus");
        columns.add("printCnt");

        //文件名前缀：库存日志
        String prefixname = "rp";
        //下载
        ExcelUtils.downloadExcel(list, titles, columns, prefixname, response);
    }

    /**
     * 10097454 20190415 车辆出库司机更新
     *
     * @param file
     * @param userCode
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/pdaUploadOutStockApi", method = RequestMethod.POST)
    @PassToken
    @Transactional
    public String pdaUploadOutStockApi(@RequestParam(value = "ckFile") MultipartFile file, @RequestParam(value = "userCode") String userCode) throws IOException, ParseException {
        SysUser u = sysUserMapper.selectById(userCode);
        if (u == null) {
            return "-1";//用户不存在。
        }

        //匹配VIN码
        String pattern = "[A-Z|a-z|\\d]{17}";
        //先删除work表中该人员的临时数据
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("operator", userCode);
        inventoryWorkMapper.delete(wrapper);

        //往work表中批量插入vin
        List<InventoryWork> works = new ArrayList();
        String vins = new String(file.getBytes(), "UTF-8");
        String[] vinList = vins.split("\\r\\n|\\n\\r|\\n|\\r");
        for (String vin : vinList) {
            if (!"".equals(vin.replace(" ", ""))) {//判断获取的VIN码是否有空值
                if (!Pattern.matches(pattern, vin)) {
                    continue;
                }
                InventoryWork w = new InventoryWork();
                w.setVin(vin.toUpperCase());
                w.setOperator(userCode);
                works.add(w);
            }
        }
        iInventoryWorkService.saveBatch(works);
        //更新出库司机
        outInvoiceDetailsMapper.updateOutStockDriver(userCode, userCode);
        return "0";//上传成功。
    }

    /**
     * 添加车辆，获取VIN
     *
     * @param outBoundOrderBean sap发货单号
     * @return
     */
    @RequestMapping("/getAddVins")
    public ResponseResult getAddVins(OutBoundOrderBean outBoundOrderBean) {
        outBoundOrderBean.setOutStatusList(new String[]{SysParamConstant.P_CAR_STATUS_INSTORAGE, SysParamConstant.P_CAR_STATUS_CHARGE});
        List<Map<String, Object>> list = outInvoiceDetailsMapper.getAddVins(outBoundOrderBean);
        return ResponseResult.buildOK(list);
    }

    /**
     * 添加车辆
     *
     * @param sapInvoiceCode
     * @param vins
     * @return
     */
    @RequestMapping("/addVins")
    @Transactional
    public ResponseResult addVins(@RequestParam("sapInvoiceCode") String sapInvoiceCode, @RequestParam("vins") String vins) {

        QueryWrapper wrapper = new QueryWrapper();

        //取得车辆物料
        wrapper.in("vin", vins.split(","));
        wrapper.orderByAsc("matCode");
        List<Stock> stockList = stockMapper.selectList(wrapper);

        //取得行项目单位的需求数和已分配数
        List<Map<String, Object>> invoiceAmoutList = outInvoiceDetailsMapper.getReAndAllot(sapInvoiceCode);

        int amoutIndex = 0;
        Map<String, String> amoutMap = new HashMap<>();
        List<OutInvoiceDetails> oidList = new ArrayList<>();
        //
        for (Stock stock : stockList) {

            //先循环到所添加的物料的下标
            while (!stock.getMatCode().equals(invoiceAmoutList.get(amoutIndex).get("matCode"))) {
                amoutIndex++;
            }
            //如果已分配的数量大于需求数量，则判断该物料是否还有其他行项目（下一条数据）。如果有，则往其他行项目上分配；如果没有，则继续往该行项目上分配
            while (Integer.parseInt(invoiceAmoutList.get(amoutIndex).get("allotAmount").toString()) >= Integer.parseInt(invoiceAmoutList.get(amoutIndex).get("amount").toString())) {
                if (amoutIndex < invoiceAmoutList.size() - 1 && stock.getMatCode().equals(invoiceAmoutList.get(amoutIndex + 1).get("matCode"))) {
                    amoutIndex++;
                } else {
                    break;
                }
            }
            //分配数量+1
            invoiceAmoutList.get(amoutIndex).put("allotAmount", Integer.parseInt(invoiceAmoutList.get(amoutIndex).get("allotAmount").toString()) + 1);

            //添加车辆准备插入
            OutInvoiceDetails oid = new OutInvoiceDetails();
            oid.setInvoiceCode(invoiceAmoutList.get(amoutIndex).get("invoiceCode").toString());
            oid.setVin(stock.getVin());
            oid.setCreateId(CommonUtils.getUserCode());
            oid.setCreateTime(CommonUtils.getCurrentDateTime());
            oid.setUpdateId(CommonUtils.getUserCode());
            oid.setUpdateTime(CommonUtils.getCurrentDateTime());
            oidList.add(oid);

            //车辆状态更新为 待出库
            stock.setStatus(SysParamConstant.P_CAR_STATUS_WAITSEND);
            stock.setUpdateId(CommonUtils.getUserCode());
            stock.setUpdateTime(CommonUtils.getCurrentDateTime());
        }

        //添加车辆
        iOutInvoiceDetailsService.saveBatch(oidList);
        //更新车辆状态
        iStockService.updateBatchById(stockList);

        return ResponseResult.buildOK();
    }


    /**
     * 删除车辆
     *
     * @param oid
     * @return
     */
    @RequestMapping("/deleteFromOidByKey")
    @Transactional
    public ResponseResult deleteFromOidByKey(OutInvoiceDetails oid) {

        //从明细表中删除
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("invoiceCode", oid.getInvoiceCode());
        wrapper.eq("vin", oid.getVin());
        outInvoiceDetailsMapper.delete(wrapper);

        //更新车辆状态为在库
        Stock stock = new Stock();
        stock.setVin(oid.getVin());
        stock.setStatus(SysParamConstant.P_CAR_STATUS_INSTORAGE);
        stock.setUpdateId(CommonUtils.getUserCode());
        stock.setUpdateTime(CommonUtils.getCurrentDateTime());
        stockMapper.updateById(stock);

        //如果库位为空，分配库位号
        stock = stockMapper.selectById(oid.getVin());
        if(StringUtils.isEmpty(stock.getStockPosition())){
            List<String> vins = new ArrayList<>();
            vins.add(oid.getVin());
            iSysSpPriorityDataService.updateStockPosition(vins);
        }

        return ResponseResult.buildOK();
    }
}
