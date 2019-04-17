package com.tre.jdevtemplateboot.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tre.jdevtemplateboot.common.constant.SysParamConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.domain.po.Stock;
import com.tre.jdevtemplateboot.domain.po.SysSpPriorityData;
import com.tre.jdevtemplateboot.domain.po.SysStockPosition;
import com.tre.jdevtemplateboot.mapper.StockMapper;
import com.tre.jdevtemplateboot.mapper.SysSpPriorityDataMapper;
import com.tre.jdevtemplateboot.mapper.SysStockPositionMapper;
import com.tre.jdevtemplateboot.mapper.WarehousingMapper;
import com.tre.jdevtemplateboot.service.ISysSpPriorityDataService;
import com.tre.jdevtemplateboot.web.pojo.StockView;
import com.tre.jdevtemplateboot.web.pojo.WarehouseParameterBean;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author JDev
 * @since 2018-12-24
 */
@Service
public class SysSpPriorityDataServiceImpl extends ServiceImpl<SysSpPriorityDataMapper, SysSpPriorityData> implements ISysSpPriorityDataService {

    @Autowired
    WarehousingMapper warehousingMapper;
    @Autowired
    SysSpPriorityDataMapper sysSpPriorityDataMapper;
    @Autowired
    private SysStockPositionMapper sysStockPositionMapper;
    @Autowired
    private StockMapper stockMapper;


    /**
     * 获取新库位 入库一览-修改库位号
     *
     * @return
     */
    public List<Map<String, Object>> getStockPosition(String vin) {
        // 获取当前仓库
        String warehouseCode = CommonUtils.getWarehouseCode();
        //获取vin对应的物料编码
        WarehouseParameterBean ws = warehousingMapper.getWarehousingByVin(vin,CommonUtils.getWarehouseCode());
        //拼接-优先级规则
        String brand = ws.getMatCode().substring(0, 1);
        String color = ws.getMatCode().substring(10, 11);
        String series[] = ws.getMatDescription().split("\\.");
        String priorityRule = brand + "_" + color + "_" + series[2];
        //获取库位-通过优先级规则
        List<Map<String, Object>> spList = sysSpPriorityDataMapper.getSpByPrioRule(warehouseCode, priorityRule);
        return spList;
    }

    /**
     * 获取库位 入库车辆分配库位-批处理
     * <p>
     * state=0表示 暂时未分配到当前型号的库位号
     * state=1表示已经分配库位号
     * 以String 形式返回
     * 根据以上返回结果判断是否继续操作
     *
     * @param vinList
     * @return
     */
    @Transactional
    public String updateStockPosition(List<String> vinList) {
        String updateId = CommonUtils.getUserCode();
        Date updateTime = CommonUtils.getCurrentDateTime();
        String warehouseCode = CommonUtils.getWarehouseCode();
        String state = "0";
        for (String vin : vinList) {
            //获取vin对应的物料编码
            StockView sv = warehousingMapper.getMatCodeByVin(vin);
            //拼接-优先级规则
            String brand = sv.getMatCode().substring(0, 1);
            String color = sv.getMatCode().substring(10, 11);
            String series[] = sv.getMatDescription().split("\\.");
            String priorityRule = brand + "_" + color + "_" + series[2];
            //获取可用的库位
            List<Map<String, Object>> list = sysSpPriorityDataMapper.getSpByPrioRule(warehouseCode, priorityRule);

            if (list == null || list.size() == 0) {
                return state;
            }
            String stockPosition = list.get(0).get("stockPosition").toString();
            SysStockPosition sysStockPosition = new SysStockPosition();
            sysStockPosition.setStockPosition(stockPosition);
            sysStockPosition.setInUse(SysParamConstant.P_IN_USE);
            sysStockPosition.setUpdateId(updateId);
            sysStockPosition.setUpdateTime(updateTime);
            //更新库位状态-使用中
            sysStockPositionMapper.updateById(sysStockPosition);

            Stock stock = new Stock();
            stock.setVin(vin);
            stock.setStockPosition(stockPosition);
            stock.setUpdateId(updateId);
            stock.setUpdateTime(updateTime);
            //更新VIN对应的库位号
            stockMapper.updateById(stock);
            state = "1";
        }
        return state;
    }
}
