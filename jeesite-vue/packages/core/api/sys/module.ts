/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '@jeesite/core/utils/http/axios';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { BasicModel, Page } from '@jeesite/core/api/model/baseModel';

const { adminPath } = useGlobSetting();

export interface Module extends BasicModel<Module> {
  moduleCode?: string; // 模块编码
  moduleName?: string; // 模块名称
  description?: string; // 模块描述
  mainClassName?: string; // 主类全名
  packageName?: string; // 基础包名
  moduleSort?: number; // 模块排序
  currentVersion?: string; // 当前版本
  upgradeInfo?: string; // 升级信息
  genBaseDir?: string; // 生成路径
  tplCategory?: string; // 生成模板分类
}

export const moduleList = (params?: Module | any) =>
  defHttp.get<Module>({ url: adminPath + '/sys/module/list', params });

export const moduleListData = (params?: Module | any) =>
  defHttp.post<Page<Module>>({ url: adminPath + '/sys/module/listData', params });

export const moduleSelectData = (params?: Module | any) =>
  defHttp.post<Recordable[]>({ url: adminPath + '/sys/module/selectData', params });

export const moduleForm = (params?: Module | any) =>
  defHttp.get<Module>({ url: adminPath + '/sys/module/form', params });

export const moduleSave = (params?: any, data?: Module | any) =>
  defHttp.postJson<Module>({ url: adminPath + '/sys/module/save', params, data });

export const moduleDisable = (params?: Module | any) =>
  defHttp.get<Module>({ url: adminPath + '/sys/module/disable', params });

export const moduleEnable = (params?: Module | any) =>
  defHttp.get<Module>({ url: adminPath + '/sys/module/enable', params });

export const moduleDelete = (params?: Module | any) =>
  defHttp.get<Module>({ url: adminPath + '/sys/module/delete', params });
