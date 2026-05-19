/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import { BasicModel } from '@jeesite/core/api/model/baseModel';
import { TestData } from '@jeesite/test/api/test/data';

export interface TestDataChild extends BasicModel<TestDataChild> {
  testSort?: number; // 排序号
  testData?: TestData; // 父表主键 父类
  testInput?: string; // 单行文本
  testTextarea?: string; // 多行文本
  testSelect?: string; // 下拉框
  testSelectMultiple?: string; // 下拉多选
  testRadio?: string; // 单选框
  testCheckbox?: string; // 复选框
  testDate?: string; // 日期选择
  testDatetime?: string; // 日期时间
  testUser?: any; // 用户选择
  testOffice?: any; // 机构选择
  testAreaCode?: string; // 区域选择
  testAreaName?: string; // 区域名称
}
