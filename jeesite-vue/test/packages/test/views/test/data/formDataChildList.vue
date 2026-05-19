<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <div>
    <BasicTable @register="registerTable" @row-click="handleRowClick" />
    <a-button class="mt-2" @click="handleRowAdd" v-auth="'test:data:edit'">
      <Icon icon="i-ant-design:plus-circle-outlined" /> {{ t('新增') }}
    </a-button>
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicTable, BasicColumn, useTable } from '@jeesite/core/components/Table';
  import { TestData } from '@jeesite/test/api/test/testData';
  import { TestDataChild } from '@jeesite/test/api/test/testDataChild';
  import { officeTreeData } from '@jeesite/core/api/sys/office';
  import { areaTreeData } from '@jeesite/core/api/sys/area';

  const { t } = useI18n('test.data');
  const record = ref<TestData>({} as TestData);

  const tableColumns: BasicColumn<TestDataChild>[] = [
    {
      title: t('排序号'),
      dataIndex: 'testSort',
      width: 130,
      align: 'center',
      editRow: true,
      editComponent: 'Input',
      editRule: false,
    },
    {
      title: t('单行文本'),
      dataIndex: 'testInput',
      width: 130,
      align: 'left',
      editRow: true,
      editComponent: 'Input',
      editComponentProps: {
        maxlength: 200,
      },
      editRule: false,
    },
    {
      title: t('多行文本'),
      dataIndex: 'testTextarea',
      width: 130,
      align: 'left',
      editRow: true,
      editComponent: 'InputTextArea',
      editComponentProps: {
        maxlength: 200,
      },
      editRule: false,
    },
    {
      title: t('下拉框'),
      dataIndex: 'testSelect',
      width: 130,
      align: 'center',
      dictType: 'sys_menu_type',
      editRow: true,
      editComponent: 'Select',
      editComponentProps: {
        dictType: 'sys_menu_type',
        allowClear: true,
      },
      editRule: false,
    },
    {
      title: t('下拉多选'),
      dataIndex: 'testSelectMultiple',
      width: 130,
      align: 'center',
      dictType: 'sys_menu_type',
      editRow: true,
      editComponent: 'Select',
      editComponentProps: {
        dictType: 'sys_menu_type',
        allowClear: true,
        mode: 'multiple',
      },
      editRule: false,
    },
    {
      title: t('单选框'),
      dataIndex: 'testRadio',
      width: 130,
      align: 'center',
      dictType: 'sys_menu_type',
      editRow: true,
      editComponent: 'Select',
      editComponentProps: {
        dictType: 'sys_menu_type',
        allowClear: true,
      },
      editRule: false,
    },
    {
      title: t('复选框'),
      dataIndex: 'testCheckbox',
      width: 130,
      align: 'center',
      dictType: 'sys_menu_type',
      editRow: true,
      editComponent: 'Select',
      editComponentProps: {
        dictType: 'sys_menu_type',
        allowClear: true,
      },
      editRule: false,
    },
    {
      title: t('日期选择'),
      dataIndex: 'testDate',
      width: 130,
      align: 'center',
      editRow: true,
      editComponent: 'DatePicker',
      editComponentProps: {
        format: 'YYYY-MM-DD',
        showTime: false,
      },
      editRule: false,
    },
    {
      title: t('日期时间'),
      dataIndex: 'testDatetime',
      width: 215,
      align: 'center',
      editRow: true,
      editComponent: 'DatePicker',
      editComponentProps: {
        format: 'YYYY-MM-DD HH:mm',
        showTime: { format: 'HH:mm' },
      },
      editRule: false,
    },
    {
      title: t('用户选择'),
      dataIndex: 'testUser.userCode',
      width: 130,
      align: 'left',
      editRow: true,
      dataLabel: 'testUser.userName',
      editComponent: 'TreeSelect',
      editComponentProps: {
        api: officeTreeData,
        params: { isLoadUser: true, userIdPrefix: '' },
        canSelectParent: false,
        allowClear: true,
      },
      editRule: false,
    },
    {
      title: t('机构选择'),
      dataIndex: 'testOffice.officeCode',
      width: 130,
      align: 'left',
      editRow: true,
      dataLabel: 'testOffice.officeName',
      editComponent: 'TreeSelect',
      editComponentProps: {
        api: officeTreeData,
        canSelectParent: false,
        allowClear: true,
      },
      editRule: false,
    },
    {
      title: t('区域选择'),
      dataIndex: 'testAreaCode',
      width: 130,
      align: 'left',
      editRow: true,
      dataLabel: 'testAreaName',
      editComponent: 'TreeSelect',
      editComponentProps: {
        api: areaTreeData,
        canSelectParent: false,
        allowClear: true,
      },
      editRule: false,
    },
  ];

  const [registerTable, tableAction] = useTable<TestDataChild>({
    columns: tableColumns,
    actionColumn: {
      width: 60,
      actions: (record: TestDataChild) => [
        {
          icon: 'i-ant-design:delete-outlined',
          color: 'error',
          popConfirm: {
            title: '是否确认删除',
            confirm: handleRowDelete.bind(this, record),
          },
          auth: 'test:data:edit',
        },
      ],
    },
    rowKey: 'id',
    pagination: false,
    bordered: true,
    size: 'small',
    inset: true,
  });

  function handleRowClick(data: Recordable) {
    data.onEdit?.(true, false);
  }

  function handleRowAdd() {
    tableAction.insertTableDataRecord({
      id: 'rid_' + new Date().getTime(),
      isNewRecord: true,
      editable: true,
    });
  }

  function handleRowDelete(data: Recordable) {
    tableAction.deleteTableDataRecord(data);
  }

  async function getTableData(data: Recordable): Promise<Recordable> {
    let valid = true;
    let tableList: Recordable[] = [];
    for (const record of tableAction.getDataSource()) {
      if (!(await record.onEdit?.(false, true))) {
        valid = false;
      }
      tableList.push({
        ...record,
        id: !!record.isNewRecord ? '' : record.id,
      });
    }
    for (const record of tableAction.getDelDataSource()) {
      if (!!record.isNewRecord) continue;
      tableList.push({
        ...record,
        status: '1',
      });
    }
    if (!valid) {
      throw {
        errorFields: [{ name: ['dataChildList'] }],
        message: t('测试数据子表填写有误，请根据提示修正'),
      };
    }
    data.dataChildList = tableList;
    return tableList;
  }

  async function setTableData(data: Recordable) {
    record.value = data as TestData;
    tableAction.setTableData(data.dataChildList || []);
  }

  defineExpose({
    getTableData,
    setTableData,
  });
</script>
