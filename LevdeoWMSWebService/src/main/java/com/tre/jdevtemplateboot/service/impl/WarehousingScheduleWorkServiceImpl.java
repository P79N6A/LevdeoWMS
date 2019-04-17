package com.tre.jdevtemplateboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tre.jdevtemplateboot.common.constant.SysParamConstant;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.domain.po.SysStockPosition;
import com.tre.jdevtemplateboot.domain.po.SysUser;
import com.tre.jdevtemplateboot.domain.po.WarehousingSchedule;
import com.tre.jdevtemplateboot.domain.po.WarehousingScheduleWork;
import com.tre.jdevtemplateboot.mapper.SysUserMapper;
import com.tre.jdevtemplateboot.mapper.WarehousingScheduleWorkMapper;
import com.tre.jdevtemplateboot.service.IWarehousingScheduleWorkService;
import com.tre.jdevtemplateboot.web.pojo.StockView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * SAP调JAVA，入库交接单
 * </p>
 *
 * @author JDev
 * @since 2019-02-19
 */
@Service
public class WarehousingScheduleWorkServiceImpl extends ServiceImpl<WarehousingScheduleWorkMapper, WarehousingScheduleWork> implements IWarehousingScheduleWorkService {
    @Autowired
    private WarehousingScheduleWorkMapper warehousingScheduleWorkMapper;
    @Autowired
    private com.tre.jdevtemplateboot.mapper.WarehousingMapper WarehousingMapper;
    @Autowired
    private com.tre.jdevtemplateboot.mapper.WarehousingScheduleMapper WarehousingScheduleMapper;
    @Autowired
    private com.tre.jdevtemplateboot.mapper.SysSpPriorityDataMapper SysSpPriorityDataMapper;
    @Autowired
    private com.tre.jdevtemplateboot.mapper.SysStockPositionMapper SysStockPositionMapper;
    @Autowired
    private SysUserMapper SysUserMapper;

    /**
     * SAP调JAVA，入库交接单
     * @param wsWorks
     */
    @Override
    public String rfcStorageRec(List<WarehousingScheduleWork> wsWorks) {
        String result = "";

        String workId = getWorkId();
        for (WarehousingScheduleWork work : wsWorks) {
            work.setWorkId(workId);
        }
        //将SAP同步过来的数据存储到WORK表内；
        saveBatch(wsWorks);

        //通过work表更新入库交接单表
        warehousingScheduleWorkMapper.updateWarehousingWork(workId);

        //获取已插入的所用数据
        List<WarehousingSchedule> works = warehousingScheduleWorkMapper.getInsertedVin(workId);

        /************************开始分配库位*************************************/
        if(works.size()>0){
            //通过sap账号获取usercode信息
            String userCode = warehousingScheduleWorkMapper.getUserCode(workId);

            for(WarehousingSchedule ws : works){
                //获取vin对应的物料编码
                StockView sv = WarehousingMapper.getMatCodeByVinFromWs(ws.getVin());

                if(sv == null){
                    result += ws.getWsTransition()+"-"+ws.getVin()+"(入库交接单-VIN码)对应的物料，在物料表中不存在；";
                    continue;
                }

                //拼接-优先级规则
                String brand = sv.getMatCode().substring(0, 1);
                String color = sv.getMatCode().substring(10, 11);
                String series[] = sv.getMatDescription().split("\\.");
                String priorityRule = brand + "_" + color + "_" + series[2];

                //获取同批的createId
                SysUser sysUser = SysUserMapper.selectById(ws.getCreateId());
                if(sysUser == null){
                    result += sv.getWsTransition()+"-"+sv.getVin()+"(入库交接单-VIN码)对应的sap员工号，在wms员工表中不存在；";
                    continue;
                }
                //获取同批的仓库编码
                String warehouseCode = sysUser.getWarehouse();
                //获取可用的库位
                List<Map<String, Object>> list = SysSpPriorityDataMapper.getSpByPrioRule(warehouseCode, priorityRule);

                if (list != null && list.size() != 0){
                    String stockPosition = list.get(0).get("stockPosition").toString();
                    SysStockPosition sysStockPosition = new SysStockPosition();
                    sysStockPosition.setStockPosition(stockPosition);
                    sysStockPosition.setInUse(SysParamConstant.P_IN_USE);
                    sysStockPosition.setUpdateId(userCode);
                    sysStockPosition.setUpdateTime(CommonUtils.getCurrentDateTime());
                    //更新库位状态-使用中
                    SysStockPositionMapper.updateById(sysStockPosition);
                    ws.setStockPosition(stockPosition);
                    ws.setUpdateId(userCode);
                    ws.setUpdateTime(CommonUtils.getCurrentDateTime());
                    //更新VIN对应的库位号
                    WarehousingScheduleMapper.updateById(ws);
                }
            }
        }
        /************************结束分配库位*************************************/

        //分配完库位号后删除work表中数据
        QueryWrapper<WarehousingScheduleWork> wrapper = new QueryWrapper<>();
        wrapper.eq("workId",workId);
        remove(wrapper);

        return result;
    }

    /**
     * 设置work插入批次的id
     * @return
     */
    private String getWorkId() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String str = simpleDateFormat.format(new Timestamp(System.currentTimeMillis()));
        return str + (int) (100 + Math.random() * 900);
    }

}
