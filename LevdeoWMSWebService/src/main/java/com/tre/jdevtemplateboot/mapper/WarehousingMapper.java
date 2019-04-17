package com.tre.jdevtemplateboot.mapper;


import com.tre.jdevtemplateboot.domain.po.Stock;
import com.tre.jdevtemplateboot.domain.po.StockLog;
import com.tre.jdevtemplateboot.domain.po.SysParm;
import com.tre.jdevtemplateboot.domain.po.SysStockPosition;
import com.tre.jdevtemplateboot.web.pojo.StockView;
import com.tre.jdevtemplateboot.web.pojo.WarehouseParameterBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WarehousingMapper {
    /**
     * 查询所有符合条件的物料交接单 并返回
     *
     * @param warehouseParameterBean
     * @return
     */
    List<WarehouseParameterBean> getWarehosingList(WarehouseParameterBean warehouseParameterBean);

    /**
     * 通过VIN获取物料CODE,VIN,车位信息
     *
     * @param vin
     * @return
     */
    WarehouseParameterBean getWarehousingByVin(@Param("vin") String vin,@Param("warehouse")String warehouse);

    /**
     * @param ws
     * @return
     */
    int updateStockPosition(WarehouseParameterBean ws);

    /**
     * @param ws
     * @return
     */
    int updateStockPositionCMDsysSPosition(WarehouseParameterBean ws);

    /**
     * @param vin
     * @return
     */
    int formatStockPosition(@Param("vin") String vin);

    /**
     * @param vin
     * @return
     */
    int delWarehousingList(@Param("vin") String vin);

    /**
     * @param map
     * @return
     */
    List<Map<String, Object>> getMapWarehosingList(Map<String, Object> map);

    /**
     * 更新成车辆入库状态
     *
     * @param userCode
     * @param operator
     * @return
     */
    int batchUpdateWarehousing(@Param("userCode") String userCode, @Param("operator") String operator);

    /**
     * 更新成车辆入库状态
     *
     * @param wplist
     * @return
     */
    int updateIntoWarehousing(List<WarehouseParameterBean> wplist);

    /**
     * 钥匙入库 修改warehousing表内状态
     *
     * @param wpList
     * @return
     */
    int updateKeyIntoWarehousing(List<WarehouseParameterBean> wpList);

    /***
     * 钥匙入库 写入stock表内
     * @param wpList
     * @return
     */
    int updateKeyIntoWarehousingCmdStock(List<WarehouseParameterBean> wpList);

    /**
     * 修改非下线入库状态
     *
     * @param wpList
     * @return
     */
    int updateOnlineIntoWarehousing(List<WarehouseParameterBean> wpList);

    /**
     * 修改非下线状态操作sysSP表
     *
     * @param wpList
     * @return
     */
    int updateOnlineIntoWarehousingCmdsysSPosition(List<WarehouseParameterBean> wpList);

    /**
     * @param stockPosition
     * @return
     */
    SysStockPosition getPositionIsUsed(@Param("stockPosition") String stockPosition);

    /**
     * @param vin
     * @return
     */
    Stock getStockInfoByVin(@Param("vin") String vin);

    /**
     * 获取入库的三种状态 待入库 车辆入库 在库
     *
     * @return
     */
    List<SysParm> getWarehousingStateByCode();

    /**
     * 释放库位号
     *
     * @param vin
     * @return
     */
    int releaseStockByVin(@Param("vin") String vin);

    /**
     * 保存库位信息 用于入库一览 分配库位
     *
     * @param stockLog
     * @return
     */
    int saveWarehousingInfo(StockLog stockLog);

    /**
     * @param vin
     * @return
     */
    WarehouseParameterBean getStockInfoByVinOnline(@Param("vin") String vin,@Param("warehouse") String warehouse);

    /**
     * 从库存表内获取物料编码
     *
     * @param vin
     * @return
     */
    StockView getMatCodeByVin(String vin);

    /**
     * 从预定入库表内获取物料编码
     *
     * @param vin
     * @return
     */
    StockView getMatCodeByVinFromWs(String vin);

    /***
     * 获取库存表中所有对应vin的值
     * @param vin
     * @return
     */
    List<StockView> queryStockInfoByVin(List<String> vin);

    /***
     * 通过VIN查询非下线入库修改的信息
     * @param wpList
     * @return
     */
    List<WarehouseParameterBean> queryOnlineUpdatingInfo(List<WarehouseParameterBean> wpList);

    /**
     * 通过VIN查询warehousing表内数据
     * @param wpList
     * @return
     */
    List<WarehouseParameterBean>queryAllWsInfoByVin(List<WarehouseParameterBean>wpList);
    /**
     * 检查pda生成的文件
     *
     * @param userCode
     * @param warehouseCode
     * @return
     */
    String checkPdaResult(@Param("userCode") String userCode, @Param("warehouseCode") String warehouseCode);
}
