package com.tre.jdevtemplateboot.service.impl;

import com.tre.jdevtemplateboot.domain.po.SysWarehouse;
import com.tre.jdevtemplateboot.mapper.SysWarehouseMapper;
import com.tre.jdevtemplateboot.service.ISysWarehouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author JDev
 * @since 2018-12-20
 */
@Service
public class SysWarehouseServiceImpl extends ServiceImpl<SysWarehouseMapper, SysWarehouse> implements ISysWarehouseService {

	@Autowired
	private SysWarehouseMapper sysWarehouseMapper;
	
	@Override
	@Transactional
	public Boolean delete(String code){
		Integer count = sysWarehouseMapper.selectCount(code);
		if(count >0) {
			return true;
		}else{
			sysWarehouseMapper.delete(code);
			return false;
		}
	}

	@Override
	@Transactional
	public void saveHouse(SysWarehouse house) {
		String max = sysWarehouseMapper.selectMaxCode();
		int code=Integer.valueOf(max)+1;
		StringBuffer sb=new StringBuffer();
		String newCode=String.valueOf(code);
		for (int j = 0; j < 4-newCode.length(); j++) {
			sb.append("0");
		}
		if(newCode.length()<=4){
			newCode=sb.toString()+newCode;
		}
		house.setWarehouseCode(newCode);	
		sysWarehouseMapper.saveHouse(house);
	}

}
