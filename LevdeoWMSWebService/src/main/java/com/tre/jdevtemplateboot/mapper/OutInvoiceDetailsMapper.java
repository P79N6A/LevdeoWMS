package com.tre.jdevtemplateboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tre.jdevtemplateboot.domain.po.OutInvoiceDetails;
import com.tre.jdevtemplateboot.domain.po.Stock;
import com.tre.jdevtemplateboot.web.pojo.AsnReportBean;
import com.tre.jdevtemplateboot.web.pojo.OutBoundOrderBean;
import com.tre.jdevtemplateboot.web.pojo.StockView;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 出库单Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2018-12-19
 */
public interface OutInvoiceDetailsMapper extends BaseMapper<OutInvoiceDetails> {

    /**
     * 通过单号-查询当前发货单VIN
     *
     * @param thisInvoiceCode 发货单号
     * @param orderColumn     排序
     * @param orderDir        排序
     * @return
     */
    List<Map<String, Object>> getThisInvoiceVIN(@Param("thisInvoiceCode") String thisInvoiceCode,
                                                @Param("orderColumn") String orderColumn, @Param("orderDir") String orderDir);

    /**
     * 更新发货单中的VIN
     *
     * @param oldVIN     旧VIN
     * @param newVIN     新VIN
     * @param updateId
     * @param updateTime
     * @return
     */
    Integer updateInvoiceDetailsVIN(@Param("oldVIN") String oldVIN, @Param("newVIN") String newVIN,
                                    @Param("updateId") String updateId, @Param("updateTime") Date updateTime);

    /**
     * 出库单一览
     *
     * @param outBoundOrderBean
     * @return
     */
    List<Map<String, Object>> getOutBoundOrder(OutBoundOrderBean outBoundOrderBean);

    /**
     * 获取 待确认、完成 的出库单
     *
     * @param outBoundOrderBean
     * @return
     */
    List<Map<String, Object>> getConfirmAndCompleteOrders(OutBoundOrderBean outBoundOrderBean);

    /**
     * 取得没有pdi检测的车辆个数
     *
     * @param sapInvoiceCode 发货单号
     * @return
     */
    int getNoPdiCheckCnt(@Param("sapInvoiceCode") String sapInvoiceCode);

    /**
     * 当前出库单详情
     *
     * @param thisOutBoundCode 出库单号
     * @return
     */
    List<Map<String, Object>> getThisOutBoundOrder(String thisOutBoundCode);

    /**
     * 当前出库单VIN
     *
     * @param thisOutBoundCode 出库单号
     * @param orderColumn      排序
     * @param orderDir         排序
     * @return
     */
    List<Map<String, Object>> getThisOutBoundVIN(@Param("thisOutBoundCode") String thisOutBoundCode,
                                                 @Param("orderColumn") String orderColumn, @Param("orderDir") String orderDir);

    /**
     * 查询发货单-by VIN
     *
     * @param vin
     * @param warehouse 仓库
     * @return
     */
    Map<String, Object> getInvoiceByVin(@Param("vin") String vin, @Param("warehouse") String warehouse);

    /**
     * 随车配件、宣传品确认，更新状态
     *
     * @param outBoundCode 出库单单号
     * @param statusColumn 赠品 or 宣传品
     * @param userCode     确认者工号
     */
    void updateStatusByOutBoundCode(
            @Param("outBoundCode") String outBoundCode,
            @Param("checkerColumn") String checkerColumn,
            @Param("statusColumn") String statusColumn,
            @Param("userCode") String userCode
    );

    /**
     * 打印出库单
     *
     * @param outBoundCode 出库单单号
     * @param userCode     确认者工号
     */
    void outBound(@Param("outBoundCode") String outBoundCode,
                  @Param("userCode") String userCode
    );

    /**
     * 修改出库单中车辆的状态
     *
     * @param sapInvoiceCode 出库单单号
     * @param userCode       确认者工号
     * @param status
     */
    void chgCarStatusOfASN(@Param("sapInvoiceCode") String sapInvoiceCode,
                           @Param("userCode") String userCode,
                           @Param("status") String status
    );

    /**
     * @param invoiceCode
     * @param oldVin
     * @param newVin
     * @param updateId
     * @return
     */
    int changeCarsVin(
            @Param("invoiceCode") String invoiceCode,
            @Param("oldVin") String oldVin,
            @Param("newVin") String newVin,
            @Param("updateId") String updateId
    );

    /**
     * outInvoiceDetails表中，通过发货单单号invoiceCode查找所有的VIN，关联库存表stock，取得入库日期较晚的VIN。
     * outInvoiceDetails表中删除
     *
     * @param map
     */
    void delRedundant(Map<String, Object> map);

    /**
     * 根据 invoiceCode，查询outInvoiceDetails，并关联出 成品库
     *
     * @param invoiceCode 发货单单号
     * @return
     */
    List<Map<String, Object>> getByInvoiceCode(@Param("invoiceCode") String invoiceCode);

    /**
     * 更新退货单到库存表（stock）
     *
     * @param sapInvoiceCode
     */
    void outBoundForReturn(@Param("sapInvoiceCode") String sapInvoiceCode, @Param("userCode") String userCode);

    /**
     * 出库单报表
     *
     * @param bean
     * @return
     */
    List<Map<String, Object>> getAsnReport(AsnReportBean bean);

    /**
     * 更新出库的司机 10097454 20190415
     *
     * @return
     */
    int updateOutStockDriver(@Param("userCode") String userCode, @Param("operator") String operator);

    /**
     * 添加车辆，获取VIN
     *
     * @param outBoundOrderBean
     * @return
     */
    List<Map<String, Object>> getAddVins(OutBoundOrderBean outBoundOrderBean);

    /**
     * 取得行项目单位的需求数和已分配数
     *
     * @param sapInvoiceCode
     */
    List<Map<String, Object>> getReAndAllot(@Param("sapInvoiceCode") String sapInvoiceCode);

    /**
     * @param sapInvoiceCode
     * @return
     */
    int getVinCntBySapInvoiceCode(String sapInvoiceCode);

}
