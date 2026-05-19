<!--
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
-->
<template>
  <BasicDrawer
    v-bind="$attrs"
    :showFooter="true"
    :okAuth="'test:data:edit'"
    @register="registerDrawer"
    @ok="handleSubmit"
    width="70%"
  >
    <template #title>
      <Icon :icon="getTitle.icon" class="m-1 pr-1" />
      <span> {{ getTitle.value }} </span>
    </template>
    <BasicForm @register="registerForm">
      <template #dataChildList>
        <FormDataChildList ref="formDataChildListRef" />
      </template>
    </BasicForm>
  </BasicDrawer>
</template>
<script lang="ts" setup name="ViewsTestDataForm">
  import { ref, unref, computed } from 'vue';
  import { useI18n } from '@jeesite/core/hooks/web/useI18n';
  import { useMessage } from '@jeesite/core/hooks/web/useMessage';
  import { router } from '@jeesite/core/router';
  import { Icon } from '@jeesite/core/components/Icon';
  import { BasicForm, FormSchema, useForm } from '@jeesite/core/components/Form';
  import { BasicDrawer, useDrawerInner } from '@jeesite/core/components/Drawer';
  import { TestData, testDataSave, testDataForm } from '@jeesite/test/api/test/data';
  import { officeTreeData } from '@jeesite/core/api/sys/office';
  import { areaTreeData } from '@jeesite/core/api/sys/area';
  import FormDataChildList from './formDataChildList.vue';

  const emit = defineEmits(['success', 'register']);

  const { t } = useI18n('test.data');
  const { showMessage } = useMessage();
  const { meta } = unref(router.currentRoute);
  const record = ref<TestData>({} as TestData);
  const formDataChildListRef = ref<InstanceType<typeof FormDataChildList>>();

  const getTitle = computed(() => ({
    icon: meta.icon || 'i-ant-design:book-outlined',
    value: record.value.isNewRecord ? t('新增数据') : t('编辑数据'),
  }));

  const inputFormSchemas: FormSchema<TestData>[] = [
    {
      label: t('基本信息'),
      field: 'basicInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('单行文本'),
      field: 'testInput',
      component: 'Input',
      componentProps: {
        maxlength: 200,
      },
    },
    {
      label: t('多行文本'),
      field: 'testTextarea',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 200,
      },
      colProps: { md: 24, lg: 24 },
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
      label: t('日期选择'),
      field: 'testDate',
      component: 'DatePicker',
      componentProps: {
        format: 'YYYY-MM-DD',
        showTime: false,
      },
    },
    {
      label: t('日期时间'),
      field: 'testDatetime',
      component: 'DatePicker',
      componentProps: {
        format: 'YYYY-MM-DD HH:mm',
        showTime: { format: 'HH:mm' },
      },
    },
    {
      label: t('用户选择'),
      field: 'testUser.userCode',
      fieldLabel: 'testUser.userName',
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
      fieldLabel: 'testOffice.officeName',
      component: 'TreeSelect',
      componentProps: {
        api: officeTreeData,
        canSelectParent: false,
        allowClear: true,
      },
    },
    {
      label: t('区域选择'),
      field: 'testAreaCode',
      fieldLabel: 'testAreaName',
      component: 'TreeSelect',
      componentProps: {
        api: areaTreeData,
        canSelectParent: false,
        allowClear: true,
      },
    },
    {
      label: t('其它信息'),
      field: 'otherInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('备注信息'),
      field: 'remarks',
      component: 'InputTextArea',
      componentProps: {
        maxlength: 500,
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('图片上传'),
      field: 'dataMap',
      component: 'Upload',
      componentProps: {
        loadTime: computed(() => record.value.__t),
        bizKey: computed(() => record.value.id),
        bizType: 'testData_image',
        uploadType: 'image',
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('附件上传'),
      field: 'dataMap',
      component: 'Upload',
      componentProps: {
        loadTime: computed(() => record.value.__t),
        bizKey: computed(() => record.value.id),
        bizType: 'testData_file',
        uploadType: 'all',
      },
      colProps: { md: 24, lg: 24 },
    },
    {
      label: t('测试数据子表'),
      field: 'dataChildInfo',
      component: 'FormGroup',
      colProps: { md: 24, lg: 24 },
    },
    {
      field: 'dataChildList',
      component: 'Input',
      colProps: { md: 24, lg: 24 },
      slot: 'dataChildList',
    },
  ];

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm<TestData>({
    labelWidth: 120,
    schemas: inputFormSchemas,
    baseColProps: { md: 24, lg: 12 },
  });

  const [registerDrawer, { setDrawerProps, closeDrawer }] = useDrawerInner(async (data) => {
    setDrawerProps({ loading: true });
    await resetFields();
    const res = await testDataForm(data);
    record.value = (res.testData || {}) as TestData;
    record.value.__t = new Date().getTime();
    await setFieldsValue(record.value);
    formDataChildListRef.value?.setTableData(record.value);
    setDrawerProps({ loading: false });
  });

  async function handleSubmit() {
    try {
      const data = await validate();
      setDrawerProps({ confirmLoading: true });
      const params: any = {
        isNewRecord: record.value.isNewRecord,
        id: record.value.id || data.id,
      };
      await formDataChildListRef.value?.getTableData(data);
      // console.log('submit', params, data, record);
      const res = await testDataSave(params, data);
      showMessage(res.message);
      setTimeout(closeDrawer);
      emit('success', data);
    } catch (error: any) {
      if (error && error.errorFields) {
        showMessage(error.message || t('common.validateError'));
      }
      console.log('error', error);
    } finally {
      setDrawerProps({ confirmLoading: false });
    }
  }
</script>
