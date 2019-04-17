package com.tre.jdevtemplateboot.mapper;

import com.tre.jdevtemplateboot.domain.po.Stock;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tre.jdevtemplateboot.web.pojo.ChargParameterBean;
import com.tre.jdevtemplateboot.web.pojo.StockView;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2018-12-15
 */
public interface StockMapper extends BaseMapper<Stock> {
    /**
     * 库存一览
     *
     * @return
     */
    List<Map<String, Object>> serchAll(StockView sv);


    /**
     * 初始化查询库存状态
     *
     * @return
     */
    List<Map<String, Object>> searchInCS();


    /**
     * 查询用户所在仓库的库位号
     *
     * @param userCode
     * @return
     */


    String searchWarehouse(String userCode);

    /**
     * 批量查询库存状态
     *
     * @return
     */
    List<Map<String, Object>> searchCS();


    /**
     * 根据vin查询状态
     *
     * @param subCode
     * @return
     */
    String serchSatus(String subCode);

    /**
     * @return
     */
    List<Map<String, Object>> searchChargeCar(ChargParameterBean pb);

    /**
     * @return
     */
    List<Map<String, Object>> searchOutofdateStock(ChargParameterBean pb);

    /**
     * 获取可用的VIN-根据状态+搜索条件查
     *
     * @param searchstr
     * @param matCode
     * @return
     */
    List<Map<String, Object>> getAvailableVIN(@Param("searchstr") String searchstr, @Param("matCode") String matCode);

    /**
     * 更改VIN-库位
     *
     * @param vin
     * @param stockPosition
     */
    void updateStockPosition(@Param("vin") Object vin, @Param("stockPosition") Object stockPosition);

    /**
     * 更改库位的状态-inUse
     *
     * @param stockPosition
     * @param inUse
     * @param updateid
     * @param updatetime
     */
    void updatePositionInUse(@Param("stockPosition") String stockPosition, @Param("inUse") String inUse,
                             @Param("updateid") String updateid, @Param("updatetime") Date updatetime);

    /**
     * 清除移库临时表
     *
     * @param userCode
     * @author SongGuiFan
     */
    void deletePoitionWork(String userCode);

    /**
     * 查询移库列表
     *
     * @param stock 库存实体
     * @return
     * @author SongGuiFan
     */
    List<Map<String, Object>> searchMove(StockView stock);

    /**
     * 校验Excel文件数据，返回错误信息
     *
     * @param userCode 登录人
     * @return
     * @author SongGuiFan
     */
    List<Map<String, String>> checkExcel(String userCode);

    /**
     * 批量移库
     *
     * @param userCode
     * @author SongGuiFan
     */
    void bachUpdatePosition(String userCode);

    /**
     * 取得不存在vin
     *
     * @param userCode
     * @param warehouseCode
     * @return
     */
    String getNotExistVins(@Param("userCode") String userCode, @Param("warehouseCode") String warehouseCode);

    /**
     * 查询无法批量更新的vin码
     *
     * @param userCode
     * @param warehouseCode
     * @return
     */
    String getWrongStatusVins(@Param("userCode") String userCode, @Param("warehouseCode") String warehouseCode);

    /**
     * 更新状态，插入日志
     *
     * @param userCode
     * @param status
     * @param reason
     */
    void updateStatusAndLog(@Param("userCode") String userCode, @Param("status") String status, @Param("reason") String reason);

    /**
     * 更新旧库位
     *
     * @param userCode
     * @author SongGuiFan
     */
    void updateOldPosition(String userCode);

    /**
     * 发货通知单接口，删除或更新，
     * outInvoiceDetails表中，根据发货单单号invoiceCode获取所有VIN，
     * 库存表stock中对应VIN码状态变为 03:在库，更新人为sap员工号对应的wms员工号
     *
     * @param map （invoiceCode 发货单单号，status 状态，sapUserCode 制单人-SAP员工号）
     */
    void updateInterfaceVinStatus(Map<String, Object> map);

    /**
     * 库存表中，根据物料编码matCode，取得(03:在库、05:充电)：待分配、02:未锁定 的车辆数量
     *
     * @param map
     * @return
     */
    int searchAssignedStocksCnt(Map<String, Object> map);

    /**
     * 库存表中，根据物料编码matCode，按时间从早到晚，取得(03:在库、05:充电)：待分配、02:未锁定 车辆，插入到发货单详情表outInvoiceDetails
     *
     * @param map
     */
    void insertAssignedStocks(Map<String, Object> map);

    /**
     * 更新 stock 库存表中取得的待分配车辆状态 03：待分配->04：待发货
     *
     * @param map
     */
    void updateAssignedStocks(Map<String, Object> map);

    /**
     * outInvoiceDetails表中，通过发货单单号invoiceCode查找所有的VIN，关联库存表stock，取得入库日期较晚的VIN。
     * stock库存表中更新状态 04：待发货->03:在库 ===>(03:在库、05:充电)：待分配，这里只更新成 03:在库
     *
     * @param map
     */
    void updateStatusToInstorage(Map<String, Object> map);
}
