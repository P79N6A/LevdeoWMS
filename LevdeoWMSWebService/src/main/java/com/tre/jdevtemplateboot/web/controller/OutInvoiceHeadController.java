package com.tre.jdevtemplateboot.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.mapper.OutInvoiceHeadMapper;
import com.tre.jdevtemplateboot.web.pojo.OutInvoiceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 发货单
 * </p>
 *
 * @author JDev
 * @since 2018-12-19
 */
@RestController
@RequestMapping("/outInvoiceHead")
public class OutInvoiceHeadController {

    @Autowired
    private OutInvoiceHeadMapper outInvoiceHeadMapper;

    /**
     * 查询发货单 发货单一览Page-查询按钮
     *
     * @param outInvoiceBean
     * @return
     */
    @RequestMapping("/getInvoiceList")
    public ResponseResult getInvoiceList(OutInvoiceBean outInvoiceBean) {

        outInvoiceBean.setWarehouse(CommonUtils.getWarehouseCode());
        PageHelper.startPage(outInvoiceBean.getCurrentPage(), outInvoiceBean.getLimit());
        // 获取发货单
        List<Map<String, Object>> list = outInvoiceHeadMapper.getInvoiceList(outInvoiceBean);
        return ResponseResult.buildOK(new PageInfo<Map<String, Object>>(list));
    }

    /**
     * 查询当前发货单详情-备货Page
     *
     * @param thisInvoiceCode 发货单号
     * @return
     */
    @RequestMapping("/getThisInvoiceList")
    public ResponseResult getThisInvoiceList(String thisInvoiceCode) {
        return ResponseResult.buildOK(outInvoiceHeadMapper.getThisInvoiceList(thisInvoiceCode));
    }
}
