package com.tre.jdevtemplateboot.service;

import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.server.JCoServerContext;

import java.util.Date;
import java.util.Map;

/**
 * SAP调JAVA 服务类
 */
public interface SAPInterfaceService {
    /**
     * 入库交接单
     * @param serverCtx
     * @param function
     */
    void storage_rec(JCoServerContext serverCtx, JCoFunction function);

    /**
     * 发货通知单
     * @param serverCtx
     * @param function
     */
    void consignment_note(JCoServerContext serverCtx, JCoFunction function);

    /**
     * 整车借用调拨归还
     * @param serverCtx
     * @param function
     */
    void borrow_doc(JCoServerContext serverCtx, JCoFunction function);

    /**
     * 出库单
     * @param sapInvoiceCode sap出库单号
     */
    Map<String, Object> stock_out(String sapInvoiceCode,String isPrint) throws JCoException;

    /**
     * 出库单冲销
     * @param sapInvoiceCode sap出库单号
     */
    Map<String, Object> wash_trans(String sapInvoiceCode) throws JCoException;

    /**
     * sap同步：物料主数据
     * @param date
     */
    void sapSyncWms_mainMat(Date date);

    /**
     * sap同步：客户主数据
     * @param date
     */
    void sapSyncWms_mainCustomers(Date date);
}
