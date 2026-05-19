/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '@jeesite/core/utils/http/axios';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { BasicModel, Page, TreeDataModel } from '@jeesite/core/api/model/baseModel';
import { Category } from '@jeesite/cms/api/cms/category';

const { adminPath } = useGlobSetting();

export interface Article extends BasicModel<Article> {
  title: string; // 内容标题
  category: Category; // 归属栏目
  source?: string; // 文章来源
  moduleType?: string; // 模块类型
  color?: string; // 标题颜色
  href?: string; // 外部链接
  weight?: number; // 权重/排序
  weightDate?: string; // 权重期限
  description?: string; // 摘要
  image?: string; // 内容图片
  keywords?: string; // 关键字
  copyfrom?: string; // 文章来源出处
  hits?: number; // 点击数
  hitsPlus?: number; // 支持数
  hitsMinus?: number; // 反对数
  wordCount?: number; // 字数
  customContentView?: string; // 自定义内容视图
  viewConfig?: string; // 视图配置
}

export const articleList = (params?: Article | any) =>
  defHttp.get<Article>({ url: adminPath + '/cms/article/list', params });

export const articleListData = (params?: Article | any) =>
  defHttp.post<Page<Article>>({ url: adminPath + '/cms/article/listData', params });

export const articleForm = (params?: Article | any) =>
  defHttp.get<Article>({ url: adminPath + '/cms/article/form', params });

export const articleSave = (params?: any, data?: Article | any) =>
  defHttp.postJson<Article>({ url: adminPath + '/cms/article/save', params, data });

export const articleDisable = (params?: Article | any) =>
  defHttp.get<Article>({ url: adminPath + '/cms/article/disable', params });

export const articleEnable = (params?: Article | any) =>
  defHttp.get<Article>({ url: adminPath + '/cms/article/enable', params });

export const articleDelete = (params?: Article | any) =>
  defHttp.get<Article>({ url: adminPath + '/cms/article/delete', params });
