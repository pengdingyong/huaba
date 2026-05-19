<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable">
      <template #tableTitle>
        <Icon :icon="getTitle.icon" class="m-1 pr-1" />
        <span> {{ getTitle.value }} </span>
      </template>
      <template #toolbar>
        <a-button type="primary" @click="handleForm({})" v-auth="'cms:site:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record }">
        <a @click="handleForm({ siteCode: record.siteCode })" :title="record.siteName">
          {{ record.siteName }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsCmsSiteList">
  import { onMounted, ref, unref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { useGlobSetting } from '@jeesite/core/hooks/setting';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '@jeesite/core/components/Table';
  import { Site, siteList, siteRebuildIndex, siteRebuildVectorStore } from '@jeesite/cms/api/cms/site';
  import { siteDelete, siteListData } from '@jeesite/cms/api/cms/site';
  import { siteDisable, siteEnable } from '@jeesite/cms/api/cms/site';
  import { useDrawer } from '@jeesite/core/components/Drawer';
  import { FormProps } from '@jeesite/core/components/Form';
  import InputForm from './form.vue';

  const { t } = useI18n('cms.site');
  const { showMessage } = useMessage();
  const { ctxPath } = useGlobSetting();
  const { meta } = unref(router.currentRoute);
  const record = ref<Site>({} as Site);

  const getTitle = {
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: meta.title || t('站点管理'),
  };

  const searchForm: FormProps<Site> = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('站点名称'),
        field: 'siteName',
        component: 'Input',
      },
      {
        label: t('站点标题'),
        field: 'title',
        component: 'Input',
      },
      {
        label: t('状态'),
        field: 'status',
        component: 'Select',
        componentProps: {
          dictType: 'sys_search_status',
          allowClear: true,
          onChange: handleSuccess,
        },
      },
      {
        label: t('站点域名'),
        field: 'domainName',
        component: 'Input',
      },
      {
        label: t('备注信息'),
        field: 'remarks',
        component: 'Input',
      },
    ],
  };

  const tableColumns: BasicColumn<Site>[] = [
    {
      title: t('站点名称'),
      dataIndex: 'siteName',
      key: 'a.site_name',
      sorter: true,
      width: 130,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('站点标题'),
      dataIndex: 'title',
      key: 'a.title',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('站点域名'),
      dataIndex: 'domainName',
      key: 'a.domain_name',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('站点排序'),
      dataIndex: 'siteSort',
      key: 'a.site_sort',
      sorter: true,
      width: 80,
      align: 'center',
    },
    {
      title: t('描述'),
      dataIndex: 'description',
      key: 'a.description',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('主题风格'),
      dataIndex: 'theme',
      key: 'a.theme',
      sorter: true,
      width: 130,
      align: 'center',
      dictType: 'cms_theme',
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      key: 'a.status',
      sorter: true,
      width: 130,
      align: 'center',
      dictType: 'sys_search_status',
    },
    {
      title: t('创建时间'),
      dataIndex: 'createDate',
      key: 'a.create_date',
      sorter: true,
      width: 130,
      align: 'center',
    },
  ];

  const actionColumn: BasicColumn<Site> = {
    width: 205,
    actions: (record: Site) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑站点'),
        onClick: handleForm.bind(this, { siteCode: record.siteCode }),
        auth: 'cms:site:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用站点'),
        popConfirm: {
          title: t('是否确认停用站点'),
          confirm: handleDisable.bind(this, record),
        },
        auth: 'cms:site:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用站点'),
        popConfirm: {
          title: t('是否确认启用站点'),
          confirm: handleEnable.bind(this, record),
        },
        auth: 'cms:site:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除站点'),
        popConfirm: {
          title: t('是否确认删除站点'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'cms:site:edit',
      },
      {
        icon: 'i-ant-design:aim-outlined',
        title: t('重建该站点索引'),
        popConfirm: {
          title: t('确认重建该站点文章索引吗'),
          confirm: handleRebuildIndex.bind(this, record),
        },
        auth: 'cms:site:rebuildIndex',
      },
      {
        icon: 'i-bx:data',
        title: t('重建该站点向量数据库'),
        popConfirm: {
          title: t('确认重建该站点文章向量数据库吗'),
          confirm: handleRebuildVectorStore.bind(this, record),
        },
        auth: 'cms:site:rebuildVectorStore',
      },
      {
        icon: 'i-mdi:globe',
        title: t('访问站点'),
        onClick: () => window.open(ctxPath + '/f/index-' + record.siteCode, '_blank'),
        auth: 'cms:site:edit',
        ifShow: () => record.status === '0',
      },
    ],
  };

  const [registerTable, { reload, getForm }] = useTable<Site>({
    api: siteListData,
    beforeFetch: (params) => {
      return params;
    },
    columns: tableColumns,
    actionColumn: actionColumn,
    formConfig: searchForm,
    showTableSetting: true,
    useSearchForm: true,
    canResize: true,
  });

  onMounted(async () => {
    const res = await siteList();
    record.value = (res.site || {}) as Site;
    await getForm().setFieldsValue(record.value);
  });

  const [registerDrawer, { openDrawer }] = useDrawer();

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  async function handleDisable(record: Recordable) {
    const params = { siteCode: record.siteCode };
    const res = await siteDisable(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const params = { siteCode: record.siteCode };
    const res = await siteEnable(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const params = { siteCode: record.siteCode };
    const res = await siteDelete(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleRebuildIndex(record: Recordable) {
    const params = { siteCode: record.siteCode };
    const res = await siteRebuildIndex(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleRebuildVectorStore(record: Recordable) {
    const params = { siteCode: record.siteCode };
    const res = await siteRebuildVectorStore(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleSuccess(record: Recordable) {
    await reload({ record });
  }
</script>
