package com.jeesite.modules.core.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.core.entity.CoreDdb;
import com.jeesite.modules.core.dao.CoreDdbDao;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 订单表 Service
 * @author pdyong
 * @version 2026-04-03
 */
@Service
public class CoreDdbService extends CrudService<CoreDdbDao, CoreDdb> {

	/**
	 * 获取单条数据
	 * @param coreDdb 主键
	 */
	@Override
	public CoreDdb get(CoreDdb coreDdb) {
		return super.get(coreDdb);
	}

	/**
	 * 查询列表数据
	 * @param coreDdb 查询条件
	 */
	@Override
	public List<CoreDdb> findList(CoreDdb coreDdb) {
		return super.findList(coreDdb);
	}
	
	/**
	 * 查询分页数据
	 * @param coreDdb 查询条件
	 * @param coreDdb page 分页对象
	 */
	@Override
	public Page<CoreDdb> findPage(CoreDdb coreDdb) {
		return super.findPage(coreDdb);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param coreDdb 数据对象
	 */
	@Override
	@Transactional
	public void save(CoreDdb coreDdb) {
		super.save(coreDdb);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(coreDdb, coreDdb.getId(), "coreDdb_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(coreDdb, coreDdb.getId(), "coreDdb_file");
	}
	
	/**
	 * 更新状态
	 * @param coreDdb 数据对象
	 */
	@Override
	@Transactional
	public void updateStatus(CoreDdb coreDdb) {
		super.updateStatus(coreDdb);
	}
	
	/**
	 * 删除数据
	 * @param coreDdb 数据对象
	 */
	@Override
	@Transactional
	public void delete(CoreDdb coreDdb) {
		super.delete(coreDdb);
	}

	/**
	 * 获取柱状图数据
	 */
	public Object getChartData() {
		// 从数据库按日期分组查询订单金额
		List<Map<String, Object>> chartData = dao.findChartData();
		
		// 转换为前端需要的数据格式
		List<String> xAxis = new ArrayList<>();
		List<BigDecimal> series = new ArrayList<>();
		
		for (Map<String, Object> data : chartData) {
			xAxis.add((String) data.get("date"));
			series.add((BigDecimal) data.get("amount"));
		}
		
		// 返回结果
		Map<String, Object> result = new HashMap<>();
		result.put("xAxis", xAxis);
		result.put("series", series);
		
		return result;
	}
	
}