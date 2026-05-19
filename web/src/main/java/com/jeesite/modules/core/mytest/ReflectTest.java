package com.jeesite.modules.core.mytest;

import com.jeesite.modules.core.entity.CoreSpb;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射工具类测试
 * @author pdyong
 * @version 2026-04-15
 */
public class ReflectTest {

    public static void main(String[] args) {
        try {
            // ==================== 测试方式1：直接字段名映射 ====================
            System.out.println("===== 测试方式1：直接字段名映射 =====");
            Map<String, Object> productMap1 = new HashMap<>();
            productMap1.put("spbSpbh", "SPB001");      // 商品编号（驼峰命名）
            productMap1.put("spbSpmc", "笔记本电脑");    // 商品名称
            productMap1.put("spbJg", "5999.99");       // 商品价格
            productMap1.put("spbSpfl", "电子产品");    // 商品分类

            // 使用反射机制将Map映射到CoreSpb实体类
            CoreSpb product1 = MapToEntityReflectUtil.mapToEntityPdy(productMap1, CoreSpb.class);
            
            System.out.println("商品编号: " + product1.getSpbSpbh());
            System.out.println("商品名称: " + product1.getSpbSpmc());
            System.out.println("商品价格: " + product1.getSpbJg());
            System.out.println("商品分类: " + product1.getSpbSpfl());

            // ==================== 测试方式2：支持下划线格式 ====================
            System.out.println("\n===== 测试方式2：支持下划线格式 =====");
            Map<String, Object> productMap2 = new HashMap<>();
            productMap2.put("spb_spbh", "SPB002");      // 商品编号（下划线格式）
            productMap2.put("spb_spmc", "智能手机");     // 商品名称
            productMap2.put("spb_jg", "3999.00");       // 商品价格
            productMap2.put("spb_spfl", "电子产品");     // 商品分类

            CoreSpb product2 = MapToEntityReflectUtil.mapToEntityWithFieldNameMapping(productMap2, CoreSpb.class);
            
            System.out.println("商品编号: " + product2.getSpbSpbh());
            System.out.println("商品名称: " + product2.getSpbSpmc());
            System.out.println("商品价格: " + product2.getSpbJg());
            System.out.println("商品分类: " + product2.getSpbSpfl());

            // ==================== 测试方式3：支持中文字段名 ====================
            System.out.println("\n===== 测试方式3：支持中文字段名 =====");
            Map<String, Object> productMap3 = new HashMap<>();
            productMap3.put("商品编号", "SPB003");
            productMap3.put("商品名称", "平板电脑");
            productMap3.put("商品价格", "2999.50");
            productMap3.put("商品分类", "电子产品");

            CoreSpb product3 = MapToEntityReflectUtil.mapToEntityWithFieldNameMapping(productMap3, CoreSpb.class);
            
            System.out.println("商品编号: " + product3.getSpbSpbh());
            System.out.println("商品名称: " + product3.getSpbSpmc());
            System.out.println("商品价格: " + product3.getSpbJg());
            System.out.println("商品分类: " + product3.getSpbSpfl());

            // ==================== 测试方式4：混合格式 ====================
            System.out.println("\n===== 测试方式4：混合格式 =====");
            Map<String, Object> productMap4 = new HashMap<>();
            productMap4.put("spbSpbh", "SPB004");       // 驼峰
            productMap4.put("spb_spmc", "智能手表");     // 下划线
            productMap4.put("商品价格", "1599.00");     // 中文
            productMap4.put("spbSpfl", "数码产品");     // 驼峰

            CoreSpb product4 = MapToEntityReflectUtil.mapToEntityWithFieldNameMapping(productMap4, CoreSpb.class);
            
            System.out.println("商品编号: " + product4.getSpbSpbh());
            System.out.println("商品名称: " + product4.getSpbSpmc());
            System.out.println("商品价格: " + product4.getSpbJg());
            System.out.println("商品分类: " + product4.getSpbSpfl());

            // ==================== 测试类型转换 ====================
            System.out.println("\n===== 测试类型转换 =====");
            Map<String, Object> productMap5 = new HashMap<>();
            productMap5.put("spbSpbh", 1001);            // Integer类型会自动转换为String
            productMap5.put("spbSpmc", "蓝牙耳机");
            productMap5.put("spbJg", 299.99);           // Double类型会自动转换为String
            productMap5.put("spbSpfl", "配件");

            CoreSpb product5 = MapToEntityReflectUtil.mapToEntityWithFieldNameMapping(productMap5, CoreSpb.class);
            
            System.out.println("商品编号: " + product5.getSpbSpbh() + " (类型: " + product5.getSpbSpbh().getClass().getSimpleName() + ")");
            System.out.println("商品名称: " + product5.getSpbSpmc());
            System.out.println("商品价格: " + product5.getSpbJg() + " (类型: " + product5.getSpbJg().getClass().getSimpleName() + ")");
            System.out.println("商品分类: " + product5.getSpbSpfl());

            System.out.println("\n===== 测试完成 =====");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
