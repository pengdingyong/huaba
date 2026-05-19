package com.jeesite.modules.core.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

import java.io.Serial;

/**
 * 订单表 Entity
 * @author pdyong
 * @version 2026-04-03
 */
@Table(name = "core_ddb", alias = "a", label = "订单表", columns = {
		@Column(name = "id", attrName = "id", label = "主键", isPK = true),
		@Column(name = "order_num", attrName = "orderNum", label = "订单编号"),
		@Column(name = "spb_id", attrName = "spbId", label = "商品ID"),
		@Column(name = "ddb_xdsj", attrName = "ddbXdsj", label = "下单时间", isUpdateForce = true),
		@Column(name = "ddb_ddje", attrName = "ddbDdje", label = "订单金额", isUpdateForce = true),
		@Column(name = "ddb_ddlx", attrName = "ddbDdlx", label = "订单类型"),
	}, orderBy = "a.id DESC"
)
public class CoreDdb extends DataEntity<CoreDdb> {
	
	@Serial
	private static final long serialVersionUID = 1L;
	private String orderNum;		// 订单编号
	private String spbId;		// 商品ID
	private Date ddbXdsj;		// 下单时间
	private Double ddbDdje;		// 订单金额
	private String ddbDdlx;		// 订单类型

	public CoreDdb() {
		this(null);
	}
	
	public CoreDdb(String id){
		super(id);
	}
	
	@NotBlank(message="订单编号不能为空")
	@Size(min=0, max=100, message="订单编号长度不能超过 100 个字符")
	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	@Size(min=0, max=64, message="商品ID长度不能超过 64 个字符")
	public String getSpbId() {
		return spbId;
	}

	public void setSpbId(String spbId) {
		this.spbId = spbId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	public Date getDdbXdsj() {
		return ddbXdsj;
	}

	public void setDdbXdsj(Date ddbXdsj) {
		this.ddbXdsj = ddbXdsj;
	}
	
	public Double getDdbDdje() {
		return ddbDdje;
	}

	public void setDdbDdje(Double ddbDdje) {
		this.ddbDdje = ddbDdje;
	}
	
	@Size(min=0, max=10, message="订单类型长度不能超过 10 个字符")
	public String getDdbDdlx() {
		return ddbDdlx;
	}

	public void setDdbDdlx(String ddbDdlx) {
		this.ddbDdlx = ddbDdlx;
	}
	
}