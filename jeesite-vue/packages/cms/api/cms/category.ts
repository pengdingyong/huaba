/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '@jeesite/core/utils/http/axios';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { TreeDataModel, TreeModel } from '@jeesite/core/api/model/baseModel';

const { adminPath } = useGlobSetting();

export interface Category extends TreeModel<Category> {
  categoryCode: string; // 栏目编码
  categoryName: string; // 栏目名称
  siteCode: string; // 站点编码
  moduleType?: string; // 内容模型
  image?: string; // 栏目图片
  href?: string; // 链接
  target?: string; // 目标
  keywords?: string; // 关键字
  description?: string; // 描述
  inMenu?: string; // 导航栏目
  inList?: string; // 栏目列表
  showModes?: string; // 展现模式
  isNeedAudit?: string; // 是否需要审核
  isCanComment?: string; // 是否允许评论
  customListView?: string; // 自定义列表视图
  customContentView?: string; // 自定义内容视图
  viewConfig?: string; // 视图配置
  extend?: any; // 扩展字段
}

export const categoryIndex = (params?: Category | any) =>
  defHttp.get<Category>({ url: adminPath + '/cms/category/index', params });

export const categoryList = (params?: Category | any) =>
  defHttp.get<Category>({ url: adminPath + '/cms/category/list', params });

export const categoryListData = (params?: Category | any) =>
  defHttp.post<Category[]>({ url: adminPath + '/cms/category/listData', params });

export const categoryForm = (params?: Category | any) =>
  defHttp.get<Category>({ url: adminPath + '/cms/category/form', params });

export const categoryCreateNextNode = (params?: Category | any) =>
  defHttp.get<Category>({ url: adminPath + '/cms/category/createNextNode', params });

export const categorySave = (params?: any, data?: Category | any) =>
  defHttp.postJson<Category>({ url: adminPath + '/cms/category/save', params, data });

export const categoryDisable = (params?: Category | any) =>
  defHttp.get<Category>({ url: adminPath + '/cms/category/disable', params });

export const categoryEnable = (params?: Category | any) =>
  defHttp.get<Category>({ url: adminPath + '/cms/category/enable', params });

export const categoryDelete = (params?: Category | any) =>
  defHttp.get<Category>({ url: adminPath + '/cms/category/delete', params });

export const categoryTreeData = (params?: any) =>
  defHttp.get<TreeDataModel[]>({ url: adminPath + '/cms/category/treeData', params });

export const categoryRebuildIndex = (params?: Category | any) =>
  defHttp.get<Category>({ url: adminPath + '/cms/category/rebuildIndex', params });

export const categoryRebuildVectorStore = (params?: Category | any) =>
  defHttp.get<Category>({ url: adminPath + '/cms/category/rebuildVectorStore', params });
