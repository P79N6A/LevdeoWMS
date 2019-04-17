package com.tre.jdevtemplateboot.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 分页对象Bean
 * @author: JDev
 * @create: 2018-11-22 09:06
 **/
public class PageBean<T> implements Serializable {

    private  static final  long serialVersionUID = 1L;

    //总记录数
    private String recordsTotal;

    //总页数
    private String pagesTotal;

    //当前页
    private Integer currentPage;

    //分页数据
    private List<T> data;


    public String getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(String recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public String getPagesTotal() {
        return pagesTotal;
    }

    public void setPagesTotal(String pagesTotal) {
        this.pagesTotal = pagesTotal;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

}
