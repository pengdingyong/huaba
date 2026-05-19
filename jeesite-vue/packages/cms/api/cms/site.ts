/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '@jeesite/core/utils/http/axios';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { BasicModel, Page } from '@jeesite/core/api/model/baseModel';

const { adminPath } = useGlobSetting();

export interface Site extends BasicModel<Site> {
  siteName: string; // 站点名称
  siteCode: string; // 站点编码
  title: string; // 站点标题
  domainName?: string; // 站点域名
  siteSort?: number; // 站点排序
  logo?: string; // 站点Logo
  description?: string; // 描述
  keywords?: string; // 关键字
  theme?: string; // 主题风格
  customIndexView?: string; // 首页视图
  copyright?: string; // 版权信息
}

export const siteList = (params?: Site | any) => defHttp.get<Site>({ url: adminPath + '/cms/site/list', params });

export const siteListData = (params?: Site | any) =>
  defHttp.post<Page<Site>>({ url: adminPath + '/cms/site/listData', params });

export const siteForm = (params?: Site | any) => defHttp.get<Site>({ url: adminPath + '/cms/site/form', params });

export const siteSave = (params?: any, data?: Site | any) =>
  defHttp.postJson<Site>({ url: adminPath + '/cms/site/save', params, data });

export const siteDisable = (params?: Site | any) => defHttp.get<Site>({ url: adminPath + '/cms/site/disable', params });

export const siteEnable = (params?: Site | any) => defHttp.get<Site>({ url: adminPath + '/cms/site/enable', params });

export const siteDelete = (params?: Site | any) => defHttp.get<Site>({ url: adminPath + '/cms/site/delete', params });

export const siteSelect = (params?: Site | any) => defHttp.get<Site>({ url: adminPath + '/cms/site/select', params });

export const siteRebuildIndex = (params?: Site | any) =>
  defHttp.get<Site>({ url: adminPath + '/cms/site/rebuildIndex', params });

export const siteRebuildVectorStore = (params?: Site | any) =>
  defHttp.get<Site>({ url: adminPath + '/cms/site/rebuildVectorStore', params });
