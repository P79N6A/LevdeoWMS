package com.tre.jdevtemplateboot.web.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tre.barcode.BarCodeType;
import com.tre.barcode.Barcode;
import com.tre.jdevtemplateboot.common.constant.SysParamConstant;
import com.tre.jdevtemplateboot.common.pojo.ParameterBean;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.common.util.LJsonUtils;
import com.tre.jdevtemplateboot.domain.po.InventoryHead;
import com.tre.jdevtemplateboot.domain.po.InventoryWork;
import com.tre.jdevtemplateboot.domain.po.SysUser;
import com.tre.jdevtemplateboot.mapper.InventoryDetailsMapper;
import com.tre.jdevtemplateboot.mapper.InventoryHeadMapper;
import com.tre.jdevtemplateboot.mapper.InventoryWorkMapper;
import com.tre.jdevtemplateboot.mapper.SysUserMapper;
import com.tre.jdevtemplateboot.service.impl.InventoryWorkServiceImpl;
import com.tre.jdevtemplateboot.web.annotation.PassToken;
import com.tre.jdevtemplateboot.web.pojo.InventoryParameterBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 盘点管理控制器
 *
 * @author SongGuiFan
 */
@RestController
@RequestMapping("/inventory-head")
@ConfigurationProperties
public class InventoryHeadController {
    @Autowired
    private InventoryHeadMapper inventoryHeadMapper;

    @Autowired
    private InventoryDetailsMapper inventoryDetailsMapper;

    @Autowired
    private InventoryWorkMapper inventoryWorkMapper;

    @Autowired
    private InventoryWorkServiceImpl inventoryWorkService;

    @Autowired
    private SysUserMapper sysUserMapper;

    //条形码保存路径
    @Value("${my.barcodepath}")
    private String barcodepath;

    //上传临时文件夹
    @Value("${spring.servlet.multipart.location}")
    private String multipartLocation;

    //PDA错误类型
    private enum PDAError {
        INVENTORYERROR("0200"),
        PLANID("0201"),
        CHECKPERSON("0202"),
        PLANDATE("0203"),
        VINPATTERN("0204"),
        STRTOOLONG("0205");
        String name;

        private PDAError(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    /**
     * 盘点计划-查询盘点计划列表
     *
     * @param params Inventory对象
     * @return
     */
    @RequestMapping(value = "/getPlanList")
    public ResponseResult getPlanList(InventoryParameterBean params) {
        params.setWarehouse(CommonUtils.getWarehouseCode());
        PageHelper.startPage(params.getCurrentPage(), params.getLimit());
        List<Map<String, Object>> list = inventoryHeadMapper.getPlanList(params);
        return ResponseResult.buildOK(new PageInfo<Map<String, Object>>(list));

    }

    /**
     * 新建盘点计划-查询在库车辆列表
     *
     * @param params Parameter对象
     * @return
     */
    @RequestMapping("/getAllCarList")
    public ResponseResult getAllCarList(ParameterBean params) {
        params.setWarehouse(CommonUtils.getWarehouseCode());
        return ResponseResult.buildOK(inventoryHeadMapper.getAllCarList(params));
    }

    /**
     * 新建盘点计划-保存
     *
     * @param planDate 盘点日
     * @return
     * @throws ParseException
     */
    @RequestMapping("/saveNewPlan")
    @Transactional
    public ResponseResult saveNewPlan(String planDate) throws ParseException {
        //封装Map
        Map<String, Object> map = new HashMap<String, Object>();
        String planId = currentPlanID();
        map.put("planId", planId);
        map.put("status", SysParamConstant.P_CHECK_WAIT);
        //将String转成Date
        map.put("planDate", CommonUtils.stringToDate(planDate, "yyyy-MM-dd"));
        //获取当前登录用户的userCode
        map.put("loginId", CommonUtils.getUserCode());
        //获取系统当前时间
        map.put("currentTime", CommonUtils.getCurrentDateTime());
        map.put("warehouseCode", CommonUtils.getWarehouseCode());
        //保存计划主表
        inventoryHeadMapper.saveInventory(map);
        //保存计划详情表
        inventoryDetailsMapper.saveInventoryDetails(map);
        return ResponseResult.buildOK(planId);
    }

    /**
     * @return
     */
    private String currentPlanID() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String str = simpleDateFormat.format(new Timestamp(System.currentTimeMillis()));
        return str + (int) (100 + Math.random() * 900);
    }

    /**
     * 计划详情-打印
     * 1、查询盘点详情列表；2、生成条形码
     *
     * @param params Inventory对象
     * @return
     */
    @RequestMapping("/doPrint")
    @ResponseBody
    public ResponseResult doPrint(InventoryParameterBean params) throws Exception {
        //获取计划单号
        String planId = params.getPlanId();
        //根据计划单号，查询需要打印的数据
        List<Map<String, Object>> printdataList = inventoryDetailsMapper.getPlanDetailsList(params);
        return ResponseResult.buildOK(printdataList);
    }

    /**
     * 计划详情-查询盘点详情列表
     *
     * @param params Inventory对象
     * @return
     */
    @RequestMapping("/getPlanDetailsList")
    @ResponseBody
    public ResponseResult getPlanDetailsList(InventoryParameterBean params) {
        return ResponseResult.buildOK(inventoryDetailsMapper.getPlanDetailsList(params));
    }

    /**
     * 计划详情- 查询全部人员列表
     *
     * @return
     */
    @RequestMapping("/getAllUserList")
    @ResponseBody
    public ResponseResult getAllUserList() {
        String warehouseCode = CommonUtils.getWarehouseCode();
        return ResponseResult.buildOK(inventoryHeadMapper.getAllUserList(warehouseCode));
    }

    /**
     * 批量更新盘点详情
     *
     * @param status 盘点状态
     * @param planId 盘点单号
     * @param list   盘点列表
     * @return
     */
    @RequestMapping("/savePlanDetails")
    @Transactional
    public ResponseResult savePlanDetails(
            @RequestParam("status") String status,
            @RequestParam("planId") String planId,
            @RequestParam("list") String list) {
        String userCode = CommonUtils.getUserCode();
        //更新盘点状态
        updateInventoryHeadStatus(planId, userCode, status);
        if ("[]".equals(list)) {
            return ResponseResult.buildOK(1);
        }
        //清空临时表，防止产生干扰
        inventoryWorkMapper.deleteInventoryWork(userCode);
        //将表单数据存入临时表
        List<InventoryWork> formList = LJsonUtils.jsonToList(list, InventoryWork.class);
        inventoryWorkService.saveBatch(formList);
        //批量更新盘点详情
        inventoryDetailsMapper.bachUpdateDetails(planId, userCode);
        //清空临时表，防止产生干扰
        inventoryWorkMapper.deleteInventoryWork(userCode);
        return ResponseResult.buildOK(1);
    }

    /**
     * PDA导入
     *
     * @param file   PDA文件
     * @param planId 盘点单号
     * @param list   盘点详情表单
     * @return
     */
    @PostMapping(value = "/pdaUpload", produces = "text/html;charset=UTF-8")
    @PassToken
    @Transactional
    public String pdaUpload(
            MultipartFile file,
            @RequestParam("planId") String planId,
            @RequestParam("list") String list
    ) {

        String userCode = CommonUtils.getUserCode();
        //将PDA文件转成List
        List<InventoryWork> pdaList;
        try {
            pdaList = padFile2List(file, planId, userCode);
            if (pdaList == null) {
                //盘点单号错误
                return inventoryDetailsMapper.getMsg(PDAError.PLANID.toString());
            }
        } catch (Exception e) {
            if (PDAError.PLANDATE.toString().equals(e.getMessage())) {
                //日期格式错误
                return inventoryDetailsMapper.getMsg(PDAError.PLANDATE.toString());
            }
            if (PDAError.STRTOOLONG.toString().equals(e.getMessage())) {
                //操作人编码超出长度
                return inventoryDetailsMapper.getMsg(PDAError.STRTOOLONG.toString());
            }
            if (PDAError.VINPATTERN.toString().equals(e.getMessage())) {
                //VIN格式不正确
                return inventoryDetailsMapper.getMsg(PDAError.VINPATTERN.toString());
            }
            if (PDAError.INVENTORYERROR.toString().equals(e.getMessage())) {
                //data.len!=4 盘点文件格式有误
                return inventoryDetailsMapper.getMsg(PDAError.INVENTORYERROR.toString());
            }
            //文件解析失败
            return "-1";
        }
        //清空临时表，防止产生干扰
        inventoryWorkMapper.deleteInventoryWork(userCode);
        //将PDA文件中的数据存入临时表
        if (pdaList != null) {
            inventoryWorkService.saveBatch(pdaList);
        }
        //将表单数据存入临时表
        List<InventoryWork> formList;
        if (!"[]".equals(list)) {
            formList = LJsonUtils.jsonToList(list, InventoryWork.class);
            inventoryWorkService.saveBatch(formList);
        }
        //核对数据
        String warehouseCode = CommonUtils.getWarehouseCode();
        List<String> errList = inventoryDetailsMapper.checkPDA(userCode, warehouseCode);
        String errMsg;
        if (errList == null || errList.size() == 0) {//没有错误信息
            //批量更新盘点详情
            inventoryDetailsMapper.bachUpdateDetails(planId, userCode);
            //更新盘点状态
            updateInventoryHeadStatus(planId, userCode, SysParamConstant.P_CHECK_CHECKING);
            errMsg = "0";
        } else {//盘点人不存在
            errMsg = inventoryDetailsMapper.getMsg(PDAError.CHECKPERSON.toString()) + "：";
            for (String str : errList) {
                errMsg = errMsg + str + "，";
            }
            errMsg = errMsg.substring(0, errMsg.length() - 1);
        }
        //清空临时表，防止产生干扰
        inventoryWorkMapper.deleteInventoryWork(userCode);
        return errMsg;
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
    public String pdaUploadApi(@RequestParam(value = "pdFile") MultipartFile file, @RequestParam(value = "planId") String planId, @RequestParam(value = "userCode") String userCode) throws IOException, ParseException {

        SysUser u = sysUserMapper.selectById(userCode);
        if (u == null) {
            return "-1";//用户不存在。
        }
        InventoryHead ih = inventoryHeadMapper.selectById(planId);
        if (ih == null) {
            return "-2";//盘点计划不存在。
        } else if (SysParamConstant.P_CHECK_OVER.equals(ih.getStatus())) {
            return "-2";//盘点计划已结束。
        }


        //读文件
        String pda = new String(file.getBytes(), "UTF-8");
        //按回车分割
        String[] rows = pda.split("\\r\\n|\\n\\r|\\n|\\r");
        if (rows.length < 1) {
            return "-3";//没有盘点结果。
        }

        List<InventoryWork> list = new ArrayList<>();
        String[] rowData;
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (String row : rows) {
            rowData = row.split("\\,\\\"|\\,|\\t\\\"");
            InventoryWork inventoryWork = new InventoryWork();
            inventoryWork.setVin(rowData[1].trim());
            inventoryWork.setCarResult(1);
            inventoryWork.setCheckPerson(rowData[2].trim());
            inventoryWork.setCheckTime(sDateFormat.parse(rowData[3].trim()));
            inventoryWork.setOperator(rowData[2].trim());
            list.add(inventoryWork);
        }

        //清空临时表，防止产生干扰
        inventoryWorkMapper.deleteInventoryWork(userCode);

        //将PDA文件中的数据存入临时表
        inventoryWorkService.saveBatch(list);

        //批量更新盘点详情
        inventoryDetailsMapper.bachUpdateDetails(planId, userCode);

        //更新盘点状态
        updateInventoryHeadStatus(planId, userCode, SysParamConstant.P_CHECK_CHECKING);

        //清空临时表，防止产生干扰
        inventoryWorkMapper.deleteInventoryWork(userCode);

        return "0";

    }

    /**
     * 删除计划外车辆
     *
     * @param planId 计划单号
     * @param vin    VIN码
     * @return
     */
    @RequestMapping("/delPlanDetails")
    @ResponseBody
    public ResponseResult delPlanDetails(
            @RequestParam("planId") String planId,
            @RequestParam("vin") String vin
    ) {
        inventoryDetailsMapper.deleteByVin(planId, vin);
        return ResponseResult.buildOK();
    }


    /**
     * 更新盘点状态
     *
     * @param planId   盘点单号
     * @param userCode 登录人编码
     * @param status   状态
     */
    private void updateInventoryHeadStatus(String planId, String userCode, String status) {
        InventoryHead inventory = new InventoryHead();
        inventory.setPlanId(planId);
        inventory.setStatus(status);
        inventory.setUpdateId(userCode);
        inventory.setUpdateTime(CommonUtils.getCurrentDateTime());
        inventoryHeadMapper.updateById(inventory);
    }

    /**
     * 将PDA文件转成List
     *
     * @param file     pda文件
     * @param planId   盘点单号
     * @param operator 操作人
     * @return
     * @throws Exception 文件解析失败
     */
    private List<InventoryWork> padFile2List(
            MultipartFile file,
            String planId,
            String operator
    ) throws Exception {
        List<InventoryWork> list = new ArrayList<>();
        String pda = new String(file.getBytes(), "UTF-8");
        //去掉pda文件头
        int index = pda.indexOf(planId);
        if (index == -1) {
            //文件解析失败
            return null;
        }
        if (index != 0) {
            pda = pda.substring(index);
        }

        //按回车分割
        String[] rows = pda.split("\\r\\n|\\n\\r|\\n|\\r");
        if (rows.length < 1) {
            return list;
        }
        //判断日期格式
        String pattern = "yyyy/MM/dd HH:mm:ss";
        //判断VIN格式是否正确
        String pattern1 = "[A-Z|a-z|\\d]{17}";
        String[] data;
        InventoryWork inventoryWork;
        for (String row : rows) {
            data = row.split("\\,\\\"|\\,|\\t\\\"");
            //System.out.println("data:"+data.length);
            if (data.length != 4) {
                throw new Exception(PDAError.INVENTORYERROR.toString());
            }
            if (!Pattern.matches(pattern1, data[1])) {
                //VIN格式不正确
                throw new Exception(PDAError.VINPATTERN.toString());
            } else if (data[2].length() > 20) {//操作人超长
                throw new Exception(PDAError.STRTOOLONG.toString());
            }
            inventoryWork = new InventoryWork();
            //盘点单号错误则直接中止
            if (!planId.equals(data[0].trim())) {
                return null;
            }
            //验证PDA文件是否被excel或wps修改过！！！
            try {
                inventoryWork.setCheckTime(CommonUtils.stringToDate(data[3].trim(), pattern));
            } catch (Exception e1) {
                //日期格式错误
                throw new Exception(PDAError.PLANDATE.toString());
            }
            inventoryWork.setOperator(operator);
            inventoryWork.setVin(data[1].trim());
            inventoryWork.setCheckPerson(data[2].trim());
            inventoryWork.setCarResult(1);

            list.add(inventoryWork);
        }
        return list;
    }


    /**
     * showBarcode
     *
     * @param planId 计划单号
     * @return
     */
    @RequestMapping(value = "/showBarcode", method = RequestMethod.GET)
    @ResponseBody
    @PassToken
    public void showBarcode(HttpServletRequest req, HttpServletResponse res, @RequestParam("planId") String planId) throws Exception {
        OutputStream fos = res.getOutputStream();
        try {
            Barcode barcode = new Barcode();
            barcode.createBarCode(planId, fos, BarCodeType.CODE128);
            fos.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

}
