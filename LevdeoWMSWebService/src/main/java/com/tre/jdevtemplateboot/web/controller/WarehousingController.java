package com.tre.jdevtemplateboot.web.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tre.jdevtemplateboot.common.constant.SysParamConstant;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.common.util.ExcelUtils;
import com.tre.jdevtemplateboot.domain.po.*;
import com.tre.jdevtemplateboot.mapper.*;
import com.tre.jdevtemplateboot.service.IInventoryWorkService;
import com.tre.jdevtemplateboot.service.IStockLogService;
import com.tre.jdevtemplateboot.service.ISysSpPriorityDataService;
import com.tre.jdevtemplateboot.service.IWarehousingScheduleWorkService;
import com.tre.jdevtemplateboot.service.impl.SysSpPriorityDataServiceImpl;
import com.tre.jdevtemplateboot.web.annotation.PassToken;
import com.tre.jdevtemplateboot.web.pojo.StockView;
import com.tre.jdevtemplateboot.web.pojo.WarehouseParameterBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 入库管理
 */

@RestController
@RequestMapping("Warehousing/")
public class WarehousingController {

    @Resource
    private WarehousingMapper warehousingMapper;

    @Resource
    private WarehousingPrintMapper warehousingPrintMapper;

    @Resource
    private ISysSpPriorityDataService iSysSpPriorityDataService;

    @Resource
    private IStockLogService iStockLogService;

    @Resource
    private SysSpPriorityDataServiceImpl sysSpPriorityDataService;

    @Resource
    private SysStockPositionMapper sysStockPositionMapper;

    @Resource
    private InventoryWorkMapper inventoryWorkMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    private IInventoryWorkService iInventoryWorkService;

    @Autowired
    private IWarehousingScheduleWorkService iWarehousingScheduleWorkService;

    /**
     * 获取入库一览信息
     *
     * @param ws
     * @return
     */
    @RequestMapping(value = "wsInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult getCarInfo(WarehouseParameterBean ws) {
        ws.setWarehouse(CommonUtils.getWarehouseCode());
        //ws.setSysUser(CommonUtils.getUserCode());
        PageHelper.startPage(ws.getCurrentPage(), ws.getLimit());
        List<WarehouseParameterBean> wsList = warehousingMapper.getWarehosingList(ws);
        return ResponseResult.buildOK(new PageInfo<WarehouseParameterBean>(wsList));
    }

    /**
     * 获取车位信息
     *
     * @param vin
     * @return
     */
    @RequestMapping(value = "getStockPosition", method = RequestMethod.GET)
    public ResponseResult getStockPosition(@RequestParam("vin") String vin, @RequestParam(value = "spFlg", required = false) String spFlg) {
        WarehouseParameterBean ws;
        if ("4".equals(spFlg)) {//非下线入库
            ws = warehousingMapper.getStockInfoByVinOnline(vin, CommonUtils.getWarehouseCode());
        } else {
            ws = warehousingMapper.getWarehousingByVin(vin, CommonUtils.getWarehouseCode());
        }

        if (ws == null) {
            return ResponseResult.buildOK(null);
        }
        switch (spFlg) {
            case "1":
                Map<String, Object> rtnMap = new HashMap<>();
                rtnMap.put("formData", ws);
                List<Map<String, Object>> spList = iSysSpPriorityDataService.getStockPosition(vin);
                //有固定库位,删除临时库位-已排序数据,临时库位在最后
                if (spList.size() > 1) {
                    spList.remove(spList.size() - 1);
                }
                rtnMap.put("spList", spList);
                return ResponseResult.buildOK(rtnMap);
            case "2"://车辆入库  状态为不等于01(待入库)状态禁止显示
                if (!ws.getStateCode().equals(SysParamConstant.P_CAR_STATUS_PREWAREHOUSEIN)) {
                    return ResponseResult.buildOK(null);
                }
                break;
            case "3"://钥匙入库 如果状态不为 01(待入库) 或者 02(车辆入库) 禁止显示
                if (!ws.getStateCode().equals(SysParamConstant.P_CAR_STATUS_PREWAREHOUSEIN)
                        && !ws.getStateCode().equals(SysParamConstant.P_CAR_STATUS_CARWAREHOUSEIN)) {//0201：车辆未入库，请先将车辆入库再次执行此操作
                    return ResponseResult.buildOK("0201");
                } else if (ws.getSapTransferCode() == null || !ws.getSapTransferCode().equals(SysParamConstant.P_SAP_PAYED)) {//SAP未过账，请先将SAP过账后再次执行此操作
                    return ResponseResult.buildOK("0202");
                }
                break;
            case "4"://非下线入库 如果状态不为07(借出),08(返厂)禁止显示
                Stock stock = warehousingMapper.getStockInfoByVin(vin);//通过VIN查询库存信息、
                if (stock == null) {
                    return ResponseResult.buildOK("0300");//未查询到车辆
                }
                if (stock.getStatus() != null) {
                    if (!stock.getStatus().equals(SysParamConstant.P_REWAREHOUSEIN_REASON_RETURN) && !stock.getStatus().equals(SysParamConstant.P_REWAREHOUSEIN_REASON_LEND)) {
                        return ResponseResult.buildOK("0301");//车辆没有在借出或者返厂状态,无法非下线入库
                    }
                }
                break;
        }

        return ResponseResult.buildOK(ws);
    }

    /**
     * 入库一览修改车位
     *
     * @param vin
     * @param stockPosition
     * @return
     */
    @RequestMapping(value = "updateStockPosition", method = RequestMethod.POST)
    @Transactional
    public ResponseResult updateStockPosition(String vin, String stockPosition) {

        // 验证新库位-是否使用中
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stockPosition", stockPosition);
        wrapper.eq("inUse", SysParamConstant.P_IN_USE);
        SysStockPosition selectOne = sysStockPositionMapper.selectOne(wrapper);
        if (selectOne != null) {
            return ResponseResult.buildCheck("-1", "新库位已被占用，请重新选择新库位。", null);
        }
        WarehouseParameterBean ws = new WarehouseParameterBean();
        ws.setUpdateId(CommonUtils.getUserCode());
        ws.setUpdateTime(CommonUtils.getCurrentDateTime());
        ws.setVin(vin);
        ws.setStockPosition(stockPosition);
        //通过VIN释放库位号
        int releaseStock = warehousingMapper.releaseStockByVin(vin);
        //进行入库-warehousingSchedule表,更新库位
        int updateWs = warehousingMapper.updateStockPosition(ws);
        //更新库位状态-sysStockPosition表
        int updateSp = warehousingMapper.updateStockPositionCMDsysSPosition(ws);

        if (updateWs < 1 || updateSp < 1) {
            return ResponseResult.buildCheck("-1", "更换库位失败。请重新操作。", null);
        }

        return ResponseResult.buildOK();
    }

    /**
     * 删除入库一览
     *
     * @param vin
     * @return
     */
    @RequestMapping(value = "delWarehousing", method = RequestMethod.GET)
    @Transactional
    public ResponseResult delWarehousing(String vin) {
        int state = 0;
        warehousingMapper.formatStockPosition(vin);//删除前先释放占用库位
        state = warehousingMapper.delWarehousingList(vin);//释放完库位后进行删除
        if (state > 0) {
            return ResponseResult.buildOK("2000");
        } else {
            return ResponseResult.buildOK("2001");
        }
    }

    /**
     * 车辆入库 通过usercode获取用户名
     *
     * @param userCode
     * @return
     */
    @RequestMapping(value = "getUserName", method = RequestMethod.GET)
    public ResponseResult getUserName(String userCode) {
        userCode = userCode.replace("\"", "");
        String user;

        user = warehousingPrintMapper.getUserName(userCode, CommonUtils.getWarehouseCode());
        return ResponseResult.buildOK(user);
    }

    /**
     * 入库一览导出EXCEL表格
     *
     * @param finished
     * @param vin
     * @param state
     * @param sap
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "exportWsExcel", method = RequestMethod.POST)
    @PassToken
    public void exportWsExcel(@RequestParam(value = "finished", required = false) String finished,
                              @RequestParam(value = "vin", required = false) String vin,
                              @RequestParam(value = "state", required = false) String state,
                              @RequestParam(value = "sap", required = false) String sap,
                              HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>();

        map.put("finishedTime", finished);
        map.put("vin", vin);
        map.put("state", state);
        map.put("sapTransfer", sap);
        map.put("warehouse", CommonUtils.getWarehouseCode());

        List<Map<String, Object>> wpList = warehousingMapper.getMapWarehosingList(map);
        List<String> titles = new ArrayList<>();
        titles.add("入库交接单");
        titles.add("物料编码");
        titles.add("物料描述");
        titles.add("VIN码");
        titles.add("完工日期");
        titles.add("状态");
        titles.add("库位号");
        titles.add("SAP过账");
        List<String> column = new ArrayList<>();
        column.add("wsTransition");
        column.add("matCode");
        column.add("matDescription");
        column.add("vin");
        column.add("finishedTime");
        column.add("state");
        column.add("stockPosition");
        column.add("sapTransfer");
        //文件名前缀：库存日志
        String prefixname = "wslist";
        //下载
        ExcelUtils.downloadExcel(wpList, titles, column, prefixname, response);
    }

    /**
     * 入库 批量库状
     *
     * @param wp
     * @return
     */
    @RequestMapping(value = "updateIntoWarehousingState", method = RequestMethod.GET)
    public ResponseResult updateState(String wp) {
        List<WarehouseParameterBean> wpList = JSON.parseArray(wp, WarehouseParameterBean.class);
        warehousingMapper.updateIntoWarehousing(wpList);//修改入库表的入库状态
        return ResponseResult.buildOK(1);
    }

    /**
     * 钥匙批量入库
     *
     * @param wp
     * @return
     */
    @RequestMapping(value = "updateKeyIntoWarehousingState", method = RequestMethod.GET)
    @Transactional
    public ResponseResult updateKeyState(String wp) {

        List<WarehouseParameterBean> wpList = JSON.parseArray(wp, WarehouseParameterBean.class);
        wpList = warehousingMapper.queryAllWsInfoByVin(wpList);
        for (int i = 0; i < wpList.size(); i++) {
            wpList.get(i).setUpdateId(CommonUtils.getUserCode());
            if (wpList.get(i).getMatDescription() != null && wpList.get(i).getMatDescription().contains("锂电")) {
                wpList.get(i).setBatteryType(SysParamConstant.P_BATTERY_LI);
            } else {
                wpList.get(i).setBatteryType(SysParamConstant.P_BATTERY_PL);
            }
        }

        warehousingMapper.updateKeyIntoWarehousing(wpList);//修改钥匙入库的入库状态
        warehousingMapper.updateKeyIntoWarehousingCmdStock(wpList);//插入stock库存表内
        return ResponseResult.buildOK(1);
    }


    /**
     * 修改非下线入库
     *
     * @param wp
     * @return
     */
    @RequestMapping(value = "updateOnlineIntoWarehousingState", method = RequestMethod.POST)
    @Transactional
    public ResponseResult updateOnlineIntoWarehousingState(String wp) {
        List<WarehouseParameterBean> wpList = JSON.parseArray(wp, WarehouseParameterBean.class);
        //批量修改库位
        List<String> vins = new ArrayList<String>();
        for (WarehouseParameterBean wpb : wpList) {
            vins.add(wpb.getVin());
        }
        //分配库位
        String getState = sysSpPriorityDataService.updateStockPosition(vins);
        if (getState.equals("0")) {
            return ResponseResult.buildOK("0401");
        } else {
            //获取VIN状态+库位号 - 用于更新log表
            // QueryWrapper queryWrapper = new QueryWrapper();
            // queryWrapper.in("vin", vins);
            //List<Stock> stockList = stockMapper.selectList(queryWrapper);
            List<StockView> stockViews = warehousingMapper.queryStockInfoByVin(vins);
            //stock表-更新vin 状态-03在库

            warehousingMapper.updateOnlineIntoWarehousing(wpList);

            return ResponseResult.buildOK(stockViews);
        }
    }

    /***
     * 非下线入库检索库存表内所有被更新的信息显示到前台页面
     * @param wp
     * @return
     */
    @RequestMapping(value = "searchOnlineIntoWarehouseUpdatingInfo")
    @PassToken
    public ResponseResult searchOnlineIntoWarehouseUpdatingInfo(String wp) {
        List<WarehouseParameterBean> wpList = JSON.parseArray(wp, WarehouseParameterBean.class);
        List<WarehouseParameterBean> wPBs = warehousingMapper.queryOnlineUpdatingInfo(wpList);
        return ResponseResult.buildOK(wPBs);
    }

    /**
     * 查询此车位信息是否被占用 如果被占用则返回此车位信息 如果没有 则返回空
     *
     * @param position
     * @return
     */
    @RequestMapping(value = "getSysPositionInfo", method = RequestMethod.GET)
    public ResponseResult getSysPositionInfo(String position) {
        return ResponseResult.buildOK(warehousingMapper.getPositionIsUsed(position));
    }

    /**
     * @param stockLog
     * @return
     */
    @RequestMapping(value = "saveStockPositionInfo", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult saveStockPositionInfo(@RequestBody StockLog stockLog) {
        warehousingMapper.saveWarehousingInfo(stockLog);
        return ResponseResult.buildOK(1);
    }

    /**
     * @param file
     * @param operator
     * @return
     */
    @PostMapping(value = "/vinUpload", produces = "text/html;charset=UTF-8")
    @PassToken
    @Transactional
    public String pdaUpload(MultipartFile file, @RequestParam("operator") String operator) throws IOException {

        //匹配VIN码
        String pattern = "[A-Z|a-z|\\d]{17}";
        //先删除work表中该人员的临时数据
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("operator", CommonUtils.getUserCode());
        inventoryWorkMapper.delete(wrapper);
        //往work表中批量插入vin
        List<InventoryWork> works = new ArrayList();
        String vins = new String(file.getBytes(), "UTF-8");
        String[] vinList = vins.split("\\r\\n|\\n\\r|\\n|\\r");
        for (String vin : vinList) {
            if (!"".equals(vin.replace(" ", ""))) {//判断获取的VIN码是否有空值
                if (!Pattern.matches(pattern, vin)) {
                    return "VIN码格式不正确";
                }
                InventoryWork w = new InventoryWork();
                w.setVin(vin);
                w.setOperator(CommonUtils.getUserCode());
                works.add(w);
            }

        }
        iInventoryWorkService.saveBatch(works);

        vins = warehousingMapper.checkPdaResult(CommonUtils.getUserCode(), CommonUtils.getWarehouseCode());
        if (StringUtils.isNotEmpty(vins)) {
            return "下列Vin码不是待入库状态，无法车辆入库。<br>" + vins.replaceAll("\\,", "<br>");
        }

        warehousingMapper.batchUpdateWarehousing(CommonUtils.getUserCode(), operator);//修改入库表的入库状态
        return "";
    }


    /**
     * 插入操作日志
     *
     * @param slog
     * @return
     */
    @RequestMapping(value = "saveLog", method = RequestMethod.POST)
    public ResponseResult saveLog(@RequestParam("slog") String slog) {
        Date date = CommonUtils.getCurrentDateTime();
        List<StockLog> stockLogs = JSON.parseArray(slog, StockLog.class);
        for (StockLog slg : stockLogs) {
            slg.setCreateTime(date);
            slg.setUpdateTime(date);
            if (StringUtils.isEmpty(slg.getCreateId())) {
                slg.setCreateId(CommonUtils.getUserCode());
                slg.setUpdateId(CommonUtils.getUserCode());
            }
        }
        iStockLogService.saveBatch(stockLogs);
        return ResponseResult.buildOK();
    }

    /**
     * 模拟SAP同步数据接口
     * 调用时 查看表内有没有与当前接口定义相同的字段
     *
     * @return
     */
    @RequestMapping(value = "workTest")
    @PassToken
    public String workTest() {
        List<WarehousingScheduleWork> works = new ArrayList<>();
        //增加1001
        WarehousingScheduleWork wsw = new WarehousingScheduleWork();
        wsw.setVin("MLD82B2A4HWC11001");
        wsw.setWsTransition("SM00000000199");
        wsw.setMatCode("BL16522000R0100");
        wsw.setFinishedTime("20190210");
        wsw.setProductStockCode("1005");
        wsw.setSapTransfer("G");
        wsw.setSapUserCode("SAP0004");
        works.add(wsw);
        //增加1002
        wsw = new WarehousingScheduleWork();
        wsw.setVin("MLD82B2A4HWC11002");
        wsw.setWsTransition("SM00000000200");
        wsw.setMatCode("BL16522000R0100");
        wsw.setFinishedTime("20190211");
        wsw.setProductStockCode("1005");
        //wsw.setCancelSapTransfer("X");
        wsw.setSapTransfer("G");
        wsw.setSapUserCode("SAP0004");
        works.add(wsw);
        //增加1010
        wsw = new WarehousingScheduleWork();
        wsw.setVin("MLD82B2A4HWC11010");
        wsw.setWsTransition("SM00000000110");
        wsw.setMatCode("BL16522000R0100");
        wsw.setFinishedTime("20190212");
        wsw.setProductStockCode("1005");
        wsw.setSapTransfer("G");
        //wsw.setCancelSapTransfer("X");
        wsw.setSapUserCode("SAP0004");
        works.add(wsw);
        //修改1012
        wsw = new WarehousingScheduleWork();
        wsw.setVin("MLD82B2A4HWC11012");
        wsw.setWsTransition("SM00000000201");
        wsw.setMatCode("BL16522000R0100");
        wsw.setFinishedTime("20190218");
        wsw.setProductStockCode("1005");
        wsw.setSapTransfer("G");
        //wsw.setCancelSapTransfer("X");
        wsw.setSapUserCode("SAP0004");
        works.add(wsw);
        //删除1013
        wsw = new WarehousingScheduleWork();
        wsw.setVin("MLD82B2A4HWC11013");
        wsw.setWsTransition("SM00000000201");
        wsw.setMatCode("BL16522000R0100");
        wsw.setFinishedTime("20190218");
        wsw.setProductStockCode("1005");
        wsw.setSapTransfer("D");
        //wsw.setCancelSapTransfer("X");
        wsw.setSapUserCode("SAP0004");
        works.add(wsw);
        try {
            iWarehousingScheduleWorkService.rfcStorageRec(works);
            return works.toString();

        } catch (Exception exp) {
            exp.printStackTrace();
            throw exp;
        }
    }

    /**
     * PDA上传
     *
     * @param file PDA文件
     * @return
     */
    @RequestMapping(value = "/pdaUploadApi", method = RequestMethod.POST)
    @PassToken
    @Transactional
    public String pdaUploadApi(@RequestParam(value = "rkFile") MultipartFile file, @RequestParam(value = "userCode") String userCode) throws IOException, ParseException {

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

        warehousingMapper.batchUpdateWarehousing(userCode, userCode);//修改入库表的入库状态

        return "0";//上传成功。

    }


}