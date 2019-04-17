package com.tre.jdevtemplateboot.mapper;

import com.tre.jdevtemplateboot.domain.po.MainCustomers;
import com.tre.jdevtemplateboot.domain.po.MainSaleCompany;
import com.tre.jdevtemplateboot.domain.po.SysParm;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tre.jdevtemplateboot.web.pojo.MainData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2018-12-17
 */
public interface SysParmMapper extends BaseMapper<SysParm> {
    /**
     * @return
     */
    List<Map<String, Object>> getPriorityList();

    /**
     * 通过subCode获取表名
     * @param subCode
     * @return
     */
    String getTableName(@Param("subCode")String subCode);

    /**
     * 通过表名获取该表内的数据
     * @param tbName
     * @return
     */
    List<Map<String, Object>> getMainDataInfos(@Param("tbName")String tbName);

    /**
     * 保存主数据通过tbName
     * @param mainData
     * @return
     */
    int saveMainData(MainData mainData);

    /**
     * 查询code和name是否有重复
     * code 重复为 1 否则为 0
     * name 重复为 1 否则为 0
     * @param subCode
     * @param code
     * @param
     * @return
     */
    int selectMainDataCodeIsREPEAT(@Param("subCode")String subCode,@Param("code")String code);
    int selectMainDataNameIsREPEAT(@Param("subCode")String subCode,@Param("name")String name);

    /**
     * 获取客户信息
     * @param subCode
     * @return
     */
    List<MainCustomers> getCustomerList(@Param("subCode")String subCode);

    /**
     * 获取公司销售对应信息
     * @param subCode
     * @return
     */
    List<MainSaleCompany> getSaleCompanyList(@Param("subCode")String subCode);


    /**
     * 通过表名更新数据
     * @param subCode
     * @param code
     * @param name
     * @param updateId
     * @return
     */
    int updateMainMatById(@Param("subCode")String subCode,@Param("code")String code,@Param("name")String name,@Param("updateId")String updateId);
}
