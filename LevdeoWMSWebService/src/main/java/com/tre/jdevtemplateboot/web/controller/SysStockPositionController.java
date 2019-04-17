package com.tre.jdevtemplateboot.web.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tre.jdevtemplateboot.common.constant.SysParamConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.domain.po.SysSpPriority;
import com.tre.jdevtemplateboot.domain.po.SysSpPriorityData;
import com.tre.jdevtemplateboot.domain.po.SysStockPosition;
import com.tre.jdevtemplateboot.mapper.SysSpPriorityDataMapper;
import com.tre.jdevtemplateboot.mapper.SysSpPriorityMapper;
import com.tre.jdevtemplateboot.mapper.SysStockPositionMapper;
import com.tre.jdevtemplateboot.service.ISysStockPositionService;
import com.tre.jdevtemplateboot.web.pojo.StockPositionParameterBean;

/**
 * <p>
 * 库位管理
 * </p>
 *
 * @author SongGuiFan
 * @since 2018-12-21
 */
@RestController
@RequestMapping("/sys-stock-position")
public class SysStockPositionController {

    @Autowired
    private SysStockPositionMapper sysStockPositionMapper;
    @Autowired
    private ISysStockPositionService sysStockPositionService;
    @Autowired
    private SysSpPriorityMapper sysSpPriorityMapper;
    @Autowired
    private SysSpPriorityDataMapper sysSpPriorityDataMapper;

    /**
     * 库位列表查询
     *
     * @param params 库位对象
     * @return
     */
    @RequestMapping("/getPositionList")
    public ResponseResult getPositionList(StockPositionParameterBean params) {
        PageHelper.startPage(params.getCurrentPage(), params.getLimit());
        List<Map<String, Object>> list = sysStockPositionMapper.getPositionList(params);
        return ResponseResult.buildOK(new PageInfo<Map<String, Object>>(list));
    }

    /**
     * 库位详情
     *
     * @param warehouse
     * @param stockPosition
     * @return
     */
    @RequestMapping("/getPositionPrioritys")
    public ResponseResult getPositionPrioritys(
            @RequestParam("warehouse") String warehouse, @RequestParam("stockPosition") String stockPosition) {

        Map<String, Object> map = new HashMap<>();
        // 获取仓库的优先级+优先级名称
        Map<String, Object> prioritys = sysStockPositionMapper.getPrioritys(warehouse);
        //查询仓库优先级
        map.put("prioritys", prioritys);
        // 左 select
        //查询品牌优先级
        map.put("colorPriority", sysStockPositionMapper.getColorPriority());
        //查询系列优先级
        map.put("brandPriority", sysStockPositionMapper.getBrandPriority());
        //查询颜色优先级
        map.put("seriesPriority", sysStockPositionMapper.getSeriesPriority());

        // 右 select
        // 查询当前库位的优先策略
        for (int a = 1; a < 4; a++) {
            //String priorityCode = "0" + a;
            String priorityLV = prioritys.get("priorityLV" + a).toString();
            List<Map<String, Object>> forList = null;
            switch (priorityLV) {
                case SysParamConstant.P_PARKING_POLICY_SERIES://a系列
                    forList = sysSpPriorityMapper.getSPLVSeries(stockPosition, priorityLV);
                    break;
                case SysParamConstant.P_PARKING_POLICY_BRAND://a品牌
                    forList = sysSpPriorityMapper.getSPLVBrand(stockPosition, priorityLV);
                    break;
                case SysParamConstant.P_PARKING_POLICY_COLOR://a颜色
                    forList = sysSpPriorityMapper.getSPLVColor(stockPosition, priorityLV);
                    break;
            }
            map.put("priorityData" + a, forList);
        }

        return ResponseResult.buildOK(map);
    }

    /**
     * 查询仓库列表
     *
     * @return
     */
    @RequestMapping("/getWarehouse")
    public ResponseResult getWarehouse() {
        return ResponseResult.buildOK(sysStockPositionMapper.getWarehouse());
    }

    /**
     * 查询库区列表
     *
     * @return
     */
    @RequestMapping("/getStockArea")
    public ResponseResult getStockArea(String warehouseCode) {
        return ResponseResult.buildOK(sysStockPositionMapper.getStockArea(warehouseCode));
    }

    /**
     * 锁定库位
     *
     * @return
     */
    @RequestMapping("/lockPosition")
    public ResponseResult lockPosition(String stockPosition, String lockReason) {
        SysStockPosition sysStockPosition = new SysStockPosition();
        sysStockPosition.setStockPosition(stockPosition);
        sysStockPosition.setUpdateId(CommonUtils.getUserCode());
        sysStockPosition.setUpdateTime(CommonUtils.getCurrentDateTime());
        sysStockPosition.setIsLock(SysParamConstant.P_LOCK_ON);
        sysStockPosition.setLockReason(lockReason);
        //a锁定库位
        sysStockPositionService.updateById(sysStockPosition);

        return ResponseResult.buildOK();
    }

    /**
     * 解锁库位
     *
     * @return
     */
    @RequestMapping("/unLockPosition")
    public ResponseResult unLockPosition(String stockPosition) {
        SysStockPosition sysStockPosition = new SysStockPosition();
        sysStockPosition.setStockPosition(stockPosition);
        sysStockPosition.setUpdateId(CommonUtils.getUserCode());
        sysStockPosition.setUpdateTime(CommonUtils.getCurrentDateTime());
        sysStockPosition.setIsLock(SysParamConstant.P_LOCK_OFF);
        sysStockPosition.setLockReason("");
        //a解锁库位
        sysStockPositionService.updateById(sysStockPosition);

        return ResponseResult.buildOK();
    }

    /**
     * 获取可以拷贝的库位
     * 同库区-不验证状态
     *
     * @param stockAreaCode
     * @return
     */
    @RequestMapping("/getCopySp")
    public ResponseResult getCopySp(String stockAreaCode) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("stockAreaCode", stockAreaCode);

        return ResponseResult.buildOK(sysStockPositionMapper.selectList(queryWrapper));
    }

    /**
     * 拷贝-批量插入库位优先策略
     *
     * @param startSp       起始库位
     * @param endSp         终止库位
     * @param stockPosition 模板库位
     * @param radioChoose   是否覆盖
     * @return
     */
    @RequestMapping("/updateSpPrioBatch")
    @Transactional
    public ResponseResult updateSpPrioBatch(String startSp, String endSp, String stockPosition, String radioChoose) {

        //a获取模板库位的优先策略
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("stockPosition", stockPosition);
        List<SysSpPriority> selectList = sysSpPriorityMapper.selectList(queryWrapper);
        List<SysSpPriorityData> selectList2 = sysSpPriorityDataMapper.selectList(queryWrapper);
        //a验证模板库位优先策略是否存在
        if (selectList.size() < 1 || selectList2.size() < 1) {
            return ResponseResult.buildCheck("-1", "模板库位优先策略不存在。", null);
        }

        String userCode = CommonUtils.getUserCode();
        sysSpPriorityMapper.insertSpPriority(stockPosition, startSp, endSp, radioChoose, userCode);

        return ResponseResult.buildOK();
    }
}
