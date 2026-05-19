package com.jeesite.modules.core.dao;

import java.util.List;
import java.util.Map;
import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.core.entity.CoreSpb;

/**
 * 商品表 DAO 接口
 * @author pdyong
 * @version 2026-04-03
 */
@MyBatisDao
public interface CoreSpbDao extends CrudDao<CoreSpb> {
	
	/**
	 * 查询商品销售额前5
	 */
	List<Map<String, Object>> findTop5Sales();
	
}