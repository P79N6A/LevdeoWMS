package com.tre.jdevtemplateboot.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tre.jdevtemplateboot.common.constant.SysParamConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.domain.po.SysStockArea;
import com.tre.jdevtemplateboot.domain.po.SysStockPosition;
import com.tre.jdevtemplateboot.mapper.SysSpPriorityDataMapper;
import com.tre.jdevtemplateboot.mapper.SysSpPriorityMapper;
import com.tre.jdevtemplateboot.mapper.SysStockAreaMapper;
import com.tre.jdevtemplateboot.mapper.SysStockPositionMapper;
import com.tre.jdevtemplateboot.service.ISysStockPositionService;
import com.tre.jdevtemplateboot.web.pojo.StockPositionParameterBean;

/**
 * <p>
 * 库区
 * </p>
 *
 * @author JDev
 * @since 2018-12-20
 */
@RestController
@RequestMapping("/sys-stock-area")
public class SysStockAreaController {

    @Autowired
    private SysStockAreaMapper sysStockAreaMapper;
    @Autowired
    private SysStockPositionMapper sysStockPositionMapper;
    @Autowired
    private ISysStockPositionService iSysStockPositionService;
    @Autowired
    private SysSpPriorityMapper sysSpPriorityMapper;
    @Autowired
    private SysSpPriorityDataMapper sysSpPriorityDataMapper;

    /**
     * 查询所有库区
     *
     * @param pb
     * @return
     */
    @RequestMapping("/searchAll")
    @ResponseBody
    public ResponseResult searchChargeCar(StockPositionParameterBean pb) {
        PageHelper.startPage(pb.getCurrentPage(), pb.getLimit());
        List<Map<String, Object>> list = sysStockAreaMapper.searchAll(pb);
        return ResponseResult.buildOK(new PageInfo<Map<String, Object>>(list));
    }

    /**
     * 检索单个库区数据
     */
    @RequestMapping("/info")
    @ResponseBody
    public ResponseResult info(@RequestParam(value = "code", required = true) String code) {
        return ResponseResult.buildOK(sysStockAreaMapper.info(code));
    }

    /**
     * 保存新建库区
     */
    @RequestMapping("/save")
    @ResponseBody
    @Transactional
    public ResponseResult save(@RequestBody SysStockArea area) {
        // 获取最大库区code
        String max = sysStockAreaMapper.selectMaxCode();
        int code;
        if (max == null) {
            code = 1;
        } else {
            code = Integer.valueOf(max) + 1;
        }
        String stockAreaCode = String.format("%04d", code);
        area.setStockAreaCode(stockAreaCode);
        area.setCreateId(CommonUtils.getUserCode());
        area.setCreateTime(CommonUtils.getCurrentDateTime());
        area.setUpdateId(CommonUtils.getUserCode());
        area.setUpdateTime(CommonUtils.getCurrentDateTime());
        // 插入新建的库区
        sysStockAreaMapper.insert(area);
        //
        List<SysStockPosition> spList = new ArrayList<SysStockPosition>();
        for (int i = 1; i <= area.getAmount(); i++) {
            SysStockPosition sp = new SysStockPosition();
            sp.setStockPosition(stockAreaCode + String.format("%05d", i));
            sp.setStockAreaCode(stockAreaCode);
            sp.setName(area.getName() + "-" + String.format("%05d", i));
            sp.setCreateId(CommonUtils.getUserCode());
            sp.setCreateTime(CommonUtils.getCurrentDateTime());
            sp.setUpdateId(CommonUtils.getUserCode());
            sp.setUpdateTime(CommonUtils.getCurrentDateTime());
            sp.setInUse(SysParamConstant.P_NOT_USE);
            sp.setIsLock(SysParamConstant.P_LOCK_OFF);
            spList.add(sp);
        }
        // 插入库位
        iSysStockPositionService.saveBatch(spList);

        return ResponseResult.buildOK(stockAreaCode);
    }

    /**
     * 修改库区信息+增加库位
     */
    @RequestMapping("/update")
    @ResponseBody
    @Transactional
    public ResponseResult update(@RequestBody SysStockArea area) {

        area.setUpdateId(CommonUtils.getUserCode());
        area.setUpdateTime(CommonUtils.getCurrentDateTime());

        // 获取现在的库区信息
        SysStockArea stockArea = sysStockAreaMapper.info(area.getStockAreaCode());
        // 当前库位数量
        Integer beforeAmount = stockArea.getAmount();
        // 更新后库位数量
        Integer afterAmount = area.getAmount();

        // 新增库位
        if (afterAmount > beforeAmount) {
            // 封装数据,更新表做准备
            List<SysStockPosition> spData = new ArrayList<SysStockPosition>();
            for (int thisSp = beforeAmount; thisSp < afterAmount; thisSp++) {
                SysStockPosition sp = new SysStockPosition();
                sp.setStockPosition(area.getStockAreaCode() + String.format("%05d", thisSp + 1));
                sp.setStockAreaCode(area.getStockAreaCode());
                sp.setName(area.getName() + "-" + String.format("%05d", thisSp + 1));
                sp.setCreateId(CommonUtils.getUserCode());
                sp.setCreateTime(CommonUtils.getCurrentDateTime());
                sp.setUpdateId(CommonUtils.getUserCode());
                sp.setUpdateTime(CommonUtils.getCurrentDateTime());
                sp.setInUse(SysParamConstant.P_NOT_USE);
                sp.setIsLock(SysParamConstant.P_LOCK_OFF);
                spData.add(sp);
            }
            // sysStockPosition表-新加库位号
            iSysStockPositionService.saveBatch(spData);
        }
        // sysStockArea表-更新库区信息
        sysStockAreaMapper.updateById(area);

        return ResponseResult.buildOK(area.getStockAreaCode());
    }

    /**
     * 删除库区
     */
    @RequestMapping("/delete")
    @ResponseBody
    @Transactional
    public ResponseResult delete(@RequestBody String code) {

        // 验证库位是否在使用中
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("stockAreaCode", code);
        wrapper.eq("inUse", SysParamConstant.P_IN_USE);
        Integer selectCount = sysStockPositionMapper.selectCount(wrapper);
        if (selectCount > 0) {
            return ResponseResult.buildCheck("1", "该库区的库位正在使用中，无法删除。", null);
        }

        // 删除优先级
        sysSpPriorityDataMapper.deleteBycode(code);
        sysSpPriorityMapper.deleteBycode(code);
        // 删除库区+库位
        QueryWrapper wrapper2 = new QueryWrapper();
        wrapper2.eq("stockAreaCode", code);
        sysStockPositionMapper.delete(wrapper2);
        sysStockAreaMapper.delete(wrapper2);

        return ResponseResult.buildOK();
    }
}
