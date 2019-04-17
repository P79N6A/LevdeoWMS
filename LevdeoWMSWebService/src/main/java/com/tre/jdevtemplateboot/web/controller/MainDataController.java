package com.tre.jdevtemplateboot.web.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.jdevtemplateboot.common.constant.SysParamConstant;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.domain.po.MainCustomers;
import com.tre.jdevtemplateboot.domain.po.MainSaleCompany;
import com.tre.jdevtemplateboot.domain.po.SysParm;
import com.tre.jdevtemplateboot.mapper.MainCustomersMapper;
import com.tre.jdevtemplateboot.mapper.MainSaleCompanyMapper;
import com.tre.jdevtemplateboot.mapper.SysParmMapper;
import com.tre.jdevtemplateboot.service.ISysParmService;
import com.tre.jdevtemplateboot.web.annotation.PassToken;
import com.tre.jdevtemplateboot.web.pojo.MainData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("mainData/")
@Controller
public class MainDataController {

    @Autowired
    private ISysParmService iSysParmService;
    @Autowired
    private SysParmMapper sysParmMapper;
    @Autowired
    private MainCustomersMapper mainCustomersMapper;
    @Autowired
    private MainSaleCompanyMapper mainSaleCompanyMapper;


    /**
     * 获取主数据类型
     * @return
     */
    @RequestMapping("getMainDataList")
    @ResponseBody
    @PassToken
    public ResponseResult getMainDataList(){
        QueryWrapper<SysParm> wrapper =new QueryWrapper<>();
        wrapper.eq("code", SysParamConstant.PARAM_TYPE_MAIN_MAINTEN);
        List<SysParm> parms=sysParmMapper.selectList(wrapper);
        return ResponseResult.buildOK(parms);
    }


    /**
     * 获取主数据的详细信息
     * @param subCode
     * @return
     */
    @RequestMapping("getMainDataInfo")
    @ResponseBody
    @PassToken
    public ResponseResult getMainDataInfo(String subCode){
        if(subCode !=null&& subCode!=""){
            return ResponseResult.buildOK(iSysParmService.getMainDataInfo(subCode));

        }
            List<Map<String,Object>> maps = new ArrayList<>();
            Map<String,Object>map =new HashMap<>();
            map.put("code"," ");
            map.put("name"," ");
            maps.add(map);

            return ResponseResult.buildOK(maps);
    }

    /**
     * 添加主数据内容
     * @param md
     * @return
     */
    @RequestMapping("addNewMainData")
    @ResponseBody
    @PassToken
    public ResponseResult addNewMainData(@RequestBody MainData md){
        //先将subCode保存到变量
        String subCode = md.getTbName();

        //将subcode值传入到tbname 再通过tbName获取到表名 重新赋值给tbName
        md.setTbName(sysParmMapper.getTableName(md.getTbName()));

        //查询时是否有相同的编码数据
        if(sysParmMapper.selectMainDataCodeIsREPEAT(md.getTbName(),md.getCode())>0){
            return ResponseResult.buildOK("01001");

            //查询是否有相同的名称数据
        }else if(sysParmMapper.selectMainDataNameIsREPEAT(md.getTbName(),md.getName())>0){
            return ResponseResult.buildOK("01002");
        }

        //如果获取的subCode值是客户表则执行下边语句
        if(SysParamConstant.P_MAIN_CUSTOMERS.equals(subCode)){
            MainCustomers customers=new MainCustomers();
            customers.setCode(md.getCode());
            customers.setName(md.getName());
            customers.setTel(md.getSubCode());
            customers.setAddr(md.getSubName().length()>100?md.getSubName().substring(0,99):md.getSubName());
            customers.setCreateId(CommonUtils.getUserCode());
            customers.setCreateTime(CommonUtils.getCurrentDateTime());
            customers.setUpdateId(CommonUtils.getUserCode());
            customers.setUpdateTime(CommonUtils.getCurrentDateTime());
            mainCustomersMapper.insert(customers);

            //如果获取的subCode语句是销售公司对应表 则执行下列语句
        }else if(SysParamConstant.P_MAIN_SALE_COMPANY.equals(subCode)){

            MainSaleCompany msc=new MainSaleCompany();
            msc.setCode(md.getCode());
            msc.setName(md.getName());
            msc.setpCode(md.getSubCode());
            msc.setpName(md.getSubName());
            msc.setCreateId(CommonUtils.getUserCode());
            msc.setCreateTime(CommonUtils.getCurrentDateTime());
            msc.setUpdateId(CommonUtils.getUserCode());
            msc.setUpdateTime(CommonUtils.getCurrentDateTime());
            mainSaleCompanyMapper.insert(msc);
            return ResponseResult.buildOK(subCode);

            //否则为普通表 执行下列语句
        }else{
            md.setCreateId(CommonUtils.getUserCode());
            md.setUpdateId(CommonUtils.getUserCode());
            sysParmMapper.saveMainData(md);
        }

        return ResponseResult.buildOK(subCode);
    }

    @RequestMapping("updateMainData")
    @ResponseBody
    @PassToken
    public ResponseResult updateMainData(@RequestBody MainData md){

        //先将subCode保存到变量
        String subCode = md.getTbName();

        //将subcode值传入到tbname 再通过tbName获取到表名 重新赋值给tbName
        md.setTbName(sysParmMapper.getTableName(md.getTbName()));


        //如果获取的subCode值是客户表则执行下边语句
        if(SysParamConstant.P_MAIN_CUSTOMERS.equals(subCode)){
            MainCustomers customers=new MainCustomers();
            customers.setCode(md.getCode());
            customers.setName(md.getName());
            customers.setTel(md.getSubCode());
            customers.setAddr(md.getSubName());
            customers.setUpdateId(CommonUtils.getUserCode());
            customers.setUpdateTime(CommonUtils.getCurrentDateTime());
            mainCustomersMapper.updateById(customers);

            //如果获取的subCode语句是销售公司对应表 则执行下列语句
        }else if(SysParamConstant.P_MAIN_SALE_COMPANY.equals(subCode)){

            MainSaleCompany msc=new MainSaleCompany();
            msc.setCode(md.getCode());
            msc.setName(md.getName());
            msc.setpCode(md.getSubCode());
            msc.setpName(md.getSubName());
            msc.setUpdateId(CommonUtils.getUserCode());
            msc.setUpdateTime(CommonUtils.getCurrentDateTime());
            mainSaleCompanyMapper.updateById(msc);
            return ResponseResult.buildOK(subCode);

            //否则为普通表 执行下列语句
        }else{
            md.setUpdateId(CommonUtils.getUserCode());
            iSysParmService.updateMainDataById(md);
        }

        return ResponseResult.buildOK(subCode);
    }
}
