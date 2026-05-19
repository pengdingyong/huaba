/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { defHttp } from '@jeesite/core/utils/http/axios';
import { useGlobSetting } from '@jeesite/core/hooks/setting';
import { BasicModel, Page } from '@jeesite/core/api/model/baseModel';

const { adminPath } = useGlobSetting();

export interface Cache extends BasicModel<Cache> {
  cacheName?: string;
  key?: string;
  value?: any;
}

export const stateCacheNameList = (params?: Cache | any) =>
  defHttp.post<Page<Cache>>({ url: adminPath + '/state/cache/cacheNameList', params });

export const stateCacheKeyList = (params?: Cache | any) =>
  defHttp.post<Page<Cache>>({ url: adminPath + '/state/cache/cacheKeyList', params });

export const stateCacheValue = (params?: Cache | any) =>
  defHttp.post<Page<Cache>>({ url: adminPath + '/state/cache/cacheValue', params });

export const stateCacheClear = (params?: Cache | any) =>
  defHttp.get<Cache>({ url: adminPath + '/state/cache/clearCache', params });

export const stateCacheClearAll = (params?: Cache | any) =>
  defHttp.get<Cache>({ url: adminPath + '/sys/cache/clearAll', params });
