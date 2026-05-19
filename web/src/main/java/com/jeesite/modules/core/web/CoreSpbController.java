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
import com.jeesite.modules.core.entity.CoreSpb;
import com.jeesite.modules.core.service.CoreSpbService;

/**
 * 商品表 Controller
 * @author pdyong
 * @version 2026-04-03
 */
@Controller
@RequestMapping(value = "${adminPath}/core/spb")
public class CoreSpbController extends BaseController {

	private final CoreSpbService coreSpbService;

	public CoreSpbController(CoreSpbService coreSpbService) {
		this.coreSpbService = coreSpbService;
	}
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CoreSpb get(String id, boolean isNewRecord) {
		return coreSpbService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("core:spb:view")
	@RequestMapping(value = {"list", ""})
	public String list(CoreSpb coreSpb, Model model) {
		model.addAttribute("coreSpb", coreSpb);
		return "modules/core/coreSpbList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("core:spb:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CoreSpb> listData(CoreSpb coreSpb, HttpServletRequest request, HttpServletResponse response) {
		coreSpb.setPage(new Page<>(request, response));
		Page<CoreSpb> page = coreSpbService.findPage(coreSpb);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("core:spb:view")
	@RequestMapping(value = "form")
	public String form(CoreSpb coreSpb, Model model) {
		model.addAttribute("coreSpb", coreSpb);
		return "modules/core/coreSpbForm";
	}

	/**
	 * 保存数据
	 */
	@RequiresPermissions("core:spb:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CoreSpb coreSpb) {
		coreSpbService.save(coreSpb);
		return renderResult(Global.TRUE, text("保存商品表成功！"));
	}
	
	/**
	 * 停用数据
	 */
	@RequiresPermissions("core:spb:edit")
	@RequestMapping(value = "disable")
	@ResponseBody
	public String disable(CoreSpb coreSpb) {
		coreSpb.setStatus(CoreSpb.STATUS_DISABLE);
		coreSpbService.updateStatus(coreSpb);
		return renderResult(Global.TRUE, text("停用商品表成功"));
	}
	
	/**
	 * 启用数据
	 */
	@RequiresPermissions("core:spb:edit")
	@RequestMapping(value = "enable")
	@ResponseBody
	public String enable(CoreSpb coreSpb) {
		coreSpb.setStatus(CoreSpb.STATUS_NORMAL);
		coreSpbService.updateStatus(coreSpb);
		return renderResult(Global.TRUE, text("启用商品表成功"));
	}
	
	/**
	 * 删除数据
	 */
	@RequiresPermissions("core:spb:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CoreSpb coreSpb) {
		coreSpbService.delete(coreSpb);
		return renderResult(Global.TRUE, text("删除商品表成功！"));
	}
	
}