package com.tre.jdevtemplateboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sap.conn.jco.*;
import com.sap.conn.jco.server.JCoServerContext;
import com.tre.jdevtemplateboot.common.constant.SysConstant;
import com.tre.jdevtemplateboot.common.constant.SysParamConstant;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.domain.po.*;
import com.tre.jdevtemplateboot.exception.SysExceptionHandler;
import com.tre.jdevtemplateboot.mapper.OutInvoiceDetailsMapper;
import com.tre.jdevtemplateboot.mapper.StockMapper;
import com.tre.jdevtemplateboot.mapper.SysSpPriorityDataMapper;
import com.tre.jdevtemplateboot.mapper.WarehousingMapper;
import com.tre.jdevtemplateboot.service.*;
import com.tre.jdevtemplateboot.web.pojo.StockView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SAPInterfaceServiceImpl implements SAPInterfaceService {
    //配置文件是dev还是product：dev不运行；product运行
    @Value("${spring.profiles.active}")
    private String applicationType;
    @Autowired
    private ISysInterfaceLogService sysInterfaceLogService;
    @Autowired
    private IWarehousingScheduleWorkService warehousingScheduleWorkService;
    @Autowired
    private IOutInvoiceHeadService outInvoiceHeadService;
    @Autowired
    private IOutInvoiceDetailsService outInvoiceDetailsService;
    @Autowired
    private OutInvoiceDetailsMapper outInvoiceDetailsMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IStockService stockService;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private IStockLogService stockLogService;
    @Autowired
    private ISysStockPositionService sysStockPositionService;
    @Autowired
    private WarehousingMapper warehousingMapper;
    @Autowired
    private SysSpPriorityDataMapper sysSpPriorityDataMapper;
    @Autowired
    private IMainMatService mainMatService;
    @Autowired
    private IMainCustomersService mainCustomersService;

    //系统管理员
    private String sysManagerId = CommonUtils.getSysManagerUserCode();
    private String DESTINATION_WITH_POOL = "ABAP_AS_WITH_POOL";

    //Log
    private static Logger LOGGER = LoggerFactory.getLogger(SysExceptionHandler.class);

    //==========================================入库交接单 START========================================

    /**
     * 入库交接单
     *
     * @param serverCtx
     * @param function
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void storage_rec(JCoServerContext serverCtx, JCoFunction function) {
        //获取SAP传过来的参数
        JCoTable table = function.getTableParameterList().getTable(0);
        //传给SAP的返回值
        JCoParameterList exports = function.getExportParameterList();
        //接口日志
        SysInterfaceLog sysInterfaceLog = new SysInterfaceLog();
        sysInterfaceLog.setInterfaceName("ZWMS_RFC_STORAGE_REC:入库交接单");
        sysInterfaceLog.setParam(table.toString());
        sysInterfaceLog.setCreateId(sysManagerId);
        sysInterfaceLog.setUpdateId(sysManagerId);

        List<WarehousingScheduleWork> wsWorks = new ArrayList<>();
        WarehousingScheduleWork wsWork = new WarehousingScheduleWork();

        try {
            for (int i = 0; i < table.getNumRows(); i++) {
                table.setRow(i);

                //交接单号
                wsWork.setWsTransition(table.getString("ZLS"));
                //成品库
                wsWork.setProductStockCode(table.getString("LGOBE"));
                //完工日期
                wsWork.setFinishedTime(table.getString("GRDAT"));
                //物料编码
                wsWork.setMatCode(table.getString("MATNR"));
                //VIN码
                wsWork.setVin(table.getString("ZVIN"));
                //过账状态
                wsWork.setSapTransfer(table.getString("ZBSAT"));
                //取消过账标识
                wsWork.setCancelSapTransfer(table.getString("CXEKZ"));
                //制单人
                wsWork.setSapUserCode(table.getString("ERNAM"));

                wsWorks.add(wsWork);
            }

            //执行
            String result = warehousingScheduleWorkService.rfcStorageRec(wsWorks);
            if ("".equals(result)) {
                sysInterfaceLog.setFlag("Y");
                sysInterfaceLogService.save(sysInterfaceLog);

                exports.setValue("RTN_CODE", SysParamConstant.P_API_RESULT_SUCCESS);
                exports.setValue("RTN_MSG", "执行成功");
            } else {
                sysInterfaceLog.setFlag("N");
                sysInterfaceLog.setFailmsg(result);
                sysInterfaceLogService.save(sysInterfaceLog);

                exports.setValue("RTN_CODE", SysParamConstant.P_API_RESULT_FAIL);
                exports.setValue("RTN_MSG", result);
            }
        } catch (Exception e) {
            exports.setValue("RTN_CODE", SysParamConstant.P_API_RESULT_FAIL);
            exports.setValue("RTN_MSG", e.toString());

            sysInterfaceLog.setFlag("N");
            sysInterfaceLog.setFailmsg(e.toString());
            sysInterfaceLogService.save(sysInterfaceLog);

            LOGGER.error("异常:" + e.getMessage(), e);
        }
    }
    //==========================================入库交接单 END==========================================

    //==========================================发货通知单 START========================================

    /**
     * 发货通知单
     *
     * @param serverCtx
     * @param function
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void consignment_note(JCoServerContext serverCtx, JCoFunction function) {
        //获取SAP传过来的参数
        JCoTable table = function.getTableParameterList().getTable(0);
        JCoTable tableVin = function.getTableParameterList().getTable(1);
        //传给SAP的返回值
        JCoParameterList exports = function.getExportParameterList();

        //接口日志
        SysInterfaceLog sysInterfaceLog = new SysInterfaceLog();
        sysInterfaceLog.setInterfaceName("ZWMS_RFC_CONSIGNMENT_NOTE:发货通知单");
        sysInterfaceLog.setParam(table.toString() + tableVin.toString());
        sysInterfaceLog.setCreateId(sysManagerId);
        sysInterfaceLog.setUpdateId(sysManagerId);

        //当前时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        //failMessage=="" ==> 执行成功；failMessage!="" ==> 执行失败
        String failMessage = "";

        try {
            //根据SAP员工号，查询WMS员工信息
            table.setRow(0);
            QueryWrapper<SysUser> userWrapper = new QueryWrapper<>();
            userWrapper.eq("sapUserCode", table.getString("ERNAM"));
            SysUser sysUser = sysUserService.getOne(userWrapper);
            if (sysUser == null) {
                failMessage += "制单人不存在。";
            } else {

                for (int i = 0; i < table.getNumRows(); i++) {
                    table.setRow(i);

                    OutInvoiceHead head = new OutInvoiceHead();

                    //SAP发货单号，左侧去0
                    head.setSapInvoiceCode(table.getString("VBELN").replaceAll("^0+", ""));
                    //项目号，左侧去0
                    head.setItemNo(table.getString("POSNR").replaceAll("^0+", ""));
                    //发货单单号：SAP发货单单号+行项目(均不去0)
                    String invoiceCode = table.getString("VBELN") + table.getString("POSNR");
                    head.setInvoiceCode(invoiceCode);
                    //制单人：SAP员工号
                    String sapUserCode = table.getString("ERNAM");
                    head.setMakeOrderUser(sapUserCode);

                    if ("X".equals(table.getString("ZZSC"))) {
                        //删除发货通知单
                        failMessage += delete_note(invoiceCode, sapUserCode, table.getString("BWART"));
                    } else {
                        //抬头名称
                        head.setSubCompanyCode(table.getString("TITLE"));
                        //售达方
                        head.setCustomerCode(table.getString("NAME1"));
                        //送达方
                        head.setShipToParty(table.getString("NAME2"));
                        //物料编码
                        head.setMatCode(table.getString("MATNR"));
                        //数量
                        head.setAmount(String.valueOf(((int) Double.parseDouble(table.getString("LFIMG")))));
                        //开单日期
                        try {
                            head.setMakeOrderDate(sdf.parse(table.getString("ERDAT")));
                        } catch (ParseException e) {
                        }
                        //货运方式
                        head.setShipType(table.getString("SDABW"));
                        //销售订单
                        head.setSalesOrder(table.getString("OBELN"));
                        //移动类型
                        head.setOrderTypeCode(table.getString("BWART"));
                        //成品库
                        head.setProductStockCode(table.getString("LGOBE"));

                        //类型，默认 01:普通
                        head.setType(SysParamConstant.P_ASN_COMMON);
                        //状态，默认 01:待出库
                        head.setStatus(SysParamConstant.P_ASN_STATUS_WAITSEND);
                        //head.setCreateTime(date);
                        head.setUpdateTime(date);

                        if ("601".equals(head.getOrderTypeCode())) {
                            //插入、更新 发货通知单
                            failMessage += saveOrUpdate_note(head, sysUser);

                            //退货
                        } else {
                            outInvoiceHeadService.saveOrUpdate(head);
                        }
                    }
                }

                //如果不是删除的单子，并且是退货单，那插入明细数据
                List<OutInvoiceDetails> oidList = new ArrayList<>();
                table.setRow(0);
                if (!"X".equals(table.getString("ZZSC"))) {
                    for (int i = 0; i < tableVin.getNumRows(); i++) {
                        tableVin.setRow(i);

                        //先删除
                        QueryWrapper<OutInvoiceDetails> wrapper = new QueryWrapper<>();
                        wrapper.eq("invoiceCode", tableVin.getString("VBELN") + tableVin.getString("POSNR"));
                        outInvoiceDetailsMapper.delete(wrapper);

                        OutInvoiceDetails d = new OutInvoiceDetails();
                        d.setInvoiceCode(tableVin.getString("VBELN") + tableVin.getString("POSNR"));
                        d.setVin(tableVin.getString("ZVIN"));
                        d.setCreateId(sysUser.getUserCode());
                        d.setCreateTime(date);
                        d.setUpdateId(sysUser.getUserCode());
                        d.setUpdateTime(date);
                        oidList.add(d);
                    }
                    //一次性插入
                    outInvoiceDetailsService.saveBatch(oidList);
                }
            }

            if ("".equals(failMessage)) {
                sysInterfaceLog.setFlag("Y");
                sysInterfaceLogService.save(sysInterfaceLog);

                exports.setValue("RTN_CODE", SysParamConstant.P_API_RESULT_SUCCESS);
                exports.setValue("RTN_MSG", "执行成功");
            } else {
                sysInterfaceLog.setFlag("N");
                sysInterfaceLog.setFailmsg(failMessage);
                sysInterfaceLogService.save(sysInterfaceLog);

                exports.setValue("RTN_CODE", SysParamConstant.P_API_RESULT_FAIL);
                exports.setValue("RTN_MSG", failMessage);
            }
        } catch (Exception e) {
            exports.setValue("RTN_CODE", SysParamConstant.P_API_RESULT_FAIL);
            exports.setValue("RTN_MSG", e.toString());

            sysInterfaceLog.setFlag("N");
            sysInterfaceLog.setFailmsg(e.toString());
            sysInterfaceLogService.save(sysInterfaceLog);

            LOGGER.error("异常:" + e.getMessage(), e);
        }
    }

    /**
     * 删除发货通知单
     *
     * @param invoiceCode   发货单单号
     * @param sapUserCode   制单人：SAP员工号
     * @param orderTypeCode 移动类型
     */
    private String delete_note(String invoiceCode, String sapUserCode, String orderTypeCode) {
        String failMessage = "";

        //根据 发货单单号invoiceCode，获取head
        OutInvoiceHead head = outInvoiceHeadService.getById(invoiceCode);
        if (head == null) {
            return "";
        }

        //outInvoiceHead表中数据删除
        outInvoiceHeadService.removeById(invoiceCode);

        //销售出库单的时候需要改为在库，而退货单的时候不需要
        if ("601".equals(orderTypeCode)) {
            /**
             * outInvoiceDetails表中，根据发货单单号invoiceCode获取所有VIN，
             * 库存表stock中对应VIN码状态变为 03:在库，更新人为sap员工号对应的wms员工号
             */
            Map<String, Object> map = new HashMap<>();
            map.put("invoiceCode", invoiceCode);
            map.put("status", SysParamConstant.P_CAR_STATUS_INSTORAGE);
            map.put("sapUserCode", sapUserCode);
            stockMapper.updateInterfaceVinStatus(map);
        }

        //outInvoiceDetails表中数据删除
        QueryWrapper<OutInvoiceDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("invoiceCode", invoiceCode);
        outInvoiceDetailsService.remove(queryWrapper);

        return failMessage;
    }

    /**
     * 插入、更新 发货通知单
     *
     * @param head
     */
    private String saveOrUpdate_note(OutInvoiceHead head, SysUser sysUser) {
        String failMessage = "";

        Map<String, Object> map = new HashMap<>();
        //仓库编码
        map.put("warehouse", sysUser.getWarehouse());
        //WMS员工号
        map.put("userCode", sysUser.getUserCode());
        //发货单号
        map.put("invoiceCode", head.getInvoiceCode());
        //物料编码
        map.put("matCode", head.getMatCode());
        //需求数量
        map.put("amount", head.getAmount());
        //车辆状态：03:在库
        map.put("statusInstorage", SysParamConstant.P_CAR_STATUS_INSTORAGE);
        //车辆状态：05:充电
        map.put("statusCharge", SysParamConstant.P_CAR_STATUS_CHARGE);
        //车辆状态：待出库
        map.put("statusSending", SysParamConstant.P_CAR_STATUS_WAITSEND);
        //车辆锁定状态：02:未锁定
//        map.put("lock", SysParamConstant.P_LOCK_OFF);
        //时间
        map.put("date", head.getUpdateTime());

        //根据 发货单单号invoiceCode，获取oldHead
        OutInvoiceHead oldHead = outInvoiceHeadService.getById(head.getInvoiceCode());

        //oldHead==null：插入
        if (oldHead == null) {
            //分配发货单
            failMessage = doSendOut(map, "insert");

            //运行成功
            if ("".equals(failMessage)) {
                //插入outInvoiceHead
                head.setCreateId(sysUser.getUserCode());
                head.setUpdateId(sysUser.getUserCode());
                head.setCreateTime(head.getUpdateTime());
                outInvoiceHeadService.save(head);
            }
        }
        //oldHead!=null：更新
        else {
            //oldHead中的需求数量
            int oldAmount = Integer.parseInt(oldHead.getAmount());
            //本次发货单中的需求数量
            int amount = Integer.parseInt(head.getAmount());

            //需求量没有变化
            if (oldAmount == amount) {
                failMessage = "发货单号（" + head.getInvoiceCode() + "）需求量没有变化，请确认。";
            }
            //追加
            else if (oldAmount < amount) {
                //需求数量
                map.put("amount", amount - oldAmount);
                //分配发货单
                failMessage = doSendOut(map, "updateAdd");

                //运行成功
                if ("".equals(failMessage)) {
                    //更新outInvoiceHead
                    head.setUpdateId(sysUser.getUserCode());
                    outInvoiceHeadService.updateById(head);
                }
            }
            //删减
            else if (oldAmount > amount) {
                //需求数量
                map.put("amount", oldAmount - amount);
                //分配发货单
                failMessage = doSendOut(map, "updateDel");

                //运行成功
                if ("".equals(failMessage)) {
                    //更新outInvoiceHead
                    head.setUpdateId(sysUser.getUserCode());
                    outInvoiceHeadService.updateById(head);
                }
            }
        }

        return failMessage;
    }

    /**
     * 分配发货单
     *
     * @param map
     * @param operate insert：插入outInvoiceHead；updateAdd：更新outInvoiceHead，需求量增加；updateDel：更新outInvoiceHead，需求量减少
     * @return
     */
    private String doSendOut(Map<String, Object> map, String operate) {
        //库存表中，根据物料编码matCode，取得(03:在库、05:充电)：待分配、02:未锁定 的车辆数量
        int cnt = 0;
        try {
            cnt = stockMapper.searchAssignedStocksCnt(map);
        } catch (Exception e) {
        }

        //订单需要的车辆数量
        int amount = 0;
        try {
            amount = (int) Double.parseDouble((String) map.get("amount"));
        } catch (Exception e) {
        }

        //发货单号
        String invoiceCode = (String) map.get("invoiceCode");

        String failMessage = "";
        //分配发货单：oldHead==null：插入
        if (operate.equals("insert")) {
            if (amount == 0) {
                failMessage = "发货单号（" + invoiceCode + "）需求数量为0，请确认；";
            } else {
                //库存不足，有多少分配多少
                if (cnt < amount) {
                    map.put("amount", cnt);
                }

                //库存表中，根据物料编码matCode，按时间从早到晚，取得(03:在库、05:充电)：待分配、02:未锁定 车辆，插入到发货单详情表outInvoiceDetails
                stockMapper.insertAssignedStocks(map);

                //更新 stock 库存表中取得的待分配车辆状态 03：待分配->09:出库中
                stockMapper.updateAssignedStocks(map);
            }
        }
        //分配发货单：oldHead!=null：更新，需求数量增加
        else if (operate.equals("updateAdd")) {
            //库存不足，有多少分配多少
            if (cnt < amount) {
                map.put("amount", cnt);
            }
            
            //库存表中，根据物料编码matCode，按时间从早到晚，取得(03:在库、05:充电)：待分配、02:未锁定 车辆，插入到发货单详情表outInvoiceDetails
            stockMapper.insertAssignedStocks(map);

            //更新 stock 库存表中取得的待分配车辆状态 (03:在库、05:充电)：待分配->09:出库中
            stockMapper.updateAssignedStocks(map);
        }
        //分配发货单：oldHead!=null：更新，需求数量减少
        else if (operate.equals("updateDel")) {
            /**
             * outInvoiceDetails表中，通过发货单单号invoiceCode查找所有的VIN，关联库存表stock，取得入库日期较晚的VIN。
             * 1、stock库存表中更新状态 09:出库中->03:在库 ===>(03:在库、05:充电)：待分配，这里只更新成 03:在库
             * 2、outInvoiceDetails表中删除
             */
            stockMapper.updateStatusToInstorage(map);
            outInvoiceDetailsMapper.delRedundant(map);
        }

        return failMessage;
    }
    //==========================================发货通知单 END==========================================

    //==========================================整车借用调拨归还 START==================================

    /**
     * 整车借用调拨归还
     *
     * @param serverCtx
     * @param function
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void borrow_doc(JCoServerContext serverCtx, JCoFunction function) {
        //获取SAP传过来的参数
        JCoTable table = function.getTableParameterList().getTable(0);
        //传给SAP的返回值
        JCoParameterList exports = function.getExportParameterList();

        //接口日志
        SysInterfaceLog sysInterfaceLog = new SysInterfaceLog();
        sysInterfaceLog.setInterfaceName("ZWMS_RFC_BORROW_DOC:整车借用调拨归还");
        sysInterfaceLog.setParam(table.toString());
        sysInterfaceLog.setCreateId(sysManagerId);
        sysInterfaceLog.setUpdateId(sysManagerId);

        //failMessage=="" ==> 执行成功；failMessage!="" ==> 执行失败
        String failMessage = "";

        //单据类型==>JY:借用 GH:归还 KY:调拨
        String billType;
        //取消过账标识==>X：取消过账
        String cancleFlag;
        //单据号码
        String sapOrderNum;
        //调出仓库（成品库）
        String productStockCode_out;
        //调入仓库（成品库）
        String productStockCode_in;
        //VIN码
        String vin;
        //借用人、归还人、调拨人姓名
        String bgdUserName;
        //操作人（SAP员工号）
        String operateUserCode;
        String wmsUserCode;
        //操作日期
        String operateDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date sdfDate = null;
        //对应不同的处理逻辑==>JYX:借用取消、JY:借用、GHX:归还取消、GH:归还、KY:调拨
        String handle;
        List<StockLog> stockLogs = new ArrayList<>();
        StockLog stockLog;

        try {
            for (int i = 0; i < table.getNumRows(); i++) {
                table.setRow(i);

                billType = table.getString("ZUART");
                cancleFlag = table.getString("ZSC");
                sapOrderNum = table.getString("ZPSDH");
                productStockCode_out = table.getString("LGFSB");
                productStockCode_in = table.getString("LGORT");
                vin = table.getString("ZVIN");
                bgdUserName = table.getString("ZJYR");
                operateUserCode = table.getString("ERNAM");
                operateDate = table.getString("ERDAT");
                try {
                    sdfDate = sdf.parse(operateDate);
                } catch (ParseException e) {
                }

                //操作人，SAP员工号查找对应的WMS员工号
                QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("sapUserCode", operateUserCode);
                SysUser user = sysUserService.getOne(queryWrapper);
                if (user != null) {
                    wmsUserCode = user.getUserCode();
                } else {
                    failMessage += "VIN码（" + vin + "）中，操作员工号在WMS系统中不存在，请确认;";
                    continue;
                }

                //库存日志
                stockLog = new StockLog();
                stockLog.setSapOrderNum(sapOrderNum);
                stockLog.setVin(vin);
                stockLog.setBgdUserName(bgdUserName);
                stockLog.setCreateId(wmsUserCode);
                stockLog.setCreateTime(sdfDate);
                stockLog.setUpdateId(wmsUserCode);
                stockLog.setUpdateTime(sdfDate);

                String message = "";
                //借用取消
                if ("JY".equals(billType) && "X".equals(cancleFlag)) {
                    stockLog.setOperateCode(SysParamConstant.P_STOCKLOG_CANCEL_ADJUST);
                    handle = "JYX";
                }
                //借用
                else if ("JY".equals(billType)) {
                    stockLog.setOperateCode(SysParamConstant.P_STOCKLOG_LEND);
                    handle = "JY";
                }
                //归还取消
                else if ("GH".equals(billType) && "X".equals(cancleFlag)) {
                    stockLog.setOperateCode(SysParamConstant.P_STOCKLOG_CANCEL_BACK);
                    handle = "GHX";
                }
                //归还
                else if ("GH".equals(billType)) {
                    stockLog.setOperateCode(SysParamConstant.P_STOCKLOG_BACK);
                    handle = "GH";
                }
                //调拨
                else if ("KY".equals(billType)) {
                    stockLog.setOperateCode(SysParamConstant.P_STOCKLOG_ADJUST);
                    handle = "KY";
                } else {
                    failMessage += "VIN码（" + vin + "）中，单据类型、取消过账标识 传入有误，请确认;";
                    continue;
                }

                //处理逻辑
                message = borrow_doc_handle(handle, vin, sapOrderNum, productStockCode_out, productStockCode_in, wmsUserCode, sdfDate);

                //成功
                if ("".equals(message)) {
                    stockLogs.add(stockLog);
                }
                //失败
                else {
                    failMessage += message;
                }
            }

            //插入库存
            if (stockLogs.size() > 0) {
                stockLogService.saveBatch(stockLogs);
            }

            if ("".equals(failMessage)) {
                sysInterfaceLog.setFlag("Y");
                sysInterfaceLogService.save(sysInterfaceLog);

                exports.setValue("RTN_CODE", SysParamConstant.P_API_RESULT_SUCCESS);
                exports.setValue("RTN_MSG", "执行成功");
            } else {
                sysInterfaceLog.setFlag("N");
                sysInterfaceLog.setFailmsg(failMessage);
                sysInterfaceLogService.save(sysInterfaceLog);

                exports.setValue("RTN_CODE", SysParamConstant.P_API_RESULT_FAIL);
                exports.setValue("RTN_MSG", failMessage);
            }
        } catch (Exception e) {
            exports.setValue("RTN_CODE", SysParamConstant.P_API_RESULT_FAIL);
            exports.setValue("RTN_MSG", e.toString());

            sysInterfaceLog.setFlag("N");
            sysInterfaceLog.setFailmsg(e.toString());
            sysInterfaceLogService.save(sysInterfaceLog);

            LOGGER.error("异常:" + e.getMessage(), e);
        }
    }

    /**
     * 处理逻辑
     *
     * @param handle
     * @param vin
     * @param sapOrderNum          单据号码
     * @param productStockCode_out 调出仓库（成品库）
     * @param productStockCode_in  调入仓库（成品库）
     * @param wmsUserCode          wms员工号
     * @param sdfDate              日期
     */
    public String borrow_doc_handle(String handle, String vin, String sapOrderNum, String productStockCode_out,
                                    String productStockCode_in, String wmsUserCode, Date sdfDate) {
        String failMessage = "";
        //根据VIN获取库存
        Stock stock = stockService.getById(vin);
        if (stock == null) {
            failMessage = "VIN码（" + vin + "），库存中不存在所传VIN码;";
            return failMessage;
        }

        stock.setUpdateId(wmsUserCode);
        stock.setUpdateTime(sdfDate);

        //借用
        if ("JY".equals(handle)) {
            //根据库存，得到库位号
            SysStockPosition sysStockPosition = sysStockPositionService.getById(stock.getStockPosition());
            if (sysStockPosition == null) {
                failMessage = "VIN码（" + vin + "），VIN码对应的库位不存在;";
                return failMessage;
            }
            sysStockPosition.setUpdateId(wmsUserCode);
            sysStockPosition.setUpdateTime(sdfDate);

            //1、释放库位：更新库位表的 inUse --> 01:未使用
            sysStockPosition.setInUse(SysParamConstant.P_NOT_USE);
            sysStockPositionService.updateById(sysStockPosition);

            //2、更新库存表：车辆状态为 07:借用，更新成品库，库位号更新为空
            stock.setStatus(SysParamConstant.P_CAR_STATUS_LEND);
            stock.setProductStockCode(productStockCode_in);
            stock.setStockPosition("");
            stockService.updateById(stock);
        }
        //归还
        else if ("GH".equals(handle)) {
            //获取可用的库位
            List<Map<String, Object>> list = getAvailableStockPositions(wmsUserCode, vin);
            if (list == null || list.size() == 0) {
                failMessage = "VIN码（" + vin + "），暂无可用的库位;";
                return failMessage;
            }
            String stockPosition = list.get(0).get("stockPosition").toString();

            //根据库位号，获取库位
            SysStockPosition sysStockPosition = sysStockPositionService.getById(stockPosition);
            sysStockPosition.setUpdateId(wmsUserCode);
            sysStockPosition.setUpdateTime(sdfDate);

            //1、更新库位表的 inUse --> 02:使用中
            sysStockPosition.setInUse(SysParamConstant.P_IN_USE);
            sysStockPositionService.updateById(sysStockPosition);

            //2、更新库存表：车辆状态为 03:在库，更新成品库
            stock.setStatus(SysParamConstant.P_CAR_STATUS_INSTORAGE);
            stock.setProductStockCode(productStockCode_in);

            //3、更新库存表：更新库位
            stock.setStockPosition(stockPosition);
            stockService.updateById(stock);
        }
        //调拨
        else if ("KY".equals(handle)) {
            //1、更新库存表：更新成品库
            stock.setProductStockCode(productStockCode_in);

            //2、更新库存表：如果调入仓库是雷丁成品库(1001)、比德文成品库(1008)或宝路达成品库(1009)，那么车辆状态无须修改。
            //   反之，则需将车辆状态改为调拨，不能继续使用。
            if (!productStockCode_in.equals("1001") && !productStockCode_in.equals("1008") && !productStockCode_in.equals("1009")) {
                stock.setProductStockCode(SysParamConstant.P_CAR_STATUS_ADJUST);
            }
            stockService.updateById(stock);
        }
        //借用取消
        else if ("JYX".equals(handle)) {
            //获取可用的库位
            List<Map<String, Object>> list = getAvailableStockPositions(wmsUserCode, vin);
            if (list == null || list.size() == 0) {
                failMessage = "VIN码（" + vin + "），暂无可用的库位;";
                return failMessage;
            }
            String stockPosition = list.get(0).get("stockPosition").toString();

            //根据库位号，获取库位
            SysStockPosition sysStockPosition = sysStockPositionService.getById(stockPosition);
            sysStockPosition.setUpdateId(wmsUserCode);
            sysStockPosition.setUpdateTime(sdfDate);

            //1、更新库存表：更新成品库、车辆状态更新为 03:在库
            stock.setProductStockCode(productStockCode_out);
            stock.setStatus(SysParamConstant.P_CAR_STATUS_INSTORAGE);

            //2、更新库存表：分配库位
            //更新库位
            stock.setStockPosition(list.get(0).get("stockPosition").toString());
            stockService.updateById(stock);

            //3、库位表中，库位更新为 02:使用中
            sysStockPosition.setInUse(SysParamConstant.P_IN_USE);
            sysStockPositionService.updateById(sysStockPosition);
        }
        //归还取消
        else if ("GHX".equals(handle)) {
            //根据库存，得到库位号
            SysStockPosition sysStockPosition = sysStockPositionService.getById(stock.getStockPosition());
            if (sysStockPosition == null) {
                failMessage = "VIN码（" + vin + "），VIN码对应的库位不存在;";
                return failMessage;
            }
            sysStockPosition.setUpdateId(wmsUserCode);
            sysStockPosition.setUpdateTime(sdfDate);

            //1、更新库存表：更新成品库、车辆状态更新为 07:借用、库位更新为空
            stock.setProductStockCode(productStockCode_out);
            stock.setStatus(SysParamConstant.P_CAR_STATUS_LEND);
            stock.setStockPosition("");
            stockService.updateById(stock);

            //2、库位表中，库位更新为 01:未使用
            sysStockPosition.setInUse(SysParamConstant.P_NOT_USE);
            sysStockPositionService.updateById(sysStockPosition);
        }

        return failMessage;
    }

    /**
     * 根据优先级，获取可用的库位
     *
     * @param userCode
     * @param vin
     * @return
     */
    private List<Map<String, Object>> getAvailableStockPositions(String userCode, String vin) {
        //根据员工号，获取仓库编码
        SysUser sysUser = sysUserService.getById(userCode);
        String warehouseCode = sysUser.getWarehouse();
        //获取vin对应的物料编码
        StockView sv = warehousingMapper.getMatCodeByVin(vin);
        if (sv == null) {
            return null;
        }
        //拼接-优先级规则
        String brand = sv.getMatCode().substring(0, 1);
        String color = sv.getMatCode().substring(10, 11);
        String series[] = sv.getMatDescription().split("\\.");
        String priorityRule = brand + "_" + color + "_" + series[2];
        //获取可用的库位
        List<Map<String, Object>> list = sysSpPriorityDataMapper.getSpByPrioRule(warehouseCode, priorityRule);

        return list;
    }
    //==========================================整车借用调拨归还 END====================================

    //==========================================出库单 START============================================
    @Override
    /**
     * 出库单
     * @param sapInvoiceCode sap发货单号
     */
    public Map<String, Object> stock_out(String sapInvoiceCode,String isPrint) throws JCoException {

        //-----------------------------------------------------------------
        //tre测试环境无法连接sap，为了程序不出错，默认接口调用成功
        //dev不运行；product运行
        if ("dev".equals(applicationType)) {
            Map<String, Object> map = new HashMap<>();
            map.put("RTN_CODE", "S");
            map.put("RTN_MSG", "");
            return map;
        }
        //-----------------------------------------------------------------

        JCoDestination destination = JCoDestinationManager.getDestination(DESTINATION_WITH_POOL);
        JCoFunction function = destination.getRepository().getFunction("ZWMS_RFC_STOCK_OUT");
        if (function == null) {
            throw new RuntimeException("ZWMS_RFC_STOCK_OUT not found in SAP.");
        }

        //根据 invoiceCode，查询outInvoiceDetails，并关联出 成品库
        List<Map<String, Object>> mspList = outInvoiceDetailsMapper.getByInvoiceCode(sapInvoiceCode);

        if (mspList.size() == 0) {
            return null;
        }

        JCoTable table = function.getTableParameterList().getTable("IN_TAB");
        table.appendRows(mspList.size());
        Map<String, Object> map;
        for (int i = 0; i < table.getNumRows(); i++) {
            table.setRow(i);
            map = mspList.get(i);

            //sap发货单号
            table.setValue("VBELN", sapInvoiceCode);
            //行项目
            table.setValue("POSNR", map.get("itemNo"));
            //VIN码
            table.setValue("ZVIN", (String) map.get("vin"));
            //发出仓库(成品库)
            table.setValue("LGOBE", (String) map.get("productStockCode"));
        }
        //如果是打印模式则传入X如果不是则传入空值
        function.getImportParameterList().setValue("ZFLAG",isPrint);

        function.execute(destination);

        JCoParameterList paraList = function.getExportParameterList();

        String RTN_CODE = paraList.getString("RTN_CODE");
        String RTN_MSG = paraList.getString("RTN_MSG");

        map = new HashMap<>();
        map.put("RTN_CODE", RTN_CODE);
        map.put("RTN_MSG", RTN_MSG);

        return map;
    }
    //==========================================出库单 END==============================================

    //==========================================出库单冲销 START========================================
    @Override
    /**
     * 出库单冲销
     * @param sapInvoiceCode sap发货单号
     */
    public Map<String, Object> wash_trans(String sapInvoiceCode) throws JCoException {

        //-----------------------------------------------------------------
        //tre测试环境无法连接sap，为了程序不出错，默认接口调用成功
        //dev不运行；product运行
        if ("dev".equals(applicationType)) {
            Map<String, Object> map = new HashMap<>();
            map.put("RTN_CODE", "S");
            map.put("RTN_MSG", "");
            return map;
        }
        //-----------------------------------------------------------------

        JCoDestination destination = JCoDestinationManager.getDestination(DESTINATION_WITH_POOL);
        JCoFunction function = destination.getRepository().getFunction("ZWMS_RFC_WASH_TRANS");
        if (function == null) {
            throw new RuntimeException("ZWMS_RFC_WASH_TRANS not found in SAP.");
        }

        //入参：sap发货单号
        function.getImportParameterList().setValue("VBELN", sapInvoiceCode);
        //执行
        function.execute(destination);

        JCoParameterList paraList = function.getExportParameterList();
        String RTN_CODE = paraList.getString("RTN_CODE");
        String RTN_MSG = paraList.getString("RTN_MSG");

        Map<String, Object> map = new HashMap<>();
        map.put("RTN_CODE", RTN_CODE);
        map.put("RTN_MSG", RTN_MSG);

        return map;
    }
    //==========================================出库单冲销 END==========================================

    //==========================================sap同步：物料主数据 START===============================
    @Override
    /**
     * sap同步：物料主数据
     * @param sdfDate
     * @return
     * @throws JCoException
     */
    public void sapSyncWms_mainMat(Date date) {
        //-----------------------------------------------------------------
        //tre测试环境无法连接sap，为了程序不出错，默认接口调用成功
        //dev不运行；product运行
        if ("dev".equals(applicationType)) {
            return;
        }
        //-----------------------------------------------------------------

        //接口日志
        SysInterfaceLog sysInterfaceLog = new SysInterfaceLog();
        sysInterfaceLog.setInterfaceName("ZWMS_RFC_MARA:同步物料主数据");
        sysInterfaceLog.setCreateId(sysManagerId);
        sysInterfaceLog.setUpdateId(sysManagerId);

        try {
            JCoDestination destination = JCoDestinationManager.getDestination(DESTINATION_WITH_POOL);
            JCoFunction function = destination.getRepository().getFunction("ZWMS_RFC_MARA");
            if (function == null) {
                throw new RuntimeException("ZWMS_RFC_MARA not found in SAP.");
            }

            //设置入参：开始日、结束日
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String sdfDate = sdf.format(date);
            JCoParameterList impParaList = function.getImportParameterList();
            impParaList.setValue("SDATE", sdfDate);
            impParaList.setValue("EDATE", sdfDate);
            sysInterfaceLog.setParam(sdfDate);

            //执行
            function.execute(destination);

            //获取返回参数
            JCoParameterList expParaList = function.getExportParameterList();
            String RTN_CODE = expParaList.getString("RTN_CODE");
            String RTN_MSG = expParaList.getString("RTN_MSG");

            //成功
            if ("S".equals(RTN_CODE)) {
                JCoTable table = function.getTableParameterList().getTable("IN_TAB");
                List<MainMat> mainMatList = new ArrayList<>();
                MainMat mainMat, queryMainMat;
                String code, name;
                for (int i = 0; i < table.getNumRows(); i++) {
                    table.setRow(i);
                    mainMat = new MainMat();

                    //物料编码
                    code = table.getString("MATNR");
                    //物料描述
                    name = table.getString("MAKTX");

                    mainMat.setCode(code);
                    mainMat.setName(name);
                    mainMat.setUpdateId(sysManagerId);
                    mainMat.setUpdateTime(date);

                    queryMainMat = mainMatService.getById(code);
                    if (queryMainMat == null) {
                        mainMat.setCreateId(sysManagerId);
                        mainMat.setCreateTime(date);
                    }

                    mainMatList.add(mainMat);
                }

                if (mainMatList.size() > 0) {
                    mainMatService.saveOrUpdateBatch(mainMatList);
                }

                sysInterfaceLog.setFlag("Y");
            }
            //失败
            else {
                sysInterfaceLog.setFlag("N");
                sysInterfaceLog.setFailmsg(RTN_MSG);
            }

            sysInterfaceLogService.save(sysInterfaceLog);
        } catch (Exception e) {
            sysInterfaceLog.setFlag("N");
            sysInterfaceLog.setFailmsg(e.toString());
            sysInterfaceLogService.save(sysInterfaceLog);

            LOGGER.error("异常:" + e.getMessage(), e);
        }
    }
    //==========================================sap同步：物料主数据 END=================================

    //==========================================sap同步：客户主数据 START===============================
    @Override
    /**
     * sap同步：客户主数据
     * @param sdfDate
     * @return
     * @throws JCoException
     */
    public void sapSyncWms_mainCustomers(Date date) {

        //-----------------------------------------------------------------
        //tre测试环境无法连接sap，为了程序不出错，默认接口调用成功
        //dev不运行；product运行
        if ("dev".equals(applicationType)) {
            return;
        }
        //-----------------------------------------------------------------

        //接口日志
        SysInterfaceLog sysInterfaceLog = new SysInterfaceLog();
        sysInterfaceLog.setInterfaceName("ZWMS_RFC_CUSTOMER:同步客户主数据");
        sysInterfaceLog.setCreateId(sysManagerId);
        sysInterfaceLog.setUpdateId(sysManagerId);

        try {
            JCoDestination destination = JCoDestinationManager.getDestination(DESTINATION_WITH_POOL);
            JCoFunction function = destination.getRepository().getFunction("ZWMS_RFC_CUSTOMER");
            if (function == null) {
                throw new RuntimeException("ZWMS_RFC_CUSTOMER not found in SAP.");
            }

            //设置入参：开始日、结束日
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String sdfDate = sdf.format(date);
            JCoParameterList impParaList = function.getImportParameterList();
            impParaList.setValue("SDATE", sdfDate);
            impParaList.setValue("EDATE", sdfDate);
            sysInterfaceLog.setParam(sdfDate);

            //执行
            function.execute(destination);

            //获取返回参数
            JCoParameterList expParaList = function.getExportParameterList();
            String RTN_CODE = expParaList.getString("RTN_CODE");
            String RTN_MSG = expParaList.getString("RTN_MSG");

            //成功
            if ("S".equals(RTN_CODE)) {
                JCoTable table = function.getTableParameterList().getTable("IN_TAB");
                List<MainCustomers> mainCustomerList = new ArrayList<>();
                MainCustomers mainCustomer, queryMainCustomer;
                String code, name, tel, addr;
                for (int i = 0; i < table.getNumRows(); i++) {
                    table.setRow(i);
                    mainCustomer = new MainCustomers();

                    //客户编码
                    code = table.getString("KUNNR");
                    //客户名称
                    name = table.getString("NAME1");
                    //客户地址
                    addr = table.getString("STRAS");
                    //联系电话
                    tel = table.getString("TELF1");

                    mainCustomer.setCode(code);
                    mainCustomer.setName(name);
                    mainCustomer.setTel(tel);
                    mainCustomer.setAddr(addr);
                    mainCustomer.setUpdateId(sysManagerId);
                    mainCustomer.setUpdateTime(date);

                    queryMainCustomer = mainCustomersService.getById(code);
                    if (queryMainCustomer == null) {
                        mainCustomer.setCreateId(sysManagerId);
                        mainCustomer.setCreateTime(date);
                    }

                    mainCustomerList.add(mainCustomer);
                }

                if (mainCustomerList.size() > 0) {
                    mainCustomersService.saveOrUpdateBatch(mainCustomerList);
                }

                sysInterfaceLog.setFlag("Y");
            }
            //失败
            else {
                sysInterfaceLog.setFlag("N");
                sysInterfaceLog.setFailmsg(RTN_MSG);
            }

            sysInterfaceLogService.save(sysInterfaceLog);
        } catch (Exception e) {
            sysInterfaceLog.setFlag("N");
            sysInterfaceLog.setFailmsg(e.toString());
            sysInterfaceLogService.save(sysInterfaceLog);

            LOGGER.error("异常:" + e.getMessage(), e);
        }
    }
    //==========================================sap同步：客户主数据 END=================================
}
