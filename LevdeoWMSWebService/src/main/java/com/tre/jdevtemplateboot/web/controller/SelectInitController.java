package com.tre.jdevtemplateboot.web.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.jdevtemplateboot.common.constant.SysParamConstant;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.domain.po.*;
import com.tre.jdevtemplateboot.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: JDev
 * @create: 2018-12-07 13:22
 **/
@RestController
@RequestMapping("/selectInit")
public class SelectInitController {

    @Autowired
    private SysParmMapper sysParamMapper;
    @Autowired
    private SysStockAreaMapper sysStockAreaMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private MainTransportMapper mainTransportMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysWarehouseMapper sysWarehouseMapper;
    @Autowired
    private SysParmMapper sysParmMapper;
    @Autowired
    private WarehousingMapper warehousingMapper;
    @Autowired
    private MainCustomersMapper mainCustomersMapper;
    @Autowired
    private MainLogisCompanyMapper mainLogisCompanyMapper;
    @Autowired
    private MainProductStockMapper mainProductStockMapper;
    @Autowired
    private MainSaleCompanyMapper mainSaleCompanyMapper;

    /**
     * 取得在库车辆状态
     *
     * @return
     */
    @RequestMapping("/getCarStatus")
    public ResponseResult getCarStatus() {
        Wrapper wrapper = new QueryWrapper();
        ((QueryWrapper) wrapper).eq("code", SysParamConstant.PARAM_TYPE_CAR_STATUS);
        List<SysParm> list = sysParamMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * 电池类型
     *
     * @return
     */
    @RequestMapping("/getBatteryType")
    public ResponseResult getBatteryType() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("code", SysParamConstant.PARAM_TYPE_BATTERY_TYPE);
        List<SysParm> list = sysParamMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * sap过账状态
     *
     * @return
     */
    @RequestMapping("/getSapType")
    public ResponseResult getSapType() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("code", SysParamConstant.PARAM_TYPE_SAP_PAY);
        List<SysParm> list = sysParamMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * 盘点状态
     *
     * @return
     */
    @RequestMapping("/getCheckType")
    public ResponseResult getCheckType() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("code", SysParamConstant.PARAM_TYPE_CHECK_STATUS);
        List<SysParm> list = sysParamMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * 库区
     *
     * @return
     */
    @RequestMapping("/getStockAreaList")
    public ResponseResult getStockAreaList() {
        QueryWrapper wrapper = new QueryWrapper();
        List<SysStockArea> list = sysStockAreaMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * 根据仓库查询库区
     *
     * @return
     */
    @RequestMapping("/getStockAreaListWithWarehouse")
    public ResponseResult getStockAreaListWithWarehouse() {
        List<SysStockArea> list = sysStockAreaMapper.selectListWithWarehouse(CommonUtils.getWarehouseCode());
        return ResponseResult.buildOK(list);
    }

    /**
     * 发货单类型
     *
     * @return
     */
    @RequestMapping("/getInvoiceType")
    public ResponseResult getInvoiceType() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("code", SysParamConstant.PARAM_TYPE_ASN_TYPE);
        List<SysParm> list = sysParamMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * 发货单状态
     *
     * @return
     */
    @RequestMapping("/getInvoiceStatus")
    public ResponseResult getInvoiceStatus() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("code", SysParamConstant.PARAM_TYPE_ASN_STATUS);
        List<SysParm> list = sysParamMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * 客户
     *
     * @return
     */
    @RequestMapping("/getCustomer")
    public ResponseResult getCustomer() {
        QueryWrapper wrapper = new QueryWrapper();
        List<MainCustomers> list = mainCustomersMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * VIN一览
     * 换车列表
     *
     * @param searchstr
     * @return
     */
    @RequestMapping("/getVinList")
    public ResponseResult getVinList(@RequestParam(value = "searchstr") String searchstr, @RequestParam(value = "matCode", required = false) String matCode) {
        List<Map<String, Object>> list = stockMapper.getAvailableVIN(searchstr, matCode);
        return ResponseResult.buildOK(list);
    }


    /**
     * 获取客户下拉，带查询
     *
     * @param searchstr
     * @return
     */
    @RequestMapping("/getCustomerList")
    public ResponseResult getCustomerList(@RequestParam(value = "searchstr") String searchstr) {
        List<Map<String, Object>> list = mainCustomersMapper.getCustomerList(searchstr);
        return ResponseResult.buildOK(list);
    }

    /**
     * OnlineReason
     *
     * @return
     */
    @RequestMapping("/getOnlineReason")
    public ResponseResult getOnlineReason() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("code", SysParamConstant.PARAM_TYPE_REWAREHOUSEIN_REASON);
        List<SysParm> list = sysParamMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * 货运方式
     *
     * @return
     */
    @RequestMapping("/getTransportType")
    public ResponseResult getTransportType() {
        QueryWrapper wrapper = new QueryWrapper();
        List<MainTransport> list = mainTransportMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * 角色
     *
     * @return
     */
    @RequestMapping("/getRoleType")
    public ResponseResult getRoleType() {
        List<SysRole> list = sysRoleMapper.selectList(null);
        return ResponseResult.buildOK(list);
    }

    /**
     * 仓库list
     *
     * @return
     */
    @RequestMapping("/getWareHouseList")
    public ResponseResult getWareHouseList() {
        List<Map<String, Object>> list = sysWarehouseMapper.getWareHouseList();
        return ResponseResult.buildOK(list);
    }

    /**
     * 仓库list
     *
     * @return
     */
    @RequestMapping("/getPriorityList")
    public ResponseResult getPriorityList() {
        List<Map<String, Object>> list = sysParmMapper.getPriorityList();
        return ResponseResult.buildOK(list);
    }

    /**
     * 获取车辆入库状态 待入库 车辆入库 在库
     *
     * @return
     */
    @RequestMapping("/getWarehouseState")
    public ResponseResult getWarehouseState() {
        List<SysParm> list = warehousingMapper.getWarehousingStateByCode();
        return ResponseResult.buildOK(list);
    }

    /**
     * 获取账号状态  在用  停用
     *
     * @return
     */
    @RequestMapping("/getAccountStatus")
    public ResponseResult getAccountStatus() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("code", SysParamConstant.PARAM_TYPE_ACCOUNT_STATUS);
        List<SysParm> list = sysParamMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * 库位使用情况
     *
     * @return
     */
    @RequestMapping("/getSpUseStatus")
    public ResponseResult getSpUseStatus() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("code", SysParamConstant.PARAM_TYPE_IN_USE);
        List<SysParm> list = sysParamMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * 库位锁定情况
     *
     * @return
     */
    @RequestMapping("/getSpLockStatus")
    public ResponseResult getSpLockStatus() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("code", SysParamConstant.PARAM_TYPE_LOCK_STATUS);
        List<SysParm> list = sysParamMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * 获取物流公司下拉
     *
     * @return
     */
    @RequestMapping("/getlogisCompanys")
    public ResponseResult getlogisCompanys() {
        List<MainLogisCompany> list = mainLogisCompanyMapper.selectList(null);
        return ResponseResult.buildOK(list);
    }

    /**
     * 获取操作类型下拉
     *
     * @return
     */
    @RequestMapping("/getOperaType")
    public ResponseResult getOperaType() {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("code", SysParamConstant.PARAM_TYPE_STOCKLOG_TYPE);
        List<SysParm> list = sysParamMapper.selectList(wrapper);
        return ResponseResult.buildOK(list);
    }

    /**
     * 获取成品库类型下拉
     *
     * @return
     */
    @RequestMapping("/getProductStockCode")
    public ResponseResult getProductStockCode() {
        List<MainProductStock> mainProductStocks = mainProductStockMapper.selectList(new QueryWrapper());
        return ResponseResult.buildOK(mainProductStocks);
    }

    /**
     * 获取销售公司下拉
     *
     * @return
     */
    @RequestMapping("/getSaleCompany")
    public ResponseResult getSaleCompany() {
        Wrapper<MainSaleCompany> wrapper = new QueryWrapper<>();
        ((QueryWrapper<MainSaleCompany>) wrapper).select("distinct pCode, pName");
        return ResponseResult.buildOK(mainSaleCompanyMapper.selectMaps(wrapper));
    }
}
