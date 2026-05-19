package com.jeesite.modules.core.service;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.core.entity.CoreSpb;
import com.jeesite.modules.core.dao.CoreSpbDao;
import com.jeesite.modules.file.utils.FileUploadUtils;

/**
 * 商品表 Service
 * @author pdyong
 * @version 2026-04-03
 */
@Service
public class CoreSpbService extends CrudService<CoreSpbDao, CoreSpb> {

	/**
	 * 获取单条数据
	 * @param coreSpb 主键
	 */
	@Override
	public CoreSpb get(CoreSpb coreSpb) {
		return super.get(coreSpb);
	}

	/**
	 * 查询列表数据
	 * @param coreSpb 查询条件
	 */
	@Override
	public List<CoreSpb> findList(CoreSpb coreSpb) {
		return super.findList(coreSpb);
	}
	
	/**
	 * 查询分页数据
	 * @param coreSpb 查询条件
	 * @param coreSpb page 分页对象
	 */
	@Override
	public Page<CoreSpb> findPage(CoreSpb coreSpb) {
		return super.findPage(coreSpb);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param coreSpb 数据对象
	 */
	@Override
	@Transactional
	public void save(CoreSpb coreSpb) {
		super.save(coreSpb);
		// 保存上传图片
		FileUploadUtils.saveFileUpload(coreSpb, coreSpb.getId(), "coreSpb_image");
		// 保存上传附件
		FileUploadUtils.saveFileUpload(coreSpb, coreSpb.getId(), "coreSpb_file");
	}
	
	/**
	 * 更新状态
	 * @param coreSpb 数据对象
	 */
	@Override
	@Transactional
	public void updateStatus(CoreSpb coreSpb) {
		super.updateStatus(coreSpb);
	}
	
	/**
	 * 删除数据
	 * @param coreSpb 数据对象
	 */
	@Override
	@Transactional
	public void delete(CoreSpb coreSpb) {
		super.delete(coreSpb);
	}
	
	/**
	 * 查询商品销售额前5
	 */
	public List<Map<String, Object>> findTop5Sales() {
		return dao.findTop5Sales();
	}
	
}