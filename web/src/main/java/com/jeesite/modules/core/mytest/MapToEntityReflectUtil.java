package com.jeesite.modules.core.mytest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用Java反射机制将Map数据映射到实体类
 * @author pdyong
 * @version 2026-04-15
 */
public class MapToEntityReflectUtil {

    public static <T> T mapToEntityPdy(Map<String, Object> paramMap, Class<T> entityClass) throws Exception {
        T entity = entityClass.getDeclaredConstructor().newInstance();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (paramMap.containsKey(fieldName)) {
                Object value = paramMap.get(fieldName);
                field.setAccessible(true);
                field.set(entity, value);
            }
        }
        return entity;
    }

    /**
     * 将HashMap中的商品名称和价格映射到CoreSpb实体类
     * @param paramMap 包含商品信息的HashMap，key为字段名，value为字段值
     * @return 映射后的CoreSpb对象
     * @throws Exception 反射异常
     */
    public static <T> T mapToEntity(Map<String, Object> paramMap, Class<T> entityClass) throws Exception {
        // 1. 使用反射创建实体对象实例
        T entity = entityClass.getDeclaredConstructor().newInstance();
        
        // 2. 获取实体类中的所有字段
        Field[] fields = entityClass.getDeclaredFields();
        
        // 3. 遍历所有字段，通过反射设置值
        for (Field field : fields) {
            String fieldName = field.getName();
            // 跳过serialVersionUID等特殊字段
            if ("serialVersionUID".equals(fieldName)) {
                continue;
            }
            // 检查Map中是否存在对应的key
            if (paramMap.containsKey(fieldName)) {
                Object value = paramMap.get(fieldName);
                // 设置字段可访问（即使是private字段）
                field.setAccessible(true);
                // 使用反射设置字段值
                field.set(entity, convertValue(value, field.getType()));
            }
        }
        
        return entity;
    }

    /**
     * 专用方法：将HashMap中的商品名称和价格映射到CoreSpb实体类
     * 支持中文字段名自动映射
     * @param paramMap 包含商品信息的HashMap，key可以是中文或英文字段名
     * @return 映射后的CoreSpb对象
     * @throws Exception 反射异常
     */
    public static <T> T mapToEntityWithFieldNameMapping(Map<String, Object> paramMap, Class<T> entityClass) throws Exception {
        // 1. 使用反射创建实体对象实例
        T entity = entityClass.getDeclaredConstructor().newInstance();
        
        // 2. 获取实体类中的所有字段
        Field[] fields = entityClass.getDeclaredFields();
        
        // 3. 创建字段映射关系（支持中英文字段名）
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            fieldMap.put(fieldName, field);
            fieldMap.put(fieldName.toLowerCase(), field);
        }
        
        // 4. 遍历Map中的所有key，找到对应字段并设置值
        for (String key : paramMap.keySet()) {
            Object value = paramMap.get(key);
            
            // 支持多种key格式：spbSpbh、spbSpmc、spb_jg、商品编号、商品名称、商品价格等
            Field targetField = findTargetField(fieldMap, key);
            
            if (targetField != null) {
                targetField.setAccessible(true);
                targetField.set(entity, convertValue(value, targetField.getType()));
            }
        }
        
        return entity;
    }

    /**
     * 查找目标字段（支持中英文字段名映射）
     */
    private static Field findTargetField(Map<String, Field> fieldMap, String key) {
        // 直接匹配
        if (fieldMap.containsKey(key)) {
            return fieldMap.get(key);
        }
        
        // 小写匹配
        if (fieldMap.containsKey(key.toLowerCase())) {
            return fieldMap.get(key.toLowerCase());
        }
        
        // 下划线转驼峰匹配
        String camelCaseKey = underscoreToCamelCase(key);
        if (fieldMap.containsKey(camelCaseKey)) {
            return fieldMap.get(camelCaseKey);
        }
        
        // 特殊中文映射
        if (containsChinese(key)) {
            return findFieldByChineseName(fieldMap, key);
        }
        
        return null;
    }

    /**
     * 根据中文字段名查找对应的英文字段
     */
    private static Field findFieldByChineseName(Map<String, Field> fieldMap, String chineseKey) {
        // 商品名称 -> spbSpmc
        if (chineseKey.contains("商品名称") || "spm".equalsIgnoreCase(chineseKey)) {
            return fieldMap.get("spbSpmc");
        }
        // 商品价格 -> spbJg
        if (chineseKey.contains("商品价格") || "jg".equalsIgnoreCase(chineseKey) || "spjg".equalsIgnoreCase(chineseKey)) {
            return fieldMap.get("spbJg");
        }
        // 商品编号 -> spbSpbh
        if (chineseKey.contains("商品编号") || "bh".equalsIgnoreCase(chineseKey) || "spbh".equalsIgnoreCase(chineseKey)) {
            return fieldMap.get("spbSpbh");
        }
        // 商品分类 -> spbSpfl
        if (chineseKey.contains("商品分类") || "fl".equalsIgnoreCase(chineseKey) || "spfl".equalsIgnoreCase(chineseKey)) {
            return fieldMap.get("spbSpfl");
        }
        return null;
    }

    /**
     * 下划线转驼峰：spb_spmc -> spbSpmc
     */
    private static String underscoreToCamelCase(String input) {
        if (input == null || !input.contains("_")) {
            return input;
        }
        
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        
        for (char c : input.toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        
        return result.toString();
    }

    /**
     * 判断字符串是否包含中文
     */
    private static boolean containsChinese(String str) {
        if (str == null) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符是否为中文
     */
    private static boolean isChinese(char c) {
        return (c >= 0x4E00 && c <= 0x9FA5);
    }

    /**
     * 值类型转换
     */
    private static Object convertValue(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        
        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }
        
        // 转换为String
        if (targetType == String.class) {
            return value.toString();
        }
        
        // 其他类型转换
        try {
            String valueStr = value.toString();
            if (targetType == Integer.class || targetType == int.class) {
                return Integer.parseInt(valueStr);
            } else if (targetType == Long.class || targetType == long.class) {
                return Long.parseLong(valueStr);
            } else if (targetType == Double.class || targetType == double.class) {
                return Double.parseDouble(valueStr);
            } else if (targetType == Float.class || targetType == float.class) {
                return Float.parseFloat(valueStr);
            } else if (targetType == Boolean.class || targetType == boolean.class) {
                return Boolean.parseBoolean(valueStr);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("无法将值 '" + value + "' 转换为类型 " + targetType.getName());
        }
        
        return value;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        try {
            // 模拟商品数据（使用反射机制映射）
            Map<String, Object> productMap = new HashMap<>();
            productMap.put("spbSpbh", "SPB001");      // 商品编号
            productMap.put("spbSpmc", "笔记本电脑");    // 商品名称
            productMap.put("spbJg", "5999.99");       // 商品价格
            productMap.put("spb_spfl", "电子产品");    // 商品分类（下划线格式）

            System.out.println("===== 方式1：直接字段名映射 =====");
            // 需要先导入CoreSpb类
            // CoreSpb product = mapToEntity(productMap, CoreSpb.class);
            // System.out.println("商品编号: " + product.getSpbSpbh());
            // System.out.println("商品名称: " + product.getSpbSpmc());
            // System.out.println("商品价格: " + product.getSpbJg());

            System.out.println("\n===== 方式2：支持中文字段名映射 =====");
            Map<String, Object> productMap2 = new HashMap<>();
            productMap2.put("商品编号", "SPB002");
            productMap2.put("商品名称", "智能手机");
            productMap2.put("商品价格", "3999.00");
            productMap2.put("spb_spfl", "电子产品");

            // CoreSpb product2 = mapToEntityWithFieldNameMapping(productMap2, CoreSpb.class);
            // System.out.println("商品编号: " + product2.getSpbSpbh());
            // System.out.println("商品名称: " + product2.getSpbSpmc());
            // System.out.println("商品价格: " + product2.getSpbJg());
            
            System.out.println("\n工具类创建成功！使用方式：");
            System.out.println("1. 直接字段名：MapToEntityReflectUtil.mapToEntity(map, CoreSpb.class)");
            System.out.println("2. 支持中文：MapToEntityReflectUtil.mapToEntityWithFieldNameMapping(map, CoreSpb.class)");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
