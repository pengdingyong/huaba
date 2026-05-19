package com.jeesite.modules.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Size;
import java.util.List;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

import java.io.Serial;

/**
 * 商品表 Entity
 * @author pdyong
 * @version 2026-04-03
 */
@Table(name = "core_spb", alias = "a", label = "商品表", columns = {
		@Column(name = "id", attrName = "id", label = "主键", isPK = true),
		@Column(name = "spb_spbh", attrName = "spbSpbh", label = "商品编号"),
		@Column(name = "spb_spmc", attrName = "spbSpmc", label = "商品名称", queryType = QueryType.LIKE),
		@Column(name = "spb_jg", attrName = "spbJg", label = "商品价格"),
	}, orderBy = "a.id DESC"
)
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class CoreSpb extends DataEntity<CoreSpb> {
	
	@Serial
	private static final long serialVersionUID = 1L;
	private String id;
	private String spbSpbh;		// 商品编号
	private String spbSpmc;		// 商品名称
	private String spbJg;		// 商品价格
	private String spbSpfl;		// 商品分类
	private String delFlag;		//1已删除0未删除
	@JsonIgnore
	private List<CoreDdb> orderList;		// 订单列表（懒加载）

	public CoreSpb() {
		this(null);
	}
	
	public CoreSpb(String id){
		super(id);
	}
	
	@Size(min=0, max=100, message="商品编号长度不能超过 100 个字符")
	public String getSpbSpbh() {
		return spbSpbh;
	}

	public void setSpbSpbh(String spbSpbh) {
		this.spbSpbh = spbSpbh;
	}
	
	@Size(min=0, max=100, message="商品名称长度不能超过 100 个字符")
	public String getSpbSpmc() {
		return spbSpmc;
	}

	public void setSpbSpmc(String spbSpmc) {
		this.spbSpmc = spbSpmc;
	}
	
	@Size(min=0, max=100, message="商品价格长度不能超过 100 个字符")
	public String getSpbJg() {
		return spbJg;
	}

	public void setSpbJg(String spbJg) {
		this.spbJg = spbJg;
	}

	public String getSpbSpfl() {
		return spbSpfl;
	}

	public void setSpbSpfl(String spbSpfl) {
		this.spbSpfl = spbSpfl;
	}

	public List<CoreDdb> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<CoreDdb> orderList) {
		this.orderList = orderList;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
}