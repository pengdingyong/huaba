package com.jeesite.modules.core.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.core.entity.CoreDdb;

import java.util.List;
import java.util.Map;

/**
 * 订单表 DAO 接口
 * @author pdyong
 * @version 2026-04-03
 */
@MyBatisDao
public interface CoreDdbDao extends CrudDao<CoreDdb> {
	
	/**
	 * 按日期分组查询订单金额
	 */
	List<Map<String, Object>> findChartData();
	
}