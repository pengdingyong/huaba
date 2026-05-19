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
        <a-button type="primary" @click="handleForm({})" v-auth="'test:data:edit'">
          <Icon icon="i-fluent:add-12-filled" /> {{ t('新增') }}
        </a-button>
      </template>
      <template #firstColumn="{ record, text, value }">
        <a @click="handleForm({ id: record.id })" :title="value">
          {{ text }}
        </a>
      </template>
    </BasicTable>
    <InputForm @register="registerDrawer" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup name="ViewsTestDataList">
  import { onMounted, ref, unref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '@jeesite/core/components/Table';
  import { TestData, testDataList } from '@jeesite/test/api/test/data';
  import { testDataDelete, testDataListData } from '@jeesite/test/api/test/data';
  import { testDataDisable, testDataEnable } from '@jeesite/test/api/test/data';
  import { officeTreeData } from '@jeesite/core/api/sys/office';
  import { areaTreeData } from '@jeesite/core/api/sys/area';
  import { useDrawer } from '@jeesite/core/components/Drawer';
  import { FormProps } from '@jeesite/core/components/Form';
  import InputForm from './form.vue';

  const { t } = useI18n('test.data');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<TestData>({} as TestData);

  const getTitle = {
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: meta.title || t('数据管理'),
  };

  const searchForm: FormProps<TestData> = {
    baseColProps: { md: 8, lg: 6 },
    labelWidth: 90,
    schemas: [
      {
        label: t('单行文本'),
        field: 'testInput',
        component: 'Input',
      },
      {
        label: t('多行文本'),
        field: 'testTextarea',
        component: 'Input',
      },
      {
        label: t('下拉框'),
        field: 'testSelect',
        component: 'Select',
        componentProps: {
          dictType: 'sys_menu_type',
          allowClear: true,
        },
      },
      {
        label: t('下拉多选'),
        field: 'testSelectMultiple',
        component: 'Select',
        componentProps: {
          dictType: 'sys_menu_type',
          allowClear: true,
          mode: 'multiple',
        },
      },
      {
        label: t('单选框'),
        field: 'testRadio',
        component: 'RadioGroup',
        componentProps: {
          dictType: 'sys_menu_type',
        },
      },
      {
        label: t('复选框'),
        field: 'testCheckbox',
        component: 'CheckboxGroup',
        componentProps: {
          dictType: 'sys_menu_type',
        },
      },
      {
        label: t('日期选择起'),
        field: 'testDate_gte',
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          showTime: false,
        },
      },
      {
        label: t('日期选择止'),
        field: 'testDate_lte',
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD',
          showTime: false,
        },
      },
      {
        label: t('日期时间起'),
        field: 'testDatetime_gte',
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD HH:mm',
          showTime: { format: 'HH:mm' },
        },
      },
      {
        label: t('日期时间止'),
        field: 'testDatetime_lte',
        component: 'DatePicker',
        componentProps: {
          format: 'YYYY-MM-DD HH:mm',
          showTime: { format: 'HH:mm' },
        },
      },
      {
        label: t('用户选择'),
        field: 'testUser.userCode',
        component: 'TreeSelect',
        componentProps: {
          api: officeTreeData,
          params: { isLoadUser: true, userIdPrefix: '' },
          canSelectParent: false,
          allowClear: true,
        },
      },
      {
        label: t('机构选择'),
        field: 'testOffice.officeCode',
        component: 'TreeSelect',
        componentProps: {
          api: officeTreeData,
          allowClear: true,
        },
      },
      {
        label: t('区域选择'),
        field: 'testAreaCode',
        component: 'TreeSelect',
        componentProps: {
          api: areaTreeData,
          allowClear: true,
        },
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
        label: t('备注信息'),
        field: 'remarks',
        component: 'Input',
      },
    ],
  };

  const tableColumns: BasicColumn<TestData>[] = [
    {
      title: t('单行文本'),
      dataIndex: 'testInput',
      key: 'a.test_input',
      sorter: true,
      width: 230,
      align: 'left',
      slot: 'firstColumn',
    },
    {
      title: t('多行文本'),
      dataIndex: 'testTextarea',
      key: 'a.test_textarea',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('下拉框'),
      dataIndex: 'testSelect',
      key: 'a.test_select',
      sorter: true,
      width: 130,
      align: 'center',
      dictType: 'sys_menu_type',
    },
    {
      title: t('下拉多选'),
      dataIndex: 'testSelectMultiple',
      key: 'a.test_select_multiple',
      sorter: true,
      width: 130,
      align: 'center',
      dictType: 'sys_menu_type',
    },
    {
      title: t('单选框'),
      dataIndex: 'testRadio',
      key: 'a.test_radio',
      sorter: true,
      width: 130,
      align: 'center',
      dictType: 'sys_menu_type',
    },
    {
      title: t('复选框'),
      dataIndex: 'testCheckbox',
      key: 'a.test_checkbox',
      sorter: true,
      width: 130,
      align: 'center',
      dictType: 'sys_menu_type',
    },
    {
      title: t('日期选择'),
      dataIndex: 'testDate',
      key: 'a.test_date',
      sorter: true,
      width: 130,
      align: 'center',
    },
    {
      title: t('日期时间'),
      dataIndex: 'testDatetime',
      key: 'a.test_datetime',
      sorter: true,
      width: 130,
      align: 'center',
    },
    {
      title: t('用户选择'),
      dataIndex: 'testUser.userName',
      key: 'a.test_user_code',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('机构选择'),
      dataIndex: 'testOffice.officeName',
      key: 'a.test_office_code',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('区域选择'),
      dataIndex: 'testAreaName',
      key: 'a.test_area_code',
      sorter: true,
      width: 130,
      align: 'left',
    },
    {
      title: t('区域名称'),
      dataIndex: 'testAreaName',
      key: 'a.test_area_name',
      sorter: true,
      width: 130,
      align: 'left',
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
      title: t('更新时间'),
      dataIndex: 'updateDate',
      key: 'a.update_date',
      sorter: true,
      width: 130,
      align: 'center',
    },
    {
      title: t('备注信息'),
      dataIndex: 'remarks',
      key: 'a.remarks',
      sorter: true,
      width: 130,
      align: 'left',
    },
  ];

  const actionColumn: BasicColumn<TestData> = {
    width: 160,
    actions: (record: TestData) => [
      {
        icon: 'i-clarity:note-edit-line',
        title: t('编辑数据'),
        onClick: handleForm.bind(this, { id: record.id }),
        auth: 'test:data:edit',
      },
      {
        icon: 'i-ant-design:stop-outlined',
        color: 'error',
        title: t('停用数据'),
        popConfirm: {
          title: t('是否确认停用数据'),
          confirm: handleDisable.bind(this, record),
        },
        auth: 'test:data:edit',
        ifShow: () => record.status === '0',
      },
      {
        icon: 'i-ant-design:check-circle-outlined',
        color: 'success',
        title: t('启用数据'),
        popConfirm: {
          title: t('是否确认启用数据'),
          confirm: handleEnable.bind(this, record),
        },
        auth: 'test:data:edit',
        ifShow: () => record.status === '2',
      },
      {
        icon: 'i-ant-design:delete-outlined',
        color: 'error',
        title: t('删除数据'),
        popConfirm: {
          title: t('是否确认删除数据'),
          confirm: handleDelete.bind(this, record),
        },
        auth: 'test:data:edit',
      },
    ],
  };

  const [registerTable, { reload, getForm }] = useTable<TestData>({
    api: testDataListData,
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
    const res = await testDataList();
    record.value = (res.testData || {}) as TestData;
    await getForm().setFieldsValue(record.value);
  });

  const [registerDrawer, { openDrawer }] = useDrawer();

  function handleForm(record: Recordable) {
    openDrawer(true, record);
  }

  async function handleDisable(record: Recordable) {
    const params = { id: record.id };
    const res = await testDataDisable(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleEnable(record: Recordable) {
    const params = { id: record.id };
    const res = await testDataEnable(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleDelete(record: Recordable) {
    const params = { id: record.id };
    const res = await testDataDelete(params);
    showMessage(res.message);
    await handleSuccess(record);
  }

  async function handleSuccess(record: Recordable) {
    await reload({ record });
  }
</script>
