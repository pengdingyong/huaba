package com.jeesite.modules.core.controller;

import java.util.List;
import java.util.Map;

import com.jeesite.common.config.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping(value = "${adminPath}/core/coreSpb")
public class CoreSpbController2 extends BaseController {

	@Autowired
	private CoreSpbService coreSpbService;

	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = {"list", ""})
	public String list(CoreSpb coreSpb, Model model) {
		model.addAttribute("coreSpb", coreSpb);
		return "modules/core/coreSpbList";
	}

	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CoreSpb> listData(CoreSpb coreSpb) {
		Page<CoreSpb> page = coreSpbService.findPage(coreSpb);
		return page;
	}

	/**
	 * 查询商品销售额前5
	 */
	@RequestMapping(value = "top5Sales")
	@ResponseBody
	public List<Map<String, Object>> top5Sales() {
		return coreSpbService.findTop5Sales();
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(CoreSpb coreSpb, Model model) {
		if (coreSpb.getIsNewRecord()) {
			coreSpb.setSpbSpbh("SP" + System.currentTimeMillis());
		}
		model.addAttribute("coreSpb", coreSpb);
		return "modules/core/coreSpbForm";
	}

	/**
	 * 保存商品表
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public String save(CoreSpb coreSpb) {
		coreSpbService.save(coreSpb);
		return renderResult(Global.TRUE, text("保存商品表成功！"));
	}

	/**
	 * 删除商品表
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CoreSpb coreSpb) {
		coreSpbService.delete(coreSpb);
		return renderResult(Global.TRUE, text("删除商品表成功！"));
	}

}
