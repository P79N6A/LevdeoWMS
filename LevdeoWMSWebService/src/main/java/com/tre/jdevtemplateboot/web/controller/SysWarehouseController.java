package com.tre.jdevtemplateboot.web.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tre.jdevtemplateboot.common.pojo.ResponseResult;
import com.tre.jdevtemplateboot.common.util.CommonUtils;
import com.tre.jdevtemplateboot.domain.po.SysWarehouse;
import com.tre.jdevtemplateboot.mapper.SysWarehouseMapper;
import com.tre.jdevtemplateboot.service.ISysWarehouseService;
import com.tre.jdevtemplateboot.web.pojo.ChargParameterBean;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author JDev
 * @since 2018-12-20
 */
@RestController
@RequestMapping("/sys-warehouse")
public class SysWarehouseController {
	/**
	 *
	 */
	@Autowired
	private SysWarehouseMapper sysWarehouseMapper;
	@Autowired
	private ISysWarehouseService sysWarehouseService;

	/**
	 *查询所有仓库数据
	 * @param pb
	 * @return
	 */
	@RequestMapping("/searchAll")
	@ResponseBody
	public ResponseResult searchChargeCar(ChargParameterBean pb){
		PageHelper.startPage(pb.getCurrentPage(),pb.getLimit());
		List<Map<String,Object>> list=sysWarehouseMapper.searchAll(pb);
		return ResponseResult.buildOK(new PageInfo<Map<String,Object>>(list));
	}

	/**
	 * 保存新建仓库数据
	 */
	@RequestMapping("/save")
	@ResponseBody
	public ResponseResult save(@RequestBody SysWarehouse house){
		house.setCreateId(CommonUtils.getUserCode());
		house.setUpdateId(CommonUtils.getUserCode());
		sysWarehouseService.saveHouse(house);
		return ResponseResult.buildOK(house);
	}

	/**
	 * 检索单个仓库数据
	 */
	@RequestMapping("/info")
	@ResponseBody

	public ResponseResult info(@RequestParam(value="code",required=true) String code){
		System.out.println("code:"+code);
		SysWarehouse house = sysWarehouseMapper.info(code);
		return ResponseResult.buildOK(house);
	}

	/**
	 * 修改仓库数据
	 */
	@RequestMapping("/update")
	@ResponseBody
	public ResponseResult update(@RequestBody SysWarehouse house){
		house.setUpdateId(CommonUtils.getUserCode());
		sysWarehouseMapper.update(house);
		return ResponseResult.buildOK(house);
	}

	/**
	 * 删除仓库数据
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public ResponseResult delete(@RequestBody String code){
		Boolean flag = sysWarehouseService.delete(code);
		if(flag) {
			return ResponseResult.buildCheck("1","仓库已划分库区，无法删除。",null);
		}else {
			return ResponseResult.buildOK();}

	}   
}
