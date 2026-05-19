package com.jeesite.modules.core.web;

import com.jeesite.common.lang.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.core.entity.CoreDdb;
import com.jeesite.modules.core.service.CoreDdbService;

/**
 * 订单表 Controller
 * @author pdyong
 * @version 2026-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/core/ddb")
public class CoreDdbController extends BaseController {

	private final CoreDdbService coreDdbService;

	public CoreDdbController(CoreDdbService coreDdbService) {
		this.coreDdbService = coreDdbService;
	}
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CoreDdb get(String id, boolean isNewRecord) {
		return coreDdbService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("core:ddb:view")
	@RequestMapping(value = {"list", ""})
	public String list(CoreDdb coreDdb, Model model) {
		model.addAttribute("coreDdb", coreDdb);
		return "modules/core/coreDdbList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("core:ddb:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CoreDdb> listData(CoreDdb coreDdb, HttpServletRequest request, HttpServletResponse response) {
		coreDdb.setPage(new Page<>(request, response));
		Page<CoreDdb> page = coreDdbService.findPage(coreDdb);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("core:ddb:view")
	@RequestMapping(value = "form")
	public String form(CoreDdb coreDdb, Model model) {
		model.addAttribute("coreDdb", coreDdb);
		return "modules/core/coreDdbForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("core:ddb:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CoreDdb coreDdb) {
		coreDdbService.save(coreDdb);
		return renderResult(Global.TRUE, text("保存订单表成功！"));
	}
	
	/**
	 * 停用数据
	 */
	@RequiresPermissions("core:ddb:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(CoreDdb coreDdb) {
		coreDdb.setStatus(CoreDdb.STATUS_DISABLE);
		coreDdbService.updateStatus(coreDdb);
		return renderResult(Global.TRUE, text("停用订单表成功"));
	}
	
	/**
	 * 启用数据
	 */
	@RequiresPermissions("core:ddb:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(CoreDdb coreDdb) {
		coreDdb.setStatus(CoreDdb.STATUS_NORMAL);
		coreDdbService.updateStatus(coreDdb);
		return renderResult(Global.TRUE, text("启用订单表成功"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("core:ddb:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CoreDdb coreDdb) {
		coreDdbService.delete(coreDdb);
		return renderResult(Global.TRUE, text("删除订单表成功！"));
	}

	/**
	 * 生成柱状图
	 */
	@RequiresPermissions("core:ddb:view")
	@RequestMapping(value = "chart")
	public String chart(Model model) {
		return "modules/core/coreDdbChart";
	}

	/**
	 * 获取柱状图数据
	 */
	@RequiresPermissions("core:ddb:view")
	@RequestMapping(value = "chartData")
	@ResponseBody
	public Object chartData() {
		return coreDdbService.getChartData();
	}
	
}