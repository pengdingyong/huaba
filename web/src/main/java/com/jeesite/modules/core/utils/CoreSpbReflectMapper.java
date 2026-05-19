package com.jeesite.modules.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import com.jeesite.modules.core.entity.CoreSpb;

/**
 * 商品表反射映射工具类
 * @author pdyong
 * @version 2026-04-15
 */
public class CoreSpbReflectMapper {

	/**
	 * 使用反射机制将 HashMap 中的商品名称和商品价格映射到 CoreSpb
	 * @param map HashMap，key 为商品名称，value 为商品价格
	 * @return CoreSpb 实体对象
	 */
	public static CoreSpb mapToCoreSpb(HashMap<String, Object> map) {
		CoreSpb coreSpb = new CoreSpb();
		
		// 遍历 HashMap
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String spbSpmc = entry.getKey(); // 商品名称
			Object spbJgObj = entry.getValue(); // 商品价格
			
			try {
				// 使用反射设置商品名称
				setFieldValue(coreSpb, "spbSpmc", spbSpmc);
				
				// 使用反射设置商品价格
				if (spbJgObj != null) {
					setFieldValue(coreSpb, "spbJg", spbJgObj.toString());
				}
				
				// 使用反射设置商品编号
				setFieldValue(coreSpb, "spbSpbh", "SP" + System.currentTimeMillis());
				
				// 使用反射设置默认值
				setFieldValue(coreSpb, "spbSpfl", "默认分类");
				setFieldValue(coreSpb, "delFlag", "0"); // 0 表示未删除
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return coreSpb;
	}
	
	/**
	 * 使用反射机制将多个商品的 HashMap 映射到 CoreSpb 列表
	 * @param mapList HashMap 列表，每个 HashMap 包含一个商品的信息
	 * @return CoreSpb 实体对象列表
	 */
	public static java.util.List<CoreSpb> mapListToCoreSpbList(java.util.List<HashMap<String, Object>> mapList) {
		java.util.List<CoreSpb> coreSpbList = new java.util.ArrayList<>();
		
		if (mapList != null && !mapList.isEmpty()) {
			for (HashMap<String, Object> map : mapList) {
				CoreSpb coreSpb = mapToCoreSpb(map);
				coreSpbList.add(coreSpb);
			}
		}
		
		return coreSpbList;
	}
	
	/**
	 * 使用反射机制设置对象的字段值
	 * @param obj 目标对象
	 * @param fieldName 字段名称
	 * @param value 字段值
	 * @throws Exception 反射异常
	 */
	private static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
		// 获取字段
		Field field = getField(obj.getClass(), fieldName);
		if (field != null) {
			// 设置字段可访问
			field.setAccessible(true);
			// 设置字段值
			field.set(obj, value);
		} else {
			// 如果字段不存在，尝试通过 setter 方法设置
			setValueBySetter(obj, fieldName, value);
		}
	}
	
	/**
	 * 获取类及其父类的字段
	 * @param clazz 类
	 * @param fieldName 字段名称
	 * @return 字段对象
	 */
	private static Field getField(Class<?> clazz, String fieldName) {
		Field field = null;
		while (clazz != null && field == null) {
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		return field;
	}
	
	/**
	 * 通过 setter 方法设置值
	 * @param obj 目标对象
	 * @param fieldName 字段名称
	 * @param value 字段值
	 * @throws Exception 反射异常
	 */
	private static void setValueBySetter(Object obj, String fieldName, Object value) throws Exception {
		// 生成 setter 方法名
		String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		// 获取 setter 方法
		Method setter = obj.getClass().getMethod(setterName, value.getClass());
		// 调用 setter 方法
		setter.invoke(obj, value);
	}
	
}
