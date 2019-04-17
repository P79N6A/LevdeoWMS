package com.tre.jdevtemplateboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tre.jdevtemplateboot.domain.po.OutInvoiceHead;
import com.tre.jdevtemplateboot.web.pojo.OutInvoiceBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 发货单Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2018-12-19
 */
public interface OutInvoiceHeadMapper extends BaseMapper<OutInvoiceHead> {

    /**
     * 查询发货单
     *
     * @param outInvoiceBean
     * @return
     */
    List<Map<String, Object>> getInvoiceList(OutInvoiceBean outInvoiceBean);

    /**
     * 通过单号-查询当前发货单详情
     *
     * @param thisInvoiceCode 发货单号
     * @return
     */
    List<Map<String, Object>> getThisInvoiceList(String thisInvoiceCode);


    /**
     * 打印备货单-根据发货单号查询
     *
     * @param invoiceCode 发货单号
     * @return
     */
    Map<String, Object> getHeadDataByInvoiceCode(@Param("invoiceCode") String invoiceCode);

    /**
     * 冲销
     *
     * @param sapInvoiceCode
     */
    void doWash(
            @Param("sapInvoiceCode") String sapInvoiceCode,
            @Param("userCode") String userCode,
            @Param("P_ASN_STATUS_WAITSEND") String P_ASN_STATUS_WAITSEND,
            @Param("P_CAR_STATUS_SENDING") String P_CAR_STATUS_SENDING,
            @Param("P_STOCKLOG_WASH") String P_STOCKLOG_WASH
    );

    /**
     * 冲销
     *
     * @param sapInvoiceCode
     */
    void doWashForReturn(
            @Param("sapInvoiceCode") String sapInvoiceCode,
            @Param("userCode") String userCode
    );

    /**
     * 将打印出库单的状态改为1
     * @param sapInvoiceCode
     */
    void printInvoice(@Param("sapInvoiceCode") String sapInvoiceCode);

    /**
     *冲销后，将重置打印次数
     * @param sapInvoiceCode
     */
    void cancelPrintInvoice(@Param("sapInvoiceCode") String sapInvoiceCode);
}
