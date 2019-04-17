package com.tre.jdevtemplateboot.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.domain.po.SysSpPriority;
import com.tre.jdevtemplateboot.domain.po.SysSpPriorityData;
import com.tre.jdevtemplateboot.mapper.SysSpPriorityDataMapper;
import com.tre.jdevtemplateboot.mapper.SysSpPriorityMapper;
import com.tre.jdevtemplateboot.service.ISysSpPriorityDataService;
import com.tre.jdevtemplateboot.service.ISysSpPriorityService;

/**
 * <p>
 * 优先级策略
 * </p>
 *
 * @author JDev
 * @since 2018_12_25
 */
@RestController
@RequestMapping("/sys-sp-priority")
public class SysSpPriorityController {

	@Autowired
	private SysSpPriorityMapper sysSpPriorityMapper;
	@Autowired
	private ISysSpPriorityService sysSpPriorityService;
	@Autowired
	private SysSpPriorityDataMapper sysSpPriorityDataMapper;
	@Autowired
	private ISysSpPriorityDataService sysSpPriorityDataService;

	/**
	 * 插入 库位优先级-优先级详情 在sysSpPriority表和sysSpPriorityData表中添加数据
	 * 
	 * @param stockPosition
	 * @param priorityLV1   优先级1 Code
	 * @param data1         优先级1的数据
	 * @param priorityLV2   优先级2 Code
	 * @param data2         优先级2的数据
	 * @param priorityLV3   优先级3 Code
	 * @param data3         优先级3的数据
	 * @return
	 */
	@Transactional
	@RequestMapping("/insertPriority")
	public ResponseResult insertPriority(@RequestParam(value = "stockPosition") String stockPosition,
			@RequestParam(value = "priorityLV1") String priorityLV1, @RequestParam(value = "data1") String data1,
			@RequestParam(value = "priorityLV2") String priorityLV2, @RequestParam(value = "data2") String data2,
			@RequestParam(value = "priorityLV3") String priorityLV3, @RequestParam(value = "data3") String data3) {

		String updateId = CommonUtils.getUserCode();
		Date updateTime = CommonUtils.getCurrentDateTime();

		String[] dt1 = data1.split(",");
		String[] dt2 = data2.split(",");
		String[] dt3 = data3.split(",");
		int priorityId = 1;

		/**
		 * 操作-sysSpPriority表
		 */
		List<SysSpPriority> priorityList = new ArrayList<SysSpPriority>();
		SysSpPriority sysSpPriority;

		// a删除已存在的数据
		sysSpPriorityMapper.deleteById(stockPosition);

		// a准备插入表数据
		// a 优先级1
		for (String prioSubCode : dt1) {
			sysSpPriority = new SysSpPriority();
			sysSpPriority.setStockPosition(stockPosition);
			sysSpPriority.setPriorityCode(priorityLV1);
			sysSpPriority.setPrioritySubCode(prioSubCode);
			sysSpPriority.setPriorityId(priorityId++);
			sysSpPriority.setCreateId(updateId);
			sysSpPriority.setCreateTime(updateTime);
			sysSpPriority.setUpdateId(updateId);
			sysSpPriority.setUpdateTime(updateTime);
			priorityList.add(sysSpPriority);
		}
		// a优先级2
		priorityId = 1;
		for (String prioSubCode : dt2) {
			sysSpPriority = new SysSpPriority();
			sysSpPriority.setStockPosition(stockPosition);
			sysSpPriority.setPriorityCode(priorityLV2);
			sysSpPriority.setPrioritySubCode(prioSubCode);
			sysSpPriority.setPriorityId(priorityId++);
			sysSpPriority.setCreateId(updateId);
			sysSpPriority.setCreateTime(updateTime);
			sysSpPriority.setUpdateId(updateId);
			sysSpPriority.setUpdateTime(updateTime);
			priorityList.add(sysSpPriority);
		}
		// a 优先级3
		priorityId = 1;
		for (String prioSubCode : dt3) {
			sysSpPriority = new SysSpPriority();
			sysSpPriority.setStockPosition(stockPosition);
			sysSpPriority.setPriorityCode(priorityLV3);
			sysSpPriority.setPrioritySubCode(prioSubCode);
			sysSpPriority.setPriorityId(priorityId++);
			sysSpPriority.setCreateId(updateId);
			sysSpPriority.setCreateTime(updateTime);
			sysSpPriority.setUpdateId(updateId);
			sysSpPriority.setUpdateTime(updateTime);
			priorityList.add(sysSpPriority);
		}
		// a插入数据
		sysSpPriorityService.saveBatch(priorityList);

		/**
		 * 操作- sysSpPriorityData表
		 */
		List<SysSpPriorityData> priorityDataList = new ArrayList<SysSpPriorityData>();
		SysSpPriorityData sspData;
		String priorityRule = "";
		priorityId = 1;
		// a删除已存在的数据
		sysSpPriorityDataMapper.deleteById(stockPosition);

		// a准备插入表数据_拼接priorityRule_规则:品牌+颜色+系列
		for (String prioSubCode1 : dt1) {
			for (String prioSubCode2 : dt2) {
				for (String prioSubCode3 : dt3) {

					if (("01").equals(priorityLV1)) {// 1_系列
						if (("02").equals(priorityLV2)) {// 1_系列,2_品牌,3_颜色
							priorityRule = prioSubCode2 + "_" + prioSubCode3 + "_" + prioSubCode1;
						} else {// 1_系列,2_颜色,3_品牌
							priorityRule = prioSubCode3 + "_" + prioSubCode2 + "_" + prioSubCode1;
						}
					} else if (("02").equals(priorityLV1)) {// 1_品牌
						if (("03").equals(priorityLV2)) {// 1_品牌,2_颜色,3_系列
							priorityRule = prioSubCode1 + "_" + prioSubCode2 + "_" + prioSubCode3;
						} else {// 1_品牌,2_系列,3_颜色
							priorityRule = prioSubCode1 + "_" + prioSubCode3 + "_" + prioSubCode2;
						}
					} else if (("03").equals(priorityLV1)) {// 1_颜色
						if (("01").equals(priorityLV2)) {// 1_颜色,2_系列,3_品牌
							priorityRule = prioSubCode3 + "_" + prioSubCode1 + "_" + prioSubCode2;
						} else {// 1_颜色,2_品牌,3_系列
							priorityRule = prioSubCode2 + "_" + prioSubCode1 + "_" + prioSubCode3;
						}
					}

					sspData = new SysSpPriorityData();
					sspData.setStockPosition(stockPosition);
					sspData.setPriorityId(priorityId++);
					sspData.setPriorityRule(priorityRule);
					sspData.setCreateId(updateId);
					sspData.setCreateTime(updateTime);
					sspData.setUpdateId(updateId);
					sspData.setUpdateTime(updateTime);
					priorityDataList.add(sspData);
				}
			}
		}
		// a插入数据
		sysSpPriorityDataService.saveBatch(priorityDataList);

		return ResponseResult.buildOK();
	}
}
